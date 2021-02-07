package LogicaCompilador;

    public class Token {

        public Tipos getTipo(){
            return tipo;
        }
        
        public void setTipo(Tipos tipo){
            this.tipo = tipo;
        }
        
        public String getValor(){
            return valor;
        }
        
        public void setValor(String valor){
            this.valor = valor;
        }
        
        private Tipos tipo;
        private String valor;

        enum Tipos{
            NUMERO ("[0-9]+"),
            OPERADOR_BINARIO("[*|/|+|-]");

            public final String patron;
            Tipos(String s){
                this.patron = s;
            }
        }
}
