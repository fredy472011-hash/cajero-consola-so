/*
 * PASO 6 - La cuenta con DOS versiones del retiro
 *
 * Hasta el paso 5 habia un solo proceso a la vez, asi que nunca hubo
 * problema. Ahora van a correr VARIOS AL MISMO TIEMPO sobre la misma
 * cuenta, y ahi aparece el error mas famoso de la concurrencia:
 * la CONDICION DE CARRERA.
 *
 * Esta clase tiene el retiro escrito de dos formas A PROPOSITO:
 *
 *   retirarSinSincronizar()  -> el que falla. Deja el saldo NEGATIVO.
 *   retirar()                -> el correcto, con synchronized.
 *
 * Mostrar el error primero y despues arreglarlo vale mucho mas que
 * mostrar solo la solucion: demuestra que se entendio el problema.
 *
 * Material de catedra: Procesos y Threads, seccion 2.3.1
 */
public class Cuenta {

    private String numero;
    private String titular;
    private String pin;
    private long saldo;

    public Cuenta(String numero, String titular, String pin, long saldoInicial) {
        this.numero = numero;
        this.titular = titular;
        this.pin = pin;
        this.saldo = saldoInicial;
    }

    // ==================================================================
    // VERSION QUE FALLA
    // ==================================================================

    /*
     * Retiro SIN proteger.
     *
     * El problema esta en que esto NO es una sola operacion, son dos:
     *
     *     1. LEER el saldo y preguntar si alcanza
     *     2. ESCRIBIR el saldo nuevo
     *
     * Entre el paso 1 y el paso 2 hay un huequito. Si justo ahi entra
     * otro hilo, los DOS leen el saldo viejo, los DOS creen que alcanza,
     * y los DOS descuentan. Resultado: se entrego mas plata de la que habia.
     *
     * El Thread.sleep() de abajo no causa el error: solo AGRANDA el hueco
     * para que la falla se vea siempre. Sin el sleep el error existe igual,
     * pero aparece una vez cada mil y por eso es tan peligroso.
     *
     * Por que 300 milisegundos y no 50: arrancar el PRIMER hilo de un
     * programa Java tarda unas decenas de milisegundos, porque recien ahi
     * se cargan las clases que hacen falta. Con una ventana corta, el
     * segundo hilo a veces llegaba tarde y el error no se veia. Con 300 ms
     * los dos entran seguro y la demostracion sale siempre.
     */
    public boolean retirarSinSincronizar(long monto) {

        if (saldo >= monto) {          // 1. LEER  ("alcanza?")

            dormir(300);               //    <-- aca entra el otro hilo

            saldo = saldo - monto;     // 2. ESCRIBIR
            return true;
        }
        return false;
    }

    // ==================================================================
    // VERSION CORRECTA
    // ==================================================================

    /*
     * 'synchronized' pone un CANDADO sobre este objeto.
     *
     * Mientras un hilo esta adentro del metodo, ningun otro hilo puede
     * entrar: tienen que esperar en la puerta. Cuando el primero sale,
     * entra el siguiente y recien ahi lee el saldo, que ya esta actualizado.
     *
     * Asi leer y escribir se vuelven UNA SOLA operacion indivisible.
     * En la materia a eso se lo llama operacion ATOMICA, y al pedazo de
     * codigo protegido se lo llama REGION CRITICA (seccion 2.3.2).
     *
     * OJO - detalle que se pasa por alto:
     * el candado es del OBJETO, no del metodo. retirar() y depositar()
     * son los dos synchronized, asi que comparten el mismo candado:
     * si un hilo esta retirando, otro tampoco puede depositar. Eso esta
     * bien, porque los dos tocan 'saldo'.
     *
     * Pero son candados POR CUENTA: dos hilos operando sobre cuentas
     * distintas no se estorban entre si.
     */
    public synchronized boolean retirar(long monto) {
        if (monto <= 0) {
            return false;
        }
        if (saldo < monto) {
            return false;
        }
        saldo = saldo - monto;
        return true;
    }

    public synchronized boolean depositar(long monto) {
        if (monto <= 0) {
            return false;
        }
        saldo = saldo + monto;
        return true;
    }

    /*
     * Leer tambien se sincroniza. Si no, un hilo podria leer el saldo
     * justo en la mitad de una modificacion y ver un valor a medio hacer.
     */
    public synchronized long getSaldo() {
        return saldo;
    }

    public boolean pinCorrecto(String intento) {
        return pin.equals(intento);
    }

    public String getNumero()  { return numero; }
    public String getTitular() { return titular; }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
