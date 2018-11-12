package paintManagerTools;

public class vector2D 
{
	public float x, y;
	public vector2D(float X, float Y)
	{
		this.x = X;
		this.y = Y;
	}
	public void normalise()
	{
		float lenght = (float) Math.sqrt(x*x+y*y);
		x = x/lenght;
		y = y/lenght;
	}
	public void multiply(float value)
	{
		x*=value;
		y*=value;
	}
	public float getLenght()
	{
		return (float) Math.sqrt(x*x+y*y);
	}
}
