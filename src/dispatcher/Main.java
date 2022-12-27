package dispatcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader("giris.txt"));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] lineData = line.split(",");
        String variable1 = lineData[0];
        String variable2 = lineData[1];
        String variable3 = lineData[1];
       	System.out.print(variable1+" ");
       	System.out.print(variable2+" ");
       	System.out.println(variable3);
       	System.out.println("      ");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
 
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}