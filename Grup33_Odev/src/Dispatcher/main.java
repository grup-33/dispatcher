package Dispatcher;

import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class main {
	
	static int second=-1;
	static ProcessManagement pm = new ProcessManagement();//Yeni bir proses y�neticisi olu�turulur
	
	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
	    while (true) {//komut giri�i yap�lana kadar tekrar �al��t�r�l�r

	      System.out.print(">Systemd ");
	      String input = scanner.nextLine();
	      String[] tokens = input.split("\s+");
	      String command = tokens[0];
	      switch (command) {//girilen komuta g�re do�ru komutsa program �al��t�r�l�r yanl��sa hata mesaj� yazd�r�l�r
	        case "giris.txt":
	            DispatcherShell duty = new DispatcherShell();//Yeni bir g�revlendirici olu�turulur
	    		duty.processRun(pm);//g�revlendirici program� ba�lat�r
	          break;
	        default:
	            System.out.println("Unrecognized command: " + command);
	          break;
	      }
	    }
	
		
	}

}
