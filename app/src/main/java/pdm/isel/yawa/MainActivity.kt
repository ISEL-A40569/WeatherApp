package pdm.isel.yawa

import android.app.Notification
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.*
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*
import android.content.Intent
import android.content.SharedPreferences


val URI_FACTORY = RequestUriFactory()
val DTO_MAPPER = DtoToDomainMapper()
val JSON_MAPPER = JsonToDtoMapper()

var language = Locale.getDefault().displayLanguage
var location: String? = null

var currentWeather: Current? = null

var isBatteryLow = false

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
        //TODO: how to do this in WeatherApp ?!?
        updateInterval = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).getLong("updateInterval",15)
        hourValue = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).getInt("hour",8)
        minutesValue = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).getInt("minutes",0)

        Log.d("SettingNotifications", hourValue.toString())
        Log.d("SettingNotifications", minutesValue.toString())


        if(location == null)
        location = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).getString("city","")

        Log.d("RESPONSE", "ON START, location = " + location)

        //currentWeather = crud.queryCurrent(contentResolver, "", arrayOf(location, language))

        if (currentWeather != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setViews()
        } else {
            var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if(connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected && !isBatteryLow){
                Log.d("OnStart", "Network Available")
                Log.d("RESPONSE", "LOAD FROM REQUEST")
                makeRequest()//TODO: should this keep being done here!?
            }else{
                Log.d("OnStart", "Network Not Available")
                Toast.makeText(this, "OffLine", Toast.LENGTH_SHORT).show()
            }
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
                RequestUriFactory().getNowWeather(location!!, language), null,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject?) {

                            currentWeather = DTO_MAPPER.mapCurrentDto(
                                    JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                            if (currentWeather != null) {
                                Log.d("RESPONSE ", currentWeather?.name + " " + currentWeather?.currentInfo?.temp)

                                var icon = iconCache.pop(currentWeather!!.currentInfo._icon)

                                if(icon != null){
                                    currentWeather!!.currentInfo.image = icon
                                }else{
                                    application.requestQueue.add(getIconView(URI_FACTORY.getIcon(currentWeather!!.currentInfo.icon)))
                                }
                                setViews()
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
                        iconCache.push(currentWeather!!.currentInfo!!._icon, bitmap)
                        currentWeather?.currentInfo?.image = bitmap
                        image?.setImageBitmap(currentWeather?.currentInfo?.image)
                    }
                }, 0, 0,
                ImageView.ScaleType.CENTER_INSIDE,
                null,
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
        if(currentWeather != null){
            val intent = Intent(this, DetailedCurrentWeatherInfoActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Select a City First", Toast.LENGTH_SHORT).show()
        }
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
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

}
