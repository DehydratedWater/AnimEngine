package openSave;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import structures.Animation;
import toolBox.frameCupture;

public class ImageExport 
{
	private static String path;
	private static String name = SaveAs.getName();
	
	public static void exportToPNG(Animation a)
	{
		
		
		Component frame = null;
		JOptionPane.showMessageDialog(frame,
			    "In a choosen folder will be made new folder that contains all frames as PNG photos",
			    "Choose a folder",
			    JOptionPane.INFORMATION_MESSAGE);
		
		
		choosePath();
	
		
		if(Files.exists(Paths.get(path+"/"+name)) == true){
			JOptionPane.showMessageDialog(frame,
				    "Old folder will be rewrited",
				    "Rewriting",
				    JOptionPane.WARNING_MESSAGE);
			
			System.out.println("usuwanie folderu w celu nadpisania");
			File file = new File(path+"/"+name);        
		    String[] myFiles;      

		     myFiles = file.list();  
		     for (int i=0; i<myFiles.length; i++) {  
		         File myFile = new File(file, myFiles[i]); 
		         myFile.delete();  
		     }  
		file.delete();
		}
		
		System.out.println("tworzenie folderu o nazwie "+name +" w folderze "+path);
		new File(path+"/"+name).mkdir();
		
		frameCupture.isExported = true;
		for(int i = 0; i < a.getFrameTabSize(); i++)
		{
		
			a.setCurrentFrame(i);
			captureObiectToAnimation(a);
			try {
				ImageIO.write(frameCupture.memory_frame, "PNG", new File(path+"/"+name+"/"+name+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		frameCupture.isExported = false;
		frameCupture.hasFrame = false;
	}


	
	private static void choosePath(){
		 JFrame parentFrame = new JFrame();
		 
		 JFileChooser chooser = new JFileChooser();
		 chooser.setDialogTitle("Specify a folder");  
         chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		  
		 int userSelection = chooser.showSaveDialog(parentFrame);
		  
		 System.out.println(userSelection);
			 
			 path =  chooser.getSelectedFile().getAbsolutePath();
			 
			 System.out.println(path);
		 
		 
		}		 
	private static void captureObiectToAnimation(Animation a)
	{
		System.out.println("Tworznie klatki "+a.getX()+" "+a.getY());
		BufferedImage memory_image = new BufferedImage(a.getX(), a.getY(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = memory_image.createGraphics();
		a.getFrame().setDrawWithOutMark(true);
		a.getFrame().setShowPoints(false);
		a.getFrame().setScale(1);
		a.getFrame().setMoveXY(0, 0);
		a.getFrame().DrawFrame(g);
		frameCupture.memory_frame = memory_image;

	}
}
