package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Mientras extends Sentencia {

    private ExpresionLogica expresionLogica;
    private BloqueSentencias bloqueSentencias;

    public Mientras(ExpresionLogica expresionLogica, BloqueSentencias bloqueSentencias) {
        this.expresionLogica = expresionLogica;
        this.bloqueSentencias = bloqueSentencias;
    }

    public ExpresionLogica getExpresionLogica() {
        return expresionLogica;
    }

    public void setExpresionLogica(ExpresionLogica expresionLogica) {
        this.expresionLogica = expresionLogica;
    }

    public BloqueSentencias getBloqueSentencias() {
        return bloqueSentencias;
    }

    public void setBloqueSentencias(BloqueSentencias bloqueSentencias) {
        this.bloqueSentencias = bloqueSentencias;
    }

    @Override
    public String toString() {
        return "Mientras{" + "expresionLogica=" + expresionLogica + ", bloqueSentencias=" + bloqueSentencias + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Mientras");

        raiz.add(expresionLogica.getArbolVisual());

        if (!bloqueSentencias.getSentencias().isEmpty()) {

            raiz.add(bloqueSentencias.getArbolVisual());

        }

        return raiz;
    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        if (bloqueSentencias != null) {
            for (Sentencia sen : bloqueSentencias.getSentencias()) {
                sen.analizarSemantica(tablaSimbolos, ambito);
            }
        }
    }

    @Override
    public String getJavaCode() {
        String codigo = "while ( " + expresionLogica.getJavaCode() + " ) { " + bloqueSentencias.getJavaCode() + "} ";
        return codigo;
    }
}
