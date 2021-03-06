package pdm.isel.yawa.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent
import android.support.v7.app.NotificationCompat
import android.util.Log
import pdm.isel.yawa.R
import pdm.isel.yawa.prefs


/**
 * Class used to send a notification message.
 */
class NotificationsService : IntentService("NotificationService") {
    var areNotificationsOn: Boolean = false

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnNotificationsService", "OnNotificationsService")
        areNotificationsOn = application.prefs.getBoolean("areNotificationsOn", false)

        if (areNotificationsOn)
            generateNotification()
        completeWakefulIntent(intent)
    }

    fun generateNotification() {

        var mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var notifyID = 1;

        var mNotifyBuilder = NotificationCompat.Builder(this)
                .setContentTitle(application.prefs.getString("city", "") +
                        " " + application.prefs.getString("temp", ""))
                .setContentText(application.prefs.getString("description", ""))
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setVibrate(longArrayOf(1000, 1000))
        mNotificationManager.notify(
                notifyID,
                mNotifyBuilder.build());

    }
}