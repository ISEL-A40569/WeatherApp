package pdm.isel.yawa.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.BaseColumns
import android.util.Log
import pdm.isel.yawa.NUMBER_OF_FORECAST_DAYS
import pdm.isel.yawa.model.*
import java.util.*

/**
 * Class used to provide public access to DataBase.
 */
class WeatherDatabaseApi(private val contentResolver: ContentResolver) {

    fun insert(current: Current) {
        val cid = verifyIfExists(current)

        Log.d("WeatherDatabaseApi", "current exists = " + cid)

        if (cid < 0) {
            val contentValues = ContentValues()
            contentValues.put("name", current.cityName)
            contentValues.put("country", current.cityCountry)
            contentValues.put("lon", current.ln)
            contentValues.put("lat", current.lt)
            contentValues.put("language", current.language)

            contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)
        }

        insert(current.currentInfo, cid)
    }

    fun insert(forecast: Forecast) {
        val id = verifyIfExists(forecast)
        Log.d("WeatherDatabaseApi", "forecast exists = " + id)

        if (id < 0) {
            val contentValues = ContentValues()
            contentValues.put("name", forecast.cityName)
            contentValues.put("country", forecast.cityCountry)
            contentValues.put("lon", forecast.ln)
            contentValues.put("lat", forecast.lt)
            contentValues.put("language", forecast.language)

            contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)
        }

        forecast.list.forEach {
            fwi ->
            insert(fwi, id)
        }
    }

    fun getCurrent(location: String, language: String): Current {
        val cursor = contentResolver.query(
                WeatherContract.City.CONTENT_URI,
                null,
                "name = '$location' and language = '$language'",
                null,
                null
        )

        Log.d("GetCurrent", "$location $language")

        cursor.moveToNext()

        val current = Current(cursor.getString(1)
                , cursor.getString(2)
                , cursor.getString(3)
                , cursor.getString(4)
                , getCurrentWeatherInfo(cursor.getInt(0)))

        cursor.close()

        return current

    }

    fun getForecast(location: String, language: String): Forecast {
        val cursor = contentResolver.query(
                WeatherContract.City.CONTENT_URI,
                null,
                "name = '$location' and language = '$language'",
                null,
                null
        )

        cursor.moveToNext()

        val forecast = Forecast(cursor.getString(1)
                , cursor.getString(2)
                , cursor.getString(3)
                , cursor.getString(4)
                , getFutureWeatherInfos(cursor.getInt(0)))

        cursor.close()

        return forecast
    }

    private fun insert(cwi: CurrentWeatherInfo, cid: Int) {
        val contentValues = ContentValues()
        contentValues.put("date", cwi.date)
        contentValues.put("pressure", cwi.pressure)
        contentValues.put("humidity", cwi.humidity)
        contentValues.put("description", cwi.description)
        contentValues.put("temp", cwi.temp)
        contentValues.put("sunrise", cwi.sunrise)
        contentValues.put("sunset", cwi.sunset)
        contentValues.put("windSpeed", cwi.windSpeed)
        contentValues.put("icon", cwi.icon)
        contentValues.put("currentid", cid)

        val id = verifyIfExists(cwi, cid)

        Log.d("WeatherDatabaseApi", "currentWI exists = " + id)

        if (id < 0) {
            contentResolver.insert(WeatherContract.CurrentWeatherInfo.CONTENT_URI, contentValues)
        } else {
            updateCurrentInfo(contentValues, id, cid)
        }
    }

    private fun insert(fwi: FutureWeatherInfo, fid: Int) {

        val contentValues = ContentValues()
        contentValues.put("date", fwi.date)
        contentValues.put("pressure", fwi.pressure)
        contentValues.put("humidity", fwi.humidity)
        contentValues.put("description", fwi.description)
        contentValues.put("forecastId", fid)
        contentValues.put("tempMin", fwi.tempMin)
        contentValues.put("tempMax", fwi.tempMax)
        contentValues.put("icon", fwi.icon)

        val id = verifyIfExists(fwi, fid)

        if (id < 0) {
            contentResolver.insert(WeatherContract.FutureWeatherInfo.CONTENT_URI, contentValues)
        } else {
            updateFutureInfo(contentValues, id, fid)
        }
    }

    private fun getCurrentWeatherInfo(cid: Int): CurrentWeatherInfo {
        val cursorWi = contentResolver.query(
                WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                null,
                "currentid = " + cid.toString(),
                null,
                null
        )

        cursorWi.moveToNext()

        val cwi = CurrentWeatherInfo(cursorWi.getString(2)
                , cursorWi.getString(3)
                , cursorWi.getString(4)
                , cursorWi.getString(5)
                , cursorWi.getString(6)
                , cursorWi.getString(7)
                , cursorWi.getString(8)
                , cursorWi.getString(9)
                , cursorWi.getString(10)
        )

        cursorWi.close()

        return cwi
    }

    private fun getFutureWeatherInfos(fid: Int): Array<FutureWeatherInfo> {
        val cursorFwi = contentResolver.query(
                WeatherContract.FutureWeatherInfo.CONTENT_URI,
                null,
                "forecastId = " + fid.toString(),
                null,
                null
        )

        val arrayOfFutureWeatherInfo = kotlin.arrayOfNulls<FutureWeatherInfo>(NUMBER_OF_FORECAST_DAYS) as Array<FutureWeatherInfo>
        var index = 0
        while (cursorFwi.moveToNext()) {
            val futureAux = FutureWeatherInfo(
                    cursorFwi.getString(2)
                    , cursorFwi.getString(3)
                    , cursorFwi.getString(4)
                    , cursorFwi.getString(5)
                    , cursorFwi.getString(6)
                    , cursorFwi.getString(7)
                    , cursorFwi.getString(8)
            )
            arrayOfFutureWeatherInfo[index++] = futureAux
        }

        cursorFwi.close()

        return arrayOfFutureWeatherInfo
    }

    private fun verifyIfExists(cityInfo: CityInfo): Int {
        val cursor = contentResolver.query(WeatherContract.City.CONTENT_URI,
                null,
                "name = '" + cityInfo.cityName + "'",
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()

        val aux = cursor.getInt(0)

        cursor.close()

        return aux
    }

    private fun verifyIfExists(cwi: CurrentWeatherInfo, cid: Int): Int {
        val cursor = contentResolver.query(WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                null,
                "date = '${cwi.date}' and currentid = $cid",
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        val aux = cursor.getInt(0)

        cursor.close()

        return aux
    }

    private fun verifyIfExists(fwi: FutureWeatherInfo, fid: Int): Int {
        val cursor = contentResolver.query(WeatherContract.FutureWeatherInfo.CONTENT_URI,
                null,
                "date = '${fwi.date}' and forecastId = $fid",
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        val aux = cursor.getInt(0)

        cursor.close()

        return aux
    }


    private fun updateCurrentInfo(contentValues: ContentValues, id: Int, cid: Int) {
        contentResolver.update(WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                contentValues,
                "${BaseColumns._ID} = ? and currentid = ?",
                arrayOf(id.toString(), cid.toString())
        )
    }

    private fun updateFutureInfo(contentValues: ContentValues, id: Int, fid: Int) {
        contentResolver.update(WeatherContract.FutureWeatherInfo.CONTENT_URI,
                contentValues,
                "${BaseColumns._ID} = ? and forecastId = ?",
                arrayOf(id.toString(), fid.toString())
        )
    }

    /*
     * returns a list of all cities in the DB
     */
    fun getListOfAllCities(cr: ContentResolver): LinkedList<String> {

        val cursor = cr.query(WeatherContract.City.CONTENT_URI, null, null, null, "'name' ASC")

        if (cursor.count == 0) {
            Log.d("YAWA_TAG", "WeatherCrudFunctions - NULL CURSOR")
            return LinkedList()
        }

        val list = LinkedList<String>()

        while (cursor.moveToNext()) {
            list.add(cursor.getString(1))
        }

        cursor.close()

        return list
    }
}