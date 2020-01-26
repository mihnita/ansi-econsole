package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;

import mnita.ansiconsole.AnsiConsoleUtils;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;
import mnita.ansiconsole.utils.AnsiConsoleAttributes;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

public class AnsiConsoleStyleListener implements LineStyleListener, IPositionUpdater {
    private static final Pattern PATTERN = Pattern.compile(AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX);
    private static final Font MONO_FONT = new Font(null, "Monospaced", 6, SWT.NORMAL);

    private final boolean showEscapeCodes = AnsiConsolePreferenceUtils.showAnsiEscapes();
    private final Color defForegroundColor = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
    private final Color hyperlinkColor = AnsiConsolePreferenceUtils.getHyperlinkColor();
    private final Color defaultErrorColor = AnsiConsolePreferenceUtils.getDebugConsoleErrorColor();

    private final DefaultPositionUpdater defaultPositionUpdater = new DefaultPositionUpdater(AnsiPosition.POSITION_NAME);
    private final IDocument document;

    private AnsiConsoleAttributes lastVisibleAttribute = new AnsiConsoleAttributes();

    public AnsiConsoleStyleListener(IDocument document) {
        this.document = document;
        this.document.addPositionCategory(AnsiPosition.POSITION_NAME);
        this.document.addPositionUpdater(this);
        AnsiConsoleColorPalette.setPalette(AnsiConsolePreferenceUtils.getPreferredPalette());
    }

    private void addRange(List<StyleRange> ranges, int start, int length,
            AnsiConsoleAttributes attributes, Color foreground, boolean isCode) {

        StyleRange range = new StyleRange(start, length, foreground, null);
        AnsiConsoleAttributes.updateRangeStyle(range, attributes);
        if (isCode) {
            if (showEscapeCodes) {
                range.font = MONO_FONT; // Show the codes in small, monospace font
            } else {
                range.metrics = new GlyphMetrics(0, 0, 0); // Hide the codes
            }
        }
        ranges.add(range);
    }

    @Override
    public void lineGetStyle(LineStyleEvent event) {
        if (event == null || event.lineText == null || event.lineText.length() == 0)
            return;

        if (document == null)
            return;

        boolean isAnsiconEnabled = AnsiConsolePreferenceUtils.isAnsiConsoleEnabled();
        if (!isAnsiconEnabled)
            return;

        int eventOffset = event.lineOffset;
        int eventLength = event.lineText.length();
        if (event.styles == null) { // It looks that in some cases this comes in as null
            event.styles = new StyleRange[0];
        }

        Position[] positions;
        try {
            positions = document.getPositions(AnsiPosition.POSITION_NAME);
        } catch (BadPositionCategoryException e) {
            return;
        }

        if (positions.length == 0)
            return;

        Color foregroundColor = defForegroundColor;
        if (AnsiConsolePreferenceUtils.tryPreservingStdErrColor()) {
            for (StyleRange range : event.styles) {
                if (defaultErrorColor.equals(range.foreground)) {
                	foregroundColor = defaultErrorColor;
                    break;
                }
            }
        }

        List<StyleRange> ranges = new ArrayList<>();
        AnsiConsoleAttributes prevAttr = lastVisibleAttribute;
        int prevPos = eventOffset;

        for (Position position : positions) {
            AnsiPosition apos = (AnsiPosition) position;
            if (apos.getOffset() > eventOffset + eventLength) // we passed the end of line, stop searching
                break;
            if (apos.overlapsWith(eventOffset, eventLength)) {
                if (apos.offset != prevPos) {
                    addRange(ranges, prevPos, apos.offset - prevPos, prevAttr, foregroundColor, false);
                }
                addRange(ranges, apos.offset, apos.length, apos.attributes, foregroundColor, true);
                prevPos = apos.offset + apos.length;
            }
            prevAttr = apos.attributes;
        }
        addRange(ranges, prevPos, eventOffset + eventLength - prevPos, prevAttr, foregroundColor, false);

        if (!ranges.isEmpty()) {
            // Copy the links that might already exist
            for (StyleRange range : event.styles) {
                if (hyperlinkColor.equals(range.foreground))
                    ranges.add(range);
            }
            event.styles = ranges.toArray(new StyleRange[0]);
        }
    }

    private static List<AnsiPosition> findPositions(int offset, String currentText) {
        List<AnsiPosition> result = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(currentText);
        while (matcher.find()) {
            int start = matcher.start();
            String group = matcher.group();
            result.add(new AnsiPosition(start + offset, group));
        }
        return result;
    }

    @Override
    public void update(DocumentEvent event) {
        IDocument eventDocument = event.getDocument();

        int offset = event.getOffset();
        int length = event.getLength();
        String text = event.getText();
        try {
            if (offset == 0 && length != 0) { // removes the beginning, scan to find and save the last style
                for (Position pos : eventDocument.getPositions(AnsiPosition.POSITION_NAME)) {
                    if (pos.offset >= length)
                        break;
                    lastVisibleAttribute = ((AnsiPosition) pos).attributes;
                }
            }
            defaultPositionUpdater.update(event);
            if (length == 0 && offset + text.length() == eventDocument.getLength()) { // added text, at the end
                int lineCount = eventDocument.getNumberOfLines(offset, length);
                for (int i = 0; i < lineCount; i++) {
                    List<AnsiPosition> newPos = findPositions(offset, text);
                    for (AnsiPosition apos : newPos) {
                        eventDocument.addPosition(AnsiPosition.POSITION_NAME, apos);
                    }
                }
            }
        } catch (BadPositionCategoryException | BadLocationException e) {
            e.printStackTrace();
        }
    }
}
