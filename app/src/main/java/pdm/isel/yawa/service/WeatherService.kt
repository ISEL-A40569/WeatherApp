package pdm.isel.yawa.service

import android.app.IntentService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.icons.IconCache
import pdm.isel.yawa.model.BaseWeatherInfo
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.Forecast
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

        var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //TODO: ALSO DONT DO IT IF POWER IS LOW
        if(connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected){
            makeCurrentRequest()
            makeForecastRequest()
        }
        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getNowWeather(location!!, language), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
                        if (response != null) {

                            current = DTO_MAPPER.mapCurrentDto(
                                    JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                            if (current != null) {
                                Log.d("OnService", "Updating " + current!!.name + " current info")


                                //TODO: INSERT CURRENT HERE
                            }
                        } else {
                            //TODO: throw some null response exception
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
            }
        }))
    }

    //TODO: maybe this can be used later somewhere else by everybody (activities or what?) that needs to get icons
//    private fun getIconView(url: String, wi: BaseWeatherInfo): ImageRequest {
//        return ImageRequest(url,
//                object : Response.Listener<Bitmap> {
//                    override fun onResponse(bitmap: Bitmap) {
//                        Log.d("RESPONSE", "GOT ICON")
//                        iconCache.push(current!!.currentInfo._icon, bitmap)
//                        wi.image = bitmap
//                    }
//                }, 0, 0, null,
//                object : Response.ErrorListener {
//                    override fun onErrorResponse(error: VolleyError) {
//                        Log.d("ERROR: ", error.toString())
//                    }
//                })
//    }

    private fun makeForecastRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getFutureWeather(location!!, language, NUMBER_OF_FORECAST_DAYS), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {

                        if (response != null) {
                            forecast = DTO_MAPPER.mapForecastDto(
                                    JSON_MAPPER.mapForecastJson(response.toString()))

                            Log.d("OnService", "Updating " + forecast!!.name + " forecast info")
                            //TODO: INSERT FORECAST HERE

                        } else {
                            //TODO: throw some null response exception
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
            }
        }))
    }
}