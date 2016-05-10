package mnita.ansiconsole.preferences;

import mnita.ansiconsole.AnsiConsoleActivator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class AnsiConsolePreferenceUtils {
    private final static String DEBUG_CONSOLE_PLUGIN_ID        = "org.eclipse.debug.ui";
    private final static String DEBUG_CONSOLE_FALLBACK_BKCOLOR = "0,0,0";
    private final static String DEBUG_CONSOLE_FALLBACK_FGCOLOR = "192,192,192";

    static Color colorFromStringRgb(String strRgb) {
        Color result = null;
        String[] splitted = strRgb.split(",");
        if (splitted != null && splitted.length == 3) {
            int red = tryParseInteger(splitted[0]);
            int green = tryParseInteger(splitted[1]);
            int blue = tryParseInteger(splitted[2]);
            result = new Color(null, new RGB(red, green, blue));
        }
        return result;
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

    public static Color getDebugConsoleBgColor() {
        IPreferencesService ps = Platform.getPreferencesService();
        String value = ps.getString(DEBUG_CONSOLE_PLUGIN_ID, "org.eclipse.debug.ui.consoleBackground",
                DEBUG_CONSOLE_FALLBACK_BKCOLOR, null);
        return colorFromStringRgb(value);
    }

    public static Color getDebugConsoleFgColor() {
        IPreferencesService ps = Platform.getPreferencesService();
        String value = ps.getString(DEBUG_CONSOLE_PLUGIN_ID, "org.eclipse.debug.ui.outColor",
                DEBUG_CONSOLE_FALLBACK_FGCOLOR, null);
        return colorFromStringRgb(value);
    }

    public static int tryParseInteger(String text) {
        if ("".equals(text))
            return -1;

        try {
            return Integer.parseInt(text);
        } catch (@SuppressWarnings("unused") NumberFormatException e) {
            return -1;
        }
    }

    public static void setValue(String name, boolean value) {
        AnsiConsoleActivator.getDefault().getPreferenceStore().setValue(name, value);
    }
}
