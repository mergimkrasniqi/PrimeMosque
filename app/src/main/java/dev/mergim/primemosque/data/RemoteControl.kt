package dev.mergim.primemosque.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Remote control for the board via Firebase: the TV listens to its own
 * Firestore document (boards/{code}) and applies changes straight into the
 * local settings, so the imam can manage announcements, the weekly lecture
 * and Jumu'ah settings from the web portal instead of the TV remote.
 *
 * The code (shown in the settings footer) is the pairing secret the imam
 * enters in the portal. Sync is strictly best-effort and one-directional
 * (portal → TV, except for the initial publish of current values): without
 * Firebase config or network the app runs exactly as before, and DataStore
 * remains the on-device source of truth.
 */
object RemoteControl {

    // Paste the values from the Firebase console: Project settings → General
    // → Your apps → Web app → SDK setup. The placeholders keep the whole
    // feature switched off, so the app builds and runs without a project.
    private const val PROJECT_ID = "prime---prayer-times"
    private const val APPLICATION_ID = "1:1053505395140:web:e7227b9b597a3a13e26c91"
    private const val API_KEY = "AIzaSyBRZx_Rtv2FMJksMwbCQTqPJ3JDgNuvn8k"

    val configured: Boolean get() = !PROJECT_ID.startsWith("YOUR_")

    fun start(context: Context, repository: SettingsRepository, scope: CoroutineScope) {
        if (!configured) return
        val initialized = runCatching {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(
                    context,
                    FirebaseOptions.Builder()
                        .setProjectId(PROJECT_ID)
                        .setApplicationId(APPLICATION_ID)
                        .setApiKey(API_KEY)
                        .build(),
                )
            }
        }.isSuccess
        if (!initialized) return

        // Reported with the heartbeat so the dashboard shows which mosques
        // run an outdated APK.
        val appVersion = runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "?"

        scope.launch {
            val code = repository.boardCodeOrCreate()
            val document = FirebaseFirestore.getInstance()
                .collection("boards")
                .document(code)
            document.addSnapshotListener { snapshot, _ ->
                when {
                    snapshot == null -> {}
                    !snapshot.exists() -> {
                        // First contact: publish the board's current values so
                        // the portal starts from what the TV already shows.
                        scope.launch {
                            val settings = repository.settings.first()
                            runCatching { document.set(remoteMap(settings)) }
                        }
                    }
                    // Our own initial publish echoes back; only apply changes
                    // that were confirmed by the server (i.e. the portal's).
                    snapshot.metadata.hasPendingWrites() -> {}
                    else -> scope.launch { runCatching { apply(snapshot, repository) } }
                }
            }
            // Presence heartbeat for the admin dashboard: stamp the document
            // periodically so the portal can show online/last-seen. The
            // board's own clock reading rides along, so the dashboard can
            // compare it against the server timestamp of the same write and
            // flag boards whose time is drifting. update() fails harmlessly
            // while the document does not exist yet (the initial publish
            // above creates it), and Firestore queues the write while
            // offline.
            launch {
                delay(HEARTBEAT_INITIAL_DELAY_MS)
                while (true) {
                    runCatching {
                        document.update(
                            mapOf(
                                "heartbeat" to FieldValue.serverTimestamp(),
                                "boardTimeEpochMs" to NtpClock.epochMs(),
                                "clockSynced" to NtpClock.synced,
                                "appVersion" to appVersion,
                            )
                        )
                    }
                    delay(HEARTBEAT_INTERVAL_MS)
                }
            }
        }
    }

    private const val HEARTBEAT_INITIAL_DELAY_MS = 20_000L
    private const val HEARTBEAT_INTERVAL_MS = 5 * 60_000L

    private fun remoteMap(s: Settings): Map<String, Any> = mapOf(
        "mosqueName" to s.mosqueName,
        "place" to s.place,
        "city" to s.city,
        "language" to s.language.name,
        "secondaryLanguage" to (s.secondaryLanguage?.name ?: "OFF"),
        "orientation" to s.orientation.name,
        "theme" to s.theme.name,
        "themeRotation" to s.themeRotation,
        "nightMode" to s.nightMode.name,
        "showDailyQuotes" to s.showDailyQuotes,
        "announcement1" to s.announcement1,
        "announcement2" to s.announcement2,
        "announcement1Until" to s.announcement1Until,
        "announcement2Until" to s.announcement2Until,
        "ramadanMode" to s.ramadanMode,
        "lectureTitle" to s.lectureTitle,
        "lectureDay" to s.lectureDay.name,
        "lecturePrayer" to s.lecturePrayer.name,
        "jumuahMinutes" to s.jumuahMinutes,
        "khutbahMinutes" to s.khutbahMinutes,
        "hijriOffset" to s.hijriOffset,
        "adjustments" to s.prayerAdjustments.entries.associate { it.key.name to it.value },
        "dailyQuotes" to s.customDailyQuotes.map { quoteMap(it) },
        "khutbahQuotes" to s.customKhutbahQuotes.map { quoteMap(it) },
        "heartbeat" to FieldValue.serverTimestamp(),
    )

    private fun quoteMap(q: CustomQuote): Map<String, String> =
        mapOf("text" to q.text, "source" to q.source, "arabic" to (q.arabic ?: ""))

    private suspend fun apply(doc: DocumentSnapshot, repository: SettingsRepository) {
        doc.getString("mosqueName")?.let { repository.setMosqueName(it) }
        doc.getString("place")?.let { repository.setPlace(it) }
        doc.getString("city")?.let { repository.setCity(it) }
        doc.getString("language")
            ?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
            ?.let { repository.setLanguage(it) }
        doc.getString("secondaryLanguage")?.let { raw ->
            repository.setSecondaryLanguage(
                runCatching { AppLanguage.valueOf(raw) }.getOrNull()
            )
        }
        doc.getString("orientation")
            ?.let { runCatching { DisplayOrientation.valueOf(it) }.getOrNull() }
            ?.let { repository.setOrientation(it) }
        doc.getString("theme")
            ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
            ?.let { repository.setTheme(it) }
        doc.getBoolean("themeRotation")?.let { repository.setThemeRotation(it) }
        doc.getString("nightMode")
            ?.let { runCatching { NightMode.valueOf(it) }.getOrNull() }
            ?.let { repository.setNightMode(it) }
        doc.getBoolean("showDailyQuotes")?.let { repository.setShowDailyQuotes(it) }
        doc.getString("announcement1")?.let { repository.setAnnouncement1(it) }
        doc.getString("announcement2")?.let { repository.setAnnouncement2(it) }
        doc.getString("announcement1Until")?.let { repository.setAnnouncement1Until(it) }
        doc.getString("announcement2Until")?.let { repository.setAnnouncement2Until(it) }
        doc.getBoolean("ramadanMode")?.let { repository.setRamadanMode(it) }
        doc.getString("lectureTitle")?.let { repository.setLectureTitle(it) }
        doc.getString("lectureDay")
            ?.let { runCatching { LectureDay.valueOf(it) }.getOrNull() }
            ?.let { repository.setLectureDay(it) }
        doc.getString("lecturePrayer")
            ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
            ?.let { repository.setLecturePrayer(it) }
        doc.getLong("jumuahMinutes")?.let { repository.setJumuahMinutes(it.toInt()) }
        doc.getLong("khutbahMinutes")
            ?.let { repository.setKhutbahMinutes(it.toInt().coerceIn(5, 45)) }
        doc.getLong("hijriOffset")?.let { repository.setHijriOffset(it.toInt().coerceIn(-2, 2)) }
        (doc.get("adjustments") as? Map<*, *>)?.forEach { (rawKey, rawValue) ->
            val key = (rawKey as? String)
                ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
                ?: return@forEach
            val minutes = (rawValue as? Number)?.toInt() ?: return@forEach
            repository.setPrayerAdjustment(key, minutes.coerceIn(-60, 60))
        }
        doc.get("dailyQuotes")?.let { repository.setCustomDailyQuotes(parseQuotes(it)) }
        doc.get("khutbahQuotes")?.let { repository.setCustomKhutbahQuotes(parseQuotes(it)) }
    }

    private fun parseQuotes(raw: Any?): List<CustomQuote> =
        (raw as? List<*>).orEmpty().mapNotNull { item ->
            val fields = item as? Map<*, *> ?: return@mapNotNull null
            val text = (fields["text"] as? String)?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            CustomQuote(
                text = text,
                source = fields["source"] as? String ?: "",
                arabic = (fields["arabic"] as? String)?.takeIf { it.isNotBlank() },
            )
        }
}
