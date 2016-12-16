package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        var selectButton = findViewById(R.id.SelectButton) as Button

        var editText = findViewById(R.id.CitySelectionBox) as EditText

        selectButton.setOnClickListener {
            var city = editText.text.toString()
            application.editor.putString("city", city)
            application.editor.commit()
            location = city
            cityList.add(city)
            currentWeather = null
        }
    }
}
