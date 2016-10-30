package pdm.isel.yawa.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import pdm.isel.yawa.GenericArrayAdapter
import pdm.isel.yawa.model.FutureWeatherInfo

/**
 * Created by Dani on 26-10-2016.
 */
class BasicWeatherInfoArrayAdapter(context: Context, futureWeatherInfos: Array<FutureWeatherInfo>) : GenericArrayAdapter<FutureWeatherInfo>(context, futureWeatherInfos) {

    override fun drawText(textView: TextView, futureWeatherInfo: FutureWeatherInfo) {
        textView.setTextColor(Color.BLUE)
        textView.setText(futureWeatherInfo.getDate() + ":\n" +
                Math.round(futureWeatherInfo.tempMin) + "ºC - " +
                Math.round(futureWeatherInfo.tempMax) + "ºC - " +
        futureWeatherInfo.description)
    }
}