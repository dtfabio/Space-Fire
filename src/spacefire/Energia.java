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
public class Energia {
	private double x;
	private double y;
	private int r;	
	private int tipo;
	private Color color1;
	
	// 1 -- +1 life
	// 2 -- +1 power
	// 3 -- +2 power
	// 4 -- slow down time
	
	// COSTRUTTORE
	public Energia(int tipo, double x, double y) {
		this.tipo = tipo;
		this.x = x;
		this.y = y;
		
		if(tipo == 1) {
			color1 = Color.PINK; //VITA
			r = 3;
		}
		if(tipo == 2) {
			color1 = Color.YELLOW; //POTENZA
			r = 3;
		}
		if(tipo == 3) {
			color1 = Color.YELLOW;  //DOPPIA POTENZA
			r = 5;
		}
		if(tipo == 4) {
			color1 = Color.WHITE;   //SLOW DOWN
			r = 3;
		}
	}
	// GETTER
	public double getx() { 
            return x; 
        }
	public double gety() { 
            return y; 
        }
	public double getr() { 
            return r; 
        }
	public int getTipo() { 
            return tipo; 
        }
	
	public boolean aggiorna() {
		y += 2;
		if(y > GiocoPannello.HEIGHT + r) {
			return true; //vuol dire che deve essere rimosso a queste condizioni
		}
		return false;		
	}
	
	public void disegna(Graphics2D g) {
		g.setColor(color1);
		g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);		
	}
	
}






















