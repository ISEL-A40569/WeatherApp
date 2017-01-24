package pdm.isel.yawa.model

import org.junit.Assert
import org.junit.Test

/**
 * Class used to test DtoToDomainMapper mapCurrentDto and mapForecastDto methods.
 */
class DtoToDomainMapperTests {

    private val mapper = DtoToDomainMapper()

    @Test
    fun mapCurrentDtoTest() {
        val currentDto = CurrentWeatherInfoDto(
                "Lisbon",
                Coord(100F, 200F),
                arrayOf(Weather("test", "icon1")),
                Main(20F, 30F, 40F),
                Wind(50F),
                10000000L,
                Sys("PT", 20000000L, 10000000L)
        )

        val current = mapper.mapCurrentDto(currentDto)

        Assert.assertEquals(current.name, "Lisbon")
        Assert.assertEquals(current.country, "PT")
        Assert.assertEquals(current.lon, "100.0")
        Assert.assertEquals(current.lat, "200.0")
        Assert.assertEquals(current.currentInfo.date, "26 Apr 1970")
        Assert.assertEquals(current.currentInfo.description, "test")
        Assert.assertEquals(current.currentInfo.temp, "20ºC")
        Assert.assertEquals(current.currentInfo.pressure, "30.0 hPA")
        Assert.assertEquals(current.currentInfo.humidity, "40.0 %")
        Assert.assertEquals(current.currentInfo.windSpeed, "50.0 m/s")
        Assert.assertEquals(current.currentInfo.sunrise, "12:33:20")
        Assert.assertEquals(current.currentInfo.sunset, "18:46:40")
        Assert.assertEquals(current.currentInfo.icon, "icon1")

    }

    @Test
    fun mapForecastDtoTest() {
        val forecastDto = ForecastDto(
                City("Lisbon", "PT", Coord(100F, 200F)),
                arrayOf(FutureWeatherInfoDto(10000000L, Temp(10F, 20F), 10F, 20F, arrayOf(
                        Weather("test", "icon1")
                ))
                )
        )

        val forecast = mapper.mapForecastDto(forecastDto)

        Assert.assertEquals(forecast.name, "Lisbon")
        Assert.assertEquals(forecast.country, "PT")
        Assert.assertEquals(forecast.lon, "100.0")
        Assert.assertEquals(forecast.lat, "200.0")
        Assert.assertEquals(forecast.list.size, 1)

        val fwi = forecast.list[0]

        Assert.assertEquals(fwi.date, "26 Apr 1970")
        Assert.assertEquals(fwi.description, "test")
        Assert.assertEquals(fwi.tempMin, "10ºC")
        Assert.assertEquals(fwi.tempMax, "20ºC")
        Assert.assertEquals(fwi.pressure, "10.0 hPA")
        Assert.assertEquals(fwi.humidity, "20.0 %")
        Assert.assertEquals(fwi.icon, "icon1")

    }
}