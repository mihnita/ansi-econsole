package mnita.ansiconsole.actions;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Control;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;
import mnita.ansiconsole.utils.AnsiConsoleCopyToClipboardUtil;

public class AnsiConsoleCopyToClipboardAction extends Action implements ClipboardOwner {
	
	private Control control;
	private AnsiConsoleCopyToClipboardUtil clipboardUtil;

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
