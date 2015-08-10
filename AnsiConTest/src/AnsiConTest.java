
public class AnsiConTest {
    static void allKindOfTests() {
        String Prefix = "===\u001b[1;3;4;9;38;5;153;48;5;222mMix";

        System.out.println("<\u001b[1m1:bold\u001b[m>reset.");
        System.out.println("<\u001b[3m3:italic\u001b[m>reset.");
        System.out.println("<\u001b[4m4:underline\u001b[m>reset.");
        System.out.println("<\u001b[7m7:negative\u001b[m>reset.");
        System.out.println("<\u001b[8m8:conceal\u001b[m>reset.");
        System.out.println("<\u001b[9m9:crossed-out\u001b[m>reset.");
        System.out.println("<\u001b[21m21:double-underline\u001b[m>reset.");

        System.out.println(Prefix + "\u001b[1mBold<\u001b[22m22:not bold\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[3mItalic<\u001b[23m23:not italic\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[4mUnderline<\u001b[24m24:not underline\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[21mDouble-underline<\u001b[24m24:not underline\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[7mNegative<\u001b[27m27:positive\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[8mConceal<\u001b[28m28:conceal off\u001b[m>reset.");
        System.out.println(Prefix + "\u001b[9mCrossed-out<\u001b[29m29:non-crossed-out\u001b[m>reset.");

        System.out.println(Prefix + "<\u001b[30m30:fg. black\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[31m31:fg. red\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[32m32:fg. green\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[33m33:fg. yellow\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[34m34:fg. blue\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[35m35:fg. magenta\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[36m36:fg. cyan\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[37m37:fg. white\u001b[m>reset.");

        System.out.println(Prefix + "<\u001b[40m40:bk. black\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[41m41:bk. red\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[42m42:bk. green\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[43m43:bk. yellow\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[44m44:bk. blue\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[45m45:bk. magenta\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[46m46:bk. cyan\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[47m47:bk. white\u001b[m>reset.");

        System.out.println("\u001b[31mRed<\u001b[39m39:fg. default\u001b[m>reset.");
        System.out.println("\u001b[41mBk red<\u001b[49m49:bk. default\u001b[m>reset.");

        System.out.println(Prefix + "<\u001b[90m90:fg. black\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[91m91:fg. red\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[92m92:fg. green\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[93m93:fg. yellow\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[94m94:fg. blue\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[95m95:fg. magenta\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[96m96:fg. cyan\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[97m97:fg. white\u001b[m>reset.");

        System.out.println(Prefix + "<\u001b[100m100:bk. black\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[101m101:bk. red\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[102m102:bk. green\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[103m103:bk. yellow\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[104m104:bk. blue\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[105m105:bk. magenta\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[106m106:bk. cyan\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[107m107:bk. white\u001b[m>reset.");

        System.out.println("\u001b[91mRed<\u001b[39m39:bk. default\u001b[m>reset.");
        System.out.println("\u001b[101mBk red<\u001b[49m49:bk. default\u001b[m>reset.");

        System.out.println(Prefix + "<\u001b[38;5;153m38:fg. 256 colors\u001b[m>reset.");
        System.out.println(Prefix + "<\u001b[48;5;153m48:bk. 256 colors\u001b[m>reset.");
    }

    static void allTestScrolling() {
        System.out.println("This is default before 1.");
        System.out.println("This is default before 2.");
        System.out.println("This is default before 3.");
        System.out.println("This is default before 4.");
        System.out.println("This is default before 5.");
        System.out.println("This is default before 6.\u001b[91mThis is red 1.");
        System.out.println("This is red 2.");
        System.out.println("This is red 3.");
        System.out.println("This is red 4.");
        System.out.println("This is red 5.");
        System.out.println("This is red 6.\u001b[0mThis is default after 1.");
        System.out.println("This is default after 2.");
        System.out.println("This is default after 3.");
        System.out.println("This is default after 4.");
        System.out.println("This is default after 5.");
        System.out.println("This is default after 6.");
    }

    static void allTest256() {
        for (int i = 1; i <= 256; ++i) {
            System.out.printf("\u001b[38;5;%dm<%03d>", i, i);
            if (i % 16 == 0)
                System.out.println();
        }
        System.out.println("\u001b[m");
        for (int i = 1; i <= 256; ++i) {
            System.out.printf("\u001b[48;5;%dm<%03d>", i, i);
            if (i % 16 == 0)
                System.out.println();
        }
        System.out.println("\u001b[m");
    }

    static void testCursorMovementConsumed() {
        // https://en.wikipedia.org/wiki/ANSI_escape_code#Non-CSI_codes
        final String CSI = "\u001b[";
        System.out.print("<<<");
        System.out.print(CSI + "5i"); // AUX Port On
        System.out.print(CSI + "4i"); // AUX Port Off
        System.out.print(CSI + "6n"); // DSR – Device Status Report
        System.out.print(CSI + "s"); // SCP – Save Cursor Position
        System.out.print(CSI + "u"); // RCP – Restore Cursor Position
        // System.out.print(CSI + "?25l"); // DECTCEM 	Hides the cursor.
        // System.out.print(CSI + "?25h"); // DECTCEM 	Shows the cursor.
        System.out.println(">>>");

        final String [] values = { "", "0", "1", "4", "42" };
        System.out.print("<<<");
        for (String value : values) {
            System.out.printf(CSI + "%sA", value); // CUU – Cursor Up
            System.out.printf(CSI + "%sB", value); // CUD – Cursor Down
            System.out.printf(CSI + "%sC", value); // CUF – Cursor Forward
            System.out.printf(CSI + "%sD", value); // CUB – Cursor Back
            System.out.printf(CSI + "%sE", value); // CNL – Cursor Next Line
            System.out.printf(CSI + "%sF", value); // CPL – Cursor Previous Line
            System.out.printf(CSI + "%sG", value); // CHA – Cursor Horizontal Absolute
            System.out.printf(CSI + "%sJ", value); // ED – Erase Display
            System.out.printf(CSI + "%sK", value); // EL – Erase in Line
            System.out.printf(CSI + "%sS", value); // SU – Scroll Up
            System.out.printf(CSI + "%sT", value); // SD – Scroll Down
            // System.out.printf(CSI + "%sm", value); // SGR – Select Graphic Rendition
        }
        System.out.println(">>>");

        System.out.print("<<<");
        for (String val1 : values) {
            for (String val2 : values) {
                System.out.printf(CSI + "%s;%sH", val1, val2); // CUP – Cursor Position
                System.out.printf(CSI + "%s;%sf", val1, val2); // HVP – Horizontal and Vertical Position
            }
        }
        System.out.println(">>>");
    }

    public static void main(String[] args) {
        // allTestScrolling();
        // allKindOfTests();
        // allTest256();
        testCursorMovementConsumed();
    }
}
