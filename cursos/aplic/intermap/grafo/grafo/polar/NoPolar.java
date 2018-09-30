/*
 * NoPolar.java
 * Vers�o: 2004-08-24
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.io.*;
import grafo.*;

/** Estende classe No para implementar caracter�sticas de um n� utilizado em um
 * grafo polar.
 * Dentre essas caracter�sticas, destacam-se: ser ou n�o marcado, atingido ou
 * escolhido, estar ou n�o sendo movido, grupo do n�, e cor individual.
 * � respons�vel tamb�m por desenhar o n� e por retornar informa��es sobre sua
 * conex�o com o grafo polar.
 */
public class NoPolar extends No implements Aneis {
    
    /** Raio - posi��o do n�. */
    public double raio;   // coordenadas polares

    /** �ngulo - posi��o do n�. */
    public double angulo; // coordenadas polares

    /** Informa em que anel do grafo polar o n� est� posicionado. */
    private boolean anel;
    
    /** Cria uma nova inst�ncia de NoPolar, sem peso nem informa��o associada.
     * @param nome Nome do n� a ser criado.
     * @param anel Anel em que o n� deve ser criado.
     */
    public NoPolar(String nome, boolean anel) {
        this(nome, anel, 0, null);
    }
    
    /** Cria uma nova inst�ncia de NoPolar, com peso e sem informa��o associada.
     * @param nome Nome do n� a ser criado.
     * @param anel Anel em que o n� deve ser criado.
     * @param peso Peso do n�.
     */
    public NoPolar(String nome, boolean anel, int peso) {
        this(nome, anel, peso, null);
    }
    
    /** Cria uma nova inst�ncia de NoPolar, sem peso mas com informa��o associada.
     * @param nome Nome do n� a ser criado.
     * @param anel Anel em que o n� deve ser criado.
     * @param info Informa��o associada.
     */
    public NoPolar(String nome, boolean anel, Info info) {
        this(nome, anel, 0, info);
    }
    
    /** Cria uma nova inst�ncia de NoPolar, com peso e informa��o associada.
     * @param nome Nome do n� a ser criado.
     * @param anel Anel em que o n� deve ser criado.
     * @param peso Peso do n�.
     * @param info Informa��o associada.
     */
    public NoPolar(String nome, boolean anel, int peso, Info info) {
        super(nome, peso, info);
        this.anel = anel;
        raio = 0.0;
        angulo = 0.0;
        cor = new CorNo(GrafoPolar.defaultCorNo);
        grupo = GrafoPolar.defaultGrupo;
        //abreviado = false;
        removerCorIndividual();
   }
    
    /** Cria uma nova inst�ncia de NoPolar, copiando um n� informado.
     * @param no N� a ser copiado
     */
    public NoPolar(NoPolar no) {
        this(no.nome, no.anel, no.peso, no.info);
        raio = no.raio;
        angulo = no.angulo;
        cor = no.cor;
        grupo = no.grupo;
    }
    
    /** Desenha o n�.
     * @param grafo Grafo ao qual o n� pertence. O n� precisa saber disso, uma vez que ele precisa
     * saber qual � o centro do grafo para, assim, poder se desenhar.
     * @param g Onde o n� ser� desenhado.
     */
    public void desenhar(Grafo grafo, Graphics g) {
        if (grafo instanceof GrafoPolar) {
            GrafoPolar gr = (GrafoPolar)grafo;
            Point2D.Double centro = new Point2D.Double((int)gr.centro.x, (int)gr.centro.y);
            x = (int) PolarCartesiano.polar2Cartesiano_x(raio, angulo, centro);
            y = (int) PolarCartesiano.polar2Cartesiano_y(raio, angulo, centro);
            String textoaux = nomeAMostrar();
            desenharNo(x,y, textoaux, g);
        } 
    }

    /** Informa se o n� pertence ou n�o ao anel central.
     * @return True se o n� pertence ao anel central, false caso contr�rio.
     */    
    public boolean eCentral() {
        return anel == ANEL_CENTRAL;
    }
    
    /** Informa se o n� pertence ou n�o ao anel perif�rico.
     * � exatamente o contr�rio de eCentral().
     * @return True se o n� pertence ao anel perif�rico, false caso contr�rio.
     */    
    public boolean ePeriferico() {
        return anel == ANEL_PERIFERICO;
    }
    
    
    
    /** Transfere o n� para o anel perif�rico. */    
    public void moverParaAnelPeriferico() {
        anel = ANEL_PERIFERICO;
    }
    
    /** Transfere o n� para o anel central. */    
    public void moverParaAnelCentral() {
        anel = ANEL_CENTRAL;
    }

    /** Retorna o anel ao qual o n� pertence.
     * @return Retorna ANEL_CENTRAL se o n� pertence ao anel central, ou ANEL_PERIFERICO se
     * pertence ao perif�rico.
     */    
    public boolean retornarAnel() {
        return anel;
    }
    

    
}

