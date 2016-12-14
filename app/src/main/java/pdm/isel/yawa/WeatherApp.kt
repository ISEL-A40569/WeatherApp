package pdm.isel.yawa

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pdm.isel.yawa.broadcast_receivers.NotificationsReceiver
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver
import pdm.isel.yawa.icons.IconCache
import pdm.isel.yawa.provider.WeatherCrudFunctions
import java.util.*

val crud = WeatherCrudFunctions()
val iconCache = IconCache()


class WeatherApp : Application() {

    val requestQueue by lazy { Volley.newRequestQueue(this) }

    override fun onCreate() {
        super.onCreate()
        Log.d("Weather/App", "WeatherApp onCreate")

        val alarmIntentForWeatherService = Intent(this, WeatherBroadcastReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 0, alarmIntentForWeatherService, PendingIntent.FLAG_NO_CREATE) == null) {
            Log.i("Weather/App", "no alarm")

            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntentForWeatherService, 0)

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 6000,
                    updateInterval * 60000,
                    pendingAlarmIntent
            )
        }

        val alarmIntentForNotificationsService = Intent(this, NotificationsReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 1, alarmIntentForNotificationsService, PendingIntent.FLAG_NO_CREATE) == null) {
            Log.i("Weather/App", "no alarm")

            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntentForNotificationsService, 0)

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 49)
            calendar.set(Calendar.SECOND, 0)
//            calendar.set(2016, 12, 14, 23, 42, 0)
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingAlarmIntent
            )
        }
    }



//    Intent intent = new Intent(MainActivity.this, Receiver.class);
//    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
//    AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//    am.setRepeating(am.RTC_WAKEUP, System.currentTimeInMillis(), am.INTERVAL_DAY*7, pendingIntent);
}

val Application.requestQueue : RequestQueue
    get() = (this as WeatherApp).requestQueue