package pdm.isel.yawa.uri
import java.util.*

/**
 * Created by Dani on 20-10-2016.
 */
class RequestUriFactory {

    private val WEATHERINFO_REQUEST_BASE_URL = "http://api.openweathermap.org/data/2.5/%s?q=%s&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"
    private val ICON_REQUEST_BASE_URL = "http://openweathermap.org/img/w/%s.png"
    private val LANGUAGES: HashMap<String, String> = HashMap<String, String>()

    constructor(){
        fillLanguages()
    }

    private fun fillLanguages(){
        LANGUAGES.put("English", "")
        LANGUAGES.put("português", "&lang=pt")
    }

    fun getNowWeather(locationName: String, language: String): String{
       return String.format(WEATHERINFO_REQUEST_BASE_URL, "weather", prepareLocationName(locationName) + LANGUAGES[language])
    }

    fun getFutureWeather(locationName: String, language: String, count: Int): String{
        return String.format(WEATHERINFO_REQUEST_BASE_URL, "forecast/daily", prepareLocationName(locationName) + LANGUAGES[language] + "&cnt=" + count )
    }

    private fun prepareLocationName(locationName: String): String {
        return locationName.replace(" ", "+", false)
    }

    fun getIcon(iconId: String): String {
        return String.format(ICON_REQUEST_BASE_URL, iconId)
    }


}
