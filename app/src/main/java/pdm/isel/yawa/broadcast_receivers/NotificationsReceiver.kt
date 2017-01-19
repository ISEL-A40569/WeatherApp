package pdm.isel.yawa.broadcast_receivers

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import pdm.isel.yawa.services.NotificationsService

/**
 * Class used to start a NotificationsService repeatedly.
 */
class NotificationsReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (Intent.ACTION_BOOT_COMPLETED != intent?.action) {
            context!!.startService(Intent(context, NotificationsService::class.java))
        }
    }
}