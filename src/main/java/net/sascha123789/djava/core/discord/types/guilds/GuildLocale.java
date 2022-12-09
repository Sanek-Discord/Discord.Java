package net.sascha123789.djava.core.discord.types.guilds;

public enum GuildLocale {
    DANISH("da"),
    GERMAN("de"),
    ENGLISH_UK("en-GB"),
    ENGLISH_US("en-US"),
    SPANISH("es-ES"),
    FRENCH("fr"),
    CROATIAN("hr"),
    ITALIAN("it"),
    LITHUANIAN("lt"),
    HUNGARIAN("hu"),
    DUTCH("nl"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt-BR"),
    ROMANIAN("ro"),
    FINNISH("fi"),
    SWEDISH("sv-SE"),
    VIETNAMESE("vi"),
    TURKISH("tr"),
    CZECH("cs"),
    GREEK("el"),
    BULGARIAN("bg"),
    RUSSIAN("ru"),
    UKRAINIAN("uk"),
    HINDI("hi"),
    THAI("th"),
    CHINESE_CHINA("zh-CN"),
    JAPANESE("ja"),
    CHINESE_TAIWAN("zh-TW"),
    KOREAN("ko");

    private String locale;

    private GuildLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }
}
