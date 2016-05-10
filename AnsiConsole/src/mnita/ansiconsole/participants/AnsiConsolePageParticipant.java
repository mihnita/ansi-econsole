package mnita.ansiconsole.participants;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.actions.AnsiConsoleCopyHandler;

public class AnsiConsolePageParticipant implements IConsolePageParticipant {
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }

    @Override
    public void activated() {
        // Nothing to do, but we are forced to implement it for IConsolePageParticipant
    }

    @Override
    public void deactivated() {
        // Nothing to do, but we are forced to implement it for IConsolePageParticipant
    }

    @Override
    public void dispose() {
        AnsiConsoleActivator.getDefault().removeViewerWithPageParticipant(this);
    }

    @Override
    public void init(IPageBookViewPage page, IConsole console) {
        if (page.getControl() instanceof StyledText) {
            StyledText viewer = (StyledText) page.getControl();
            AnsiConsoleStyleListener myListener = new AnsiConsoleStyleListener();
            viewer.addLineStyleListener(myListener);
            AnsiConsoleActivator.getDefault().addViewer(viewer, this);

            AnsiConsoleCopyHandler copy = new AnsiConsoleCopyHandler(viewer);
            page.getSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
        }
    }
}
