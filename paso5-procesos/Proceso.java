/*
 * PASO 5 - Una transaccion vista como PROCESO del sistema operativo
 *
 * Este es el corazon del trabajo.
 *
 * Cuando alguien retira plata en un cajero, para el sistema operativo eso
 * es un PROCESO: nace, espera su turno, usa la CPU, se bloquea esperando
 * al dispensador, vuelve, y termina.
 *
 * En este paso el proceso se ejecuta de a uno, en orden. En el paso 6
 * vamos a hacer que varios corran AL MISMO TIEMPO con hilos.
 */
public class Proceso {

    /*
     * Un contador compartido por TODOS los procesos, para darles un numero
     * unico (el PID, igual que en Linux o Windows).
     *
     * 'static' hace que sea uno solo para toda la clase, no uno por objeto.
     * Si fuera de cada objeto, todos arrancarian en 1 y no servirian.
     */
    private static int contadorPid = 1;

    // Los tipos de operacion que puede hacer un proceso.
    // Otro enum, igual que EstadoProceso.
    public enum Tipo { RETIRO, DEPOSITO, CONSULTA }

    private final int pid;
    private final Tipo tipo;
    private final Cuenta cuenta;
    private final long monto;
    private final Dispensador dispensador;
    private final Bitacora bitacora;

    private EstadoProceso estado;
    private String resultado;

    /*
     * La prioridad: numero mas chico = mas importante.
     *
     * Una CONSULTA de saldo no toca el dispensador, asi que termina enseguida.
     * Conviene atenderla antes para que no quede esperando detras de un
     * retiro que tarda 2 segundos. Eso se ve en el paso 7.
     */
    private final int prioridad;

    public Proceso(Tipo tipo, Cuenta cuenta, long monto,
                   Dispensador dispensador, Bitacora bitacora) {

        this.pid = contadorPid;
        contadorPid = contadorPid + 1;

        this.tipo = tipo;
        this.cuenta = cuenta;
        this.monto = monto;
        this.dispensador = dispensador;
        this.bitacora = bitacora;

        this.estado = EstadoProceso.NUEVO;
        this.resultado = "";

        // Operador ternario:   condicion ? valorSiEsCierto : valorSiEsFalso
        // Es un if corto que devuelve un valor.
        this.prioridad = (tipo == Tipo.CONSULTA) ? 1 : 5;
    }

    /*
     * Cambia de estado y lo anota en la bitacora.
     * TODOS los cambios pasan por aca: asi ninguno queda sin registrar.
     */
    private void cambiarEstado(EstadoProceso nuevo, String motivo) {
        EstadoProceso anterior = this.estado;
        this.estado = nuevo;
        bitacora.cambioDeEstado(getNombre(), anterior, nuevo, motivo);
    }

    /*
     * NUEVO -> LISTO. Lo llama el planificador cuando admite el proceso.
     */
    public void admitir() {
        cambiarEstado(EstadoProceso.LISTO, "admitido en la cola de listos");
    }

    /*
     * Ejecuta el proceso completo.
     */
    public void ejecutar() {

        cambiarEstado(EstadoProceso.EJECUTANDO, "CPU asignada");

        switch (tipo) {
            case CONSULTA:
                hacerConsulta();
                break;
            case DEPOSITO:
                hacerDeposito();
                break;
            case RETIRO:
                hacerRetiro();
                break;
        }

        cambiarEstado(EstadoProceso.TERMINADO, resultado);
    }

    // ==================================================================

    private void hacerConsulta() {
        // Solo lee memoria. No toca ningun dispositivo, asi que es
        // instantanea: nunca se bloquea.
        resultado = String.format("Saldo: Gs. %,d", cuenta.getSaldo());
    }

    private void hacerDeposito() {
        // Recibir y contar los billetes es entrada/salida: tarda.
        esperarEntradaSalida("deposito: contando los billetes recibidos", 1200);

        if (cuenta.depositar(monto)) {
            resultado = String.format("Deposito de Gs. %,d acreditado", monto);
        } else {
            resultado = "Deposito rechazado";
        }
    }

    private void hacerRetiro() {

        // 1. Descontar de la cuenta. Esto es solo memoria: rapido.
        if (!cuenta.retirar(monto)) {
            resultado = "Saldo insuficiente";
            return;
        }

        // 2. Entregar los billetes. ESTO ES ENTRADA/SALIDA: el dispensador
        //    es un aparato mecanico y tarda. El proceso se BLOQUEA.
        cambiarEstado(EstadoProceso.BLOQUEADO, "solicita E/S: dispensador");

        int[] billetes = dispensador.dispensar(monto);
        dormir(1800);   // el dispensador trabajando

        cambiarEstado(EstadoProceso.LISTO, "E/S completada");
        cambiarEstado(EstadoProceso.EJECUTANDO, "CPU reasignada");

        if (billetes == null) {
            // No se pudo armar el monto: hay que devolverle la plata.
            cuenta.depositar(monto);
            resultado = String.format("No hay billetes para Gs. %,d (reintegrado)", monto);
        } else {
            resultado = "Entregado: " + dispensador.describir(billetes);
        }
    }

    /*
     * Simula una operacion de entrada/salida que bloquea al proceso.
     */
    private void esperarEntradaSalida(String motivo, long milisegundos) {
        cambiarEstado(EstadoProceso.BLOQUEADO, motivo);
        dormir(milisegundos);
        cambiarEstado(EstadoProceso.LISTO, "E/S completada");
        cambiarEstado(EstadoProceso.EJECUTANDO, "CPU reasignada");
    }

    /*
     * Thread.sleep() detiene la ejecucion la cantidad de milisegundos
     * que se le diga. 1000 milisegundos = 1 segundo.
     *
     * Lo usamos para que las operaciones lentas se vean lentas. Sin esto
     * todo pasaria tan rapido que no se notaria nada en pantalla.
     *
     * Hace falta el try/catch porque sleep puede ser INTERRUMPIDO por otro
     * hilo. Java obliga a decir que se hace en ese caso.
     */
    private void dormir(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            // Si nos interrumpen, volvemos a marcar el hilo como interrumpido
            // para que quien nos llamo se entere.
            Thread.currentThread().interrupt();
        }
    }

    // ==================================================================

    public String getNombre()        { return "Proceso-" + pid; }
    public int getPid()              { return pid; }
    public Tipo getTipo()            { return tipo; }
    public EstadoProceso getEstado() { return estado; }
    public String getResultado()     { return resultado; }
    public int getPrioridad()        { return prioridad; }
    public long getMonto()           { return monto; }
}
