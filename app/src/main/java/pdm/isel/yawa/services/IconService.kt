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
import java.util.concurrent.CountDownLatch

/**
 * Created by Dani on 20-12-2016.
 */
class IconService : IntentService("IconService"){
    val s = "Inside Icon Service"
    override fun onCreate() {
        super.onCreate()
        Log.d("OnIconService", s)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("OnIconService", "onHandleIntent start")
        val receiver: ResultReceiver = intent!!.getParcelableExtra("iconReceiver")
        val icon = intent!!.getStringExtra("icon")
        Log.d("OnIconService", "icon is " + icon)

        makeIconRequest(icon, receiver)
        Log.d("OnIconService", "onHandleIntent end")
    }

    private fun makeIconRequest(iconCode:String,  receiver: ResultReceiver) {
        Log.d("OnIconService", "makeIconRequest start")

        application.requestQueue.add(IconRequest(
                URI_FACTORY.getIcon(iconCode),
                getIconRequestCallback(iconCode, receiver)
                ))
        Log.d("OnIconService", "makeIconRequest end")
    }

    private fun getIconRequestCallback(iconCode: String, receiver: ResultReceiver): Callback<Bitmap> {
        Log.d("OnIconService", "getIconRequestCallback start")
        return object : Callback<Bitmap> {
            override fun onSuccess(icon: Bitmap) {
                Log.d("OnIconService", "getIconRequestCallback onSuccess")
                application.iconCache.push(iconCode, icon)
                sendInfo(receiver, "icon", icon)
            }
        }
        Log.d("OnIconService", "getIconRequestCallback end")
    }

    private fun sendInfo(receiver: ResultReceiver, key: String, image: Bitmap) {
        Log.d("OnIconService", "sendInfo start")
        val bundle = Bundle()
        bundle.putParcelable(key, image)
        receiver.send(200, bundle)
        Log.d("OnIconService", "sendInfo end")
    }
}