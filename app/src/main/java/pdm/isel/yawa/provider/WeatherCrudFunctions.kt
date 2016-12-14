package pdm.isel.yawa.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.BaseColumns
import pdm.isel.yawa.model.*


class WeatherCrudFunctions  {


    fun insertNewCity(cr:ContentResolver, cityInfo: CityInfo){

        var contentValues = ContentValues()
        contentValues.put("name", cityInfo.cityName)
        contentValues.put("country", cityInfo.cityCountry)
        contentValues.put("lon", cityInfo.ln)
        contentValues.put("lat", cityInfo.lt)
        contentValues.put("language", cityInfo.language)
        cr.insert(WeatherContract.City.CONTENT_URI, contentValues)
    }

    fun insertCurrentWeatherInfo(cr:ContentResolver, cwi: CurrentWeatherInfo, city_id: Int, language:String){

        var contentValues = ContentValues()

        contentValues.put("date", cwi.date)
        contentValues.put("pressure", cwi.pressure)
        contentValues.put("humidity", cwi.humidity)
        contentValues.put("language", language)
        contentValues.put("description", cwi.description)
        contentValues.put("temp", cwi.temp)
        contentValues.put("sunrise", cwi.sunrise)
        contentValues.put("sunset", cwi.sunset)
        contentValues.put("windSpeed", cwi.windSpeed)
        contentValues.put(BaseColumns._ID, city_id)

        cr.insert(WeatherContract.CurrentWeatherInfo.CONTENT_URI, contentValues)

    }
    fun insertFutureWeatherInfo(cr:ContentResolver, fwi: FutureWeatherInfo, forecastid: Int, language:String){
        var contentValues = ContentValues()

        contentValues.put("date", fwi.date)
        contentValues.put("pressure", fwi.pressure)
        contentValues.put("humidity", fwi.humidity)
        contentValues.put("language", language)
        contentValues.put("description", fwi.description)
        contentValues.put("forecastId", forecastid)
        contentValues.put("tempMin", fwi.tempMin)
        contentValues.put("tempMax", fwi.tempMax)

        cr.insert(WeatherContract.FutureWeatherInfo.CONTENT_URI, contentValues)
    }


    fun deleteCity(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{

        var del = cr.delete(WeatherContract.City.CONTENT_URI, selection, selectionArgs)
        return del
    }

    fun deleteCurrentWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{

        var del = cr.delete(WeatherContract.CurrentWeatherInfo.CONTENT_URI, selection, selectionArgs)
        return del
    }

    fun deleteFutureWeatherInfo(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?) : Int{

        var del = cr.delete(WeatherContract.FutureWeatherInfo.CONTENT_URI, selection, selectionArgs)
        return del
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

    //TODO: CitiInfo is an abstract class, only its implementations (Current and Forecast) should be returned
    //TODO: fun getCurrent(cr:ContentResolver, location: String, language: String): Current? {...}
    // -> queryCurrent. ## location e language colocados na clausula selection
    //TODO: fun getForecast(cr:ContentResolver, location: String, language: String): Forecast? {...}
    // -> queryForecast

    fun queryCity(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?):CityInfo??{return null}

    /**
     * decidir melhor este método. precisa de mais dados nos parametros
     */
    fun queryCurrent(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?): Current??{

        //val cursorwi = cr.query(WeatherContract.CurrentWeatherInfo.CONTENT_URI,null,selection, selectionArgs, "_id")
        //val cursorCity = cr.query(WeatherContract.City.CONTENT_URI, null, )
        //TODO
        return null
    }
    fun queryForecast(cr:ContentResolver, selection: String?, selectionArgs: Array<out String>?):Forecast??{return null}

}
