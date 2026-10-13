/*
 * PASO 5 - Ver los estados de un proceso
 *
 *     javac *.java
 *     java Main
 *
 * No hay menu en este paso. El objetivo es UNO SOLO: que se vea en pantalla
 * como un proceso recorre los cinco estados.
 *
 * Esta salida es la que hay que mostrarle al profesor.
 */
public class Main {

    public static void main(String[] args) {

        Bitacora bitacora = new Bitacora();
        Dispensador dispensador = new Dispensador(40, 30);
        Cuenta cuenta = new Cuenta("1001", "Fredy Canete", "1234", 2500000);

        titulo("1. UNA CONSULTA DE SALDO");
        System.out.println("No usa ningun dispositivo. Nunca se bloquea:");
        System.out.println("va directo de EJECUTANDO a TERMINADO.");
        System.out.println();

        Proceso consulta = new Proceso(Proceso.Tipo.CONSULTA, cuenta, 0,
                                       dispensador, bitacora);
        consulta.admitir();
        consulta.ejecutar();

        // ------------------------------------------------------------------
        titulo("2. UN RETIRO");
        System.out.println("Usa el dispensador, que es un aparato mecanico y");
        System.out.println("tarda. Mira como aparece el estado BLOQUEADO:");
        System.out.println();

        Proceso retiro = new Proceso(Proceso.Tipo.RETIRO, cuenta, 250000,
                                     dispensador, bitacora);
        retiro.admitir();
        retiro.ejecutar();

        // ------------------------------------------------------------------
        titulo("3. UN DEPOSITO");
        System.out.println("Tambien se bloquea: hay que contar los billetes.");
        System.out.println();

        Proceso deposito = new Proceso(Proceso.Tipo.DEPOSITO, cuenta, 500000,
                                       dispensador, bitacora);
        deposito.admitir();
        deposito.ejecutar();

        // ------------------------------------------------------------------
        titulo("RESUMEN - la tabla de procesos");
        System.out.println("Esto es lo que el sistema operativo guarda de cada");
        System.out.println("proceso. Se llama Bloque de Control de Proceso (BCP).");
        System.out.println();

        System.out.printf("  %-4s %-10s %-11s %-5s %s%n",
                "PID", "TIPO", "ESTADO", "PRIO", "RESULTADO");
        System.out.println("  " + "-".repeat(72));

        Proceso[] todos = { consulta, retiro, deposito };

        for (Proceso p : todos) {
            System.out.printf("  %-4d %-10s %-11s %-5d %s%n",
                    p.getPid(), p.getTipo(), p.getEstado(),
                    p.getPrioridad(), p.getResultado());
        }

        System.out.printf("%n  Saldo final: Gs. %,d%n", cuenta.getSaldo());

        // ------------------------------------------------------------------
        titulo("LO IMPORTANTE DE ESTE PASO");
        System.out.println("  La consulta  tiene prioridad 1  (no toca el hardware)");
        System.out.println("  El retiro    tiene prioridad 5  (usa el dispensador)");
        System.out.println();
        System.out.println("  Pero aca los procesos corrieron UNO DETRAS DE OTRO,");
        System.out.println("  asi que la prioridad no sirvio de nada todavia.");
        System.out.println();
        System.out.println("  Mientras el retiro estuvo BLOQUEADO 1,8 segundos, la");
        System.out.println("  CPU estuvo sin hacer nada. Eso es desperdicio.");
        System.out.println();
        System.out.println("  En el paso 6 los vamos a hacer correr AL MISMO TIEMPO.");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("  " + texto);
        System.out.println("======================================================================");
    }
}
