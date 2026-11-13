package dev.mergim.primemosque.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Remote control for the board via Firebase: the TV and the web portal share
 * one Firestore document (boards/{code}), so the imam can manage the board
 * from a browser as well as with the TV remote.
 *
 * Both directions are kept apart deliberately:
 * - **Portal → TV.** Every portal save carries a new `rev`. The TV applies a
 *   document only when its `rev` is one it has not applied yet. Heartbeats
 *   and the TV's own writes also produce snapshots, and those must never
 *   re-apply an old portal save over a change made with the remote.
 * - **TV → portal.** Changes made with the remote are written back field by
 *   field, without a new `rev`, so the portal shows what the board shows.
 *
 * The code (shown in the settings footer) is the pairing secret the imam
 * enters in the portal. Sync is strictly best-effort: without network the
 * app runs exactly as before, and DataStore remains the on-device source of
 * truth.
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
            val sync = BoardSync(document, repository, scope)
            sync.listen()
            launch { sync.pushLocalChanges() }

            // Presence heartbeat for the admin dashboard: stamp the document
            // periodically so the portal can show online/last-seen. The
            // board's own clock reading rides along, so the dashboard can
            // compare it against the server timestamp of the same write and
            // flag boards whose time is drifting. update() fails harmlessly
            // while the document does not exist yet (the initial publish
            // creates it), and Firestore queues the write while offline.
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

    private class BoardSync(
        private val document: DocumentReference,
        private val repository: SettingsRepository,
        private val scope: CoroutineScope,
    ) {
        // Applying a portal save and pushing a local edit never overlap, so a
        // half-applied save is never mistaken for an edit made with the remote.
        private val lock = Mutex()

        // The synced fields as the server last confirmed them: a local edit
        // is pushed only where it differs from these.
        private var confirmed: Map<String, Any?>? = null

        fun listen() {
            var registration: ListenerRegistration? = null
            registration = document.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // A denied or over-quota listener is dead for good. Try
                    // again later instead of silently ignoring the portal
                    // until the next restart.
                    registration?.remove()
                    scope.launch {
                        delay(RELISTEN_DELAY_MS)
                        listen()
                    }
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener
                when {
                    // First contact: publish the board's current values so the
                    // portal starts from what the TV already shows. Only on the
                    // server's word: a cache miss while offline does not mean
                    // the document is missing, and publishing then would
                    // overwrite the portal's settings with this board's.
                    !snapshot.exists() -> if (!snapshot.metadata.isFromCache) {
                        scope.launch {
                            val settings = repository.settings.first()
                            runCatching {
                                document.set(
                                    syncedValues(settings) +
                                        ("heartbeat" to FieldValue.serverTimestamp())
                                )
                            }
                        }
                    }
                    // Our own writes echo back before the server confirms them.
                    snapshot.metadata.hasPendingWrites() -> {}
                    else -> onConfirmed(snapshot)
                }
            }
            scope.coroutineContext[Job]?.invokeOnCompletion { registration?.remove() }
        }

        private fun onConfirmed(snapshot: DocumentSnapshot) {
            val data = snapshot.data.orEmpty()
            confirmed = SYNCED_FIELDS.associateWith { data[it] }
            val rev = (snapshot.get("rev") as? Number)?.toLong() ?: return
            scope.launch {
                lock.withLock {
                    if (rev == repository.appliedRemoteRev.first()) return@withLock
                    apply(snapshot, repository)
                    repository.setAppliedRemoteRev(rev)
                }
            }
        }

        /** Writes edits made with the TV remote back to the document. */
        @OptIn(FlowPreview::class)
        suspend fun pushLocalChanges() {
            repository.settings
                .map { syncedValues(it) }
                .distinctUntilChanged()
                // One write for a burst of edits, e.g. stepping through
                // themes or adjusting a prayer time minute by minute.
                .debounce(PUSH_DEBOUNCE_MS)
                .collect {
                    lock.withLock {
                        // Not seen the server's copy yet: nothing to compare.
                        val known = confirmed ?: return@withLock
                        val local = syncedValues(repository.settings.first())
                        val changes = local.filter { (key, value) -> known[key] != value }
                        if (changes.isNotEmpty()) runCatching { document.update(changes) }
                    }
                }
        }
    }

    private const val HEARTBEAT_INITIAL_DELAY_MS = 20_000L
    private const val HEARTBEAT_INTERVAL_MS = 5 * 60_000L
    private const val RELISTEN_DELAY_MS = 5 * 60_000L
    private const val PUSH_DEBOUNCE_MS = 3_000L

    // Upper bounds for portal text, so a runaway paste cannot fill the TV's
    // storage or push the board's layout off the screen.
    private const val MAX_NAME_CHARS = 80
    private const val MAX_TEXT_CHARS = 600
    private const val MAX_QUOTES = 500
    private const val MAX_QUOTE_CHARS = 2_000

    private val SYNCED_FIELDS = listOf(
        "mosqueName", "place", "city", "language", "secondaryLanguage",
        "orientation", "theme", "themeRotation", "nightMode", "showDailyQuotes",
        "announcement1", "announcement2", "announcement1Until", "announcement2Until",
        "ramadanMode", "lectureTitle", "lectureDay", "lecturePrayer",
        "jumuahMinutes", "khutbahMinutes", "hijriOffset", "adjustments",
        "adhanSequence", "preAdhanMinutes", "adhanDuaSeconds", "adhanSeconds",
        "dailyQuotes", "khutbahQuotes",
    )

    /**
     * The synced fields as Firestore stores them: whole numbers as Long, so
     * the values compare equal to what a snapshot hands back.
     */
    private fun syncedValues(s: Settings): Map<String, Any> = mapOf(
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
        "jumuahMinutes" to s.jumuahMinutes.toLong(),
        "khutbahMinutes" to s.khutbahMinutes.toLong(),
        "hijriOffset" to s.hijriOffset.toLong(),
        "adjustments" to s.prayerAdjustments.entries.associate { it.key.name to it.value.toLong() },
        "adhanSequence" to s.adhanSequence,
        "preAdhanMinutes" to s.preAdhanMinutes.toLong(),
        "adhanDuaSeconds" to s.adhanDuaSeconds.toLong(),
        "adhanSeconds" to s.adhanSeconds.entries.associate { it.key.name to it.value.toLong() },
        "dailyQuotes" to s.customDailyQuotes.map { quoteMap(it) },
        "khutbahQuotes" to s.customKhutbahQuotes.map { quoteMap(it) },
    )

    private fun quoteMap(q: CustomQuote): Map<String, String> =
        mapOf("text" to q.text, "source" to q.source, "arabic" to (q.arabic ?: ""))

    /**
     * Applies one field, skipping it when its value is unusable: a wrong type
     * from the portal costs that one setting, not every setting after it.
     */
    private inline fun field(block: () -> Unit) {
        runCatching(block)
    }

    private suspend fun apply(doc: DocumentSnapshot, repository: SettingsRepository) {
        field { doc.getString("mosqueName")?.let { repository.setMosqueName(it.take(MAX_NAME_CHARS)) } }
        field { doc.getString("place")?.let { repository.setPlace(it.take(MAX_NAME_CHARS)) } }
        field { doc.getString("city")?.let { repository.setCity(it.take(MAX_NAME_CHARS)) } }
        field {
            doc.getString("language")
                ?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?.let { repository.setLanguage(it) }
        }
        field {
            doc.getString("secondaryLanguage")?.let { raw ->
                repository.setSecondaryLanguage(runCatching { AppLanguage.valueOf(raw) }.getOrNull())
            }
        }
        field {
            doc.getString("orientation")
                ?.let { runCatching { DisplayOrientation.valueOf(it) }.getOrNull() }
                ?.let { repository.setOrientation(it) }
        }
        field {
            doc.getString("theme")
                ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?.let { repository.setTheme(it) }
        }
        field { doc.getBoolean("themeRotation")?.let { repository.setThemeRotation(it) } }
        field {
            doc.getString("nightMode")
                ?.let { runCatching { NightMode.valueOf(it) }.getOrNull() }
                ?.let { repository.setNightMode(it) }
        }
        field { doc.getBoolean("showDailyQuotes")?.let { repository.setShowDailyQuotes(it) } }
        field { doc.getString("announcement1")?.let { repository.setAnnouncement1(it.take(MAX_TEXT_CHARS)) } }
        field { doc.getString("announcement2")?.let { repository.setAnnouncement2(it.take(MAX_TEXT_CHARS)) } }
        field { doc.getString("announcement1Until")?.let { repository.setAnnouncement1Until(it.take(10)) } }
        field { doc.getString("announcement2Until")?.let { repository.setAnnouncement2Until(it.take(10)) } }
        field { doc.getBoolean("ramadanMode")?.let { repository.setRamadanMode(it) } }
        field { doc.getString("lectureTitle")?.let { repository.setLectureTitle(it.take(MAX_NAME_CHARS)) } }
        field {
            doc.getString("lectureDay")
                ?.let { runCatching { LectureDay.valueOf(it) }.getOrNull() }
                ?.let { repository.setLectureDay(it) }
        }
        field {
            doc.getString("lecturePrayer")
                ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
                ?.let { repository.setLecturePrayer(it) }
        }
        field {
            // -1 follows Dhuhr; anything else must be a time of day, or the
            // board cannot place Jumu'ah at all.
            doc.getLong("jumuahMinutes")?.toInt()
                ?.takeIf { it == -1 || it in 0 until MINUTES_PER_DAY }
                ?.let { repository.setJumuahMinutes(it) }
        }
        field {
            doc.getLong("khutbahMinutes")
                ?.let { repository.setKhutbahMinutes(it.toInt().coerceIn(5, 45)) }
        }
        field { doc.getLong("hijriOffset")?.let { repository.setHijriOffset(it.toInt().coerceIn(-2, 2)) } }
        field {
            (doc.get("adjustments") as? Map<*, *>)?.forEach { (rawKey, rawValue) ->
                val key = (rawKey as? String)
                    ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
                    ?: return@forEach
                val minutes = (rawValue as? Number)?.toInt() ?: return@forEach
                repository.setPrayerAdjustment(key, minutes.coerceIn(-60, 60))
            }
        }
        field { doc.getBoolean("adhanSequence")?.let { repository.setAdhanSequence(it) } }
        field {
            doc.getLong("preAdhanMinutes")
                ?.let { repository.setPreAdhanMinutes(it.toInt().coerceIn(0, 10)) }
        }
        field {
            doc.getLong("adhanDuaSeconds")
                ?.let { repository.setAdhanDuaSeconds(it.toInt().coerceIn(30, 60)) }
        }
        field {
            (doc.get("adhanSeconds") as? Map<*, *>)?.forEach { (rawKey, rawValue) ->
                val key = (rawKey as? String)
                    ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
                    ?.takeIf { it in ADHAN_PRAYERS }
                    ?: return@forEach
                val seconds = (rawValue as? Number)?.toInt() ?: return@forEach
                repository.setAdhanSeconds(key, seconds.coerceIn(30, 420))
            }
        }
        field { doc.get("dailyQuotes")?.let { repository.setCustomDailyQuotes(parseQuotes(it)) } }
        field { doc.get("khutbahQuotes")?.let { repository.setCustomKhutbahQuotes(parseQuotes(it)) } }
    }

    private const val MINUTES_PER_DAY = 24 * 60

    private fun parseQuotes(raw: Any?): List<CustomQuote> =
        (raw as? List<*>).orEmpty().take(MAX_QUOTES).mapNotNull { item ->
            val fields = item as? Map<*, *> ?: return@mapNotNull null
            val text = (fields["text"] as? String)?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            CustomQuote(
                text = text.take(MAX_QUOTE_CHARS),
                source = (fields["source"] as? String ?: "").take(MAX_NAME_CHARS),
                arabic = (fields["arabic"] as? String)?.takeIf { it.isNotBlank() }
                    ?.take(MAX_QUOTE_CHARS),
            )
        }
}
