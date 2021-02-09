
package LogicaCompilador;
import java.util.Stack;
public class AnalizadorLexico{

    private AFD a; //AFD que utilizará para realizar el analisis
    private int inicio; //posicion en la cadena en donde esta situado actualmente el analizador
    private boolean banderaAceptacion;  //bandera para saber si ha pasado por un estado de aceptacion
    private String cadena; //cadena que analizara
    private String lexema; //lexema que regresara
    private Stack<Integer> inicios; //pila de inicios

    public AnalizadorLexico(){  //se inicializan las variables
        this.inicio = 0; //inicia en la posicion 0 de la cadena 
        this.cadena = "";
        this.lexema = "";
        this.banderaAceptacion = false;
        this.inicios = new Stack<Integer>(); //la pila se inicializa 
    }

    public AnalizadorLexico(AFD a){
        this.a = a;
    }

    public void retrieveToken(){ //regresa el analizador un token a la cadena
        if(!inicios.empty()){ //si la pila no esta vacia
            inicio = inicios.pop(); //saca un inicio de la pila
            //System.out.println("Regreso a "+inicio);
            lexema=""; //el ultimo lexema se borra
        }else{
            System.out.println("No es posible regresar mas");
        }
    }

    public int getToken(){ //obtiene un token
        int fin=inicio; //inicio fin de lexema
        banderaAceptacion = false;
        Estado actual = a.getInicial(); //inicia en edoinicial
        int tokenAux = -1; //guarda un estado de aceptacion auxiliar

        for(int i = inicio; i< cadena.length();i++){ //se recorre la cadena caracter a caracter
            actual  = actual.moverAFD(cadena.charAt(i)); //se mueve con el caracter
 
            if(actual!=null && actual.getAceptacion()){ //si a donde se movio, no es vacio y es de aceptacion
                banderaAceptacion = true; //se paso por un estado de aceptacion
                tokenAux = actual.getToken(); //guardas el token
                fin = i; //mueves el final de lexema
            }

            if(actual==null || i == cadena.length()-1){ //si no hay transicion con el caracter

                if(!banderaAceptacion){ //si no se ha pasado por un estado de aceptacion 
                    System.out.print("no"); //la cadena no es aceptada
                    return -1;
                }
                //si si se paso por un estado de aceptacion
                lexema = cadena.substring(inicio,fin+1);
                //System.out.println("\tLexema "+cadena.substring(inicio,fin+1)+"  Token: "+tokenAux); //se imprime el lexema que se recorrio
                inicios.push(inicio);
                inicio = fin+1; //se modifica el inicio al ultimo estado final
                return tokenAux;
            }
        }   
        return 1000;
    }
    public boolean cadenaUnica(String s){ //para una palabra completa
        Estado actual = a.getInicial(); //se inicial desde el edoinicial
        for(Character c:s.toCharArray()){ //se recorre cada caracter en la cadena
            //System.out.println(String.valueOf(c));
            actual = actual.moverAFD(c); //se mueve el actual con el caracter
            if(actual==null) //si da null, significa que no hay transicion
                return false; //por lo cual la cadena no es aceptada
        }       
        
        if(actual.getAceptacion()) //si termino de recorrerse la palabra y el estado donde se quedo es de aceptacion
            return true;
        
        return false; 
    }

    public boolean cadena(String s){ //para una cadena donde tienes muchos lexemas Ejemplo: tssdltsd.d -> t ssdl t sd.d
        inicio = 0;
        int fin=0; //inicio fin de lexema
        Estado actual = a.getInicial(); //inicia en edoinicial
        Estado finalaux = a.getInicial(); //guarda un estado de aceptacion auxiliar
        banderaAceptacion = false;  //sirve para ver si se paso por un estado de aceptacion

        for(int i = 0; i< s.length();i++){ //se recorre la cadena caracter a caracter
            actual  = actual.moverAFD(s.charAt(i)); //se mueve con el caracter
 
            if(actual!=null && actual.getAceptacion()){ //si a donde se movio, no es vacio y es de aceptacion
                banderaAceptacion = true; //se paso por un estado de aceptacion
                finalaux = actual; //guardas el estado
                fin = i; //mueves el final de lexema
            }

            if(actual==null){ //si no hay transicion con el caracter

                if(!banderaAceptacion){ //si no se ha pasado por un estado de aceptacion 
                    System.out.print("no"); //la cadena no es aceptada
                    return false;
                }
                //si si se paso por un estado de aceptacion
                //System.out.println("\tLexema "+s.substring(inicio,fin+1)+"  Token: "+finalaux.getToken()); //se imprime el lexema que se recorrio
                actual = a.getInicial(); //se reinicia el estado actual
                finalaux = a.getInicial(); //y el final
                inicio = fin+1; //se modifica el inicio al ultimo estado final
                i=fin; //se regresa al ultimo caracter con estado final
                banderaAceptacion = false;  //se regresa la aceptacion a falso
            }
   
            
        }       

        if(s.length()!=0 && actual.getAceptacion()){ //si la cadena se recorrio completa y termino en aceptacion
            //System.out.println("\tLexema "+s.substring(inicio,fin+1)+"  Token: "+actual.getToken());
            return true;
        }

        if(s.length()==0 && actual.getAceptacion()){ //si la cadena era vacia y es de aceptacion el inicial
            //System.out.println("\tToken: "+actual.getToken());
            return true;
        }

        return false;
    }  




    /*-------------------------------------------------------------------------------------------------------------
                                            Getters Setters
    -------------------------------------------------------------------------------------------------------------*/
    public String getLexema(){
        return lexema;
    }
    public String getCadena(){
        return cadena;
    }
    public AFD getAFD(){
        return a;
    }

    public int getInicio(){
        return inicio;
    }

    public boolean setBanderaAceptacion(){
        return banderaAceptacion;
    }

    public void setAFD(AFD a){
        this.a = a;
    }

    public void setInicio(int i){
        inicio=i;
    }

    public void setBanderaAceptacion(boolean i){
        banderaAceptacion = i;
    }
    public void setCadena(String s){
        cadena=s;
        this.inicio = 0; //inicia en la posicion 0 de la cadena 
        this.lexema = "";
        this.banderaAceptacion = false;
        this.inicios = new Stack<Integer>(); //la pila se inicializa   
    }
}