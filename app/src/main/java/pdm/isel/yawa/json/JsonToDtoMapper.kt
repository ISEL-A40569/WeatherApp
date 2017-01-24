package pdm.isel.yawa.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pdm.isel.yawa.model.CurrentWeatherInfoDto
import pdm.isel.yawa.model.ForecastDto

/**
 * Class used to map json response strings into DTO objects.
 */
class JsonToDtoMapper {
    val gson = Gson()

    fun mapWeatherInfoJson(weatherInfoJsonString: String): CurrentWeatherInfoDto {
        val wiToken = object : TypeToken<CurrentWeatherInfoDto>() {}.type
        return gson.fromJson<CurrentWeatherInfoDto>(weatherInfoJsonString, wiToken)
    }

    fun mapForecastJson(forecastJsonString: String): ForecastDto {
        val fcToken = object : TypeToken<ForecastDto>() {}.type
        return gson.fromJson<ForecastDto>(forecastJsonString, fcToken)
    }

}