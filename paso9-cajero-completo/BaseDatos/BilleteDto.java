package BaseDatos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import Clases.Billete;
import Interfaces_General.CrudSimpleInterface;

public class BilleteDto implements CrudSimpleInterface<Billete>{

    // Trae solo los billetes activos (estaActivo = 1).
    // Statement y no PreparedStatement: el SQL es fijo, no lleva ningun ? ni dato de afuera.
    @Override
    public List<Billete> seleccionar() {
        List<Billete> billetes = new ArrayList<>();
        String consulta = "SELECT id, cantidad, nombre FROM billete WHERE estaActivo = 1";

        try (Connection c = BaseDeDatos.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(consulta)) { //El ResultSet ahora también está dentro del try (...), así se cierra solo y no hacen falta st.close() ni rs.close().

            while (rs.next()) {
                billetes.add(new Billete(
                        rs.getInt("id"),
                        rs.getInt("cantidad"),
                        rs.getString("nombre")));
            }

        } catch (SQLException e) {
            System.out.println("Error al leer los billetes: " + e.getMessage());
        }
        return billetes;   // si no hay filas, vuelve vacía, nunca null
    }

    public static void main(String[] args) {
        BilleteDto dto = new BilleteDto();
        System.out.println(dto.seleccionar());
        System.out.println("Billetes activos: " + dto.total());
    }

    // El id no se manda: lo pone MySQL solo (AUTO_INCREMENT).
    // Y estaActivo tampoco: por defecto vale 1 (DEFAULT 1 en la tabla).
    @Override
    public boolean insertar(Billete obj) {
        String consulta = "INSERT INTO billete (cantidad, nombre) VALUES (?, ?)";

        try (Connection c = BaseDeDatos.conectar();
             // Permite ejecutar sentencias SQL con parametros de entrada (los ?)
             PreparedStatement ps = c.prepareStatement(consulta)) {

            ps.setInt(1, obj.getCantidad());
            ps.setString(2, obj.getNombre());
            // executeUpdate devuelve cuantas filas cambio: si es mas de 0, salio bien
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar el billete: " + e.getMessage());
            return false;
        }
    }

    // Cambia cantidad y nombre del billete que tiene ese id
    @Override
    public boolean actualizar(Billete obj) {
        String consulta = "UPDATE billete SET cantidad = ?, nombre = ? WHERE id = ?";

        try (Connection c = BaseDeDatos.conectar();
             // Permite ejecutar sentencias SQL con parametros de entrada (los ?)
             PreparedStatement ps = c.prepareStatement(consulta)) {

            ps.setInt(1, obj.getCantidad());
            ps.setString(2, obj.getNombre());
            ps.setInt(3, obj.getId());
            // Si no existe ningun billete con ese id, cambia 0 filas y devuelve false
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el billete: " + e.getMessage());
            return false;
        }
    }

    // No borra la fila: la marca como inactiva (estaActivo = 0).
    // Asi no se pierde el dato, y seleccionar() y total() ya no la cuentan.
    @Override
    public boolean desactivar(int id) {
        String consulta = "UPDATE billete SET estaActivo = 0 WHERE id = ?";

        try (Connection c = BaseDeDatos.conectar();
             // Permite ejecutar sentencias SQL con parametros de entrada (los ?)
             PreparedStatement ps = c.prepareStatement(consulta)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al desactivar el billete: " + e.getMessage());
            return false;
        }
    }

    // Cuantos billetes activos hay. COUNT(*) devuelve una sola fila con un numero.
    // Tambien Statement: SQL fijo, sin ?
    @Override
    public int total() {
        String consulta = "SELECT COUNT(*) FROM billete WHERE estaActivo = 1";

        try (Connection c = BaseDeDatos.conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(consulta)) {

            if (rs.next()) {
                return rs.getInt(1);   // la columna 1 es el resultado del COUNT
            }

        } catch (SQLException e) {
            System.out.println("Error al contar los billetes: " + e.getMessage());
        }
        return 0;
    }

}
