/*
 * PASO 4 - El dispensador de billetes
 *
 * Hasta ahora el cajero restaba un numero del saldo y listo. Un cajero
 * de verdad tiene que ENTREGAR BILLETES, y ahi aparece un problema real:
 *
 *   - solo tiene algunas denominaciones (no todas las que existen)
 *   - de cada una tiene una cantidad limitada
 *   - hay montos que NO se pueden formar aunque haya saldo
 *
 * Un cajero Itau de Paraguay solo tiene casetes de 50.000 y 100.000.
 * Por eso pedir 30.000 se rechaza: no hay billete mas chico que 50.000.
 *
 * Esta clase usa ARREGLOS (arrays), que son listas de tamano fijo.
 */
public class Dispensador {

    /*
     * ARREGLO (array): una fila de casilleros numerados, de tamano fijo.
     *
     *   int[] numeros = { 10, 20, 30 };
     *
     *   numeros[0] vale 10     <- se empieza a contar en CERO, no en uno
     *   numeros[1] vale 20
     *   numeros[2] vale 30
     *   numeros.length vale 3  <- 'length' es la cantidad de casilleros
     *
     * Aca usamos dos arreglos "en paralelo": la posicion 0 de uno se
     * corresponde con la posicion 0 del otro.
     *
     *   DENOMINACIONES[0] = 100000   y   cantidades[0] = cuantos hay de 100.000
     *   DENOMINACIONES[1] =  50000   y   cantidades[1] = cuantos hay de 50.000
     *
     * El ORDEN importa: tienen que ir de mayor a menor para que el
     * algoritmo entregue la menor cantidad de billetes posible.
     */
    private static final int[] DENOMINACIONES = { 100000, 50000 };

    /*
     * 'static' = pertenece a la CLASE, no a cada objeto. Hay una sola copia
     *            compartida. Tiene sentido: las denominaciones son las
     *            mismas para todos los dispensadores.
     * 'final'  = no se puede cambiar despues de asignarlo.
     *
     * Por costumbre, las constantes (static final) se escriben EN MAYUSCULAS.
     */

    private final int[] cantidades;

    public Dispensador(int billetesDe100k, int billetesDe50k) {
        // 'new int[2]' crea un arreglo de 2 casilleros, todos en cero.
        cantidades = new int[DENOMINACIONES.length];
        cantidades[0] = billetesDe100k;
        cantidades[1] = billetesDe50k;
    }

    /*
     * Calcula que billetes hacen falta para armar el monto, SIN entregarlos.
     *
     * Devuelve un arreglo con la cantidad de cada denominacion,
     * o null si el monto no se puede formar.
     *
     * ALGORITMO VORAZ (greedy): en cada paso agarra todo lo que puede de
     * la denominacion mas grande, y sigue con la que le sigue. Se llama
     * "voraz" porque nunca se arrepiente de lo que ya eligio.
     */
    public int[] planificar(long monto) {

        if (monto <= 0) {
            return null;
        }

        int[] plan = new int[DENOMINACIONES.length];
        long restante = monto;

        /*
         * 'for' clasico, con contador. Se lee:
         *
         *   for (arranca en 0 ; mientras i sea menor que la cantidad ; i sube de a 1)
         *
         * Se usa este y no el 'for mejorado' porque necesitamos el INDICE i
         * para tocar los dos arreglos en la misma posicion.
         */
        for (int i = 0; i < DENOMINACIONES.length; i++) {

            int denominacion = DENOMINACIONES[i];
            int disponibles = cantidades[i];

            /*
             * Division ENTERA: entre dos enteros, Java tira los decimales.
             *
             *   250000 / 100000  da  2   (no 2,5)
             *
             * Eso es justo lo que queremos: cuantos billetes enteros entran.
             */
            int necesarios = (int) (restante / denominacion);
            // El  (int)  de adelante se llama CAST: convierte de long a int.
            // Hace falta porque 'restante' es long y el resultado tambien,
            // pero la cantidad de billetes entra comoda en un int.

            // Math.min devuelve el menor de dos numeros.
            // No podemos entregar mas billetes de los que hay.
            int usar = Math.min(necesarios, disponibles);

            plan[i] = usar;
            restante = restante - ((long) usar * denominacion);
        }

        /*
         * Si sobro resto, el monto NO se puede armar con lo que hay.
         * Ejemplos reales:
         *   - pedir 30.000: el billete mas chico es 50.000
         *   - pedir 150.000 cuando ya no quedan billetes de 50.000
         */
        if (restante > 0) {
            return null;
        }

        return plan;
    }

    /*
     * Entrega los billetes de verdad: los descuenta de los casetes.
     * Devuelve el plan entregado, o null si no se pudo.
     */
    public int[] dispensar(long monto) {

        int[] plan = planificar(monto);

        if (plan == null) {
            return null;
        }

        for (int i = 0; i < plan.length; i++) {
            cantidades[i] = cantidades[i] - plan[i];
        }

        return plan;
    }

    /*
     * Arma un texto con el desglose, para mostrarlo en pantalla.
     *
     * StringBuilder sirve para ir pegando texto de a pedazos.
     * Se podria hacer con  texto = texto + "algo"  pero eso crea un String
     * nuevo cada vez. Con pocos pedazos da igual; con muchos es lento.
     */
    public String describir(int[] plan) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plan.length; i++) {
            if (plan[i] > 0) {
                sb.append(plan[i]);
                sb.append(" x ");
                sb.append(String.format("%,d", DENOMINACIONES[i]));
                sb.append("   ");
            }
        }
        return sb.toString().trim();
    }

    /*
     * Cuanto efectivo queda en total.
     */
    public long efectivoTotal() {
        long total = 0;
        for (int i = 0; i < DENOMINACIONES.length; i++) {
            total = total + ((long) cantidades[i] * DENOMINACIONES[i]);
        }
        return total;
    }

    public void mostrarCasetes() {
        System.out.println("  Billetes disponibles:");
        for (int i = 0; i < DENOMINACIONES.length; i++) {
            System.out.printf("    %,9d  ->  %3d billetes%n",
                    DENOMINACIONES[i], cantidades[i]);
        }
        System.out.printf("    Total en el cajero: Gs. %,d%n", efectivoTotal());
    }

    public static int[] getDenominaciones() {
        return DENOMINACIONES;
    }
}
