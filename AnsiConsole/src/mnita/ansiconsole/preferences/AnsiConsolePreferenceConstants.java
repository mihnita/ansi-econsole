package mnita.ansiconsole.preferences;

public class AnsiConsolePreferenceConstants {
    public static final String PREF_ANSI_CONSOLE_ENABLED = "booleanEnabled";
    public static final String PREF_ENABLE_PERFORMANCE_WARNING = "booleanPerformanceWarningEnabled";
    public static final String PREF_WINDOWS_MAPPING = "booleanWindowsMapping";
    public static final String PREF_SHOW_ESCAPES = "booleanShowEscapes";
    public static final String PREF_COLOR_PALETTE = "choiceColorPalette";
    public static final String PREF_KEEP_STDERR_COLOR = "booleanKeepStderrColor";
    public static final String PREF_PUT_RTF_IN_CLIPBOARD = "booleanPutRtfInClipboard";

    private AnsiConsolePreferenceConstants() {
        // Utility class, should not be instantiated
    }
}
