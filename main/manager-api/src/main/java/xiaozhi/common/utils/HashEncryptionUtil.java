package xiaozhi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
public class HashEncryptionUtil {
    
    public static String Md5hexDigest(String context){
        return hexDigest(context,"MD5");
    }

    
   public static String hexDigest(String context,String algorithm ){

       MessageDigest md = null;
       try {
           md = MessageDigest.getInstance(algorithm);
       } catch (NoSuchAlgorithmException e) {
           log.error("Failed encryption algorithm: {}",algorithm);
           throw new RuntimeException("，"+ algorithm +"");
       }

       byte[] messageDigest = md.digest(context.getBytes());

       StringBuilder hexString = new StringBuilder();
       for (byte b : messageDigest) {
           String hex = Integer.toHexString(0xFF & b);
           if (hex.length() == 1) {
               hexString.append('0');
           }
           hexString.append(hex);
       }
       return hexString.toString();
   }

}
