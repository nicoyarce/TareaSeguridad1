Tarea 1 Seguridad Informática UBB
Contenido:
    - Encriptar.java
    - Desencriptar.java
    - mensaje.txt
    - encriptado.txt
    - desencriptado.txt

Para compilar encriptador:
    Revisar las lineas 30 y 31 que poseen la ruta del almacen de llaves y la
    clave del almacen, respectivamente.
    Revisar la linea 75 que posee la ruta del certificado que encriptara con su
    llave publica RSA.
    $ javac Encriptar.java    

Para compilar desencriptador:
    Revisar las lineas 71 y 72 que poseen la ruta del almacen de llaves y la
    clave del almacen, respectivamente.
    Revisar la linea 96 que posee la ruta del certificado del autor del mensaje.
    $ javac Desencriptar.java

Para ejecutar:    
    $ java Encriptar
    Luego de la ejecución se generará un archivo llamado encriptado.txt el cual 
    sera buscado por la aplicacion Desencriptar
    $ java Desencriptar
