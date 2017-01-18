package pdm.isel.yawa

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val selectButton = findViewById(R.id.SelectButton) as Button

        val editText = findViewById(R.id.CitySelectionBox) as EditText

        selectButton.setOnClickListener {
            val location = editText.text.toString()
            application.editor.putString("city", location)
            application.editor.commit()

            Toast.makeText(this, location, Toast.LENGTH_LONG).show()
        }
        findViewById(android.R.id.content)
                .setBackgroundDrawable(resources.getDrawable(R.drawable.choose))
    }
}
