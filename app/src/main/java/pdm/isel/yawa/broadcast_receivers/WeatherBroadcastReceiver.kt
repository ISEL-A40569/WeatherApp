package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.services.WeatherService

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {

//            TODO: Something like:
//            foreach(CityInfo ci in list){}
//            val intent = Intent(context, WeatherService::class.java)
//            intent.putExtra("type", "both")
//            intent.putExtra("location",ci.name )
//            intent.putExtra("language", ci.language)
//            intent.putExtra("receiver", receiver)   TODO: have to pass a receiver every time that wont be used!?
//            context!!.startService(intent)
        }
    }
}