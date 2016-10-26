package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.adapter.BasicWeatherInfoArrayAdapter
import pdm.isel.yawa.model.BasicWeatherInfo
import pdm.isel.yawa.model.WeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory

class ForecastActivity : ListActivity()  {

    var list: Array<BasicWeatherInfo>? = null

    val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            RequestUriFactory().getFutureWeather(LOCATION, LANGUAGE, 7), null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {

                    if (response != null) {
                        var forecast = dtoMapper.mapForecastDto(
                                jsonMapper.mapForecastJson(response.toString()))

                        list = forecast.list

                        listView.setAdapter(BasicWeatherInfoArrayAdapter(applicationContext, forecast.list))

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

        application.requestQueue.add(request)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if(list != null) {
            var bwis = list
            if(bwis != null){
                BASIC_WEATHER_INFO = bwis[position]

                val intent = Intent(this, BasicWeatherInfoActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
