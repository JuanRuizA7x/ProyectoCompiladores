package Sintactico;

import java.util.ArrayList;
import Lexico.Categoria;
import Lexico.Token;

/**
 * Analizador Sintáctico del compilador
 *
 * @author Juan Ruiz
 * @author Juan Pinzon
 */
public class AnalizadorSintactico {

    private ArrayList<Token> listaTokens;
    private int posActual;
    private Token tokenActual;
    private ArrayList<Error> listaErrores;

    public AnalizadorSintactico(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
        this.tokenActual = listaTokens.get(posActual);
        this.listaErrores = new ArrayList<>();
    }

    public void obtenerSiguienteToken() {
        posActual++;
        if (posActual < listaTokens.size()) {
            tokenActual = listaTokens.get(posActual);
        } else {
            tokenActual.setPalabra("¿");
            tokenActual.setCategoria(Categoria.DESCONOCIDO);
        }
    }

    public void hacerBT(int posInicial) {
        posActual = posInicial;
        tokenActual = listaTokens.get(posActual);
    }

    public void reportarError(String mensaje) {
        listaErrores.add(new Error(mensaje, tokenActual.getFila(), tokenActual.getColumna()));
    }

    /**
     * <UnidadDeCompilacion> ::= <ListaFunciones>
     * @return 
     */
    public UnidadDeCompilacion esUnidadDeCompilacion() {
        ArrayList<Funcion> f = esListaFunciones();

        if (f != null) {
            UnidadDeCompilacion u = new UnidadDeCompilacion(f);
            return u;
        }

        return null;
    }

    /**
     * <ListaFunciones> ::= <Funcion>[<ListaFunciones>]
     */
    public ArrayList<Funcion> esListaFunciones() {

        ArrayList<Funcion> lista = new ArrayList<>();
        Funcion f = esFuncion();

        while (f != null) {
            lista.add(f);
            f = esFuncion();
        }

        return lista;
    }

    /**
     * <Funcion> ::= funcion <TipoRetorno> identificador
     * "("[<ListaParametros>]")"
     * <BloqueSentencias>
     */
    public Funcion esFuncion() {

        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("funcion")) {
            obtenerSiguienteToken();

            Token tipoRetorno;
            if (esTipoRetorno() != null) {
                tipoRetorno = esTipoRetorno();
                obtenerSiguienteToken();

                if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
                    Token nombre = tokenActual;
                    obtenerSiguienteToken();

                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                        obtenerSiguienteToken();

                        ArrayList<Parametro> parametros = esListaParametros();

                        if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                            obtenerSiguienteToken();

                            BloqueSentencias bloqueSentencias = esBloqueSentencias();

                            if (bloqueSentencias != null) {
                                return new Funcion(tipoRetorno, nombre, parametros, bloqueSentencias);
                            } else {
                                reportarError("Faltó el bloque de sentencias de la función " + nombre.getPalabra());
                            }

                        } else {
                            reportarError("Faltó paréntesis cerrado de la función " + nombre.getPalabra());
                        }
                    } else {
                        reportarError("Faltó paréntesis abierto de la función " + nombre.getPalabra());
                    }

                } else {
                    reportarError("Faltó el nombre de la función");
                }
            } else {
                reportarError("Faltó el tipo de retorno de la función");
            }
        }
        return null;
    }

    /**
     * <BloqueSentencias> ::= "{" <ListaSentencias> "}"
     */
    public BloqueSentencias esBloqueSentencias() {

        if (tokenActual.getCategoria() == Categoria.LLAVE_ABIERTA) {
            obtenerSiguienteToken();
            ArrayList<Sentencia> sentencias = esListaSentencias();

            if (!sentencias.isEmpty()) {
                if (tokenActual.getCategoria() == Categoria.LLAVE_CERRADA) {
                    obtenerSiguienteToken();
                    return new BloqueSentencias(sentencias);

                } else {
                    reportarError("Falta llave cerrada");
                }
            } else {
                reportarError("Falta la lista de sentencias");
            }
        }
        return null;
    }

    /**
     * <ListaSentencias> ::= <Sentencia>[<ListaSentencias>]
     */
    public ArrayList<Sentencia> esListaSentencias() {
        ArrayList<Sentencia> lista = new ArrayList<>();
        Sentencia s = esSentencia();

        while (s != null) {
            lista.add(s);
            s = esSentencia();
        }

        return lista;
    }

    /**
     * <Sentencia> ::= <Si>|<Declaracion>|<Asignacion>|<Impresion>|
     * <Mientras>|<Retorno>|<LecturaDatos>|<InvocarFuncion>|<Arreglo>|
     * <Romper>|<Incremento>|<Decremento>
     * @return 
     */
    public Sentencia esSentencia() {
        Sentencia s = esSi();
        if (s != null) {
            return s;
        }
        s = esDeclaracion();
        if (s != null) {
            return s;
        }
        s = esAsignacion();
        if (s != null) {
            return s;
        }
        s = esImpresion();
        if (s != null) {
            return s;
        }
        s = esMientras();
        if (s != null) {
            return s;
        }
        s = esRetorno();
        if (s != null) {
            return s;
        }
        s = esInvocacionFuncion();
        if (s != null) {
            obtenerSiguienteToken();
            return s;
        }
        s = esArreglo();
        if (s != null) {
            return s;
        }
        s = esRomper();
        if (s != null) {
            return s;
        }
        s = esIncremento();
        if (s != null) {
            return s;
        }
        s = esDecremento();
        if (s != null) {
            return s;
        }

        return null;
    }

    /**
     * <Si> ::= Si"("<ExpresionLogica>")"<BloqueSentencias>[sino
     * <BloqueSentencias>]
     */
    public Si esSi() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("si")) {
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                obtenerSiguienteToken();

                ExpresionLogica exp;

                exp = esExpresionLogica();

                if (exp != null) {
                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {

                        obtenerSiguienteToken();

                        BloqueSentencias bloqueSentenciasSi = esBloqueSentencias();

                        if (bloqueSentenciasSi != null) {

                            return new Si(exp, bloqueSentenciasSi);

                        } else {
                            reportarError("Faltó el bloque de sentencias \"Si\"");
                        }

                    } else {
                        reportarError("Faltó el parénteris cerrado");
                    }

                } else {
                    reportarError("Faltó la expresión lógica");
                }

            } else {
                reportarError("Faltó el parénteris abierto");
            }
        }
        return null;
    }

    /**
     * <Declaracion> ::= TipoDato identificador ["=" <Expresion>]";"| TipoDato
     * identificador ["=" <InvocarFuncion>]";"| TipoDato identificador ["="
     * <LecturaDatos>]";"
     */
    public Sentencia esDeclaracion() {
        int posInicial = posActual;
        Token tipoDato;
        tipoDato = esTipoDato();
        if (tipoDato != null) {
            obtenerSiguienteToken();
            if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
                Token nombre = tokenActual;
                obtenerSiguienteToken();
                if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION && tokenActual.getPalabra().equals("|=")) {
                    obtenerSiguienteToken();
                    Expresion exp;
                    exp = esExpresion();
                    if (exp != null) {
                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();
                            return new Declaracion(tipoDato, nombre, exp);
                        } else {
                            reportarError("Faltó el fin de sentencia");
                        }
                    } else {
                        InvocacionFuncion invFun;
                        invFun = (InvocacionFuncion) esInvocacionFuncion();
                        if (invFun != null) {
                            if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                                obtenerSiguienteToken();
                                return new Declaracion(tipoDato, nombre, invFun);
                            } else {
                                reportarError("Faltó el fin de sentencia");
                            }
                        } else {
                            LecturaDeDatos lecDatos;
                            lecDatos = (LecturaDeDatos) esLecturaDeDatos();
                            if (lecDatos != null) {
                                if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                                    obtenerSiguienteToken();
                                    return new Declaracion(tipoDato, nombre, lecDatos);
                                } else {
                                    reportarError("Faltó el fin de sentencia");
                                }
                            } else {
                                reportarError("Faltó la asignación del identificador");
                            }
                        }
                    }
                } else if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                    obtenerSiguienteToken();
                    return new Declaracion(tipoDato, nombre);
                } else if (tokenActual.getCategoria() == Categoria.SEPARADOR) {
                    hacerBT(posInicial);
                } else {
                    reportarError("Faltó el fin de sentencia");
                }
            } else {
                reportarError("Faltó el identificador");
            }
        }
        return null;
    }

    /**
     * <Asignacion> ::= Identificador OpAsignacion <Expresion> ";"|
     * Identificador OpAsignacion <InvFuncion> ";"| Identificador OpAsignacion
     * <LecDatos> ";"
     */
    public Asignacion esAsignacion() {
        int posInicial = posActual;
        if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token nombre = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION) {
                Token operadorAsignacion = tokenActual;
                obtenerSiguienteToken();

                Expresion exp;
                exp = esExpresion();

                if (exp != null) {

                    if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                        obtenerSiguienteToken();

                        return new Asignacion(nombre, operadorAsignacion, exp);

                    } else {
                        reportarError("Faltó el fin de sentencia");
                    }
                } else {
                    InvocacionFuncion fun;
                    fun = esInvocacionFuncion();

                    if (fun != null) {

                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();

                            return new Asignacion(nombre, operadorAsignacion, fun);

                        } else {
                            reportarError("Faltó el fin de sentencia");
                        }
                    } else {
                        LecturaDeDatos lec;
                        lec = (LecturaDeDatos) esLecturaDeDatos();

                        if (lec != null) {

                            if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                                obtenerSiguienteToken();

                                return new Asignacion(nombre, operadorAsignacion, lec);

                            } else {
                                reportarError("Faltó el fin de sentencia");
                            }
                        } else {
                            reportarError("Faltó la asignación del identificador");
                        }
                    }
                }
            } else {
                hacerBT(posInicial);
            }
        }
        return null;
    }

    /**
     * <LecturaDeDatos> ::= leer "(" CadenaCaracteres ")"
     */
    public Sentencia esLecturaDeDatos() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("leer")) {
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                obtenerSiguienteToken();

                if (tokenActual.getCategoria() == Categoria.CADENA_CARACTERES) {
                    Token cadena = tokenActual;
                    obtenerSiguienteToken();

                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                        obtenerSiguienteToken();

                        return new LecturaDeDatos(cadena);
                    } else {
                        reportarError("Faltó el paréntesis cerrado");
                    }
                } else {
                    reportarError("Faltó la cadena de caracteres");
                }
            } else {
                reportarError("Faltó el paréntesis abierto");
            }
        }
        return null;
    }

    /**
     * <Impresion> ::= imprirmir "(" Identificador ")" ";" | imprirmir "("
     * CadenaCaracteres ")" ";" | imprirmir "(" <InvocacionFuncion> ")" ";"
     */
    public Impresion esImpresion() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("imprimir")) {
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                obtenerSiguienteToken();

                InvocacionFuncion inv;
                inv = esInvocacionFuncion();

                if (inv != null) {
                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                        obtenerSiguienteToken();

                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();

                            return new Impresion(inv);
                        } else {
                            reportarError("Faltó el fin de sentencia");
                        }

                    } else {
                        reportarError("Faltó el paréntesis cerrado");
                    }
                } else if (tokenActual.getCategoria() == Categoria.CADENA_CARACTERES) {
                    Token cadenaCaracteres = tokenActual;
                    obtenerSiguienteToken();

                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                        obtenerSiguienteToken();

                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();

                            return new Impresion(cadenaCaracteres);
                        } else {
                            reportarError("Faltó el fin de sentencia");
                        }
                    } else {
                        reportarError("Faltó el paréntesis cerrado");
                    }
                } else if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {

                    Token identificador = tokenActual;
                    obtenerSiguienteToken();

                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                        obtenerSiguienteToken();

                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();

                            return new Impresion(identificador);
                        } else {
                            reportarError("Faltó el fin de sentencia");
                        }
                    } else {
                        reportarError("Faltó el paréntesis cerrado");
                    }
                } else {
                    reportarError("Faltó la expresión a imprimir");
                }
            } else {
                reportarError("Faltó el paréntesis abierto");
            }
        }

        return null;
    }

    /**
     * <Mientras> ::= mientras "(" <ExpresionLogica> ")" <BloqueSentencias>
     */
    public Mientras esMientras() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("mientras")) {
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                obtenerSiguienteToken();

                ExpresionLogica exp;
                exp = esExpresionLogica();

                if (exp != null) {

                    if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                        obtenerSiguienteToken();

                        BloqueSentencias bloque;
                        bloque = esBloqueSentencias();

                        if (bloque != null) {
                            return new Mientras(exp, bloque);
                        } else {
                            reportarError("Faltó el bloque de sentencias");
                        }
                    } else {
                        reportarError("Faltó el paréntesis cerrado");
                    }
                } else {
                    reportarError("Faltó la expresión lógica");
                }

            } else {
                reportarError("Faltó el paréntesis abierto");
            }
        }
        return null;
    }

    /**
     * <Retorno> ::= retornar <Expresion> ";" | retornar <identificador> ";" |
     * retornar nulo ";"
     */
    public Sentencia esRetorno() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("retornar")) {
            obtenerSiguienteToken();
            Expresion exp;
            exp = esExpresion();

            if (exp != null) {
                if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                    obtenerSiguienteToken();
                    return new Retorno(exp);
                } else {
                    reportarError("Faltó el fin de sentencia");
                }
            } else if ((tokenActual.getCategoria() == Categoria.IDENTIFICADOR) || (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("nulo"))) {
                Token token = tokenActual;
                obtenerSiguienteToken();
                if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                    obtenerSiguienteToken();
                    return new Retorno(token);
                } else {
                    reportarError("Faltó el fin de sentencia");
                }
            } else {
                reportarError("Faltó el retorno");
            }
        }
        return null;
    }

    /**
     * <InvocacionFuncion> ::= Identificador "("[ <ListaArgumentos> ] ")" [";"]
     */
    public InvocacionFuncion esInvocacionFuncion() {
        int posInicial = posActual;
        if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token identificador = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
                obtenerSiguienteToken();

                ArrayList<Argumento> argumentos = esListaArgumentos();

                if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                    obtenerSiguienteToken();

                    int pos = posActual;
                    if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                        obtenerSiguienteToken();

                        if (tokenActual.getCategoria() != Categoria.FIN_DE_SENTENCIA) {
                            hacerBT(pos);
                        }                     
                    }
                    return new InvocacionFuncion(identificador, argumentos);
                } else {
                    reportarError("Faltó el paréntesis cerrado");
                }

            } else {
                hacerBT(posInicial);
            }
        }
        return null;
    }

    /**
     * <ListaArgumentos> ::= <Argumento>[","<ListaArgumentos>]
     */
    public ArrayList<Argumento> esListaArgumentos() {
        ArrayList<Argumento> lista = new ArrayList<>();
        Argumento a = esArgumento();

        while (a != null) {
            lista.add(a);
            if (tokenActual.getCategoria() == Categoria.SEPARADOR) {
                obtenerSiguienteToken();
                a = esArgumento();
            } else {
                return lista;
            }
        }

        return lista;
    }

    /**
     * <Argumento> ::= <Expresion> | Identificador
     */
    public Argumento esArgumento() {
        int posInicial = posActual;
        if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token identificador = tokenActual;
            obtenerSiguienteToken();
            if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION
                    || tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO
                    || tokenActual.getCategoria() == Categoria.OPERADOR_INCREMENTO
                    || tokenActual.getCategoria() == Categoria.OPERADOR_DECREMENTO) {
                hacerBT(posInicial);
            } else {
                return new Argumento(identificador);
            }
        } else {
            Expresion exp;
            exp = esExpresion();

            if (exp != null) {
                return new Argumento(exp);
            }
        }

        return null;
    }

    /**
     * <TipoDato> ::= entero | real | booleano | cadena
     */
    public Token esTipoDato() {

        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA
                && (tokenActual.getPalabra().equals("entero")
                || tokenActual.getPalabra().equals("real")
                || tokenActual.getPalabra().equals("booleano")
                || tokenActual.getPalabra().equals("cadena"))) {
            return tokenActual;
        }

        return null;
    }

    /**
     * <TipoRetorno> ::= entero | real | booleano | vacio | cadena
     */
    public Token esTipoRetorno() {

        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA
                && (tokenActual.getPalabra().equals("entero")
                || tokenActual.getPalabra().equals("real")
                || tokenActual.getPalabra().equals("booleano")
                || tokenActual.getPalabra().equals("vacio")
                || tokenActual.getPalabra().equals("cadena"))) {
            return tokenActual;
        }

        return null;
    }

    /**
     * <Expresion> ::=
     * <ExpresionRelacional>|<ExpresionLogica>|<ExpresionAritmetica>|
     * <ExpresionCadena>
     */
    public Expresion esExpresion() {
        int posInicial = posActual;

        Expresion exp = esExpresionAritmetica();
        if (exp != null) {
            if (tokenActual.getCategoria() != Categoria.OPERADOR_RELACIONAL) {
                return exp;
            } else {
                hacerBT(posInicial);
            }
        }

        exp = esExpresionRelacional();
        if (exp != null) {
            if (tokenActual.getCategoria() != Categoria.OPERADOR_LOGICO) {
                return exp;
            } else {
                hacerBT(posInicial);
            }
        }
        exp = esExpresionLogica();
        if (exp != null) {
            return exp;
        }
        exp = esExpresionCadena();
        if (exp != null) {
            return exp;
        }
        return null;
    }

    /**
     * <ExpresionRelacional> ::= "(" <ExpresionRelacional> ")" |
     * <ExpresionAritmetica> opRelacional <ExpresionAritmetica> | verdadero |
     * falso
     */
    public Expresion esExpresionRelacional() {
        int posInicial = posActual;

        ExpresionAritmetica expAt;
        expAt = (ExpresionAritmetica) esExpresionAritmetica();
        if (expAt != null) {
            if (tokenActual.getCategoria() == Categoria.OPERADOR_RELACIONAL) {
                Token opeRel = tokenActual;
                obtenerSiguienteToken();
                ExpresionAritmetica expAt2;
                expAt2 = (ExpresionAritmetica) esExpresionAritmetica();
                if (expAt2 != null) {
                    return new ExpresionRelacional(expAt, expAt2, opeRel);
                } else {
                    reportarError("Faltó la segunda expresión aritmetica");
                }
            } else {
                reportarError("Faltó el operador relacional");
            }
        } else if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
            obtenerSiguienteToken();
            ExpresionRelacional exp;
            exp = (ExpresionRelacional) esExpresionRelacional();
            if (exp != null) {
                if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                    obtenerSiguienteToken();
                    return new ExpresionRelacional(exp);
                } else {
                    reportarError("Faltó el paréntesis cerrado");
                }
            } else {
                hacerBT(posInicial);
            }
        } else if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && (tokenActual.getPalabra().equals("verdadero") || tokenActual.getPalabra().equals("falso"))) {
            Token estado = tokenActual;
            obtenerSiguienteToken();
            return new ExpresionRelacional(estado);
        }
        return null;
    }

    /**
     * <ExpresionCadena> ::= CadenaCaracteres [":""+" <Expresion>] | "("<ExpresionCadena>")"
     */
    public Expresion esExpresionCadena() {
        if (tokenActual.getCategoria() == Categoria.CADENA_CARACTERES) {
            Token cadena = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.OPERADOR_ARTIMETICO && tokenActual.getPalabra().equals(":+")) {

                obtenerSiguienteToken();
                Expresion exp;
                exp = esExpresion();
                if (exp != null) {
                    return new ExpresionCadena(cadena, exp);
                } else {
                    reportarError("Faltó una Expresión");
                }
            }
            return new ExpresionCadena(cadena);
        } else if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
            obtenerSiguienteToken();
            ExpresionCadena exp;
            exp = (ExpresionCadena) esExpresionCadena();
            if (exp != null) {
                if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                    obtenerSiguienteToken();
                    return new ExpresionCadena(exp);
                } else {
                    reportarError("Faltó el paréntesis cerrado");
                }
            } else {
                reportarError("Faltó la expresión");
            }
        }
        return null;
    }

    /**
     * <ExpresionAritmetica> ::= "(" <ExpresionAritmetica> ")"
     * [<ExpresionAuxiliar>] | <ValorNumerico> [<ExpresionAuxiliar>] |
     * Identificador [<ExpresionAuxiliar>]
     */
    public Expresion esExpresionAritmetica() {

        int posInicial = posActual;
        if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
            obtenerSiguienteToken();
            ExpresionAritmetica exp;
            exp = (ExpresionAritmetica) esExpresionAritmetica();
            if (exp != null) {
                if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                    obtenerSiguienteToken();
                    ExpresionAuxiliar aux;
                    aux = (ExpresionAuxiliar) esExpresionAuxiliar();
                    return new ExpresionAritmetica(exp, aux);

                } else if (tokenActual.getCategoria() == Categoria.OPERADOR_RELACIONAL) {
                    hacerBT(posInicial);
                } else {
                    reportarError("Faltó el paréntesis cerrado");
                }
            } else {
                hacerBT(posInicial);
            }
        } else if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token identificador = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION
                    || tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO
                    || tokenActual.getCategoria() == Categoria.SEPARADOR) {
                hacerBT(posInicial);

            } else {
                ExpresionAuxiliar aux;
                aux = (ExpresionAuxiliar) esExpresionAuxiliar();
                return new ExpresionAritmetica(identificador, aux);
            }

        } else {
            ValorNumerico valor;
            valor = esValorNumerico();
            if (valor != null) {
                ExpresionAuxiliar aux;
                aux = (ExpresionAuxiliar) esExpresionAuxiliar();
                return new ExpresionAritmetica(valor, aux);
            }
        }
        return null;
    }

    /**
     * <ExpresionAuxiliar> ::= OperadorAritmetico <ExpresionAritmetica>
     * [<ExpresionAuxiliar>]
     */
    public Expresion esExpresionAuxiliar() {

        if (tokenActual.getCategoria() == Categoria.OPERADOR_ARTIMETICO) {
            Token operador = tokenActual;
            obtenerSiguienteToken();
            ExpresionAritmetica exp;
            exp = (ExpresionAritmetica) esExpresionAritmetica();

            if (exp != null) {
                ExpresionAuxiliar expAux;
                expAux = (ExpresionAuxiliar) esExpresionAuxiliar();
                return new ExpresionAuxiliar(operador, exp, expAux);
            } else {
                reportarError("Faltó la expresión aritmética");
            }
        }
        return null;
    }

    /**
     * <ValorNumerico> ::= [":""-"] Entero | [":""-"] Real
     */
    public ValorNumerico esValorNumerico() {

        Token signo = null;

        if (tokenActual.getCategoria() == Categoria.OPERADOR_ARTIMETICO && tokenActual.getPalabra().equals(":-")) {
            signo = tokenActual;
            obtenerSiguienteToken();
            if (tokenActual.getCategoria() == Categoria.ENTERO || tokenActual.getCategoria() == Categoria.REAL) {
                Token valor = tokenActual;
                obtenerSiguienteToken();
                return new ValorNumerico(signo, valor);
            } else {
                reportarError("Faltó el valor numerico");
            }
        } else if (tokenActual.getCategoria() == Categoria.ENTERO || tokenActual.getCategoria() == Categoria.REAL) {
            Token valor = tokenActual;
            obtenerSiguienteToken();
            return new ValorNumerico(signo, valor);
        }
        return null;
    }

    /**
     * <ListaParametros> ::= <Parametro> ["," <ListaParametros>]
     */
    public ArrayList<Parametro> esListaParametros() {

        ArrayList<Parametro> lista = new ArrayList<>();
        Parametro p = esParametro();

        while (p != null) {
            lista.add(p);
            if (tokenActual.getCategoria() == Categoria.SEPARADOR) {
                obtenerSiguienteToken();
                p = esParametro();
            } else {
                return lista;
            }
        }

        return lista;
    }

    /**
     * <Parametro> ::= TipoDato Identificador
     */
    public Parametro esParametro() {
        int posInicial = posActual;
        Token tipoDato;
        tipoDato = esTipoDato();

        if (tipoDato != null) {
            tipoDato = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
                Token identificador = tokenActual;
                obtenerSiguienteToken();
                if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION && tokenActual.getPalabra().equals("|=")) {
                    hacerBT(posInicial);
                } else {
                    return new Parametro(tipoDato, identificador);
                }

            } else {
                reportarError("Faltó el identificador");
            }

        }
        return null;
    }

    /**
     * <ExpresionLogica> ::= ":""~" <ExpresionRelacional> |
     * <ExpresionRelacional> OperadorLogico <ExpresionRelacional> |
     * <ExpresionRelacional>
     */
    public ExpresionLogica esExpresionLogica() {

        int posInicial = posActual;

        ExpresionRelacional expR1;
        expR1 = (ExpresionRelacional) esExpresionRelacional();

        if (expR1 != null) {
            if (tokenActual.getCategoria() == Categoria.OPERADOR_LOGICO && !tokenActual.getPalabra().equals(":~")) {
                Token operador = tokenActual;
                obtenerSiguienteToken();
                ExpresionRelacional expR2;
                expR2 = (ExpresionRelacional) esExpresionRelacional();
                if (expR2 != null) {
                    return new ExpresionLogica(expR1, operador, expR2);
                } else {
                    reportarError("Faltó la segunda expresión relacional");
                }
            } else {
                return new ExpresionLogica(expR1);
            }
        } else if (tokenActual.getCategoria() == Categoria.PARENTESIS_ABIERTO) {
            obtenerSiguienteToken();
            ExpresionLogica exp;
            exp = (ExpresionLogica) esExpresionLogica();
            if (exp != null) {
                if (tokenActual.getCategoria() == Categoria.PARENTESIS_CERRADO) {
                    obtenerSiguienteToken();
                    return new ExpresionLogica(exp);
                } else {
                    reportarError("Faltó el paréntesis cerrado");
                }
            } else {
                hacerBT(posInicial);
            }
        } else if (tokenActual.getCategoria() == Categoria.OPERADOR_LOGICO && tokenActual.getPalabra().equals(":~")) {
            Token negacion = tokenActual;
            obtenerSiguienteToken();
            ExpresionLogica expL;
            expL = (ExpresionLogica) esExpresionLogica();

            if (expL != null) {
                return new ExpresionLogica(negacion, expL);
            } else {
                reportarError("Faltó la expresión lógica");
            }
        }
        return null;
    }

    /**
     * <Arreglo> ::= arreglo TipoDato Identificador ["|""=" ""{"
     * <ListaArgumentos> "}"] ";"
     */
    public Arreglo esArreglo() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("arreglo")) {
            obtenerSiguienteToken();

            Token tipoDato;
            tipoDato = esTipoDato();

            if (tipoDato != null) {
                obtenerSiguienteToken();

                if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
                    Token identificador = tokenActual;
                    obtenerSiguienteToken();

                    if (tokenActual.getCategoria() == Categoria.OPERADOR_ASIGNACION && tokenActual.getPalabra().equals("|=")) {
                        obtenerSiguienteToken();

                        if (tokenActual.getCategoria() == Categoria.LLAVE_ABIERTA) {
                            obtenerSiguienteToken();

                            ArrayList<Argumento> argumentos;
                            argumentos = esListaArgumentos();

                            if (!argumentos.isEmpty()) {

                                if (tokenActual.getCategoria() == Categoria.LLAVE_CERRADA) {
                                    obtenerSiguienteToken();

                                    if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                                        obtenerSiguienteToken();

                                        return new Arreglo(tipoDato, identificador, argumentos);
                                    } else {
                                        reportarError("Faltó fin de sentencia");
                                    }
                                } else {
                                    reportarError("Faltó llave cerrada");
                                }

                            } else {
                                reportarError("Faltó lista de argumentos");
                            }
                        } else {
                            reportarError("Faltó llave abierta");
                        }
                    } else {
                        if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                            obtenerSiguienteToken();
                            return new Arreglo(tipoDato, identificador);
                        } else {
                            reportarError("Faltó fin de sentencia");
                        }
                    }
                } else {
                    reportarError("Faltó identificador");
                }
            } else {
                reportarError("Faltó tipo de dato");
            }

        }
        return null;
    }

    /**
     *
     */
    public Romper esRomper() {
        if (tokenActual.getCategoria() == Categoria.PALABRA_RESERVADA && tokenActual.getPalabra().equals("romper")) {
            Token token = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                obtenerSiguienteToken();

                return new Romper();
            } else {
                reportarError("Faltó fin de sentencia");
            }
        }

        return null;
    }

    /**
     *
     */
    public Incremento esIncremento() {
        int posInicial = posActual;
        if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token token = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.OPERADOR_INCREMENTO) {
                Token incremento = tokenActual;
                obtenerSiguienteToken();

                if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                    obtenerSiguienteToken();

                    return new Incremento(token, incremento);
                } else {
                    reportarError("Faltó el fin de sentencia");
                }

            } else {
                hacerBT(posInicial);
            }
        }

        return null;
    }

    /**
     *
     */
    public Decremento esDecremento() {
        if (tokenActual.getCategoria() == Categoria.IDENTIFICADOR) {
            Token token = tokenActual;
            obtenerSiguienteToken();

            if (tokenActual.getCategoria() == Categoria.OPERADOR_DECREMENTO) {
                Token decremento = tokenActual;
                obtenerSiguienteToken();

                if (tokenActual.getCategoria() == Categoria.FIN_DE_SENTENCIA) {
                    obtenerSiguienteToken();

                    return new Decremento(token, decremento);
                } else {
                    reportarError("Faltó el fin de sentencia");
                }
            } else {
                reportarError("Falltó el operador del identificador");
            }
        }

        return null;
    }

    public ArrayList<Error> getListaErrores() {
        return listaErrores;
    }

}
