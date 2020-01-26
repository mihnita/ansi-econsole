package mnita.ansiconsole.preferences;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.utils.ColorCache;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class AnsiConsolePreferenceUtils {
	/* File config here:
	 *   <workspace>\.metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.debug.ui.prefs
	 * Example:
	 *   org.eclipse.debug.ui.consoleBackground=53,53,53
	 *   org.eclipse.debug.ui.errorColor=225,30,70
	 *   org.eclipse.debug.ui.inColor=192,192,192
	 *   org.eclipse.debug.ui.outColor=192,192,192
	 */
    private static final String DEBUG_CONSOLE_PLUGIN_ID        = "org.eclipse.debug.ui";
    private static final String DEBUG_CONSOLE_FALLBACK_BKCOLOR = "47,47,47"; // Default dark background
    private static final String DEBUG_CONSOLE_FALLBACK_FGCOLOR = "192,192,192";
    private static final String DEBUG_CONSOLE_FALLBACK_ERRCOLOR = "255,0,0";
    private static final String DEBUG_CONSOLE_FALLBACK_LINK_COLOR = "111,197,238";

    private AnsiConsolePreferenceUtils() { /* Utility class, should not be instantiated */ }

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

    public static boolean getBoolean(String name) {
        return AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(name);
    }

    public static String getString(String name) {
        return AnsiConsoleActivator.getDefault().getPreferenceStore().getString(name);
    }

    public static Color prefGetColor(String id) {
        String strColor = AnsiConsoleActivator.getDefault().getPreferenceStore().getString(id);
        return colorFromStringRgb(strColor);
    }

    public static void setValue(String name, boolean value) {
        AnsiConsoleActivator.getDefault().getPreferenceStore().setValue(name, value);
    }

    private static Color debugConsoleBgColor = null;
    public static Color getDebugConsoleBgColor() {
    	if (debugConsoleBgColor == null) {
	        String value = Platform.getPreferencesService().getString(DEBUG_CONSOLE_PLUGIN_ID,
	        		"org.eclipse.debug.ui.consoleBackground", DEBUG_CONSOLE_FALLBACK_BKCOLOR, null);
	        debugConsoleBgColor = colorFromStringRgb(value);
    	}
        return debugConsoleBgColor;
    }

    private static Color debugConsoleFgColor = null;
    public static Color getDebugConsoleFgColor() {
    	if (debugConsoleFgColor == null) {
	        String value = Platform.getPreferencesService().getString(DEBUG_CONSOLE_PLUGIN_ID,
	        		"org.eclipse.debug.ui.outColor", DEBUG_CONSOLE_FALLBACK_FGCOLOR, null);
	        debugConsoleFgColor = colorFromStringRgb(value);
    	}
        return debugConsoleFgColor;
    }

    private static Color debugConsoleErrorColor = null;
    public static Color getDebugConsoleErrorColor() {
    	if (debugConsoleErrorColor == null) {
	        String value = Platform.getPreferencesService().getString(DEBUG_CONSOLE_PLUGIN_ID,
	        		"org.eclipse.debug.ui.errorColor", DEBUG_CONSOLE_FALLBACK_ERRCOLOR, null);
	        debugConsoleErrorColor = colorFromStringRgb(value);
    	}
        return debugConsoleErrorColor;
    }

    private static Color hyperlinkColor = null;
    public static Color getHyperlinkColor() {
    	if (hyperlinkColor == null) {
	        String value = Platform.getPreferencesService().getString("org.eclipse.ui.workbench",
	        		"HYPERLINK_COLOR", DEBUG_CONSOLE_FALLBACK_LINK_COLOR, null);
	        hyperlinkColor = colorFromStringRgb(value);
    	}
        return hyperlinkColor;
    }

    // Convenience methods, simple "aliases" for get / set of common values
    
    /* True if we should interpret bold as intense, italic as reverse, the way the (old?) Windows console does */
    public static boolean useWindowsMapping() {
    	return AnsiConsolePreferenceUtils.getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
    }

    public static boolean isAnsiConsoleEnabled() {
    	return AnsiConsolePreferenceUtils.getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED);
    }

    public static void setAnsiConsoleEnabled(boolean enabled) {
    	AnsiConsolePreferenceUtils.setValue(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED, enabled);
    }
    
    public static String getPreferredPalette() {
    	return AnsiConsolePreferenceUtils.getString(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE);
    }

    public static boolean showAnsiEscapes() {
    	return AnsiConsolePreferenceUtils.getBoolean(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES);
    }

    public static boolean tryPreservingStdErrColor() {
        return AnsiConsolePreferenceUtils.getBoolean(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR);
    }
}
