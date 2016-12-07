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
                    WeatherContract.CurrentWeatherInfo.CONTENT_URI,
                    WeatherContract.CurrentWeatherInfo.SELECT_TEST,
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
        val from = arrayOf(WeatherContract.CurrentWeatherInfo.CURR_ID, WeatherContract.CurrentWeatherInfo.DESCRIPTION)
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
        Log.d("DbTest", "onInsert")

        var contentValues = ContentValues()

        contentValues.put("currentid", 1)
        contentValues.put("date", "05/12/2016")
        contentValues.put("pressure", "5.5")
        contentValues.put("humidity", "1")
        contentValues.put("description", "nice weather")
        contentValues.put("temp", 15)
        contentValues.put("sunrise", "7:50")
        contentValues.put("sunset", "20:00")
        contentValues.put("windSpeed", "5")

        contentResolver.insert(WeatherContract.CurrentWeatherInfo.CONTENT_URI, contentValues)
    }

    fun onDelete(view: View) {
        Log.d("DbTest", "onDelete")
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
