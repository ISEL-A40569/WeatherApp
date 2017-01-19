package pdm.isel.yawa

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import pdm.isel.yawa.model.Current
import pdm.isel.yawa.services.IconService
import pdm.isel.yawa.services.WeatherService
import java.util.*

class MainActivity : AppCompatActivity() {

    //Textviews
    var temp: TextView? = null
    var cityName: TextView? = null
    var country: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null

    //location and language strings
    var location: String? = null
    var language: String? = null

    //wifiOnly
    var wifiOnly: Boolean = false

    //Current instance
    var currentWeather: Current? = null

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
        language = Locale.getDefault().displayLanguage
        application.editor.putString("language", language)

        location = application.prefs.getString("city", "Lisbon")
        wifiOnly = application.prefs.getBoolean("wifiOnly", false)

        if (isServiceAccessAllowed()) {
            Log.d("OnStart", "Network Available")
            Log.d("RESPONSE", "LOAD FROM REQUEST")
            startServiceForDataRequest()

        } else {

            Log.d("RESPONSE", "LOAD CURRENT FROM DATABASE")

            currentWeather = application.DbApi.getCurrent(location!!, language!!, "PT")
            getIcon(currentWeather!!.currentInfo.icon)
        }
    }


    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)
        val receiver = object : ResultReceiver(Handler()) {
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
        val icon = application.iconCache.pop(key)
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
        val iconReceiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                stopService(intent)
                super.onReceiveResult(resultCode, resultData)
                val image: Bitmap = resultData!!.getParcelable("icon")
                currentWeather!!.currentInfo.image = image
                setViews()
            }
        }
        intent.putExtra("iconReceiver", iconReceiver)
        intent.putExtra("icon", currentWeather!!.currentInfo.icon)
        startService(intent)
    }

    private fun setViews() {
        cityName!!.text = currentWeather!!.name
        temp!!.text = currentWeather!!.currentInfo.temp
        country!!.text = currentWeather!!.country
        description!!.text = currentWeather!!.currentInfo.description

        if (currentWeather!!.currentInfo.image != null) {
            Log.d("RESPONSE", "SETTING IMAGE")
            image!!.setImageBitmap(currentWeather!!.currentInfo.image)
        }
    }

    private fun isServiceAccessAllowed(): Boolean {
        return isConnectionAvailable() && !isPowerLow()
    }

    private fun isConnectionAvailable(): Boolean {
        val isConnected = application.connectivityManager.activeNetworkInfo != null &&
                application.connectivityManager.activeNetworkInfo.isConnected

        if (!isConnected) {
            return false
        } else {
            if (wifiOnly)
                return application.connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI

            return true
        }
    }

    private fun isPowerLow(): Boolean {
        if (isCharging()) return false

        return getBatteryLevel() < application.prefs.getInt("minimumBatteryLevel", 25)
    }

    fun getBatteryLevel(): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = applicationContext.registerReceiver(null, iFilter)

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
        val intent = Intent(this, DetailedCurrentWeatherInfoActivity::class.java)
        intent.putExtra("name", currentWeather!!.name)
        intent.putExtra("country", currentWeather!!.country)
        intent.putExtra("lat", currentWeather!!.lat)
        intent.putExtra("long", currentWeather!!.lon)
        intent.putExtra("date", currentWeather!!.currentInfo.date)
        intent.putExtra("desc", currentWeather!!.currentInfo.description)
        intent.putExtra("hum", currentWeather!!.currentInfo.humidity)
        intent.putExtra("press", currentWeather!!.currentInfo.pressure)
        intent.putExtra("temp", currentWeather!!.currentInfo.temp)
        intent.putExtra("sunr", currentWeather!!.currentInfo.sunrise)
        intent.putExtra("suns", currentWeather!!.currentInfo.sunset)
        intent.putExtra("ws", currentWeather!!.currentInfo.windSpeed)
        intent.putExtra("image", currentWeather!!.currentInfo.image)
        intent.putExtra("icon", currentWeather!!.currentInfo.icon)

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

    fun onMenu(view: View) {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }


    private fun setBackGroundImage(str: String) {

        when (str) {

            "01d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i01d, null)
            }
            "01n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i01n, null)
            }
            "02d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i02d, null)
            }
            "02n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i02n, null)
            }
            "03d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i03d, null)
            }
            "03n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i03n, null)
            }
            "04d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i04d, null)
            }
            "04n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i04n, null)
            }
            "05d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i05d, null)
            }
            "05n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i05n, null)
            }
            "06d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i06d, null)
            }
            "06n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i06n, null)
            }
            "07d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i07d, null)
            }
            "07n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i07n, null)
            }
            "08d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i08d, null)            }
            "08n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i08n, null)
            }
            "09d" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i09d, null)
            }
            "09n" -> {
                findViewById(android.R.id.content)
                        .background = ResourcesCompat.getDrawable(resources, R.drawable.i09n, null)            }
        }
    }
}
