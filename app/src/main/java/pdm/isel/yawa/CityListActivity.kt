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

    var cities: Array<Any>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var cityList = crud.getListOfAllCitys(contentResolver, null, null, null, "'name' ASC")

        cities = cityList.toArray()!!


//        cityList.add("Lisboa")
//        cityList.add("Porto")
//        cityList.add("ATENÇÃO!! Dados hard codded!!")



        setContentView(R.layout.activity_city_list)
        Log.d("YAWA_TAG", "COUNTRY_onCreate")

        val adapter = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, cities)
        //val listView = findViewById(android.R.id.list) as ListView    // tmb funciona
        listView.setAdapter(adapter)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        currentWeather = null
        location = cities!![position] as String
        Toast.makeText(this, location, Toast.LENGTH_LONG).show()
        application.editor.putString("city", location)
        application.editor.commit()

    }

}
