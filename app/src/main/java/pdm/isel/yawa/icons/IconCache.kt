package pdm.isel.yawa.icons

import android.graphics.Bitmap
import android.util.Log
import java.util.HashMap

/**
 * Icon Bitmaps cache.
 */
class IconCache {

    private val iconMap = HashMap<String, Bitmap>()

    fun pop(key: String): Bitmap? {
        Log.d("OnCache", "Pop key "+ key)
        Log.d("OnCache", "contains = "+ iconMap.containsKey(key).toString())

        return iconMap.get(key)
    }

    fun push(key: String, icon: Bitmap) {
        Log.d("OnCache", "contains = "+ iconMap.containsKey(key).toString())
        if(!iconMap.containsKey(key)){
            Log.d("OnCache", "Pushing key "+ key)
            iconMap.put(key, icon)
        }
    }

}