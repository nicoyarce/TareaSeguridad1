
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

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
        // Almacen de llaves
        File archivoAlmacen = new File("C:\\Users\\Usuario\\almacen.jks");
        FileInputStream is = new FileInputStream(archivoAlmacen);
        KeyStore almacen = KeyStore.getInstance("JKS");
        String password = "holas1";
        almacen.load(is, password.toCharArray());
        
        // Texto desde archivo a encriptar
        File textoCifrado = new File("texto.txt"); 
        byte[] datos = new byte[(int) textoCifrado.length()];        
        is = new FileInputStream(textoCifrado);
        is.read(datos);
        String texto = new String(datos);        
        System.out.println("Texto plano:" + texto);

        // Generamos llave de sesion AES de 128 bits
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key llaveDeSesion = keyGenerator.generateKey();

        // Se obtiene un cifrador AES que usa CBC
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[aes.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        
        // Se inicializa para encriptacion y se encripta el texto,
        // que debemos pasar como bytes.
        aes.init(Cipher.ENCRYPT_MODE, llaveDeSesion, ivParams);
        byte[] encriptado = aes.doFinal(texto.getBytes());     
        
        //Generacion de hash del mensaje
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(texto.getBytes());
        byte[] hash = md.digest();
        
        // Encriptar con llave publica RSA del compañero
        
        // FALTA
        
        // Firmar digitalmente el hash del mensaje        
        Key llavePrivada = almacen.getKey("llaves", password.toCharArray());
        byte[] encodedKey = Base64.getEncoder().encode(llavePrivada.getEncoded());
        
        
        FileOutputStream alArchivo = new FileOutputStream(textoCifrado);
        System.out.println("Escribiendo Datos");
        alArchivo.write(encriptado);   
    }
}
