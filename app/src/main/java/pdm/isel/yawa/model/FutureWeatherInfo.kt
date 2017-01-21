package pdm.isel.yawa.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

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
) : BaseWeatherInfo(_date, _description, _pressure, _humidity, _icon), Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(date)
        dest.writeString(pressure)
        dest.writeString(humidity)
        dest.writeString(description)
        dest.writeString(icon)
        dest.writeString(tempMin)
        dest.writeString(tempMax)
        dest.writeParcelable(image, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<FutureWeatherInfo> = object : Parcelable.Creator<FutureWeatherInfo> {
            override fun newArray(size: Int): Array<out FutureWeatherInfo> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createFromParcel(source: Parcel?): FutureWeatherInfo {
                val fwi = FutureWeatherInfo(
                        source!!.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString()
                )

                fwi.image = source.readParcelable(Bitmap::class.java.classLoader)
                return fwi
            }
        }
    }
}

