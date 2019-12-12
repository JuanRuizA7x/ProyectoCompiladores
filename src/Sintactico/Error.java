package Sintactico;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Error {

    private String mensaje;
    private int fila;
    private int columna;

    public Error(String mensaje, int fila, int columna) {
        this.mensaje = mensaje;
        this.fila = fila;
        this.columna = columna;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    @Override
    public String toString() {
        return "Error{" + "mensaje=" + mensaje + ", fila=" + fila + ", columna=" + columna + '}';
    }

}
