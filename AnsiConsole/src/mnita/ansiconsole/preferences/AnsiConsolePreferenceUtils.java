package mnita.ansiconsole.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;
import mnita.ansiconsole.utils.ColorCache;

public class AnsiConsolePreferenceUtils {
    /* File config here:
     *   <workspace>\.metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.debug.ui.prefs
     * Example:
     *   org.eclipse.debug.ui.consoleBackground=53,53,53
     *   org.eclipse.debug.ui.errorColor=225,30,70
     *   org.eclipse.debug.ui.inColor=192,192,192
     *   org.eclipse.debug.ui.outColor=192,192,192
     * Limiting console output:
     *   1M:
     *     Console.highWaterMark=1008000
     *     Console.lowWaterMark=1000000
     *   80K (default):
     *     Console.highWaterMark=88000
     *     Console.lowWaterMark not defined
     *   No limit (lowWaterMark / highWaterMark ignored):
     *     Console.limitConsoleOutput=false
     *
     * This was added in Eclipse 2020-12. Disaster performance, more than 20 times slower
     *   <workspace>\.metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.ui.console.prefs
     *     org.eclipse.ui.console.P_CONSOLE_WORD_WRAP=true
     */
    private static final String ECLIPSE_UI_WORKBENCH = "org.eclipse.ui.workbench";
    private static final String ECLIPSE_UI_CONSOLE = "org.eclipse.ui.console";
    private static final String ECLIPSE_DEBUG_UI = "org.eclipse.debug.ui";

    private static final String DEBUG_CONSOLE_FALLBACK_BKCOLOR = "47,47,47"; // Default dark background
    private static final String DEBUG_CONSOLE_FALLBACK_FGCOLOR = "192,192,192";
    private static final String DEBUG_CONSOLE_FALLBACK_ERRCOLOR = "255,0,0";
    private static final String DEBUG_CONSOLE_FALLBACK_LINK_COLOR = "111,197,238";

    private static final IPreferenceStore PREF_STORE = AnsiConsoleActivator.getDefault().getPreferenceStore();

    // Caching, for better performance.
    // Start with "reasonable" values, but refresh() will update them from the saved preferences
    private static Color debugConsoleBgColor = null;
    private static Color debugConsoleFgColor = null;
    private static Color debugConsoleErrorColor = null;
    private static Color hyperlinkColor = null;
    private static String getPreferredPalette = AnsiConsoleColorPalette.getBestPaletteForOS();
    private static boolean useWindowsMapping = false;
    private static boolean showAnsiEscapes = false;
    private static boolean tryPreservingStdErrColor = true;
    private static boolean isAnsiConsoleEnabled = true;
    private static boolean clipboardPutRtf = true;

    static {
        refresh();

        // Add some listeners to react to setting changes in my plugin and a few other areas

        // When some setting changes in my own plugin
        PREF_STORE.addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh());

        // When some setting changes in the debug settings (for example colors)
        IPreferenceStore preferenceStoreDebug = new ScopedPreferenceStore(InstanceScope.INSTANCE, ECLIPSE_DEBUG_UI);
        // This is to capture the changes of the hyperlink color
        IPreferenceStore preferenceStoreWorkbench = new ScopedPreferenceStore(InstanceScope.INSTANCE, ECLIPSE_UI_WORKBENCH);

        preferenceStoreWorkbench.addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh());
        preferenceStoreDebug.addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh());
    }

    private AnsiConsolePreferenceUtils() {
        // Utility class, should not be instantiated
    }

    private static Color colorFromStringRgb(String strRgb) {
        Color result = null;
        String[] splitted = strRgb.split(",");
        if (splitted != null && splitted.length == 3) {
            int red = tryParseInteger(splitted[0]);
            int green = tryParseInteger(splitted[1]);
            int blue = tryParseInteger(splitted[2]);
            result = ColorCache.get(new RGB(red, green, blue));
        }
        return result;
    }

    public static int tryParseInteger(String text) {
        if ("".equals(text))
            return -1;

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Color getDebugConsoleBgColor() {
        return debugConsoleBgColor;
    }

    public static Color getDebugConsoleFgColor() {
        return debugConsoleFgColor;
    }

    public static Color getDebugConsoleErrorColor() {
        return debugConsoleErrorColor;
    }

    public static Color getHyperlinkColor() {
        return hyperlinkColor;
    }

    public static boolean putRtfInClipboard() {
        return clipboardPutRtf;
    }

    // This is not cached, because it can change from both Preferences and the icon on console
    public static boolean isAnsiConsoleEnabled() {
        return isAnsiConsoleEnabled;
    }

    public static void setAnsiConsoleEnabled(boolean enabled) {
        isAnsiConsoleEnabled = enabled;
        PREF_STORE.setValue(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED, enabled);
    }

    /** @return True if we should interpret bold as intense, italic as reverse, the way the (old?) Windows console does */
    public static boolean useWindowsMapping() {
        return useWindowsMapping;
    }

    public static String getPreferredPalette() {
        return getPreferredPalette;
    }

    public static boolean showAnsiEscapes() {
        return showAnsiEscapes;
    }

    public static boolean tryPreservingStdErrColor() {
        return tryPreservingStdErrColor;
    }

    public static int getWattermarkLevel() {
        final IPreferencesService prefServices = Platform.getPreferencesService();
        if (prefServices.getBoolean(ECLIPSE_DEBUG_UI, "Console.limitConsoleOutput", true, null)) {
            return prefServices.getInt(ECLIPSE_DEBUG_UI, "Console.lowWaterMark", 80000, null);
        }
        return Integer.MAX_VALUE;
    }

    public static boolean isWordWrapEnabled() {
        final IPreferencesService prefServices = Platform.getPreferencesService();
        return prefServices.getBoolean(ECLIPSE_UI_CONSOLE,
                "org.eclipse.ui.console.P_CONSOLE_WORD_WRAP", false, null);
    }

    public static boolean isPerformanceWarningEnabled() {
        return PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_ENABLE_PERFORMANCE_WARNING);
    }

    public static void setEnablePerformanceWarning(boolean newValue) {
        PREF_STORE.setValue(AnsiConsolePreferenceConstants.PREF_ENABLE_PERFORMANCE_WARNING, newValue);
    }

    public static void refresh() {
        final IPreferencesService prefServices = Platform.getPreferencesService();

        // Various console settings (the default Eclipse, not mine)
        String value = prefServices.getString(ECLIPSE_DEBUG_UI,
                "org.eclipse.debug.ui.consoleBackground", DEBUG_CONSOLE_FALLBACK_BKCOLOR, null);
        debugConsoleBgColor = colorFromStringRgb(value);

        value = prefServices.getString(ECLIPSE_DEBUG_UI,
                "org.eclipse.debug.ui.outColor", DEBUG_CONSOLE_FALLBACK_FGCOLOR, null);
        debugConsoleFgColor = colorFromStringRgb(value);

        value = prefServices.getString(ECLIPSE_DEBUG_UI,
                "org.eclipse.debug.ui.errorColor", DEBUG_CONSOLE_FALLBACK_ERRCOLOR, null);
        debugConsoleErrorColor = colorFromStringRgb(value);

        value = prefServices.getString(ECLIPSE_UI_WORKBENCH,
                "HYPERLINK_COLOR", DEBUG_CONSOLE_FALLBACK_LINK_COLOR, null);
        hyperlinkColor = colorFromStringRgb(value);

        // My own settings
        useWindowsMapping = PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        getPreferredPalette = PREF_STORE.getString(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE);
        showAnsiEscapes = PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES);
        tryPreservingStdErrColor = PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR);
        isAnsiConsoleEnabled = PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED);
        clipboardPutRtf = PREF_STORE.getBoolean(AnsiConsolePreferenceConstants.PREF_PUT_RTF_IN_CLIPBOARD);
    }
}
