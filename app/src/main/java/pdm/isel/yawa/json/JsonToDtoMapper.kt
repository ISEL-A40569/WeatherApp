package pdm.isel.yawa.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pdm.isel.yawa.model.ForecastDto
import pdm.isel.yawa.model.WeatherInfoDto

/**
 * Created by Dani on 21-10-2016.
 */
class JsonToDtoMapper {
    val gson = Gson()

    val fcToken = object : TypeToken<ForecastDto>() {}.type

    public fun mapWeatherInfoJson(weatherInfoJsonString: String): WeatherInfoDto {
        val wiToken = object : TypeToken<WeatherInfoDto>() {}.type
        return gson.fromJson<WeatherInfoDto>(weatherInfoJsonString, wiToken)
    }

    public fun mapForecastJson(forecastJsonString: String): ForecastDto {
        val wiToken = object : TypeToken<WeatherInfoDto>() {}.type
        return gson.fromJson<ForecastDto>(forecastJsonString, wiToken)
    }

}