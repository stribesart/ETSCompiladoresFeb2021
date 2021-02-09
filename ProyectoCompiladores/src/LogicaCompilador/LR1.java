package LogicaCompilador;

import java.util.Arrays;
import java.util.ArrayList;;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


/**
 * LR1
 */
public class LR1 {

    /**
     * @return the columnas
     */
    public String[] getColumnas() {
        return columnas;
    }
    private ListaDeListas gramatica;
    private String[] columnas;
    private LinkedList<String[]> tablaLR1;

    public LR1(ListaDeListas gramatica) {
        this.gramatica = gramatica;
    }

    /**
     * Metodo que se encarga de realizar la cerradura de un conjunto de estados (la cerradura consiste en
     * verificar los elementos contenidos en los estados y las reglas asociadas de los terminales que se encuentren despues del item)
     * @param estados conjunto de estados a los que se aplicara la cerradura
     * @return se retorna una lista de listas que contiene los elementos que conforman la cerradura
     */
    public LinkedList<ReglaConjunto> cerradura(LinkedList<ReglaConjunto> estados) {
        int index = 0;
        imprimeItems2(estados);
        System.out.println("^^Cerradura:");
        LinkedList<ReglaConjunto> pila = new LinkedList<>();
        LinkedList<ReglaConjunto> memoria = new LinkedList<>();

        LinkedList<ReglaConjunto> conjunto = new LinkedList<>();


        pila.addAll(estados);
        conjunto.addAll(estados);

        while(!pila.isEmpty()){ //mientras no este vacia
            ReglaConjunto aux = pila.getFirst();
            pila.remove(aux);


                int ind = aux.getRegla().indexOf(gramatica.getItem());
                if(ind+1 < aux.getRegla().size()){
                    String actual = aux.getRegla().get(ind+1);
                    // System.out.println("ind "+ind+" : "+actual);
                    if(gramatica.getNoTerminales().contains(actual)){ //si es no terminal
                        // System.out.println("Derivados "+actual);
                        for (LinkedList<String> conjReglas : gramatica.getMapa().get(actual)) { //obtengo todas las reglas que derivan
                            LinkedList<String> regla = new LinkedList<>();
                            regla.addAll(conjReglas);
                            regla.addFirst(actual); //completa la regla
                            // for(String s:regla){
                            //     System.out.print(s+" ");
                            // }
                            // System.out.println();

                            ReglaConjunto rcAux = new ReglaConjunto();
                            rcAux.setRegla(aux.getRegla());
                            HashSet<String> n = new HashSet<>();
                            n.addAll(aux.getConjunto());
                            rcAux.setConjunto(n);
                            HashSet<String> a = first(rcAux);
                            rcAux.setRegla(regla);
                            rcAux.setConjunto(a);


                            if((index = indice(conjunto,rcAux.getRegla())) != -1){ //si ya contiene la regla
                                //hago first y pongo agrego
                                // System.out.println(index+" --");
                                ReglaConjunto rca = conjunto.get(index);
                                // System.out.println();
                                rca.getConjunto().addAll(rcAux.getConjunto()); //agrega los que tenia en el conjunto anterior
                                

                            }else{
                                // System.out.println(index+" ++");
                                conjunto.add(rcAux);
                            }

                            if(!memoria.contains(rcAux) && !pila.contains(rcAux)){
                                memoria.add(rcAux);
                                pila.add(rcAux);
                            }
                        }
                    }
                }
        //    System.out.println("------");        
            // imprimeItems2(conjunto);
        }
        // System.out.println("------");
        // imprimeItems2(conjunto);
        return conjunto;
    }

   

    public Integer indice(LinkedList<ReglaConjunto> conjunto,LinkedList<String> Regla){
        int i=0;
        for(ReglaConjunto r:conjunto){
            if(r.getRegla().equals(Regla))
                return i;
            i++;
        }
        return -1;
    }


    public HashSet<String> first(ReglaConjunto R) {
        int ind = R.getRegla().indexOf(gramatica.getItem()); //se obtiene el index
        if(ind+2<R.getRegla().size()){//si no se pasa regresa el conjunto actual
            // System.out.println("+{{{{{"+R.getRegla().get(ind+2));
            HashSet<String> a = new HashSet<>();
            a.add(R.getRegla().get(ind+2));
            return a; 
        }
        return R.getConjunto(); //si se pasa
    }


    /**
     * Metodo que se encarga de verificar en que estados se encuentra el simbolo ingresado despues del item. Cuando lo
     * anterior se cumple se realiza un desplazamiento del item y se agrega a un conjunto
     * @param Sj Conjunto de estados que se revisaran
     * @param simbolo cadena a buscar dentro de las listas
     * @return se retorna una lista con los estados encontrados y actualizados donde se cumplido la condicion
     */
    public LinkedList<ReglaConjunto> moverA(LinkedList<ReglaConjunto> Sj, String simbolo){
        LinkedList<ReglaConjunto> conjEstados = new LinkedList<>();
        ReglaConjunto listaAux;
        int itemIndex;
        for(ReglaConjunto elementos: Sj){
            itemIndex = elementos.getRegla().indexOf(gramatica.getItem());
            //se verifica que el item no se encuentre al final de la regla
            if(itemIndex != elementos.getRegla().size()-1){
                //se verifica si el elemento despues del item es igual al simbolo ingresado
                if(elementos.getRegla().get(itemIndex+1).equals(simbolo)){
                    listaAux = new ReglaConjunto();
                    //se intercambia posicion del item con el simbolo de la derecha
                    listaAux.setRegla(swapItemPosition(elementos.getRegla(),itemIndex));
                    listaAux.setConjunto(elementos.getConjunto());
                    if(!conjEstados.contains(listaAux)){
                        conjEstados.add(listaAux);
                    }
                }
            }
        }
        return conjEstados;
    }
    /**
     * Metodo que implementa la cerradura de moverA con un simbolo especifico
     * @param Sj Conjunto de estados a revisar
     * @param simbolo Simbolo a evaluar
     * @return se retorna un conjunto de transiciones
     */
    public LinkedList<ReglaConjunto> irA(LinkedList<ReglaConjunto> Sj, String simbolo){
        LinkedList<ReglaConjunto> resultado = new LinkedList<>();
        resultado = cerradura(moverA(Sj,simbolo));
        return resultado;
    }

    /**
     * Metodo para intercambiar la posicion de un item con el simbolo que se encuentre en su lado derecho
     * @param derivacion lista que contiene el item que se desea intercambiar
     * @param indice indice del item
     * @return se retorna una lista con la actualizacion del intercambio del item
     */
    public LinkedList<String> swapItemPosition(LinkedList<String> derivacion,int indice){
        String[] arrAux = new String[derivacion.size()];
        arrAux = derivacion.toArray(arrAux);
        arrAux[indice] = arrAux[indice+1];
        arrAux[indice+1] = gramatica.getItem();
        return new LinkedList<String>(Arrays.asList(arrAux));
    }


    /**
     * Metodo para mostrar en pantalla los elementos que conforman la lista de transiciones
     * @param itemList recibe como parametro la lista de elementos que se desea mostrar
     */
    public void imprimeItems2(LinkedList<ReglaConjunto> itemList){
        for (ReglaConjunto lista1 : itemList) {
            int bandera = 0;
            LinkedList<String> lista = lista1.getRegla();
            for (String cad : lista) {
                if(bandera == 0){
                    System.out.print(cad+"->");    
                    bandera++;
                }else{
                    System.out.print(" " + cad);
                }
                
            }

            HashSet<String> h = lista1.getConjunto();
            System.out.print("\t\t\t[ ");
            for (String cad : h) {
                    System.out.print(cad+"  ");
                }
            System.out.println(" ] ");
        }
    }

    public void imprimeItems(LinkedList<LinkedList<String>> itemList){
        int bandera = 0;
        for (LinkedList<String> lista : itemList) {
            for (String cad : lista) {
                if(bandera == 0){
                    System.out.print(cad+"->");    
                    bandera++;
                }else{
                    System.out.print(" " + cad);
                }
                
            }
            bandera = 0;
            System.out.println();
        }
    }

    public void imprimeRegla(LinkedList<String> itemList){
        int bandera = 0;
        for (String cad : itemList) {
            if(bandera == 0){
                System.out.print(cad+"->");    
                bandera++;
            }else{
                System.out.print(" " + cad);
            }
            
        }
        bandera = 0;
        System.out.println();
        
    }

    /**
     * Metodo encargado de realizar la busqueda de simbolos que se encuentren despues del item para ser evaluados posteriormente
     * @param itemList recibe como parametro la lista de listas que se desea verificar
     * @return retorna una lista de simbolos que hayan cumplido con la condicion
     */
    public LinkedList<String> encuentraSimbolos(LinkedList<ReglaConjunto> itemList){
        LinkedList<String> simbolos = new LinkedList<>();
        int itemIndex;
        for(ReglaConjunto listas2: itemList){
            LinkedList<String> listas = listas2.getRegla();
            itemIndex = listas.indexOf(gramatica.getItem());
            if(itemIndex != listas.size()-1){
                if(!simbolos.contains(listas.get(itemIndex+1))){
                    simbolos.add(listas.get(itemIndex+1));
                }

            }
        }
        return simbolos;
    }

    public void inicializaRenglon(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = "";
        }

    }

    /**
     * Metodo para asignar un indice de columna para la evaluacion de la tabla LR1 con base
     * en la posicion del terminal o no terminal que se este revisando
     * @param arreglo
     * @param valor
     * @return
     */
    public int asignarColumna(String[] arreglo,String valor){
        int indice = -1;
        for(int i = 0; i < arreglo.length; i++){
            if(arreglo[i].equals(valor)){
                indice = i;
                break;
            }
        }
        return indice;
    }

 

    public void asignarReducciones(LinkedList<ReglaConjunto> Sj, String[] columnas,String[] renglon){
        HashSet<String> resFollow;
        int numRegla = 0;
        String[] arregloAux;
        for(ReglaConjunto listas2: Sj){
            LinkedList<String> listas = listas2.getRegla();
            if(listas.get(listas.size()-1) == gramatica.getItem()){
                resFollow = new HashSet<String>();
                if(listas.get(0) == gramatica.getAumentado()){
                    renglon[asignarColumna(columnas, "$")] = "acc";
                }else{
                    arregloAux = new String[listas.size()];
                    resFollow = gramatica.follow(listas.get(0));
                    listas.toArray(arregloAux);
                    String[] nuevoArr = new String[listas.size()-1];
                    nuevoArr = Arrays.copyOfRange(arregloAux, 0, listas.size()-1);
                    numRegla = gramatica.numeroDeRegla(new LinkedList<String>(Arrays.asList(nuevoArr)));
                    for(String sim: listas2.getConjunto()){
                        renglon[asignarColumna(columnas, sim)] = "r"+Integer.toString(numRegla);
                    }
                }
            }
        }
    }


    public boolean existe(Set<LinkedList<ReglaConjunto>> a,LinkedList<ReglaConjunto> b){
        // System.out.println("------------------------------------");
        // imprimeItems2(b);
        // System.out.println(":------------------------------------");
        for(LinkedList<ReglaConjunto> r:a){
            // imprimeItems2(r);
            // System.out.println();
            int bandera = 0;
            for(int m=0;m<r.size();m++){
                // imprimeRegla(r.get(m).getRegla());
                // imprimeRegla(b.get(m).getRegla());

                if(r.get(m).getRegla().equals(b.get(m).getRegla())){
                    // System.out.println("chi");
                    if(r.get(m).getConjunto().equals(b.get(m).getConjunto())){
                        // System.out.println("chi");
                        bandera++;
                    }
                }else{
                    break;
                }
            }
            if(bandera==r.size()){
                // System.out.println("si");
                return true;
            }
            // System.out.println("no "+bandera);
            
        }
        return false;
    }

    public Integer obtener(LinkedHashMap<LinkedList<ReglaConjunto>, Integer> memoria,LinkedList<ReglaConjunto> b){
        Set<LinkedList<ReglaConjunto>> a = memoria.keySet();
        // System.out.println("------------------------------------");
        // imprimeItems2(b);
        // System.out.println(":------------------------------------");
        for(LinkedList<ReglaConjunto> r:a){
            // imprimeItems2(r);
            // System.out.println();
            int bandera = 0;
            for(int m=0;m<r.size();m++){
                // imprimeRegla(r.get(m).getRegla());
                // imprimeRegla(b.get(m).getRegla());

                if(r.get(m).getRegla().equals(b.get(m).getRegla())){
                    // System.out.println("chi");
                    if(r.get(m).getConjunto().equals(b.get(m).getConjunto())){
                        // System.out.println("chi");
                        bandera++;
                    }
                }else{
                    break;
                }
            }
            if(bandera==r.size()){
                // System.out.println("si");
                return memoria.get(r);
            }
            // System.out.println("no "+bandera);
            
        }
        return -1;
    }




    public LinkedList<String[]> tablaLR1() {
        // estado sj contiene n derivaciones
        
        Queue<LinkedList<ReglaConjunto>> cola = new LinkedList<>();

        Queue<LinkedList<ReglaConjunto>> marcados = new LinkedList<>();
        LinkedHashMap<LinkedList<ReglaConjunto>, Integer> memoria = new LinkedHashMap<>();

        LinkedList<ReglaConjunto> S = new LinkedList<>();
        LinkedList<ReglaConjunto> SAux = new LinkedList<>();
        //se guardan elementos finales de la gramatica
        gramatica.guardarNoTerminales();
        gramatica.guardarTerminales();
        //se crea la columna de terminales y no terminales con la que se mapearan las posiciones en la tabla
        columnas = gramatica.generarColumnas();        
        //int[] renglon = new int[columnas.length];
        String[] renglon = new String[getColumnas().length];

        //se agrega un estado a la gramatica
        gramatica.gramaticaAumentada();
        gramatica.iniciarItems();

        LinkedList<String> listaAux;
        
        LinkedList<String> simbolos;

        //se obtiene lista inicial que se analizara para obtener S0
        listaAux = gramatica.getMapa().get(gramatica.getAumentado()).get(0);
        //se agrega simbolo de la izquierda de la gramatica aumentada
        listaAux.addFirst(gramatica.getAumentado());
        // listaAux = swapItemPosition(listaAux, listaAux.indexOf(gramatica.getItem()));
        ReglaConjunto a = new ReglaConjunto();
        HashSet<String> conjAux = new HashSet<>();
        conjAux.add("$");
        a.setConjunto(conjAux);
        a.setRegla(listaAux);
        S.add(a);


        //obtencion de S0
        S = cerradura(S);

        imprimeItems2(S);
        // String s = "E";
        // SAux = irA(S,s);
        // System.out.println("moviendo con "+s);
        // imprimeItems2(SAux);

        cola.add(S);
         int indice = 1;
        
        //LinkedList<int[]> tablaLR1 = new LinkedList<>();
        tablaLR1 = new LinkedList<>();
        while(!cola.isEmpty()){
            S = cola.remove();
            System.out.println("------------------------------------------"+indice);
            imprimeItems2(S);
            System.out.println("------------------------------------------");
            SAux = new LinkedList<>();
            if(!marcados.contains(S)){
                //se realiza busqueda de simbolos que se encuentren despues del item
                simbolos = encuentraSimbolos(S);
                renglon = new String[getColumnas().length];
                inicializaRenglon(renglon);
                for(String s: simbolos){
                    System.out.println("Con "+s);
                    SAux = irA(S,s);
                    if(SAux.size() > 0){
                        //se hace manejo de memoria con HashMap para obtener correctamente los indices de la tabla
                        
                        if(!existe(memoria.keySet(),SAux)){
                            memoria.put(SAux, indice++);
                            System.out.println("al "+indice);

                            if(gramatica.getNoTerminales().contains(s)){
                                renglon[asignarColumna(getColumnas(), s)] = Integer.toString(memoria.get(SAux));
                            }else{
                                //se agrega al inicio indicador de desplazamiento
                                System.out.println("Antes" + renglon[asignarColumna(getColumnas(), s)]);
                                renglon[asignarColumna(getColumnas(), s)] = "d"+Integer.toString(memoria.get(SAux));
                            }
                            
                            // se guarda estado en la cola de los que no han sido analizados
                            cola.add(SAux);
                        }
                        else{


                            if(gramatica.getNoTerminales().contains(s)){
                                //renglon[asignarColumna(columnas, s)] = Integer.toString(memoria.get(SAux));
                                renglon[asignarColumna(getColumnas(), s)] = Integer.toString(obtener(memoria,SAux));

                            }else{
                                System.out.println("Antes" + renglon[asignarColumna(getColumnas(), s)]);
                                //renglon[asignarColumna(columnas, s)] = "d"+Integer.toString(memoria.get(SAux));
                                renglon[asignarColumna(getColumnas(), s)] = "d"+Integer.toString(obtener(memoria,SAux));

                            }                        
                        }
                    }
                }
                asignarReducciones(S, getColumnas(), renglon);
                marcados.add(S);
                tablaLR1.add(renglon);
            }
        }

        int i;
        for(i = 0; i < getColumnas().length; i++){
            System.out.print(getColumnas()[i]+"\t");
        }
        System.out.println("");
        for(String[] arr: tablaLR1){
            for(i = 0; i < arr.length; i++){
                System.out.print(arr[i]+"\t");
            }
            System.out.println("");

        }
        // gramatica.mostrarMapa();
        // gramatica.mostrarMapaSinItems();
        // System.out.println("FOLLOW");
        // HashSet<String> res = gramatica.follow("F");
        // res.forEach(System.out::println);

        // for(int j=0;j<9;j++){
        //    // LinkedList<String> m =gramatica.obtenerRegla(j);
        //    // m = new LinkedList( m.subList(1, m.size()));
        //     for(String s:m){
        //         System.out.print(s+" ");
        //     }
        //     System.out.print("\n");
        // }
        //System.out.print(asignarColumna(columnas,"F"));

        return tablaLR1;
    }

    public void analizarCadena(LinkedList<String> aux2){
        LinkedList<String> s1=new LinkedList<>();

        LinkedList<Integer> s2=new LinkedList<>();
        s2.add(0);


        
        LinkedList<String> aux3 = new LinkedList<>();
        int m;
        String pr[];
        String auxm ="";
        System.out.println("Inicio");

        try{
            while(true){
                System.out.println("\t"+s2+" "+s1+" "+"  cadena:"+aux2);
                m= asignarColumna(getColumnas(),aux2.getFirst());
                pr = tablaLR1.get(s2.getLast());
                //System.out.println(pr[m]+" ---- "+m+"  "+aux2.getFirst());

                if(s2.getLast()==1 && aux2.getFirst()=="$"){
                    System.out.print("Siiii");
                    break;
                }
                auxm = pr[m];
                int v = Integer.valueOf(auxm.substring(1,auxm.length()));
                System.out.println("\tAccion "+pr[m]);
                if(pr[m].charAt(0)=='d'){
                    s1.add(aux2.getFirst());
                    s2.add(v);
                    aux2.removeFirst();
                }else{
                    int tamm = gramatica.obtenerRegla(v).size()-1;
                    //System.out.println("Tam "+tamm);
                    for(int t=0;t<tamm;t++){
                        s2.removeLast();
                        s1.removeLast();
                    }
                    s1.add(gramatica.obtenerRegla(v).get(0));
                    m= asignarColumna(getColumnas(),s1.getLast());
                    pr = tablaLR1.get(s2.getLast());
                    auxm = pr[m];
                    //System.out.println("\t"+auxm+" ---- "+m+"  "+s1.getLast());
                    v = Integer.valueOf(auxm);
                    s2.add(v);
                }
                //System.out.println("\nInicio:\n"+s2+" "+s1+" "+"  cadena:"+aux2);
            }
        }catch(StringIndexOutOfBoundsException e){
            System.out.println("\nError cadena no aceptada");            
        }
    }

    //num + - * / ( )
    public AnalizadorLexico generarAnalizador(){

        AFN token10 = new AFN();
        AFN token20 = new AFN();
        AFN token30 = new AFN();
        AFN token40 = new AFN();
        AFN token50 = new AFN();
        AFN token60 = new AFN();
        AFN token70 = new AFN();
        AFN a = new AFN();
        AFN b = new AFN();

        token10.basico('n');
        a = new AFN();
        a.basico('u');
        token10.concatenar(a);
        a = new AFN();
        a.basico('m');
        token10.concatenar(a);
        token10.setToken(10);

        token20.basico('+');
        token20.setToken(20);

        token30.basico('-');
        token30.setToken(30);

        token40.basico('*');
        token40.setToken(40);

        token50.basico('/');
        token50.setToken(50);

        token60.basico('(');
        token60.setToken(60);

        token70.basico(')');
        token70.setToken(70);



        ArrayList<AFN> automatas = new ArrayList<AFN>();
        automatas.add(token10);
        automatas.add(token20);
        automatas.add(token30);
        automatas.add(token40);
        automatas.add(token50);
        automatas.add(token60);
        automatas.add(token70);
        token10.unirAL(automatas);

        AFD afd = new AFD();
        afd.convertirAFN(token10);

        AnalizadorLexico lex = new AnalizadorLexico();

        lex.setAFD(afd);
        return lex;
   }

    public int analizarCadena(String cadena, LinkedList<String[]> datosTabla){

        LinkedList<String> s1=new LinkedList<>();
        LinkedList<Integer> s2=new LinkedList<>();
        s2.add(0);

        AnalizadorLexico les = generarAnalizador();
        les.setCadena(cadena);
        
        LinkedList<String> aux2 = new LinkedList<>();
        int tokaux = 0;
        while((tokaux = les.getToken()) != 1000 && tokaux != -1){ //el 0 es de termino -1 de error
                System.out.println(tokaux);
                String s="";
                switch(tokaux){
                    case 10:
                        s="num";
                    break;
                    case 20:
                        s="+";
                    break;
                    case 30:
                        s="-";
                    break;
                    case 40:
                        s="*";
                    break;
                    case 50:
                        s="/";
                    break;
                    case 60:
                        s="(";
                    break;
                    case 70:
                        s=")";
                    break;
                }
                aux2.add(s);
        }
        if(tokaux==-1)
            return -1;
        aux2.add("$");

        LinkedList<String> aux3 = new LinkedList<>();
        int m;
        String pr[];
        String auxm ="";
        System.out.println("Inicio");


        String[] auxTabla;
        try{
            while(true){
                auxTabla = new String[3];
                auxTabla[0] = s2.toString()+" "+s1.toString();
                auxTabla[1] = aux2.toString();
                System.out.println("\t"+s2+" "+s1+" "+"  cadena:"+aux2);
                m= asignarColumna(getColumnas(),aux2.getFirst());
                pr = tablaLR1.get(s2.getLast());
                //System.out.println(pr[m]+" ---- "+m+"  "+aux2.getFirst());

                if(s2.getLast()==1 && aux2.getFirst()=="$"){
                    auxTabla[2] = "acc";
                    datosTabla.add(auxTabla);
                    System.out.print("Siiii");
                    return 0;
                }
                auxm = pr[m];
                int v = Integer.valueOf(auxm.substring(1,auxm.length()));
                auxTabla[2] = pr[m];
                datosTabla.add(auxTabla);
                System.out.println("\tAccion "+pr[m]);
                if(pr[m].charAt(0)=='d'){
                    s1.add(aux2.getFirst());
                    s2.add(v);
                    aux2.removeFirst();
                }else{
                    int tamm = gramatica.obtenerRegla(v).size()-1;
                    //System.out.println("Tam "+tamm);
                    for(int t=0;t<tamm;t++){
                        s2.removeLast();
                        s1.removeLast();
                    }
                    s1.add(gramatica.obtenerRegla(v).get(0));
                    m= asignarColumna(getColumnas(),s1.getLast());
                    pr = tablaLR1.get(s2.getLast());
                    auxm = pr[m];
                    //System.out.println("\t"+auxm+" ---- "+m+"  "+s1.getLast());
                    v = Integer.valueOf(auxm);
                    s2.add(v);
                }
                //System.out.println("\nInicio:\n"+s2+" "+s1+" "+"  cadena:"+aux2);
            }
        }catch(StringIndexOutOfBoundsException e){
            System.out.println("\nError cadena no aceptada");
            return -1;
        }
    }
}