package pdm.isel.yawa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

abstract class GenericArrayAdapter<T>(context: Context, objects: Array<T>) : ArrayAdapter<T>(context, 0, objects) {

    // Vars
    private var mInflater: LayoutInflater? = null

    init {
        init(context)
    }

    // Headers
    abstract fun drawText(textView: TextView, `object`: T)

    private fun init(context: Context) {
        this.mInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val vh: ViewHolder
        if (convertView == null) {
            convertView = mInflater!!.inflate(android.R.layout.simple_list_item_2, parent, false)
            vh = ViewHolder(convertView)
            convertView!!.setTag(vh)
        } else {
            vh = convertView!!.getTag() as ViewHolder
        }

        drawText(vh.textView, getItem(position))

        return convertView
    }

    internal class ViewHolder constructor(rootView: View) {

        var textView: TextView

        init {
            textView = rootView.findViewById(android.R.id.text1) as TextView
        }
    }
}