package pdm.isel.yawa.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Dani on 21-10-2016.
 */
class Current(val name: String,
              val country: String,
              val lon: String,
              val lat: String,
              val currentInfo: CurrentWeatherInfo
              ) : CityInfo(name, country, lon, lat), Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        
    }
}