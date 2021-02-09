package LogicaCompilador;

public class GeneradorAutomata {

    // --------------------Constantes provisionales----------------------
    public final int OR = 10;
    public final int CONC = 20;
    public final int CERR_POS = 30;
    public final int CERR_KLEEN = 40;
    public final int OPC = 50;
    public final int PAR_I = 60;
    public final int PAR_D = 70;
    public final int SIMB = 80;
    public final int CORR_I = 90;
    public final int CORR_D = 100;
    public final int GUION = 110;
    public final int FIN = 1000;

    // --------------------Constantes provisionales----------------------

    private AFN afn;
    private AnalizadorLexico lex;

    public GeneradorAutomata(AnalizadorLexico lex) {
        this.lex = lex;
        afn = new AFN();
    }

    boolean E(AFN f) {
        if (T(f)) {
            if (Ep(f)) {
                return true;
            }

        }
        return false;
    }

    boolean Ep(AFN f) {
        int token;
        AFN f1 = new AFN();
        token = lex.getToken();
        //System.out.println("si llega a ep");
        if (token == OR) {
           // System.out.println("si llega a la condicion");
            if (T(f1)) {
                f.unir(f1);
                if (Ep(f)) {
                    return true;
                }
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    boolean T(AFN f) {
        if (C(f)) {
            if (Tp(f)) {
                return true;
            }
        }
        return false;
    }

    boolean Tp(AFN f) {
        int token;
        AFN f1 = new AFN();
        token = lex.getToken();
        if (token == CONC) {
            if (C(f1)) {
                f.concatenar(f1);
                if (Tp(f)) {
                    return true;
                }
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    boolean C(AFN f) {
        if (F(f)) {
            if (Cp(f)) {
                return true;
            }
        }
        return false;
    }

    boolean Cp(AFN f) {
        int token;
        token = lex.getToken();
        switch (token) {
        case CERR_KLEEN:
            f.cerraduraEstrella();
            if (Cp(f)) {
                return true;
            }
            return false;
        case CERR_POS:
            f.cerraduraPositiva();
            if (Cp(f)) {
                return true;
            }
            return false;
        case OPC:
            f.Question();
            if (Cp(f)) {
                return true;
            }
            return false;
        }
        lex.retrieveToken();
        return true;
    }

    boolean F(AFN f) {
        int token;
        token = lex.getToken();
        switch (token) {
            case PAR_I:
                if (E(f)) {
                    token = lex.getToken();
                    if (token == PAR_D) {
                        return true;
                    }
                }
                return false;
            case SIMB:
                String s = lex.getLexema();
                f.basico(takeChar(lex.getLexema()));
                return true;
            case CORR_I:
                Character s1, s2;
                token = lex.getToken();
                if (token == SIMB) {
                    s1 = takeChar(lex.getLexema());
                    token = lex.getToken();
                    if (token == GUION) {
                        token = lex.getToken();
                        if (token == SIMB) {
                            s2 = takeChar(lex.getLexema());
                            token = lex.getToken();
                            if (token == CORR_D) {
                                f.basico(s1, s2);
                                return true;
                            }
                        }
                    }
                }
                return false;
        }

        return false;
    }

    public Character takeChar(String cad){
        if(cad.length() == 1){
            return cad.charAt(0);
        }
        else{
            return cad.charAt(1);
        }
    }

    // agregar a F la derivacion [simb-simb2]

    public boolean validar() {
        int token;
        this.afn = new AFN();
        if (E(this.afn)) {
            token = lex.getToken();
            token = lex.getToken();
            if (token == FIN)
                return true;
        }
        return false;
    }

    public void setAnalizador(AnalizadorLexico lex){
        this.lex = lex;
    }
    
    public AFN evaluar(boolean respuesta){
        if(respuesta){
            return this.afn;
        }
        else{
            return new AFN();
        }
    }
    public AFN evaluar() throws Exception {
        if (validar()) {
            System.out.println("Si se genera");
            return afn;
        } else {
            throw new Exception("Expresion invalida, no se genero automata");
        }
    }

}