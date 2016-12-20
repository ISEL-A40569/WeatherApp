package pdm.isel.yawa.services

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import pdm.isel.yawa.URI_FACTORY
import pdm.isel.yawa.iconCache
import pdm.isel.yawa.model.BaseWeatherInfo
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.model.FutureWeatherInfo
import pdm.isel.yawa.requestQueue
import pdm.isel.yawa.requests.Callback
import pdm.isel.yawa.requests.IconRequest

/**
 * Created by Dani on 20-12-2016.
 */
class IconService : IntentService("IconService"){
    override fun onHandleIntent(intent: Intent?) {
        val receiver: ResultReceiver = intent!!.getParcelableExtra("receiver")
        val icon = intent!!.getStringExtra("icon")

        makeIconRequest(icon, getIconRequestCallback(icon, receiver))
    }

    private fun makeIconRequest(iconCode:String, callback: Callback<Bitmap>) {
        Log.d("OnIconService", "makeIconRequest start")

        application.requestQueue.add(IconRequest(
                URI_FACTORY.getIcon(iconCode),
                callback))
        Log.d("OnIconService", "makeIconRequest end")
    }

    private fun getIconRequestCallback(iconCode: String, receiver: ResultReceiver): Callback<Bitmap> {
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                iconCache.push(iconCode, icon)
                sendInfo(receiver, "icon", icon)
            }
        }
    }

    private fun sendInfo(receiver: ResultReceiver, key: String, image: Bitmap) {
        Log.d("OnService", "sendInfo start")

        val bundle = Bundle()
        bundle.putParcelable(key, image)
        receiver.send(200, bundle)
        Log.d("OnService", "sendInfo end")

    }
}