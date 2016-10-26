package pdm.isel.yawa.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import pdm.isel.yawa.GenericArrayAdapter
import pdm.isel.yawa.model.BasicWeatherInfo
import java.util.*

/**
 * Created by Dani on 26-10-2016.
 */
class BasicWeatherInfoArrayAdapter(context: Context, basicWeatherInfos: Array<BasicWeatherInfo>) : GenericArrayAdapter<BasicWeatherInfo>(context, basicWeatherInfos) {

    override fun drawText(textView: TextView, basicWeatherInfo: BasicWeatherInfo) {
        textView.setTextColor(Color.BLUE)
        textView.setText(basicWeatherInfo.dateTime)
    }
}