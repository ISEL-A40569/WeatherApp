package pdm.isel.yawa

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import pdm.isel.yawa.broadcast_receivers.NotificationsReceiver
import java.util.*

var hourValue: Int = 0
var minutesValue: Int = 0
var areNotificionsOn = true

class NotificationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        var button: Button = findViewById(R.id.DefineTimeButton) as Button

        var hour: EditText = findViewById(R.id.HourSelectionBox) as EditText
        var minutes: EditText = findViewById(R.id.MinutesSelectionBox) as EditText

        var checkBox = findViewById(R.id.NotificationsCheckBox) as CheckBox

        checkBox.setOnClickListener {
            Log.d("OnSettingNotifications", checkBox.isChecked.toString())

            if (checkBox.isChecked) {
                areNotificionsOn = true
            }else{
                areNotificionsOn = false
            }

            application.editor.putBoolean("areNotificionsOn", areNotificionsOn)
        }
            button.setOnClickListener {
                Log.d("OnSettingNotifications", hourValue.toString())
                Log.d("OnSettingNotifications", minutesValue.toString())

                hourValue = Integer.valueOf(hour.text.toString())
                minutesValue = Integer.valueOf(minutes.text.toString())

                application.editor.putInt("hour", hourValue)
                application.editor.putInt("minutes", minutesValue)
                application.editor.commit()

                var pendingAlarmIntent = android.app.PendingIntent.getBroadcast(applicationContext,1, Intent(applicationContext, NotificationsReceiver::class.java), 0)

                application.alarmManager.cancel(pendingAlarmIntent)

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourValue!!)
                calendar.set(Calendar.MINUTE, minutesValue!!)
                calendar.set(Calendar.SECOND, 0)

                application.alarmManager!!.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingAlarmIntent
                )

                Log.d("OnSettingNotifications", hourValue.toString())
                Log.d("OnSettingNotifications", minutesValue.toString())

        }
    }
}
