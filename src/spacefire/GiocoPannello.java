package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

/**
 *
 * @author FabioDT
 */
import static spacefire.Frame.HEIGHT;
import static spacefire.Frame.WIDTH;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GiocoPannello extends JPanel  {
        //Dimensioni finestra
        public static final int WIDTH= 440;
        public static final int HEIGHT= 540;   
        //Immagine Sfondo
        private Image SfondoImm;  
        //Rettangolo Sfondo
        private Rectangle SfondoRett;
        //Thread di animazione
        private final MoveThread thread;
        //Suoni
        private Sound Preso_nemico;
        private Sound Preso_giocatore;
        private Sound Energia;
        private Sound New_round;
        private Sound Sconfitta;
        private Sound Vittoria;
        //Running
	private boolean running;
        //Perso
        public boolean fine = false;
	private BufferedImage image;
	private Graphics2D g;
        //Utilizzo la classe Frame per gestire i pannelli
        Frame mp;
        private int FPS = 30;
	private double averageFPS;
	//Inizializzazione Arraylist
	public static Giocatore giocatore;
	public static ArrayList<Colpo> colpi;
	public static ArrayList<Nemico> nemici;
	public static ArrayList<Energia> energie;
	public static ArrayList<Esplosione> esplosioni;
	public static ArrayList<Testo> testi;
	private long tempoRound;
	private long tempoRoundDiff;
	public int numRound;
	private boolean startRound;
	private int ritardoRound = 2000;
	private long tempoRall;
	private long tempoRallDiff;
	private int tempoRallLung = 6000;
        public boolean pausa=false;
        //Cursore invisibile
        private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

	public GiocoPannello(Frame aThis) {
                super(); // richiama il costruttore della classe base, senza parametri
                //Attraverso questa chiamata riesco a chiamare gli altri pannelli
                this.mp = aThis;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
                //Creo un nuovo thread
                this.thread = new MoveThread(); //MoveThread è un metodo
                //Carico lo sfondo
                this.SfondoImm = CaricatoreImmagini.load("/images/sfondo3.jpg");
                //Inizializzo il rettangolo dello sfondo
                this.SfondoRett = new Rectangle(0, 0, WIDTH, HEIGHT);
                //Inizializzo i suoni
                this.Preso_nemico = Resources.getSound("/sounds/preso_nemico.wav");
                this.Preso_giocatore = Resources.getSound("/sounds/preso_giocatore.wav");
                this.New_round = Resources.getSound("/sounds/new_round.wav");
                this.Energia = Resources.getSound("/sounds/energia.wav"); 
                this.Sconfitta = Resources.getSound("/sounds/sconfitta.wav");
                this.Vittoria = Resources.getSound("/sounds/vittoria.wav");
                //Nascondo il cursore
                this.setCursor(HIDDEN_CURSOR);  
        }
  
	// Funzioni
        public void addNotify(){
                super.addNotify(); 
                this.addKeyListener(new KeyInput(this)); //Aggiunge l'ascoltatore della tastiera 
                this.setFocusable(true);                 //Permette di farlo funzionare
                this.requestFocusInWindow();             //Permette di farlo funzionare 
	}
        
        private class MoveThread implements Runnable {
        private Thread thread;
        
        public void run() {
                running=true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); /////
		g = (Graphics2D) image.getGraphics(); /////     
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //ANTIALIASING, disegna meglio tutto
      
                //Allocazione Arraylist
		giocatore = new Giocatore();
		colpi = new ArrayList<Colpo>();
		nemici = new ArrayList<Nemico>();
		energie = new ArrayList<Energia>();
		esplosioni = new ArrayList<Esplosione>();
		testi = new ArrayList<Testo>();
		
		tempoRound = 0;
		tempoRoundDiff = 0;
		startRound = true;
		numRound = 0;
		
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
                long targetTime = 1000 / FPS; 		
		int frameCount = 0;
		int maxFrameCount = 30;
                                    
		//GAME LOOP
		while(running) {
			startTime = System.nanoTime(); //variabile nanosecondi
                        if(!pausa){
			aggiornagioco();
			rendergioco(); 
                        }
                                          
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;  //creiamo un altra variabile in nanosecondi
			waitTime = targetTime - URDTimeMillis; //generiamo il tempo di attesa del thread
			
			try {
				Thread.sleep(waitTime);  //per ogni movimento attende waitTime      
			}
			catch(Exception e) {
			}
                                frameCount++;//Aumenta il frame contatore
                                if(frameCount == maxFrameCount) { //se è al massimo (30) lo riazzeriamo
                                    averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                                    frameCount = 0;
                                    totalTime = 0;
			}                  
		}
                if(numRound==9) Vittoria.play(); else Sconfitta.play();
                mp.initPanel(mp.gioco_panel, false);
                mp.initPanel(mp.gameover_panel, true);
        }
        /**
         * Avvia il thread
         */
        public void start() {
            thread = new Thread(this);
            thread.start();
        }

        /**
         * Ferma il thread
         */
        public void stop() {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }
        
        public void setVisible(boolean b) {
            super.setVisible(b);
            if (b) {
                this.thread.start();
            } else {
                this.thread.stop();
            }
        }
   
	private void aggiornagioco() {
            
		// Nuovo round
		if(tempoRound == 0 && nemici.size() == 0) {
			numRound++;
			startRound = false;
			tempoRound = System.nanoTime();
                        New_round.play(); 

		}
		else {
			tempoRoundDiff = (System.nanoTime() - tempoRound) / 1000000;
			if(tempoRoundDiff > ritardoRound) {
				startRound = true;
				tempoRound = 0;
				tempoRoundDiff = 0;
			}
		}	
                
		// Crea nemici
		if(startRound && nemici.size() == 0) {
			creaNuoviNemici();
                           
		}
		// Aggiorna giocatore
		giocatore.aggiorna();
                
		// Aggiorna colpi
		for(int i = 0; i < colpi.size(); i++) {
			boolean remove = colpi.get(i).aggiorna();
			if(remove) {
				colpi.remove(i);
				i--;
			}
		}
		
		// Aggiorna nemici
		for(int i = 0; i < nemici.size(); i++) {
			nemici.get(i).aggiorna();
		}
		
		// Aggiorna energie
		for(int i = 0; i < energie.size(); i++) {
			boolean remove = energie.get(i).aggiorna();
			if(remove) {
				energie.remove(i);
				i--;
			}
		}
		
		// Aggiorna esplosioni
		for(int i = 0; i < esplosioni.size(); i++) {
			boolean remove = esplosioni.get(i).aggiorna();
			if(remove) {
				esplosioni.remove(i);
				i--;
			}
		}
		
		// Aggiorna testo
		for(int i = 0; i < testi.size(); i++) {
			boolean remove = testi.get(i).aggiorna();
			if(remove) {
				testi.remove(i);
				i--;
			}
		}
			
		// Controlla morte nemici
		for(int i = 0; i < nemici.size(); i++) {
			if(nemici.get(i).isMorto()) {
				Nemico e = nemici.get(i);
				
				// Eventuale Energia
				double rand = Math.random(); //genera numero compreso tra 0 e 1
				if(rand < 0.003) 
                                    energie.add(new Energia(1, e.getx(), e.gety())); //aggiunge un Energia di tipo 1
				else if(rand < 0.020) 
                                    energie.add(new Energia(3, e.getx(), e.gety())); //aggiunge un Energia di tipo 2
				else if(rand < 0.120) 
                                    energie.add(new Energia(2, e.getx(), e.gety())); //aggiunge un Energia di tipo 3
				else if(rand < 0.130) 
                                    energie.add(new Energia(4, e.getx(), e.gety())); //aggiunge un Energia di tipo 4
				
				giocatore.aggiungiPunti(e.getTipo() + e.getRango()); //aggiunge i punti, in base alla somma di tipo e rango
				nemici.remove(i); //rimuove nemico
				e.esplodere(); //metodo esplodere nemico, per vedere se aggiungere nemici
				esplosioni.add(new Esplosione(e.getx(), e.gety(), e.getr(), e.getr() + 30)); //aggiunge esplosione
				i--; 
			}
			
		}
		
		// Controlla morte giocatore
 		if(giocatore.isDead()) {
                            running=false;
                            thread.stop();
		}
                
		// Collisione giocatore - nemico
		if(!giocatore.isColpito()) {
			int px = giocatore.getx(); //Assegna la x del giocatore
			int py = giocatore.gety();
			int pr = giocatore.getr();
			for(int i = 0; i < nemici.size(); i++) {
				Nemico e = nemici.get(i);
                                
				double ex = e.getx(); //Assegna la x del nemico
				double ey = e.gety();
				double er = e.getr();
				
				double dx = px - ex; //Fa la differenza tra le x
				double dy = py - ey;
				double dist = Math.sqrt(dx * dx + dy * dy); //Calcola la distanza, la funzione restituisce la radice quadrata
				
				if(dist < pr + er) { //se la distanza è minore della somma dei raggi
					giocatore.perdiVite(); //metodo perdivite di giocatore
                                        Preso_giocatore.play();
                                }
				
			}
		}
                
		// Collisione giocatore - Energia
		int px = giocatore.getx(); //Assegna la x del giocatore
		int py = giocatore.gety();
		int pr = giocatore.getr();
		for(int i = 0; i < energie.size(); i++) {
			Energia p = energie.get(i);
			double x = p.getx(); //Assegna la x dell'Energia
			double y = p.gety();
			double r = p.getr();
			double dx = px - x; //Fa la differenza tra le x
			double dy = py - y;
			double dist = Math.sqrt(dx * dx + dy * dy); //Calcola la distanza, la funzione restituisce la radice quadrata
			
			// Organizza il testo delle energie
			if(dist < pr + r) { //se la distanza è minore della somma dei raggi
				int tipo = p.getTipo(); //ottiene il tipo di Energia
				
				if(tipo == 1) {
					giocatore.aggiungiVite();
					testi.add(new Testo(giocatore.getx(), giocatore.gety(), 2000, "Vita"));
				}
				if(tipo == 2) {
					giocatore.aggiungiEnergia(1);
					testi.add(new Testo(giocatore.getx(), giocatore.gety(), 2000, "Potenza"));
				}
				if(tipo == 3) {
					giocatore.aggiungiEnergia(2);
					testi.add(new Testo(giocatore.getx(), giocatore.gety(), 2000, "Doppia Potenza"));
				}
				if(tipo == 4) {
					tempoRall = System.nanoTime(); //tempo di rallentamento dello slowdown
					for(int j = 0; j < nemici.size(); j++) {
						nemici.get(j).setPiano(true); 
					}
					testi.add(new Testo(giocatore.getx(), giocatore.gety(), 2000, "Slow Down"));
				}		
				energie.remove(i); //rimuove l'Energia
                                Energia.play();
				i--;	
			}			
		}
		
		// Collisione colpo - nemico
		for(int i = 0; i < colpi.size(); i++) {
			Colpo b = colpi.get(i);
			double bx = b.getx(); //assegna x del colpo (tramite metodo getterx di colpo)
			double by = b.gety();
			double br = b.getr();
			
			for(int j = 0; j < nemici.size(); j++) {
                                Nemico e = nemici.get(j);
				double ex = e.getx(); //assegna x del nemico (tramite metodo getterx di nemico)
				double ey = e.gety();
				double er = e.getr();
				
				double dx = bx - ex; //fa la differenza x e l'assegna
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy); //calcola la distanza, la funzione restituisce la radice quadrata
				
				if(dist < br + er) { //se la distanza è minore della somma dei raggi
                                        e.colpire(); //metodo colpire di nemico, lo fa bianco
					colpi.remove(i); //rimuovi colpo
					i--;
                                        Preso_nemico.play();
					break;
				}
			}			
		}
	
		// Aggiorna SlowDown
		if(tempoRall != 0) {
			tempoRallDiff = (System.nanoTime() - tempoRall) / 1000000;
			if(tempoRallDiff > tempoRallLung) {   //se si verifica lo slow down finisce
				tempoRall = 0;
				for(int j = 0; j < nemici.size(); j++) {
					nemici.get(j).setPiano(false);
				}
			}
		}
	}
	
	private void rendergioco() {  
                Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null); //disegna l'immagine che da la base a gioco
		g2.dispose();
		Graphics2D g2D = (Graphics2D) g;
                // Disegno Sfondo gioco
                g2D.drawImage(this.SfondoImm, 0, 0, this.SfondoRett.width, this.SfondoRett.height, getParent());
           
                
                // Disegna Sfondo per SlowDown 
		if(tempoRall != 0) {
			g.setColor(new Color(255, 255, 255, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
                
		// Disegna Giocatore
		giocatore.disegna(g);
		
		// Disegna Colpi
		for(int i = 0; i < colpi.size(); i++) {
			colpi.get(i).disegna(g);
		}
		  
		// Disegna Nemici
		for(int i = 0; i < nemici.size(); i++) {
			nemici.get(i).disegna(g);
		}
	
		// Disegna Energie
		for(int i = 0; i < energie.size(); i++) {
			energie.get(i).disegna(g);
		}
		
		// Disegna Esplosioni
		for(int i = 0; i < esplosioni.size(); i++) {
			esplosioni.get(i).disegna(g);
		}
		
		// Disegna Testi
		for(int i = 0; i < testi.size(); i++) {
			testi.get(i).disegna(g);
		}
		
		// Disegna Numero Round 
		if(tempoRound != 0) {
			g.setFont(new Font("Arial", Font.PLAIN, 18));
			int alpha = (int) (255 * Math.sin(3.14 * tempoRoundDiff / ritardoRound)); //funzione che permette lo schiarimento della scritta
			g.setColor(new Color(255, 255, 255, alpha)); 
			g.drawString("----  R O U N D    " + numRound + "   ----", 110, HEIGHT / 2);
                }
		
		// Disegna Giocatore Vita (alto a sinistra)
                g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Vita: ", 17,27);
		for(int i = 0; i < giocatore.getVite(); i++) {
			g.setStroke(new BasicStroke(3)); //imposta il bordo più spesso
			g.setColor(Color.WHITE);
			g.drawOval(50 + (22 * i), 11, giocatore.getr() * 2, giocatore.getr() * 2); //20*i cosi ogni ciclo li mette in posizioni differenti
			g.setStroke(new BasicStroke(1));
		}
		
		// Disegna scritta Energia (alto a sinistra)
                g.setColor(Color.WHITE);
                g.drawString("Energia: ", 17,47);
		for(int i = 0; i < giocatore.getEnergiaRichiesta(); i++) {
                    	g.setColor(Color.YELLOW);
                        g.setStroke(new BasicStroke(3));
			g.fillRect(76 + (8*i), 39, 8, 8);
                        g.setColor(Color.YELLOW.darker());
                        g.drawRect(76 + (8*i), 39, 8, 8);
		}
		
		// Disegna scritta Punteggio (alto a sinistra)
		g.setColor(Color.WHITE);
		g.drawString("Punteggio: " + giocatore.getPunteggio(), 17, 67);
		
                // Disegna scritta Tempo (alto a sinistra)
                g.setColor(Color.WHITE);
		g.drawString("Tempo: " + giocatore.getTimer(), 17, 86);
                
                // Disegna scritta Round (alto a sinistra)
                g.setColor(Color.WHITE);
                g.drawString("Round: " + numRound , 17, 106);
                
		// Disegna barra Slow Down
		if(tempoRall != 0) {
			g.setColor(Color.WHITE);
			g.drawRect(20, 60, 100, 8); //contorno
			g.fillRect(20, 60, (int) (100 - 100.0 * tempoRallDiff / tempoRallLung), 8); //barra che diminuisce
		}
	}
	
	
	private void creaNuoviNemici() {
		nemici.clear();
                //Al primo round
		if(numRound == 1) {
			for(int i = 0; i < 1; i++) {
				nemici.add(new Nemico(1, 1));
			}
		}
                //Al secondo round 
		if(numRound == 2) {
			for(int i = 0; i < 1; i++) {
				nemici.add(new Nemico(1, 1));
			}
		}
                //Al terzo round 
		if(numRound == 3) {
			for(int i = 0; i < 1; i++) {
				nemici.add(new Nemico(1, 1));
			}
			nemici.add(new Nemico(1, 2));
			//nemici.add(new Nemico(1, 2));
		}
                //Al quarto round 
		if(numRound == 4) {
			nemici.add(new Nemico(1, 3));
			//nemici.add(new Nemico(1, 4));
			for(int i = 0; i < 1; i++) {
				nemici.add(new Nemico(2, 1));
			}
		}
                //Al quinto round 
		if(numRound == 5) {
			nemici.add(new Nemico(1, 4));
			//nemici.add(new Nemico(1, 3));
			//nemici.add(new Nemico(2, 3));
		}
                //Al sesto round 
		if(numRound == 6) {
			//nemici.add(new Nemico(1, 3));
			for(int i = 0; i < 1; i++) {
			nemici.add(new Nemico(2, 1));
			nemici.add(new Nemico(3, 1));
			}
		}
                //Al settimo round
		if(numRound == 7) {
			nemici.add(new Nemico(1, 3));
			//nemici.add(new Nemico(2, 3));
			//nemici.add(new Nemico(3, 3));
		}
                //All'ottavo round
		if(numRound == 8) {
			nemici.add(new Nemico(1, 4));
			//nemici.add(new Nemico(2, 4));
			//nemici.add(new Nemico(3, 4));
		}
                //Al nono round ferma
		if(numRound == 9) {
			running = false; //si ferma il gioco
               }
	}
}
























