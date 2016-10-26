package pdm.isel.yawa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class DetailedWeatherInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        Log.d("RESPONSE", "WEATHER INFO FOR: " + weather!!.name)

        //TODO
    }
}
