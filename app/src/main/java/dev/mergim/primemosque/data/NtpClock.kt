package dev.mergim.primemosque.data

import android.os.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Network-synchronized wall clock. Signage TV boxes usually have no
 * battery-backed RTC, so after a power cut the system clock is wrong until
 * (and unless) Android itself re-syncs. To keep prayer times correct we ask
 * NTP servers for the true time ourselves and anchor it to
 * [SystemClock.elapsedRealtime], which is monotonic and immune to system
 * clock changes. Until the first successful sync, the system clock is used
 * as a fallback.
 */
object NtpClock {

    private val servers = listOf(
        "time.google.com",
        "time.cloudflare.com",
        "pool.ntp.org",
    )

    private const val NTP_PORT = 123
    private const val PACKET_SIZE = 48
    private const val TIMEOUT_MS = 5_000

    /** Offset from 1900-01-01 (NTP epoch) to 1970-01-01 (Unix epoch), in ms. */
    private const val NTP_TO_UNIX_MS = 2_208_988_800_000L

    /** True epoch millis minus elapsedRealtime at the moment of sync. */
    @Volatile
    private var offsetMs: Long? = null

    val synced: Boolean get() = offsetMs != null

    /** Best-known epoch millis: NTP-anchored after a sync, system clock before. */
    fun epochMs(): Long =
        offsetMs?.let { SystemClock.elapsedRealtime() + it } ?: System.currentTimeMillis()

    fun now(zone: ZoneId): LocalDateTime =
        Instant.ofEpochMilli(epochMs()).atZone(zone).toLocalDateTime()

    /** Tries each server in turn; returns true once one answers sanely. */
    suspend fun sync(): Boolean = withContext(Dispatchers.IO) {
        servers.any { host ->
            runCatching { query(host) }.getOrNull()?.let { offsetMs = it } != null
        }
    }

    @Throws(IOException::class)
    private fun query(host: String): Long? {
        val address = InetAddress.getByName(host)
        DatagramSocket().use { socket ->
            socket.soTimeout = TIMEOUT_MS
            val buffer = ByteArray(PACKET_SIZE)
            buffer[0] = 0b00_011_011 // LI = 0, version = 3, mode = 3 (client)
            val sentAt = SystemClock.elapsedRealtime()
            socket.send(DatagramPacket(buffer, buffer.size, address, NTP_PORT))
            socket.receive(DatagramPacket(buffer, buffer.size))
            val receivedAt = SystemClock.elapsedRealtime()

            val serverReceiveMs = timestampMs(buffer, 32)
            val serverTransmitMs = timestampMs(buffer, 40)
            if (serverTransmitMs == 0L) return null

            // True time when our reply arrived = server transmit + half the
            // network round trip (total exchange minus server processing).
            val roundTripMs = (receivedAt - sentAt) - (serverTransmitMs - serverReceiveMs)
            val trueMsAtReceive = serverTransmitMs + roundTripMs.coerceAtLeast(0) / 2

            // Reject nonsense answers (e.g. unsynchronized stratum-0 servers).
            val year = Instant.ofEpochMilli(trueMsAtReceive)
                .atZone(ZoneId.of("UTC")).year
            if (year < 2024 || year > 2100) return null

            return trueMsAtReceive - receivedAt
        }
    }

    /** Reads a 64-bit NTP timestamp (32.32 fixed point, epoch 1900) as Unix ms. */
    private fun timestampMs(buffer: ByteArray, offset: Int): Long {
        var seconds = 0L
        var fraction = 0L
        for (i in 0 until 4) {
            seconds = (seconds shl 8) or (buffer[offset + i].toLong() and 0xFF)
            fraction = (fraction shl 8) or (buffer[offset + 4 + i].toLong() and 0xFF)
        }
        if (seconds == 0L) return 0L
        return seconds * 1_000L + (fraction * 1_000L shr 32) - NTP_TO_UNIX_MS
    }
}
