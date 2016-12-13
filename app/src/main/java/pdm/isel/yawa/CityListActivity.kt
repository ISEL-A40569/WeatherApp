package pdm.isel.yawa

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.util.*

class CityListActivity : ListActivity() {

    private val cities = arrayOf("Lisbon", "Porto", "Coimbra", "Faro", "Funchal", "Ponta Delgada",
            "Madrid", "London", "Roma", "Paris", "New York", "Moscow")//TODO: THIS MUST BE USERS CHOICE AND BE STORED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        Log.d("YAWA_TAG", "COUNTRY_onCreate")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities)
        //val listView = findViewById(android.R.id.list) as ListView    // tmb funciona
        listView.setAdapter(adapter)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        currentWeather = null
        location = cities[position]
        Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
        location = cities[position]

    }

}
