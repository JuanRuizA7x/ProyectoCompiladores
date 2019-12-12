package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Romper extends Sentencia{

    @Override
    public String toString() {
        return "Romper{" + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Romper");

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        
    }

    @Override
    public String getJavaCode() {
        return "break; ";
    }
}
