package mnita.ansiconsole.participants;

import org.eclipse.jface.text.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;
import mnita.ansiconsole.utils.AnsiConsoleAttributes;

public class AnsiPosition extends Position {

	private static final Font MONO_FONT = new Font(null, "Monospaced", 6, SWT.NORMAL);

	public final StyleRange range;

	/**
	 * Build an ansi position for a text area with custom attributes
	 * @param offset the position offset
	 * @param length the position length
	 * @param attributes the position ansi attributes
	 */
	public AnsiPosition(int offset, int length, AnsiConsoleAttributes attributes) {
		super(offset, length);

		this.range = new StyleRange(offset, length, null, null);
		AnsiConsoleAttributes.updateRangeStyle(range, attributes);
	}
/**
 * Build an ansi position for an escape code
 * @param offset the position offset
 * @param length the position length
 */
	public AnsiPosition(int offset, int length) {
		super(offset, length);

		this.range = new StyleRange(offset, length, null, null);
		if (AnsiConsolePreferenceUtils.showAnsiEscapes()) {
			range.font = MONO_FONT; // Show the codes in small, monospaced font
		} else {
			range.metrics = new GlyphMetrics(0, 0, 0); // Hide the codes
		}
	}

}
