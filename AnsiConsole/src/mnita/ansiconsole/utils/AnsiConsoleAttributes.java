package mnita.ansiconsole.utils;

import static mnita.ansiconsole.utils.AnsiCommands.*;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class AnsiConsoleAttributes implements Cloneable {
    public final static int UNDERLINE_NONE = -1; // nothing in SWT, a bit of an abuse

    public Integer currentBgColor;
    public Integer currentFgColor;
    public int     underline;
    public boolean bold;
    public boolean italic;
    public boolean invert;
    public boolean conceal;
    public boolean strike;
    public boolean framed;

    public AnsiConsoleAttributes() {
        reset();
    }

    public void reset() {
        currentBgColor = null;
        currentFgColor = null;
        underline = UNDERLINE_NONE;
        bold = false;
        italic = false;
        invert = false;
        conceal = false;
        strike = false;
        framed = false;
    }

    @Override
    public AnsiConsoleAttributes clone() {
        AnsiConsoleAttributes result = new AnsiConsoleAttributes();
        result.currentBgColor = currentBgColor;
        result.currentFgColor = currentFgColor;
        result.underline = underline;
        result.bold = bold;
        result.italic = italic;
        result.invert = invert;
        result.conceal = conceal;
        result.strike = strike;
        result.framed = framed;
        return result;
    }

    public static Color hiliteRgbColor(Color c) {
        if (c == null)
            return new Color(null, new RGB(0xff, 0xff, 0xff));
        int red = c.getRed() * 2;
        int green = c.getGreen() * 2;
        int blue = c.getBlue() * 2;

        if (red > 0xff)   red = 0xff;
        if (green > 0xff) green = 0xff;
        if (blue > 0xff)  blue = 0xff;

        return new Color(null, new RGB(red, green, blue)); // here
    }

    public static void updateRangeStyle(StyleRange range, AnsiConsoleAttributes attribute) {
        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        AnsiConsoleAttributes tempAttrib = attribute.clone();
        boolean hilite = false;

        if (useWindowsMapping) {
            if (tempAttrib.bold) {
                tempAttrib.bold = false; // not supported, rendered as intense, already done that
                hilite = true;
            }
            if (tempAttrib.italic) {
                tempAttrib.italic = false;
                tempAttrib.invert = true;
            }
            tempAttrib.underline = UNDERLINE_NONE; // not supported on Windows
            tempAttrib.strike = false; // not supported on Windows
            tempAttrib.framed = false; // not supported on Windows
        }

        RGB color;
        // Prepare the foreground color
        if (hilite && tempAttrib.currentFgColor != null && tempAttrib.currentFgColor < COMMAND_COLOR_INTENSITY_DELTA) {
            color = AnsiConsoleColorPalette.getColor(tempAttrib.currentFgColor + COMMAND_COLOR_INTENSITY_DELTA);
            hilite = false;
        }
        else
            color = AnsiConsoleColorPalette.getColor(tempAttrib.currentFgColor);

        if (color == null)
            range.foreground = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
        else
            range.foreground = new Color(null, color);

        if (hilite) {
            range.foreground = hiliteRgbColor(range.foreground);
            hilite = false;
        }

        // Prepare the background color
        color = AnsiConsoleColorPalette.getColor(tempAttrib.currentBgColor);
        if (color == null)
            range.background = AnsiConsolePreferenceUtils.getDebugConsoleBgColor();
        else
            range.background = new Color(null, color);

        // These two still mess with the foreground/background colors
        // We need to solve them before we use them for strike/underline/frame colors
        if (tempAttrib.invert) {
            Color tmp = range.background;
            range.background = range.foreground;
            range.foreground = tmp;
        }

        if (tempAttrib.conceal)
            range.foreground = range.background;

        range.font = null;
        range.fontStyle = SWT.NORMAL;
        // Prepare the rest of the attributes
        if (tempAttrib.bold)
            range.fontStyle |= SWT.BOLD;

        if (tempAttrib.italic)
            range.fontStyle |= SWT.ITALIC;

        if (tempAttrib.underline != UNDERLINE_NONE) {
            range.underline = true;
            range.underlineColor = range.foreground;
            range.underlineStyle = tempAttrib.underline;
        }
        else
            range.underline = false;

        range.strikeout = tempAttrib.strike;
        range.strikeoutColor = range.foreground;

        if (tempAttrib.framed) {
            range.borderStyle = SWT.BORDER_SOLID;
            range.borderColor = range.foreground;
        }
        else
            range.borderStyle = SWT.NONE;
    }
}
