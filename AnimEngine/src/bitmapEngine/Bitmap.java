package bitmapEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import bitmapEngine.Texture;
import bitmapEngine.textureManager;
import main.Window;

public class Bitmap 
{
	private int bitmapIndex;
	private float angle = 0;
	private float scaleX = 1, scaleY = 1, winScaleX = 1, winScaleY = 1;
	private float X = 50, Y = 50, winX, winY;
	



	/**
	 * Podstawowy konstruktor biutmapy na bazie istniej¹cego indeksu tekstury
	 * @param texIndex
	 */
	public Bitmap(int texIndex)
	{
		setBitmapIndex(texIndex);
	}
	
	public boolean isBitmapClicked(float x, float y)
	{
		float[] xy = scaleAndRotateValue(x, y);
		if(xy[0]>=0&&xy[1]>=0&&xy[0]<=Window.tm.getTexture(bitmapIndex).texture.getWidth()&&xy[1]<=Window.tm.getTexture(bitmapIndex).texture.getHeight())
			return true;
		else
			return false;
	}
	
	/**
	 * Konstruktor który przyjmuje poza indeksem istniej¹cej tekstury równie¿ podstawowe parametry
	 * @param texIndex
	 * @param x
	 * @param y
	 * @param scaleX
	 * @param scaleY
	 * @param angle
	 */
	public Bitmap(int texIndex, float x, float y, float scaleX, float scaleY, float angle)
	{
		setBitmapIndex(texIndex);
		setX(x);
		setY(y);
		setScaleX(scaleX);
		setScaleY(scaleY);
		setAngle(angle);
	}
	/**
	 * Tworzy now¹ teksturê w obiekcie textureManagera i nadaje jej nazwê poza tym powsta³a Bitmapa ma indeks tekstury
	 * @param name
	 * @param path
	 * @param texManager
	 */
	public Bitmap(String name, String path, textureManager texManager)
	{
		setBitmapIndex(texManager.addNewTextureAndGetTextureID(path, name));
	}

/**
 * Generuje now¹ bitmapê o okreœlonym rozmiarze i okreœlonej nazwie
 * @param name
 * @param x
 * @param y
 * @param texManager
 */
	public Bitmap(String name, int x, int y, textureManager texManager)
	{
		setBitmapIndex(texManager.generateNewTextureAndGetTextureID(name, x ,y));
	}
	
	public Bitmap(String name, String path, textureManager texManager, float x, float y, float scaleX, float scaleY, float angle)
	{
		setBitmapIndex(texManager.addNewTextureAndGetTextureID(path, name));
		setX(x);
		setY(y);
		setScaleX(scaleX);
		setScaleY(scaleY);
		setAngle(angle);
	}
	/**
	 * Wyœwietla bitmape na ekranie
	 * @param g
	 * @param texManager
	 */
	public void draw(Graphics2D g, textureManager texManager) 
	{
		//System.out.println("Rysowanie bitmapy pod indexem "+bitmapIndex);
		float ScaleX = scaleX * winScaleX;
		float ScaleY = scaleY * winScaleY;
		float xMov = X * winScaleX + winX;
		float yMov = Y * winScaleY + winY;
		
		Texture tex = texManager.getTexture(bitmapIndex);
		g.translate(xMov+(tex.texture.getWidth()*ScaleX)/2, yMov+(tex.texture.getHeight()*ScaleY)/2);
		g.rotate(Math.toRadians(angle));
		
		//Dodanie Wyœwietlania zarysu zdjêcia
		int minX = (int) (-(tex.texture.getWidth()*ScaleX)/2)-3;
		int minY = (int) (-(tex.texture.getHeight()*ScaleY)/2)-3;
		int maxX = (int)(tex.texture.getWidth()*ScaleX-(tex.texture.getWidth()*ScaleX)/2)+3;
		int maxY = (int)(tex.texture.getHeight()*ScaleY-(tex.texture.getHeight()*ScaleY)/2)+3;
		g.setStroke(new BasicStroke(5));
		g.setColor(new Color(0, 0, 60, 125));
		g.drawLine(minX, minY, maxX, minY);
		g.drawLine(maxX, minY, maxX, maxY);
		g.drawLine(maxX, maxY, minX, maxY);
		g.drawLine(minX, maxY, minX, minY);
		g.drawOval(-2, -2, 4, 4);
		g.setColor(Color.blue);
		g.fillOval(minX-5, minY-5, 10, 10);
		g.fillOval(maxX-5, minY-5, 10, 10);
		g.fillOval(maxX-5, maxY-5, 10, 10);
		g.fillOval(minX-5, maxY-5, 10, 10);
		
		g.drawImage(tex.texture, (int)(-tex.texture.getWidth()*ScaleX)/2, (int)(-tex.texture.getHeight()*ScaleY)/2, (int)(tex.texture.getWidth()*ScaleX), (int)(tex.texture.getHeight()*ScaleY), null);

		g.rotate(Math.toRadians(-angle));
		g.translate(-xMov-(tex.texture.getWidth()*ScaleX)/2, -yMov-(tex.texture.getHeight()*ScaleY)/2);
	}
		
	public float[] onlyScaleValue(float x, float y)
	{
		float[] xy = new float[]{x,y};
		xy = new float[]{(xy[0]/winScaleX)-(winX/winScaleX),(xy[1]/winScaleY)-(winY/winScaleY)};
		return xy;
	}
	
	public float[] scaleAndRotateValue(float x, float y)
	{

		float[] xy = new float[]{x,y};
		Texture tex = Window.tm.getTexture(bitmapIndex);
		xy = new float[]{(xy[0]/winScaleX)-(winX/winScaleX) - X,(xy[1]/winScaleY)-(winY/winScaleY) - Y};
		float[] rotate = new float[]{(tex.texture.getWidth()*scaleX)/2, (tex.texture.getHeight()*scaleY)/2};
		rotate(xy, rotate, -angle);
		scaleReverse(xy, scaleX, scaleY);
		return xy;
	}
	
	
	public float[] translate(float xy[], float x, float y)
	{
		xy[0] += x;
		xy[1] += y;
		return xy;
	}
	public float[] scaleReverse(float xy[], float x, float y)
	{
		xy[0] /= x;
		xy[1] /= y;
		return xy;
	}
	public float[] rotate(float xy[], float rotate[], float angle)
	{
		float nx = (float) ((xy[0]-rotate[0])*Math.cos(Math.toRadians(angle)) - (xy[1]-rotate[1])*Math.sin(Math.toRadians(angle)) + rotate[0]);
		float ny = (float) ((xy[0]-rotate[0])*Math.sin(Math.toRadians(angle)) + (xy[1]-rotate[1])*Math.cos(Math.toRadians(angle)) + rotate[1]);
		xy[0] = nx;
		xy[1] = ny;
		return xy;
	}

	
	
	
	public Bitmap clone()
	{
		return new Bitmap(bitmapIndex, X, Y, scaleX, scaleY, angle);
	}
	public int getBitmapIndex() {
		return bitmapIndex;
	}

	public void setBitmapIndex(int bitmapIndex) {
		this.bitmapIndex = bitmapIndex;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}
	
	public void modY(float y) {
		Y += y;
	}
	public void modX(float x) {
		X += x;
	}
	public void setWinXY(float x, float y)
	{
		this.winX = x;
		this.winY = y;
	}
	
	public float getWinX() {
		return winX;
	}


	public void setWinX(float winX) {
		this.winX = winX;
	}


	public float getWinY() {
		return winY;
	}


	public void setWinY(float winY) {
		this.winY = winY;
	}
	
	public float getWinScaleX() {
		return winScaleX;
	}


	public void setWinScaleX(float winScaleX) {
		this.winScaleX = winScaleX;
	}


	public float getWinScaleY() {
		return winScaleY;
	}


	public void setWinScaleY(float winScaleY) {
		this.winScaleY = winScaleY;
	}
	
	public void setWindowsScale(float scale)
	{
		setWinScaleX(scale);
		setWinScaleY(scale);
	}
	public float[] scaleValue(float x, float y)
	{

		float[] xy = new float[]{x,y};
		xy = new float[]{(xy[0]/winScaleX)-(winX/winScaleX) - X,(xy[1]/winScaleY)-(winY/winScaleY) - Y};
		
		return xy;
	}
	public float[] rotateValue(float[] xy, Graphics2D g)
	{
		Texture tex = Window.tm.getTexture(bitmapIndex);
		float ScaleX = scaleX * winScaleX;
		float ScaleY = scaleY * winScaleY;
		System.out.println(winScaleX+" "+ tex.texture.getWidth()+" "+ (tex.texture.getHeight()));
		float[] rotate = new float[]{(float)(tex.texture.getWidth()*ScaleX)/2, (float)(tex.texture.getHeight()*ScaleY)/2};
		g.fillOval((int)rotate[0]-2, (int)rotate[1]-2, 4, 4);
		rotate(xy, rotate, -angle);
		scaleReverse(xy, scaleX, scaleY);
		return xy;
	}
	public float[] scaleAndRotateValue(float x, float y, Graphics2D g)
	{
		float ScaleX = scaleX * winScaleX;
		float ScaleY = scaleY * winScaleY;
		Texture tex = Window.tm.getTexture(bitmapIndex);
		
		float xMov = X * winScaleX + winX;
		float yMov = Y * winScaleY + winY;
		
		float[] xy = new float[]{x,y};
		float[] rotate = new float[]{xMov +(tex.texture.getWidth()* ScaleX )/2,yMov +(tex.texture.getHeight() * ScaleY)/2};
		xy = rotate(xy, rotate, -angle);
		xy = new float[]{xy[0]/winScaleX-xMov/winScaleX,xy[1]/winScaleY-yMov/winScaleY};
		scaleReverse(xy, scaleX, scaleY);
		
		g.setColor(Color.red);
		g.drawOval((int)rotate[0]-3, (int)rotate[1]-3, 6, 6);
		return xy;
	}
	
}
