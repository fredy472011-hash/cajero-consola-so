/*
 * PASO 9 - El cajero completo
 *
 *     mysql -u root -p < ../sql/esquema.sql        (una sola vez)
 *
 *     javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
 *     java  -cp ".;mysql-connector-j-9.4.0.jar" Main
 *
 * Opciones de la linea de comandos:
 *
 *     java -cp ... Main            -> el cajero normal
 *     java -cp ... Main carrera    -> la demo de la condicion de carrera
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /*
         * args son los parametros escritos despues del nombre de la clase.
         * Si se ejecuta  'java Main carrera',  entonces args[0] vale "carrera".
         *
         * Hay que preguntar primero por el length: si no se escribio nada,
         * args esta vacio y args[0] daria un error.
         */
        if (args.length > 0 && args[0].equals("carrera")) {
            demostrarCondicionDeCarrera();
            return;
        }

        cajeroNormal();
    }

    // ==================================================================
    private static void cajeroNormal() {

        System.out.println("======================================================================");
        System.out.println("  CAJERO AUTOMATICO - version consola");
        System.out.println("  Informatica VI - Sistemas Operativos");
        System.out.println("======================================================================");

        // 1. Memoria de largo plazo
        if (!BaseDeDatos.responde()) {
            return;
        }
        System.out.println("  [OK] MySQL conectado");

        BancoBD banco = new BancoBD();

        // 2. Hardware
        Dispensador dispensador = new Dispensador(40, 30);
        System.out.println("  [OK] Dispensador cargado: 40 x 100.000 + 30 x 50.000");

        // 3. Bitacora del sistema
        Bitacora bitacora = new Bitacora();

        // 4. Teclado
        Scanner teclado = new Scanner(System.in);

        // 5. El cajero, armado con todo lo anterior
        Cajero cajero = new Cajero(banco, dispensador, bitacora, teclado);

        System.out.println();
        System.out.println("  Probe la opcion 7 (CANCELAR): ahi se ve la diferencia");
        System.out.println("  entre la memoria volatil y la de largo plazo.");

        cajero.iniciar();
        teclado.close();
    }

    // ==================================================================
    private static void demostrarCondicionDeCarrera() throws InterruptedException {

        System.out.println("======================================================================");
        System.out.println("  CONDICION DE CARRERA");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  Una cuenta con Gs. 150.000.");
        System.out.println("  Dos cajeros piden Gs. 100.000 al mismo tiempo.");
        System.out.println("  Solo uno deberia poder.");

        // --------------------------------------------------------------
        System.out.println();
        System.out.println("  --- SIN synchronized ---");
        System.out.println();

        Cuenta mala = new Cuenta("999", "Prueba", "0000", 150000);

        HiloRetiro a1 = new HiloRetiro("Cajero-1", mala, 100000, false);
        HiloRetiro a2 = new HiloRetiro("Cajero-2", mala, 100000, false);

        a1.start();
        a2.start();
        a1.join();
        a2.join();

        System.out.println();
        System.out.printf("  Saldo final: Gs. %,d%n", mala.getSaldo());

        if (mala.getSaldo() < 0) {
            System.out.println("  >>> FALLO: se entregaron Gs. 200.000 habiendo 150.000.");
        } else {
            System.out.println("  >>> Esta vez no fallo. Volve a correrlo: el error");
            System.out.println("      es intermitente, y por eso es tan peligroso.");
        }

        // --------------------------------------------------------------
        System.out.println();
        System.out.println("  --- CON synchronized ---");
        System.out.println();

        Cuenta buena = new Cuenta("998", "Prueba", "0000", 150000);

        HiloRetiro b1 = new HiloRetiro("Cajero-1", buena, 100000, true);
        HiloRetiro b2 = new HiloRetiro("Cajero-2", buena, 100000, true);

        b1.start();
        b2.start();
        b1.join();
        b2.join();

        System.out.println();
        System.out.printf("  Saldo final: Gs. %,d%n", buena.getSaldo());
        System.out.println("  >>> Correcto, y da siempre lo mismo.");
    }
}
