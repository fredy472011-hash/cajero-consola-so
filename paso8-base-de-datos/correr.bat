@echo off
REM ------------------------------------------------------------
REM  Compila y ejecuta este paso en un solo comando.
REM  Uso:   .\correr          o   correr
REM ------------------------------------------------------------

echo Compilando...
javac -cp ".;mysql-connector-j-9.4.0.jar" *.java

if errorlevel 1 (
    echo.
    echo  ^>^> HAY ERRORES DE COMPILACION. Mira el mensaje de arriba.
    echo     Corregi el codigo y volve a ejecutar.
    exit /b 1
)

echo Listo. Ejecutando...
echo.
java -cp ".;mysql-connector-j-9.4.0.jar" Main %*
