package LogicaCompilador;
import java.util.*;
import javafx.util.Pair;

public class Estado{

    private Transiciones ts;                    //Conjunto de transiciones del estado
    public static int identificador_global = -1; //contador global, que se irá incementando cada que se cree un Estado
    private int identificador;                  //identificador del estado
    private boolean aceptacion;   
    private int token;

    public Estado(){
        this.ts = new Transiciones();
        this.aceptacion = false;               //todos inician como no aceptacion
        this.identificador = ++identificador_global;    //Aumenta el contador global y guarda el identificador actual
        this.token = -1;
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Operaciones de Estado
    ---------------------------------------------------------------------------------------------------------*/

    public void agregarTransicion(Character c,Estado a){
        System.out.print("Agregando transicion del estado "+identificador+": ");
        ts.agregarTransicion(c,a);
    }

    public void agregarTransicion(Character c,Character c1,Estado a){
        System.out.print("Agregando transicion del estado "+identificador+": ");
        ts.agregarTransicion(c,c1,a);
    }

    public void agregarTransicionEpsilon(Estado a){
        System.out.print("Agregando transicion del estado "+identificador+": ");
        ts.agregarTransicionEpsilon(a);
    }

    public void mostrarTransiciones(){
        System.out.println("\nTransiciones de "+identificador+" :");
        ts.mostrarTransiciones();
        System.out.println("Fin de transciones");
        
    }

   

    public void borrarTransiciones(){   
        ts.borrarTransiciones();
    }

    public Set<Estado> mover(Character c){  //regresa el estado al que se mueve, con el caracter c para un AFN
        //System.out.println("Moviendo con "+c);
        Set<Estado> estadosPosibles= new HashSet<Estado>(); //creas el conjunto de estados

        //recorres el conjunto de transiciones
        Hashtable<Pair<Character,Character>,Estado> t = ts.getTransiciones();

        Set<Pair<Character,Character>> keys = t.keySet(); //consigue las llaves de el hashset  

        for(Pair<Character,Character> key: keys){ //las recorre
            if(c>=key.getKey() && c<=key.getValue()){ //si encuentra una valida, lo agrega al conjunto
                System.out.println(key+" -> Nodo  "+t.get(key).getIdentificador());
                estadosPosibles.add(t.get(key));  //agrego al conjunto el estado apuntado por la llave
            }
        }
        return estadosPosibles;
    }

    public Estado moverAFD(Character c){  //regresa el estado al que se mueve, con el caracter c para un AFD
        //System.out.println("Moviendo con "+c);
        Set<Estado> estadosPosibles= new HashSet<Estado>(); //creas el conjunto de estados

        //recorres el conjunto de transiciones
        Hashtable<Pair<Character,Character>,Estado> t = ts.getTransiciones();

        Set<Pair<Character,Character>> keys = t.keySet(); //consigue las llaves del hash
        System.out.println("Nodo "+identificador);
        System.out.println(""+c);
        for(Pair<Character,Character> key: keys){ //las recorre
            //System.out.println(""+key.getKey()+"   "+key.getValue());
            if(c>=key.getKey() && c<=key.getValue()){ //si encuentra una transicion valida, la regresa
                //System.out.println(key+" -> Nodo  "+t.get(key).getIdentificador());
                return t.get(key);
            }
        }
        return null;
    }

    public Set<Estado> irA(Character c){
        return cerraduraEpsilon(mover(c));
    }

    public Set<Estado> cerraduraEpsilon(){
        Set<Estado> aux = new HashSet<Estado>();
        aux.add(this);
        return cerraduraEpsilon(aux);
    }
    public Set<Estado> cerraduraEpsilon(Set<Estado> estados){ //no terminado //se hace cerradura a cada estado del conjunto
        Set<Estado> cerradura = new HashSet<Estado>();
        Queue <Estado> cola = new LinkedList<Estado>();
        for(Estado e:estados){  //por cada estado en e
            //if(!cerradura.contains(e)){  //si no esta en el conjunto, lo agrega
            cola.add(e); //agrego a la cola los estados.
            //}
        }

        while(cola.peek()!=null){ //mientras la cola no este vacia
            Estado aux = cola.poll(); //guardo el estado en aux
            cerradura.add(aux); //agrego el estado a la cerradura

            Transiciones aux_t = aux.getTransiciones(); //obtiene las transiciones del estado actual
            Set<Estado> cerraduraAux = aux_t.getTransicionesEpsilon(); //tengo los estados donde puede ir el estado actual

            for(Estado e:cerraduraAux){  //por cada estado en e
                if(!cerradura.contains(e)){  //si no esta en el conjunto, lo agrega
                    cola.add(e); //agrego a la cola los estados.
                    cerradura.add(e);
                }
            }


        }
        return cerradura;
    }


    /*---------------------------------------------------------------------------------------------------------
                                                    Getters
    ---------------------------------------------------------------------------------------------------------*/
    public int getIdentificador(){
        return identificador;
    }

    public Transiciones getTransiciones(){
        return ts;
    }

    public boolean getAceptacion(){
        return aceptacion;
    }
    public int getToken(){
        return token;
    }


    /*---------------------------------------------------------------------------------------------------------
                                                    Setters
    ---------------------------------------------------------------------------------------------------------*/

    public void setToken(int i){
        token = i;
    }

    public void setIdentificador(int i){
        identificador = i;
    }

    public void setTransiciones(Transiciones tr){
        System.out.println("Agregando siguientes transiciones a "+identificador);
        tr.mostrarTransiciones();
        tr.mostrarTransicionesEpsilon();
        ts = tr;
    }

    public void setAceptacion(boolean a){
        aceptacion=a;
    }

}
