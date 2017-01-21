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
import android.widget.*
import org.w3c.dom.Text
import pdm.isel.yawa.broadcast_receivers.WeatherBroadcastReceiver


class UpdateSettingsActivity : AppCompatActivity() {
    var updateInterval: Int = 1
    var wifiOnly = false
    var view: TextView? = null
    var bar: SeekBar? = null
    var text: TextView? = null
    var checkBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_interval_setting)

        initViews()

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

        checkBox!!.setOnClickListener {

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

    private fun initViews() {
        view = findViewById(R.id.UpdateLevelTextView) as TextView
        bar = findViewById(R.id.UpdateLevelBar) as SeekBar
        text = findViewById(R.id.UpdateTextView) as TextView
        text!!.setTextColor(Color.GREEN)
        checkBox = findViewById(R.id.WifiCheckBox) as CheckBox
        checkBox!!.setBackgroundColor(Color.WHITE)
    }

    override fun onStart() {
        super.onStart()

        wifiOnly = application.prefs.getBoolean("wifiOnly", false)
        checkBox!!.isChecked = wifiOnly

        updateInterval = application.prefs.getInt("updateInterval", 1)

        view!!.text =  updateInterval.toString() + "min"

        bar!!.progress = updateInterval
    }

    fun onUpdateTime(view: View) {
        Log.d("OnUpdatingInterval", updateInterval.toString())
        updateInterval = bar!!.progress
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

    private fun setTextView(bar: SeekBar, view: TextView) {
        view.text = bar.progress.toString() + "min"
    }
}
