package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Argumento {

    private Expresion exp;
    private Token identificador;

    public Argumento(Expresion exp) {
        this.exp = exp;
    }

    public Argumento(Token identificador) {
        this.identificador = identificador;
    }

    public Expresion getExp() {
        return exp;
    }

    public void setExp(Expresion exp) {
        this.exp = exp;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "Argumento{" + "exp=" + exp + ", identificador=" + identificador + '}';
    }

    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Argumentos");

        if (identificador != null) {
            raiz.add(new DefaultMutableTreeNode("Identificador: " + identificador.getPalabra()));
        } else {
            raiz.add(exp.getArbolVisual());
        }

        return raiz;

    }

    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        if ( exp != null) {
            return exp.obtenerTipo(tablaSimbolos, ambito);
        } else {
            Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);
            if (s != null) {
                return s.getTipo();
            } else {
                tablaSimbolos.agregarErrorSemantico("Argumento: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
            }
        }
        return "";
    }

    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        
    }

    public String getJavaCode() {
        String codigo = "";
        
        if(identificador != null) {
            codigo += identificador.getPalabra();
        } else {
            codigo += exp.getJavaCode();
        }
        
        return codigo;
    }
}
