package outils;

import java.util.HashMap;

public class CustomException extends Exception{
    public static final int ERR_USER = 100;
    public static final int ERR_SQL = 200;
    
    private int numError;
    private HashMap<String, String> mp;

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
           
    public CustomException(String message,HashMap<String, String> mp) {
        super(message);
        this.mp = mp;
    }

    public CustomException(int numError, String message) {
        super(message);
        this.numError = numError;
    }

    public CustomException(int numError, HashMap<String, String> mp, String message) {
        super(message);
        this.numError = numError;
        this.mp = mp;
    }
    
    
    
    public HashMap<String, String> getMp() {
        return mp;
    }

    public void setMp(HashMap<String, String> mp) {
        this.mp = mp;
    }   

    public int getNumError() {
        return numError;
    }

    public void setNumError(int numError) {
        this.numError = numError;
    }
    
    
}
