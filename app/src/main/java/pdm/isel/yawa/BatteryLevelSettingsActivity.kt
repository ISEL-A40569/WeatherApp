package pdm.isel.yawa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

var batteryLevel: Int = 0

class BatteryLevelSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_level_settings)

        var editText = findViewById(R.id.BatteryLevelBox) as EditText

        var button = findViewById(R.id.BatteryLevelButton) as Button

        button.setOnClickListener {
            batteryLevel = editText!!.text.toString().toInt()
            //TODO: VALIDATIONS, HERE AND ELSEWHERE NEEDED

            application.editor.putInt("batteryLevel", batteryLevel)

            Log.d("OnBatteryLevelButton", batteryLevel.toString())
        }

    }
}
