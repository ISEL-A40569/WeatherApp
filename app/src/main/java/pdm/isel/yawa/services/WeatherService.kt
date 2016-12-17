package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.*
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.provider.WeatherCrudFunctions
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

                        var id = crud.verifyIfCityExists(contentResolver,
                                null,
                                "name = "+current!!.cityName +" and country = "+current!!.cityCountry + " language = "+current!!.language,
                                null,null)

                        if (id < 0){
                            id = crud.insertNewCity(contentResolver, current!!)
                        }

                        var curr = crud.queryCurrent(contentResolver, null, "currentid = "+id, null, null, id)
                        if (curr != null){
                            crud.deleteCurrentWeatherInfo(contentResolver, "currentid = "+id, null)
                        }
                        crud.insertCurrentWeatherInfo(contentResolver, current!!.currentInfo, id)
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

                    var id = crud.verifyIfCityExists(contentResolver,
                            null,
                            "name = "+forecast!!.cityName +" and country = "+forecast!!.cityCountry + " language = "+forecast!!.language,
                            null,null)

                    if (id < 0){
                        id = crud.insertNewCity(contentResolver, forecast!!)
                    }

                    var curr = crud.queryForecast(contentResolver, null, "forecastid = "+id, null, null, id)
                    if (curr != null){
                        crud.deleteFutureWeatherInfo(contentResolver, "forecastid = "+id, null)
                    }

                    for (list in forecast!!.list)
                        crud.insertFutureWeatherInfo(contentResolver, list, id)

                } else {
                    //TODO: throw some null response exception

                    Toast.makeText(applicationContext, "What a Terrible Failure", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}