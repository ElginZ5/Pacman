package Pacman;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Objects { // parent class

	int x, y, width, height;
	Color c;
	
	public Objects (int x, int y, int w, int h, Color c) { // constructor
		
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		this.c = c;
		
	}
	
	public abstract void draw (Graphics g); // abstract draw method
	
}
