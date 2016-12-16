package pdm.isel.yawa

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.EditText
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver
import pdm.isel.yawa.services.WeatherService

var updateInterval: Long = 0

class UpdateIntervalSettingActivity : AppCompatActivity() {
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_interval_setting)
        editText = findViewById(R.id.UptdateTimeText) as EditText
    }

    fun onUpdateTime(view: View){
        Log.d("OnUpdatingInterval", updateInterval.toString())
        updateInterval =  editText!!.text.toString().toLong()
        application.editor.putLong("updateInterval", updateInterval)
        application.editor.commit()

        var pendingAlarmIntent = android.app.PendingIntent.getBroadcast(applicationContext,0, Intent(applicationContext, WeatherBroadcastReceiver::class.java), 0)

        application.alarmManager.cancel(pendingAlarmIntent)

        application.alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 6000,
                updateInterval * 60000,
                pendingAlarmIntent)

        Log.d("OnUpdatingInterval", updateInterval.toString())
    }
}