package mnita.ansiconsole;

import java.util.regex.Pattern;

import org.eclipse.swt.SWT;

public class AnsiConsoleUtils {
    public static final Pattern ESCAPE_SEQUENCE_REGEX_TXT = Pattern.compile("\u001b\\[[\\d;]*[A-HJKSTfimnsu]");
    public static final Pattern ESCAPE_SEQUENCE_REGEX_RTF = Pattern.compile("\\{\\\\cf\\d+[^}]* \u001b\\[[\\d;]*[A-HJKSTfimnsu][^}]*\\}");
    // These two are used to replace \chshdng#1\chcbpat#2 with \chshdng#1\chcbpat#2\cb#2
    public static final Pattern ESCAPE_SEQUENCE_REGEX_RTF_FIX_SRC = Pattern.compile("\\\\chshdng\\d+\\\\chcbpat(\\d+)");
    public static final String ESCAPE_SEQUENCE_REGEX_RTF_FIX_TRG = "$0\\\\cb$1";

    public static final char ESCAPE_SGR = 'm';

    private AnsiConsoleUtils() {
        // Utility class, should not be instantiated
    }

    public static boolean isWindows() {
        return "win32".equals(SWT.getPlatform());
    }

    public static boolean isMacOS() {
        return "cocoa".equals(SWT.getPlatform());
    }

    public static boolean isGTK() {
        return "gtk".equals(SWT.getPlatform());
    }
}
