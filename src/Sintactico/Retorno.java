package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Retorno extends Sentencia {

    private Expresion expresion;
    private Token identificador;

    public Retorno(Expresion expresion) {
        this.expresion = expresion;
    }

    public Retorno(Token identificador) {
        this.identificador = identificador;
    }

    public Expresion getExpresion() {
        return expresion;
    }

    public void setExpresion(Expresion expresion) {
        this.expresion = expresion;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "Retorno{" + "expresion=" + expresion + ", identificador=" + identificador + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Retorno");

        if (expresion != null) {
            raiz.add(expresion.getArbolVisual());

        } else {
            if (identificador.getPalabra().equals("nulo")) {
                raiz.add(new DefaultMutableTreeNode("Retorno nulo"));
            } else {
                raiz.add(new DefaultMutableTreeNode("identificador: " + identificador.getPalabra()));
            }
        }

        return raiz;
    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {

    }

    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        
        if (expresion != null) {
                        
            return expresion.obtenerTipo(tablaSimbolos, ambito);
            
        } else {
            
            if ("nulo".equals(identificador.getPalabra())){
                return "nulo";
            }
            
            Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);

            if (s != null) {

                return identificador.getPalabra();

            } else {
                tablaSimbolos.agregarErrorSemantico("Retorno: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
            }
        }

        return "";
    }

    @Override
    public String getJavaCode() {
        String codigo = "return ";
        if(identificador != null){
            if(identificador.equals("nulo")){
                codigo += "null";
            } else {
                codigo += identificador.getPalabra();
            }
        } else {
            codigo += expresion.getJavaCode();
        }
        
        codigo += "; ";
        
        return codigo;
    }
}
