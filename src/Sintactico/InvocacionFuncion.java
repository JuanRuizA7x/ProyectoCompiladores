package Sintactico;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class InvocacionFuncion extends Sentencia {

    private Token nombre;
    private ArrayList<Argumento> argumentos;

    public InvocacionFuncion(Token nombre, ArrayList<Argumento> argumentos) {
        this.nombre = nombre;
        this.argumentos = argumentos;
    }

    public Token getNombre() {
        return nombre;
    }

    public void setNombre(Token nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Argumento> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(ArrayList<Argumento> argumentos) {
        this.argumentos = argumentos;
    }

    @Override
    public String toString() {
        return "InvocacionFuncion{" + "nombre=" + nombre + ", argumentos=" + argumentos + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Invocación de Función");
        raiz.add(new DefaultMutableTreeNode("Nombre función: " + nombre.getPalabra()));

        if (!argumentos.isEmpty()) {
            DefaultMutableTreeNode argums = new DefaultMutableTreeNode("Argumentos");

            for (Argumento argumento : argumentos) {
                argums.add(argumento.getArbolVisual());
            }

            raiz.add(argums);
        }

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        ArrayList<String> tipoArgumentos = obtenerTipoArgumentos(tablaSimbolos, ambito);
        Simbolo s = tablaSimbolos.buscarSimboloFuncionArgs(nombre.getPalabra(), tipoArgumentos, tablaSimbolos);
        
        if( s == null) {
            tablaSimbolos.agregarErrorSemantico("Invocación Función: La función [" + nombre.getPalabra() + "] no existe");
        }
    }

    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        ArrayList<String> tipoArgumentos = obtenerTipoArgumentos(tablaSimbolos, ambito);
        Simbolo s = tablaSimbolos.buscarSimboloFuncionArgs(nombre.getPalabra(), tipoArgumentos, tablaSimbolos);
        if (s != null) {
            return s.getTipo();
        } else {
            return null;
        }
    }

    public ArrayList<String> obtenerTipoArgumentos(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        ArrayList<String> tipoArgumentos = new ArrayList<>();

        for (Argumento argumento : argumentos) {
            String tipo = argumento.obtenerTipo(tablaSimbolos, ambito);
            tipoArgumentos.add(tipo);
        }

        return tipoArgumentos;
    }

    @Override
    public String getJavaCode() {
        String codigo = nombre.getPalabra() + "( ";
        if (!argumentos.isEmpty()) {
            for (Argumento argumento : argumentos) {
                codigo += argumento.getJavaCode() + ", ";
            }
            codigo = codigo.substring(0, codigo.length() - 2);
        }
        codigo += " ); ";
        return codigo;
    }

}
