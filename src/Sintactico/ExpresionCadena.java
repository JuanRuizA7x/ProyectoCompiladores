package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ExpresionCadena extends Expresion {

    private Token cadena;
    private Expresion exp;
    private ExpresionCadena expC;

    public ExpresionCadena(Token cadena, Expresion exp) {
        this.cadena = cadena;
        this.exp = exp;
    }

    public ExpresionCadena(ExpresionCadena expC) {
        this.expC = expC;
    }

    public ExpresionCadena(Token cadena) {
        this.cadena = cadena;
    }

    public Token getCadena() {
        return cadena;
    }

    public void setCadena(Token cadena) {
        this.cadena = cadena;
    }

    public Expresion getExp() {
        return exp;
    }

    public void setExp(Expresion exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "ExpresionCadena{" + "cadena=" + cadena + ", exp=" + exp + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Expresión Cadena");

        if (expC != null) {
            raiz.add(expC.getArbolVisual());
        } else {
            raiz.add(new DefaultMutableTreeNode("Cadena de caracteres: " + cadena.getPalabra()));

            if (exp != null) {
                raiz.add(exp.getArbolVisual());
            }
        }

        return raiz;

    }

    @Override
    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        return "cadena";
    }

    @Override
    public String getJavaCode() {
        String codigo = "( ";
        String cad = cadena.getPalabra();
        cad = cad.substring(1, cad.length()-1);
        codigo += "\"" + cad + "\" )";
        
        if(exp != null){
            codigo += " + " + exp.getJavaCode();
        }
        return codigo;
    }

}
