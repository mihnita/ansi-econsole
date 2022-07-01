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
            "org.eclipse.debug.ui.ConsolePreferencePage"
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
