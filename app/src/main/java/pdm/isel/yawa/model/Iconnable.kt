package pdm.isel.yawa.model

/**
 * Created by Dani on 21-10-2016.
 */
open class Iconnable {
    val URL = "http://openweathermap.org/img/w/%s.png"
    val iconID: String

    constructor(iconID: String) {
        this.iconID = iconID
    }

    public fun getIconUrl() : String {
        return String.format(URL, iconID)
    }
}