package openSave;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.*;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SizeSetter {

private static int startSizeX = 1280;
private static int startSizeY = 720;
private static String s;
private static boolean ifReady = false;
private static int max = 6000;
private static float fps = 30;
private static boolean firstUse = true;
JFrame frame1; 



 


 

 
 
public static void setAnimationResolution(){
	JTextField xResolution = new JTextField(5);
    JTextField yResolution = new JTextField(5);
    JTextField frameRate = new JTextField(5);
 

    JPanel myPanel = new JPanel();
    myPanel.add(new JLabel("Animation Weidth:"));
    myPanel.add(xResolution);
    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
    myPanel.add(new JLabel("Animation Height:"));
    myPanel.add(yResolution);
    myPanel.add(Box.createHorizontalStrut(15));
    myPanel.add(new JLabel("FrameRate:"));
    myPanel.add(frameRate);

    yResolution.setText(String.valueOf(startSizeY));
    xResolution.setText(String.valueOf(startSizeX));
    frameRate.setText(String.valueOf(fps));

    int result = JOptionPane.showConfirmDialog(null, myPanel, 
             "Set parameters", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.CANCEL_OPTION){
    	if(firstUse == true){
    	Runtime.getRuntime().exit(0);
    	firstUse = false;
    	}
    	
    	if(firstUse == false)
    		return;
    }
    
    if (result == JOptionPane.OK_OPTION) {
    
    	s = frameRate.getText();
    	
   
    		s.replaceFirst(".", ",");
    			
    
    	
    
    	if(yResolution.getText() == "" || Integer.parseInt(yResolution.getText()) < 1 || Integer.parseInt(yResolution.getText()) > max ){
      		 Component frame = null;
      		 System.out.println(Integer.parseInt(yResolution.getText()));
      			JOptionPane.showMessageDialog(frame,
      				    "Wrong Number",
      				    "Give right value",
      				    JOptionPane.WARNING_MESSAGE);
      		 setAnimationResolution();
      	 }
    	
    	if(frameRate.getText() == "" || Float.parseFloat(s) < 1 || Float.parseFloat(s) > 360){
      		 Component frame = null;
      			JOptionPane.showMessageDialog(frame,
      				    "Wrong Number",
      				    "Give right value",
      				    JOptionPane.WARNING_MESSAGE);
      		 setAnimationResolution();
      	 }
    	if(xResolution.getText() == "" || Integer.parseInt(xResolution.getText()) < 1 || Integer.parseInt(xResolution.getText()) > max){
     		 Component frame = null;
     			JOptionPane.showMessageDialog(frame,
     				    "Wrong Number",
     				    "Give right value",
     				    JOptionPane.WARNING_MESSAGE);
     		 setAnimationResolution();
     	 }
    
    startSizeX = Integer.parseInt(xResolution.getText());
    startSizeY = Integer.parseInt(yResolution.getText());
    fps = Float.parseFloat(s);
    ifReady = true;
    firstUse = false;	
    }
 }


		
public static void setIfReady(boolean x){
	ifReady = x;
}

 public static int getStartSizeX(){
		return startSizeX;
	}
	
 public static int getStartSizeY(){
		return startSizeY;
	}
	
  public static boolean getifReady(){
		return ifReady;
	}
	
 public static float getFrameRate(){
	 return fps;
 }
}

