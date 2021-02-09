/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazCompilador;



import LogicaCompilador.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author sandu
 */
public class VentanaCalculadoraCientifica extends javax.swing.JFrame {

    /**
     * Creates new form VentanaCalculadoraCientifica
     */
    private AnalizadorLexico lex;
    private CalculadoraCientifica calc;
    private boolean exists;    
    public VentanaCalculadoraCientifica() {
        lex = null;
        calc = null;
        exists = false;
        
        initComponents();
    }

    public AnalizadorLexico generaReglas() {
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
        int numeroInicial = token10.getInicial().getIdentificador();

        System.out.println("----------------------------------------");
        n.imprimirTransicionesAFD();

        ArrayList<ArrayList<Integer>> tabla = n.tabla();
        ArrayList<Character> alfabetoOrdenado = n.getAlfabetoOrdenado();
        System.out.println("----------------------------------------");
        System.out.print("E\t");

        for (int i = 0; i < alfabetoOrdenado.size(); i++) {
            System.out.print(alfabetoOrdenado.get(i) + "\t");
        }
        System.out.println("A\tT");
        for (int i = 0; i < tabla.size(); i++) { //por cada fila
            for (int j = 0; j < n.getAlfabeto().size() + 3; j++) { //por cada caracter + 3 (estado inicial token y final)
                if (j < n.getAlfabeto().size() + 1 && tabla.get(i).get(j) != -1) {
                    System.out.print((tabla.get(i).get(j)) + "\t");
                } else {
                    System.out.print(tabla.get(i).get(j) + "\t");
                }
            }
            System.out.println();
        }
        //para probar una cadena
        System.out.println("\n\n\n\n\n***************");

        AnalizadorLexico al = new AnalizadorLexico();
        al.setAFD(n);
        return al;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnGenera = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtExpresion = new javax.swing.JTextField();
        btnEvaluar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        resultado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Calculadora Cientifica");

        btnGenera.setText("Generar Reglas");
        btnGenera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneraActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Evaluar Expresion");

        btnEvaluar.setText("Evaluar");
        btnEvaluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEvaluarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Resultado");

        resultado.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        resultado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btnGenera, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtExpresion)
                            .addComponent(btnEvaluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(resultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGenera)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtExpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnEvaluar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(resultado, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGeneraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneraActionPerformed
        if(exists){
            JOptionPane.showMessageDialog(null, "Ya se generaron las reglas", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }else{
            this.lex = generaReglas();
            this.calc = new CalculadoraCientifica(lex);
            JOptionPane.showMessageDialog(null, "Se han creado reglas de forma exitosa","Correcto",JOptionPane.INFORMATION_MESSAGE);
            exists=true;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGeneraActionPerformed

    private void btnEvaluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEvaluarActionPerformed
        String cadena = this.txtExpresion.getText();
        if (cadena.equals("")){
            JOptionPane.showMessageDialog(null, "Ingrese una cadena valida","ERROR",JOptionPane.ERROR_MESSAGE);
        }else{
            float res;
            this.lex.setCadena(cadena);
            res = this.calc.evaluar();
            if(res == -1f){
                this.resultado.setText("expresion invalida");
            }else{
                this.resultado.setText(Float.toString(res));
            }
        }
    }//GEN-LAST:event_btnEvaluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaCalculadoraCientifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaCalculadoraCientifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaCalculadoraCientifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaCalculadoraCientifica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaCalculadoraCientifica().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEvaluar;
    private javax.swing.JButton btnGenera;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel resultado;
    private javax.swing.JTextField txtExpresion;
    // End of variables declaration//GEN-END:variables
}
