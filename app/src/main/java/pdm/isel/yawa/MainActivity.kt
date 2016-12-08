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
import pdm.isel.yawa.model.*
import pdm.isel.yawa.provider.WeatherContract
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*


val URI_FACTORY = RequestUriFactory()
val DTO_MAPPER = DtoToDomainMapper()
val JSON_MAPPER = JsonToDtoMapper()

val cache: Cache = Cache(100)

var language = Locale.getDefault().getDisplayLanguage()
var location = "Lisbon" //TODO: GET DEVICE location

var currentWeather: Current? = null

class MainActivity : AppCompatActivity() {

    //INFORMATION IN TEXTVIEWS
    var temp: TextView? = null
    var cityName: TextView? = null
    var country: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "MAIN_onCreate")


        cityName = findViewById(R.id.main_city) as TextView?
        country = findViewById(R.id.main_country) as TextView?
        description = findViewById(R.id.main_description) as TextView?
        image = findViewById(R.id.main_view) as ImageView?

    }

    override fun onStart() {
        super.onStart()
        Log.d("RESPONSE", "ON START, location = " + location)
        currentWeather = cache.pop(location + language + "current") as Current?
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
        temp?.setText(currentWeather?.currentInfo?.temp)
        country?.setText(currentWeather?.country)
        description?.setText(currentWeather?.currentInfo?.description)

        if(currentWeather?.currentInfo?.image != null){
            Log.d("RESPONSE", "SETTING IMAGE")
            image?.setImageBitmap(currentWeather?.currentInfo?.image!!)
        }
    }

    private fun makeRequest() {
        application.requestQueue.add(JsonObjectRequest(Request.Method.GET,
                RequestUriFactory().getNowWeather(location, language), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {
//                        Log.d("RESPONSE ", "URL " + URI_FACTORY.getNowWeather(location, language))

                        if (response != null) {

                            currentWeather = DTO_MAPPER.mapCurrentDto(
                                    JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                            if (currentWeather != null) {
                                Log.d("RESPONSE ", currentWeather?.name + " " + currentWeather?.currentInfo?.temp)
                                application.requestQueue.add(getIconView(URI_FACTORY.getIcon(currentWeather!!.currentInfo.icon)))
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
        }))
    }

    private fun getIconView(url: String): ImageRequest {
        return ImageRequest(url,
                object : Response.Listener<Bitmap> {
                    override fun onResponse(bitmap: Bitmap) {
                        Log.d("RESPONSE", "GOT ICON")
                        currentWeather?.currentInfo?.image = bitmap
                        cache.push(currentWeather!!, "current")
                        image?.setImageBitmap(currentWeather?.currentInfo?.image)
                    }
                }, 0, 0, null,
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("ERROR: ", error.toString())
                    }
                })
    }

    fun onRefresh(view: View) {
        makeRequest()
        setViews()
    }


//INTENTS
// #############################################################################################

    fun onCity(view: View) {

        Log.d("YAWA_TAG", "onCity")

        val intent = Intent(this, CityListActivity::class.java)
        startActivity(intent)
    }


    fun onDetails(view: View) {

        Log.d("YAWA_TAG", "onDetails")
        val intent = Intent(this, DetailedCurrentWeatherInfoActivity::class.java)
        startActivity(intent)
    }


    fun onNext(view: View) {

        Log.d("YAWA_TAG", "onNext")
        val intent = Intent(this, ForecastActivity::class.java)
        startActivity(intent)
    }


    fun onCredits(view: View) {
        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }

    fun onDbTest(view: View){
        val intent = Intent(this, DbTestActivity::class.java)
        startActivity(intent)
    }


    private fun insertIntoTableCurrent(curr: Current) {
        //TODO
    }
    private fun insertIntoTableForescast(forecast: Forecast) {
        //TODO
    }
    private fun insertIntoTableCurrent(curr: CurrentWeatherInfo) {
        //TODO
    }
    private fun insertIntoTableCurrent(curr: FutureWeatherInfo) {
        //TODO
    }
}
