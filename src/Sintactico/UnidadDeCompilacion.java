package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class UnidadDeCompilacion {

    private ArrayList<Funcion> listaFunciones;
    private TablaSimbolos tablaSimbolos;

    public UnidadDeCompilacion(ArrayList<Funcion> listaFunciones) {
        super();
        this.listaFunciones = listaFunciones;
        tablaSimbolos = new TablaSimbolos();
    }

    public ArrayList<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(ArrayList<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public void setTablaSimbolos(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    @Override
    public String toString() {
        return "UnidadDeCompilacion [listaFunciones=" + listaFunciones + "]";
    }

    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Unidad de compilación");

        if (!listaFunciones.isEmpty()) {

            for (Funcion funcion : listaFunciones) {
                raiz.add(funcion.getArbolVisual());
            }

        }

        return raiz;
    }

    public void analizarSemantica() {
        Simbolo principal = null;
        boolean funcionMain = false;
        for (Funcion fun : listaFunciones) {
            Simbolo s = new Simbolo(fun.getNombre().getPalabra(), fun.getTipoRetorno().getPalabra(),
                    fun.getParametros());
            tablaSimbolos.guardarSimboloFuncion(s.getNombre(), s.getTipo(), s.getTipoParametros(), tablaSimbolos);
            if (fun.getNombre().getPalabra().equals("principal")) {
                funcionMain = true;
                principal = s;
            }
        }

        if (funcionMain) {
            if (!principal.getTipo().equals("vacio")) {
                tablaSimbolos.agregarErrorSemantico("Función: La función [principal] debe tener tipo de retorno vacio");
            }
            if (!principal.getTipoParametros().isEmpty()) {
                tablaSimbolos.agregarErrorSemantico("Función: La función [principal] no debe tener parámetros");
            }
        } else {
            tablaSimbolos.agregarErrorSemantico("Función: No existe una función con el nombre [principal]");
        }

        for (Funcion fun : listaFunciones) {
            Simbolo s = new Simbolo(fun.getNombre().getPalabra(), fun.getTipoRetorno().getPalabra(),
                    fun.getParametros());

            if (fun.getParametros() != null) {
                for (Parametro parm : fun.getParametros()) {
                    parm.analizarSemantica(tablaSimbolos, s);
                }
            }

            if (fun.getBloqueSentencias().getSentencias() != null) {
                for (Sentencia sen : fun.getBloqueSentencias().getSentencias()) {
                    if (fun.getBloqueSentencias().getSentencias().get(fun.getBloqueSentencias().getSentencias().size() - 1) == sen) {
                        if (!(sen instanceof Retorno)) {
                            if (!(fun.getTipoRetorno().getPalabra().equals("vacio"))) {
                                tablaSimbolos.agregarErrorSemantico("Función: No se encuentra el retorno en la función [" + s.getNombre() + "]");
                            }
                        } else {
                            Retorno ret = (Retorno) sen;
                            if (!ret.obtenerTipo(tablaSimbolos, s).equals("nulo")) {
                                if (!(fun.getTipoRetorno().getPalabra().equals(ret.obtenerTipo(tablaSimbolos, s)))) {
                                    tablaSimbolos.agregarErrorSemantico("Función: El retorno de la función [" + s.getNombre() + "] no es compatible con su tipo de retorno");
                                }
                            } else {
                                if (fun.getTipoRetorno().getPalabra().equals("vacio")) {
                                    tablaSimbolos.agregarErrorSemantico("Función: El retorno de la función [" + s.getNombre() + "] no es compatible con su tipo de retorno");
                                }
                            }
                        }
                    }
                    sen.analizarSemantica(tablaSimbolos, s);
                }
            }
        }

    }

    public String getJavaCode() {
        String codigo = "package CodigoFuente; import javax.swing.JOptionPane; public class Principal { ";
        if (listaFunciones != null) {
            for (Funcion funcion : listaFunciones) {

                codigo += funcion.getJavaCode();
            }
        }

        codigo += " }";
        return codigo;
    }
    
}
