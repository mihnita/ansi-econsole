package mnita.ansiconsole.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;

import mnita.ansiconsole.utils.AnsiClipboardUtils;

public class AnsiConsoleCopyHandler extends Action {
    private final StyledText viewer;

    public AnsiConsoleCopyHandler(StyledText viewer) {
        this.viewer = viewer;
    }

    @Override
    public void runWithEvent(Event event) {
        AnsiClipboardUtils.textToClipboard(viewer, true);
    }
}
