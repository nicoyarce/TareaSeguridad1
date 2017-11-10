import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Ejemplo de encriptado y desencriptado con algoritmo AES.
 * Se apoya en RSAEncrypt.java para salvar en fichero
 * o recuperar la clave de encriptacion.
 * 
 * @author Chuidiang
 *
 */
public class Encriptar {

  public static void main(String[] args) throws Exception {
    // Generamos una clave de 128 bits adecuada para AES
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    Key key = keyGenerator.generateKey();

    File archivoLlave = new File("llave.txt");
    File textoCifrado = new File("texto.txt");

    // Texto a encriptar
    String texto = "Hola1";
    System.out.println("Texto plano:"+texto);
      
    // Se obtiene un cifrador AES
    Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

    // Se inicializa para encriptacion y se encripta el texto,
    // que debemos pasar como bytes.
    aes.init(Cipher.ENCRYPT_MODE, key);
    byte[] encriptado = aes.doFinal(texto.getBytes());

    // Se escribe byte a byte en hexadecimal el texto
    // encriptado para ver su pinta.
    /*System.out.print("Texto encriptado:");
    for (byte b : encriptado) {
       System.out.print(Integer.toHexString(0xFF & b));
    }
    System.out.println();*/

    System.out.println("Abriendo archivo llave: "+archivoLlave);

    byte[] fileData = key.getEncoded();

    FileOutputStream alArchivo = new FileOutputStream(archivoLlave);
    System.out.println("Escribiendo Datos");
    alArchivo.write(fileData);

    ////////////////////////////////////////////////////////////////////
    
    System.out.println("Abriendo archivo texto cifrado: "+textoCifrado);

    fileData = encriptado;

    alArchivo = new FileOutputStream(textoCifrado);
    System.out.println("Escribiendo Datos");
    alArchivo.write(fileData);     
  }
}
