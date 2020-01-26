package mnita.ansiconsole;

public class AnsiConsoleUtils {
  public static final String ESCAPE_SEQUENCE_REGEX = "\u001b\\[[\\d;]*[A-HJKSTfimnsu]";
  public static final char ESCAPE_SGR = 'm';

  private AnsiConsoleUtils() { /* Utility class, should not be instantiated */ }
}
