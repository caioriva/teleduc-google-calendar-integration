/*
 * ChaveDeAresta.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

/** Classe que estabelece uma chave que define uma aresta polar.
 */
public class ChaveDeAresta {
    
    /** �ngulo do primeiro n� da aresta.
     */
    public double angulo1;
    
    /** �ngulo do segundo n� da aresta.
     */    
    public double angulo2;
    
    /** Anel do primeiro n� da aresta.
     */
    public boolean anel1;
    
    /** Anel do segundo n� da aresta.
     */    
    public boolean anel2;

    /** Precis�o utilizada na compara��o de �ngulos.
     */
    private final double precisao = 100000;
    
    /** Cria uma nova inst�ncia de ChaveDeAresta
     * @param angulo1 �ngulo do primeiro n� da aresta.
     * @param anel1 Anel do primeiro n� da aresta.
     * @param angulo2 �ngulo do segundo n� da aresta.
     * @param anel2 Anel do segundo n� da aresta.
     */
    public ChaveDeAresta(double angulo1, boolean anel1, double angulo2, boolean anel2) {
        this.angulo1 = ajustaPrecisao(angulo1);
        this.anel1 = anel1;
        this.angulo2 = ajustaPrecisao(angulo2);
        this.anel2 = anel2;
    }

    /** Verifica se um objeto informado � igual � chave instanciada.
     * Nesta verifica��o, duas chaves de arestas s�o consideradas iguais se o conjunto
     * de n�s de ambos os objetos for igual, independentemente da ordem dos n�s.
     * Ou seja, os dois objetos A e B nos exemplos abaixo s�o iguais:
     * A = { (45, true) , (90, false) }
     * B = { (45, true) , (90, false) }
     *
     * A = { (45, true) , (90, false) }
     * B = { (90, false) , (45, true) }
     *
     * Mas no exemplo abaixo A e B n�o s�o iguais:
     * A = { (45, true) , (90, false) }
     * B = { (90, true) , (45, false) }
     * @param obj Objeto a ser comparado com a inst�ncia em quest�o.
     * @return True se os objetos forem iguais, false caso contr�rio.
     */    
    public boolean equals(Object obj) {
        boolean resposta = false;
        if (obj instanceof ChaveDeAresta) {
            ChaveDeAresta ca = (ChaveDeAresta) obj;
//            double ca1 = ajustaPrecisao(ca.angulo1);
//            double ca2 = ajustaPrecisao(ca.angulo2);
            
            resposta = (angulo1 == ca.angulo1 && anel1 == ca.anel1 && 
                        angulo2 == ca.angulo2 && anel2 == ca.anel2) ||
                       (angulo1 == ca.angulo2 && anel1 == ca.anel2 && 
                        angulo2 == ca.angulo1 && anel2 == ca.anel1);
        } 
        return resposta;
    }

    /** Arredonda um valor informado, mantendo apenas um n�mero fixo de casas decimais.
     * � necess�rio devido a imprecis�es m�nimas que impediam dois �ngulos de serem
     * considerados iguais.
     * @param d Valor a ser arredondado.
     * @return Valor ap�s o arredondamento.
     */    
    public double ajustaPrecisao(double d) {
        return(Math.round(d * precisao) / precisao);
    }
    
    /** Gera o hashCode da chave.
     * @return hashCode.
     */    
    public int hashCode() {
        double a1 = angulo1;
        if (!anel1) {
            a1 = -a1;
        }
        double a2 = angulo2;
        if (!anel2) {
            a2 = -a2;
        }
        return 23 * (int)a1 + 59 * (int)a2;
    }
    
}
