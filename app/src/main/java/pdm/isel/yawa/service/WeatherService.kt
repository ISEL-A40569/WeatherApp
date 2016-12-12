package pdm.isel.yawa.service

import android.app.IntentService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.uri.RequestUriFactory

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherService() : IntentService("WeatherService") {

    var current: Current? = null
    var forecast: Forecast? = null
    val NUMBER_OF_FORECAST_DAYS = 16

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent")
        makeCurrentRequest()
        makeForecastRequest()
    }

    private fun makeCurrentRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getNowWeather(location, language), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
//                        Log.d("RESPONSE ", "URL " + URI_FACTORY.getNowWeather(location, language))

                        if (response != null) {

                            current = DTO_MAPPER.mapCurrentDto(
                                    JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                            if (currentWeather != null) {

                                //TODO: INSERT CURRENT HERE
                            }
                        } else {
                            //TODO
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
            }
        }))
    }

    private fun makeForecastRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getFutureWeather(location, language, NUMBER_OF_FORECAST_DAYS), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {

                        if (response != null) {
                            forecast = DTO_MAPPER.mapForecastDto(
                                    JSON_MAPPER.mapForecastJson(response.toString()))

                            for(i in forecast?.list!!.indices){
                                var futureWI = forecast?.list!![i]
                                application.requestQueue.add(ImageRequest(URI_FACTORY.getIcon(futureWI.icon),
                                        object : Response.Listener<Bitmap> {
                                            override fun onResponse(bitmap: Bitmap) {
                                                Log.d("RESPONSE", "GOT ICON")
                                                futureWI.image = bitmap

                                                if(i == forecast?.list!!.size -1){
                                                    cache.push(forecast!!, "forecast")
                                                }
                                            }
                                        }, 0, 0, null,
                                        object : Response.ErrorListener {
                                            override fun onErrorResponse(error: VolleyError) {
                                                Log.d("ERROR: ", error.toString())
                                            }
                                        }))
                            }

                            //TODO: INSERT FORECAST HERE

                        } else {
                            //TODO
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
            }
        }))
    }
}