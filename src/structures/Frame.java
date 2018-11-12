package structures;

import java.awt.Graphics2D;
import java.util.ArrayList;

import bitmapEngine.Bitmap;
import main.Window;
import pastManager.StateBox;
import renderSource.Object;
import toolBox.TextBox;

public class Frame 
{
private ArrayList<Object> objTab = new ArrayList<Object>();

private ArrayList<Bitmap> bitTab = new ArrayList<Bitmap>();

private ArrayList<frameQuene> queneTab = new ArrayList<frameQuene>();

private float scale = 1;
private float moveX = 0, moveY = 0;
private boolean showPoints = false;
private boolean drawWithOutMark = false;
private int object = 0;

public Frame(Object[] o)
{
	for(int i = 0; i < o.length; i++)
	{
		queneTab.add(new frameQuene(true, objTab.size()));
		objTab.add(o[i]);
	}
}
public Frame()
{
		queneTab.add(new frameQuene(true, 0));
		objTab.add(new Object());
}

//public Frame(ArrayList<Object> oTab, int obj)
//{
//		objTab = oTab;
//		object = obj;
//		
//		StateBox.objIndex = object;
//}

public Frame(ArrayList<Object> oTab, ArrayList<Bitmap> bTab, ArrayList<frameQuene> fTab, int obj)
{
		objTab = oTab;
		bitTab = bTab;
		queneTab = fTab;
		object = obj;
		
		StateBox.objIndex = object;
}
public Frame(int i)
{
	
}

public Frame clone()
{
	//System.out.println("Kopiowanie tablicy obiektów o d³ugoœci "+objTab.size());
	ArrayList<Object> oTab = new ArrayList<Object>(objTab.size());
	ArrayList<Bitmap> bTab = new ArrayList<Bitmap>(bitTab.size());
	ArrayList<frameQuene> fTab = new ArrayList<frameQuene>(queneTab.size());
	for(int i = 0; i < objTab.size(); i++)
	{
		oTab.add(objTab.get(i).clone());
	}
	for(int i = 0; i < bitTab.size(); i++)
	{
		bTab.add(bitTab.get(i).clone());
	}
	for(int i = 0; i < queneTab.size(); i++)
	{
		fTab.add(queneTab.get(i).clone());
	}
	TextBox.addMessage("Sklonowano klatkê zawieraj¹c¹ "+oTab.size()+"obiektów wektorowych, "+bTab.size()+" obiektów bitmapowych ³¹cznie "+fTab.size()+" obieków", 1000);
	return new Frame(oTab, bTab, fTab, object);
}
public void DrawFrame(Graphics2D g2)
{
	System.out.println("Rozpoczynanie rysowania klatki z "+queneTab.size()+" objektami");
	for(int i = 0; i < queneTab.size(); i++)
	{
		System.out.println("Aktualna "+queneTab.get(i).isObject+" "+queneTab.get(i).index);
		if(queneTab.get(i).isObject)
		{
			System.out.println("Rysowanie vektora "+queneTab.get(i).index);
			objTab.get(queneTab.get(i).index).setDrawWithOutMark(drawWithOutMark);
			objTab.get(queneTab.get(i).index).setShowPoints(showPoints);
			objTab.get(queneTab.get(i).index).setXY(moveX, moveY);
			objTab.get(queneTab.get(i).index).setWindowsScale(scale);
			objTab.get(queneTab.get(i).index).renderObjectAsVector(g2);
		}
		else
		{
			System.out.println("Rysowanie bitmapy "+queneTab.get(i).index);
			bitTab.get(queneTab.get(i).index).setWinXY(moveX, moveY);
			bitTab.get(queneTab.get(i).index).setWindowsScale(scale);
			bitTab.get(queneTab.get(i).index).draw(g2, Window.tm);
			
		}
	}
//	for(int i = 0; i < objTab.size(); i++)
//	{
//		objTab.get(i).setDrawWithOutMark(drawWithOutMark);
//		objTab.get(i).setShowPoints(showPoints);
//		objTab.get(i).setXY(moveX, moveY);
//		objTab.get(i).setWindowsScale(scale);
//		objTab.get(i).renderObjectAsVector(g2);
//	}
}

public void setObject(int index, Object ob)
{
	objTab.set(index, ob);
}

public void removeObj(int i)
{
	objTab.remove(i);
}

public void addBitmap(String name, String path)
{
	queneTab.add(new frameQuene(false, bitTab.size()));
	bitTab.add(new Bitmap(name, path, Window.tm));
	object = queneTab.size()-1;
}
public void addBitmap(String name, String path, float x, float y, float scaleX, float scaleY, float angle)
{
	queneTab.add(new frameQuene(false, bitTab.size()));
	bitTab.add(new Bitmap(name, path, Window.tm, x, y, scaleX, scaleY, angle));
	object = queneTab.size()-1;
}

public void addBitmap(String name, int x, int y)
{
	queneTab.add(new frameQuene(false, bitTab.size()));
	bitTab.add(new Bitmap(name, x, y, Window.tm));
	object = queneTab.size()-1;
}
public Object getObj(int i)
{
	return objTab.get(i);
}
public Object gco()
{
//	System.out.println("Pobieranie obiektu nr."+object+" z listy o d³ugoœci "+objTab.size()+" poprzez kolejkê o d³ugoœci "+queneTab.size());
	return objTab.get(queneTab.get(object).index);
}

public Bitmap gcb()
{
	return bitTab.get(queneTab.get(object).index);
}
public int getType(int i)
{
	if(queneTab.get(i).isObject==true)
		return 0;
	else
		return 1;
}
public int gcoType()
{
	if(queneTab.get(object).isObject==true)
		return 0;
	else
		return 1;
}
public void addObj(Object obj)
{
	queneTab.add(new frameQuene(true, objTab.size()));
	objTab.add(obj);
	object = queneTab.size()-1;
}
public ArrayList<Object> getObjTab() {
	return objTab;
}

public void setObjTab(ArrayList<Object> objTab) {
	this.objTab = objTab;
}

public float getScale() {
	return scale;
}

public void setScale(float scale) {
	this.scale = scale;
}

public float getMoveY() {
	return moveY;
}

public void setMoveXY(float moveX, float moveY) {
	this.moveX = moveX;
	this.moveY = moveY;
}

public boolean isShowPoints() {
	return showPoints;
}

public void setShowPoints(boolean showPoints) {
	this.showPoints = showPoints;
}

public int getObject() {
	return object;
}

public void setObject(int object) {
	this.object = object;
	StateBox.objIndex = object;
}
public int getObjTabSize()
{
	return objTab.size();
}
public boolean isDrawWithOutMark() {
	return drawWithOutMark;
}
public void setDrawWithOutMark(boolean drawWithOutMark) {
	this.drawWithOutMark = drawWithOutMark;
}
public ArrayList<Bitmap> getTexTab() {
	return bitTab;
}

public void addBitmap(Bitmap b)
{
	bitTab.add(b);
}
public Bitmap getBitmap(int i)
{
	return bitTab.get(i);
}
public int getTexTabSize() {
	return bitTab.size();
}
public void setTexTab(ArrayList<Bitmap> texTab) {
	this.bitTab = texTab;
}
public ArrayList<frameQuene> getQueneTab() {
	return queneTab;
}

public void addFrameQuene(frameQuene fq) {
	queneTab.add(fq);
}
public frameQuene getFrameQuene(int i)
{
	return queneTab.get(i);
}
public int getQueneTabSize() {
	return queneTab.size();
}
public void setQueneTab(ArrayList<frameQuene> queneTab) {
	this.queneTab = queneTab;
}

}
