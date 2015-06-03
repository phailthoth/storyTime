package storyTime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Story{
	
	public JLabel background, sSpeaker, sText;
	public JPanel click;
	public ArrayList<String> text; public ArrayList<File> images; public ArrayList<Integer> queue;
	public int phase, nText, nBack;
	public MouseListener clicker;
	
	public Story(JFrame frame, JLayeredPane pane) throws IOException{
		background = new JLabel(); sSpeaker = new JLabel(); sText = new JLabel();
		text = new ArrayList<String>(); images = new ArrayList<File>(); queue  = new ArrayList<Integer>();
		click = new JPanel();
		phase = 1; nText = nBack = 0;
		clicker = new MouseAdapter() {
		    public void mouseClicked(MouseEvent m) {
		    	try {
					setStuff(frame, pane);
					refresh(frame);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		};
	}
	
	public Story(JFrame frame, JLayeredPane pane, int p, int ta, int tb) throws IOException{
		background = new JLabel(); sSpeaker = new JLabel(); sText = new JLabel();
		text = new ArrayList<String>(); images = new ArrayList<File>(); queue  = new ArrayList<Integer>();
		click = new JPanel();
		phase = p; nText = ta; nBack = tb;
		clicker = new MouseAdapter() {
		    public void mouseClicked(MouseEvent m) {
		    	try {
					setStuff(frame, pane);
					refresh(frame);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		};
	}
	
	public void addStuff(JFrame frame, JLayeredPane pane) throws IOException{
		Dimension size;
		pane.add(sSpeaker, new Integer(2), 0);
		sSpeaker.setBounds(50, 450, 200, 60);
		
		pane.add(sText, new Integer(2), 0);
		sText.setBounds(120, 480, 520, 60);
		
		sSpeaker.setForeground(Color.white);
		sText.setForeground(Color.white);
		
		pane.add(background, new Integer(1), 0);
		background.setIcon(new ImageIcon("resources/images/blackback.png"));
		size = background.getPreferredSize();
		background.setBounds(0,0,size.width,size.height);
		
		pane.add(click, new Integer(4),0);
		click.setOpaque(false);
		click.addMouseListener(clicker);
		click.setBounds(0, 0, 800, 600);
		
		setFont();
		setArrays();
	}
	
	public void setArrays() throws FileNotFoundException{
		text.clear(); images.clear(); queue.clear();
		
		Scanner scan = new Scanner(new File("resources/text/text"+phase+"/"));
		while(scan.hasNextLine()){
			text.add(scan.nextLine());
		}
		scan.close();
		
		scan = new Scanner(new File("resources/queue/queue"+phase+"/"));
		while(scan.hasNextInt()){
			queue.add(scan.nextInt());
		}
		scan.close();
		
		File f = new File("resources/images/image"+phase+"/");
		FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
               if(name.lastIndexOf('.')>0){
                  int lastIndex = name.lastIndexOf('.');
                  String ext = name.substring(lastIndex);
                  if(ext.equals(".png") || ext.equals(".jpg") || ext.equals(".jpeg")){
                     return true;
                  }
               }
               return false;
            }
         };
		images = new ArrayList<File>(Arrays.asList(f.listFiles(filter)));
	}
	
	public void setStuff(JFrame frame, JLayeredPane pane) throws IOException{
		if(nText >= text.size()){
			setButtons(frame, pane);
		}
		else{
			setText(pane);
			if(nBack < images.size() && nText == queue.get(nBack)){
				setBackground(pane);
			}
		}
	}
	
	public void setText(JLayeredPane pane){
		sSpeaker.setText("<html>"+text.get(nText)+"</html>");
		nText++;
		sText.setText("<html>"+text.get(nText)+"</html>");
		nText++;
	}
	
	public void setBackground(JLayeredPane pane) throws IOException{
		background.setIcon(new ImageIcon(ImageIO.read(images.get(nBack))));
		nBack++;
	}
	
	public void setSave(JFrame frame, JLayeredPane pane) throws IOException{
		if(nText >= 2){
			nText -= 2;
		}
		else{
			nText = 0;
		} 
		setText(pane);
		background.setIcon(new ImageIcon(ImageIO.read(images.get(nBack-1))));
	}
	
	public void setButtons(JFrame frame, JLayeredPane pane) throws FileNotFoundException{
		File check = new File("resources/buttons/buttons"+phase+"/");
		if(check.exists() && !check.isDirectory()){
			Scanner count = new Scanner(new File("resources/buttons/buttons"+phase+"/"));
			int times = count.nextInt(); int height = 600; int width = 800; int space = height/times;
			ArrayList<Integer> phases = new ArrayList<Integer>();
			while(count.hasNextInt()){
				phases.add(count.nextInt());
			}
			count.close();
			
			click.removeMouseListener(clicker);
			pane.remove(click);
			Dimension size;
			JLabel back = new JLabel(new ImageIcon("resources/images/blackback.png"));
			size = back.getPreferredSize();
			back.setBounds(0,0,size.width,size.height);
			pane.add(back, new Integer(5),0);
			
			for(int z = 0; z <= times; z++){
				int hold = z;
				JLabel a = new JLabel();
				pane.add(a, new Integer(6),0);
				a.setIcon(new ImageIcon("resources/choices/choice"+phase+"/"+hold+"_default.png"));
				size = a.getPreferredSize();
				a.setBounds(width/times, (space*(z-1))+((space)/(times*2)), size.width, size.height);
				a.addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent m) {
						final int holder = hold;
						pane.removeAll();
						try {
							phase = phases.get(holder);
							nText = nBack = 0;
							refresh(frame);
							addStuff(frame, pane);
							setStuff(frame, pane);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					public void mouseEntered(MouseEvent m){
						final int holder = hold;
						a.setIcon(new ImageIcon("resources/choices/choice"+phase+"/"+holder+"_highlighted.png"));
					}
					public void mouseExited(MouseEvent m){
						final int holder = hold;
						a.setIcon(new ImageIcon("resources/choices/choice"+phase+"/"+holder+"_default.png"));
					}
					public void mousePressed(MouseEvent m){
						final int holder = hold;
						a.setIcon(new ImageIcon("resources/choices/choice"+phase+"/"+holder+"_pressed.png"));
					}
					public void mouseReleased(MouseEvent m){
						final int holder = hold;
						a.setIcon(new ImageIcon("resources/choices/choice"+phase+"/"+holder+"_default.png"));
					}
				});
			}
		}
		else{
			end(pane);
		}
	}
	
	public void setFont(){
		try {
			Font cali = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Calligraffitti-Regular.ttf"));
			Font meta = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Metamorphous-Regular.ttf"));
		    
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Calligraffitti-Regular.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Metamorphous-Regular.ttf")));
		    cali = cali.deriveFont(25.0f); 
		    meta = meta.deriveFont(15.0f);
		    sSpeaker.setFont(cali); 
		    sText.setFont(meta);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void refresh(JFrame frame){
		frame.invalidate();
		frame.validate();
	}
	
	public void end(JLayeredPane pane){
		pane.remove(click);
		Dimension size;
		JLabel back = new JLabel(new ImageIcon("resources/images/blackback.png"));
		size = back.getPreferredSize();
		back.setBounds(0,0,size.width,size.height);
		pane.add(back, new Integer(5),0);
	}
	
	public String getSave(){
		return phase+" "+nText+" "+nBack;
	}
}