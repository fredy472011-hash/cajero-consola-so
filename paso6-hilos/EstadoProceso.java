/*
 * PASO 5 - Los estados de un proceso
 *
 * Esto es TEORIA DE LA MATERIA puesta en codigo.
 *
 * Segun el material de catedra (Tanenbaum, seccion 2.1.5), un proceso
 * pasa por estos estados:
 *
 *      NUEVO
 *        |  el sistema lo admite
 *        v
 *      LISTO  <-------------------+
 *        |  el planificador       |
 *        |  le da la CPU          | la E/S termino
 *        v                        |
 *    EJECUTANDO ---------->  BLOQUEADO
 *        |      pide E/S
 *        |  termino
 *        v
 *    TERMINADO
 *
 * Lo importante: BLOQUEADO **no consume CPU**. Mientras un proceso espera
 * que el dispensador entregue los billetes, otro proceso puede ejecutarse.
 * Ahi esta la diferencia entre un sistema que aprovecha la CPU y uno que no.
 */

/*
 * 'enum' (enumeracion) es un tipo que solo puede tener UNOS VALORES FIJOS,
 * los que se escriben aca abajo. No existe un sexto estado posible.
 *
 * Por que no usar un String o un int?
 *
 *   String estado = "EJECUTANDO";   // se puede escribir "EJCUTANDO" y compila
 *   int estado = 7;                 // que significa 7?
 *   EstadoProceso e = EstadoProceso.EJECUTANDO;   // si te equivocas, no compila
 *
 * El enum hace que el compilador te atrape el error antes de ejecutar.
 */
public enum EstadoProceso {

    NUEVO,        // creado, todavia no entro en la cola
    LISTO,        // esperando que le den la CPU
    EJECUTANDO,   // tiene la CPU, esta corriendo instrucciones
    BLOQUEADO,    // esperando entrada/salida. NO usa CPU
    TERMINADO     // ya termino, se liberan sus recursos
}
