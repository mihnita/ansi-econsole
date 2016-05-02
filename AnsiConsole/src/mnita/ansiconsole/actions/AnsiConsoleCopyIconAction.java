package mnita.ansiconsole.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;

import mnita.ansiconsole.utils.AnsiConsoleCopyToClipboardUtil;

public class AnsiConsoleCopyIconAction implements IViewActionDelegate {
	
    private PageBookView consoleView = null;
    private AnsiConsoleCopyToClipboardUtil clipboardUtil = new AnsiConsoleCopyToClipboardUtil();

   
    @Override
    public void run(IAction action) {
        IPage currentPage = (consoleView == null) ? null : consoleView.getCurrentPage();
        if (currentPage != null) {
            Control control = currentPage.getControl();
            clipboardUtil.copyRawToClipboard(control);
        }
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
        // Nothing to do, but we are forced to implement it for IViewActionDelegate
    }

}
