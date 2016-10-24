package pdm.isel.yawa.cache

import pdm.isel.yawa.LANGUAGE
import pdm.isel.yawa.model.CityInfo
import java.util.*

/**
 * Created by Dani on 24-10-2016.
 */
class Cache (size: Int){
    val map = HashMap<String, CityInfo> (size)

    public fun pop(key: String): CityInfo? {
        return map.get(key)
    }

    public fun push(cityInfo: CityInfo) {
        map.put(cityInfo.cityName + LANGUAGE, cityInfo)
    }
    
}