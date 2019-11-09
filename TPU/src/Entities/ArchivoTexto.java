package Entities;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ArchivoTexto {
    //Atributos
    private String ruta;
    private static String codigoPresidente = "000100000000000";

    //Constructores
    public ArchivoTexto(String ruta) {
        this.ruta = ruta;
    }

    //Metodos
    public TSBHashtableDA leerAgrupaciones() {
        String linea, nombre, codigo, categoria;
        String campos[];
        TSBHashtableDA tabla = new TSBHashtableDA();
        try {
            Scanner sc = new Scanner(new File(ruta));
            while (sc.hasNextLine()) {
                linea = sc.nextLine();
                campos = linea.split("\\|");
                categoria = campos[0];
                if (categoria.equals(codigoPresidente)) {
                    codigo = campos[2];
                    nombre = campos[3];
                    Agrupacion a = new Agrupacion(nombre, 0);
                    tabla.put(codigo, a);
                    a.setCodigo(codigo);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo ingresado es inexistente o no se encontró.");
        } catch (Exception e) {
            System.out.println(e);
        }
        return tabla;
    }

    //¿Posible implementacion más eficiente reutilizando el primer case?
    public void leerRegiones(Distritos distritos)
    {
        String linea, nombre, codigo, codigoDistrito, codigoSeccion;
        String campos[];
        try
        {
            Scanner sc = new Scanner(new File(ruta));
            while (sc.hasNextLine())
            {
                linea = sc.nextLine();
                campos = linea.split("\\|");
                codigo = campos[0];
                nombre = campos[1];
                switch(codigo.length())
                {
                    case 2:
                        Distrito distrito = (Distrito) distritos.getDistritos().get(codigo);
                        if(distrito == null)
                        {

                            distrito = new Distrito(codigo, nombre);
                            distritos.getDistritos().put(codigo, distrito);
                        }
                        else
                        {
                            distrito.setNombre(nombre);
                        }
                        break;

                    case 5:
                        codigoDistrito = codigo.substring(0,2);
                        distrito = (Distrito) distritos.getDistritos().get(codigoDistrito);
                        if(distrito == null)
                        {
                            distrito = new Distrito(codigoDistrito, "SIN NOMBRE AÚN");
                            distritos.getDistritos().put(codigoDistrito, distrito);
                        }

                        Secciones secciones = distrito.getSecciones();
                        Seccion seccion = (Seccion) secciones.getSecciones().get(codigo);
                        if(seccion == null)
                        {
                            seccion = new Seccion(codigo, nombre);
                            secciones.getSecciones().put(codigo, seccion);
                        }
                        else
                        {
                            seccion.setNombre(nombre);
                        }
                        break;

                    case 11:
                        codigoDistrito = codigo.substring(0,2);
                        distrito = (Distrito) distritos.getDistritos().get(codigoDistrito);
                        if(distrito == null)
                        {
                            distrito = new Distrito(codigoDistrito, "SIN NOMBRE AÚN");
                            distritos.getDistritos().put(codigoDistrito, distrito);
                        }
                        secciones = distrito.getSecciones();
                        codigoSeccion = codigo.substring(0, 5);
                        seccion = (Seccion) secciones.getSecciones().get(codigoSeccion);
                        if(seccion == null)
                        {
                            seccion = new Seccion(codigoSeccion,"SIN NOMBRE AÚN");
                            secciones.getSecciones().put(codigoSeccion, seccion);
                        }

                        Circuitos circuitos = seccion.getCircuitos();
                        Circuito circuito = (Circuito) circuitos.getCircuitos().get(codigo);
                        if(circuito == null)
                        {
                            circuito = new Circuito(codigo, nombre);
                            circuitos.getCircuitos().put(codigo, circuito);
                        }
                        break;
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("El archivo ingresado es inexistente o no se encontró.");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }


    public void contarVotos(Distritos distritos, Agrupaciones agrupaciones){
        String linea, codigoDistrito, codigoSeccion, codigoCircuito, codigoMesa, codigoCategoria, codigoAgrupacion, votos;
        String campos[];
        try {
            Scanner sc = new Scanner(new File(ruta));
            while (sc.hasNextLine()) {
                linea = sc.nextLine();
                campos = linea.split("\\|");
                codigoDistrito = campos[0];
                codigoSeccion = campos[1];
                codigoCircuito = campos[2];
                codigoMesa = campos[3];
                codigoCategoria = campos[4];
                codigoAgrupacion = campos[5];
                votos = campos[6];

                if(codigoCategoria.equals(codigoPresidente))
                {

                    //Buscar agrupacion e incrementar el total de sus votos.
                    Agrupacion agrupacionActual = (Agrupacion) agrupaciones.getAgrupaciones().get(codigoAgrupacion);
                    agrupacionActual.sumar(votos);

                    //Buscar Distrito segun su codigo y crear si no existe.
                    Distrito distrito = (Distrito)distritos.getDistritos().get(codigoDistrito);
                    if(distrito == null)
                    {
                        distrito = new Distrito(codigoDistrito, "SIN NOMBRE AÚN");
                        distritos.getDistritos().put(codigoDistrito, distrito);
                    }

                    //Buscar Agrupacion en ese Distrito, crearla si no existe, y si existe sumarle los votos.
                    TSBHashtableDA agrupacionesLinea = distrito.getAgrupaciones();
                    Agrupacion agrupacion = (Agrupacion) agrupacionesLinea.get(codigoAgrupacion);
                    if(agrupacion == null)
                    {
                        agrupacion = new Agrupacion(agrupacionActual.getNombre(),Integer.parseInt(votos));
                        agrupacionesLinea.put(codigoAgrupacion,agrupacion);
                    }
                    else
                    {
                        agrupacion.sumar(votos);
                    }

                    //Buscar Secciones del Distrito.
                    Secciones secciones = distrito.getSecciones();

                    //Buscar Seccion segun su codigo.
                    Seccion seccion = (Seccion) secciones.getSecciones().get(codigoSeccion);

                    //Buscar Agrupacion en esa Seccion, crearla si no existe, y si existe sumarle los votos.
                    agrupacionesLinea = seccion.getAgrupaciones();
                    agrupacion = (Agrupacion) agrupacionesLinea.get(codigoAgrupacion);
                    if(agrupacion == null)
                    {
                        agrupacion = new Agrupacion(agrupacionActual.getNombre(),Integer.parseInt(votos));
                        agrupacionesLinea.put(codigoAgrupacion,agrupacion);
                    }
                    else
                    {
                        agrupacion.sumar(votos);
                    }

                    //Buscar Circuitos de la Seccion.
                    Circuitos circuitos =  seccion.getCircuitos();

                    //Buscar Circuito segun su codigo.
                    Circuito circuito = (Circuito) circuitos.getCircuitos().get(codigoCircuito);

                    //Buscar Agrupacion en ese Circuito, crearla si no existe, y si existe sumarle los votos.
                    agrupacionesLinea = circuito.getAgrupaciones();
                    agrupacion = (Agrupacion) agrupacionesLinea.get(codigoAgrupacion);
                    if(agrupacion == null)
                    {
                        agrupacion = new Agrupacion(agrupacionActual.getNombre(),Integer.parseInt(votos));
                        agrupacionesLinea.put(codigoAgrupacion,agrupacion);
                    }
                    else
                    {
                        agrupacion.sumar(votos);
                    }

                    //Buscar Mesas en el Circuito.
                    Mesas mesas = circuito.getMesas();

                    //Buscar mesa segun su codigo y crearla si no existe.
                    Mesa mesa = (Mesa) mesas.getMesas().get(codigoMesa);
                    if(mesa == null)
                    {
                        mesa = new Mesa(codigoMesa);
                        mesas.getMesas().put(codigoMesa, mesa);
                    }

                    //Buscar Agrupacion en esa Mesa, crearla si no existe, y si existe sumarle los votos.
                    agrupacionesLinea = mesa.getAgrupaciones();
                    agrupacion = (Agrupacion) agrupacionesLinea.get(codigoAgrupacion);
                    if(agrupacion == null)
                    {
                        agrupacion = new Agrupacion(agrupacionActual.getNombre(),Integer.parseInt(votos));
                        agrupacionesLinea.put(codigoAgrupacion,agrupacion);
                    }
                    else
                    {
                        agrupacion.sumar(votos);
                    }


                }

            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("El archivo ingresado es inexistente o no se encontró.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

}


