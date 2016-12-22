package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import pdm.isel.yawa.*
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.services.WeatherService

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            //TODO: just for now, will only work if app is on and for current only
            val intent = Intent(context, WeatherService::class.java)

            var receiver = object : ResultReceiver(Handler()) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    super.onReceiveResult(resultCode, resultData)
                }
            }
            intent.putExtra("receiver", receiver)
            intent.putExtra("type", "both")
            intent.putExtra("location", location)
            intent.putExtra("language", language)
            context!!.startService(intent)

//            if (isConnected && !isBatteryLow)TODO: wheres the best place to do this!?

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
}