/*
 * PolarCartesiano.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;
import java.awt.Point;
import java.awt.geom.Point2D;
import grafo.*;

/** Classe que re�ne m�todos relacionados �s coordenadas polares e cartesianas.
 * Dentre os m�todos apresentados, cont�m m�todos para convers�o polar/cartesiano
 * e cartesiano/polar.
 */
public class PolarCartesiano {
    
    /** N�o se deve criar uma inst�ncia de PolarCartesiano. */
    private PolarCartesiano() {
        // Evita que algu�m seja sub-classe de PolarCartesiano.
    }
    
    /** Dado um ponto P com coordenadas polares (raio, angulo), obt�m a abscissa
     * da coordenada cartesiana equivalente, considerando como origem do eixo 
     * horizontal o valor de centroCartesiano.x
     * @param raio Raio do ponto em coordenadas polares.
     * @param angulo �ngulo do ponto em coordenadas polares.
     * @param centroCartesiano Coordenadas do centro dos eixos cartesiano/polar
     * na tela.
     * @return Abscissa do ponto P.
     */
    public static double polar2Cartesiano_x(double raio, double angulo, Point2D.Double centroCartesiano) {
        return raio*Math.cos(Math.toRadians(angulo))+centroCartesiano.x;
    }

    /** Dado um ponto P com coordenadas polares (raio, angulo), obt�m a ordenada 
     * da coordenada cartesiana equivalente, considerando como origem do eixo 
     * vertical o valor de centroCartesiano.y
     * @param raio Raio do ponto em coordenadas polares.
     * @param angulo �ngulo do ponto em coordenadas polares.
     * @param centroCartesiano Coordenadas do centro dos eixos cartesiano/polar
     * na tela.
     * @return Ordenada do ponto P.
     */
    public static double polar2Cartesiano_y(double raio, double angulo, Point2D.Double centroCartesiano) {
        return raio*Math.sin(Math.toRadians(angulo))+centroCartesiano.y;
    }

    /** Dado um ponto P com coordenadas polares (raio, angulo), obt�m as 
     * coordenadas cartesianas equivalentes, considerando como origem dos eixos 
     * cartesiano/polar o ponto centroCartesiano.
     * @param raio Raio do ponto em coordenadas polares.
     * @param angulo �ngulo do ponto em coordenadas polares.
     * @param centroCartesiano Coordenadas do centro dos eixos cartesiano/polar
     * na tela.
     * @return Ponto P.
     */    
    public static Point2D.Double polar2Cartesiano(double raio, double angulo, Point2D.Double centroCartesiano) {
        return new Point2D.Double(polar2Cartesiano_x(raio,angulo,centroCartesiano), polar2Cartesiano_y(raio,angulo,centroCartesiano));
    }
    
    /** Dada uma coordenada cartesiana (x,y), retorna o raio da coordenada
     * polar equivalente, considerando centroCartesiano como centro dos eixos 
     * cartesiano/polar
     * @param x Abscissa do ponto em coordenadas cartesianas.
     * @param y Ordenada do ponto em coordenadas cartesianas.
     * @param centroCartesiano Coordenadas do centro dos eixos cartesiano/polar 
     * na tela.
     * @return �ngulo do ponto P.
     */
    public static double cartesiano2Polar_raio(int x, int y, Point2D.Double centroCartesiano) {
        return Point.distance(x,y,centroCartesiano.x, centroCartesiano.y);
    }

    /** Dada uma coordenada cartesiana (x,y), retorna o �ngulo da coordenada
     * polar equivalente, considerando (centroCartesiano.x,centroCartesiano.y) 
     * como origem do plano cartesiano.
     * @param x Abscissa do ponto em coordenadas cartesianas.
     * @param y Ordenada do ponto em coordenadas cartesianas.
     * @param centroCartesiano Coordenadas do centro dos eixos cartesiano/polar 
     * na tela.
     * @return �ngulo de (x,y) com rela��o a centroCartesiano.
     */
    public static double cartesiano2Polar_angulo(int x, int y, Point2D.Double centroCartesiano) {
        return retornarAnguloPontos(x,y,centroCartesiano.x, centroCartesiano.y);
    }

    /** Dados dois pontos A=(x,y) e C=(xc,yc), calcula qual o �ngulo do ponto A,
     * considerando C como centro.
     * @param x Abscissa de A.
     * @param y Ordenada de A.
     * @param xc Abscissa de C.
     * @param yc Ordenada de C.
     * @return �ngulo de A com rela��o a C.
     */
    public static double retornarAnguloPontos(double x, double y, double xc, double yc) {
        double r = Point.distance(x,y,xc,yc);
        double a;
        if (r!=0) {
            a = Math.toDegrees(Math.asin((y-yc)/r));
            if (x < xc) {
                a = 180 - a;
            }
            if (a < 0) {
                a = a + 360;
            }
        } else {
            a=0;
        }
        return a;
    }

    /** Calcula a dist�ncia entre dois pontos A=(xa,ya) e B=(xb,yb).
     * @param xa Abscissa de A.
     * @param ya Ordenada de A.
     * @param xb Abscissa de B.
     * @param yb Ordenada de B.
     * @return �ngulo de A com rela��o a B.
     */
    public static double distanciaEntrePontos(double xa, double ya, double xb, double yb) {
        return Point.distance(xa,ya,xb,yb);
    }
    
    /** Retorna a menor dist�ncia angular entre dois �ngulos.
     * @param a �ngulo.
     * @param b �ngulo.
     * @return Menor dist�ncia angular entre a e b.
     */
    public static double menorDistanciaEntreAngulos(double a, double b) {
        double aAux = Util.normalizarAngulo(a);
        double bAux = Util.normalizarAngulo(b);
        double d1 = Util.normalizarAngulo(bAux - aAux);
        double d2 = 360 - d1;
        return(Math.min(d1,d2));
    }    

}
