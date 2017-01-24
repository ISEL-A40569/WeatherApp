package pdm.isel.yawa.requests

/**
 * Interface whose implementations are used to obtain HTTP requests responses.
 */
interface Callback<T> {
    fun onSuccess(response: T)
}