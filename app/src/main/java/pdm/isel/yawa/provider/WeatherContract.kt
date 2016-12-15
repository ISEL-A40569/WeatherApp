package pdm.isel.yawa.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns


object WeatherContract{


    val AUTHORITY = "pdm.isel.yawa.provider"

    val CONTENT_URI = Uri.parse("content://" + AUTHORITY)

    val MEDIA_BASE_SUBTYPE = "/vnd.weather."

    object City : BaseColumns{
        val RESOURCE = "city"

        val CONTENT_URI = Uri.withAppendedPath(WeatherContract.CONTENT_URI, RESOURCE)
        val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE       //para listas
        val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE //para items

        val NAME = "name"
        val COUNTRY = "country"
        val LON = "lon"
        val LAT = "lat"
        val LANGUAGE = "language"

        val SELECT_ALL = arrayOf(BaseColumns._ID, NAME, COUNTRY, LON, LAT, LANGUAGE)
        val DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC"
        val SELECT_TEST = arrayOf(BaseColumns._ID, "name", "language")
    }

    object FutureWeatherInfo : BaseColumns{
        val RESOURCE = "futureweatherinfo"

        val CONTENT_URI = Uri.withAppendedPath(WeatherContract.CONTENT_URI, RESOURCE)
        val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE       //para listas
        val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE       //para items

        val FORECAST_ID = "forecastId"
        val DATE = "date"
        val PRESSURE = "pressure"
        val HUM = "humidity"
        val DESC = "description"
        val ICON = "icon"
        val TEMP_MIN = "tempMin"
        val TEMP_MAX = "tempMax"

        val SELECT_ALL = arrayOf(BaseColumns._ID, FORECAST_ID, DATE, PRESSURE, HUM, DESC, ICON, TEMP_MIN, TEMP_MAX)
        val DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC"
    }

    object CurrentWeatherInfo : BaseColumns{
        val RESOURCE = "currentweatherinfo"
        val CONTENT_URI = Uri.withAppendedPath(WeatherContract.CONTENT_URI, RESOURCE)
        val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE       //para listas
        val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE       //para items

        val CURR_ID = "currentid"
        val DATE = "date"
        val PRESS = "pressure"
        val HUM = "humidity"
        val DESCRIPTION = "description"
        val ICON = "icon"
        val TEMP = "temp"
        val SUNRISE = "sunrise"
        val SUNSET = "sunset"
        val WINDSPEED = "windSpeed"


        val SELECT_ALL = arrayOf(BaseColumns._ID, CURR_ID, DATE, PRESS, HUM, DESCRIPTION, ICON, TEMP, SUNRISE, SUNSET, WINDSPEED)
        val DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC"
        val SELECT_TEST = arrayOf(BaseColumns._ID, CURR_ID, DESCRIPTION)
    }
}
