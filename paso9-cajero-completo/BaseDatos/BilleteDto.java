package BaseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import Clases.Billete;
import Interfaces_General.CrudSimpleInterface;

public class BilleteDto implements CrudSimpleInterface<Billete>{
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public List<Billete> seleccionar() {
        




        throw new UnsupportedOperationException("Unimplemented method 'seleccionar'");
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
