package Sintactico;

import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class ExpresionAritmetica extends Expresion {

    private ExpresionAritmetica expArt;
    private ExpresionAuxiliar expAux;
    private ValorNumerico valor;
    private Token identificador;

    public ExpresionAritmetica(ExpresionAritmetica expArt, ExpresionAuxiliar expAux) {
        this.expArt = expArt;
        this.expAux = expAux;
    }

    public ExpresionAritmetica(ValorNumerico valor, ExpresionAuxiliar expAux) {
        this.valor = valor;
        this.expAux = expAux;
    }

    public ExpresionAritmetica(Token identificador, ExpresionAuxiliar expAux) {
        this.identificador = identificador;
        this.expAux = expAux;
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

    public ValorNumerico getValor() {
        return valor;
    }

    public void setValor(ValorNumerico valor) {
        this.valor = valor;
    }

    public Token getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Token identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return "ExpresionAritmetica{" + "expArt=" + expArt + ", expAux=" + expAux + ", valor=" + valor + ", identificador=" + identificador + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Expresión Aritmética");

        if (expArt != null) {
            raiz.add(expArt.getArbolVisual());

            if (expAux != null) {
                raiz.add(expAux.getArbolVisual());
            }
        } else if (valor != null) {
            raiz.add(valor.getArbolVisual());

            if (expAux != null) {
                raiz.add(expAux.getArbolVisual());
            }
        } else {
            raiz.add(new DefaultMutableTreeNode("Identificador: " + identificador.getPalabra()));

            if (expAux != null) {
                raiz.add(expAux.getArbolVisual());
            }
        }

        return raiz;

    }

    @Override
    public String obtenerTipo(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        String tipoArit;
        String tipoAux;
        String valorNum;

        if (identificador != null) {
            if (identificador.equals("nulo")) {
                return "nulo";
            }
            Simbolo s = tablaSimbolos.buscarSimboloVariable(identificador.getPalabra(), ambito);
            if (s != null) {
                if (expAux != null) {
                    tipoAux = expAux.obtenerTipo(tablaSimbolos, ambito);
                    if (s.getTipo().equals(tipoAux)) {
                        if (s.getTipo().equals("booleano")) {
                            return "";
                        }
                        return s.getTipo();
                    } else {
                        if ("cadena".equals(s.getTipo())) {
                            return s.getTipo();
                        } else {
                            if (("entero".equals(s.getTipo()) || "real".equals(s.getTipo())) && ("entero".equals(tipoAux) || "real".equals(tipoAux))) {
                                if ("entero".equals(s.getTipo()) && "entero".equals(tipoAux)) {
                                    return "entero";
                                }
                                return "real";
                            } else {
                                return "";
                            }
                        }
                    }
                }
                return s.getTipo();
            } else {
                tablaSimbolos.agregarErrorSemantico("Expresión aritmética: La variable [" + identificador.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
            }
        } else {
            if (valor != null) {
                valorNum = valor.obtenerTipo();
                if (expAux != null) {
                    tipoAux = expAux.obtenerTipo(tablaSimbolos, ambito);
                    if (valorNum.equals(tipoAux)) {
                        return valorNum;
                    } else if (("entero".equals(valorNum) || "real".equals(valorNum)) && ("entero".equals(tipoAux) || "real".equals(tipoAux))) {
                        if ("entero".equals(valorNum) && "entero".equals(tipoAux)) {
                            return "entero";
                        }
                        return "real";
                    } else {
                        return "";
                    }
                }
                return valorNum;
            } else {
                tipoArit = expArt.obtenerTipo(tablaSimbolos, ambito);
                if (expAux != null) {
                    tipoAux = expAux.obtenerTipo(tablaSimbolos, ambito);
                    if (tipoArit.equals(tipoAux)) {
                        if (tipoArit.equals("booleano")) {
                            return "";
                        }
                        return tipoArit;
                    } else {
                        if ("cadena".equals(tipoArit)) {
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
        }
        return "";
    }

    @Override
    public String getJavaCode() {
        String codigo = "";
        if (expArt != null) {
            codigo += "( " + expArt.getJavaCode() + " )";

            if (expAux != null) {
                codigo += " " + expAux.getJavaCode();
            }
        } else {
            if (valor != null) {
                codigo += valor.getJavaCode();

                if (expAux != null) {
                    codigo += " " + expAux.getJavaCode();
                }
            } else {
                codigo += identificador.getPalabra();

                if (expAux != null) {
                    codigo += " " + expAux.getJavaCode();
                }
            }
        }
        return codigo;
    }

}
