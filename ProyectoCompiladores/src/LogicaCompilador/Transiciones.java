package LogicaCompilador;
import java.util.*;
import javafx.util.Pair;

public class Transiciones{
    private Hashtable<Pair<Character,Character>,Estado> t; //transiciones con caracter
    private Set<Estado> te; //transiciones epsilon

    public Transiciones(){
        this.t = new Hashtable<Pair<Character,Character>,Estado>();
        this.te = new HashSet<Estado>();
        //t.put('c',new Estado());
        //System.out.println("Agregando transicion c"+"-> "+e.getIdentificador());
    }






    /*---------------------------------------------------------------------------------------------------------
                                                    Operaciones de transicion
    ---------------------------------------------------------------------------------------------------------*/
  

    public void agregarTransicion(Character c, Estado e){
        t.put(new Pair<Character,Character>(c,c),e);
        System.out.println(c+" -> "+e.getIdentificador());
    }

    public void agregarTransicion(Character c,Character c1, Estado e){
        t.put(new Pair<Character,Character>(c,c1),e);
        System.out.println(c+":"+c1+" -> "+e.getIdentificador());
    }
    public void agregarTransicionEpsilon(Estado e){
        te.add(e);
        System.out.println("\t[Epsilon] -> "+e.getIdentificador());
    }

    public void mostrarTransiciones(){
        // Obtain an Iterator for the entries Set
        // Get a set of all the entries (key - value pairs) contained in the Hashtable
        Set<Pair<Character,Character>> keys = t.keySet();
        for(Pair<Character,Character> key: keys){
            System.out.println("\t"+key+" -> "+t.get(key).getIdentificador());
        }

    }

    public void mostrarTransicionesEpsilon(){
        // Obtain an Iterator for the entries Set
        // Get a set of all the entries (key - value pairs) contained in the Hashtable
        for(Estado e: te){
            System.out.println("\t[Epsilon] -> "+e.getIdentificador());
        }

    }
    public void borrarTransiciones(){
        t.clear();
    }


    /*---------------------------------------------------------------------------------------------------------
                                                    Getters
    ---------------------------------------------------------------------------------------------------------*/
    public Hashtable<Pair<Character,Character>,Estado> getTransiciones(){
        return t;
    }

    public Set<Estado> getTransicionesEpsilon(){
        return te;
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Setters
    ---------------------------------------------------------------------------------------------------------*/
    public void setTransiciones(Hashtable<Pair<Character,Character>,Estado> a){
        t = a;
    }

    public void setTransicionesEpsilon(Set<Estado> a){
        this.te = a;
    }


}
