package undo;

import java.util.ArrayList;

import structures.Frame;

public class MovieBox 
{
	private ArrayList<Frame> frameTab = new ArrayList<Frame>();

	public ArrayList<Frame> getFrameTab() {
		return frameTab;
	}

	public void setFrameTab(ArrayList<Frame> frameTab) {
		this.frameTab = frameTab;
	}
}
