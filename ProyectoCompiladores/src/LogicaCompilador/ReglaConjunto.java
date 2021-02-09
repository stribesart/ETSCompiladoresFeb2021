package LogicaCompilador;

import java.util.LinkedList;
import java.util.HashSet;

public class ReglaConjunto{
    private LinkedList<String> regla;
    private HashSet<String> conjunto;

    public ReglaConjunto(){
        this.regla = new LinkedList<>();
        this.conjunto = new HashSet<>();
    }

    //setters
    public void setRegla(LinkedList<String> a){
        this.regla = a;
    }

    public void setConjunto(HashSet<String> a){
        this.conjunto = a;
    }


    //getters
    public LinkedList<String> getRegla(){
        return regla;
    }

    public HashSet<String> getConjunto(){
        return conjunto;
    }

    // Overriding equals() to compare two Complex objects 
    @Override
    public boolean equals(Object o) { 
  
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof ReglaConjunto)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        ReglaConjunto c = (ReglaConjunto) o; 
          
        // Compare the data members and return accordingly
        return c.conjunto.equals(conjunto) && c.regla.equals(regla);
    } 
     
}