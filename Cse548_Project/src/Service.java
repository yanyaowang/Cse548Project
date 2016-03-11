import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class Service implements Runnable{
	public static final int backLog = 5;
	public static final int bufferSize = 4096;
	private boolean onOff = true;
	private String header;
	private String log;
	private String error;
	private int srcPort;
	private int dstPort;
	private InetAddress srcAddress;
	private InetAddress dstAddress;
	private ServerSocket serverSocket;
	private List connections = Collections.synchronizedList(new ArrayList());
	private Object locker = new Object();
	//private ServerSocket[] serverSocket = new ServerSocket[4];
	
	public Service(int srcPort, InetAddress dstAddress, int dstPort)
	{
		try {
			this.serverSocket = new ServerSocket(srcPort, backLog);
		} catch (IOException e) {
			System.out.println("Service Constructor Error!");
			e.printStackTrace();
		}
		
		this.srcAddress = srcAddress;
		this.srcPort = srcPort;
		this.dstAddress = dstAddress;
		this.dstPort = dstPort;
	}
	
	public Service(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort)
	{
		try {
			this.serverSocket = new ServerSocket(srcPort, backLog, srcAddress);
		} catch (IOException e) {
			System.out.println("Service Constructor Error!");
			e.printStackTrace();
		}
		
		this.srcAddress = srcAddress;
		this.srcPort = srcPort;
		this.dstAddress = dstAddress;
		this.dstPort = dstPort;
	}
	public Service(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort, 
				   String log, String error)
	{
		try {
			this.serverSocket = new ServerSocket(srcPort, backLog, srcAddress);
		} catch (IOException e) {
			System.out.println("Service Constructor Error!");
			e.printStackTrace();
		}
		
		this.srcAddress = srcAddress;
		this.srcPort = srcPort;
		this.dstAddress = dstAddress;
		this.dstPort = dstPort;
		this.log = log;
		this.error = error;
		this.header = srcAddress.toString() + ": " + srcPort + " ---> " + dstAddress
					  + ": " + dstPort;
	}
	
	public void run() 
	{
		System.out.println(header + " : start running...");
		try
		{
			while(onOff)
			{
				//server's socket connected to the outside clients
				Socket socketOnServer = serverSocket.accept();
				//dstSocket -- robot
				Socket dstSocket = new Socket(dstAddress, dstPort);
				
				DuplicateStream serverToRobot = new DuplicateStream(socketOnServer, dstSocket);
				DuplicateStream robotToServer = new DuplicateStream(dstSocket, socketOnServer);
				
				synchronized(locker)
				{
					
					serverToRobot.start();
					robotToServer.start();
				}
			}
			
		} catch(IOException ioe) {
			System.out.println("Exception in run method of Service class...");
			ioe.printStackTrace();
		}
	}
	
	private void stopService()
	{
		this.onOff = false;
	}
	
	
	protected class DuplicateStream extends Thread
	{
		private Socket sockIn;
		private Socket sockOut;
		
		public DuplicateStream(Socket sockIn, Socket sockOut) 
		{
			this.sockIn = sockIn;
			this.sockOut = sockOut;
		}
		
		public void run() 
		{
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			
			try
			{
				InputStream ips = sockIn.getInputStream();
				OutputStream ops = sockOut.getOutputStream();
				
				while((length = ips.read(buffer)) > 0)
				{
					ops.write(buffer, 0, length);
					ops.flush();
				}
			
			}catch(Exception e) {
				//System.out.println("DuplicateStream run method Exception...");
				//e.printStackTrace();
				System.out.println("Connection between " + sockIn.getInetAddress() + " and " + sockOut.getInetAddress() + " break...");
			}
			
			try
			{
				sockIn.close();
				sockOut.close();
			}catch(Exception ee) {
				System.out.println("Socket Close Error!");
				ee.printStackTrace();
			}
		}
		
	}
}
