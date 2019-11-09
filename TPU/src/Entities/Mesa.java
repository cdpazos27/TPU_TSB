package Entities;

public class Mesa {
    private String codigo;
    private TSBHashtableDA agrupaciones;
    public Mesa(String codigo)
    {
        this.codigo = codigo;
        agrupaciones = new TSBHashtableDA();
    }

    public TSBHashtableDA getAgrupaciones()
    {
        return agrupaciones;
    }
}
