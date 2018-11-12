package toolBox;


public final class tb {
	
	
	public static final float[] linesInsertionSimple(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
	{
		float x = (x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4);
		float y = (x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4);
		float d = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
		if(d==0)
			return null;
		else
			return new float[]{x/d,y/d};
	}
	public static final boolean isBBinsertion(float t1[], float t2[])
	{
		if(!(t1[0]>=t2[2]||t1[2]<=t2[0]||t1[1]>=t2[3]||t1[3]<=t2[1]))
		{
			return true;
		}
		return false;
	}
	public static final boolean isPointBetwenOrOn(float[] range, float x, float y)
	{
		if(x>=range[0]&&x<=range[2]&&y>=range[1]&&y<=range[3])
		{
			return true;
		}
		return false;
	}
	

	public static final boolean isPointBetwenOrOnR(float[] range, float x, float y, float r)
	{
		if(x>=range[0]-r&&x<=range[2]+r&&y>=range[1]-r&&y<=range[3]+r)
		{
			return true;
		}
		return false;
	}
	public static final boolean isVerticalLineOrOn(float[] range, float x, float y)
	{
		if(x==range[0]&&x==range[2]&&y>=range[1]&&y<=range[3])
		{
			return true;
		}
		return false;
	}
	public static final boolean isPointInside(float t1[], float t2[], float x, float y, float range)
	{
		float minX, maxX, minY, maxY;
		if(t1[0]>t2[0])
		{
			minX = t2[0];
			maxX = t1[0];
		}
		else
		{
			minX = t1[0];
			maxX = t2[0];
		}
		if(t1[1]>t2[1])
		{
			minY = t2[1];
			maxY = t1[1];
		}
		else
		{
			minY = t1[1];
			maxY = t2[1];
		}
		if(x>(minX-range)&&x<(maxX+range)&&y>(minY-range)&&y<(maxY+range))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public static final float[] generateMinMax(float x1, float y1, float x2, float y2)
	{
		float minX, maxX, minY, maxY;
		if(x1>x2)
		{
			minX = x2;
			maxX = x1;
		}
		else
		{
			minX = x1;
			maxX = x2;
		}
		if(y1>y2)
		{
			minY = y2;
			maxY = y1;
		}
		else
		{
			minY = y1;
			maxY = y2;
		}
		return new float[]{minX, minY, maxX, maxY};
	}
	
	public static final float[] corossPointWithLine(float x1, float y1, float x2, float y2, float x, float y)
	{
		float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(x1, y1, x2, y2), x, y);
		return t3;
	}
	
	public static final float getDistToLine(float A, float B, float C, int x, int y)
	{
		return (float) ((Math.abs(A*x+B*y+C))/(Math.sqrt(A*A+B*B)));
	}
	
	public static final float[] corossPointOfTwoLines(float t1[], float t2[])
	{
		
		//System.out.println("Szukanie punktu przeciêcia ze wzorami "+t1[0]+" "+t1[1]+" "+ t1[2]+" i "+ t2[0]+" "+t2[1]+" "+ t2[2]);
		float t3[] = getCrossOfLine(t1[0], t1[2], t2[0], t2[2]);
		if(t3!=null)
		{
		if((isVertical(t1)||isVertical(t2))==false)
		{
			//System.out.println("ROZPOCZ");
			
			if(t1[0]==0)
				t3[1] = t1[2];
			if(t2[0]==0)
				t3[1] = t2[2];
			//System.out.println("Zwracanie podstawowego wzoru punkt:  "+t3[0]+" "+t3[1]);
			return t3;
		}
		else
		{
			
			if(isVertical(t1)&&isVertical(t2))
			{
				return null;
			}
			else if(isVertical(t1))
			{
				//System.out.println("Pierwszy wzór");
				t3 = new float[2];
				t3[0] = t1[1];
				t3[1] = t2[0]*t1[1]+t2[2];
				return t3;
			}
			else if(isVertical(t2))
			{
				//System.out.println("Drugi wzór");
				t3 = new float[2];
				t3[0] = t2[1];
				t3[1] = t1[0]*t2[1]+t1[2];
				return t3;
			}
		}
			
		}
		else
		{
			if(t1[0]==t2[0]&&t1[1]==t2[1]&&t1[2]==t2[2])
				return null;
			else
			{
				if(isVertical(t1))
				{
					//System.out.println("Pierwszy wzór");
					t3 = new float[2];
					t3[0] = t1[1];
					t3[1] = t2[0]*t1[1]+t2[2];
					return t3;
				}
				else if(isVertical(t2))
				{
					//System.out.println("Drugi wzór");
					t3 = new float[2];
					t3[0] = t2[1];
					t3[1] = t1[0]*t2[1]+t1[2];
					return t3;
				}
			}
			//System.out.println("ROZPOCZ");
		}
		//System.out.println("równoleg³e");
		return null;
	}
	
	
	public static final boolean isPointInside(float x, float y, float maxX, float maxY, float minX, float minY)
	{
		if(x>minX&&x<maxX&&y>minY&&y<maxY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final boolean isPointInsideOrOn(float x, float y, float maxX, float maxY, float minX, float minY)
	{
		if(x>minX&&x<maxX&&y>minY&&y<maxY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final boolean isPointInsideOrOn(float x, float y, float maxX, float maxY, float minX, float minY, float r)
	{
		if(x>minX-r&&x<maxX+r&&y>minY-r&&y<maxY+r)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final boolean isPointOn(float x, float y, float maxX, float maxY, float minX, float minY)
	{
		if(x==minX||x==maxX&&y==minY||y==maxY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final boolean isPointInsideVertical(float x, float y, float maxX, float maxY, float minX, float minY)
	{
		if(x==minX&&x==maxX&&y>minY&&y<maxY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final float[] corossPointOfTwoLines(float abc[], float x1_2, float y1_2, float x2_2, float y2_2)
	{
		float t1[] = abc;
		float t2[] = generatePatternABC(x1_2, y1_2, x2_2, y2_2);
		float t3[] = getCrossOfLine(t1[0], t1[1], t2[0], t2[1]);
		if(t3!=null)
		{
			return t3;
		}
		else
		{
			if(isVertical(t1)&&isVertical(t2))
			{
				return null;
			}
			else if(isVertical(t1))
			{
				t3 = new float[2];
				t3[0] = t1[1];
				t3[1] = (-t2[2]-t2[0]*t1[1])/t2[1];
				return t3;
			}
			else if(isVertical(t2))
			{
				t3 = new float[2];
				t3[0] = t2[1];
				t3[1] = (-t1[2]-t1[0]*t2[1])/t1[1];
				return t3;
			}
			return null;
		}
	}
	
	
	public static final float[] corossPointOfTwoLines(float x1_1, float y1_1, float x2_1, float y2_1, float x1_2, float y1_2, float x2_2, float y2_2)
	{
		float t1[] = generatePatternABC(x1_1, y1_1, x2_1, y2_1);
		float t2[] = generatePatternABC(x1_2, y1_2, x2_2, y2_2);
		float t3[] = getCrossOfLine(t1[0], t1[1], t2[0], t2[1]);
		if(t3!=null)
		{
			return t3;
		}
		else
		{
			if(isVertical(t1)&&isVertical(t2))
			{
				return null;
			}
			else if(isVertical(t1))
			{
				t3 = new float[2];
				t3[0] = t1[1];
				t3[1] = (-t2[2]-t2[0]*t1[1])/t2[1];
				return t3;
			}
			else if(isVertical(t2))
			{
				t3 = new float[2];
				t3[0] = t2[1];
				t3[1] = (-t1[2]-t1[0]*t2[1])/t1[1];
				return t3;
			}
			return null;
		}
	}
	
	public static final boolean isVertical(float t3[])
	{
		//System.out.println("Sprawdzanie pionowoœci dla "+t3[0]+" "+t3[1]+" "+t3[2]);
		if(t3[0] == 0&&t3[2]==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static final float[] getPoinOfCross(float[] pattOfLine, float x, float y)
	{
		float tab[] = new float[2];
		float newPatt[] = new float[2];
		newPatt[0] = generateA(pattOfLine[0]);
		newPatt[1] = generateB(newPatt[0], x, y);
		tab = getCrossOfLine(pattOfLine[0], pattOfLine[1], newPatt[0], newPatt[1]);
		//System.out.println("Znaleziono punkt na lini "+tab[0]+" "+tab[1]);
		return tab;
	}
	public static final float[] getPoinOfCrossABC(float[] pattOfLine, float x, float y)
	{
		float tab[] = new float[2];
		float newPatt[] = new float[3];
		if(pattOfLine[0]!=0&&pattOfLine[2]!=0&&pattOfLine[1]==-1)
		{
		newPatt[0] = generateA(pattOfLine[0]);
		newPatt[2] = generateB(newPatt[0], x, y);
		tab = getCrossOfLine(pattOfLine[0], pattOfLine[2], newPatt[0], newPatt[2]);
		//System.out.println("Znaleziono punkt na lini "+tab[0]+" "+tab[1]);
		return tab;
		}
		else
		{
			//System.out.println("Drugie");
			if(pattOfLine[1]!=-1)
			{
				tab[0] = pattOfLine[1];
				tab[1] = y;
				return tab;
			}
			else
			{
				tab[0] = x;
				tab[1] = pattOfLine[2];
				return tab;
			}
		}
	}
	public static final float[] getPoinOfCross(float Xpattern, float x, float y)
	{
		float tab[] = new float[2];
		tab[0] = Xpattern;
		tab[1] = y;
		return tab;
	}
	
	public static final float generateB(float a, float x, float y)
	{
		return (y-a*x);
	}
	public static float generateA(float a)
	{
		return -1/a;
	}
	public static final float[] generatePatternAB(float xa, float ya, float xb, float yb)
	{
		float[] tab = new float[2];
		if(xa!=xb)
		{
		tab[0]=(yb-ya)/(xb-xa);
		tab[1]=ya-tab[0]*xa;
		//System.out.println("Wzór a: "+tab[0]+" b"+tab[1]);
		return tab;
		}
		else
		{
			return null;
		}

	}

	public static final int countDET(float x1, float y1, float x2, float y2, float x3, float y3)
	{
	    float dot = (y2-y1)*(x2-x3) + (x1-x2)*(y2-y3);
	    if (dot == 0) return 0;
	    if (dot < 0) return -1;
	    return 1;
	}
	public static final float[] generatePatternABC(float xa, float ya, float xb, float yb)
	{
		//System.out.println("Generowanie ABC");
		float[] tab = new float[3];
		if(xa!=xb)
		{
		tab[0]=(yb-ya)/(xb-xa);
		tab[1] = -1;
		tab[2]=ya-tab[0]*xa;
		//System.out.println("Wzór a: "+tab[0]+" b"+tab[1]+" c"+tab[2]);
		return tab;
		}
		else
		{
			tab = new float[3];
			tab[0] = 0;
			tab[1] = xb;
			tab[2] = 0;
			//System.out.println("Wzór pionowa a: "+tab[0]+" b"+tab[1]+" c"+tab[2]);
			return tab;
		}

	}

	public static final float[] getCrossOfLine(float a1, float b1, float a2, float b2)
	{
		if(a1==a2)
		{
			//System.out.println(a1+" "+a2+" "+b1+" "+b2);
			return null;
		}
		else
		{
			float[] tab = new float[2];
			tab[0] = (b2-b1)/(a1-a2);
			tab[1] = a1*tab[0]+b1;
			return tab;
		}
		
	}

	public static final float[] getCrossOfLineX(float a1, float b1, float x)
	{
			float[] tab = new float[2];
			tab[0] = x;
			tab[1] = a1*x+b1;
			return tab;
	}
	
	public static final double distance(float x1, float y1, float x2, float y2)
	{
		return Math.sqrt(Math.pow((x2-x1), 2)+Math.pow((y2-y1), 2));
	}
	public static final float distanceF(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(Math.pow((x2-x1), 2)+Math.pow((y2-y1), 2));
	}
	public static final float getDistToLine(float A, float B, float C, float x, float y)
	{
		return (float) ((Math.abs(A*x+B*y+C))/(Math.sqrt(A*A+B*B)));
	}

	public static final float[] generateLine(float x1, float y1, float x2, float y2)
	{
		float tab[] = new float[2];
		tab[0] = (y1-y2)/(x1-x2);
		tab[1] = y1 - x1*tab[0];
		return tab;
	}
}
