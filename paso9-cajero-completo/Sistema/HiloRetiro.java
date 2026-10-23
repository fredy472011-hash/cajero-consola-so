package Sistema;

import Clases.Cuenta;

/*
 * PASO 6 - Un hilo que retira plata
 *
 * QUE ES UN HILO (thread)
 *
 * Hasta ahora el programa hacia una cosa por vez, de arriba hacia abajo.
 * Un HILO es una linea de ejecucion que corre EN PARALELO con las demas.
 *
 *   Sin hilos:   [--- tarea A ---][--- tarea B ---]
 *   Con hilos:   [--- tarea A ---]
 *                [--- tarea B ---]      (las dos al mismo tiempo)
 *
 * Eso es exactamente lo que pasa en un banco: dos cajeros distintos
 * pueden operar sobre la misma cuenta en el mismo segundo.
 *
 * COMO SE HACE UN HILO EN JAVA
 *
 * Se escribe una clase que 'extends Thread' y se le pone el codigo dentro
 * de un metodo llamado run(). Eso es lo que va a correr en paralelo.
 *
 *   'extends' significa HEREDAR: esta clase es un Thread y ademas agrega
 *   lo suyo. Recibe gratis todo lo que Thread ya sabe hacer (start, join...).
 */
public class HiloRetiro extends Thread {

    private final Cuenta cuenta;
    private final long monto;
    private final boolean usarVersionSegura;
    private boolean pudoRetirar;

    public HiloRetiro(String nombre, Cuenta cuenta, long monto, boolean usarVersionSegura) {
        // setName() le pone nombre al hilo, para reconocerlo en pantalla.
        // Es un metodo que viene heredado de Thread.
        setName(nombre);

        this.cuenta = cuenta;
        this.monto = monto;
        this.usarVersionSegura = usarVersionSegura;
    }

    /*
     * '@Override' avisa al compilador: "este metodo reemplaza a uno que
     * ya existia en la clase padre". Si te equivocas al escribir el nombre
     * (por ejemplo 'runn'), el compilador te avisa en vez de dejarlo pasar.
     *
     * run() es el metodo que Thread ejecuta cuando arranca el hilo.
     */
    @Override
    public void run() {

        if (usarVersionSegura) {
            pudoRetirar = cuenta.retirar(monto);
        } else {
            pudoRetirar = cuenta.retirarSinSincronizar(monto);
        }

        System.out.printf("    %-10s pidio Gs. %,d  ->  %s%n",
                getName(), monto, pudoRetirar ? "SE LO DIO" : "RECHAZADO");
    }

    public boolean pudoRetirar() {
        return pudoRetirar;
    }
}
