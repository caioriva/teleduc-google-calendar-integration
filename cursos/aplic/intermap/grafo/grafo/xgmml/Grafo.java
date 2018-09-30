/*
 * Grafo.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;

import grafo.Aresta;
import grafo.No;
import grafo.Grupo;
import java.io.*;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe para leitura e escrita de tags XGMML que descrevam um grafo.
 */
public abstract class Grafo implements Constantes, IoGrafo {
    
    /**
     * Cria uma nova inst�ncia de grafo.
     */
    //public Grafo() {
    //}
    
    /**
     * Escreve as tags XGMML que representam um grafo.
     * @param g Grafo cujas propriedades ser�o descritas.
     * @param o Inst�ncia de FileWriter (arquivo) na qual os atributos ser�o
     * escritos.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    public void escreverXGMML(FileWriter o, grafo.Grafo g) throws IOException {
        // Verificar se o melhor a fazer � usar throws IOException ou tratar a excecao aqui.
        escreverCabecalhoXGMML(o);
        escreverAtributosPadraoXGMML(o,g);
        escreverGruposXGMML(o,g);
        escreverNosXGMML(o,g);
        escreverArestasXGMML(o,g);
        escreverRodapeXGMML(o);
    }
    
    /** Escreve as tags XGMML do cabe�alho de um grafo.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o 
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    private void escreverCabecalhoXGMML(FileWriter o) throws IOException {
        o.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        // A linha adicionada abaixo causa problemas durante a leitura do grafo
        // se a pessoa n�o tiver conex�o com a Internet no momento do uso do 
        // programa. Verificar como solucionar o problema.
        //o.write("<!DOCTYPE graph PUBLIC \"-//John Punin//DTD graph description//EN\" \"http://www.cs.rpi.edu/~puninj/XGMML/xgmml.dtd\">\n");
        o.write("<graph directed=\"0\">\n");
    }
    
    /** Escreve as tags XGMML que encerram um grafo.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o 
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    private void escreverRodapeXGMML(FileWriter o) throws IOException {
        o.write("</graph>\n");
    }
    
    /**
     * Escreve as tags XGMML que registram os atributos do grafo.
     * Esta fun��o n�o faz nada atualmente. Ela deve ser estendida por classes
     * derivadas para escrever atributos do grafo de acordo com a necessidade.
     * @param g Grafo cujo conte�do ser� descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    protected abstract void escreverAtributosPadraoXGMML(FileWriter o, grafo.Grafo g) throws IOException;
    
    /**
     * Escreve as tags XGMML que definem os grupos de um grafo.
     * @param g Grafo cujo conte�do ser� descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    protected void escreverGruposXGMML(FileWriter o, grafo.Grafo g) throws IOException {
        if (g.retornarQuantidadeDeGrupos()>0) {
            // Grupos do grafo
            Iterator gi = g.retornarGrupos();
            Grupo grupo;
            while (gi.hasNext()) {
                grupo = (Grupo)gi.next();
                Util.escreverAtributosXGMML(o, grupo);
            }
        }        
    }
    
    /**
     * Escreve as tags XGMML que definem os n�s de um grafo.
     * @param g Grafo cujo conte�do ser� descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    protected void escreverNosXGMML(FileWriter o, grafo.Grafo g) throws IOException {
        No no;
        for (int i=0; i<g.retornarQuantidadeDeNos(); i++) {
            no = (No)(g.nos).get(i);
            // atribui um id para o n�, caso n�o exista
            if (no.retornarId()==null) {  
                no.ajustarId(Integer.toString(i));
                // n�o verifica se existe outro n� com esse id.
            }
            Util.escreverAtributosXGMML(o, no);
        }
    }
    
    /**
     * Escreve as tags XGMML que definem as arestas de um grafo.
     * @param g Grafo cujo conte�do ser� descrito.
     * @param o Inst�ncia de FileWriter (arquivo) na qual as tags ser�o
     * escritas.
     * @throws IOException Ocorre exce��o se houver problemas com o arquivo.
     */
    protected void escreverArestasXGMML(FileWriter o, grafo.Grafo g) throws IOException {
        Aresta aresta;
        Iterator it = g.arestas.iterator();
        while (it.hasNext()) {
            aresta=(Aresta)it.next();
            Util.escreverAtributosXGMML(o, aresta);
        }
    }
     
     

    /**
     * L� um arquivo XGMML especificado e armazena o grafo por ele descrito.
     * @param g Grafo cujo conte�do ser� definido.
     * @param arquivo Arquivo XGMML
     * @throws IOException Ocorre quando o arquivo informado n�o existir.
     */    
    public void lerXGMML(File arquivo, grafo.Grafo g) throws IOException {
        Document dom = null;
        //---usando DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            dom = builder.parse(arquivo);
        } catch (SAXParseException e) {
            System.out.println("Erro ao ler o arquivo XML (m�todo Grafo.lerXGMML). Mensagem:"+e.getMessage() + ". Linha n�mero " + e.getLineNumber()+ ".");
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo XML (m�todo Grafo.lerXGMML). Mensagem:"+e.getMessage());
        }
        
        org.w3c.dom.Node noGraph = null;
        if (dom!=null) {
            //dom � a raiz do documento DOM.
            // DOM precisa ter um n� chamado graph. Sen�o n�o deve ser carregado.
            int i = 0;
            boolean achei = false;
            while (i<dom.getChildNodes().getLength() && !achei) {
                noGraph = dom.getChildNodes().item(i);
                if (noGraph.getNodeName() == "graph" && noGraph.getNodeType() == nodeTypeElement) {
                    achei = true;
                } else {
                    i++;
                }
            }
            
            if (achei) {
                // Limpando o grafo para mostrar grafo novo.
                // limparGrafo();
                // Iniciando leitura de elementos XGMML
                lerAtributosXGMML(noGraph, g);
            }
        }
    } 
    
    /**
     * L� tags de atributos XGMML de um n� DOM e ajusta as propriedades de um objeto
     * grafo.Grafo de acordo com esses atributos.
     * @param g Grafo cujos atributos XGMML ser�o ajustados.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas
     */
    private void lerAtributosXGMML(org.w3c.dom.Node raiz, grafo.Grafo g) {
        org.w3c.dom.Node noDom;
        HashMap nosHash = new HashMap();
        for (int i = 0; i<raiz.getChildNodes().getLength(); i++) {
            noDom = raiz.getChildNodes().item(i);
            if (noDom.getNodeType() == nodeTypeElement) {
                if (noDom.getNodeName() == "node") {
                    lerNoXGMML(noDom, nosHash, g);
                } else if (noDom.getNodeName() == "edge") {
                    lerArestaXGMML(noDom, nosHash, g);
                } else if (noDom.getNodeName() == "att") {
                    lerAtributoXGMML(noDom, g);
                }
            }
        }
    }

    /**
     * L� tags XGMML que definem um n� do grafo, ajustando suas propriedades
     * de acordo com esses atributos.
     * @param g Grafo cujo conte�do ser� definido.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas.
     * @param nosHash Hash que armazena os nomes utilizados para identificar os n�s dentro do
     * arquivo XGMML.
     */    
    private void lerNoXGMML(org.w3c.dom.Node raiz, Map nosHash, grafo.Grafo g) {
        if (raiz.getNodeType() == nodeTypeElement && raiz.getNodeName() == "node") {
            
            org.w3c.dom.Node noDom, atr;
            String id = "";
            String label = "";
            String weight = "0";
            grafo.Info info = null;
            
            // Atributos internos � tag <node> : id, label e weight
            for (int i = 0; i<raiz.getAttributes().getLength(); i++) {
                atr = raiz.getAttributes().item(i);
                if (atr.getNodeType() == nodeTypeAttr) {
                    if (atr.getNodeName() == "id") {
                        id = atr.getNodeValue();
                    } else if (atr.getNodeName() == "label") {
                        label = atr.getNodeValue();
                    } else if (atr.getNodeName() == "weight") {
                        weight = atr.getNodeValue();
                    }
                }
            }
            
            // Procura atributo <att name="group" value="???????"/> dentro de <node>... </node>
            AtributoXGMML a;
            grafo.Grupo gr = null;
            for (int i = 0; i<raiz.getChildNodes().getLength(); i++) {
                noDom = raiz.getChildNodes().item(i);
                if (noDom.getNodeType() == nodeTypeElement && noDom.getNodeName() == "att") {
                    // Atributos internos � tag <att> : group
                    // Os demais atributos s�o tratados pela classe No.
                    a = new AtributoXGMML(noDom);
                    if (a.nome.equals("group")) {
                        gr = g.retornarGrupo(a.valor);
                    }
                }
            }
            
            No no = obterNoXGMML(raiz, label, Integer.parseInt(weight), info);
            g.adicionarNo(no);
            no.ajustarId(id);
            nosHash.put(id, no);
            if (gr!=null) {
                no.moverParaGrupo(gr);
            }

        }
    }
    
    /** Retorna um n� especificado pelo elemento DOM informado.
     * @param raiz N� DOM.
     * @param nome Nome do n�, j� obtido anteriormente.
     * @param peso Peso do n�, j� obtido anteriormente.
     * @param info Informa��o sobre o n�, j� obtida anteriormente.
     * @return N� a ser adicionado ao grafo.
     */ 
    protected abstract grafo.No obterNoXGMML(org.w3c.dom.Node raiz, String nome, int peso, grafo.Info info);

    /**
     * L� tags XGMML que definem uma aresta do grafo, ajustando suas propriedades
     * de acordo com esses atributos.
     * @param g Grafo cujo conte�do ser� definido.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas.
     * @param nosHash Hash que cont�m os nomes utilizados para identificar os n�s dentro do
     * arquivo XGMML.
     */    
    private void lerArestaXGMML(org.w3c.dom.Node raiz, Map nosHash, grafo.Grafo g) {
        if (raiz.getNodeType() == nodeTypeElement && raiz.getNodeName() == "edge") {
            org.w3c.dom.Node atr;
            String source = "";
            String target = "";
            String weight = "0";
            grafo.Info info = null;
            
            // Atributos internos � tag <node> : source, target e weight
            for (int i = 0; i<raiz.getAttributes().getLength(); i++) {
                atr = raiz.getAttributes().item(i);
                if (atr.getNodeType() == nodeTypeAttr) {
                    if (atr.getNodeName() == "source") {
                        source = atr.getNodeValue();
                    } else if (atr.getNodeName() == "target") {
                        target = atr.getNodeValue();
                    } else if (atr.getNodeName() == "weight") {
                        weight = atr.getNodeValue();
                    }
                }
            }
            
            Aresta aresta = obterArestaXGMML(raiz, source, target, Integer.parseInt(weight), info, nosHash);
            g.adicionarAresta(aresta);
        }
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
    protected abstract grafo.Aresta obterArestaXGMML(org.w3c.dom.Node raiz, String source, String target, int peso, grafo.Info info, Map nosHash);

    /**
     * L� tags XGMML que definem um atributo do grafo, ajustando suas propriedades
     * de acordo com esses atributos.
     * @param g Grafo cujo conte�do ser� definido.
     * @param raiz N� DOM do qual as tags &lt;ATT&gt; ser�o lidas
     */    
    private void lerAtributoXGMML(org.w3c.dom.Node raiz, grafo.Grafo g) {
        AtributoXGMML atr = new AtributoXGMML(raiz);
        processaAtributo(raiz, atr, g);
    }
    
    /**
     * Processa uma tag XGMML contendo um atributo do grafo.
     * @return True se o atributo foi reconhecido e tratado, False caso
     * contr�rio.
     * @param atr Atributo do grafo.
     * @param g Grafo cujo conte�do ser� definido.
     * @param raiz N� DOM.
     */    
    protected boolean processaAtributo(org.w3c.dom.Node raiz, AtributoXGMML atr, grafo.Grafo g) {
        boolean resp = true;
        if (atr.nome.equals("group")) {
            Grupo gr = new Grupo(atr.valor, null);
            Util.lerAtributosXGMML(raiz, gr);
            g.adicionarGrupo(gr);
        } else {
            resp = false;
            System.out.println("Atributo desconhecido: <att name = \""+atr.nome + "\" value = \""+atr.valor+"\" >"); 
        }
        return resp;
    }
    
}
