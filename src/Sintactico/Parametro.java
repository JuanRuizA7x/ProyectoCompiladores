package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Parametro {

    private Token nombre, tipoDato;

    public Parametro(Token tipoDato, Token nombre) {
        super();
        this.nombre = nombre;
        this.tipoDato = tipoDato;
    }

    public Token getNombre() {
        return nombre;
    }

    public void setNombre(Token nombre) {
        this.nombre = nombre;
    }

    public Token getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Token tipoDato) {
        this.tipoDato = tipoDato;
    }

    @Override
    public String toString() {
        return "Parametro [tipoDato=" + tipoDato + ", nombre=" + nombre + "]";
    }

    public DefaultMutableTreeNode getArbolVisual() {
        return new DefaultMutableTreeNode(tipoDato.getPalabra() + " " + nombre.getPalabra());
    }

    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {

        Simbolo s = tablaSimbolos.buscarSimboloVariable(nombre.getPalabra(), ambito);

        if (s == null) {
            tablaSimbolos.guardarSimboloVariable(nombre.getPalabra(), tipoDato.getPalabra(), nombre.getFila(),
                    nombre.getColumna(), ambito);
        } else {
            tablaSimbolos.agregarErrorSemantico(ambito.getNombre() + ": El parámeto [" + nombre.getPalabra() + "] ya existe");
        }

    }

    String getJavaCode() {
        
        String tipo = "";
        
        switch (tipoDato.getPalabra()) {
            case "entero":
                tipo = "int";
                break;
            case "real":
                tipo = "double";
                break;
            case "booleano":
                tipo = "boolean";
                break;
            case "cadena":
                tipo = "String";
                break;
            default:
                break;
        }
        String codigo = tipo + " " + nombre.getPalabra();

        return codigo;
    }
}
