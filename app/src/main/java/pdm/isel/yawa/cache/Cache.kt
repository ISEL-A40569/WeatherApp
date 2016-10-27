package pdm.isel.yawa.cache

import android.util.Log
import pdm.isel.yawa.language
import pdm.isel.yawa.model.CityInfo
import java.util.*

/**
 * Created by Dani on 24-10-2016.
 */
class Cache(val size: Int) {
    private val map = HashMap<String, CacheEntry>(size)
    private val list = LinkedList<String>();


    fun pop(key: String): CityInfo? {

        if (list.contains(key)) {
            if((System.currentTimeMillis() - map.get(key)?.timeStamp!!) > 3600000){
                list.remove(key)
                map.remove(key)
                return null
            }else{
                list.remove(key)
                list.addFirst(key)
                return map.get(key)?.cityInfo
            }
        }
        return null
    }

    fun push(cityInfo: CityInfo, type: String) {
        val key = cityInfo.cityName + language + type

        if (list.size == size) {
            map.remove(list.getLast());
            list.removeLast();
        }

        list.addFirst(key)
        map.put(key, CacheEntry(cityInfo, System.currentTimeMillis()))
    }

}

private data class CacheEntry(val cityInfo: CityInfo, val timeStamp: Long)