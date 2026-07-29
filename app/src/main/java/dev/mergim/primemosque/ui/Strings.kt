package dev.mergim.primemosque.ui

import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.data.LectureDay
import dev.mergim.primemosque.data.NightMode
import dev.mergim.primemosque.data.PrayerKey
import java.util.Locale

data class Reminder(val text: String, val translation: String)

data class NoticeText(val title: String, val body: String)

/**
 * Quote for the khutbah screen. [text] is the full narration (narrator +
 * saying) and may mark key phrases with `**bold**`; [arabic] carries the
 * original Qur'anic text when the quote is a verse.
 */
data class KhutbahQuote(val text: String, val source: String, val arabic: String? = null)
data class Strings(
    val locale: Locale,
    val prayerNames: Map<PrayerKey, String>,
    val fridayDhuhrName: String,
    val announceTemplate: String,
    val announceNames: Map<PrayerKey, String>,
    val fridayDhuhrAnnounceName: String,
    val fridaySalawat: Reminder,
    val reminders: List<Reminder>,
    val nextPrayerIn: String,
    val noticeTexts: Map<NoticeKey, NoticeText>,
    val settingsTitle: String,
    val mosqueNameLabel: String,
    val placeLabel: String,
    val cityLabel: String,
    val orientationLabel: String,
    val languageLabel: String,
    val orientationNames: Map<DisplayOrientation, String>,
    val themeLabel: String,
    val themeNames: Map<AppTheme, String>,
    val nightModeLabel: String,
    val nightModeNames: Map<NightMode, String>,
    val mosqueSectionLabel: String,
    val displaySectionLabel: String,
    val fridaySectionLabel: String,
    val lectureSectionLabel: String,
    val lectureTitleLabel: String,
    val lectureDayLabel: String,
    val lecturePrayerLabel: String,
    val lectureDayNames: Map<LectureDay, String>,
    val lectureBody: String,
    val adjustSectionLabel: String,
    val adjustResetLabel: String,
    val hijriOffsetLabel: String,
    val jumuahLabel: String,
    val jumuahFollowDhuhr: String,
    val khutbahDurationLabel: String,
    val khutbahTitle: String,
    val khutbahQuotes: List<KhutbahQuote>,
    val announcementsLabel: String,
    val announcementLabel: String,
    val setupTitle: String,
    val setupStart: String,
    val back: String,
    val languageName: String,
    val done: String,
    val save: String,
    val cancel: String,
    val minutesShort: String,
    val hijriMonths: List<String>,
    val eventNames: Map<String, String>,
)

private val SQ = Strings(
    locale = Locale.forLanguageTag("sq"),
    prayerNames = mapOf(
        PrayerKey.IMSAK to "Imsaku",
        PrayerKey.FAJR to "Sabahu",
        PrayerKey.SUNRISE to "Lindja e Diellit",
        PrayerKey.DHUHR to "Dreka",
        PrayerKey.ASR to "Ikindia",
        PrayerKey.MAGHRIB to "Akshami",
        PrayerKey.ISHA to "Jacia",
    ),
    fridayDhuhrName = "Xhuma",
    announceTemplate = "Koha e Namazit të %s",
    announceNames = mapOf(
        PrayerKey.FAJR to "Sabahut",
        PrayerKey.DHUHR to "Drekës",
        PrayerKey.ASR to "Ikindisë",
        PrayerKey.MAGHRIB to "Akshamit",
        PrayerKey.ISHA to "Jacisë",
    ),
    fridayDhuhrAnnounceName = "Xhumasë",
    fridaySalawat = Reminder(
        "All-llahumme sal-li ve sel-lim ala nebijjina Muhammed",
        "O Allahu im, mëshiroje dhe përshëndete të Dërguarin tonë, Muhammedin.",
    ),
    reminders = listOf(
        Reminder(
            "Estagfirullahe ve etubu ilejhi",
            "Kërkoj faljen e All-llahut dhe tek Ai pendohem",
        ),
        Reminder(
            "Subhanall-llah",
            "I Lartësuar është Allahu",
        ),
        Reminder(
            "SubhanAll-llahi ve bihamdihi",
            "I Lartësuar qoftë Allahu, Atij të Cilit i takon Lavdërimi",
        ),
        Reminder(
            "SubhanAllahil Adhim",
            "I Lartësuar është Allahu i Madhëruar",
        ),
        Reminder(
            "Elhamdulilah",
            "Falenderimi i takon Allahut",
        ),
        Reminder(
            "Allahu Ekber",
            "Allahu është më i madhi",
        ),
        Reminder(
            "La ilahe il-lallah",
            "S'ka të adhuruar të denjë përveç Allahut",
        ),
        Reminder(
            "All-llahumme sal-li ve sel-lim ala nebijjina Muhammed",
            "O Allahu im, mëshiroje dhe përshëndete të Dërguarin tonë, Muhammedin.",
        ),
        Reminder(
            "La havle ve la kuv-vete il-la bil-lah",
            "Nuk ka fuqi as forcë vetëm se me Allahun",
        ),
    ),
    nextPrayerIn = "deri në",
    noticeTexts = mapOf(
        NoticeKey.FRIDAY_KAHF to NoticeText(
            "Surja El-Kehf",
            "Është sunet të lexohet Surja El-Kehf ditën e Xhuma.",
        ),
        NoticeKey.FRIDAY_SUNNAH to NoticeText(
            "Sunetet e Xhumasë",
            "Gusli, veshja e bukur, parfumi dhe shkuarja herët në xhami.",
        ),
        NoticeKey.FRIDAY_DUA to NoticeText(
            "Çasti i pranimit të duasë",
            "Ditën e Xhuma ka një çast kur duaja pranohet - shtoni duatë dhe salavatet.",
        ),
        NoticeKey.FRIDAY_KHUTBAH to NoticeText(
            "Heshtja gjatë hutbes",
            "Kur imami mban hutben, hesht dhe dëgjo me vëmendje.",
        ),
        NoticeKey.DUHA to NoticeText(
            "Namazi i Duhasë",
            "Tani është koha e namazit të Duhasë - së paku 2 rekate.",
        ),
        NoticeKey.MORNING_DHIKR to NoticeText(
            "Dhikri i mëngjesit",
            "Koha për dhikrin e mëngjesit.",
        ),
        NoticeKey.EVENING_DHIKR to NoticeText(
            "Dhikri i mbrëmjes",
            "Koha për dhikrin e mbrëmjes",
        ),
        NoticeKey.FAST_MONDAY to NoticeText(
            "Agjërimi sunet (vullnetar)",
            "Nesër është e hënë - agjërimi i saj është sunet.",
        ),
        NoticeKey.FAST_THURSDAY to NoticeText(
            "Agjërimi sunet (vullnetar)",
            "Nesër është e enjte - agjërimi i saj është sunet.",
        ),
        NoticeKey.FAST_WHITE_DAYS to NoticeText(
            "Ditët e Bardha",
            "Nesër është dita e %d e muajit hënor - agjërimi është sunet.",
        ),
        NoticeKey.CUSTOM to NoticeText("Njoftim", ""),
    ),
    settingsTitle = "Cilësimet",
    mosqueNameLabel = "Emri i xhamisë",
    placeLabel = "Vendi (nëntitulli)",
    cityLabel = "Qyteti (koha e namazit)",
    orientationLabel = "Orientimi i ekranit",
    languageLabel = "Gjuha",
    orientationNames = mapOf(
        DisplayOrientation.LANDSCAPE to "Horizontal",
        DisplayOrientation.PORTRAIT to "Vertikal",
        DisplayOrientation.PORTRAIT_REVERSED to "Vertikal (i kthyer)",
        DisplayOrientation.LANDSCAPE_FLIPPED to "Horizontal (i kthyer)",
    ),
    themeLabel = "Pamja",
    themeNames = mapOf(
        AppTheme.DARK to "E errët",
        AppTheme.BLACK to "E zezë",
        AppTheme.EMERALD to "Smerald",
        AppTheme.MIDNIGHT to "Blu e errët",
        AppTheme.BURGUNDY to "Bordo",
        AppTheme.LIGHT to "E çelët",
        AppTheme.GOLD to "Ari",
        AppTheme.BLUE to "Blu",
        AppTheme.GREEN to "Jeshile",
    ),
    nightModeLabel = "Kursimi i energjisë natën (deri në Imsak)",
    nightModeNames = mapOf(
        NightMode.OFF to "Fikur",
        NightMode.AT_ISHA to "Nga Jacia",
        NightMode.AFTER_15 to "Jacia +15 min",
        NightMode.AFTER_30 to "Jacia +30 min",
        NightMode.AFTER_45 to "Jacia +45 min",
        NightMode.AFTER_60 to "Jacia +1 orë",
    ),
    mosqueSectionLabel = "Xhamia",
    displaySectionLabel = "Ekrani",
    fridaySectionLabel = "Xhumaja",
    lectureSectionLabel = "Ligjërata javore",
    lectureTitleLabel = "Titulli",
    lectureDayLabel = "Dita",
    lecturePrayerLabel = "Pas namazit",
    lectureDayNames = mapOf(
        LectureDay.OFF to "Fikur",
        LectureDay.MONDAY to "E hënë",
        LectureDay.TUESDAY to "E martë",
        LectureDay.WEDNESDAY to "E mërkurë",
        LectureDay.THURSDAY to "E enjte",
        LectureDay.FRIDAY to "E premte",
        LectureDay.SATURDAY to "E shtunë",
        LectureDay.SUNDAY to "E diel",
    ),
    lectureBody = "Sot pas namazit të %s (%s)",
    adjustSectionLabel = "Përshtatja e kohëve (min)",
    adjustResetLabel = "Rikthe të gjitha në 0",
    hijriOffsetLabel = "Data hixhri (ditë)",
    jumuahLabel = "Koha e Xhumasë",
    jumuahFollowDhuhr = "Sipas Drekës",
    khutbahDurationLabel = "Kohëzgjatja e hutbes (min)",
    khutbahTitle = "Koha e Hutbes",
    khutbahQuotes = listOf(
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Nëse i thua shokut tënd **'hesht'** ditën e xhuma, ndërsa imami mban hutben, **ke folur kotë**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "O ju që besuat, kur bëhet thirrja për namaz ditën e xhuma, **nxitoni drejt përmendjes së Allahut dhe lëreni tregtinë**! Kjo është më mirë për ju, nëse e dini.",
            "Kur'ani, El-Xhumua 9",
            arabic = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ ۚ ذَلِكُمْ خَيْرٌ لَكُمْ إِن كُنتُمْ تَعْلَمُونَ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Dita më e mirë në të cilën ka lindur dielli është dita e xhuma**: atë ditë u krijua Ademi, atë ditë hyri në Xhenet dhe atë ditë doli prej tij.»",
            "Muslimi",
        ),
        KhutbahQuote(
            "Evs ibn Evsi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Dita juaj më e vlefshme është dita e xhuma, andaj **shtoni salavatet për mua** në të, sepse salavatet tuaja më paraqiten mua.»",
            "Ebu Davudi",
        ),
        KhutbahQuote(
            "Selman el-Farisiu r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush pastrohet ditën e xhuma, vishet bukur, shkon herët në xhami dhe **dëgjon me heshtje**, i **falen mëkatet** deri në xhumanë tjetër.»",
            "Buhariu",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Në ditën e xhuma ka **një çast** kur robi musliman, që qëndron në namaz dhe e lut Allahun për diçka, **Ai ia jep atë**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Vërtet, Allahu dhe engjëjt e Tij dërgojnë salavate mbi Pejgamberin. **O besimtarë, dërgoni salavate dhe përshëndetje mbi të!**",
            "Kur'ani, El-Ahzab 56",
            arabic = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
        ),
        KhutbahQuote(
            "E kur të përfundojë namazi, **shpërndahuni nëpër tokë dhe kërkoni nga mirësitë e Allahut**, dhe **përmendeni Allahun shumë**, që të shpëtoni!",
            "Kur'ani, El-Xhumua 10",
            arabic = "فَإِذَا قُضِيَتِ الصَّلَاةُ فَانتَشِرُوا فِي الْأَرْضِ وَابْتَغُوا مِن فَضْلِ اللَّهِ وَاذْكُرُوا اللَّهَ كَثِيرًا لَعَلَّكُمْ تُفْلِحُونَ",
        ),
        KhutbahQuote(
            "Ebu Seid el-Hudriu r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush e lexon **suren El-Kehf** ditën e xhuma, **i ndriçohet drita** mes dy xhumave.»",
            "Hakimi & Bejhekiu",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Pesë namazet, xhumaja deri në xhuma dhe ramazani deri në ramazan **i shlyejnë mëkatet** mes tyre, nëse u shmangesh mëkateve të mëdha.»",
            "Muslimi",
        ),
    ),
    announcementsLabel = "Njoftimet",
    announcementLabel = "Njoftimi",
    setupTitle = "Mirë se vini",
    setupStart = "Fillo",
    back = "Kthehu",
    languageName = "Shqip",
    done = "Mbyll",
    save = "Ruaj",
    cancel = "Anulo",
    minutesShort = "min",
    hijriMonths = listOf(
        "Muharrem", "Safer", "Rebiul-evvel", "Rebiul-ahir",
        "Xhumadel-ula", "Xhumadel-uhra", "Rexheb", "Shaban",
        "Ramazan", "Shevval", "Dhul-kade", "Dhul-hixhe",
    ),
    eventNames = mapOf(
        "ramadan_start" to "Fillimi i Ramazanit",
        "laylat_al_qadr" to "Nata e Kadrit",
        "eid_al_fitr" to "Fitër Bajrami",
        "eid_al_adha" to "Kurban Bajrami",
        "islamic_new_year_1448" to "Viti i Ri Islam",
    ),
)

private val EN = Strings(
    locale = Locale.ENGLISH,
    prayerNames = mapOf(
        PrayerKey.IMSAK to "Imsak",
        PrayerKey.FAJR to "Fajr",
        PrayerKey.SUNRISE to "Sunrise",
        PrayerKey.DHUHR to "Dhuhr",
        PrayerKey.ASR to "Asr",
        PrayerKey.MAGHRIB to "Maghrib",
        PrayerKey.ISHA to "Isha",
    ),
    fridayDhuhrName = "Jumu'ah",
    announceTemplate = "Time for %s prayer",
    announceNames = mapOf(
        PrayerKey.FAJR to "Fajr",
        PrayerKey.DHUHR to "Dhuhr",
        PrayerKey.ASR to "Asr",
        PrayerKey.MAGHRIB to "Maghrib",
        PrayerKey.ISHA to "Isha",
    ),
    fridayDhuhrAnnounceName = "Jumu'ah",
    fridaySalawat = Reminder(
        "All-llahumme sal-li ve sel-lim ala nebijjina Muhammed",
        "O Allah, bestow Your mercy and peace upon our Prophet Muhammad.",
    ),
    reminders = listOf(
        Reminder(
            "Estagfirullahe ve etubu ilejhi",
            "I seek Allah's forgiveness and to Him I repent",
        ),
        Reminder(
            "Subhanall-llah",
            "Glory be to Allah",
        ),
        Reminder(
            "SubhanAll-llahi ve bihamdihi",
            "Glory be to Allah and His is all praise",
        ),
        Reminder(
            "SubhanAllahil Adhim",
            "Glory be to Allah, the Most Great",
        ),
        Reminder(
            "Elhamdulilah",
            "All praise is due to Allah",
        ),
        Reminder(
            "Allahu Ekber",
            "Allah is the Greatest",
        ),
        Reminder(
            "La ilahe il-lallah",
            "There is no god worthy of worship except Allah",
        ),
        Reminder(
            "All-llahumme sal-li ve sel-lim ala nebijjina Muhammed",
            "O Allah, bestow Your mercy and peace upon our Prophet Muhammad.",
        ),
        Reminder(
            "La havle ve la kuv-vete il-la bil-lah",
            "There is no power nor strength except with Allah",
        ),
    ),
    nextPrayerIn = "until",
    noticeTexts = mapOf(
        NoticeKey.FRIDAY_KAHF to NoticeText(
            "Surah Al-Kahf",
            "It is Sunnah to read Surah Al-Kahf on Friday.",
        ),
        NoticeKey.FRIDAY_SUNNAH to NoticeText(
            "Friday Sunnahs",
            "Ghusl, fine clothes, perfume, and going early to the mosque.",
        ),
        NoticeKey.FRIDAY_DUA to NoticeText(
            "Hour of accepted du'a",
            "On Friday there is an hour when du'a is accepted - increase du'a and salawat.",
        ),
        NoticeKey.FRIDAY_KHUTBAH to NoticeText(
            "Silence during the khutbah",
            "While the imam delivers the khutbah, remain silent and listen.",
        ),
        NoticeKey.DUHA to NoticeText(
            "Duha prayer",
            "It is now time for the Duha prayer - at least 2 rak'ahs.",
        ),
        NoticeKey.MORNING_DHIKR to NoticeText(
            "Morning adhkar",
            "Time for the morning remembrance of Allah.",
        ),
        NoticeKey.EVENING_DHIKR to NoticeText(
            "Evening adhkar",
            "Time for the evening remembrance of Allah.",
        ),
        NoticeKey.FAST_MONDAY to NoticeText(
            "Sunnah fast",
            "Tomorrow is Monday - fasting it's Sunnah.",
        ),
        NoticeKey.FAST_THURSDAY to NoticeText(
            "Sunnah fast",
            "Tomorrow is Thursday - fasting it's Sunnah.",
        ),
        NoticeKey.FAST_WHITE_DAYS to NoticeText(
            "The White Days",
            "Tomorrow is day %d of the lunar month - fasting is Sunnah.",
        ),
        NoticeKey.CUSTOM to NoticeText("Announcement", ""),
    ),
    settingsTitle = "Settings",
    mosqueNameLabel = "Mosque name",
    placeLabel = "Place (subtitle)",
    cityLabel = "City (prayer times)",
    orientationLabel = "Screen orientation",
    languageLabel = "Language",
    orientationNames = mapOf(
        DisplayOrientation.LANDSCAPE to "Landscape",
        DisplayOrientation.PORTRAIT to "Portrait",
        DisplayOrientation.PORTRAIT_REVERSED to "Portrait (reversed)",
        DisplayOrientation.LANDSCAPE_FLIPPED to "Landscape (flipped)",
    ),
    themeLabel = "Theme",
    themeNames = mapOf(
        AppTheme.DARK to "Dark",
        AppTheme.BLACK to "Black",
        AppTheme.EMERALD to "Emerald",
        AppTheme.MIDNIGHT to "Midnight",
        AppTheme.BURGUNDY to "Burgundy",
        AppTheme.LIGHT to "Light",
        AppTheme.GOLD to "Gold",
        AppTheme.BLUE to "Blue",
        AppTheme.GREEN to "Green",
    ),
    nightModeLabel = "Night energy saver (until Imsak)",
    nightModeNames = mapOf(
        NightMode.OFF to "Off",
        NightMode.AT_ISHA to "From Isha",
        NightMode.AFTER_15 to "Isha +15 min",
        NightMode.AFTER_30 to "Isha +30 min",
        NightMode.AFTER_45 to "Isha +45 min",
        NightMode.AFTER_60 to "Isha +1 h",
    ),
    mosqueSectionLabel = "Mosque",
    displaySectionLabel = "Display",
    fridaySectionLabel = "Friday",
    lectureSectionLabel = "Weekly lecture",
    lectureTitleLabel = "Title",
    lectureDayLabel = "Day",
    lecturePrayerLabel = "After prayer",
    lectureDayNames = mapOf(
        LectureDay.OFF to "Off",
        LectureDay.MONDAY to "Monday",
        LectureDay.TUESDAY to "Tuesday",
        LectureDay.WEDNESDAY to "Wednesday",
        LectureDay.THURSDAY to "Thursday",
        LectureDay.FRIDAY to "Friday",
        LectureDay.SATURDAY to "Saturday",
        LectureDay.SUNDAY to "Sunday",
    ),
    lectureBody = "Today after the %s prayer (%s)",
    adjustSectionLabel = "Time adjustments (min)",
    adjustResetLabel = "Reset all to 0",
    hijriOffsetLabel = "Hijri date (days)",
    jumuahLabel = "Jumu'ah time",
    jumuahFollowDhuhr = "Same as Dhuhr",
    khutbahDurationLabel = "Khutbah duration (min)",
    khutbahTitle = "Khutbah Time",
    khutbahQuotes = listOf(
        KhutbahQuote(
            "Abu Hurairah (r.a.) narrated that the Prophet ﷺ said: \"If you say to your companion **'be quiet'** on Friday while the imam is delivering the khutbah, **you have spoken in vain**.\"",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "O you who believe! When the call to prayer is made on Friday, **hasten to the remembrance of Allah and leave off trade**. That is better for you, if only you knew.",
            "Qur'an, Al-Jumu'ah 9",
            arabic = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ ۚ ذَلِكُمْ خَيْرٌ لَكُمْ إِن كُنتُمْ تَعْلَمُونَ",
        ),
        KhutbahQuote(
            "Abu Hurairah (r.a.) narrated that the Prophet ﷺ said: \"**The best day on which the sun has risen is Friday**: on it Adam was created, on it he entered Paradise, and on it he was expelled from it.\"",
            "Muslim",
        ),
        KhutbahQuote(
            "Aws ibn Aws (r.a.) narrated that the Prophet ﷺ said: \"The most excellent of your days is Friday, so **increase your salawat upon me** on it, for your salawat are presented to me.\"",
            "Abu Dawud",
        ),
        KhutbahQuote(
            "Salman al-Farisi (r.a.) narrated that the Prophet ﷺ said: \"Whoever performs ghusl on Friday, dresses well, goes early to the mosque and **listens in silence**, is **forgiven** what is until the next Friday.\"",
            "Bukhari",
        ),
        KhutbahQuote(
            "Abu Hurairah (r.a.) narrated that the Prophet ﷺ said: \"On Friday there is **an hour** when no Muslim servant stands in prayer asking Allah for something but **He grants it to him**.\"",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Indeed, Allah and His angels send blessings upon the Prophet. **O you who believe, send blessings upon him and greet him with peace!**",
            "Qur'an, Al-Ahzab 56",
            arabic = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
        ),
        KhutbahQuote(
            "And when the prayer has ended, **disperse throughout the land and seek from the bounty of Allah**, and **remember Allah much**, that you may succeed.",
            "Qur'an, Al-Jumu'ah 10",
            arabic = "فَإِذَا قُضِيَتِ الصَّلَاةُ فَانتَشِرُوا فِي الْأَرْضِ وَابْتَغُوا مِن فَضْلِ اللَّهِ وَاذْكُرُوا اللَّهَ كَثِيرًا لَعَلَّكُمْ تُفْلِحُونَ",
        ),
        KhutbahQuote(
            "Abu Sa'id al-Khudri (r.a.) narrated that the Prophet ﷺ said: \"Whoever recites **Surah Al-Kahf** on Friday, **a light will shine for him** between the two Fridays.\"",
            "Hakim & Bayhaqi",
        ),
        KhutbahQuote(
            "Abu Hurairah (r.a.) narrated that the Prophet ﷺ said: \"The five prayers, Friday to Friday, and Ramadan to Ramadan **expiate the sins** between them, as long as major sins are avoided.\"",
            "Muslim",
        ),
    ),
    announcementsLabel = "Announcements",
    announcementLabel = "Announcement",
    setupTitle = "Welcome",
    setupStart = "Start",
    back = "Back",
    languageName = "English",
    done = "Done",
    save = "Save",
    cancel = "Cancel",
    minutesShort = "min",
    hijriMonths = listOf(
        "Muharram", "Safar", "Rabi' al-awwal", "Rabi' al-thani",
        "Jumada al-ula", "Jumada al-akhirah", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qa'dah", "Dhu al-Hijjah",
    ),
    eventNames = mapOf(
        "ramadan_start" to "Start of Ramadan",
        "laylat_al_qadr" to "Laylat al-Qadr",
        "eid_al_fitr" to "Eid al-Fitr",
        "eid_al_adha" to "Eid al-Adha",
        "islamic_new_year_1448" to "Islamic New Year",
    ),
)

private val TR = Strings(
    locale = Locale.forLanguageTag("tr"),
    prayerNames = mapOf(
        PrayerKey.IMSAK to "İmsak",
        PrayerKey.FAJR to "Sabah",
        PrayerKey.SUNRISE to "Güneş",
        PrayerKey.DHUHR to "Öğle",
        PrayerKey.ASR to "İkindi",
        PrayerKey.MAGHRIB to "Akşam",
        PrayerKey.ISHA to "Yatsı",
    ),
    fridayDhuhrName = "Cuma",
    announceTemplate = "%s Namazı Vakti",
    announceNames = mapOf(
        PrayerKey.FAJR to "Sabah",
        PrayerKey.DHUHR to "Öğle",
        PrayerKey.ASR to "İkindi",
        PrayerKey.MAGHRIB to "Akşam",
        PrayerKey.ISHA to "Yatsı",
    ),
    fridayDhuhrAnnounceName = "Cuma",
    fridaySalawat = Reminder(
        "Allahümme salli ve sellim alâ nebiyyinâ Muhammed",
        "Allah'ım, Peygamberimiz Muhammed'e salât ve selam eyle.",
    ),
    reminders = listOf(
        Reminder(
            "Estağfirullahe ve etûbu ileyh",
            "Allah'tan bağışlanma diler ve O'na tövbe ederim",
        ),
        Reminder(
            "Sübhanallah",
            "Allah her türlü eksiklikten uzaktır",
        ),
        Reminder(
            "Sübhanallahi ve bihamdihi",
            "Allah'ı hamd ile tesbih ederim",
        ),
        Reminder(
            "Sübhanallahil-Azîm",
            "Yüce Allah her türlü eksiklikten uzaktır",
        ),
        Reminder(
            "Elhamdülillah",
            "Hamd Allah'a mahsustur",
        ),
        Reminder(
            "Allahu Ekber",
            "Allah en büyüktür",
        ),
        Reminder(
            "La ilahe illallah",
            "Allah'tan başka ilah yoktur",
        ),
        Reminder(
            "Allahümme salli ve sellim alâ nebiyyinâ Muhammed",
            "Allah'ım, Peygamberimiz Muhammed'e salât ve selam eyle.",
        ),
        Reminder(
            "La havle ve la kuvvete illa billah",
            "Güç ve kuvvet ancak Allah'tandır",
        ),
    ),
    nextPrayerIn = "kaldı:",
    noticeTexts = mapOf(
        NoticeKey.FRIDAY_KAHF to NoticeText(
            "Kehf Suresi",
            "Cuma günü Kehf Suresi'ni okumak sünnettir.",
        ),
        NoticeKey.FRIDAY_SUNNAH to NoticeText(
            "Cuma sünnetleri",
            "Gusül, güzel giyinmek, güzel koku ve camiye erken gitmek.",
        ),
        NoticeKey.FRIDAY_DUA to NoticeText(
            "Duaların kabul anı",
            "Cuma günü duaların kabul edildiği bir an vardır - dua ve salavatı çoğaltın.",
        ),
        NoticeKey.FRIDAY_KHUTBAH to NoticeText(
            "Hutbede sessizlik",
            "İmam hutbe okurken susup dikkatle dinleyin.",
        ),
        NoticeKey.DUHA to NoticeText(
            "Kuşluk (Duha) Namazı",
            "Şimdi kuşluk namazı vakti - en az 2 rekât.",
        ),
        NoticeKey.MORNING_DHIKR to NoticeText(
            "Sabah zikirleri",
            "Sabah zikirlerinin vakti.",
        ),
        NoticeKey.EVENING_DHIKR to NoticeText(
            "Akşam zikirleri",
            "Akşam zikirlerinin vakti.",
        ),
        NoticeKey.FAST_MONDAY to NoticeText(
            "Sünnet oruç",
            "Yarın pazartesi - orucunu tutmak sünnettir.",
        ),
        NoticeKey.FAST_THURSDAY to NoticeText(
            "Sünnet oruç",
            "Yarın perşembe - orucunu tutmak sünnettir.",
        ),
        NoticeKey.FAST_WHITE_DAYS to NoticeText(
            "Beyaz Günler",
            "Yarın hicri ayın %d. günü - oruç tutmak sünnettir.",
        ),
        NoticeKey.CUSTOM to NoticeText("Duyuru", ""),
    ),
    settingsTitle = "Ayarlar",
    mosqueNameLabel = "Cami adı",
    placeLabel = "Yer (alt başlık)",
    cityLabel = "Şehir (namaz vakitleri)",
    orientationLabel = "Ekran yönü",
    languageLabel = "Dil",
    orientationNames = mapOf(
        DisplayOrientation.LANDSCAPE to "Yatay",
        DisplayOrientation.PORTRAIT to "Dikey",
        DisplayOrientation.PORTRAIT_REVERSED to "Dikey (ters)",
        DisplayOrientation.LANDSCAPE_FLIPPED to "Yatay (ters)",
    ),
    themeLabel = "Görünüm",
    themeNames = mapOf(
        AppTheme.DARK to "Koyu",
        AppTheme.BLACK to "Siyah",
        AppTheme.EMERALD to "Zümrüt",
        AppTheme.MIDNIGHT to "Gece mavisi",
        AppTheme.BURGUNDY to "Bordo",
        AppTheme.LIGHT to "Açık",
        AppTheme.GOLD to "Altın",
        AppTheme.BLUE to "Mavi",
        AppTheme.GREEN to "Yeşil",
    ),
    nightModeLabel = "Gece enerji tasarrufu (İmsak'a kadar)",
    nightModeNames = mapOf(
        NightMode.OFF to "Kapalı",
        NightMode.AT_ISHA to "Yatsı'dan itibaren",
        NightMode.AFTER_15 to "Yatsı +15 dk",
        NightMode.AFTER_30 to "Yatsı +30 dk",
        NightMode.AFTER_45 to "Yatsı +45 dk",
        NightMode.AFTER_60 to "Yatsı +1 saat",
    ),
    mosqueSectionLabel = "Cami",
    displaySectionLabel = "Ekran",
    fridaySectionLabel = "Cuma",
    lectureSectionLabel = "Haftalık ders",
    lectureTitleLabel = "Başlık",
    lectureDayLabel = "Gün",
    lecturePrayerLabel = "Hangi namazdan sonra",
    lectureDayNames = mapOf(
        LectureDay.OFF to "Kapalı",
        LectureDay.MONDAY to "Pazartesi",
        LectureDay.TUESDAY to "Salı",
        LectureDay.WEDNESDAY to "Çarşamba",
        LectureDay.THURSDAY to "Perşembe",
        LectureDay.FRIDAY to "Cuma",
        LectureDay.SATURDAY to "Cumartesi",
        LectureDay.SUNDAY to "Pazar",
    ),
    lectureBody = "Bugün %s namazından sonra (%s)",
    adjustSectionLabel = "Vakit düzeltmeleri (dk)",
    adjustResetLabel = "Tümünü sıfırla",
    hijriOffsetLabel = "Hicri tarih (gün)",
    jumuahLabel = "Cuma vakti",
    jumuahFollowDhuhr = "Öğle ile aynı",
    khutbahDurationLabel = "Hutbe süresi (dk)",
    khutbahTitle = "Hutbe Vakti",
    khutbahQuotes = listOf(
        KhutbahQuote(
            "Ebû Hüreyre (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Cuma günü imam hutbe okurken arkadaşına **'sus'** desen bile **boş söz söylemiş olursun**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ey iman edenler! Cuma günü namaz için çağrı yapıldığında **Allah'ı anmaya koşun ve alışverişi bırakın**. Eğer bilirseniz bu sizin için daha hayırlıdır.",
            "Kur'an, Cum'a 9",
            arabic = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ ۚ ذَلِكُمْ خَيْرٌ لَكُمْ إِن كُنتُمْ تَعْلَمُونَ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «**Üzerine güneş doğan en hayırlı gün cuma günüdür**: Âdem o gün yaratıldı, o gün cennete girdi ve o gün oradan çıkarıldı.»",
            "Müslim",
        ),
        KhutbahQuote(
            "Evs bin Evs (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Günlerinizin en faziletlisi cuma günüdür; o gün **bana çokça salavat getirin**, çünkü salavatlarınız bana arz edilir.»",
            "Ebû Dâvûd",
        ),
        KhutbahQuote(
            "Selmân-ı Fârisî (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Kim cuma günü gusleder, güzelce giyinir, erkenden camiye gider ve **hutbeyi sessizce dinlerse**, iki cuma arasındaki günahları **bağışlanır**.»",
            "Buhârî",
        ),
        KhutbahQuote(
            "Ebû Hüreyre (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Cuma gününde **öyle bir an** vardır ki, namazda olan Müslüman bir kul o anda Allah'tan ne isterse **Allah ona verir**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Şüphesiz Allah ve melekleri Peygamber'e salât ederler. **Ey iman edenler, siz de ona salât edin ve tam bir teslimiyetle selam verin!**",
            "Kur'an, Ahzâb 56",
            arabic = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
        ),
        KhutbahQuote(
            "Namaz kılınınca **yeryüzüne dağılın ve Allah'ın lütfundan arayın**; **Allah'ı çokça anın** ki kurtuluşa eresiniz.",
            "Kur'an, Cum'a 10",
            arabic = "فَإِذَا قُضِيَتِ الصَّلَاةُ فَانتَشِرُوا فِي الْأَرْضِ وَابْتَغُوا مِن فَضْلِ اللَّهِ وَاذْكُرُوا اللَّهَ كَثِيرًا لَعَلَّكُمْ تُفْلِحُونَ",
        ),
        KhutbahQuote(
            "Ebû Saîd el-Hudrî (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Kim cuma günü **Kehf Sûresi'ni** okursa, iki cuma arasında onun için **bir nur parlar**.»",
            "Hâkim & Beyhakî",
        ),
        KhutbahQuote(
            "Ebû Hüreyre (r.a.) rivayet ediyor; Peygamber (s.a.v.) şöyle buyurdu: «Beş vakit namaz, cumadan cumaya ve ramazandan ramazana, büyük günahlardan kaçınıldığı sürece **aralarındaki günahlara kefarettir**.»",
            "Müslim",
        ),
    ),
    announcementsLabel = "Duyurular",
    announcementLabel = "Duyuru",
    setupTitle = "Hoş geldiniz",
    setupStart = "Başla",
    back = "Geri",
    languageName = "Türkçe",
    done = "Kapat",
    save = "Kaydet",
    cancel = "İptal",
    minutesShort = "dk",
    hijriMonths = listOf(
        "Muharrem", "Safer", "Rebiülevvel", "Rebiülahir",
        "Cemaziyelevvel", "Cemaziyelahir", "Recep", "Şaban",
        "Ramazan", "Şevval", "Zilkade", "Zilhicce",
    ),
    eventNames = mapOf(
        "ramadan_start" to "Ramazan başlangıcı",
        "laylat_al_qadr" to "Kadir Gecesi",
        "eid_al_fitr" to "Ramazan Bayramı",
        "eid_al_adha" to "Kurban Bayramı",
        "islamic_new_year_1448" to "Hicri Yılbaşı",
    ),
)

private val BS = Strings(
    locale = Locale.forLanguageTag("bs"),
    prayerNames = mapOf(
        PrayerKey.IMSAK to "Imsak",
        PrayerKey.FAJR to "Sabah",
        PrayerKey.SUNRISE to "Izlazak sunca",
        PrayerKey.DHUHR to "Podne",
        PrayerKey.ASR to "Ikindija",
        PrayerKey.MAGHRIB to "Akšam",
        PrayerKey.ISHA to "Jacija",
    ),
    fridayDhuhrName = "Džuma",
    announceTemplate = "Vrijeme namaza: %s",
    announceNames = mapOf(
        PrayerKey.FAJR to "Sabah",
        PrayerKey.DHUHR to "Podne",
        PrayerKey.ASR to "Ikindija",
        PrayerKey.MAGHRIB to "Akšam",
        PrayerKey.ISHA to "Jacija",
    ),
    fridayDhuhrAnnounceName = "Džuma",
    fridaySalawat = Reminder(
        "Allahumme salli ve sellim ala nebijjina Muhammed",
        "Allahu moj, blagoslovi i spasi našeg Vjerovjesnika Muhammeda.",
    ),
    reminders = listOf(
        Reminder(
            "Estagfirullahe ve etubu ilejhi",
            "Tražim oprost od Allaha i Njemu se kajem",
        ),
        Reminder(
            "Subhanallah",
            "Slavljen neka je Allah",
        ),
        Reminder(
            "Subhanallahi ve bihamdihi",
            "Slavljen je Allah i Njemu pripada hvala",
        ),
        Reminder(
            "Subhanallahil-Azim",
            "Slavljen je Allah Veličanstveni",
        ),
        Reminder(
            "Elhamdulillah",
            "Hvala pripada Allahu",
        ),
        Reminder(
            "Allahu Ekber",
            "Allah je najveći",
        ),
        Reminder(
            "La ilahe illallah",
            "Nema boga osim Allaha",
        ),
        Reminder(
            "Allahumme salli ve sellim ala nebijjina Muhammed",
            "Allahu moj, blagoslovi i spasi našeg Vjerovjesnika Muhammeda.",
        ),
        Reminder(
            "La havle ve la kuvvete illa billah",
            "Nema snage ni moći osim kod Allaha",
        ),
    ),
    nextPrayerIn = "do",
    noticeTexts = mapOf(
        NoticeKey.FRIDAY_KAHF to NoticeText(
            "Sura El-Kehf",
            "Sunnet je petkom učiti suru El-Kehf.",
        ),
        NoticeKey.FRIDAY_SUNNAH to NoticeText(
            "Sunneti džume",
            "Gusul, lijepa odjeća, miris i rano odlaženje u džamiju.",
        ),
        NoticeKey.FRIDAY_DUA to NoticeText(
            "Čas primanja dove",
            "Petkom postoji čas kada se dova prima - činite više dove i salavata.",
        ),
        NoticeKey.FRIDAY_KHUTBAH to NoticeText(
            "Šutnja tokom hutbe",
            "Dok imam drži hutbu, šuti i pažljivo slušaj.",
        ),
        NoticeKey.DUHA to NoticeText(
            "Duha-namaz",
            "Sada je vrijeme duha-namaza - najmanje 2 rekata.",
        ),
        NoticeKey.MORNING_DHIKR to NoticeText(
            "Jutarnji zikr",
            "Vrijeme jutarnjeg zikra.",
        ),
        NoticeKey.EVENING_DHIKR to NoticeText(
            "Večernji zikr",
            "Vrijeme večernjeg zikra.",
        ),
        NoticeKey.FAST_MONDAY to NoticeText(
            "Sunnet post",
            "Sutra je ponedjeljak - postiti ga je sunnet.",
        ),
        NoticeKey.FAST_THURSDAY to NoticeText(
            "Sunnet post",
            "Sutra je četvrtak - postiti ga je sunnet.",
        ),
        NoticeKey.FAST_WHITE_DAYS to NoticeText(
            "Bijeli dani",
            "Sutra je %d. dan hidžretskog mjeseca - post je sunnet.",
        ),
        NoticeKey.CUSTOM to NoticeText("Obavještenje", ""),
    ),
    settingsTitle = "Postavke",
    mosqueNameLabel = "Naziv džamije",
    placeLabel = "Mjesto (podnaslov)",
    cityLabel = "Grad (namaska vremena)",
    orientationLabel = "Orijentacija ekrana",
    languageLabel = "Jezik",
    orientationNames = mapOf(
        DisplayOrientation.LANDSCAPE to "Horizontalno",
        DisplayOrientation.PORTRAIT to "Vertikalno",
        DisplayOrientation.PORTRAIT_REVERSED to "Vertikalno (obrnuto)",
        DisplayOrientation.LANDSCAPE_FLIPPED to "Horizontalno (obrnuto)",
    ),
    themeLabel = "Izgled",
    themeNames = mapOf(
        AppTheme.DARK to "Tamna",
        AppTheme.BLACK to "Crna",
        AppTheme.EMERALD to "Smaragdna",
        AppTheme.MIDNIGHT to "Tamnoplava",
        AppTheme.BURGUNDY to "Bordo",
        AppTheme.LIGHT to "Svijetla",
        AppTheme.GOLD to "Zlatna",
        AppTheme.BLUE to "Plava",
        AppTheme.GREEN to "Zelena",
    ),
    nightModeLabel = "Noćna ušteda energije (do imsaka)",
    nightModeNames = mapOf(
        NightMode.OFF to "Isključeno",
        NightMode.AT_ISHA to "Od jacije",
        NightMode.AFTER_15 to "Jacija +15 min",
        NightMode.AFTER_30 to "Jacija +30 min",
        NightMode.AFTER_45 to "Jacija +45 min",
        NightMode.AFTER_60 to "Jacija +1 h",
    ),
    mosqueSectionLabel = "Džamija",
    displaySectionLabel = "Ekran",
    fridaySectionLabel = "Džuma",
    lectureSectionLabel = "Sedmično predavanje",
    lectureTitleLabel = "Naslov",
    lectureDayLabel = "Dan",
    lecturePrayerLabel = "Poslije namaza",
    lectureDayNames = mapOf(
        LectureDay.OFF to "Isključeno",
        LectureDay.MONDAY to "Ponedjeljak",
        LectureDay.TUESDAY to "Utorak",
        LectureDay.WEDNESDAY to "Srijeda",
        LectureDay.THURSDAY to "Četvrtak",
        LectureDay.FRIDAY to "Petak",
        LectureDay.SATURDAY to "Subota",
        LectureDay.SUNDAY to "Nedjelja",
    ),
    lectureBody = "Danas poslije %s namaza (%s)",
    adjustSectionLabel = "Korekcija vremena (min)",
    adjustResetLabel = "Vrati sve na 0",
    hijriOffsetLabel = "Hidžretski datum (dana)",
    jumuahLabel = "Vrijeme džume",
    jumuahFollowDhuhr = "Kao podne",
    khutbahDurationLabel = "Trajanje hutbe (min)",
    khutbahTitle = "Vrijeme hutbe",
    khutbahQuotes = listOf(
        KhutbahQuote(
            "Ebu Hurejre, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Ako u petak, dok imam drži hutbu, kažeš svome drugu **'šuti'**, **rekao si suvišan govor**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "O vjernici, kada se u petak na molitvu pozove, **požurite ka spominjanju Allaha i ostavite trgovinu**! To vam je bolje, ako znate.",
            "Kur'an, El-Džumu'a 9",
            arabic = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ ۚ ذَلِكُمْ خَيْرٌ لَكُمْ إِن كُنتُمْ تَعْلَمُونَ",
        ),
        KhutbahQuote(
            "Ebu Hurejre, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «**Najbolji dan u kojem je sunce izašlo jest petak**: u njemu je Adem stvoren, u njemu je uveden u Džennet i u njemu je iz njega izveden.»",
            "Muslim",
        ),
        KhutbahQuote(
            "Evs ibn Evs, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Najvredniji vaš dan jest petak, pa **donosite što više salavata na mene** u njemu, jer se vaši salavati meni predočavaju.»",
            "Ebu Davud",
        ),
        KhutbahQuote(
            "Selman el-Farisi, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Ko se petkom okupa, lijepo obuče, porani u džamiju i **sluša u tišini**, **oprošteno mu je** do sljedeće džume.»",
            "Buharija",
        ),
        KhutbahQuote(
            "Ebu Hurejre, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Petkom postoji **jedan čas** u kojem musliman, koji stoji u namazu i moli Allaha za nešto, **to i dobije**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Zaista Allah i Njegovi meleki donose salavate na Vjerovjesnika. **O vjernici, i vi donosite salavate na njega i šaljite mu selam!**",
            "Kur'an, El-Ahzab 56",
            arabic = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
        ),
        KhutbahQuote(
            "A kada se namaz obavi, **raziđite se po zemlji i tražite Allahovu blagodat**, i **mnogo Allaha spominjite** da biste uspjeli.",
            "Kur'an, El-Džumu'a 10",
            arabic = "فَإِذَا قُضِيَتِ الصَّلَاةُ فَانتَشِرُوا فِي الْأَرْضِ وَابْتَغُوا مِن فَضْلِ اللَّهِ وَاذْكُرُوا اللَّهَ كَثِيرًا لَعَلَّكُمْ تُفْلِحُونَ",
        ),
        KhutbahQuote(
            "Ebu Seid el-Hudri, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Ko petkom prouči **suru El-Kehf**, **obasja ga svjetlo** između dvije džume.»",
            "Hakim & Bejheki",
        ),
        KhutbahQuote(
            "Ebu Hurejre, r.a., prenosi da je Poslanik, s.a.v.s., rekao: «Pet namaza, džuma do džume i ramazan do ramazana **brišu grijehe** između njih, ako se izbjegavaju veliki grijesi.»",
            "Muslim",
        ),
    ),
    announcementsLabel = "Obavještenja",
    announcementLabel = "Obavještenje",
    setupTitle = "Dobrodošli",
    setupStart = "Počni",
    back = "Nazad",
    languageName = "Bosanski",
    done = "Zatvori",
    save = "Sačuvaj",
    cancel = "Otkaži",
    minutesShort = "min",
    hijriMonths = listOf(
        "Muharrem", "Safer", "Rebiul-evvel", "Rebiul-ahir",
        "Džumadel-ula", "Džumadel-uhra", "Redžeb", "Ša'ban",
        "Ramazan", "Ševval", "Zul-ka'de", "Zul-hidždže",
    ),
    eventNames = mapOf(
        "ramadan_start" to "Početak ramazana",
        "laylat_al_qadr" to "Lejletul-kadr",
        "eid_al_fitr" to "Ramazanski bajram",
        "eid_al_adha" to "Kurban-bajram",
        "islamic_new_year_1448" to "Nova hidžretska godina",
    ),
)

fun stringsFor(language: AppLanguage): Strings = when (language) {
    AppLanguage.SQ -> SQ
    AppLanguage.EN -> EN
    AppLanguage.TR -> TR
    AppLanguage.BS -> BS
}
