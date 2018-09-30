/*
 * Io.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;

import java.io.*;

/** Estipula m�todos de leitura e escrita de arquivos XGMML.
 */
public interface Io {
    
    /** Escreve as tags XGMML que representam o objeto obj.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o 
     * escritas.
     * @param obj Objeto a ser descrito pelas tags.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    public void escreverAtributosXGMML(FileWriter o, Object obj) throws IOException;
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades do objeto
     * obj de acordo com esses atributos.
     * @param raiz N� DOM do qual as tags ser�o lidas.
     * @param obj Objeto cujas caracteristicas ser�o estabelecidas pelas tags.
     */
    public void lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj);
    
}
