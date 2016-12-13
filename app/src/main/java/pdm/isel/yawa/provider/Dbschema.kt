package pdm.isel.yawa.provider

import android.provider.BaseColumns



object DbSchema {

    val DB_NAME = "yawa.db"
    val DB_VERSION = 10
    val COL_ID = BaseColumns._ID


    object City {
        val TBL_NAME = "city"
        val COL_NAME = "name"
        val COL_COUNTRY = "country"
        val COL_LON = "lon"
        val COL_LAT = "lat"
        val COL_LAN = "language"

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_NAME + " TEXT UNIQUE, " +
                        COL_COUNTRY + " TEXT, " +
                        COL_LON + " TEXT, " +
                        COL_LAT + " TEXT, " +
                        COL_LAN + " TEXT, " +
                        "CONSTRAINT un UNIQUE (name, country, language) " +
                        ");"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME +";"
    }



    object FutureWeatherInfo {
        //val COL_ID = BaseColumns._ID
        val TBL_NAME = "futureweatherinfo"
        val COL_CITY_ID = "forecastId"
        val COL_DATE = "date"
        val COL_PRESSURE = "pressure"
        val COL_HUM = "humidity"
        val COL_DESC = "description"
        val COL_TEMP_MIN = "tempMin"
        val COL_TEMP_MAX = "tempMax"

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                            COL_ID + " INTEGER, " +
                            COL_CITY_ID + " INTEGER , " +
                            COL_DATE + " TEXT, " +
                            COL_PRESSURE + " TEXT, " +
                            COL_HUM + " TEXT, " +
                            COL_DESC + " TEXT, " +
                            COL_TEMP_MIN + " TEXT, " +
                            COL_TEMP_MAX + " TEXT, " +
                            "PRIMARY KEY ("+COL_ID+", "+ COL_CITY_ID+"), " +
                            "FOREIGN KEY ("+COL_CITY_ID+") REFERENCES city("+ COL_ID +" ) " +
                            " );"
        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME + ";"
    }

    object CurrentWeatherInfo {
        //val COL_ID = BaseColumns._ID
        val TBL_NAME = "currentweatherinfo"
        val COL_CITY_ID = "currentid"
        val COL_DATE = "date"
        val COL_PRESS = "pressure"
        val COL_HUM = "humidity"
        val COL_DESCRIPTION = "description"
        val COL_TEMP = "temp"
        val COL_SUNRISE = "sunrise"
        val COL_SUNSET = "sunset"
        val COL_WINDSPEED = "windSpeed"


        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + " ( " +
                        COL_ID + " INTEGER, " +
                        COL_CITY_ID + " INTEGER, " +
                        COL_DATE + " TEXT, " +
                        COL_PRESS + " TEXT, " +
                        COL_HUM + " TEXT, " +
                        COL_DESCRIPTION + " TEXT, " +
                        COL_TEMP + " TEXT, " +
                        COL_SUNRISE + " TEXT, " +
                        COL_SUNSET + " TEXT, " +
                        COL_WINDSPEED + " TEXT, " +
                        "PRIMARY KEY ("+COL_ID+","+ COL_CITY_ID+"), " +
                        "FOREIGN KEY ("+ COL_CITY_ID+") REFERENCES city("+ COL_ID+") " +
                        ");"
        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME + ";"
    }


}