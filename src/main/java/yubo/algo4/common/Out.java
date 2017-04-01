package yubo.algo4.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Locale;

public final class Out implements AutoCloseable {
    private static final String CHARSET_NAME = "UTF-8";
    private static final Locale LOCALE = Locale.US;
    private PrintWriter out;

    public Out(final OutputStream os) {
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Out() {
        this(System.out);
    }

    public Out(final Socket socket) {
        try {
            final OutputStream os = socket.getOutputStream();
            final OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Out(final String filename) {
        try {
            final OutputStream os = new FileOutputStream(filename);
            final OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        out.close();
    }

    public void println() {
        out.println();
    }

    public void println(final Object o) {
        out.print(o);
    }

    public void println(boolean x) {
        out.println(x);
    }

    public void println(char x) {
        out.println(x);
    }

    public void println(double x) {
        out.println(x);
    }

    public void println(float x) {
        out.println(x);
    }

    public void println(int x) {
        out.println(x);
    }

    public void println(long x) {
        out.println(x);
    }

    public void println(byte x) {
        out.println(x);
    }

    public void print() {
        out.flush();
    }

    public void print(Object x) {
        out.print(x);
        out.flush();
    }

    public void print(boolean x) {
        out.print(x);
        out.flush();
    }

    public void print(char x) {
        out.print(x);
        out.flush();
    }

    public void print(double x) {
        out.print(x);
        out.flush();
    }

    public void print(float x) {
        out.print(x);
        out.flush();
    }

    public void print(int x) {
        out.print(x);
        out.flush();
    }

    public void print(long x) {
        out.print(x);
        out.flush();
    }

    public void print(byte x) {
        out.print(x);
        out.flush();
    }

    public void printf(String format, Object... args) {
        out.printf(LOCALE, format, args);
        out.flush();
    }

    public void printf(Locale locale, String format, Object... args) {
        out.printf(locale, format, args);
        out.flush();
    }

    public static void main(String[] args) {
        try (Out out = new Out()) {
            out.println("Test 1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Out out = new Out("/tmp/OutTest.txt")) {
            out.println("Test 2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
