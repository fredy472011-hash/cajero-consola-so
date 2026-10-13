/*
 * PASO 2 - Guardar muchas cuentas juntas
 *
 * En el paso 1 teniamos dos variables sueltas: cuenta1 y cuenta2.
 * Eso no sirve: un banco tiene miles de cuentas y no se pueden
 * inventar miles de nombres de variable.
 *
 * La solucion es una LISTA: una sola variable que guarda muchos objetos.
 */

// 'import' trae una clase que vive en otra parte de Java.
// ArrayList y List estan en el paquete java.util.
import java.util.ArrayList;
import java.util.List;

public class Banco {

    /*
     * List<Cuenta> se lee: "una lista DE Cuenta".
     * Lo que va entre < > dice que tipo de cosas guarda. Si intentas meter
     * un texto en una List<Cuenta>, el compilador te frena.
     *
     * List es el tipo general; ArrayList es la implementacion concreta.
     * Se declara con el general y se crea con el concreto: es la costumbre
     * en Java, porque permite cambiar ArrayList por otra cosa mas adelante
     * sin tocar el resto del codigo.
     *
     * 'final' significa que esta variable no va a apuntar a OTRA lista.
     * Ojo: la lista si puede cambiar por dentro (se le agregan cuentas).
     * Lo que no se puede es reemplazarla entera.
     */
    private final List<Cuenta> cuentas = new ArrayList<Cuenta>();

    /*
     * Agrega una cuenta al banco.
     * .add() es el metodo de la lista para meter un elemento al final.
     */
    public void agregar(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    /*
     * Busca una cuenta por su numero.
     * Devuelve la cuenta, o null si no existe.
     *
     * 'null' significa "ninguno", "no hay objeto aca".
     * Quien llame a este metodo TIENE que preguntar si vino null,
     * porque si intenta usar un null el programa se cae con
     * NullPointerException, el error mas comun de Java.
     */
    public Cuenta buscarPorNumero(String numero) {

        // El 'for mejorado' se lee: "para cada Cuenta c dentro de cuentas".
        // Recorre toda la lista sin que haya que manejar un contador.
        for (Cuenta c : cuentas) {
            if (c.getNumero().equals(numero)) {
                return c;   // la encontro: sale del metodo de inmediato
            }
        }
        return null;        // recorrio todo y no la encontro
    }

    /*
     * Cuantas cuentas hay. .size() devuelve el tamano de la lista.
     */
    public int cantidad() {
        return cuentas.size();
    }

    /*
     * Devuelve la lista para poder recorrerla desde afuera.
     */
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    /*
     * Suma el saldo de todas las cuentas.
     */
    public long saldoTotal() {
        long total = 0;
        for (Cuenta c : cuentas) {
            total = total + c.getSaldo();
        }
        return total;
    }

    /*
     * Carga cuentas de prueba. En el paso 8 esto se va a reemplazar
     * por una lectura de MySQL, y el resto del programa no se entera:
     * sigue pidiendole las cuentas al Banco igual que ahora.
     */
    public void cargarCuentasDePrueba() {
        agregar(new Cuenta("1001", "Fredy Canete",  "1234", 2500000));
        agregar(new Cuenta("1002", "Elias Benitez", "4321", 1800000));
        agregar(new Cuenta("1003", "Cuenta Vacia",  "0000",   30000));
    }
}
