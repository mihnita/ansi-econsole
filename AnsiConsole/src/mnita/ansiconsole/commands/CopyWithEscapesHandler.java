package mnita.ansiconsole.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;

import mnita.ansiconsole.utils.AnsiClipboardUtils;

public class CopyWithEscapesHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IWorkbenchPart part = HandlerUtil.getActivePart(event);
        if (part != null) {
            if (part instanceof PageBookView) {
                final IPage currentPage = ((PageBookView) part).getCurrentPage();
                if (currentPage != null) {
                    final Control control = currentPage.getControl();
                    if (control instanceof StyledText) {
                        AnsiClipboardUtils.textToClipboard((StyledText) control, false);
                    }
                }
            }
        }
        return null;
    }
}
