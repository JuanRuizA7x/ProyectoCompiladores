package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ValorNumerico {

    private Token signo, valor;

    public ValorNumerico(Token signo, Token valor) {
        this.signo = signo;
        this.valor = valor;
    }

    public Token getSigno() {
        return signo;
    }

    public void setSigno(Token signo) {
        this.signo = signo;
    }

    public Token getValor() {
        return valor;
    }

    public void setValor(Token valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "ValorNumerico{" + "signo=" + signo + ", valor=" + valor + '}';
    }

    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Valor numérico");

        if (signo != null) {
            raiz.add(new DefaultMutableTreeNode(signo.getPalabra() + valor.getPalabra()));
        } else {
            raiz.add(new DefaultMutableTreeNode(valor.getPalabra()));
        }

        return raiz;

    }

    String obtenerTipo() {
        return valor.getCategoria().toString().toLowerCase();
    }

    String getJavaCode() {
        String codigo = "";
        if(signo != null) {
            codigo += "-";
        }
        codigo += valor.getPalabra();
        return codigo;
    }
}
