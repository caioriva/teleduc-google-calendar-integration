/*
 * ArestaForceDirected.java
 * Vers�o: 2004-08-24
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.forceDirected;
import grafo.*;
import grafo.simples.*;

/** Classe que cria arestas para um grafo force-directed. Essas arestas se 
 * comportam como molas que desejam ter um tamanho espec�fico (tamanhoDesejado),
 * mas que na tela possuem um tamanho real que nem sempre � igual ao desejado.
 * Disso decorre uma medida de erro, o stress da aresta (stress).
 */
public class ArestaForceDirected extends grafo.simples.ArestaSimples {
    
    /** Indica que o tamanho desejado de uma aresta n�o � importante. */
    public static final int TAMANHO_DESEJADO_TANTO_FAZ = -1;
    
    /** Indica que o tamanho desejado de uma aresta n�o foi definido. */
    public static final int TAMANHO_DESEJADO_NAO_DEFINIDO = -2;
    
    /** Tamanho desejado atual da aresta. */
    private double tamanhoDesejado = TAMANHO_DESEJADO_NAO_DEFINIDO;
    
    /** Stress da aresta, ou seja, diferen�a entre o tamanho desejado e o
     * tamanho real da aresta na tela. */
    private double stress;
    
    /** Construtor de aresta sem peso (peso = 0) e sem informa��o (info = null).
     * @param no1 Primeiro n� da aresta
     * @param no2 Segundo n� da aresta
     */
    public ArestaForceDirected(NoSimples no1, NoSimples no2) {
        this(no1, no2, 0, null);
    }
    
    /** Construtor de aresta sem informa��o (info = null) e com peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta.
     */
    public ArestaForceDirected(NoSimples no1, NoSimples no2, int peso) {
        this(no1, no2, peso, null);
    }
    
    /** Construtor de aresta com informa��o e sem peso (peso = 0).
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param info Informa��o relacionada � aresta.
     */
    public ArestaForceDirected(NoSimples no1, NoSimples no2, Info info) {
        this(no1, no2, 0, info);
    }
    
    /** Construtor de aresta com informa��o e peso.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta.
     * @param info Informa��o relacionada � aresta.
     */
    public ArestaForceDirected(NoSimples no1, NoSimples no2, int peso, Info info) {
        super(no1,no2,peso,info);
        tamanhoDesejado = 0;
        stress = 0;
    }
    
    /** Calcula e retorna o stress da aresta.
     * @return N�mero n�o negativo que representa o stress da aresta. */
    public double retornarStress() {
        calcularStress();
        return(stress);
    }
    
    /** Calcula o stress da aresta, e o armazena na propriedade stress. */
    public void calcularStress() {
        // stress da aresta = tamanhoDesejado - distancia atual = "quanto falta para a aresta ter o tamanho desejado?"
        if (tamanhoDesejado == TAMANHO_DESEJADO_TANTO_FAZ) {
            stress = 0;
        } else {
            stress = Math.abs(tamanhoDesejado - retornarTamanhoAresta());
        }
    }
    
    /** Retorna o tamanho real da aresta na tela.
     * @return Tamanho da aresta. */
    public double retornarTamanhoAresta() {
        return java.awt.geom.Point2D.Double.distance(no1.x, no1.y, no2.x, no2.y);
    }
    
    /** Calcula o tamanho desejado da aresta, e o armazena na propriedade
     * tamanhoDesejado.
     * @param tamanhoMaximoAresta tamanho m�ximo estipulado para qualquer aresta
     * @param tamanhoMinimoAresta tamanho m�nimo estipulado para qualquer aresta
     * @param pesoMaximoArestas peso m�ximo atual de qualquer aresta do grafo
     * @param pesoMinimoArestas peso m�nimo atual de qualquer aresta do grafo
     */
    public void calcularTamanhoDesejado(double tamanhoMaximoAresta, double tamanhoMinimoAresta,
    int pesoMaximoArestas, int pesoMinimoArestas) {
        if (pesoMaximoArestas==0 || peso == 0) {
            // Obs: arestas de peso zero devem se comportar como se n�o
            // existissem.
            // stress = 0;
            tamanhoDesejado = TAMANHO_DESEJADO_TANTO_FAZ;
        } else {
            // Calcula o tamanho desejado para a aresta
            //double tamanhoDesejado = (double)((pesoMaximoArestas-a.peso)*k/pesoMaximoArestas);// + 1;
            if (pesoMaximoArestas == pesoMinimoArestas) {
                tamanhoDesejado = tamanhoMaximoAresta;
            } else {
                tamanhoDesejado = tamanhoMinimoAresta + (tamanhoMaximoAresta-tamanhoMinimoAresta)
                * (pesoMaximoArestas - peso) / (pesoMaximoArestas - pesoMinimoArestas);
            }
        }
    }

    /** Retorna o tamanho desejado da aresta.
     * @return Tamanho desejado da aresta. */
    public double retornarTamanhoDesejado() {
        return tamanhoDesejado;
    }
    
}
