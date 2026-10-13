/*
 * PASO 8 - La memoria de largo plazo
 *
 * ANTES, crear la base (una sola vez):
 *
 *     mysql -u root -p < ../sql/esquema.sql
 *
 * DESPUES, compilar y ejecutar INCLUYENDO EL DRIVER:
 *
 *     javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
 *     java  -cp ".;mysql-connector-j-9.4.0.jar" Main
 *
 * (en Linux o Mac cambiar el ; por : )
 *
 * Si se olvida el -cp, el error es:
 *     No suitable driver found for jdbc:mysql://...
 *
 * ESTE PASO DEMUESTRA EL TEMA DE LA MATERIA:
 * la diferencia entre memoria volatil y memoria de largo plazo.
 */

import java.util.List;

public class Main {

    public static void main(String[] args) {

        titulo("PASO 8 - MEMORIA VOLATIL contra MEMORIA DE LARGO PLAZO");

        if (!BaseDeDatos.responde()) {
            return;   // el mensaje de error ya lo mostro responde()
        }
        System.out.println("  Conectado a MySQL.");

        BancoBD banco = new BancoBD();

        // ------------------------------------------------------------------
        titulo("1. LEER LAS CUENTAS DE LA BASE");
        System.out.println("  Estas cuentas NO estan escritas en el programa.");
        System.out.println("  Salen de la tabla 'cuentas' de MySQL.");
        System.out.println();

        List<Cuenta> cuentas = banco.listar();
        for (Cuenta c : cuentas) {
            System.out.printf("    %-6s  %-16s  Gs. %,12d%n",
                    c.getNumero(), c.getTitular(), c.getSaldo());
        }

        // ------------------------------------------------------------------
        titulo("2. RETIRAR 300.000 DE LA CUENTA 1001");

        Cuenta cuenta = banco.buscarPorNumero("1001");

        if (cuenta == null) {
            System.out.println("  No existe la cuenta 1001.");
            return;
        }

        System.out.printf("  Saldo antes: Gs. %,d%n", cuenta.getSaldo());

        long monto = 300000;

        if (cuenta.retirar(monto)) {

            System.out.println();
            System.out.println("  El objeto en memoria ya cambio:");
            System.out.printf("    Saldo en RAM: Gs. %,d%n", cuenta.getSaldo());
            System.out.println();
            System.out.println("  PERO en la base todavia no. Si se cortara la luz");
            System.out.println("  en este instante, el retiro se perderia entero.");
            System.out.println();
            System.out.println("  Ahora si, se graba:");

            banco.actualizarSaldo(cuenta.getNumero(), cuenta.getSaldo());
            banco.registrarMovimiento(cuenta.getNumero(), "RETIRO",
                                      monto, cuenta.getSaldo());

            System.out.println("    >> Grabado. A partir de aca sobrevive a todo.");
        } else {
            System.out.println("  Saldo insuficiente.");
        }

        // ------------------------------------------------------------------
        titulo("3. LA PRUEBA: VOLVER A LEER DESDE CERO");
        System.out.println("  Se tira el objeto que teniamos y se pide de nuevo");
        System.out.println("  a la base, como si el programa recien arrancara:");
        System.out.println();

        cuenta = null;   // se descarta la copia en memoria

        Cuenta recienLeida = banco.buscarPorNumero("1001");
        System.out.printf("    Saldo en la base: Gs. %,d%n", recienLeida.getSaldo());
        System.out.println();
        System.out.println("  >> El retiro quedo. Eso es memoria de largo plazo.");

        // ------------------------------------------------------------------
        titulo("4. EL HISTORIAL");
        System.out.println("  Cada operacion quedo registrada para siempre:");
        System.out.println();

        List<String> movimientos = banco.ultimosMovimientos("1001", 5);

        if (movimientos.isEmpty()) {
            System.out.println("    (todavia no hay movimientos)");
        } else {
            for (String m : movimientos) {
                System.out.println("    " + m);
            }
        }

        // ------------------------------------------------------------------
        titulo("PARA LA DEFENSA DEL TRABAJO");
        System.out.println("  CORRE ESTE PROGRAMA DOS O TRES VECES SEGUIDAS.");
        System.out.println();
        System.out.println("  Cada vez el saldo arranca 300.000 mas abajo y aparece");
        System.out.println("  un movimiento nuevo en el historial. El programa se");
        System.out.println("  cerro entero entre una corrida y la otra, pero los");
        System.out.println("  datos siguen ahi.");
        System.out.println();
        System.out.println("    Lista en memoria (pasos 1 a 7)  ->  VOLATIL");
        System.out.println("       se pierde al cerrar el programa");
        System.out.println();
        System.out.println("    Tabla en MySQL (este paso)      ->  LARGO PLAZO");
        System.out.println("       sobrevive al apagado y al corte de luz");
        System.out.println();
        System.out.println("  Un cajero real tiene las dos: el PIN que escribiste");
        System.out.println("  vive en RAM y se borra al cancelar, pero el saldo");
        System.out.println("  va a la base y no se borra nunca.");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("======================================================================");
        System.out.println("  " + texto);
        System.out.println("======================================================================");
    }
}
