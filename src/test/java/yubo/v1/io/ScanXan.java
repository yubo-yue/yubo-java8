package yubo.v1.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ScanXan {
    public static void main(String[] args) {
        try (final Scanner s = new Scanner(new BufferedReader(new FileReader("src/test/resources/xanadu.txt")))){
            while (s.hasNext()) {
                System.out.println(s.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
