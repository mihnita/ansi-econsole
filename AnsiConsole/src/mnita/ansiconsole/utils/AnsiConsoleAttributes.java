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
    public Integer currentBgColor;
    public Integer currentFgColor;
    public boolean underline;
    public boolean bold;
    public boolean italic;
    public boolean invert;
    public boolean conceal;
    public boolean strike;

    public AnsiConsoleAttributes() {
        reset();
    }

    public void reset() {
        currentBgColor = null;
        currentFgColor = null;
        underline = false;
        bold = false;
        italic = false;
        invert = false;
        conceal = false;
        strike = false;
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
        return result;
    }

    public static Color hiliteRgbColor( Color c ) {
        if( c == null )
            return  new Color(null, new RGB(0xff, 0xff, 0xff));
        int red = c.getRed() * 2;
        int green = c.getGreen() * 2;
        int blue = c.getBlue() * 2;

        if( red > 0xff ) red = 0xff;
        if( green > 0xff ) green = 0xff;
        if( blue > 0xff ) blue = 0xff;

        return new Color(null, new RGB(red, green, blue)); // here
    }

//    public RGB getBgColor() {
//        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
//        if( currentBgColor == null ) { // default background color
//            Color defaultConsoleBkColor = AnsiConsolePreferenceUtils.getDebugConsoleBkColor();
//            if (useWindowsMapping && underline)
//                defaultConsoleBkColor = hiliteColor(defaultConsoleBkColor);
//            return defaultConsoleBkColor.getRGB();
//        }
//
//        // We do the hilite using the color palette
//        if( useWindowsMapping && underline && currentBgColor < COMMAND_COLOR_INTENSITY_DELTA)
//            return AnsiConsoleColorPalette.getColor(currentBgColor + COMMAND_COLOR_INTENSITY_DELTA);
//        else
//            return AnsiConsoleColorPalette.getColor(currentBgColor);
//    }
//
//    public RGB getFgColor() {
//        if( conceal ) // With conceal the text color is the same as the background one
//            return getBgColor();
//
//        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
//        if( currentFgColor == null ) {
//            Color defaultConsoleFgColor = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
//            if(useWindowsMapping && bold)
//                defaultConsoleFgColor = hiliteColor(defaultConsoleFgColor);
//            return defaultConsoleFgColor.getRGB();
//        }
//
//        // We do the hilite using the color palette
//        if( useWindowsMapping && bold && currentFgColor < COMMAND_COLOR_INTENSITY_DELTA)
//            return AnsiConsoleColorPalette.getColor(currentFgColor + COMMAND_COLOR_INTENSITY_DELTA);
//        else
//            return AnsiConsoleColorPalette.getColor(currentFgColor);
//    }

    public void updateRangeStyle(StyleRange range) {
        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        AnsiConsoleAttributes tempAttrib = this.clone();
        boolean hilite = false;

        if( useWindowsMapping ) {
            if (tempAttrib.bold) {
                tempAttrib.bold = false; // not supported, rendered as intense, already done that
                hilite = true;
            }
            if (tempAttrib.italic) {
                tempAttrib.italic = false;
                tempAttrib.invert = true;
            }
            tempAttrib.underline = false; // not supported on Windows
            tempAttrib.strike = false; // not supported on Windows
        }

        RGB color;
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

        color = AnsiConsoleColorPalette.getColor(tempAttrib.currentBgColor);
        if (color == null) {
            range.background = AnsiConsolePreferenceUtils.getDebugConsoleBgColor();
        }
        else
            range.background = new Color(null, color);

        range.underline = tempAttrib.underline;
        if( tempAttrib.bold )
            range.fontStyle |= SWT.BOLD;
        if( tempAttrib.italic )
            range.fontStyle |= SWT.ITALIC;
        if( tempAttrib.strike ) {
            range.strikeout = true;
            range.strikeoutColor = range.foreground;
        }

        if( tempAttrib.invert ) {
            Color tmp = range.background;
            range.background = range.foreground;
            range.foreground = tmp;
        }

        if (tempAttrib.conceal)
            range.foreground = range.background;
    }
}
