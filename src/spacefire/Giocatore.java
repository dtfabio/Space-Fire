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
public class Giocatore {
	private int x;
	private int y;
	private int r;
	private int dx;
	private int dy;
	private int velocità;
	private boolean sinistra;
	private boolean destra;
	private boolean sopra;
	private boolean sotto;
	private boolean sparare;
	private long sparareTempo;
	private long sparareRitardo;	
	private boolean colpito;
	private long recuperareTempo;	
	private int vite;
	private Color colore1;
	private Color colore2;
	private int punteggio;
        private long time;
        private Sound Sparo;
	private int energiaLivello;
	private int energia;
	private int[] energiaRichiesta = {1, 2, 3, 4, 5};
	
	// COSTRUTTORE
	public Giocatore() {
		x = GiocoPannello.WIDTH / 2;        //POSIZIONE INIZIALE
		y = GiocoPannello.HEIGHT / 2;
		r = 9; //raggio
		dx = 0;
		dy = 0;
		velocità = 5;
		vite = 3;
		colore1 = Color.WHITE;
		colore2 = Color.RED;
		sparare = false;
		sparareTempo = System.nanoTime();
		sparareRitardo = 200;
		colpito = false; //colpito è falso all inizio
		recuperareTempo = 0;
		punteggio = 0;
                this.Sparo = Resources.getSound("/sounds/sparo.wav");
         }
	
	// FUNZIONI GETTER
	public int getx() { 
            return x;
        }
	public int gety() { 
            return y; 
        }
	public int getr() { 
            return r; 
        }
	public int getPunteggio() { 
            return punteggio; 
        }
	public int getVite() { 
            return vite; 
        }	
	public boolean isDead() { 
            return vite <= 0; 
        }
	public boolean isColpito() { 
            return colpito; 
        }
        public int getEnergiaLivello() { 
            return energiaLivello; 
        }
	public int getEnergia() { 
            return energia; 
        }
	public int getEnergiaRichiesta() { 
            return energiaRichiesta[energiaLivello]; 
        }
        public String getTimer() {
            int minutes = (int) (time / 3600);
            int seconds = (int) ((time % 3600) / 60);
            return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	//FUNZIONI SETTER
	public void setSinistra(boolean b) { 
            sinistra = b; 
        }
	public void setDestra(boolean b) { 
            destra = b; 
        }
	public void setSopra(boolean b) { 
            sopra = b; 
        }
	public void setSotto(boolean b) { 
            sotto = b;
        }
	public void setSparare(boolean b) { 
            sparare = b; 
        }
	public void aggiungiPunti(int i) { 
            punteggio += i; 
        }
	public void aggiungiVite() {
		vite++;
	}	
	public void perdiVite() {
		vite--;
		colpito = true;
		recuperareTempo = System.nanoTime();
	}
	public void aggiungiEnergia(int i) {
		energia += i;
		if(energiaLivello == 4) {
			if(energia > energiaRichiesta[energiaLivello]) {
				energia = energiaRichiesta[energiaLivello];
			}
			return;
		}
		if(energia >= energiaRichiesta[energiaLivello]) {
			energia -= energiaRichiesta[energiaLivello];
			energiaLivello++;
		}
	}
        
	public void aggiorna() {
                //variabili booleane che permettono il movimento del giocatore
                time++;
		if(sinistra==true) {
			dx = -velocità;
		}
		if(destra==true) {
			dx = velocità;
		}
		if(sopra==true) {
			dy = -velocità;
		}
		if(sotto==true) {
			dy = velocità;
		}
		
		x += dx; //permette di muovere il giocatore sulla x
		y += dy; //permette di muovere il giocatore sulla y
		
		if(x < r) x = r;    //controlla collisione a sinistra
		if(y < r) y = r;    //controlla collisione sopra
		if(x > GiocoPannello.WIDTH - r) x = GiocoPannello.WIDTH - r; //controlla collisione destra
		if(y > GiocoPannello.HEIGHT - r) y = GiocoPannello.HEIGHT - r; //controlla collisione basso
		
		dx = 0;
		dy = 0; 
		
		//sparare
		if(sparare) {
			long elapsed = (System.nanoTime() - sparareTempo) / 1000000;	
			if(elapsed > sparareRitardo) { //quando è maggiore di questo tempo, spara i colpi
				sparareTempo = System.nanoTime();
				if(energiaLivello < 2) { 
					GiocoPannello.colpi.add(new Colpo(270, x, y));  //spara a 270 gradi
				}
				else if(energiaLivello < 4) { //spara 2 colpi
					GiocoPannello.colpi.add(new Colpo(270, x + 5, y));
					GiocoPannello.colpi.add(new Colpo(270, x - 5, y));
				}
				else { //spara 3 colpi
					GiocoPannello.colpi.add(new Colpo(270, x, y)); 
					GiocoPannello.colpi.add(new Colpo(275, x + 5, y));
					GiocoPannello.colpi.add(new Colpo(265, x - 5, y));
				}
                                Sparo.play();  
			}
		}
		
		if(colpito) { //se è colpito
			long elapsed = (System.nanoTime() - recuperareTempo) / 1000000;
			if(elapsed > 2000) { //quando è maggiore di questo tempo, aggiorni il giocatore come prima
				colpito = false;
				recuperareTempo = 0;
			}
		}
		
	}
	
        //disegna giocatore
	public void disegna(Graphics2D g) {
		if(colpito) {
			g.setColor(colore2);
			g.fillRect(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
			g.setColor(colore2.darker());
			g.drawRect(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
		else {
               
			g.setColor(colore1);
			g.fillRect(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
			g.setColor(colore1.darker());
			g.drawRect(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
		
	}
	
}
















