package spacefire;
/*
 * (C) Copyright 2016
 * dtfabio96 
 * Projects 2015/2016
 */

public class SoundException extends Exception {

    SoundException(String string) {
        super(string);
    }

    public SoundException(String fileName, Throwable cause) {
        super("Cannot read/play " + fileName + ": " + cause.getMessage());
    }

    public SoundException(Throwable cause) {
        super(cause);
    }
}