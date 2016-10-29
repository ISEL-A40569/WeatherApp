package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class FutureWeatherInfo(val dateTime: String,
                        val tempMin: Float,
                        val tempMax: Float,
                        val pressure: Float,
                        val humidity: Float,
                        val main: String,
                        val description: String,
                        val icon: String) {


    public fun getDate():String{

        var d = dateTime.split(" ")
        return d[2] + " " + d[1] + " "+ d[5]
    }



}

