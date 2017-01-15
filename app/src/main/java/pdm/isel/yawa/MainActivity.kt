package pdm.isel.yawa

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.services.IconService
import pdm.isel.yawa.services.WeatherService
import java.util.*

var language = Locale.getDefault().displayLanguage
var location: String? = null

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

        application.editor.putString("language", language)

        //findViewById(android.R.id.content).setBackgroundDrawable(resources.getDrawable(R.drawable.i02n))

    }


    override fun onStart() {
        super.onStart()

        location = application.prefs.getString("city", "Lisbon")

        if (isServiceAccessAllowed()) {
            Log.d("OnStart", "Network Available")
            Log.d("RESPONSE", "LOAD FROM REQUEST")
            startServiceForDataRequest()

        } else {
//            var cityId = crud.verifyIfCityExists(contentResolver, null
//                    , "name = '$location' and language = '$language'"
//                    , null, null)
//            if (cityId > 0)
//                currentWeather = crud.queryCurrent(contentResolver, null, null, null, null, cityId)
//            Log.d("DB_DEBUG", currentWeather!!.name)
//            Log.d("DB_DEBUG", currentWeather!!.language)
//            Log.d("DB_DEBUG", currentWeather!!.country)

            Log.d("RESPONSE", "LOAD CURRENT FROM DATABASE")

            currentWeather = application.DbApi.getCurrent(location!!, language, "PT")
            getIcon(currentWeather!!.currentInfo.icon)
        }
        //setBackGroundImage(currentWeather.currentInfo.icon)
    }


    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)
        var receiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)
                stopService(intent)
                currentWeather = resultData!!.getParcelable("current")
                application.editor.putString("city", location)
                application.editor.putString("temp", currentWeather!!.currentInfo.temp)
                application.editor.putString("description", currentWeather!!.currentInfo._description)
                getIcon(currentWeather!!.currentInfo._icon)
            }
        }
        intent.putExtra("type", "current")
        intent.putExtra("receiver", receiver)
        intent.putExtra("location", location)
        intent.putExtra("language", language)
        startService(intent)
    }

    private fun getIcon(key: String) {
        var icon = application.iconCache.pop(key)
        if (icon != null) {
            currentWeather!!.currentInfo.image = icon
            setViews()
        } else {
            startServiceForIconRequest()
        }
        setBackGroundImage(currentWeather!!.currentInfo.icon)
    }

    private fun startServiceForIconRequest() {
        val intent = Intent(this, IconService::class.java)
        var iconReceiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                stopService(intent)
                super.onReceiveResult(resultCode, resultData)
                var image: Bitmap = resultData!!.getParcelable("icon")
                currentWeather!!.currentInfo.image = image
                setViews()
            }
        }
        intent.putExtra("iconReceiver", iconReceiver)
        intent.putExtra("icon", currentWeather!!.currentInfo.icon)
        startService(intent)
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

    private fun isServiceAccessAllowed(): Boolean {
        return isConnectionAvailable() && !isPowerLow()
    }

    private fun isConnectionAvailable(): Boolean {
        var isConnected = application.connectivityManager!!.activeNetworkInfo != null &&
                application.connectivityManager!!.activeNetworkInfo.isConnected

        if (!isConnected) {
            return false
        } else {
            if (wifiOnly)
                return application.connectivityManager!!.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI

            return true
        }
    }

    private fun isPowerLow(): Boolean {
        return !isCharging() || getBatteryLevel() < minimumBatteryLevel
    }

    fun getBatteryLevel(): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = applicationContext.registerReceiver(null, ifilter)

        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return ((level / scale.toFloat()) * 100).toInt()
    }

    fun isCharging(): Boolean {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = applicationContext.registerReceiver(null, ifilter)

        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        Log.d("isCharging", (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL).toString())

        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    fun onRefresh(view: View) {
        startServiceForDataRequest()
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

    fun onMenu(view: View) {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }


    private fun setBackGroundImage(str:String){

        when(str){

            "01d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i01d))
            }
            "01n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i01n))
            }
            "02d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i02d))
            }
            "02n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i02n))
            }
            "03d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i03d))
            }
            "03n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i03n))
            }
            "04d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i04d))
            }
            "04n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i04n))
            }
            "05d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i05d))
            }
            "05n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i05n))
            }
            "06d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i06d))
            }
            "06n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i06n))
            }
            "07d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i07d))
            }
            "07n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i07n))
            }
            "08d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i08d))
            }
            "08n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i08n))
            }
            "09d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i09d))
            }
            "09n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i09n))
            }
        }
    }
}
