/*
 * AlgoritmoMDSChalmers.java
 * Vers�o: 2004-08-24
 * Autor: Celmar Guimar�es da Silva
 */


package grafo.forceDirected;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.awt.geom.Point2D;
import grafo.simples.NoSimples;
import grafo.forceDirected.ArestaForceDirected;

/** Classe que usa o algoritmo de MDS de [Chalmers1996] para implementar o 
 * modelo de aproxima��o/afastamento de n�s baseado em for�as.
 * 
 * Refer�ncia Bibliogr�fica:
 * [Chalmers1996] Chalmers, M. "A Linear Iteration Time Layout Algorithm for 
 * Visualising High-Dimensional Data". Proceedings of Visualization '96, 
 * p.127-132, San Francisco, 1996.
 */
public class AlgoritmoMDSChalmers implements AlgoritmoMDS {

    /** Valor vMax do algoritmo de [Chalmers1996]. */
    private int vMax = 5; ///original = 5;
    
    /** Valor sMax do algoritmo de [Chalmers1996]. */
    private int sMax = 5; // original = 10;
    
    /** Mapa em que cada v�rtice i apresenta uma lista de v�rtices associados a 
     * ele. Equivale ao conjunto V do algoritmo de [Chalmers1996]
     */
    private Map vizinhos;
    
    /** Para cada lista de arestas armazenada em "vizinhos", armazena o maior 
     * "tamanho desejado" das arestas da lista.
     */
    private Map tamanhoMaximoParaVizinhos;
    
//    /** Contador de itera��es para definir quando o stress poderia ser medido. */
//    private int contadorDeIteracoes = 0;
    
    
    /** Peso m�ximo do conjunto atual de arestas do grafo. */
    private int pesoMaximoArestas;
    
    /** Peso m�nimo do conjunto atual de arestas do grafo. */
    private int pesoMinimoArestas;

    /** Inst�ncia do algoritmo force-directed b�sico, do qual o algoritmo de
     * Chalmers se utiliza.
     */
    private AlgoritmoMDSAdaptado mdsAdaptado;
    
    /** Grafo utilizado pelo algoritmo. 
     */
    private GrafoForceDirected grafo;
    
    /**
     * Cria uma nova inst�ncia de AlgoritmoMDSChalmers
     * @param grafo Grafo a ser utilizado pelo algoritmo.
     */
    public AlgoritmoMDSChalmers(GrafoForceDirected grafo) {
        this.grafo = grafo;
        mdsAdaptado = new AlgoritmoMDSAdaptado(grafo);        
    }
    
    /** Prepara o algoritmo para ser executado. */
    synchronized public void preparar() {
        mdsAdaptado.preparar();
        // Criar, para cada no, uma entrada nos HashMaps adequados,
        // que representam seus vizinhos (V) e a dist�ncia do vizinho mais
        // distante (maxDist);
        vizinhos = new HashMap();
        vizinhos.clear();
        tamanhoMaximoParaVizinhos = new HashMap();
        tamanhoMaximoParaVizinhos.clear();        
        
        for (Iterator i = grafo.nos.iterator(); i.hasNext(); ) {
            NoSimples no = (NoSimples)i.next();
            vizinhos.put(no, new ArrayList());
            tamanhoMaximoParaVizinhos.put(no, null);
        }
        
        pesoMaximoArestas = grafo.retornarPesoMaximoArestas();
        pesoMinimoArestas = grafo.retornarPesoMinimoArestas();
        int numNos = grafo.retornarQuantidadeDeNos();        
        if (numNos<vMax+sMax) {
            vMax = 1;
            sMax = numNos/2;
        }
        

//        contadorDeIteracoes = 0;
    }

    /** Executa um passo do algoritmo. */
    synchronized public void executarPasso() {
        // Executa 1 intera�ao do algoritmo, passando por todos os nos.
        // Algoritmo:
        // Para cada n� i,
        // - Iniciar o conjunto S como vazio; (S = secundarios)
        // - Repita, at� que S tenha sMax elementos:
        //   - Sortear um n� j do conjunto de v�rtices (j<>i)
        //   - Calcular a distancia desejada d(i,j) entre os n�s.
        //   - Se d(i,j)<maxDist(i), ou se maxDist(i) n�o estiver definida:
        //     - Inserir j em V(i), de maneira ordenada com rela��o �s dist�ncias
        //       dos elementos ja inseridos em V.
        //     - Se V(i) tiver mais que vMax elementos, podar V(i) em vMax elementos.
        //     - Atualizar maxDist(i) = d(i,j);
        //   - Caso contr�rio:
        //     - Inserir j em S.
        // - Calcular as for�as de atra��o e repuls�o considerando apenas os
        //   v�rtices e arestas dos conjuntos V e S.
        // Estando todas as for�as calculadas, aplicar as for�as.
        
        Collection secundarios = new ArrayList();
        int numNos = grafo.retornarQuantidadeDeNos();
        if (numNos<=1) {
            return; // nao h� o que fazer neste caso...
        }
        
        NoSimples i;
        NoSimples j;
        ArestaForceDirected ij;
        Double maxDist = null;
        boolean inserirEmVizinhos;
        Collection arestasDeSecundarios = new HashSet();
        
        double tamanhoDesejado;
        for (int x = 0 ; x < numNos; x++) {
            secundarios.clear();            
            arestasDeSecundarios.clear();
            i = (NoSimples)grafo.nos.get(x);
            
            java.util.List nosJ = new ArrayList();
            nosJ.addAll(grafo.nos);

            nosJ.remove(i);
            // Preenchendo listas vizinhos e secund�rios de i.
            while (secundarios.size()<sMax && !nosJ.isEmpty()) {
                // Sorteando j dentre todos os n�s do grafo
                do {
                    j = (NoSimples)nosJ.get((int)(Math.random()*nosJ.size()));
                } while (j== null || j == i);
                nosJ.remove(j);
                
                // Encontrar a aresta i<-->j, se houver
                
                ij = (ArestaForceDirected)grafo.buscarAresta(i,j);
                // essa rotina precisaria ser transformada em O(1) para n�o prejudicar o algoritmo.
                
                tamanhoDesejado = distanciaDesejadaEntreNos(i,j,ij);
                
                maxDist = (Double)tamanhoMaximoParaVizinhos.get(i);
                
                inserirEmVizinhos = (maxDist == null || tamanhoDesejado < maxDist.doubleValue());

                if (inserirEmVizinhos) {
                    inserirVizinhoDeI(i, j, ij);
                } else {
                    // Inserindo j em Secundarios
                    if (!secundarios.contains(j)) {
                        secundarios.add(j); // Logo, secundarios � um ArrayList de n�s.
                        if (ij!=null) {
                            arestasDeSecundarios.add(ij);
                        }
                    }
                }
                
            }
            
            
            // forcas de repulsao em i: de um lado, o no i; de outro os nos de vizinhos + secundarios
            Collection nosConsiderados = new HashSet();
            nosConsiderados.addAll(secundarios);
            ArrayList vizinhosDeI = (ArrayList)vizinhos.get(i);
            nosConsiderados.addAll(vizinhosDeI);
            mdsAdaptado.calcularForcaDeRepulsaoSofrida(i, nosConsiderados);
            
            // forcas de atra�ao em i: todas as arestas de Secundarios mais as que atingem os vertices vizinhos de i.
            Collection arestasConsideradas = new HashSet();
            arestasConsideradas.addAll(arestasDeSecundarios);
            NoSimples n;
            ArestaForceDirected a;
            for (Iterator it = vizinhosDeI.iterator(); it.hasNext(); ) {
                n = (NoSimples)it.next();
                a = (ArestaForceDirected)grafo.buscarAresta(i,n);
                if (a!=null) {
                    arestasConsideradas.add(a);
                }
            }
            mdsAdaptado.calcularForcaDeAtracaoDasArestas(arestasConsideradas,i);
            
        }
        
        //aplicando forcas
        mdsAdaptado.aplicarForcasCalculadas(grafo.nos);
        
    }

    /** Insere n� j como vizinho de i (conjunto V de i) 
     * @param i N� em cujo conjunto de vizinhos se pretende inserir o n� j.
     * @param j N� a ser inserido no conjunto de vizinhos de i
     * @param ij Aresta que ligue i e j, se houver.
     */    
    private void inserirVizinhoDeI(NoSimples i, NoSimples j, ArestaForceDirected ij) {
        if (i!=null && j!=null) {
            // Inserindo j como vizinho de i, ou seja, inserindo j no conjunto V.

            ArrayList vizinhosDeI = (ArrayList)vizinhos.get(i);
            if (!vizinhosDeI.contains(j)) {
                double distanciaEntreIeJ = distanciaDesejadaEntreNos(i,j,ij);
                Double maxDist = (Double)tamanhoMaximoParaVizinhos.get(i);
                boolean inseriu = false;                
                if (vizinhosDeI.isEmpty()) {
                    vizinhosDeI.add(j);
                    inseriu = true;
                } else {
                    // Rotina de inser�ao
                    ListIterator li = vizinhosDeI.listIterator();
                    ArestaForceDirected a;
                    NoSimples n;
                    while (li.hasNext() && !inseriu) {
                        n = (NoSimples)li.next();
                        a = (ArestaForceDirected)grafo.buscarAresta(i,n);
                        if (distanciaEntreIeJ <= distanciaDesejadaEntreNos(i,n,a)) {
                            li.previous();
                            li.add(j);
                            inseriu = true;
                        }
                    }
                }
                while (vizinhosDeI.size() > vMax) {
                    vizinhosDeI.remove(vMax);
                }
                if (inseriu) {
                    
                    // Pegar o �ltimo da lista de VizinhosDeI ---> k
                    // Calcular a distancia desejada entre i e k
                    // maxDist(i) <-- k
                    NoSimples k = (NoSimples)vizinhosDeI.get(vizinhosDeI.size()-1);
                    ArestaForceDirected ik = (ArestaForceDirected)grafo.buscarAresta(i,k);
                    double distanciaEntreIeK = distanciaDesejadaEntreNos(i,k,ik);
                    maxDist = new Double(distanciaEntreIeK);
                    tamanhoMaximoParaVizinhos.put(i, maxDist);
                }
                
            }
            
        }
    }
    
    /** Calcula a dist�ncia desejada entre dois n�s i e j.
     * Se existir aresta entre i e j, o tamanho desejado dessa aresta � a 
     * dist�ncia desejada entre os n�s.
     * Caso n�o exista aresta entre i e j, assume-se como dist�ncia desejada
     * a pr�pria dist�ncia atual entre eles (ou a dist�ncia m�nima entre n�s, 
     * se esta for maior).
     * Obs: N�o confere se i e j s�o nulos.
     *
     * @param i N�.
     * @param j N�.
     * @param ij Aresta que ligue i e j, se houver.
     * @return Dist�ncia desejada entre i e j
     */
    private double distanciaDesejadaEntreNos(NoSimples i, NoSimples j, ArestaForceDirected ij) { 
        double distanciaDesejada;
        if (ij == null) {
            // Aresta n�o existe. Considerar que a distancia desejada �
            // a distancia minima desejada entre os n�s pela forca de repulsao,
            // ou a dist�ncia atual entre eles (o que for maior).
            // Ou seja, "se est�o suficientemente longe um do outro, est� ok; 
            // sen�o, tratem de ficar longe um do outro".
            distanciaDesejada = Math.max(grafo.distanciaMinimaEntreNos, Point2D.distance(i.x,i.y,j.x,j.y));
        } else {
            // Calcular distancia desejada
            ij.calcularTamanhoDesejado(grafo.tamanhoMaximoAresta, grafo.tamanhoMinimoAresta, pesoMaximoArestas, pesoMinimoArestas);
            distanciaDesejada = ij.retornarTamanhoDesejado();
        }
        return(distanciaDesejada);        
    }
}

