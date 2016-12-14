package pdm.isel.yawa.broadcaste_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pdm.isel.yawa.service.WeatherService

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            context!!.startService(Intent(context, WeatherService::class.java))
        }
    }
}