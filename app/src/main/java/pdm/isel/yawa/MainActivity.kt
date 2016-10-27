package pdm.isel.yawa

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.cache.Cache
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.model.CurrentWeatherInfo
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*


val URI_FACTORY = RequestUriFactory()
val DTO_MAPPER = DtoToDomainMapper()
val JSON_MAPPER = JsonToDtoMapper()

val cache: Cache = Cache(100)

var language = Locale.getDefault().getDisplayLanguage()
var location = "Lisbon" //TODO: GET DEVICE location

var currentWeather: CurrentWeatherInfo? = null

class MainActivity : AppCompatActivity() {

    //INFORMATION IN TEXTVIEWS
    var temp: TextView? = null
    var cityName: TextView? = null
    var country: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null

    val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,
            RequestUriFactory().getNowWeather(location, language), null,
            object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject?) {
                    Log.d("RESPONSE ", "URL " + URI_FACTORY.getNowWeather(location, language)
                    )
                    if (response != null) {

                        currentWeather = DTO_MAPPER.mapWeatherInfoDto(
                                JSON_MAPPER.mapWeatherInfoJson(response.toString()))


                        if (currentWeather != null) {
                            Log.d("RESPONSE ", currentWeather?.name + " " + currentWeather?.temp + " " + URI_FACTORY.getIcon(currentWeather!!.icon))
                            cache.push(currentWeather!!, "current")

                            setViews()

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
                        Log.d("ERROR: ", error.toString())
                    }
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "MAIN_onCreate")

        temp = findViewById(R.id.main_temp) as TextView?
        cityName = findViewById(R.id.main_city) as TextView?
        country = findViewById(R.id.main_country) as TextView?
        description = findViewById(R.id.main_description) as TextView?

        image = findViewById(R.id.main_view) as ImageView?

    }
//    else {
//            if (savedInstanceState != null) {
//                Log.d("RESPONSE", "LOAD FROM BUNDLE")
//                val value = savedInstanceState.getInt("temp")
//                temp?.setText("" + value + "º")
//                country?.setText(savedInstanceState.getString("country"))
//                cityName?.setText(savedInstanceState.getString("cityName"))
//                description?.setText(savedInstanceState.getString("description"))
///               image = findViewById(R.id.main_view) as ImageView
//                image!!.setImageResource(R.drawable.slb)
//            }
//        }
//     }

    override fun onStart(){
        super.onStart()

        currentWeather = cache.pop(location + language + "current") as CurrentWeatherInfo?

        if (currentWeather != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setViews()
        } else {
            Log.d("RESPONSE", "LOAD FROM REQUEST")
            makeRequest()
        }
    }

    private fun setViews() {
        cityName?.setText(currentWeather?.name)
        temp?.setText(currentWeather?.temp.toString())
        country?.setText(currentWeather?.country)
        description?.setText(currentWeather?.description)

        application.requestQueue.add(getIconView(URI_FACTORY.getIcon(currentWeather!!.icon)))//TODO: IMAGE IS NOT BEING SAVED
    }

    private fun makeRequest() {
        application.requestQueue.add(request)
    }

    fun refresh(view: View) {
        makeRequest()//TODO: REFRESH BUTTON
    }
//SAVING INFORMATION
// #############################################################################################

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null) {
            outState.putInt("temp", currentWeather!!.temp.toInt())
            outState.putString("cityName", currentWeather!!.name)
            outState.putString("country", currentWeather!!.country)
            outState.putString("description", currentWeather!!.description)
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
        val intent = Intent(this, DetailedCurrentWeatherInfoActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this, "Detalhes do dia", Toast.LENGTH_LONG).show()
    }


    fun onNext(view: View) {

        Log.d("YAWA_TAG", "onNext")
        val intent = Intent(this, ForecastActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this, "Next days", Toast.LENGTH_LONG).show()
    }


    fun credits(view: View) {
        val intent = Intent(this, CreditsActivity::class.java)//TODO: OPEN CREDITS ACTIVITY BUTTON
        startActivity(intent)
    }
}
