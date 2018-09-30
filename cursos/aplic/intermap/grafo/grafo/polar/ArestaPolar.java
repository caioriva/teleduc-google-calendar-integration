/*
 * ArestaPolar.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

import java.awt.*;
import java.io.*;
import java.awt.geom.Point2D;
import grafo.*;

/** Classe que define as caracter�sticas de uma aresta pertencente a um grafo polar.
 * Estende a classe Aresta, para desenhar arestas retas e curvas.
 */
public class ArestaPolar extends Aresta {

    /** N�mero de partes em que uma aresta em forma de arco � dividida.
     */
    private int numPartesArco = 20;
    
    /** Raio da aresta circular.
     */
    public static int raioArestaCircular = 20;
    
    /** Cria uma aresta ligando dois n�s, sem informa��o associada (info = null)
     *  e sem peso (peso = 0)
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     */
    public ArestaPolar(NoPolar no1, NoPolar no2) {
        this(no1, no2, 0, null);
    }
    
    /** Cria uma aresta ligando dois n�s, sem informa��o associada (info = null)
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta.
     */
    public ArestaPolar(NoPolar no1, NoPolar no2, int peso) {
        this(no1, no2, peso, null);
    }
    
    /** Cria uma aresta ligando dois n�s, sem peso associado (peso = 0)
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param info Informa��o associada � aresta.
     */
    public ArestaPolar(NoPolar no1, NoPolar no2, Info info) {
        this(no1, no2, 0, info);
    }
    
    /** Cria uma aresta ligando dois n�s, com peso e informa��o associados.
     * @param no1 Primeiro n� da aresta.
     * @param no2 Segundo n� da aresta.
     * @param peso Peso da aresta.
     * @param info Informa��o associada � aresta.
     */
    public ArestaPolar(NoPolar no1, NoPolar no2, int peso, Info info) {
        super(no1, no2, peso, info);
    }
    
    
    /** Calcula a coordenada do centro do c�rculo que comp�em a auto-aresta
     * de um n� informado.
     * @param no N� do qual se deseja saber o centro da auto-aresta
     * @param g Grafo do qual o n� faz parte.
     * @return Retorna o ponto no centro da auto-aresta.
     */
    public Point retornarCentroAutoAresta(No no, Grafo g) {
        Point resposta = null;
        if (no instanceof NoPolar && g instanceof GrafoPolar) {
            Point2D.Double p = PolarCartesiano.polar2Cartesiano(((NoPolar)no).raio+raioArestaCircular, ((NoPolar)no).angulo, ((GrafoPolar)g).centro);
            resposta = new Point((int)p.x, (int)p.y);
        } 
        return resposta;
    }
    
    /** Desenhar arco.
     * @param g Onde o arco ser� desenhado.
     * @param x Abscissa do centro do c�rculo que cont�m o arco.
     * @param y Ordenada do centro do c�rculo que cont�m o arco.
     * @param raio Raio do c�rculo que cont�m o arco.
     * @param xi Abscissa do in�cio do arco.
     * @param yi Ordenada do in�cio do arco.
     * @param xf Abscissa do fim do arco.
     * @param yf Ordenada do fim do arco.
     */
    private void arco(Graphics g, int x, int y, double raio, double xi, double yi, double xf, double yf) {
        
        double anguloi = 0.0, angulof = 0.0;
        
        if ((xi-x) == 0.0) {
            if (yi-y > 0.0) {
                anguloi = 90.0;
            } else {
                anguloi = 270.0;
            }
        } else {
            if (xi-x > 0) {
                anguloi = 360 - (Math.atan((y-yi)/(xi-x)))/Math.PI*180;
            } else {
                anguloi = 180 - (Math.atan((y-yi)/(xi-x)))/Math.PI*180;
            }
        }
        anguloi = anguloi - ((int)(anguloi/360))*360;
        
        if (xf-x == 0.0) {
            if (yf-y > 0.0) {
                angulof = 90.0;
            } else {
                angulof = 270.0;
            }
        } else {
            if (xf-x > 0.0) {
                angulof = 360 - (Math.atan((y-yf)/(xf-x)))/Math.PI*180;
            } else {
                angulof = 180 - (Math.atan((y-yf)/(xf-x)))/Math.PI*180;
            }
        }
        angulof = angulof - ((int)(angulof/360))*360;
        
        if (anguloi < angulof) {
            anguloi = anguloi + 360.0;
        }
        
        double step = (angulof-anguloi) / numPartesArco;
        double a1 = anguloi;
        double x1 = raio * Math.cos(a1/180*Math.PI)+x;
        double y1 = raio * Math.sin(a1/180*Math.PI)+y;
        
        for(int i = 0; i < numPartesArco; i++) {
            a1 = a1 + step;
            double ox1 = x1, oy1 = y1;
            x1 = raio * Math.cos(a1/180*Math.PI)+x;
            y1 = raio * Math.sin(a1/180*Math.PI)+y;
            g.drawLine((int) ox1, (int) oy1, (int) x1, (int) y1);
        }
    }
    
    /** Desenha aresta para o grafo polar.
     * @param grafo Grafo ao qual a aresta pertence.
     * @param g Objeto em que a aresta ser� desenhada.
     */
    public void desenhar(Grafo grafo, Graphics g) {
        if (grafo instanceof GrafoPolar) {
            NoPolar no1 = (NoPolar)(this.no1);
            NoPolar no2 = (NoPolar)(this.no2);
            GrafoPolar grafoPolar = (GrafoPolar)grafo;
            Graphics2D g2 = (Graphics2D) g;
            
            
//            // Desenhar aresta mais gorda se ela � marcada ou atingida.
//            if (eMarcada() || eAtingida()) g2.setStroke(new BasicStroke(2.0f));
            
            // Desenhar aresta mais gorda se ela � atingida.
            if (eAtingida()) {
                g2.setStroke(new BasicStroke(2.0f));
            }
            
            
            g.setColor(retornarCor(grafo));
            
//            if (no1.eCentral() || no2.eCentral() || eMarcada() || eAtingida()) {
            if (no1.eCentral() || no2.eCentral() || eAtingida()) {            
                if (no1 != no2) { // Arestas comuns (retas)
                    g.drawLine((int) PolarCartesiano.polar2Cartesiano_x(no1.raio,no1.angulo, grafoPolar.centro),
                    (int) PolarCartesiano.polar2Cartesiano_y(no1.raio,no1.angulo, grafoPolar.centro),
                    (int) PolarCartesiano.polar2Cartesiano_x(no2.raio,no2.angulo, grafoPolar.centro),
                    (int) PolarCartesiano.polar2Cartesiano_y(no2.raio,no2.angulo, grafoPolar.centro));
                } else {
                    desenharArestaParaOProprioNo(grafo, g, no1);
                }
            } else {
                
                // Algoritmo de c�lculo de curvatura da aresta distorcida.
                
                double x1,x2,y1,y2;
                double a1,a2;
                // Garante que x1,y1 � o n� de menor �ngulo (ou seja, a1<a2)
                if (no1.angulo<no2.angulo) {
                    x1 = PolarCartesiano.polar2Cartesiano_x(no1.raio,no1.angulo, grafoPolar.centro);
                    y1 = PolarCartesiano.polar2Cartesiano_y(no1.raio,no1.angulo, grafoPolar.centro);
                    x2 = PolarCartesiano.polar2Cartesiano_x(no2.raio,no2.angulo, grafoPolar.centro);
                    y2 = PolarCartesiano.polar2Cartesiano_y(no2.raio,no2.angulo, grafoPolar.centro);
                    a1 = no1.angulo;
                    a2 = no2.angulo;
                } else {
                    x2 = PolarCartesiano.polar2Cartesiano_x(no1.raio,no1.angulo, grafoPolar.centro);
                    y2 = PolarCartesiano.polar2Cartesiano_y(no1.raio,no1.angulo, grafoPolar.centro);
                    x1 = PolarCartesiano.polar2Cartesiano_x(no2.raio,no2.angulo, grafoPolar.centro);
                    y1 = PolarCartesiano.polar2Cartesiano_y(no2.raio,no2.angulo, grafoPolar.centro);
                    a2 = no1.angulo;
                    a1 = no2.angulo;
                }
                
                double angulo_medio = (a1+a2)/2;
                if (a2-a1 < 180) {
                    angulo_medio = angulo_medio-180;
                }
                if (angulo_medio>360) {
                    angulo_medio = angulo_medio-360;
                }
                if (angulo_medio<360) {
                    angulo_medio = angulo_medio+360;
                }
                
                // Ponto de distor��o
                
                double d_a_b_quad = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
                double d_c_mlinha = grafoPolar.retornarRaioPeriferico()*grafoPolar.retornarRaioPeriferico() - d_a_b_quad/4;
                if (d_c_mlinha > 0) {
                    d_c_mlinha = Math.sqrt(d_c_mlinha);
                }
                double d_mlinha_m = grafoPolar.retornarRaioAreaProibida() - d_c_mlinha;
                
                double raio_distorcao = 0.0;
                if (d_mlinha_m > 0) {
                    // calculando CD
                    raio_distorcao = (grafoPolar.retornarRaioPeriferico()*grafoPolar.retornarRaioPeriferico()-grafoPolar.retornarRaioAreaProibida()*grafoPolar.retornarRaioAreaProibida()) /
                    (2*(grafoPolar.retornarRaioAreaProibida() - d_c_mlinha));
                }
                if (d_mlinha_m > 0 && raio_distorcao < 10000) {
                    double origem_distorcao_x = PolarCartesiano.polar2Cartesiano_x(raio_distorcao,angulo_medio, grafoPolar.centro);
                    double origem_distorcao_y = PolarCartesiano.polar2Cartesiano_y(raio_distorcao,angulo_medio, grafoPolar.centro);
                    double raio_aresta = raio_distorcao + d_c_mlinha + d_mlinha_m;
                    
                    
                /* Como a aresta � desenhada pelo comando arc sempre no sentido
                anti-hor�rio, � preciso dizer quem � o primeiro n� e quem � o �ltimo,
                para que as arestas sempre sejam desenhadas com o lado c�ncavo voltado
                para o p�lo do grafo */
                    if (no1 != no2) {
                        if (a2-a1 < 180) {
                            // O comando arc foi abandonado por apresentar imprecis�es indesejadas
                            arco(g, (int) origem_distorcao_x, (int) origem_distorcao_y,
                            raio_aresta,x2,y2,x1,y1);
                        //                        g.drawArc((int) x2, (int) y2, (int) (x1-x2), (int) (y1-y2), (int) a2, (int) (a2-a1));
                        } else {
                            arco(g, (int) origem_distorcao_x, (int) origem_distorcao_y,
                            raio_aresta,x1,y1,x2,y2);
                        //                        g.drawArc((int) x1, (int) y1, (int) (x2-x1), (int) (y2-y1), (int) a1, (int) (a1-a2));
                        }
                    } else {
                        desenharArestaParaOProprioNo(grafo, g, no1);
                    }
                    
                } else {
                    // raio de distor��o muito grande
                    if (no1 != no2) {
                        // Desenhar retas em vez de arcos}
                        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                    } else {
                        desenharArestaParaOProprioNo(grafo, g, no1);
                    }
                    
                }
            }
            g2.setStroke(new BasicStroke());

        }
    }
    
    /** Informa se a aresta � perif�rica, ou seja, se ela conecta n�s
     * pertencentes ao anel perif�rico.
     * @return True se a aresta � perif�rica, false caso contr�rio.
     */
    public boolean ePeriferica() {
        //return !(((NoPolar)no1).eCentral()) && !(((NoPolar)no2).eCentral());
        boolean resposta = false;
        if (no1 instanceof NoPolar && no2 instanceof NoPolar) {
            resposta = !((NoPolar)no1).eCentral() && !((NoPolar)no2).eCentral();
        }
        return resposta;
    }
    
    /** Informa se a aresta � centro-perif�rica, ou seja, se ela conecta um n�
     * pertencente ao anel central a outro pertencente ao anel perif�rico, ou
     * vice-versa.
     * @return True se a aresta � centro-perif�rica, false caso contr�rio.
     */
    public boolean eCentroperiferica() {
        boolean resposta = false;
        if (no1 instanceof NoPolar && no2 instanceof NoPolar) {
            resposta = (((NoPolar)no1).eCentral() && !(((NoPolar)no2).eCentral())) || (!(((NoPolar)no1).eCentral()) && ((NoPolar)no2).eCentral());
        }
        return resposta;
    }
    
    /** Informa se a aresta � central, ou seja, se ela conecta n�s
     * pertencentes ao anel central.
     * @return True se a aresta � central, false caso contr�rio.
     */
    public boolean eCentral() {
        boolean resposta = false;
        if (no1 instanceof NoPolar && no2 instanceof NoPolar) {        
            resposta = ((NoPolar)no1).eCentral() && ((NoPolar)no2).eCentral();
        }
        return resposta;
    }
    
    /** Retorna chave da aresta, para ser usada em estruturas de Hash que 
     * indexem a aresta pelos �ngulos de seus n�s e pelos an�is a que pertencem.
     * @return Chave da aresta
     */
    public ChaveDeAresta retornarChave() {
        return new ChaveDeAresta(((NoPolar)no1).angulo, ((NoPolar)no1).eCentral(),
        ((NoPolar)no2).angulo, ((NoPolar)no2).eCentral());
    }
    
    /** Retorna o hashCode da aresta.
     * @return hashCode.
     */
//    public int hashCode() {
//        return(retornarChave().hashCode());
//    }
    
//    /** Verifica se um objeto O � igual � aresta polar instanciada A.
//     * A igualdade acontece se a chave das duas arestas � a mesma.
//     * @param o Objeto a ser comparado com a aresta instanciada.
//     * @return True se o objeto � igual � aresta, false caso contr�rio.
//     */
//    public boolean equals(Object o) {
//        if (o instanceof ArestaPolar) {
//            ArestaPolar a = (ArestaPolar)o;
//            boolean ok = this.retornarChave().equals(a.retornarChave());
//            return ok;
//        } else {
//            return false;
//        }
//    }
    
}
