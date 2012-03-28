package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;
import mnita.ansiconsole.utils.AnsiConsoleAttributes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.RGB;

import static mnita.ansiconsole.utils.AnsiCommands.*;

public class AnsiConsoleStyleListener implements LineStyleListener {
    private AnsiConsoleAttributes lastAttributes = new AnsiConsoleAttributes();
    private AnsiConsoleAttributes currentAttributes = new AnsiConsoleAttributes();
    private final static Pattern pattern = Pattern.compile("\u001b\\[[\\d;]*m");

    int lastRangeEnd = 0;

    private static int tryParseInteger(String text) {
        if( "".equals(text) )
            return 0;

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean interpretCommand(String cmd) {
        boolean result = false;

        int nCmd = tryParseInteger(cmd);

        switch( nCmd ) {
            case COMMAND_ATTR_RESET:             currentAttributes.reset(); break;

            case COMMAND_ATTR_INTENSITY_BRIGHT:  currentAttributes.bold = true; break;
            case COMMAND_ATTR_INTENSITY_FAINT:   currentAttributes.bold = false; break;
            case COMMAND_ATTR_INTENSITY_NORMAL:  currentAttributes.bold = false; break;

            case COMMAND_ATTR_ITALIC:            currentAttributes.italic = true; break;
            case COMMAND_ATTR_ITALIC_OFF:        currentAttributes.italic = false; break;

            case COMMAND_ATTR_UNDERLINE:         currentAttributes.underline = true; break;
            case COMMAND_ATTR_UNDERLINE_OFF:     currentAttributes.underline = false; break;

            case COMMAND_ATTR_NEGATIVE_ON:       currentAttributes.invert = true; break;
            case COMMAND_ATTR_NEGATIVE_Off:      currentAttributes.invert = false; break;

            case COMMAND_COLOR_FOREGROUND_RESET: currentAttributes.currentFgColor = null; break;
            case COMMAND_COLOR_BACKGROUND_RESET: currentAttributes.currentBgColor = null; break;

            default:
                if( nCmd >= COMMAND_COLOR_FOREGROUND_FIRST && nCmd <= COMMAND_COLOR_FOREGROUND_LAST ) // text color
                    currentAttributes.currentFgColor = Integer.valueOf(nCmd - COMMAND_COLOR_FOREGROUND_FIRST);
                else if( nCmd >= COMMAND_COLOR_BACKGROUND_FIRST && nCmd <= COMMAND_COLOR_BACKGROUND_LAST ) // background color
                    currentAttributes.currentBgColor = Integer.valueOf(nCmd - COMMAND_COLOR_BACKGROUND_FIRST);
        }

        return result;
    }

    private static Color prefGetColor(String id) {
        Color result = null;
        String strBgColor = AnsiConsoleActivator.getDefault().getPreferenceStore().getString(id);
        String[] splitted = strBgColor.split(",");
        if(splitted != null && splitted.length == 3) {
            int red = tryParseInteger(splitted[0]);
            int green = tryParseInteger(splitted[1]);
            int blue = tryParseInteger(splitted[2]);
            result = new Color(null, new RGB(red,green,blue));
        }
        return result;
    }

    private void addRange(List<StyleRange> ranges, int start, int length, Color foreground, boolean isCode) {
        StyleRange range = new StyleRange( start, length, foreground, null );
        lastAttributes.updateRangeStyle(range);

        if( isCode ) {
            boolean showEscapeCodes = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES);
            if( showEscapeCodes )
                range.font = new Font(null, "Monospaced", 6, SWT.NORMAL);
            else
                range.metrics = new GlyphMetrics(0, 0, 0);
        }
        else
            lastAttributes = currentAttributes.clone();

        ranges.add(range);
        lastRangeEnd = lastRangeEnd + range.length;
    }

    @Override
    public void lineGetStyle(LineStyleEvent event) {
        if( (event == null) || (event.lineText == null) || (event.lineText.length() == 0) )
            return;
        
        boolean isAnsiconEnabled = AnsiConsoleActivator.getDefault().getPreferenceStore().getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED);
        if( ! isAnsiconEnabled )
            return;

        String currentPalette = AnsiConsoleActivator.getDefault().getPreferenceStore().getString(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE);
        AnsiConsoleColorPalette.setPalette(currentPalette);
        StyleRange defStyle;

        if (event.styles != null && event.styles.length > 0) {
            defStyle = (StyleRange) event.styles[0].clone();
            if (defStyle.background == null)
                defStyle.background = prefGetColor(AnsiConsolePreferenceConstants.PREF_BGCOLOR);
        }
        else {
            defStyle = new StyleRange(1, lastRangeEnd,
                    new Color(null, AnsiConsoleColorPalette.getColor(0)),
                    new Color(null, AnsiConsoleColorPalette.getColor(15)),
                    SWT.NORMAL);
        }

        lastRangeEnd = 0;
        List<StyleRange> ranges = new ArrayList<StyleRange>();
        String currentText = event.lineText;
        Matcher matcher = pattern.matcher(currentText);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            String theEscape = currentText.substring(matcher.start() + 2, matcher.end() - 1);
            for( String cmd : theEscape.split(";") )
                interpretCommand(cmd);

            if( lastRangeEnd != start )
                addRange(ranges, event.lineOffset + lastRangeEnd, start - lastRangeEnd, defStyle.foreground, false);

            addRange(ranges, event.lineOffset + start, end - start, defStyle.foreground, true);
        }
        if( lastRangeEnd != currentText.length() )
            addRange(ranges, event.lineOffset + lastRangeEnd, currentText.length() - lastRangeEnd, defStyle.foreground, false);

        if( !ranges.isEmpty() )
            event.styles = ranges.toArray(new StyleRange[ranges.size()]);
    }
}
