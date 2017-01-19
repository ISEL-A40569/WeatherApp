package pdm.isel.yawa

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*



class BatteryLevelSettingsActivity : AppCompatActivity() {
    var minimumBatteryLevel: Int = 0
    var view: TextView? = null
    var bar: SeekBar? = null
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_level_settings)

        view = findViewById(R.id.BatteryLevelTextView) as TextView
        bar = findViewById(R.id.BatteryLevelBar) as SeekBar
        button = findViewById(R.id.BatteryLevelButton) as Button

        bar!!.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        setTextView(bar!!, view!!)
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        setTextView(bar!!, view!!)                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        setTextView(bar!!, view!!)                    }

                }
        )

        button!!.setOnClickListener {
            minimumBatteryLevel = bar!!.progress

            application.editor.putInt("minimumBatteryLevel", minimumBatteryLevel)
            application.editor.commit()
            Log.d("OnBatteryLevelButton", minimumBatteryLevel.toString())

            Toast.makeText(this, "Minimum Battery Level: " + minimumBatteryLevel.toString(), Toast.LENGTH_SHORT).show()
        }

        findViewById(android.R.id.content)
                .background = ResourcesCompat.getDrawable(resources, R.drawable.battery, null)
    }


    override fun onStart() {
        super.onStart()

        minimumBatteryLevel = application.prefs.getInt("minimumBatteryLevel", 25)

        view!!.text =  minimumBatteryLevel.toString() + "%"

        bar!!.progress = minimumBatteryLevel
    }


    private fun setTextView(bar: SeekBar, view: TextView) {
        view.text = bar.progress.toString() + "%"
    }
}
