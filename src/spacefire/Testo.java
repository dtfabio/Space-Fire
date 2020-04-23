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
public class Testo {
	private double x;
	private double y;
	private long tempo;
	private long start;
        private String s;
        
	// COSTRUTTORE
	public Testo(double x, double y, long tempo, String s) {
		this.x = x;
		this.y = y;
		this.tempo = tempo;
		this.s = s;
		start = System.nanoTime();
	}
	
	public boolean aggiorna() {
		long elapsed = (System.nanoTime() - start) / 1000000; //tempo d'attesa 
		if(elapsed > tempo) { //quando è maggiore di tempo
			return true; //vuol dire che deve essere rimosso a queste condizioni
		}
		return false;
	}
	
	public void disegna(Graphics2D g) {
                Font fnt= new Font("Century Gothic",1,12);
                g.setFont(fnt);                        
                g.setColor(Color.red);
		g.drawString(s, (int) (x)-15, (int) y);		
	}
}



















