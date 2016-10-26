package pdm.isel.yawa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CityBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)

        val editText = findViewById(R.id.editCitySearch) as EditText

        val searchButton = findViewById(R.id.searchButton) as Button

        searchButton.setOnClickListener {
            LOCATION = editText.getText().toString()

            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)

        }


    }
}
