
public class ServerList 
{
	private Service[] serv;
	private int size;
	private int[] emptyPos;	//match with the Service[] and point out the empty position
	
	ServerList()
	{
		 serv = new Service[FrontPanel.MAX_SERVER_NUM];
		 emptyPos = new int[FrontPanel.MAX_SERVER_NUM];
		 size = 0;
		 for(int i = 0; i < FrontPanel.MAX_SERVER_NUM; i++)
		 {
			 emptyPos[i] = -1;
		 }
	}
	
	public int addServer(Service s)
	{
		int position = -10;
		
		if(size == 0)
		{
			serv[0] = s;
			emptyPos[0] = 1;
			size++;
			position = 0;
		}
		else
		{
			if(size == FrontPanel.MAX_SERVER_NUM)
			{
				position = -1;
			}
			else
			{
				int pos = pickPosition();
				serv[pos] = s;
				emptyPos[pos] = 1;
				size++;
				position = pos;
			}
		}
		
		return position;
	}
	
	public int deleteServer(Service s, int pos)
	{
		int position = -10;
		
		if(size == 0)
		{
			position = -1;
		}
		else
		{
			serv[pos] = null;
			emptyPos[pos] = -1;
			size--;
			position = pos;
		}
		
		return position;
	}
	
	public int getSize()
	{
		return size;
	}
	
	//get an empty position
	private int pickPosition()
	{
		int position = -1;
		
		for(int i = 0; i < emptyPos.length; i++)
		{
			if(emptyPos[i] == -1)
				return i;
		}
		
		return position;
	}
	
}
