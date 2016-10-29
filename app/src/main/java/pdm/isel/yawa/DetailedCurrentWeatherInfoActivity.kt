package pdm.isel.yawa

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import java.util.Date;

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

    var bitMap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_current_weather_info)

        Log.d("RESPONSE", "WEATHER INFO FOR: " + currentWeather!!.name)

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


        city!!.setText(currentWeather!!.cityName)
        country!!.setText(currentWeather!!.country)
        date!!.setText(currentWeather!!.getDate())
        lat!!.setText(currentWeather!!.lat.toString())
        lon!!.setText(currentWeather!!.lon.toString())
        desc!!.setText(currentWeather!!.description)
        hum!!.setText(currentWeather!!.humidity.toString())
        wind!!.setText(currentWeather!!.windSpeed.toString())
        sunrise!!.setText(currentWeather!!.getSunrise())
        sunset!!.setText(currentWeather!!.getSunset())
        temp!!.setText(currentWeather!!.temp.toInt().toString()+"º")
        press!!.setText(currentWeather!!.pressure.toString())

        image = findViewById(R.id.detail_image) as ImageView?


    }
}
