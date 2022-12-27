package dispatcher;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessList {

public List<ProcessBuilder> process = new ArrayList<ProcessBuilder>();
	
	public ProcessList() throws IOException{
		int id = 0;
		FileReader fread = new FileReader("giris.txt");	
		String satir;
		BufferedReader bRead = new BufferedReader(fread);
		
		while ((satir = bRead.readLine())!= null) {
			String[] dizi= satir.split(", ");	
			
			for (int i=0;i<dizi.length;i++) {
				ProcessBuilder pb = new ProcessBuilder();
				pb.id = id++;
				pb.varisZamani = Integer.parseInt(dizi[i]);
				pb.oncelik = Integer.parseInt(dizi[++i]);
				pb.patlamaZamani = Integer.parseInt(dizi[++i]);
				
				process.add(pb);
				
			}
			
			
		}
		bRead.close();
		
	}
}
