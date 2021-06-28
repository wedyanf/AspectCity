package org.ui;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AspectCityGUI{
	public static void main(String[] args) {
    createWindow();
 }
 private static void createWindow() {    
    JFrame frame = new JFrame("AspectCity");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    createUI(frame);
   // frame.setSize(800, 200);
    frame.pack();
    frame.setLocationRelativeTo(null);  
    frame.setVisible(true);
    frame.setResizable(false);
 }

 private static void createUI(final JFrame frame){
	 
	 ImageIcon img = new ImageIcon("res\\icon.png");
	 frame.setIconImage(img.getImage());

	 frame.setLayout(new BorderLayout());  
     
	 //JLabel background=setBackground(frame, "res\\amman.jpg");  
	 //JPanel background = new ImagePanel("res\\amman.jpg");
	 
	 Image image =   new ImageIcon("res\\back2.jpeg").getImage() ;
     Image temp=image.getScaledInstance(600,300,Image.SCALE_SMOOTH);
     ImageIcon backgroundImage=new ImageIcon(temp);
     JPanel background = new ImagePanel(backgroundImage);
     
     
     frame.add(background,BorderLayout.CENTER);
	 background.setLayout(new FlowLayout());
    JButton button1 = new JButton("Browse...");
    JTextField txtInFolder = new JTextField(30);

    final JLabel label1 = new JLabel("Select bytecode Folder*   ");
    //label1.setForeground(Color.ORANGE);
    txtInFolder.setEnabled(false);
    button1.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          int option = fileChooser.showOpenDialog(frame);
          if(option == JFileChooser.APPROVE_OPTION){
             File file = fileChooser.getSelectedFile();
             txtInFolder.setText(file.getPath());
          }else{
        	  txtInFolder.setText("Open command canceled");
          }
       }
    });
    button1.setBackground(Color.LIGHT_GRAY);
    
    JButton button2 = new JButton("Browse...");
    button2.setBackground(Color.LIGHT_GRAY);
    JTextField txtOutFolder = new JTextField(30);
    final JLabel label2 = new JLabel("Select output Folder*         ");
    txtOutFolder.setEnabled(false);
    button2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           JFileChooser fileChooser = new JFileChooser();
           fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
           int option = fileChooser.showOpenDialog(frame);
           if(option == JFileChooser.APPROVE_OPTION){
              File file = fileChooser.getSelectedFile();
              txtOutFolder.setText(file.getPath());
           }else{
         	  txtOutFolder.setText("Open command canceled");
           }
        }
     });

    JButton button3 = new JButton("Browse..."); button3.setBackground(Color.LIGHT_GRAY);
    JTextField txtSrcFolder = new JTextField(30);
    final JLabel label3 = new JLabel("Select source code Folder");
    txtSrcFolder.setEnabled(false);
    button3.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           JFileChooser fileChooser = new JFileChooser();
           fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
           int option = fileChooser.showOpenDialog(frame);
           if(option == JFileChooser.APPROVE_OPTION){
              File file = fileChooser.getSelectedFile();
              txtSrcFolder.setText(file.getPath());
           }else{
         	  txtSrcFolder.setText("Open command canceled");
           }
        }
     });

    JButton btnRun = new JButton("Run...");btnRun.setBackground(Color.LIGHT_GRAY);
    JButton btnHtml = new JButton("View Produced Html Pages"); btnHtml.setBackground(Color.LIGHT_GRAY);
    JButton btnXml = new JButton("View Produced XML Pages");btnXml.setBackground(Color.LIGHT_GRAY);
    
    String empty="                                                                                                                                                                  ";
    JLabel upper = new JLabel(empty);
    upper.setBackground(new Color(0, 0, 0, 0));
    JLabel upper2 = new JLabel(empty);
    upper2.setBackground(new Color(0, 0, 0, 0));
    
    JLabel upper3 = new JLabel(empty);
    upper3.setBackground(new Color(0, 0, 0, 0));
    
    JLabel upper4 = new JLabel(empty);
    upper4.setBackground(new Color(0, 0, 0, 0));
    
    JLabel left = new JLabel("                                          ");
    left.setBackground(new Color(0, 0, 0, 0));
    
    background.add(upper);
    background.add(upper2);
  //  background.add(upper3);
   // background.add(upper4);
    
    background.add(label1);
    background.add(txtInFolder);
    background.add(button1)    ;

    background.add(label2);
    background.add(txtOutFolder);
    background.add(button2)    ;

    background.add(label3);
    background.add(txtSrcFolder);
    background.add(button3)    ;
    
    background.add(left);
    background.add(btnRun);
    background.add(btnHtml);
    background.add(btnXml)    ;
    

 }  
 public static JLabel setBackground(JFrame frame, String backgroundFilePath)  
 {  
      JLabel background=new JLabel(new ImageIcon(backgroundFilePath));  
      frame.add(background);  
      background.setLayout(new FlowLayout());  
      return background;  
 }  
}

class ImagePanel extends JPanel {

	  private Image img;

	  public ImagePanel(String img) {
		  this(new ImageIcon(img).getImage());
		  
	  }
	  public ImagePanel(ImageIcon img) {
		  this(img.getImage());
	  }
	  public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	  }

	}