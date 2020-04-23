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
public class Nemico {
	private double x;
	private double y;
	private int r;	
	private double dx;
	private double dy;
	private double rad;
	private double velocità;
	private int vita;
	private int tipo;
	private int rango;
	private Color color1;
	private boolean pronto;
	private boolean morto;
	private boolean colpito;
	private long colpireTempo;
	private boolean piano;
	
	// COSTRUTTORE
	public Nemico(int tipo, int rango) {
		this.tipo = tipo;
		this.rango = rango;
		
		// nemico di base
		if(tipo == 1) {
			color1 = new Color(0, 0, 255, 128); //blue
			if(rango == 1) {
				velocità = 2;   //velocità
				r = 8;          //dimensione
				vita = 1;       //numero di volte che puo essere colpito
			}
			if(rango == 2) {
				velocità = 2;
				r = 13;
				vita = 2;
			}
			if(rango == 3) {
				velocità = 1.5;
				r = 23;
				vita = 3;
			}
			if(rango == 4) {
				velocità = 1.5;
				r = 33;
				vita = 4;
			}
		}
                
		//nemico più forte e più veloce del nemico base
		if(tipo == 2) {
			color1 = new Color(255, 0, 0, 128); //red
			if(rango == 1) {
				velocità = 3; 
				r = 8;      //dimensione
				vita = 2;
			}
			if(rango == 2) {
				velocità = 3;
				r = 13;
				vita = 3;
			}
			if(rango == 3) {
				velocità = 2.5;
				r = 23;
				vita = 3;
			}
			if(rango == 4) {
				velocità = 2.5;
				r = 33;
				vita = 4;
			}
		}
		// nemico lento ma forte ad uccidere
		if(tipo == 3) {
			color1 = new Color(0, 255, 0, 128); //green
			if(rango == 1) {
				velocità = 1.5;
				r = 8;
				vita = 3;
			}
			if(rango == 2) {
				velocità = 1.5;
				r = 13;
				vita = 4;
			}
			if(rango == 3) {
				velocità = 1.5;
				r = 28;
				vita = 5;
			}
			if(rango == 4) {
				velocità = 1.5;
				r = 48;
				vita = 5;
			}
		}
		
		x = Math.random() * GiocoPannello.WIDTH / 2 + GiocoPannello.WIDTH / 4;
		y = -r;
		
		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * velocità;
		dy = Math.sin(rad) * velocità;
		
		pronto = false;
		morto = false;
		
		colpito = false;
		colpireTempo = 0;
		
	}
	
	// FUNZIONE GETTER
	public double getx() { 
            return x; 
        }
	public double gety() { 
            return y; 
        }
	public int getr() { 
            return r; 
        }
	public int getTipo() { 
            return tipo; 
        }
	public int getRango() { 
            return rango; 
        }
        public boolean isMorto() {
            return morto; 
        }        
        //FUNZIONI SETTER
	public void setPiano(boolean b) { 
            piano = b; 
        }
	
        //Quando il nemico viene colpito
	public void colpire() {
		vita--;         //quando colpisci diminuisce la vita
		if(vita <= 0) { //se la vita è <=0 imposta morto a true
                    morto = true;
		}
		colpito = true;
		colpireTempo = System.nanoTime();
	}
	
        //Al momento dell'esplosione
	public void esplodere() {
		if(rango > 1) {			
			int amount = 0;
			if(tipo == 1) {   //al momento dell esplosione
				amount = 3; //imposta 3 nemici
			}
			if(tipo == 2) {
				amount = 3; //imposta 3 nemici
			}
			if(tipo == 3) {
				amount = 4; //imposta 4 nemici
			}
			
			for(int i = 0; i < amount; i++) {
				Nemico e = new Nemico(getTipo(), getRango() - 1); //assegna alla variabile e, un nemico di un rango inferiore
				e.setPiano(piano);                                
				e.x = this.x; //alla stessa posizione
				e.y = this.y;
				GiocoPannello.nemici.add(e); //aggiunge un nemico di questo tipo
			}
			
		}
		
	}
	
	public void aggiorna() { 
		if(piano) {   //se piano è vero, la velocità diminuisce (slow motion)
			x += dx * 0.3;
			y += dy * 0.3;
		}
		else {
			x += dx;
			y += dy;
		}
		
		if(!pronto) {
			if(x > r && x < GiocoPannello.WIDTH - r && y > r && y < GiocoPannello.HEIGHT - r) {
				pronto = true;
			}
		}
		
		if(x < r && dx < 0) dx = -dx;  //controlla collisione sinistra
		if(y < r && dy < 0) dy = -dy;  //controlla collisione sopra
		if(x > GiocoPannello.WIDTH - r && dx > 0) dx = -dx; //controlla collisione destra
		if(y > GiocoPannello.HEIGHT - r && dy > 0) dy = -dy; //controlla collisione sotto
		
		if(colpito) {
			long elapsed = (System.nanoTime() - colpireTempo) / 1000000;
			if(elapsed > 50) { //quando è maggiore di questo tempo, si riassesta dal bianco
				colpito = false;
				colpireTempo = 0;
			}
		}
		
	}
	
        //Disegna nemici
	public void disegna(Graphics2D g) {
		if(colpito) {
			g.setColor(Color.WHITE);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
		else {
			g.setColor(color1);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
		
	}
	
	
}

























