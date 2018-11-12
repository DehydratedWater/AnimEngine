package processCapturer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Window;
import renderSource.Object;
import renderSource.ObjectRenderer;
import structures.Frame;
import toolBox.frameCupture;


public class BitmapCapturer 
{
	public static ArrayList<BufferedImage> bitMap = new ArrayList<BufferedImage>();
	
	public static void addBufferImage(BufferedImage bitmap)
	{
		bitMap.add(bitmap);
	}
	public static void renderBitmap(Frame f)
	{
		BufferedImage memory_image = new BufferedImage(Window.windowsWidth, Window.windowHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = memory_image.createGraphics();
		boolean lastPoints = Window.ShowPoints;
		boolean lastConns = Object.drawConnNumber;
		Window.ShowPoints = true;
		Object.drawConnNumber = true;
		ObjectRenderer.drawObjectBETA(g2, f.gco().getRaundPointTab(), f.gco().getPointTab(), f.gco().getConnectionTab(), f.gco().getPolygonTab(), f.gco().getPolygonToDraw(), f.gco().getActualTransformation());
		Window.ShowPoints = lastPoints;
		Object.drawConnNumber = lastConns;
		bitMap.add(memory_image);
	}
	public static void clearList()
	{
		bitMap = new ArrayList<BufferedImage>();
	}
	public static void saveBitmaps()
	{
		for(int i = 0 ; i < bitMap.size(); i++)
		{
			try {
				ImageIO.write(frameCupture.memory_frame, "PNG", new File("logs/"+i+".png"));
			} catch (IOException e) {
				System.out.println("Nie mo¿na zapisaæ klati nr "+i);
				e.printStackTrace();
			}
		}
		clearList();
	}
}
