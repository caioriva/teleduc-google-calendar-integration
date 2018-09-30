/*
 * Aresta.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;

import java.io.*;
import java.awt.*;

/** Classe b�sica para criar arestas de um grafo.
 */
public abstract class Aresta 
{
    
    /** Um dos v�rtices atingidos pela aresta.
     */
    public No no1;
    
    /** O outro v�rtice atingido pela aresta.
     */
    public No no2;

    /** Peso da aresta.
     */
    public int peso;

    /** Informa��o sobre aresta.
     */
    public Info info;

    /** Raio da aresta circular.
     */
    public static int raioArestaCircular = 10;
    
//    /** Informa se a aresta � marcada.
//     */
//    private boolean marcada;

    /** Informa se a aresta � atingida por um n� selecionado.
     */
    private boolean atingida;
    
    /** Diz se a aresta est� escondida ou n�o
     */
    private boolean escondida;
    
    /** Construtor de aresta sem peso (peso = 0) e sem informa��o (info = null).
     * @param no1 Primeiro n� da aresta
     * @param no2 Segundo n� da aresta
     */
    public Aresta(No no1, No no2) {
        this(no1, no2, 0, null);
    }
    
    /** Construtor de aresta sem informa��o (info = null) e com peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta. 
     */
    public Aresta(No no1, No no2, int peso) {
        this(no1, no2, peso, null);
    }
    
    /** Construtor de aresta com informa��o e sem peso (peso = 0).
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param info Informa��o relacionada � aresta.
     */
    public Aresta(No no1, No no2, Info info) {
        this(no1, no2, 0, info);
    }
    
    /** Construtor de aresta com informa��o e peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta. 
     * @param info Informa��o relacionada � aresta.
     */
    public Aresta(No no1, No no2, int peso, Info info) {
        this.no1 = no1; 
        this.no2 = no2;
        this.peso = peso;
        this.info = info;
//        marcada = false;
        atingida = false;
        escondida = false;        
    }

    /** Desenha aresta com no1 == no2 (self-edge).
     * @param grafo Grafo ao qual a aresta pertence
     * @param g Objeto em que a aresta ser� desenhada.
     * @param no N� para o qual a aresta aponta.
     */
    public void desenharArestaParaOProprioNo(Grafo grafo, Graphics g, No no) {
        no.atualizarDimensoesTexto(g, no.nomeAMostrar());
        Point centroAutoAresta = retornarCentroAutoAresta(no, grafo);
        g.drawOval((int)centroAutoAresta.x-raioArestaCircular,(int)centroAutoAresta.y-raioArestaCircular,(int)2*raioArestaCircular,(int)2*raioArestaCircular);
    }

    /** Calcula a coordenada do centro do c�rculo que comp�em a auto-aresta 
     * de um n� informado.
     * @param no N� do qual se deseja saber o centro da auto-aresta.
     * @param g Grafo do qual o n� faz parte.
     * @return Retorna o ponto no centro da auto-aresta.
     */
    public abstract Point retornarCentroAutoAresta(No no, Grafo g);
    
    /** Desenha aresta para o grafo.
     * @param grafo Grafo ao qual a aresta pertence.
     * @param g Objeto em que a aresta ser� desenhada.
     */
    public abstract void desenhar(Grafo grafo, Graphics g) ;

    /** Retorna a cor que deve ser usada para desenhar a aresta, de acordo com as
     * especifica��es de cor estabelecidas no grafo em quest�o.
     * @param grafo Grafo cuja estrutura de cores a aresta deve seguir.
     * @return Cor da aresta.
     */
    public Color retornarCor(Grafo grafo) {
        //if (eMarcada() || eAtingida()) {
        Color resposta;
        if (eAtingida()) {
            resposta = grafo.corAresta.corMarcada;
        } else {
            resposta = grafo.corAresta.corNormal;
        }
        return resposta;
    }
    
    
    /** Marca internamente como atingidos a aresta e os n�s a ela conectados.
     */
    public void atingir() {
        if (!atingida) { // Previne que ela seja atingida duas vezes, o que 
                         // poderia causar problemas nos n�s. 
            atingida = true;
            if (!escondida) {
                no1.atingir();
                no2.atingir();
            }
        }
    }

    /** Desmarca como atingidos a aresta e os n�s a ela conectados.
     */
    public void desfazerAtingirSePossivel() {
        if (atingida) { // Previne que ela seja "desatingida" duas vezes, o que 
                        // poderia causar problemas nos n�s.
            if (escondida) {
                atingida = false;
            } else if (!no1.eMarcado() && !no1.eEscolhido() && !no2.eMarcado() && !no2.eEscolhido()) {
                desfazerAtingir();
            }
        }
    }
    
    /** Desmarca como atingida uma aresta, bem como os n�s a ela conectados.
     */
    public void desfazerAtingir() {
        if (atingida) {
            atingida = false;
            no1.desfazerAtingir();
            no2.desfazerAtingir();
        }
    }
    
    /** Informa se a aresta � atingida.
     * @return True se a aresta � atingida, false caso contr�rio.
     */
    public boolean eAtingida() {
        return atingida;
    }

    /** Informa se a aresta est� escondida (oculta).
     * @return True se a aresta est� escondida, false caso contr�rio.
     */
    public boolean eEscondida() {
        return escondida;
    }
    
    /** "Esconde" uma aresta, marcando a aresta como escondida.
     */
    public void esconder() {
        if (!escondida) {
            if (atingida) {
                desfazerAtingir();
            } 
            escondida=true;            
        }
    }
    
    /** "Mostra" uma aresta, marcando a aresta como n�o escondida.
     */
    public void mostrar() {
        if (escondida) {
            escondida=false;
            if (no1.eMarcado() || no1.eEscolhido() || no2.eMarcado() || no2.eEscolhido()) {
                atingir();
            }
        }
    }
    
    /** Verifica se um objeto O � igual � aresta instanciada A.
     * A igualdade acontece se todos os itens abaixo forem verdadeiros:
     * - O � uma aresta
     * - Os n�s de O sao iguais aos de A;
     * - O peso de O � igual ao de A;
     * @param o Objeto a ser comparado com a aresta instanciada.
     * @return True se o objeto � igual � aresta, false caso contr�rio.
     */    
    public boolean equals(Object o) {
        boolean ok = false;
        if (o instanceof Aresta) {
            Aresta a=(Aresta)o;
            ok = (a.no1 == no1 && a.no2 == no2) || (a.no1 == no2 && a.no2 == no1);
        }
        return ok;
    }
    
    /** Retorna uma string que caracteriza a aresta. 
     * @return String no formato "{a,b}", onde a e b s�o os n�s conectados pela
     * aresta.
     */
    public String toString() {
        return "{"+no1+","+no2+"}";
    }

    /** Retorna o hashCode da aresta.
     * @return hashCode.
     */
    public int hashCode() {
        return(53 * no1.hashCode() * no2.hashCode());
        // Assim, uma aresta (a,b) e uma aresta (b,a) retornarao o mesmo HashCode.
    }

}


