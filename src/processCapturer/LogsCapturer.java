package processCapturer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

public class LogsCapturer 
{
	public static boolean needToSaveLogs = false;
	public static ArrayList<String> logsList = new ArrayList<String>();
	
	public static void addTextLog(String log)
	{
		if(needToSaveLogs)
		logsList.add(log);
	}
	public static void clearList()
	{
		logsList = new ArrayList<String>();
	}
	public static void saveLogs()
	{
		if(!needToSaveLogs)
			return;
		Date d = new Date(System.currentTimeMillis());
		File f = new File("logs/log "+d.toString()+" "+System.currentTimeMillis()+".txt");
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		try {
			PrintWriter fw = new PrintWriter(f);
			for(int i = 0; i < logsList.size(); i++)
			{
				fw.println(i+". "+logsList.get(i));
				
			}
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		clearList();
	}
}
