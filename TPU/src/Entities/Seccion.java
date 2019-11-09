package Entities;

public class Seccion {
    private String codigo, nombre;
    private Circuitos circuitos;
    private TSBHashtableDA agrupaciones;
    public Seccion(String codigo, String nombre){
        this.codigo = codigo;
        this.nombre = nombre;
        this.circuitos = new Circuitos();
        this.agrupaciones = new TSBHashtableDA();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Circuitos getCircuitos() {
        return circuitos;
    }

    public TSBHashtableDA getAgrupaciones() {
        return agrupaciones;
    }
}
