package pdm.isel.yawa.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.BaseColumns
import pdm.isel.yawa.NUMBER_OF_FORECAST_DAYS
import pdm.isel.yawa.model.*

/**
 * Created by Dani on 29-12-2016.
 */
class WeatherDatabaseApi(private val contentResolver: ContentResolver) {

    fun insert(current: Current) {
        val cid = verifyIfExists(current)

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

    fun getCurrent(location: String, language: String, country: String): Current {
        var cursor = contentResolver.query(
                WeatherContract.City.CONTENT_URI,
                null,
                "name = '$location' and country = '$country' and language = '$language'",
                null,
                null
        )

        cursor.moveToFirst()

        return Current(cursor.getString(2)
                , cursor.getString(3)
                , cursor.getString(4)
                , cursor.getString(5)
                , getCurrentWeatherInfo(cursor.getInt(1)))

    }

    fun getForecast(location: String, language: String, country: String): Forecast {
        var cursor = contentResolver.query(
                WeatherContract.City.CONTENT_URI,
                null,
                "name = '$location' and country = '$country' and language = '$language'",
                null,
                null
        )

        cursor.moveToNext()

        return Forecast(cursor.getString(2)
                , cursor.getString(3)
                , cursor.getString(4)
                , cursor.getString(5)
                , getFutureWeatherInfos(cursor.getInt(1)))
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

        return CurrentWeatherInfo(cursorWi.getString(2)
                , cursorWi.getString(3)
                , cursorWi.getString(4)
                , cursorWi.getString(5)
                , cursorWi.getString(6)
                , cursorWi.getString(7)
                , cursorWi.getString(8)
                , cursorWi.getString(9)
                , cursorWi.getString(10)
        )
    }

    private fun getFutureWeatherInfos(fid: Int): Array<FutureWeatherInfo> {
        var cursorfwi = contentResolver.query(
                WeatherContract.FutureWeatherInfo.CONTENT_URI,
                null,
                "forecastId = " + fid.toString(),
                null,
                null
        )

        var arrayOfFutureWeatherInfo = kotlin.arrayOfNulls<FutureWeatherInfo>(NUMBER_OF_FORECAST_DAYS) as Array<FutureWeatherInfo>
        var index = 0
        while (cursorfwi.moveToNext()) {
            var futureAux = FutureWeatherInfo(
                    cursorfwi.getString(2)
                    , cursorfwi.getString(3)
                    , cursorfwi.getString(4)
                    , cursorfwi.getString(5)
                    , cursorfwi.getString(6)
                    , cursorfwi.getString(7)
                    , cursorfwi.getString(8)
            )
            arrayOfFutureWeatherInfo[index++] = futureAux
        }

        return arrayOfFutureWeatherInfo
    }

    private fun verifyIfExists(cityInfo: CityInfo): Int {
        var cursor = contentResolver.query(WeatherContract.City.CONTENT_URI,
                null,
                "name = '" + cityInfo.cityName + "' and country = '" + cityInfo.cityCountry + "' and language = '" + cityInfo.language + "'",
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    private fun verifyIfExists(cwi: CurrentWeatherInfo, cid: Int): Int {
        var cursor = contentResolver.query(WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                null,
                "date = '" + cwi.date + "' and currentid = " + cid.toString(),
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    private fun verifyIfExists(fwi: FutureWeatherInfo, fid: Int): Int {
        var cursor = contentResolver.query(WeatherContract.FutureWeatherInfo.CONTENT_URI,
                null,
                "date = '" + fwi.date + "' and forecastId = " + fid.toString(),
                null,
                null)

        if (cursor.count == 0)
            return -1

        cursor.moveToFirst()
        return cursor.getInt(0)
    }


    private fun updateCurrentInfo(contentValues: ContentValues, id: Int, cid: Int) {
        contentResolver.update(WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                contentValues,
                BaseColumns._ID + " = ? and currentid = ?",
                arrayOf(id.toString(), cid.toString())
        )
    }

    private fun updateFutureInfo(contentValues: ContentValues, id: Int, fid: Int) {
        contentResolver.update(WeatherContract.FutureWeatherInfo.CONTENT_URI,
                contentValues,
                BaseColumns._ID + " = ? and forecastId = ?",
                arrayOf(id.toString(), fid.toString())
        )
    }
}