
class SrvNode 
{
	private String ipFrom;
	private int portFrom;
	private String ipTo;
	private int portTo;
	private String ipServer;
	private int portServer;

	SrvNode(String ipServer, int portServer, String ipFrom, int portFrom, String ipTo, int portTo)
	{
		this.ipServer = ipServer;
		this.portServer = portServer;
		this.ipFrom = ipFrom;
		this.ipTo = ipTo;
		this.portFrom = portFrom;
		this.portTo = portTo;
	}

	public String getIpFrom()
	{
		return this.ipFrom;
	}

	public int getPortFrom()
	{
		return this.portFrom;
	}

	public String getIpTo()
	{
		return this.ipTo;
	}

	public int getPortTo()
	{
		return this.portTo;
	}

	public String getIpServer()
	{
		return this.ipServer;
	}

	public int getPortServer()
	{
		return this.portServer;
	}

	public int isSameServer(SrvNode node)
	{
		if(this.ipServer.equals(node.getIpServer()))
			return 1;
		else
			return -1;
	}

	public int isSameClient(SrvNode node)
	{
		if(this.ipFrom.equals(node.getIpFrom()))
			return 1;
		else
			return -1;
	}
}

/*



*/