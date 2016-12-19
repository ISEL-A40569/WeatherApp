package pdm.isel.yawa.model

import android.os.Parcel

/**
 * Created by Dani on 24-10-2016.
 */
abstract class CityInfo(
        val cityName: String,
                         val cityCountry: String,
                         val ln: String,
                         val lt: String
                         ) : MyParcelable(Parcel.obtain()){
    var language: String? = null;

}