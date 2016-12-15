package pdm.isel.yawa

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.adapter.FutureWeatherInfoArrayAdapter
import pdm.isel.yawa.model.Forecast
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

        //forecast = crud.queryForecast(contentResolver, "", arrayOf(location, language))

        if (forecast != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setView()
        } else {

            var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if(connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected && !isBatteryLow){
                Log.d("OnStart", "Network Available")
                makeRequest()//TODO: should this keep being done here!?
            }else{
                Log.d("OnStart", "Network Not Available")
                Toast.makeText(this, "OffLine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getFutureWeather(location!!, language, NUMBER_OF_FORECAST_DAYS), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {

                            forecast = DTO_MAPPER.mapForecastDto(
                                    JSON_MAPPER.mapForecastJson(response.toString()))

                            for(i in forecast?.list!!.indices){
                                var futureWI = forecast?.list!![i]

//                                var icon: Bitmap? = iconCache.pop(futureWI._icon)//TODO: solve IconCache parallel access problem
//
//                                if(icon != null){
//                                    futureWI.image = icon
//                                }else{
                                    application.requestQueue.add(ImageRequest(URI_FACTORY.getIcon(futureWI.icon),
                                            object : Response.Listener<Bitmap> {
                                                override fun onResponse(bitmap: Bitmap) {
                                                    Log.d("RESPONSE", "GOT ICON " + i.toString())

                                                    futureWI.image = bitmap
                                                    //iconCache.push(futureWI._icon, bitmap)//TODO: solve IconCache parallel access problem

                                                    if(i == forecast?.list!!.size -1){
                                                        setView()
                                                    }
                                                }
                                            }, 0, 0,
                                            ImageView.ScaleType.CENTER_INSIDE,
                                            null,
                                            object : Response.ErrorListener {
                                                override fun onErrorResponse(error: VolleyError) {
                                                    Log.d("ERROR: ", error.toString())
                                                }
                                            }))
//                                }
                            }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
            }
        }))
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

