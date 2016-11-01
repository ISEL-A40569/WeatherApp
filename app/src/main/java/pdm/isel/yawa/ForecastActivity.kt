package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.adapter.BasicWeatherInfoArrayAdapter
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.model.FutureWeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory

class ForecastActivity : ListActivity() {
    val NUMBER_OF_FORECAST_DAYS = 10

    var forecast: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
    }

    override fun onStart() {
        super.onStart()

        forecast = cache.pop(location + language + "forecast") as Forecast?

        if (forecast != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setView()
        } else {
            Log.d("RESPONSE", "LOAD FROM REQUEST")
            makeRequest()

        }

    }

    private fun makeRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getFutureWeather(location, language, NUMBER_OF_FORECAST_DAYS), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {

                        if (response != null) {
                            forecast = DTO_MAPPER.mapForecastDto(
                                    JSON_MAPPER.mapForecastJson(response.toString()))

                            cache.push(forecast!!, "forecast")


                            setView()

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

    private fun setView() {
        listView.setAdapter(BasicWeatherInfoArrayAdapter(applicationContext, forecast?.list!!))
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

