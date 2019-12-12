package Sintactico;

import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Decremento extends Sentencia {

    private Token identificador;
    private Token operadorDecremento;

    public Decremento(Token identificador, Token operadorDecremento) {
        this.identificador = identificador;
        this.operadorDecremento = operadorDecremento;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    public Token getOperadorDecremento() {
        return operadorDecremento;
    }

    public void setOperadorDecremento(Token operadorDecremento) {
        this.operadorDecremento = operadorDecremento;
    }

    @Override
    public String toString() {
        return "Decremento{" + "identificador=" + identificador + ", operadorDecremento=" + operadorDecremento + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Decremento");

        raiz.add(new DefaultMutableTreeNode(identificador.getPalabra() + operadorDecremento.getPalabra()));

        return raiz;
    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);

        if (s == null) {
            tablaSimbolos.agregarErrorSemantico("Decremento: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
        } else {
            if (!s.getTipo().equals("entero")) {
                tablaSimbolos.agregarErrorSemantico("Decremento: La variable [" + identificador.getPalabra() + "] no es de tipo entero");
            }
        }
    }

    @Override
    public String getJavaCode() {
        return identificador.getPalabra() + "--; ";
    }

}
