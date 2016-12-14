package pdm.isel.yawa.broadcaste_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pdm.isel.yawa.isBatteryLow

/**
 * Created by Dani on 14-12-2016.
 */
class BatteryLevelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        isBatteryLow = !isBatteryLow //TODO: not sure if this works
    }
}