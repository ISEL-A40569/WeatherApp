package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.*
import pdm.isel.yawa.provider.WeatherCrudFunctions
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.requests.IconRequest
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.concurrent.CountDownLatch


/**
 *
 *
 */
class WeatherService() : IntentService("WeatherService") {

    var current: Current? = null
    var forecast: Forecast? = null

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent start")

        val type = intent!!.getStringExtra("type")
        val lang = intent!!.getStringExtra("language")
        val city = intent!!.getStringExtra("location")
        val receiver: ResultReceiver = intent!!.getParcelableExtra("receiver")

        Log.d("OnService", city)
        Log.d("OnService", lang)


        if (type.equals("current") || type.equals("both"))
            makeCurrentRequest(city, lang, receiver)

        if (type.equals("forecast") || type.equals("both"))
            makeForecastRequest(city, lang, receiver)

        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest(loc: String, lang: String, receiver: ResultReceiver) {
        Log.d("OnService", "makeCurrentRequest start")

        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(loc!!, lang),
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
                    insertInDB(current)
            }
        }

        Log.d("OnService", "getCurrentResponseCallback end")

    }

    private fun insertInDB(current: Current?) {
        if (current != null) {
            Log.d("OnService", "Updating " + current!!.name + " current info")

            var id = crud.verifyIfCityExists(contentResolver,
                    null,
                    "name = '" + location + "' and language = '" + language + "'",
                    null, null)

            if (id < 0) {
                id = crud.insertNewCity(contentResolver, current!!)
            }

            var curr = crud.queryCurrent(contentResolver, null, "currentid = " + id, null, null, id)
            if (curr != null) {
                crud.deleteCurrentWeatherInfo(contentResolver, "currentid = " + id, null)
            }
            crud.insertCurrentWeatherInfo(contentResolver, current!!.currentInfo, id)
        }
    }


    private fun sendInfo(receiver: ResultReceiver, key: String, info: CityInfo) {
        Log.d("OnService", "sendInfo start")

        val bundle = Bundle()
        bundle.putParcelable(key, info)
        receiver.send(200, bundle)
        Log.d("OnService", "sendInfo end")

    }


    private fun getForecastResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {

                if (response != null) {
                    forecast = DTO_MAPPER.mapForecastDto(
                            JSON_MAPPER.mapForecastJson(response.toString()))
                    forecast!!.language = language
                    sendInfo(receiver, "forecast", forecast!!)
                    Log.d("OnService", "Updating " + forecast!!.name + " forecast info")
                    //TODO: insertInDB(forecast)

                }
//                    var id = crud.verifyIfCityExists(contentResolver,
//                            null,
//                            "name = '" + location + "' and language = '" + language + "'",
//                            null,null)
//
//                    if (id < 0){
//                        id = crud.insertNewCity(contentResolver, forecast!!)
//                    }
//
//                    var curr = crud.queryForecast(contentResolver, null, "forecastid = "+id, null, null, id)
//                    if (curr != null){
//                        crud.deleteFutureWeatherInfo(contentResolver, "forecastid = "+id, null)
//                    }
//
//                    for (list in forecast!!.list)
//                        crud.insertFutureWeatherInfo(contentResolver, list, id)
//
//                } else {
//                    //TODO: throw some null response exception
//
//                    Toast.makeText(applicationContext, "What a Terrible Failure", Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }


}