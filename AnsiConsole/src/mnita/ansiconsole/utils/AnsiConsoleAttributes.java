package mnita.ansiconsole.utils;

import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_CONCEAL_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_CONCEAL_ON;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_CROSSOUT_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_CROSSOUT_ON;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_FRAMED_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_FRAMED_ON;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_INTENSITY_BRIGHT;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_INTENSITY_FAINT;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_INTENSITY_NORMAL;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_ITALIC;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_ITALIC_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_NEGATIVE_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_NEGATIVE_ON;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_RESET;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_UNDERLINE;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_UNDERLINE_DOUBLE;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_ATTR_UNDERLINE_OFF;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_BACKGROUND_FIRST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_BACKGROUND_LAST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_BACKGROUND_RESET;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_FOREGROUND_FIRST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_FOREGROUND_LAST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_FOREGROUND_RESET;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_COLOR_INTENSITY_DELTA;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_BACKGROUND;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_BACKGROUND_FIRST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_BACKGROUND_LAST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_FOREGROUND;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_FOREGROUND_FIRST;
import static mnita.ansiconsole.utils.AnsiCommands.COMMAND_HICOLOR_FOREGROUND_LAST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import mnita.ansiconsole.AnsiConsoleUtils;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class AnsiConsoleAttributes {
	public static final int UNDERLINE_NONE = -1; // nothing in SWT, a bit of an abuse

	public static final AnsiConsoleAttributes DEFAULT = new AnsiConsoleAttributes();

	// If you change any of these also update reset()
	public Integer currentBgColor;
	public Integer currentFgColor;
	public int underline;
	public boolean bold;
	public boolean italic;
	public boolean invert;
	public boolean conceal;
	public boolean strike;
	public boolean framed;

	private AnsiConsoleAttributes() {
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

	public static AnsiConsoleAttributes from(AnsiConsoleAttributes other) {
		AnsiConsoleAttributes result = new AnsiConsoleAttributes();
		if (other != null) {
			result.currentBgColor = other.currentBgColor;
			result.currentFgColor = other.currentFgColor;
			result.underline = other.underline;
			result.bold = other.bold;
			result.italic = other.italic;
			result.invert = other.invert;
			result.conceal = other.conceal;
			result.strike = other.strike;
			result.framed = other.framed;
		}
		return result;
	}

	public boolean equals(AnsiConsoleAttributes other) {

		return other != null && currentBgColor == other.currentBgColor && currentFgColor == other.currentFgColor
				&& underline == other.underline && bold == other.bold && italic == other.italic
				&& invert == other.invert && conceal == other.conceal && strike == other.strike
				&& framed == other.framed;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (currentBgColor != null)
			result.append("Bg" + currentBgColor);
		if (currentFgColor != null)
			result.append("Fg" + currentFgColor);
		if (underline != UNDERLINE_NONE)
			result.append("_");
		if (bold)
			result.append("B");
		if (italic)
			result.append("I");
		if (invert)
			result.append("!");
		if (conceal)
			result.append("H");
		if (strike)
			result.append("-");
		if (framed)
			result.append("[]");
		return result.toString();
	}

	private static Color hiliteRgbColor(Color color) {
		if (color == null)
			return ColorCache.get(new RGB(0xff, 0xff, 0xff));
		int red = color.getRed() * 2;
		int green = color.getGreen() * 2;
		int blue = color.getBlue() * 2;

		if (red > 0xff)
			red = 0xff;
		if (green > 0xff)
			green = 0xff;
		if (blue > 0xff)
			blue = 0xff;

		return ColorCache.get(new RGB(red, green, blue)); // here
	}

	// This function maps from the current attributes as "described" by escape
	// sequences to real,
	// Eclipse console specific attributes (resolving color palette, default colors,
	// etc.)
	public static void updateRangeStyle(StyleRange range, AnsiConsoleAttributes attribute) {
		if (attribute == null)
			return;

		AnsiConsoleAttributes tempAttrib = AnsiConsoleAttributes.from(attribute);

		boolean hilite = false;

		if (AnsiConsolePreferenceUtils.useWindowsMapping()) {
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

		// Prepare the foreground color
		if (hilite) {
			if (tempAttrib.currentFgColor == null) {
				range.foreground = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
				range.foreground = hiliteRgbColor(range.foreground);
			} else {
				if (tempAttrib.currentFgColor < COMMAND_COLOR_INTENSITY_DELTA)
					range.foreground = ColorCache.get(AnsiConsoleColorPalette
							.getColor(tempAttrib.currentFgColor + COMMAND_COLOR_INTENSITY_DELTA));
				else
					range.foreground = ColorCache.get(AnsiConsoleColorPalette.getColor(tempAttrib.currentFgColor));
			}
		} else {
			if (tempAttrib.currentFgColor != null)
				range.foreground = ColorCache.get(AnsiConsoleColorPalette.getColor(tempAttrib.currentFgColor));
		}

		// Prepare the background color
		if (tempAttrib.currentBgColor != null)
			range.background = ColorCache.get(AnsiConsoleColorPalette.getColor(tempAttrib.currentBgColor));

		// These two still mess with the foreground/background colors
		// We need to solve them before we use them for strike/underline/frame colors
		if (tempAttrib.invert) {
			if (range.foreground == null)
				range.foreground = AnsiConsolePreferenceUtils.getDebugConsoleFgColor();
			if (range.background == null)
				range.background = AnsiConsolePreferenceUtils.getDebugConsoleBgColor();
			Color tmp = range.background;
			range.background = range.foreground;
			range.foreground = tmp;
		}

		if (tempAttrib.conceal) {
			if (range.background == null)
				range.background = AnsiConsolePreferenceUtils.getDebugConsoleBgColor();
			range.foreground = range.background;
		}

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
		} else {
			range.underline = false;
		}

		range.strikeout = tempAttrib.strike;
		range.strikeoutColor = range.foreground;

		if (tempAttrib.framed) {
			range.borderStyle = SWT.BORDER_SOLID;
			range.borderColor = range.foreground;
		} else {
			range.borderStyle = SWT.NONE;
		}
	}

	/**
	 * Apply an ansi escape code to the current attribute
	 * @param ansiCode the ansi code
	 * @return the resulting attributes
	 */
	public AnsiConsoleAttributes apply(String ansiCode) {
		char code = ansiCode.charAt(ansiCode.length() - 1);
		if (code == AnsiConsoleUtils.ESCAPE_SGR) {
			String theEscape = ansiCode.substring(2, ansiCode.length() - 1);

			AnsiConsoleAttributes current = AnsiConsoleAttributes.from(this);
			// Select Graphic Rendition (SGR) escape sequence
			current.interpretCommand(parseSemicolonSeparatedIntList(theEscape));
			if (current.equals(AnsiConsoleAttributes.DEFAULT))
				return AnsiConsoleAttributes.DEFAULT;

			return current;
		}
		return this;
	}

	// Takes a string that looks like this: int [ ';' int] and returns a list of the
	// integers
	private static List<Integer> parseSemicolonSeparatedIntList(String text) {
		final List<Integer> result = new ArrayList<>(10);
		int crtValue = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch >= '0' && ch <= '9') {
				crtValue *= 10;
				crtValue += ch - '0';
			} else {
				result.add(crtValue);
				crtValue = 0;
			}
		}
		result.add(crtValue);
		return result;
	}

	private void interpretCommand(List<Integer> nCommands) {

		Iterator<Integer> iter = nCommands.iterator();
		while (iter.hasNext()) {
			int nCmd = iter.next();
			switch (nCmd) {
			case COMMAND_ATTR_RESET:
				reset();
				break;

			case COMMAND_ATTR_INTENSITY_BRIGHT:
				bold = true;
				break;
			case COMMAND_ATTR_INTENSITY_FAINT: // Intentional fallthrough
			case COMMAND_ATTR_INTENSITY_NORMAL:
				bold = false;
				break;

			case COMMAND_ATTR_ITALIC:
				italic = true;
				break;
			case COMMAND_ATTR_ITALIC_OFF:
				italic = false;
				break;

			case COMMAND_ATTR_UNDERLINE:
				underline = SWT.UNDERLINE_SINGLE;
				break;
			case COMMAND_ATTR_UNDERLINE_DOUBLE:
				underline = SWT.UNDERLINE_DOUBLE;
				break;
			case COMMAND_ATTR_UNDERLINE_OFF:
				underline = AnsiConsoleAttributes.UNDERLINE_NONE;
				break;

			case COMMAND_ATTR_CROSSOUT_ON:
				strike = true;
				break;
			case COMMAND_ATTR_CROSSOUT_OFF:
				strike = false;
				break;

			case COMMAND_ATTR_NEGATIVE_ON:
				invert = true;
				break;
			case COMMAND_ATTR_NEGATIVE_OFF:
				invert = false;
				break;

			case COMMAND_ATTR_CONCEAL_ON:
				conceal = true;
				break;
			case COMMAND_ATTR_CONCEAL_OFF:
				conceal = false;
				break;

			case COMMAND_ATTR_FRAMED_ON:
				framed = true;
				break;
			case COMMAND_ATTR_FRAMED_OFF:
				framed = false;
				break;

			case COMMAND_COLOR_FOREGROUND_RESET:
				currentFgColor = null;
				break;
			case COMMAND_COLOR_BACKGROUND_RESET:
				currentBgColor = null;
				break;

			case COMMAND_HICOLOR_FOREGROUND:
			case COMMAND_HICOLOR_BACKGROUND: // {esc}[48;5;{color}m
				int color = -1;
				int nMustBe2or5 = iter.hasNext() ? iter.next() : -1;
				if (nMustBe2or5 == 5) { // 256 colors
					color = iter.hasNext() ? iter.next() : -1;
					if (!AnsiConsoleColorPalette.isValidIndex(color))
						color = -1;
				} else if (nMustBe2or5 == 2) { // rgb colors
					int r = iter.hasNext() ? iter.next() : -1;
					int g = iter.hasNext() ? iter.next() : -1;
					int b = iter.hasNext() ? iter.next() : -1;
					color = AnsiConsoleColorPalette.hackRgb(r, g, b);
				}
				if (color != -1) {
					if (nCmd == COMMAND_HICOLOR_FOREGROUND)
						currentFgColor = color;
					else
						currentBgColor = color;
				}
				break;

			case -1:
				break; // do nothing

			default:
				if (nCmd >= COMMAND_COLOR_FOREGROUND_FIRST && nCmd <= COMMAND_COLOR_FOREGROUND_LAST) // text color
					currentFgColor = nCmd - COMMAND_COLOR_FOREGROUND_FIRST;
				else if (nCmd >= COMMAND_COLOR_BACKGROUND_FIRST && nCmd <= COMMAND_COLOR_BACKGROUND_LAST) // background
																											// color
					currentBgColor = nCmd - COMMAND_COLOR_BACKGROUND_FIRST;
				else if (nCmd >= COMMAND_HICOLOR_FOREGROUND_FIRST && nCmd <= COMMAND_HICOLOR_FOREGROUND_LAST) // text
																												// color
					currentFgColor = nCmd - COMMAND_HICOLOR_FOREGROUND_FIRST + COMMAND_COLOR_INTENSITY_DELTA;
				else if (nCmd >= COMMAND_HICOLOR_BACKGROUND_FIRST && nCmd <= COMMAND_HICOLOR_BACKGROUND_LAST) // background
																												// color
					currentBgColor = nCmd - COMMAND_HICOLOR_BACKGROUND_FIRST + COMMAND_COLOR_INTENSITY_DELTA;
			}
		}
	}
}
