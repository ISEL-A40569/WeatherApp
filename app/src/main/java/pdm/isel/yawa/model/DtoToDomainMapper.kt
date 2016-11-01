package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 01-11-2016.
 */
class DtoToDomainMapper(){

    public fun mapCurrentDto(currentInfo: CurrentWeatherInfoDto): Current{
        return Current(currentInfo.name,
                currentInfo.sys.country,
                currentInfo.coord.lon.toString(),
                currentInfo.coord.lat.toString(),
                CurrentWeatherInfo(
                getDate(currentInfo.dt),
                        currentInfo.main.pressure.toString() + " hPA",
                        currentInfo.main.humidity.toString() + " %",
                        currentInfo.weather[0].description,
                        currentInfo.weather[0].icon,
                        getRoundTempString(currentInfo.main.temp),
                        getSunriseOrSunsetString(currentInfo.sys.sunrise),
                        getSunriseOrSunsetString(currentInfo.sys.sunset),
                        currentInfo.wind.speed.toString() + " m/s"
                ))
    }

    public fun mapForecastDto(forecastDto: ForecastDto): Forecast{
        var infoElements = arrayOfNulls<FutureWeatherInfo>(forecastDto.list.size) as Array<FutureWeatherInfo>

        for(i in forecastDto.list.indices){
            infoElements[i] = mapFutureWeatherInfoDto(forecastDto.list[i])
        }
        return Forecast(forecastDto.city.name,
                forecastDto.city.country,
                forecastDto.city.coord.lon.toString(),
                forecastDto.city.coord.lat.toString(),
                infoElements
                )
    }

    private fun mapFutureWeatherInfoDto(futureWeatherInfoDto: FutureWeatherInfoDto): FutureWeatherInfo {
        return FutureWeatherInfo(getDate(futureWeatherInfoDto.dt),
                futureWeatherInfoDto.pressure.toString() + " hPA",
                futureWeatherInfoDto.humidity.toString() + " %",
                futureWeatherInfoDto.weather[0].description,
                futureWeatherInfoDto.weather[0].icon,
                getRoundTempString(futureWeatherInfoDto.temp.min),
                getRoundTempString(futureWeatherInfoDto.temp.max)
                )
    }

    private fun formatDateValue (dateValue: Long) : String {
        return Date(dateValue * 1000).toString()
    }

    private fun getDate(dateValue: Long):String{
        var d = formatDateValue(dateValue).split(" ")
        return d[2] + " " + d[1] + " "+ d[5]
    }

    public fun getSunriseOrSunsetString(riseOrSetDateValue: Long): String{
        var date:String = formatDateValue(riseOrSetDateValue)
        var d = date.split(" ")
        return d[3]
    }

    private fun getRoundTempString(value: Float): String {
        return Math.round(value).toString()  + "ºC"
    }
}