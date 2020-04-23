package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;//
import java.awt.Point;//
import java.awt.Rectangle;  //
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author FabioDT
 */
public class AiutoPannello extends JPanel {
        //Immagini
        private Image Indietro;
        private Image Sfondo;
        private Image Cursore;
        
        //Suono
        private Sound Click;
        
        //Rettangoli    
        private Rectangle IndietroRett;
        private Rectangle SfondoRett;
        private Rectangle CursoreRett;
        
        //Imposto dimensione,Utilizzo variabile Dimension per il cursore e il pulsante indietro
        private Dimension DimCursore = new Dimension(40, 40);
        private Dimension DimIndietro = new Dimension(200, 64);
        
        //Cursore invisibile
        private static Cursor HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");

        //Utilizzo la classe Frame per gestire i pannelli
        Frame mp;
        
        //Questa classe è utilizzata per creare il pannello aiuto del videogioco compresa dei pulsanti. 
        //Passo al costruttore una variabile FramePrincipale per richiamare il frame contenitore dei pannelli.
        //parametro aThis: Frame che contiene il pannello
        public AiutoPannello(Frame aThis){
            //Attraverso questa chiamata riesco a gestire gli altri pannelli
            this.mp = aThis;
            //Carico il suono
            this.Click = Resources.getSound("/sounds/click.wav");
            //Carico le immagini
            this.Sfondo = CaricatoreImmagini.load("/images/sfondo2.jpg");
            this.Indietro = CaricatoreImmagini.load("/images/bottone.png");
            this.Cursore = CaricatoreImmagini.load("/images/scope.png");
            
            //Inizializzo il rettangolo dello sfondo
            this.SfondoRett = new Rectangle(0, 0, mp.WIDTH, mp.HEIGHT);
            //Inizializzo il rettangolo del pulsante indietro
            this.IndietroRett = new Rectangle(125 , 250, DimIndietro.width, DimIndietro.height); // x,  y, larghezza, altezza
            //Inizializzo il rettangolo del mirino fuori dalla schermata, l'effettiva dimensione è DimScope
            this.CursoreRett = new Rectangle();
            this.CursoreRett.setSize(Cursore.getWidth(null), Cursore.getHeight(null));
            this.CursoreRett.setLocation(-Cursore.getWidth(null), -Cursore.getHeight(null));
            
            //Nasconde freccia cursore
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
            
            //Aggiungo il Mouse movimento ascoltatore
            this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {  
                //Ottengo la posizione del Mouse
                Point[] coordinate = new Point[1];
                coordinate[0] = e.getPoint();
                //prendo x e y, le coordinate

                //Ottengo le coordinate del Click rispetto a Start e Exit
                coordinate[0].x -= IndietroRett.x;
                coordinate[0].y -= IndietroRett.y;
                
                //Controllo se il Click è interno od esterno ad i due rettangoli grazie al containsIndietro
                this.containsIndietro(IndietroRett, coordinate[0].x, coordinate[0].y);
            }
            
            private void containsIndietro(Rectangle pulsante, int x, int y) {
                 //Controllo se il Click non centra il pulsante Indietro
                if (x < 0 || y < 0 || x >= pulsante.width || y >= pulsante.height) {
                    //se NON centro il pulsante indietro
                } else {
                    //se centro il pulsante
                    Click.play();
                    mp.initPanel(mp.aiuto_panel, false);
                    mp.initPanel(mp.menu_panel, true);
                }
            }
            });
        }
        
        protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        //Disegno lo sfondo
        /////////////(immagine   , x, y, larghezza            ,altezza                ,restituisce il genitore)
        g2D.drawImage(this.Sfondo, 0, 0, this.SfondoRett.width, this.SfondoRett.height, getParent());
        //Disegno il pulsante indietro
        g2D.drawImage(this.Indietro, this.IndietroRett.x, this.IndietroRett.y, this.IndietroRett.width, this.IndietroRett.height, getParent());
        //Disegno il mirino
        g2D.drawImage(this.Cursore, this.CursoreRett.x, this.CursoreRett.y, this.DimCursore.width, this.DimCursore.height, this);
        
        
        Font fnt1= new Font("arial",1,30); // per il tasto indietro
        Font fnt2= new Font("arial",1,16); // per le scritte di istruzioni
        
        g.setFont(fnt1);   
        g.setColor(Color.BLACK);
        g.drawString("Indietro",170,294);
        
        g.setFont(fnt2); 
        g.setColor(Color.WHITE);
        g.drawString("Usa i seguenti tasti della tastiera per poter giocare:",30,30);
        g.drawString("W = Sopra | S = Sotto | A = Sinistra | D = Destra",30,130);   
        g.drawString("M = Spara | P = Pausa ",30,180);   
        } 
}

