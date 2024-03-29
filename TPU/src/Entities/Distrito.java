package Entities;

public class Distrito {
    private String codigo, nombre;
    private Secciones secciones;
    private TSBHashtableDA agrupaciones;
    public Distrito(String codigo, String nombre){
        this.codigo = codigo;
        this.nombre = nombre;
        this.secciones = new Secciones();
        this.agrupaciones = new TSBHashtableDA();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Secciones getSecciones() {
        return secciones;
    }

    public TSBHashtableDA getAgrupaciones() {
        return agrupaciones;
    }
}
