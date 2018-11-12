package toolBox;

import java.awt.Color;
import java.awt.Graphics2D;

public class TextBox 
{
	public static String message = "";
	private static int timmer = 0;
	private static boolean isReady = false;
	public static void drawMessage(Graphics2D g, float x, float y)
	{
		if(isReady)
		{
			timmer--;
			if(timmer<=0)
			{
				isReady=false;
			}
			g.setColor(Color.BLACK);
			//System.out.println("Wyœwietlono wiadomoœæ");
			g.drawString(message, x, y);
		}
	}
	public static void addMessage(String mess, int time)
	{
		System.out.println("Dodano wiadomoœæ: "+mess);
		isReady = true;
		message = mess;
		timmer = time;
	}
}
