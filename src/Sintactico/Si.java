package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Si extends Sentencia {

    private ExpresionLogica expresionLogica;
    private BloqueSentencias bloqueSi;

    public Si(ExpresionLogica expresionLogica, BloqueSentencias bloqueSi) {
        this.expresionLogica = expresionLogica;
        this.bloqueSi = bloqueSi;
    }

    public ExpresionLogica getExpresionLogica() {
        return expresionLogica;
    }

    public void setExpresionLogica(ExpresionLogica expresionLogica) {
        this.expresionLogica = expresionLogica;
    }

    public BloqueSentencias getBloqueSi() {
        return bloqueSi;
    }

    public void setBloqueSi(BloqueSentencias bloqueSi) {
        this.bloqueSi = bloqueSi;
    }

    @Override
    public String toString() {
        return "Si{" + "expresionLogica=" + expresionLogica + ", bloqueSi=" + bloqueSi + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Si");

        raiz.add(expresionLogica.getArbolVisual());

        if (!bloqueSi.getSentencias().isEmpty()) {

            DefaultMutableTreeNode raizTrue = new DefaultMutableTreeNode("Sentencias \"Si\"");

            raizTrue.add(bloqueSi.getArbolVisual());

            raiz.add(raizTrue);
        }

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        if (bloqueSi != null) {
            for (Sentencia sen : bloqueSi.getSentencias()) {
                sen.analizarSemantica(tablaSimbolos, ambito);
            }
        }
    }

    @Override
    public String getJavaCode() {
        String codigo = "if ( " + expresionLogica.getJavaCode() + " ) { " + bloqueSi.getJavaCode() + "} ";
        return codigo;
    }

}
