package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pdm.isel.yawa.services.WeatherService

/**
 * Created by Dani on 08-12-2016.
 */
class WeatherBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            var intent = Intent(context, WeatherService::class.java)
            intent.putExtra("type", "both")
            context!!.startService(intent)
        }
    }
}