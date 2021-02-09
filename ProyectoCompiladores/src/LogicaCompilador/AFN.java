package LogicaCompilador;
import java.util.*;

public class AFN{

    private Estado edo_inicial;
    private Set<Character> alfabeto;
    private Set<Estado> estados;
    private Set<Estado> estadosAceptacion;


    public AFN(){
       this.alfabeto = new HashSet<Character>();
       this.estados = new HashSet<Estado>();
       this.estadosAceptacion = new HashSet<Estado>();
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Agregar a conjuntos
    ---------------------------------------------------------------------------------------------------------*/


    public void agregarAlfabeto(Character c){
        alfabeto.add(c);
    }

    public void agregarAlfabeto(Character c,Character c1){
        for(int i=c;i<=c1;i++)
            alfabeto.add((char)i);
    }

    public void agregarEstados(Estado e){
        estados.add(e);
    }

    public void agregarEstadosAceptacion(Estado e){
        estadosAceptacion.add(e);
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Creacion de ANF's
    ---------------------------------------------------------------------------------------------------------*/

    public void basico(char c,char c1){ //basico
        Estado e; //estado medio
        Estado einicial;
        Estado efinal;


        agregarAlfabeto(c,c1); //agrega al alfabeto
        einicial = new Estado();  //estado inicial
        efinal = new Estado(); //estado final
        efinal.setAceptacion(true); //pongo el final como aceptacion
        
        //conexion
        einicial.agregarTransicion(c,c1,efinal); //conecto el inicial al medio     einicial --c--> e

        //muestro las transiciones
        /*
        einicial.mostrarTransiciones();
        e.mostrarTransiciones(); 
        efinal.mostrarTransiciones();
        */

        //agrego los estados
        agregarEstados(einicial);
        agregarEstados(efinal);

        //agrego el de aceptacion
        agregarEstadosAceptacion(efinal);

        edo_inicial = einicial;


    }

    public void basico(char c){ //basico
        Estado e; //estado medio
        Estado einicial;
        Estado efinal;


        agregarAlfabeto(c); //agrega al alfabeto
        einicial = new Estado();  //estado inicial
        efinal = new Estado(); //estado final
        efinal.setAceptacion(true); //pongo el final como aceptacion
        
        //conexion
        einicial.agregarTransicion(c,efinal); //conecto el inicial al medio     einicial --c--> e

        //muestro las transiciones
        /*
        einicial.mostrarTransiciones();
        e.mostrarTransiciones(); 
        efinal.mostrarTransiciones();
        */

        //agrego los estados
        agregarEstados(einicial);
        agregarEstados(efinal);

        //agrego el de aceptacion
        agregarEstadosAceptacion(efinal);

        edo_inicial = einicial;


    }

    public void Question(){ //AFN tipo a?  
        Estado inicioNuevo = new Estado();
        Estado finNuevo = new Estado();
        finNuevo.setAceptacion(true);
        //conexion
        inicioNuevo.agregarTransicionEpsilon(edo_inicial); //agrego transicion del nuevo al inicial
        inicioNuevo.agregarTransicionEpsilon(finNuevo);
        for(Estado e:estadosAceptacion){
            e.setAceptacion(false);
            e.agregarTransicionEpsilon(finNuevo);
        }
        edo_inicial = inicioNuevo; //cambio el inicial

        estadosAceptacion.clear();
        estadosAceptacion.add(finNuevo);

    }

    public void cerraduraPositiva(){ //lo hace un AFN estrella
        Estado inicioNuevo = new Estado();
        Estado finNuevo = new Estado();

        //agrego al conjunto de estados
        estados.add(inicioNuevo);
        estados.add(finNuevo);

        //transiciones nuevas
        inicioNuevo.agregarTransicionEpsilon(edo_inicial);


        for(Estado e:estadosAceptacion){
            e.setAceptacion(false); //les quito la propiedad de aceptacion
            e.agregarTransicionEpsilon(finNuevo); //transicion al nuevo fin
            e.agregarTransicionEpsilon(edo_inicial); //del final antiguo al inicio antiguo
        }

        //cambio de iniciales y finales
        estadosAceptacion.clear();
        estadosAceptacion.add(finNuevo);
        edo_inicial = inicioNuevo;    

    }

    public void cerraduraEstrella(){ //lo hace un AFN estrella
        Estado inicioNuevo = new Estado();
        Estado finNuevo = new Estado();

        //agrego al conjunto de estados
        estados.add(inicioNuevo);
        estados.add(finNuevo);

        //transiciones nuevas
        inicioNuevo.agregarTransicionEpsilon(finNuevo);
        inicioNuevo.agregarTransicionEpsilon(edo_inicial);

        for(Estado e:estadosAceptacion){
            e.setAceptacion(false); //les quito la propiedad de aceptacion
            e.agregarTransicionEpsilon(finNuevo); //transicion al nuevo fin
            e.agregarTransicionEpsilon(edo_inicial);
        }

        //cambio de iniciales y finales
        estadosAceptacion.clear();
        estadosAceptacion.add(finNuevo);
        edo_inicial = inicioNuevo;    

    }

    public void unirAL(ArrayList<AFN> automatas){ //unir para analisis lexico
        Estado inicioNuevo = new Estado();
        estados.add(inicioNuevo);
        inicioNuevo.agregarTransicionEpsilon(edo_inicial); //agrego transicion del nuevo al inicio anterior
        edo_inicial = inicioNuevo; //cambio el inicio
        for(AFN a:automatas){

            inicioNuevo.agregarTransicionEpsilon(a.getInicial()); //inicio nuevo al inicial del AFN 1

            estadosAceptacion.addAll(a.getEstadosAceptacion()); //agrego los estados de aceptacion 

        //unir alfabeto
        alfabeto.addAll(a.getAlfabeto());

        //unimos los estados
        estados.addAll(a.getEstados());
        }
    }

    public void unir(AFN a){ //lo hace un AFN estrella

        Estado inicioNuevo = new Estado();
        Estado finNuevo = new Estado();

        //agrego al conjunto de estados
        estados.add(inicioNuevo);
        estados.add(finNuevo);

        //transiciones iniciales nuevas
        inicioNuevo.agregarTransicionEpsilon(edo_inicial); //inicio nuevo al inicial del AFN 1
        inicioNuevo.agregarTransicionEpsilon(a.getInicial()); //inicio nuevo al inicial del AFN 1

        //transiciones finales nuevas
        for(Estado e:estadosAceptacion){
            e.setAceptacion(false); //les quito la propiedad de aceptacion
            e.agregarTransicionEpsilon(finNuevo); //transicion al nuevo fin
        }

        for(Estado e:a.getEstadosAceptacion()){
            e.setAceptacion(false); //les quito la propiedad de aceptacion
            e.agregarTransicionEpsilon(finNuevo); //transicion al nuevo fin
        }

        //cambio de iniciales y finales
        estadosAceptacion.clear();
        estadosAceptacion.add(finNuevo);
        edo_inicial = inicioNuevo;    

        //unir alfabeto
        alfabeto.addAll(a.getAlfabeto());

        //unimos los estados
        estados.addAll(a.getEstados());
    }


    public void concatenar(AFN a){ //lo hace un AFN estrella
 
        //transiciones finales actuales -> apuntan a todos los que apuntaba el inicial de a
        for(Estado e:estadosAceptacion){
            e.setAceptacion(false); //les quito la propiedad de aceptacion
            e.setTransiciones(a.getInicial().getTransiciones());
        }

        //cambio de iniciales y finales
        estadosAceptacion.clear();
        setEstadosAceptacion(a.getEstadosAceptacion());

        //unir alfabeto
        alfabeto.addAll(a.getAlfabeto());

        //unimos los estados
        estados.addAll(a.getEstados());

        //quitamos el inicial final
        estados.remove(a.getInicial());
    }

  /*---------------------------------------------------------------------------------------------------------
                                                    Imprimir
    ---------------------------------------------------------------------------------------------------------*/

    public void imprimirTransicionesAFN(){
        System.out.println("\n\nTransiciones AFN");
        for(Estado e:estados){
            System.out.println("Transiciones nodo "+e.getIdentificador());
            e.getTransiciones().mostrarTransiciones();
            e.getTransiciones().mostrarTransicionesEpsilon();
        }

    }

    public void imprimirConjuntoEstados(Set<Estado> set){
        //recorres el conjunto de transiciones
        for (Estado s : set) {
            System.out.println(s.getIdentificador());
        }
    }

    public void imprimirConjuntoCaracteres(Set<Character> set){
        //recorres el conjunto de transiciones
        for (Character s : set) {
            System.out.println(" "+s);
        }
    }

   /*---------------------------------------------------------------------------------------------------------
                                                    Setters
    ---------------------------------------------------------------------------------------------------------*/

    public void setEstadoInicial(Estado e){
        this.edo_inicial = e;
    }

    public void setAlfabeto(Set<Character> a){
        this.alfabeto = a;
    }

    public void setEstados(Set<Estado> e){
        estados = e;
    }
    
    public void setEstadosAceptacion(Set<Estado> e){
        estadosAceptacion = e;
    }

    public void setToken(int a){
        for(Estado e:estadosAceptacion){
            e.setToken(a);
        }
    }


     /*---------------------------------------------------------------------------------------------------------
                                                    Getters
    ---------------------------------------------------------------------------------------------------------*/
  
    public Set<Character> getAlfabeto(){
        return alfabeto;
    }

    public Estado getInicial(){
        return edo_inicial;
    }

    public Set<Estado> getEstadosAceptacion(){
        return estadosAceptacion;
    }

    public Set<Estado> getEstados(){
        return estados;
    }


}
