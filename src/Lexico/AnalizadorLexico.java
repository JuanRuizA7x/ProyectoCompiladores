package Lexico;

import java.util.ArrayList;

/**
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class AnalizadorLexico {

    private String codigoFuente;
    private ArrayList<Token> listaTokens;
    private char caracterActual, finCodigo;
    private int posActual, filaActual, columnaActual;

    public AnalizadorLexico(String codigoFuente) {
        this.codigoFuente = codigoFuente;
        this.listaTokens = new ArrayList<Token>();
        this.caracterActual = codigoFuente.charAt(0);
        this.finCodigo = 0;
    }

    public void analizar() {

        while (caracterActual != finCodigo) {

            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSgteCaracter();
                continue;
            }
            if (esReal()) {
                continue;
            }

            if (esEntero()) {
                continue;
            }

            if (esIdentificador()) {
                continue;
            }

            if (esOperadorLogico()) {
                continue;
            }

            if (esCaracter()) {
                continue;
            }
            if (esParentesis()) {
                continue;
            }
            if (esOperadorIncremento()) {
                continue;
            }
            if (esOperadorDecremento()) {
                continue;
            }
            if (esOperadorAritmetico()) {
                continue;
            }
            if (esPunto()) {
                continue;
            }
            if (esDosPuntos()) {
                continue;
            }
            if (esOperadorAsignacion()) {
                continue;
            }
            if (esCadenaCaracteres()) {
                continue;
            }
            if (esComentarioBloque()) {
                continue;
            }
            if (esComentarioLinea()) {
                continue;
            }
            if (esOperadorRelacional()) {
                continue;
            }
            if (esLlave()) {
                continue;
            }
            if (esCorchete()) {
                continue;
            }
            if (esSeparador()) {
                continue;
            }
            if (esFinDeSentencia()) {
                continue;
            }
            listaTokens.add(new Token("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual));
            obtenerSgteCaracter();

        }

    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un número
     * entero. Expresión regular= (D)(D)* [Digito(D)]
     *
     * @return verdadero si cumple la condicion, falso en otro caso.
     */
    public boolean esEntero() {

        //RI
        if (!Character.isDigit(caracterActual)) {
            return false;
        }

        String lexema = "";
        int posInicial = posActual, fila = filaActual, columna = columnaActual;;

        //Transición 1
        lexema += caracterActual;
        obtenerSgteCaracter();

        //Bucle
        while (Character.isDigit(caracterActual)) {
            lexema += caracterActual;
            obtenerSgteCaracter();
        }

        //BT
        if (caracterActual == '.') {
            hacerBT(posInicial, fila, columna);
            return false;
        }

        //AA
        listaTokens.add(new Token(lexema, Categoria.ENTERO, fila, columna));
        return true;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un número
     * Real. Expresión regular = ((D)(D)*.(D)(D)* U .(D)(D)* U(D)(D)*.) [DIGITO
     * (D)]
     *
     * @return true si cumple la condicion, falso en otro caso.
     */
    private boolean esReal() {
        //RI
        if (!Character.isDigit(caracterActual) && caracterActual != '.') {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;

        if (caracterActual == '.') {

            lexema += caracterActual;
            obtenerSgteCaracter();

            if (!Character.isDigit(caracterActual)) {
                hacerBT(posInicial, fila, columna);
                return false;
            } else {
                lexema += caracterActual;
                obtenerSgteCaracter();
            }

        } else {

            lexema += caracterActual;
            obtenerSgteCaracter();
            while (Character.isDigit(caracterActual)) {
                lexema += caracterActual;
                obtenerSgteCaracter();
            }

            if (caracterActual != '.') {
                hacerBT(posInicial, fila, columna);
                return false;
            } else {
                lexema += caracterActual;
                obtenerSgteCaracter();
            }
        }

        while (Character.isDigit(caracterActual)) {
            lexema += caracterActual;
            obtenerSgteCaracter();
        }

        listaTokens.add(new Token(lexema, Categoria.REAL, fila, columna));
        return true;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Identificador Expresion regualar = ((L)((L)U(D)U(¬)U(_))
     *
     *
     * @return verdaderi si cumple la condición, falso en otro caso.
     */
    public boolean esIdentificador() {
        //RI

        if (!Character.isLetter(caracterActual)) {
            return false;
        }

        String lexema = "";
        int fila = filaActual;
        int columna = columnaActual;

        //Transición
        lexema += caracterActual;
        obtenerSgteCaracter();

        while (Character.isLetter(caracterActual) || caracterActual == '_' || Character.isDigit(caracterActual)) {
            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();
        }

        for (PalabraReservada palabraReservada : PalabraReservada.values()) {
            if (lexema.equals(palabraReservada.toString().toLowerCase())) {
                listaTokens.add(new Token(lexema, Categoria.PALABRA_RESERVADA, fila, columna));
                return true;
            }
        }
        listaTokens.add(new Token(lexema, Categoria.IDENTIFICADOR, fila, columna));
        return true;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * Logico. Expresión regular = (:)(& U $ U !)
     *
     * @return true si cumple la condicion, false en otro caso.
     */
    private boolean esOperadorLogico() {

        //RI
        if ((caracterActual != ':')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();

        if (caracterActual == '&' || caracterActual == '~' || caracterActual == '$') {

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();
            listaTokens.add(new Token(lexema, Categoria.OPERADOR_LOGICO, fila, columna));
            return true;
        } else {
            hacerBT(posInicial, fila, columna);
            return false;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * de Incremento Expresion regualar = (+)(+)
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esOperadorIncremento() {
        //RI
        if ((caracterActual != '+')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();

        if ((caracterActual == '+')) {
            lexema += caracterActual;
            obtenerSgteCaracter();
            listaTokens.add(new Token(lexema, Categoria.OPERADOR_INCREMENTO, fila, columna));
            return true;
        } else {
            listaTokens.add(new Token(lexema, Categoria.ERROR_OPERADOR_INCREMENTO, fila, columna));
            return true;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * de Incremento Expresion regualar = (-)(-)
     *
     * @return verdadero si cumple la condición, falso en otro caso.
     */
    public boolean esOperadorDecremento() {
        //RI
        if ((caracterActual != '-')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();

        if ((caracterActual == '-')) {
            lexema += caracterActual;
            obtenerSgteCaracter();
            listaTokens.add(new Token(lexema, Categoria.OPERADOR_DECREMENTO, fila, columna));
            return true;
        } else {
            listaTokens.add(new Token(lexema, Categoria.ERROR_OPERADOR_DECREMENTO, fila, columna));
            return true;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * de Asignacion. Expresión regular = (|)(+ U - U * U / U % U =)
     *
     * @return true si cumple la condicion, false en otro caso.
     */
    public boolean esOperadorAsignacion() {

        //RI
        if (caracterActual == '|') {

            String palabra = "";
            int posInicial = posActual;
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '%' || caracterActual == '=') {
                //Transición
                palabra += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(palabra, Categoria.OPERADOR_ASIGNACION, fila, columna));
                return true;
            } else {
                hacerBT(posInicial, fila, columna);
                return false;
            }

        }

        return false;

    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Paréntesis abierto o cerrado Expresion regular = ( ( ) U ( ) )
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esParentesis() {
        if (caracterActual == '(') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.PARENTESIS_ABIERTO, fila, columna));
            return true;
        } else if (caracterActual == ')') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.PARENTESIS_CERRADO, fila, columna));
            return true;
        }
        return false;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Paréntesis abierto o cerrado Expresion regular = ( { ) U ( } )
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esLlave() {
        if (caracterActual == '{') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.LLAVE_ABIERTA, fila, columna));
            return true;
        } else if (caracterActual == '}') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.LLAVE_CERRADA, fila, columna));
            return true;
        }
        return false;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Paréntesis abierto o cerrado Expresion regular = ( [ ) U ( ] )
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esCorchete() {
        if (caracterActual == '[') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.CORCHETE_ABIERTO, fila, columna));
            return true;
        } else if (caracterActual == ']') {
            String palabra = "";
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            palabra += caracterActual;
            obtenerSgteCaracter();

            listaTokens.add(new Token(palabra, Categoria.CORCHETE_CERRADO, fila, columna));
            return true;
        }
        return false;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de una Cadena
     * de Caracteres Expresion regualar = (¬)(COS)(¬)
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esCaracter() {

        //RI
        if ((caracterActual != '¬')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();

        if (caracterActual != '\\') {
            if (caracterActual == finCodigo) {
                listaTokens.add(new Token(lexema, Categoria.ERROR_CARACTER, fila, columna));
                return true;
            } else {
                lexema += caracterActual;
                obtenerSgteCaracter();
            }
        } else {
            lexema += caracterActual;
            obtenerSgteCaracter();
            if (caracterActual == '\\' || caracterActual == 'n' || caracterActual == 't' || caracterActual == '¬') {
                lexema += caracterActual;
                obtenerSgteCaracter();
            } else {
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.ERROR_CARACTER, fila, columna));
                return true;
            }
        }

        if (caracterActual == '¬') {
            lexema += caracterActual;
            obtenerSgteCaracter();
            listaTokens.add(new Token(lexema, Categoria.CARACTER, fila, columna));
            return true;
        } else {
            listaTokens.add(new Token(lexema, Categoria.ERROR_CARACTER, fila, columna));
            return true;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * Aritmetico. Expresión regular = (:)(+ U - U * U / U %)
     *
     * @return true si cumple la condicion, false en otro caso.
     */
    private boolean esOperadorAritmetico() {
        //RI
        if ((caracterActual != ':')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();

        if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();
            listaTokens.add(new Token(lexema, Categoria.OPERADOR_ARTIMETICO, fila, columna));
            return true;
        } else {
            hacerBT(posInicial, fila, columna);
            return false;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un punto
     * Expresión regular= (.)
     *
     * @return verdadero si cumple la condicion, falso en otro caso.
     */
    private boolean esPunto() {
        //RI
        if ((caracterActual != '.')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();
        if (Character.isDigit(caracterActual)) {
            hacerBT(posInicial, fila, columna);
            return false;
        } else {
            listaTokens.add(new Token(lexema, Categoria.PUNTO, fila, columna));
            return true;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un separador
     * Expresión regular= (,)
     *
     * @return verdadero si cumple la condicion, falso en otro caso.
     */
    private boolean esSeparador() {
        //RI
        if ((caracterActual != ',')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();
        listaTokens.add(new Token(lexema, Categoria.SEPARADOR, fila, columna));
        return true;
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un separador
     * Expresión regular= (;)
     *
     * @return verdadero si cumple la condicion, falso en otro caso.
     */
    private boolean esFinDeSentencia() {
        //RI
        if ((caracterActual != ';')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();
        listaTokens.add(new Token(lexema, Categoria.FIN_DE_SENTENCIA, fila, columna));
        return true;

    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de dos puntos
     * Expresión regular= (:)
     *
     * @return verdadero si cumple la condicion, falso en otro caso.
     */
    private boolean esDosPuntos() {
        //RI
        if ((caracterActual != ':')) {
            return false;
        }

        String lexema = "";
        int fila = filaActual, columna = columnaActual, posInicial = posActual;
        lexema += caracterActual;
        obtenerSgteCaracter();
        if (caracterActual == '<' || caracterActual == '>' || caracterActual == '='
                || caracterActual == '|' || caracterActual == '&' || caracterActual == '$'
                || caracterActual == '!' || caracterActual == '+' || caracterActual == '-'
                || caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
            hacerBT(posInicial, fila, columna);
            return false;
        } else {
            listaTokens.add(new Token(lexema, Categoria.DOS_PUNTOS, fila, columna));
            return true;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de una Cadena
     * de Caracteres Expresion regualar = ($)(COS)(COS)*($)
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esCadenaCaracteres() {

        if (caracterActual == '$') {
            String lexema = "";
            int ultimaPos = posActual;
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();

            while (caracterActual != '$' && posActual != (codigoFuente.length() - 1)) {
                //Transición
                lexema += caracterActual;
                obtenerSgteCaracter();

                if (caracterActual == '\\') {
                    //Transición
                    lexema += caracterActual;
                    obtenerSgteCaracter();
                    if (caracterActual != 'n' && caracterActual != 't' && caracterActual != '$' && caracterActual != '\\') {
                        //Transición
                        lexema += caracterActual;
                        obtenerSgteCaracter();
                        listaTokens.add(new Token(lexema, Categoria.ERROR_CADENA_CARACTERES, fila, columna));
                        return true;
                    } else {
                        if (caracterActual == '$') {
                            //Transición
                            lexema += caracterActual;
                            obtenerSgteCaracter();
                            continue;
                        }
                        continue;
                    }
                }

                if (caracterActual == finCodigo) {
                    posActual = ultimaPos;
                    caracterActual = codigoFuente.charAt(posActual);
                    return false;
                }
            }

            if (caracterActual == '$' && lexema.length() != 1) //Transición
            {
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.CADENA_CARACTERES, fila, columna));
                return true;

            } else if (caracterActual == '$' && lexema.length() == 1) {
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.CADENA_CARACTERES, fila, columna));
                return true;
            } else {
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.ERROR_CADENA_CARACTERES, fila, columna));
                return true;
            }

        } else {
            return false;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Comentario de Bloque Expresion regualar = (#)(*)(COS)*(*)($)
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esComentarioBloque() {

        //RI
        if (caracterActual == '#') {
            String lexema = "";
            int posInicial = posActual;
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();

            if (caracterActual == finCodigo) {
                //Transición
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.ERROR_COMENTARIO_BLOQUE, fila, columna));
                return true;
            } else if (caracterActual == '*') {

                while (posActual != (codigoFuente.length() - 1)) {
                    //Transición
                    lexema += caracterActual;
                    obtenerSgteCaracter();

                    if (caracterActual == '*') {
                        //Transición
                        lexema += caracterActual;
                        obtenerSgteCaracter();
                        if (caracterActual == '#') {
                            //Transición
                            lexema += caracterActual;
                            obtenerSgteCaracter();
                            listaTokens.add(new Token(lexema, Categoria.COMENTARIO_BLOQUE, fila, columna));
                            return true;
                        }
                    }

                    if (caracterActual == finCodigo) {
                        lexema += caracterActual;
                        obtenerSgteCaracter();
                        listaTokens.add(new Token(lexema, Categoria.ERROR_COMENTARIO_BLOQUE, fila, columna));
                        return true;
                    }
                }

                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.ERROR_COMENTARIO_BLOQUE, fila, columna));
                return true;

            } else {
                hacerBT(posInicial, fila, columna);
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un
     * Comentario Expresion regualar = (#)(#)(COS)*
     *
     * @return verdadero si cumple la condición, falso en otro caso
     */
    public boolean esComentarioLinea() {
        //RI
        if (caracterActual == '#') {
            String lexema = "";
            int posInicial = posActual;
            int fila = filaActual;
            int columna = columnaActual;

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();

            if (caracterActual == finCodigo) {
                //Transición
                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.ERROR_COMENTARIO_LINEA, fila, columna));
                return true;
            } else if (caracterActual == '#') {

                while (posActual != (codigoFuente.length() - 1) && caracterActual != '\n') {
                    //Transición
                    lexema += caracterActual;
                    obtenerSgteCaracter();

                }

                lexema += caracterActual;
                obtenerSgteCaracter();
                listaTokens.add(new Token(lexema, Categoria.COMENTARIO_LINEA, fila, columna));
                return true;

            } else {
                hacerBT(posInicial, fila, columna);
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Determina si la secuencia de caracteres cumple con un AFD de un Operador
     * Relacional Expresión regular= (:) ((< U >) U (< U >)(=) U (| U =)(=))
     *
     * @return true si cumple la condicion, false en otro caso.
     */
    private boolean esOperadorRelacional() {

        if (caracterActual == ':') {

            String lexema = "";
            int posInicial = posActual, fila = filaActual, columna = columnaActual;

            //Transición
            lexema += caracterActual;
            obtenerSgteCaracter();

            if (caracterActual == finCodigo) {
                hacerBT(posInicial, fila, columna);
                return false;
            } else if (caracterActual == '<' || caracterActual == '>') {

                //Transición
                lexema += caracterActual;
                obtenerSgteCaracter();

                if (caracterActual == '=') {
                    //Transición
                    lexema += caracterActual;
                    obtenerSgteCaracter();
                }
                listaTokens.add(new Token(lexema, Categoria.OPERADOR_RELACIONAL, fila, columna));
                return true;
            } else if (caracterActual == '|' || caracterActual == '=') {

                //Transición
                lexema += caracterActual;
                obtenerSgteCaracter();
                if (caracterActual == finCodigo) {
                    hacerBT(posInicial, fila, columna);
                    return false;
                } else if (caracterActual == '=') {
                    //Transición
                    lexema += caracterActual;
                    obtenerSgteCaracter();
                    listaTokens.add(new Token(lexema, Categoria.OPERADOR_RELACIONAL, fila, columna));
                    return true;
                } else {
                    hacerBT(posInicial, fila, columna);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Metodo que obtiene el siguiente caractar del codigo fuente
     */
    public void obtenerSgteCaracter() {
        posActual++;
        if (posActual < codigoFuente.length()) {

            if (caracterActual == '\n') {
                filaActual++;
                columnaActual = 0;
            } else {
                columnaActual++;
            }

            caracterActual = codigoFuente.charAt(posActual);
        } else {
            caracterActual = finCodigo;
        }

    }

    public void hacerBT(int posInicial, int fila, int columna) {
        posActual = posInicial;
        filaActual = fila;
        columnaActual = columna;
        caracterActual = codigoFuente.charAt(posInicial);
    }

    /**
     * Metodo que devuelve la lista de tokens
     *
     * @return
     */
    public ArrayList<Token> getListaTokens() {
        return listaTokens;
    }

    /**
     * Metodo que inserta la lista de tokens
     *
     */
    public void setListaTokens(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
    }

}
