package renderSource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bitmap 
{
private BufferedImage bitmap;
private float x;
private float y;
private float scalex = 1;
private float scaley = 1;
private float Endscalex = 1;
private float Endscaley = 1;
private float align = 0;

	public Bitmap(BufferedImage BITMAP, float X, float Y)
	{
		this.setBitmap(BITMAP);
		this.setX(X);
		this.setY(Y);
	}
	public Bitmap(String sorce, float X, float Y)
	{
		try {
			this.setBitmap(ImageIO.read(new File(sorce)));
		} catch (IOException e) {
			e.printStackTrace();
			this.setBitmap(null);
		}
		this.setX(X);
		this.setY(Y);
	}
	
	public BufferedImage RenerBitmap()
	{
		if(bitmap!=null)
		{
		if(align<0)
		{
			align = align-(int)(align/360-1)*360;
		}
		float alignN = (float) Math.toRadians(align-(int)(align/90)*90);
		int sizex = 0, sizey = 0;
		if((int)(align/90)%2==0)
		{
		sizex = (int)((Math.cos(alignN)*bitmap.getWidth()*scalex+Math.sin(alignN)*bitmap.getHeight()*scaley));
		sizey = (int)((Math.sin(alignN)*bitmap.getWidth()*scalex+Math.cos(alignN)*bitmap.getHeight()*scaley));
		}
		if((int)(align/90)%2==1)
		{
		sizex = (int)((Math.sin(alignN)*bitmap.getWidth()*scalex+Math.cos(alignN)*bitmap.getHeight()*scaley));
		sizey = (int)((Math.cos(alignN)*bitmap.getWidth()*scalex+Math.sin(alignN)*bitmap.getHeight()*scaley));
		}
		BufferedImage memory_image = new BufferedImage(sizex, sizey, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = memory_image.createGraphics();
		
		g2.translate(sizex/2, sizey/2);
		
		g2.rotate(Math.toRadians(align));
		g2.scale(scalex, scaley);
	
		g2.drawImage(bitmap, (int)(-bitmap.getWidth()/2), (int)(-bitmap.getHeight()/2), null);
	
		g2.translate(-sizex/2, -sizey/2);
		g2.setColor(Color.black);
		return memory_image;
		}
		else
		{
			return null;
		}
	}
	
	public float getX(){
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getScaleX() {
		return scalex;
	}
	public void setScaleX(float scale) {
		this.scalex = scale;
	}
	public float getScaleY() {
		return scaley;
	}
	public void setScaleY(float scale) {
		this.scaley = scale;
	}
	public float getAlign() {
		return align;
	}
	public void setAlign(float align) {
		this.align = align;
	}
	public Image getBitmap() {
		return bitmap;
	}
	public void setBitmap(BufferedImage bitmap) {
		this.bitmap = bitmap;
	}
	public void setXY(float x, float y)
	{
		setX(x);
		setY(y);
	}
	public void setScaleXY(float x, float y)//skalowanie dok³adne
	{
		setScaleX(x);
		setScaleY(y);
	}
	public void setScaleUXY(float xy)
	{
		setScaleX(xy);
		setScaleY(xy);
	}
	
	public float getEndscalex() {
		return Endscalex;
	}
	public void setEndscalex(float endscalex) {
		Endscalex = endscalex;
	}
	public float getEndscaley() {
		return Endscaley;
	}
	public void setEndscaley(float endscaley) {
		Endscaley = endscaley;
	}

}
