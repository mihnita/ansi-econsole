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
package mnita.ansiconsole;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class AnsiConsoleUtils {
	public static final Pattern ESCAPE_SEQUENCE_REGEX_TXT = Pattern.compile("\u001b\\[[\\d;]*[A-HJKSTfimnsu]");
	public static final Pattern ESCAPE_SEQUENCE_REGEX_RTF = Pattern.compile("\\{\\\\cf\\d+[^}]* \u001b\\[[\\d;]*[A-HJKSTfimnsu][^}]*\\}");
	// These two are used to replace \chshdng#1\chcbpat#2 with \chshdng#1\chcbpat#2\cb#2
	public static final Pattern ESCAPE_SEQUENCE_REGEX_RTF_FIX_SRC = Pattern.compile("\\\\chshdng\\d+\\\\chcbpat(\\d+)");
	public static final String ESCAPE_SEQUENCE_REGEX_RTF_FIX_TRG = "$0\\\\cb$1";

	private static final String DLG_TITLE = "Ansi Console";
	static final String[] DLG_BUTTONS = { "Remind me later", "Never remind me again" };

	public static final char ESCAPE_SGR = 'm';

	private AnsiConsoleUtils() {
		// Utility class, should not be instantiated
	}

	public static boolean isWindows() {
		return "win32".equals(SWT.getPlatform());
	}

	public static boolean isMacOS() {
		return "cocoa".equals(SWT.getPlatform());
	}

	public static boolean isGTK() {
		return "gtk".equals(SWT.getPlatform());
	}

	public static void showDialogAsync(String message, java.util.function.Consumer<Boolean> func) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				final MessageDialog dlg = new MessageDialog(window.getShell(),
						DLG_TITLE, /*image*/ null, message,
						MessageDialog.WARNING, 0, DLG_BUTTONS);
				if (dlg.open() == 1) {
					func.accept(false);
				}
			}
		});
	}
}
