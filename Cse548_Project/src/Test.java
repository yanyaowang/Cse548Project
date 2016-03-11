import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args) throws UnknownHostException 
	{
		InetAddress source = InetAddress.getByName("192.168.0.11");
		InetAddress destination = InetAddress.getByName("192.168.0.55");
		
		Service s = new Service(source, 6789, destination, 22);
		
		Thread t1 = new Thread(s);
		t1.start();
		
	}

}

/*

public class Test {

public static void main(String[] args) throws Exception
{
    System.out.println("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
    Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
    for (; n.hasMoreElements();)
    {
        NetworkInterface e = n.nextElement();

        Enumeration<InetAddress> a = e.getInetAddresses();
        for (; a.hasMoreElements();)
        {
            InetAddress addr = a.nextElement();
            System.out.println("  " + addr.getHostAddress());
        }
    }
} 
}


public class Test {
	
	private static InetAddress ip;
	private static String hostName;

	public static void main(String[] args) throws UnknownHostException {
		//Service s = new Service();
		ip = InetAddress.getLocalHost();
		hostName =ip.getHostName();
		
		System.out.println(ip);
		System.out.println(hostName);
	}

}

*/