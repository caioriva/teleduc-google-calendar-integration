/*
 * ArestaSimples.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.simples;

import java.io.*;
import java.awt.*;
import grafo.*;

/** Classe b�sica para criar arestas de um grafo.
 */
public class ArestaSimples extends Aresta 
//implements GerenciadorXGMML  
{
    BasicStroke normalStroke = new BasicStroke();
    BasicStroke boldStroke = new BasicStroke(2.0f);
    
    /** Construtor de aresta sem peso (peso = 0) e sem informa��o (info = null).
     * @param no1 Primeiro n� da aresta
     * @param no2 Segundo n� da aresta
     */
    public ArestaSimples(NoSimples no1, NoSimples no2) {
        this(no1, no2, 0, null);
    }
    
    /** Construtor de aresta sem informa��o (info = null) e com peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta. 
     */
    public ArestaSimples(NoSimples no1, NoSimples no2, int peso) {
        this(no1, no2, peso, null);
    }
    
    /** Construtor de aresta com informa��o e sem peso (peso = 0).
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param info Informa��o relacionada � aresta.
     */
    public ArestaSimples(NoSimples no1, NoSimples no2, Info info) {
        this(no1, no2, 0, info);
    }
    
    /** Construtor de aresta com informa��o e peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta. 
     * @param info Informa��o relacionada � aresta.
     */
    public ArestaSimples(NoSimples no1, NoSimples no2, int peso, Info info) {
        super(no1,no2,peso,info);
    }

    /** Calcula a coordenada do centro do c�rculo que comp�em a auto-aresta 
     * de um n� informado.
     * @param no N� do qual se deseja saber o centro da auto-aresta.
     * @param g Grafo do qual o n� faz parte.
     * @return Retorna o ponto no centro da auto-aresta.
     */
    public Point retornarCentroAutoAresta(No no, Grafo g) {
        return new Point((int)no.x, (int)(no.y-no.getHeight()*3/4));
    }
    
    /** Desenha aresta para o grafo.
     * @param grafo Grafo ao qual a aresta pertence.
     * @param g Objeto em que a aresta ser� desenhada.
     */

    public void desenhar(Grafo grafo, Graphics g) {
        if (grafo instanceof GrafoSimples) {
            Graphics2D g2 = (Graphics2D) g;
            // Desenhar aresta mais gorda se ela � atingida.
            if (eAtingida()) {
                g2.setStroke(boldStroke);
            }
            g.setColor(retornarCor(grafo));
            if (no1 != no2) {
                g.drawLine((int)no1.x, (int)no1.y, (int)no2.x, (int)no2.y);
            } else {
                desenharArestaParaOProprioNo(grafo, g, no1);
            }
            g2.setStroke(normalStroke);
        }
    }
   
}


