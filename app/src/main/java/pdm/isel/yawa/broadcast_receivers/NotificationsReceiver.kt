package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pdm.isel.yawa.services.NotificationsService

/**
 * Created by Dani on 14-12-2016.
 */
class NotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            context!!.startService(Intent(context, NotificationsService::class.java))
        }
    }
}