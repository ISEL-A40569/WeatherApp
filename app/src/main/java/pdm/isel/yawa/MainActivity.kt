package pdm.isel.yawa

import android.content.Intent
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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.model.WeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*


var LOCATION = "Porto" //TODO: GET DEVICE LOCATION
val LANGUAGE = Locale.getDefault().getDisplayLanguage()
val uriFactory = RequestUriFactory()
val dtoMapper = DtoToDomainMapper()
val jsonMapper = JsonToDtoMapper()
var changes = false     // Make changes ?



class MainActivity : AppCompatActivity() {

    //INFORMATION IN TEXTVIEWS
    var temp: TextView? = null
    var city: TextView? = null
    var country: TextView? = null
    var image: ImageView? = null

    var weather: WeatherInfo? = null


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

                            temp = (findViewById(R.id.main_temp) as TextView?)!!
                            temp?.setText("" + (weatherInfo.temp.toInt()) + "º")

                            city = findViewById(R.id.main_city) as TextView?
                            city?.setText("" + weatherInfo.name)

                            country = findViewById(R.id.main_country) as TextView?
                            country?.setText("" + weatherInfo.country)

                            //image = findViewById(R.id.main_view) as ImageView
                            //image!!.setImageResource(R.drawable.slb)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "MAIN_onCreate")

        application.requestQueue.add(request) //TODO: onde é que encaixa com a AsyncTask!!???

        if(changes) {
            temp = findViewById(R.id.main_temp) as TextView?
            temp?.setText("" + (weather?.temp?.toInt()) + "º")
            city = findViewById(R.id.main_city) as TextView?
            city?.setText("" + weather?.name)
            country = findViewById(R.id.main_country) as TextView?
            country?.setText("" + weather?.country)

            //image = findViewById(R.id.main_view) as ImageView
            //image!!.setImageResource(R.drawable.slb)
            changes = false
        }

        //Toast.makeText(this, "URL = " + uriFactory.getIcon(weather?.icon), Toast.LENGTH_LONG).show()
    }




    override fun onStart(){
        super.onStart()
        Log.d("YAWA_TAG", "MAIN_onStart")
    }

    override fun onResume(){
        super.onResume()
        Log.d("YAWA_TAG", "MAIN_onResume")
    }

    override fun onPause(){
        super.onPause()
        Log.d("YAWA_TAG", "MAIN_onPause")
    }

    override fun onStop(){
        super.onStop()
        Log.d("YAWA_TAG", "MAIN_onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d("YAWA_TAG", "MAIN_onDestroy")
    }





    //SAVING INFORMATION
    // #############################################################################################

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null) {
            //outState.putInt("temp", weather!!.temp.toInt())
            //outState.putString("city", weather!!.name)
            //outState.putString("country", weather!!.country)
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            /*
            val value = savedInstanceState.getInt("temp")
            temp?.setText("" + value + "º")
            country?.setText(savedInstanceState.getString("country"))
            city?.setText(savedInstanceState.getString("city"))
            image = findViewById(R.id.main_view) as ImageView
            image!!.setImageResource(R.drawable.slb)
            */
        }
    }


    //INTENTS
    // #############################################################################################

    fun onCity(view: View) {

        Log.d("YAWA_TAG", "onCity")
        // Toast.makeText(this, "GO TO LIST", Toast.LENGTH_LONG).show()

        val intent = Intent(this, Country_List::class.java)
        startActivity(intent)
    }


    fun onDetails(view: View) {

        Log.d("YAWA_TAG", "onDetails")
        val intent = Intent(this, Details::class.java)
        startActivity(intent)
        Toast.makeText(this, "Detalhes do dia", Toast.LENGTH_LONG).show()
    }


    fun onNext(view: View) {

        Log.d("YAWA_TAG", "onNext")
        val intent = Intent(this, Details::class.java)
        startActivity(intent)
        Toast.makeText(this, "Next days", Toast.LENGTH_LONG).show()
    }
}
