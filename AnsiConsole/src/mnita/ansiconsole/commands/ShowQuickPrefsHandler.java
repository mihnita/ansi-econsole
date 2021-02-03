package mnita.ansiconsole.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowQuickPrefsHandler extends AbstractHandler {
    private static final String PREF_PAGE_NAME = "mnita.ansiconsole.preferences.AnsiConsolePreferencePage";
    private static final String[] PREFS_PAGES_TO_SHOW = {
            PREF_PAGE_NAME,
            "org.eclipse.debug.ui.DebugPreferencePage",
            "org.eclipse.debug.ui.ConsolePreferencePage",
    };

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final Shell shell = HandlerUtil.getActiveShell(event);
        if (shell != null) {
            // replace PREFS_PAGES_TO_SHOW with null to show all pages
            final PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
                    shell, PREF_PAGE_NAME, PREFS_PAGES_TO_SHOW, null);
            dialog.open();
        }
        return null;
    }
}
