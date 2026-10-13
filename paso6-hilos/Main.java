/*
 * PASO 6 - La condicion de carrera, y como se arregla
 *
 *     javac *.java
 *     java Main
 *
 * ESTE ES EL PASO MAS IMPORTANTE DEL TRABAJO.
 *
 * Corre el programa VARIAS VECES seguidas. La demostracion 1 a veces
 * falla y a veces no: esa intermitencia es justamente lo que hace que
 * estos errores sean tan peligrosos en un sistema real.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        // 'throws InterruptedException' avisa que este metodo puede lanzar
        // ese error (por culpa del join()) y que no lo va a atrapar aca.
        // Es la alternativa a poner try/catch en todos lados.

        demo1SinSincronizar();
        demo2ConSincronizar();
        demo3VariosHilos();

        resumen();
    }

    // ==================================================================
    // DEMO 1: el error
    // ==================================================================
    private static void demo1SinSincronizar() throws InterruptedException {

        titulo("1. SIN SYNCHRONIZED  (el error)");

        Cuenta cuenta = new Cuenta("001", "Fredy", "1234", 150000);

        System.out.printf("  Saldo inicial: Gs. %,d%n", cuenta.getSaldo());
        System.out.println("  Dos cajeros piden Gs. 100.000 AL MISMO TIEMPO.");
        System.out.println("  Solo uno deberia poder.");
        System.out.println();

        HiloRetiro cajero1 = new HiloRetiro("Cajero-1", cuenta, 100000, false);
        HiloRetiro cajero2 = new HiloRetiro("Cajero-2", cuenta, 100000, false);

        /*
         * start() arranca el hilo y VUELVE DE INMEDIATO.
         * El codigo de run() empieza a correr por su cuenta, en paralelo.
         *
         * OJO: hay que llamar a start(), NO a run().
         * Si se llama a run() directamente se ejecuta como un metodo normal,
         * en el mismo hilo, y no hay ningun paralelismo. Es un error comun.
         */
        cajero1.start();
        cajero2.start();

        /*
         * join() significa "esperar a que ese hilo termine".
         * Sin esto, main seguiria de largo e imprimiria el saldo antes de
         * que los cajeros hayan terminado de operar.
         */
        cajero1.join();
        cajero2.join();

        long saldoFinal = cuenta.getSaldo();
        System.out.println();
        System.out.printf("  Saldo final:   Gs. %,d%n", saldoFinal);
        System.out.println();

        if (saldoFinal < 0) {
            System.out.println("  >>> FALLO. El banco entrego Gs. 200.000 teniendo 150.000.");
            System.out.println("  >>> Los dos hilos preguntaron 'alcanza?' ANTES de que");
            System.out.println("  >>> alguno descontara, y a los dos les dijeron que si.");
            System.out.println("  >>> Aparecieron Gs. 50.000 de la nada.");
        } else {
            System.out.println("  >>> Esta vez NO fallo.");
            System.out.println("  >>> Las condiciones de carrera son INTERMITENTES:");
            System.out.println("  >>> dependen de como se intercalen los hilos, que cambia");
            System.out.println("  >>> en cada ejecucion. Volve a correr el programa.");
        }
    }

    // ==================================================================
    // DEMO 2: la solucion
    // ==================================================================
    private static void demo2ConSincronizar() throws InterruptedException {

        titulo("2. CON SYNCHRONIZED  (la solucion)");

        Cuenta cuenta = new Cuenta("002", "Fredy", "1234", 150000);

        System.out.printf("  Saldo inicial: Gs. %,d%n", cuenta.getSaldo());
        System.out.println("  Mismo escenario, pero ahora el metodo esta protegido.");
        System.out.println();

        HiloRetiro cajero1 = new HiloRetiro("Cajero-1", cuenta, 100000, true);
        HiloRetiro cajero2 = new HiloRetiro("Cajero-2", cuenta, 100000, true);

        cajero1.start();
        cajero2.start();
        cajero1.join();
        cajero2.join();

        System.out.println();
        System.out.printf("  Saldo final:   Gs. %,d%n", cuenta.getSaldo());
        System.out.println();
        System.out.println("  >>> Correcto. Un hilo entra por vez, asi que el segundo");
        System.out.println("  >>> pregunta DESPUES de que el primero descontara,");
        System.out.println("  >>> ve que quedan 50.000 y se rechaza como corresponde.");
        System.out.println();
        System.out.println("  >>> Corre el programa 10 veces: esta demo da SIEMPRE lo mismo.");
    }

    // ==================================================================
    // DEMO 3: muchos hilos
    // ==================================================================
    private static void demo3VariosHilos() throws InterruptedException {

        titulo("3. DIEZ CAJEROS A LA VEZ");

        Cuenta cuenta = new Cuenta("003", "Fredy", "1234", 500000);

        System.out.printf("  Saldo inicial: Gs. %,d%n", cuenta.getSaldo());
        System.out.println("  Diez cajeros piden Gs. 100.000 cada uno.");
        System.out.println("  Solo cinco tendrian que poder (5 x 100.000 = 500.000).");
        System.out.println();

        // Un arreglo de hilos, para no escribir diez variables.
        HiloRetiro[] cajeros = new HiloRetiro[10];

        for (int i = 0; i < cajeros.length; i++) {
            cajeros[i] = new HiloRetiro("Cajero-" + (i + 1), cuenta, 100000, true);
        }

        // Primero se arrancan TODOS, y recien despues se espera a todos.
        // Si se hiciera start() y join() dentro del mismo for, cada hilo
        // terminaria antes de que arranque el siguiente y no habria
        // paralelismo ninguno.
        for (int i = 0; i < cajeros.length; i++) {
            cajeros[i].start();
        }
        for (int i = 0; i < cajeros.length; i++) {
            cajeros[i].join();
        }

        int exitosos = 0;
        for (int i = 0; i < cajeros.length; i++) {
            if (cajeros[i].pudoRetirar()) {
                exitosos = exitosos + 1;
            }
        }

        System.out.println();
        System.out.println("  Retiros exitosos: " + exitosos + " de 10");
        System.out.printf("  Saldo final:      Gs. %,d%n", cuenta.getSaldo());
        System.out.println();
        System.out.println("  >>> Siempre 5 exitosos y saldo 0, sin importar el orden");
        System.out.println("  >>> en que los hilos se hayan intercalado.");
    }

    // ==================================================================
    private static void resumen() {
        titulo("PARA LA DEFENSA DEL TRABAJO");
        System.out.println("  Si el profesor pregunta por que synchronized y no un semaforo:");
        System.out.println();
        System.out.println("    synchronized  -> deja pasar UNO SOLO. Es exclusion mutua.");
        System.out.println("    semaforo      -> deja pasar HASTA N. Sirve para limitar");
        System.out.println("                     cuantos usan un recurso, no para protegerlo.");
        System.out.println();
        System.out.println("  Aca el problema es que dos hilos no toquen el MISMO saldo");
        System.out.println("  al mismo tiempo. Eso es exclusion mutua, y para eso existe");
        System.out.println("  synchronized. Un semaforo de 1 haria lo mismo, pero da mas");
        System.out.println("  vueltas para el mismo resultado.");
        System.out.println();
        System.out.println("  (En Java, synchronized es un MONITOR. Material de catedra,");
        System.out.println("   Procesos y Threads, seccion 2.3.7.)");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("  " + texto);
        System.out.println("======================================================================");
    }
}
