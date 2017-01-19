package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.uri.RequestUriFactory


/**
 * Class used to execute a data request.
 */
class WeatherService() : IntentService("WeatherService") {

    var current: Current? = null
    var forecast: Forecast? = null

    var language: String? = null
    var location: String? = null
    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent start")

        val type = intent!!.getStringExtra("type")
        language = intent.getStringExtra("language")
        location = intent.getStringExtra("location")
        val receiver: ResultReceiver = intent.getParcelableExtra("receiver")

        Log.d("OnService", location)
        Log.d("OnService", language)


        if (type == "current" || type == "both")
            makeCurrentRequest(location!!, language!!, receiver)

        if (type == "forecast" || type == "both")
            makeForecastRequest(location!!, language!!, receiver)

        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest(loc: String, lang: String, receiver: ResultReceiver) {
        Log.d("OnService", "makeCurrentRequest start")

        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(loc, lang),
                getCurrentResponseCallback(receiver)))
        Log.d("OnService", "makeCurrentRequest end")

    }


    private fun makeForecastRequest(loc: String, lang: String, receiver: ResultReceiver) {
        Log.d("OnService", "makeForecastRequest start")

        application.requestQueue.add(DataRequest(
                RequestUriFactory().getFutureWeather(loc, lang, NUMBER_OF_FORECAST_DAYS),
                getForecastResponseCallback(receiver)))
        Log.d("OnService", "makeForecastRequest end")

    }

    private fun getCurrentResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        Log.d("OnService", "getCurrentResponseCallback start")
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {

                current = DTO_MAPPER.mapCurrentDto(
                            JSON_MAPPER.mapWeatherInfoJson(response.toString()))
                    current!!.language = language
                    Log.d("OnService", "JUST GOT CURRENT FOR: " + current!!.name)
                    sendInfo(receiver, "current", current!!)
                    application.DbApi.insert(current!!)
            }
        }

//        Log.d("OnService", "getCurrentResponseCallback end") unreachable

    }

    private fun sendInfo(receiver: ResultReceiver, key: String, info: CityInfo) {
        Log.d("OnService", key + " sendInfo start")

        val bundle = Bundle()
        bundle.putParcelable(key, info)
        receiver.send(200, bundle)
        Log.d("OnService", key + "sendInfo end")

    }


    private fun getForecastResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                    forecast = DTO_MAPPER.mapForecastDto(
                            JSON_MAPPER.mapForecastJson(response.toString()))
                    forecast!!.language = language
                    
                    Log.d("OnService", "JUST GOT FORECAST FOR: " + forecast!!.name)

                    sendInfo(receiver, "forecast", forecast!!)

                    application.DbApi.insert(forecast!!)
            }
        }
    }


}