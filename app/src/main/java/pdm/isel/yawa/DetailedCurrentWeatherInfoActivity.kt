package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class DetailedCurrentWeatherInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_current_weather_info)

        Log.d("RESPONSE", "WEATHER INFO FOR: " + currentWeather!!.name)

        //TODO
    }
}
