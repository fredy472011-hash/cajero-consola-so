/*
 * PASO 7 - El planificador de CPU
 *
 * LA PREGUNTA QUE RESPONDE EL PLANIFICADOR:
 *
 *     "Hay varias tareas esperando. Cual le doy a la CPU AHORA?"
 *
 * Hay una sola CPU y muchas tareas. El planificador es la parte del
 * sistema operativo que decide el orden. Segun que criterio use, el
 * resultado cambia: unos esperan mas y otros menos.
 *
 * Este programa implementa las politicas que da la catedra y calcula
 * los mismos numeros que se sacan a mano en el Excel de ejercicios.
 *
 * Material: Procesos y Threads, seccion 2.5 (Planificacion)
 */

import java.util.ArrayList;
import java.util.List;

public class Planificador {

    /*
     * Un SEGMENTO es un pedazo de tiempo en el que una tarea tuvo la CPU.
     * Con la lista de segmentos se dibuja el diagrama de Gantt.
     *
     * Esta clase vive ADENTRO de Planificador porque no se usa en otro lado.
     * Se la llama clase anidada. 'static' aca significa que no necesita
     * un Planificador para existir.
     */
    public static class Segmento {
        public final String tarea;
        public final int desde;
        public final int hasta;

        public Segmento(String tarea, int desde, int hasta) {
            this.tarea = tarea;
            this.desde = desde;
            this.hasta = hasta;
        }
    }

    private final List<Segmento> gantt = new ArrayList<Segmento>();

    // ==================================================================
    // POLITICA 1 - FCFS: primero en llegar, primero en ser servido
    // ==================================================================
    /*
     * La mas simple y la mas justa: se atiende por orden de llegada.
     * Problema: si llega primero una tarea larguisima, todas las cortas
     * que vienen atras esperan. Eso se llama "efecto convoy".
     */
    public void fcfs(List<Tarea> tareas) {
        reiniciar(tareas);
        int reloj = 0;

        while (hayPendientes(tareas)) {

            Tarea elegida = null;

            // Entre las que ya llegaron y no terminaron, la de menor llegada
            for (Tarea t : tareas) {
                if (t.termino() || t.getLlegada() > reloj) {
                    continue;   // 'continue' salta a la siguiente vuelta del for
                }
                if (elegida == null || t.getLlegada() < elegida.getLlegada()) {
                    elegida = t;
                }
            }

            if (elegida == null) {
                reloj = reloj + 1;   // CPU ociosa: nadie llego todavia
                continue;
            }

            // Sin expulsion: corre hasta terminar
            int usado = elegida.ejecutar(elegida.getRestante());
            gantt.add(new Segmento(elegida.getNombre(), reloj, reloj + usado));
            reloj = reloj + usado;
            elegida.setFin(reloj);
        }
    }

    // ==================================================================
    // POLITICA 2 - SJF: primero el trabajo mas corto
    // ==================================================================
    /*
     * De las que ya llegaron, elige la que MENOS dura.
     *
     * Mejora el tiempo de espera promedio, pero una tarea larga puede
     * quedarse esperando para siempre si no paran de llegar cortas.
     * Eso se llama INANICION (starvation).
     *
     * IMPORTANTE: solo se puede elegir entre las que YA LLEGARON.
     * No se puede ejecutar algo que todavia no existe.
     */
    public void sjf(List<Tarea> tareas) {
        reiniciar(tareas);
        int reloj = 0;

        while (hayPendientes(tareas)) {

            Tarea elegida = elegirMasCorta(tareas, reloj, false);

            if (elegida == null) {
                reloj = reloj + 1;
                continue;
            }

            int usado = elegida.ejecutar(elegida.getRestante());
            gantt.add(new Segmento(elegida.getNombre(), reloj, reloj + usado));
            reloj = reloj + usado;
            elegida.setFin(reloj);
        }
    }

    // ==================================================================
    // POLITICA 3 - PRIORIDAD
    // ==================================================================
    public void prioridad(List<Tarea> tareas) {
        reiniciar(tareas);
        int reloj = 0;

        while (hayPendientes(tareas)) {

            Tarea elegida = null;
            for (Tarea t : tareas) {
                if (t.termino() || t.getLlegada() > reloj) {
                    continue;
                }
                if (elegida == null || t.getPrioridad() < elegida.getPrioridad()) {
                    elegida = t;
                }
            }

            if (elegida == null) {
                reloj = reloj + 1;
                continue;
            }

            int usado = elegida.ejecutar(elegida.getRestante());
            gantt.add(new Segmento(elegida.getNombre(), reloj, reloj + usado));
            reloj = reloj + usado;
            elegida.setFin(reloj);
        }
    }

    // ==================================================================
    // POLITICA 4 - ROUND ROBIN POR VUELTAS
    // ==================================================================
    /*
     * Esta es la del "Caso 5" del Excel.
     *
     * Funciona en VUELTAS. En cada vuelta:
     *   - se arma la fila con todas las tareas que no terminaron
     *   - cada una corre como maximo 'quantum' unidades
     *   - cuando pasaron todas, se arma la fila de nuevo
     *
     * La regla clave de Round Robin: el que no termina VUELVE AL FINAL
     * de la fila. No puede correr otra vez hasta que pasen los demas.
     *
     * El parametro 'ordenarPorRestante' decide como se arma la fila:
     *   false -> por orden de llegada (Round Robin clasico)
     *   true  -> por lo que le queda (primera vuelta: por duracion)
     */
    public void roundRobin(List<Tarea> tareas, int quantum, boolean ordenarPorRestante) {
        reiniciar(tareas);
        int reloj = 0;
        int vuelta = 1;

        while (hayPendientes(tareas)) {

            // Armar la fila de esta vuelta
            List<Tarea> fila = new ArrayList<Tarea>();
            for (Tarea t : tareas) {
                if (!t.termino() && t.getLlegada() <= reloj) {
                    fila.add(t);
                }
            }

            if (fila.isEmpty()) {
                reloj = reloj + 1;   // nadie disponible: la CPU espera
                continue;
            }

            if (ordenarPorRestante) {
                ordenarPorLoQueLeQueda(fila);
            }

            // Cada uno corre su quantum
            for (Tarea t : fila) {
                if (t.termino()) {
                    continue;
                }
                int usado = t.ejecutar(quantum);
                gantt.add(new Segmento(t.getNombre(), reloj, reloj + usado));
                reloj = reloj + usado;

                if (t.termino()) {
                    t.setFin(reloj);
                }
            }

            vuelta = vuelta + 1;

            if (vuelta > 1000) {
                break;   // red de seguridad para no colgarse nunca
            }
        }
    }

    // ==================================================================
    // Ayudantes
    // ==================================================================

    private Tarea elegirMasCorta(List<Tarea> tareas, int reloj, boolean porRestante) {
        Tarea elegida = null;
        for (Tarea t : tareas) {
            if (t.termino() || t.getLlegada() > reloj) {
                continue;
            }
            if (elegida == null) {
                elegida = t;
                continue;
            }

            int valorT = porRestante ? t.getRestante() : t.getDuracion();
            int valorE = porRestante ? elegida.getRestante() : elegida.getDuracion();

            // Si empatan, gana el que llego antes
            if (valorT < valorE ||
                (valorT == valorE && t.getLlegada() < elegida.getLlegada())) {
                elegida = t;
            }
        }
        return elegida;
    }

    /*
     * Ordena la fila de menor a mayor tiempo restante.
     *
     * Es el metodo de la BURBUJA: compara de a dos vecinos y los
     * intercambia si estan al reves. Se repite hasta que no haya
     * mas cambios. Es el algoritmo de ordenamiento mas simple que hay.
     *
     * Java tiene Collections.sort(), pero hacerlo a mano aca deja ver
     * lo que pasa, y para 5 tareas da exactamente lo mismo.
     */
    private void ordenarPorLoQueLeQueda(List<Tarea> fila) {
        for (int i = 0; i < fila.size() - 1; i++) {
            for (int j = 0; j < fila.size() - 1 - i; j++) {

                Tarea a = fila.get(j);
                Tarea b = fila.get(j + 1);

                boolean hayQueIntercambiar =
                        b.getRestante() < a.getRestante() ||
                        (b.getRestante() == a.getRestante() &&
                         b.getLlegada() < a.getLlegada());

                if (hayQueIntercambiar) {
                    fila.set(j, b);
                    fila.set(j + 1, a);
                }
            }
        }
    }

    private boolean hayPendientes(List<Tarea> tareas) {
        for (Tarea t : tareas) {
            if (!t.termino()) {
                return true;
            }
        }
        return false;
    }

    private void reiniciar(List<Tarea> tareas) {
        gantt.clear();
        for (Tarea t : tareas) {
            t.reiniciar();
        }
    }

    // ==================================================================
    // Mostrar los resultados
    // ==================================================================

    public void mostrarResultado(String titulo, List<Tarea> tareas) {

        System.out.println();
        System.out.println("  " + titulo);
        System.out.println("  " + "-".repeat(66));

        mostrarGantt(tareas);

        System.out.println();
        System.out.printf("    %-8s %-8s %-8s %-14s %-12s%n",
                "Tarea", "Llega", "Dura", "T. Ejecucion", "T. Espera");
        System.out.println("    " + "-".repeat(56));

        double sumaEjec = 0;
        double sumaEsp = 0;

        for (Tarea t : tareas) {
            System.out.printf("    %-8s %-8d %-8d %-14d %-12d%n",
                    t.getNombre(), t.getLlegada(), t.getDuracion(),
                    t.tiempoEjecucion(), t.tiempoEspera());
            sumaEjec = sumaEjec + t.tiempoEjecucion();
            sumaEsp = sumaEsp + t.tiempoEspera();
        }

        int n = tareas.size();
        System.out.println("    " + "-".repeat(56));
        System.out.printf("    %-8s %-8s %-8s %-14.2f %-12.2f%n",
                "PROMEDIO", "", "", sumaEjec / n, sumaEsp / n);
    }

    /*
     * Dibuja el diagrama de Gantt en la consola, igual que el del Excel.
     */
    private void mostrarGantt(List<Tarea> tareas) {

        int finTotal = 0;
        for (Segmento s : gantt) {
            if (s.hasta > finTotal) {
                finTotal = s.hasta;
            }
        }

        // Regla de tiempos arriba
        StringBuilder regla = new StringBuilder("           ");
        for (int t = 0; t <= finTotal; t++) {
            if (t % 5 == 0) {
                regla.append(String.format("%-5d", t));
            }
        }
        System.out.println(regla.toString());

        // Una fila por tarea
        for (Tarea tarea : tareas) {

            StringBuilder fila = new StringBuilder();
            fila.append(String.format("    %-7s", tarea.getNombre()));

            for (int t = 0; t < finTotal; t++) {

                boolean corriendo = false;
                for (Segmento s : gantt) {
                    if (s.tarea.equals(tarea.getNombre()) && t >= s.desde && t < s.hasta) {
                        corriendo = true;
                        break;
                    }
                }

                if (corriendo) {
                    fila.append("#");
                } else if (t >= tarea.getLlegada() && t < tarea.getFin()) {
                    fila.append(".");     // ya llego pero esta esperando
                } else {
                    fila.append(" ");     // todavia no llego, o ya termino
                }
            }
            System.out.println(fila.toString());
        }

        System.out.println();
        System.out.println("           #  corriendo       .  esperando su turno");
    }
}
