package Semantico;

import java.util.ArrayList;

import Sintactico.Parametro;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class TablaSimbolos {

    private ArrayList<Simbolo> listaSimbolos;
    private ArrayList<String> listaErrores;

    public TablaSimbolos() {
        this.listaSimbolos = new ArrayList<>();
        this.listaErrores = new ArrayList<>();
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public void setListaSimbolos(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
    }

    public ArrayList<String> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     *
     * @param nombre
     * @param tipo
     * @param fila
     * @param columna
     * @param ambito
     */
    public void guardarSimboloVariable(String nombre, String tipo, int fila, int columna, Simbolo ambito) {

        Simbolo s = buscarSimboloVariable(nombre, ambito);

        if (s == null) {
            Simbolo nuevo = new Simbolo(nombre, tipo, fila, columna, ambito);
            listaSimbolos.add(nuevo);
        }

    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     *
     * @param nombre
     * @param tipo
     * @param tipoParametros
     * @param tablaSimbolos
     */
    public void guardarSimboloFuncion(String nombre, String tipo, ArrayList<Parametro> tipoParametros, TablaSimbolos tablaSimbolos) {

        Simbolo s = buscarSimboloFuncion(nombre, tipoParametros, tablaSimbolos);

        if (s == null) {
            Simbolo nuevo = new Simbolo(nombre, tipo, tipoParametros);
            ArrayList<Simbolo> funciones = tablaSimbolos.getListaSimbolos();
            funciones.add(nuevo);
            tablaSimbolos.setListaSimbolos(funciones);
        } else {
            tablaSimbolos.agregarErrorSemantico("Función: La funcion [" + nombre + "] ya existe");
        }

    }

    /**
     * @param error 
     */
    public void agregarErrorSemantico(String error) {
        listaErrores.add(error);
    }

    /**
     * @param nombre
     * @param ambito
     * @return 
     */
    public Simbolo buscarSimboloVariable(String nombre, Simbolo ambito) {

        for (Simbolo simbolo : listaSimbolos) {
            if (simbolo.getAmbito() != null) {
                if (nombre.equals(simbolo.getNombre()) && ambito.equals(simbolo.getAmbito())) {
                    return simbolo;
                }
            }
        }

        return null;
    }

    /**
     * @param nombre
     * @param tiposParametros
     * @param tablaSimbolos
     * @return 
     */
    public Simbolo buscarSimboloFuncion(String nombre, ArrayList<Parametro> tiposParametros, TablaSimbolos tablaSimbolos) {

        ArrayList<Simbolo> s = tablaSimbolos.getListaSimbolos();
        for (Simbolo simbolo : s) {
            if (simbolo.getAmbito() == null) {
                if (nombre.equals(simbolo.getNombre())) {
                    if (simbolo.getTipoParametros().size() == tiposParametros.size()) {
                        boolean parm = true;
                        for (int i = 0; i < tiposParametros.size(); i++) {
                            if (!simbolo.getTipoParametros().get(i).getTipoDato().getPalabra().equals(tiposParametros.get(i).getTipoDato().getPalabra())) {
                                parm = false;
                            }
                            if (!parm) {
                                i = tiposParametros.size();
                            }
                        }
                        if (parm) {
                            return simbolo;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param nombre
     * @param tiposArgumentos
     * @param tablaSimbolos
     * @return 
     */
    public Simbolo buscarSimboloFuncionArgs(String nombre, ArrayList<String> tiposArgumentos, TablaSimbolos tablaSimbolos) {

        ArrayList<Simbolo> s = tablaSimbolos.getListaSimbolos();
        for (Simbolo simbolo : s) {
            if (simbolo.getAmbito() == null) {
                if (nombre.equals(simbolo.getNombre())) {
                    if (simbolo.getTipoParametros().size() == tiposArgumentos.size()) {
                        boolean parm = true;
                        for (int i = 0; i < tiposArgumentos.size(); i++) {
                            if (!simbolo.getTipoParametros().get(i).getTipoDato().getPalabra().equals(tiposArgumentos.get(i))) {
                                parm = false;
                            }
                            if (!parm) {
                                i = tiposArgumentos.size();
                            }
                        }
                        if (parm) {
                            return simbolo;
                        }
                    }
                }
            }
        }
        return null;
    }

}
