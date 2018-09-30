/*
 * IoGrafo.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;

/**
 * Interface que define m�todos de leitura e escrita de grafos.
 */
public interface IoGrafo {
    
    /**
     * Escreve as tags XGMML que representam um grafo.
     * @param g Grafo cujas propriedades ser�o descritas.
     * @param o Inst�ncia de FileWriter (arquivo) na qual os atributos ser�o
     * escritos.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */  
    public void escreverXGMML(java.io.FileWriter o, grafo.Grafo g) throws java.io.IOException;
    
    /**
     * L� um arquivo XGMML especificado e armazena o grafo por ele descrito.
     * @param g Grafo cujo conte�do ser� definido.
     * @param arquivo Arquivo XGMML
     * @throws IOException Ocorre quando o arquivo informado n�o existir.
     */      
    public void lerXGMML(java.io.File arquivo, grafo.Grafo g) throws java.io.IOException;
    
}
