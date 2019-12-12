package Sintactico;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Funcion {

    private Token nombre;
    private Token tipoRetorno;
    private ArrayList<Parametro> parametros;
    private BloqueSentencias bloqueSentencias;

    public Funcion(Token tipoRetorno, Token nombre, ArrayList<Parametro> parametros, BloqueSentencias bloqueSentencias) {
        this.tipoRetorno = tipoRetorno;
        this.nombre = nombre;
        this.parametros = parametros;
        this.bloqueSentencias = bloqueSentencias;
    }

    public Token getNombre() {
        return nombre;
    }

    public void setNombre(Token nombre) {
        this.nombre = nombre;
    }

    public Token getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(Token tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<Parametro> parametros) {
        this.parametros = parametros;
    }

    public BloqueSentencias getBloqueSentencias() {
        return bloqueSentencias;
    }

    public void setBloqueSentencias(BloqueSentencias bloqueSentencias) {
        this.bloqueSentencias = bloqueSentencias;
    }

    @Override
    public String toString() {
        return "Funcion{" + "nombre=" + nombre + ", tipoRetorno=" + tipoRetorno + ", parametros=" + parametros + ", bloqueSentencias=" + bloqueSentencias + '}';
    }

    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Función");

        raiz.add(new DefaultMutableTreeNode("Nombre: " + nombre.getPalabra()));
        raiz.add(new DefaultMutableTreeNode("Tipo de retorno: " + tipoRetorno.getPalabra()));

        if (!parametros.isEmpty()) {
            DefaultMutableTreeNode params = new DefaultMutableTreeNode("Parámetros");
            raiz.add(params);

            for (Parametro parametro : parametros) {
                params.add(parametro.getArbolVisual());
            }
        }

        if (!bloqueSentencias.getSentencias().isEmpty()) {
            raiz.add(bloqueSentencias.getArbolVisual());
        }

        return raiz;

    }

    public String getJavaCode() {
        String retorno = "";
        switch (tipoRetorno.getPalabra()) {
            case "entero":
                retorno = "int";
                break;
            case "real":
                retorno = "double";
                break;
            case "booleano":
                retorno = "boolean";
                break;
            case "cadena":
                retorno = "String";
                break;
            case "vacio":
                retorno = "void";
                break;
            default:
                break;
        }

        String codigo = "public static " + retorno;
        if (nombre.getPalabra().equals("principal")) {
            codigo += " main (String [] args) { " + bloqueSentencias.getJavaCode();
        } else {
            codigo += " " + nombre.getPalabra() + " ( ";

            if (!parametros.isEmpty()) {
                for (Parametro parametro : parametros) {
                    codigo += parametro.getJavaCode() + ", ";
                }
                codigo = codigo.substring(0, codigo.length() - 2);
            }

            codigo += " ) { " + bloqueSentencias.getJavaCode();
        }
        codigo += "} ";
        return codigo;
    }
}
