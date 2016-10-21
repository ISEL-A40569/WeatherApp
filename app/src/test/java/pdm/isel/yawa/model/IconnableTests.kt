package pdm.isel.yawa.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by Dani on 20-10-2016.
 */
class IconnableTests {

    val EXPECTED = "http://openweathermap.org/img/w/10d.png"

    @Test
    public fun shouldReturnIconUrl(){
        val iconnable: Iconnable = Iconnable("10d")
        Assert.assertEquals(EXPECTED, iconnable.getIconUrl())
    }

}