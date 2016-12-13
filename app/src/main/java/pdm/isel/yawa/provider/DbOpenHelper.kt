package pdm.isel.yawa.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("YAWA_TAG", "DbOpenHelper - onCreate method")
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("YAWA_TAG", "DbOpenHelper - onUpgrade method")
        deleteDb(db)
        createDb(db)
    }

    private fun createDb(db: SQLiteDatabase?) {
        Log.d("YAWA_TAG", "DbOpenHelper - createDb method")
        db!!.execSQL(DbSchema.City.DDL_CREATE_TABLE)
        db!!.execSQL(DbSchema.FutureWeatherInfo.DDL_CREATE_TABLE)
        db!!.execSQL(DbSchema.CurrentWeatherInfo.DDL_CREATE_TABLE)
    }

    private fun deleteDb(db: SQLiteDatabase?) {
        Log.d("YAWA_TAG", "DbOpenHelper - deleteDb method")
        db!!.execSQL(DbSchema.FutureWeatherInfo.DDL_DROP_TABLE)
        db!!.execSQL(DbSchema.CurrentWeatherInfo.DDL_DROP_TABLE)
        db!!.execSQL(DbSchema.City.DDL_DROP_TABLE)
    }
}