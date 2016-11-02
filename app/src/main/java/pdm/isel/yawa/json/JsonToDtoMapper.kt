package pdm.isel.yawa.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pdm.isel.yawa.model.CurrentWeatherInfoDto
import pdm.isel.yawa.model.ForecastDto

/**
 * Created by Dani on 21-10-2016.
 */
class JsonToDtoMapper {
    val gson = Gson()

    val fcToken = object : TypeToken<ForecastDto>() {}.type

    public fun mapWeatherInfoJson(weatherInfoJsonString: String): CurrentWeatherInfoDto {
        val wiToken = object : TypeToken<CurrentWeatherInfoDto>() {}.type
        return gson.fromJson<CurrentWeatherInfoDto>(weatherInfoJsonString, wiToken)
    }

    public fun mapForecastJson(forecastJsonString: String): ForecastDto {
        val fcToken = object : TypeToken<ForecastDto>() {}.type
        return gson.fromJson<ForecastDto>(forecastJsonString, fcToken)
    }

}