public class ThreadList 
{
	private int serverId = -1;
	private Thread[] thread = new Thread[FrontPanel.MAX_CLIENT_NUM];
	
	protected int addThread(Thread t)
	{	
		int threadIndex = -1;
		for(int i = 0; i < FrontPanel.MAX_CLIENT_NUM; i++)
		{
			if(!thread[i].isAlive())
			{
				thread[i] = t;
				threadIndex = i;
			}
		}
		
		return threadIndex;
	}
}
