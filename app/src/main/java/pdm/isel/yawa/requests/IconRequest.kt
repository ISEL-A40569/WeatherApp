package pdm.isel.yawa.requests

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest

/**
 * Created by Dani on 16-12-2016.
 */
class IconRequest(uri: String, callback: Callback<Bitmap>) : ImageRequest(
        uri,
        Response.Listener<Bitmap> { bitmap ->
            Log.d("RESPONSE", "GOT ICON ")
            callback.onSuccess(bitmap)
        },
        0, 0,
        ImageView.ScaleType.CENTER_INSIDE,
        null,
        Response.ErrorListener { error ->
            Log.d("ERROR: ", error.toString())
        }
) {
}