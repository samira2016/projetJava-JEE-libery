
package outilsWar;

import beansession.GestionPanierLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class PanierWar implements Serializable{
    GestionPanierLocal gestionPanier;
 
    public PanierWar() {
        gestionPanier = lookupGestionPanierLocal();
    }

    public GestionPanierLocal getGestionPanier() {
        return gestionPanier;
    }
    
    
    private GestionPanierLocal lookupGestionPanierLocal() {
        try {
            Context c = new InitialContext();
            return (GestionPanierLocal) c.lookup("java:global/AfpaLibJee/AfpaLibJee-ejb/GestionPanier!beansession.GestionPanierLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
