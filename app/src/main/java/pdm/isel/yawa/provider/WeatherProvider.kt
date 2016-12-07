package pdm.isel.yawa.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import android.util.Log


class WeatherProvider : ContentProvider() {


    private val CURRENT_LST = 1
    private val CURRENT_OBJ = 2
    private val FORECAST_LST = 3
    private val FORECAST_OBJ = 4
    private val CURRENT_WI_LST = 5
    private val CURRENT_WI_OBJ = 6
    private val FUTURE_WI_LST = 7
    private val FUTURE_WI_OBJ = 8
    //TODO potencialmente aqui estará um erro
    // um LST e um OBJ para cada objecto

    private val URI_MATCHER: UriMatcher

    init {
        URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.Current.RESOURCE,
                CURRENT_LST)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.Current.RESOURCE + "/#",
                CURRENT_OBJ)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.Forecast.RESOURCE,
                FORECAST_LST)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.Forecast.RESOURCE + "/#",
                FORECAST_OBJ)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.CurrentWeatherInfo.RESOURCE,
                CURRENT_WI_LST)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.CurrentWeatherInfo.RESOURCE + "/#",
                CURRENT_WI_OBJ)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.FutureWeatherInfo.RESOURCE,
                FUTURE_WI_LST)
        URI_MATCHER.addURI(
                WeatherContract.AUTHORITY,
                WeatherContract.FutureWeatherInfo.RESOURCE + "/#",
                FUTURE_WI_OBJ)
    }

    private var dbHelper: DbOpenHelper? = null

    override fun onCreate(): Boolean {
        Log.d("YAWA_TAG", "WeatherProvider - onCreate")
        dbHelper = DbOpenHelper(context)
        return true
    }


    override fun getType(uri: Uri?): String {
        Log.d("YAWA_TAG", "WeatherProvider - getType")
        when (URI_MATCHER.match(uri)) {
            CURRENT_LST -> return WeatherContract.Current.CONTENT_TYPE
            CURRENT_OBJ -> return WeatherContract.Current.CONTENT_ITEM_TYPE
            FORECAST_LST  -> return WeatherContract.Forecast.CONTENT_TYPE
            FORECAST_OBJ -> return WeatherContract.Forecast.CONTENT_ITEM_TYPE
            CURRENT_WI_LST -> return WeatherContract.CurrentWeatherInfo.CONTENT_TYPE
            CURRENT_WI_OBJ -> return WeatherContract.CurrentWeatherInfo.CONTENT_ITEM_TYPE
            FUTURE_WI_LST -> return WeatherContract.FutureWeatherInfo.CONTENT_TYPE
            FUTURE_WI_OBJ -> return WeatherContract.FutureWeatherInfo.CONTENT_ITEM_TYPE
            else -> throw badUri(uri!!)
        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        Log.d("YAWA_TAG", "WeatherProvider - query method")
        var sortOrder = sortOrder
        val qbuilder = SQLiteQueryBuilder()
        when (URI_MATCHER.match(uri)) {

            // TODO tenho a variavel uri com !! não sei pq tive de colocar
            //CURRENT
            CURRENT_LST -> {
                qbuilder.tables = DbSchema.Current.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WeatherContract.Current.DEFAULT_SORT_ORDER
                }
            }
            CURRENT_OBJ -> {
                qbuilder.tables = DbSchema.Current.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }
            //FORECASTE
            FORECAST_LST -> {
                qbuilder.tables = DbSchema.Forecast.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WeatherContract.Forecast.DEFAULT_SORT_ORDER
                }
            }
            FORECAST_OBJ -> {
                qbuilder.tables = DbSchema.Forecast.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }

            //CURRENTE WEATHER INFO
            CURRENT_WI_LST -> {
                qbuilder.tables = DbSchema.CurrentWeatherInfo.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WeatherContract.CurrentWeatherInfo.DEFAULT_SORT_ORDER
                }
            }
            CURRENT_WI_OBJ -> {
                qbuilder.tables = DbSchema.CurrentWeatherInfo.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }

            //FUTURE WEATHER INFO
            FUTURE_WI_LST -> {
                qbuilder.tables = DbSchema.FutureWeatherInfo.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WeatherContract.FutureWeatherInfo.DEFAULT_SORT_ORDER
                }
            }
            FUTURE_WI_OBJ -> {
                qbuilder.tables = DbSchema.FutureWeatherInfo.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }

            else -> badUri(uri!!)
        }

        val db = dbHelper!!.readableDatabase
        val cursor = qbuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }


    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d("YAWA_TAG", "WeatherProvider - update")
        val table: String
        if (selection != null) throw IllegalArgumentException("selection not supported")
        when (URI_MATCHER.match(uri)) {
            CURRENT_LST -> table = DbSchema.Current.TBL_NAME
            FORECAST_LST -> table = DbSchema.Forecast.TBL_NAME
            CURRENT_WI_LST -> table = DbSchema.CurrentWeatherInfo.TBL_NAME
            FUTURE_WI_LST ->table = DbSchema.FutureWeatherInfo.TBL_NAME
            else -> throw badUri(uri!!)
        }

        val db = dbHelper!!.writableDatabase
        val count = db.update(table, values, selection,selectionArgs)

        context.contentResolver.notifyChange(uri, null)
        return count

    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d("YAWA_TAG", "WeatherProvider - delete method")
        val table: String
        // posso deixar o IF aqui fora?
        if (selection != null) throw IllegalArgumentException("selection not supported")

        when (URI_MATCHER.match(uri)) {
            CURRENT_LST -> {
                table = DbSchema.Current.TBL_NAME
                //if (selection != null) {throw IllegalArgumentException("selection not supported")}
            }
            FORECAST_LST -> {
                table = DbSchema.Forecast.TBL_NAME
                //if (selection != null) {throw IllegalArgumentException("selection not supported")}
            }
            CURRENT_WI_LST -> {
                table = DbSchema.CurrentWeatherInfo.TBL_NAME
                //if (selection != null) {throw IllegalArgumentException("selection not supported")}
            }
            FUTURE_WI_LST -> {
                table = DbSchema.FutureWeatherInfo.TBL_NAME
                //if (selection != null) {throw IllegalArgumentException("selection not supported")}
            }
            else -> throw badUri(uri!!)
        }

        val db = dbHelper!!.writableDatabase
        val ndel = db.delete(table, null, null)

        context.contentResolver.notifyChange(uri, null)
        return ndel
    }


    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        Log.d("YAWA_TAG", "WeatherProvider - insert method")
        val table: String
        when (URI_MATCHER.match(uri)) {
            CURRENT_LST -> table = DbSchema.Current.TBL_NAME
            FORECAST_LST -> table = DbSchema.Forecast.TBL_NAME
            CURRENT_WI_LST -> table = DbSchema.CurrentWeatherInfo.TBL_NAME
            FUTURE_WI_LST ->table = DbSchema.FutureWeatherInfo.TBL_NAME
            else -> throw badUri(uri!!)
        }

        val db = dbHelper!!.writableDatabase
        val newId = db.insert(table, null, values)

        context.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, newId)
    }

    private fun badUri(uri: Uri): Exception {
        Log.d("YAWA_TAG", "WeatherProvider - badUri method")
        throw IllegalArgumentException("Unsupported URI: " + uri)
    }
}