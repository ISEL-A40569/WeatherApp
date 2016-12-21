package pdm.isel.yawa.requests

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import java.util.concurrent.CountDownLatch

/**
 * Created by Dani on 16-12-2016.
 */
class IconRequest(uri: String, callback: Callback<Bitmap>) : ImageRequest(
        uri,
        object : Response.Listener<Bitmap> {
            override fun onResponse(bitmap: Bitmap) {
                Log.d("RESPONSE", "GOT ICON ")
                callback.onSuccess(bitmap)
            }
        },
        0, 0,
        ImageView.ScaleType.CENTER_INSIDE,
        null,
        object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
                //TODO: throw some null response exception
            }
        }
){
}