package Sintactico;

import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public abstract class Sentencia {
    
    public abstract DefaultMutableTreeNode getArbolVisual();
    public abstract void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito);
    public abstract String getJavaCode ();
    
}
