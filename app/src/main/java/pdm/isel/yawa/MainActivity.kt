package pdm.isel.yawa

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.model.WeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*

var LOCATION = "Lisboa" //TODO: GET DEVICE LOCATION
var LANGUAGE = Locale.getDefault().getDisplayLanguage()

class MainActivity : AppCompatActivity() {

    val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            RequestUriFactory().getNowWeather(LOCATION, LANGUAGE), null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {
                    Log.d("RESPONSE ", "URL " + RequestUriFactory().getNowWeather(LOCATION, LANGUAGE)
                    )
                    if (response != null) {
                        val weatherInfo: WeatherInfo = DtoToDomainMapper().mapWeatherInfoDto(
                                JsonToDtoMapper().mapWeatherInfoJson(response.toString()))

                        Log.d("RESPONSE ", weatherInfo.name + " " + weatherInfo.temp + " " + weatherInfo.description + " " + weatherInfo.getIconUrl())

                        //TODO: SET VIEWS VALUES

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
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "onCreate")

        application.requestQueue.add(request) //TODO: onde é que encaixa com a AsyncTask!!???

        //val imageView = ImageView(this)
        //imageView.setImageResource(R.drawable.slb)


    }

    fun onCity(view: View) {

        Log.d("YAWA_TAG", "onCity")
        // Toast.makeText(this, "GO TO LIST", Toast.LENGTH_LONG).show()

        //val intent = Intent(this, Country_List::class.java)

        val intent = Intent(this, SearchCity::class.java)

        startActivity(intent)
    }
}
