# PrimeMosque

Offline prayer-times display (digital signage) for Android TV / Google TV, built for mosques in Kosovo. Designed for a TV mounted in **portrait** orientation — the app renders its UI rotated, since TV panels only output landscape. A landscape layout is also available for normally mounted TVs.

Originally built for Xhamia "Sinan Katib" in Prizren, running on a 55" Google TV.

## Features

### Prayer times board
- **Fully offline** — the yearly BIK Kosovo takvim (from [kohet-e-namazit-kosove-json](https://github.com/drilonjaha/kohet-e-namazit-kosove-json)) is bundled as an asset. The takvim repeats every year, so lookups use month + day only. No network needed, ever.
- All 7 daily times (Imsaku, Sabahu, Lindja e Diellit, Dreka, Ikindia, Akshami, Jacia) with the current prayer highlighted and a live countdown to the next one.
- Live clock, Gregorian + Hijri date, upcoming Islamic events.
- **Full-screen announcement** for one minute at the moment each prayer time arrives ("Koha e Namazit të ...", Xhuma-aware on Fridays).
- **Per-prayer time adjustments** — every displayed time can be corrected by ±minutes from settings; the countdown, announcements, notices and night saver all follow the corrected times. The **Hijri date** can also be corrected by ±days for moon-sighting differences (the White Days reminders follow it).
- Kosovo timezone is hardcoded, so a misconfigured TV clock zone still shows correct local times.

### Jumu'ah & khutbah mode
- **Custom Jumu'ah time** — each mosque can hold Xhuma at its own fixed time (12:00–15:00 in 5-minute steps) or simply follow Dhuhr; the board, announcement, countdown and notices all shift together.
- **Full-screen khutbah mode** — from the Jumu'ah time, for a configurable duration (default 20 min), the whole board is replaced: mosque name, clock and date on top, and in the center a rotating collection of **10 Jumu'ah hadiths and Qur'an verses**. Hadiths are given in full narration form ("Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «…»"), verses include the **original Arabic** above the translation, and the key phrase of each quote is highlighted in the theme accent. A calm crossfade rotates them every 30 seconds.

### Custom announcements
Two free-text slots ("Njoftimet") for the mosque's own messages — janaza notices, Ramadan programs, fundraisers. While set, they rotate in the notice card all day alongside the contextual notices.

### Contextual notices (rotating card)
A card on the board shows guidance only while it applies, rotating every 30 s when several are active:
- **Friday**: read Surah El-Kehf, the hour of accepted du'a, Jumu'ah preparation sunnahs — and around the khutbah a single exclusive "remain silent" notice.
- **Duha prayer** window (from ~20 min after sunrise until shortly before Dhuhr).
- **Morning dhikr** (Sabahu → sunrise) and **evening dhikr** (Ikindia → Akshami).
- **Sunnah fasting reminders** the evening before: Monday & Thursday fasts, and the White Days (13/14/15 of the Hijri month).

### Weekly lecture banner
A recurring lecture configured on its own settings page: title, day of week, and which prayer it follows (Akshami in summer, Jacia in winter). On that day a pinned banner appears next to the clock showing "Sot pas namazit të Akshamit (19:25)". When the banner is visible, the portrait board scales itself down slightly so everything still fits.

### Night energy saver
Between Jacia (plus a configurable delay, so the congregation still sees the normal board) and Imsaku the display switches to the pure-black theme and dims the backlight — the mosque is empty, no reason to burn power. The selected theme returns automatically at Imsak. Note: app-level backlight dimming is ignored by most TV firmwares; the biggest savings come from the TV's own on/off timer (the app resumes from standby right where it was).

### Setup & customisation
- **First-run setup wizard** — on first launch the essential settings (language, city, mosque name, place, orientation, theme) are presented once, so a new mosque can configure the board without discovering the settings screen.
- **Categorised settings** (press OK on the remote): a compact main page with the language plus sub-pages — Xhamia (name, place, city with official minute offsets), Ekrani (orientation, theme, night saver), Xhumaja (Jumu'ah time, khutbah duration), Ligjërata javore, Njoftimet, and Përshtatja e kohëve (per-prayer ±min, Hijri date). Every category row shows a live summary of its values.
- **9 themes**: Dark, Black, Emerald, Midnight, Burgundy, Light, Gold, Blue, Green.
- **4 languages**: Shqip, English, Türkçe, Bosanski — including prayer names, notices, dhikr translations, khutbah quotes, Hijri month spellings and date locales.
- Screen is kept awake permanently (signage use), and a boot receiver relaunches the board after a power cut on firmwares that allow it (elsewhere, set the app as home or open it manually).
- Custom adaptive launcher icon and 16:9 Android TV banner (gold mosque on navy gradient); app version shown at the bottom of settings for support.

## Screenshots

<table>
  <tr>
    <td align="center"><b>Portrait board</b> (Gold theme)</td>
    <td align="center"><b>Friday</b> — lecture banner, rotating notice card, pinned salawat (Green theme)</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/portrait-board.png" width="340" alt="Portrait prayer board"></td>
    <td><img src="docs/screenshots/portrait-friday.png" width="340" alt="Portrait board on Friday with lecture banner and notice card"></td>
  </tr>
</table>

**Landscape board on a Friday** (Midnight theme) — weekly lecture banner and notice card flanking the clock:

<img src="docs/screenshots/landscape-friday.png" width="720" alt="Landscape board with lecture banner and notice card">

**Khutbah mode** — during the khutbah the board is replaced by rotating Jumu'ah hadiths and verses (Qur'anic Arabic + translation, key phrases highlighted):

<img src="docs/screenshots/khutbah-verse.png" width="720" alt="Khutbah mode showing a Qur'an verse with Arabic and translation">

<table>
  <tr>
    <td align="center"><b>Khutbah mode</b> in portrait — full hadith narration with the key phrase highlighted</td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/khutbah-portrait.png" width="340" alt="Khutbah mode in portrait with a hadith"></td>
  </tr>
</table>

**Night energy saver** — after Isha the board drops to pure black regardless of the selected theme, until Imsak:

<img src="docs/screenshots/night-saver.png" width="720" alt="Night energy saver black theme">

<table>
  <tr>
    <td align="center"><b>Settings</b> (D-pad driven)</td>
    <td align="center"><b>Weekly lecture sub-page</b></td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/settings.png" width="340" alt="Settings screen"></td>
    <td><img src="docs/screenshots/settings-lecture.png" width="340" alt="Weekly lecture settings page"></td>
  </tr>
</table>

## Tech

Kotlin · Jetpack Compose (Material 3) · MVVM (ViewModel + StateFlow) · DataStore Preferences · kotlinx-serialization · min SDK 26

Notable implementation details:

- `ui/PrayerViewModel.kt` — a single 1-second wall-clock-aligned ticker `Flow` drives everything: board state, countdown, announcements, contextual notices, lecture banner, khutbah mode and the night saver. No alarms or background workers.
- `ui/RotatedLayout.kt` — renders the whole UI rotated by 90/180/270° with swapped measurement constraints, for portrait-mounted TVs.
- `ui/SettingsScreen.kt` — uses an explicit `focusProperties` up/down focus chain, because Compose's geometric D-pad focus search breaks inside a rotated layout. Text settings open an edit dialog on OK-press so the on-screen keyboard doesn't pop up while navigating. A shared `SettingsPage` scaffold powers the main page, the setup wizard and all category sub-pages.
- `ui/Strings.kt` — all four languages live in code as plain data (no Android resources), so language switching is instant and driven by the same settings flow. Khutbah quotes mark key phrases with `**…**`, rendered as accent-coloured bold spans by a tiny markup helper in `DisplayScreen.kt`.

## Build & install

```
gradlew :app:assembleRelease
adb connect <tv-ip>
adb install app/build/outputs/apk/release/app-release.apk
```

The release build is signed with the debug key for easy sideloading — replace with a proper keystore for Play Store distribution.

## Data caveat

The bundled takvim embeds one specific year's DST switchover dates. In other years, times within a few days of the late-March / late-October clock change may be off by up to an hour for those few days only.
