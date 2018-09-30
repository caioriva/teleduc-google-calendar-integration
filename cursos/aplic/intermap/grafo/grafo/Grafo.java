/*
 * Grafo.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.Point2D;

/** Classe abstrata para representar grafos.
 */
public abstract class Grafo extends Observable
{
    
    /** Indica a �rea externa ao ret�ngulo que cont�m o grafo.
     */
    public final static int AREA_EXTERIOR = 0;
    
    /** Indica a �rea interna ao ret�ngulo em que o grafo est� desenhado, exceto as
     * �reas dos n�s.
     */
    public final static int AREA_INTERIOR = 1;
    
    /** Indica a �rea dos n�s.
     */
    public final static int AREA_DE_NO    = 2;
    
    /** Cor padr�o de um n�.
     */
    public static CorNo defaultCorNo = new CorNo();
    
    /** Cores de arestas.
     */
    public CorAresta corAresta = new CorAresta();
    
    /** Grupo padr�o (grupo a que todos os n�s pertencem caso eles n�o tenham sido
     * colocados em nenhum grupo).
     */
    public static Grupo defaultGrupo = new Grupo("Default", defaultCorNo);
    
    /** Lista dos n�s.
     */
    public java.util.List nos;
    
    /** Lista das arestas.
     */
    public java.util.Set arestas;
    
    /** Informa se grafo cont�m peso nos n�s.
     */
    private boolean nosPossuemPeso = false;
    
    /** Informa se grafo cont�m peso nas arestas.
     */
    private boolean arestasPossuemPeso = false;
    
    /** Cor de fundo.
     */
    public Color corDeFundo;
    
    /** Lista dos grupos (tipos) de n�s.
     */
    private java.util.List grupos;
    
    /** N� que est� sendo movido no momento.
     */
    private No noMovendo;
    
    /** Valor anterior de noMovendo.
     */
    private Point2D.Double posicaoAnteriorNoMovendo = new Point2D.Double(0,0);

    /** Indica a dimens�o da tela com a qual se pode trabalhar. */
    private Dimension dimensaoDaTela;
    
    /** Cria uma nova inst�ncia de Grafo.
     */
    public Grafo() {
        nos = new ArrayList();
        arestas = new HashSet();
        corDeFundo = new Color(0xe0e0e0); //Color.white;
        grupos = new ArrayList();
        noMovendo=null;
    }
 
    /** Faz com que o grafo leve em considera��o os pesos associados aos n�s.
     */    
    public void considerarPesosDosNos() {
        nosPossuemPeso = true;
    }
    
    /** Faz com que o grafo n�o leve em considera��o os pesos associados aos n�s.
     */    
    public void desconsiderarPesosDosNos() {
        nosPossuemPeso = false;
    }
    
    /** Faz com que o grafo leve em considera��o os pesos associados �s arestas.
     */    
    public void considerarPesosDasArestas() {
        arestasPossuemPeso = true;
    }
    
    /** Faz com que o grafo n�o leve em considera��o os pesos associados �s arestas.
     */    
    public void desconsiderarPesosDasArestas() {
        arestasPossuemPeso = false;
    }    
    
    /** Informa se o peso dos n�s est� sendo considerado.
     * @return True se o peso est� sendo considerado, false caso contr�rio.
     */    
    public boolean nosPossuemPeso() {
        return nosPossuemPeso;
    }
    
    /** Informa se o peso das arestas est� sendo considerado.
     * @return True se o peso est� sendo considerado, false caso contr�rio.
     */    
    public boolean arestasPossuemPeso() {
        return arestasPossuemPeso;
    }
    /** Desenha o grafo.
     * @param g Onde o grafo ser� desenhado.
     */
    public void desenhar(Graphics2D g) {
        Shape shape = g.getClip();
        
        // Desenhar arestas na ordem:
        // 1 - Arestas dos nos n�o selecionados. (Menos importantes)
        // 2 - Arestas dos n�s selecionados. (Mais importantes)
        
        Iterator li = arestas.iterator();
        while (li.hasNext()) {
            Aresta aresta = (Aresta) li.next();
            if (!aresta.eAtingida() && !aresta.eEscondida()) {
                aresta.desenhar(this, g);
            }
        }
        
        li = arestas.iterator();
        while (li.hasNext()) {
            Aresta aresta = (Aresta) li.next();
            if (aresta.eAtingida() && !aresta.eEscondida()) {
                aresta.desenhar(this, g);
            }
        }
        
        // Desenhar n�s na ordem:
        // 1 - N�s n�o selecionados (menos importantes)
        // 2 - N�s selecionados 
        // 3 - N�s marcados
        // 4 - N� foco (mais importante)
        
        li = nos.listIterator();
        while (li.hasNext()) {
            No no = (No) li.next();
            if (!no.eAtingido() && !no.eMarcado()) {
                no.desenhar(this, g);
            }
        }
        
        li = nos.listIterator();
        while (li.hasNext()) {
            No no = (No) li.next();
            if (no.eAtingido() && !no.eMarcado()) {
                no.desenhar(this, g);
            }
        }
        
        li = nos.listIterator();
        while (li.hasNext()) {
            No no = (No) li.next();
            if (no.eMarcado()) {
                no.desenhar(this, g);
            }
        }
        
        if (noMovendo != null) {
            noMovendo.desenhar(this, g);
        }
        
        g.setClip(shape); // voltar forma antiga
        
    }    

    /** Indica se um ponto P informado est� dentro de um n� do grafo ou n�o
     * @param x Abscissa do ponto P
     * @param y Ordenada do ponto P
     * @return  Retorna AREA_DE_NO - se o ponto est� sobre um n�;
     *          AREA_INTERIOR - se est� dentro do ret�ngulo mas fora de um n�;
     *          AREA_EXTERIOR - se est� fora do ret�ngulo.
     */    
    public int areaEm(int x, int y) {
        int resposta;
        if (dimensaoDaTela!=null) {
            if (x >= 0 && y>= 0 && x < dimensaoDaTela.width && y < dimensaoDaTela.height) {
                No no;
                resposta = AREA_INTERIOR;
                for (Iterator i = nos.iterator(); i.hasNext(); ) {
                    no = (No)i.next();
                    if (no.areaEm(x,y) == No.AREA_INTERIOR && !no.eEscondido()) {
                        resposta = AREA_DE_NO;
                    }
                }
                
            } else {
                resposta = AREA_EXTERIOR;
            }
        } else {
            resposta = AREA_EXTERIOR;
        }
        return resposta;
    }    

    /** Registra qual n� est� em movimento.
     * @param no N� que est� em movimento.
     */
    public void registrarNoMovendo(No no) {
        noMovendo = no;
        noMovendo.marcarComoMovendo();
        posicaoAnteriorNoMovendo.x = noMovendo.x;
        posicaoAnteriorNoMovendo.y = noMovendo.y;
    }
    
    /** Desfaz o registro de qual n� est� em movimento.
     */
    public void desfazerRegistrarNoMovendo() {
        if (noMovendo != null) {
            noMovendo.desmarcarComoMovendo();
            if (posicaoAnteriorNoMovendo.x != noMovendo.x || posicaoAnteriorNoMovendo.y != noMovendo.y) {
                registrarModificacaoDoGrafo();
            }
        }
        noMovendo = null;
    }
    
    /** Retorna qual o n� que est� em movimento.
     * @return Retorna o n� que est� em movimento, ou null se nenhum n� estiver nessa situa��o.
     */
    public No retornarNoMovendo() {
        return noMovendo;
    }
    
     /** Mover n� que est� em movimento para a posi��o nova (x,y).
     * @param x Abscissa da nova posi��o.
     * @param y Ordenada da nova posi��o.
     */
    public void moverNoMovendo(int x, int y) {
        // ajusta para que o n� n�o possa ser movido para fora do ret�ngulo do
        // grafo
        int nx = x, ny = y; // posi��o do mouse
        if (x < 0) {
            nx = 0;
        }
        if (x > dimensaoDaTela.width) {
            nx = (int) dimensaoDaTela.width;
        }
        if (y < 0) {
            ny = 0;
        }
        if (y > dimensaoDaTela.height) {
            ny = (int) dimensaoDaTela.height;
        }
        // aqui nx e ny j� tem a posi��o do mouse, ajustada para estar dentro
        // do ret�ngulo
        
        // ajustando posi��o do noMovendo
        noMovendo.x = nx;
        noMovendo.y = ny;
    }

    /** Verifica qual o n� sob um determinado ponto (x,y).
     * @param x Abscissa do ponto.
     * @param y Ordenada do ponto.
     * @return  Retorna o n�, ou null se o n� n�o existir.
     */
    public No noEm(int x, int y) {
        /* Confere n� a n�, na sequ�ncia inversa da que as arestas
        desenhadas, se o n� cont�m o ponto x,y .*/
        //nos.toArray();
        
        No no;
        No resposta = null;
        
        // primeiro, os n�s atingidos
        for (int i = nos.size()-1; i>=0 && resposta==null; i--) {
            no = (No) nos.get(i);
            if (no.eAtingido() && !no.eEscondido()) {
                if (no.areaEm(x,y) == No.AREA_INTERIOR) {
                    resposta = no;
                }
            }
        }
        
        if (resposta==null) {
            // depois, os outros.
            for (int i = nos.size()-1; i>=0 && resposta==null; i--) {
                no = (No) nos.get(i);
                if (!no.eAtingido() && !no.eEscondido()) {
                    if (no.areaEm(x,y) == No.AREA_INTERIOR) {
                        resposta = no;
                    }
                }
            }
        }
        return resposta;
    }
    
    
    /** Adiciona ao grafo um n� especificado.
     * @param no N� a ser adicionado.
     */
    public void adicionarNo(No no) {
        adicionarNo(no, true);
    }
        
    /** Adiciona ao grafo um n� especificado.
     * @param no N� a ser adicionado.
     * @param registrarModificacao Informa se a a��o de adicionar o n� deve ser 
     * considerada como modifica��o.
     */    
    synchronized public void adicionarNo(No no, boolean registrarModificacao) {        
        no.desmarcar();
        nos.add(no);
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }

    /** Adiciona ao grafo um conjunto de n�s especificados. 
     * @param colecaoDeNos Conjunto de n�s a serem adicionados.
     */    
    public void adicionarNos(Collection colecaoDeNos) {
        adicionarNos(colecaoDeNos, true);
    }
    
    /** Adiciona ao grafo um conjunto de n�s especificados. 
     * @param colecaoDeNos Conjunto de n�s a serem adicionados.
     * @param registrarModificacao Informa se a a��o de adicionar esses n�s deve 
     * ser considerada como modifica��o.
     */    
    synchronized public void adicionarNos(Collection colecaoDeNos, boolean registrarModificacao) {
        Iterator i = colecaoDeNos.iterator();
        No n;
        while (i.hasNext()) {
            n = (No)i.next();
            adicionarNo(n, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }
    
    /** Adiciona ao grafo uma aresta sem peso e sem informa��o.
     * @param no1 Primeiro n� da aresta a ser adicionada.
     * @param no2 Segundo n� da aresta a ser adicionada.
     */
    synchronized public void adicionarAresta(No no1, No no2) {
        adicionarAresta(no1, no2, 0, null);
    }
    
    /** Adiciona ao grafo uma aresta sem informa��o.
     * @param no1 Primeiro n� da aresta a ser adicionada.
     * @param no2 Segundo n� da aresta a ser adicionada.
     * @param peso Peso da aresta a ser adicionada.
     */
    synchronized public void adicionarAresta(No no1, No no2, int peso) {
        adicionarAresta(no1, no2, peso, null);
    }
    
    /** Adiciona ao grafo uma aresta sem peso.
     * @param no1 Primeiro n� da aresta a ser adicionada.
     * @param no2 Segundo n� da aresta a ser adicionada.
     * @param info Informa��o sobre a aresta a ser adicionada.
     */
    synchronized public void adicionarAresta(No no1, No no2, Info info) {
        adicionarAresta(no1, no2, 0, info);
    }
    
    /** Adiciona ao grafo uma aresta com peso e informa��o.
     * @param no1 Primeiro n� da aresta a ser adicionada.
     * @param no2 Segundo n� da aresta a ser adicionada.
     * @param peso Peso da aresta a ser adicionada.
     * @param info Informa��o sobre a aresta a ser adicionada.
     */
    public void adicionarAresta(No no1, No no2, int peso, Info info) {
        adicionarAresta(no1,no2,peso,info,true);
    }
    
    /** Adiciona ao grafo uma aresta com peso e informa��o.
     * @param no1 Primeiro n� da aresta a ser adicionada.
     * @param no2 Segundo n� da aresta a ser adicionada.
     * @param peso Peso da aresta a ser adicionada.
     * @param info Informa��o sobre a aresta a ser adicionada.
     * @param registrarModificacao Informa se a a��o de adicionar a aresta deve 
     * ser considerada como modifica��o do grafo ou n�o.
     */
    public abstract void adicionarAresta(No no1, No no2, int peso, Info info, boolean registrarModificacao);

    /** Adiciona ao grafo uma aresta informada.
     *  Observa��es
     *  - N�o verifica se essa aresta j� existe.
     *  - Caso um n� na extremidade da aresta n�o exista na lista de n�s,
     *  insere-o nessa lista.
     * @param aresta Aresta a ser adicionada ao grafo.
     */
    public void adicionarAresta(Aresta aresta) {
        adicionarAresta(aresta, true);
    }
    
    /** Adiciona ao grafo uma aresta informada.
     *  Observa��es
     *  - N�o verifica se essa aresta j� existe.
     *  - Caso um n� na extremidade da aresta n�o exista na lista de n�s,
     *  insere-o nessa lista.
     * @param aresta Aresta a ser adicionada ao grafo.
     * @param registrarModificacao Informa se a a��o de adicionar a aresta deve ser considerada como modifica��o
     * do grafo ou n�o.
     */
    synchronized public void adicionarAresta(Aresta aresta, boolean registrarModificacao) {
        if (aresta!=null) {
            aresta.no1.arestas.add(aresta);
            if (aresta.no1 != aresta.no2) {// evita adicionar uma aresta duas vezes ao mesmo n�.
                aresta.no2.arestas.add(aresta);
            }
            
            // Adiciona n� se ele ainda n�o est� na lista de n�s do grafo.
            if (!conterNo(aresta.no1)) {
                adicionarNo(aresta.no1);
            }
            if (!conterNo(aresta.no2)) {
                adicionarNo(aresta.no2);
            }
            if (aresta.no1.eEscondido() || aresta.no2.eEscondido()) {
                aresta.esconder();
            } 
            if (aresta.no1.eMarcado() || aresta.no2.eMarcado()) {
                aresta.atingir();
            }
            
            arestas.add(aresta);
            if (aresta.peso!=0 && !arestasPossuemPeso) {
                considerarPesosDasArestas();
            }
            if (registrarModificacao) {
                registrarModificacaoDoGrafo();
            }
        }

    }

    /** Adiciona ao grafo um conjunto de arestas informadas.
     * @param colecaoDeArestas Arestas a serem adicionadas ao grafo.
     */
    public void adicionarArestas(Collection colecaoDeArestas) {
        adicionarArestas(colecaoDeArestas, true);
    }
    
    /** Adiciona ao grafo um conjunto de arestas informadas.
     * @param colecaoDeArestas Arestas a serem adicionadas ao grafo.
     * @param registrarModificacao Informa se a a��o de adicionar essas arestas
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void adicionarArestas(Collection colecaoDeArestas, boolean registrarModificacao) {
        Iterator i = colecaoDeArestas.iterator();
        Aresta a;
        while (i.hasNext()) {
            a = (Aresta)i.next();
            adicionarAresta(a, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }

    /** Remove do grafo um n� especificado e as arestas conectadas a ele.
     * @param no N� a ser removido.
     */
    public void removerNo(No no) {
        removerNo(no, true);
    }
        
    /** Remove do grafo um n� especificado e as arestas conectadas a ele.
     * @param no N� a ser removido.
     * @param registrarModificacao Informa se a a��o de remover esse n�
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void removerNo(No no, boolean registrarModificacao) {
        // remover n�
        no.desmarcar();
        if (nos.remove(no)) {
            // remover arestas conectadas ao n�
            Iterator li = arestas.iterator();
            while (li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                if (aresta.no1 == no || aresta.no2 == no) {
                    // remover aresta da lista nos n�s
                    
                    aresta.no1.arestas.remove(aresta);
                    if (aresta.no1 != aresta.no2) {
                        aresta.no2.arestas.remove(aresta);
                    }
                    // remover aresta da lista em grafo
                    li.remove(); // nao use arestas.remove!!! <- problemas com sincroniza��o (Petr)
                    
                }
            }
            if (registrarModificacao) {
                registrarModificacaoDoGrafo();
            }
        }
    }

    /**
     * Remove do grafo um conjunto de n�s especificados e as arestas conectadas
     * a eles.
     * @param colecaoDeNos N�s a serem removidos.
     */
    public void removerNos(Collection colecaoDeNos) {
        removerNos(colecaoDeNos, true);
    }
    
    /** Remove do grafo um conjunto de n�s especificados e as arestas conectadas
     * a eles.
     * @param colecaoDeNos N�s a serem removidos.
     * @param registrarModificacao Informa se a a��o de remover esses n�s
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void removerNos(Collection colecaoDeNos, boolean registrarModificacao) {
        Iterator i = colecaoDeNos.iterator();
        No n;
        while (i.hasNext()) {
            n = (No)i.next();
            removerNo(n, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }


    /** Remove do grafo uma aresta especificada.
     * @param aresta Aresta a ser removida.
     */
    public void removerAresta(Aresta aresta) {
        removerAresta(aresta, true);
    }
    
    /** Remove do grafo uma aresta especificada.
     * @param aresta Aresta a ser removida.
     * @param registrarModificacao Informa se a a��o de remover essa aresta
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void removerAresta(Aresta aresta, boolean registrarModificacao) {
        if (arestas.remove(aresta)) {
            aresta.desfazerAtingir();

            aresta.esconder();            
            // remover aresta da lista de arestas de cada n�
            aresta.no1.arestas.remove(aresta);
            if (aresta.no1 != aresta.no2) {
                aresta.no2.arestas.remove(aresta);
            }
            if (registrarModificacao) {
                registrarModificacaoDoGrafo();
            }
        } else {
            System.out.println("ERRO: Aresta n�o encontrada. Impossivel remover aresta.");
            // excecao...
        }
    }

    /** Remove do grafo um conjunto de arestas especificadas.
     * @param colecaoDeArestas Arestas a serem removidas.
     */
    public void removerArestas(Collection colecaoDeArestas) {
        removerArestas(colecaoDeArestas, true);
    }

    /** Remove do grafo um conjunto de arestas especificadas.
     * @param colecaoDeArestas Arestas a serem removidas.
     * @param registrarModificacao Informa se a a��o de remover essas arestas
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void removerArestas(Collection colecaoDeArestas, boolean registrarModificacao) {
        Iterator i = colecaoDeArestas.iterator();
        Aresta a;
        while (i.hasNext()) {
            a = (Aresta)i.next();
            removerAresta(a, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }

    /** Informa se um n� determinado pertence ao grafo.
     * @param no N� a ser procurado.
     * @return True se o n� existe na lista de n�s do grafo, false caso
     * contr�rio.
     */
    public boolean conterNo(No no) {
        return nos.indexOf(no) != -1 ;
    }
    
    /** Informa se a aresta pertence ao grafo.
     * @param aresta Aresta a ser procurada.
     * @return True se a aresta existe no grafo, false caso contr�rio.
     */
    public boolean conterAresta(Aresta aresta) {
        return arestas.contains(aresta);
    }
    
    /** Retorna um n� do grafo que contenha uma determinada informa��o.
     * @param info Informa��o a ser procurada.
     * @return Retorna o primeiro n� que for encontrado contendo essa
     * informa��o, ou null caso n�o exista tal n�.
     */
    public No buscarNo(Info info) {
        ListIterator li = nos.listIterator();
        No resposta = null;
        No no;
        while (li.hasNext() && resposta == null) {
            no = (No) li.next();
            if (no.info != null && no.info.equals(info)) {
                resposta = no;
            }
        }
        return null;
    }
    
    /** Retorna uma aresta do grafo que contenha uma determinada informa��o.
     * @param info Informa��o a ser procurada.
     * @return Retorna a primeira aresta que for encontrada contendo essa
     * informa��o, ou null caso n�o exista tal aresta.
     */
    public Aresta buscarAresta(Info info) {
        Iterator li = arestas.iterator();
        Aresta resposta = null;
        Aresta aresta;
        while (li.hasNext() && resposta==null) {
            aresta = (Aresta)li.next();
            if (aresta.info != null && aresta.info.equals(info)) {
                resposta=aresta;
            }
        }
        return resposta;
    }
    
    /** Retorna uma aresta do grafo que ligue os n�s no1 e no2.
     * @param no1 N� da aresta.
     * @param no2 N� da aresta.
     * @return Retorna a primeira aresta que for encontrada ligando esses n�s,
     * ou null caso n�o exista tal aresta.
     */
    public Aresta buscarAresta(No no1, No no2) {
        Aresta resposta = null;
        if (no1 != null && no2 != null) {
            Iterator li = no1.arestas.iterator();
            while (li.hasNext() && resposta == null) {
                Aresta aresta = (Aresta) li.next();
                if ( (aresta.no1 == no1 && aresta.no2 == no2) ||
                (aresta.no1 == no2 && aresta.no2 == no1) ) {
                    resposta = aresta;
                }
            }
        }
        return resposta;
        // Obs: Esse algoritmo pode ser transformado em O(1). Hoje � O(n).
        // Para ser transformado em O(1), � preciso que this.arestas seja um 
        // hashMap indexado segundo um par de n�s (os n�s que comp�em cada 
        // aresta).
    }
    
    
    /** Adiciona um grupo (categoria/tipo) de n�s ao grafo. N�o adiciona nenhum
     * n�.
     * @param grupo Grupo a ser adicionado.
     */
    public void adicionarGrupo(Grupo grupo) {
        grupos.add(grupo);
    }
    
    /** Remove um grupo (categoria/tipo) de n�s ao Grafo. N�o remove nenhum n�.
     * @param grupo Grupo a ser removido.
     */
    public void removerGrupo(Grupo grupo) {
        grupos.remove(grupos.indexOf(grupo));
    }
    
    /** Retorna um ListIterator com os grupos existentes no grafo.
     * @return Lista de grupos do grafo.
     */
    public Iterator retornarGrupos() {
        return grupos.iterator();
    }

    /** Retorna a quantidade de grupos existentes no grafo.
     * @return Quantidade de grupos.
     */
    public int retornarQuantidadeDeGrupos() {
        return grupos.size();
    }
    
    /** Procura um grupo com o nome informado.
     * @param nome Nome do grupo a ser procurado.
     * @return Retorna o grupo, se exisitr, ou null, caso contr�rio.
     */    
    public Grupo retornarGrupo(String nome) {
        ListIterator li = grupos.listIterator();
        boolean achei = false;
        Grupo gr = null;
        while (li.hasNext() && !achei) {
            gr = (Grupo)li.next();
            achei = (gr.nome.equals(nome));
        }
        if (!achei) {
            gr = null;
        }
        return gr;
    }

    /** Retorna a quantidade de arestas existentes no grafo.
     * @return Quantidade de grupos.
     */
    public int retornarQuantidadeDeArestas() {
        return arestas.size();
    }

    /** Retorna a quantidade de n�s existentes no grafo.
     * @return Quantidade de grupos.
     */
    public int retornarQuantidadeDeNos() {
        return nos.size();
    }
    
    /** Retorna um ListIterator com os n�s existentes no grafo.
     * @return Lista de grupos do grafo.
     */
    public Iterator retornarNos() {
        return nos.iterator();
    }
    
    /** Esconde um n� do grafo.
     * @param no N� a ser escondido.
     */
    public void esconderNo(No no) {
        esconderNo(no, true);
    }
        
    /** Esconde um n� do grafo.
     * @param no N� a ser escondido.
     * @param registrarModificacao Informa se a a��o de esconder o n� deve ser
     * considerada como modifica��o do grafo ou n�o.
     */
    public void esconderNo(No no, boolean registrarModificacao) {
        if (no.eMovendo()) {
            return; // � muito dificil para esconder n� que est� em movimento
        }
        // remover n� de lista em grafo
        int indexNo = nos.indexOf(no);
        if (indexNo!=-1 && !no.eEscondido()) { // se n� pertence � lista de n�s, ent�o posso escond�-lo.
            if (no.eMarcado()) {
                no.desmarcar();
            }
            if (no.eEscolhido()) {
                no.desfazerEscolher();
            }
            no.esconder();
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }
    
    /** Esconde uma lista de n�s e suas respectivas arestas.
     * @param colecaoDeNos Conjunto de n�s a ser mostrado.
     */
    public void esconderNos(Collection colecaoDeNos) {
        esconderNos(colecaoDeNos, true);
    }
    
    /** Esconde uma lista de n�s e suas respectivas arestas.
     * @param colecaoDeNos Conjunto de n�s a ser mostrado.
     * @param registrarModificacao Informa se a a��o de esconder esses n�s
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    public void esconderNos(Collection colecaoDeNos, boolean registrarModificacao) {
        Iterator i = colecaoDeNos.iterator();
        No n;
        while (i.hasNext()) {
            n = (No)i.next();
            esconderNo(n, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }
    
    /** Torna vis�vel um n� que havia sido escondido.
     * @param no N� a ser mostrado.
     */
    public void mostrarNo(No no) {
        mostrarNo(no, true);
    }
    
    /** Torna vis�vel um n� que havia sido escondido.
     * @param no N� a ser mostrado.
     * @param registrarModificacao Informa se a a��o de esconder o n� deve ser
     * considerada como modifica��o do grafo ou n�o.
     */
    public void mostrarNo(No no, boolean registrarModificacao) {
        no.mostrar();
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }

    /** Torna vis�vel uma lista de n�s e suas respectivas arestas.
     * @param colecaoDeNos Conjunto de n�s a ser mostrado.
     */
    public void mostrarNos(Collection colecaoDeNos) {
        mostrarNos(colecaoDeNos,true);
    }
    
    /** Torna vis�vel uma lista de n�s e suas respectivas arestas.
     * @param colecaoDeNos Conjunto de n�s a ser mostrado.
     * @param registrarModificacao Informa se a a��o de mostrar esses n�s
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    public void mostrarNos(Collection colecaoDeNos, boolean registrarModificacao) {
        Iterator i = colecaoDeNos.iterator();
        No n;
        while (i.hasNext()) {
            n = (No)i.next();
            mostrarNo(n, false);
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }
    
    /** Mostra todos os n�s que estavam escondidos.
     */
    public void mostrarTodosOsNos() {
        mostrarTodosOsNos(true);
    }
        
    /** Mostra todos os n�s que estavam escondidos.
     * @param registrarModificacao Informa se a a��o de mostrar esses n�s
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    public void mostrarTodosOsNos(boolean registrarModificacao) {
        Object[] a = nos.toArray();
        for(int i = 0; i < a.length; i++) {
            if (((No)a[i]).eEscondido()) {
                mostrarNo((No)a[i], false);
            }
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();        
        }
    }
    
    /** Retorna uma lista de todos os n�s escondidos.
     * @return Lista contendo n�s escondidos.
     */
    public Collection retornarEscondidos() {
        Collection c = new ArrayList();
        Iterator i = nos.iterator();
        while(i.hasNext()) {
            No no = (No)i.next();
            if (no.eEscondido()) {
                c.add(no);
            }
        }
        return c;
    }
    
    /** Retorna uma lista de todos os n�s vis�veis marcados (escolhidos).
     * @return Lista contendo n�s marcados.
     */
    public Collection retornarEscolhidos() {
        Collection c = new ArrayList();
        Iterator i = nos.iterator();
        while(i.hasNext()) {
            No no = (No)i.next();
            if (no.eMarcado() && !no.eEscondido()) {
                c.add(no);
            }
        }
        return c;
    }
    
    /** Retorna uma lista de todos os n�s vis�veis n�o marcados (n�o escolhidos).
     * @return Lista de n�s n�o marcados.
     */
    public Collection retornarNaoEscolhidos() {
        Collection c = new ArrayList();
        Iterator i = nos.iterator();
        while(i.hasNext()) {
            No no = (No)i.next();
            if (!no.eMarcado() && !no.eEscondido()) {
                c.add(no);
            }
        }
        return c;
    }
    
    /** Algoritmo quadr�tico ( O(n^2), n = n�mero de n�s ) para descobrir o conjunto de
     * arestas que passam por um ponto M=(xm,ym) no grafo.
     * @param xm Abscissa de M.
     * @param ym Ordenada de M.
     * @return Retorna o conjunto de arestas do grafo que passam pelo ponto M.
     */
    private Collection retornarConjuntoArestasEmQuadratico(int xm, int ym) {
        Collection conjuntoArestas = new HashSet();
        Aresta a;
        No no1, no2;
        Iterator i = arestas.iterator();
        while (i.hasNext()) {
            a = (Aresta)i.next();
            if (!a.eEscondida()) {
                no1 = (No)(a.no1);
                no2 = (No)(a.no2);
                if (a.eAtingida() && pontoEstaProximoDaAresta(xm,ym,no1.x, no1.y, no2.x, no2.y)) {
                    conjuntoArestas.add(a);
                }
            }
        }
        return (conjuntoArestas);
    }

    /** Algoritmo quadratico (O(n^2), n = n�mero de n�s) para descobrir o conjunto de
     * auto-arestas (arestas cujas extremidades incidem sobre o mesmo n�) que passam
     * pelo ponto M=(xm,ym).
     * @param xm Abscissa de M.
     * @param ym Ordenada de M.
     * @return Retorna o conjunto de auto-arestas do grafo que passam pelo ponto M.
     */
    public Collection retornarConjuntoAutoArestasEm(int xm, int ym) {
        HashSet conjuntoArestas = new HashSet();
        Aresta aresta;
        double rm;
        final int delta = 3;
        Iterator it = arestas.iterator();
        while (it.hasNext()) {
            aresta = (Aresta)it.next();
            if (aresta.no1.equals(aresta.no2)) {
                Point centroAutoAresta = aresta.retornarCentroAutoAresta(aresta.no1, this);
                rm = Point.distance(centroAutoAresta.x,centroAutoAresta.y,xm,ym);
                if (aresta.raioArestaCircular-delta <= rm && rm <=aresta.raioArestaCircular+delta) {
                    if (!aresta.eEscondida() && aresta.eAtingida()) {
                        conjuntoArestas.add(aresta);
                    }
                }
            }
        }
        return (conjuntoArestas);
    }
    
    /** Verifica se o ponto M = (xm,ym) est� pr�ximo ao segmento de reta
     * AB = ((xa,ya),(xb,yb)).
     * @param xm Abscissa do ponto M.
     * @param ym Ordenada do ponto M.
     * @param xa Abscissa do ponto A.
     * @param ya Ordenada do ponto A.
     * @param xb Abscissa do ponto B.
     * @param yb Ordenada do ponto B.
     * @return True se o ponto est� pr�ximo do segmento de reta AB, false caso contr�rio.
     */
    public boolean pontoEstaProximoDaAresta(int xm, int ym, double xa, double ya, double xb, double yb) {
        double dab, dam, dmb, dap, dbp, thetaA, thetaM, d;
        double dmax=5;
        boolean resposta = false;
        dab = Math.sqrt(Math.pow(xa-xb,2) + Math.pow(ya-yb,2));
        dam = Math.sqrt(Math.pow(xa-xm,2) + Math.pow(ya-ym,2));
        dmb = Math.sqrt(Math.pow(xm-xb,2) + Math.pow(ym-yb,2));
        thetaA = Math.toDegrees(Math.asin((ya-yb)/dab));
        if (xa>xb) { thetaA = 180 - thetaA; }
        thetaA = Util.normalizarAngulo(thetaA);
        thetaM = Math.toDegrees(Math.asin((ym-yb)/dmb));
        if (xm>xb) { thetaM = 180 - thetaM; }
        thetaM = Util.normalizarAngulo(thetaM);
        d = dmb * Math.sin(Math.toRadians(thetaA-thetaM));
        if (Math.abs(d) < dmax) {
            // M est� pr�ximo da aresta a. Mas P pertence a ela?
            dap = Math.sqrt(Math.pow(dam,2) - Math.pow(d,2));
            dbp = Math.sqrt(Math.pow(dmb,2) - Math.pow(d,2));
            resposta = (Math.abs(dap+dbp-dab)<1);
        }
        return resposta;
    }
    
    /** Limpa o grafo, removendo todos os seus n�s, arestas e grupos.
     */
    synchronized public void limparGrafo() {
        nos.clear();
        arestas.clear();
        grupos.clear();
        registrarModificacaoDoGrafo();
    }

    /** Remove todas as arestas do grafo.
     */
    public void removerTodasAsArestas() {    
        removerTodasAsArestas(true);
    }
    
    /** Remove todas as arestas do grafo.
     * @param registrarModificacao Informa se a a��o de remover essas arestas
     * deve ser considerada como modifica��o do grafo ou n�o.
     */
    synchronized public void removerTodasAsArestas(boolean registrarModificacao) {
        Iterator li = arestas.iterator();
        while (li.hasNext()) {
            ((Aresta)li.next()).esconder();
        }
        arestas.clear();
        li = nos.iterator();
        No n;
        while (li.hasNext()) {
            n = (No)li.next();
            n.arestas.clear();
            n.anularAtingir();
        }
        if (registrarModificacao) {
            registrarModificacaoDoGrafo();
        }
    }    
    
    /** Retorna o conjunto de arestas em um ponto M = (xm,ym)
     * @param xm Abscissa do ponto M.
     * @param ym Ordenada do ponto M.
     * @return Conjunto de arestas que passam pelo ponto M.
     */
    public Collection retornarConjuntoArestasEm(int xm, int ym) {
        Collection resposta;
        resposta = retornarConjuntoArestasEmQuadratico(xm, ym);
        resposta.addAll(retornarConjuntoAutoArestasEm(xm,ym));
        return(resposta);
    }
 
    /** Registra a ocorr�ncia de uma modifica��o no grafo, notificando seus
     * observadores (Observers).
     */
    public void registrarModificacaoDoGrafo() {
        setChanged();
        notifyObservers();
    }
    
    /** Atualiza o indicador da dimens�o da tela.
     * @param d Dimens�o atual da tela com a qual se pode trabalhar para desenhar o grafo.
     */
    public void ajustarDimensaoDaTela(Dimension d) {
        //DebugMessenger.showMessage("Nova dimensao = "+d);
        dimensaoDaTela=d;
        setChanged();
        notifyObservers();
    }

    /** Retorna a dimens�o atualmente considerada pelo grafo como limite de 
     * desenho. 
     * @return Dimens�o considerada.
     */
    public Dimension retornarDimensaoDaTela() {
        //DebugMessenger.showMessage("Usando dimensao atual como = "+dimensaoDaTela);
        return dimensaoDaTela;
    }

    /** Calcula o peso m�ximo do conjunto atual de arestas.
     * @return Retorna o peso m�ximo do conjunto atual de arestas; 0 se o 
     * conjunto atual for vazio.
     */
    public int retornarPesoMaximoArestas() {
        int max = 0;
        Aresta a;
        boolean primeiro = true;
        for (Iterator it = arestas.iterator(); it.hasNext(); ) {
            a = (Aresta)it.next();
            if (!a.eEscondida()) {
                if (primeiro) {
                    max = a.peso;
                    primeiro = false;
                } else if (max < a.peso) {
                    max = a.peso;
                }
            }
        }
        return max;
    }
    
    /** Calcula o peso m�nimo do conjunto atual de arestas.
     * @return Retorna o peso m�ximo do conjunto atual de arestas; 0 se o 
     * conjunto atual for vazio.
     */
    public int retornarPesoMinimoArestas() {
        int min=0;
        Aresta a;
        boolean primeiro = true;
        for (Iterator it = arestas.iterator(); it.hasNext(); ) {
            a = (Aresta)it.next();
            if (!a.eEscondida()) {
                if (primeiro) {
                    min = a.peso;
                    primeiro = false;
                } else if (min > a.peso) {
                    min = a.peso;
                }
            }
        }
        return min;
    }

//    /** M�todo para depura��o. Mostra na sa�da padr�o uma lista com todos os n�s do
//     * ArrayList nos.
//     */
//    private void mostrarListaNos() {
//        Iterator li = nos.iterator();
//        while (li.hasNext()) {
//            No n = (No)li.next();
//            System.out.print(n.nome + ",");
//        }
//        System.out.println();
//    }    
    
}