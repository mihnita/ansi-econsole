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

public class AnsiConsolePreferenceConstants {
	public static final String PREF_ANSI_CONSOLE_ENABLED = "booleanEnabled";
	public static final String PREF_ENABLE_PERFORMANCE_WARNING = "booleanPerformanceWarningEnabled";
	public static final String PREF_ENABLE_M2ECHROMATICCORE_WARNING = "booleanM2eChromaticCoreWarningEnabled";
	public static final String PREF_WINDOWS_MAPPING = "booleanWindowsMapping";
	public static final String PREF_SHOW_ESCAPES = "booleanShowEscapes";
	public static final String PREF_COLOR_PALETTE = "choiceColorPalette";
	public static final String PREF_KEEP_STDERR_COLOR = "booleanKeepStderrColor";
	public static final String PREF_PUT_RTF_IN_CLIPBOARD = "booleanPutRtfInClipboard";

	private AnsiConsolePreferenceConstants() {
		// Utility class, should not be instantiated
	}
}
