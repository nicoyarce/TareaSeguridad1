
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Ejemplo de encriptado y desencriptado con algoritmo AES. Se apoya en
 * RSAEncrypt.java para salvar en fichero o recuperar la clave de encriptacion.
 *
 * @author Chuidiang
 *
 */
public class Encriptar {

    public static void main(String[] args) throws Exception {        
        File textoCifrado = new File("texto.txt");        

        // Texto a encriptar
        String texto = "Hola1";
        System.out.println("Texto plano:" + texto);

        // Generamos llave de sesion AES de 128 bits
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();

        // Se obtiene un cifrador AES que usa CBC
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[aes.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        
        //Generacion de hash del mensaje
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(texto.getBytes());
        byte[] hash = md.digest();
        

        // Se inicializa para encriptacion y se encripta el texto,
        // que debemos pasar como bytes.
        aes.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] encriptado = aes.doFinal(texto.getBytes());           
        
        FileOutputStream alArchivo = new FileOutputStream(textoCifrado);
        System.out.println("Escribiendo Datos");
        alArchivo.write(encriptado);   
    }
}
