package pdm.isel.yawa.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        deleteDb(db)
        createDb(db)
    }

    private fun createDb(db: SQLiteDatabase?) {
        db!!.execSQL(DbSchema.Forecast.DDL_CREATE_TABLE)
        db!!.execSQL(DbSchema.Current.DDL_CREATE_TABLE)
        db!!.execSQL(DbSchema.FutureWeatherInfo.DDL_CREATE_TABLE)
        db!!.execSQL(DbSchema.CurrentWeatherInfo.DDL_CREATE_TABLE)
    }

    private fun deleteDb(db: SQLiteDatabase?) {
        db!!.execSQL(DbSchema.FutureWeatherInfo.DDL_DROP_TABLE)
        db!!.execSQL(DbSchema.CurrentWeatherInfo.DDL_DROP_TABLE)
        db!!.execSQL(DbSchema.Forecast.DDL_DROP_TABLE)
        db!!.execSQL(DbSchema.Current.DDL_DROP_TABLE)
    }
}