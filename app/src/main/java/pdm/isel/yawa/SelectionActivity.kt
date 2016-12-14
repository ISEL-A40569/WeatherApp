package pdm.isel.yawa

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

val MY_PREFS_NAME = "Prefs"

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        var selectButton = findViewById(R.id.SelectButton) as Button

        var editText = findViewById(R.id.CitySelectionBox) as EditText

        selectButton.setOnClickListener {
            var city = editText.text.toString()
            val editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()
            editor.putString("city", city)
            editor.commit()
            location = city
            cityList.add(city)
            currentWeather = null
        }
    }
}
