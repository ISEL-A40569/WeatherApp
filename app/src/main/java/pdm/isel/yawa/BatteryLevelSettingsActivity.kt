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

        val view = findViewById(R.id.BatteryLevelTextView) as TextView

        val startLevel = application.prefs.getInt("minimumBatteryLevel", 25)

        view.text =  startLevel.toString() + "%"

        val bar = findViewById(R.id.BatteryLevelBar) as SeekBar
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


        val button = findViewById(R.id.BatteryLevelButton) as Button

        button.setOnClickListener {
            minimumBatteryLevel = bar.progress

            application.editor.putInt("minimumBatteryLevel", minimumBatteryLevel)
            application.editor.commit()
            Log.d("OnBatteryLevelButton", minimumBatteryLevel.toString())

            Toast.makeText(this, "Minimum Battery Level: " + minimumBatteryLevel.toString(), Toast.LENGTH_SHORT).show()

        }
        findViewById(android.R.id.content)
                .setBackgroundDrawable(resources.getDrawable(R.drawable.battery))//TODO: nao deviamos usar deprecated's

    }

    private fun setTextView(bar: SeekBar, view: TextView) {
        view.text = bar.progress.toString() + "%"
    }
}
