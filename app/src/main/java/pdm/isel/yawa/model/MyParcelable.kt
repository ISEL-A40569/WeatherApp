package pdm.isel.yawa.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Dani on 18-12-2016.
 */

open class MyParcelable protected constructor(`in`: Parcel) : Parcelable {

    val s = "Here"

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
    }

    companion object {

        val CREATOR: Parcelable.Creator<MyParcelable> = object : Parcelable.Creator<MyParcelable> {
            override fun createFromParcel(`in`: Parcel): MyParcelable {
                return MyParcelable(`in`)
            }

            override fun newArray(size: Int): Array<MyParcelable?> {
                return arrayOfNulls(size)
            }
        }
    }
}
