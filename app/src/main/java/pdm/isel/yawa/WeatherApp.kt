package pdm.isel.yawa

import android.app.Application
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by Dani on 21-10-2016.
 */
class WeatherApp : Application() {

    val requestQueue by lazy { Volley.newRequestQueue(this) }

    override fun onCreate() {
        super.onCreate()
        Log.d("Weather/App", "WeatherApp onCreate")
    }
}

val Application.requestQueue : RequestQueue
    get() = (this as WeatherApp).requestQueue