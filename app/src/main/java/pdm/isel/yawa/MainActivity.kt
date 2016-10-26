package pdm.isel.yawa

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.model.WeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*


var LOCATION = "Lisboa" //TODO: GET DEVICE LOCATION
val LANGUAGE = Locale.getDefault().getDisplayLanguage()
val uriFactory = RequestUriFactory()
val dtoMapper = DtoToDomainMapper()
val jsonMapper = JsonToDtoMapper()
var changes = false     // Make changes ?
var weather: WeatherInfo? = null

class MainActivity : AppCompatActivity() {

    //INFORMATION IN TEXTVIEWS
    var temp: TextView? = null
    var city: TextView? = null
    var country: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null

    val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            RequestUriFactory().getNowWeather(LOCATION, LANGUAGE), null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {
                    Log.d("RESPONSE ", "URL " + uriFactory.getNowWeather(LOCATION, LANGUAGE)
                    )
                    if (response != null) {
                        var weatherInfo: WeatherInfo = dtoMapper.mapWeatherInfoDto(
                                jsonMapper.mapWeatherInfoJson(response.toString()))

                        weather = weatherInfo

                        if (weatherInfo != null) {
                            Log.d("RESPONSE ", weatherInfo.name + " " + weatherInfo.temp + " " + uriFactory.getIcon(weatherInfo.icon))


                            temp?.setText("" + (weatherInfo.temp.toInt()) + "º")

                            city?.setText("" + weatherInfo.name)

                            country?.setText("" + weatherInfo.country)

                            description?.setText(weatherInfo.description)

                            application.requestQueue.add(getIconView(uriFactory.getIcon(weatherInfo.icon)))

                            changes=false

                        }
                    } else {
                        //TODO
                    }
                }
            }, object : Response.ErrorListener {
        override fun onErrorResponse(error: VolleyError) {
            Log.d("ERROR: ", error.toString())
        }
    })

    private fun getIconView(url: String): ImageRequest {
        return ImageRequest(url,
                object : Response.Listener<Bitmap> {
                    override fun onResponse(bitmap: Bitmap) {
                        image?.setImageBitmap(bitmap)
                    }
                }, 0, 0, null,
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        //TODO
                    }
                })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "MAIN_onCreate")

        temp = findViewById(R.id.main_temp) as TextView?
        city = findViewById(R.id.main_city) as TextView?
        country = findViewById(R.id.main_country) as TextView?
        description = findViewById(R.id.main_description) as TextView?

        image = findViewById(R.id.main_view) as ImageView?

        if(weather==null){
            application.requestQueue.add(request) //TODO: onde é que encaixa com a AsyncTask!!???
        }else{
            if (savedInstanceState != null) {
                val value = savedInstanceState.getInt("temp")
                temp?.setText("" + value + "º")
                country?.setText(savedInstanceState.getString("country"))
                city?.setText(savedInstanceState.getString("city"))
                description?.setText(savedInstanceState.getString("description"))
//            image = findViewById(R.id.main_view) as ImageView
//            image!!.setImageResource(R.drawable.slb)

            }
        }


//        if(changes) {
//            temp = findViewById(R.id.main_temp) as TextView?
//            temp?.setText("" + (weather?.temp?.toInt()) + "º")
//            city = findViewById(R.id.main_city) as TextView?
//            city?.setText("" + weather?.name)
//            country = findViewById(R.id.main_country) as TextView?
//            country?.setText("" + weather?.country)
//
//            //image = findViewById(R.id.main_view) as ImageView
//            //image!!.setImageResource(R.drawable.slb)
//            changes = false
//        }

    }


    //SAVING INFORMATION
    // #############################################################################################

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null) {
            outState.putInt("temp", weather!!.temp.toInt())
            outState.putString("city", weather!!.name)
            outState.putString("country", weather!!.country)
            outState.putString("description", weather!!.description)
        }
    }

    //INTENTS
    // #############################################################################################

    fun onCity(view: View) {

        Log.d("YAWA_TAG", "onCity")
        // Toast.makeText(this, "GO TO LIST", Toast.LENGTH_LONG).show()

        val intent = Intent(this, CityListActivity::class.java)
        startActivity(intent)
    }


    fun onDetails(view: View) {

        Log.d("YAWA_TAG", "onDetails")
        val intent = Intent(this, DetailedWeatherInfoActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this, "Detalhes do dia", Toast.LENGTH_LONG).show()
    }


    fun onNext(view: View) {

        Log.d("YAWA_TAG", "onNext")
        val intent = Intent(this, ForecastActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this, "Next days", Toast.LENGTH_LONG).show()
    }
}
