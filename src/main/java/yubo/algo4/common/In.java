package yubo.algo4.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class In implements AutoCloseable {
    private static final String CHARSET_NAME = "UTF-8";
    private static final Locale LOCALE = Locale.US;
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");
    private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

    private Scanner scanner;

    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

    public In(final Socket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("socket is null");
        }

        try {
            final InputStream is = socket.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (final IOException e) {
            throw new IllegalArgumentException("could not open " + e);
        }
    }

    public In(final URL url) {
        if (null == url)
            throw new IllegalArgumentException("url is null");
        try {
            final URLConnection connection = url.openConnection();
            final InputStream is = connection.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (final IOException e) {
            throw new IllegalArgumentException("could not open " + e);
        }
    }

    public In(final File file) {
        if (null == file)
            throw new IllegalArgumentException("file is null");

        try {
            final FileInputStream fis = new FileInputStream(file);
            scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (final FileNotFoundException e) {
            throw new IllegalArgumentException("could not open " + e);
        }
    }

    public In(final String name) {
        if (null == name)
            throw new IllegalArgumentException("name is null");

        try {
            final File file = new File(name);
            if (file.exists()) {
                final FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                scanner.useLocale(LOCALE);
                return;
            }

            URL url = getClass().getResource(name);
            if (null == url) {
                url = new URL(name);
            }

            final URLConnection connection = url.openConnection();
            final InputStream is = connection.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public In(final Scanner scanner) {
        if (null == scanner)
            throw new IllegalArgumentException("scanner is null");
        this.scanner = scanner;
    }

    public boolean exists() {
        return scanner != null;
    }

    public boolean isEmpty() {
        return !scanner.hasNext();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String result = scanner.next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result.charAt(0);
    }

    public String readAll() {
        if (!scanner.hasNext()) {
            return "";
        }
        scanner.useDelimiter(EVERYTHING_PATTERN);
        String result = scanner.next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }

    public String readString() {
        return scanner.next();
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public double readDouble() {
        return scanner.nextDouble();
    }

    public float readFloat() {
        return scanner.nextFloat();
    }

    public long readLong() {
        return scanner.nextLong();
    }

    public short readShort() {
        return scanner.nextShort();
    }

    public byte readByte() {
        return scanner.nextByte();
    }

    public boolean readBoolean() {
        String s = readString();
        if ("true".equalsIgnoreCase(s))
            return true;
        if ("false".equalsIgnoreCase(s))
            return false;
        if ("1".equals(s))
            return true;
        if ("0".equals(s))
            return false;
        throw new InputMismatchException();
    }

    public String[] readAllStrings() {
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0) {
            return tokens;
        }
        String[] decaptitokens = new String[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i++) {
            decaptitokens[i] = tokens[i + 1];
        }
        return decaptitokens;
    }

    public String[] readAllLines() {
        final ArrayList<String> lines = new ArrayList<>();
        while (hasNextLine()) {
            lines.add(readLine());
        }
        return lines.toArray(new String[lines.size()]);
    }

    public int[] readAllInts() {
        final String[] fields = readAllStrings();
        final int[] array = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            array[i] = Integer.parseInt(fields[i]);
        }
        return array;
    }

    public long[] readAllLongs() {
        final String[] fields = readAllStrings();
        final long[] values = new long[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = Long.parseLong(fields[i]);
        }
        return values;
    }

    public double[] readAllDoubles() {
        final String[] fields = readAllStrings();
        final double[] values = new double[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = Double.parseDouble(fields[i]);
        }
        return values;
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }

    public static void main(String[] args) {
        final String urlName = "http://www.baidu.com";
        System.out.printf("Read all from url %s%n", urlName);
        System.out.println("-------------------------------");
        try (In in = new In(urlName)) {
            System.out.println(in.readAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        try (In in = new In(urlName)) {
            while (in.hasNextLine()) {
                System.out.println(in.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.printf("readLine() from current directory%n");
        System.out.println("-------------------------------");
        try (In in = new In("/InTest.txt")) {
            while (!in.isEmpty()) {
                final String s = in.readLine();
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
