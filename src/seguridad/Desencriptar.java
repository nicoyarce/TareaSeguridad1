package seguridad;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Desencriptar {

    public static void main(String[] args) throws Exception {

        File entrada = new File("encriptado.txt");
        // Se obtienen los cifradores a usar
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        
        // Se lee el archivo encriptado
        System.out.println("Abriendo archivo a leer: " + entrada);
        FileInputStream fis = new FileInputStream(entrada);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String strLine;
        int nLinea = 0;
        byte[] ivArray = null, llaveDeSesionArray = null, hashMensajeArray = null, mensajeArray = null;
        // La lectura es linea por linea
        while ((strLine = br.readLine()) != null) {
            switch (nLinea) {
                case 0:
                    // Se obtiene el iv del vector de inicializacion
                    ivArray = new byte[strLine.length()];
                    ivArray = Base64.getDecoder().decode(strLine);
                    break;
                case 1:
                    // Se obtiene la llave de sesion
                    llaveDeSesionArray = new byte[strLine.length()];
                    llaveDeSesionArray = Base64.getDecoder().decode(strLine);
                    break;
                case 2:
                    // Se obtiene el hash del mensaje encriptado
                    hashMensajeArray = new byte[strLine.length()];
                    hashMensajeArray = Base64.getDecoder().decode(strLine);
                    break;
                case 3:
                    // Se obtiene el mensaje en si
                    mensajeArray = new byte[strLine.length()];
                    mensajeArray = Base64.getDecoder().decode(strLine);
                    break;
            }
            nLinea++;
        }

        // Se carga el almacen de llaves del amigo con su llave privada
        File archivoAlmacen = new File("C:\\Users\\Nicoyarce\\amigo.jks");
        String password = "holas1";
        FileInputStream is = new FileInputStream(archivoAlmacen);
        KeyStore almacen = KeyStore.getInstance("JKS");        
        almacen.load(is, password.toCharArray());
        
        // Carga de llave privada para desencriptar llave de sesion
        Key llavePrivada = (PrivateKey) almacen.getKey("llaveAmigo", password.toCharArray());

        // Se desencripta la llave de sesion
        rsa.init(Cipher.DECRYPT_MODE, llavePrivada);
        byte[] llaveDeSesionDes = rsa.doFinal(llaveDeSesionArray);

        // Se desencripta el texto
        Key llaveDeSesion = new SecretKeySpec(llaveDeSesionDes, "AES");
        aes.init(Cipher.DECRYPT_MODE, llaveDeSesion, new IvParameterSpec(ivArray));
        byte[] desencriptado = aes.doFinal(mensajeArray);

        //Generacion de hash del mensaje
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(desencriptado);
        byte[] hashCalculado = md.digest();

        // Comprobacion de firma digital
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(abrirCertificado("C:\\Users\\Nicoyarce\\certificado.cer"));
        sig.update(hashCalculado);
        boolean hashVerified = sig.verify(hashMensajeArray);
             
        if (hashVerified) {
            System.out.println("Autor correcto");
        }else
            System.out.println("Autor no puede ser confirmado");
        
        System.out.println("Mensaje original: " + new String(desencriptado));
        
        // Guarda string desencriptado en archivo
        System.out.println("Guardando texto desencriptado");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter, true);
        writer.println(new String(desencriptado));        
        File salida = new File("desencriptado.txt");
        FileOutputStream alArchivo = new FileOutputStream(salida);
        alArchivo.write(stringWriter.toString().getBytes());
    }
    // Funcion para abrir certificados utilizando una truststore "dummy"
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
