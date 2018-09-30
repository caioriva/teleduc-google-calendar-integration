/*
 * No.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;


/**
 * Classe abstrata para leitura e escrita de tags XGMML relativas a um n�.
 */
public abstract class No implements Io, Constantes {
    
    /** Escreve as tags XGMML que representam o n� obj.
     * @param o Arquivo em que devem ser escritas as tags.
     * @param obj N� cujos atributos ser�o escritos.
     * @throws IOException Acontece se "o" n�o estiver bem definido.
     */
    public void escreverAtributosXGMML(java.io.FileWriter o, Object obj) throws java.io.IOException {
        if (obj instanceof grafo.No) {
            grafo.No no = (grafo.No)obj;
            o.write("  <node id=\""+no.retornarId()+"\" label=\""+grafo.Util.ascii2xml(no.nome)+"\" weight=\""+no.peso+"\">\n");
            o.write("    <att name=\"x\" value = \""+no.x+"\"/>\n");
            o.write("    <att name=\"y\" value = \""+no.y+"\"/>\n");
            String selected = no.eMarcado() ? "Y" : "N";
            o.write("    <att name=\"selected\" value = \""+selected+"\"/>\n");
            String hidden = no.eEscondido() ? "Y" : "N";
            o.write("    <att name=\"hidden\" value = \""+hidden+"\"/>\n");
            if (no.grupo!=null) {
                o.write("    <att name=\"group\" value = \""+no.grupo.nome+"\"/>\n");
            }
            if (no.info!=null) {
                Util.escreverAtributosXGMML(o, no.info);
            }
            escreverAtributosExtrasXGMML(o, obj);
            o.write("  </node>\n");
        }
    }
    
    /**
     * Escreve as tags XGMML que cont�m atributos extras do n� obj.
     * @param o Arquivo em que devem ser escritas as tags.
     * @param obj N� cujos atributos ser�o escritos.
     * @throws IOException Acontece se "o" n�o estiver bem definido.
     */
    public void escreverAtributosExtrasXGMML(java.io.FileWriter o, Object obj) throws java.io.IOException {
        // Aqui n�o faz nada. Pode ser estendida por outras classes derivadas.
    }
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades de um objeto
     * grafo.No de acordo com esses atributos.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas.
     * @param obj N� cujas propriedades ser�o ajustadas.
     */
    public void lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj) {
        if (obj instanceof grafo.No && raiz.getNodeType() == nodeTypeElement && raiz.getNodeName() == "node") {
            grafo.No no = (grafo.No)obj;

            org.w3c.dom.Node noDom;
            AtributoXGMML a;
            for (int i = 0; i<raiz.getChildNodes().getLength(); i++) {
                noDom = raiz.getChildNodes().item(i);
                if (noDom.getNodeType() == nodeTypeElement && noDom.getNodeName() == "att") {
                    // Atributos internos � tag <att> : x, y, selected, group e hidden
                    // O atributo information � tratado pela classe Info.
                    // O atributo group � tratado pela classe Grafo.
                    a = new AtributoXGMML(noDom);
                    if (a.nome.equals("x")) {
                        //no.x = Integer.parseInt(a.valor);
                        no.x = Double.parseDouble(a.valor);
                    } else if (a.nome.equals("y")) {
                        //no.y = Integer.parseInt(a.valor);
                        no.y = Double.parseDouble(a.valor);
                    } else if (a.nome.equals("selected")) {
                        // Y para sim; N para n�o. Outro valor => n�o.
                        if (a.valor.equals("Y")) {
                            no.marcar();
                        };
                    } else if (a.nome.equals("hidden")) {
                        // Y para sim; N para n�o. Outro valor => n�o.
                        if (a.valor.equals("Y")) {
                            no.esconder();
                        };
                    } else if (a.nome.equals("information")) {
                        no.info = null;
                        try {
                            no.info = (grafo.Info)Class.forName("grafo."+a.valor).getConstructor(null).newInstance(null);
                            Util.lerAtributosXGMML(noDom, no.info);
                        } catch (Exception e) {
                            System.out.println("N�o consegui acessar a classe \"grafo."+a.valor+"\"");
                        }
                    }
                    
                }
            }
            
        }
        
    }
    
}