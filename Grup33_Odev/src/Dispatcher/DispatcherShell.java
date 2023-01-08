package Dispatcher;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class DispatcherShell {
	public Queue<ProcessManagement> gercekZamanli = new LinkedList<ProcessManagement>();//Ger�ek zamanl� proseslerin tutulaca�� proses kuyru�u
	
	public Queue<ProcessManagement> feedback1 = new LinkedList<ProcessManagement>();//�nceli�i 1 olan proseslerin tutulaca�� kuyruk
	public Queue<ProcessManagement> feedback2 = new LinkedList<ProcessManagement>();//�nceli�i 2 olan proseslerin tutulaca�� kuyruk
	public Queue<ProcessManagement> feedback3 = new LinkedList<ProcessManagement>();//�nceli�i 3 olan proseslerin tutulaca�� kuyruk
	
	static int second;
	static int controlId = -1;
	static int controlArrival = -1;
	static int controlPriority = -1;
	static int controlBurst = -1;
	
	public void processAdd(ProcessManagement p) {//Prosesleri �nceliklerine g�re kuyruklara ekler
		if(p.priority == 0) {
			gercekZamanli.add(p);
		}
		else if(p.priority == 1) {
			feedback1.add(p);
		}
		else if(p.priority == 2) {
			feedback2.add(p);
		}
		else {
			feedback3.add(p);
		}
	}
	
	public void processRun(ProcessManagement p) throws IOException {//prosesler �al��t�r�l�p kontrol i�lemi yap�l�r
		ProcessList list=new ProcessList();
		DispatcherShell duty = new DispatcherShell();
		Timer myTimer = new Timer();
		second=-1;
		
		TimerTask task = new TimerTask() {	
			@Override
			public void run() {
				for	(int i=0;i<list.processes.size();i++) {	
					//Listedeki prosesleri ekleme fonksiyonuna g�nderir ve g�nderdikten sonra listeden siler
					if(list.processes.get(i).arrivalTime == second) {
						duty.processAdd(list.processes.get(i));	
						list.processes.remove(i--);
					}
				}
				

				try {
					duty.processPrint(duty, p);//g�revlendirici prosesleri yazd�rma methodunu �a��r�r
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				if(duty.gercekZamanli.size() == 0 && duty.feedback1.size() == 0 && duty.feedback2.size() == 0 && duty.feedback3.size() == 0 && list.processes.size() == 0) 
				{
					//Proses yoksa program� sonland�r�r
					System.out.print("DispatcherShell Sonlandi");
					myTimer.cancel();
				}
				
			}
		};
		
		myTimer.schedule(task,0,100);
	}
	
	public void processPrint(DispatcherShell duty, ProcessManagement p) throws IOException {//Proseslerin ekrana yazd�r�lma i�lemleri yap�l�r
		
		

		for(int i=0;i<gercekZamanli.size();i++) {
			p = (ProcessManagement) duty.gercekZamanli.poll();//ger�ek zamanl� kuyru�un en ba��ndaki prosesi al�r ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman a��m�na u�ray�p u�ramad���n� kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;	
			}
			else duty.gercekZamanli.add(p);//proses zaman a��m�na u�ramad�ysa tekrar kuyru�a eklenir
		}

		for(int i=0;i<feedback1.size();i++) {
			p = (ProcessManagement) duty.feedback1.poll();//�nceli�i 1 olan kuyru�un en ba��ndaki prosesi al�r ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman a��m�na u�ray�p u�ramad���n� kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else feedback1.add(p);//proses zaman a��m�na u�ramad�ysa tekrar kuyru�a eklenir
		}

		for(int i=0;i<feedback2.size();i++) {
			p = (ProcessManagement) duty.feedback2.poll();//�nceli�i 2 olan kuyru�un en ba��ndaki prosesi al�r ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman a��m�na u�ray�p u�ramad���n� kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback2.add(p);//proses zaman a��m�na u�ramad�ysa tekrar kuyru�a eklenir
		}

		for(int i=0;i<feedback3.size();i++) {
			p = (ProcessManagement) duty.feedback3.poll();//�nceli�i 3 olan kuyru�un en ba��ndaki prosesi al�r ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman a��m�na u�ray�p u�ramad���n� kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback3.add(p);//proses zaman a��m�na u�ramad�ysa tekrar kuyru�a eklenir
		}

		
		if(duty.gercekZamanli.size() != 0) {//ger�ek zamanl� kuyruk bo� de�ilse i�lem yapar
			p = (ProcessManagement) duty.gercekZamanli.peek();
			
		
				if(p.id != controlId && controlId != -1) {//prosesin ask�ya al�nmas�n� kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				if(p.status == 0) {//prosesi ba�lat�r
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//proses patlama zaman�na gelmemi�se y�r�t�lmeye ba�lan�r
					p.processExecuted(second, p);
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zaman� gelen prosesi sonland�r�r ve kuyruktan siler
					try {	
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					p.processTerminated(second+1, p);
					duty.gercekZamanli.remove();
					controlId=-1;	
				}
			
			
			
		}
		
		else if(duty.feedback1.size() != 0) {	//ger�ek zamanl� kuyruk bo�sa �nceli�i 1 olan kuyruk i�lem yapar
			p = (ProcessManagement) duty.feedback1.poll();	
			
			
				if(p.id != controlId && controlId != -1) {//prosesin ask�ya al�nma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses ba�lat�l�r
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zaman� gelmemi�se y�r�t�lmeye ba�lan�r 
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zaman� gelen prosesi sonland�r�r
					try {	
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses y�r�t�ld�kten sonra patlama zaman� gelmemi�se �nceli�i d���r�l�r
					duty.feedback2.add(p);	
					
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
			
		}
		
		else if(duty.feedback2.size() != 0) {//�nceli�i 1 olan kuyruk bo�sa �nceli�i 2 olan kuyruk i�lem yapar 
			p = (ProcessManagement) duty.feedback2.poll();
			

				if(p.id != controlId && controlId != -1) {//prosesin ask�ya al�nma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses ba�lat�l�r
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zaman� gelmemi�se y�r�t�l�r 
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zaman� gelen prosesi sonland�r�r 
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses y�r�t�ld�kten sonra patlama zaman� gelmemi�se �nceli�i d���r�l�r
					duty.feedback3.add(p);	
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
				
		}
		
		else if(duty.feedback3.size() != 0) {//�nceli�i 2 olan kuyruk bo�sa �nceli�i 3 olan kuyruk i�lem yapar
			
			p = (ProcessManagement) duty.feedback3.poll();

			
				if(p.id != controlId && controlId != -1) {//prosesin ask�ya al�nma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses ba�lat�l�r
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zaman� gelmemi�se y�r�t�l�r 
					p.processExecuted(second, p);
					p.burstTime--;
				}
				
				
				if(p.burstTime==0) {//patlama zaman� gelen prosesi sonland�r�r
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses y�r�t�ld�kten sonra patlama zaman� gelmemi�se tekrar �nceli�i 3 olan kuyru�a eklenir
					duty.feedback3.add(p);
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
				
			
			
			
		}
		second++;
		}
}
