package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ExpresionLogica extends Expresion {

    private Token negacion, operador;
    private ExpresionLogica expL;
    private ExpresionRelacional exp1, exp2;

    public ExpresionLogica(Token negacion, ExpresionLogica expL) {
        this.negacion = negacion;
        this.expL = expL;
    }

    public ExpresionLogica(ExpresionRelacional exp1, Token operador, ExpresionRelacional exp2) {
        this.exp1 = exp1;
        this.operador = operador;
        this.exp2 = exp2;
    }

    public ExpresionLogica(ExpresionRelacional exp1) {
        this.exp1 = exp1;
    }

    public ExpresionLogica(ExpresionLogica expL) {
        this.expL = expL;
    }

    public Token getNegacion() {
        return negacion;
    }

    public void setNegacion(Token negacion) {
        this.negacion = negacion;
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
    }

    public ExpresionLogica getExpL() {
        return expL;
    }

    public void setExpL(ExpresionLogica expL) {
        this.expL = expL;
    }

    public ExpresionRelacional getExp1() {
        return exp1;
    }

    public void setExp1(ExpresionRelacional exp1) {
        this.exp1 = exp1;
    }

    public ExpresionRelacional getExp2() {
        return exp2;
    }

    public void setExp2(ExpresionRelacional exp2) {
        this.exp2 = exp2;
    }

    @Override
    public String toString() {
        return "ExpresionLogica{" + "negacion=" + negacion + ", operador=" + operador + ", expL=" + expL + ", exp1=" + exp1 + ", exp2=" + exp2 + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Expresión Lógica");

        if (negacion != null) {
            raiz.add(new DefaultMutableTreeNode("Negación"));
            raiz.add(expL.getArbolVisual());
        } else if ((exp1 != null) && (exp2 == null)) {
            raiz.add(exp1.getArbolVisual());
        } else if (exp2 != null) {
            raiz.add(exp1.getArbolVisual());
            raiz.add(new DefaultMutableTreeNode("Operador: " + operador.getPalabra()));
            raiz.add(exp2.getArbolVisual());
        } else {
            raiz.add(expL.getArbolVisual());
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
        if (expL != null) {
            codigo += "( ";
            if(negacion != null){
                codigo += "!";
            }
            codigo += expL.getJavaCode() + " )";
        } else{
            if (exp2 != null) {
                String opLogico = "";
                
                if(operador.getPalabra().equals(":&")){
                    opLogico = "&&";
                } else {
                    opLogico = "||";
                }
                
                codigo += exp1.getJavaCode() + " " + opLogico + " " + exp2.getJavaCode();
            } else {
                codigo += exp1.getJavaCode();
            }
        }
        return codigo;
    }

}
