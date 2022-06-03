package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class AnsiConsoleStyleListener implements LineStyleListener {

	public static AnsiConsoleStyleListener INSTANCE = new AnsiConsoleStyleListener();

	private AnsiConsoleStyleListener() {
	}

	private IDocument getDocument(LineStyleEvent event) {
		if (event.getSource() instanceof StyledText) {

			StyledText text = (StyledText) event.getSource();
			return AnsiConsolePageParticipant.getDocument(text);
		}
		return null;
	}

	@Override
	public void lineGetStyle(LineStyleEvent event) {

		if (!AnsiConsolePreferenceUtils.isAnsiConsoleEnabled())
			return;

		IDocument document = getDocument(event);

		if (document == null || document.getLength() == 0)
			return;

		// retrieve ansi positions
		Position[] positions;
		try {
			positions = document.getPositions(AnsiConsolePositionUpdater.POSITION_NAME);
		} catch (BadPositionCategoryException e1) {
			// this exception is raised when the document is processed for the first time

			// init the PositionCategory and AnsiConsolePositionUpdater for this document
			document.addPositionCategory(AnsiConsolePositionUpdater.POSITION_NAME);
			document.addPositionUpdater(new AnsiConsolePositionUpdater(document));

			// positions should be initialized now
			try {
				positions = document.getPositions(AnsiConsolePositionUpdater.POSITION_NAME);
			} catch (BadPositionCategoryException e) {
				return;
			}
		}

		List<StyleRange> styles = new ArrayList<>(4);

		// keep existing styles if any
		if (event.styles != null)
			Collections.addAll(styles, event.styles);

		// process positions that overlap with the current line
		if (processPositions(styles, event.lineOffset, event.lineText.length(), positions))
			// update event styles
			event.styles = styles.toArray(new StyleRange[styles.size()]);

	}

	/**
	 * Process the positions overlapping the given range
	 *
	 * @param styles    the result list of StyleRange
	 * @param offset    the offset of the range
	 * @param length    the length of the range
	 * @param positions the positions to search
	 */
	private boolean processPositions(List<StyleRange> styles, int offset, int length, Position[] positions) {

		if (positions.length == 0)
			return false;

		int rangeEnd = offset + length;
		int left = 0;
		int right = positions.length - 1;
		int mid = 0;
		Position position;

		while (left < right) {

			mid = (left + right) / 2;

			position = positions[mid];
			if (rangeEnd < position.getOffset()) {
				if (left == mid) {
					right = left;
				} else {
					right = mid - 1;
				}
			} else if (offset > (position.getOffset() + position.getLength() - 1)) {
				if (right == mid) {
					left = right;
				} else {
					left = mid + 1;
				}
			} else {
				left = right = mid;
			}
		}

		int index = left - 1;
		if (index >= 0) {
			position = positions[index];
			while (index >= 0 && (position.getOffset() + position.getLength()) > offset) {
				index--;
				if (index > 0) {
					position = positions[index];
				}
			}
		}
		index++;
		position = positions[index];
		boolean found = false;
		while (index < positions.length && (position.getOffset() < rangeEnd)) {
			styles.add(((AnsiPosition) position).range);
			found = true;
			index++;
			if (index < positions.length) {
				position = positions[index];
			}
		}
		return found;

	}

}
