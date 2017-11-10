package seguridad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Ejemplo de encriptado y desencriptado con algoritmo AES. Se apoya en
 * RSAEncrypt.java para salvar en fichero o recuperar la clave de encriptacion.
 *
 * @author Chuidiang
 *
 */
public class Desencriptar {

    public static void main(String[] args) throws Exception {
        File archivoLlave = new File("llave.txt");
        File textoCifrado = new File("texto.txt");

        // Se obtiene un cifrador AES
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

        System.out.println("Abriendo archivo a leer: " + archivoLlave);
        FileInputStream rawDataFromFile = new FileInputStream(archivoLlave);
        byte[] fileData1 = new byte[(int) archivoLlave.length()];
        rawDataFromFile.read(fileData1);
        
        Key key = new SecretKeySpec(fileData1,"AES");
        aes.init(Cipher.DECRYPT_MODE, key);
                
        System.out.println("Abriendo archivo a leer: " + textoCifrado);
        rawDataFromFile = new FileInputStream(textoCifrado);
        byte[] fileData2 = new byte[(int) textoCifrado.length()];
        rawDataFromFile.read(fileData2);
        
        byte[] desencriptado = aes.doFinal(fileData2);
        
        System.out.println(new String(desencriptado));
    }
}
