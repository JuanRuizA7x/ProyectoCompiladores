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
public class Arreglo extends Sentencia {

    private Token tipoDato;
    private Token identificador;
    private ArrayList<Argumento> argumentos;

    public Arreglo(Token tipoDato, Token identificador) {
        this.tipoDato = tipoDato;
        this.identificador = identificador;
    }

    public Arreglo(Token tipoDato, Token identificador, ArrayList<Argumento> argumentos) {
        this.tipoDato = tipoDato;
        this.identificador = identificador;
        this.argumentos = argumentos;
    }

    public Token getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Token tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    public ArrayList<Argumento> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(ArrayList<Argumento> argumentos) {
        this.argumentos = argumentos;
    }

    @Override
    public String toString() {
        return "Arreglo{" + "tipoDato=" + tipoDato + ", identificador=" + identificador + ", argumentos=" + argumentos + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Arreglo");

        raiz.add(new DefaultMutableTreeNode("Tipo de dato: " + tipoDato.getPalabra()));
        raiz.add(new DefaultMutableTreeNode("Nombre: " + identificador.getPalabra()));

        if (argumentos != null) {

            for (Argumento argumento : argumentos) {
                raiz.add(argumento.getArbolVisual());
            }

        }

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {

        Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);

        if (s != null) {
            tablaSimbolos.agregarErrorSemantico("Areglo: La variable [" + identificador.getPalabra() + "] ya existe en la función [" + ambito.getNombre() + "]");
        } else {
            tablaSimbolos.guardarSimboloVariable(identificador.getPalabra(), tipoDato.getPalabra(), identificador.getFila(), identificador.getColumna(), ambito);
            if (argumentos != null) {
                int i = 0;
                for (Argumento arg : argumentos) {
                    if (!tipoDato.getPalabra().equals(arg.obtenerTipo(tablaSimbolos, ambito))) {
                        tablaSimbolos.agregarErrorSemantico("Arreglo: El argumento [" + i + "] no es asignable al arreglo [" + identificador.getPalabra() + "]");
                    }
                    i++;
                }
            }
        }

    }

    @Override
    public String getJavaCode() {

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

        String codigo = tipo + " " + identificador.getPalabra() + " []";
        
        int a [] = {5, 3};

        if (argumentos != null) {
            codigo += " = { ";
            for (Argumento argumento : argumentos) {
                codigo += argumento.getJavaCode() + ", ";
            }
            codigo = codigo.substring(0, codigo.length() - 2);
            codigo += " }";
        }

        codigo += "; ";
        return codigo;
    }

}
