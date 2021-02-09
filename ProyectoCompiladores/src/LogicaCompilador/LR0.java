package LogicaCompilador;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;

/**
 * LR0
 */
public class LR0 {

    /**
     * @return the columnas
     */
    public String[] getColumnas() {
        return columnas;
    }
    private ListaDeListas gramatica;
    private String[] columnas;
    private LinkedList<String[]> tablaResultado;

    public LR0(ListaDeListas gramatica) {
        this.gramatica = gramatica;
    }

    /**
     * Metodo que se encarga de realizar la cerradura de un conjunto de estados
     * (la cerradura consiste en verificar los elementos contenidos en los
     * estados y las reglas asociadas de los terminales que se encuentren
     * despues del item)
     *
     * @param estados conjunto de estados a los que se aplicara la cerradura
     * @return se retorna una lista de listas que contiene los elementos que
     * conforman la cerradura
     */
    public LinkedList<LinkedList<String>> cerradura(LinkedList<LinkedList<String>> estados) {
        LinkedList<LinkedList<String>> conjCerradura = new LinkedList<>();
        int itemIndex;
        String edoAux = "";
        String edoActual = "";
        /*
         * Se crea una pila para guardar los terminales que se encuentren despues de
         * cada item y otra que funcionara como memoria para corroborar si algun estado
         * ya fue agregado a la cerradura
         */
        Stack<String> pila = new Stack<>();
        Stack<String> memoria = new Stack<>();
        // se obtienen los primeros terminales a revisar del conjunto recibido
        for (LinkedList<String> listas : estados) {
            conjCerradura.add(listas);
            // se verifica posicion del item y se obtiene el simbolo que se encuentre una
            // posicion despues
            itemIndex = listas.indexOf(gramatica.getItem());
            if (itemIndex != listas.size() - 1) {
                edoAux = listas.get(itemIndex + 1);
            }
            // se verifica que el simbolo no este en ninguna de las pilas y que sea un
            // no terminal
            if (!pila.contains(edoAux) && !memoria.contains(edoAux) && gramatica.getNoTerminales().contains(edoAux)) {
                pila.add(edoAux);
                memoria.add(edoAux);
            }
        }
        // se crea lista provisional donde se juntara el simbolo de la izquierda con sus
        // derivaciones
        LinkedList<String> listaProv;

        // se realiza un ciclo mientras la pila no este vacia (hasta que ya no haya
        // no terminales que aparezcan despues del punto que se deban analizar)
        while (!pila.isEmpty()) {
            edoActual = pila.pop();
            // Se obtienen las derivaciones que tenga el estado removido de la pila
            // en el mapa de reglas
            for (LinkedList<String> conjReglas : gramatica.getMapa().get(edoActual)) {
                listaProv = new LinkedList<>();
                listaProv = conjReglas;
                // se agrega el estado de la izquierda a la lista
                if (!conjCerradura.contains(listaProv)) {
                    //si el item esta al principio se agrega simbolo de la izquierda a la lista
                    if (listaProv.get(0).equals(gramatica.getItem())) {
                        listaProv.addFirst(edoActual);
                    }
                    conjCerradura.add(listaProv);

                }
            }
            // se verifica que en los nuevos elementos de la cerradura no exista un simbolo
            // despues del item que no se haya verificado
            for (LinkedList<String> elementos : conjCerradura) {
                itemIndex = elementos.indexOf(gramatica.getItem());
                if (itemIndex != elementos.size() - 1) {
                    edoAux = elementos.get(itemIndex + 1);
                }
                if (!pila.contains(edoAux) && !memoria.contains(edoAux) && gramatica.getNoTerminales().contains(edoAux)) {
                    pila.add(edoAux);
                    memoria.add(edoAux);
                }
            }
        }
        return conjCerradura;
    }

    /**
     * Metodo que se encarga de verificar en que estados se encuentra el simbolo
     * ingresado despues del item. Cuando lo anterior se cumple se realiza un
     * desplazamiento del item y se agrega a un conjunto
     *
     * @param Sj Conjunto de estados que se revisaran
     * @param simbolo cadena a buscar dentro de las listas
     * @return se retorna una lista con los estados encontrados y actualizados
     * donde se cumplido la condicion
     */
    public LinkedList<LinkedList<String>> moverA(LinkedList<LinkedList<String>> Sj, String simbolo) {
        LinkedList<LinkedList<String>> conjEstados = new LinkedList<>();
        LinkedList<String> listaAux;
        int itemIndex;
        for (LinkedList<String> elementos : Sj) {
            itemIndex = elementos.indexOf(gramatica.getItem());
            //se verifica que el item no se encuentre al final de la regla
            if (itemIndex != elementos.size() - 1) {
                //se verifica si el elemento despues del item es igual al simbolo ingresado
                if (elementos.get(itemIndex + 1).equals(simbolo)) {
                    listaAux = new LinkedList<>();
                    //se intercambia posicion del item con el simbolo de la derecha
                    listaAux = swapItemPosition(elementos, itemIndex);
                    if (!conjEstados.contains(listaAux)) {
                        conjEstados.add(listaAux);
                    }
                }
            }
        }
        return conjEstados;
    }

    /**
     * Metodo que implementa la cerradura de moverA con un simbolo especifico
     *
     * @param Sj Conjunto de estados a revisar
     * @param simbolo Simbolo a evaluar
     * @return se retorna un conjunto de transiciones
     */
    public LinkedList<LinkedList<String>> irA(LinkedList<LinkedList<String>> Sj, String simbolo) {
        LinkedList<LinkedList<String>> resultado = new LinkedList<>();
        resultado = cerradura(moverA(Sj, simbolo));
        return resultado;
    }

    /**
     * Metodo para intercambiar la posicion de un item con el simbolo que se
     * encuentre en su lado derecho
     *
     * @param derivacion lista que contiene el item que se desea intercambiar
     * @param indice indice del item
     * @return se retorna una lista con la actualizacion del intercambio del
     * item
     */
    public LinkedList<String> swapItemPosition(LinkedList<String> derivacion, int indice) {
        String[] arrAux = new String[derivacion.size()];
        arrAux = derivacion.toArray(arrAux);
        arrAux[indice] = arrAux[indice + 1];
        arrAux[indice + 1] = gramatica.getItem();
        return new LinkedList<String>(Arrays.asList(arrAux));
    }

    /**
     * Metodo para mostrar en pantalla los elementos que conforman la lista de
     * transiciones
     *
     * @param itemList recibe como parametro la lista de elementos que se desea
     * mostrar
     */
    public void imprimeItems(LinkedList<LinkedList<String>> itemList) {
        int bandera = 0;
        for (LinkedList<String> lista : itemList) {
            for (String cad : lista) {
                if (bandera == 0) {
                    System.out.print(cad + "->");
                    bandera++;
                } else {
                    System.out.print(" " + cad);
                }

            }
            bandera = 0;
            System.out.println();
        }
    }

    /**
     * Metodo encargado de realizar la busqueda de simbolos que se encuentren
     * despues del item para ser evaluados posteriormente
     *
     * @param itemList recibe como parametro la lista de listas que se desea
     * verificar
     * @return retorna una lista de simbolos que hayan cumplido con la condicion
     */
    public LinkedList<String> encuentraSimbolos(LinkedList<LinkedList<String>> itemList) {
        LinkedList<String> simbolos = new LinkedList<>();
        int itemIndex;
        for (LinkedList<String> listas : itemList) {
            itemIndex = listas.indexOf(gramatica.getItem());
            if (itemIndex != listas.size() - 1) {
                if (!simbolos.contains(listas.get(itemIndex + 1))) {
                    simbolos.add(listas.get(itemIndex + 1));
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
     * Metodo para asignar un indice de columna para la evaluacion de la tabla
     * LR0 con base en la posicion del terminal o no terminal que se este
     * revisando
     *
     * @param arreglo
     * @param valor
     * @return
     */
    public int asignarColumna(String[] arreglo, String valor) {
        int indice = -1;
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i].equals(valor)) {
                indice = i;
                break;
            }
        }
        return indice;
    }

    public void asignarReducciones(LinkedList<LinkedList<String>> Sj, String[] columnas, String[] renglon) {
        HashSet<String> resFollow;
        int numRegla = 0;
        String[] arregloAux;
        for (LinkedList<String> listas : Sj) {
            if (listas.get(listas.size() - 1) == gramatica.getItem()) {
                resFollow = new HashSet<String>();
                if (listas.get(0) == gramatica.getAumentado()) {
                    renglon[asignarColumna(columnas, "$")] = "acc";
                } else {
                    arregloAux = new String[listas.size()];
                    resFollow = gramatica.follow(listas.get(0));
                    listas.toArray(arregloAux);
                    String[] nuevoArr = new String[listas.size() - 1];
                    nuevoArr = Arrays.copyOfRange(arregloAux, 0, listas.size() - 1);
                    numRegla = gramatica.numeroDeRegla(new LinkedList<String>(Arrays.asList(nuevoArr)));
                    for (String sim : resFollow) {
                        renglon[asignarColumna(columnas, sim)] = "r" + Integer.toString(numRegla);
                    }
                }
            }
        }
    }

    public LinkedList<String[]> tablaLR0() {
        // estado sj contiene n derivaciones
        Queue<LinkedList<LinkedList<String>>> cola = new LinkedList<>();

        Queue<LinkedList<LinkedList<String>>> marcados = new LinkedList<>();
        LinkedHashMap<LinkedList<LinkedList<String>>, Integer> memoria = new LinkedHashMap<>();

        LinkedList<LinkedList<String>> S = new LinkedList<>();
        LinkedList<LinkedList<String>> SAux = new LinkedList<>();
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

        S.add(listaAux);

        //obtencion de S0
        S = cerradura(S);
        imprimeItems(S);
        cola.add(S);
        int indice = 1;

        //LinkedList<int[]> tablaLR0 = new LinkedList<>();
        tablaResultado = new LinkedList<>();
        while (!cola.isEmpty()) {
            S = cola.remove();
            System.out.println("------------------------------------------");
            imprimeItems(S);
            System.out.println("------------------------------------------");
            SAux = new LinkedList<>();
            if (!marcados.contains(S)) {
                //se realiza busqueda de simbolos que se encuentren despues del item
                simbolos = encuentraSimbolos(S);
                renglon = new String[getColumnas().length];
                inicializaRenglon(renglon);
                for (String s : simbolos) {
                    SAux = irA(S, s);
                    if (SAux.size() > 0) {
                        //se hace manejo de memoria con HashMap para obtener correctamente los indices de la tabla
                        if (memoria.get(SAux) == null) {
                            memoria.put(SAux, indice++);
                            if (gramatica.getNoTerminales().contains(s)) {
                                renglon[asignarColumna(getColumnas(), s)] = Integer.toString(memoria.get(SAux));
                            } else {
                                //se agrega al inicio indicador de desplazamiento
                                renglon[asignarColumna(getColumnas(), s)] = "d" + Integer.toString(memoria.get(SAux));
                            }

                            // se guarda estado en la cola de los que no han sido analizados
                            cola.add(SAux);
                        } else if (memoria.get(SAux) != null) {
                            if (gramatica.getNoTerminales().contains(s)) {
                                renglon[asignarColumna(getColumnas(), s)] = Integer.toString(memoria.get(SAux));
                            } else {
                                renglon[asignarColumna(getColumnas(), s)] = "d" + Integer.toString(memoria.get(SAux));
                            }
                        }
                    }
                }
                asignarReducciones(S, getColumnas(), renglon);
                marcados.add(S);
                tablaResultado.add(renglon);
            }

        }
        int i;
        for (i = 0; i < getColumnas().length; i++) {
            System.out.print(getColumnas()[i] + "\t");
        }
        System.out.println("");
        for (String[] arr : tablaResultado) {
            for (i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + "\t");
            }
            System.out.println("");

        }
        // gramatica.mostrarMapa();
        // gramatica.mostrarMapaSinItems();
        // System.out.println("FOLLOW");
        // HashSet<String> res = gramatica.follow("F");
        // res.forEach(System.out::println);

        return tablaResultado;
    }

    public void analizarCadena(LinkedList<String> aux2) {
        LinkedList<String> s1 = new LinkedList<>();

        LinkedList<Integer> s2 = new LinkedList<>();
        s2.add(0);

        LinkedList<String> aux3 = new LinkedList<>();
        int m;
        String pr[];
        String auxm = "";
        System.out.println("Inicio");

        try {
            while (true) {
                System.out.println("\t" + s2 + " " + s1 + " " + "  cadena:" + aux2);
                m = asignarColumna(getColumnas(), aux2.getFirst());
                pr = tablaResultado.get(s2.getLast());
                //System.out.println(pr[m]+" ---- "+m+"  "+aux2.getFirst());

                if (s2.getLast() == 1 && aux2.getFirst() == "$") {
                    System.out.print("Siiii");
                    break;
                }
                auxm = pr[m];
                int v = Integer.valueOf(auxm.substring(1, auxm.length()));
                System.out.println("\tAccion " + pr[m]);
                if (pr[m].charAt(0) == 'd') {
                    s1.add(aux2.getFirst());
                    s2.add(v);
                    aux2.removeFirst();
                } else {
                    int tamm = gramatica.obtenerRegla(v).size() - 1;
                    //System.out.println("Tam "+tamm);
                    for (int t = 0; t < tamm; t++) {
                        s2.removeLast();
                        s1.removeLast();
                    }
                    s1.add(gramatica.obtenerRegla(v).get(0));
                    m = asignarColumna(getColumnas(), s1.getLast());
                    pr = tablaResultado.get(s2.getLast());
                    auxm = pr[m];
                    //System.out.println("\t"+auxm+" ---- "+m+"  "+s1.getLast());
                    v = Integer.valueOf(auxm);
                    s2.add(v);
                }
                //System.out.println("\nInicio:\n"+s2+" "+s1+" "+"  cadena:"+aux2);
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("\nError cadena no aceptada");
        }
    }
//num + - * / ( )

    public AnalizadorLexico generarAnalizador() {

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

    public int analizarCadena(String cadena,LinkedList<String[]> datosTabla) {

        LinkedList<String> s1 = new LinkedList<>();
        LinkedList<Integer> s2 = new LinkedList<>();
        s2.add(0);

        AnalizadorLexico les = generarAnalizador();
        les.setCadena(cadena);

        LinkedList<String> aux2 = new LinkedList<>();
        int tokaux = 0;
        try{
        while ((tokaux = les.getToken()) != 1000 && tokaux != -1) { //el 0 es de termino -1 de error
            System.out.println(tokaux);
            String s = "";
            switch (tokaux) {
                case 10:
                    s = "num";
                    break;
                case 20:
                    s = "+";
                    break;
                case 30:
                    s = "-";
                    break;
                case 40:
                    s = "*";
                    break;
                case 50:
                    s = "/";
                    break;
                case 60:
                    s = "(";
                    break;
                case 70:
                    s = ")";
                    break;
            }
            aux2.add(s);
        }
        }catch(Exception e){
            System.out.println("\nError cadena no aceptada");
            return -1;
        }
        if (tokaux == -1) {
            return -1;
        }
        aux2.add("$");

        LinkedList<String> aux3 = new LinkedList<>();
        int m;
        String pr[];
        String auxm = "";
        System.out.println("Inicio");
        String[] arregloAux; 
        try {
            while (true) {
                arregloAux = new String[3];
                arregloAux[0] = s2.toString() + " "+s1.toString();
                arregloAux[1] = aux2.toString();
                System.out.println("\t" + s2 + " " + s1 + " " + "  cadena:" + aux2);
                m = asignarColumna(getColumnas(), aux2.getFirst());
                pr = tablaResultado.get(s2.getLast());
                //System.out.println(pr[m]+" ---- "+m+"  "+aux2.getFirst());

                if (s2.getLast() == 1 && aux2.getFirst() == "$") {
                    arregloAux[2] = "acc";
                    datosTabla.add(arregloAux);                    
                    System.out.print("Siiii");
                    return 0;
                }
                auxm = pr[m];
                int v = Integer.valueOf(auxm.substring(1, auxm.length()));
                System.out.println("\tAccion " + pr[m]);
                arregloAux[2] = pr[m];
                datosTabla.add(arregloAux);
                if (pr[m].charAt(0) == 'd') {
                    s1.add(aux2.getFirst());
                    s2.add(v);
                    aux2.removeFirst();
                } else {
                    int tamm = gramatica.obtenerRegla(v).size() - 1;
                    //System.out.println("Tam "+tamm);
                    for (int t = 0; t < tamm; t++) {
                        s2.removeLast();
                        s1.removeLast();
                    }
                    s1.add(gramatica.obtenerRegla(v).get(0));
                    m = asignarColumna(getColumnas(), s1.getLast());
                    pr = tablaResultado.get(s2.getLast());
                    auxm = pr[m];
                    //System.out.println("\t"+auxm+" ---- "+m+"  "+s1.getLast());
                    v = Integer.valueOf(auxm);
                    s2.add(v);
                }
                //System.out.println("\nInicio:\n"+s2+" "+s1+" "+"  cadena:"+aux2);
            }
        } catch (Exception e) {
            System.out.println("\nError cadena no aceptada");
            return -1;
        }
    }
}
