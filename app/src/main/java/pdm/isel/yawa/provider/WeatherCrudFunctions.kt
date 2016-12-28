package pdm.isel.yawa.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import pdm.isel.yawa.NUMBER_OF_FORECAST_DAYS
import pdm.isel.yawa.language
import pdm.isel.yawa.model.*
import java.util.*


class WeatherCrudFunctions {


    fun insertNewCity(cr: ContentResolver, cityInfo: CityInfo): Int {

        var contentValues = ContentValues()
        contentValues.put("name", cityInfo.cityName)
        contentValues.put("country", cityInfo.cityCountry)
        contentValues.put("lon", cityInfo.ln)
        contentValues.put("lat", cityInfo.lt)
        contentValues.put("language", language)


        var id = verifyIfCityExists(cr, null,
                "name = '" + cityInfo.cityName + "' and country = '" + cityInfo.cityCountry + "' and language = '" + language + "'",
                null, null)

        if (id > 0)
            return id

        cr.insert(WeatherContract.City.CONTENT_URI, contentValues)

        id = verifyIfCityExists(cr, null,
                "name = '" + cityInfo.cityName + "' and country = '" + cityInfo.cityCountry + "' and language = '" + language + "'",
                null, null)
        return id
    }

    fun insertCurrentWeatherInfo(cr: ContentResolver, cwi: CurrentWeatherInfo, city_id: Int) {

        var contentValues = ContentValues()

        contentValues.put("date", cwi.date)
        contentValues.put("pressure", cwi.pressure)
        contentValues.put("humidity", cwi.humidity)
        //contentValues.put("language", language)
        contentValues.put("description", cwi.description)
        contentValues.put("temp", cwi.temp)
        contentValues.put("sunrise", cwi.sunrise)
        contentValues.put("sunset", cwi.sunset)
        contentValues.put("windSpeed", cwi.windSpeed)
        contentValues.put("currentid", city_id)

        cr.insert(WeatherContract.CurrentWeatherInfo.CONTENT_URI, contentValues)

    }

    fun insertFutureWeatherInfo(cr: ContentResolver, fwi: FutureWeatherInfo, forecastid: Int) {
        var contentValues = ContentValues()

        contentValues.put("date", fwi.date)
        contentValues.put("pressure", fwi.pressure)
        contentValues.put("humidity", fwi.humidity)
        //contentValues.put("language", language)
        contentValues.put("description", fwi.description)
        contentValues.put("forecastId", forecastid)
        contentValues.put("tempMin", fwi.tempMin)
        contentValues.put("tempMax", fwi.tempMax)

        cr.insert(WeatherContract.FutureWeatherInfo.CONTENT_URI, contentValues)
    }


    fun deleteCity(cr: ContentResolver, selection: String?, selectionArgs: Array<out String>?): Int {

        var del = cr.delete(WeatherContract.City.CONTENT_URI, selection, selectionArgs)
        return del
    }

    fun deleteCurrentWeatherInfo(cr: ContentResolver, selection: String?, selectionArgs: Array<out String>?): Int {

        var del = cr.delete(WeatherContract.CurrentWeatherInfo.CONTENT_URI, selection, selectionArgs)
        return del
    }

    fun deleteFutureWeatherInfo(cr: ContentResolver, selection: String?, selectionArgs: Array<out String>?): Int {

        var del = cr.delete(WeatherContract.FutureWeatherInfo.CONTENT_URI, selection, selectionArgs)
        return del
    }

    /*
    fun updateCity(cr:ContentResolver,ci:CityInfo, selection: String?, selectionArgs: Array<out String>?) : Int{
        //TODO not necessary at this point
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
    */

    fun verifyIfCityExists(cr: ContentResolver
                           , projection: Array<out String>?
                           , selection: String?
                           , selectionArgs: Array<out String>?
                           , sortOrder: String?): Int {

        var cursor = cr.query(WeatherContract.City.CONTENT_URI, projection, selection, selectionArgs, sortOrder)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    /**
     * returns a list of all citys in the DB
     */
    fun getListOfAllCitys(cr: ContentResolver
                          , projection: Array<out String>?
                          , selection: String?
                          , selectionArgs: Array<out String>?
                          , sortOrder: String?): LinkedList<String> {

        var cursor = cr.query(WeatherContract.City.CONTENT_URI, projection, selection, selectionArgs, sortOrder) as Cursor

        if (cursor.count == 0) {
            Log.d("YAWA_TAG", "WeatherCrudFunctions - NULL CURSOR")
            return LinkedList<String>()
        }

        var list = LinkedList<String>()

        while (cursor.moveToNext()) {
            list.add(cursor.getString(1))
        }
        return list
    }


    fun queryCurrent(cr: ContentResolver, projection: Array<out String>?
                     , selection: String?
                     , selectionArgs: Array<out String>?
                     , sortOrder: String?
                     , id: Int): Current?? {

        var cursorwi = cr.query(WeatherContract.CurrentWeatherInfo.CONTENT_URI, null, "currentid = " + id, selectionArgs, "_id")
        var cursorCity = cr.query(WeatherContract.City.CONTENT_URI, projection, "_id = " + id, selectionArgs, sortOrder)

        if (cursorCity.count == 0 || cursorwi.count == 0) {
            Log.d("YAWA_TAG", "cursorCity.count = " + cursorCity.count.toString())
            Log.d("YAWA_TAG", "cursorwi.count = " + cursorwi.count.toString())

            Log.d("YAWA_TAG", "WeatherCrudFunctions - NULL CURSOR")
            return null
        }

        var currInfo = CurrentWeatherInfo(cursorwi.getString(2)
                , cursorwi.getString(3)
                , cursorwi.getString(4)
                , cursorwi.getString(5)
                , cursorwi.getString(6)
                , cursorwi.getString(7)
                , cursorwi.getString(8)
                , cursorwi.getString(9)
                , cursorwi.getString(10)
        )

        var curr = Current(cursorCity.getString(2)
                , cursorCity.getString(3)
                , cursorCity.getString(4)
                , cursorCity.getString(5)
                , currInfo)

        Log.d("DB_DEBUG", curr.name)
        Log.d("DB_DEBUG", curr.country)
        Log.d("DB_DEBUG", curr.language)

        return curr
    }

    fun queryForecast(cr: ContentResolver, projection: Array<out String>?, selection: String?
                      , selectionArgs: Array<out String>?, sortOrder: String?, id: Int): Forecast?? {

        var cursorfwi = cr.query(WeatherContract.FutureWeatherInfo.CONTENT_URI, null, "forecastId = " + id, selectionArgs, "_id")
        var cursorCity = cr.query(WeatherContract.City.CONTENT_URI, projection, "_id = " + id, selectionArgs, sortOrder)

        if (cursorCity.count == 0 || cursorfwi.count == 0) {
            Log.d("YAWA_TAG", "WeatherCrudFunctions - NULL CURSOR")
            return null
        }

        var arrayOfFutureWeatherInfo = kotlin.arrayOfNulls<FutureWeatherInfo>(NUMBER_OF_FORECAST_DAYS) as Array<FutureWeatherInfo>

        var index = 0
        while (cursorfwi.moveToNext()) {
            var futureAux = FutureWeatherInfo(
                    cursorfwi.getString(4)
                    , cursorfwi.getString(5)
                    , cursorfwi.getString(6)
                    , cursorfwi.getString(7)
                    , cursorfwi.getString(8)
                    , cursorfwi.getString(9)
                    , cursorfwi.getString(10)
            )
            arrayOfFutureWeatherInfo[index++]
        }
        var fwi = Forecast(cursorCity.getString(2)
                , cursorCity.getString(3)
                , cursorCity.getString(4)
                , cursorCity.getString(5)
                , arrayOfFutureWeatherInfo)

        return fwi
    }

}






