package mnita.ansiconsole.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.eclipse.swt.custom.StyledText;

import mnita.ansiconsole.AnsiConsoleUtils;

public class AnsiClipboardUtils implements ClipboardOwner {
  
    public static void textToClipboard(StyledText styledText, boolean removeEscapeSeq) {
        String text = styledText.getSelectionText();
        if (text.isEmpty()) {
            text = styledText.getText();
        }
        if (removeEscapeSeq) {
            text = text.replaceAll(AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX, "");
        }
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, new AnsiClipboardUtils());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
        // Nothing to do, but we are forced to implement it for IViewActionDelegate
    }
}
