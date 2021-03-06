package pdm.isel.yawa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pdm.isel.yawa.R
import pdm.isel.yawa.model.FutureWeatherInfo

/**
 * Class used as an ArrayAdapter for FutureWeatherInfo instances.
 */
class FutureWeatherInfoArrayAdapter(context: Context, val futureWeatherInfos: Array<FutureWeatherInfo>) : ArrayAdapter<FutureWeatherInfo>(context, 0, futureWeatherInfos) {

    val inflater = LayoutInflater.from(getContext())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val vh: ViewHolder
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.list_entry_layout, parent, false)
            vh = ViewHolder(convertView!!)
            convertView.tag = vh
        } else {
            vh = convertView.tag as ViewHolder
        }

        val fwi: FutureWeatherInfo = getItem(position)
        vh.textView!!.text = fwi.date
        vh.imageView!!.setImageBitmap(fwi.image!!)

        return convertView
    }

    internal class ViewHolder constructor(rootView: View) {

        var textView: TextView?
        var imageView: ImageView?

        init {
            textView = rootView.findViewById(R.id.item_date_view) as TextView?
            imageView = rootView.findViewById(R.id.item_icon_view) as ImageView?
        }
    }
}
