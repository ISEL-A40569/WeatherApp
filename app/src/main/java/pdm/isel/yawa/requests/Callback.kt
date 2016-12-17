package pdm.isel.yawa.requests

import android.graphics.Bitmap

/**
 * Created by Dani on 16-12-2016.
 */
interface Callback<T> {
    fun onSuccess(response:T)
}