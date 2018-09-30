/*
 * CorAresta.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */
package grafo.xgmml;
import java.awt.Color;

/**
 * Classe abstrata para leitura e escrita de tags XGMML relativas �s cores de uma
 * aresta.
 */
public class CorAresta implements Io, Constantes {
    
    /**
     * Escreve as tags XGMML que representam as cores de aresta definidas em obj.
     * @param obj Objeto grafo.CorAresta que deve ser descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual os atributos ser�o
     * escritos.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    public void escreverAtributosXGMML(java.io.FileWriter o, Object obj) throws java.io.IOException {
        if (obj instanceof grafo.CorAresta) {
            grafo.CorAresta c = (grafo.CorAresta)obj;
            o.write("      <att name=\"rgb-unselected-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corNormal) + "\"/>\n");
            o.write("      <att name=\"rgb-selected-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corMarcada) + "\"/>\n");
        }
    }
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades de um objeto
     * grafo.CorAresta de acordo com esses atributos.
     * @param obj Objeto grafo.CorAresta cujos atributos ser�o ajustados.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas
     */
    public void lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj) {
        if (obj instanceof grafo.CorAresta) {
            grafo.CorAresta c = (grafo.CorAresta)obj;
            
            org.w3c.dom.Node noDom;
            AtributoXGMML atr;
            for (int i = 0; i<raiz.getChildNodes().getLength(); i++) {
                noDom = raiz.getChildNodes().item(i);
                if (noDom.getNodeType() == nodeTypeElement && noDom.getNodeName() == "att") {
                    //Atributos internos � tag <att> :
                    //rgb-unselected-color-hexa,
                    //rgb-selected-color-hexa,
                    atr = new AtributoXGMML(noDom);
                    // Tenho atrNome e atrValor. Posso agora verificar quem � atrNome.
                    if (atr.nome.equals("rgb-unselected-color-hexa")) {
                        c.corNormal = Color.decode("#"+atr.valor);
                    } else if (atr.nome.equals("rgb-selected-color-hexa")) {
                        c.corMarcada = Color.decode("#"+atr.valor);
                    }
                    
                }
            }

        }
    }
    
}
