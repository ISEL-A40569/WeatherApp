package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
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

    var forecast: Forecast? = null
    var list: Array<FutureWeatherInfo>? = null

    val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            RequestUriFactory().getFutureWeather(location, language, 7), null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {

                    if (response != null) {
                        forecast = DTO_MAPPER.mapForecastDto(
                                JSON_MAPPER.mapForecastJson(response.toString()))

                        cache.push(forecast!!, "forecast")

                        list = forecast!!.list

                        setView()

                    } else {
                        //TODO
                    }
                }
            }, object : Response.ErrorListener {
        override fun onErrorResponse(error: VolleyError) {
            Log.d("ERROR: ", error.toString())
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        forecast = cache.pop(location + language + "forecast") as Forecast?

        if(forecast != null){
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setView()
        }else{
            Log.d("RESPONSE", "LOAD FROM REQUEST")
            application.requestQueue.add(request)
        }

    }

    private fun setView() {
        listView.setAdapter(BasicWeatherInfoArrayAdapter(applicationContext, forecast!!.list))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if (list != null) {
            futureWeatherInfo = list!![position]

            val intent = Intent(this, BasicWeatherInfoActivity::class.java)
            startActivity(intent)
        }
    }
}
