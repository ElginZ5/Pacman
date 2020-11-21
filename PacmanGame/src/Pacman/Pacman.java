 package Pacman;
import java.awt.Color;
import java.awt.Graphics;

public class Pacman extends Objects { // pacman class extends objects

	public Pacman(int x, int y, int w, int h, Color c) { // constructor
		super(x, y, w, h, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) { // draws pacman
		// TODO Auto-generated method stub
		g.setColor(c);
		g.fillRect(x, y, width, height);
		
	}

}
