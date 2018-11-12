package openSave;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import bitmapEngine.Bitmap;
import main.Window;
import polygonEngine.FilledPolygonWithCurves;
import polygonEngineStructures.shapeConnector;
import renderSource.Connection;
import renderSource.Object;
import renderSource.Point;
import structures.Animation;
import structures.Frame;
import structures.frameQuene;
import toolBox.frameCupture;

public class Open 
{

	public static Animation loadFile(String fileName)
	{
		frameCupture.hasFrame = false;
		SaveAs.setIfBeenSaved(true);
		SaveAs.setPath(fileName);
		
		File f = new File(fileName);
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Nie uda�o si� wczyta� pliku");
			e.printStackTrace();
			return null;
		}
		Animation a = new Animation(1);
//		ArrayList<Point> pt = new ArrayList<Point>();
//		ArrayList<Connection> ct = new ArrayList<Connection>();
//		ArrayList<ColorPoint> cp = new ArrayList<ColorPoint>();
		String buffor = s.nextLine();
		if(buffor.equals("ANIMATION"))
		{
			buffor = s.nextLine();
			SaveAs.setName(buffor);
			System.out.println("Wczytywanie pliku "+buffor);
			buffor = s.nextLine();
			if(buffor.equals("RESOLUTION"))
			{
				a.setX(s.nextInt());
				a.setY(s.nextInt());
				Window.SizeX = a.getX();
				Window.SizeY = a.getY();
				frameCupture.hasFrame = false;
				System.out.println("Wczytano rozdzielczo�� "+a.getX()+" "+a.getY());
				s.nextLine();
				buffor = s.nextLine();

				if(buffor.equals("LENGHT"))
				{
					int animL = s.nextInt();
					System.out.println("D�ugo�� animacji "+animL);
					s.nextLine();
					for(int i = 0; i < animL; i++)
					{
						int c = readFrame(s, a);
						if(c==-1)
						{
							System.out.println("Err loading animation save 0 Wrong Frame");
							//System.out.println("Zako�czono niespodziewanie");
							return null;
						}
					}
					
				}
				else
				{
					s.close();
					System.out.println("Err loading animation save 1 Cant Read Lenght");
					return null;
				}
				buffor = s.nextLine();
				if(!buffor.equals("BITMAPS"))
				{
					System.out.println("Err No bitmaps");
					return null;
				}
				buffor = s.nextLine();
				if(buffor.equals("LENGHT"))
				{
					int bitmapL = s.nextInt();
					System.out.println("Ilośc bitmap "+bitmapL);
					s.nextLine();
					for(int i = 0; i < bitmapL; i++)
					{
						int c = readBitmap(s);
						if(c==-1)
						{
							System.out.println("Err loading animation save 0 Wrong Bitmap");
							//System.out.println("Zako�czono niespodziewanie");
							return null;
						}
					}
					
				}
				else
				{
					s.close();
					System.out.println("Err loading animation save 1 Cant Read Lenght of bitmap");
					return null;
				}
			}
			else
			{
				s.close();
				System.out.println("Err loading animation save 2 Cant Read Resolution");
				return null;
			}
			frameCupture.hasFrame = false;
		}
		else
		{
			System.out.println("Err loading animation save 3 Cant find ANIMATION");
			s.close();
			return null;
		}
		s.close();
		//System.out.println(a.getFrameTab().size());
		return a;
	
	}
	
	public static int readBitmap(Scanner s)
	{
		s.next();
		Window.tm.addNewTextureAndGetTextureID(s.next(), s.next());
		s.nextLine();
		return 1;
	}
	
	public static int readFrame(Scanner s, Animation a)
	{
		
		String buffor = s.nextLine();
		s.useLocale(Locale.US);
		Frame f = new Frame(1);
		if(buffor.equals("FRAME"))
		{

			//System.out.println("Wczytywanie Klatki");
			buffor = s.nextLine();
			if(buffor.equals("QUENETAB"))
			{
				buffor = s.nextLine();
				if(buffor.equals("LENGHT"))
				{
					int queTabL = s.nextInt();
					s.nextLine();
					//System.out.println("Odczytywanie tablicy objekt�w o d�ugo�ci "+objTabL);
					for(int i = 0; i < queTabL; i++)
					{
						int c = readFrameQuene(s, f);
						if(c==-1)
						{
							System.out.println("Err F0 cant read Frame Quene "+i);
							return -1;
						}
					}	
				}
				else
				{
					System.out.println("Err F3 Cant read Quene Tab Lenght");
					return -1;
				}
				//System.out.println("Dodawanie KLATKI xDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
				
				//System.out.println("D�ugo�� animacji "+a.getFrameTab().size());
				
			}
			else
			{
				System.out.println("Err F2 Cand Fint OBJTAB");
				return -1;
			}
		//DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
			buffor = s.nextLine();
			if(buffor.equals("OBJTAB"))
			{
				buffor = s.nextLine();
				if(buffor.equals("LENGHT"))
				{
					int objTabL = s.nextInt();
					s.nextLine();
					//System.out.println("Odczytywanie tablicy objekt�w o d�ugo�ci "+objTabL);
					for(int i = 0; i < objTabL; i++)
					{
						int c = readObject(s, f);
						if(c==-1)
						{
							System.out.println("Err F0 cant read Object "+i);
							return -1;
						}
					}	
				}
				else
				{
					System.out.println("Err F3 Cant read Frame Lenght");
					return -1;
				}
				//System.out.println("Dodawanie KLATKI xDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
			
				//System.out.println("D�ugo�� animacji "+a.getFrameTab().size());
				
			}
			else
			{
				System.out.println("Err F2 Cand Fint OBJTAB");
				return -1;
			}
		}	
		else
		{
			System.out.println("Err F1 Cant Find FRAME");
			return -1;
		}
		
		buffor = s.nextLine();
		if(buffor.equals("BITMAPTAB"))
		{
			buffor = s.nextLine();
			if(buffor.equals("LENGHT"))
			{
				int queTabL = s.nextInt();
				s.nextLine();
				//System.out.println("Odczytywanie tablicy objekt�w o d�ugo�ci "+objTabL);
				for(int i = 0; i < queTabL; i++)
				{
					int c = readBitmap(s, f);
					if(c==-1)
					{
						System.out.println("Err F0 cant read Bitmap "+i);
						return -1;
					}
				}	
			}
			else
			{
				System.out.println("Err F3 Cant read Bitmap Tab Lenght");
				return -1;
			}
			//System.out.println("Dodawanie KLATKI xDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
			a.addFrame(f);
			//System.out.println("D�ugo�� animacji "+a.getFrameTab().size());
			
		}
		else
		{
			System.out.println("Err F2 Cand Fint OBJTAB");
			return -1;
		}
		return 1;
	}
	
	public static int readFrameQuene(Scanner s, Frame f)
	{
		s.next();
		f.addFrameQuene(new frameQuene(s.nextBoolean(), s.nextInt()));
		s.nextLine();
		return 1;
	}
	public static int readBitmap(Scanner s, Frame f)
	{
		s.next();
		f.addBitmap(new Bitmap(s.nextInt(), Float.parseFloat(s.next()), Float.parseFloat(s.next()), Float.parseFloat(s.next()), Float.parseFloat(s.next()), Float.parseFloat(s.next())));
		s.nextLine();
		return 1;
	}
	public static int readObject(Scanner s, Frame f)
	{
		
		Object obj = new Object();
		String buffor = s.nextLine();
		if(buffor.equals("OBJ"))
		{
			//System.out.println("Wczytywanie objektu");
			buffor = s.nextLine();
			if(buffor.equals("POINTS"))
			{
				int pointL = s.nextInt();
				s.nextLine();
				//System.out.println("Wczytywanie tablicy punkt�w o d�ugo�ci "+pointL);
				ArrayList<Point> pointTab = new ArrayList<Point>();

				
				//System.out.println(s.locale());
				for(int i = 0; i < pointL; i++)
				{
					
					//System.out.println("Wczytywanie punktu ");

					float x = s.nextFloat();
					float y = s.nextFloat();
					Point p = new Point(x, y);
					//System.out.println("Wczytano nowy punkt "+ x+" "+ y);
					pointTab.add(p);
					s.nextLine();
					
				}
				obj.setPointTab(pointTab);
				//System.out.println("Wczytano tablic� punkt�w");
				buffor = s.nextLine();
				if(buffor.equals("CONNECTIONES"))
				{
					int conL = s.nextInt();
					s.nextLine();
					//System.out.println("Wczytywanie tablicy po��cze� o d�ugo�ci "+conL);
					ArrayList<Connection> conTab = new ArrayList<Connection>();
					for (int i = 0; i < conL; i++)
					{
						//System.out.println("Wczytywanie po��czenia nr. "+i);
						boolean a = false;
						boolean b = false;
						if(s.next().equals("true"))
						{
							a = true;
						}
						if(s.next().equals("true"))
						{
							b = true;
						}
						int p1 = s.nextInt();
						int p2 = s.nextInt();
						int p3 = s.nextInt();
						int p4 = s.nextInt();
						float size = s.nextFloat();
						//System.out.println(a+" "+b+" "+p1+" "+p2+" "+p3+" "+p4+" "+size);
						Connection c = new Connection(a, b, p1, p2, p3, p4, size);
					
						//System.out.println("Wczytno po��czenie "+ a+" "+b);
						buffor = s.next();
						if(buffor.equals("COLOR"))
						{
							Color col = new Color(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
							//System.out.println("COLOR "+col.toString());
							c.setC(col);
						}
						else
						{
							System.out.println("Err O7 Wrong connection Color Cant find COLOR");
							return -1;
						}
						s.nextLine();
						conTab.add(c);
					}
					//System.out.println("wczytano tablic� po��cze� "+conTab.size());
					obj.setConnectionTab(conTab);
					//System.out.println("Wczytano tablic� po��cze�");
					buffor = s.nextLine();
					if(buffor.equals("POLYGONS"))
					{
						int colorL = s.nextInt();
						s.nextLine();
						//System.out.println("Wczytywanie tablicy wype�nie� kolorem o d�ugo�ci "+colorL);
						ArrayList<FilledPolygonWithCurves> polygonTab = new ArrayList<FilledPolygonWithCurves>();
						for (int i = 0; i < colorL; i++)
						{
							Color col;
							ArrayList<shapeConnector> polTab;
							ArrayList<Integer> intTab;
							
							if(s.next().equals("COLOR"))
							{
									//System.out.println("Wczytywanie koloru");
									col = new Color(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
									//System.out.println("Wczytano kolor");
							}
							else
							{
								System.out.println("Err O6 Cant find COLOR");
								return -1;
							}
							if(s.next().equals("POLYGON"))
							{
								//System.out.println("Wczytywanie shapeconector�w");
									int shapL = s.nextInt();
									polTab = new ArrayList<shapeConnector>();
									for(int j = 0; j < shapL; j++)
									{
										polTab.add(new shapeConnector(s.nextBoolean(), s.nextBoolean(), s.nextBoolean(), s.nextBoolean(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt()));
									}
									//System.out.println("Zako�czono");
							}
							else
							{
								System.out.println("Err O5 Cant find SHAPECONN");
								return -1;
							}
							if(s.next().equals("CONN"))
							{
								//System.out.println("Wczytywanie po�acze� powi�zanych");
									int connL = s.nextInt();
									intTab = new ArrayList<Integer>();
									for(int j = 0; j < connL; j++)
									{
										intTab.add(s.nextInt());
									}
									s.nextLine();
								//	System.out.println("Zako�czono");
							}
							else
							{
								System.out.println("Err O4 Cant find CONN");
								return -1;
							}
							FilledPolygonWithCurves fPollygon = new FilledPolygonWithCurves(polTab, intTab, col);
							polygonTab.add(fPollygon);
							//System.out.println("Dodawanie nowego ColorPointu");
						}
						//System.out.println("Dodawanie Tablicy colorpoint�w "+colorTab.size());
						obj.setPolygonTab(polygonTab);
					}
					else
					{
						System.out.println("Err O3 cant find COLORPOINTS");
						return -1;
					}
				}
				else
				{
					System.out.println("Err O2 Cant find CONNECTIONES");
					return -1;
				}
			}
			else
			{
				System.out.println("Err O1 Cant find POINTS");
				return -1;
			}
			f.addObj(obj);
			//System.out.println("zako�czono wczytywanie objektu");
			
		}
		else
		{
			System.out.println("Err O0 Cant find OBJ");
			return -1;
		}
		return 1;
	}
}
