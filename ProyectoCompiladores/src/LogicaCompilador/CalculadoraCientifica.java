package LogicaCompilador;

public class CalculadoraCientifica {

    // --------------------Constantes provisionales----------------------
    public final int MAS = 10;
    public final int MENOS = 20;
    public final int PROD = 30;
    public final int DIV = 40;
    public final int POT = 50;
    public final int PAR_I = 60;
    public final int PAR_D = 70;
    public final int SIN = 80;
    public final int COS = 90;
    public final int TAN = 100;
    public final int EXP = 110;
    public final int LOG = 120;
    public final int LN = 130;
    public final int NUM = 140;
    public final int FIN = 1000;

    // --------------------Constantes provisionales----------------------
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

    public CalculadoraCientifica(AnalizadorLexico lex) {
        this.lex = lex;
    }

    public boolean E(Numero v) {
        if (T(v)) {
            if (Ep(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean Ep(Numero v) {
        int token;
        Numero v1 = new Numero();
        token = lex.getToken();

        if (token == MAS || token == MENOS) {
            if (T(v1)) {
                v.setValor(v.getValor() + ((token == MAS) ? v1.getValor() : -v1.getValor()));

                if (Ep(v)) {
                    return true;
                }
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    public boolean T(Numero v) {
        if (P(v)) {
            if (Tp(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean Tp(Numero v) {
        int token;
        Numero v1 = new Numero();
        token = lex.getToken();
        if (token == PROD || token == DIV) {
            if (P(v1)) {
                v.setValor(v.getValor() * ((token == PROD) ? v1.getValor() : 1f / v1.getValor()));
                if (Tp(v)) {
                    return true;
                }
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    public boolean P(Numero v) {
        if (F(v)) {
            if (Pp(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean Pp(Numero v) {
        int token;
        Numero v1 = new Numero();
        token = lex.getToken();
        if (token == POT) {
            if (F(v1)) {
                v.setValor((float) Math.pow(v.getValor(), v1.getValor()));
                if (Pp(v)) {
                    return true;
                }
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    public boolean F(Numero v) {
        int token = lex.getToken();
        switch (token) {
        case PAR_I:
            if (E(v)) {
                token = this.lex.getToken();
                if (token == PAR_D) {
                    return true;
                }
            }
            return false;
        case SIN:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.sin(v.getValor()));
                return true;
            }
            return false;
        case COS:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.cos(v.getValor()));
                return true;
            }
            return false;

        case TAN:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.tan(v.getValor()));
                return true;
            }
            return false;

        case EXP:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.exp(v.getValor()));
                return true;
            }
            return false;

        case LOG:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.log10(v.getValor()));
                return true;
            }
            return false;

        case LN:
            token = lex.getToken();
            if (verificarParentesis(token, v)) {
                v.setValor((float) Math.log(v.getValor()));
                return true;
            }
            return false;

        case NUM:
            v.setValor(Float.parseFloat(lex.getLexema()));
            return true;
        }
        return false;
    }

    public boolean verificarParentesis(int tok, Numero v) {
        if (tok == PAR_I) {
            if (E(v)) {
                int token = lex.getToken();
                if (token == PAR_D) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean validar(Numero v) {
        int token;
        if (E(v)) {
            token = lex.getToken();
            token = lex.getToken();
            if (token == FIN) {
                return true;
            }
        }
        return false;
    }

    public float evaluar() {
        if (validar(this.resultado)) {
            return this.resultado.getValor();
        }
        //System.out.println("esto se calculo: " + this.resultado.getValor());
        return -1f;
    }
}