package pdm.isel.yawa.requests

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

/**
 * Created by Dani on 16-12-2016.
 */
class DataRequest(url: String, listener: Response.Listener<JSONObject>) : JsonObjectRequest(
        Method.GET,
        url,
        null,
        listener,
        object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
                //TODO: throw some null response exception
            }
        })



