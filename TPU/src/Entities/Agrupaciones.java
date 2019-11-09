package Entities;

public class Agrupaciones implements Cloneable
{
    TSBHashtableDA agrupaciones;
    public Agrupaciones(String ruta)
    {
        ArchivoTexto res = new ArchivoTexto(ruta);
        agrupaciones = res.leerAgrupaciones();
    }

    public TSBHashtableDA getAgrupaciones() {
        return agrupaciones;
    }
    public Agrupaciones clone() throws CloneNotSupportedException
    {
        return (Agrupaciones) super.clone();
    }

    public void setAgrupaciones(TSBHashtableDA agrupaciones) {
        this.agrupaciones = agrupaciones;
    }

    public Agrupaciones() {}
}
