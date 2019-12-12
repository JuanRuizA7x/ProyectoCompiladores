package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ExpresionAuxiliar extends Expresion {

    private Token opeAt;
    private ExpresionAritmetica expArt;
    private ExpresionAuxiliar expAux;

    public ExpresionAuxiliar(Token opeAt, ExpresionAritmetica expArt, ExpresionAuxiliar expAux) {
        this.opeAt = opeAt;
        this.expArt = expArt;
        this.expAux = expAux;
    }

    public Token getOpeAt() {
        return opeAt;
    }

    public void setOpeAt(Token opeAt) {
        this.opeAt = opeAt;
    }

    public ExpresionAritmetica getExpArt() {
        return expArt;
    }

    public void setExpArt(ExpresionAritmetica expArt) {
        this.expArt = expArt;
    }

    public ExpresionAuxiliar getExpAux() {
        return expAux;
    }

    public void setExpAux(ExpresionAuxiliar expAux) {
        this.expAux = expAux;
    }

    @Override
    public String toString() {
        return "ExpresionAuxiliar{" + "opeAt=" + opeAt + ", expArt=" + expArt + ", expAux=" + expAux + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Expresión Auxiliar");

        raiz.add(new DefaultMutableTreeNode("Operador: " + opeAt.getPalabra()));
        raiz.add(expArt.getArbolVisual());

        if (expAux != null) {
            raiz.add(expAux.getArbolVisual());
        }

        return raiz;

    }

    @Override
    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        String tipoArit = expArt.obtenerTipo(tablaSimbolos, ambito);
        String tipoAux;

        if (expAux != null) {
            tipoAux = expAux.obtenerTipo(tablaSimbolos, ambito);
            if (tipoArit.equals(tipoAux)) {
                if (tipoArit.equals("booleano")) {
                    return "";
                }
                return tipoArit;
            } else {
                if (tipoArit.equals("cadena")) {
                    return tipoArit;
                } else {
                    if (("entero".equals(tipoArit) || "real".equals(tipoArit)) && ("entero".equals(tipoAux) || "real".equals(tipoAux))) {
                        if ("entero".equals(tipoArit) && "entero".equals(tipoAux)) {
                            return "entero";
                        }
                        return "real";
                    } else {
                        return "";
                    }
                }
            }
        }

        return tipoArit;
    }

    @Override
    public String getJavaCode() {
        String operador = "";

        switch (opeAt.getPalabra()) {
            case ":+":
                operador = "+";
                break;
            case ":-":
                operador = "-";
                break;
            case ":*":
                operador = "*";
                break;
            case ":/":
                operador = "/";
                break;
            case ":%":
                operador = "%";
                break;
            default:
                break;
        }

        String codigo = operador + " " + expArt.getJavaCode();
        
        if(expAux != null) {
            codigo += " " + expAux.getJavaCode();
        }
        
        return codigo;
    }

}
