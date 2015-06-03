package storyTime;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.*;

public class MainMenu{
	
	public PrintWriter writer;
	public JLayeredPane pane;
	public JPanel playArea;
	public JLabel bButton, cButton, background;
	public Story game;
	public int a, b, c;
	
	public MainMenu(JFrame frame) throws FileNotFoundException{
		writer = new PrintWriter("save");
		pane = new JLayeredPane();
		bButton = new JLabel(); cButton = new JLabel(); background = new JLabel();
		a = b = c = 0;
		frame.add(pane);
	}
	
	public void display(JFrame frame){
		Dimension size;
		pane.add(background, new Integer(1), 0);
		background.setIcon(new ImageIcon("resources/mainmenu/pictures/background.png"));
		size = background.getPreferredSize();
		background.setBounds(0,0,size.width,size.height);
		
		bButton.setIcon(new ImageIcon("resources/mainmenu/pictures/bButton_default.png"));
		bButton.setBounds(300,300,200,100);
		pane.add(bButton, new Integer(2), 0);
		bButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent m) {
				try {
					bButton.setVisible(false); cButton.setVisible(false); background.setVisible(false);
					pane.removeAll();
					refresh(frame);
					begin(frame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			public void mouseEntered(MouseEvent m){
				bButton.setIcon(new ImageIcon("resources/mainmenu/pictures/bButton_highlighted.png"));
			}
			public void mouseExited(MouseEvent m){
				bButton.setIcon(new ImageIcon("resources/mainmenu/pictures/bButton_default.png"));
			}
			public void mousePressed(MouseEvent m){
				bButton.setIcon(new ImageIcon("resources/mainmenu/pictures/bButton_pressed.png"));
			}
			public void mouseReleased(MouseEvent m){
				bButton.setIcon(new ImageIcon("resources/mainmenu/pictures/bButton_default.png"));
			}
		});
		
		File f = new File("resources/save");
		if(f.exists() && !f.isDirectory()){
			cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_default.png"));
			size = cButton.getPreferredSize();
			cButton.setBounds(300,400,size.width,size.height);
			pane.add(cButton, new Integer(2),0);
			cButton.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent m) {
					try {
						Scanner s = new Scanner(new File("resources/save"));
						a = s.nextInt(); b = s.nextInt(); c = s.nextInt();
						System.out.println(a+" "+b+" "+c);
						s.close();
						pane.removeAll();
						refresh(frame);
						cont(frame);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				public void mouseEntered(MouseEvent m){
					cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_highlighted.png"));
				}
				public void mouseExited(MouseEvent m){
					cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_default.png"));
				}
				public void mousePressed(MouseEvent m){
					cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_pressed.png"));
				}
				public void mouseReleased(MouseEvent m){
					cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_default.png"));
				}
			});
		}
		else{
			cButton.setIcon(new ImageIcon("resources/mainmenu/pictures/cButton_disabled.png"));
			size = bButton.getPreferredSize();
			cButton.setBounds(300,425,size.width,size.height);
			pane.add(cButton, new Integer(2),0);
		}
		refresh(frame);
	}

	public void begin(JFrame frame) throws IOException{
		game = new Story(frame, pane);
		game.addStuff(frame, pane);
		game.setStuff(frame, pane);
	}

	public void cont(JFrame frame) throws IOException{
		game = new Story(frame, pane, a, b, c);
		game.addStuff(frame,pane);
		game.setSave(frame, pane);
		refresh(frame);
	}
	
	public void write() throws IOException{
		String save = game.getSave();
		BufferedWriter writer = Files.newBufferedWriter(Paths.get("resources/save"), StandardCharsets.UTF_8);
		writer.write(save);
		writer.close();
	}
	
	public static void refresh(JFrame frame){
		frame.invalidate();
		frame.validate();
	}
}