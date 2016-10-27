package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import pdm.isel.yawa.model.FutureWeatherInfo

var futureWeatherInfo: FutureWeatherInfo? = null

class BasicWeatherInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_weather_info)

        Log.d("RESPONSE", "futureWeatherInfo FOR: " + futureWeatherInfo!!.dateTime)

        //TODO
    }
}
