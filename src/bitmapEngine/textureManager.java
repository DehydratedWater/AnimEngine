package bitmapEngine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class textureManager 
{
	private ArrayList<Texture> textureList;
	private HashMap<String, Integer> serchMap;
	
	/**
	 * Tworzy nowy managerTekstur obiekt kt�ry zawiera w sobie wszystkie bitmapy jako BufferedImage i zwraca ich indeksy w tablicy 
	 */
	public textureManager()
	{
		textureList= new ArrayList<Texture>();
		serchMap = new HashMap<>();
	}
	/**
	 * Dodaje now� teksur� do listy tekstur zwracajac jej indeks w li�cie, path to �cie�ka dost�pu do pliku a name to nazwa indeksowa po kt�rej b�dzie mo�na wyszuka� tekstur� i zwr�ci� jej indeks
	 * @param path
	 * @param name
	 * @return indeks tekstury w li�cie
	 */
	public int addNewTextureAndGetTextureID(String path, String name)
	{

		if(serchMap.containsKey(name))
		{
			System.out.println("Tekstura jest ju� w pami�ci");
			return serchMap.get(name);
		}
		else
		{
			BufferedImage tex = null;
			
			try {
				tex = ImageIO.read(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("B��d �adowania textury");
				return -10;
			}
			serchMap.put(name, textureList.size());
			textureList.add(new Texture(path, name, tex));
			System.out.println("Pomy�lnie za�adowano textur� "+name+" "+path+" "+(textureList.size()-1));
			return textureList.size()-1;
		}
	}
	
	
	public int generateNewTextureAndGetTextureID(String name, int x, int y)
	{

		if(serchMap.containsKey(name))
		{
			System.out.println("Tekstura jest ju� w pami�ci");
			return serchMap.get(name);
		}
		else
		{
			BufferedImage tex = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
			
			serchMap.put(name, textureList.size());
			textureList.add(new Texture(name, name, tex));
			System.out.println("Pomy�lnie wygenerowano textur� "+name+" "+(textureList.size()-1));
			return textureList.size()-1;
		}
	}
	/**
	 * Zwraca tekstur� pod danym indeksem
	 * @param i
	 * @return
	 */
	public Texture getTexture(int i)
	{
		return textureList.get(i);
	}
	
	public int getTextureTabSize()
	{
		return textureList.size();
	}
}
