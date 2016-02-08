/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outils;

import java.util.HashMap;

/**
 *
 * @author fafiec103
 */
public class ErreurMessage extends Exception{
    
    
    private HashMap<String,String>mapErr;
    
    
    
    public ErreurMessage(HashMap<String, String> mg, String message) {
        super(message);
        this.mapErr = mg;
    }

    public ErreurMessage(String message) {
        super(message);
    }

    public ErreurMessage(HashMap<String, String> mg) {
        this.mapErr = mg;
    }

    public ErreurMessage() {
    }
    
    

    public HashMap<String,String> getMapErr() {
        return mapErr;
    }

    public void setMapErr(HashMap<String,String> mg) {
        this.mapErr = mg;
    }

    
    
    
}
