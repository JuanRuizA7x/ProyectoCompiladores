package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ExpresionRelacional extends Expresion {

    private ExpresionRelacional expRel;
    private ExpresionAritmetica expAt1, expAt2;
    private Token OpeRel, estado;

    public ExpresionRelacional(ExpresionRelacional expRel) {
        this.expRel = expRel;
    }

    public ExpresionRelacional(ExpresionAritmetica expAt1, ExpresionAritmetica expAt2, Token OpeAt) {
        this.expAt1 = expAt1;
        this.expAt2 = expAt2;
        this.OpeRel = OpeAt;
    }

    public ExpresionRelacional getExpRel() {
        return expRel;
    }

    public void setExpRel(ExpresionRelacional expRel) {
        this.expRel = expRel;
    }

    public ExpresionRelacional(Token estado) {
        this.estado = estado;
    }

    public ExpresionAritmetica getExpAt1() {
        return expAt1;
    }

    public void setExpAt1(ExpresionAritmetica expAt1) {
        this.expAt1 = expAt1;
    }

    public ExpresionAritmetica getExpAt2() {
        return expAt2;
    }

    public void setExpAt2(ExpresionAritmetica expAt2) {
        this.expAt2 = expAt2;
    }

    public Token getOpeRel() {
        return OpeRel;
    }

    public void setOpeRel(Token OpeAt) {
        this.OpeRel = OpeAt;
    }

    public Token getEstado() {
        return estado;
    }

    public void setEstado(Token estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ExpresionRelacional{" + "expRel=" + expRel + ", expAt1=" + expAt1 + ", expAt2=" + expAt2 + ", OpeRel=" + OpeRel + ", estado=" + estado + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Expresión Relacional");

        if (expRel != null) {
            raiz.add(expRel.getArbolVisual());
        } else if (expAt1 != null) {
            raiz.add(expAt1.getArbolVisual());
            raiz.add(new DefaultMutableTreeNode("Operador: " + OpeRel.getPalabra()));
            raiz.add(expAt2.getArbolVisual());
        } else {
            raiz.add(new DefaultMutableTreeNode("Estado: " + estado.getPalabra()));
        }

        return raiz;

    }

    @Override
    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        return "booleano";
    }

    @Override
    public String getJavaCode() {
        String codigo = "";
        if (expRel != null) {
            codigo += "( " + expRel.getJavaCode() + " )";
        } else {
            if (expAt1 != null) {
                String opRelacional = "";

                switch (OpeRel.getPalabra()) {
                    case ":<":
                        opRelacional = "<";
                        break;
                    case ":>":
                        opRelacional = ">";
                        break;
                    case ":<=":
                        opRelacional = "<=";
                        break;
                    case ":>=":
                        opRelacional = ">=";
                        break;
                    case ":==":
                        opRelacional = "==";
                        break;
                    case ":|=":
                        opRelacional = "!=";
                        break;
                    default:
                        break;
                }

                codigo += expAt1.getJavaCode() + " " + opRelacional + " " + expAt2.getJavaCode();
            } else {
                String booleano = "";
                
                if(estado.getPalabra().equals("verdadero")){
                    booleano = "true";
                } else {
                    booleano = "false";
                }
                
                codigo += booleano;
            }
        }
        return codigo;
    }

}
