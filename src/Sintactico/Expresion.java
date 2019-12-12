package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public abstract class Expresion {

    public abstract DefaultMutableTreeNode getArbolVisual();
    public abstract String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito);
    public abstract String getJavaCode();

}
