package mnita.ansiconsole;

public class AnsiConsoleUtils {
  public final static String ESCAPE_SEQUENCE_REGEX = "\u001b\\[[\\d;]*[A-HJKSTfimnsu]";
  public final static char ESCAPE_SGR = 'm';
}
