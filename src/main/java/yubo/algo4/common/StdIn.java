package yubo.algo4.common;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class StdIn {
    private static final String CHARSET_NAME = "UTF-8";
    private static final Locale LOCALE = Locale.US;
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");
    private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

    private static Scanner scanner;

    static {
        resync();
    }

    private static void resync() {
        setScanner(new Scanner(new BufferedInputStream(System.in), CHARSET_NAME));
    }

    private static void setScanner(final Scanner scanner) {
        StdIn.scanner = scanner;
        StdIn.scanner.useLocale(LOCALE);
    }

    private StdIn() {
    }


    public static boolean isEmpty() {
        return !scanner.hasNext();
    }

    public static boolean hasNextLine() {
        return scanner.hasNextLine();
    }


    public static boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }

    public static String readLine() {
        String line;
        try {
            line = scanner.nextLine();
        } catch (final NoSuchElementException e) {
            line = null;
        }
        return line;
    }

    public static char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String ch = scanner.next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return ch.charAt(0);
    }

    public static String readAll() {
        if (!scanner.hasNextLine()) {
            return "";
        }
        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }

    public static String readString() {
        return scanner.next();
    }

    public static int readInt() {
        return scanner.nextInt();
    }

    public static double readDouble() {
        return scanner.nextDouble();
    }

    public static float readFloat() {
        return scanner.nextFloat();
    }

    public static long readLong() {
        return scanner.nextLong();
    }

    public static short readShort() {
        return scanner.nextShort();
    }

    public static byte readByte() {
        return scanner.nextByte();
    }

    public static boolean readBoolean() {
        String s = scanner.next();
        if ("true".equalsIgnoreCase(s)) return true;
        if ("false".equalsIgnoreCase(s)) return false;
        if ("1".equals(s)) return true;
        if ("0".equals(s)) return false;

        throw new InputMismatchException();
    }

    public static String[] readAllStrings() {
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0)
            return tokens;
        String[] decapitokens = new String[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i++) {
            decapitokens[i] = tokens[i + 1];
        }

        return decapitokens;
    }

    public static String[] readAllLines() {
        final ArrayList<String> lines = new ArrayList<>();
        while (hasNextLine()) {
            lines.add(readLine());
        }

        return lines.toArray(new String[lines.size()]);
    }

    public static int[] readAllInts() {
        final String[] fields = readAllStrings();
        int[] vals = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Integer.parseInt(fields[i]);
        }
        return vals;
    }

    public static long[] readAllLongs() {
        final String[] fields = readAllStrings();
        long[] vals = new long[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Long.parseLong(fields[i]);
        }
        return vals;
    }

    public static double[] readAllDoubles() {
        final String[] fields = readAllStrings();
        double[] vals = new double[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Double.parseDouble(fields[i]);
        }
        return vals;
    }

    public static void main(String[] args) {
        String s = StdIn.readString();
    }
}
