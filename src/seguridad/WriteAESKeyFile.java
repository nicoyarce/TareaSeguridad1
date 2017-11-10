

/* Este programa es una demostración de la lectura y escritura de archivos en Java.
  * Se lee el archivo dado como argumento de la línea de comandos 
  * Y lo escribe en el archivo dado como segundo argumento.
  */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class WriteAESKeyFile {

	public static void main(String[] args) {

		File outFile = new File(args[0]);
		try {

			Key secreto = new SecretKeySpec("llave secreta de prueba".getBytes(),  0, 16, "AES");

					  // Texto a encriptar
			String texto = "Este es el texto que queremos encriptar";
			System.out.println("Texto plano:"+texto);
	
			// Se obtiene un cifrador AES
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

			// Se inicializa para encriptacion y se encripta el texto,
			// que debemos pasar como bytes.
			aes.init(Cipher.ENCRYPT_MODE, secreto);
			byte[] encriptado = aes.doFinal(texto.getBytes());
			
			byte[] encoded_key = secreto.getEncoded();
			System.out.println("Abriendo archivo para escribir: "+outFile);
			FileOutputStream outToFile = new FileOutputStream(outFile);

			System.out.println("Escribiendo llave secreta en archivo");
			outToFile.write(encoded_key);
	
			System.out.println("Cerrando Archivos");
			outToFile.close();

			//leer llave desde archivo
			Key llave_leida = new SecretKeySpec(encoded_key,"AES");

			aes.init(Cipher.DECRYPT_MODE, llave_leida);
			byte[] desencriptado = aes.doFinal(encriptado);

			// Texto obtenido, igual al original.
			System.out.println("Texto desencriptado:"+new String(desencriptado));
		}
		catch (Exception e) {
		/* Si hay algún tipo de error de disco al leer o escribir el archivo
		* (Por ejemplo, el disco esta sin espacio), entonces se ejecuta el código.
		*/
			System.out.println("Doh: "+e); }
	}
}
