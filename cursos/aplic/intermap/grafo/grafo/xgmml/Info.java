/*
 * Info.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;

/**
 * Classe abstrata para leitura e escrita de tags XGMML relativas a uma informa��o.
 */
public interface Info extends Io  {
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades de um objeto
     * grafo.Info de acordo com esses atributos.
     * @param raiz N� DOM contendo as tags a serem lidas.
     * @param obj Objeto grafo.Info cujos atributos ser�o ajustados.
     */
    //public Info() {
    //}
    
    public void lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj);
    
    /** Escreve as tags XGMML que representam uma informa��o definida em obj.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o 
     * escritas.
     * @param obj Objeto a ser descrito pelas tags.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */  
    public void escreverAtributosXGMML(java.io.FileWriter o, Object obj) throws java.io.IOException;
    
}
