package pdm.isel.yawa.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Dani on 01-11-2016.
 */
class CurrentWeatherInfo(val _date: String,
                         val _pressure: String,
                         val _humidity: String,
                         val _description: String,
                         val _icon: String,
                         val temp: String,
                         val sunrise: String,
                         val sunset: String,
                         val windSpeed: String
) : BaseWeatherInfo(_date, _description, _pressure, _humidity, _icon), Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(date)
        dest.writeString(pressure)
        dest.writeString(humidity)
        dest.writeString(description)
        dest.writeString(icon)
        dest.writeString(temp)
        dest.writeString(sunrise)
        dest.writeString(sunset)
        dest.writeString(windSpeed)
        dest.writeParcelable(image, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<CurrentWeatherInfo> = object : Parcelable.Creator<CurrentWeatherInfo> {
            override fun newArray(size: Int): Array<out CurrentWeatherInfo> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createFromParcel(source: Parcel?): CurrentWeatherInfo {
                val wi = CurrentWeatherInfo(
                        source!!.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString())

                wi.image = source.readParcelable(Bitmap::class.java.classLoader)
                return wi
            }
        }
    }
}