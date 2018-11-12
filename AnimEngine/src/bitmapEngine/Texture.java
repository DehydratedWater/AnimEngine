package bitmapEngine;

import java.awt.image.BufferedImage;

public class Texture 
{
	public BufferedImage texture;
	private String path;
	private String name;
	
	/**
	 * Tworzy now� tekstur� kt�ra przechowuje nazw� pliku (path) oraz nazw� indeksow� po kt�rej tekstura b�dzie rozpoznawalna
	 * @param path
	 * @param name
	 * @param texture
	 */
	public Texture(String path, String name, BufferedImage texture)
	{
		this.setName(name);
		this.setPath(path);
		this.texture = texture;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBitmap()
	{
		String s = "BITMAP "+path+" "+name;
		return s;
	}
}
