/*
 * AtributoXGMML.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;

/**
 * Classe para leitura e escrita de tags XGMML <CODE>att</code>, ou seja,
 * tags com o formato:
 * <CODE>&lt;att name="atrNome" value="atrValor"&gt;...&lt;/att&gt;</CODE>
 */
public class AtributoXGMML implements Constantes {
    
    /**
     * Conte�do do atributo "nome" da tag att.
     */    
    public String nome="";
    
    /**
     * Conte�do do atributo "valor" da tag att.
     */    
    public String valor="";
    
    /** Cria uma nova inst�ncia de AtributoXGMML
     * @param noDom N� DOM que cont�m uma tag att do XGMML.
     */
    public AtributoXGMML(org.w3c.dom.Node noDom) {
        org.w3c.dom.Node atr;
        if (noDom.getNodeType() == nodeTypeElement && noDom.getNodeName() == "att") {
            for (int i = 0; i<noDom.getAttributes().getLength(); i++) {
                // Um atributo aqui � 'name="atrNome" ' ou 'value = "atrValor" '
                // Qualquer outro atributo encontrado ser� ignorado.
                atr = noDom.getAttributes().item(i);
                if (atr.getNodeType() == nodeTypeAttr) {
                    if (atr.getNodeName() == "name") {
                        nome = atr.getNodeValue();
                    } else if (atr.getNodeName() == "value") {
                        valor = atr.getNodeValue();
                    }
                }
            }
        }
    }
    
}
