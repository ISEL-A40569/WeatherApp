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

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnService", "onHandleIntent")
        makeRequest()
        //contentResolver.insert() TODO
    }

    private fun makeRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getNowWeather(location, language), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
//                        Log.d("RESPONSE ", "URL " + URI_FACTORY.getNowWeather(location, language))

                        if (response != null) {

                            current = DTO_MAPPER.mapCurrentDto(
                                    JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                            if (currentWeather != null) {
                                var cv = ContentValues()
                                //cv.put()TODO
                                //contentResolver.insert()
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


}