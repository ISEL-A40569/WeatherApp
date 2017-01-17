package pdm.isel.yawa.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import pdm.isel.yawa.services.NotificationsService

/**
 * Class used to start a NotificationsService repeatedly.
 */
class NotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent?.action)) {
            context!!.startService(Intent(context, NotificationsService::class.java))
        }
    }
}