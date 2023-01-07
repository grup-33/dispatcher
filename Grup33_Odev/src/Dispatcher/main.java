package Dispatcher;

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
	static ProcessManagement pm = new ProcessManagement();
	
	public static void main(String[] args) throws IOException {
		
	
		DispatcherShell duty = new DispatcherShell();
		duty.processRun(pm);
	}

}
