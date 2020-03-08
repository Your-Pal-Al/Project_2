package pkg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {

	public static void main(String[] args){
		
		//Creates main frame and names it PIMS
		JFrame frame = new JFrame("PIMS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500); //sets size of window to 800 x 500
		
		frame.setResizable(false); //sets frame to NON-resizable	
		
		//changes program icon
		frame.setIconImage(new ImageIcon(MainPanel.class.getResource("/pkg/P.png")).getImage());
		
		//creates mainPanel and adds to mainFrame
		frame.getContentPane().add(new MainPanel());
		
		frame.pack();
		frame.setVisible(true);
	}

}
