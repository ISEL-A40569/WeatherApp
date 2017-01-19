package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.ListView
import pdm.isel.yawa.adapter.FutureWeatherInfoArrayAdapter
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.model.FutureWeatherInfo
import pdm.isel.yawa.services.IconService
import pdm.isel.yawa.services.WeatherService


class ForecastActivity : ListActivity() {

    var forecast: Forecast? = null
    var language: String? = null
    var location: String? = null
    var wifiOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
    }

    override fun onStart() {
        super.onStart()

        location = application.prefs.getString("city", "Lisbon")
        language = application.prefs.getString("language", "português")
        wifiOnly = application.prefs.getBoolean("wifiOnly", false)

        if (isServiceAccessAllowed()) {
            Log.d("RESPONSE", "LOAD FORECAST FROM REQUEST")
            Log.d("RESPONSE", "FORECAST FOR $location in $language")

            startServiceForDataRequest()
        } else {
            Log.d("RESPONSE", "LOAD FORECAST FROM DATABASE")
            forecast = application.DbApi.getForecast(location!!, language!!, "PT")//TODO: USE COUNTRY OR NOT?
            Log.d("RESPONSE", "FWI COUNT: " + forecast!!.list.size)

            startServiceForIconsRequest(0, forecast!!.list)
        }
    }

    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)

        val receiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)
                forecast = resultData!!.getParcelable("forecast")

                startServiceForIconsRequest(0, forecast!!.list)

                stopService(intent)
            }
        }
        intent.putExtra("type", "forecast")
        intent.putExtra("receiver", receiver)
        intent.putExtra("location", application.prefs.getString("city", "Lisbon"))
        intent.putExtra("language", application.prefs.getString("language", "português"))

        startService(intent)
    }

    private fun startServiceForIconsRequest(count: Int, list: Array<FutureWeatherInfo>) {
        if (count == NUMBER_OF_FORECAST_DAYS) {
            setView()
        } else {
            val futureWI = list[count]
            Log.d("OnCache", futureWI._icon + "\n")
            val icon: Bitmap? = application.iconCache.pop(futureWI.icon)
            if (icon != null) {
                futureWI.image = icon
                startServiceForIconsRequest(count + 1, list)
            } else {
                val intent = Intent(this, IconService::class.java)

                val iconReceiver = object : ResultReceiver(Handler()) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        super.onReceiveResult(resultCode, resultData)
                        stopService(intent)
                        val image: Bitmap = resultData!!.getParcelable("icon")
                        futureWI.image = image
                        startServiceForIconsRequest(count + 1, list)
                    }
                }
                intent.putExtra("iconReceiver", iconReceiver)
                intent.putExtra("icon", futureWI._icon)

                startService(intent)
            }
        }
    }

    private fun setView() {
        listView.adapter = FutureWeatherInfoArrayAdapter(applicationContext, forecast!!.list)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Log.d("RESPONSE", "CLICKING " + position)

        val futureWeatherInfo = forecast!!.list[position]

        val intent = Intent(this, BasicWeatherInfoActivity::class.java)

        intent.putExtra("date", futureWeatherInfo.date)
        intent.putExtra("press", futureWeatherInfo.pressure)
        intent.putExtra("hum", futureWeatherInfo.humidity)
        intent.putExtra("desc", futureWeatherInfo.description)
        intent.putExtra("icon", futureWeatherInfo.icon)
        intent.putExtra("tmin", futureWeatherInfo.tempMin)
        intent.putExtra("tmax", futureWeatherInfo.tempMax)
        intent.putExtra("image", futureWeatherInfo.image)

        startActivity(intent)
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
        if(isCharging()) return false

        return getBatteryLevel() < application.prefs.getInt("minimumBatteryLevel", 25)
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

        Log.d("isCharging", (status == BatteryManager.BATTERY_STATUS_CHARGING).toString())

        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }
}

