package mnita.ansiconsole.participants;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.services.IServiceLocator;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.handlers.AnsiConsoleCopyHandler;

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
            IDocument document = getDocument(viewer);
            if (document == null)
                return;
            AnsiConsoleStyleListener myListener = new AnsiConsoleStyleListener(document);
            viewer.addLineStyleListener(myListener);
            AnsiConsoleActivator.getDefault().addViewer(viewer, this);

            // Install copy handler, replacing the old one
            IServiceLocator sloc = page.getSite().getActionBars().getServiceLocator();
            ICommandService commandService = sloc.getService(ICommandService.class);
            Command command = commandService.getCommand(ActionFactory.COPY.getId());
            if (command != null) {
                command.setHandler(new AnsiConsoleCopyHandler(viewer));
            }
        }
    }

    // Find the document associated with the viewer
    private static IDocument getDocument(StyledText viewer) {
        for (Listener listener : viewer.getListeners(ST.LineGetStyle)) {
            if (listener instanceof TypedListener) {
                Object evenListener = ((TypedListener) listener).getEventListener();
                if (evenListener instanceof ITextViewer) {
                    return ((ITextViewer) evenListener).getDocument();
                }
            }
        }
        return null;
    }
}
