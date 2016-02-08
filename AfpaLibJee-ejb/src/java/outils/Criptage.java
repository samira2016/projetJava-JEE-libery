package outils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author fafiec103
 */
public class Criptage {

   // private String code;

//    public Criptage(String md5) {
//        Passe(md5);
//        
//    }

    public  static String  Passe(String pass) {
        byte[] passBytes = pass.getBytes();
        String code;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(passBytes);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(passBytes);
            BigInteger number = new BigInteger(1, messageDigest);
            code = number.toString(16);
            return code;
        } catch (NoSuchAlgorithmException e) {
            throw new Error("invalid JRE: have not 'MD5' impl.", e);
        }
    }

   

}
