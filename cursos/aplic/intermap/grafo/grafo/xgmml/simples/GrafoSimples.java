/*
 * GrafoSimples.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */


package grafo.xgmml.simples;
import grafo.xgmml.*;
import java.util.Map;

public class GrafoSimples extends grafo.xgmml.Grafo {
    
    protected void escreverAtributosPadraoXGMML(java.io.FileWriter o, grafo.Grafo g) throws java.io.IOException {
        // nao precisa fazer nada.
    }
    
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
        grafo.simples.ArestaSimples aresta;
        if (no1 != null && no2 != null) {
            aresta = new grafo.simples.ArestaSimples(no1, no2, peso, info);
            // Atributos especificados por tags <att>: responsabilidade da classe Aresta.
            Util.lerAtributosXGMML(raiz, aresta);
        } else {
            aresta = null;
        }
        //adicionarAresta(aresta);        
        return aresta;        
    }
    
    /** Retorna um n� especificado pelo elemento DOM informado.
     * @param raiz N� DOM.
     * @param nome Nome do n�, j� obtido anteriormente.
     * @param peso Peso do n�, j� obtido anteriormente.
     * @param info Informa��o sobre o n�, j� obtida anteriormente.
     * @return N� a ser adicionado ao grafo.
     */ 
    protected grafo.No obterNoXGMML(org.w3c.dom.Node raiz, String nome, int peso, grafo.Info info) {
        grafo.simples.NoSimples no = new grafo.simples.NoSimples(nome, peso, info);
        Util.lerAtributosXGMML(raiz, no);
        //adicionarNo(no);
        return no;
    }
    
}
