	
import java.awt.*;
import javax.swing.*;

public class TestPanel extends JFrame
{
   public static void main(String[] args)
   {
      JFrame frame = new JFrame("Proxy Server");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      FrontPanel fp = new FrontPanel();
      frame.getContentPane().add(fp);
   	
      frame.pack();
      frame.setVisible(true);
   }
}