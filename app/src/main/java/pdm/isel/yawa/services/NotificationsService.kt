package pdm.isel.yawa.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.NotificationCompat
import android.util.Log
import pdm.isel.yawa.R
import pdm.isel.yawa.areNotificationsOn
import pdm.isel.yawa.prefs


/**
 * Created by Dani on 14-12-2016.
 */
class NotificationsService : IntentService("NotificationService"){
    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnNotificationsService", "OnNotificationsService")
        if(areNotificationsOn)
        generateNotification()
    }

    fun generateNotification() {

        var mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var notifyID = 1;

        var mNotifyBuilder =  NotificationCompat.Builder(this)
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