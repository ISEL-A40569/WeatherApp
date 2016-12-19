package pdm.isel.yawa

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.model.MyParcelable
import pdm.isel.yawa.requests.IconRequest
import pdm.isel.yawa.requests.DataRequest
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.services.WeatherService
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

    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)

        var receiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)
                currentWeather = resultData!!.getParcelable("current")
                setViews()
                Log.d("TestResultReceiver", "Got weather for " + currentWeather!!.name)
            }
        }
        intent.putExtra("type", "current")
        intent.putExtra("receiver", receiver)
        intent.putExtra("location", location)
        intent.putExtra("language", language)

        startService(intent)
    }

    override fun onStart() {
        super.onStart()

        if (location == null) {
            location = application.prefs.getString("city", "")
        }

        Log.d("RESPONSE", "ON START, location = " + location)

//        var cityId = crud.verifyIfCityExists(contentResolver, null
//                , "name = '" + location + "' and language = '" + language + "'"
//                , null, null)
//        if (cityId > 0)
//            currentWeather = crud.queryCurrent(contentResolver, null, null, null, null, cityId)

        if (currentWeather != null) {
            Log.d("RESPONSE", "LOAD FROM CACHE")
            application.editor.putString("temp", currentWeather!!.currentInfo.temp)
            application.editor.putString("description", currentWeather!!.currentInfo._description)

            setViews()
        } else {

            if (application.isConnected && !isBatteryLow) {
                Log.d("OnStart", "Network Available")
                Log.d("RESPONSE", "LOAD FROM REQUEST")
                startServiceForDataRequest()

            } else {
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

        if (currentWeather!!.currentInfo.image != null) {
            Log.d("RESPONSE", "SETTING IMAGE")
            image!!.setImageBitmap(currentWeather!!.currentInfo.image)
        }
    }


        fun onRefresh(view: View) {
            startServiceForDataRequest()
            //setViews()
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
            if (currentWeather != null) {
                val intent = Intent(this, DetailedCurrentWeatherInfoActivity::class.java)
                startActivity(intent)
            } else {
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

        fun onDbTest(view: View) {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }

    }
