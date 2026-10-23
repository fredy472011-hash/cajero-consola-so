package Sistema;

/*
 * PASO 5 - La bitacora del sistema
 *
 * Un sistema operativo de verdad no muestra lo que pasa adentro. Nosotros
 * SI tenemos que mostrarlo, porque de eso se trata el trabajo.
 *
 * Esta clase imprime cada cambio de estado con la hora exacta. La salida
 * que produce es la evidencia que hay que mostrar en la presentacion.
 */

// LocalTime guarda una hora (sin fecha). Viene en java.time.
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Bitacora {

    /*
     * Un formateador decide como se escribe la hora.
     *   HH  -> hora de 00 a 23
     *   mm  -> minutos
     *   ss  -> segundos
     *   SSS -> milesimas de segundo
     *
     * Las milesimas importan: los cambios de estado pasan muy rapido y sin
     * ellas se verian todos a la misma hora.
     */
    private static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /*
     * Registra un cambio de estado.
     * Ejemplo de lo que imprime:
     *
     *   [10:32:01.044] [Proceso-1] NUEVO -> LISTO   (admitido en la cola)
     */
    public void cambioDeEstado(String proceso, EstadoProceso desde,
                               EstadoProceso hacia, String motivo) {
        // LocalTime.now() devuelve la hora de este instante.
        String hora = LocalTime.now().format(HORA);

        System.out.printf("[%s] [%-10s] %-11s -> %-11s  (%s)%n",
                hora, proceso, desde, hacia, motivo);
    }

    /*
     * Un mensaje suelto, sin cambio de estado.
     */
    public void registrar(String quien, String mensaje) {
        String hora = LocalTime.now().format(HORA);
        System.out.printf("[%s] [%-10s] %s%n", hora, quien, mensaje);
    }
}
