package Bussiness_Layer;
import Entities.*;

import java.util.*;

public class Gestor {
    Distritos distritos;
    Agrupaciones agrupaciones;

    public void obtenerCandidatos(String path){

        ArchivoTexto file = new ArchivoTexto(path);
        TSBHashtableDA<Integer, Agrupacion> htAgrupaciones;
        htAgrupaciones = file.leerAgrupaciones();
        agrupaciones = new Agrupaciones();
        agrupaciones.setAgrupaciones(htAgrupaciones);
    }

    public Collection<Distrito> obtenerDistritos(String path) {
        distritos = new Distritos();
        ArchivoTexto file = new ArchivoTexto(path);
        file.leerRegiones(distritos);
        Collection<Distrito> dist = (Collection<Distrito>) distritos.getDistritos().values();
        return dist;
    }


    public Collection<Seccion> filtrarSecciones(String codigo) {
        int cod = Integer.parseInt(codigo);
        Distrito selected = (Distrito) distritos.getDistritos().get(codigo);
        return selected.getSecciones().getSecciones().values();

    }

    public Collection<Circuito> filtrarCircuitos(String codigoSeccion, String codDist) {
        int codDistrito = Integer.parseInt(codDist);
        int codSeccion = Integer.parseInt(codigoSeccion);
        Distrito d = (Distrito) distritos.getDistritos().get(codDist);
        Seccion s = (Seccion) d.getSecciones().getSecciones().get(codigoSeccion);
        Collection<Circuito> col = s.getCircuitos().getCircuitos().values();
        return col;
    }

    public void cargarConteoVotos(String path) {
        ArchivoTexto file = new ArchivoTexto(path);
        file.contarVotos(distritos,agrupaciones);
    }

    public int contarVotosPorAgrupacion(String codigoAgrupacion){
        Agrupacion a = (Agrupacion) agrupaciones.getAgrupaciones().get(codigoAgrupacion);
        return a.getVotos();
    }

    public int contarVotosPorCircuito(String circuito, String agrupacion) {
        String codigoCircuito = circuito.split(" ")[0];
        String codigoSeccion = codigoCircuito.substring(0,5);
        String codigoDistrito = codigoCircuito.substring(0,2);
        String codigoAgrupacion = agrupacion.split(" ")[0];
        Distrito dist = (Distrito) distritos.getDistritos().get(codigoDistrito);
        Seccion s = (Seccion) dist.getSecciones().getSecciones().get(codigoSeccion);
        Circuito c = (Circuito) s.getCircuitos().getCircuitos().get(codigoCircuito);
        TSBHashtableDA ag =  c.getAgrupaciones();
        Agrupacion a = (Agrupacion) ag.get(codigoAgrupacion);
        return a.getVotos();
    }

    public int contarVotosPorSeccion(String seccion,String agrupacion) {
        String codigoSeccion = seccion.split(" ")[0];
        String codigoDistrito = codigoSeccion.substring(0,2);
        String codigoAgrupacion = agrupacion.split(" " )[0];
        Distrito dist = (Distrito) distritos.getDistritos().get(codigoDistrito);
        Seccion s = (Seccion) dist.getSecciones().getSecciones().get(codigoSeccion);
        Agrupacion ag = (Agrupacion) s.getAgrupaciones().get(codigoAgrupacion);
        return ag.getVotos();
    }

    public int contarVotosPorDistrito(String distrito,String agrupacion) {
        String codigoDistrito = distrito.split(" ")[0];
        String codigoAgrupacion = agrupacion.split(" ")[0];
        Distrito dist = (Distrito) distritos.getDistritos().get(codigoDistrito);
        Agrupacion ag = (Agrupacion) dist.getAgrupaciones().get(codigoAgrupacion);
        return ag.getVotos();
    }

    public List<String> getCandidatos(){
        ArrayList<String> candidatos = new ArrayList<>();
        Set<Map.Entry<Integer,Agrupacion>> entradas = agrupaciones.getAgrupaciones().entrySet();
        Iterator it = entradas.iterator();
        while (it.hasNext()) {
            Map.Entry<Integer,Agrupacion> entry = (Map.Entry<Integer, Agrupacion>) it.next();
            String candidato = entry.getValue().getCodigo() + " " + entry.getValue().getNombre();
            candidatos.add(candidato);
        }
        return candidatos;
    }

    public Distritos getDistritos() {

        return distritos;
    }


    public List<Agrupacion> contarTodos() {
        List<Agrupacion> list = new ArrayList<>();
        Collection<Agrupacion> col = agrupaciones.getAgrupaciones().values();
        Iterator<Agrupacion> it = col.iterator();
        while (it.hasNext()) {
            Agrupacion a = it.next();
            list.add(a);
        }
        return list;
    }
}
