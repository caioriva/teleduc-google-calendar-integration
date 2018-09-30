/*
 * AlgoritmoMDSAdaptado.java
 * Vers�o: 2004-08-17
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.forceDirected;

import java.util.Collection;
import grafo.simples.NoSimples;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.awt.Dimension;

/** Classe respons�vel por implementar uma varia��o do algoritmo force-directed
 * (MDS) de Eades. A adapta��o feita faz com que o tamanho das arestas seja 
 * inversamente proporcional aos seus pesos; ou seja, quanto menor o peso da 
 * aresta, maior tende a ser o seu tamanho, e vice-versa.
 *
 * Refer�ncia bibliogr�fica:
 * Fruchterman e Reingold (1991). "Graph Drawing by Force-directed Placement".
 * Software - Practice and Experience, Vol. 21 (11), 1129-1164.
 */
public class AlgoritmoMDSAdaptado implements AlgoritmoMDS {
    
    /** Constante que reduz a dist�ncia percorrida por uma �nica itera��o do
     * algoritmo do Intermap. 
     * Obs: O valor originalmente utilizado na ferramenta Intermap do TelEduc � 3.
     */
    private final static double numeroPassos = 3;
    
    /** Peso m�ximo do conjunto atual de arestas do grafo. */
    private int pesoMaximoArestas;
    
    /** Peso m�nimo do conjunto atual de arestas do grafo. */
    private int pesoMinimoArestas;
    
    /** Grafo utilizado pelo algoritmo. */
    private GrafoForceDirected grafo;
    
    /**
     * Cria uma nova inst�ncia de AlgoritmoMDSAdaptado
     * @param grafo Grafo a ser utilizado pelo algoritmo.
     */
    public AlgoritmoMDSAdaptado(GrafoForceDirected grafo) {
        this.grafo = grafo;
    }
    
    /** Prepara o algoritmo para ser executado. */
    public void preparar() {
        if (grafo.retornarQuantidadeDeNos() - grafo.retornarEscondidos().size() >1) {
            pesoMaximoArestas = grafo.retornarPesoMaximoArestas();
            pesoMinimoArestas = grafo.retornarPesoMinimoArestas();
        }
    }

    /** Executa um passo do algoritmo. */
    public void executarPasso() {
        if (grafo.retornarQuantidadeDeNos() - grafo.retornarEscondidos().size() >1) {
//            pesoMaximoArestas = grafo.retornarPesoMaximoArestas();
//            pesoMinimoArestas = grafo.retornarPesoMinimoArestas();
            
            calcularForcaDeAtracaoDasArestas(grafo.arestas);
            calcularForcaDeRepulsaoDosNos(grafo.nos);
            aplicarForcasCalculadas(grafo.nos);
        }
    }
    
    
    
    /** Calcula as for�as de atra��o que as arestas informadas exercem no grafo, 
     * e as armazena no hash de deslocamento acumulado.
     * @param arestas Conjunto de arestas cujas for�as de atra��o devem ser 
     * calculadas.
     */    
    void calcularForcaDeAtracaoDasArestas(Collection arestas) {
        calcularForcaDeAtracaoDasArestas(arestas,null);
    }
        
    /** Calcula as for�as de atra��o que as arestas informadas exercem no n�
     * especificado, e as armazena no hash de deslocamento acumulado.
     * @param arestas Conjunto de arestas cujas for�as de atra��o devem ser 
     * calculadas.
     * @param n N� sobre o qual as for�as de atra��o das arestas devem ser 
     * calculadas. Se n == null, as for�as s�o calculadas para todos os n�s.
     */    
    void calcularForcaDeAtracaoDasArestas(Collection arestas, NoSimples n) {
        double distancia, distanciaX, distanciaY;
        double f, fx, fy;
        ArestaForceDirected a;
        Point2D.Double p1, p2;
        for (Iterator it = arestas.iterator(); it.hasNext(); ) {
            a = (ArestaForceDirected)it.next();
            if (a!=null) {
                if (!a.eEscondida() && !a.no1.eEscondido() && !a.no2.eEscondido()) {
                    
                    // v � a dist�ncia entre no1 e no2. vx e vy s�o seus
                    // componentes horizontal e vertical.
                    // v^2 = vx^2 + vy^2
                    distanciaX = a.no2.x - a.no1.x;
                    distanciaY = a.no2.y - a.no1.y;
                    distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
                    
                    if (distancia!=0) {
                        a.calcularTamanhoDesejado(grafo.tamanhoMaximoAresta, grafo.tamanhoMinimoAresta, pesoMaximoArestas, pesoMinimoArestas);
                        // Na verdade, somente seria preciso calcular isso se houvesse mudan�a de arestas ou de n�s.
                        // � um ponto consider�vel onde o programa pode ser otimizado.
                        
                        // f � o tamanho que a aresta vai aumentar.
                        f = (a.retornarTamanhoDesejado() - distancia) / numeroPassos;
                        
                        // Fazendo o vetor f se inclinar na dire��o do vetor v.
                        fx = f * distanciaX / distancia;
                        fy = f * distanciaY / distancia;
                        
                        p1 = (Point2D.Double)grafo.deslocamentoAcumulado.get(a.no1);
                        p2 = (Point2D.Double)grafo.deslocamentoAcumulado.get(a.no2);
                        if (p1!=null && p2!=null) {
                            // O if � necess�rio porque em situa��es de concorr�ncia o HashMap pode n�o conter determinadas arestas.

                            if (n==null) {
                                // p1 := p1 - f
                                p1.setLocation(p1.x - fx/2, p1.y - fy/2);
                                // p2 := p2 + f
                                //p2.setLocation(p2.x + fx/2, p2.y + fy/2);
                                p2.setLocation(p2.x + (fx - fx/2), p2.y + (fy - fy/2));
                            } else if (n==a.no1) {
                                p1.setLocation(p1.x - fx/2, p1.y - fy/2);
                            } else if (n==a.no2) {
                                p2.setLocation(p2.x + (fx - fx/2), p2.y + (fy - fy/2));
                            }
                        }
                        //else {
                        //System.out.print("n�o est�o no hashmap...");
                        //System.out.println(a.no1.nome+"-->"+a.no2.nome);
                        //}
                    }
                }
            }
        }
    }
    
    /** Calcula as for�as de repuls�o que os n�s de um conjunto exercem entre si,
     * e as armazena no hash de deslocamento acumulado.
     * @param nos Conjunto de n�s a ser considerado.
     */    
    void calcularForcaDeRepulsaoDosNos(Collection nos) {
        NoSimples n1;
        for (Iterator i = nos.iterator(); i.hasNext(); ) {
            n1 = (NoSimples)i.next();
            calcularForcaDeRepulsaoSofrida(n1, nos);
        }
    }
    
    /** Calcula as for�as de repuls�o sofridas que um conjunto de n�s imp�em a
     * um n� espec�fico, e as armazena no hash de deslocamento acumulado.
     * @param n1 N� sobre o qual as for�as atuam.
     * @param nos Conjunto de n�s cujas for�as de repuls�o devem ser 
     * consideradas.
     */    
    void calcularForcaDeRepulsaoSofrida(NoSimples n1, Collection nos) {
        
        double afx, afy;
        NoSimples n2;
        double distancia2;
        double distancia, distanciaX, distanciaY;
        double intensidadeDaRepulsao;
        Point2D.Double p1;
        
        if (!n1.eEscondido() && !n1.eMovendo()) {
            // Definindo o vetor de afastamento af para o n� n1.
            // afx e afy s�o os componentes horizontal e vertical de af.
            afx = 0;
            afy = 0;
            
            p1 = (Point2D.Double)grafo.deslocamentoAcumulado.get(n1);
            if (p1!=null) {
                
                for (Iterator i = nos.iterator(); i.hasNext();) {
                    n2 = (NoSimples)i.next();
                    if (n1.equals(n2)) {
                        continue;
                    }
                    if (!n2.eEscondido()) {
                        // Calculando dire��o do afastamento
                        distanciaX = n1.x - n2.x;
                        distanciaY = n1.y - n2.y;
                        distancia2 = distanciaX * distanciaX + distanciaY * distanciaY;
                        distancia = Math.sqrt(distancia2);
                        if (distancia2 == 0) {
                            // Os n�s est�o na mesma posi��o. Devem ser afastados em qualquer dire��o.
                            p1.x += Math.random();
                            p1.y += Math.random();
                        } else {                            
                            intensidadeDaRepulsao = (distancia > grafo.distanciaMinimaEntreNos ? 0 : grafo.distanciaMinimaEntreNos - distancia) / numeroPassos;
                            p1.x += intensidadeDaRepulsao * distanciaX / distancia; // cos alfa = afx/af
                            p1.y += intensidadeDaRepulsao * distanciaY / distancia; // sin alfa = afy/af
                        }
                    }
                }
                
                
            }
        }
        
    }
    
    /** Aplica ao grafo todas as for�as calculadas, ou seja, todos os 
     * deslocamentos acumulados pelos c�lculos de for�as de atra��o e de 
     * repuls�o.
     * Al�m disso, guarda metade do deslocamento atual para a pr�xima itera��o, 
     * como se tentasse manter a velocidade (por in�rcia) mas tivesse um atrito
     * causando desacelera��o.
     * Tamb�m reduz o deslocamento m�ximo de cada n� a um limite m�ximo
     * estipulado pela vari�vel deslocamentoMaximo do grafo em quest�o.
     * @param nos Conjunto de n�s nos quais os deslocamentos devem ser 
     * aplicados.
     */    
    void aplicarForcasCalculadas(Collection nos) {
        Point2D.Double p;
        NoSimples n;
        Dimension dimensaoDaTela = grafo.retornarDimensaoDaTela();
        
        
        for (Iterator i = nos.iterator(); i.hasNext();) {
            n = (NoSimples)i.next();
            p = (Point2D.Double)grafo.deslocamentoAcumulado.get(n);
            if (p!=null) {
                if (!n.eMovendo()) {
                    // Controlando deslocamento m�ximo .
                    n.x += Math.max(-grafo.deslocamentoMaximo, Math.min(grafo.deslocamentoMaximo, p.x));
                    n.y += Math.max(-grafo.deslocamentoMaximo, Math.min(grafo.deslocamentoMaximo, p.y));
                    // N� deve estar nos limites da tela.
                    grafo.posicionaNoNosLimites(n, dimensaoDaTela);
                    // Guarda metade do deslocamento como "heran�a" para o pr�ximo movimento
                    p.x /= 2.0;
                    p.y /= 2.0;
                } else {
                    p.x=0;
                    p.y=0;
                }
            }
        }
        
    }
    
}
