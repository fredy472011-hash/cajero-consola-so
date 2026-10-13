/*
 * PASO 2 - Probar el Banco
 *
 *     javac *.java
 *     java Main
 */
public class Main {

    public static void main(String[] args) {

        titulo("PASO 2 - Muchas cuentas en una sola lista");

        Banco banco = new Banco();
        banco.cargarCuentasDePrueba();

        System.out.println("El banco tiene " + banco.cantidad() + " cuentas:");
        System.out.println();

        // Recorrer la lista con el for mejorado
        for (Cuenta c : banco.getCuentas()) {
            mostrar(c);
        }

        System.out.printf("%nSaldo total del banco: Gs. %,d%n", banco.saldoTotal());

        // ------------------------------------------------------------------
        titulo("Buscar una cuenta que SI existe");

        Cuenta encontrada = banco.buscarPorNumero("1002");

        // Siempre preguntar por null antes de usar el objeto.
        if (encontrada != null) {
            System.out.println("Encontrada:");
            mostrar(encontrada);
        } else {
            System.out.println("No existe esa cuenta");
        }

        // ------------------------------------------------------------------
        titulo("Buscar una cuenta que NO existe");

        Cuenta fantasma = banco.buscarPorNumero("9999");

        if (fantasma != null) {
            mostrar(fantasma);
        } else {
            System.out.println("No existe la cuenta 9999  ->  el metodo devolvio null");
            System.out.println();
            System.out.println("Si hubieramos hecho  fantasma.getSaldo()  sin preguntar,");
            System.out.println("el programa se caia con NullPointerException.");
            System.out.println("Preguntar por null antes de usar un objeto es obligatorio.");
        }

        // ------------------------------------------------------------------
        titulo("Operar sobre una cuenta de la lista");

        Cuenta deFredy = banco.buscarPorNumero("1001");
        System.out.println("Antes:");
        mostrar(deFredy);

        deFredy.retirar(500000);

        System.out.println("Despues de retirar 500.000:");
        mostrar(deFredy);

        System.out.println();
        System.out.println("Ahora buscamos la 1001 de nuevo en el banco:");
        mostrar(banco.buscarPorNumero("1001"));
        System.out.println();
        System.out.println("Cambio tambien. Es la MISMA cuenta, no una copia:");
        System.out.println("la lista guarda la direccion del objeto, no un duplicado.");
    }

    private static void mostrar(Cuenta c) {
        System.out.printf("  Cuenta %-6s  %-16s  Gs. %,12d%n",
                c.getNumero(), c.getTitular(), c.getSaldo());
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("  " + texto);
        System.out.println("==================================================");
    }
}
