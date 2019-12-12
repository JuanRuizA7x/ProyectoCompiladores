package Sintactico;

import javax.swing.tree.DefaultMutableTreeNode;
import Lexico.Token;
import Semantico.Simbolo;
import Semantico.TablaSimbolos;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Declaracion extends Sentencia {

    private Token tipoDato;
    private Token nombre;
    private Expresion expresion;
    private InvocacionFuncion invocacionFuncion;
    private LecturaDeDatos lecturaDeDatos;

    public Declaracion(Token tipoDato, Token nombre, Expresion expresion) {
        this.tipoDato = tipoDato;
        this.nombre = nombre;
        this.expresion = expresion;
    }

    public Declaracion(Token tipoDato, Token nombre, InvocacionFuncion invocacionFuncion) {
        this.tipoDato = tipoDato;
        this.nombre = nombre;
        this.invocacionFuncion = invocacionFuncion;
    }

    public Declaracion(Token tipoDato, Token nombre, LecturaDeDatos lecturaDeDatos) {
        this.tipoDato = tipoDato;
        this.nombre = nombre;
        this.lecturaDeDatos = lecturaDeDatos;
    }

    public Declaracion(Token tipoDato, Token nombre) {
        this.tipoDato = tipoDato;
        this.nombre = nombre;
    }

    public Token getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Token tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Token getNombre() {
        return nombre;
    }

    public void setNombre(Token nombre) {
        this.nombre = nombre;
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
        return "Declaracion{" + "tipoDato=" + tipoDato + ", nombre=" + nombre + ", expresion=" + expresion + ", invocacionFuncion=" + invocacionFuncion + ", lecturaDeDatos=" + lecturaDeDatos + '}';
    }

    @Override
    public DefaultMutableTreeNode getArbolVisual() {

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Declaración");

        raiz.add(new DefaultMutableTreeNode("Tipo de dato: " + tipoDato.getPalabra()));
        raiz.add(new DefaultMutableTreeNode("Identificador: " + nombre.getPalabra()));

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
        Simbolo s = tablaSimbolos.buscarSimboloVariable(nombre.getPalabra(), ambito);

        if (s == null) {

            tablaSimbolos.guardarSimboloVariable(nombre.getPalabra(), tipoDato.getPalabra(), nombre.getFila(),
                    nombre.getColumna(), ambito);

            if (expresion != null) {
                String tipoDatoExpresion = expresion.obtenerTipo(tablaSimbolos, ambito);
                if (!tipoDatoExpresion.equals(tipoDato.getPalabra())) {
                    tablaSimbolos.agregarErrorSemantico("Declaración: La expresión no es asignable a la variable [" + nombre.getPalabra() + "]");
                }
            } else if (invocacionFuncion != null) {
                Simbolo inv = tablaSimbolos.buscarSimboloFuncionArgs(invocacionFuncion.getNombre().getPalabra(), invocacionFuncion.obtenerTipoArgumentos(tablaSimbolos, ambito), tablaSimbolos);
                if (inv == null) {
                    tablaSimbolos.agregarErrorSemantico("Declaración: La función [" + invocacionFuncion.getNombre().getPalabra() + "] no existe");
                } else if (!invocacionFuncion.obtenerTipo(tablaSimbolos, ambito).equals(tipoDato.getPalabra())) {
                    tablaSimbolos.agregarErrorSemantico("Declaración: El retorno de la función [" + invocacionFuncion.getNombre().getPalabra() + "] no es aignable a la variable [" + nombre.getPalabra() + "]");
                }
            } else if (lecturaDeDatos != null) {
                if (tipoDato.getPalabra().equals("booleano")) {
                    tablaSimbolos.agregarErrorSemantico("Declaración: Una lectura de datos no es asignable a la variable [" + nombre.getPalabra() + "]");
                }
            }
        } else {

            tablaSimbolos.agregarErrorSemantico("Declaración: La variable [" + nombre.getPalabra() + "] ya existe en la función [" + ambito.getNombre() + "]");
        }
    }

    @Override
    public String getJavaCode() {

        String tipo = "";

        switch (tipoDato.getPalabra()) {
            case "entero":
                tipo = "int";
                break;
            case "real":
                tipo = "double";
                break;
            case "booleano":
                tipo = "boolean";
                break;
            case "cadena":
                tipo = "String";
                break;
            default:
                break;
        }

        String codigo = tipo + " " + nombre.getPalabra();

        if (expresion != null) {
            codigo += " = " + expresion.getJavaCode();
        } else if (invocacionFuncion != null) {
            codigo += " = " + invocacionFuncion.getJavaCode();

        } else if (lecturaDeDatos != null) {
            if (tipo.equals("int")) {
                codigo += " = Integer.parseInt( " + lecturaDeDatos.getJavaCode() + " )";
            } else if (tipo.equals("double")) {
                codigo += " = Double.parseDouble( " + lecturaDeDatos.getJavaCode() + " )";
            } else {
                codigo += " = " + lecturaDeDatos.getJavaCode();
            }
        }

        codigo += "; ";

        return codigo;
    }

}
