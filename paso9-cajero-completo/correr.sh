#!/bin/bash
# Compila y ejecuta este paso. Version para Linux y Mac.
# El * toma cualquier .jar de esta carpeta, sin importar la version.

if ! ls *.jar >/dev/null 2>&1; then
    echo
    echo '  >> FALTA EL DRIVER DE MYSQL (un archivo .jar).'
    echo '     https://dev.mysql.com/downloads/connector/j/'
    echo '     Copialo en ESTA carpeta. Sirve cualquier version.'
    exit 1
fi

echo Compilando...
if ! javac -cp ".:*" -d out Principal/*.java Vista/*.java Clases/*.java Sistema/*.java BaseDatos/*.java Service/*.java Interfaces_General/*.java; then
    echo
    echo '  >> HAY ERRORES DE COMPILACION. Mira el mensaje de arriba.'
    exit 1
fi

echo 'Listo. Ejecutando...'
echo
java -cp "out:*" Principal.Main "$@"
