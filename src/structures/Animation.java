package structures;

import java.util.ArrayList;

import pastManager.StateBox;
import polygonEngine.FillManager;
import toolBox.frameCupture;

public class Animation 
{
	ArrayList<Frame> frame = new ArrayList<Frame>();
	private int X, Y;
	private float frameRate = 30;
	private int currentFrame;
	public Animation(int x, int y) 
	{
		this.X = x;
		this.Y = y;
		frame.add(new Frame());
		currentFrame = 0;
		
		StateBox.frameIndex = currentFrame;
	}
	public Animation(int i) 
	{
		currentFrame = 0;
		StateBox.frameIndex = currentFrame;
	}

	public Animation(ArrayList<Frame> f, int x, int y, float fRate, int cFrame) 
	{
		frame = f;
		X = x;
		Y = y;
		frameRate = fRate;
		currentFrame = cFrame;
		StateBox.frameIndex = currentFrame;
	}
	public Animation clone()
	{
		//System.out.println("Kopiowanie listy obiektów "+frame.size());
		ArrayList<Frame> f = new ArrayList<Frame>();
		for(int i = 0; i < frame.size(); i++)
		{
			f.add(frame.get(i).clone());
		}
		return new Animation(f, X, Y, frameRate, currentFrame);
	}
	
	//Je¿eli usunie siê clone() ro powstanie referencja do pocz¹tkowej klatki i ta klatka bêdzie z ni¹ powi¹zana
	public void addLastFrame()
	{
		Frame f = frame.get(frame.size()-1).clone();
		currentFrame = frame.size();
		frame.add(f);
		frameCupture.hasFrame=false;
		StateBox.frameIndex = currentFrame;
	}

	public void removeCurrentFrame()
	{
		if(frame.size()>1)
		{
		frame.remove(currentFrame);
		if(currentFrame>0)
		currentFrame--;
		}
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveToEnd()
	{
		currentFrame = frame.size()-1;
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveTo(int num)
	{
		currentFrame = num;
		if(currentFrame<0)
			currentFrame = 0;
		if(currentFrame>frame.size()-1)
			currentFrame = frame.size()-1;
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveForeword()
	{
		currentFrame++;
		if(currentFrame>frame.size()-1)
			MoveForewordWithNewFrame();
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveForewordWithFrameCopy()
	{
		currentFrame++;
		if(currentFrame>frame.size()-1)
			MoveForewordWithCopyFrame();
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveBack()
	{
		currentFrame--;
		if(currentFrame<0)
			currentFrame = 0;
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public void addFrameInPosition(int num)
	{
		frameCupture.hasFrame = false;
		frame.add(num, new Frame());
		frameCupture.hasFrame=false;
	}
	public void MoveToBegining()
	{
		frameCupture.hasFrame = false;
		currentFrame = 0;
		frameCupture.hasFrame=false;
		
		StateBox.frameIndex = currentFrame;
	}
	public Frame getFrameNum(int index)
	{
		return frame.get(index);
	}
	public Frame getFrame()
	{
		return frame.get(currentFrame);
	}
	public void addFrame()
	{
		frameCupture.hasFrame = false;
		frame.add(new Frame());
		
	}
	public void MoveForewordWithNewFrame()
	{
		FillManager.needToRefresh = true;
		frameCupture.hasFrame = false;
		addFrame();
		currentFrame = frame.size()-1;
		
		StateBox.frameIndex = currentFrame;
	}
	public void MoveForewordWithCopyFrame()
	{
		FillManager.needToRefresh = true;
		frameCupture.hasFrame = false;
		addLastFrame();
		currentFrame = frame.size()-1;
		
		StateBox.frameIndex = currentFrame;
	}
	public void ChangeFrame()
	{
		frameCupture.hasFrame = false;
		currentFrame++;
		if(currentFrame==frame.size())
			currentFrame=0;
		
		StateBox.frameIndex = currentFrame;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public float getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}
	public int getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(int currentFrame) {
		StateBox.frameIndex = currentFrame;
		this.currentFrame = currentFrame;
		frameCupture.hasFrame=false;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getFrameTabSize()
	{
		return frame.size();
	}
	public ArrayList<Frame> getFrameTab()
	{
		return frame;
	}
	
	public void setFrame(Frame f, int index)
	{
		frameCupture.hasFrame = false;
		this.frame.set(index, f);
		
	}
	
	public void setFrameTab(ArrayList<Frame> f)
	{
		frameCupture.hasFrame = false;
		this.frame = f;
	}
	public void addFrame(Frame f)
	{
		frameCupture.hasFrame = false;
		frame.add(f);
	}

}
