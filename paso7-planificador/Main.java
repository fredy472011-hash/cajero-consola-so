/*
 * PASO 7 - Resolver los ejercicios del Excel con el programa
 *
 *     javac *.java
 *     java Main
 *
 * Este programa resuelve LOS MISMOS ejercicios que se hacen a mano en el
 * Excel de la materia. Si los numeros coinciden, quiere decir que el
 * algoritmo se entendio bien.
 *
 * Es la mejor prueba que se puede llevar a la defensa: el calculo a mano
 * y el programa dan lo mismo.
 */

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ejercicio1();
        ejercicio2();
        comparativa();
    }

    // ==================================================================
    // EJERCICIO 1 - con tiempos de llegada distintos
    // ==================================================================
    private static void ejercicio1() {

        titulo("EJERCICIO 1 - CASO 3 DEL EXCEL");
        System.out.println("  Consigna: 'Modificar el tiempo de llegada del proceso 4");
        System.out.println("             a 3, y tener en cuenta el tiempo mas corto primero.'");
        System.out.println();
        System.out.println("    Proceso   Llega   Dura");
        System.out.println("       P1       0       3");
        System.out.println("       P2       1       5");
        System.out.println("       P3       3       2");
        System.out.println("       P4       3       5     <- antes llegaba en 9");
        System.out.println("       P5      12       5");

        List<Tarea> tareas = new ArrayList<Tarea>();
        tareas.add(new Tarea("P1",  0, 3));
        tareas.add(new Tarea("P2",  1, 5));
        tareas.add(new Tarea("P3",  3, 2));
        tareas.add(new Tarea("P4",  3, 5));
        tareas.add(new Tarea("P5", 12, 5));

        Planificador p = new Planificador();
        p.sjf(tareas);
        p.mostrarResultado("SJF - primero el trabajo mas corto", tareas);

        System.out.println();
        System.out.println("  Lo que hay que mirar:");
        System.out.println("    - P1 corre primero aunque P3 dura menos, porque en el");
        System.out.println("      instante 0 P3 todavia no llego (llega en 3).");
        System.out.println("    - En el instante 3 si hay donde elegir: estan P2(5),");
        System.out.println("      P3(2) y P4(5). Gana P3 por ser el mas corto.");
        System.out.println("    - P3 se adelanta a P2 aunque P2 llego dos horas antes.");
        System.out.println("      ESO es 'el mas corto primero' funcionando.");
    }

    // ==================================================================
    // EJERCICIO 2 - todos llegan en cero
    // ==================================================================
    private static void ejercicio2() {

        titulo("EJERCICIO 2 - CASO 5 DEL EXCEL");
        System.out.println("  Consigna: 'Para comenzar tiempo mas corto primero, luego");
        System.out.println("             tiempo restante mas corto. Round Robin Q=2.'");
        System.out.println();
        System.out.println("  Todos llegan en el instante cero.");
        System.out.println();
        System.out.println("    Proceso   Prioridad   Dura");
        System.out.println("       P1         1         7");
        System.out.println("       P2         4         3");
        System.out.println("       P3         2         2");
        System.out.println("       P4         5         4");
        System.out.println("       P5         3         5");

        List<Tarea> tareas = new ArrayList<Tarea>();
        tareas.add(new Tarea("P1", 0, 7, 1));
        tareas.add(new Tarea("P2", 0, 3, 4));
        tareas.add(new Tarea("P3", 0, 2, 2));
        tareas.add(new Tarea("P4", 0, 4, 5));
        tareas.add(new Tarea("P5", 0, 5, 3));

        Planificador p = new Planificador();
        p.roundRobin(tareas, 2, true);
        p.mostrarResultado("Round Robin Q=2, ordenado por tiempo restante", tareas);

        System.out.println();
        System.out.println("  Comproba contra tu Excel: tienen que dar");
        System.out.println("    T. Ejecucion:  21  11  2  13  18     promedio 13");
        System.out.println("    T. Espera:     14   8  0   9  13     promedio 8,8");
        System.out.println();
        System.out.println("  Control: 13 - 8,8 = 4,2, y el promedio de 'dura' es");
        System.out.println("           21/5 = 4,2. Si no da, hay un error en algun lado.");
    }

    // ==================================================================
    // Las cuatro politicas con los mismos datos
    // ==================================================================
    private static void comparativa() {

        titulo("LAS CUATRO POLITICAS CON LOS MISMOS PROCESOS");
        System.out.println("  Mismos datos del ejercicio 2, cambiando solo el criterio.");
        System.out.println("  Mira como cambia el tiempo de espera promedio.");

        Planificador p = new Planificador();

        p.fcfs(crearTareasEjercicio2());
        List<Tarea> t1 = crearTareasEjercicio2();
        p.fcfs(t1);
        p.mostrarResultado("1) FCFS - primero en llegar, primero en ser servido", t1);

        List<Tarea> t2 = crearTareasEjercicio2();
        p.sjf(t2);
        p.mostrarResultado("2) SJF - primero el trabajo mas corto", t2);

        List<Tarea> t3 = crearTareasEjercicio2();
        p.prioridad(t3);
        p.mostrarResultado("3) PRIORIDAD", t3);

        List<Tarea> t4 = crearTareasEjercicio2();
        p.roundRobin(t4, 2, true);
        p.mostrarResultado("4) ROUND ROBIN Q=2 por tiempo restante", t4);

        titulo("CONCLUSION PARA EL INFORME");
        System.out.println("  SJF da el MEJOR tiempo de espera promedio. Es un resultado");
        System.out.println("  demostrado: poner primero los cortos despeja la cola mas");
        System.out.println("  rapido y todos los demas esperan menos.");
        System.out.println();
        System.out.println("  Pero SJF tiene un problema: si no paran de llegar tareas");
        System.out.println("  cortas, una larga NUNCA se ejecuta. Eso se llama INANICION.");
        System.out.println();
        System.out.println("  Round Robin es peor en el promedio, pero GARANTIZA que");
        System.out.println("  todos avancen un poco. Por eso lo usan los sistemas");
        System.out.println("  interactivos: nadie quiere que su ventana se congele");
        System.out.println("  porque otro programa esta haciendo algo largo.");
        System.out.println();
        System.out.println("  No hay una politica mejor: depende de que se busque.");
    }

    private static List<Tarea> crearTareasEjercicio2() {
        List<Tarea> tareas = new ArrayList<Tarea>();
        tareas.add(new Tarea("P1", 0, 7, 1));
        tareas.add(new Tarea("P2", 0, 3, 4));
        tareas.add(new Tarea("P3", 0, 2, 2));
        tareas.add(new Tarea("P4", 0, 4, 5));
        tareas.add(new Tarea("P5", 0, 5, 3));
        return tareas;
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("  " + texto);
        System.out.println("======================================================================");
    }
}
