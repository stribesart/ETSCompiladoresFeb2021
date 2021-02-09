package LogicaCompilador;

public class GramaticaDeGramaticas{


    //--------------tokens------------------
    public final int SIMBOLO = 10; // SIMBOLO -> G
    public final int FLECHA = 20; // FLECHA -> -> 
    //public final int SIMBOLO = 30 // SIMBOLO -> ListaReglas
    public final int PUNTO_COMA = 30; // PUNTO_COMA -> ;
    public final int OR = 40;
    public final int FIN = 1000;
    //--------------------------------------
    private AnalizadorLexico lex;
    private ListaDeListas lista;
    private boolean inicialFlag;
    public GramaticaDeGramaticas(AnalizadorLexico lex){
        this.lex = lex;
        this.lista = null;
        this.inicialFlag = true;
    }

    boolean G(ListaDeListas l){
        return listaReglas(l);
    }

    boolean listaReglas(ListaDeListas l){
        
        //System.out.println("entra lista reglas");
        int e;
        int token;
        if(regla(l)){
            token = this.lex.getToken();
            
            if(token == PUNTO_COMA){
                e = this.lex.getInicio();
                //System.out.println("inicio actual:"+e);
                if(listaReglas(l)){
                    return true;
                }
                //System.out.println("inicio final:"+e);
                this.lex.setInicio(e);
                return true;
            }
        }
        return false;
    }

    boolean regla(ListaDeListas l){
        int token;
        //System.out.println("entra regla");
        if(ladoIzquierdo(l)){
            token = lex.getToken();
            if(token == FLECHA){
                if(listaLadosDerechos(l)){
                    return true;
                }
            }
        }
        return false;
    }

    boolean ladoIzquierdo(ListaDeListas l){
        int token;
        //System.out.println("entra lado izquierdo");
        token = lex.getToken();
        if(token == SIMBOLO){
            //System.out.println(lex.getLexema());
            if(inicialFlag){
                l.addInicial(lex.getLexema());
                inicialFlag = false;
            }else{
                l.nuevaRegla(lex.getLexema());
            }
            //agregar simbolo al lado izquierdo
            return true;
        }        
        return false;
    }


    boolean listaLadosDerechos(ListaDeListas l){
        int token;
        //System.out.println("entra lista lados derechos");
        if(ladoDerecho(l)){
            token = lex.getToken();
            if(token == OR){
                //System.out.println("Entra OR");
                l.terminarReglaActual();
                //l.nuevaRegla();
                if(listaLadosDerechos(l))
                    return true;
                return false;
            }
            l.terminarReglaActual();
            
            lex.retrieveToken();
            return true;
        }
        return false;
    }


    boolean ladoDerecho(ListaDeListas l){
        //System.out.println("entra lado derecho");
        return listaSimbolos(l);
    }

    boolean listaSimbolos(ListaDeListas l){
        int token;
        //System.out.println("entra lista simbolos");
        token = lex.getToken();
        if(token == SIMBOLO){
            //agregar simbolo
            //System.out.println(lex.getLexema());
            if(!lex.getLexema().equals(" ")){l.agregarNodo(lex.getLexema());}
            //l.agregarNodo(lex.getLexema());
            int e = lex.getInicio();
            //verificar si el lexema no arroja un simbolo de punto y coma 
            if(listaSimbolos(l)){
                return true;
            }
            lex.setInicio(e);
            return true;

        }
        return false;
    }

    public boolean evaluar(){
        this.lista = new ListaDeListas();
        if(G(lista)){
            lex.getToken();
            if(lex.getToken() == 1000){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public ListaDeListas analizar(){
        if(evaluar()){
            return this.lista;
        }else{
            return null;
        }
    }

}