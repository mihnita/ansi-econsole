package mnita.ansiconsole.utils;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorCache {
    private static final HashMap<RGB, Color> CACHE = new HashMap<RGB, Color>();

    private ColorCache() { /* Utility class, should not be instantiated */ }

    public static Color get(RGB rgb) {
        Color result = CACHE.get(rgb);
        if (result == null) {
            result = new Color(null, rgb);
            CACHE.put(rgb, result);
        }
        return result;
    }
}
