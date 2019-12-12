package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class LecturaDeDatos extends Sentencia {

    private Token cadenaCaracteres;

    public LecturaDeDatos(Token cadenaCaracteres) {
        this.cadenaCaracteres = cadenaCaracteres;
    }

    public Token getCadenaCaracteres() {
        return cadenaCaracteres;
    }

    public void setCadenaCaracteres(Token cadenaCaracteres) {
        this.cadenaCaracteres = cadenaCaracteres;
    }

    @Override
    public String toString() {
        return "LecturaDeDatos{" + "cadenaCaracteres=" + cadenaCaracteres + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Lectura de Datos");

        raiz.add(new DefaultMutableTreeNode("Cadena de caracteres: " + cadenaCaracteres.getPalabra()));
        return raiz;
    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {

    }

    @Override
    public String getJavaCode() {
        String codigo = "JOptionPane.showInputDialog( ";
        String cad = cadenaCaracteres.getPalabra();
        cad = cad.substring(1, cad.length() - 1);
        codigo += "\"" + cad + "\" )";
        return codigo;
    }

}
