package mnita.ansiconsole.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;

import mnita.ansiconsole.AnsiConsoleUtils;

public class AnsiConsoleCopyIconAction implements IViewActionDelegate, ClipboardOwner {
    PageBookView consoleView = null;

    @Override
    public void run(IAction action) {
        String text = "";

        IPage currentPage = (consoleView == null) ? null : consoleView.getCurrentPage();
        if (currentPage != null) {
            Control control = currentPage.getControl();
            if (control instanceof StyledText) {
                StyledText styledText = (StyledText) control;
                text = styledText.getSelectionText();
                if (text.isEmpty()) {
                    text = styledText.getText();
                }
                text = text.replaceAll(AnsiConsoleUtils.ESCAPE_SEQUENCE_REGEX, "");        
            }
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, new AnsiConsoleCopyIconAction());
    }

    @Override
    public void init(IViewPart view) {
        System.out.println("init(IViewPart):" + view.getClass().getName());
        if (view instanceof PageBookView) {
          consoleView = (PageBookView) view;
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // Nothing to do, but we are forced to implement it for ClipboardOwner
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
        // Nothing to do, but we are forced to implement it for ClipboardOwner
    }
}
