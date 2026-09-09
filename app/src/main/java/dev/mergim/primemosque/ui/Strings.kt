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

/**
 * The dua recited once the muezzin has finished the adhan, shown on its own
 * screen before the board returns to the prayer table: the Arabic original,
 * a transliteration the congregation can read along, and the translation.
 */
data class AdhanDua(
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val source: String,
)
data class Strings(
    val locale: Locale,
    val prayerNames: Map<PrayerKey, String>,
    val fridayDhuhrName: String,
    // Prayer names as used after [nextPrayerIn] in the countdown, in the
    // grammatical form the language needs there (e.g. Albanian indefinite
    // "deri në Drekë", Bosnian genitive "do ikindije").
    val countdownNames: Map<PrayerKey, String>,
    val fridayDhuhrCountdownName: String,
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
    // Weekly automatic theme-family rotation.
    val themeRotationLabel: String,
    val themeRotationOff: String,
    val themeRotationWeekly: String,
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
    // Daily wisdom breaks: verses and hadiths that briefly replace the
    // prayer table on a fixed cycle during the day.
    val dailyQuotesTitle: String,
    val dailyQuotesLabel: String,
    val dailyQuotes: List<KhutbahQuote>,
    // Generic toggle values for boolean settings.
    val switchOn: String,
    val switchOff: String,
    // Label for the web-portal pairing code in the settings footer.
    val portalCodeLabel: String,
    // Secondary board language (mixed congregations), alternating in blocks.
    val secondaryLanguageLabel: String,
    // Ramadan mode: pinned iftar/imsak banner and Iftar countdown.
    val ramadanTitle: String,
    val iftarLabel: String,
    val iftarCountdownName: String,
    val ramadanModeLabel: String,
    // Full-screen adhan sequence: the countdown before the call, the adhan
    // itself, then the dua that follows it.
    val adhanTemplate: String,
    val preAdhanTitle: String,
    val preAdhanNote: String,
    val adhanDuaTitle: String,
    val adhanDua: AdhanDua,
    val adhanSectionLabel: String,
    val adhanSequenceLabel: String,
    val preAdhanLabel: String,
    val adhanDurationLabel: String,
    val adhanDuaDurationLabel: String,
    val secondsShort: String,
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

    // Warning shown when the TV clock is provably wrong after a power cut.
    val clockWarningTitle: String,
    val clockWarningBody: String,
    val clockWarningButton: String,
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
    countdownNames = mapOf(
        PrayerKey.FAJR to "Sabah",
        PrayerKey.DHUHR to "Drekë",
        PrayerKey.ASR to "Ikindi",
        PrayerKey.MAGHRIB to "Aksham",
        PrayerKey.ISHA to "Jaci",
    ),
    fridayDhuhrCountdownName = "Xhuma",
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
        AppTheme.MUSHAF to "Mushaf",
        AppTheme.MUSHAF_DARK to "Mushaf (e errët)",
        AppTheme.ZAYTUN to "Ulli",
        AppTheme.ZAYTUN_DARK to "Ulli (e errët)",
        AppTheme.NILA to "Indigo",
        AppTheme.NILA_DARK to "Indigo (e errët)",
        AppTheme.HIBR to "Bojë",
        AppTheme.HIBR_DARK to "Bojë (e errët)",
    ),
    themeRotationLabel = "Ndërrimi automatik i pamjes",
    themeRotationOff = "Fikur",
    themeRotationWeekly = "Çdo javë",
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
            "O besimtarë, kur të thirreni për (të falur) namazin (e xhumasë) në ditën e premte, **nxitoni për ta përmendur Allahun dhe pezulloni tregtinë**! Kjo, që ta dini, është më mirë për ju!",
            "Kur'an, El-Xhumua 9",
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
            "Vërtet, Allahu e bekon të Dërguarin dhe engjëjt e Tij luten për atë. **O besimtarë, lutuni për atë dhe përshëndeteni me \"selam\"!**",
            "Kur'an, El-Ahzab 56",
            arabic = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
        ),
        KhutbahQuote(
            "Dhe, kur të përfundojë namazi, atëherë **shpërndahuni nëpër tokë, kërkoni nga mirësitë e Allahut** dhe **përmendeni shumë Allahun**, në mënyrë që të shpëtoni.",
            "Kur'an, El-Xhumua 10",
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
    dailyQuotesTitle = "Nga Kur'ani dhe Sunneti",
    dailyQuotesLabel = "Ajete dhe hadithe gjatë ditës",
    dailyQuotes = listOf(
        // Rijadu Salihin (short narrations), at the imam's request, at the
        // head of the collection.
        KhutbahQuote(
            "Omeri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Veprat vlerësohen " +
                "sipas qëllimit**, dhe secili shpërblehet sipas qëllimit që ka pasur.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ebu Dherri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Ki frikë Allahun " +
                "kudo që të gjendesh**; pas veprës së keqe bëj një të mirë, sepse ajo e " +
                "shlyen; dhe **sillu me njerëzit me moral të mirë**.»",
            "Rijadu Salihin • Tirmidhiu",
        ),
        KhutbahQuote(
            "Ebu Dherri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Mos e përbuz " +
                "asnjë të mirë**, edhe sikur ta takosh vëllain tënd **me fytyrë të " +
                "buzëqeshur**.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon: Një njeri pyeti: \"O i Dërguar i Allahut, kujt " +
                "t'i bëj mirë më së parë?\" Tha: «**Nënës tënde**», pastaj: «**Nënës " +
                "tënde**», pastaj: «**Nënës tënde**», pastaj: «**Babait tënd**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush beson në " +
                "Allahun dhe në Ditën e Fundit, **le t'i bëjë mirë fqinjit të vet**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Kur Allahu " +
                "dëshiron të mirën për një rob, e sprovon atë**.»",
            "Rijadu Salihin • Buhariu",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Dy fjalë **të " +
                "lehta për gjuhën, të rënda në peshojë** dhe të dashura për të " +
                "Gjithëmëshirshmin: SubhanAllahi ve bihamdihi, SubhanAllahil-Adhim.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'udi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Sinqeriteti udhëzon në mirësi, dhe mirësia udhëzon në Xhenet**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "E kur të qetësoheni, atëherë faleni namazin në mënyrë të rregullt, pse " +
                "**namazi është obligim (për kohë) i caktuar për besimtarët**.",
            "Kur'an, En-Nisa 103",
            arabic = "فَإِذَا ٱطْمَأْنَنتُمْ فَأَقِيمُوا۟ ٱلصَّلَوٰةَ ۚ إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَٰبًۭا مَّوْقُوتًۭا",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'udi r.a. tregon: E pyeta Pejgamberin s.a.v.s.: " +
                "\"Cila vepër është më e dashur tek Allahu?\" Tha: «**Namazi në kohën e vet**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ti lexo atë që po të shpallet nga libri (Kur'ani), fal namazin, **vërtet " +
                "namazi largon nga të shëmtuarat dhe të irituarat**, e përmendja e " +
                "All-llahut është më e madhja.",
            "Kur'an, El-Ankebut 45",
            arabic = "ٱتْلُ مَآ أُوحِىَ إِلَيْكَ مِنَ ٱلْكِتَٰبِ وَأَقِمِ ٱلصَّلَوٰةَ ۖ إِنَّ ٱلصَّلَوٰةَ تَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ ۗ وَلَذِكْرُ ٱللَّهِ أَكْبَرُ",
        ),
        KhutbahQuote(
            "Abdullah ibn Amri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Më të mirët prej jush janë ata që kanë moralin më të mirë**.»",
            "Buhariu",
        ),
        KhutbahQuote(
            "**E kush punoi ndonjë të mirë, që peshon sa grimca, atë do ta gjejë.** " +
                "Dhe kush punoi ndonjë të keqe sa grimca, atë do ta gjejë.",
            "Kur'an, Ez-Zelzele 7-8",
            arabic = "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًۭا يَرَهُۥ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍۢ شَرًّۭا يَرَهُۥ",
        ),
        KhutbahQuote(
            "Enesi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Askush prej jush nuk " +
                "beson vërtet, derisa **t'ia dojë vëllait të vet atë që ia do vetes**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Të cilët japin kur janë shlirë edhe kur janë në vështirësi dhe që **e " +
                "frenojnë mllefin, që u falin (keqen) njerëzve**, e All-llahu i do " +
                "bamirësit.",
            "Kur'an, Ali Imran 134",
            arabic = "ٱلَّذِينَ يُنفِقُونَ فِى ٱلسَّرَّآءِ وَٱلضَّرَّآءِ وَٱلْكَٰظِمِينَ ٱلْغَيْظَ وَٱلْعَافِينَ عَنِ ٱلنَّاسِ ۗ وَٱللَّهُ يُحِبُّ ٱلْمُحْسِنِينَ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «I fortë nuk është " +
                "ai që i mund të tjerët, por **ai që e përmban veten në zemërim**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Dhe fale namazin në dy skajet e ditës, e edhe në orët e afërta (me " +
                "ditën) të natës. **S'ka dyshim se veprat e mira i shlyejnë ato të " +
                "këqija.**",
            "Kur'an, Hud 114",
            arabic = "وَأَقِمِ ٱلصَّلَوٰةَ طَرَفَىِ ٱلنَّهَارِ وَزُلَفًۭا مِّنَ ٱلَّيْلِ ۚ إِنَّ ٱلْحَسَنَٰتِ يُذْهِبْنَ ٱلسَّيِّـَٔاتِ",
        ),
        KhutbahQuote(
            "Ebu Musa r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush i fal **dy " +
                "namazet e freskëta** (Sabahun dhe Ikindinë), **do të hyjë në Xhenet**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ata që besuan dhe me të përmendur All-llahun, zemrat e tyre qetësohen; " +
                "pra ta dini se **me të përmendur All-llahun zemrat stabilizohen**.",
            "Kur'an, Er-Ra'd 28",
            arabic = "ٱلَّذِينَ ءَامَنُوا۟ وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ ٱللَّهِ ۗ أَلَا بِذِكْرِ ٱللَّهِ تَطْمَئِنُّ ٱلْقُلُوبُ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush beson në " +
                "Allahun dhe në Ditën e Fundit, **le të flasë mirë ose le të heshtë**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "O ju që keni besuar, **kërkoni ndihmë me durim e me të falur**, se " +
                "vërtetë All-llahu është me durimtarët.",
            "Kur'an, El-Bekare 153",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱسْتَعِينُوا۟ بِٱلصَّبْرِ وَٱلصَّلَوٰةِ ۚ إِنَّ ٱللَّهَ مَعَ ٱلصَّٰبِرِينَ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Kush ia largon " +
                "besimtarit një brengë të kësaj bote, **Allahu ia largon atij një brengë " +
                "të Ditës së Kijametit**.»",
            "Muslimi",
        ),
        KhutbahQuote(
            "Pasha kohën! Nuk ka dyshim njeriu është në një humbje të sigurt. **Me " +
                "përjashtim të atyre që besuan, që bënë vepra të mira**, që porositën " +
                "njëri-tjetrin t'i përmbahen të vërtetës dhe që këshilluan njëri-tjetrin " +
                "të jenë të durueshëm.",
            "Kur'an, El-Asr 1-3",
            arabic = "وَٱلْعَصْرِ إِنَّ ٱلْإِنسَٰنَ لَفِى خُسْرٍ إِلَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّٰلِحَٰتِ وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Fjala e mirë është sadaka**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Xheriri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Kush nuk i mëshiron njerëzit, Allahu nuk e mëshiron atë**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Enesi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Lehtësoni e mos vështirësoni, përgëzoni e mos largoni**.»",
            "Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Ebu Maliku el-Esh'ari r.a. tregon se Pejgamberi s.a.v.s. ka thënë: " +
                "«**Pastërtia është gjysma e besimit**.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "**E, pa dyshim se pas vështirësisë është lehtësimi.** Vërtet, pas " +
                "vështirësisë vjen lehtësimi.",
            "Kur'an, El-Inshirah 5-6",
            arabic = "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا إِنَّ مَعَ ٱلْعُسْرِ يُسْرًۭا",
        ),
        KhutbahQuote(
            "Ibn Omeri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Turpi (hajaja) " +
                "është pjesë e besimit**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "**All-llahu nuk ngarkon askënd përtej mundësive të veta**, atij i takon " +
                "ajo që e fitoi dhe atij i bie ajo që e meritoi.",
            "Kur'an, El-Bekare 286",
            arabic = "لَا يُكَلِّفُ ٱللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا ٱكْتَسَبَتْ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Largimi i " +
                "pengesës nga rruga është sadaka**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "E kush iu përmbahet dispozitave të All-llahut, **atij Ai i hap rrugë, " +
                "dhe e furnizon atë prej nga nuk kujton fare**.",
            "Kur'an, Et-Talak 2-3",
            arabic = "وَمَن يَتَّقِ ٱللَّهَ يَجْعَل لَّهُۥ مَخْرَجًۭا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Të drejtat e " +
                "myslimanit ndaj myslimanit janë pesë: **kthimi i selamit, vizita e të " +
                "sëmurit, përcjellja e xhenazes, pranimi i ftesës dhe lutja për atë që " +
                "teshtin**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Dhe (përkujtoni) kur Zoti juaj njoftoi bindshëm: \"**Nëse falënderoni, " +
                "do t'ua shtojë të mirat**, e nëse përbuzni, s'ka dyshim, dënimi Im është " +
                "i vështirë!\"",
            "Kur'an, Ibrahim 7",
            arabic = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِى لَشَدِيدٌۭ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Nuk hyni në " +
                "Xhenet derisa të besoni, dhe nuk besoni derisa ta doni njëri-tjetrin. A " +
                "t'ju tregoj diçka që, po ta bëni, do ta doni njëri-tjetrin? **Përhapeni " +
                "selamin mes jush**.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "**Pra më kujtoni Mua, Unë ju kujtoj juve**, Më falënderoni e mos Më mohoni.",
            "Kur'an, El-Bekare 152",
            arabic = "فَٱذْكُرُونِىٓ أَذْكُرْكُمْ وَٱشْكُرُوا۟ لِى وَلَا تَكْفُرُونِ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Sadakaja nuk " +
                "e pakëson pasurinë**; Allahu ia shton nderin atij që fal; dhe kush " +
                "përulet për Allahun, Allahu e ngre atë.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "**All-llahu urdhëron drejtësi, bamirësi, ndihmë të afërmve**, e ndalon " +
                "nga imoraliteti, nga e neveritura dhe dhuna. Ju këshillon ashtu që të " +
                "merrni mësim.",
            "Kur'an, En-Nahl 90",
            arabic = "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَٰنِ وَإِيتَآئِ ذِى ٱلْقُرْبَىٰ وَيَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ وَٱلْبَغْىِ ۚ يَعِظُكُمْ لَعَلَّكُمْ تَذَكَّرُونَ",
        ),
        KhutbahQuote(
            "Othmani r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Më i miri prej " +
                "jush është ai që e mëson Kur'anin dhe ua mëson të tjerëve**.»",
            "Rijadu Salihin • Buhariu",
        ),
        KhutbahQuote(
            "O ju njerëz, vërtetë Ne ju krijuam prej një mashkulli dhe një femre, ju " +
                "bëmë popuj e fise që të njiheni ndërmjet vete, e **s'ka dyshim se te " +
                "All-llahu më i ndershmi ndër ju është ai që më tepër është ruajtur**.",
            "Kur'an, El-Huxhurat 13",
            arabic = "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَٰكُم مِّن ذَكَرٍۢ وَأُنثَىٰ وَجَعَلْنَٰكُمْ شُعُوبًۭا وَقَبَآئِلَ لِتَعَارَفُوٓا۟ ۚ إِنَّ أَكْرَمَكُمْ عِندَ ٱللَّهِ أَتْقَىٰكُمْ",
        ),
        KhutbahQuote(
            "Ibn Omeri r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Namazi me " +
                "xhemat është njëzet e shtatë herë më i vlefshëm se namazi i falur " +
                "vetëm**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "**S'ka dyshim se besimtarët janë vëllezër**, pra bëni pajtim ndërmjet " +
                "vëllezërve tuaj dhe kinie frikë All-llahun, që të jeni të mëshiruar.",
            "Kur'an, El-Huxhurat 10",
            arabic = "إِنَّمَا ٱلْمُؤْمِنُونَ إِخْوَةٌۭ فَأَصْلِحُوا۟ بَيْنَ أَخَوَيْكُمْ ۚ وَٱتَّقُوا۟ ٱللَّهَ لَعَلَّكُمْ تُرْحَمُونَ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Kush shkon " +
                "në xhami në mëngjes a në mbrëmje, Allahu i përgatit atij një vend në " +
                "Xhenet sa herë që shkon**.»",
            "Rijadu Salihin • Buhariu & Muslimi",
        ),
        KhutbahQuote(
            "Thuaj: \"O robërit e Mi, të cilët e keni ngarkuar me shumë gabime veten " +
                "tuaj, **mos e humbni shpresën ndaj mëshirës së All-llahut**, pse " +
                "All-llahu i falë të gjitha mëkatet.\"",
            "Kur'an, Ez-Zumer 53",
            arabic = "قُلْ يَٰعِبَادِىَ ٱلَّذِينَ أَسْرَفُوا۟ عَلَىٰٓ أَنفُسِهِمْ لَا تَقْنَطُوا۟ مِن رَّحْمَةِ ٱللَّهِ ۚ إِنَّ ٱللَّهَ يَغْفِرُ ٱلذُّنُوبَ جَمِيعًا",
        ),
        KhutbahQuote(
            "Ibn Abbasi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «Dy begati për " +
                "të cilat shumë njerëz janë të mashtruar: **shëndeti dhe koha e lirë**.»",
            "Rijadu Salihin • Buhariu",
        ),
        KhutbahQuote(
            "**Zoti yt ka dhënë urdhër të prerë që të mos adhuroni tjetër pos Tij, që " +
                "të silleni në mënyrë bamirëse ndaj prindërve.**",
            "Kur'an, El-Isra 23",
            arabic = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوٓا۟ إِلَّآ إِيَّاهُ وَبِٱلْوَٰلِدَيْنِ إِحْسَٰنًا",
        ),
        KhutbahQuote(
            "Ebu Mes'udi r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Kush udhëzon " +
                "në një të mirë, ka shpërblim si ai që e vepron atë**.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "Vërtet, vetëm Unë jam All-llahu, nuk ka zot tjetër pos Meje, pra Mua më " +
                "adhuro dhe **fal namazin për të më kujtuar Mua**.",
            "Kur'an, Ta Ha 14",
            arabic = "إِنَّنِىٓ أَنَا ٱللَّهُ لَآ إِلَٰهَ إِلَّآ أَنَا۠ فَٱعْبُدْنِى وَأَقِمِ ٱلصَّلَوٰةَ لِذِكْرِىٓ",
        ),
        KhutbahQuote(
            "Ebu Hurejra r.a. tregon se Pejgamberi s.a.v.s. ka thënë: «**Allahu nuk " +
                "shikon në trupat tuaj e as në fytyrat tuaja, por shikon në zemrat " +
                "tuaja**.»",
            "Rijadu Salihin • Muslimi",
        ),
        KhutbahQuote(
            "O ju që besuat! **Ta keni në kujdes All-llahun dhe të jeni me ata të " +
                "drejtit.**",
            "Kur'an, Et-Teube 119",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱتَّقُوا۟ ٱللَّهَ وَكُونُوا۟ مَعَ ٱلصَّٰدِقِينَ",
        ),
    ),
    switchOn = "Ndezur",
    switchOff = "Fikur",
    portalCodeLabel = "Kodi i portalit",
    secondaryLanguageLabel = "Gjuha e dytë (alternohet çdo 2 min)",
    ramadanTitle = "Ramazani",
    iftarLabel = "Iftari",
    iftarCountdownName = "Iftar",
    ramadanModeLabel = "Modaliteti i Ramazanit (vetëm gjatë Ramazanit)",
    adhanTemplate = "Ezani i %s",
    preAdhanTitle = "Përgatituni për namaz",
    preAdhanNote = "Ju lutemi, ndaleni zilen e telefonit",
    adhanDuaTitle = "Duaja e Ezanit",
    adhanDua = AdhanDua(
        arabic = "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ، آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ",
        transliteration = "All-llahumme rabbe hadhihid-da'vetit-tamme, ves-salatil-kaime, ati Muhammedenil-vesilete vel-fadilete, veb'ath-hu mekamen mahmudenil-ledhi ve'adteh.",
        translation = "O Allah, Zot i kësaj thirrjeje të plotë dhe i namazit që do të falet, jepi Muhammedit vesilen dhe vlerën e lartë dhe ngrite atë në vendin e lavdëruar që ia ke premtuar.",
        source = "Buhariu",
    ),
    adhanSectionLabel = "Ezani",
    adhanSequenceLabel = "Ekranet e ezanit",
    preAdhanLabel = "Numërimi para ezanit",
    adhanDurationLabel = "Kohëzgjatja e ezanit",
    adhanDuaDurationLabel = "Kohëzgjatja e duasë",
    secondsShort = "sek",
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
    clockWarningTitle = "Kujdes: ora e TV-së mund të jetë gabim!",
    clockWarningBody = "Pas ndërprerjes së rrymës ora e TV-së ka mbetur prapa — " +
        "kohët e namazit mund të shfaqen gabim. Lidhe TV-në me internet dhe ora " +
        "rregullohet vetë, ose rregulloje manualisht te cilësimet e TV-së.",
    clockWarningButton = "Hap cilësimet e rrjetit",
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
    countdownNames = mapOf(
        PrayerKey.FAJR to "Fajr",
        PrayerKey.DHUHR to "Dhuhr",
        PrayerKey.ASR to "Asr",
        PrayerKey.MAGHRIB to "Maghrib",
        PrayerKey.ISHA to "Isha",
    ),
    fridayDhuhrCountdownName = "Jumu'ah",
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
        AppTheme.MUSHAF to "Mushaf",
        AppTheme.MUSHAF_DARK to "Mushaf (dark)",
        AppTheme.ZAYTUN to "Olive",
        AppTheme.ZAYTUN_DARK to "Olive (dark)",
        AppTheme.NILA to "Indigo",
        AppTheme.NILA_DARK to "Indigo (dark)",
        AppTheme.HIBR to "Ink",
        AppTheme.HIBR_DARK to "Ink (dark)",
    ),
    themeRotationLabel = "Automatic theme change",
    themeRotationOff = "Off",
    themeRotationWeekly = "Every week",
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
    dailyQuotesTitle = "From the Qur'an and Sunnah",
    dailyQuotesLabel = "Verses & hadiths during the day",
    dailyQuotes = listOf(
        // Riyad us-Salihin (short narrations), at the imam's request, at the
        // head of the collection.
        KhutbahQuote(
            "Umar r.a. narrates that the Prophet s.a.w.s. said: «**Deeds are judged " +
                "by intentions**, and everyone will have what he intended.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Abu Dharr r.a. narrates that the Prophet s.a.w.s. said: «**Fear Allah " +
                "wherever you are**; follow a bad deed with a good one, for it will " +
                "wipe it out; and **treat people with good character**.»",
            "Riyad us-Salihin • Tirmidhi",
        ),
        KhutbahQuote(
            "Abu Dharr r.a. narrates that the Prophet s.a.w.s. said: «**Do not " +
                "belittle any good deed**, even meeting your brother **with a " +
                "cheerful face**.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates: A man asked: \"Messenger of Allah, who " +
                "deserves my kindness most?\" He said: «**Your mother**», then: " +
                "«**Your mother**», then: «**Your mother**», then: «**Your father**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «Whoever " +
                "believes in Allah and the Last Day, **let him be good to his " +
                "neighbour**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «**When Allah " +
                "wills good for a servant, He tests him**.»",
            "Riyad us-Salihin • Bukhari",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «Two words " +
                "**light on the tongue, heavy on the scale**, beloved to the Most " +
                "Merciful: SubhanAllahi wa bihamdihi, SubhanAllahil-Adhim.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Abdullah ibn Mas'ud r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**Truthfulness leads to righteousness, and righteousness leads to " +
                "Paradise**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "But when you become secure, re-establish prayer. Indeed, **prayer has " +
                "been decreed upon the believers a decree of specified times**.",
            "Qur'an, An-Nisa 103",
            arabic = "فَإِذَا ٱطْمَأْنَنتُمْ فَأَقِيمُوا۟ ٱلصَّلَوٰةَ ۚ إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَٰبًۭا مَّوْقُوتًۭا",
        ),
        KhutbahQuote(
            "Abdullah ibn Mas'ud r.a. narrates: I asked the Prophet s.a.w.s.: " +
                "\"Which deed is most beloved to Allah?\" He said: «**Prayer at its proper time**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Recite what has been revealed to you of the Book and establish prayer. " +
                "**Indeed, prayer prohibits immorality and wrongdoing**, and the " +
                "remembrance of Allah is greater.",
            "Qur'an, Al-Ankabut 45",
            arabic = "ٱتْلُ مَآ أُوحِىَ إِلَيْكَ مِنَ ٱلْكِتَٰبِ وَأَقِمِ ٱلصَّلَوٰةَ ۖ إِنَّ ٱلصَّلَوٰةَ تَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ ۗ وَلَذِكْرُ ٱللَّهِ أَكْبَرُ",
        ),
        KhutbahQuote(
            "Abdullah ibn Amr r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**The best among you are those with the best character**.»",
            "Bukhari",
        ),
        KhutbahQuote(
            "**So whoever does an atom's weight of good will see it.** And whoever " +
                "does an atom's weight of evil will see it.",
            "Qur'an, Az-Zalzalah 7-8",
            arabic = "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًۭا يَرَهُۥ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍۢ شَرًّۭا يَرَهُۥ",
        ),
        KhutbahQuote(
            "Anas r.a. narrates that the Prophet s.a.w.s. said: «None of you truly " +
                "believes until **he loves for his brother what he loves for himself**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Who spend during ease and hardship and **who restrain anger and who " +
                "pardon the people** – and Allah loves the doers of good.",
            "Qur'an, Al-Imran 134",
            arabic = "ٱلَّذِينَ يُنفِقُونَ فِى ٱلسَّرَّآءِ وَٱلضَّرَّآءِ وَٱلْكَٰظِمِينَ ٱلْغَيْظَ وَٱلْعَافِينَ عَنِ ٱلنَّاسِ ۗ وَٱللَّهُ يُحِبُّ ٱلْمُحْسِنِينَ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «The strong one " +
                "is not the one who overcomes others; the strong one is **he who controls " +
                "himself when angry**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "And establish prayer at the two ends of the day and at the approach of " +
                "the night. **Indeed, good deeds do away with misdeeds.**",
            "Qur'an, Hud 114",
            arabic = "وَأَقِمِ ٱلصَّلَوٰةَ طَرَفَىِ ٱلنَّهَارِ وَزُلَفًۭا مِّنَ ٱلَّيْلِ ۚ إِنَّ ٱلْحَسَنَٰتِ يُذْهِبْنَ ٱلسَّيِّـَٔاتِ",
        ),
        KhutbahQuote(
            "Abu Musa r.a. narrates that the Prophet s.a.w.s. said: «Whoever prays " +
                "**the two cool prayers** (Fajr and Asr) **will enter Paradise**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Those who have believed and whose hearts are assured by the remembrance " +
                "of Allah. **Unquestionably, by the remembrance of Allah hearts are " +
                "assured.**",
            "Qur'an, Ar-Ra'd 28",
            arabic = "ٱلَّذِينَ ءَامَنُوا۟ وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ ٱللَّهِ ۗ أَلَا بِذِكْرِ ٱللَّهِ تَطْمَئِنُّ ٱلْقُلُوبُ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «Whoever believes " +
                "in Allah and the Last Day, **let him speak good or remain silent**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "O you who have believed, **seek help through patience and prayer**. " +
                "Indeed, Allah is with the patient.",
            "Qur'an, Al-Baqarah 153",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱسْتَعِينُوا۟ بِٱلصَّبْرِ وَٱلصَّلَوٰةِ ۚ إِنَّ ٱللَّهَ مَعَ ٱلصَّٰبِرِينَ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «Whoever relieves " +
                "a believer of a hardship of this world, **Allah will relieve him of a " +
                "hardship on the Day of Resurrection**.»",
            "Muslim",
        ),
        KhutbahQuote(
            "By time, indeed, mankind is in loss, **except for those who have " +
                "believed and done righteous deeds** and advised each other to truth and " +
                "advised each other to patience.",
            "Qur'an, Al-Asr 1-3",
            arabic = "وَٱلْعَصْرِ إِنَّ ٱلْإِنسَٰنَ لَفِى خُسْرٍ إِلَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّٰلِحَٰتِ وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**A kind word is charity**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Jarir r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**He who shows no mercy to people, Allah shows no mercy to him**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Anas r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**Make things easy and do not make them hard; give glad tidings and do " +
                "not drive people away**.»",
            "Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Abu Malik al-Ash'ari r.a. narrates that the Prophet s.a.w.s. said: " +
                "«**Purity is half of faith**.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "**For indeed, with hardship will be ease.** Indeed, with hardship will " +
                "be ease.",
            "Qur'an, Ash-Sharh 5-6",
            arabic = "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا إِنَّ مَعَ ٱلْعُسْرِ يُسْرًۭا",
        ),
        KhutbahQuote(
            "Ibn Umar r.a. narrates that the Prophet s.a.w.s. said: «**Modesty is " +
                "part of faith**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "**Allah does not charge a soul except with that within its capacity.** " +
                "It will have what good it has gained, and it will bear what evil it has " +
                "earned.",
            "Qur'an, Al-Baqarah 286",
            arabic = "لَا يُكَلِّفُ ٱللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا ٱكْتَسَبَتْ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «**Removing " +
                "something harmful from the road is charity**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "**And whoever fears Allah – He will make for him a way out and will " +
                "provide for him from where he does not expect.**",
            "Qur'an, At-Talaq 2-3",
            arabic = "وَمَن يَتَّقِ ٱللَّهَ يَجْعَل لَّهُۥ مَخْرَجًۭا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «The rights of " +
                "a Muslim over a Muslim are five: **returning the greeting, visiting the " +
                "sick, following the funeral, accepting an invitation, and answering one " +
                "who sneezes**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "And when your Lord proclaimed: '**If you are grateful, I will surely " +
                "increase you in favor**; but if you deny, indeed, My punishment is " +
                "severe.'",
            "Qur'an, Ibrahim 7",
            arabic = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِى لَشَدِيدٌۭ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «You will not " +
                "enter Paradise until you believe, and you will not believe until you " +
                "love one another. Shall I tell you of something which, if you do it, you " +
                "will love one another? **Spread the greeting of peace among you**.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "**So remember Me; I will remember you.** And be grateful to Me and do " +
                "not deny Me.",
            "Qur'an, Al-Baqarah 152",
            arabic = "فَٱذْكُرُونِىٓ أَذْكُرْكُمْ وَٱشْكُرُوا۟ لِى وَلَا تَكْفُرُونِ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «**Charity " +
                "does not decrease wealth**; Allah increases the honour of one who " +
                "forgives; and no one humbles himself for Allah except that Allah raises " +
                "him.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "**Indeed, Allah orders justice and good conduct and giving to " +
                "relatives** and forbids immorality and bad conduct and oppression. He " +
                "admonishes you that perhaps you will be reminded.",
            "Qur'an, An-Nahl 90",
            arabic = "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَٰنِ وَإِيتَآئِ ذِى ٱلْقُرْبَىٰ وَيَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ وَٱلْبَغْىِ ۚ يَعِظُكُمْ لَعَلَّكُمْ تَذَكَّرُونَ",
        ),
        KhutbahQuote(
            "Uthman r.a. narrates that the Prophet s.a.w.s. said: «**The best of you " +
                "is the one who learns the Qur'an and teaches it**.»",
            "Riyad us-Salihin • Bukhari",
        ),
        KhutbahQuote(
            "O mankind, indeed We have created you from male and female and made you " +
                "peoples and tribes that you may know one another. **Indeed, the most " +
                "noble of you in the sight of Allah is the most righteous of you.**",
            "Qur'an, Al-Hujurat 13",
            arabic = "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَٰكُم مِّن ذَكَرٍۢ وَأُنثَىٰ وَجَعَلْنَٰكُمْ شُعُوبًۭا وَقَبَآئِلَ لِتَعَارَفُوٓا۟ ۚ إِنَّ أَكْرَمَكُمْ عِندَ ٱللَّهِ أَتْقَىٰكُمْ",
        ),
        KhutbahQuote(
            "Ibn Umar r.a. narrates that the Prophet s.a.w.s. said: «**Prayer in " +
                "congregation is twenty-seven degrees better than prayer alone**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "**The believers are but brothers**, so make settlement between your " +
                "brothers. And fear Allah that you may receive mercy.",
            "Qur'an, Al-Hujurat 10",
            arabic = "إِنَّمَا ٱلْمُؤْمِنُونَ إِخْوَةٌۭ فَأَصْلِحُوا۟ بَيْنَ أَخَوَيْكُمْ ۚ وَٱتَّقُوا۟ ٱللَّهَ لَعَلَّكُمْ تُرْحَمُونَ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «**Whoever " +
                "goes to the mosque in the morning or the evening, Allah prepares for him " +
                "a place in Paradise each time he goes**.»",
            "Riyad us-Salihin • Bukhari & Muslim",
        ),
        KhutbahQuote(
            "Say: \"O My servants who have transgressed against themselves, **do not " +
                "despair of the mercy of Allah**. Indeed, Allah forgives all sins.\"",
            "Qur'an, Az-Zumar 53",
            arabic = "قُلْ يَٰعِبَادِىَ ٱلَّذِينَ أَسْرَفُوا۟ عَلَىٰٓ أَنفُسِهِمْ لَا تَقْنَطُوا۟ مِن رَّحْمَةِ ٱللَّهِ ۚ إِنَّ ٱللَّهَ يَغْفِرُ ٱلذُّنُوبَ جَمِيعًا",
        ),
        KhutbahQuote(
            "Ibn Abbas r.a. narrates that the Prophet s.a.w.s. said: «Two blessings " +
                "which many people squander: **health and free time**.»",
            "Riyad us-Salihin • Bukhari",
        ),
        KhutbahQuote(
            "**And your Lord has decreed that you not worship except Him, and to " +
                "parents, good treatment.**",
            "Qur'an, Al-Isra 23",
            arabic = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوٓا۟ إِلَّآ إِيَّاهُ وَبِٱلْوَٰلِدَيْنِ إِحْسَٰنًا",
        ),
        KhutbahQuote(
            "Abu Mas'ud r.a. narrates that the Prophet s.a.w.s. said: «**Whoever " +
                "guides someone to good has a reward like the one who does it**.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "Indeed, I am Allah. There is no deity except Me, so worship Me and " +
                "**establish prayer for My remembrance**.",
            "Qur'an, Ta-Ha 14",
            arabic = "إِنَّنِىٓ أَنَا ٱللَّهُ لَآ إِلَٰهَ إِلَّآ أَنَا۠ فَٱعْبُدْنِى وَأَقِمِ ٱلصَّلَوٰةَ لِذِكْرِىٓ",
        ),
        KhutbahQuote(
            "Abu Hurayra r.a. narrates that the Prophet s.a.w.s. said: «**Allah does " +
                "not look at your bodies nor at your faces, but He looks at your " +
                "hearts**.»",
            "Riyad us-Salihin • Muslim",
        ),
        KhutbahQuote(
            "O you who have believed, **fear Allah and be with those who are true**.",
            "Qur'an, At-Tawbah 119",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱتَّقُوا۟ ٱللَّهَ وَكُونُوا۟ مَعَ ٱلصَّٰدِقِينَ",
        ),
    ),
    switchOn = "On",
    switchOff = "Off",
    portalCodeLabel = "Portal code",
    secondaryLanguageLabel = "Secondary language (alternates every 2 min)",
    ramadanTitle = "Ramadan",
    iftarLabel = "Iftar",
    iftarCountdownName = "Iftar",
    ramadanModeLabel = "Ramadan mode (only during Ramadan)",
    adhanTemplate = "Adhan for %s",
    preAdhanTitle = "Prepare for prayer",
    preAdhanNote = "Please silence your phones",
    adhanDuaTitle = "Dua after the Adhan",
    adhanDua = AdhanDua(
        arabic = "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ، آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ",
        transliteration = "Allahumma Rabba hadhihi'd-da'wati't-tammah, wa's-salati'l-qa'imah, ati Muhammadan al-wasilata wa'l-fadilah, wab'ath-hu maqaman mahmudan alladhi wa'adtah.",
        translation = "O Allah, Lord of this perfect call and of the prayer to be established, grant Muhammad the wasila and the excellence, and raise him to the praiseworthy station which You have promised him.",
        source = "Bukhari",
    ),
    adhanSectionLabel = "Adhan",
    adhanSequenceLabel = "Adhan screens",
    preAdhanLabel = "Countdown before the adhan",
    adhanDurationLabel = "Adhan duration",
    adhanDuaDurationLabel = "Dua duration",
    secondsShort = "sec",
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
    clockWarningTitle = "Warning: the TV clock may be wrong!",
    clockWarningBody = "After a power cut the TV clock falls behind — prayer " +
        "times may be shown incorrectly. Connect the TV to the internet and the " +
        "clock fixes itself, or set it manually in the TV settings.",
    clockWarningButton = "Open network settings",
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
    countdownNames = mapOf(
        PrayerKey.FAJR to "Sabah",
        PrayerKey.DHUHR to "Öğle",
        PrayerKey.ASR to "İkindi",
        PrayerKey.MAGHRIB to "Akşam",
        PrayerKey.ISHA to "Yatsı",
    ),
    fridayDhuhrCountdownName = "Cuma",
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
        AppTheme.MUSHAF to "Mushaf",
        AppTheme.MUSHAF_DARK to "Mushaf (koyu)",
        AppTheme.ZAYTUN to "Zeytin",
        AppTheme.ZAYTUN_DARK to "Zeytin (koyu)",
        AppTheme.NILA to "Çivit",
        AppTheme.NILA_DARK to "Çivit (koyu)",
        AppTheme.HIBR to "Mürekkep",
        AppTheme.HIBR_DARK to "Mürekkep (koyu)",
    ),
    themeRotationLabel = "Otomatik tema değişimi",
    themeRotationOff = "Kapalı",
    themeRotationWeekly = "Her hafta",
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
    dailyQuotesTitle = "Kur'an ve Sünnet'ten",
    dailyQuotesLabel = "Gün içinde ayet ve hadisler",
    dailyQuotes = listOf(
        // Riyâzü's-Sâlihîn (short narrations), at the imam's request, at the
        // head of the collection.
        KhutbahQuote(
            "Ömer r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Ameller niyetlere " +
                "göredir**; herkese niyet ettiği şey vardır.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ebû Zer r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Nerede olursan " +
                "ol Allah'tan kork**; kötülüğün ardından bir iyilik yap ki onu silsin; " +
                "**insanlara güzel ahlakla davran**.»",
            "Riyâzü's-Sâlihîn • Tirmizî",
        ),
        KhutbahQuote(
            "Ebû Zer r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Hiçbir iyiliği " +
                "küçük görme**; kardeşini **güler yüzle** karşılaman bile.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Bir adam \"Ey Allah'ın Resûlü, iyiliği en çok " +
                "kime yapayım?\" diye sordu. «**Annene**» buyurdu, sonra: «**Annene**», " +
                "sonra: «**Annene**», sonra: «**Babana**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Allah'a ve " +
                "âhiret gününe inanan, **komşusuna iyilik etsin**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Allah bir " +
                "kuluna hayır dilediğinde onu imtihan eder**.»",
            "Riyâzü's-Sâlihîn • Buhârî",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Dile hafif, " +
                "terazide ağır** ve Rahmân'a sevimli iki söz: Sübhânallâhi ve " +
                "bihamdihî, Sübhânallâhi'l-Azîm.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'ud r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**Doğruluk iyiliğe, iyilik de cennete götürür**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Emniyete kavuştuğunuzda, namazı gereğince kılın. **Namaz şüphesiz, " +
                "inananlara belirli vakitlerde farz kılınmıştır**.",
            "Kur'an, Nisâ 103",
            arabic = "فَإِذَا ٱطْمَأْنَنتُمْ فَأَقِيمُوا۟ ٱلصَّلَوٰةَ ۚ إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَٰبًۭا مَّوْقُوتًۭا",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'ud r.a. anlatır: Peygamber s.a.v.'e \"Allah katında en " +
                "sevimli amel hangisidir?\" diye sordum. «**Vaktinde kılınan namazdır**» buyurdu.",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Kitap'tan sana vahyolunanı oku; namaz kıl; **muhakkak ki namaz " +
                "hayasızlıktan ve fenalıktan alıkor**; Allah'ı anmak en büyük şeydir!",
            "Kur'an, Ankebût 45",
            arabic = "ٱتْلُ مَآ أُوحِىَ إِلَيْكَ مِنَ ٱلْكِتَٰبِ وَأَقِمِ ٱلصَّلَوٰةَ ۖ إِنَّ ٱلصَّلَوٰةَ تَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ ۗ وَلَذِكْرُ ٱللَّهِ أَكْبَرُ",
        ),
        KhutbahQuote(
            "Abdullah ibn Amr r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**Sizin en hayırlınız, ahlâkı en güzel olanınızdır**.»",
            "Buhârî",
        ),
        KhutbahQuote(
            "**Kim zerre kadar iyilik yapmışsa onu görür.** Kim de zerre kadar " +
                "kötülük yapmışsa onu görür.",
            "Kur'an, Zilzâl 7-8",
            arabic = "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًۭا يَرَهُۥ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍۢ شَرًّۭا يَرَهُۥ",
        ),
        KhutbahQuote(
            "Enes r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Sizden biriniz, " +
                "**kendisi için istediğini kardeşi için de istemedikçe** gerçek mümin olamaz.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Onlar bollukta ve darlıkta sarfederler, **öfkelerini yenerler, " +
                "insanların kusurlarını affederler**. Allah iyilik yapanları sever.",
            "Kur'an, Âl-i İmrân 134",
            arabic = "ٱلَّذِينَ يُنفِقُونَ فِى ٱلسَّرَّآءِ وَٱلضَّرَّآءِ وَٱلْكَٰظِمِينَ ٱلْغَيْظَ وَٱلْعَافِينَ عَنِ ٱلنَّاسِ ۗ وَٱللَّهُ يُحِبُّ ٱلْمُحْسِنِينَ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Güçlü olan, " +
                "insanları yenen değil; **öfkelendiğinde kendine hâkim olandır**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Gündüzün iki ucunda ve gecenin gündüze yakın zamanlarında namaz kıl. " +
                "**Doğrusu iyilikler kötülükleri giderir.**",
            "Kur'an, Hûd 114",
            arabic = "وَأَقِمِ ٱلصَّلَوٰةَ طَرَفَىِ ٱلنَّهَارِ وَزُلَفًۭا مِّنَ ٱلَّيْلِ ۚ إِنَّ ٱلْحَسَنَٰتِ يُذْهِبْنَ ٱلسَّيِّـَٔاتِ",
        ),
        KhutbahQuote(
            "Ebû Mûsâ r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**İki serin namazı** " +
                "(sabah ve ikindiyi) **kılan cennete girer**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Onlar inanmışlar, kalbleri Allah'ı anmakla huzura kavuşmuştur. **Dikkat " +
                "edin, kalbler ancak Allah'ı anmakla huzura kavuşur.**",
            "Kur'an, Ra'd 28",
            arabic = "ٱلَّذِينَ ءَامَنُوا۟ وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ ٱللَّهِ ۗ أَلَا بِذِكْرِ ٱللَّهِ تَطْمَئِنُّ ٱلْقُلُوبُ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Allah'a ve ahiret " +
                "gününe iman eden, **ya hayır söylesin ya da sussun**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ey İnananlar! **Sabır ve namazla yardım dileyin.** Allah, muhakkak ki " +
                "sabredenlerle beraberdir.",
            "Kur'an, Bakara 153",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱسْتَعِينُوا۟ بِٱلصَّبْرِ وَٱلصَّلَوٰةِ ۚ إِنَّ ٱللَّهَ مَعَ ٱلصَّٰبِرِينَ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Kim bir müminin " +
                "dünya sıkıntılarından birini giderirse, **Allah da onun kıyamet günü " +
                "sıkıntılarından birini giderir**.»",
            "Müslim",
        ),
        KhutbahQuote(
            "Asra and olsun ki, insan hiç şüphesiz hüsran içindedir. **Ancak inanıp " +
                "yararlı iş işleyenler**, birbirlerine gerçeği tavsiye edenler ve sabırlı " +
                "olmayı tavsiye edenler bunun dışındadır.",
            "Kur'an, Asr 1-3",
            arabic = "وَٱلْعَصْرِ إِنَّ ٱلْإِنسَٰنَ لَفِى خُسْرٍ إِلَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّٰلِحَٰتِ وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**Güzel söz sadakadır**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Cerîr r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**İnsanlara merhamet etmeyene Allah da merhamet etmez**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Enes r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**Kolaylaştırın, zorlaştırmayın; müjdeleyin, nefret ettirmeyin**.»",
            "Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Ebû Mâlik el-Eş'arî r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: " +
                "«**Temizlik imanın yarısıdır**.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "**Elbette güçlükle beraber şüphesiz bir kolaylık vardır.** Gerçekten, " +
                "güçlükle beraber bir kolaylık vardır.",
            "Kur'an, İnşirâh 5-6",
            arabic = "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا إِنَّ مَعَ ٱلْعُسْرِ يُسْرًۭا",
        ),
        KhutbahQuote(
            "İbn Ömer r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Hayâ " +
                "imandandır**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "**Allah kişiye ancak gücünün yeteceği kadar yükler**; kazandığı iyilik " +
                "lehine, ettiği kötülük de aleyhinedir.",
            "Kur'an, Bakara 286",
            arabic = "لَا يُكَلِّفُ ٱللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا ٱكْتَسَبَتْ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Yoldan " +
                "eziyet veren şeyi kaldırmak sadakadır**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "**Allah, kendisine karşı gelmekten sakınan kimseye kurtuluş yolu sağlar, " +
                "ona beklemediği yerden rızık verir.**",
            "Kur'an, Talâk 2-3",
            arabic = "وَمَن يَتَّقِ ٱللَّهَ يَجْعَل لَّهُۥ مَخْرَجًۭا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «Müslümanın " +
                "müslüman üzerindeki hakkı beştir: **selamı almak, hastayı ziyaret etmek, " +
                "cenazeye katılmak, davete icabet etmek ve aksırana dua etmek**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "Rabbiniz: \"**Şükrederseniz and olsun ki, size karşılığını " +
                "artıracağım**; nankörlük ederseniz bilin ki azabım pek çetindir\" diye " +
                "bildirmişti.",
            "Kur'an, İbrâhîm 7",
            arabic = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِى لَشَدِيدٌۭ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «İman " +
                "etmedikçe cennete giremezsiniz, birbirinizi sevmedikçe de iman etmiş " +
                "olmazsınız. Yaptığınızda birbirinizi seveceğiniz şeyi size söyleyeyim " +
                "mi? **Aranızda selamı yayın**.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "**Artık Beni anın, Ben de sizi anayım**; Bana şükredin, nankörlük etmeyin.",
            "Kur'an, Bakara 152",
            arabic = "فَٱذْكُرُونِىٓ أَذْكُرْكُمْ وَٱشْكُرُوا۟ لِى وَلَا تَكْفُرُونِ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Sadaka " +
                "maldan eksiltmez**; Allah affeden kulun şerefini artırır; Allah için " +
                "alçakgönüllü olanı da Allah yüceltir.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "**Allah şüphesiz adaleti, iyilik yapmayı, yakınlara bakmayı emreder**; " +
                "hayasızlığı, fenalığı ve haddi aşmayı yasak eder. Tutasınız diye size " +
                "öğüt verir.",
            "Kur'an, Nahl 90",
            arabic = "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَٰنِ وَإِيتَآئِ ذِى ٱلْقُرْبَىٰ وَيَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ وَٱلْبَغْىِ ۚ يَعِظُكُمْ لَعَلَّكُمْ تَذَكَّرُونَ",
        ),
        KhutbahQuote(
            "Osman r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Sizin en " +
                "hayırlınız, Kur'an'ı öğrenen ve öğretendir**.»",
            "Riyâzü's-Sâlihîn • Buhârî",
        ),
        KhutbahQuote(
            "Ey insanlar! Doğrusu Biz sizleri bir erkekle bir dişiden yarattık. Sizi " +
                "milletler ve kabileler haline koyduk ki birbirinizi kolayca tanıyasınız. " +
                "**Şüphesiz, Allah katında en değerliniz, O'na karşı gelmekten en çok " +
                "sakınanınızdır.**",
            "Kur'an, Hucurât 13",
            arabic = "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَٰكُم مِّن ذَكَرٍۢ وَأُنثَىٰ وَجَعَلْنَٰكُمْ شُعُوبًۭا وَقَبَآئِلَ لِتَعَارَفُوٓا۟ ۚ إِنَّ أَكْرَمَكُمْ عِندَ ٱللَّهِ أَتْقَىٰكُمْ",
        ),
        KhutbahQuote(
            "İbn Ömer r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Cemaatle " +
                "kılınan namaz, tek başına kılınan namazdan yirmi yedi derece daha " +
                "faziletlidir**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "**Şüphesiz müminler birbiri ile kardeştirler**; öyle ise dargın olan " +
                "kardeşlerinizin arasını düzeltin; Allah'tan sakının ki size acısın.",
            "Kur'an, Hucurât 10",
            arabic = "إِنَّمَا ٱلْمُؤْمِنُونَ إِخْوَةٌۭ فَأَصْلِحُوا۟ بَيْنَ أَخَوَيْكُمْ ۚ وَٱتَّقُوا۟ ٱللَّهَ لَعَلَّكُمْ تُرْحَمُونَ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Kim sabah " +
                "akşam mescide giderse, her gidişinde Allah ona cennette bir yer " +
                "hazırlar**.»",
            "Riyâzü's-Sâlihîn • Buhârî & Müslim",
        ),
        KhutbahQuote(
            "De ki: \"Ey kendilerine kötülük edip aşırı giden kullarım! **Allah'ın " +
                "rahmetinden umudunuzu kesmeyin.** Doğrusu Allah günahların hepsini " +
                "bağışlar.\"",
            "Kur'an, Zümer 53",
            arabic = "قُلْ يَٰعِبَادِىَ ٱلَّذِينَ أَسْرَفُوا۟ عَلَىٰٓ أَنفُسِهِمْ لَا تَقْنَطُوا۟ مِن رَّحْمَةِ ٱللَّهِ ۚ إِنَّ ٱللَّهَ يَغْفِرُ ٱلذُّنُوبَ جَمِيعًا",
        ),
        KhutbahQuote(
            "İbn Abbas r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «İki nimet " +
                "vardır ki insanların çoğu onlar hakkında aldanmıştır: **sağlık ve boş " +
                "vakit**.»",
            "Riyâzü's-Sâlihîn • Buhârî",
        ),
        KhutbahQuote(
            "**Rabbin, yalnız Kendisine tapmanızı ve ana babaya iyilik etmeyi " +
                "buyurmuştur.**",
            "Kur'an, İsrâ 23",
            arabic = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوٓا۟ إِلَّآ إِيَّاهُ وَبِٱلْوَٰلِدَيْنِ إِحْسَٰنًا",
        ),
        KhutbahQuote(
            "Ebû Mes'ûd r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Hayra vesile " +
                "olan, onu yapan gibi ecir alır**.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "\"Şüphesiz Ben Allah'ım, Benden başka tanrı yoktur; Bana kulluk et; " +
                "**Beni anmak için namaz kıl**.\"",
            "Kur'an, Tâhâ 14",
            arabic = "إِنَّنِىٓ أَنَا ٱللَّهُ لَآ إِلَٰهَ إِلَّآ أَنَا۠ فَٱعْبُدْنِى وَأَقِمِ ٱلصَّلَوٰةَ لِذِكْرِىٓ",
        ),
        KhutbahQuote(
            "Ebû Hüreyre r.a. anlatır: Peygamber s.a.v. şöyle buyurdu: «**Allah sizin " +
                "bedenlerinize ve yüzlerinize bakmaz; kalplerinize bakar**.»",
            "Riyâzü's-Sâlihîn • Müslim",
        ),
        KhutbahQuote(
            "Ey inananlar! **Allah'tan sakının ve doğrularla beraber olun.**",
            "Kur'an, Tevbe 119",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱتَّقُوا۟ ٱللَّهَ وَكُونُوا۟ مَعَ ٱلصَّٰدِقِينَ",
        ),
    ),
    switchOn = "Açık",
    switchOff = "Kapalı",
    portalCodeLabel = "Portal kodu",
    secondaryLanguageLabel = "İkinci dil (2 dakikada bir dönüşür)",
    ramadanTitle = "Ramazan",
    iftarLabel = "İftar",
    iftarCountdownName = "İftar",
    ramadanModeLabel = "Ramazan modu (yalnızca Ramazan'da)",
    adhanTemplate = "%s Ezanı",
    preAdhanTitle = "Namaza hazırlanın",
    preAdhanNote = "Lütfen telefonlarınızı sessize alın",
    adhanDuaTitle = "Ezan Duası",
    adhanDua = AdhanDua(
        arabic = "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ، آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ",
        transliteration = "Allâhümme Rabbe hâzihi'd-da'veti't-tâmme ve's-salâti'l-kâime, âti Muhammeden'il-vesîlete ve'l-fadîlete, veb'ashü makâmen mahmûden'illezî ve'adteh.",
        translation = "Ey bu eksiksiz davetin ve kılınacak namazın Rabbi olan Allah'ım! Muhammed'e vesîleyi ve fazîleti ver; onu kendisine vaad ettiğin Makâm-ı Mahmûd'a ulaştır.",
        source = "Buhârî",
    ),
    adhanSectionLabel = "Ezan",
    adhanSequenceLabel = "Ezan ekranları",
    preAdhanLabel = "Ezandan önce geri sayım",
    adhanDurationLabel = "Ezan süresi",
    adhanDuaDurationLabel = "Dua süresi",
    secondsShort = "sn",
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
    clockWarningTitle = "Dikkat: TV saati yanlış olabilir!",
    clockWarningBody = "Elektrik kesintisinden sonra TV saati geride kalır — " +
        "namaz vakitleri yanlış görünebilir. TV'yi internete bağlayın, saat " +
        "kendini düzeltir; ya da TV ayarlarından elle ayarlayın.",
    clockWarningButton = "Ağ ayarlarını aç",
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
    countdownNames = mapOf(
        PrayerKey.FAJR to "sabaha",
        PrayerKey.DHUHR to "podne",
        PrayerKey.ASR to "ikindije",
        PrayerKey.MAGHRIB to "akšama",
        PrayerKey.ISHA to "jacije",
    ),
    fridayDhuhrCountdownName = "džume",
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
        AppTheme.MUSHAF to "Mushaf",
        AppTheme.MUSHAF_DARK to "Mushaf (tamna)",
        AppTheme.ZAYTUN to "Maslina",
        AppTheme.ZAYTUN_DARK to "Maslina (tamna)",
        AppTheme.NILA to "Indigo",
        AppTheme.NILA_DARK to "Indigo (tamna)",
        AppTheme.HIBR to "Tinta",
        AppTheme.HIBR_DARK to "Tinta (tamna)",
    ),
    themeRotationLabel = "Automatska promjena teme",
    themeRotationOff = "Isključeno",
    themeRotationWeekly = "Svake sedmice",
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
    dailyQuotesTitle = "Iz Kur'ana i sunneta",
    dailyQuotesLabel = "Ajeti i hadisi tokom dana",
    dailyQuotes = listOf(
        // Rijadus-salihin (short narrations), at the imam's request, at the
        // head of the collection.
        KhutbahQuote(
            "Omer r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Djela se " +
                "vrednuju po namjeri**, i svakome pripada ono što je naumio.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "Ebu Zerr r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Boj se " +
                "Allaha gdje god bio**; nakon lošeg djela učini dobro, ono ga briše; " +
                "i **prema ljudima se ophodi lijepim ahlakom**.»",
            "Rijadus-salihin • Tirmizi",
        ),
        KhutbahQuote(
            "Ebu Zerr r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Nijedno " +
                "dobro djelo ne potcjenjuj**, pa ni to da brata sretneš " +
                "**nasmiješenim licem**.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi: Neki čovjek upita: \"Allahov Poslaniče, kome " +
                "da najviše činim dobro?\" Reče: «**Svojoj majci**», potom: «**Svojoj " +
                "majci**», potom: «**Svojoj majci**», potom: «**Svome ocu**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Ko vjeruje " +
                "u Allaha i Sudnji dan, **neka čini dobro svome susjedu**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Kada " +
                "Allah nekome želi dobro, iskuša ga**.»",
            "Rijadus-salihin • Buharija",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Dvije " +
                "riječi **lahke za jezik, teške na mizanu** i drage Milostivom: " +
                "Subhanallahi ve bihamdihi, Subhanallahil-Azim.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'ud r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Istinoljubivost vodi ka dobru, a dobro vodi u Džennet**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "A u sigurnosti obavljajte molitvu u potpunosti, jer **vjernicima je " +
                "propisano da u određeno vrijeme molitvu obavljaju**.",
            "Kur'an, En-Nisa 103",
            arabic = "فَإِذَا ٱطْمَأْنَنتُمْ فَأَقِيمُوا۟ ٱلصَّلَوٰةَ ۚ إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَٰبًۭا مَّوْقُوتًۭا",
        ),
        KhutbahQuote(
            "Abdullah ibn Mes'ud r.a. prenosi: Upitao sam Vjerovjesnika s.a.v.s.: " +
                "\"Koje je djelo Allahu najdraže?\" Reče: «**Namaz u njegovo vrijeme**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Kazuj Knjigu koja ti se objavljuje i obavljaj molitvu, **molitva, " +
                "zaista, odvraća od razvrata i od svega što je ružno**; obavljanje " +
                "molitve je najveća poslušnost!",
            "Kur'an, El-Ankebut 45",
            arabic = "ٱتْلُ مَآ أُوحِىَ إِلَيْكَ مِنَ ٱلْكِتَٰبِ وَأَقِمِ ٱلصَّلَوٰةَ ۖ إِنَّ ٱلصَّلَوٰةَ تَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ ۗ وَلَذِكْرُ ٱللَّهِ أَكْبَرُ",
        ),
        KhutbahQuote(
            "Abdullah ibn Amr r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Najbolji među vama su oni najljepšeg ahlaka**.»",
            "Buharija",
        ),
        KhutbahQuote(
            "**Onaj ko bude uradio koliko trun dobra – vidjeće ga**, a onaj ko bude " +
                "uradio koliko trun zla – vidjeće ga.",
            "Kur'an, Ez-Zilzal 7-8",
            arabic = "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًۭا يَرَهُۥ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍۢ شَرًّۭا يَرَهُۥ",
        ),
        KhutbahQuote(
            "Enes r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Niko od vas neće " +
                "istinski vjerovati dok **ne bude želio bratu svome ono što želi sebi**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Za one koji, i kad su u obilju i kad su u oskudici, udjeljuju, **koji " +
                "srdžbu savlađuju i ljudima praštaju** – a Allah voli one koji dobra " +
                "djela čine.",
            "Kur'an, Ali Imran 134",
            arabic = "ٱلَّذِينَ يُنفِقُونَ فِى ٱلسَّرَّآءِ وَٱلضَّرَّآءِ وَٱلْكَٰظِمِينَ ٱلْغَيْظَ وَٱلْعَافِينَ عَنِ ٱلنَّاسِ ۗ وَٱللَّهُ يُحِبُّ ٱلْمُحْسِنِينَ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Nije snažan " +
                "onaj ko druge savladava, nego **onaj ko sebe savlada u srdžbi**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "I obavljaj molitvu početkom i krajem dana, i u prvim časovima noći! " +
                "**Dobra djela zaista poništavaju hrđava.**",
            "Kur'an, Hud 114",
            arabic = "وَأَقِمِ ٱلصَّلَوٰةَ طَرَفَىِ ٱلنَّهَارِ وَزُلَفًۭا مِّنَ ٱلَّيْلِ ۚ إِنَّ ٱلْحَسَنَٰتِ يُذْهِبْنَ ٱلسَّيِّـَٔاتِ",
        ),
        KhutbahQuote(
            "Ebu Musa r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Ko klanja " +
                "**dva svježa namaza** (sabah i ikindiju), **ući će u Džennet**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "One koji vjeruju i čija se srca, kad se Allah spomene, smiruju – **a " +
                "srca se doista, kad se Allah spomene, smiruju!**",
            "Kur'an, Er-Ra'd 28",
            arabic = "ٱلَّذِينَ ءَامَنُوا۟ وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ ٱللَّهِ ۗ أَلَا بِذِكْرِ ٱللَّهِ تَطْمَئِنُّ ٱلْقُلُوبُ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Ko vjeruje u " +
                "Allaha i Sudnji dan, **neka govori dobro ili neka šuti**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "O vjernici, **tražite sebi pomoći u strpljivosti i obavljanju molitve**! " +
                "Allah je doista na strani strpljivih.",
            "Kur'an, El-Bekare 153",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱسْتَعِينُوا۟ بِٱلصَّبْرِ وَٱلصَّلَوٰةِ ۚ إِنَّ ٱللَّهَ مَعَ ٱلصَّٰبِرِينَ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Ko vjerniku " +
                "otkloni jednu dunjalučku nevolju, **Allah će njemu otkloniti jednu " +
                "nevolju na Sudnjem danu**.»",
            "Muslim",
        ),
        KhutbahQuote(
            "Tako mi vremena – čovjek, doista, gubi, **samo ne oni koji vjeruju i " +
                "dobra djela čine**, i koji jedni drugima istinu preporučuju i koji jedni " +
                "drugima preporučuju strpljenje.",
            "Kur'an, El-Asr 1-3",
            arabic = "وَٱلْعَصْرِ إِنَّ ٱلْإِنسَٰنَ لَفِى خُسْرٍ إِلَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّٰلِحَٰتِ وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Lijepa riječ je sadaka**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Džerir r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Ko nije milostiv prema ljudima, ni Allah neće biti milostiv prema njemu**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Enes r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Olakšavajte, a ne otežavajte; obradujte, a ne odbijajte**.»",
            "Buharija & Muslim",
        ),
        KhutbahQuote(
            "Ebu Malik el-Eš'ari r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Čistoća je pola imana**.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "**Ta, zaista, s mukom je i last**, zaista, s mukom je i last!",
            "Kur'an, El-Inširah 5-6",
            arabic = "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا إِنَّ مَعَ ٱلْعُسْرِ يُسْرًۭا",
        ),
        KhutbahQuote(
            "Ibn Omer r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Stid je dio " +
                "imana**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "**Allah nikoga ne opterećuje preko mogućnosti njegovih**: u njegovu " +
                "korist je dobro koje učini, a na njegovu štetu zlo koje uradi.",
            "Kur'an, El-Bekare 286",
            arabic = "لَا يُكَلِّفُ ٱللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا ٱكْتَسَبَتْ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: " +
                "«**Uklanjanje smetnje s puta je sadaka**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "**A onome koji se Allaha boji, On će izlaz naći i opskrbiće ga odakle se " +
                "i ne nada.**",
            "Kur'an, Et-Talak 2-3",
            arabic = "وَمَن يَتَّقِ ٱللَّهَ يَجْعَل لَّهُۥ مَخْرَجًۭا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Prava " +
                "muslimana prema muslimanu su pet: **uzvratiti selam, obići bolesnika, " +
                "ispratiti dženazu, odazvati se pozivu i nazvati dovu onome ko kihne**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "I kad je Gospodar vaš objavio: '**Ako budete zahvalni, Ja ću vam, " +
                "zacijelo, još više dati**; budete li nezahvalni, kazna Moja doista će " +
                "stroga biti.'",
            "Kur'an, Ibrahim 7",
            arabic = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِى لَشَدِيدٌۭ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Nećete ući " +
                "u Džennet dok ne budete vjerovali, a nećete vjerovati dok se ne budete " +
                "voljeli. Hoćete li da vas uputim na nešto čime ćete se zavoljeti? " +
                "**Širite selam među sobom**.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "**Sjećajte se vi Mene, i Ja ću se vas sjetiti**, i zahvaljujte Mi, i na " +
                "blagodatima Mojim nemojte neblagodarni biti!",
            "Kur'an, El-Bekare 152",
            arabic = "فَٱذْكُرُونِىٓ أَذْكُرْكُمْ وَٱشْكُرُوا۟ لِى وَلَا تَكْفُرُونِ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Sadaka ne " +
                "umanjuje imetak**; Allah povećava čast onome ko oprašta; i ko se ponizi " +
                "radi Allaha, Allah ga uzdigne.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "**Allah zahtijeva da se svačije pravo poštuje, dobro čini, i da se " +
                "bližnjima udjeljuje**, i razvrat i sve što je odvratno i nasilje " +
                "zabranjuje; da pouku primite, On vas savjetuje.",
            "Kur'an, En-Nahl 90",
            arabic = "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَٰنِ وَإِيتَآئِ ذِى ٱلْقُرْبَىٰ وَيَنْهَىٰ عَنِ ٱلْفَحْشَآءِ وَٱلْمُنكَرِ وَٱلْبَغْىِ ۚ يَعِظُكُمْ لَعَلَّكُمْ تَذَكَّرُونَ",
        ),
        KhutbahQuote(
            "Osman r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Najbolji među " +
                "vama je onaj koji nauči Kur'an i druge poučava**.»",
            "Rijadus-salihin • Buharija",
        ),
        KhutbahQuote(
            "O ljudi, Mi vas od jednog čovjeka i jedne žene stvaramo i na narode i " +
                "plemena vas dijelimo da biste se upoznali. **Najugledniji kod Allaha je " +
                "onaj koji Ga se najviše boji.**",
            "Kur'an, El-Hudžurat 13",
            arabic = "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَٰكُم مِّن ذَكَرٍۢ وَأُنثَىٰ وَجَعَلْنَٰكُمْ شُعُوبًۭا وَقَبَآئِلَ لِتَعَارَفُوٓا۟ ۚ إِنَّ أَكْرَمَكُمْ عِندَ ٱللَّهِ أَتْقَىٰكُمْ",
        ),
        KhutbahQuote(
            "Ibn Omer r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Namaz u " +
                "džematu vredniji je od namaza pojedinca za dvadeset sedam stepeni**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "**Vjernici su samo braća**, zato pomirite vaša dva brata i bojte se " +
                "Allaha, da bi vam se milost ukazala.",
            "Kur'an, El-Hudžurat 10",
            arabic = "إِنَّمَا ٱلْمُؤْمِنُونَ إِخْوَةٌۭ فَأَصْلِحُوا۟ بَيْنَ أَخَوَيْكُمْ ۚ وَٱتَّقُوا۟ ٱللَّهَ لَعَلَّكُمْ تُرْحَمُونَ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Ko ode u " +
                "džamiju ujutro ili navečer, Allah mu za svaki odlazak pripremi mjesto u " +
                "Džennetu**.»",
            "Rijadus-salihin • Buharija & Muslim",
        ),
        KhutbahQuote(
            "Reci: \"O robovi moji koji ste se prema sebi ogriješili, **ne gubite " +
                "nadu u Allahovu milost**! Allah će, sigurno, sve grijehe oprostiti.\"",
            "Kur'an, Ez-Zumer 53",
            arabic = "قُلْ يَٰعِبَادِىَ ٱلَّذِينَ أَسْرَفُوا۟ عَلَىٰٓ أَنفُسِهِمْ لَا تَقْنَطُوا۟ مِن رَّحْمَةِ ٱللَّهِ ۚ إِنَّ ٱللَّهَ يَغْفِرُ ٱلذُّنُوبَ جَمِيعًا",
        ),
        KhutbahQuote(
            "Ibn Abbas r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «Dvije su " +
                "blagodati u kojima su mnogi ljudi na gubitku: **zdravlje i slobodno " +
                "vrijeme**.»",
            "Rijadus-salihin • Buharija",
        ),
        KhutbahQuote(
            "**Gospodar tvoj zapovijeda da se samo Njemu klanjate i da roditeljima " +
                "dobročinstvo činite.**",
            "Kur'an, El-Isra 23",
            arabic = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوٓا۟ إِلَّآ إِيَّاهُ وَبِٱلْوَٰلِدَيْنِ إِحْسَٰنًا",
        ),
        KhutbahQuote(
            "Ebu Mes'ud r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Ko uputi " +
                "na dobro ima nagradu kao onaj ko ga učini**.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "Ja sam, uistinu, Allah, drugog boga, osim Mene, nema; zato se samo Meni " +
                "klanjaj i **molitvu obavljaj – da bih ti uvijek na umu bio**!",
            "Kur'an, Ta-Ha 14",
            arabic = "إِنَّنِىٓ أَنَا ٱللَّهُ لَآ إِلَٰهَ إِلَّآ أَنَا۠ فَٱعْبُدْنِى وَأَقِمِ ٱلصَّلَوٰةَ لِذِكْرِىٓ",
        ),
        KhutbahQuote(
            "Ebu Hurejre r.a. prenosi da je Vjerovjesnik s.a.v.s. rekao: «**Allah ne " +
                "gleda u vaša tijela niti u vaša lica, nego gleda u vaša srca**.»",
            "Rijadus-salihin • Muslim",
        ),
        KhutbahQuote(
            "O vjernici, **bojte se Allaha i budite s onima koji su iskreni**!",
            "Kur'an, Et-Tevba 119",
            arabic = "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱتَّقُوا۟ ٱللَّهَ وَكُونُوا۟ مَعَ ٱلصَّٰدِقِينَ",
        ),
    ),
    switchOn = "Uključeno",
    switchOff = "Isključeno",
    portalCodeLabel = "Kod portala",
    secondaryLanguageLabel = "Drugi jezik (naizmjenično svake 2 min)",
    ramadanTitle = "Ramazan",
    iftarLabel = "Iftar",
    iftarCountdownName = "iftara",
    ramadanModeLabel = "Ramazanski način (samo tokom ramazana)",
    adhanTemplate = "Ezan: %s",
    preAdhanTitle = "Pripremite se za namaz",
    preAdhanNote = "Molimo utišajte telefone",
    adhanDuaTitle = "Dova poslije ezana",
    adhanDua = AdhanDua(
        arabic = "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ، آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ، وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ",
        transliteration = "Allahumme Rabbe hazihi'd-da'veti't-tamme ve's-salati'l-kaime, ati Muhammeden'il-vesilete ve'l-fadilete, veb'ashu mekamen mahmuden'illezi ve'adteh.",
        translation = "Allahu moj, Gospodaru ovog potpunog poziva i namaza koji se uspostavlja, podari Muhammedu vesilu i odliku i proživi ga na hvale vrijednom mjestu koje si mu obećao.",
        source = "Buharija",
    ),
    adhanSectionLabel = "Ezan",
    adhanSequenceLabel = "Ezanski ekrani",
    preAdhanLabel = "Odbrojavanje prije ezana",
    adhanDurationLabel = "Trajanje ezana",
    adhanDuaDurationLabel = "Trajanje dove",
    secondsShort = "sek",
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
    clockWarningTitle = "Pažnja: sat televizora je možda pogrešan!",
    clockWarningBody = "Nakon nestanka struje sat televizora kasni — namaska " +
        "vremena mogu biti pogrešno prikazana. Povežite televizor na internet i " +
        "sat će se sam ispraviti, ili ga podesite ručno u postavkama televizora.",
    clockWarningButton = "Otvori mrežne postavke",
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
