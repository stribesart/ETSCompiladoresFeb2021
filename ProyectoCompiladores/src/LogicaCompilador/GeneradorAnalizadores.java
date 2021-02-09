package LogicaCompilador;

import java.util.ArrayList;

/**
 *
 * @author sandu
 */
public class GeneradorAnalizadores {
    private AnalizadorLexico lex;
    private Calculadora calc;
    private CalculadoraCientifica calcCientifica;
    private GeneradorAutomata genAutomata;
    public GeneradorAnalizadores(){
        this.lex = null;
        this.calc = null;
        this.calcCientifica = null;
        this.genAutomata = null;
    }
    
    /**
     * 
     * @param regex Cadena en forma de regex
     * @return AFN con las reglas definidas en la regex
     */
    
    public void setGeneradorAFN(){
        this.lex = reglaAutomatas();
    }
    
    
    public boolean check(String regex){
        if(this.lex == null){
            return false;
        }else{
            this.lex.setCadena(regex);
            boolean respuesta = this.genAutomata.validar();
            return respuesta;
        }
    }
    public AFN generaAFN(boolean respuesta){
        AFN automata = new AFN();
        automata = this.genAutomata.evaluar(respuesta);
        return automata;
    }
    public AnalizadorLexico reglaAutomatas(){
        AFN a = new AFN(); //se crea un AFN
        AFN b = new AFN(); //se crea un AFN     
        AFN token10 = new AFN(); //     or
        AFN token20 = new AFN(); //     conc
        AFN token30 = new AFN(); //     cerr_pos
        AFN token40 = new AFN(); //     cerr_kleen
        AFN token50 = new AFN(); //     opc
        AFN token60 = new AFN(); //     (
        AFN token70 = new AFN(); //     )
        AFN token80 = new AFN(); //     simb
        AFN token90 = new AFN(); //     [
        AFN token100 = new AFN(); //     ]
        AFN token110 = new AFN(); //     -

        token90.basico('[');
        token90.setToken(90);
        token100.basico(']');
        token100.setToken(100);
        token110.basico('-');
        token110.setToken(110);

        token10.basico('|');
        token10.setToken(10);
        token20.basico('&');
        token20.setToken(20);
        token30.basico('+');
        token30.setToken(30);
        token40.basico('*');
        token40.setToken(40);
        token50.basico('?');
        token50.setToken(50);

        token60.basico('(');
        token60.setToken(60);
        token70.basico(')');
        token70.setToken(70);

        //quito el +/-
        //token70.basico('+');
        //a.basico('-');
        //token70.unir(a);
        //token70.Question();



        //----------------------------------simbolo-------------------------------------------

        token80.basico('\\');
        a = new AFN();
        a.basico('+');
        token80.concatenar(a);
        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico('*');
        a.concatenar(b);
        token80.unir(a);
        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico('?');
        a.concatenar(b);
        token80.unir(a);


        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico('(');
        a.concatenar(b);
        token80.unir(a);
        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico(')');
        a.concatenar(b);
        token80.unir(a);


        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico('-');
        a.concatenar(b);
        token80.unir(a);

        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico(';');
        a.concatenar(b);
        token80.unir(a);        


        a = new AFN();
        a.basico('.');
        token80.unir(a);

        a = new AFN();
        a.basico('\\');
        token80.unir(a);        

        a = new AFN();
        a.basico(' ');
        token80.unir(a);
        a = new AFN();
        b = new AFN();
        a.basico('\\');
        b.basico('|');
        a.concatenar(b);
        token80.unir(a);
        a = new AFN();
        a.basico('a','z');
        token80.unir(a);
        a = new AFN();
        a.basico('A','Z');
        token80.unir(a);
        a = new AFN();
        a.basico('0','9');
        token80.unir(a);
        a = new AFN();
        a.basico('/');
        token80.unir(a);
        a = new AFN();
        a.basico('>');
        token80.unir(a);
        a = new AFN();
        a.basico(';');
        token80.unir(a);   
        a = new AFN();
        a.basico('#');
        token80.unir(a);                         
        //token80.cerraduraPositiva();
        token80.setToken(80);

       
        //unirlos
        ArrayList<AFN> automatas = new ArrayList<AFN>();
        automatas.add(token10);
        automatas.add(token20);
        automatas.add(token30);
        automatas.add(token40);
        automatas.add(token50);
        automatas.add(token60);
        automatas.add(token70);
        automatas.add(token80);
        automatas.add(token90);
        automatas.add(token100);
        automatas.add(token110);

        token10.unirAL(automatas);


        //convertir a AFD
        AFD n = new AFD();
        n.convertirAFN(token10);
        AnalizadorLexico al = new AnalizadorLexico();
        al.setAFD(n);  
        this.genAutomata = new GeneradorAutomata(al);
        return al;
    }
    
    public AnalizadorLexico reglaCalcCientifica(){
        AFN a = new AFN(); //se crea un AFN

        AFN token10 = new AFN(); //     +
        AFN token20 = new AFN(); //     -
        AFN token30 = new AFN(); //     *
        AFN token40 = new AFN(); //     /
        AFN token50 = new AFN(); //     ^
        AFN token60 = new AFN(); //     (
        AFN token70 = new AFN(); //     )
        AFN token80 = new AFN(); //     numero flotante
        AFN token90 = new AFN(); //     sin
        AFN token100 = new AFN(); //     cos
        AFN token110 = new AFN(); //     tan
        AFN token120 = new AFN(); //     exp
        AFN token130 = new AFN(); //     log
        AFN token140 = new AFN(); //     ln

        token10.basico('+');
        token10.setToken(10);
        token20.basico('-');
        token20.setToken(20);
        token30.basico('*');
        token30.setToken(30);
        token40.basico('/');
        token40.setToken(40);
        token50.basico('^');
        token50.setToken(50);

        token60.basico('(');
        token60.setToken(60);
        token70.basico(')');
        token70.setToken(70);

        //quito el +/-
        //token70.basico('+');
        //a.basico('-');
        //token70.unir(a);
        //token70.Question();
        //----------------------------------trigonometricas-------------------------------------------
        token80.basico('s');
        a = new AFN();
        a.basico('i');
        token80.concatenar(a);
        a = new AFN();
        a.basico('n');
        token80.concatenar(a);
        token80.setToken(80);

        token90.basico('c');
        a = new AFN();
        a.basico('o');
        token90.concatenar(a);
        a = new AFN();
        a.basico('s');
        token90.concatenar(a);
        token90.setToken(90);

        token100.basico('t');
        a = new AFN();
        a.basico('a');
        token100.concatenar(a);
        a = new AFN();
        a.basico('n');
        token100.concatenar(a);
        token100.setToken(100);

        token110.basico('e');
        a = new AFN();
        a.basico('x');
        token110.concatenar(a);
        a = new AFN();
        a.basico('p');
        token110.concatenar(a);
        token110.setToken(110);

        token120.basico('l');
        a = new AFN();
        a.basico('o');
        token120.concatenar(a);
        a = new AFN();
        a.basico('g');
        token120.concatenar(a);
        token120.setToken(120);

        token130.basico('l');
        a = new AFN();
        a.basico('n');
        token130.concatenar(a);
        token130.setToken(130);

        token140 = new AFN();
        token140.basico('0', '9');
        token140.cerraduraPositiva();

        //token70.concatenar(num1);
        AFN punto = new AFN();
        punto.basico('.');

        AFN num1 = new AFN();
        num1.basico('0', '9');
        num1.cerraduraPositiva();

        punto.concatenar(num1);
        punto.Question();

        token140.concatenar(punto);
        token140.setToken(140);

        //unirlos
        ArrayList<AFN> automatas = new ArrayList<AFN>();
        automatas.add(token10);
        automatas.add(token20);
        automatas.add(token30);
        automatas.add(token40);
        automatas.add(token50);
        automatas.add(token60);
        automatas.add(token70);
        automatas.add(token80);
        automatas.add(token90);
        automatas.add(token100);
        automatas.add(token110);
        automatas.add(token120);
        automatas.add(token130);
        automatas.add(token140);

        token10.unirAL(automatas);
        token10.imprimirTransicionesAFN();

        AFD n = new AFD();
        n.convertirAFN(token10);
        AnalizadorLexico al = new AnalizadorLexico();
        al.setAFD(n);
        return al;        
    }
    
    public AnalizadorLexico reglaCalculadora(){
        AFN token10 = new AFN();
        AFN token20 = new AFN();
        AFN token30 = new AFN();
        AFN token40 = new AFN();
        AFN token50 = new AFN();
        AFN token60 = new AFN();
        AFN token70 = new AFN();
        token10.basico('+');
        token10.setToken(10);
        token20.basico('-');
        token20.setToken(20);
        token30.basico('*');
        token30.setToken(30);
        token40.basico('/');
        token40.setToken(40);
        token50.basico('(');
        token50.setToken(50);
        token60.basico(')');
        token60.setToken(60);



        token70 = new AFN();
        token70.basico('0','9');
        token70.cerraduraPositiva();

        AFN punto = new AFN();
        punto.basico('.');

        AFN num1 = new AFN();
        num1.basico('0','9');
        num1.cerraduraPositiva();

        punto.concatenar(num1);
        punto.Question();

        token70.concatenar(punto);
        token70.setToken(70);

        //unirlos
        ArrayList<AFN> automatas = new ArrayList<AFN>();
        automatas.add(token10);
        automatas.add(token20);
        automatas.add(token30);
        automatas.add(token40);
        automatas.add(token50);
        automatas.add(token60);
        automatas.add(token70);


        token10.unirAL(automatas);
        token10.imprimirTransicionesAFN();

        //convertir a AFD
        AFD n = new AFD();
        n.convertirAFN(token10);
        
        AnalizadorLexico al = new AnalizadorLexico();
        al.setAFD(n);
        return al;        
    } 
}
