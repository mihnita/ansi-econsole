/*
 * Copyright (c) 2012-2022 Mihai Nita and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package mnita.ansiconsole.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.utils.AnsiConsoleColorPalette;

public class AnsiConsolePreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = AnsiConsoleActivator.getDefault().getPreferenceStore();
        store.setDefault(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED, true);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_ENABLE_PERFORMANCE_WARNING, true);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_ENABLE_M2ECHROMATICCORE_WARNING, true);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_SHOW_ESCAPES, false);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_WINDOWS_MAPPING, false);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_KEEP_STDERR_COLOR, true);
        store.setDefault(AnsiConsolePreferenceConstants.PREF_COLOR_PALETTE, AnsiConsoleColorPalette.getPalette());
        store.setDefault(AnsiConsolePreferenceConstants.PREF_PUT_RTF_IN_CLIPBOARD, true);
    }
}
