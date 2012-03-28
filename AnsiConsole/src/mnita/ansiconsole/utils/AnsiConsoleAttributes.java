package mnita.ansiconsole.utils;

import static mnita.ansiconsole.utils.AnsiCommands.*;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;

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
        return result;
    }

    public static Color hiliteColor( Color c ) {
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

    public RGB getBgColor() {
        if( currentBgColor == null )
            return null;
        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        if( useWindowsMapping && underline )
            return AnsiConsoleColorPalette.getColor(currentBgColor.intValue() + COMMAND_COLOR_INTENSITY_DELTA);
        else
            return AnsiConsoleColorPalette.getColor(currentBgColor.intValue());
    }

    public RGB getFgColor() {
        if( currentFgColor == null )
            return null;

        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);
        if( useWindowsMapping && bold )
            return AnsiConsoleColorPalette.getColor(currentFgColor.intValue() + COMMAND_COLOR_INTENSITY_DELTA);
        else
            return AnsiConsoleColorPalette.getColor(currentFgColor.intValue());
    }

    public void updateRangeStyle(StyleRange range) {
        boolean useWindowsMapping = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING);

        RGB rgbColor = getFgColor();
        if( rgbColor != null )
            range.foreground = new Color(null, rgbColor);
        else
            if( useWindowsMapping && bold )
                range.foreground = hiliteColor(range.foreground);

        rgbColor = getBgColor();
        if( rgbColor != null )
            range.background = new Color(null, rgbColor);
        else
            if( useWindowsMapping && underline )
                range.background = hiliteColor(range.background);

        if( !useWindowsMapping )
            range.underline = underline;
        if( bold && !useWindowsMapping )
            range.fontStyle |= SWT.BOLD;
        if( italic )
            range.fontStyle |= SWT.ITALIC;

        if( invert ) {
            Color tmp = range.background;
            range.background = range.foreground;
            range.foreground = tmp;
        }
    }
}
