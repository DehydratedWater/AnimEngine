package pastManager;

import java.util.ArrayList;
import java.util.Scanner;

import polygonEngine.FilledPolygonWithCurves;
import renderSource.Connection;
import renderSource.Object;
import renderSource.Point;
import structures.Animation;
import structures.Frame;

public class PastBox 
{
	public static ArrayList<String> eventList = new ArrayList<String>();
	public static ArrayList<Connection> connBase = new ArrayList<Connection>();
	public static ArrayList<FilledPolygonWithCurves> polygonBase = new ArrayList<FilledPolygonWithCurves>();
	public static ArrayList<Object> objectBase = new ArrayList<Object>();
	public static ArrayList<Frame> frameBase = new ArrayList<Frame>();
	public static ArrayList<ConnTabKeeper> connTabBase = new ArrayList<ConnTabKeeper>();
	public static ArrayList<PointTabKeeper> pointTabBase = new ArrayList<PointTabKeeper>();
	public static boolean hasObjectBackUp = false;
	
	public static int steps = 0;
	
	public static Animation reverseStep(Animation a)
	{
		if(steps>0)
		steps--;
		
//		System.out.println("Steps "+eventList.size());
//		for(String d: eventList)
//		{
//			System.out.println(d);
//		}
		
		Scanner s;
		for(int i = 0 ; i < eventList.size(); i++)
		{
			
			String step = eventList.get(eventList.size()-1);
			eventList.remove(eventList.size()-1);
			i--;
			s = new Scanner(step);
			
			String head = s.next();
			
			if(head.equals("STEP"))
			{
				break;
			}
			else if(head.equals("ADDP"))
			{
				removePoint(a, s.nextInt(), s.nextInt(), s.nextInt());
			} 
			else if(head.equals("ADDC"))
			{
				removeConn(a, s.nextInt(), s.nextInt(), s.nextInt());
			}
			else if(head.equals("CHAC"))
			{
				//System.out.println("Cofanie przeciêcia lini");
				changeConn(a, s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
//				changeConn(a, s.nextInt(), s.nextInt(), s.nextInt(),
//				new Connection(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), 
//				s.nextBoolean(), s.nextBoolean(), Float.parseFloat(s.next()), 
//				new Color(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt())));
			}
			else if(head.equals("MOVP"))
			{
				movePoint(a, s.nextInt(), s.nextInt(), s.nextInt(), Float.parseFloat(s.next()), Float.parseFloat(s.next()));
			}
			else if(head.equals("APOL"))
			{
				removePolygon(a, s.nextInt(), s.nextInt(), s.nextInt());
			}
			else if(head.equals("REMC"))
			{
				addConnOnIndex(a, s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
			}
			else if(head.equals("REMP"))
			{
				addPointOnIndex(a, s.nextInt(), s.nextInt(), s.nextInt(), new Point(Float.parseFloat(s.next()), Float.parseFloat(s.next()), s.nextBoolean()));
			}
			else if(head.equals("RPOL"))
			{
				addPolygon(a, s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
			}
			else if(head.equals("REMC"))
			{
				
			}
			else if(head.equals("HSCE"))
			{
				reverseHugeStructureChanges(a, s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
			}
			else if(head.equals("EOBJ"))
			{
				replaceActualObjectWithObjectFromMemory(a, s.nextInt(), s.nextInt(), s.nextInt());
			}
			
			
		}
		
		return a;
	}
	
	

	private static void addPolygon(Animation a, int frameIndex, int objIndex, int index, int indexInTab) 
	{
		//TODO
		System.out.println("Dodawanie polygona");
		a.getFrameNum(frameIndex).getObj(objIndex).addPolygon(polygonBase.get(indexInTab));
		polygonBase.remove(indexInTab);
//		ArrayList<FilledPolygonWithCurves> polygonTab = new ArrayList<FilledPolygonWithCurves>(a.getFrameNum(frameIndex).getObj(objIndex).getPolygonTab().size()+1);
//		
//		boolean added = false;
//		for(int i = 0; i < a.getFrameNum(frameIndex).getObj(objIndex).getPointTabSize(); i++)
//		{
//			if(i == index)
//			{
//				System.out.println("Dodawanie polygona3");
//				polygonTab.add(polygonBase.get(indexInTab));
//				System.out.println("Dodawanie polygona4");
//				polygonBase.remove(indexInTab);
//				System.out.println("Dodawanie polygona5");
//				added = true;
//			}
//			polygonTab.add(a.getFrameNum(frameIndex).getObj(objIndex).getPolygon(i));
//			
//		}
//		if(a.getFrameNum(frameIndex).getObj(objIndex).getPolygonTab().size() == index&&!added)
//		{
//			System.out.println("Dodawanie polygona2");
//			polygonTab.add(polygonBase.get(indexInTab));
//			polygonBase.remove(indexInTab);
//		}
//		a.getFrameNum(frameIndex).getObj(objIndex).setPolygonTab(polygonTab);
	}

	private static void removePoint(Animation a, int frameIndex, int objIndex,int index)
	{
		a.getFrameNum(frameIndex).getObj(objIndex).getPointTab().remove(index);
	}
	
	private static void addPointOnIndex(Animation a, int frameIndex, int objIndex,int index, Point p)
	{
		ArrayList<Point> pointTab = new ArrayList<Point>(a.getFrameNum(frameIndex).getObj(objIndex).getPointTabSize()+1);
		
		boolean added = false;
		for(int i = 0; i < a.getFrameNum(frameIndex).getObj(objIndex).getPointTabSize(); i++)
		{
			if(i == index)
			{
				pointTab.add(p);
				added = true;
			}
			pointTab.add(a.getFrameNum(frameIndex).getObj(objIndex).getNormalPoint(i));
			
		}
		if(a.getFrameNum(frameIndex).getObj(objIndex).getPointTabSize() == index&&!added)
		{
			pointTab.add(p);
		}
		a.getFrameNum(frameIndex).getObj(objIndex).setPointTab(pointTab);
	}
	
	private static void addConnOnIndex(Animation a, int frameIndex, int objIndex,int index, int indexFromBase)
	{
		//System.out.println("Dodawniae po³¹cznia "+index+" "+indexFromBase+" "+a.getFrameNum(frameIndex).getObj(objIndex).getConnectionTab().size());
		ArrayList<Connection> connTab = new ArrayList<Connection>(a.getFrameNum(frameIndex).gco().getConnectionTabSize()+1);
		boolean added = false;
		for(int i = 0; i < a.getFrameNum(frameIndex).gco().getConnectionTabSize(); i++)
		{
			if(i == index)
			{
				//System.out.println("Dodawanie Usuniêtego po³¹cznia "+index+" || "+ connBase.get(indexFromBase).getP1()+" "+connBase.get(indexFromBase).getP2());
				connTab.add(new Connection(connBase.get(indexFromBase)));
				connBase.remove(indexFromBase);
				added = true;
			}
			//System.out.println("Dodawanie po³acznia "+i+" || "+a.getFrameNum(frameIndex).getObj(objIndex).getCon(i).getP1()+" "+a.getFrameNum(frameIndex).getObj(objIndex).getCon(i).getP2());
			connTab.add(a.getFrameNum(frameIndex).getObj(objIndex).getCon(i));
		}
		if(a.getFrameNum(frameIndex).getObj(objIndex).getConnectionTab().size() == index&&!added)
		{
			connTab.add(connBase.get(indexFromBase).clone());
			connBase.remove(indexFromBase);
		}
		//System.out.println(connTab.size());
		
		
		a.getFrameNum(frameIndex).getObj(objIndex).setConnectionTab(connTab);
	}
	private static void removePolygon(Animation a, int frameIndex, int objIndex,int index)
	{
		a.getFrameNum(frameIndex).getObj(objIndex).removePolygon(index);
	}
	private static void movePoint(Animation a, int frameIndex, int objIndex,int index, float x, float y)
	{
		a.getFrameNum(frameIndex).getObj(objIndex).getNormalPoint(index).x = x;
		a.getFrameNum(frameIndex).getObj(objIndex).getNormalPoint(index).y = y;
		a.getFrameNum(frameIndex).getObj(objIndex).getNormalPoint(index).MarkedPoint = false;
	}
	
	private static void removeConn(Animation a, int frameIndex, int objIndex,int index)
	{
		a.getFrameNum(frameIndex).getObj(objIndex).getConnectionTab().remove(index);
	}
	
	private static void reverseHugeStructureChanges(Animation a, int frameIndex, int objIndex, int pIndex, int cIndex)
	{
		a.getFrameNum(frameIndex).getObj(objIndex).setPointTab(pointTabBase.get(pIndex).pointTab);
		a.getFrameNum(frameIndex).getObj(objIndex).setConnectionTab(connTabBase.get(cIndex).connTab);
		pointTabBase.remove(pIndex);
		connTabBase.remove(cIndex);
	}
	
	private static void changeConn(Animation a, int frameIndex, int objIndex,int index, int connBaseIndex)
	{
		//System.out.println(c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.isARC()+" "+c.isDoubleArc()+" "+c.getSize()+" "+c.getC().toString());
		a.getFrameNum(frameIndex).getObj(objIndex).getConnectionTab().set(index, connBase.get(connBaseIndex));
		connBase.remove(connBaseIndex);
	}
//	private static void changeConn(Animation a, int frameIndex, int objIndex,int index, Connection c)
//	{
//		//System.out.println(c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.isARC()+" "+c.isDoubleArc()+" "+c.getSize()+" "+c.getC().toString());
//		a.getFrameNum(frameIndex).getObj(objIndex).getConnectionTab().set(index, c);
//	}
	public static void eventAddPoint(int frameIndex, int objIndex, int index)
	{
		if(hasObjectBackUp)
			return;
		String s = "ADDP "+frameIndex+" "+objIndex+" "+index;
		eventList.add(s);
	}

	public static void eventAddPolygon(int frameIndex, int objIndex, int index)
	{
		if(hasObjectBackUp)
			return;
		String s = "APOL "+frameIndex+" "+objIndex+" "+index;
		eventList.add(s);
	}
	public static void eventAllObjectDeform(int frameIndex, int objIndex, Object obj)
	{
		hasObjectBackUp = true;
		System.out.println("Zrobiono kopiê zapasow¹ ca³ego obiektu");
		String s = "EOBJ "+frameIndex+" "+objIndex+" "+objectBase.size();
		eventList.add(s);
		objectBase.add(obj.clone());
	}
	private static void replaceActualObjectWithObjectFromMemory(Animation a, int frameIndex, int objIndex,int index) {
		System.out.println("Przywracanie kopi zapasowej obiektu "+frameIndex+" "+objIndex);
		a.getFrameNum(frameIndex).setObject(objIndex, objectBase.get(index));
		objectBase.remove(index);
	}
//	public static void eventChangeConn(int frameIndex, int objIndex, int index, Connection OldC)
//	{
//		//System.out.println("Zmiana po³¹cznia nr."+index+": "+OldC.getP1()+" "+OldC.getP2()+" "+OldC.getP3()+" "+OldC.getP4()+" "+OldC.isARC()+" "+OldC.isDoubleArc()+" "+OldC.getSize()+" "+OldC.getC().toString());
//		String s = "CHAC "+frameIndex+" "+objIndex+" "+index+" "+OldC.getP1()+" "+OldC.getP2()+" "+OldC.getP3()+" "+OldC.getP4()+" "+
//		OldC.isARC()+" "+OldC.isDoubleArc()+" "+OldC.getSize()
//		+" "+OldC.getC().getRed()+" "+OldC.getC().getGreen()+" "+OldC.getC().getBlue()+" "+OldC.getC().getAlpha();
//		eventList.add(s);
//	}
	
	public static void eventRemPoint(int frameIndex, int objIndex, int index, Point p)
	{
		if(hasObjectBackUp)
			return;
		String s = "REMP "+frameIndex+" "+objIndex+" "+index+" "+p.x+" "+p.y+" "+p.TechPoint;
		eventList.add(s);
	}
	
	public static void eventMovPoint(int frameIndex, int objIndex, int index, Point p)
	{
		if(hasObjectBackUp)
			return;
		String s = "MOVP "+frameIndex+" "+objIndex+" "+index+" "+p.x+" "+p.y;
		eventList.add(s);
	}
	public static void eventAddConn(int frameIndex, int objIndex, int index)
	{
		if(hasObjectBackUp)
			return;
		String s = "ADDC "+frameIndex+" "+objIndex+" "+index;
		eventList.add(s);
	}
	
	public static void eventRemConn(int frameIndex, int objIndex, int index, Connection c)
	{
		if(hasObjectBackUp)
			return;
		String s = "REMC "+frameIndex+" "+objIndex+" "+index+" "+connBase.size();
		connBase.add(new Connection(c));
		eventList.add(s);
	}
	
	public static void eventRemPolygon(int frameIndex, int objIndex, int index, FilledPolygonWithCurves fp)
	{
		if(hasObjectBackUp)
			return;
		String s = "RPOL "+frameIndex+" "+objIndex+" "+index+" "+polygonBase.size();
		System.out.println(s);
		polygonBase.add(fp);
		eventList.add(s);
	}
	public static void eventChangeConn(int frameIndex, int objIndex, int index, Connection c)
	{
		if(hasObjectBackUp)
			return;
		String s = "CHAC "+frameIndex+" "+objIndex+" "+index+" "+connBase.size();
		connBase.add(new Connection(c));
		eventList.add(s);
	}
	public static void eventHugeStructureChange(int frameIndex, int objIndex, Object o)
	{
		if(hasObjectBackUp)
			return;
		String s = "HSCE "+frameIndex+" "+objIndex+" "+pointTabBase.size()+" "+connTabBase.size();
		pointTabBase.add(new PointTabKeeper(o.getPointTab()));
		connTabBase.add(new ConnTabKeeper(o.getConnectionTab()));
		eventList.add(s);
	}
	public static void addStepStart()
	{
		
		String s = "STEP";
		hasObjectBackUp = false;
		if(eventList.size()==0)
		{
			steps++;
			eventList.add(s);
		}
		else if(!eventList.get(eventList.size()-1).equals("STEP"))
		{
			steps++;
			eventList.add(s);
		}
	}
}
