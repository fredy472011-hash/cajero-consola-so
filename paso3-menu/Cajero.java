/*
 * PASO 3 - El cajero: leer del teclado y mostrar el menu
 *
 * Hasta ahora el programa hacia siempre lo mismo. Ahora decide el usuario.
 *
 * Esta clase tiene TODA la logica del cajero. Main solo la arranca.
 * Esa separacion importa: si manana en vez de consola fuera una pantalla
 * grafica, esta clase no cambiaria casi nada.
 */

// Scanner sirve para LEER lo que el usuario escribe.
import java.util.Scanner;

public class Cajero {

    private final Banco banco;
    private final Scanner teclado;

    // La cuenta de quien esta usando el cajero ahora.
    // null = no hay nadie, el cajero esta esperando una tarjeta.
    private Cuenta cuentaActual;

    private static final int MAXIMO_INTENTOS_PIN = 3;

    /*
     * El constructor recibe el banco y el teclado desde afuera en vez de
     * fabricarlos aca adentro. Asi el Cajero no depende de DONDE salen:
     * manana el banco puede venir de MySQL y esta clase ni se entera.
     */
    public Cajero(Banco banco, Scanner teclado) {
        this.banco = banco;
        this.teclado = teclado;
    }

    /*
     * Arranca el cajero. Se queda dando vueltas hasta que el usuario sale.
     */
    public void iniciar() {

        // 'while (true)' es un bucle infinito: se repite para siempre.
        // Solo se corta con un 'break' o un 'return' adentro.
        while (true) {

            cuentaActual = null;     // cada vuelta empieza sin tarjeta

            if (!pedirTarjeta()) {
                // pedirTarjeta devolvio false = el usuario quiere salir
                System.out.println();
                System.out.println("Cajero apagado. Hasta luego.");
                return;              // 'return' termina el metodo iniciar()
            }

            if (pedirPin()) {
                menuPrincipal();
            }
        }
    }

    // ==================================================================
    // PASO A: insertar la tarjeta
    // ==================================================================
    private boolean pedirTarjeta() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("   BANCO SIMULADO - Inserte su tarjeta");
        System.out.println("==================================================");
        System.out.println("   Cuentas de prueba: 1001  1002  1003");
        System.out.println("   Escriba 'salir' para apagar el cajero");
        System.out.println();
        System.out.print("Numero de tarjeta: ");

        // nextLine() espera a que el usuario escriba y apriete Enter,
        // y devuelve todo lo que escribio como texto.
        String numero = teclado.nextLine().trim();
        // .trim() saca los espacios de los costados: si escribio " 1001 "
        // lo deja en "1001".

        // equalsIgnoreCase compara textos sin distinguir mayusculas:
        // "SALIR", "Salir" y "salir" dan lo mismo.
        if (numero.equalsIgnoreCase("salir")) {
            return false;
        }

        cuentaActual = banco.buscarPorNumero(numero);

        if (cuentaActual == null) {
            System.out.println();
            System.out.println("  >> Tarjeta no reconocida.");
            return pedirTarjeta();   // vuelve a preguntar
        }

        return true;
    }

    // ==================================================================
    // PASO B: validar el PIN
    // ==================================================================
    private boolean pedirPin() {
        int intentos = 0;

        // 'while' con condicion: se repite MIENTRAS la condicion sea cierta.
        while (intentos < MAXIMO_INTENTOS_PIN) {
            System.out.println();
            System.out.print("Digite su PIN: ");
            String pin = teclado.nextLine().trim();

            if (cuentaActual.pinCorrecto(pin)) {
                System.out.println();
                System.out.println("  Bienvenido, " + cuentaActual.getTitular());
                return true;
            }

            intentos = intentos + 1;      // tambien se escribe  intentos++;
            int quedan = MAXIMO_INTENTOS_PIN - intentos;

            if (quedan > 0) {
                System.out.println("  >> PIN incorrecto. Le quedan " + quedan + " intentos.");
            }
        }

        System.out.println();
        System.out.println("  >> Tarjeta retenida por seguridad.");
        return false;
    }

    // ==================================================================
    // PASO C: el menu
    // ==================================================================
    private void menuPrincipal() {

        while (true) {
            System.out.println();
            System.out.println("--------------------------------------------------");
            System.out.println("   Elija una transaccion");
            System.out.println("--------------------------------------------------");
            System.out.println("   1) Consultar saldo");
            System.out.println("   2) Extraccion");
            System.out.println("   3) Deposito");
            System.out.println("   4) Retirar tarjeta");
            System.out.println();
            System.out.print("Opcion: ");

            String opcion = teclado.nextLine().trim();

            /*
             * 'switch' compara una variable contra varios valores posibles.
             * Es mas claro que una cadena larga de if / else if.
             *
             * Cada 'case' necesita su 'break', si no sigue ejecutando el
             * case de abajo. Ese olvido es un error clasico en Java.
             *
             * 'default' es lo que pasa si no coincide con ningun case.
             */
            switch (opcion) {
                case "1":
                    consultarSaldo();
                    break;
                case "2":
                    extraer();
                    break;
                case "3":
                    depositar();
                    break;
                case "4":
                    System.out.println();
                    System.out.println("  Retire su tarjeta. Gracias por su visita.");
                    return;     // sale del menu y vuelve a pedir tarjeta
                default:
                    System.out.println("  >> Opcion no valida.");
                    break;
            }
        }
    }

    // ==================================================================
    // Las tres operaciones
    // ==================================================================

    private void consultarSaldo() {
        System.out.println();
        System.out.printf("  Saldo disponible: Gs. %,d%n", cuentaActual.getSaldo());
    }

    private void extraer() {
        System.out.println();
        System.out.print("Monto a retirar: ");
        long monto = leerMonto();

        if (monto <= 0) {
            System.out.println("  >> Monto no valido.");
            return;
        }

        // Aca se ve por que la regla vive dentro de Cuenta y no aca:
        // el Cajero solo pregunta "se pudo?" y no toca el saldo directamente.
        if (cuentaActual.retirar(monto)) {
            System.out.printf("  Retire Gs. %,d%n", monto);
            System.out.printf("  Nuevo saldo: Gs. %,d%n", cuentaActual.getSaldo());
        } else {
            System.out.println("  >> Saldo insuficiente.");
        }
    }

    private void depositar() {
        System.out.println();
        System.out.print("Monto a depositar: ");
        long monto = leerMonto();

        if (cuentaActual.depositar(monto)) {
            System.out.printf("  Deposito acreditado: Gs. %,d%n", monto);
            System.out.printf("  Nuevo saldo: Gs. %,d%n", cuentaActual.getSaldo());
        } else {
            System.out.println("  >> Monto no valido.");
        }
    }

    /*
     * Convierte lo que escribio el usuario en un numero.
     *
     * El usuario puede escribir cualquier cosa ("hola", "12a3", nada).
     * Long.parseLong() revienta con esos textos, asi que hay que atraparlo.
     *
     * try / catch:
     *   try   -> "intenta esto"
     *   catch -> "si explota, haz esto otro en vez de caerte"
     *
     * Sin el try/catch, escribir "hola" cerraria el programa entero.
     */
    private long leerMonto() {
        String texto = teclado.nextLine().trim();
        try {
            return Long.parseLong(texto);
        } catch (NumberFormatException e) {
            // NumberFormatException es el error que lanza parseLong
            // cuando el texto no es un numero.
            return -1;
        }
    }
}
