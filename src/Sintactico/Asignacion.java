package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Asignacion extends Sentencia {

    private Token nombre;
    private Token operadorAsignacion;
    private Expresion expresion;
    private InvocacionFuncion invocacionFuncion;
    private LecturaDeDatos lecturaDeDatos;
    private TablaSimbolos tSimbolos;
    private Simbolo amb;

    public Asignacion(Token nombre, Token operadorAsignacion, Expresion expresion) {
        this.nombre = nombre;
        this.operadorAsignacion = operadorAsignacion;
        this.expresion = expresion;
    }

    public Asignacion(Token nombre, Token operadorAsignacion, InvocacionFuncion invocacionFuncion) {
        this.nombre = nombre;
        this.operadorAsignacion = operadorAsignacion;
        this.invocacionFuncion = invocacionFuncion;
    }

    public Asignacion(Token nombre, Token operadorAsignacion, LecturaDeDatos lecturaDeDatos) {
        this.nombre = nombre;
        this.operadorAsignacion = operadorAsignacion;
        this.lecturaDeDatos = lecturaDeDatos;
        this.tSimbolos = null;
        this.amb = null;
    }

    public Token getNombre() {
        return nombre;
    }

    public void setNombre(Token nombre) {
        this.nombre = nombre;
    }

    public Token getOperadorAsignacion() {
        return operadorAsignacion;
    }

    public void setOperadorAsignacion(Token operadorAsignacion) {
        this.operadorAsignacion = operadorAsignacion;
    }

    public Expresion getExpresion() {
        return expresion;
    }

    public void setExpresion(Expresion expresion) {
        this.expresion = expresion;
    }

    public InvocacionFuncion getInvocacionFuncion() {
        return invocacionFuncion;
    }

    public void setInvocacionFuncion(InvocacionFuncion invocacionFuncion) {
        this.invocacionFuncion = invocacionFuncion;
    }

    public LecturaDeDatos getLecturaDeDatos() {
        return lecturaDeDatos;
    }

    public void setLecturaDeDatos(LecturaDeDatos lecturaDeDatos) {
        this.lecturaDeDatos = lecturaDeDatos;
    }

    @Override
    public String toString() {
        return "Asignacion{" + "nombre=" + nombre + ", operadorAsignacion=" + operadorAsignacion + ", expresion=" + expresion + ", invocacionFuncion=" + invocacionFuncion + ", lecturaDeDatos=" + lecturaDeDatos + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Asignación");

        raiz.add(new DefaultMutableTreeNode("Identificador: " + nombre.getPalabra()));
        raiz.add(new DefaultMutableTreeNode("Operador: " + operadorAsignacion.getPalabra()));

        if (expresion != null) {
            raiz.add(expresion.getArbolVisual());
        } else if (invocacionFuncion != null) {
            raiz.add(invocacionFuncion.getArbolVisual());
        } else if (lecturaDeDatos != null) {
            raiz.add(lecturaDeDatos.getArbolVisual());
        }

        return raiz;

    }

    @Override
    public void analizarSemantica(TablaSimbolos tablaSimbolos, Simbolo ambito) {
        tSimbolos = tablaSimbolos;
        amb = ambito;
        Simbolo s = tablaSimbolos.buscarSimboloVariable(nombre.getPalabra(), ambito);

        if (s != null) {

            if (expresion != null) {
                String tipoDatoExpresion = expresion.obtenerTipo(tablaSimbolos, ambito);
                if (!s.getTipo().equals(tipoDatoExpresion)) {
                    tablaSimbolos.agregarErrorSemantico("Asignación: La expresión no es aignable a la variable [" + nombre.getPalabra() + "]");
                }
            } else if (invocacionFuncion != null) {
                Simbolo inv = tablaSimbolos.buscarSimboloFuncionArgs(invocacionFuncion.getNombre().getPalabra(), invocacionFuncion.obtenerTipoArgumentos(tablaSimbolos, ambito), tablaSimbolos);
                if (inv == null) {
                    tablaSimbolos.agregarErrorSemantico("Asignación: La función [" + invocacionFuncion.getNombre().getPalabra() + "] no existe");
                } else if (!s.getTipo().equals(invocacionFuncion.obtenerTipo(tablaSimbolos, ambito))) {
                    tablaSimbolos.agregarErrorSemantico("Asignación: El retorno de la función [" + invocacionFuncion.getNombre().getPalabra() + "] no es aignable a la variable [" + nombre.getPalabra() + "]");
                } else if (lecturaDeDatos != null) {
                    if (s.getTipo().equals("booleano")) {
                        tablaSimbolos.agregarErrorSemantico("Asignación: Una lectura de datos no es asignable a la variable [" + nombre.getPalabra() + "]");
                    }
                }
            }
        } else {

            tablaSimbolos.agregarErrorSemantico("Asignación: La variable [" + nombre.getPalabra() + "] no existe en la función [" + ambito.getNombre() + "]");
        }
    }

    @Override
    public String getJavaCode() {
        Simbolo s = tSimbolos.buscarSimboloVariable(nombre.getPalabra(), amb);
        String operador = "";
        switch (operadorAsignacion.getPalabra()) {
            case "|+":
                operador = "+=";
                break;
            case "|-":
                operador = "-=";
                break;
            case "|*":
                operador = "*=";
                break;
            case "|/":
                operador = "/=";
                break;
            case "|%":
                operador = "%=";
                break;
            case "|=":
                operador = "=";
                break;
            default:
                break;
        }

        String codigo = nombre.getPalabra() + " " + operador + " ";

        if (expresion != null) {
            codigo += expresion.getJavaCode();
        } else if (invocacionFuncion != null) {
            codigo += invocacionFuncion.getJavaCode();

        } else {
            if (s.getTipo().equals("entero")
            
                ) {
                codigo += "Integer.parseInt( " + lecturaDeDatos.getJavaCode() + " )";
            }else if (s.getTipo().equals("real")) {
                codigo += "Double.parseDouble( " + lecturaDeDatos.getJavaCode() + " )";
            } else {
                codigo += lecturaDeDatos.getJavaCode();
            }
        }

        codigo += "; ";

        return codigo;
    }

}
