import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class Service implements Runnable{
	public static final int backLog = 5;
	public static final int bufferSize = 4096;
	private volatile boolean onOff = true;
	private volatile int countClient = 0;	//client number count
	private volatile int id = -1;
	private String header = "";
	private String log;
	private String error;
	private int srcPort;
	private int dstPort;
	private InetAddress srcAddress;
	private InetAddress dstAddress;
	private volatile ServerSocket serverSocket;
//	private List connections = Collections.synchronizedList(new ArrayList());
	private Object locker = new Object();
	Socket socketOnServer;
	private String[] clientIp;
	
	public Service(int srcPort, InetAddress dstAddress, int dstPort)
	{
		try 
		{
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
		try 
		{
			this.serverSocket = new ServerSocket(srcPort, backLog, srcAddress);
		} catch (IOException e) {
			System.out.println("Service Constructor Error!");
			e.printStackTrace();
		}
		
		this.srcAddress = srcAddress;
		this.srcPort = srcPort;
		this.dstAddress = dstAddress;
		this.dstPort = dstPort;
		this.header = srcAddress.toString() + ":" + srcPort + " ---> " + dstAddress.toString()
				  + ": " + dstPort;
	}
	public Service(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort, 
				   String log, String error)
	{
		try 
		{
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
		System.out.println(header + " Start running...");
		try
		{
			while(onOff)
			{
				//when client is more than the MAX_CLIENT_NUM, the server stops to accept
				if(countClient < FrontPanel.MAX_CLIENT_NUM)
				{
					//server's socket connected to the outside clients
					socketOnServer = serverSocket.accept();
					countClient++;
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
			}
			
			if(!onOff && FrontPanel.debug)
				System.out.println("run is set to off!");
			
		} catch(IOException ioe) {
			if(FrontPanel.debug)
				System.out.println("Exception in run method of Service class...");
			ioe.printStackTrace();
		}
	}
	
	protected void stopService()
	{
		//System.out.println(this.onOff);
		this.onOff = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		this.serverSocket = null;
		this.socketOnServer = null;
		
		System.out.println(this.onOff);
		//System.out.println("The server" +  this.serverSocket.getInetAddress() + "is turned off!");
	}
	
	protected void stopClient()
	{
		
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
				
				while((length = ips.read(buffer)) > 0 && onOff)
				{
					ops.write(buffer, 0, length);
					ops.flush();
				}
				if(!onOff)
					System.out.println("DuplicateStream is set to off!");
			
			}catch(Exception e) {
				//System.out.println("DuplicateStream run method Exception...");
				//e.printStackTrace();
				countClient--;
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
	
	protected synchronized int getId()
	{
		return this.id;
	}
	
	protected synchronized void setId(int id)
	{
		this.id = id;
	}
}
