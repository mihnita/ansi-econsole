package mnita.ansiconsole.participants;

import java.util.regex.Matcher;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

import mnita.ansiconsole.AnsiConsoleUtils;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;
import mnita.ansiconsole.utils.AnsiConsoleAttributes;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

public class AnsiConsolePositionUpdater extends DefaultPositionUpdater {

	public static final String POSITION_NAME = "ansi_color";

	// store the last processed attributes
	AnsiConsoleAttributes attributes = AnsiConsoleAttributes.DEFAULT;
	
	public AnsiConsolePositionUpdater(IDocument document) {
		super(POSITION_NAME);

		AnsiConsoleColorPalette.setPalette(AnsiConsolePreferenceUtils.getPreferredPalette());
		update(document, 0, document.get());
	}

	public void update(IDocument document, final int offset, final String text) {

		if (text == null || text.isEmpty())
			return;
		
		final Matcher matcher = AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_TXT.matcher(text);


		try {
	
			int escapeOffet = 0;

			while (matcher.find()) {
				int start = matcher.start();
				String group = matcher.group();

				if (attributes != AnsiConsoleAttributes.DEFAULT && start > escapeOffet)
					document.addPosition(POSITION_NAME,
							new AnsiPosition(escapeOffet + offset, start - escapeOffet, attributes));


				// escape the code
				document.addPosition(POSITION_NAME, new AnsiPosition(start + offset, group.length()));

				// update the attributes
				attributes = attributes.apply(group);

				// update the offset
				escapeOffet = start + group.length();

			}
			// if the attributes is of interest and length is not null add a position
			if (attributes != AnsiConsoleAttributes.DEFAULT && text.length() > escapeOffet) {
				AnsiPosition pos = new AnsiPosition(escapeOffet + offset, text.length() - escapeOffet, attributes);
				document.addPosition(POSITION_NAME, pos);
			}

		} catch (BadPositionCategoryException | BadLocationException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update(DocumentEvent event) {
		super.update(event);
		
		if (!AnsiConsolePreferenceUtils.isAnsiConsoleEnabled())
			return;

		
		
		update(event.getDocument(), event.getOffset(), event.getText());
	}

}
