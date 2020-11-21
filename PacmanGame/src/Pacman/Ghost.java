package Pacman;
import java.awt.Color;
import java.awt.Graphics;

public class Ghost extends Objects { // ghost class

	public Ghost(int x, int y, int w, int h, Color c) {
		super(x, y, w, h, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) { // draws a ghost
		// TODO Auto-generated method stub
		g.setColor(c);
		g.fillRect(x, y, width, height);
		
	}

}
