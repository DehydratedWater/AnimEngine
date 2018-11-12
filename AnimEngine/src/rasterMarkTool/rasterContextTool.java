package rasterMarkTool;

import java.awt.Graphics2D;

import bitmapEngine.Bitmap;
import bitmapEngine.Texture;
import main.Window;
import mark.Mark;
import mark.dataBox;
import structures.Frame;
import toolBox.tb;

public class rasterContextTool 
{
	private static float range = 5;
	public static int setTool(Frame f, int x, int y, dataBox db, int tool, Mark mark, Graphics2D g)
	{
		Bitmap b = f.gcb();
		Texture t = Window.tm.getTexture(b.getBitmapIndex());
		float xy[] = b.onlyScaleValue(x, y);
		
		if(tb.distanceF(0, 0, xy[0], xy[1])<range)
		{
			mark.indexOfMovedPoint =  0;
			return 1;
		}
		else if(tb.distanceF(t.texture.getWidth(), 0, xy[0], xy[1])<range)
		{
			mark.indexOfMovedPoint =  1;
			return 1;
		}
		else if(tb.distanceF(0, t.texture.getHeight(), xy[0], xy[1])<range)
		{
			mark.indexOfMovedPoint =  2;
			return 1;
		}
		else if(tb.distanceF(t.texture.getWidth(), t.texture.getHeight(), xy[0], xy[1])<range)
		{
			mark.indexOfMovedPoint =  3;
			return 1;
		}
		if(b.isBitmapClicked(x, y))
		{
			System.out.println("Wykryto klikniêcie na teksturê");
			return 0;
		}
		
			

		
		
		
		return 0;
	}
}
