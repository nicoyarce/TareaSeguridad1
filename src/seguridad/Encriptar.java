package seguridad;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class Encriptar {

    public static void main(String[] args) throws Exception {
        // Carga de Almacen de llaves
        File archivoAlmacen = new File("C:\\Users\\Nicoyarce\\almacen.jks");
        String password = "holas1";
        FileInputStream is = new FileInputStream(archivoAlmacen);
        KeyStore almacen = KeyStore.getInstance("JKS");        
        almacen.load(is, password.toCharArray());

        // Texto desde archivo a encriptar
        File textoCifrado = new File("mensaje.txt");
        if (textoCifrado.exists()) {
            System.out.println("Archivo con texto a encriptar encontrado");
        } else {
            System.out.println("Archivo con texto a encriptar no encontrado");
            System.exit(0);
        }
        byte[] datos = new byte[(int) textoCifrado.length()];
        is = new FileInputStream(textoCifrado);
        is.read(datos);
        String texto = new String(datos);
        System.out.println("Texto a encriptar: " + texto);

        // Generamos llave de sesion AES de 128 bits
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key llaveDeSesion = keyGenerator.generateKey();

        // Se obtiene un cifrador AES que usa CBC
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Se genera el IV
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[aes.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        // Se inicializa para encriptacion y se encripta el texto, que debemos
        // pasar como bytes.
        aes.init(Cipher.ENCRYPT_MODE, llaveDeSesion, ivParams);
        byte[] encriptado = aes.doFinal(texto.getBytes());

        //Generacion de hash del mensaje
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(texto.getBytes());
        byte[] hash = md.digest();

        // Encriptar con llave publica RSA del compañero
        Certificate certificadoAmigo = abrirCertificado("C:\\Users\\Nicoyarce\\certificadoAmigo.cer");
        Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsa.init(Cipher.ENCRYPT_MODE, certificadoAmigo);
        byte[] llaveSesionCifrada = rsa.doFinal(llaveDeSesion.getEncoded());

        // Firmar digitalmente el hash del mensaje        
        Key llavePrivada = almacen.getKey("llave", password.toCharArray());
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign((PrivateKey) llavePrivada);
        sig.update(hash);
        byte[] hashFirmado = sig.sign();

        // Armando archivo de salida        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter, true);
        writer.println(new String(Base64.getEncoder().encode(iv)));
        writer.println(new String(Base64.getEncoder().encode(llaveSesionCifrada)));
        writer.println(new String(Base64.getEncoder().encode(hashFirmado)));
        writer.print(new String(Base64.getEncoder().encode(encriptado)));
        File salida = new File("encriptado.txt");
        FileOutputStream alArchivo = new FileOutputStream(salida);
        alArchivo.write(stringWriter.toString().getBytes());
    }

    public static Certificate abrirCertificado(String ruta) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        File f = new File(ruta);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null);
        FileInputStream fis = new FileInputStream(f);
        byte[] datos = new byte[(int) f.length()];
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        while (bis.available() > 0) {
            Certificate cert = cf.generateCertificate(bis);
            trustStore.setCertificateEntry("fiddler" + bis.available(), cert);
            return cert;
        }
        return null;
    }
}
