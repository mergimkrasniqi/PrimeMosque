package dev.mergim.primemosque.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** One narration of the bundled collection; [n] is its number in the source. */
@Serializable
data class Hadith(val n: Int, val text: String)

/**
 * Loads the bundled Albanian Rijadus Salihin (buhariu.com/Rijad), shown in
 * the daily wisdom breaks alongside the curated verses and hadiths.
 */
class HadithRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun rijadusSalihin(): List<Hadith> = context.assets
        .open("rijadus-salihin-sq.json")
        .bufferedReader()
        .use { it.readText() }
        .let { json.decodeFromString<List<Hadith>>(it) }
}
