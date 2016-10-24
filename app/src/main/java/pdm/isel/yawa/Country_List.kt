package pdm.isel.yawa

import android.app.ListActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class Country_List : ListActivity() {

    private val citys = arrayOf("Lisbon", "Porto", "London", "Rome")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        Log.d("YAWA_TAG", "COUNTRY_onCreate")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, citys)
        //val listView = findViewById(android.R.id.list) as ListView    // tmb funciona
        listView.setAdapter(adapter)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {

        Toast.makeText(this, citys[position], Toast.LENGTH_LONG).show()
    }

}
