/*
 * Copyright (c) 2012-2022 Mihai Nita and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package mnita.ansiconsole.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import mnita.ansiconsole.AnsiConsoleUtils;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class AnsiClipboardUtils {

	public static void textToClipboard(StyledText styledText, boolean removeEscapeSeq) {
		Clipboard clipboard = new Clipboard(Display.getDefault());

		clipboard.clearContents();
		styledText.copy(); // copy to clipboard using the default Eclipse behavior

		List<Object> clipboardData = new ArrayList<>(2);
		List<Transfer> clipboardTransfers = new ArrayList<>(2);

		TextTransfer textTransfer = TextTransfer.getInstance();
		Object textData = clipboard.getContents(textTransfer);
		if (textData != null && textData instanceof String) {
			String plainText = (String) textData;
			if (removeEscapeSeq) {
				plainText = AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_TXT
						.matcher(plainText)
						.replaceAll("");
			}
			clipboardData.add(plainText);
			clipboardTransfers.add(textTransfer);
		}

		if (AnsiConsolePreferenceUtils.putRtfInClipboard()) {
			RTFTransfer rtfTransfer = RTFTransfer.getInstance();
			Object rtfData = clipboard.getContents(rtfTransfer);
			if (rtfData != null && rtfData instanceof String) {
				String rtfText = (String) rtfData;
				if (removeEscapeSeq) {
					rtfText = AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_RTF
							.matcher(rtfText)
							.replaceAll("");
				}
				// The Win version of MS Word, and Write, understand \chshdng and \chcbpat, but not \cb
				// The MacOS tools seem to understand \cb, but not \chshdng and \chcbpat
				// But using both seems to work fine, both systems just ignore the tags they don't understand.
				rtfText = AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_RTF_FIX_SRC
						.matcher(rtfText)
						.replaceAll(AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX_RTF_FIX_TRG);
				clipboardData.add(rtfText);
				clipboardTransfers.add(rtfTransfer);
			}
		}

		clipboard.setContents(clipboardData.toArray(), clipboardTransfers.toArray(new Transfer[0]));

		clipboard.dispose();
	}
}
