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
        Log.d("OnCache", "Pop")
        return iconMap.get(key)
    }

    fun push(key: String, icon: Bitmap) {
        Log.d("OnCache", "Push")
        iconMap.put(key, icon)
    }

}