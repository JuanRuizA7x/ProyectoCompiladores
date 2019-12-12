package Lexico;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class Token {

    private String palabra;
    private Categoria categoria;
    private int fila, columna;

    public Token(String palabra, Categoria categoria, int fila, int columna) {
        super();
        this.palabra = palabra;
        this.categoria = categoria;
        this.fila = fila;
        this.columna = columna;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
        return "Token [palabra=" + palabra + ", categoria=" + categoria + ", fila=" + fila + ", columna=" + columna
                + "]";
    }

}
