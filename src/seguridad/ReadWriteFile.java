

/* Este programa es una demostración de la lectura y escritura de archivos en Java.
  * Se lee el archivo dado como argumento de la línea de comandos 
  * Y lo escribe en el archivo dado como segundo argumento.
  */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReadWriteFile {

	public static void main(String[] args) {

		File inFile = new File(args[0]);
		File outFile = new File(args[1]);
		try {

			System.out.println("Abriendo archivo a leer: "+inFile);
			FileInputStream rawDataFromFile = new FileInputStream(inFile);
			byte[] fileData = new byte[(int) inFile.length()];

			System.out.println("Leyendo Datos");
			rawDataFromFile.read(fileData);

			System.out.println("Abriendo archivo para escribir: "+outFile);
			FileOutputStream outToFile = new FileOutputStream(outFile);

			System.out.println("Escribiendo Datos");
			outToFile.write(fileData);

			System.out.println("Cerrando Archivos");
			rawDataFromFile.close();
			outToFile.close();
		}
		catch (Exception e) {
		/* Si hay algún tipo de error de disco al leer o escribir el archivo
		* (Por ejemplo, el disco esta sin espacio), entonces se ejecuta el código.
		*/
			System.out.println("Doh: "+e); }
	}
}
