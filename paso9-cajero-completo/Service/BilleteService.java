package Service;

import java.util.List;

import BaseDatos.BilleteDto;
import Clases.Billete;

/*
 * LOGICA DE NEGOCIO de los billetes.
 *
 * Es lo mismo que los *Control del paquete negocio en el proyecto de NetBeans
 * (ClientesControl, CiudadControl...), pero para un solo tema: Billete.
 *
 * Cada capa tiene UN trabajo:
 *
 *     Vista / Principal   ->  pide algo: "carga un billete de 20.000"
 *     Service  (ESTA)     ->  DECIDE si se puede: revisa las reglas del negocio
 *     BaseDatos (Dto)     ->  solo habla con MySQL; no decide nada
 *
 * El Dto no conoce reglas: si le pasas una cantidad negativa, la guarda igual.
 * Por eso las reglas viven aca, en un solo lugar. Si manana la regla cambia
 * ("no mas de 2.000 billetes por casete"), se cambia solo en esta clase.
 *
 * Igual que en NetBeans, los metodos devuelven un MENSAJE (String) en vez de
 * true/false: la vista solo tiene que mostrarlo.
 */
public class BilleteService {

    // El Service USA al Dto (como ClientesControl usaba a ClientesDAO)
    private final BilleteDto billeteDto;

    public BilleteService() {
        this.billeteDto = new BilleteDto();
    }

    /*
     * Arma la tabla para mostrar en la consola.
     * En NetBeans este metodo devolvia un DefaultTableModel para la JTable;
     * en consola no hay JTable, asi que devolvemos el texto ya armado.
     */
    public String listar() {
        List<Billete> billetes = billeteDto.seleccionar();

        if (billetes.isEmpty()) {
            return "No hay billetes cargados.";
        }

        // Se arma el texto sumando lineas. "\n" = salto de linea (bajar al renglon de abajo)
        String tabla = "ID | Nombre | Cantidad\n";

        for (Billete b : billetes) {
            // una linea por billete, por ejemplo:  1 | Cien mil | 40
            tabla = tabla + b.getId() + " | " + b.getNombre() + " | " + b.getCantidad() + "\n";
        }

        tabla = tabla + "Billetes activos: " + billeteDto.total();
        return tabla;
    }

    public String insertar(int cantidad, String nombre) {
        // Regla 1: el nombre es obligatorio
        if (nombre == null || nombre.isBlank()) {
            return "El nombre del billete es obligatorio.";
        }
        // Regla 2: no se cargan cantidades negativas
        if (cantidad < 0) {
            return "La cantidad no puede ser negativa.";
        }
        // Regla 3: no repetir un billete que ya existe
        if (buscarPorNombre(nombre) != null) {
            return "Ya existe un billete '" + nombre.trim() + "'. Usa actualizar.";
        }

        // Paso todas las reglas: recien ahora se le pide al Dto que guarde
        boolean respuestaSQL = billeteDto.insertar(new Billete(cantidad, nombre.trim()));
        if (respuestaSQL) {
            return "El billete fue insertado satisfactoriamente!";
        } else {
            return "El billete no pudo ser insertado.";
        }
    }

    public String actualizar(int id, int cantidad, String nombre) {
        // Regla 1: solo se actualiza un billete que existe y esta activo
        if (buscarActivo(id) == null) {
            return "No existe un billete activo con id " + id + ".";
        }
        if (nombre == null || nombre.isBlank()) {
            return "El nombre del billete es obligatorio.";
        }
        if (cantidad < 0) {
            return "La cantidad no puede ser negativa.";
        }
        // Regla 2: el nombre nuevo no puede ser el de OTRO billete
        Billete otro = buscarPorNombre(nombre);
        if (otro != null && otro.getId() != id) {
            return "Ya existe otro billete '" + nombre.trim() + "'.";
        }

        boolean respuestaSQL = billeteDto.actualizar(new Billete(id, cantidad, nombre.trim()));
        if (respuestaSQL) {
            return "El billete fue actualizado satisfactoriamente!";
        } else {
            return "El billete no pudo ser actualizado.";
        }
    }

    public String desactivar(int id) {
        // Regla: no tiene sentido desactivar uno que no existe o que ya esta desactivado
        if (buscarActivo(id) == null) {
            return "No existe un billete activo con id " + id + ".";
        }

        boolean respuestaSQL = billeteDto.desactivar(id);
        if (respuestaSQL) {
            return "El billete fue desactivado.";
        } else {
            return "El billete no pudo ser desactivado.";
        }
    }

    public int total() {
        return billeteDto.total();
    }

    // ------------------------------------------------------------------
    // Ayudantes privados: solo los usa esta clase para revisar las reglas
    // ------------------------------------------------------------------

    // Busca entre los activos el que tenga ese id. Si no lo encuentra, null.
    private Billete buscarActivo(int id) {
        for (Billete b : billeteDto.seleccionar()) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    // Busca por nombre sin importar mayusculas ni espacios de mas.
    private Billete buscarPorNombre(String nombre) {
        for (Billete b : billeteDto.seleccionar()) {
            if (b.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                return b;
            }
        }
        return null;
    }

    /*
     * Prueba rapida, como el main de BilleteDto.
     * Solo prueba reglas que RECHAZAN, asi no cambia nada en la base.
     */
    public static void main(String[] args) {
        BilleteService servicio = new BilleteService();

        System.out.println(servicio.listar());
        System.out.println();
        System.out.println("Insertar cantidad -5   : " + servicio.insertar(-5, "Mil"));
        System.out.println("Insertar sin nombre    : " + servicio.insertar(10, "  "));
        System.out.println("Insertar 'cien mil'    : " + servicio.insertar(10, "cien mil"));
        System.out.println("Desactivar id 99       : " + servicio.desactivar(99));
    }
}
