package pdm.isel.yawa.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Dani on 21-10-2016.
 */
class Forecast(val name: String,
               val country: String,
               val lon: String,
               val lat: String,
               val list: Array<FutureWeatherInfo>) :
        CityInfo(name, country, lon, lat), Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(name)
        dest.writeString(country)
        dest.writeString(lon)
        dest.writeString(lat)
        dest.writeParcelableArray(list, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Forecast> = object : Parcelable.Creator<Forecast> {
            override fun newArray(size: Int): Array<out Forecast> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createFromParcel(source: Parcel?): Forecast {
                return Forecast(
                        source!!.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readParcelableArray(FutureWeatherInfo::class.java.classLoader) as Array<FutureWeatherInfo>
                )
            }
        }
    }

}