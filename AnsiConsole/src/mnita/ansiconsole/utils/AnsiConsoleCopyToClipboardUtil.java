/*******************************************************************************
 * Copyright (c) 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package mnita.ansiconsole.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;

import mnita.ansiconsole.AnsiConsoleUtils;

/**
 * @author Martin Lippert
 * (parts of the code moved from AnsiConsoleCopyIconAction)
 */
public class AnsiConsoleCopyToClipboardUtil implements ClipboardOwner {

	public void copyTextToClipboard(Control control) {
		copyToClipboard(control, true);
	}

	public void copyRawToClipboard(Control control) {
		copyToClipboard(control, false);
	}

	protected void copyToClipboard(Control control, boolean removeEscapes) {
		if (control instanceof StyledText) {
			StyledText styledText = (StyledText) control;
			String text = styledText.getSelectionText();
			if (text.isEmpty()) {
				text = styledText.getText();
			}

			if (removeEscapes) {
				text = text.replaceAll(AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX, "");
			}

			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection stringSelection = new StringSelection(text);
			clipboard.setContents(stringSelection, this);
		}
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}

}
