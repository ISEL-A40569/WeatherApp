package pdm.isel.yawa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pdm.isel.yawa.model.BasicWeatherInfo

var BASIC_WEATHER_INFO : BasicWeatherInfo? = null

class BasicWeatherInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_weather_info)

        Log.d("RESPONSE", "BASIC_WEATHER_INFO FOR: " + BASIC_WEATHER_INFO!!.dateTime)

        //TODO
    }
}
