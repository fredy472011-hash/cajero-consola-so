/*
 * PASO 1 - Una clase y un objeto
 *
 * Una CLASE es un molde. Describe que datos tiene algo y que sabe hacer.
 * De un molde se pueden fabricar muchos OBJETOS distintos.
 *
 *   Clase  Cuenta  ->  el molde (esto)
 *   Objeto cuenta1 ->  la cuenta de Fredy,  saldo 2.500.000
 *   Objeto cuenta2 ->  la cuenta de Elias,  saldo 1.800.000
 *
 * Los dos objetos usan el mismo molde pero tienen datos propios.
 */
public class Cuenta {

    // ------------------------------------------------------------------
    // ATRIBUTOS: los datos que guarda cada cuenta.
    //
    // 'private' significa que SOLO el codigo de esta clase puede tocarlos.
    // Desde afuera nadie puede hacer  cuenta.saldo = 999999;
    // Eso obliga a pasar por los metodos, que son los que controlan las
    // reglas (no dejar saldo negativo, no depositar montos negativos, etc).
    // ------------------------------------------------------------------
    private String numero;
    private String titular;
    private String pin;
    private long saldo;     // 'long' es un numero entero grande.
                            // 'int' llega hasta 2.147.483.647, y en guaranies
                            // eso se queda corto rapido. Por eso long.

    // ------------------------------------------------------------------
    // CONSTRUCTOR: el metodo que se ejecuta al crear el objeto.
    //
    // Se llama IGUAL que la clase y no lleva tipo de retorno (ni void).
    // Se usa asi:   Cuenta c = new Cuenta("1001", "Fredy", "1234", 2500000);
    // ------------------------------------------------------------------
    public Cuenta(String numero, String titular, String pin, long saldoInicial) {
        // 'this' significa "este objeto".
        // Hace falta porque el atributo y el parametro se llaman igual:
        //   this.numero  es el atributo de la cuenta
        //   numero       es el parametro que llego por el parentesis
        this.numero = numero;
        this.titular = titular;
        this.pin = pin;
        this.saldo = saldoInicial;
    }

    // ------------------------------------------------------------------
    // METODOS: lo que la cuenta sabe hacer.
    // ------------------------------------------------------------------

    /*
     * Deposita dinero.
     * Devuelve true si se pudo, false si el monto no servia.
     *
     * 'boolean' es un tipo que solo guarda true o false.
     */
    public boolean depositar(long monto) {
        if (monto <= 0) {
            return false;   // 'return' termina el metodo y devuelve el valor
        }
        saldo = saldo + monto;
        return true;
    }

    /*
     * Retira dinero. Solo si hay saldo suficiente.
     */
    public boolean retirar(long monto) {
        if (monto <= 0) {
            return false;
        }
        if (saldo < monto) {
            return false;   // no alcanza
        }
        saldo = saldo - monto;
        return true;
    }

    /*
     * Comprueba el PIN.
     *
     * OJO: para comparar textos se usa  .equals()  y NO  ==
     * El  ==  compara si son el MISMO objeto en memoria, no si dicen lo mismo.
     * Con numeros (int, long) si se usa  ==  normalmente.
     */
    public boolean pinCorrecto(String intento) {
        return pin.equals(intento);
    }

    // ------------------------------------------------------------------
    // GETTERS: metodos para leer los atributos desde afuera.
    //
    // Como los atributos son 'private', nadie los ve. Estos metodos los
    // dejan LEER pero no cambiar. Por eso hay getSaldo() pero no setSaldo():
    // el saldo solo se cambia depositando o retirando.
    // ------------------------------------------------------------------
    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public long getSaldo() {
        return saldo;
    }
}
