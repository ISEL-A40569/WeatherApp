package pdm.isel.yawa

import android.app.ListActivity
import android.app.LoaderManager
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import pdm.isel.yawa.provider.DbSchema
import pdm.isel.yawa.provider.WeatherContract
import pdm.isel.yawa.provider.WeatherContract.CONTENT_URI

class DbTestActivity : ListActivity(), LoaderManager.LoaderCallbacks<Cursor> {


    private val WEATHER_LOADER = 0


    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter.changeCursor(null)
    }

    override fun onCreateLoader(id: Int, args: Bundle?) =

        when (id) {
            WEATHER_LOADER -> CursorLoader(
                    this,
                    WeatherContract.City.CONTENT_URI,
                    WeatherContract.City.SELECT_TEST,
                    null,
                    null,
                    null
            )
            else -> {
                Log.w("Profs", "Unknown id for loader")
                throw IllegalArgumentException("id")
            }
        }


    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        adapter.changeCursor(data)
    }

    var res: TextView? = null

    val adapter by lazy {
        val from = arrayOf(WeatherContract.City.NAME, "_id")
        val to   = intArrayOf(android.R.id.text1, android.R.id.text2)

        SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db_test)
        Log.d("YAWA_TAG", "Db Test")

        res = findViewById(R.id.res) as TextView?

        listView.adapter = adapter

        loaderManager.initLoader(WEATHER_LOADER, null, this)
    }


    fun onInsert(view: View) {
        Log.d("DbTest", "onInsert ")

        var contentValues = ContentValues()

        contentValues.put("name", "Lisboa")
        contentValues.put("country", "Portugal")
        contentValues.put("lon", "14")
        contentValues.put("lat", "10")
        contentValues.put("language", "pt")
        contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)

        contentValues.put("name", "Porto")
        contentValues.put("country", "Portugal")
        contentValues.put("lon", "14")
        contentValues.put("lat", "10")
        contentValues.put("language", "pt2")
        contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)

        contentValues.put("name", "leiria")
        contentValues.put("country", "Portugal")
        contentValues.put("lon", "14")
        contentValues.put("lat", "10")
        contentValues.put("language", "pt2")
        contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)

        contentValues.put("name", "braga")
        contentValues.put("country", "Portugal")
        contentValues.put("lon", "14")
        contentValues.put("lat", "10")
        contentValues.put("language", "pt2")
        contentResolver.insert(WeatherContract.City.CONTENT_URI, contentValues)
    }

    fun onDelete(view: View) {
        Log.d("DbTest", "onDelete")
        contentResolver.delete(WeatherContract.CurrentWeatherInfo.CONTENT_URI, null, null)
    }

    fun onUpdate(view: View) {
        Log.d("DbTest", "onUpdate")
    }

    fun onGetCurrent(view: View) {
        Log.d("DbTest", "onGetCurrent")
    }

    fun onGetForecast(view: View) {
        Log.d("DbTest", "onGetForecast")
    }

    fun onGetCurrentWI(view: View) {
        Log.d("DbTest", "onGetCurrentWI")
    }
}
