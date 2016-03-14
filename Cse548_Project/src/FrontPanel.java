import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
//import java.util.ArrayList;
import java.awt.event.*;

import javax.swing.*;

class FrontPanel extends JPanel
{
	public static final boolean debug = true;
	public static final int MAX_SERVER_NUM = 4;
	public static final int MAX_CLIENT_NUM = 2;
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	String temp = "";
	String serverIp = null;
	int serverPort = 0;
	String robotIp = null;
	int robotPort = 0;
	int serverFlag;
//	ArrayList<SrvNode> srvNodeList = new ArrayList<SrvNode>();
	JButton addServerJButton;
	JLabel topLabel;
	JScrollPane bodyPane; 
	JLabel[] serverImage = new JLabel[MAX_SERVER_NUM];
	JLabel[] serverTitle = new JLabel[MAX_SERVER_NUM];
	JTextArea[] serverTextArea = new JTextArea[MAX_SERVER_NUM];
	JButton[] serverButtonList =new JButton[MAX_SERVER_NUM];
	//JPanel[] serverDetail = new JPanel[MAX_SERVER_NUM];
	JPanel[] serverPanelList = new JPanel[MAX_SERVER_NUM];
	Dimension topPanelDimension = new Dimension(800, 80);
	//ServerList serverList = new ServerList();
	Service[] serv = new Service[MAX_SERVER_NUM];
	ThreadList[] threadList = new ThreadList[MAX_SERVER_NUM];
	Thread[] thread = new Thread[MAX_SERVER_NUM];
	
	//String[] connections = {"Proxy Server 1", "Proxy Server 2", "Proxy Server 3", "Proxy Server 4"};
	
	String[] clientsConnection = {"From [client 1: 192.168.0.50:22] To [Robot 1: 192.168.0.11:6789]", 
					   "From [client 2: 192.168.0.51:22] To [Robot 1: 192.168.0.12:6789]", 
					   "From [client 3: 192.168.0.52:22] To [Robot 1: 192.168.0.13:1234]",
					   "From [client 4: 192.168.0.53:22] To [Robot 1: 192.168.0.14:2345]",
					   "From [client 5: 192.168.0.54:22] To [Robot 1: 192.168.0.15:2345]"};
	
	public FrontPanel()
    {
       setLayout(new BorderLayout());
          
       TopPanel topPanel = new TopPanel();
       bodyPane = new JScrollPane(new BodyPanel());   
    	
       add(topPanel, BorderLayout.NORTH);
       add(bodyPane, BorderLayout.CENTER);
    }
	
	//build the north region for front panel
    class TopPanel extends JPanel
    {
       private TopPanel()
       {
          setPreferredSize(topPanelDimension);

          topLabel = new JLabel();
          topLabel.setText("Proxy Server Manager");
          topLabel .setFont(new Font(topLabel.getName(), Font.PLAIN, 30));
          
          addServerJButton = new JButton("Add a New Server");          
          addServerJButton.addActionListener(new ButtonListener());
         
          topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          addServerJButton.setAlignmentX(Component.CENTER_ALIGNMENT);
          this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
          
          add(topLabel);
          add(addServerJButton);
       }
    }
    
  //build the center region for front panel
    class BodyPanel extends JPanel
    {
       private BodyPanel()
       {
          setPreferredSize(new Dimension(800, 580));  
       }
    }
    
    private JPanel addServerPanel()
    {
       JPanel tempJPanel = new JPanel();             
       tempJPanel.setLayout(new fitWidthFlowLayout());
       
       if(debug)
       {
	       System.out.println("addServerPanel() -> " + serverIp + ":" + serverPort);
	       System.out.println("addServerPanel() -> " + robotIp + ":" + robotPort);
       }
       
       //check if the server is more than the MAX_SERVER_NUM
       if((serverFlag = addServer()) != -1)
       {
    	   buildOneServerPanel(serverFlag);
    	   serverFlag = -1;
       }
  
       //cleanPanel(tempJPanel);
       for(int i = 0; i < MAX_SERVER_NUM; i++)
       {
    	   if(serv[i] != null)
    		   tempJPanel.add(serverPanelList[i]);
       }
       
       return tempJPanel;
    }
    
    private JPanel deleteServerPanel(int index)
    {
    	JPanel tempJPanel = new JPanel();             
        tempJPanel.setLayout(new fitWidthFlowLayout());
        
        serv[index] = null;
        
        for(int i = 0; i < MAX_SERVER_NUM; i++)
        {
     	   if(serv[i] != null)
     		   tempJPanel.add(serverPanelList[i]);
        }
        
        return tempJPanel;
    }
    
    
	private void buildOneServerPanel(int index)
	{
		serverPanelList[index] = new JPanel();
		
		//server's title
	    serverTitle[index] = new JLabel();    
	    serverTitle[index].setText("Proxy Server " + Integer.toString(index + 1));
	    serverTitle[index].setFont(new Font(topLabel.getName(), Font.PLAIN, 20));
	    //serverTitle[index].setBounds(0, 20, 300, 80);
	    serverTitle[index].setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    serverImage[index] = new JLabel((new ImageIcon("chilun-xx.gif")));
	    serverImage[index].setAlignmentX(Component.CENTER_ALIGNMENT);
	   
	    //disconnect buttons
	    serverButtonList[index] = new JButton("Disconnect");
	    serverButtonList[index].addActionListener(new ButtonListener());
	    serverButtonList[index].setAlignmentX(Component.CENTER_ALIGNMENT);	   
	    serverPanelList[index].setLayout(new BoxLayout(serverPanelList[index], BoxLayout.PAGE_AXIS));
	   
	    serverTextArea[index] = new JTextArea();
	    
	    for(int row = 0; row < MAX_CLIENT_NUM; row++)
	    {
		    temp += "<<   " + clientsConnection[row] + "   >>" + "\n";
	    }
	    serverTextArea[index].setText(temp);
	    temp = "";
	    serverTextArea[index].setEditable(false);
	    serverTextArea[index].setBackground(new Color(238, 238, 238));
  
	    serverPanelList[index].add(serverTitle[index]);
	    serverPanelList[index].add(serverImage[index]);
	    serverPanelList[index].add(serverButtonList[index]);
	    serverPanelList[index].add(serverTextArea[index]);
	}
	
	private void popServerDialog()
	{
    	JTextField fieldServerIp = new JTextField("192.168.0.11");
    	JTextField fieldServerPort = new JTextField("6789");
    	JTextField fieldRobotIp = new JTextField("192.168.0.55");
    	JTextField fieldRobotPort = new JTextField("22");
    	
    	Object[] message = {"Server IP:", fieldServerIp, "Server Port:", fieldServerPort, 
    						"Target IP:", fieldRobotIp, "Target Port:", fieldRobotPort}; 	
    	int choice = JOptionPane.showConfirmDialog(null, message, "Add a Server", JOptionPane.OK_CANCEL_OPTION);
    	if(choice == JOptionPane.OK_OPTION)
    	{
    		serverIp = fieldServerIp.getText();
    		serverPort = Integer.parseInt(fieldServerPort.getText());
    		robotIp = fieldRobotIp.getText();
    		robotPort = Integer.parseInt(fieldRobotPort.getText());
    		temp += "=========   Server " + serverIp + ":" + serverPort + "  ---->  " + robotIp + ": " + 
    				robotPort + " is running   =========\n";
    	}
    	else
    	{
    		serverIp = "123";
    	}
	}
  
   
    private class ButtonListener implements ActionListener 
    {
       public void actionPerformed(ActionEvent event) 
       {
          //respond model button clicks with the top region's changes
          if(event.getSource() == addServerJButton)
          {
         	  popServerDialog();
        	  //System.out.println("-------------------------------" + getServerIp() + "***********");
        	  if(serverIp.matches(IPADDRESS_PATTERN) && robotIp.matches(IPADDRESS_PATTERN))
        	  {  		  
        		  bodyPane.setViewportView(addServerPanel());
        		  //bodyPane.setViewportView(addServer(connections, clientsConnection));
        	  }
        	  else
        	  {
        		  JOptionPane.showMessageDialog(null, "Please input correct IP and Port!");
        	  }
        		  
          }
          for(int index = 0; index < MAX_SERVER_NUM; index++)
          {
        	  //serverButtonList matching disconnect button of a server
	          if(event.getSource() == serverButtonList[index])
	          {
	        	  if(debug)
	        		  System.out.println("disconnect button!!");
	        	  serv[index].stopService();
	        	  serv[index] = null;
	        	  //thread[index].getThreadGroup().destroy();
	        	  thread[index].stop();
	        	  thread[index] = null;
	        	  
	        	  bodyPane.setViewportView(deleteServerPanel(index));
	        	
	          }
          }
       }
    }
    
    private int addServer()
    {
    	int pos = checkAvailablePos();
    	
    	if(pos != -1)
    	{
	    	try {
				serv[pos] = new Service(InetAddress.getByName(serverIp), serverPort, 
										InetAddress.getByName(robotIp), robotPort);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	    			
	        thread[pos] = new Thread(serv[pos]);
	        thread[pos].start();
    	}
    	
    	return pos;
    }
    
    private int checkAvailablePos()
    {
    	for(int i = 0; i < MAX_SERVER_NUM; i++)
    	{
    		if(serv[i] == null)
    			return i;
    	}
    	
    	return -1;
    }
    
    public int addServerPosition(Service s)
	{
		int position = -10;
		
		for(int i = 0; i < MAX_SERVER_NUM; i++)
		{
			if(serv[i].getId() != -1)
			{
				i++;
			}
			else
			{
				serv[i] = s;
				serv[i].setId(i);
				position = i;
				return position;
			}
		}
		
		return position;
	}
    
    public int deleteServerPosition(int pos)
	{
		int finished = -1;
		
		if(pos >= 0 && pos < MAX_SERVER_NUM)
		{
			serv[pos] = null;
			finished = 1;
		}
	
		return finished;
	}
    
    public int deleteServerPosition(Service s)
	{
		int position = -10;
		
		for(int i = 0; i < MAX_SERVER_NUM; i++)
		{
			if(serv[i].getId() != s.getId())
			{
				i++;
			}
			else
			{
				serv[i] = null;
				position = i;
				return position;
			}
		}
		
		return position;
	}
        
    private void addClient()
	{
		
	}
	
	private String getServerIp()
	{
		return this.serverIp;
	}
	
	private int getServerPort()
	{
		return this.serverPort;
	}
	
	private String getRobotIp()
	{
		return this.robotIp;
	}
	
	private int getRobotPort()
	{
		return this.robotPort;
	}
    
	//modify the flowlayout so let the panels in the center region
   	//change lines following the size changes of the center region
      private class fitWidthFlowLayout extends FlowLayout 
      { 
         public fitWidthFlowLayout() 
         { 
            super(); 
         } 
         public Dimension preferredLayoutSize(Container target) 
         { 
            return computeSize(target); 
         } 
         private Dimension computeSize(Container target) 
         { 
            synchronized(target.getTreeLock()) 
            { 
               int hgap = getHgap(); 
               int vgap = getVgap(); 
               int tempWidth = target.getWidth(); 
               
               if (tempWidth == 0) 
               { 
                  tempWidth = Integer.MAX_VALUE; 
               }
               Insets insets = target.getInsets();
               if (insets == null)	
               { 
                  insets = new Insets(0, 0, 0, 0); 
               } 
               int reqdWidth = 0;
               int rowHeight = 0; 
               int maxwidth = tempWidth - (insets.left + insets.right + hgap * 2); 
               int nmembers = target.getComponentCount(); 
               Dimension dim = new Dimension(0, 0);
               dim.height = insets.top + vgap;
             
               for (int i = 0; i < nmembers; i++) 
               { 
                  Component m = target.getComponent(i); 
                  if (m.isVisible()) 
                  { 
                     Dimension d = m.getPreferredSize(); 
                     if ((dim.width == 0) || ((dim.width + d.width) <= maxwidth)) 
                     { 
                        if (dim.width > 0) 
                        { 
                           dim.width += hgap; 
                        } 
                        dim.width += d.width; 
                        rowHeight = Math.max(rowHeight, d.height);
                     } 
                     else 
                     { 
                        dim.width = d.width; 
                        dim.height += vgap + rowHeight; 
                        rowHeight = d.height; 
                     } 
                     reqdWidth = Math.max(reqdWidth, dim.width); 
                  }
               } 
               dim.height += rowHeight; 
               dim.height += insets.bottom;
               return dim;
            }
         }   
      }

     
//  //clean components located on the panel
//    private void cleanPanel(JPanel panel)
//    {
//       panel.removeAll();
//       panel.revalidate();
//       panel.repaint();
//    }
}
