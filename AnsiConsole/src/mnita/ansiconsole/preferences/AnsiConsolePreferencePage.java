package mnita.ansiconsole.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

public class AnsiConsolePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public AnsiConsolePreferencePage() {
        super(GRID);
        setPreferenceStore(AnsiConsoleActivator.getDefault().getPreferenceStore());
        setDescription("Preferences for Ansi Console");
    }

    @Override
    public void createFieldEditors() {

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED,
                "Enabled", getFieldEditorParent()));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING,
                "Use &Windows color mapping (bold => intense, italic => reverse)", getFieldEditorParent()));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES,
                "&Show the escape sequences", getFieldEditorParent()));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR,
                "&Try using the standard error color setting for stderr output", getFieldEditorParent()));

        addField(new RadioGroupFieldEditor(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE, "&Color palette", 1,
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
                getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
        // Nothing to do, but we are forced to implement it for IWorkbenchPreferencePage
    }
}
