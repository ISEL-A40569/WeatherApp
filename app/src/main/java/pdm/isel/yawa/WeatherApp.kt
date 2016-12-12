package pdm.isel.yawa

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pdm.isel.yawa.broadcaste_receiver.WeatherBroadcastReceiver

/**
 * Created by Dani on 21-10-2016.
 */
class WeatherApp : Application() {

    val requestQueue by lazy { Volley.newRequestQueue(this) }

    override fun onCreate() {
        super.onCreate()
        Log.d("Weather/App", "WeatherApp onCreate")


        val alarmIntent = Intent(this, WeatherBroadcastReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null)
        {
            Log.i("Weather/App", "no alarm")

            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 10000,
                    10000,
                    pendingAlarmIntent
            )
        }
    }

}

val Application.requestQueue : RequestQueue
    get() = (this as WeatherApp).requestQueue