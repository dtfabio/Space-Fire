package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author FabioDT
 */
public class CaricatoreImmagini {
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public static Image load(String name) {
        try {
            return ImageIO.read(CaricatoreImmagini.class.getResource(name)); //getresource -> percorso dell'immagine . . . solo se stanno nello stesso package immagine e questo metodo!
        }catch(Exception e) {
            return null;
        }   
    } 
}
