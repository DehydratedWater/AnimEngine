package toolBox;

import supportingStructures.Vector2D;

public class vectorTools {
	public static float multiplyVectors(Vector2D v1, Vector2D v2)
	{
		return (v1.a*v1.b+v2.a*v2.b);
	}
	
	public static float lenghtVector(Vector2D v)
	{
		return (float) Math.sqrt(v.a*v.a+v.b*v.b);
	}
	public static float getCosOfAngleBetwenTwoVectors(Vector2D v1, Vector2D v2)
	{
		return multiplyVectors(v1, v2)/(lenghtVector(v1)*lenghtVector(v2));
	}
	public static float getAngleBetwenVectors(Vector2D v1, Vector2D v2)
	{
		return (float) Math.acos(getCosOfAngleBetwenTwoVectors(v1, v2))*57.29577951308f;
	}
	public static int sideOfPointWithLine(float x1, float y1, float x2, float y2, float x3, float y3)
	{
	    float dot = (y2-y1)*(x2-x3) + (x1-x2)*(y2-y3);
	    if (dot == 0) return 0;
	    if (dot < 0) return -1;
	    return 1;
	}
}
