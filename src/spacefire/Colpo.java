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
public class Colpo {
	private double x;
	private double y;
	private int r;
	private double dx;
	private double dy;
	private double rad;
	private double velocità;
	
	//COSTRUTTORE
	public Colpo(double angle, int x, int y) {
		this.x = x;   //la x del parametro la assegni a una variabile della classe per poterci lavorare
		this.y = y;                
		r = 5;        //dimensione
		velocità = 10;//velocità
                
                //DIREZIONE DEL COLPO
		rad = Math.toRadians(angle); //funzione converte un angolo in radianti
		dx = Math.cos(rad) * velocità;//Math.cos restituisce il coseno
		dy = Math.sin(rad) * velocità;//Math.sin restituisce il seno
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
        
	public boolean aggiorna() {		
		x += dx; //permette il movimento
		y += dy;
		
                //SPARISCE IL COLPO, quando la x è minore dell -r sparisce il colpo
		if(x < -r || x > GiocoPannello.WIDTH +r || y < -r || y > GiocoPannello.HEIGHT+r  ) {
			return true; //vuol dire che deve essere rimosso a queste condizioni
		}		
		return false;
	}
	
	public void disegna(Graphics2D g) {
		g.setColor(Color.yellow);
                g.setStroke(new BasicStroke(2));
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
                g.setColor(Color.MAGENTA);
                g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        }	
}











































