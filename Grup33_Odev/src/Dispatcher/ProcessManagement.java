package Dispatcher;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ProcessManagement {
	public int burstTime;
	public int priority;
	public int arrivalTime;
	public int id;
	public int status;
	
	public Process process;
	
	public ProcessManagement() {
		status=0;
	}
	
	public void processStarted(int second , ProcessManagement p) throws IOException {
		//Ba�lat�lacak olan prosese komutlar ve prosesin sahip oldu�u bilgiler atanarak prosesin ba�lad��� ekrana yazd�r�l�r
		List<String> params = java.util.Arrays.asList( "java", "-jar","ProgramManagement.jar",Integer.toString(second),Integer.toString(p.id),
		Integer.toString(p.priority),Integer.toString(p.burstTime),"basladi");
		ProcessBuilder builder = new ProcessBuilder(params);
		process=builder.start();		
		Scanner scanner = new Scanner(process.getInputStream());
		while (scanner.hasNextLine()) {
		    System.out.println(scanner.nextLine());
		    
		}
		
	}
	public void processExecuted(int second , ProcessManagement p) throws IOException {
		//Y�r�t�len prosese proses bilgileri ve komutlar atanarak ekrana yazd�r�l�r
		List<String> params = java.util.Arrays.asList( "java", "-jar","ProgramManagement.jar",Integer.toString(second),Integer.toString(p.id),
				Integer.toString(p.priority),Integer.toString(p.burstTime),"yurutuluyor");
				ProcessBuilder builder = new ProcessBuilder(params);
				process=builder.start();
				Scanner scanner = new Scanner(process.getInputStream());
				while (scanner.hasNextLine()) {
				    System.out.println(scanner.nextLine());
				}
		
	}
	public void processOnHold(int second , ProcessManagement p, int controlId,int controlPriority,int controlBurst,int controlArrival) throws IOException {
		//Ask�ya al�nan prosese bilgileri ve komutlar atanarak ekrana yazd�r�l�r
		List<String> params = java.util.Arrays.asList( "java", "-jar","ProgramManagement.jar",Integer.toString(second),Integer.toString(controlId),
				Integer.toString(controlPriority),Integer.toString(controlBurst),"askida");
				ProcessBuilder builder = new ProcessBuilder(params);
				process=builder.start();
				Scanner scanner = new Scanner(process.getInputStream());
				while (scanner.hasNextLine()) {
				    System.out.println(scanner.nextLine());
				}
		
	}
	
	public void processTimeOut(int second , ProcessManagement p) throws IOException {
		//Zaman a��m�na u�rayan prosese bilgileri ve komutlar atanarak ekrana bilgiler yazd�r�l�r
		List<String> params = java.util.Arrays.asList( "java", "-jar","ProgramManagement.jar",Integer.toString(second),Integer.toString(p.id),
				Integer.toString(p.priority),Integer.toString(p.burstTime),"sonlandi");
				ProcessBuilder builder = new ProcessBuilder(params);
				process=builder.start();
				builder.command("asdasd");
				Scanner scanner = new Scanner(process.getInputStream());
				while (scanner.hasNextLine()) {
				    System.out.println(scanner.nextLine());
				}
			
		
	}
	public void processTerminated(int second , ProcessManagement p) throws IOException {
		//Sonland�r�lan prosesin bilgileri ekrana yazd�r�l�r
		List<String> params = java.util.Arrays.asList( "java", "-jar","ProgramManagement.jar",Integer.toString(second),Integer.toString(p.id),
				Integer.toString(p.priority),Integer.toString(p.burstTime),"sonlandi");
				ProcessBuilder builder = new ProcessBuilder(params);
				process=builder.start();
				builder.command("asdasd");
				Scanner scanner = new Scanner(process.getInputStream());
				while (scanner.hasNextLine()) {
					System.out.println(scanner.nextLine());
				}
		
	
	}
}
