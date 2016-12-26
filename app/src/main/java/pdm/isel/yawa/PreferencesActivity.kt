package pdm.isel.yawa

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
    }

    fun onUpdateIntervalSetting(view: View){
        val intent = Intent(this, UpdateIntervalSettingActivity::class.java)
        startActivity(intent)
    }

    fun onSelectCity(view: View){
        val intent = Intent(this, SelectionActivity::class.java)
        startActivity(intent)
    }

    fun onNotificationSettings(view: View){
        val intent = Intent(this, NotificationSettingsActivity::class.java)
        startActivity(intent)
    }

    fun onBatteryLevelSettings(view: View){
        val intent = Intent(this, BatteryLevelSettingsActivity::class.java)
        startActivity(intent)
    }

}


