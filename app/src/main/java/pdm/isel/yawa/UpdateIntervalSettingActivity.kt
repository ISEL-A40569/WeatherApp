package pdm.isel.yawa

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver

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
                SystemClock.elapsedRealtime() + updateInterval,
                updateInterval * 60000,
                pendingAlarmIntent)

        Toast.makeText(this, updateInterval.toString() + " min", Toast.LENGTH_LONG).show()
        Log.d("OnUpdatingInterval", updateInterval.toString())
    }
}
