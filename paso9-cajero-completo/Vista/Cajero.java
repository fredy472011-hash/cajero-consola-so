package Vista;

import Clases.Cuenta;
import BaseDatos.BancoBD;
import Sistema.Proceso;
import Sistema.Dispensador;
import Sistema.Bitacora;

/*
 * PASO 9 - El cajero completo
 *
 * Aca se junta todo lo de los pasos anteriores:
 *
 *   paso 1 y 2 -> Cuenta
 *   paso 3     -> el menu por consola
 *   paso 4     -> Dispensador
 *   paso 5     -> Proceso con sus estados
 *   paso 6     -> synchronized
 *   paso 8     -> MySQL
 *
 * Cada operacion del usuario crea un PROCESO, que recorre sus estados en
 * pantalla, y al terminar GRABA el resultado en la base de datos.
 */

import java.util.List;
import java.util.Scanner;

public class Cajero {

    private final BancoBD banco;
    private final Dispensador dispensador;
    private final Bitacora bitacora;
    private final Scanner teclado;

    /*
     * MEMORIA VOLATIL (la RAM del cajero).
     *
     * Todo lo que vive en estos tres atributos se PIERDE cuando el usuario
     * cancela o retira la tarjeta. No queda ningun rastro.
     *
     * El saldo real, en cambio, esta en MySQL y sobrevive a todo.
     * Esa diferencia es uno de los temas centrales de la materia.
     */
    private Cuenta cuentaActual;
    private boolean pinValidado;
    private long montoEnCurso;

    // Para mostrar la tabla de procesos al final
    private final List<Proceso> procesosDeLaSesion = new java.util.ArrayList<Proceso>();

    private static final int MAXIMO_INTENTOS_PIN = 3;

    public Cajero(BancoBD banco, Dispensador dispensador,
                  Bitacora bitacora, Scanner teclado) {
        this.banco = banco;
        this.dispensador = dispensador;
        this.bitacora = bitacora;
        this.teclado = teclado;
    }

    // ==================================================================
    public void iniciar() {

        while (true) {

            limpiarMemoriaVolatil();

            if (!pedirTarjeta()) {
                System.out.println();
                System.out.println("  Cajero apagado. Hasta luego.");
                return;
            }

            if (pedirPin()) {
                menuPrincipal();
            }
        }
    }

    /*
     * Borra toda la memoria volatil. Es lo que pasa al cancelar o al
     * cortarse la luz: el cajero se olvida de todo.
     */
    private void limpiarMemoriaVolatil() {
        cuentaActual = null;
        pinValidado = false;
        montoEnCurso = 0;
    }

    // ==================================================================
    private boolean pedirTarjeta() {

        System.out.println();
        System.out.println("======================================================================");
        System.out.println("   BANCO SIMULADO - Inserte su tarjeta");
        System.out.println("======================================================================");
        System.out.println("   Cuentas de prueba: 1001  1002  1003");
        System.out.println("   Escriba 'salir' para apagar el cajero");
        System.out.println();
        System.out.print("Numero de tarjeta: ");

        String numero = teclado.nextLine().trim();

        if (numero.equalsIgnoreCase("salir")) {
            return false;
        }

        // Lee de MySQL (memoria de largo plazo)
        cuentaActual = banco.buscarPorNumero(numero);

        if (cuentaActual == null) {
            System.out.println();
            System.out.println("  >> Tarjeta no reconocida.");
            return pedirTarjeta();
        }

        bitacora.registrar("CAJERO", "tarjeta leida: " + numero
                + " (" + cuentaActual.getTitular() + ")");
        return true;
    }

    private boolean pedirPin() {

        int intentos = 0;

        while (intentos < MAXIMO_INTENTOS_PIN) {

            System.out.println();
            System.out.print("Digite su PIN: ");
            String pin = teclado.nextLine().trim();

            if (cuentaActual.pinCorrecto(pin)) {
                pinValidado = true;
                bitacora.registrar("CAJERO", "PIN correcto");
                System.out.println();
                System.out.println("  Bienvenido, " + cuentaActual.getTitular());
                return true;
            }

            intentos = intentos + 1;
            int quedan = MAXIMO_INTENTOS_PIN - intentos;

            if (quedan > 0) {
                System.out.println("  >> PIN incorrecto. Le quedan " + quedan + " intentos.");
            }
        }

        bitacora.registrar("CAJERO", "tarjeta retenida: 3 PIN incorrectos");
        System.out.println();
        System.out.println("  >> Tarjeta retenida por seguridad.");
        return false;
    }

    // ==================================================================
    private void menuPrincipal() {

        while (true) {

            System.out.println();
            System.out.println("----------------------------------------------------------------------");
            System.out.println("   Elija una transaccion");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("   1) Consultar saldo");
            System.out.println("   2) Extraccion");
            System.out.println("   3) Deposito");
            System.out.println("   4) Ver ultimos movimientos");
            System.out.println("   5) Ver billetes disponibles");
            System.out.println("   6) Ver la tabla de procesos del sistema operativo");
            System.out.println("   7) CANCELAR  (borra la memoria volatil)");
            System.out.println("   8) Retirar tarjeta");
            System.out.println();
            System.out.print("Opcion: ");

            String opcion = teclado.nextLine().trim();

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
                    verMovimientos();
                    break;
                case "5":
                    System.out.println();
                    dispensador.mostrarCasetes();
                    break;
                case "6":
                    mostrarTablaDeProcesos();
                    break;
                case "7":
                    demostrarCancelacion();
                    return;
                case "8":
                    System.out.println();
                    System.out.println("  Retire su tarjeta. Gracias por su visita.");
                    return;
                default:
                    System.out.println("  >> Opcion no valida.");
                    break;
            }
        }
    }

    // ==================================================================
    // Las operaciones, cada una como un PROCESO
    // ==================================================================

    private void consultarSaldo() {
        System.out.println();
        Proceso p = new Proceso(Proceso.Tipo.CONSULTA, cuentaActual, 0,
                                dispensador, bitacora);
        procesosDeLaSesion.add(p);
        p.admitir();
        p.ejecutar();

        System.out.println();
        System.out.println("  " + p.getResultado());

        // Una consulta tambien queda registrada
        banco.registrarMovimiento(cuentaActual.getNumero(), "CONSULTA",
                                  0, cuentaActual.getSaldo());
    }

    private void extraer() {

        System.out.println();
        System.out.print("Monto a retirar: ");
        long monto = leerMonto();

        if (monto <= 0) {
            System.out.println("  >> Monto no valido.");
            return;
        }

        // Antes de tocar nada: se puede armar con los billetes que hay?
        if (dispensador.planificar(monto) == null) {
            System.out.println("  >> No se puede formar ese monto con los billetes");
            System.out.println("     disponibles. Pruebe con multiplos de 50.000.");
            return;
        }

        montoEnCurso = monto;   // queda en memoria VOLATIL
        System.out.println();

        Proceso p = new Proceso(Proceso.Tipo.RETIRO, cuentaActual, monto,
                                dispensador, bitacora);
        procesosDeLaSesion.add(p);
        p.admitir();
        p.ejecutar();

        System.out.println();
        System.out.println("  " + p.getResultado());

        // GRABAR EN LA MEMORIA DE LARGO PLAZO.
        // Hasta esta linea el retiro vivia solo en RAM.
        if (p.getResultado().startsWith("Entregado")) {
            banco.actualizarSaldo(cuentaActual.getNumero(), cuentaActual.getSaldo());
            banco.registrarMovimiento(cuentaActual.getNumero(), "RETIRO",
                                      monto, cuentaActual.getSaldo());
            System.out.printf("  Nuevo saldo: Gs. %,d%n", cuentaActual.getSaldo());
            System.out.println("  (grabado en la base de datos)");
        }

        montoEnCurso = 0;
    }

    private void depositar() {

        System.out.println();
        System.out.print("Monto a depositar: ");
        long monto = leerMonto();

        if (monto <= 0) {
            System.out.println("  >> Monto no valido.");
            return;
        }

        montoEnCurso = monto;
        System.out.println();

        Proceso p = new Proceso(Proceso.Tipo.DEPOSITO, cuentaActual, monto,
                                dispensador, bitacora);
        procesosDeLaSesion.add(p);
        p.admitir();
        p.ejecutar();

        System.out.println();
        System.out.println("  " + p.getResultado());

        if (p.getResultado().startsWith("Deposito de")) {
            banco.actualizarSaldo(cuentaActual.getNumero(), cuentaActual.getSaldo());
            banco.registrarMovimiento(cuentaActual.getNumero(), "DEPOSITO",
                                      monto, cuentaActual.getSaldo());
            System.out.printf("  Nuevo saldo: Gs. %,d%n", cuentaActual.getSaldo());
            System.out.println("  (grabado en la base de datos)");
        }

        montoEnCurso = 0;
    }

    private void verMovimientos() {

        System.out.println();
        System.out.println("  Ultimos movimientos (desde MySQL):");
        System.out.println();

        List<String> movimientos = banco.ultimosMovimientos(cuentaActual.getNumero(), 8);

        if (movimientos.isEmpty()) {
            System.out.println("    (no hay movimientos)");
            return;
        }

        for (String m : movimientos) {
            System.out.println("    " + m);
        }
    }

    // ==================================================================
    // Las dos pantallas que muestran el sistema operativo por dentro
    // ==================================================================

    private void mostrarTablaDeProcesos() {

        System.out.println();
        System.out.println("  TABLA DE PROCESOS DE ESTA SESION");
        System.out.println("  (en un sistema operativo real se llama");
        System.out.println("   Bloque de Control de Proceso)");
        System.out.println();

        if (procesosDeLaSesion.isEmpty()) {
            System.out.println("    Todavia no se ejecuto ningun proceso.");
            return;
        }

        System.out.printf("    %-5s %-10s %-11s %-6s %s%n",
                "PID", "TIPO", "ESTADO", "PRIO", "RESULTADO");
        System.out.println("    " + "-".repeat(70));

        for (Proceso p : procesosDeLaSesion) {
            System.out.printf("    %-5d %-10s %-11s %-6d %s%n",
                    p.getPid(), p.getTipo(), p.getEstado(),
                    p.getPrioridad(), p.getResultado());
        }
    }

    /*
     * La demostracion mas importante para la materia.
     */
    private void demostrarCancelacion() {

        System.out.println();
        System.out.println("======================================================================");
        System.out.println("  CANCELANDO - mira que se pierde y que no");
        System.out.println("======================================================================");

        String numero = cuentaActual.getNumero();
        long saldoEnMemoria = cuentaActual.getSaldo();

        System.out.println();
        System.out.println("  ANTES de cancelar, en la memoria VOLATIL habia:");
        System.out.println("    Tarjeta leida:  " + numero);
        System.out.println("    Titular:        " + cuentaActual.getTitular());
        System.out.println("    PIN validado:   " + pinValidado);
        System.out.printf("    Monto en curso: Gs. %,d%n", montoEnCurso);
        System.out.printf("    Saldo en RAM:   Gs. %,d%n", saldoEnMemoria);

        bitacora.registrar("CAJERO", "CANCELAR: se borra la memoria volatil");
        limpiarMemoriaVolatil();

        System.out.println();
        System.out.println("  DESPUES de cancelar:");
        System.out.println("    Tarjeta leida:  " + cuentaActual);   // null
        System.out.println("    PIN validado:   " + pinValidado);
        System.out.printf("    Monto en curso: Gs. %,d%n", montoEnCurso);
        System.out.println();
        System.out.println("    >> Se borro TODO. El cajero se olvido de quien era.");

        System.out.println();
        System.out.println("  Pero la MEMORIA DE LARGO PLAZO no se toco.");
        System.out.println("  Se vuelve a leer la cuenta " + numero + " desde MySQL:");
        System.out.println();

        Cuenta desdeLaBase = banco.buscarPorNumero(numero);
        System.out.printf("    Saldo en la base: Gs. %,d%n", desdeLaBase.getSaldo());

        System.out.println();
        System.out.println("  >>> ESTA ES LA DEMOSTRACION CENTRAL DEL TRABAJO:");
        System.out.println("      una memoria se borro entera, la otra quedo intacta.");
    }

    // ==================================================================
    private long leerMonto() {
        String texto = teclado.nextLine().trim();
        try {
            return Long.parseLong(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
