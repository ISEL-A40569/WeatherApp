package pdm.isel.yawa

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.SystemClock
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import pdm.isel.yawa.broadcast_receivers.NotificationsReceiver
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver
import pdm.isel.yawa.icons.IconCache
import pdm.isel.yawa.json.JsonToDtoMapper
import pdm.isel.yawa.model.DtoToDomainMapper
import pdm.isel.yawa.provider.WeatherCrudFunctions
import pdm.isel.yawa.uri.RequestUriFactory
import java.util.*


val URI_FACTORY = RequestUriFactory()
val DTO_MAPPER = DtoToDomainMapper()
val JSON_MAPPER = JsonToDtoMapper()

val crud = WeatherCrudFunctions()

class WeatherApp : Application() {
    val MY_PREFS_NAME = "Prefs"

    val requestQueue by lazy { Volley.newRequestQueue(this) }

    var prefs: SharedPreferences? = null

    var editor: SharedPreferences.Editor? = null

    var alarmManager: AlarmManager? = null

    var connectivityManager: ConnectivityManager? = null

    val iconCache = IconCache()

    override fun onCreate() {
        super.onCreate()
        Log.d("Weather/App", "WeatherApp onCreate")

        prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs!!.edit()
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        getPreferences(prefs!!)

        //setWeatherReceiver()TODO

        if (areNotificationsOn)
            setNotificationsReceiver()
    }

    fun getPreferences(prefs: SharedPreferences) {
        Log.d("AppGetPrefs", updateInterval.toString())
        Log.d("AppGetPrefs", areNotificationsOn.toString())
        Log.d("AppGetPrefs", hourValue.toString())
        Log.d("AppGetPrefs", minutesValue.toString())

        updateInterval = prefs.getInt("updateInterval", 1)
        areNotificationsOn = prefs.getBoolean("areNotificationsOn", true)
        hourValue = prefs.getInt("hour", 22)
        minutesValue = prefs.getInt("minutes", 26)
        wifiOnly = prefs.getBoolean("wifiOnly", false)

        Log.d("AppGetPrefs", updateInterval.toString())
        Log.d("AppGetPrefs", areNotificationsOn.toString())
        Log.d("AppGetPrefs", hourValue.toString())
        Log.d("AppGetPrefs", minutesValue.toString())
    }

    fun setNotificationsReceiver() {
        val alarmIntentForNotificationsService = Intent(this, NotificationsReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 1, alarmIntentForNotificationsService, PendingIntent.FLAG_NO_CREATE) == null) {
            Log.i("Weather/App", "no alarm")

            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntentForNotificationsService, 0)

            Log.d("SettingNotifications", hourValue.toString())
            Log.d("SettingNotifications", minutesValue.toString())

            val calendar = getCalendar(hourValue, minutesValue)

            alarmManager!!.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    INTERVAL_DAY,
                    pendingAlarmIntent
            )
        }
    }

    private fun getCalendar(hour: Int, minutes: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }

    fun setWeatherReceiver() {
        val alarmIntentForWeatherService = Intent(this, WeatherBroadcastReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 0, alarmIntentForWeatherService, PendingIntent.FLAG_NO_CREATE) == null) {
            Log.i("Weather/App", "no alarm")

            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntentForWeatherService, 0)

            alarmManager!!.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + updateInterval,
                    updateInterval * 60000L,
                    pendingAlarmIntent
            )
        }
    }

}

val Application.requestQueue: RequestQueue
    get() = (this as WeatherApp).requestQueue

val Application.prefs: SharedPreferences
    get() = (this as WeatherApp).prefs!!

val Application.editor: SharedPreferences.Editor
    get() = (this as WeatherApp).editor!!

val Application.alarmManager: AlarmManager
    get() = (this as WeatherApp).alarmManager!!

val Application.connectivityManager: ConnectivityManager
    get() = (this as WeatherApp).connectivityManager!!

val Application.iconCache: IconCache
    get() = (this as WeatherApp).iconCache
