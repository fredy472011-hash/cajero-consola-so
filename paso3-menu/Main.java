/*
 * PASO 3 - Arrancar el cajero
 *
 *     javac *.java
 *     java Main
 *
 * Fijate que Main quedo muy corto. Eso es a proposito: Main solo ARMA
 * las piezas y las conecta. Toda la logica esta en Cajero.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. Se crea el banco con sus cuentas
        Banco banco = new Banco();
        banco.cargarCuentasDePrueba();

        // 2. Se crea el lector del teclado.
        //
        //    System.in es la "entrada estandar": lo que se escribe en la
        //    consola. Scanner lo envuelve para poder leerlo comodo.
        Scanner teclado = new Scanner(System.in);

        // 3. Se crea el cajero y se le pasan las dos cosas que necesita
        Cajero cajero = new Cajero(banco, teclado, null);

        // 4. Arranca
        cajero.iniciar();

        // 5. Se cierra el Scanner.
        //    Todo lo que se abre hay que cerrarlo: archivos, conexiones
        //    de base de datos, lectores. Si no, quedan recursos ocupados.
        teclado.close();
    }
}
