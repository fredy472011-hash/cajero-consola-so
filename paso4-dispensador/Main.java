/*
 * PASO 4 - Arrancar el cajero con dispensador
 *
 *     javac *.java
 *     java Main
 *
 * Antes de abrir el menu, este Main muestra una demostracion del
 * algoritmo voraz, para ver por que algunos montos se rechazan.
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        demostrarAlgoritmo();

        Banco banco = new Banco();
        banco.cargarCuentasDePrueba();

        // 40 billetes de 100.000 y 30 de 50.000
        Dispensador dispensador = new Dispensador(40, 30);

        Scanner teclado = new Scanner(System.in);
        Cajero cajero = new Cajero(banco, teclado, dispensador);
        cajero.iniciar();
        teclado.close();
    }

    /*
     * Dispensador chico, para que se vea cuando se queda sin billetes.
     */
    private static void demostrarAlgoritmo() {
        System.out.println("==================================================");
        System.out.println("  EL ALGORITMO VORAZ Y SUS LIMITES");
        System.out.println("==================================================");

        Dispensador d = new Dispensador(3, 2);   // 3 de 100.000 + 2 de 50.000
        d.mostrarCasetes();
        System.out.println();

        long[] pedidos = { 300000, 150000, 30000, 450000, 500000, 250000 };

        for (long monto : pedidos) {
            int[] plan = d.planificar(monto);

            System.out.printf("  Pedir Gs. %,9d  ->  ", monto);

            if (plan == null) {
                System.out.println("RECHAZADO");
            } else {
                System.out.println(d.describir(plan));
            }
        }

        System.out.println();
        System.out.println("  30.000  se rechaza: el billete mas chico es 50.000.");
        System.out.println("  450.000 se rechaza: quedarian 50.000 sueltos y solo");
        System.out.println("          hay 2 billetes de 50.000, ya usados.");
        System.out.println("  500.000 se rechaza: hay 400.000 en total nada mas.");
        System.out.println();
        System.out.println("  Un cajero real falla igual. Modelarlo es parte del trabajo.");
        System.out.println();
    }
}
