@echo off
REM ------------------------------------------------------------
REM  Compila y ejecuta este paso en un solo comando.
REM  Uso:   .\correr          o   correr
REM
REM  El * del classpath toma CUALQUIER .jar de esta carpeta, sin
REM  importar el numero de version. Asi cada uno puede bajar la
REM  version que le de la pagina de MySQL y funciona igual.
REM ------------------------------------------------------------

if not exist *.jar (
    echo.
    echo  ^>^> FALTA EL DRIVER DE MYSQL ^(un archivo .jar^).
    echo.
    echo     Bajalo de:
    echo       https://dev.mysql.com/downloads/connector/j/
    echo       opcion Platform Independent, y descomprimi el .zip
    echo.
    echo     Copia el .jar EN ESTA CARPETA. Sirve cualquier version.
    exit /b 1
)

echo Compilando...
javac -cp ".;*" *.java

if errorlevel 1 (
    echo.
    echo  ^>^> HAY ERRORES DE COMPILACION. Mira el mensaje de arriba.
    echo     Corregi el codigo y volve a ejecutar.
    exit /b 1
)

echo Listo. Ejecutando...
echo.
java -cp ".;*" Main %*
