package pdm.isel.yawa.requests

import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import pdm.isel.yawa.model.CityInfo


class DataRequest(url: String, callback: Callback<JSONObject>) : JsonObjectRequest(
        Method.GET,
        url,
        null,
        object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                callback.onSuccess(response)
            }

        },
        object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("ERROR: ", error.toString())
                //TODO: throw some null response exception
            }
        })



