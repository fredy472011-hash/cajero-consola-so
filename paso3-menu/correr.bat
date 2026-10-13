@echo off
REM ------------------------------------------------------------
REM  Compila y ejecuta este paso en un solo comando.
REM  Uso:   .\correr          o   correr
REM ------------------------------------------------------------

echo Compilando...
javac -cp "." *.java

if errorlevel 1 (
    echo.
    echo  ^>^> HAY ERRORES DE COMPILACION. Mira el mensaje de arriba.
    echo     Corregi el codigo y volve a ejecutar.
    exit /b 1
)

echo Listo. Ejecutando...
echo.
java -cp "." Main %*
