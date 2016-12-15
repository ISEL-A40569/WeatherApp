package pdm.isel.yawa

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

var hourValue: Int = 8
var minutesValue: Int = 0

class NotificationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        var button: Button = findViewById(R.id.DefineTimeButton) as Button

        var hour: EditText = findViewById(R.id.HourSelectionBox) as EditText
        var minutes: EditText = findViewById(R.id.MinutesSelectionBox) as EditText

        button.setOnClickListener {
            Log.d("OnSettingNotifications", hourValue.toString())
            Log.d("OnSettingNotifications", minutesValue.toString())

            hourValue= Integer.valueOf(hour.text.toString())
            minutesValue = Integer.valueOf(minutes.text.toString())

            val editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()//TODO: use only one editor in app!?
            editor.putInt("hour", hourValue)
            editor.putInt("minutes", minutesValue)
            editor.commit()

            Log.d("OnSettingNotifications", hourValue.toString())
            Log.d("OnSettingNotifications", minutesValue.toString())
        }
    }
}
