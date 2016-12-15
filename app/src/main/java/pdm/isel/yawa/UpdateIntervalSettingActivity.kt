package pdm.isel.yawa

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
var updateInterval: Long = 0

class UpdateIntervalSettingActivity : AppCompatActivity() {
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_interval_setting)
        editText = findViewById(R.id.UptdateTimeText) as EditText
    }

    fun onUpdateTime(view: View){
        Log.d("OnUpdatingInterval", updateInterval.toString())
        updateInterval =  editText!!.text.toString().toLong()
        val editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()//TODO: use only one editor in app!?
        editor.putLong("updateInterval", updateInterval)
        editor.commit()

        Log.d("OnUpdatingInterval", updateInterval.toString())
    }
}
