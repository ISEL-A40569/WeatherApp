package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import pdm.isel.yawa.model.FutureWeatherInfo

var futureWeatherInfo: FutureWeatherInfo? = null

class BasicWeatherInfoActivity : AppCompatActivity() {
    var date: TextView? = null
    var tmin: TextView? = null
    var tmax: TextView? = null
    var pressure: TextView? = null
    var humidity: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.future_weather_info)

        initViews()
    }

    private fun initViews() {
        date = findViewById(R.id.future_date) as TextView
        tmin = findViewById(R.id.future_temp_min) as TextView
        tmax = findViewById(R.id.future_temp_max) as TextView
        pressure = findViewById(R.id.future_pressure) as TextView
        humidity = findViewById(R.id.future_hum) as TextView
        description = findViewById(R.id.future_description) as TextView
        image = findViewById(R.id.future_image) as ImageView
    }

    override fun onStart() {
        super.onStart()
        setViews()
    }

    private fun setViews() {
        date?.setText(futureWeatherInfo?._date)
        tmin?.setText(futureWeatherInfo?.tempMin)
        tmax?.setText(futureWeatherInfo?.tempMax)
        pressure?.setText(futureWeatherInfo?._pressure)
        humidity?.setText(futureWeatherInfo?._humidity)
        description?.setText(futureWeatherInfo?.description)
        image?.setImageBitmap(futureWeatherInfo?.image)
        setBackGroundImage(futureWeatherInfo?.icon!!)
    }

    private fun setBackGroundImage(str:String) {

        when (str) {

            "01d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i01d))
            }
            "01n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i01n))
            }
            "02d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i02d))
            }
            "02n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i02n))
            }
            "03d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i03d))
            }
            "03n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i03n))
            }
            "04d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i04d))
            }
            "04n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i04n))
            }
            "05d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i05d))
            }
            "05n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i05n))
            }
            "06d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i06d))
            }
            "06n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i06n))
            }
            "07d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i07d))
            }
            "07n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i07n))
            }
            "08d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i08d))
            }
            "08n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i08n))
            }
            "09d" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i09d))
            }
            "09n" -> {
                findViewById(android.R.id.content)
                        .setBackgroundDrawable(resources.getDrawable(R.drawable.i09n))
            }
        }
    }

}
