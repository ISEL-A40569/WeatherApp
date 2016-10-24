package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class DtoToDomainMapper {

    public fun mapWeatherInfoDto(weatherInfoDto: WeatherInfoDto) : WeatherInfo{
        return WeatherInfo(weatherInfoDto.name,
                weatherInfoDto.sys.country,
                weatherInfoDto.coord.lon,
                weatherInfoDto.coord.lat,
                formatDateValue(weatherInfoDto.dt),
                weatherInfoDto.weather[0].main,
                weatherInfoDto.weather[0].description,
                weatherInfoDto.weather[0].icon,
                weatherInfoDto.main.temp,
                weatherInfoDto.main.pressure,
                weatherInfoDto.main.humidity,
                weatherInfoDto.wind.speed,
                weatherInfoDto.wind.deg,
                weatherInfoDto.sys.sunrise,
                weatherInfoDto.sys.sunset)
    }

    var j = 0
    public fun mapForecastDto(forecastDto: ForecastDto) : Forecast {

        val InfoElements = arrayOfNulls<BasicWeatherInfo>(forecastDto.list.size) as Array<BasicWeatherInfo>

        var i = 0
        forecastDto.list.asList().forEach {
            InfoElements[i++] = mapBasicWeatherInfoDto(forecastDto.list[i++])
        }

        return Forecast(forecastDto.city.name,
                forecastDto.city.country,
                forecastDto.city.coords.lon,
                forecastDto.city.coords.lat,
                InfoElements[0].dateTime, //Forecast date is the date of the first BasicWeatherInfo element of the forecast list
                InfoElements
        )
    }

    public fun mapBasicWeatherInfoDto(basicWeatherInfoDto: BasicWeatherInfoDto) : BasicWeatherInfo{
        return BasicWeatherInfo(formatDateValue(basicWeatherInfoDto.dt),
                basicWeatherInfoDto.temp.min,
                basicWeatherInfoDto.temp.max,
                basicWeatherInfoDto.pressure,
                basicWeatherInfoDto.pressure,
                basicWeatherInfoDto.weather[0].main,
                basicWeatherInfoDto.weather[0].description,
                basicWeatherInfoDto.weather[0].icon)
    }

    private fun formatDateValue (dateValue: Long) : String {
        return Date(dateValue * 1000).toString()
    }
}