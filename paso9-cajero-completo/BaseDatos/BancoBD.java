package BaseDatos;

import Clases.Cuenta;

/*
 * PASO 8 - El banco que lee y escribe en MySQL
 *
 * Esta clase reemplaza al Banco del paso 2.
 *
 * Fijate que tiene LOS MISMOS METODOS: buscarPorNumero(), listar()...
 * El resto del programa la usa igual que antes y ni se entera de que
 * ahora los datos vienen de una base. Esa es la ventaja de separar en
 * capas: se cambia una pieza sin tocar las demas.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BancoBD {

    /*
     * Busca una cuenta por su numero. Devuelve null si no existe.
     *
     * SOBRE LOS SIGNOS DE PREGUNTA
     *
     * El ? es un hueco que se rellena despues con setString().
     * NUNCA hay que armar el SQL pegando texto:
     *
     *     MAL:  "SELECT * FROM cuentas WHERE numero = '" + numero + "'"
     *
     * Si alguien escribe como numero de cuenta esto:
     *
     *     1001' OR '1'='1
     *
     * la consulta queda  WHERE numero = '1001' OR '1'='1'  y devuelve
     * TODAS las cuentas del banco. Eso se llama INYECCION SQL y es una
     * de las formas mas comunes de robar datos.
     *
     * Con el ? eso no puede pasar: lo que se pone ahi se trata siempre
     * como un VALOR, nunca como parte del comando.
     */
    public Cuenta buscarPorNumero(String numero) {

        String sql = "SELECT numero, titular, pin, saldo FROM cuentas WHERE numero = ?";

        // Se abren dos recursos: la conexion y la consulta preparada.
        // Los dos se cierran solos al terminar el bloque.
        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // El 1 es el numero de ?, contando desde UNO (no desde cero,
            // al reves que los arreglos).
            ps.setString(1, numero);

            /*
             * ResultSet es el resultado de la consulta: una tabla con las
             * filas que encontro. Arranca ANTES de la primera fila.
             *
             * rs.next() avanza a la fila siguiente y devuelve:
             *    true  -> habia una fila
             *    false -> no hay mas filas
             */
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;     // no encontro ninguna cuenta
                }

                // Se leen las columnas por su nombre y se arma el objeto.
                return new Cuenta(
                        rs.getString("numero"),
                        rs.getString("titular"),
                        rs.getString("pin"),
                        rs.getLong("saldo"));
            }

        } catch (SQLException e) {
            System.out.println("  Error al buscar la cuenta: " + e.getMessage());
            return null;
        }
    }

    /*
     * Trae todas las cuentas.
     */
    public List<Cuenta> listar() {

        List<Cuenta> lista = new ArrayList<Cuenta>();
        String sql = "SELECT numero, titular, pin, saldo FROM cuentas ORDER BY numero";

        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 'while' porque pueden venir muchas filas
            while (rs.next()) {
                lista.add(new Cuenta(
                        rs.getString("numero"),
                        rs.getString("titular"),
                        rs.getString("pin"),
                        rs.getLong("saldo")));
            }

        } catch (SQLException e) {
            System.out.println("  Error al listar: " + e.getMessage());
        }

        return lista;
    }

    /*
     * Graba el saldo nuevo.
     *
     * OJO - esta es la linea mas importante del paso.
     *
     * Hasta que esto no se ejecuta, el retiro vive SOLO EN MEMORIA.
     * Si se corta la luz antes, no paso nada. Despues de esto, ya esta
     * grabado y sobrevive a todo.
     *
     * executeUpdate() se usa para INSERT, UPDATE y DELETE, y devuelve
     * cuantas filas se modificaron. executeQuery() es para SELECT.
     */
    public boolean actualizarSaldo(String numero, long nuevoSaldo) {

        String sql = "UPDATE cuentas SET saldo = ? WHERE numero = ?";

        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, nuevoSaldo);
            ps.setString(2, numero);

            return ps.executeUpdate() == 1;   // tiene que haber cambiado 1 fila

        } catch (SQLException e) {
            System.out.println("  Error al actualizar el saldo: " + e.getMessage());
            return false;
        }
    }

    /*
     * Deja constancia permanente de la operacion.
     */
    public void registrarMovimiento(String numeroCuenta, String tipo,
                                    long monto, long saldoResultante) {

        String sql = "INSERT INTO movimientos "
                   + "(numero_cuenta, tipo, monto, saldo_resultante) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);
            ps.setString(2, tipo);
            ps.setLong(3, monto);
            ps.setLong(4, saldoResultante);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("  Error al registrar el movimiento: " + e.getMessage());
        }
    }

    /*
     * Los ultimos movimientos de una cuenta, para el recibo.
     */
    public List<String> ultimosMovimientos(String numeroCuenta, int cuantos) {

        List<String> lista = new ArrayList<String>();

        String sql = "SELECT tipo, monto, saldo_resultante, ocurrido_en "
                   + "FROM movimientos WHERE numero_cuenta = ? "
                   + "ORDER BY id DESC LIMIT ?";

        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, numeroCuenta);
            ps.setInt(2, cuantos);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(String.format("%s  %-8s  Gs. %,10d   ->  saldo Gs. %,d",
                            rs.getTimestamp("ocurrido_en").toLocalDateTime().toLocalDate(),
                            rs.getString("tipo"),
                            rs.getLong("monto"),
                            rs.getLong("saldo_resultante")));
                }
            }

        } catch (SQLException e) {
            System.out.println("  Error al leer los movimientos: " + e.getMessage());
        }

        return lista;
    }
}
