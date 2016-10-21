package pdm.isel.yawa.uri
import java.util.*

/**
 * Created by Dani on 20-10-2016.
 */
class RequestUriFactory {

    private val URL: String = "http://api.openweathermap.org/data/2.5/%s?q=%s&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"
    private val LANGUAGES: HashMap<String, String> = HashMap<String, String>()

    constructor(){
        fillLanguages()
    }

    private fun fillLanguages(){
        LANGUAGES.put("English", "")
        LANGUAGES.put("português", "&lang=pt")
    }

    public fun getNowWeather(locationName: String, language: String): String{
       return String.format(URL, "weather", locationName + LANGUAGES.get(language))
    }

    public fun getFutureWeather(locationName: String, language: String, count: Int): String{
        return String.format(URL, "forecast/daily", locationName + LANGUAGES.get(language) + "&cnt=" + count )
    }


}
