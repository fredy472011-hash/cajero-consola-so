#!/bin/bash
# Compila y ejecuta este paso. Version para Linux y Mac.
# Uso:  bash correr.sh

echo Compilando...
if ! javac -cp ".:mysql-connector-j-9.4.0.jar" *.java; then
    echo
    echo '  >> HAY ERRORES DE COMPILACION. Mira el mensaje de arriba.'
    exit 1
fi

echo 'Listo. Ejecutando...'
echo
java -cp ".:mysql-connector-j-9.4.0.jar" Main "$@"
