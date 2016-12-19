package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.*
import pdm.isel.yawa.provider.WeatherCrudFunctions
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.requests.IconRequest
import pdm.isel.yawa.uri.RequestUriFactory

/**
 *
 *
 */
class WeatherService() : IntentService("WeatherService") {

    var current: Current? = null
    var forecast: Forecast? = null
    val NUMBER_OF_FORECAST_DAYS = 16

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent start")

//        if(application.isConnected && !isBatteryLow){
//            makeCurrentRequest()
//            makeForecastRequest()
//        }
        val type = intent!!.getStringExtra("type")

        if(type.equals("current") || type.equals("both"))
        makeCurrentRequest(intent!!.getStringExtra("location"),
                intent!!.getStringExtra("language"),
                intent!!.getParcelableExtra("receiver")
        )

        if(type.equals("forecast") || type.equals("both"))
        makeForecastRequest(intent!!.getStringExtra("location"),
                intent!!.getStringExtra("language"),
                intent!!.getParcelableExtra("receiver")
        )

        //testResultReceiver(intent!!.getParcelableExtra("receiver"))
        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest(loc:String, lang: String, receiver: ResultReceiver) {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(loc!!, lang),
                getCurrentResponseCallback(receiver)))
    }


    private fun makeForecastRequest(loc:String, lang: String, receiver: ResultReceiver) {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getFutureWeather(loc, lang, NUMBER_OF_FORECAST_DAYS),
                getForecastResponseCallback(receiver)))
    }

    private fun getCurrentResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                if (response != null) {

                    current = DTO_MAPPER.mapCurrentDto(
                            JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                    //TODO: insertInDB(current)
                    val icon = current!!.currentInfo._icon
                    var image = iconCache.pop(icon)
                    if(image !=null){
                        current!!.currentInfo.image = image
                    }else{
                        makeIconRequest(icon, getIconCallbackForCurrent(receiver))
                    }

                }
//                    if (current != null) {
//                        Log.d("OnService", "Updating " + current!!.name + " current info")
//
//                        var id = crud.verifyIfCityExists(contentResolver,
//                                null,
//                                "name = '" + location + "' and language = '" + language + "'",
//                                null,null)
//
//                        if (id < 0){
//                            id = crud.insertNewCity(contentResolver, current!!)
//                        }
//
//                        var curr = crud.queryCurrent(contentResolver, null, "currentid = "+id, null, null, id)
//                        if (curr != null){
//                            crud.deleteCurrentWeatherInfo(contentResolver, "currentid = "+id, null)
//                        }
//                        crud.insertCurrentWeatherInfo(contentResolver, current!!.currentInfo, id)
//                        //TODO: INSERT CURRENT HERE
//                    }
//                } else {
//                    //TODO: throw some null response exception
//                }
            }
        }
    }


    private fun getForecastResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {

                if (response != null) {
                    forecast = DTO_MAPPER.mapForecastDto(
                            JSON_MAPPER.mapForecastJson(response.toString()))

                    Log.d("OnService", "Updating " + forecast!!.name + " forecast info")
                    //TODO: INSERT FORECAST HERE

                    sendInfo(receiver, "forecast", forecast!!)
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

    private fun fillForecastIcons(){

    }

    private fun makeIconRequest(uri:String, callback: Callback<Bitmap>) {
        application.requestQueue.add(IconRequest(
                URI_FACTORY.getIcon(uri),
                callback))
    }

    private fun getIconCallbackForCurrent(receiver: ResultReceiver): Callback<Bitmap> {
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                Log.d("RESPONSE", "GOT ICON")
                current!!.currentInfo.image = icon
                iconCache.push(current!!.currentInfo._icon, icon)
                sendInfo(receiver, "current", current!!)
            }
        }
    }

    private fun getIconCallbackForForecast(receiver: ResultReceiver): Callback<Bitmap> {
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                Log.d("RESPONSE", "GOT ICON")

            }
        }
    }

    private fun sendInfo(receiver: ResultReceiver, key: String, info: CityInfo) {
        val bundle = Bundle()
        bundle.putParcelable(key, info)
        receiver.send(200, bundle)
    }

}