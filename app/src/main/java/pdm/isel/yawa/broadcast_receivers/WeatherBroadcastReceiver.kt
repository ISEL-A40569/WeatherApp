package pdm.isel.yawa.broadcast_receivers

import android.content.*
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import pdm.isel.yawa.services.WeatherService

/**
 * This class is used to start WeatherService repeatedly who updates
 * Current and Forecast weather info for the location and language
 * stored in shared preferences.
 */
class WeatherBroadcastReceiver : BroadcastReceiver() {
    var connectivityManager: ConnectivityManager? = null
    var cnxt: Context? = null
    var prefs: SharedPreferences? = null
    var wifiOnly: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        cnxt = context
        prefs = cnxt!!.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiOnly = prefs!!.getBoolean("wifiOnly", false)

        if (isServiceAccessAllowed())
            if (Intent.ACTION_BOOT_COMPLETED != intent?.action) {
                val intent = Intent(context, WeatherService::class.java)

                val receiver = object : ResultReceiver(Handler()) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        super.onReceiveResult(resultCode, resultData)

                        context.stopService(intent)
                    }
                }
                intent.putExtra("receiver", receiver)
                intent.putExtra("type", "both")
                intent.putExtra("location", prefs!!.getString("city", "Lisboa"))
                intent.putExtra("language", prefs!!.getString("language", "português"))

                Log.d("WEATHER RECEIVER", "STARTING SERVICE")
                context.startService(intent)
            }
    }


    private fun isServiceAccessAllowed(): Boolean {
        return isConnectionAvailable() && !isPowerLow()
    }

    private fun isConnectionAvailable(): Boolean {
        val isConnected = connectivityManager!!.activeNetworkInfo != null &&
                connectivityManager!!.activeNetworkInfo.isConnected

        if (!isConnected) {
            return false
        } else {
            if (wifiOnly)
                return connectivityManager!!.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI

            return true
        }
    }

    private fun isPowerLow(): Boolean {
        if (isCharging()) return false

        return getBatteryLevel() < prefs!!.getInt("minimumBatteryLevel", 25)
    }

    fun getBatteryLevel(): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = cnxt!!.registerReceiver(null, ifilter)

        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return ((level / scale.toFloat()) * 100).toInt()
    }

    fun isCharging(): Boolean {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = cnxt!!.registerReceiver(null, ifilter)

        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        Log.d("isCharging", (status == BatteryManager.BATTERY_STATUS_CHARGING).toString())

        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

}