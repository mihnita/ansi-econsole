package mnita.ansiconsole;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class AnsiConsoleActivator extends AbstractUIPlugin {

    private static AnsiConsoleActivator plugin;
    private static boolean showWarning = true;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static AnsiConsoleActivator getDefault() {
        return plugin;
    }

    private final Map<StyledText, IConsolePageParticipant> viewers = new HashMap<>();

    public void addViewer(StyledText viewer, IConsolePageParticipant participant) {
        viewers.put(viewer, participant);
        checkConsolePerformanceSettings();
    }

    public void removeViewerWithPageParticipant(IConsolePageParticipant participant) {
        Set<StyledText> toRemove = new HashSet<>();

        for (Entry<StyledText, IConsolePageParticipant> entry : viewers.entrySet()) {
            if (entry.getValue() == participant)
                toRemove.add(entry.getKey());
        }

        for (StyledText viewer : toRemove)
            viewers.remove(viewer);
    }

    // Check some of the console settings that I know are bad for performance
    private void checkConsolePerformanceSettings() {
        if (!showWarning) return;
        if (!AnsiConsolePreferenceUtils.isPerformanceWarningEnabled()) return;

        StringBuffer where = new StringBuffer(AnsiConsoleUtils.isMacOS()
                ? "Main menu \u2192 Eclipse \u2192 Preferences... \u2192 Run Debug \u2192 Console\n"
                : "Main menu \u2192 Window \u2192 Preferences \u2192 Run/Debug \u2192 Console\n");
        StringBuffer text = new StringBuffer();
        int wattermarkLevel = AnsiConsolePreferenceUtils.getWattermarkLevel();
        if (wattermarkLevel < 150_000) {
            NumberFormat nf = NumberFormat.getInstance();
            text.append(String.format("\n"
                    + "Console buffer size too low (%s). About 2 times slower.\n\n"
                    + where
                    + "\u00a0\u00a0\u00a0\u00a0\u2022 Check \"Limit console output\"\n"
                    + "\u00a0\u00a0\u00a0\u00a0\u2022 Set \"Console buffer size (characters)\" to a bigger value.\n"
                    + "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0The sweet spot seems to be about %s\n"
                    + "or\n"
                    + "\u00a0\u00a0\u00a0\u00a0\u2022 Uncheck \"Limit console output\"\n"
                    + "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0No visible performance difference compared to %s\n",
                    nf.format(wattermarkLevel), nf.format(1_000_000), nf.format(1_000_000)));
        }
        if (AnsiConsolePreferenceUtils.isWordWrapEnabled()) {
            text.append("\n"
                    + "Word wrap enabled. Up to 20 times slower!!!\n\n"
                    + where
                    + "\u00a0\u00a0\u00a0\u00a0\u2022 Uncheck \"Enable word wrap\"\n");
        }

        if (text.length() > 0) {
            showWarning = false;
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    showDialog("Ansi Console", "CONSOLE PERFORMANCE WARNING (from Ansi Console)!\n" + text);
                }
            });
        }
    }

    private void showDialog(String title, String message) {
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final String[] buttons = { "Remind me later", "Never remind me again" };
        final MessageDialog dlg = new MessageDialog(window.getShell(), title, null, message, MessageDialog.WARNING, 0, buttons);
        if (dlg.open() == 1) {
            AnsiConsolePreferenceUtils.setEnablePerformanceWarning(false);
        }
    }
}
