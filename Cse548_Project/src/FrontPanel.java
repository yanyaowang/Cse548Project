import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.awt.event.*;

import javax.swing.*;

class FrontPanel extends JPanel
{
	public static final int MAX_SERVER_NUM = 4;
	public static final int MAX_CLIENT_NUM = 5;
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
	ArrayList<SrvNode> srvNodeList = new ArrayList<SrvNode>();
	JButton addServerJButton;
	JLabel topLabel;
	JScrollPane bodyPane; 
	JLabel[] serverTitle = new JLabel[MAX_SERVER_NUM];
	JTextArea[] serverTextArea = new JTextArea[MAX_SERVER_NUM];
	JButton[] serverButtonList =new JButton[MAX_SERVER_NUM];
	//JPanel[] serverDetail = new JPanel[MAX_SERVER_NUM];
	JPanel[] serverPanelList = new JPanel[MAX_SERVER_NUM];
	Dimension topPanelDimension = new Dimension(800, 80);
	ServerList serverList = new ServerList();
	//Service[] serv = new Service[MAX_SERVER_NUM];
	Thread[] thread = new Thread[MAX_SERVER_NUM];
	
	String[] connections = {"Proxy Server 1", "Proxy Server 2", "Proxy Server 3", "Proxy Server 4"};
	
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
          JPanel panel = new JPanel();
          topLabel = new JLabel();
          //topLabel.setText("Proxy Server");
          topLabel.setText("<html><h1>Proxy Server</h1></html>");
          topLabel.setBounds(0, 20, 200, 50);
          
          addServerJButton = new JButton("Add a New Server");          
          addServerJButton.addActionListener(new ButtonListener());
          
          panel.add(topLabel);
          panel.add(addServerJButton);
          add(panel);
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
    
    private JPanel addServer()
    {
       JPanel tempJPanel = new JPanel();             
       tempJPanel.setLayout(new fitWidthFlowLayout());
       
       System.out.println(serverIp);
       System.out.println(serverPort);
       System.out.println(robotIp);
       System.out.println(robotPort);
       
       try
       {
    	   serverList.addServer(new Service
    	      (InetAddress.getByName(serverIp), serverPort, InetAddress.getByName(robotIp), robotPort));
       //serv[0] = new Service(InetAddress.getByName(serverIp), serverPort, InetAddress.getByName(robotIp), robotPort);
       thread[0] = new Thread(serv[0]);
       thread[0].start();
       }catch(Exception e){
    	   e.printStackTrace();
       }
       
       serverPanelList[0] = new JPanel();
	   serverTitle[0] = new JLabel();
	   
	   serverTitle[0].setText("Proxy Server " + Integer.toString(0 + 1));
	   serverTitle[0].setBounds(0, 20, 200, 50);
	   serverTitle[0].setAlignmentX(Component.CENTER_ALIGNMENT);
	   
	   //disconnect buttons
	   serverButtonList[0] = new JButton("Disconnect");
	   serverButtonList[0].addActionListener(new ButtonListener());
	   serverButtonList[0].setAlignmentX(Component.CENTER_ALIGNMENT);
	   
	   serverPanelList[0].setLayout(new BoxLayout(serverPanelList[0], BoxLayout.PAGE_AXIS));
	   
	   serverTextArea[0] = new JTextArea();
	   for(int row = 0; row < MAX_CLIENT_NUM; row++)
	   {
		   temp += "<<   " + clientsConnection[row] + "   >>" + "\n";
	   }
	   serverTextArea[0].setText(temp);
	   temp = "";
	   serverTextArea[0].setEditable(false);
	   serverTextArea[0].setBackground(new Color(238, 238, 238));
  
	   //serverDetail[0].add(serverTextArea[0]);
	   //styleJPanel[index].add(images[index]);  
  
	   serverPanelList[0].add(serverTitle[0]);
	   serverPanelList[0].add(serverButtonList[0]);
	   serverPanelList[0].add(serverTextArea[0]);
  
	   tempJPanel.add(serverPanelList[0]);
            
       return tempJPanel;
    }
    
    private JPanel addServer(String[] connections, String[] status)
    {
       JPanel tempJPanel = new JPanel();
       //serverPanelList = new JPanel[];
       //serverTitle = new JLabel[connections.length];
       //serverButtonList = new JButton[connections.length];
       //serverTextArea = new JTextArea[status.length];
       //serverDetail = new JPanel[connections.length];
       //images = new JLabel[string.length];
       //product = new Product[string.length];
       
       tempJPanel.setLayout(new fitWidthFlowLayout());
       
       for(int index = 0; index < MAX_SERVER_NUM; index++)
       {
    	   //panel for the added server 
    	   serverPanelList[index] = new JPanel();
    	   serverTitle[index] = new JLabel();
    	   //serverPanelList[index].setSize(800, 250);
    	   //serverPanelList[index].setBackground(new Color(238, 238, 238));
    	   
    	   serverTitle[index].setText("Proxy Server " + Integer.toString(index + 1));
    	   serverTitle[index].setBounds(0, 20, 200, 50);
    	   serverTitle[index].setAlignmentX(Component.CENTER_ALIGNMENT);
    	   
    	   //disconnect buttons
    	   serverButtonList[index] = new JButton("Disconnect");
    	   serverButtonList[index].addActionListener(new ButtonListener());
    	   serverButtonList[index].setAlignmentX(Component.CENTER_ALIGNMENT);
	 
    	   //clients connected on the server
    	   //serverDetail[index] = new JPanel();
    	   //product[index] = new Product(modelIndex, index);
    	   //images[index] = new JLabel((new ImageIcon (Product.STYLE_PICTURE[modelIndex][index])));
         
    	   
    	   //images[index].setAlignmentX(Component.CENTER_ALIGNMENT);
    	   serverPanelList[index].setLayout(new BoxLayout(serverPanelList[index], BoxLayout.PAGE_AXIS));
	  
    	   serverTextArea[index] = new JTextArea();
    	   for(int row = 0; row < MAX_CLIENT_NUM; row++)
    	   {
    		   temp += status[row] + "\n";
    	   }
    	   serverTextArea[index].setText(temp);
    	   temp = "";
    	   serverTextArea[index].setEditable(false);
    	   serverTextArea[index].setBackground(new Color(238, 238, 238));
      
    	   //serverDetail[index].add(serverTextArea[index]);
    	   //styleJPanel[index].add(images[index]);  
	  
    	   serverPanelList[index].add(serverTitle[index]);
    	   serverPanelList[index].add(serverButtonList[index]);
    	   serverPanelList[index].add(serverTextArea[index]);
	  
    	   tempJPanel.add(serverPanelList[index]);
            
       }
       return tempJPanel;
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
        		  bodyPane.setViewportView(addServer());
        		  //bodyPane.setViewportView(addServer(connections, clientsConnection));
        	  }
        	  else
        	  {
        		  JOptionPane.showMessageDialog(null, "Please input correct IP and Port!");
        	  }
        		  
        	  //addMultiInputDialog();  
            //modelIndex = index;
            //cleanPanel(bodyPane);
            //styleList = twoToOneDimensionArray(index, Product.STYLE_TYPE);
            //bodyPane.setViewportView(stylePanelList(styleList, index));
          }
          if(event.getSource() == serverButtonList[0])
          {
        	  System.out.println("disconnect button!!");
        	  serv[0].stopService();
        	  serv[0] = null;
        	  thread[0] = null;
          }
          
       }
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

     
  //clean components located on the panel
    private void cleanPanel(JPanel panel)
    {
       panel.removeAll();
       panel.revalidate();
       panel.repaint();
    }
}




/*
//show the combined style panels which will be located in the center region  
private JPanel stylePanelList(String[] string, int modelIndex)
{
   JPanel tempJPanel = new JPanel();
   styleJPanel = new JPanel[string.length];
   styleJButton = new JButton[string.length];
   text = new JTextArea[string.length];
   description = new JPanel[string.length];
   images = new JLabel[string.length];
   product = new Product[string.length];
   
   tempJPanel.setLayout(new fitWidthFlowLayout());
   for(int index = 0; index < string.length; index++)
   {
      styleJPanel[index] = new JPanel();
      styleJButton[index] = new JButton("Order");
      styleJButton[index].addActionListener(new ButtonListener());
      text[index] = new JTextArea(); 
      description[index] = new JPanel();
      product[index] = new Product(modelIndex, index);
      images[index] = new JLabel((new ImageIcon (Product.STYLE_PICTURE[modelIndex][index])));
         
      styleJButton[index].setAlignmentX(Component.CENTER_ALIGNMENT);
      images[index].setAlignmentX(Component.CENTER_ALIGNMENT);
      styleJPanel[index].setLayout(new BoxLayout(styleJPanel[index], BoxLayout.PAGE_AXIS));
      text[index].setText(product[index].toString());
      text[index].setEditable(false);
      text[index].setBackground(new Color(238, 238, 238));
      
      description[index].add(text[index]);
      styleJPanel[index].add(images[index]);  
      styleJPanel[index].add(styleJButton[index]);
      styleJPanel[index].add(description[index]);
      tempJPanel.add(styleJPanel[index]);
   }     
   return tempJPanel;
}

private void addServer()
    {
    	String serverIP;
    	String serverPort;
    	String robotIP;
    	String robotPort;
    	int again = 0;
    	
    	do
    	{
    		JOptionPane.showInputDialog("Server IP:");
        	JOptionPane.showInputDialog("Server Port:");
        	JOptionPane.showInputDialog("Robot IP:");
        	JOptionPane.showInputDialog("Robot Port:");
    	}
    	while(again == JOptionPane.YES_OPTION);
    }

//show the combined style panels which will be located in the center region  
private JPanel stylePanelList(String[] string, int modelIndex)
{
   JPanel tempJPanel = new JPanel();
   styleJPanel = new JPanel[string.length];
   styleJButton = new JButton[string.length];
   text = new JTextArea[string.length];
   description = new JPanel[string.length];
   images = new JLabel[string.length];
   product = new Product[string.length];
   
   tempJPanel.setLayout(new fitWidthFlowLayout());
   for(int index = 0; index < string.length; index++)
   {
      styleJPanel[index] = new JPanel();
      styleJButton[index] = new JButton("Order");
      styleJButton[index].addActionListener(new ButtonListener());
      text[index] = new JTextArea(); 
      description[index] = new JPanel();
      product[index] = new Product(modelIndex, index);
      images[index] = new JLabel((new ImageIcon (Product.STYLE_PICTURE[modelIndex][index])));
         
      styleJButton[index].setAlignmentX(Component.CENTER_ALIGNMENT);
      images[index].setAlignmentX(Component.CENTER_ALIGNMENT);
      styleJPanel[index].setLayout(new BoxLayout(styleJPanel[index], BoxLayout.PAGE_AXIS));
      text[index].setText(product[index].toString());
      text[index].setEditable(false);
      text[index].setBackground(new Color(238, 238, 238));
      
      description[index].add(text[index]);
      styleJPanel[index].add(images[index]);  
      styleJPanel[index].add(styleJButton[index]);
      styleJPanel[index].add(description[index]);
      tempJPanel.add(styleJPanel[index]);
   }     
   return tempJPanel;
}

private JPanel stylePanelList(String[] string)
    {
       JPanel tempJPanel = new JPanel();
       serverPanelList = new JPanel[string.length];
       serverButtonList = new JButton[string.length];
       serverTextArea = new JTextArea[string.length];
       serverDetail = new JPanel[string.length];
       //images = new JLabel[string.length];
       //product = new Product[string.length];
       
       tempJPanel.setLayout(new fitWidthFlowLayout());
       for(int index = 0; index < string.length; index++)
       {
    	  serverPanelList[index] = new JPanel();
    	  //serverPanelList[index].setSize(800, 250);
    	  //serverPanelList[index].setBackground(new Color(238, 238, 238));
    	  serverButtonList[index] = new JButton("Disconnect");
    	  serverButtonList[index].addActionListener(new ButtonListener());
    	  serverTextArea[index] = new JTextArea(); 
    	  serverDetail[index] = new JPanel();
          //product[index] = new Product(modelIndex, index);
          //images[index] = new JLabel((new ImageIcon (Product.STYLE_PICTURE[modelIndex][index])));
             
    	  serverButtonList[index].setAlignmentX(Component.CENTER_ALIGNMENT);
          //images[index].setAlignmentX(Component.CENTER_ALIGNMENT);
    	  serverPanelList[index].setLayout(new BoxLayout(serverPanelList[index], BoxLayout.PAGE_AXIS));
    	  serverTextArea[index].setText(string[index].toString());
    	  serverTextArea[index].setEditable(false);
    	  serverTextArea[index].setBackground(new Color(238, 238, 238));
          
    	  serverDetail[index].add(serverTextArea[index]);
          //styleJPanel[index].add(images[index]);  
    	  serverPanelList[index].add(serverDetail[index]);
    	  serverPanelList[index].add(serverButtonList[index]);
    	  
          tempJPanel.add(serverPanelList[index]);
       }     
       return tempJPanel;
    }

*/

