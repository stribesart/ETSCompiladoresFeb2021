package LogicaCompilador;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;



public class ListaDeListas{
    private String inicial;
    private LinkedHashMap<String,LinkedList<LinkedList<String>>> mapaReglas;
    private LinkedHashMap<String,LinkedList<LinkedList<String>>> mapaReglasSinItem;
    private String izquierdo;
    private final String item = ".";
    private String aumentado;
    private LinkedList<String> tempRegla;
    private LinkedList<String> terminales;
    private LinkedList<String> noTerminales;
    private boolean banderaInicial = false;
    public ListaDeListas(){

        mapaReglas = new LinkedHashMap<>();
        mapaReglasSinItem = new LinkedHashMap<>();
        tempRegla = new LinkedList<>();
        terminales = new LinkedList<>();
        noTerminales = new LinkedList<>();
    }

    /**
     * Metodo para agregar el Terminal inicial de la gramatica a la lista
     * @param inicial recibe el simbolo terminal inicial
     */
    public void addInicial(String inicial){
        this.inicial = inicial;
        tempRegla = new LinkedList<>();
        nuevaRegla(inicial);
    }

    public String getInicial(){
        return this.inicial;
    }

    /**
     * Metodo para crear una nueva regla para la gramatica 
     * @param izquierdo Se recibe como parametro la parte izquierda de la regla y se inicializa
     * un arreglo de reglas temporales 
     */
    public void nuevaRegla(String izquierdo){
        //se verifica que sea la primera vez que se agrega el simbolo
        if(!mapaReglas.containsKey(izquierdo)){
            if(banderaInicial == false){
                //si es el primer simbolo agregado a la gramatica se guarda como simbolo inicial
                this.inicial = izquierdo;
                banderaInicial = true;
            }
            mapaReglas.put(izquierdo,new LinkedList<>());
            this.izquierdo = izquierdo;
            tempRegla = new LinkedList<>();    
        }else{
            //si ya existe el simbolo solo se inicializa una regla temporal
            tempRegla = new LinkedList<>();
        }
    }

    /**
     * Forma de iniciar regla con valor actual que se encuentren en izquierdo
     */
    public void nuevaRegla(){
        mapaReglas.put(izquierdo,new LinkedList<>());
        tempRegla = new LinkedList<>();
    }

    /**
     * Metodo para finalizar la regla y agregarla al conjunto de reglas a las que deriva el
     * simbolo izquierdo actual
     */
    public void terminarReglaActual(){
        mapaReglas.get(this.izquierdo).add(tempRegla);
        tempRegla = new LinkedList<>();
        
    }

    /**
     * Metodo que guarda los terminales en una lista ligada para
     * cuando se requiera monitorearlos
     */
    public void guardarNoTerminales(){
        for(String ter: mapaReglas.keySet()){
            this.noTerminales.add(ter);
        }
    }

    /**
     * Metodo para generar el conjunto de simbolos no terminales,
     * nota: el metodo guardarTerminales debe ser invocado antes que este
     */
    public void guardarTerminales(){
        for(String izq: mapaReglas.keySet()){
            for(LinkedList<String> lista: mapaReglas.get(izq)){
                for(String simbolos: lista){
                    //se verifica que el simbolo no este repetido en la lista y que no se encuentre en terminales
                    if(!terminales.contains(simbolos) && !noTerminales.contains(simbolos)){
                        terminales.add(simbolos);
                    }
                }
            }
        }
    }

    /**
     * Metodo para generar columnas necesarias para realizar un mapeo o asignacion 
     * si se requiere para la generacion de tablas LR
     * @return retorna un arreglo de cadenas que contiene ordenados los simbolos no terminales y los terminales
     */
    public String[] generarColumnas(){
        String[] arreglo = new String[terminales.size()+noTerminales.size()+1];
        int contador = 0;
        for(String noTer: terminales){
            arreglo[contador] = noTer;
            contador++;
        }
        //se agrega la columna donde se verifica la aceptacion
        arreglo[contador] = "$";
        contador++;
        for(String ter: noTerminales){
            arreglo[contador] = ter;
            contador++;
        }
        return arreglo;
    }

    /**
     * Metodo para obtener el contenido actual del mapa de reglas
     * @return Regresa un LinkedHashMap con los terminales y sus reglas asociadas 
     */
    public LinkedHashMap<String,LinkedList<LinkedList<String>>> getMapa(){
        return this.mapaReglas;
    }


    /**
     * Metodo para mostrar el contenido del mapa de reglas
     */
    public void mostrarMapa(){
        String aux = "";
        System.out.println(mapaReglas.size());
        int aux_cont;
        for(String s: mapaReglas.keySet()){
            System.out.print(s+"->");
            for(LinkedList<String> lin: mapaReglas.get(s)){
                aux_cont = 0;
                for(String regla: lin){
                    if (aux_cont == 0){
                        aux += regla+" ";    
                    }else{
                        aux += "|"+regla+" ";    
                    }
                    aux_cont += 1;
                }
                System.out.print(aux);
                aux = "";

            }

            System.out.println("");
            aux = "";
        }
    }

    public void mostrarMapaSinItems(){
        String aux = "";
        System.out.println(mapaReglasSinItem.size());
        int aux_cont;
        for(String s: mapaReglasSinItem.keySet()){
            System.out.print(s+"->");
            for(LinkedList<String> lin: mapaReglasSinItem.get(s)){
                aux_cont = 0;
                for(String regla: lin){
                    if (aux_cont == 0){
                        aux += regla+" ";    
                    }else{
                        aux += "|"+regla+" ";    
                    }
                    aux_cont += 1;
                }
                System.out.print(aux);
                aux = "";

            }

            System.out.println("");
            aux = "";
        }
    }    


    /**
     * En este metodo se agregaran uno a uno los nodos correspondientes a cada regla
     * @param s se recibe como argumento un caracter que representa un terminal o un no terminal
     * que pasara a formar un nodo
     */
    public void agregarNodo(String s){
        this.tempRegla.add(s);
    }

    /**
     * Metodo para agregar una regla adicional en la gramatica que tenga como derivacion el
     * estado inicial
     */
    public void gramaticaAumentada(){
        if(mapaReglas.size()>1){
            LinkedHashMap<String,LinkedList<LinkedList<String>>> tempMap = new LinkedHashMap<>();
            aumentado =this.inicial+'p'; 
            //se crea mapa temporal para que la regla aumentada sea la que aparezca primero
            tempMap.put(aumentado,new LinkedList<>());
            tempRegla = new LinkedList<>();    
            tempRegla.add(this.inicial);
            tempMap.get(aumentado).add(tempRegla);
            tempMap.putAll(mapaReglas);
    
            mapaReglas = tempMap;
            tempMap = null;
            noTerminales.add(aumentado);
            tempRegla = new LinkedList<>();    
        }
    }

    /**
     * Metodo que regresa el simbolo generado en la gramatica aumentada
     * @return cadena con el simbolo de gramatica aumentadad
     */
    public String getAumentado(){
        return this.aumentado;
    }

    /**
     * Metodo para agregar items a cada una de las reglas
     */
    @SuppressWarnings("unchecked")
    public void iniciarItems(){
        if(mapaReglas.size()>1){
            for(String s: mapaReglas.keySet()){
                mapaReglasSinItem.put(s, new LinkedList<LinkedList<String>>());
                for(LinkedList<String> rule: mapaReglas.get(s)){
                    mapaReglasSinItem.get(s).add((LinkedList<String>)rule.clone());
                    //se agrega simbolo al inicio de cada regla
                    rule.addFirst(this.item);
                }
            }
        }
    }

    public int numeroDeRegla(LinkedList<String> regla){
        LinkedList<String> reglaAux;
        int num = 0;
        for(String noTerm: mapaReglasSinItem.keySet()){

            for(LinkedList<String> listas: mapaReglasSinItem.get(noTerm)){
                reglaAux =  new LinkedList<String>();
                reglaAux.add(noTerm);                
                reglaAux.addAll(listas);
                if(reglaAux.equals(regla)){
                    return num;
                }
                num++;
            }
        }
        return -1;
    }

    public LinkedList<String> obtenerRegla(int n){
        LinkedList<String> reglaAux;
        int num = 0;
        for(String noTerm: mapaReglasSinItem.keySet()){

            for(LinkedList<String> listas: mapaReglasSinItem.get(noTerm)){
                reglaAux =  new LinkedList<String>();
                reglaAux.add(noTerm);                
                reglaAux.addAll(listas);
                if(num==n){
                    return reglaAux;
                }
                num++;
            }
        }
        return null;
    }

    /**
     * Metodo para obtener el simbolo que representa el item
     * @return String del item
     */
    public String getItem(){
        return this.item;
    }
    
    /**
     * Metodo para obtencion de lista de terminales
     * @return lista de cadenas con los simbolos terminales
     */
    public LinkedList<String> getTerminales(){
        return this.terminales;
    }

    /**
     * Metodo para obtencion de lista de no terminales
     * @return lista de cadenas con simbolos no terminales
     */
    public LinkedList<String> getNoTerminales(){
        return this.noTerminales;
    }

    public HashSet<String> first(LinkedList<String> R){
        HashSet<String> conjunto = new HashSet<String>();

        if(terminales.contains(R.get(0))){             //si es terminal el primer elemento
            conjunto.add(R.get(0));                     //se agrega el caracter al conjunto
            return conjunto;
        }

        for(LinkedList<String> lin: mapaReglasSinItem.get(R.get(0))){ //cada regla separada
            conjunto.addAll(first(lin));
        }

        if(conjunto.contains("#") && R.size()>1){
            conjunto.remove("#");
            conjunto.addAll(first((LinkedList<String>)R.subList(1,R.size())));
        } 
        return conjunto;
    }

    public HashSet<String> follow (String R){ //R es la regla a la que se quiere aplicar
        HashSet<String> conjunto = new HashSet<String>();
        if(R==inicial){                        //si R es inicial
            conjunto.add("$");
        }
        //System.out.println("\nFollow de "+R+":");

        for(String s: mapaReglasSinItem.keySet()){ //busca en cada regla
            for(LinkedList<String> lin: mapaReglasSinItem.get(s)){
                
                if(lin.contains(R)){ //si R esta del lado derecho de la regla
                    
                    int inicio=lin.indexOf(R);                                                  //busca en que parte esta

                    LinkedList<String> aux = new LinkedList<>();
                    aux.addAll(lin.subList(inicio, lin.size()));
                    
                    if(aux.size()==1){                      //no tiene nada despues

                        if(R!=s){                               //si no es el mismo que ya se esta evaluando
                            conjunto.addAll(follow(s));                    
                        }
                    }else{ //si tiene algo despues
                        
                        LinkedList<String> aux2 = new LinkedList<>();
                        aux2.addAll(aux.subList(1, aux.size()));
                        conjunto.addAll(first(aux2));              //saca first
                        if(conjunto.contains("#")){                 //si tiene epsilon, se hace follow al no terminal
                            conjunto.remove("#");                   //se quita epsilon
                            conjunto.addAll(follow(s));
                        }
                    } 
                }
            }
        }
        return conjunto;
    }


}


