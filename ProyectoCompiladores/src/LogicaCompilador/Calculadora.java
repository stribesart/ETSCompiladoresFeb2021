/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCompilador;

/**
 *
 * @author sandu
 */
/**
 * Calculadora
 */
public class Calculadora {

    // --------------------Constantes provisionales----------------------
    public final int MAS = 10;
    public final int MENOS = 20;
    public final int PROD = 30;
    public final int DIV = 40;
    public final int PAR_I = 50;
    public final int PAR_D = 60;
    public final int NUM = 70;
    public final int FIN = 1000;

    // --------------------Constantes provisionales----------------------

    //clase interna utilizada para guardar los estados del valor numerico en las funciones recursivas 
    public class Numero {
        private float valor;
        /**
         * @param valor actualizacion de valor numerico
         */
        public void setValor(float valor) {
            this.valor = valor;
        }

        /**
         * 
         * @return valor actual 
         */
        public float getValor() {
            return this.valor;
        }

        public Numero() {
            this.valor = 0f;
        }

    }

    private final Numero resultado = new Numero();
    private AnalizadorLexico lex;
    //se crea clase donde se simula el comportamiento del analizador lexico
    //private LexTest lex;
    public Calculadora(AnalizadorLexico lex) {
    //public Calculadora(LexTest lex) {    
        this.lex = lex;
    }


    /**
     * Estado inicial de la gramatica
     * @param v en esta variable se iran guardando los resultados cada que se haga la evaluacion de una operacion 
     * @return retorna verdadero si la navegacion sobre el arbol de sintaxis fue correcta
     * 
     */
    public boolean E(Numero v) {
        if (T(v)) {
            if (Ep(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dentro de esta regla se avanza en el arbol de sintaxis y tambien se evaluan valores numericos encontrados como lexemas
     * @param v en esta variable se iran guardando los resultados cada que se haga la evaluacion de una operacion 
     * @return valor booleano que indica si se encontro una suma o resta o epsilon
     */
    public boolean Ep(Numero v) {
        int token;
        token = this.lex.getToken();
        Numero v1 = new Numero();
        if (token == MAS) {
            if (T(v1)) {
                v.setValor(v.getValor() + v1.getValor());
                if (Ep(v)) {
                    return true;
                }
            }
            return false;
        } else if (token == MENOS) {
            if (T(v1)) {
                //despues de pasar por
                v.setValor(v.getValor() - v1.getValor());
                if (Ep(v)) {
                    return true;
                }
            }
            return false;

        }
        //si se llega aqui se considera como si hubiera entrado un epsilon y se retorna al lexema anterior
        lex.retrieveToken();
        return true;
    }

    // no se hacen operaciones en este no terminal
    /**
     * Regla intermediaria para viajar hacia T prima
     * @param v en esta variable se iran guardando los resultados cada que se haga la evaluacion de una operacion 
     * @return retorna verdadero si la navegacion sobre el arbol de sintaxis fue correcta
     */
    public boolean T(Numero v) {
        if (F(v)) {
            if (Tp(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dentro de esta regla se avanza en el arbol de sintaxis y tambien se evaluan valores numericos encontrados como lexemas
     * @param v  en esta variable se iran guardando los resultados cada que se haga la evaluacion de una operacion 
     * @return valor booleano que indica si se encontro una suma o resta o epsilon
     */
    public boolean Tp(Numero v) {
        int token = this.lex.getToken();
        Numero v1 = new Numero();
        if (token == PROD) {
            if (F(v1)) {
                v.setValor(v.getValor() * v1.getValor());
                if (Tp(v)) {
                    return true;
                }
            }
            return false;
        } else if (token == DIV) {
            if (F(v1)) {
                v.setValor(v.getValor() / v1.getValor());
                if (Tp(v)) {
                    return true;
                }
            }
            return false;
        }
        this.lex.retrieveToken();
        return true;
    }

    /**
     * Regla final que se encarga de validar si el lexema corresponde a un parentesis o un numero
     * @param v en esta variable se iran guardando los resultados cada que se haga la evaluacion de una operacion 
     * @return retorna verdadero si se encontro algun parentesis o si la expresion corresponde a un numero
     */
    public boolean F(Numero v) {
        int token = this.lex.getToken();
        switch (token) {
        case PAR_I:
            if (E(v)) {
                token = this.lex.getToken();
                if (token == PAR_D) {
                    return true;
                }
            }
            return false;
        case NUM:
            v.setValor(Float.parseFloat(lex.getLexema()));

            return true;
        }
        return false;
    }

    public boolean Analizar(){
        if(E(this.resultado)){
            this.lex.getToken();
            if(this.lex.getToken()==FIN){
                System.out.println("Correcta");
                return true;
            }
        }
        System.out.println("Incorrecta");
        return false;
    }

    
    public float Evaluar(){
        System.out.println("se debe imprimir algo");
        if(Analizar()){
            return this.resultado.getValor();
            //System.out.println("Resultado de evaluacion: "+this.resultado.getValor());
        }else{
            return -1f;
            //System.out.println("No se puede evaluar expresion");
            //System.out.println("No se puede evaluar expresion "+this.resultado.getValor());
        }
    }
}
