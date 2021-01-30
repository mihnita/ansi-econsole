package mnita.ansiconsole.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBookView;

public class AnsiConsoleShowPrefsIconAction implements IViewActionDelegate {
    private static final String PREF_PAGE_NAME = "mnita.ansiconsole.preferences.AnsiConsolePreferencePage";
    private static final String[] PREFS_PAGES_TO_SHOW = {
            PREF_PAGE_NAME,
            "org.eclipse.debug.ui.DebugPreferencePage",
            "org.eclipse.debug.ui.ConsolePreferencePage",
    };
    private PageBookView consoleView = null;

    @Override
    public void run(IAction action) {
        final IPage currentPage = (consoleView == null) ? null : consoleView.getCurrentPage();
        if (currentPage != null) {
            final Shell shell = currentPage.getControl().getShell();
            // replace PREFS_PAGES_TO_SHOW with null to show all pages
            final PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
                    shell, PREF_PAGE_NAME, PREFS_PAGES_TO_SHOW, null);
            dialog.open();
        }
    }

    @Override
    public void init(IViewPart view) {
        if (view instanceof PageBookView) {
            consoleView = (PageBookView) view;
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // Nothing to do, but we are forced to implement it for IViewActionDelegate
    }
}
