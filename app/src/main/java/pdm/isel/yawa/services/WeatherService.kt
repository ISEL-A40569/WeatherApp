package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import org.json.JSONObject
import pdm.isel.yawa.DbApi
import pdm.isel.yawa.NUMBER_OF_FORECAST_DAYS
import pdm.isel.yawa.URI_FACTORY
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.requestQueue
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.requests.DataRequest


/**
 * Class used to execute a data request.
 */
class WeatherService() : IntentService("WeatherService") {
    val DTO_MAPPER = DtoToDomainMapper()
    val JSON_MAPPER = JsonToDtoMapper()

    var current: Current? = null
    var forecast: Forecast? = null

    var language: String? = null
    var location: String? = null

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent start")
        val type = intent!!.getStringExtra("type")
        val receiver: ResultReceiver = intent.getParcelableExtra("receiver")

        language = intent.getStringExtra("language")
        location = intent.getStringExtra("location")

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
        makeDataRequest(DataRequest(
                application.URI_FACTORY.getNowWeather(loc, lang),
                getCurrentResponseCallback(receiver)))
        Log.d("OnService", "makeCurrentRequest end")
    }

    private fun makeForecastRequest(loc: String, lang: String, receiver: ResultReceiver) {
        Log.d("OnService", "makeForecastRequest start")
        makeDataRequest(DataRequest(
                application.URI_FACTORY.getFutureWeather(loc, lang, NUMBER_OF_FORECAST_DAYS),
                getForecastResponseCallback(receiver)))
        Log.d("OnService", "makeForecastRequest end")
    }

    private fun makeDataRequest(request: DataRequest){
        application.requestQueue.add(request)
    }

    private fun getCurrentResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        Log.d("OnService", "getCurrentResponseCallback start")
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                current = DTO_MAPPER.mapCurrentDto(
                            JSON_MAPPER.mapWeatherInfoJson(response.toString()))
                    current!!.language = language
                    Log.d("OnService", "JUST GOT CURRENT FOR: " + current!!.name)

                    sendInfo(receiver, "current",current!!)
                    application.DbApi.insert(current!!)
            }
        }
    }

    private fun getForecastResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        Log.d("OnService", "getForecastResponseCallback start")
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

    private fun sendInfo(receiver: ResultReceiver, key: String, info: CityInfo) {
        Log.d("OnService", "sendInfo start")

        val bundle = Bundle()
        bundle.putParcelable(key, info)
        receiver.send(200, bundle)

        Log.d("OnService", "sendInfo end")
    }

}