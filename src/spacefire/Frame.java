package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author FabioDT
 */
public class Frame extends JFrame {
    MenuPannello menu_panel = new MenuPannello(this); //dichiarazione e allocazione 
    GiocoPannello gioco_panel = new GiocoPannello(this); //dichiarazione e allocazione 
    AiutoPannello aiuto_panel = new AiutoPannello(this); //dichiarazione e allocazione 
    GameOverPannello gameover_panel = new GameOverPannello(this); //dichiarazione e allocazione 
    
    Container c;
    
    public static final int WIDTH= 440;
    public static final int HEIGHT= 540;  
    
    public Frame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Imposta operazione per la chiusura della finestra
        setSize(WIDTH, HEIGHT); //Imposta la dimensione dell finestra
        c = getContentPane();   
        setVisible(true);  //Imposta visibilità
        setResizable(false); //Imposta non modificabilità
       
        this.initPanel(this.menu_panel, true);  //Attiva il pannello iniziale
        this.initPanel(this.gioco_panel, false);
        this.initPanel(this.aiuto_panel, false);
        this.initPanel(this.gameover_panel, false);
 }

    //questo metodo dà la possibilità di fare lo switch tra i vari pannelli
    //1parametro pannello: il pannello che passo al metodo
    //2parametro b: variabile booleana che mi rende visibile/invisibile il pannello
    public void initPanel(JPanel pannello, boolean b) {
        pannello.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        pannello.setVisible(b); //Imposta visibilità del pannello
        this.add(pannello); //Aggiunge pannello al frame
    }
}
