package Entities;

public class Circuito {
    private String nombre, codigo;
    private TSBHashtableDA agrupaciones;
    private Mesas mesas;
    public Circuito(String codigo, String nombre){
        this.nombre = nombre;
        this.codigo = codigo;
        mesas = new Mesas();
        this.agrupaciones = new TSBHashtableDA();
    }

    public TSBHashtableDA getAgrupaciones() {
        return agrupaciones;
    }

    public Mesas getMesas() {
        return mesas;
    }

    public String getCodigo() { return this.codigo;
    }

    public String getNombre() { return this.nombre;
    }
}
