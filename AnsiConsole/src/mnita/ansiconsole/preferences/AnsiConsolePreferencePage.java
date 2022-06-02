package mnita.ansiconsole.preferences;

import java.net.URL;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.commands.EnableDisableHandler;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

public class AnsiConsolePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public AnsiConsolePreferencePage() {
        super(GRID);
        setPreferenceStore(AnsiConsoleActivator.getDefault().getPreferenceStore());
        setDescription("Preferences for Ansi Console");
    }

    @Override
    public void createFieldEditors() {
        final Composite parent = getFieldEditorParent();

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED,
                "Plugin enabled", parent));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING,
                "Use &Windows color mapping (bold => intense, italic => reverse)", parent));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES,
                "&Show the escape sequences", parent));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR,
                "&Try using the standard error color setting for stderr output", parent));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_PUT_RTF_IN_CLIPBOARD,
                "Put &RTF in Clipboard. You will be able to paste styled text in some applications.", parent));

        addField(new ComboFieldEditor(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE, "&Color palette:",
                new String[][] {
                        { "Standard VGA colors", AnsiConsoleColorPalette.PALETTE_VGA },
                        { "Windows XP command prompt", AnsiConsoleColorPalette.PALETTE_WINXP },
                        { "Windows 10 command prompt", AnsiConsoleColorPalette.PALETTE_WIN10 },
                        { "Mac OS X Terminal.app", AnsiConsoleColorPalette.PALETTE_MAC },
                        { "PuTTY", AnsiConsoleColorPalette.PALETTE_PUTTY },
                        { "xterm", AnsiConsoleColorPalette.PALETTE_XTERM },
                        { "mIRC", AnsiConsoleColorPalette.PALETTE_MIRC },
                        { "Ubuntu", AnsiConsoleColorPalette.PALETTE_UBUNTU }
                },
                parent));

        createSeparator(parent, SWT.TOP, false);

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_ENABLE_PERFORMANCE_WARNING,
                "Enable performance check", parent));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_ENABLE_M2ECHROMATICCORE_WARNING,
                "Enable check for M2e Chromatic Core", parent));

        createSeparator(parent, SWT.BOTTOM, true);

        createLink(parent, true, "<a href=\"https://github.com/mihnita/ansi-econsole/wiki/\">Home page</a>:"
                + " some documentation, release notes, etc.");
        createLink(parent, false, "<a href=\"https://github.com/mihnita/ansi-econsole/\">GitHub page</a>:"
                + " source code, report issues, etc.");
        createLink(parent, false, "<a href=\"https://marketplace.eclipse.org/content/ansi-escape-console\">Eclipse Marketplace</a>:"
                + " give it a star / review if you like it :-).");
    }

    @Override
    public void init(IWorkbench workbench) {
        // Nothing to do, but we are forced to implement it for IWorkbenchPreferencePage
    }

    private void createSeparator(Composite parent, int verticalAlignment, boolean grabExcessVerticalSpace) {
        new Label(parent, SWT.NONE);
    }

    private void createLink(Composite parent, boolean fillGap, String text) {
        Link link = new Link(parent, SWT.WRAP);
        link.setText(text);

        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
    	gridData.grabExcessVerticalSpace = fillGap;
        gridData.verticalAlignment = SWT.BOTTOM;
        link.setLayoutData(gridData);

        link.addListener(SWT.Selection, event -> {
            try {
                PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(event.text));
            } catch (Exception e) {
                System.out.println("AnsiConsole: error opening url in browser."
                        + "URL: " + event.text
                        + "Error: " + e.getMessage());
            }
        });
    }

    @Override
    public boolean performOk() {
        boolean result = super.performOk();
        IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
        try {
            handlerService.executeCommand(EnableDisableHandler.COMMAND_ID, new Event());
        } catch (Exception ex) {
            System.out.println("AnsiConsole: Command '" + EnableDisableHandler.COMMAND_ID + "' not found");
        }
        return result;
    }
}
