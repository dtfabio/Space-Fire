package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import static spacefire.GiocoPannello.giocatore;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author FabioDT
 */
    public class KeyInput extends KeyAdapter{
    private boolean[] keyDown = new boolean[4]; //con questo array booleano il movimento del giocatore è più fluido senza intoppi, se premo due tasti contemporaneamente non funziona bene
    GiocoPannello giocopannello;
    Frame mp;
    
    public KeyInput(GiocoPannello giocopannello){
        this.giocopannello = giocopannello;
    }
    
    public void keyTyped(KeyEvent e) {}
  
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
                //evento per giocatore 
                if(keyCode == KeyEvent.VK_W) {
			giocatore.setSopra(true);                  
		}                		
		if(keyCode == KeyEvent.VK_S) {
			giocatore.setSotto(true);
		}
                if(keyCode == KeyEvent.VK_D) {
			giocatore.setDestra(true);
		}
                if(keyCode == KeyEvent.VK_A) {
			giocatore.setSinistra(true);
		}
		if(keyCode == KeyEvent.VK_M) {
			giocatore.setSparare(true);
		} 
                if(keyCode == KeyEvent.VK_P) {
			if(giocopannello.pausa)giocopannello.pausa=false;
                        else giocopannello.pausa=true;
		} 
    } 

    public void keyReleased(KeyEvent e){ //evento per quando il tasto viene rilasciato
        int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_W) {
			giocatore.setSopra(false);
		}
		if(keyCode == KeyEvent.VK_S) {
			giocatore.setSotto(false);
		}
                if(keyCode == KeyEvent.VK_D) {
			giocatore.setDestra(false);
		}
                if(keyCode == KeyEvent.VK_A) {
			giocatore.setSinistra(false);
		}
		if(keyCode == KeyEvent.VK_M) {
                        giocatore.setSparare(false);
		}
	}
}
         
 