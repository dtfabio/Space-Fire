package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import static spacefire.Frame.HEIGHT;
import static spacefire.Frame.WIDTH;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import static spacefire.GiocoPannello.giocatore;

/**
 *
 * @author FabioDT
 */
public class GameOverPannello extends JPanel {
    //Immagini
    private Image Sfondo;
    private Image Rigioca;
    private Image Esci;
    private Image Cursore;
    
    //Suoni
    private Sound Click;
    private Sound Background;
    private Sound Vittoria;
    private Sound Sconfitta;
    
    //Rettangoli
    private Rectangle SfondoRett;
    private Rectangle RigiocaRett;
    private Rectangle EsciRett;
    private Rectangle CursoreRett;

    //Utilizzo variabili Dimension per il cursore, e per i pulsanti start, aiuto ed esci
    private Dimension DimCursore = new Dimension(40, 40);
    private Dimension DimBottone = new Dimension(200, 64);
    
    //Cursore invisibile
    private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

    //Utilizzo la classe Frame per gestire i pannelli
    Frame mp;
    private GiocoPannello gioco;
    
    //Questa classe è utilizzata per creare il pannello menu del videogioco compresa dei pulsanti. 
    //Passo al costruttore una variabile FramePrincipale per richiamare il frame contenitore dei pannelli.
    //parametro aThis: Frame che contiene il pannello
    public GameOverPannello(Frame aThis) {
        //Attraverso questa chiamata riesco a gestire gli altri pannelli
        this.mp = aThis;
        //Carico suoni
        this.Click = Resources.getSound("/sounds/click.wav");
        //Carico le immagini
        this.Sfondo = CaricatoreImmagini.load("/images/sfondo4.jpg");
        this.Rigioca = CaricatoreImmagini.load("/images/bottone.png");
        this.Esci = CaricatoreImmagini.load("/images/bottone.png");
        this.Cursore = CaricatoreImmagini.load("/images/scope.png");
        //Inizializzo il rettangolo dello sfondo
        this.SfondoRett = new Rectangle(0, 0, aThis.WIDTH, aThis.HEIGHT);
        //Inizializzo il rettangolo del pulsante xstart
        this.RigiocaRett = new Rectangle(125 , 325, DimBottone.width, DimBottone.height);// x,  y, larghezza, altezza
       //Inizializzo il rettangolo del pulsante esci
        this.EsciRett = new Rectangle(125, 425, DimBottone.width, DimBottone.height);// x,  y, larghezza, altezza
        //Inizializzo il rettangolo del mirino fuori dalla schermata, l'effettiva dimensione è DimScope
        this.CursoreRett = new Rectangle();
        this.CursoreRett.setSize(Cursore.getWidth(null), Cursore.getHeight(null));
        this.CursoreRett.setLocation(-Cursore.getWidth(null), -Cursore.getHeight(null));
        //Nascondo il cursore
        this.setCursor(HIDDEN_CURSOR);
        //Aggiungo il Mouse movimento ascoltatore
        this.addMouseMotionListener(new MouseMotionAdapter() {
            //Metodo che viene chiamato ogni volta che viene spostato il cursore all'interno del pannello
            //parametro e: variabile che registra l'evento
            @Override
            public void mouseMoved(MouseEvent e) {
                //Imposto il mirino sul cursore e chiamo il metodo repaint()
                CursoreRett.x = e.getPoint().x - CursoreRett.width / 2;
                CursoreRett.y = e.getPoint().y - CursoreRett.height / 2;
               repaint();
            }
        });
        //Aggiungo il Mouse ascoltatore
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {  
                //Ottengo la posizione del Mouse
                Point[] coordinate = new Point[2];
                coordinate[0] = e.getPoint();
                coordinate[1] = e.getPoint();
                //prendo x e y, le coordinate

                //Ottengo le coordinate del Click rispetto a Rigioca e Exit
                coordinate[0].x -= RigiocaRett.x;
                coordinate[0].y -= RigiocaRett.y;
                coordinate[1].x -= EsciRett.x;
                coordinate[1].y -= EsciRett.y;
                
                //Invoco la funzione Contains
                //Controllo se il Click è interno od esterno ad i due rettangoli grazie al containsStart o containsEsci                
                this.containsMenu(RigiocaRett, coordinate[0].x, coordinate[0].y);
                this.containsEsci(EsciRett, coordinate[1].x, coordinate[1].y);
            }

            private void containsMenu(Rectangle pulsante, int x, int y) {
                //Controllo se il Click non centra il pulsante Rigioca
                if (x < 0 || y < 0 || x >= pulsante.width || y >= pulsante.height) {
                    //se non centro il pulsante Rigioca   
                } else {
                    Click.play();
                    mp.initPanel(mp.gameover_panel, false);
                    mp.initPanel(mp.gioco_panel, true);
                }
            }
            
            private void containsEsci(Rectangle pulsante, int x, int y) {
                 //Controllo se il Click non centra il pulsante Esci
                if (x < 0 || y < 0 || x >= pulsante.width || y >= pulsante.height) {
                    //Non eseguo alcun suono 
                } else {
                    //Esce dal gioco
                    System.exit(0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
 
        //Disegno lo sfondo
        g2D.drawImage(this.Sfondo, 0, 0, this.SfondoRett.width, this.SfondoRett.height, getParent());
        //Disegno il pulsante start
        g2D.drawImage(this.Rigioca, this.RigiocaRett.x, this.RigiocaRett.y, this.RigiocaRett.width, this.RigiocaRett.height, getParent());
        //Disegno il pulsante esci
        g2D.drawImage(this.Esci, this.EsciRett.x, this.EsciRett.y, this.EsciRett.width, this.EsciRett.height, getParent());
        //Disegno il mirino
        g2D.drawImage(this.Cursore, this.CursoreRett.x, this.CursoreRett.y, this.DimCursore.width, this.DimCursore.height, this);
        Font fnt= new Font("arial",1,30);
        g.setFont(fnt);
        g.setColor(Color.white);
        if(mp.gioco_panel.numRound==9){
                g.drawString("H A I   V I N T O",110, 190);   //scrive stringa hai vinto
        }else{
                g.drawString("H A I   P E R S O",110, 190);   //scrive stringa hai perso
        }
        g.drawString ("Punteggio: " + mp.gioco_panel.giocatore.getPunteggio(), 130, 230); //scrive stringa punteggio
        g.drawString ("In " + mp.gioco_panel.giocatore.getTimer() + " secondi.", 130, 263); //scrive stringa tempo
        g.setColor(Color.black);
        g.drawString("Rigioca",168,370);
        g.drawString("Esci",185,470);
    }
}
