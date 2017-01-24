package pdm.isel.yawa.model

/**
 * Created by Dani on 13-10-2016.
 */
data class CurrentWeatherInfoDto(val name: String, val coord: Coord, val weather: Array<Weather>, val main: Main, val wind: Wind, val dt: Long, val sys: Sys) {}

data class Coord(val lon: Float, val lat: Float)

data class Weather(val description: String, val icon: String)

data class Main(val temp: Float, val pressure: Float, val humidity: Float)

data class Wind(val speed: Float)

data class Sys(val country: String, val sunrise: Long, val sunset: Long)

data class ForecastDto(val city: City, val list: Array<FutureWeatherInfoDto>)

data class City(val name: String, val country: String, val lon: Float, val lat: Float)

data class FutureWeatherInfoDto(val dt: Long, val temp: Temp, val pressure: Float, val humidity: Float, val weather: Array<Weather>)

data class Temp(val min: Float, val max: Float)