package pdm.isel.yawa.cache

import android.util.Log
import pdm.isel.yawa.language
import pdm.isel.yawa.model.CityInfo
import java.util.*

/**
 * Created by Dani on 24-10-2016.
 */
class Cache (size: Int){
    private val map = HashMap<String, CacheEntry> (size)

    //TODO LRU IMPLEMENTATION

    fun pop(key: String): CityInfo? {
        Log.d("RESPONSE", "POP, SIZE = " + map.size)
        return map.get(key)?.cityInfo
    }

    fun push(cityInfo: CityInfo, type: String) {
        Log.d("RESPONSE", cityInfo.cityName + language + type)
        map.put(cityInfo.cityName + language + type, CacheEntry(cityInfo, 1000))
    }
    
}

private data class CacheEntry(val cityInfo: CityInfo, val timeStamp: Long)