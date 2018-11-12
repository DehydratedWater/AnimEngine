package undo;

public class Step 
{
	private boolean isMovie = false;
	private boolean isFrame = false;
	private boolean isObject = false;
	
	Step(boolean movie, boolean frame, boolean object)
	{
		setMovie(movie);
		setFrame(frame);
		setObject(object);
	}
	
	public Step clone()
	{
		return new Step(isMovie, isFrame, isObject);
	}
	public boolean isFrame() {
		return isFrame;
	}
	public void setFrame(boolean isFrame) {
		this.isFrame = isFrame;
	}
	public boolean isMovie() {
		return isMovie;
	}
	public void setMovie(boolean isMovie) {
		this.isMovie = isMovie;
	}
	public boolean isObject() {
		return isObject;
	}
	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

}
