package Semantico;

import java.util.ArrayList;

import Sintactico.Parametro;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Simbolo {

    private String nombre;
    private String tipo;
    private int fila, columna;
    private Simbolo ambito;
    private ArrayList<Parametro> tipoParametros;
    
    public Simbolo(String nombre, String tipo, int fila, int columna, Simbolo ambito) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.ambito = ambito;
    }

    public Simbolo(String nombre, String tipo, ArrayList<Parametro> tipoParametros) {
        super();
        this.nombre = nombre;
        this.tipo = tipo;
        this.tipoParametros = tipoParametros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Simbolo getAmbito() {
        return ambito;
    }

    public void setAmbito(Simbolo ambito) {
        this.ambito = ambito;
    }

    public ArrayList<Parametro> getTipoParametros() {
        return tipoParametros;
    }

    public void setTipoParametros(ArrayList<Parametro> tipoParametros) {
        this.tipoParametros = tipoParametros;
    }
    
    public String toStringFuncion() {
        return "Simbolo{" + "nombre=" + nombre + ", tipo=" + tipo + ", tipoParametros=" + tipoParametros + '}';
    }

    public String toStringVariable() {
        return "Simbolo{" + "nombre=" + nombre + ", tipo=" + tipo + ", fila=" + fila + ", columna=" + columna + ", ambito=" + ambito + '}';
    }

}
