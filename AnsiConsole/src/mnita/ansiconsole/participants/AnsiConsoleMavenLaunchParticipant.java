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
package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.eclipse.m2e.internal.launch.IMavenLaunchParticipant;

import mnita.ansiconsole.AnsiConsoleUtils;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

// Force in code what was done by hand here:
//   https://mihai-nita.net/2020/08/23/force-maven-colors-in-eclipse-console/
@SuppressWarnings("restriction")
public class AnsiConsoleMavenLaunchParticipant implements IMavenLaunchParticipant {

	private static boolean showM2eChromaticCoreWarning = true;

	@Override
	public String getProgramArguments(
			ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
		checkM2eChromaticCoreInstalled();
		return AnsiConsolePreferenceUtils.isAnsiConsoleEnabled() ? "-Dstyle.color=always" : "";
	}

	@Override
	public List<ISourceLookupParticipant> getSourceLookupParticipants(
			ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
		return new ArrayList<>();
	}

	@Override
	public String getVMArguments(
			ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
		return AnsiConsolePreferenceUtils.isAnsiConsoleEnabled() ? "-Djansi.passthrough=true" : "";
	}

	// Check some of the console settings that I know are bad for performance
	private static void checkM2eChromaticCoreInstalled() {
		if (!showM2eChromaticCoreWarning) {
			return;
		}
		if (!AnsiConsolePreferenceUtils.isM2eChromaticCoreWarningEnabled()) {
			return;
		}

		IExtension[] m2eExt = Platform.getExtensionRegistry().getExtensions("m2e.chromatic.core");
		if (m2eExt.length > 0) {
			showM2eChromaticCoreWarning = false;

			String indent = "\u00a0\u00a0\u00a0\u00a0";
			String where = AnsiConsoleUtils.isMacOS()
				? "Main menu \u2192 \u201cEclipse\u201d \u2192 \u201cAbout Eclipse\u201d \u2192 \u201cInstallation Details\u201d\n"
				: "Main menu \u2192 \u201cHelp\u201d \u2192 \u201cAbout Eclipse\u201d \u2192 \u201cInstallation Details\u201d\n";
			String message = ""
					+ "We detected that you have the \u201cM2E Chromatic Core Plugin\u201d installed.\n"
					+ "\n"
					+ "Now Ansi Console implements the same functionality (force Maven to output color).\n"
					+ "So M2E Chromatic Core is redundant.\n"
					+ "\n"
					+ "You should uninstall it, otherwise there might be some interferences:\n"
					+ indent + where
					+ indent + "Select \u201cM2E Chromatic Feature\u201d \u2192 click \u201cUninstall...\u201d";
			AnsiConsoleUtils.showDialogAsync(message, AnsiConsolePreferenceUtils::setEnableM2eChromaticCoreWarning, false);
		}
	}
}
