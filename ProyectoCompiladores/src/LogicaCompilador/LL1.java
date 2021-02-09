package LogicaCompilador;

import java.util.*;

public class LL1 {
    private Hashtable<Character, String> gramatica;
    private HashSet<Character> noTerminal;
    private Character sInicial;

    public LL1() {
        gramatica = new Hashtable<Character, String>();
        noTerminal = new HashSet<Character>();
    }

    public void setSInicial(Character s) {
        sInicial = s;
    }

    public void agregarRegla(Character c, String s) {
        gramatica.put(c, s); // agrego a la gramatica
        noTerminal.add(c); // agrego a los no terminales
    }

    public void setGramatica(Hashtable<Character, String> g) {
        gramatica = g;
        noTerminal.clear();
        Set<Character> keys = gramatica.keySet(); // se checa si está el conjunto ya
        for (Character c : keys) {
            noTerminal.add(c); // agrego a los no terminales
        }
    }

    public HashSet<Character> getNoTerminales() {
        return noTerminal;
    }

    public Set<Character> first(String R) {
        Set<Character> conjunto = new HashSet<Character>();

        if (!noTerminal.contains(R.charAt(0))) { // si es terminal
            conjunto.add(R.charAt(0)); // se agrega el caracter al conjunto
            return conjunto;
        }

        String[] reglas = gramatica.get(R.charAt(0)).split("\\|"); // Obtiene y separa la regla

        for (String regla : reglas)
            conjunto.addAll(first(regla));

        if (conjunto.contains('#') && R.length() > 1) {
            conjunto.remove('#');
            conjunto.addAll(first(R.substring(1, R.length())));
        }
        return conjunto;
    }

    public Set<Character> follow(Character R) { // R es la regla a la que se quiere aplicar
        Set<Character> conjunto = new HashSet<Character>();
        if (R == sInicial) { // si R es inicial
            conjunto.add('$');
        }
        // System.out.println("\nFollow de "+R+":");

        for (Character c : noTerminal) { // busca en cada regla
            String a = gramatica.get(c);
            if (a.contains(R + "")) { // si R esta en el lado derecho de la regla

                int inicio = a.indexOf(R + "", 0); // busca en que parte esta
                String reglas[] = a.substring(inicio, a.length()).split("\\|"); // se parte la cadena ya que puede tener
                                                                                // uniones
                String regla = reglas[0]; // se toma la regla donde R esta contenida
                if (regla.length() == 1) { // no tiene nada despues
                    if (R != c) { // si no es el mismo que ya se esta evaluando
                        conjunto.addAll(follow(c));
                    }
                } else { // si tiene algo despues

                    regla = regla.substring(1, regla.length()); // corta el inicio
                    conjunto.addAll(first(regla)); // saca first
                    if (conjunto.contains('#')) { // si tiene epsilon, se hace follow al no terminal
                        conjunto.remove('#'); // se quita epsilon
                        conjunto.addAll(follow(c));
                    }
                }
            }
        }
        return conjunto;
    }

}