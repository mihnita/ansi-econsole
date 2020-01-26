package mnita.ansiconsole.utils;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorCache {
    private final static HashMap<RGB, Color> CACHE = new HashMap<RGB, Color>();

    public static Color get(RGB rgb) {
        Color result;
        if (CACHE.containsKey(rgb)) {
            result = CACHE.get(rgb);
        } else {
            result = new Color(null, rgb);
            CACHE.put(rgb, result);
        }
        return result;
    }
}
