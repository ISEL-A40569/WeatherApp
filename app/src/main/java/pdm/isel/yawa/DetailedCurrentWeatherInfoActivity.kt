package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class DetailedCurrentWeatherInfoActivity : AppCompatActivity() {

    var city: TextView? = null
    var country: TextView? = null
    var date: TextView? = null
    var lat: TextView? = null
    var lon: TextView? = null
    var desc: TextView? = null
    var hum: TextView? = null
    var wind: TextView? = null
    var sunrise: TextView? = null
    var sunset: TextView? = null
    var temp: TextView? = null
    var press: TextView? = null
    var image: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_current_weather_info)

        Log.d("RESPONSE", "WEATHER INFO FOR: " + currentWeather!!.name)

        initViews()
    }

    private fun initViews() {
        city = findViewById(R.id.d_city) as TextView?
        country = findViewById(R.id.d_country) as TextView?
        date = findViewById(R.id.d_date) as TextView?
        lat = findViewById(R.id.d_lat) as TextView?
        lon = findViewById(R.id.d_lon) as TextView?
        desc = findViewById(R.id.d_desc) as TextView?
        hum = findViewById(R.id.d_hum) as TextView?
        wind = findViewById(R.id.d_wind) as TextView?
        sunrise = findViewById(R.id.d_sunrise) as TextView?
        sunset = findViewById(R.id.d_sunset) as TextView?
        temp = findViewById(R.id.d_temp) as TextView?
        press = findViewById(R.id.d_press) as TextView?
        image = findViewById(R.id.detail_image) as ImageView?
    }

    override fun onStart(){
        super.onStart()
        setViews()
    }

    private fun setViews() {
        city!!.setText(currentWeather!!.cityName)
        country!!.setText(currentWeather!!.country)
        date!!.setText(currentWeather!!.currentInfo._date)
        lat!!.setText(currentWeather!!.lat.toString())
        lon!!.setText(currentWeather!!.lon.toString())
        desc!!.setText(currentWeather!!.currentInfo.description)
        hum!!.setText(currentWeather!!.currentInfo.humidity)
        wind!!.setText(currentWeather!!.currentInfo.windSpeed)
        sunrise!!.setText(currentWeather!!.currentInfo.sunrise)
        sunset!!.setText(currentWeather!!.currentInfo.sunset)
        temp!!.setText(currentWeather!!.currentInfo.temp)
        press!!.setText(currentWeather!!.currentInfo._pressure)
        image!!.setImageBitmap(currentWeather!!.currentInfo.image)
    }


}
