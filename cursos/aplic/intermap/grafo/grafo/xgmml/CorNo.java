/*
 * CorNo.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;
import java.awt.Color;

/**
 * Classe abstrata para leitura e escrita de tags XGMML relativas �s cores de uma
 * aresta.
 */
public class CorNo implements Io, Constantes {
    
    /**
     * Escreve as tags XGMML que representam as cores de n� definidas em obj.
     * @param obj Objeto grafo.CorNo que deve ser descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual os atributos ser�o
     * escritos.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    public void escreverAtributosXGMML(java.io.FileWriter o, Object obj) throws java.io.IOException {
        if (obj instanceof grafo.CorNo) {
            grafo.CorNo c = (grafo.CorNo)obj;
            o.write("      <att name=\"rgb-unselected-background-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corDeFundoNormal) + "\"/>\n");
            o.write("      <att name=\"rgb-selected-background-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corDeFundoMarcado) + "\"/>\n");
            o.write("      <att name=\"rgb-unselected-foreground-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corNormal) + "\"/>\n");
            o.write("      <att name=\"rgb-selected-foreground-color-hexa\" value=\"" + grafo.Util.hexaRGBColor(c.corMarcado) + "\"/>\n");
        }
    }
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades de um objeto
     * grafo.CorNo de acordo com esses atributos.
     * @param obj Objeto grafo.CorNo cujos atributos ser�o ajustados.
     * @param raiz N� DOM do qual as tags ser�o lidas
     */
    public void lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj) {
        if (obj instanceof grafo.CorNo) {
            grafo.CorNo c = (grafo.CorNo)obj;
            
            org.w3c.dom.Node noDom;
            AtributoXGMML atr;
            for (int i = 0; i<raiz.getChildNodes().getLength(); i++) {
                noDom = raiz.getChildNodes().item(i);
                if (noDom.getNodeType() == nodeTypeElement && noDom.getNodeName() == "att") {
                    // Atributos internos � tag <att> :
                    // rgb-unselected-background-color-hexa,
                    // rgb-selected-background-color-hexa,
                    // rgb-unselected-foreground-color-hexa,
                    // rgb-selected-foreground-color-hexa
                    //
                    
                    atr = new AtributoXGMML(noDom);
                    
                    // Tenho atrNome e atrValor. Posso agora verificar quem � atrNome.
                    if (atr.nome.equals("rgb-unselected-background-color-hexa")) {
                        c.corDeFundoNormal = Color.decode("#"+atr.valor);
                    } else if (atr.nome.equals("rgb-selected-background-color-hexa")) {
                        c.corDeFundoMarcado = Color.decode("#"+atr.valor);
                    } else if (atr.nome.equals("rgb-unselected-foreground-color-hexa")) {
                        c.corNormal = Color.decode("#"+atr.valor);
                    } else if (atr.nome.equals("rgb-selected-foreground-color-hexa")) {
                        c.corMarcado = Color.decode("#"+atr.valor);
                    }
                    
                }
                
            }
            
        }
        
    }
    
}
