/*
 * PASO 1 - Probar la clase Cuenta
 *
 * Para correr esto, parado en esta carpeta:
 *
 *     javac *.java
 *     java Main
 *
 * 'javac' compila (traduce el .java a .class, que es lo que entiende Java).
 * 'java Main' ejecuta la clase que tiene el metodo main.
 */
public class Main {

    /*
     * main es el PUNTO DE ARRANQUE. Java siempre empieza aca.
     * La firma tiene que ser exactamente esta:
     *
     *   public  -> accesible desde afuera
     *   static  -> se puede llamar SIN crear un objeto Main.
     *              Tiene sentido: si hay que crear un objeto para arrancar,
     *              quien crearia ese primer objeto?
     *   void    -> no devuelve nada
     *   String[] args -> los parametros que se escriben en la linea de comandos
     */
    public static void main(String[] args) {

        titulo("PASO 1 - Una clase y sus objetos");

        // 'new' fabrica un objeto nuevo a partir del molde (la clase).
        Cuenta cuenta1 = new Cuenta("1001", "Fredy Canete", "1234", 2500000);
        Cuenta cuenta2 = new Cuenta("1002", "Elias Benitez", "4321", 1800000);

        System.out.println("Se crearon dos cuentas con el MISMO molde,");
        System.out.println("pero cada una guarda sus propios datos:");
        System.out.println();

        mostrar(cuenta1);
        mostrar(cuenta2);

        // ------------------------------------------------------------------
        titulo("Depositar 500.000 en la cuenta de Fredy");

        boolean ok = cuenta1.depositar(500000);
        System.out.println("Se pudo depositar? " + ok);
        mostrar(cuenta1);
        System.out.println("La cuenta de Elias NO cambio:");
        mostrar(cuenta2);

        // ------------------------------------------------------------------
        titulo("Retirar 1.000.000 de la cuenta de Fredy");

        ok = cuenta1.retirar(1000000);
        System.out.println("Se pudo retirar? " + ok);
        mostrar(cuenta1);

        // ------------------------------------------------------------------
        titulo("Retirar 99.000.000 (no alcanza el saldo)");

        ok = cuenta1.retirar(99000000);
        System.out.println("Se pudo retirar? " + ok + "   <- la regla funciono");
        mostrar(cuenta1);

        // ------------------------------------------------------------------
        titulo("Probar el PIN");

        System.out.println("PIN 9999 es correcto? " + cuenta1.pinCorrecto("9999"));
        System.out.println("PIN 1234 es correcto? " + cuenta1.pinCorrecto("1234"));

        // ------------------------------------------------------------------
        titulo("Lo importante de este paso");

        System.out.println("El saldo es 'private': desde aca NO se puede hacer");
        System.out.println("    cuenta1.saldo = 999999999;");
        System.out.println("Hay que pasar por depositar() o retirar(), que son");
        System.out.println("los que controlan las reglas. Eso se llama ENCAPSULAMIENTO.");
    }

    /*
     * Un metodo propio para no repetir el println de siempre.
     * Tambien es 'static' porque lo llama main, que es static.
     */
    private static void mostrar(Cuenta c) {
        // printf imprime con formato. Los %s y %,d son huecos que se rellenan:
        //   %-10s  texto alineado a la izquierda en 10 espacios
        //   %,d    numero entero con separador de miles
        //   %n     salto de linea
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
