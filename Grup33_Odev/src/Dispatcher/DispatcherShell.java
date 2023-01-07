package Dispatcher;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class DispatcherShell {
	public Queue<ProcessManagement> gercekZamanli = new LinkedList<ProcessManagement>();
	
	public Queue<ProcessManagement> feedback1 = new LinkedList<ProcessManagement>();
	public Queue<ProcessManagement> feedback2 = new LinkedList<ProcessManagement>();
	public Queue<ProcessManagement> feedback3 = new LinkedList<ProcessManagement>();
	
	static int second;
	static int controlId = -1;
	static int controlArrival = -1;
	static int controlPriority = -1;
	static int controlBurst = -1;
	
	public void processAdd(ProcessManagement p) {
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
	
	public void processRun(ProcessManagement p) throws IOException {
		ProcessList list=new ProcessList();
		DispatcherShell duty = new DispatcherShell();
		Timer myTimer = new Timer();
		second=-1;
		
		TimerTask task = new TimerTask() {	
			@Override
			public void run() {
				for	(int i=0;i<list.processes.size();i++) {	
					if(list.processes.get(i).arrivalTime == second) {
						duty.processAdd(list.processes.get(i));	
						list.processes.remove(i--);
					}
				}
				

				try {
					duty.processPrint(duty, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				if(duty.gercekZamanli.size() == 0 && duty.feedback1.size() == 0 && duty.feedback2.size() == 0 && duty.feedback3.size() == 0 && list.processes.size() == 0) 
				{
					System.out.print("DispatcherShell Sonlandi");
					myTimer.cancel();
				}
				
			}
		};
		
		myTimer.schedule(task,0,100);
	}
	
	public void processPrint(DispatcherShell duty, ProcessManagement p) throws IOException {
		
		

		for(int i=0;i<gercekZamanli.size();i++) {
			p = (ProcessManagement) duty.gercekZamanli.poll();
			if(second - p.arrivalTime>20) {	
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;	
			}
			else duty.gercekZamanli.add(p);
		}

		for(int i=0;i<feedback1.size();i++) {
			p = (ProcessManagement) duty.feedback1.poll();
			if(second - p.arrivalTime>20) {	
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else feedback1.add(p);
		}

		for(int i=0;i<feedback2.size();i++) {
			p = (ProcessManagement) duty.feedback2.poll();
			if(second - p.arrivalTime>20) {	
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback2.add(p);
		}

		for(int i=0;i<feedback3.size();i++) {
			p = (ProcessManagement) duty.feedback3.poll();
			if(second - p.arrivalTime>20) {	
				try {
					p.processTimeOut(second, p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
			else duty.feedback3.add(p);
		}

		
		if(duty.gercekZamanli.size() != 0) {
			p = (ProcessManagement) duty.gercekZamanli.peek();
			
		
				if(p.id != controlId && controlId != -1) {
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				if(p.status == 0) {
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {
					p.processExecuted(second, p);
					p.burstTime--;
				}
				if(p.burstTime==0) {
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
		
		else if(duty.feedback1.size() != 0) {	
			p = (ProcessManagement) duty.feedback1.poll();	
			
			
				if(p.id != controlId && controlId != -1) {
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {
					try {	
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {
					duty.feedback2.add(p);	
					
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
			
		}
		
		else if(duty.feedback2.size() != 0) {
			p = (ProcessManagement) duty.feedback2.poll();
			

				if(p.id != controlId && controlId != -1) {
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {
					p.processStarted(second, p);
					p.priority++;
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {
					p.processExecuted(second, p);
					p.priority++;
					p.burstTime--;
				}
				if(p.burstTime==0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {
					duty.feedback3.add(p);	
					controlId = p.id;
					controlPriority=p.priority;
					controlArrival=p.arrivalTime;
					controlBurst=p.burstTime;
				}
			
			
				
		}
		
		else if(duty.feedback3.size() != 0) {
			
			p = (ProcessManagement) duty.feedback3.poll();

			
				if(p.id != controlId && controlId != -1) {
					p.processOnHold(second, p, controlId, controlPriority, controlBurst, controlArrival);
					controlId=-1;
				}
				
				if(p.status == 0) {
					p.processStarted(second, p);
					p.burstTime--;
					p.status++;
				}
				else if(p.burstTime != 0) {
					p.processExecuted(second, p);
					p.burstTime--;
				}
				
				
				if(p.burstTime==0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					p.processTerminated(second+1, p);
					controlId = -1;
				}
				else {
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
