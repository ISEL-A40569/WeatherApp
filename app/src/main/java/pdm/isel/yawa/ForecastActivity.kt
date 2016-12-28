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

val NUMBER_OF_FORECAST_DAYS = 16

class ForecastActivity : ListActivity() {

    var forecast: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
    }

    override fun onStart() {
        super.onStart()

        if (isServiceAccessAllowed()) {
            startServiceForDataRequest()
        } else {
//            var cityId = crud.verifyIfCityExists(contentResolver, null
//                    , "name = '" + location + "' and language = '" + language + "'"
//                    , null, null)
//
//            forecast = crud.queryForecast(contentResolver, null, null, null, null, cityId)
//
//            startServiceForIconsRequest(0, forecast!!.list)
        }
    }

    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)

        var receiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)
                forecast = resultData!!.getParcelable("forecast")

                startServiceForIconsRequest(0, forecast!!.list)

                stopService(intent)
            }
        }
        intent.putExtra("type", "forecast")
        intent.putExtra("receiver", receiver)
        intent.putExtra("location", location)
        intent.putExtra("language", language)

        startService(intent)
    }

    private fun startServiceForIconsRequest(count: Int, list: Array<FutureWeatherInfo>) {
        if (count == NUMBER_OF_FORECAST_DAYS) {
            setView()
        } else {
            val futureWI = list[count]
            Log.d("OnCache", futureWI._icon + "\n")
            var icon: Bitmap? = application.iconCache.pop(futureWI.icon)
            if (icon != null) {
                futureWI.image = icon
                startServiceForIconsRequest(count + 1, list)
            } else {
                val intent = Intent(this, IconService::class.java)

                var iconReceiver = object : ResultReceiver(Handler()) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        super.onReceiveResult(resultCode, resultData)
                        stopService(intent)
                        var image: Bitmap = resultData!!.getParcelable("icon")
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
        listView.setAdapter(FutureWeatherInfoArrayAdapter(applicationContext, forecast?.list!!))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Log.d("RESPONSE", "CLICKING " + position)

        futureWeatherInfo = forecast!!.list[position]
        Log.d("RESPONSE", "HERE!")

        val intent = Intent(this, BasicWeatherInfoActivity::class.java)
        startActivity(intent)
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

        Log.d("isCharging", (status == BatteryManager.BATTERY_STATUS_CHARGING).toString())

        return status == BatteryManager.BATTERY_STATUS_CHARGING
    }
}

