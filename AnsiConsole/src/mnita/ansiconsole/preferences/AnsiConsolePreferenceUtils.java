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
     */
    private static final String ECLIPSE_UI_WORKBENCH = "org.eclipse.ui.workbench";
    private static final String DEBUG_CONSOLE_PLUGIN_ID = "org.eclipse.debug.ui";
    private static final String DEBUG_CONSOLE_FALLBACK_BKCOLOR = "47,47,47"; // Default dark background
    private static final String DEBUG_CONSOLE_FALLBACK_FGCOLOR = "192,192,192";
    private static final String DEBUG_CONSOLE_FALLBACK_ERRCOLOR = "255,0,0";
    private static final String DEBUG_CONSOLE_FALLBACK_LINK_COLOR = "111,197,238";

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

    static {
        refresh();

        // Add some listeners to react to setting changes in my plugin and a few other areas

        // When some setting changes in my own plugin
        AnsiConsoleActivator.getDefault().getPreferenceStore()
                .addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh() );

        // When some setting changes in the debug settings (for example colors)
        IPreferenceStore preferenceStoreDebug = new ScopedPreferenceStore(InstanceScope.INSTANCE, DEBUG_CONSOLE_PLUGIN_ID);
        // This is to capture the changes of the hyperlink color
        IPreferenceStore preferenceStoreWorkbench = new ScopedPreferenceStore(InstanceScope.INSTANCE, ECLIPSE_UI_WORKBENCH);

        preferenceStoreWorkbench.addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh() );
        preferenceStoreDebug.addPropertyChangeListener(evt -> AnsiConsolePreferenceUtils.refresh() );
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

    // This is not cached, because it can change from both Preferences and the icon on console
    public static boolean isAnsiConsoleEnabled() {
        return isAnsiConsoleEnabled;
    }

    public static void setAnsiConsoleEnabled(boolean enabled) {
        isAnsiConsoleEnabled = enabled;
        AnsiConsoleActivator
                .getDefault()
                .getPreferenceStore()
                .setValue(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED, enabled);
    }

    /** True if we should interpret bold as intense, italic as reverse, the way the (old?) Windows console does */
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

    public static void refresh() {
        final IPreferenceStore prefStore = AnsiConsoleActivator.getDefault().getPreferenceStore();
        final IPreferencesService prefServices = Platform.getPreferencesService();

        String value = prefServices.getString(DEBUG_CONSOLE_PLUGIN_ID,
                "org.eclipse.debug.ui.consoleBackground", DEBUG_CONSOLE_FALLBACK_BKCOLOR, null);
        debugConsoleBgColor = colorFromStringRgb(value);

        value = prefServices.getString(DEBUG_CONSOLE_PLUGIN_ID,
                "org.eclipse.debug.ui.outColor", DEBUG_CONSOLE_FALLBACK_FGCOLOR, null);
        debugConsoleFgColor = colorFromStringRgb(value);

        value = prefServices.getString(DEBUG_CONSOLE_PLUGIN_ID,
                "org.eclipse.debug.ui.errorColor", DEBUG_CONSOLE_FALLBACK_ERRCOLOR, null);
        debugConsoleErrorColor = colorFromStringRgb(value);

        value = prefServices.getString(ECLIPSE_UI_WORKBENCH,
                "HYPERLINK_COLOR", DEBUG_CONSOLE_FALLBACK_LINK_COLOR, null);
        hyperlinkColor = colorFromStringRgb(value);

        useWindowsMapping = prefStore.getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        getPreferredPalette = prefStore.getString(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE);
        showAnsiEscapes = prefStore.getBoolean(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES);
        tryPreservingStdErrColor = prefStore.getBoolean(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR);
        isAnsiConsoleEnabled = prefStore.getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED);
    }
}
