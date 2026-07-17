package dev.mergim.primemosque.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class DisplayOrientation(val degrees: Int) {
    LANDSCAPE(0),
    PORTRAIT(90),
    PORTRAIT_REVERSED(270),
    LANDSCAPE_FLIPPED(180),
}

enum class AppLanguage { SQ, EN }

enum class AppTheme { DARK, BLACK, EMERALD, MIDNIGHT, BURGUNDY, LIGHT, GOLD, BLUE, GREEN }

data class Settings(
    val mosqueName: String = "Xhamia",
    val place: String = "Prizren",
    val city: String = "Prizren",
    val orientation: DisplayOrientation = DisplayOrientation.PORTRAIT,
    val language: AppLanguage = AppLanguage.SQ,
    val theme: AppTheme = AppTheme.DARK,
)

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val MOSQUE_NAME = stringPreferencesKey("mosque_name")
        val PLACE = stringPreferencesKey("place")
        val CITY = stringPreferencesKey("city")
        val ORIENTATION = stringPreferencesKey("orientation")
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme")
    }

    val settings: Flow<Settings> = context.dataStore.data.map { p ->
        val defaults = Settings()
        Settings(
            mosqueName = p[Keys.MOSQUE_NAME] ?: defaults.mosqueName,
            place = p[Keys.PLACE] ?: defaults.place,
            city = p[Keys.CITY] ?: defaults.city,
            orientation = p[Keys.ORIENTATION]
                ?.let { runCatching { DisplayOrientation.valueOf(it) }.getOrNull() }
                ?: defaults.orientation,
            language = p[Keys.LANGUAGE]
                ?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?: defaults.language,
            theme = p[Keys.THEME]
                ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?: defaults.theme,
        )
    }

    suspend fun setMosqueName(value: String) =
        context.dataStore.edit { it[Keys.MOSQUE_NAME] = value }

    suspend fun setPlace(value: String) =
        context.dataStore.edit { it[Keys.PLACE] = value }

    suspend fun setCity(value: String) =
        context.dataStore.edit { it[Keys.CITY] = value }

    suspend fun setOrientation(value: DisplayOrientation) =
        context.dataStore.edit { it[Keys.ORIENTATION] = value.name }

    suspend fun setLanguage(value: AppLanguage) =
        context.dataStore.edit { it[Keys.LANGUAGE] = value.name }

    suspend fun setTheme(value: AppTheme) =
        context.dataStore.edit { it[Keys.THEME] = value.name }
}
