package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import pdm.isel.yawa.adapter.FutureWeatherInfoArrayAdapter
import pdm.isel.yawa.model.Forecast
import pdm.isel.yawa.model.FutureWeatherInfo

import pdm.isel.yawa.services.IconService
import pdm.isel.yawa.services.WeatherService

val NUMBER_OF_FORECAST_DAYS = 16

class ForecastActivity : ListActivity() {

    var forecast: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
    }

    override fun onStart() {
        super.onStart()
        startServiceForDataRequest()

        var cityId = crud.verifyIfCityExists(contentResolver,null
                ,"name = '"+ location + "' and language = '"+ language+"'"
                , null, null)

        forecast = crud.queryForecast(contentResolver, null, null, null, null, cityId)
        //TODO: after still have to get icons

//        if (forecast != null) {
//            Log.d("RESPONSE", "LOAD FROM CACHE")
//            setView()
//        } else {
//
//            if (application.isConnected && !isBatteryLow) {
//                Log.d("OnStart", "Network Available")
//                makeRequest()
//            } else {
//                Log.d("OnStart", "Network Not Available")
//                Toast.makeText(this, "OffLine", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun startServiceForDataRequest() {
        val intent = Intent(this, WeatherService::class.java)

        var receiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)
                forecast = resultData!!.getParcelable("forecast")

                startServiceForIconsRequest(0, forecast!!.list)

                stopService(intent)
            }
        }
        intent.putExtra("type", "forecast")
        intent.putExtra("receiver", receiver)
        intent.putExtra("location", location)
        intent.putExtra("language", language)

        startService(intent)
    }


    private fun startServiceForIconsRequest(count: Int, list: Array<FutureWeatherInfo>){
        if (count == NUMBER_OF_FORECAST_DAYS) {
            setView()
        }else{
            val futureWI = list[count]
            Log.d("OnCache", futureWI._date+ "\n")
            var icon: Bitmap? = iconCache.pop(futureWI._icon)
            if (icon != null) {
                futureWI.image = icon
                startServiceForIconsRequest(count+1, list)
            }else{
                val intent = Intent(this, IconService::class.java)

                var iconReceiver = object : ResultReceiver(Handler()) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        super.onReceiveResult(resultCode, resultData)
                        var image: Bitmap = resultData!!.getParcelable("icon")
                        futureWI.image = image
                        iconCache.push(futureWI._icon, image)
                        stopService(intent)

                        startServiceForIconsRequest(count+1, list)
                    }
                }
                intent.putExtra("iconReceiver", iconReceiver)
                intent.putExtra("icon", currentWeather!!.currentInfo._icon)

                startService(intent)
            }
        }
    }

    private fun setView() {
        listView.setAdapter(FutureWeatherInfoArrayAdapter(applicationContext, forecast?.list!!))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Log.d("RESPONSE", "CLICKING " + position)

        futureWeatherInfo = forecast!!.list[position]
        Log.d("RESPONSE", "HERE!")

        val intent = Intent(this, BasicWeatherInfoActivity::class.java)
        startActivity(intent)
    }
}

