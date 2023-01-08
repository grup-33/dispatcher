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
	static ProcessManagement pm = new ProcessManagement();//Yeni bir proses yöneticisi oluþturulur
	
	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
	    while (true) {//komut giriþi yapýlana kadar tekrar çalýþtýrýlýr

	      System.out.print(">Systemd ");
	      String input = scanner.nextLine();
	      String[] tokens = input.split("\s+");
	      String command = tokens[0];
	      switch (command) {//girilen komuta göre doðru komutsa program çalýþtýrýlýr yanlýþsa hata mesajý yazdýrýlýr
	        case "giris.txt":
	            DispatcherShell duty = new DispatcherShell();//Yeni bir görevlendirici oluþturulur
	    		duty.processRun(pm);//görevlendirici programý baþlatýr
	          break;
	        default:
	            System.out.println("Unrecognized command: " + command);
	          break;
	      }
	    }
	
		
	}

}
