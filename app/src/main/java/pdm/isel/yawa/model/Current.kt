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
        dest!!.writeString(name)
        dest.writeString(country)
        dest.writeString(lon)
        dest.writeString(lat)
        dest.writeParcelable(currentInfo, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Current> = object : Parcelable.Creator<Current> {
            override fun newArray(size: Int): Array<out Current> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createFromParcel(source: Parcel?): Current {
                return Current(
                        source!!.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readParcelable(CurrentWeatherInfo::class.java.classLoader)
                )
            }
        }
    }
}