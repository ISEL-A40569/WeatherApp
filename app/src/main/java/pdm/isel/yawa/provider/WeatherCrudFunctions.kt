package pdm.isel.yawa.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import pdm.isel.yawa.model.CityInfo
import pdm.isel.yawa.model.CurrentWeatherInfo
import pdm.isel.yawa.model.FutureWeatherInfo


class WeatherCrudFunctions  {


    fun insertNewCity(cr:ContentResolver, cityInfo: CityInfo){

    }

    fun insertCurrentWeatherInfo(cr:ContentResolver, cwi: CurrentWeatherInfo){
        //TODO
    }
    fun insertFutureWeatherInfo(cr:ContentResolver, fwi: FutureWeatherInfo){
        //TODO
    }


    fun deleteCity(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{

        var del = cr.delete(WeatherContract.City.CONTENT_URI, selection, selectionArgs)
        return del
    }

    fun deleteCurrentWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO
        return 0
    }

    fun deleteFutureWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO
        return 0
    }

    fun updateCity(cr:ContentResolver,ci:CityInfo, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO
        return 0
    }
    fun updateCurrenteWeatherInfo(cr:ContentResolver,cwi: CurrentWeatherInfo, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO
        return 0
    }
    fun updateFutureWeatherInfo(cr:ContentResolver, fwi: FutureWeatherInfo, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO
        return 0
    }

    fun queryCity(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?):CityInfo??{return null}
    fun queryCurrentWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?):CurrentWeatherInfo??{return null}
    fun queryFutureWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?):FutureWeatherInfo??{return null}

}
