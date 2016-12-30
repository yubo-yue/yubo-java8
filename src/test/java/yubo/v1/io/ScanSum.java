package yubo.v1.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Scanner;

public class ScanSum {
    public static void main(String[] args) {
        double sum = 0.0;

        try (final Scanner s = new Scanner(new BufferedReader(new FileReader("src/test/resources/usnumber.txt")))){
            s.useLocale(Locale.US);

            while (s.hasNext()) {
                if (s.hasNextDouble()) {
                    sum += s.nextDouble();
                } else
                    s.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(sum);
    }
}
