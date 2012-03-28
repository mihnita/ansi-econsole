package mnita.ansiconsole.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
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
                "Use &Windows color mapping", getFieldEditorParent()));

        addField(new BooleanFieldEditor(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES,
                "&Show the escape sequences", getFieldEditorParent()));

        addField(new ColorFieldEditor(AnsiConsolePreferenceConstants.PREF_BGCOLOR,
                "&Background color:", getFieldEditorParent()));

         addField(new RadioGroupFieldEditor( AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE,
             "&Color palette", 1, new String[][] {
                 { "Standard VGA colors", AnsiConsoleColorPalette.PALETTE_VGA },
                 { "Windows XP command prompt", AnsiConsoleColorPalette.PALETTE_WINXP },
                 { "Mac OS X Terminal.app", AnsiConsoleColorPalette.PALETTE_MAC },
                 { "PuTTY", AnsiConsoleColorPalette.PALETTE_PUTTY },
                 { "xterm", AnsiConsoleColorPalette.PALETTE_XTERM }
             },
             getFieldEditorParent())
         );
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
