package pdm.isel.yawa.requests

/**
 * Created by Dani on 16-12-2016.
 */
interface Callback<T> {
    fun onSuccess(response:T)
}