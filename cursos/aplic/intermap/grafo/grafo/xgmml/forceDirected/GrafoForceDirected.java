/*
 * GrafoForceDirected.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml.forceDirected;
import grafo.xgmml.*;
import java.util.Map;
/**
 *
 * @author  Celmar
 */
public class GrafoForceDirected extends grafo.xgmml.simples.GrafoSimples {
    
    /** Retorna uma aresta especificada pelo elemento DOM informado.
     * @param raiz N� DOM.
     * @param source Origem da aresta, j� descoberta anteriormente.
     * @param target Destino da aresta, j� descoberto anteriormente.
     * @param peso Peso da aresta, j� descoberto anteriormente.
     * @param info Informa�ao sobre a aresta, j� descoberta anteriormente.
     * @param nosHash Hash contendo nomes dos n�s dentro do XGMML.
     * @return Aresta a ser adicionada ao grafo.
     */
    protected grafo.Aresta obterArestaXGMML(org.w3c.dom.Node raiz, String source, String target, int peso, grafo.Info info, Map nosHash) {
        grafo.simples.NoSimples no1 = (grafo.simples.NoSimples)nosHash.get(source);
        grafo.simples.NoSimples no2 = (grafo.simples.NoSimples)nosHash.get(target);
        grafo.forceDirected.ArestaForceDirected aresta;
        if (no1 != null && no2 != null) {
            aresta = new grafo.forceDirected.ArestaForceDirected(no1, no2, peso, info);
            // Atributos especificados por tags <att>: responsabilidade da classe Aresta.
            Util.lerAtributosXGMML(raiz, aresta);
        } else {
            aresta = null;
        }
        //adicionarAresta(aresta);        
        return aresta;        
    }
    
}
