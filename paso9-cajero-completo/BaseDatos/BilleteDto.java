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
    //private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public List<Billete> seleccionar() {
        
        List<Billete> billetes = new ArrayList<>();
        String consulta = "SELECT cantidad, nombre from Billete";

        try(Connection c = BaseDeDatos.conectar();
            PreparedStatement ps = c.prepareStatement(consulta)){
            rs = ps.executeQuery();
                //Try(rs = ps.executeQuery()) {
                if(!rs.next()){
                    return null;
                }

                while(rs.next()){
                    billetes.add(new Billete(
                        rs.getInt(2), // cantidad
                        rs.getString(3) // nombre
                    )
                    );
                }
            //}catch(Exception e){
            ps.close();
            rs.close();
            //}
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return billetes;
    }
    public static void main(String[] args) {
        List<Billete> billetes = new ArrayList<>();
        BilleteDto dto = new BilleteDto();
        billetes = dto.seleccionar();
        System.out.println(billetes.toString());
    }

    @Override
    public boolean insertar(Billete obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertar'");
    }

    @Override
    public boolean actualizar(Billete obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

    @Override
    public boolean desactivar(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desactivar'");
    }

    @Override
    public int total() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'total'");
    }
    
}
