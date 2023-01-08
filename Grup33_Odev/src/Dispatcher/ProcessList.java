package Dispatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;


public class ProcessList {

	public List<ProcessManagement> processes = new ArrayList<ProcessManagement>();//Dosyadan okunan proseslerin tutulacaðý bir proses listesi oluþturulur

	public ProcessList() throws IOException{
		int id = 0;
		File file =new File("giris.txt");	
		
		String line;
		FileReader fileReader = new FileReader(file);	
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		while ((line = bufferedReader.readLine())!= null) {//dosyayý satýr satýr okur
			String[] array= line.split(", ");
			
			for (int i=0;i<array.length;i++) {//Okunan dosyadaki bilgileri prosesin ilgili deðerlerine atar
				ProcessManagement p = new ProcessManagement();
				p.id = id++;
				p.arrivalTime = Integer.parseInt(array[i]);
				p.priority = Integer.parseInt(array[++i]);
				p.burstTime = Integer.parseInt(array[++i]);
				
				processes.add(p);
				
			}
			
			
		}
		bufferedReader.close();
		
	}
	
}
