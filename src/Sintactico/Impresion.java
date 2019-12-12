package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Categoria;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Impresion extends Sentencia {

    private Token identificador;
    private Token cadenaCaracteres;
    private InvocacionFuncion invocacionFuncion;

    public Impresion(Token token) {
        if (token.getCategoria() == Categoria.IDENTIFICADOR) {
            this.identificador = token;
        } else {
            this.cadenaCaracteres = token;
        }
    }

    public Impresion(InvocacionFuncion invocacionFuncion) {
        this.invocacionFuncion = invocacionFuncion;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    public Token getCadenaCaracteres() {
        return cadenaCaracteres;
    }

    public void setCadenaCaracteres(Token cadenaCaracteres) {
        this.cadenaCaracteres = cadenaCaracteres;
    }

    public InvocacionFuncion getInvocacionFuncion() {
        return invocacionFuncion;
    }

    public void setInvocacionFuncion(InvocacionFuncion invocacionFuncion) {
        this.invocacionFuncion = invocacionFuncion;
    }

    @Override
    public String toString() {
        return "Impresion{" + "identificador=" + identificador + ", cadenaCaracteres=" + cadenaCaracteres + ", invocacionFuncion=" + invocacionFuncion + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Impresión");

        if (identificador != null) {
            raiz.add(new DefaultMutableTreeNode("Identificador: " + identificador.getPalabra()));
        } else if (cadenaCaracteres != null) {
            raiz.add(new DefaultMutableTreeNode("Cadena de caracteres: " + cadenaCaracteres.getPalabra()));
        } else if (invocacionFuncion != null) {
            raiz.add(invocacionFuncion.getArbolVisual());
        }

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        if (identificador != null) {
            Simbolo iden = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);
            if (iden == null) {
                tablaSimbolos.agregarErrorSemantico("Impresión: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
            }
        } else if (invocacionFuncion != null) {
            Simbolo inv = tablaSimbolos.buscarSimboloFuncionArgs(invocacionFuncion.getNombre().getPalabra(), invocacionFuncion.obtenerTipoArgumentos(tablaSimbolos, ambito), tablaSimbolos);
            if (inv == null) {
                tablaSimbolos.agregarErrorSemantico("Impresión: La función [" + invocacionFuncion.getNombre().getPalabra() + "] no existe");
            }
        }
    }

    @Override
    public String getJavaCode() {
        String codigo = "JOptionPane.showMessageDialog(null, ";

        if (identificador != null) {
            codigo += identificador.getPalabra();
        } else if (cadenaCaracteres != null) {
            String cad = cadenaCaracteres.getPalabra();
            cad = cad.substring(1, cad.length() - 1);
            codigo += "\"" + cad + "\"";
        } else {
            String inv = invocacionFuncion.getJavaCode();
            inv = inv.substring(0, inv.length() -2);
            codigo += inv;
        }
        codigo += "); ";
        return codigo;
    }
}
