package Sintactico;

import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Incremento extends Sentencia {
    
    private Token identificador;
    private Token operadorIncremento;
    
    public Incremento(Token identificador, Token operadorIncremento) {
        this.identificador = identificador;
        this.operadorIncremento = operadorIncremento;
    }
    
    public Token getIdentificador() {
        return identificador;
    }
    
    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }
    
    public Token getOperadorIncremento() {
        return operadorIncremento;
    }
    
    public void setOperadorIncremento(Token operadorIncremento) {
        this.operadorIncremento = operadorIncremento;
    }
    
    @Override
    public String toString() {
        return "Incremento{" + "identificador=" + identificador + ", operadorIncremento=" + operadorIncremento + '}';
    }
    
    @Override
    public DefaultMutableTreeNode getArbolVisual() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Incremento");
        
        raiz.add(new DefaultMutableTreeNode(identificador.getPalabra() + operadorIncremento.getPalabra()));
        
        return raiz;
    }
    
    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);
        
        if (s == null) {
            tablaSimbolos.agregarErrorSemantico("Incremento: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
        } else {
            if (!s.getTipo().equals("entero")) {
                tablaSimbolos.agregarErrorSemantico("Incremento: La variable [" + identificador.getPalabra() + "] no es de tipo entero");
            }
        }
    }

    @Override
    public String getJavaCode() {
        return identificador.getPalabra() + "++; ";
    }
    
}
