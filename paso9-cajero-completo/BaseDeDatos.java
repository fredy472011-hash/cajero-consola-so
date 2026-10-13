/*
 * PASO 8 - Hablar con MySQL desde Java
 *
 * QUE ES JDBC
 *
 * JDBC (Java Database Connectivity) es la forma estandar que tiene Java
 * para hablar con cualquier base de datos.
 *
 * Lo importante: java.sql viene DENTRO de Java, pero es solo la INTERFAZ.
 * Define "como se pide una conexion", no sabe hablar MySQL. Quien sabe
 * hablar MySQL es el DRIVER, que es el archivo .jar que hay que bajar.
 *
 *     java.sql              ->  el idioma comun (viene con Java)
 *     mysql-connector-j.jar ->  el traductor a MySQL (hay que bajarlo)
 *
 * Por eso al compilar y ejecutar hay que incluir el .jar:
 *
 *     javac -cp ".;mysql-connector-j-9.4.0.jar" *.java
 *     java  -cp ".;mysql-connector-j-9.4.0.jar" Main
 *
 * El -cp es el "classpath": donde buscar las clases.
 * El punto "." significa "tambien en esta carpeta".
 * En Windows se separa con ";" y en Linux o Mac con ":".
 *
 * Lo bueno de esto: si manana el cajero tuviera que usar PostgreSQL o
 * DB2, se cambia el .jar y la URL, y el resto del codigo queda igual.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDeDatos {

    /*
     * La URL dice a donde conectarse:
     *
     *   jdbc:mysql://  -> el protocolo (por eso hace falta el driver de MySQL)
     *   localhost      -> esta misma computadora
     *   3306           -> el puerto en el que escucha MySQL
     *   cajero_simple  -> el nombre de la base
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/cajero_simple?useSSL=false&serverTimezone=UTC";

    /*
     * El usuario y la clave NO se escriben en el codigo en un sistema real:
     * se leen de variables de entorno. Si alguien sube el proyecto a GitHub
     * con la clave escrita aca, queda publicada para siempre.
     *
     * System.getenv("DB_USER") lee una variable de entorno del sistema.
     * El metodo getOrDefault devuelve un valor por defecto si no existe,
     * para que el programa igual arranque mientras aprenden.
     */
    private static final String USUARIO =
            System.getenv().getOrDefault("DB_USER", "root");
    private static final String CLAVE =
            System.getenv().getOrDefault("DB_PASSWORD", "root");

    /*
     * Pide una conexion nueva.
     *
     * 'throws SQLException' avisa que este metodo puede fallar (la base
     * puede estar apagada, la clave puede ser incorrecta...) y que el
     * que lo llame tiene que hacerse cargo.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    /*
     * Prueba si la base responde, antes de arrancar el cajero.
     * Asi el usuario ve un mensaje claro en vez de un error raro.
     */
    public static boolean responde() {

        /*
         * try CON PARENTESIS: se llama "try-with-resources".
         *
         * Lo que se abre adentro del parentesis se CIERRA SOLO al salir,
         * incluso si hay un error en el medio.
         *
         * Sin esto habria que escribir:
         *
         *     Connection c = null;
         *     try { c = conectar(); ... }
         *     finally { if (c != null) c.close(); }
         *
         * Una conexion que no se cierra queda ocupando lugar en el servidor.
         * Si eso pasa muchas veces, MySQL deja de aceptar conexiones nuevas.
         */
        try (Connection c = conectar()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            System.out.println();
            System.out.println("  No se pudo conectar a MySQL:");
            System.out.println("    " + e.getMessage());
            System.out.println();
            System.out.println("  Revisar:");
            System.out.println("    1. Que el servicio de MySQL este corriendo");
            System.out.println("    2. Que la base exista:");
            System.out.println("       mysql -u root -p < ../sql/esquema.sql");
            System.out.println("    3. El usuario y la clave");
            return false;
        }
    }
}
