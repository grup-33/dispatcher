package Dispatcher;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class DispatcherShell {
	public Queue<ProcessManagement> gercekZamanli = new LinkedList<ProcessManagement>();//Gerçek zamanlý proseslerin tutulacaðý proses kuyruðu
	
	public Queue<ProcessManagement> feedback1 = new LinkedList<ProcessManagement>();//Önceliði 1 olan proseslerin tutulacaðý kuyruk
	public Queue<ProcessManagement> feedback2 = new LinkedList<ProcessManagement>();//Önceliði 2 olan proseslerin tutulacaðý kuyruk
	public Queue<ProcessManagement> feedback3 = new LinkedList<ProcessManagement>();//Önceliði 3 olan proseslerin tutulacaðý kuyruk
	
	static int second;
	static int controlId = -1;
	static int controlArrival = -1;
	static int controlPriority = -1;
	static int controlBurst = -1;
	
	public void processAdd(ProcessManagement p) {//Prosesleri önceliklerine göre kuyruklara ekler
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
	
	public void processRun(ProcessManagement p) throws IOException {//prosesler çalýþtýrýlýp kontrol iþlemi yapýlýr
		ProcessList list=new ProcessList();
		DispatcherShell duty = new DispatcherShell();
		Timer myTimer = new Timer();
		second=-1;
		
		TimerTask task = new TimerTask() {	
			@Override
			public void run() {
				for	(int i=0;i<list.processes.size();i++) {	
					//Listedeki prosesleri ekleme fonksiyonuna gönderir ve gönderdikten sonra listeden siler
					if(list.processes.get(i).arrivalTime == second) {
						duty.processAdd(list.processes.get(i));	
						list.processes.remove(i--);
					}
				}
				

				try {
					duty.processPrint(duty, p);//görevlendirici prosesleri yazdýrma methodunu çaðýrýr
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				if(duty.gercekZamanli.size() == 0 && duty.feedback1.size() == 0 && duty.feedback2.size() == 0 && duty.feedback3.size() == 0 && list.processes.size() == 0) 
				{
					//Proses yoksa programý sonlandýrýr
					System.out.print("DispatcherShell Sonlandi");
					myTimer.cancel();
				}
				
			}
		};
		
		myTimer.schedule(task,0,100);
	}
	
	public void processPrint(DispatcherShell duty, ProcessManagement p) throws IOException {//Proseslerin ekrana yazdýrýlma iþlemleri yapýlýr
		
		

		for(int i=0;i<gercekZamanli.size();i++) {
			p = (ProcessManagement) duty.gercekZamanli.poll();//gerçek zamanlý kuyruðun en baþýndaki prosesi alýr ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman aþýmýna uðrayýp uðramadýðýný kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;	
			}
			else duty.gercekZamanli.add(p);//proses zaman aþýmýna uðramadýysa tekrar kuyruða eklenir
		}

		for(int i=0;i<feedback1.size();i++) {
			p = (ProcessManagement) duty.feedback1.poll();//önceliði 1 olan kuyruðun en baþýndaki prosesi alýr ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman aþýmýna uðrayýp uðramadýðýný kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else feedback1.add(p);//proses zaman aþýmýna uðramadýysa tekrar kuyruða eklenir
		}

		for(int i=0;i<feedback2.size();i++) {
			p = (ProcessManagement) duty.feedback2.poll();//önceliði 2 olan kuyruðun en baþýndaki prosesi alýr ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman aþýmýna uðrayýp uðramadýðýný kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback2.add(p);//proses zaman aþýmýna uðramadýysa tekrar kuyruða eklenir
		}

		for(int i=0;i<feedback3.size();i++) {
			p = (ProcessManagement) duty.feedback3.poll();//önceliði 3 olan kuyruðun en baþýndaki prosesi alýr ve kuyruktan prosesi siler 
			if(second - p.arrivalTime>20) {	//prosesin zaman aþýmýna uðrayýp uðramadýðýný kontrol eder
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback3.add(p);//proses zaman aþýmýna uðramadýysa tekrar kuyruða eklenir
		}

		
		if(duty.gercekZamanli.size() != 0) {//gerçek zamanlý kuyruk boþ deðilse iþlem yapar
			p = (ProcessManagement) duty.gercekZamanli.peek();
			
		
				if(p.id != controlId && controlId != -1) {//prosesin askýya alýnmasýný kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				if(p.status == 0) {//prosesi baþlatýr
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//proses patlama zamanýna gelmemiþse yürütülmeye baþlanýr
					p.processExecuted(second, p);
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zamaný gelen prosesi sonlandýrýr ve kuyruktan siler
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
		
		else if(duty.feedback1.size() != 0) {	//gerçek zamanlý kuyruk boþsa önceliði 1 olan kuyruk iþlem yapar
			p = (ProcessManagement) duty.feedback1.poll();	
			
			
				if(p.id != controlId && controlId != -1) {//prosesin askýya alýnma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses baþlatýlýr
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zamaný gelmemiþse yürütülmeye baþlanýr 
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zamaný gelen prosesi sonlandýrýr
					try {	
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses yürütüldükten sonra patlama zamaný gelmemiþse önceliði düþürülür
					duty.feedback2.add(p);	
					
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
			
		}
		
		else if(duty.feedback2.size() != 0) {//önceliði 1 olan kuyruk boþsa önceliði 2 olan kuyruk iþlem yapar 
			p = (ProcessManagement) duty.feedback2.poll();
			

				if(p.id != controlId && controlId != -1) {//prosesin askýya alýnma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses baþlatýlýr
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zamaný gelmemiþse yürütülür 
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {//patlama zamaný gelen prosesi sonlandýrýr 
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses yürütüldükten sonra patlama zamaný gelmemiþse önceliði düþürülür
					duty.feedback3.add(p);	
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
				
		}
		
		else if(duty.feedback3.size() != 0) {//önceliði 2 olan kuyruk boþsa önceliði 3 olan kuyruk iþlem yapar
			
			p = (ProcessManagement) duty.feedback3.poll();

			
				if(p.id != controlId && controlId != -1) {//prosesin askýya alýnma durumunu kontrol eder
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {//proses baþlatýlýr
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {//prosesin patlama zamaný gelmemiþse yürütülür 
					p.processExecuted(second, p);
					p.burstTime--;
				}
				
				
				if(p.burstTime==0) {//patlama zamaný gelen prosesi sonlandýrýr
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {//proses yürütüldükten sonra patlama zamaný gelmemiþse tekrar önceliði 3 olan kuyruða eklenir
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
