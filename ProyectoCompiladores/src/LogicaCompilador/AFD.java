package LogicaCompilador;

import java.util.*;

public class AFD {

    private Estado edo_inicial;
    private Set<Character> alfabeto;
    private Set<Estado> estados;
    private Set<Estado> estadosAceptacion;

    public AFD() {
        this.alfabeto = new HashSet<Character>();
        this.estados = new HashSet<Estado>();
        this.estadosAceptacion = new HashSet<Estado>();

    }

    /*---------------------------------------------------------------------------------------------------------
                                                   Conversion AFN -> AFD
    ---------------------------------------------------------------------------------------------------------*/

    public ArrayList<Character> getAlfabetoOrdenado() {
        // paso el set a un arraylist para que tengan un orden
        ArrayList<Character> alfa = new ArrayList<Character>();
        for (Character car : alfabeto) { // recorro cada caracter en el alfabeto y lo guardo en el alfabeto (con orden)
            alfa.add(car);
        }

        return alfa;
    }

    public ArrayList<ArrayList<Integer>> tabla() {
        ArrayList<ArrayList<Integer>> tabla = new ArrayList<ArrayList<Integer>>(); // es la que regresare
        ArrayList<Integer> fila; // para ir agregando a la tabla

        Estado aux; // para guardar los movimientos

        // paso el set a un arraylist para que tengan un orden
        ArrayList<Character> alfa = getAlfabetoOrdenado();

        for (Estado e : estados) { // por cada estado en los estados del AFN
            fila = new ArrayList<Integer>();
            fila.add(e.getIdentificador()); // primero se agrega el identificador
            //System.out.println("Estado " + e.getIdentificador());

            for (Character c : alfa) {
                aux = e.moverAFD(c);
                if (aux == null) {
                   // System.out.println(c + " -> -1");
                    fila.add(-1); // si no hay movimiento se pone -1

                } else {
                    fila.add(aux.getIdentificador()); // si lo hay, se agrega el identificador del nodo al que se movio
                    // System.out.println(c+" -> "+aux.getIdentificador());
                }

            }

            if (e.getAceptacion()) {
                fila.add(1);
            } else {
                fila.add(0);
            }

            fila.add(e.getToken());

            tabla.add(fila);
        }

        return tabla;
    }

    public void convertirAFN(AFN afn) {
        int num_nodo = 0;

        alfabeto = afn.getAlfabeto(); // copio el alfabeto
        Hashtable<Estado, Set<Estado>> estadosSinRecorrer = new Hashtable<Estado, Set<Estado>>(); // servira de cola
        Hashtable<Estado, Set<Estado>> estadosNuevos = new Hashtable<Estado, Set<Estado>>(); // transiciones con
                                                                                             // caracter

        // se calcula cerradura epsilon del edo incial
        Estado actual;
        Set<Estado> conjuntoAux, conjuntoAux2;
        actual = new Estado();
        actual.setIdentificador(num_nodo++);
        edo_inicial = actual; // pongo al estado nuevo como incial (S0)
        conjuntoAux = afn.getInicial().cerraduraEpsilon(); // hago epsilon del estado inicial
        estadosNuevos.put(actual, conjuntoAux); // agrego el estado nuevo junto a el conjunto que era en el AFN
        estados.add(actual); // se agrega a los estados del AFD

        if (verificarAceptacion(actual, conjuntoAux, afn.getEstadosAceptacion())) {
            //System.out.println("El inicial es de aceptacion");
            estadosAceptacion.add(actual);
        }

        //imprimirConjuntoEstados(conjuntoAux);

        estadosSinRecorrer.put(actual, conjuntoAux); // se agrega a la cola/pila

        while (!estadosSinRecorrer.isEmpty()) { // mientras no este vacia
            actual = obtenerPrimeroH(estadosSinRecorrer);
            // imprimirConjuntoEstados(estadosNuevos.get(actual));
            estadosNuevos.put(actual, estadosSinRecorrer.get(actual)); // se agrega a los recorridos

            conjuntoAux2 = estadosSinRecorrer.get(actual); // saca el primer conjunto que tenga

            //System.out.println("Con estado " + actual.getIdentificador());
            for (Character c : alfabeto) { // por cada caracter en el alfabeto se hace irA en el estado
                //System.out.println("Moviendo con "+c);

                conjuntoAux = new HashSet<Estado>(); // con cada caracter se reinicia
                for (Estado estado : conjuntoAux2) {
                    conjuntoAux.addAll(estado.irA(c)); // se obtiene un conjunto y se une con el anterior

                }
                // imprimirConjuntoEstados(conjuntoAux2);
                //imprimirConjuntoEstados(conjuntoAux); // este conjunto es a los posibles estados desde x con el caracter
                                                      // c

                if (!conjuntoAux.isEmpty()) {
                    if (estadosNuevos.contains(conjuntoAux)) {
                        Set<Estado> keys2 = estadosNuevos.keySet(); // se checa si está el conjunto ya
                        for (Estado e2 : keys2) {
                            if (estadosNuevos.get(e2).equals(conjuntoAux)) {
                                //System.out.println("Ya existe");
                                // si ya existe, no se crea un nodo nuevo, se hace la transicion del actual al
                                // existente
                                actual.agregarTransicion(c, e2); // se agrega transicion
                            }
                            // imprimirConjuntoEstados(estadosNuevos.get(actual));
                        }
                    } else { // no existe el estado aun
                        //System.out.println("Aun no existe");
                        Estado aux = new Estado();
                        aux.setIdentificador(num_nodo++);
                        actual.agregarTransicion(c, aux); // se agrega la transicion

                        if (verificarAceptacion(aux, conjuntoAux, afn.getEstadosAceptacion())) {
                            estadosAceptacion.add(aux);
                        }
                        estadosSinRecorrer.put(aux, conjuntoAux); // se agrega a la cola/pila
                        estadosNuevos.put(aux, conjuntoAux); // se agrega a los estados existentes
                        estados.add(aux); // se agrega a los estados del AFD
                    }
                } else {
                    //System.out.println("vacio");
                }
            }
            estadosSinRecorrer.remove(actual);
        }
        
    }

    public Estado obtenerPrimero(Set<Estado> e) {
        for (Estado es : e) {
            return es;
        }
        return null;
    }

    public Estado obtenerPrimeroH(Hashtable<Estado, Set<Estado>> e) {
        Set<Estado> keys = e.keySet();
        for (Estado k : keys) {
            return k;
        }
        return null;
    }

    public Set<Estado> interseccion(Set<Estado> a, Set<Estado> b) {
        HashSet<Estado> intersection = new HashSet<Estado>(a); // se copia el conjunto 3 en intersecion
        intersection.retainAll(b); // se hace intersecion con estados de aceptacion
        return intersection;
    }

    public boolean verificarAceptacion(Estado aux, Set<Estado> a, Set<Estado> b) {
        Set<Estado> intersection = interseccion(a, b); // obtiene la intersecion
        if (!intersection.isEmpty()) { // si no esta vacio, tiene al menos un estado de aceptacion
            aux.setToken(obtenerPrimero(intersection).getToken()); // se copia el token
            aux.setAceptacion(true);
            return true;
        }
        return false;
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Agregar a conjuntos
    ---------------------------------------------------------------------------------------------------------*/

    public void agregarAlfabeto(Character c) {
        alfabeto.add(c);
    }

    public void agregarEstados(Estado e) {
        estados.add(e);
    }

    public void agregarEstadosAceptacion(Estado e) {
        estadosAceptacion.add(e);
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Imprimir
    ---------------------------------------------------------------------------------------------------------*/

    public void imprimirTransicionesAFD() {
        System.out.println("\n\nTransiciones AFD");
        for (Estado e : estados) {
            System.out.println("Transiciones nodo " + e.getIdentificador() + " final " + e.getAceptacion() + " token "
                    + e.getToken());
            e.getTransiciones().mostrarTransiciones();
            e.getTransiciones().mostrarTransicionesEpsilon();
        }

    }

    public void imprimirConjuntoEstados(Set<Estado> set) {
        // recorres el conjunto de transiciones
        for (Estado s : set) {
            System.out.println(s.getIdentificador());
        }
    }

    public void imprimirConjuntoCaracteres(Set<Character> set) {
        // recorres el conjunto de transiciones
        for (Character s : set) {
            System.out.println(" " + s);
        }
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Setters
    ---------------------------------------------------------------------------------------------------------*/

    public void setEstadoInicial(Estado e) {
        this.edo_inicial = e;
    }

    public void setAlfabeto(Set<Character> a) {
        this.alfabeto = a;
    }

    public void setEstados(Set<Estado> e) {
        estados = e;
    }

    public void setEstadosAceptacion(Set<Estado> e) {
        estadosAceptacion = e;
    }

    /*---------------------------------------------------------------------------------------------------------
                                                    Getters
    ---------------------------------------------------------------------------------------------------------*/

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public Estado getInicial() {
        return edo_inicial;
    }

    public Set<Estado> getEstadosAceptacion() {
        return estadosAceptacion;
    }

    public Set<Estado> getEstados() {
        return estados;
    }

}