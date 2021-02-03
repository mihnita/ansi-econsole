package mnita.ansiconsole.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.custom.StyledText;

import mnita.ansiconsole.utils.AnsiClipboardUtils;

public class AnsiConsoleCopyHandler extends AbstractHandler {
    private final StyledText viewer;

    public AnsiConsoleCopyHandler(StyledText viewer) {
        this.viewer = viewer;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        AnsiClipboardUtils.textToClipboard(viewer, true);
        return null;
    }
}
