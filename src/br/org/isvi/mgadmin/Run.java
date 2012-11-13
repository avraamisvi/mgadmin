package br.org.isvi.mgadmin;

import java.io.IOException;

public class Run {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		boolean startMainWindow = false;
		
		if(args.length > 0) {
			startMainWindow = true;
			System.out.println("ARGS: " + args);
		}
		
		if(!startMainWindow) {
			System.out.println("Starting....");
			Process p = Runtime.getRuntime().exec("java -XstartOnFirstThread -jar mgadmin.jar 1");
			p.waitFor();
			System.out.println("Started");
		} else {
			MainWindow.start();
		}
	}
}
