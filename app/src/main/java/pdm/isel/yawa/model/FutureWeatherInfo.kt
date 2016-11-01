package pdm.isel.yawa.model

import android.graphics.Bitmap
import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class FutureWeatherInfo(val _date: String,
                        val _pressure: String,
                        val _humidity: String,
                        val _description: String,
                        val _icon: String,
                        val tempMin: String,
                        val tempMax: String
                        ) : BaseWeatherInfo(_date, _description, _pressure, _humidity, _icon) {

}

