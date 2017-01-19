package pdm.isel.yawa.requests

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class DataRequest(url: String, callback: Callback<JSONObject>) : JsonObjectRequest(
        Method.GET,
        url,
        null,
        Response.Listener<JSONObject> { response ->
            Log.d("OnService: ", response.toString())

            callback.onSuccess(response)
        },
        Response.ErrorListener { error ->
            Log.d("ERROR: ", error.toString())
            //TODO: throw some null response exception
        })



