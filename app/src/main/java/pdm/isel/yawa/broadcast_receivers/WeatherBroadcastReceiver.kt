package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import pdm.isel.yawa.*
import pdm.isel.yawa.services.WeatherService

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherBroadcastReceiver : WakefulBroadcastReceiver() {
    var connectivityManager: ConnectivityManager? = null
    var cnxt: Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        cnxt = context
        connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isServiceAccessAllowed())
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            //TODO: just for now, will only work if app is on and for current only
            val intent = Intent(context, WeatherService::class.java)

            var receiver = object : ResultReceiver(Handler()) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    super.onReceiveResult(resultCode, resultData)

                    context!!.stopService(intent)
                }
            }
            intent.putExtra("receiver", receiver)
            intent.putExtra("type", "both")
            intent.putExtra("location", location)
            intent.putExtra("language", language)
            context!!.startService(intent)


//            TODO: Something like:
//            foreach(CityInfo ci in list){}
//            val intent = Intent(context, WeatherService::class.java)
//            intent.putExtra("type", "both")
//            intent.putExtra("location",ci.name )
//            intent.putExtra("language", ci.language)
//            intent.putExtra("receiver", receiver)   TODO: have to pass a receiver every time that wont be used!? maybe to close the service
//            context!!.startService(intent)
        }
    }


    private fun isServiceAccessAllowed(): Boolean {
        return isConnectionAvailable() && !isPowerLow()
    }

    private fun isConnectionAvailable(): Boolean {
        var isConnected = connectivityManager!!.activeNetworkInfo != null &&
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
        return !isCharging() || getBatteryLevel() < minimumBatteryLevel//TODO: USE SHARED PREFERENCES ONLY!? EVERYWHERE POSSIBLE??
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