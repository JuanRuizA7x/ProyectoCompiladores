package Sintactico;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class BloqueSentencias {

    ArrayList<Sentencia> sentencias;

    public BloqueSentencias(ArrayList<Sentencia> sentencias) {
        this.sentencias = sentencias;
    }

    public ArrayList<Sentencia> getSentencias() {
        return sentencias;
    }

    public void setSentencias(ArrayList<Sentencia> sentencias) {
        this.sentencias = sentencias;
    }

    @Override
    public String toString() {
        return "BloqueSentencias{" + "sentencias=" + sentencias + '}';
    }

    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Bloque de sentencias");

        if (sentencias != null) {

            for (Sentencia sentencias : sentencias) {
                raiz.add(sentencias.getArbolVisual());
            }
        }

        return raiz;

    }

    String getJavaCode() {
        String codigo = "";
        for (Sentencia sentencia : sentencias) {
            codigo += sentencia.getJavaCode();
        }
        return codigo;
    }

}
