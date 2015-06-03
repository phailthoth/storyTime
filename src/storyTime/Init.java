package storyTime;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.JFrame;

public class Init extends JFrame{
	private static final long serialVersionUID = -8126222949950769777L;
	
	public static JFrame frame = new JFrame("Top Secrets.");
	
	public static void main(String[] args) throws IOException{
		
		frame.setBounds(350, 150, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close it, close it, close it!
		frame.setResizable(false); // we want it at 800x600, no need to let user change it.
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.black);
		MainMenu menu = new MainMenu(frame);
		menu.display(frame);
		
		frame.addWindowListener(new WindowAdapter(){
  			public void windowClosing(WindowEvent e){
     			try {
     				if(menu.game != null){
     					menu.write();
     				}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
  			}
		});
	}
}