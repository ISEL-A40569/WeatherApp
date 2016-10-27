package pdm.isel.yawa.uri

import org.junit.Assert
import org.junit.Test

/**
 * Created by Dani on 20-10-2016.
 */
class RequestUriFactoryTests {
    val uriFactory: RequestUriFactory = RequestUriFactory()
    val EXPECTED_NOW_WEATHER: String = "http://api.openweathermap.org/data/2.5/currentWeather?q=Lisbon&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"
    val EXPECTED_FUTURE_WEATHER: String = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Madrid&lang=pt&cnt=16&units=metric&appid=3653dafe6dfbcaec795a87592caa3cb6"

    @Test
    public fun shouldObtainCorrectUriForNowWeather(){
        Assert.assertEquals(EXPECTED_NOW_WEATHER, uriFactory.getNowWeather("Lisbon", "English"))
    }

    @Test
    public fun shouldObtainCorrectUriForFutureWeather(){
        Assert.assertEquals(EXPECTED_FUTURE_WEATHER, uriFactory.getFutureWeather("Madrid", "português", 16))
    }
}