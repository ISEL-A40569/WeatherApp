package pdm.isel.yawa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("YAWA_TAG", "onCreate")
    }

    fun onCity(view: View){

        Log.d("YAWA_TAG", "onCity")
       // Toast.makeText(this, "GO TO LIST", Toast.LENGTH_LONG).show()
        
        val intent = Intent(this, Country_List::class.java)
        startActivity(intent)
    }
}
