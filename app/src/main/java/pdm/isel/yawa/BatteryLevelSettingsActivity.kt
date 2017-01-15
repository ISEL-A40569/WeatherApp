package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

var minimumBatteryLevel: Int = 0

class BatteryLevelSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_level_settings)

        var editText = findViewById(R.id.BatteryLevelBox) as EditText

        var button = findViewById(R.id.BatteryLevelButton) as Button

        button.setOnClickListener {
            minimumBatteryLevel = editText!!.text.toString().toInt()
            //TODO: VALIDATIONS, HERE AND ELSEWHERE NEEDED

            application.editor.putInt("minimumBatteryLevel", minimumBatteryLevel)
            application.editor.commit()
            Log.d("OnBatteryLevelButton", minimumBatteryLevel.toString())

            Toast.makeText(this, "Minimum Battery Level: " + minimumBatteryLevel.toString(), Toast.LENGTH_SHORT).show()

        }
        findViewById(android.R.id.content)
                .setBackgroundDrawable(resources.getDrawable(R.drawable.battery))

    }
}
