package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.adapter.FutureWeatherInfoArrayAdapter
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.requests.IconRequest
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.requests.VolleyIconCallback
import pdm.isel.yawa.uri.RequestUriFactory

val NUMBER_OF_FORECAST_DAYS = 16

class ForecastActivity : ListActivity() {

    var forecast: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
    }

    override fun onStart() {
        super.onStart()

        /*
        var cityId = crud.verifyIfCityExists(contentResolver,null
                ,"name = '"+ location + "' and language = '"+ language+"'"
                , null, null)

        forecast = crud.queryForecast(contentResolver, null, null, null, null, cityId)
        //TODO: after still have to get icons
        */
        if (forecast != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setView()
        } else {

            if (application.isConnected && !isBatteryLow) {
                Log.d("OnStart", "Network Available")
                makeRequest()
            } else {
                Log.d("OnStart", "Network Not Available")
                Toast.makeText(this, "OffLine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeRequest() {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getFutureWeather(location!!, language, NUMBER_OF_FORECAST_DAYS),
                getForecastResponseListener()))
    }

    private fun getForecastResponseListener(): Response.Listener<JSONObject> {
        return object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {

                forecast = DTO_MAPPER.mapForecastDto(
                        JSON_MAPPER.mapForecastJson(response.toString()))
                var count = 0
                for (i in forecast!!.list.indices) {
                    var futureWI = forecast!!.list[i]

                    var icon: Bitmap? = iconCache.pop(futureWI._icon)

                    if (icon != null) {
                        futureWI.image = icon
                        ++count
                        Log.d("GettingIcon" + count, "cache")

                        if (count == NUMBER_OF_FORECAST_DAYS) {
                            setView()
                        }
                    } else {
                        application.requestQueue.add(IconRequest(
                                URI_FACTORY.getIcon(futureWI.icon),
                                object : VolleyIconCallback {
                                    override fun onSuccess(icon: Bitmap) {
                                        futureWI.image = icon;
                                        iconCache.push(futureWI._icon, icon)
                                        ++count
                                        Log.d("GettingIcon" + count, "request")
                                        if (count == NUMBER_OF_FORECAST_DAYS) {
                                            setView()
                                        }
                                    }

                                }
                        )
                        )
                    }

                }
            }
        }
    }


    private fun setView() {
        listView.setAdapter(FutureWeatherInfoArrayAdapter(applicationContext, forecast?.list!!))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Log.d("RESPONSE", "CLICKING " + position)

        futureWeatherInfo = forecast!!.list[position]
        Log.d("RESPONSE", "HERE!")

        val intent = Intent(this, BasicWeatherInfoActivity::class.java)
        startActivity(intent)
    }
}

