package pdm.isel.yawa.icons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream


/**
 * Icon Bitmap files cache.
 */
class IconCache(ctx: Context) {

    //private val iconMap = HashMap<String, Bitmap>()
    private val context: Context

    init {
        context = ctx
    }

    fun pop(key: String): Bitmap? {
//        Log.d("OnCache", "Pop key "+ key)
//        Log.d("OnCache", "contains = "+ iconMap.containsKey(key).toString())
//        if(iconMap.containsKey(key))
//        return iconMap[key]

        val file = File(context.filesDir, key + ".bmp")
        Log.d("OnCache", "file exists " + file.exists().toString())
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.path)
        }

        return null
    }

    fun push(key: String, icon: Bitmap) {
//        if(!iconMap.containsKey(key)){
//            Log.d("OnCache", "Push key "+ key)
//            iconMap.put(key, icon)
//        }

        Log.d("OnCache", "files dir  " + context.filesDir)
        Log.d("OnCache", "file name  $key.bmp")

        val file = File(context.filesDir, key + ".bmp")

        if (!file.exists()) {
            val outputStream = FileOutputStream(file)
            icon.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        }
        Log.d("OnCache", "file exists " + file.exists().toString())

    }

}