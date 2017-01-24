package pdm.isel.yawa.model

import android.graphics.Bitmap
import android.os.Parcelable

abstract class BaseWeatherInfo(val date: String,
                               val description: String,
                               val pressure: String,
                               val humidity: String,
                               val icon: String
                               ) : Parcelable {
    var image: Bitmap? = null

}