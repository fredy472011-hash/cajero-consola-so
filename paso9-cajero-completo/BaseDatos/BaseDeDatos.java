package BaseDatos;

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
            "jdbc:mysql://localhost:3306/cajero_simple"
            + "?useSSL=false"                    // conexion sin cifrar: es local
            + "&allowPublicKeyRetrieval=true"    // ver la nota de abajo
            + "&serverTimezone=UTC";             // zona horaria, para las fechas

    /*
     * SOBRE allowPublicKeyRetrieval
     *
     * MySQL 8 guarda las contrasenas con un metodo llamado
     * caching_sha2_password. Para mandar la contrasena, el driver primero
     * necesita LA CLAVE PUBLICA del servidor, y por seguridad no la pide
     * sola. Sin este parametro el error es:
     *
     *     Public Key Retrieval is not allowed
     *
     * En instalaciones viejas de MySQL no pasa, porque usan el metodo
     * anterior (mysql_native_password). Por eso a una persona le funciona
     * y a otra no, con el mismo codigo.
     *
     * OJO - esto NO se pone en un sistema real. Pedirle la clave publica
     * al servidor por una conexion sin cifrar permite que alguien en el
     * medio de la red intercepte la contrasena. Acá es aceptable porque
     * la base esta en la misma computadora (localhost): no hay red de
     * por medio. En produccion se usa SSL en vez de esto.
     */

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

            String mensaje = e.getMessage();

            System.out.println();
            System.out.println("  No se pudo conectar a MySQL:");
            System.out.println("    " + mensaje);
            System.out.println();

            /*
             * Cada error tiene una causa distinta. En vez de mostrar una lista
             * larga donde hay que adivinar cual es, se detecta el caso y se
             * dice la solucion exacta.
             */
            if (mensaje != null && mensaje.contains("No suitable driver")) {

                System.out.println("  FALTA EL DRIVER EN EL CLASSPATH.");
                System.out.println();
                System.out.println("  Java no encontro quien sepa hablar con MySQL.");
                System.out.println("  El archivo .jar esta en esta carpeta, pero hay que");
                System.out.println("  decirle a Java que lo use.");
                System.out.println();
                System.out.println("  La forma facil:");
                System.out.println("      .\\correr");
                System.out.println();
                System.out.println("  O escribiendo los comandos completos:");
                System.out.println("      javac -cp \".;mysql-connector-j-9.4.0.jar\" *.java");
                System.out.println("      java  -cp \".;mysql-connector-j-9.4.0.jar\" Main");

            } else if (mensaje != null && mensaje.contains("Unknown database")) {

                System.out.println("  LA BASE NO EXISTE TODAVIA.");
                System.out.println();
                System.out.println("  Hay que crearla una sola vez:");
                System.out.println("      mysql -u root -p < ../sql/esquema.sql");

            } else if (mensaje != null && mensaje.contains("Access denied")) {

                System.out.println("  USUARIO O CONTRASENA INCORRECTOS.");
                System.out.println();
                System.out.println("  Por defecto usa root/root. Si tu clave es otra,");
                System.out.println("  antes de ejecutar, en PowerShell:");
                System.out.println("      $env:DB_PASSWORD = \"tu_clave\"");

            } else if (mensaje != null && mensaje.contains("Public Key Retrieval")) {

                System.out.println("  FALTA allowPublicKeyRetrieval EN LA URL.");
                System.out.println();
                System.out.println("  Tu MySQL guarda las contrasenas con el metodo");
                System.out.println("  caching_sha2_password (el de MySQL 8). El driver");
                System.out.println("  necesita pedirle la clave publica al servidor.");
                System.out.println();
                System.out.println("  Ya esta arreglado en el codigo. Actualiza el proyecto:");
                System.out.println("      git pull");

            } else if (mensaje != null && (mensaje.contains("Communications link")
                                        || mensaje.contains("Connection refused"))) {

                System.out.println("  MYSQL NO ESTA CORRIENDO.");
                System.out.println();
                System.out.println("  Arrancar el servicio:");
                System.out.println("      services.msc  ->  buscar MySQL80  ->  Iniciar");

            } else {

                System.out.println("  Revisar:");
                System.out.println("    1. Que el servicio de MySQL este corriendo");
                System.out.println("    2. Que la base exista:");
                System.out.println("       mysql -u root -p < ../sql/esquema.sql");
                System.out.println("    3. El usuario y la clave");
            }

            return false;
        }
    }
}
