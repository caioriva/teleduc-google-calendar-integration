/*
 * GerenciadorXGMML.java
 * Vers�o: 2004-03-16
 * Autor: Celmar Guimar�es da Silva
 */

package grafo;

import java.io.*;

/** Estipula m�todos de leitura e escrita de arquivos XGMML.
 */
public interface GerenciadorXGMML {
    
    // Constantes de tipos de n�s de um documento DOM.
    /** N� DOM do tipo atributo */    
    final int nodeTypeAttr = 2;
    /** N� DOM do tipo documento. */    
    final int nodeTypeDocument = 9;
    /** N� DOM do tipo elemento. */    
    final int nodeTypeElement = 1;
    /** N� DOM do tipo texto. */    
    final int nodeTypeText = 3;

    
    /** Retorna tags de atributos para grava��o de arquivo XGMML.
     * @param o Inst�ncia de FileWriter (arquivo) na qual os atributos ser�o 
     * escritos.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    public void escreverAtributosXGMML(FileWriter o) throws IOException;
    
    /** L� tags de atributos XGMML de um elemento, ajustando suas propriedades
     * de acordo com esses atributos.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas
     */
    public void lerAtributosXGMML(org.w3c.dom.Node raiz);
    
}
