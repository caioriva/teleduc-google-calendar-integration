/*
 * GrafoSimplesDisplay.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.simples;
import java.awt.Graphics2D;
import java.awt.event.*;
import grafo.GrafoDisplay;

/** Panel em que o Grafo ser� mostrado. Cuida de todos os cliques efetuados
 * nessa estrutura visual.
 */

public class GrafoSimplesDisplay extends GrafoDisplay
implements MouseMotionListener, MouseListener, ActionListener, ComponentListener {
    
    /** Grafo a ser desenhado. */
    private GrafoSimples grafoSimples;
    
    /** Cria uma nova inst�ncia de GrafoDisplay.
     * @param grafo Grafo a ser desenhado no display. */
    public GrafoSimplesDisplay(GrafoSimples grafo) {
        super(grafo);
        grafoSimples = grafo;
    }
    
    /**
     * Desenha elementos que ficar�o em segundo plano.
     * @param g Objeto Graphics em que o desenho ser� feito.
     */
    
    public void desenhaElementosEmSegundoPlano(Graphics2D g) {
        // Nao existem elementos de segundo plano a serem desenhados...
    }

}
