package pdm.isel.yawa

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class CityListActivity : ListActivity() {

    var cities: Array<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("YAWA_TAG", "COUNTRY_onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
    }

    override fun onStart() {
        Log.d("YAWA_TAG", "COUNTRY_onStart")
        super.onStart()
        cities = application.DbApi.getListOfAllCities(contentResolver).toArray()
        val adapter = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, cities)
        listView.adapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val location = cities!![position] as String
        application.editor.putString("city", location)
        application.editor.commit()

        Toast.makeText(this, location, Toast.LENGTH_LONG).show()
    }

}
