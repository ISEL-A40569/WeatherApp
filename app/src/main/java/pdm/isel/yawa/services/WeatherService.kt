package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.requests.DataRequest
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

        if(application.isConnected && !isBatteryLow){
            makeCurrentRequest()
            makeForecastRequest()
        }
        Log.d("OnService", "onHandleIntent end")
    }

    private fun makeCurrentRequest() {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(location!!, language),
                getCurrentResponseListener()))
    }

    private fun getCurrentResponseListener(): Response.Listener<JSONObject> {
        return object : Response.Listener<JSONObject> {
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
        }
    }

    private fun makeForecastRequest() {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getFutureWeather(location!!, language, NUMBER_OF_FORECAST_DAYS),
                getForecastResponseListener()))
    }

    private fun getForecastResponseListener(): Response.Listener<JSONObject> {
        return object : Response.Listener<JSONObject> {
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
        }
    }
}