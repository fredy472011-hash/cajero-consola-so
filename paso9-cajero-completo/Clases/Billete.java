package Clases;

public class Billete {
    private int id;
    private int cantidad;
    private String nombre;

    // Para un billete nuevo: todavia no tiene id, lo pone MySQL al insertar
    public Billete(int cantidad, String nombre){
        this.cantidad = cantidad;
        this.nombre = nombre;
    }

    // Para un billete leido de la base: ya tiene id
    public Billete(int id, int cantidad, String nombre){
        this.id = id;
        this.cantidad = cantidad;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " x " + cantidad;
    }

    

}
