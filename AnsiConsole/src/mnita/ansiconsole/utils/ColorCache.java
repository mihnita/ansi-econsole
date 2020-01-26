package mnita.ansiconsole.utils;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorCache {
    private static final HashMap<RGB, Color> CACHE = new HashMap<>();

    private ColorCache() {
        // Utility class, should not be instantiated
    }

    public static Color get(RGB rgb) {
        return CACHE.computeIfAbsent(rgb, color -> new Color(null, color));
    }
}
