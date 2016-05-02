package mnita.ansiconsole.actions;
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


import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Control;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;
import mnita.ansiconsole.utils.AnsiConsoleCopyToClipboardUtil;

/**
 * @author Martin Lippert
 */
public class AnsiConsoleCopyToClipboardAction extends Action implements ClipboardOwner {
	
	private Control control;
	private AnsiConsoleCopyToClipboardUtil clipboardUtil;

	/**
	 * @param control
	 */
	public AnsiConsoleCopyToClipboardAction(Control control) {
		this.control = control;
		this.clipboardUtil = new AnsiConsoleCopyToClipboardUtil();
	}

	@Override
	public void run() {
		System.out.println("run it !!!");
        boolean isAnsiConsoleEnabled = AnsiConsolePreferenceUtils.getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED);

		if (isAnsiConsoleEnabled) {
			this.clipboardUtil.copyTextToClipboard(this.control);
		}
		else {
			this.clipboardUtil.copyRawToClipboard(this.control);
		}
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
	
}
