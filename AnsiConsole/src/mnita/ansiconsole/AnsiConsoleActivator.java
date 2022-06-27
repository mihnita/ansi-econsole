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
package mnita.ansiconsole;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.custom.StyledText;
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
    private static void checkConsolePerformanceSettings() {
        if (!showWarning) return;
        if (!AnsiConsolePreferenceUtils.isPerformanceWarningEnabled()) return;

        String indent = "\u00a0\u00a0\u00a0\u00a0";
        String where = AnsiConsoleUtils.isMacOS()
                ? "Main menu \u2192 \u201cEclipse\u201d \u2192 \u201cPreferences...\u201d \u2192 \u201cRun Debug\u201d \u2192 \u201cConsole\u201d\n"
                : "Main menu \u2192 \u201cWindow\u201d \u2192 \u201cPreferences\u201d \u2192 \u201cRun/Debug\u201d \u2192 \u201cConsole\u201d\n";
        StringBuffer message = new StringBuffer();
        int wattermarkLevel = AnsiConsolePreferenceUtils.getWattermarkLevel();
        if (wattermarkLevel < 150_000) {
            NumberFormat nf = NumberFormat.getInstance();
            message.append(String.format("\n"
                    + "Console buffer size too low (%s). About 2 times slower.\n\n"
                    + where
                    + indent + "\u2022 Check \u201cLimit console output\u201d\n"
                    + indent + "\u2022 Set \u201cConsole buffer size (characters)\u201d to a bigger value.\n"
                    + indent + "\u00a0\u00a0\u00a0The sweet spot seems to be around %s\n"
                    + "or\n"
                    + indent + "\u2022 Uncheck \u201cLimit console output\u201d\n"
                    + indent + "\u00a0\u00a0\u00a0No noticeable performance difference compared to %s\n",
                    nf.format(wattermarkLevel), nf.format(1_000_000), nf.format(1_000_000)));
        }
        if (AnsiConsolePreferenceUtils.isWordWrapEnabled()) {
            message.append("\n"
                    + "Word wrap enabled. Up to 20 times slower!!!\n\n"
                    + indent + where
                    + indent + "\u2022 Uncheck \u201cEnable word wrap\u201d\n");
        }

        if (message.length() > 0) {
            showWarning = false;
            AnsiConsoleUtils.showDialogAsync("CONSOLE PERFORMANCE WARNING (from Ansi Console)!\n" + message,
                    AnsiConsolePreferenceUtils::setEnablePerformanceWarning);
        }
    }
}
