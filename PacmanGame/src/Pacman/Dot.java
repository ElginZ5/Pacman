package Pacman;
import java.awt.Color;
import java.awt.Graphics;

public class Dot extends Objects { // dots class

	public Dot(int x, int y, int w, int h, Color c) {
		super(x, y, w, h, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g2d) { // draws a dot
		// TODO Auto-generated method stub
		g2d.setColor(c);
		g2d.fillOval(x, y, width, height);
		
	}

}