/*
 * PASO 7 - Una tarea para el planificador
 *
 * Esta clase es mas chica que Proceso del paso 5. Aca no nos interesa
 * QUE hace el proceso (retirar, depositar), sino solo CUANTO TARDA y
 * CUANDO LLEGA, que es lo unico que mira el planificador de CPU.
 *
 * Es exactamente la tabla de los ejercicios del Excel:
 *
 *     Proceso | Tiempo Llegada | Tiempo Proceso
 *        1    |       0        |       3
 *        2    |       1        |       5
 *       ...
 */
public class Tarea {

    private final String nombre;
    private final int llegada;      // en que instante aparece
    private final int duracion;     // cuanto necesita de CPU en total
    private final int prioridad;    // numero mas chico = mas importante

    private int restante;           // cuanto le falta todavia
    private int fin;                // en que instante termino

    public Tarea(String nombre, int llegada, int duracion, int prioridad) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.duracion = duracion;
        this.prioridad = prioridad;
        this.restante = duracion;   // al principio le falta todo
        this.fin = -1;              // -1 = todavia no termino
    }

    /*
     * Un segundo constructor, para cuando no hay prioridades.
     *
     * Esto se llama SOBRECARGA: dos metodos con el mismo nombre pero
     * distintos parametros. Java elige el correcto segun como lo llames.
     *
     * 'this(...)' llama al OTRO constructor de esta misma clase.
     */
    public Tarea(String nombre, int llegada, int duracion) {
        this(nombre, llegada, duracion, 0);
    }

    /*
     * Ejecuta la tarea durante 'unidades' de tiempo.
     * Devuelve cuanto se ejecuto realmente (puede ser menos si le faltaba poco).
     */
    public int ejecutar(int unidades) {
        int usado = Math.min(unidades, restante);
        restante = restante - usado;
        return usado;
    }

    public boolean termino() {
        return restante == 0;
    }

    /*
     * TIEMPO DE EJECUCION (o de retorno): desde que llego hasta que termino.
     * Es lo que el usuario percibe que tardo.
     */
    public int tiempoEjecucion() {
        return fin - llegada;
    }

    /*
     * TIEMPO DE ESPERA: del tiempo total, cuanto estuvo sin hacer nada.
     *
     *     espera = tiempo de ejecucion - lo que realmente uso de CPU
     *
     * Esta es la formula que usas en el Excel.
     */
    public int tiempoEspera() {
        return tiempoEjecucion() - duracion;
    }

    /*
     * Deja la tarea como nueva, para poder probar otra politica
     * con los mismos datos.
     */
    public void reiniciar() {
        restante = duracion;
        fin = -1;
    }

    public String getNombre()  { return nombre; }
    public int getLlegada()    { return llegada; }
    public int getDuracion()   { return duracion; }
    public int getPrioridad()  { return prioridad; }
    public int getRestante()   { return restante; }
    public int getFin()        { return fin; }
    public void setFin(int f)  { this.fin = f; }
}
