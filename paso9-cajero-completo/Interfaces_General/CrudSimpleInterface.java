package Interfaces_General;

import java.util.List;

/**
 *
 * @author USER
 */
public interface CrudSimpleInterface<T> {
    // son contratos que las clases deben implementar con el tipo de dato generico
    public List<T> seleccionar();
    public boolean insertar(T obj);
    public boolean actualizar(T obj);
    public boolean desactivar(int id);
    public int total();
}
