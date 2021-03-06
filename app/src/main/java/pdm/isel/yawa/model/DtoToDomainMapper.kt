package pdm.isel.yawa.model

import android.util.Log
import java.util.*

/**
 * Created by Dani on 01-11-2016.
 */
class DtoToDomainMapper() {

    fun mapCurrentDto(currentInfo: CurrentWeatherInfoDto): Current {
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

    fun mapForecastDto(forecastDto: ForecastDto): Forecast {
        var infoElements = arrayOfNulls<FutureWeatherInfo>(forecastDto.list.size) as Array<FutureWeatherInfo>

        Log.d("OnService", "mapping forecast, list size = " + forecastDto.list.size)

        for (i in forecastDto.list.indices) {
            infoElements[i] = mapFutureWeatherInfoDto(forecastDto.list[i])
        }
        return Forecast(forecastDto.city.name,
                forecastDto.city.country,
                forecastDto.city.lon.toString(),
                forecastDto.city.lat.toString(),
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

    private fun formatDateValue(dateValue: Long): String {
        return Date(dateValue * 1000).toString()
    }

    private fun getDate(dateValue: Long): String {
        val d = formatDateValue(dateValue).split(" ")
        return d[2] + " " + d[1] + " " + d[5]
    }

    private fun getSunriseOrSunsetString(riseOrSetDateValue: Long): String {
        val date: String = formatDateValue(riseOrSetDateValue)
        val d = date.split(" ")
        return d[3]
    }

    private fun getRoundTempString(value: Float): String {
        return Math.round(value).toString() + "ºC"
    }
}