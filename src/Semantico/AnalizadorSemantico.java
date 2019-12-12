package Semantico;

import Sintactico.UnidadDeCompilacion;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class AnalizadorSemantico {

    private TablaSimbolos tablaSimbolos;
    private UnidadDeCompilacion uc;

    public AnalizadorSemantico(UnidadDeCompilacion uc) {
        this.tablaSimbolos = new TablaSimbolos();
        this.uc = uc;
    }

    public void analizarSemantica() {
        uc.analizarSemantica();
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public void setTablaSimbolos(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public UnidadDeCompilacion getUc() {
        return uc;
    }

    public void setUc(UnidadDeCompilacion uc) {
        this.uc = uc;
    }

}