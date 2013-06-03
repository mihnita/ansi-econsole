package mnita.ansiconsole.preferences;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class AnsiConsolePreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = AnsiConsoleActivator.getDefault().getPreferenceStore();
        store.setDefault(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED, true);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES, false);

        String os = System.getProperty("os.name");
        if (os == null || os.startsWith("Windows"))
            store.setDefault(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING, true);
        else
            store.setDefault(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING, false);

        store.setDefault(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE, AnsiConsoleColorPalette.getPalette());
    }
}
