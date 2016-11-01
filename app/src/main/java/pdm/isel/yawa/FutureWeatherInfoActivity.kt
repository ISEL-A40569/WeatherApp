package pdm.isel.yawa

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import pdm.isel.yawa.model.FutureWeatherInfo

var futureWeatherInfo: FutureWeatherInfo? = null

class BasicWeatherInfoActivity : AppCompatActivity() {
    var date: TextView? = null
    var tmin: TextView? = null
    var tmax: TextView? = null
    var pressure: TextView? = null
    var humidity: TextView? = null
    var main: TextView? = null
    var description: TextView? = null

    var image: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_weather_info)

        date = findViewById(R.id.dateView) as TextView
        tmin = findViewById(R.id.tMinView) as TextView
        tmax = findViewById(R.id.tMaxView) as TextView
        pressure = findViewById(R.id.pressureView) as TextView
        humidity = findViewById(R.id.humidityView) as TextView
        description = findViewById(R.id.descriptionView) as TextView

        image = findViewById(R.id.iconView) as ImageView

    }

    override fun onStart(){
        super.onStart()

        date?.setText(futureWeatherInfo?._date)
        tmin?.setText(futureWeatherInfo?.tempMin)
        tmax?.setText(futureWeatherInfo?.tempMax)
        pressure?.setText(futureWeatherInfo?._pressure)
        humidity?.setText(futureWeatherInfo?._humidity)
        description?.setText(futureWeatherInfo?.description)

        application.requestQueue.add(getIconView(URI_FACTORY.getIcon(futureWeatherInfo!!.icon)))//TODO: IMAGE IS NOT BEING SAVED
    }

    public fun getIconView(url: String): ImageRequest {
        return ImageRequest(url,
                object : Response.Listener<Bitmap> {
                    override fun onResponse(bitmap: Bitmap) {
                        image?.setImageBitmap(bitmap)
                    }
                }, 0, 0, null,
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("ERROR: ", error.toString())
                    }
                })
    }
}
