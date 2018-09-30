/*
 * SeletorDuploDeAngulos.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.polar;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Point2D;

/** Objeto para sele��o de um par de �ngulos, composto por dois seletores
 * superpostos de tamanhos distintos.
 */
public class SeletorDuploDeAngulos extends JPanel 
implements MouseListener, MouseMotionListener {
    
    /** Largura do objeto. */    
    public final int largura = 120;
    /** Altura do objeto. */    
    public final int altura = 120;
    /** Vetor contendo o par de �ngulos do objeto. Esses �ngulos s�o objetos observ�veis
     * (Observable), de modo que suas altera��es podem ser monitoradas externamente.
     */    
    private ObservableAngle[] angulo;
    /** Cor de fundo do objeto quando ele est� ativado. */    
    private Color corDeFundoAtivado = Color.white;
//    /** Cor de fundo do objeto quando ele est� desativado. */    
//    private Color corDeFundoDesativado = new Color(240,240,240);
    /** Par de cores de frente do objeto (uma cor para cada seletor) quando o objeto est�
     * ativado.
     */    
    private Color[] corDeFrenteAtivado = {Color.red, Color.blue};
    /** Par de cores de frente do objeto (uma cor para cada seletor) quando o objeto
     * est� desativado.
     */    
    private Color[] corDeFrenteDesativado = {new Color(200,200,200), new Color(210,210,210)};
    /** Cor dos punhos do objeto (fim das al�as de movimenta��o) quando o objeto est�
     * ativado.
     */    
    private Color corDePunhoAtivado = Color.black;
    /** Cor dos punhos do objeto (fim das al�as de movimenta��o) quando o objeto est�
     * desativado.
     */    
    private Color corDePunhoDesativado = Color.gray;
    /** Par de raios dos c�rculos referentes a cada seletor. */    
    private final int[] raioCirculo = {20, 30};
    /** Par de tamanhos das al�as de movimenta��o de cada seletor. */    
    private final int[] tamanhoAlca = {30, 20};
    /** Cursor em formato de seta. */    
    private Cursor cursorSeta = new Cursor(Cursor.DEFAULT_CURSOR);
    /** Cursor em formato de m�o. */    
    private Cursor cursorMao = new Cursor(Cursor.HAND_CURSOR);
    /** Registra qual o estado de cada um dos dois seletores do objeto: ativo ou n�o. */    
    private boolean[] ativado = {false, false};
    /** Indica qual o seletor com o qual se est� trabalhando. Pode ter os valores
     * SELETOR_INTERNO, SELETOR_EXTERNO E SELETOR_NENHUM.
     */    
    private int seletorEscolhido= SELETOR_NENHUM; // nenhum seletor escolhido no momento.
    /** Indica se o mouse est� ou n�o dentro do controle. � um valor observ�vel
     * (Observable), podendo ser monitorado por outros objetos.
     */    
    public ObservableBoolean mouseDentroDoControle;
    /** Indica o seletor interno (c�rculo menor). */    
    public static final int SELETOR_INTERNO = 0;
    /** Indica o seletor externo (c�rculo maior). */    
    public static final int SELETOR_EXTERNO = 1;
    /** Indica que nenhum seletor foi escolhido. */    
    public static final int SELETOR_NENHUM = -1;
    
    /** Cria uma nova inst�ncia de SeletorDuploDeAngulos.
     * @param cor0 Cor do primeiro seletor.
     * @param cor1 Cor do segundo seletor.
     * @param anguloInicial0 �ngulo inicial do primeiro seletor.
     * @param anguloInicial1 �ngulo inicial do segundo seletor.
     */
    public SeletorDuploDeAngulos(Color cor0, Color cor1, ObservableAngle anguloInicial0, ObservableAngle anguloInicial1) {

        angulo = new ObservableAngle[2];
        angulo[SELETOR_INTERNO] = anguloInicial0;
        angulo[SELETOR_EXTERNO] = anguloInicial1;
        corDeFrenteAtivado[SELETOR_INTERNO] = cor0;
        corDeFrenteAtivado[SELETOR_EXTERNO] = cor1;
        addMouseMotionListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(largura, altura));
        setMinimumSize(new Dimension(largura, altura));
        setMaximumSize(new Dimension(largura, altura));
        mouseDentroDoControle = new ObservableBoolean(false);
    }

    /** Retorna o �ngulo do seletor especificado.
     * @param numeroSeletor Seletor especificado.
     * @return �ngulo do seletor.
     */    
    public double retornarAngulo(int numeroSeletor) {
        double resposta;
        if (numeroSeletorEValido(numeroSeletor)) {
            resposta = (angulo[numeroSeletor].getValue());
        } else {
            resposta = -1;
        }
        return resposta;
    }
    
    /** Desenha o objeto.
     * @param g Onde o objeto ser� desenhado.
     */    
    public void paint(Graphics g) {
        desenhar((Graphics2D)g);
    }
    
    /** Desenha o objeto.
     * @param g Onde o objeto ser� desenhado.
     */    
    public void desenhar(Graphics2D g) {
        //Rectangle bounds = getBounds();
        int xm = getWidth()/2;
        int ym = getHeight()/2;
        /*
        // limpa fundo
        if (ativado) {
            g.setColor(corDeFundoAtivado);
        } else {
            g.setColor(corDeFundoDesativado);
        }
         */
        g.setColor(corDeFundoAtivado);
        g.fillRect(0,0,getWidth(),getHeight());
        // Desenha os dois seletores
        Point2D.Double centro = new Point2D.Double(xm,ym);        
        Color cor;
        for (int i=0; i<2; i++) {
            
            if (ativado[i]) {
                //g.setColor(corDeFrenteAtivado[i]);
                cor = corDeFrenteAtivado[i];
            } else {
                //g.setColor(corDeFrenteDesativado[i]);
                cor = corDeFrenteDesativado[i];
            }
            g.setColor(cor);
            g.setStroke(new BasicStroke(4.0f));
            //g.drawOval(xm - raioCirculo[i], ym - raioCirculo[i], 2 * raioCirculo[i], 2 * raioCirculo[i]);
            g.drawArc(xm - raioCirculo[i], ym - raioCirculo[i], 2 * raioCirculo[i], 
            2 * raioCirculo[i], -((int)angulo[i].getValue() + 90), 340);            
            
            // Desenha as duas al�as de cada seletor;
            Point2D.Double p1, p2, p3;
            for (int i2 = 0; i2<2; i2++) {
                double theta = angulo[i].getValue()+i2*180;
                p1 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] , theta, centro);
                p2 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] + tamanhoAlca[i], theta, centro);
                p3 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] + tamanhoAlca[i] - 10, theta, centro);
                g.setColor(cor);
                g.setStroke(new BasicStroke(4.0f));
                g.drawLine((int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y);

                // desenha punho (parte preta)
                g.setStroke(new BasicStroke(6.0f));
                if (ativado[i]) {
                    g.setColor(corDePunhoAtivado);
                } else {
                    g.setColor(corDePunhoDesativado);
                }
                g.drawLine((int)p2.x,(int)p2.y,(int)p3.x,(int)p3.y);

            }
            
            g.setStroke(new BasicStroke());            
            
            // Desenha o tri�ngulo da seta.
//            p1 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] -6, angulo[i].getValue()+80 , centro);
//            p2 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] +6, angulo[i].getValue()+80 , centro);            
            p1 = PolarCartesiano.polar2Cartesiano(raioCirculo[i] , angulo[i].getValue()+100 , centro);
            Point2D.Double pAux = PolarCartesiano.polar2Cartesiano(raioCirculo[i] , angulo[i].getValue()+80 , centro);
            p2 = PolarCartesiano.polar2Cartesiano(8, angulo[i].getValue()+90 , pAux);
            p3 = PolarCartesiano.polar2Cartesiano(8, angulo[i].getValue()-90 , pAux);            
            
            int[] px = new int[3];
            int[] py = new int[3];
            px[0] = (int)p1.x;
            py[0] = (int)p1.y;
            px[1] = (int)p2.x;
            py[1] = (int)p2.y;
            px[2] = (int)p3.x;
            py[2] = (int)p3.y;

            g.setColor(cor);
            //g.drawPolygon(px, py, 3);
            g.fillPolygon(px, py, 3);
            
        }
    }
    
    /** Informa se o n�mero do seletor especificado � v�lido, ou seja, se o seletor �
     * SELETOR_INTERNO ou SELETOR_EXTERNO.
     * @param numeroSeletor N�mero a ser verificado.
     * @return True se o n�mero � v�lido, false caso contr�rio.
     */    
    private boolean numeroSeletorEValido(int numeroSeletor) {
        return (numeroSeletor==SELETOR_INTERNO || numeroSeletor==SELETOR_EXTERNO);
    }
    
    /** Ajusta o �ngulo de um seletor especificado.
     * @param numeroSeletor Seletor a ser modificado.
     * @param novoAngulo Novo �ngulo a ser atribuido.
     */    
    public void ajustarAngulo(int numeroSeletor, double novoAngulo) {
        if (numeroSeletorEValido(numeroSeletor)) {
            angulo[numeroSeletor].setValue(novoAngulo);
        }
        repaint();
    }
    

    /** Dado um ponto P=(x,y), calcula o �ngulo de uma al�a que passe por ele e atribui
     * esse valor ao seletor escolhido, atualizando-o.
     * @param x Abscissa do ponto P.
     * @param y Ordenada do ponto P.
     */    
    public void moveSeletorEscolhidoPara(int x, int y) {
        int numeroSeletor=seletorEscolhido;
        int xm = getWidth()/2;
        int ym = getHeight()/2;
        // calcular novo angulo
        //double r = PolarCartesiano.distanciaEntrePontos(x,y,xm,ym);
        //if (raioCirculo[numeroSeletor] < r && r < raioCirculo[numeroSeletor] + tamanhoAlca[numeroSeletor] + 4 + 20) {
        double a = PolarCartesiano.retornarAnguloPontos(x,y,xm,ym);
        if (PolarCartesiano.menorDistanciaEntreAngulos(a,angulo[numeroSeletor].getValue()) < 90) {
            ajustarAngulo(numeroSeletor, a);
        } else {
            ajustarAngulo(numeroSeletor, a + 180);            
        }
        //}
    }
    
    /** Habilita o seletor especificado.
     * @param numeroSeletor Seletor especificado.
     */    
    public void habilitar(int numeroSeletor) {
        if (numeroSeletorEValido(numeroSeletor)) {
            ativado[numeroSeletor] = true;
        }
        repaint();
    }
    
    /** Desabilita o seletor especificado.
     * @param numeroSeletor Seletor especificado.
     */    
    public void desabilitar(int numeroSeletor) {
        if (numeroSeletorEValido(numeroSeletor)) {
            ativado[numeroSeletor] = false;
        }
        repaint();
    }
    
    
    // M�todos de MouseListener
    /** Se usu�rio clicou no objeto, atualiza propriedade
     * <CODE>mouseDentroDoControle</CODE> como True.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        mouseDentroDoControle.setValue(true);  
    }
    
    /** Se usu�rio entrou no objeto, atualiza propriedade
     * <CODE>mouseDentroDoControle</CODE> como True.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
        mouseDentroDoControle.setValue(true);  
    }
    
    /** Se usu�rio saiu do objeto, atualiza propriedade
     * <CODE>mouseDentroDoContrOle</CODE> como False.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        mouseDentroDoControle.setValue(false);  
    }
    
    /** Se bot�o principal do mouse for pressionado sobre o objeto, move o 
     * seletor mais pr�ximo e ativo para a posi��o do mouse.
     * @param mouseEvent Evento do mouse.
     */    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        if (ativado[SELETOR_INTERNO] || ativado[SELETOR_EXTERNO]) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            
            // descobrir qual � o seletor escolhido.
            if (ativado[SELETOR_INTERNO] && ativado[SELETOR_EXTERNO]) {
                int xm = getWidth()/2;
                int ym = getHeight()/2;
                double a = PolarCartesiano.retornarAnguloPontos(x,y,xm,ym);
                // a � o angulo do mouse. 
                
                // MUDAR A L�GICA ABAIXO.
                
                
                // Qual o seletor com angulo mais pr�ximo de a?
                /*
                double d0 = Math.abs(angulo[SELETOR_INTERNO].getValue()-a);
                double d1 = Math.abs(angulo[SELETOR_EXTERNO].getValue()-a);
                if (d0>180) {
                    d0 = 360 - d0;
                }
                if (d1>180) {
                    d1 = 360 - d1;
                }
                 */
                // angulos das al�as
                double[] distancias = new double[4];
                distancias[0] = PolarCartesiano.menorDistanciaEntreAngulos(angulo[SELETOR_INTERNO].getValue(),a);
                distancias[2] = PolarCartesiano.menorDistanciaEntreAngulos(angulo[SELETOR_EXTERNO].getValue(),a);
                
                if (Math.abs(distancias[0]-distancias[2])<1) {
                    // Empate. prevalece o mais superior.
                    seletorEscolhido = 1;
                    //System.out.println("Empate");
                } else {
                    distancias[1] = PolarCartesiano.menorDistanciaEntreAngulos(angulo[SELETOR_INTERNO].getValue()+180,a);                    
                    distancias[3] = PolarCartesiano.menorDistanciaEntreAngulos(angulo[SELETOR_EXTERNO].getValue()+180,a);
                    // procurando al�a mais pr�xima (menor distancia)
                    int indiceAlca=0;
                    for (int i=1; i<=3; i++) {
                        if (distancias[indiceAlca]>distancias[i]) {
                            indiceAlca = i;
                        }
                    }
                    seletorEscolhido = (int)(indiceAlca/2);
                    //seletorEscolhido = (d0<d1 ? 0 : 1);
                    
                }
            } else {
                // apenas um dos dois est� ativado
                seletorEscolhido = (ativado[SELETOR_INTERNO] ? 0 : 1);
            }
            
            int botao = mouseEvent.getModifiers();
            if (botao == mouseEvent.BUTTON1_MASK) {
                moveSeletorEscolhidoPara(x,y);
            }
        }
        mouseDentroDoControle.setValue(true);
    }
    
    
    /** Quando o bot�o do mouse for solto, marca que nenhum seletor est�
     * escolhido e escolhe o tipo de cursor apropriado � sua posi��o atual.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        if (ativado[SELETOR_INTERNO] || ativado[SELETOR_EXTERNO]) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
        }
        seletorEscolhido = SELETOR_NENHUM;
    }
    
    // M�todos de MouseMotionListener
    
    /** Quando o mouse for arrastado, move junto com ele o seletor escolhido.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
        if (ativado[SELETOR_INTERNO] || ativado[SELETOR_EXTERNO]) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (x<0 || y<0 || x>getWidth()- 1 || y>getHeight()-1) {
                mouseDentroDoControle.setValue(false);                          
            } else {
                ajustarCursor(x,y);
                int botao = mouseEvent.getModifiers();
                if (botao == mouseEvent.BUTTON1_MASK) {
                    moveSeletorEscolhidoPara(x,y);
                }
               mouseDentroDoControle.setValue(true);          
            }
        }
    }
    
    /** Quando o mouse for movido sobre o controle (sem arrastar), apenas
     * escolhe o tipo de cursor apropriado � sua posi��o atual e ajusta o tipo
     * de cursor apropriado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        if (ativado[SELETOR_INTERNO] || ativado[SELETOR_EXTERNO]) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
        }
        mouseDentroDoControle.setValue(true);          
    }
    
    
     /** Ajusta o cursor para o tipo mais apropriado (m�o ou seta) de acordo com
     * a posi��o (x,y) em que se encontra.
     * @param x Abscissa atual do mouse.
     * @param y Ordenada atual do mouse.
     */
    public void ajustarCursor(int x, int y) {
        int xm = getWidth()/2;
        int ym = getHeight()/2;
        double r = PolarCartesiano.distanciaEntrePontos(x,y,xm,ym);
        
        if ((raioCirculo[SELETOR_INTERNO] < r && r < raioCirculo[SELETOR_INTERNO] + tamanhoAlca[SELETOR_INTERNO] + 4) ||
        (raioCirculo[SELETOR_EXTERNO] < r && r < raioCirculo[SELETOR_EXTERNO] + tamanhoAlca[SELETOR_EXTERNO] + 4)) {
            setCursor(cursorMao);
        } else {
            setCursor(cursorSeta);
        }
    }
    
}
