package pdm.isel.yawa

import android.app.AlarmManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver


class UpdateSettingsActivity : AppCompatActivity() {
    var updateInterval: Int = 1
    var wifiOnly = false
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_interval_setting)
        editText = findViewById(R.id.UptdateTimeText) as EditText

        val text = findViewById(R.id.UpdateTextView) as TextView
        text.setTextColor(Color.GREEN)


        val checkBox = findViewById(R.id.WifiCheckBox) as CheckBox
        checkBox.setBackgroundColor(Color.WHITE)

        wifiOnly = application.prefs.getBoolean("wifiOnly", false)

        checkBox.isChecked = wifiOnly
        checkBox.setOnClickListener {

            wifiOnly = !wifiOnly

            application.editor.putBoolean("wifiOnly", wifiOnly)

            if (wifiOnly) {
                Toast.makeText(this, "Only Wifi Allowed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All Connections Allowed", Toast.LENGTH_SHORT).show()
            }

            application.editor.commit()
        }
        findViewById(android.R.id.content)
                .background = ResourcesCompat.getDrawable(resources, R.drawable.menu, null)
    }

    fun onUpdateTime(view: View) {
        Log.d("OnUpdatingInterval", updateInterval.toString())
        updateInterval = editText!!.text.toString().toInt()
        application.editor.putInt("updateInterval", updateInterval)
        application.editor.commit()

        var pendingAlarmIntent = android.app.PendingIntent.getBroadcast(applicationContext, 0, Intent(applicationContext, WeatherBroadcastReceiver::class.java), 0)

        application.alarmManager.cancel(pendingAlarmIntent)

        application.alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + updateInterval,
                updateInterval * 60000L,
                pendingAlarmIntent)

        Toast.makeText(this, updateInterval.toString() + " min", Toast.LENGTH_LONG).show()
        Log.d("OnUpdatingInterval", updateInterval.toString())
    }
}
