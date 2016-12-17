package pdm.isel.yawa

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.requests.IconRequest
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.requests.VolleyIconCallback
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*

var language = Locale.getDefault().displayLanguage
var location: String? = "Lisboa"

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

        cityName = findViewById(R.id.main_city) as TextView
        country = findViewById(R.id.main_country) as TextView
        temp = findViewById(R.id.main_temp) as TextView
        description = findViewById(R.id.main_description) as TextView
        image = findViewById(R.id.main_view) as ImageView
    }

    override fun onStart() {
        super.onStart()

        if(location == null){
            location = application.prefs.getString("city","")
        }

        Log.d("RESPONSE", "ON START, location = " + location)


        var cityId = crud.verifyIfCityExists(contentResolver,null
                ,"name = "+ location + " and language = "+ language
                , null, null)
        if ( cityId > 0)
            currentWeather = crud.queryCurrent(contentResolver, null, null,null,null, cityId )

        if (currentWeather != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            setViews()
        } else {

            if(application.isConnected && !isBatteryLow){
                Log.d("OnStart", "Network Available")
                Log.d("RESPONSE", "LOAD FROM REQUEST")
                makeRequest()
            }else{
                Log.d("OnStart", "Network Not Available")
                Toast.makeText(this, "OffLine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViews() {
        cityName!!.setText(currentWeather!!.name)
        temp!!.setText(currentWeather!!.currentInfo.temp)
        country!!.setText(currentWeather!!.country)
        description!!.setText(currentWeather!!.currentInfo.description)

        if(currentWeather!!.currentInfo.image != null){
            Log.d("RESPONSE", "SETTING IMAGE")
            image!!.setImageBitmap(currentWeather!!.currentInfo.image)
        }
    }

    private fun makeRequest() {
        application.requestQueue.add(DataRequest(
                RequestUriFactory().getNowWeather(location!!, language),
                getResponseListener()))
    }

    private fun getResponseListener(): Response.Listener<JSONObject> {
        return object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject?) {

                currentWeather = DTO_MAPPER.mapCurrentDto(
                        JSON_MAPPER.mapWeatherInfoJson(response.toString()))

                if (currentWeather != null) {
                    Log.d("RESPONSE ", currentWeather!!.name + " " + currentWeather!!.currentInfo.temp)

                    var icon = iconCache.pop(currentWeather!!.currentInfo._icon)

                    if (icon != null) {
                        currentWeather!!.currentInfo.image = icon
                    } else {
                        application.requestQueue.add(IconRequest(
                                URI_FACTORY.getIcon(currentWeather!!.currentInfo.icon),
                                object : VolleyIconCallback {
                                    override fun  onSuccess(icon: Bitmap) {
                                        Log.d("RESPONSE", "GOT ICON")
                                        iconCache.push(currentWeather!!.currentInfo._icon, icon)
                                        currentWeather!!.currentInfo.image = icon
                                        image?.setImageBitmap(currentWeather!!.currentInfo.image)
                                    }

                                }))
                    }
                    setViews()
                }
            }
        }
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
