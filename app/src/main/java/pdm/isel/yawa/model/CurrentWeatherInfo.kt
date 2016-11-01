package pdm.isel.yawa.model

/**
 * Created by Dani on 01-11-2016.
 */
class CurrentWeatherInfo(val _date: String,
                         val _pressure: String,
                         val _humidity: String,
                         val _description: String,
                         val _icon: String,
                         val temp: String,
                         val sunrise: String,
                         val sunset: String,
                         val windSpeed: String
) : BaseWeatherInfo(_date, _description, _pressure, _humidity, _icon) {
}