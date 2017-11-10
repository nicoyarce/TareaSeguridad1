
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Ejemplo sencillo de encriptado/desencriptado con algoritmo RSA. Se comenta
 * tambien como guardar las claves en fichero y recuperarlas despues.
 * 
 * @author Chuidiang
 */
public class RSAEncrypt {
   private static Cipher rsa;

   public static void main(String[] args) throws Exception {
      // Generar el par de claves
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      PublicKey publicKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();

      // Obtener la clase para encriptar/desencriptar
      rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      // Texto a encriptar
      String text = "Texto a encriptar";
      System.out.println("texto plano:"+text);

      // Se encripta
      rsa.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encriptado = rsa.doFinal(text.getBytes());

      // Escribimos el encriptado para verlo, con caracteres visibles
      System.out.print("texto encriptado::");
      for (byte b : encriptado) {
         System.out.print(Integer.toHexString(0xFF & b));
      }
      System.out.println();

      // Se desencripta
      rsa.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] bytesDesencriptados = rsa.doFinal(encriptado);
      String textoDesencripado = new String(bytesDesencriptados);

      // Se escribe el texto desencriptado
      System.out.println("Texto desencriptado:"+textoDesencripado);

   }
}
