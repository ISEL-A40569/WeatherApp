package pdm.isel.yawa

import android.app.AlarmManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import pdm.isel.yawa.broadcast_receivers.NotificationsReceiver
import java.util.*


class NotificationSettingsActivity : AppCompatActivity() {
    var hourValue: Int = 0
    var minutesValue: Int = 0
    var areNotificationsOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        val text = findViewById(R.id.NotificationsTextView) as TextView
        text.setTextColor(Color.GREEN)

        val button: Button = findViewById(R.id.DefineTimeButton) as Button

        val timePicker: TimePicker = findViewById(R.id.NotificationsTimePicker) as TimePicker

        timePicker.hour = application.prefs.getInt("hour", 8)
        timePicker.minute = application.prefs.getInt("minutes", 0)
        areNotificationsOn = application.prefs.getBoolean("areNotificationsOn", false)

        val checkBox = findViewById(R.id.NotificationsCheckBox) as CheckBox
        checkBox.isChecked = areNotificationsOn

        Log.d("OnSettingNotifications", checkBox.isChecked.toString())

        checkBox.setOnClickListener {
            Log.d("OnSettingNotifications", checkBox.isChecked.toString())

            areNotificationsOn = !areNotificationsOn

            application.editor.putBoolean("areNotificationsOn", areNotificationsOn)

            if (areNotificationsOn) {
                Toast.makeText(this, "Notifications On", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications Off", Toast.LENGTH_SHORT).show()
            }

            application.editor.commit()
        }
        button.setOnClickListener {
            Log.d("OnSettingNotifications", hourValue.toString())
            Log.d("OnSettingNotifications", minutesValue.toString())

            hourValue = timePicker.hour
            minutesValue = timePicker.minute

            application.editor.putInt("hour", hourValue)
            application.editor.putInt("minutes", minutesValue)
            application.editor.commit()

            val pendingAlarmIntent = android.app.PendingIntent.getBroadcast(applicationContext, 1, Intent(applicationContext, NotificationsReceiver::class.java), 0)

            application.alarmManager.cancel(pendingAlarmIntent)

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourValue)
            calendar.set(Calendar.MINUTE, minutesValue)
            calendar.set(Calendar.SECOND, 0)

            application.alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingAlarmIntent
            )

            Log.d("OnSettingNotifications", hourValue.toString())
            Log.d("OnSettingNotifications", minutesValue.toString())
            Toast.makeText(this, hourValue.toString() + ":" + minutesValue.toString(), Toast.LENGTH_LONG).show()

        }
        findViewById(android.R.id.content)
                .background = ResourcesCompat.getDrawable(resources, R.drawable.menu, null)

    }
}
