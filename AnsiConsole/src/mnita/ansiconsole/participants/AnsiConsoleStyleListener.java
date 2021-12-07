package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

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
    private static final Font MONO_FONT = new Font(null, "Monospaced", 6, SWT.NORMAL);

    private final DefaultPositionUpdater defaultPositionUpdater = new DefaultPositionUpdater(AnsiPosition.POSITION_NAME);
    private final HashMap<Integer, List<StyleRange>> offsetToStyleRangeCache = new HashMap<>();
    private final IDocument document;
    private boolean documentEverScanned = false;

    private AnsiConsoleAttributes lastVisibleAttribute = new AnsiConsoleAttributes();

    public AnsiConsoleStyleListener(IDocument document) {
        this.document = document;
        this.document.addPositionCategory(AnsiPosition.POSITION_NAME);
        this.document.addPositionUpdater(this);
        AnsiConsoleColorPalette.setPalette(AnsiConsolePreferenceUtils.getPreferredPalette());
    }

    private static void addRange(List<StyleRange> ranges, int start, int length,
            AnsiConsoleAttributes attributes, Color foreground, boolean isCode) {

        final StyleRange range = new StyleRange(start, length, foreground, null);
        AnsiConsoleAttributes.updateRangeStyle(range, attributes);
        if (isCode) {
            if (AnsiConsolePreferenceUtils.showAnsiEscapes()) {
                range.font = MONO_FONT; // Show the codes in small, monospaced font
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

        if (!AnsiConsolePreferenceUtils.isAnsiConsoleEnabled())
            return;

        final int eventOffset = event.lineOffset;
        final int eventLength = event.lineText.length();
        final List<StyleRange> cachedRanges = offsetToStyleRangeCache.get(eventOffset);
        if (cachedRanges != null) {
            event.styles = cachedRanges.toArray(new StyleRange[0]);
            return;
        }
        if (event.styles == null) { // It looks that in some cases this comes in as null
            event.styles = new StyleRange[0];
        }

        if (!documentEverScanned) {
            calculateDocumentAnsiPositions(document, 0, 0, null);
        }

        Position[] positions;
        try {
            positions = document.getPositions(AnsiPosition.POSITION_NAME);
        } catch (BadPositionCategoryException e) {
            return;
        }

        if (positions.length == 0)
            return;

        final Color errorColor = AnsiConsolePreferenceUtils.getDebugConsoleErrorColor();
        Color foregroundColor = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
        if (AnsiConsolePreferenceUtils.tryPreservingStdErrColor()) {
            for (StyleRange range : event.styles) {
                if (errorColor.equals(range.foreground)) {
                    foregroundColor = errorColor;
                    break;
                }
            }
        }

        final List<StyleRange> ranges = new ArrayList<>();
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
            if (apos.attributes != null) {
                // Attributes can be null for non \e[..m escapes, for example \e[K
                // Those kind of escape sequences don't affect the attributes.
                prevAttr = apos.attributes;
            }
        }
        addRange(ranges, prevPos, eventOffset + eventLength - prevPos, prevAttr, foregroundColor, false);

        if (!ranges.isEmpty()) {
            // Copy the links that might already exist
            for (StyleRange range : event.styles) {
                if (AnsiConsolePreferenceUtils.getHyperlinkColor().equals(range.foreground))
                    ranges.add(range);
            }
            offsetToStyleRangeCache.put(eventOffset, ranges);
            event.styles = ranges.toArray(new StyleRange[0]);
        }
    }

    private static List<AnsiPosition> findPositions(int offset, String currentText) {
        final List<AnsiPosition> result = new ArrayList<>();
        final Matcher matcher = AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_TXT.matcher(currentText);
        while (matcher.find()) {
            int start = matcher.start();
            String group = matcher.group();
            result.add(new AnsiPosition(start + offset, group));
        }
        return result;
    }

    /**
     * We scan newly appended text, or the full document the first time.
     *
     * Although in a general an IDocument can also replace text, or insert text in the middle (or beginning)
     * that would complicate things a lot.
     * But this is a console output, so I don't expect such a thing to happen.
     * We only expect to append (at the end), or remove from the beginning (when the doc size > console buffer size)
     *   offset: 0, length: 0, "something" => Appended at beginning of doc (the doc was empty)
     *   offset:42, length: 0, "something" => Appended at end of doc. 42 was doc size before append.
     *   offset:0, length: 100, ""         => Removed the first 100 characters (replace with "" == remove)
     *
     * @param eventDocument The document
     * @param offset        Where was the text added. 0 => might mean remove, or the first content added
     * @param length        The length of the text replaced. 0 if append, != if removed (from the beginning)
     * @param text          For insert / append, this is the new text. For delete, empty string.
     */
    private void calculateDocumentAnsiPositions(IDocument eventDocument, int offset, int length, String text) {
        if (text == null)
            text = eventDocument.get();

        if (length != 0) { // This is the length of the text replaced. If not zero then this is not append, is replace.
            return;
        }
        // First time or the appended text is at the end (so it is not inserted text).
        if (documentEverScanned && offset + text.length() != eventDocument.getLength()) {
            return;
        }
        documentEverScanned = true;
        try {
            final int lineCount = eventDocument.getNumberOfLines(offset, length);
            for (int i = 0; i < lineCount; i++) {
                List<AnsiPosition> newPos = findPositions(offset, text);
                for (AnsiPosition apos : newPos) {
                    eventDocument.addPosition(AnsiPosition.POSITION_NAME, apos);
                }
            }
        } catch (BadPositionCategoryException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DocumentEvent event) {
        // Make sure we don't do anything if disabled
        if (!AnsiConsolePreferenceUtils.isAnsiConsoleEnabled())
            return;

        final IDocument eventDocument = event.getDocument();

        final int offset = event.getOffset();
        final int length = event.getLength();
        final String text = event.getText();
        try {
            if (offset == 0 && length != 0) { // This removes the beginning. We scan to find and save the last style.
                for (Position pos : eventDocument.getPositions(AnsiPosition.POSITION_NAME)) {
                    if (pos.offset >= length)
                        break;
                    AnsiConsoleAttributes attributes = ((AnsiPosition) pos).attributes;
                    if (attributes != null) {
                        lastVisibleAttribute = attributes;
                    }
                }
            }
            defaultPositionUpdater.update(event);
            // This will only do something on new text (appended)
            calculateDocumentAnsiPositions(eventDocument, offset, length, text);
            // Would probably be possible to re-calculate things. But let's measure first (premature optimization and all that :-)
            offsetToStyleRangeCache.clear();
        } catch (BadPositionCategoryException e) {
            e.printStackTrace();
        }
    }
}
