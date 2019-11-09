package Entities;

public class Agrupacion {
    private String nombre;
    private String codigo;
    private int votos;

    public Agrupacion(String nombre, int i) {
        this.nombre = nombre;
        votos = i;

    }
    public void sumar(String votos)
    {
        int n;
        try{
            n = Integer.parseInt(votos);
        }
        catch (NumberFormatException e)
        {
            n = 0;
        }
        if(n > 0){
            this.votos += n;
        }

    }
    public String toString(){
        return "(Nombre: "+nombre+" | Votos: "+ votos+")";
    }

    public String getNombre() { return this.nombre;
    }

    public void setCodigo(String cod) {this.codigo = cod;}

    public String getCodigo() {return this.codigo;}

    public int getVotos(){return votos;}
}
