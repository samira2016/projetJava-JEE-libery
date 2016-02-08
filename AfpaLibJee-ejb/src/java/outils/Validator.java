/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outils;

/**
 *
 * @author fafiec103
 */
public class Validator {
    
    public static boolean validNom(String nom){
        
        if(nom.trim().length()==2){
          return false;
        
        
        }
        return true ;
    
    
    
    
    }
     public static boolean validPrenom(String prenom){
        
        if(prenom.trim().length()==2){
          return false;
        
        }
        return true ;
    
    }
     public static boolean validLogin(String login){
        
        if(login!=null){
            
          String regex = "[a-z0-9._+-]+@[a-z0-9.-]+\\.[a-z]{2,6}";
            if (login.matches(regex)) {
                return true;
            }
        }
        return false ;
    
    
    }
     public static boolean validPassword(String password){
        
        if(password!=null){
            
          
            if (password.length() >=6 && password.length() <=12) {
           
               return true;
            }
        }
        return false ;
    
    
    }
     public static boolean validPhone(String phone){
        
        if(phone!=null){
            
          
            if (phone.length() >=6 && phone.length() <=12) {
           
               return true;
            }
        }
        return false ;
    
    
    }
     
    
}
