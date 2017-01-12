package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*

var minimumBatteryLevel: Int = 0

class BatteryLevelSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_level_settings)

        var view = findViewById(R.id.BatteryLevelTextView) as TextView

        var startLevel = application.prefs.getInt("minimumBatteryLevel", 25)

        view.text =  startLevel.toString() + "%"

        var bar = findViewById(R.id.BatteryLevelBar) as SeekBar
        bar.progress = startLevel

        bar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        setTextView(bar, view)
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        setTextView(bar, view)                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        setTextView(bar, view)                    }

                }
        )


        var button = findViewById(R.id.BatteryLevelButton) as Button

        button.setOnClickListener {
            minimumBatteryLevel = bar.progress
            //TODO: VALIDATIONS, HERE AND ELSEWHERE NEEDED

            application.editor.putInt("minimumBatteryLevel", minimumBatteryLevel)
            application.editor.commit()
            Log.d("OnBatteryLevelButton", minimumBatteryLevel.toString())

            Toast.makeText(this, "Minimum Battery Level: " + minimumBatteryLevel.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    private fun setTextView(bar: SeekBar, view: TextView) {
        view.text = bar.progress.toString() + "%"
    }
}
