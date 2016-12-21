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


val NUMBER_OF_FORECAST_DAYS = 8

/**
 *
 *
 */
class WeatherService() : IntentService("WeatherService") {

    var current: Current? = null
    var forecast: Forecast? = null

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent start")

//        if(application.isConnected && !isBatteryLow){
//            makeCurrentRequest()
//            makeForecastRequest()
//        }
        val type = intent!!.getStringExtra("type")
        val lang = intent!!.getStringExtra("language")
        val city = intent!!.getStringExtra("location")
        val receiver: ResultReceiver = intent!!.getParcelableExtra("receiver")

        Log.d("OnService", city)
        Log.d("OnService", lang)


        if(type.equals("current") || type.equals("both"))
        makeCurrentRequest(city, lang, receiver)

        if(type.equals("forecast") || type.equals("both"))
        makeForecastRequest(city, lang, receiver)


        //testResultReceiver(intent!!.getParcelableExtra("receiver"))
        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest(loc:String, lang: String, receiver: ResultReceiver) {
        Log.d("OnService", "makeCurrentRequest start")

        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(loc!!, lang),
                getCurrentResponseCallback(receiver)))
        Log.d("OnService", "makeCurrentRequest end")

    }


    private fun makeForecastRequest(loc:String, lang: String, receiver: ResultReceiver) {
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
                if (response != null) {

                    current = DTO_MAPPER.mapCurrentDto(
                            JSON_MAPPER.mapWeatherInfoJson(response.toString()))
                    Log.d("OnService", "JUST GOT CURRENT FOR: " + current!!.name)

                    //TODO: insertInDB(current)
                    val icon = current!!.currentInfo._icon
                    var image = iconCache.pop(icon)
                    if(image !=null){
                        current!!.currentInfo.image = image
                        sendInfo(receiver, "current", current!!)
                    }else{
                        //makeIconRequest(icon, getIconCallbackForCurrent(receiver))
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
        Log.d("OnService", "getCurrentResponseCallback end")

    }



    private fun getIconCallbackForCurrent(receiver: ResultReceiver): Callback<Bitmap> {
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                Log.d("OnService", "GOT ICON")
                current!!.currentInfo.image = icon
                iconCache.push(current!!.currentInfo._icon, icon)
                sendInfo(receiver, "current", current!!)
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



//    private fun makeIconRequest(uri:String, callback: Callback<Bitmap>) {
//        Log.d("OnService", "makeIconRequest start")
//
//        application.requestQueue.add(IconRequest(
//                URI_FACTORY.getIcon(uri),
//                callback, latch))
//        Log.d("OnService", "makeIconRequest end")
//
//    }



    private fun getForecastResponseCallback(receiver: ResultReceiver): Callback<JSONObject> {
        return object : Callback<JSONObject> {
            override fun onSuccess(response: JSONObject) {

                if (response != null) {
                    forecast = DTO_MAPPER.mapForecastDto(
                            JSON_MAPPER.mapForecastJson(response.toString()))

                    Log.d("OnService", "Updating " + forecast!!.name + " forecast info")
                    //TODO: INSERT FORECAST HERE

                    fillForecastIcons(forecast!!, receiver)
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


    private fun fillForecastIcons(forecast: Forecast, receiver: ResultReceiver){
        var count = 0
        for(i in forecast.list.indices) {
            var futureWI = forecast.list[count]
            var icon: Bitmap? = iconCache.pop(futureWI._icon)

            if(icon != null){
                futureWI.image = icon
                ++count

                if (count == NUMBER_OF_FORECAST_DAYS) {
                    sendInfo(receiver, "forecast", forecast!!)
                }
            }else{
                //count = makeIconRequest(receiver, count, futureWI)//TODO: NOT WORKING 100%, NEEDS SOMETHING LIKE CountDownLatch OR CREATING AN ICONSERVICE USING RESULTRECEIVER

                val intent = Intent(this, IconService::class.java)

                val receiver = object : ResultReceiver(Handler()) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        Log.d("OnIconService", "receiving result")

                        forecast.list[i].image = resultData!!.getParcelable("icon")
                        Log.d("OnIconService", "just got icon for " + futureWI._date + "count is " + count.toString())
                        ++count
                        Log.d("OnIconService", "count is " + count.toString())


                        if (count == NUMBER_OF_FORECAST_DAYS) {
                            Log.d("OnIconService", "sending result")

                            sendInfo(receiver, "forecast", forecast!!)
                            Log.d("OnIconService", "result sent")

                        }
                        stopService(intent)
                    }
                }

                intent.putExtra("receiver", receiver)
                intent.putExtra("icon", futureWI._icon)

                startService(intent)
            }
            Log.d("OnIconService", count.toString())
        }
    }

//    private fun makeIconRequest(receiver: ResultReceiver, count: Int, futureWI: FutureWeatherInfo): Int {
//        var count1 = count
//        application.requestQueue.add(IconRequest(
//                URI_FACTORY.getIcon(futureWI.icon),
//                getIconCallbackForForecast(receiver, count, futureWI), latch
//        )
//        )
//        return count1
//    }


    private fun getIconCallbackForForecast(receiver: ResultReceiver, count: Int, futureWI: FutureWeatherInfo): Callback<Bitmap> {
        var count1 = count
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                futureWI.image = icon;
                iconCache.push(futureWI._icon, icon)
                ++count1
                Log.d("GettingIcon" + count1, "request")
                if (count1 == NUMBER_OF_FORECAST_DAYS) {
                    sendInfo(receiver, "forecast", forecast!!)
                }
            }
        }
    }

}