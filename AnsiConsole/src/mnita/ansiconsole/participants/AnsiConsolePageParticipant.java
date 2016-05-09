package mnita.ansiconsole.participants;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.actions.AnsiConsoleCopyToClipboardAction;

public class AnsiConsolePageParticipant implements IConsolePageParticipant {

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class arg) {
		return null;
	}

	@Override
	public void activated() {
	}

	@Override
	public void deactivated() {
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
			
			AnsiConsoleCopyToClipboardAction copyAction = new AnsiConsoleCopyToClipboardAction(viewer);
			page.getSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
			page.getSite().getActionBars().updateActionBars();
		}
	}
}
