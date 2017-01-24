package pdm.isel.yawa.uri

import org.junit.Assert
import org.junit.Test

/**
 * Class used to test RequestUriFactory getNowWeather and getFutureWeather methods.
 */
class RequestUriFactoryTests {
    private val uriFactory: RequestUriFactory = RequestUriFactory()
    private val EXPECTED_NOW_WEATHER: String = "http://api.openweathermap.org/data/2.5/weather?q=Lisbon&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"
    private val EXPECTED_FUTURE_WEATHER: String = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Madrid&lang=pt&cnt=16&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"

    @Test
    fun shouldObtainCorrectUriForNowWeather(){
        Assert.assertEquals(EXPECTED_NOW_WEATHER, uriFactory.getNowWeather("Lisbon", "English"))
    }

    @Test
    fun shouldObtainCorrectUriForFutureWeather(){
        Assert.assertEquals(EXPECTED_FUTURE_WEATHER, uriFactory.getFutureWeather("Madrid", "português", 16))
    }
}