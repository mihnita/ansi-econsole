package mnita.ansiconsole.utils;

import org.eclipse.swt.graphics.RGB;

public class AnsiConsoleColorPalette {
    public static final String PALETTE_VGA   = "paletteVGA";
    public static final String PALETTE_WINXP = "paletteXP";
    public static final String PALETTE_MAC   = "paletteMac";
    public static final String PALETTE_PUTTY = "palettePuTTY";
    public static final String PALETTE_XTERM = "paletteXTerm";

 // From Wikipedia, http://en.wikipedia.org/wiki/ANSI_escape_code
    private final static RGB []  paletteVGA = {
        new RGB(  0,   0,   0), // black
        new RGB(170,   0,   0), // red
        new RGB(  0, 170,   0), // green
        new RGB(170,  85,   0), // brown/yellow
        new RGB(  0,   0, 170), // blue
        new RGB(170,   0, 170), // magenta
        new RGB(  0, 170, 170), // cyan
        new RGB(170, 170, 170), // gray
        new RGB( 85,  85,  85), // dark gray
        new RGB(255,  85,  85), // bright red
        new RGB( 85, 255,  85), // bright green
        new RGB(255, 255,  85), // yellow
        new RGB( 85,  85, 255), // bright blue
        new RGB(255,  85, 255), // bright magenta
        new RGB( 85, 255, 255), // bright cyan
        new RGB(255, 255, 255)  // white
    };
    private final static RGB []  paletteXP = {
        new RGB(  0,   0,   0), // black
        new RGB(128,   0,   0), // red
        new RGB(  0, 128,   0), // green
        new RGB(128, 128,   0), // brown/yellow
        new RGB(  0,   0, 128), // blue
        new RGB(128,   0, 128), // magenta
        new RGB(  0, 128, 128), // cyan
        new RGB(192, 192, 192), // gray
        new RGB(128, 128, 128), // dark gray
        new RGB(255,   0,   0), // bright red
        new RGB(  0, 255,   0), // bright green
        new RGB(255, 255,   0), // yellow
        new RGB(  0,   0, 255), // bright blue
        new RGB(255,   0, 255), // bright magenta
        new RGB(  0, 255, 255), // bright cyan
        new RGB(255, 255, 255)  // white
    };
    private final static RGB []  paletteMac = {
        new RGB(  0,   0,   0), // black
        new RGB(194,  54,  33), // red
        new RGB( 37, 188,  36), // green
        new RGB(173, 173,  39), // brown/yellow
        new RGB( 73,  46, 225), // blue
        new RGB(211,  56, 211), // magenta
        new RGB( 51, 187, 200), // cyan
        new RGB(203, 204, 205), // gray
        new RGB(129, 131, 131), // dark gray
        new RGB(252,  57,  31), // bright red
        new RGB( 49, 231,  34), // bright green
        new RGB(234, 236,  35), // yellow
        new RGB( 88,  51, 255), // bright blue
        new RGB(249,  53, 248), // bright magenta
        new RGB( 20, 240, 240), // bright cyan
        new RGB(233, 235, 235)  // white
    };
    private final static RGB []  palettePuTTY = {
        new RGB(  0,   0,   0), // black
        new RGB(187,   0,   0), // red
        new RGB(  0, 187,   0), // green
        new RGB(187, 187,   0), // brown/yellow
        new RGB(  0,   0, 187), // blue
        new RGB(187,   0, 187), // magenta
        new RGB(  0, 187, 187), // cyan
        new RGB(187, 187, 187), // gray
        new RGB( 85,  85,  85), // dark gray
        new RGB(255,  85,  85), // bright red
        new RGB( 85, 255,  85), // bright green
        new RGB(255, 255,  85), // yellow
        new RGB( 85,  85, 255), // bright blue
        new RGB(255,  85, 255), // bright magenta
        new RGB( 85, 255, 255), // bright cyan
        new RGB(255, 255, 255)  // white
    };
    private final static RGB []  paletteXTerm = {
        new RGB(  0,   0,   0), // black
        new RGB(205,   0,   0), // red
        new RGB(  0, 205,   0), // green
        new RGB(205, 205,   0), // brown/yellow
        new RGB(  0,   0, 238), // blue
        new RGB(205,   0, 205), // magenta
        new RGB(  0, 205, 205), // cyan
        new RGB(229, 229, 229), // gray
        new RGB(127, 127, 127), // dark gray
        new RGB(255,   0,   0), // bright red
        new RGB(  0, 255,   0), // bright green
        new RGB(255, 255,   0), // yellow
        new RGB( 92,  92, 255), // bright blue
        new RGB(255,   0, 255), // bright magenta
        new RGB(  0, 255, 255), // bright cyan
        new RGB(255, 255, 255)  // white
    };
    private static RGB [] palette = paletteXP;
    private static String currentPaletteName = PALETTE_WINXP;

    public static RGB getColor(int index) {
        if( index < 0 || index > palette.length )
            return new RGB(0x00,0x00,0x00);

        return palette[index];
    }

    public static String getPalette() {
        return currentPaletteName;
    }

    public static void setPalette(String paletteName) {
        currentPaletteName = paletteName;
        if( "paletteVGA".equalsIgnoreCase(paletteName) )
            palette = paletteVGA;
        else if ( "paletteXP".equalsIgnoreCase(paletteName) )
            palette = paletteXP;
        else if ( "paletteIOS".equalsIgnoreCase(paletteName) )
            palette = paletteMac;
        else if ( "palettePuTTY".equalsIgnoreCase(paletteName) )
            palette = palettePuTTY;
        else if ( "paletteXTerm".equalsIgnoreCase(paletteName) )
            palette = paletteXTerm;
        else {
            String os = System.getProperty("os.name");
            if (os == null || os.startsWith("Windows"))
                setPalette(PALETTE_WINXP);
            else if (os.startsWith("Mac"))
                setPalette(PALETTE_MAC);
            else
                setPalette(PALETTE_XTERM);
        }
    }

}
