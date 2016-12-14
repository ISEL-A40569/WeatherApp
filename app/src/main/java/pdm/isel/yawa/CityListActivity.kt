package pdm.isel.yawa

import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.util.*

val cityList = LinkedList<String?>()//TODO

class CityListActivity : ListActivity() {

    private val cities = cityList.toArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        Log.d("YAWA_TAG", "COUNTRY_onCreate")

        val adapter = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, cities)
        //val listView = findViewById(android.R.id.list) as ListView    // tmb funciona
        listView.setAdapter(adapter)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        currentWeather = null
        location = cities[position] as String
        Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
        val editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("city", location)
        editor.commit()

    }

}
