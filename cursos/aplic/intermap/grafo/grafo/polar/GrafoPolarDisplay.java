/*
 * GrafoPolarDisplay.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;          
import java.util.Observer;
import java.util.Observable;
import java.awt.geom.Point2D;
import java.io.*;
import java.awt.image.*;
import grafo.*;


/** Panel em que o Grafo Polar ser� mostrado. Cuida de todos os cliques efetuados
 * nessa estrutura visual.
 */

public class GrafoPolarDisplay extends GrafoDisplay
implements ComponentListener, MouseMotionListener, MouseListener, ActionListener, Observer {
    
    /** Pausa em anima��o (milissegundos). */
    public final static int DELAY = 50;
    
    /** Raz�o de movimenta��o para efetuar movimenta��o de um no (noMovendo)
     * (tem que ser >2). 
     */
    public final static int RAZAO = 5;

    /** Timer usado para anima��o de movimenta��o de um n�, quando ele retorna para sua
     * posi��o correta.
     */
    private Timer timer;
    
    /** Raio de um n�, usado na sua movimenta��o para retornar para sua posi��o correta.
     */
    private double raioF;
    
    /** �ngulo de um n�, usado na sua movimenta��o para retornar para sua posi��o
     * correta.
     */
    private double anguloF;
    
    /** Abscissa de um n�, usada na sua movimenta��o para retornar para sua posi��o
     * correta.
     */
    private int fx;
    
    /** Ordenada de um n�, usada na sua movimenta��o para retornar para sua posi��o
     * correta.
     */
    private int fy;

    /** Indica se a anima��o est� sendo executada ou n�o.
     */
    private boolean anim;
    
    /** Indica se o disco que representa a �rea proibida deve ou n�o ser mostrado na
     * tela. Tipicamente deve ser ajustado como True ou False quando o usu�rio estiver
     * ou n�o sobre um controle externo que ajuste o tamanho da �rea proibida.
     * Se True, desenha tamb�m uma linha do topo do disco at� a margem direita do
     * objeto.
     */    
    public boolean mostrarCirculoAreaProibida;

    /** Indica se o c�rculo que representa o anel central deve ou n�o ser mostrado na
     * tela. Tipicamente deve ser ajustado como True ou False quando o usu�rio estiver
     * ou n�o sobre um controle externo que ajuste o tamanho ou a rota��o do anel.
     */    
    public boolean mostrarCirculoCentral;

    /** Indica se o c�rculo que representa o anel perif�rico deve ou n�o ser mostrado na
     * tela. Tipicamente deve ser ajustado como True ou False quando o usu�rio estiver
     * ou n�o sobre um controle externo que ajuste o tamanho ou a rota��o do anel.
     */    
    public boolean mostrarCirculoPeriferico;
    
    /** Indica se linha horizontal que sai do topo do c�rculo do anel central e vai at�
     * a margem direta deve ou n�o ser mostrada na tela. Tipicamente deve ser ajustada
     * como True ou False quando o usu�rio estiver ou n�o sobre um controle externo que
     * ajuste o tamanho do anel central.
     */    
    public boolean mostrarLinhaAuxiliarDoAnelCentral;

    /** Indica se linha horizontal que sai do topo do c�rculo do anel perif�rico e vai
     * at� a margem direta deve ou n�o ser mostrada na tela. Tipicamente deve ser
     * ajustada como True ou False quando o usu�rio estiver ou n�o sobre um controle
     * externo que ajuste o tamanho do anel perif�rico.
     */    
    public boolean mostrarLinhaAuxiliarDoAnelPeriferico;

    /** Indica se deve ou n�o ser mostrada uma linha que indique o �ngulo inicial do
     * anel central. Tipicamente deve ser ajustada como True ou False quando o usu�rio
     * estiver ou n�o sobre um controle externo que ajuste a rota��o do anel central.
     */    
    public boolean mostrarAlcaDoAnelCentral;

    /** Indica se deve ou n�o ser mostrada uma linha que indique o �ngulo inicial do
     * anel perif�rico. Tipicamente deve ser ajustada como True ou False quando o
     * usu�rio  estiver ou n�o sobre um controle externo que ajuste a rota��o do anel
     * perif�rico.
     */    
    public boolean mostrarAlcaDoAnelPeriferico;
    
    /** Indica se deve ou n�o ser mostrado um c�rculo "preview" que indica qual o
     * tamanho de um anel (ou da �rea proibida) ao se modificar um controle.
     * Tipicamente deve ser ajustado como True ou False quando o usu�rio estiver ou n�o
     * sobre um controle externo que ajuste o tamanho de um dos an�is ou da �rea
     * proibida. Ao ser desenhado, tamb�m ser� tra�ada uma linha que parte de seu topo
     * e se dirige � margem direita do objeto.
     */    
    public boolean mostrarCirculoPreview;
    
    /** Raio do c�rculo "preview".
     */    
    public int raioCirculoPreview = 0;
    
    /** O grafo
     */
    private GrafoPolar grafoPolar;
    
    /** Cria uma nova inst�ncia de GrafoPolarDisplay.
     * @param grafo Grafo a ser desenhado no display.
     */
    public GrafoPolarDisplay(GrafoPolar grafo) {
        super(grafo);
        grafoPolar = grafo;
        //this.grafo = grafo;
        
        // Listeners
        /*
        addComponentListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        setPreferredSize(new Dimension(300,300));
         */

        // Elementos auxiliadores de manipula��o de tamanho e orienta��o dos
        // an�is
        mostrarCirculoAreaProibida = false;
        mostrarCirculoCentral = false;
        mostrarCirculoPeriferico = false;
        mostrarLinhaAuxiliarDoAnelCentral = false;
        mostrarLinhaAuxiliarDoAnelPeriferico = false;
        mostrarAlcaDoAnelCentral = false;
        mostrarAlcaDoAnelPeriferico = false;
        mostrarCirculoPreview = false;

        timer = new Timer(DELAY, this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        anim = false;
        
    }
    


    
    /**
     * Desenha elementos que ficar�o em segundo plano.
     * No Grafo Polar, esses elementos s�o
     * - os c�rculos da �rea proibida, do anel central, do anel perif�rico e de
     * pr�via
     * - as "al�as" (linhas retas) que ligam as barras de controle de tamanho aos seus
     * respectivos c�rculos.
     * @param g Objeto Graphics em que esses elementos ser�o desenhados.
     */
    public void desenhaElementosEmSegundoPlano(Graphics2D g) {
        Shape shape = g.getClip();
        Dimension d = grafoPolar.retornarDimensaoDaTela();

        Rectangle2D rect = new Rectangle2D.Float();
        rect.setRect((int) grafoPolar.centro.x - d.width / 2 + 1, 
        (int) grafoPolar.centro.y - d.height / 2 + 1,
        d.width - 1, d.height - 1);
        g.setClip(rect);
        
        int by;
        int raio;
        if (mostrarCirculoAreaProibida) {
            raio = grafoPolar.raioAreaProibida.getValue();
            g.setColor(grafoPolar.corAreaProibida);
            g.fillOval((int)grafoPolar.centro.x - raio, (int)grafoPolar.centro.y - raio,
            2 * raio, 2 * raio);
            g.setColor(Color.black);
            by = (int) (grafoPolar.centro.y - raio);
            g.drawLine((int) (grafoPolar.centro.x), by, (int) (grafoPolar.centro.x + d.width/ 2), by);
            g.drawOval((int) (grafoPolar.centro.x - (grafoPolar.centro.y - by)), by, 
            (int) (2 * (grafoPolar.centro.y - by)), (int) (2 * (grafoPolar.centro.y - by)));
        }
        if (mostrarCirculoCentral) {
            raio = grafoPolar.raioCentral.getValue();
            g.setColor(grafoPolar.corAreaCentral);
            by = (int) (grafoPolar.centro.y - raio);
            if (mostrarLinhaAuxiliarDoAnelCentral) {
                g.drawLine((int) (grafoPolar.centro.x), by,
                (int) (grafoPolar.centro.x + d.width / 2), by);
            }
            g.drawOval((int) (grafoPolar.centro.x - (grafoPolar.centro.y - by)), by,
            (int) (2 * (grafoPolar.centro.y - by)), (int) (2 * (grafoPolar.centro.y - by)));
            g.setStroke(new BasicStroke(4.0f));
            g.drawOval((int) (grafoPolar.centro.x - raio), (int) (grafoPolar.centro.y - raio),
            2 * raio, 2 * raio);
            g.setStroke(new BasicStroke());
        }
        if (mostrarCirculoPeriferico) {
            raio = grafoPolar.raioPeriferico.getValue();
            g.setColor(grafoPolar.corAreaPeriferica);
            by = (int) (grafoPolar.centro.y - raio);
            if (mostrarLinhaAuxiliarDoAnelPeriferico) {
                g.drawLine((int) (grafoPolar.centro.x), by, 
                (int) (grafoPolar.centro.x + d.width / 2), by);
            }
            g.drawOval((int) (grafoPolar.centro.x - (grafoPolar.centro.y - by)), by,
            (int) (2 * (grafoPolar.centro.y - by)), (int) (2 * (grafoPolar.centro.y - by)));
            g.setStroke(new BasicStroke(4.0f));
            g.drawOval((int) (grafoPolar.centro.x - raio), (int) (grafoPolar.centro.y - raio), 
            2 * raio, 2 * raio);
            g.setStroke(new BasicStroke());
        }
        if (mostrarCirculoPreview) {
            g.setColor(Color.lightGray);
            g.drawOval((int)grafoPolar.centro.x - raioCirculoPreview, 
            (int)grafoPolar.centro.y-raioCirculoPreview,
            2 * raioCirculoPreview, 2 * raioCirculoPreview);
            g.drawLine((int)grafoPolar.centro.x, (int)grafoPolar.centro.y-raioCirculoPreview, 
            (int)grafoPolar.centro.x + d.width / 2,
            (int)grafoPolar.centro.y-raioCirculoPreview);
        }
        if (mostrarAlcaDoAnelCentral && grafoPolar.numeroNosCentrais.getValue()>1) {
            desenharAlca(g, grafoPolar.anguloInicialNosCentrais.getValue(), grafoPolar.raioCentral.getValue(), grafoPolar.corAreaCentral);
            desenharAlca(g, grafoPolar.anguloInicialNosCentrais.getValue() + 180, grafoPolar.raioCentral.getValue(), grafoPolar.corAreaCentral);            
        }
        if (mostrarAlcaDoAnelPeriferico && grafoPolar.numeroNosPerifericos.getValue()>0) {
            desenharAlca(g, grafoPolar.anguloInicialNosPerifericos.getValue(), grafoPolar.raioPeriferico.getValue(), grafoPolar.corAreaPeriferica);
            desenharAlca(g, grafoPolar.anguloInicialNosPerifericos.getValue() + 180, grafoPolar.raioPeriferico.getValue(), grafoPolar.corAreaPeriferica);            
        }
        
        g.setClip(shape);        
    }
    
    /** Desenha a al�a relacionada � rota��o dos an�is central e perif�rico.
     * @param g Onde o grafo ser� desenhado.
     * @param angulo �ngulo em que a al�a deve ser desenhada.
     * @param raioInicial Raio a partir do qual a al�a deve ser desenhada.
     * @param cor Cor com a qual a al�a deve ser desenhada.
     */    
    public void desenharAlca(Graphics2D g, double angulo, int raioInicial, Color cor) {
        Point2D.Double p1,p2,p3;
        int tamanhoAlca = 30;
        int raioInicioAlca = Math.max(grafoPolar.raioCentral.getValue(), grafoPolar.raioPeriferico.getValue())+50;
        int raioFimAlca = raioInicioAlca+tamanhoAlca;
        g.setStroke(new BasicStroke(4.0f));
        p1 = PolarCartesiano.polar2Cartesiano(raioInicial, angulo, grafoPolar.centro);
        p2 = PolarCartesiano.polar2Cartesiano(raioFimAlca, angulo, grafoPolar.centro);
        p3 = PolarCartesiano.polar2Cartesiano(raioInicioAlca, angulo, grafoPolar.centro);        
        g.setColor(cor);
        g.drawLine((int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y);

        g.setStroke(new BasicStroke(6.0f));        
        g.setColor(Color.black);
        g.drawLine((int)p3.x,(int)p3.y,(int)p2.x,(int)p2.y);        
        
        g.setStroke(new BasicStroke());
    }
    
    /** Fun��o de ComponentListener.
     * @param e Evento do mouse.
     */
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        grafoPolar.centro.x = getWidth() / 2;
        grafoPolar.centro.y = getHeight() / 2;
    }
    
   /** Trata dos casos em que o mouse est� se movendo sobre o objeto.
     * Estabelece qual � o n� sobre o qual o mouse est� (noFoco).
     * @param e Evento do mouse.
     */
    public void mouseMoved(MouseEvent e) {
        if (!anim) { // n�o faz nada durante animacao
            super.mouseMoved(e);
        }
    }
    
    /** Trata dos casos em que o objeto foi clicado.
     * @param e Evento do mouse.
     */
    public void mouseClicked(MouseEvent e) {
        atualizarCoordenadasDoMouseInternas(e);
        int cnt = e.getClickCount(), btn = e.getModifiers();
        
        // Clique em bot�o principal: Marcar um n�.
        if (btn == InputEvent.BUTTON1_MASK && cnt == 1) {
            boolean ok = marcarNoSobMouse();
            if (ok) {
                mouseMoved(e);
            }
        }
        
        // Clique em bot�o central: mover n� para outro anel
        if (!anim && btn  == InputEvent.BUTTON2_MASK && cnt == 1) {
            NoPolar no = (NoPolar)noSobMouse();
            if (no != null) {
                if (no.eCentral()) {
                    grafoPolar.moverParaAnelPeriferico(no);
                } else {
                    grafoPolar.moverParaAnelCentral(no);
                }
                repaint();
            }
        }
    }
    
    /** N�o implementado.
     * @param e Evento do mouse.
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /** N�o implementado.
     * @param e Evento do mouse.
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /** Funcao de MouseListener.
     * @param e Evento do mouse.
     */
    public void mousePressed(MouseEvent e) {
        if (!anim) { // n�o faz nada durante animacao
            super.mousePressed(e);
        }
    }
    
    /** Funcao de MouseListener.
     * @param e Evento do mouse.
     */
    public void mouseReleased(MouseEvent e) {
        if (!anim) { // n�o faz nada durante animacao
            int btn = e.getModifiers();
            
            // Se bot�o principal foi solto
            
            if (btn  == InputEvent.BUTTON1_MASK) {
                if (grafoPolar.retornarNoMovendo() != null) {
                    NoPolar noMovendoPolar = (NoPolar)grafoPolar.retornarNoMovendo();
                    double raio   = noMovendoPolar.raio;   // salvar posicao original
                    
                    double angulo = noMovendoPolar.angulo;
                    grafoPolar.posicionarCorretamenteNoMovendo();
                    
                    raioF   = noMovendoPolar.raio;
                    anguloF = noMovendoPolar.angulo;
                    fx = (int)PolarCartesiano.polar2Cartesiano_x(noMovendoPolar.raio, noMovendoPolar.angulo, grafoPolar.centro);
                    fy = (int)PolarCartesiano.polar2Cartesiano_y(noMovendoPolar.raio, noMovendoPolar.angulo, grafoPolar.centro);
                    
                    noMovendoPolar.raio   = raio;
                    noMovendoPolar.angulo = angulo;
                    
                    startAnimation();
                } else {
                    repaint();
                }
            }
            testarPopup(e);
        }
    }
    
//    public void mouseDragged(MouseEvent e) {
//        super.mouseDragged(e);
//    }
    
    /** Invoca anima��o que faz a movimenta��o de um n� para sua posi��o correta. */
    public synchronized void startAnimation() {
        anim = true;
        //Inicia a anima��o.
        if (!timer.isRunning()) {
            timer.start();
        }
    }
    
    /** P�ra a animacao que faz a movimenta��o de um n� para sua posi��o correta. */
    public synchronized void stopAnimation() {
        anim = false;
        //P�ra a thread de anima��o.
        if (timer.isRunning()) {
            timer.stop();
        }
    }
    
    /** Trata diversas a��es referentes a diversos eventos, como
     * @param e Evento do mouse.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == timer) { // a��o de timer de anima��o -> mover no para nova posi��o
            NoPolar noMovendoPolar = (NoPolar)grafoPolar.retornarNoMovendo();
            int ax = (int) PolarCartesiano.polar2Cartesiano_x(noMovendoPolar.raio, noMovendoPolar.angulo, grafoPolar.centro);
            int ay = (int) PolarCartesiano.polar2Cartesiano_y(noMovendoPolar.raio, noMovendoPolar.angulo, grafoPolar.centro);
            
            int passox = Math.abs(ax - fx);
            int passoy = Math.abs(ay - fy);
            if (passox < 5 && passoy < 5) { // parar anima��o.
                noMovendoPolar.raio = raioF;
                noMovendoPolar.angulo = anguloF;
                stopAnimation();
                grafoPolar.desfazerRegistrarNoMovendo();
                noMovendoPolar = null;
               
                NoPolar no = (NoPolar)noSobMouse();
                if (noFoco != no) {
                    if (noFoco != null) {
                        noFoco.desfazerEscolher();
                    }
                    noFoco = no;
                    if (noFoco != null) {
                        noFoco.escolher();
                    }
                    repaint();
                }
                ajustarCursor();
                
                

            } else {
                passox = passox / RAZAO + 1;
                passoy = passoy / RAZAO + 1;
                if (fx < ax) {
                    ax -= passox; 
                } else {
                    ax += passox;
                }
                if (fy < ay) {
                    ay -= passoy; 
                } else {
                    ay += passoy;
                }
                noMovendoPolar.raio   = PolarCartesiano.cartesiano2Polar_raio(ax, ay, grafoPolar.centro);
                noMovendoPolar.angulo = PolarCartesiano.cartesiano2Polar_angulo(ax, ay, grafoPolar.centro);
            }
            repaint();
        } else {
            super.actionPerformed(e);
        }
    }
    
    /** Este m�todo � chamado sempre que os objetos observados por este objeto s�o
     * modificados. Uma aplica��o chama o m�todo <code>nofifyObservers</code> de um
     * objeto <tt>Observable</tt>, para notificar todos os objetos sobre a mudan�a.
     * @param o O objeto observado (Observable).
     * @param arg Um argumento passado pelo m�todo <code>notifyObservers</code>.
     * Neste caso, este argumento est� sendo ignorado.
     */
    public void update(Observable o, Object arg) {
        grafoPolar.reposicionarNos();
        repaint();
    }
  
}
