package pdm.isel.yawa.icons

import android.graphics.Bitmap
import android.util.Log
import java.util.*

/**
 * Icon Bitmaps cache.
 */
class IconCache {

    private val iconMap = HashMap<String, Bitmap>()

    fun pop(key: String): Bitmap? {
        Log.d("OnCache", "Pop key "+ key)
        Log.d("OnCache", "contains = "+ iconMap.containsKey(key).toString())
        if(iconMap.containsKey(key))
        return iconMap[key]

        return null
    }

    fun push(key: String, icon: Bitmap) {
        if(!iconMap.containsKey(key)){
            Log.d("OnCache", "Push key "+ key)
            iconMap.put(key, icon)
        }
    }

}