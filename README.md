#Ejemplo de uso:

##Para ocultar informacion:

java -jar esteganografia-1.0-SNAPSHOT-shaded.jar -o -i "C:\Users\acpat\Documents\projects\poli\seguridad-de-la-informacion\Esteganografia\src\main\resources\test1.png" -m "hola mundo"

##Para leer la informacion oculta:

java -jar esteganografia-1.0-SNAPSHOT-shaded.jar -l -i "C:\Users\acpat\Documents\projects\poli\seguridad-de-la-informacion\Esteganografia\src\main\resources\test1-modificado.png"
