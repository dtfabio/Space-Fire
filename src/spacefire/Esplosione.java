package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import java.awt.*;

/**
 *
 * @author FabioDT
 */
public class Esplosione {
	private double x;
	private double y;
	private int r;
	private int maxRadius;
	
	// COSTRUTTORE
	public Esplosione(double x, double y, int r, int max) {
		this.x = x;
		this.y = y;
		this.r = r;
		maxRadius = max;
	}
	
	public boolean aggiorna() {
		r += 2;
		if(r >= maxRadius) {
			return true; //vuol dire che deve essere rimosso a queste condizioni
		}
		return false;
	}
	
	public void disegna(Graphics2D g) {
		g.setColor(new Color(255, 255, 255, 128));
		g.setStroke(new BasicStroke(3));
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	}
	
}



















