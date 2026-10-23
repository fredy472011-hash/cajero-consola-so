package BaseDatos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Clases.Billete;
import Interfaces_General.CrudSimpleInterface;

public class BilleteDto implements CrudSimpleInterface<Billete>{

    @Override
    public List<Billete> seleccionar() {
        List<Billete> billetes = new ArrayList<>();
        String consulta = "SELECT cantidad, nombre FROM billete";

        try (Connection c = BaseDeDatos.conectar();
             PreparedStatement ps = c.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) { //El ResultSet ahora también está dentro del try (...), así se cierra solo y no hacen falta ps.close() ni rs.close().

            while (rs.next()) {
                billetes.add(new Billete(
                        rs.getInt("cantidad"),
                        rs.getString("nombre")));
            }

        } catch (SQLException e) {
            System.out.println("Error al leer los billetes: " + e.getMessage());
        }
        return billetes;   // si no hay filas, vuelve vacía, nunca null
    }

    public static void main(String[] args) {
        List<Billete> billetes = new ArrayList<>();
        BilleteDto dto = new BilleteDto();
        billetes = dto.seleccionar();
        System.out.println(billetes.toString());
    }

    @Override
    public boolean insertar(Billete obj) {
      
        throw new UnsupportedOperationException("Unimplemented method 'insertar'");
    }

    @Override
    public boolean actualizar(Billete obj) {
   
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

    @Override
    public boolean desactivar(int id) {
        
        throw new UnsupportedOperationException("Unimplemented method 'desactivar'");
    }

    @Override
    public int total() {
        
        throw new UnsupportedOperationException("Unimplemented method 'total'");
    }
    
}
