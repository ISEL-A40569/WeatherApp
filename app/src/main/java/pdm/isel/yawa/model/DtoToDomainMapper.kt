package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class DtoToDomainMapper {

    public fun mapWeatherInfoDto(currentWeatherInfoDto: CurrentWeatherInfoDto) : CurrentWeatherInfo {
        return CurrentWeatherInfo(currentWeatherInfoDto.name,
                currentWeatherInfoDto.sys.country,
                currentWeatherInfoDto.coord.lon,
                currentWeatherInfoDto.coord.lat,
                formatDateValue(currentWeatherInfoDto.dt),
                currentWeatherInfoDto.weather[0].main,
                currentWeatherInfoDto.weather[0].description,
                currentWeatherInfoDto.weather[0].icon,
                currentWeatherInfoDto.main.temp,
                currentWeatherInfoDto.main.pressure,
                currentWeatherInfoDto.main.humidity,
                currentWeatherInfoDto.wind.speed,
                currentWeatherInfoDto.sys.sunrise,
                currentWeatherInfoDto.sys.sunset,
                currentWeatherInfoDto.dt)
    }


    public fun mapForecastDto(forecastDto: ForecastDto) : Forecast {

        var InfoElements = arrayOfNulls<FutureWeatherInfo>(forecastDto.list.size) as Array<FutureWeatherInfo>

        for(i in forecastDto.list.indices){
            InfoElements[i] = mapBasicWeatherInfoDto(forecastDto.list[i])
        }


        return Forecast(forecastDto.city.name,
                forecastDto.city.country,
                forecastDto.city.coord.lon,
                forecastDto.city.coord.lat,
                InfoElements[0].dateTime, //Forecast date is the date of the first FutureWeatherInfo element of the forecast list
                InfoElements,
                InfoElements[0]._dt)
    }

    public fun mapBasicWeatherInfoDto(futureWeatherInfoDto: FutureWeatherInfoDto) : FutureWeatherInfo {
        return FutureWeatherInfo(formatDateValue(futureWeatherInfoDto.dt),
                futureWeatherInfoDto.temp.min,
                futureWeatherInfoDto.temp.max,
                futureWeatherInfoDto.pressure,
                futureWeatherInfoDto.pressure,
                futureWeatherInfoDto.weather[0].main,
                futureWeatherInfoDto.weather[0].description,
                futureWeatherInfoDto.weather[0].icon,
                futureWeatherInfoDto.dt)
    }

    private fun formatDateValue (dateValue: Long) : String {
        return Date(dateValue * 1000).toString()
    }
}