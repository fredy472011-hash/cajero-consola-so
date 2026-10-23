package Clases;

public class Billete {
    private int cantidad;
    private String nombre;

    public Billete(int cantidad, String nombre){
        this.cantidad = cantidad;
        this.nombre = nombre;
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
        // TODO Auto-generated method stub
        return super.toString();
    }

    

}
