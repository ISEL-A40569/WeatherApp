package pdm.isel.yawa

import android.app.ListActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class CityListActivity : ListActivity() {

    private val cities = arrayOf("Lisboa", "Porto", "Coimbra", "Faro", "Funchal", "Ponta Delgada",
            "Madrid", "London", "Roma", "Paris", "New York", "Moscow")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        Log.d("YAWA_TAG", "COUNTRY_onCreate")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities)
        //val listView = findViewById(android.R.id.list) as ListView    // tmb funciona
        listView.setAdapter(adapter)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        weather = null
        LOCATION = cities[position]
        changes = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
