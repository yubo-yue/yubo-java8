package yubo.v1.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCharacters {
    public static void main(String[] args) {

        try (FileReader reader = new FileReader("src/test/resources/xanadu.txt");
             FileWriter writer = new FileWriter("characteroutput.txt")) {
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
