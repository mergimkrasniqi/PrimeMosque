package dev.mergim.primemosque

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Relaunches the board after a power cut or reboot, so the display comes
 * back without anyone needing the remote. Some TV firmwares block activity
 * starts from the background (API 29+ restriction); there this is a silent
 * no-op and the app must be opened manually or set as the home app.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        runCatching {
            context.startActivity(
                Intent(context, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}
