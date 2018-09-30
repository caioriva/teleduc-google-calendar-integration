/*
 * Barra.java
 * Vers�o: 2004-11-23
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo.polar;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import grafo.*;

/** Classe que cria um objeto similar a uma barra com caracteristicas de slider
 * vertical, ou seja, uma barra cuja altura pode ser ajustada pelo usu�rio.
 */
public class Barra extends JPanel
implements MouseListener, MouseMotionListener, ComponentListener {
    /** Indica a �rea exterior ao ret�ngulo dentro do qual a barra � desenhada.
     */
    public final static int AREA_EXTERIOR = 0;

    /** Indica a �rea interior ao ret�ngulo que delimita o desenho da barra, exceto o
     * ret�ngulo preto da "al�a".
     */
    public final static int AREA_INTERIOR = 1;

    /** Indica a �rea da "al�a" (ret�ngulo preto) da barra.
     */
    public final static int AREA_DE_ALCA  = 2;
    
    /** Largura do ret�ngulo que delimita a �rea do objeto.
     */
    public static int larguraTotal = 16;

    /** Largura da barra em si (ret�ngulo colorido no meio do objeto).
     */
    public static int largura = 6;
    
    /** Abscissa do mouse.
     */
    public int x;
    
    /** Ordenada do mouse.
     */
    public int  y;

    /** Altura total do objeto.
     */
    public int altura;

    /** Posi��o escolhida. Pode ser observada por outros objetos.
     */
    public ObservableInt posicao;
    /** Cor da barra.
     */
    public Color cor;

    /** Cor que a al�a possui quanto ativada.
     */    
    public static Color corDeAlcaAtivada = Color.black;
    
    /** Cor que a al�a possui quando desativada.
     */    
    public static Color corDeAlcaDesativada = Color.gray;
    
    /** Cor que o corpo (ret�ngulo colorido da barra) tem quanto desativado.
     */    
    public static Color corDeCorpoDesativado = Color.lightGray;
    
    /** Cor que o fundo do objeto tem quando ativado.
     */    
    public static Color corDeFundoAtivado = Color.white;
    
    /** Cor que o fundo do objeto tem quando desativado.
     */    
    public static Color corDeFundoDesativado = new Color(240,240,240);
    
    /** Indica se o mouse est� dentro do objeto ou n�o, e qual a posi��o do mouse.
     */    
    public ObservableBooleanXY mouseDentroDoControle;
    
    /** Informa se a barra est� ativada ou n�o.
     */    
    public boolean ativado = false;  //  OBS: Verificar se nao deve ser transformada em private. 
    
    /** Construtor da classe.
     * @param altura Altura do objeto
     * @param posicao Posi��o inicial da barra.
     * @param cor Cor do corpo da barra.
     */
    public Barra(int altura, ObservableInt posicao, Color cor) {
        // Valores iniciais
        x = 0;
        y = 0;
        this.altura = altura;
        this.posicao = posicao;
        this.cor = cor;
        this.ativado = false;
        mouseDentroDoControle = new ObservableBooleanXY(false,-1,-1);
        
        // para testes;
        this.setDoubleBuffered(true);
        
        // Listeners
        addMouseMotionListener(this);
        addMouseListener(this);
        addComponentListener(this);
        //setLayout(new java.awt.FlowLayout());
    }
    
    /** Desenha a barra.
     * @param g Onde a barra ser� desenhada.
     */
    public void desenhar(Graphics2D g) {
        Color cor1,cor2,cor3;
        Shape shape = g.getClip();
        Rectangle2D rect = new Rectangle2D.Float();
        rect.setRect(x,y-1,larguraTotal +1 , altura+2+1);
        g.setClip(rect);
        
        if (ativado) {
            cor1 = corDeFundoAtivado;
            cor2 = cor;
            cor3 = corDeAlcaAtivada;
        } else {
            cor1 = corDeFundoDesativado;
            cor2 = corDeCorpoDesativado;
            cor3 = corDeAlcaDesativada;
        }
        g.setColor(cor1);
        g.fillRect(x,y-1,larguraTotal, altura+2+1);
        g.setColor(cor2);
        g.fillRect(x+larguraTotal/2 - largura/2, (y + altura + 1) - posicao.getValue(), largura, posicao.getValue());
        g.setColor(cor3);
        g.fillRect(x+larguraTotal/2 - largura/2 - 3, (y + altura) - posicao.getValue() - 1, largura + 6, 3);
        g.setClip(shape);
    }
    
    /** Informa em que �rea o ponto se encontra com rela��o ao objeto e suas partes.
     * @param x Abscissa do ponto.
     * @param y Ordenada do ponto.
     * @return Retorna os valores AREA_DE_ALCA, AREA_INTERIOR ou AREA_EXTERIOR.
     */
    public int areaEm(int x, int y) {
        int resp;
        if  (x >= this.x && y>= this.y && (x-this.x) < larguraTotal && (y-this.y) < altura) {
            if ((altura- (y - this.y))< (posicao.getValue() + 2) && (altura- (y - this.y)) > (posicao.getValue() - 2)) {
                resp = AREA_DE_ALCA;
            } else {
                resp = AREA_INTERIOR;
            }
        } else {
            resp = AREA_EXTERIOR;
        }
        return resp;
    }
    
    /** Ajustar posi��o nova.
     * @param y Ordenada do ponto em que a al�a deve ser desenhada.
     * @return Nova posi��o da barra.
     */
    public int ajustarPosicao(int y) {
        if (y>= this.y && (y-this.y) < altura) {
            posicao.setValue(altura - (y - this.y));
        }
        return posicao.getValue();
    }
    
    /** Desenha a barra.
     * @param g Onde a barra ser� desenhada.
     */    
    public void paint(Graphics g) {
        if (posicao.getValue()>altura) { // Se posicao ultrapassar o tamanho
            posicao.setValue(altura);    // entao corrige altura da barra
        }
        desenhar((Graphics2D) g);
    }
    
    // M�todos de MouseListener e MouseMotionListener
    
    /** Cuida do clique do mouse no objeto, quando o objeto est� ativo.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
            mouseDentroDoControle.setValue(true,x,y);
        }
    }
    
    /** Cuida do arraste do mouse, quando o objeto est� ativado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (areaEm(x,y) != AREA_EXTERIOR) {
                int novaPosicao = ajustarPosicao(y);
                ajustarCursor(x,y);
                
                repaint();
                
                mouseDentroDoControle.setValue(true,x,y);
            } else {
                mouseDentroDoControle.setValue(false,x,y);
            }
        }
    }
    
    /** Cuida da entrada do mouse no objeto, quando o objeto est� ativado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
            mouseDentroDoControle.setValue(true,x,y);
        }
    }
    
    /** Cuida da sa�da do mouse do objeto, quando o objeto est� ativado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
            mouseDentroDoControle.setValue(false,x,y);
        }
    }
    
    /** Cuida da movimenta��o do mouse dentro do objeto, quando o objeto esta ativado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            ajustarCursor(x,y);
            mouseDentroDoControle.setValue(true,x,y);
        }
    }
    
    /** Cuida do pressionamento de um bot�o do mouse sobre o objeto, quando o objeto
     * est� ativado.
     * @param mouseEvent Evento do mouse.
     */    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        if (ativado) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (areaEm(x,y) != AREA_EXTERIOR) {
                int novaPosicao = ajustarPosicao(y);
                ajustarCursor(x,y);
                repaint();
                mouseDentroDoControle.setValue(true,x,y);
            }
        }
    }
    
    /** (N�o est� implementado. Necess�rio para implementar MouseListener)
     * @param mouseEvent Evento do mouse.
     */    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    
    /** Modifica o cursor de acordo com a �rea em que ele se encontra dentro do objeto.
     * @param xm Abscissa do mouse.
     * @param ym Ordenada do mouse.
     */    
    public void ajustarCursor(int xm, int ym) {
        int resp = areaEm(xm,ym);
        switch (resp) {
            case AREA_EXTERIOR:
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
            case AREA_INTERIOR:
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                break;
            case AREA_DE_ALCA:
                setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                break;
            default:
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /** (N�o est� implementado. Necess�rio para implementar ComponentListener.)
     * @param componentEvent Evento do componente.
     */    
    public void componentHidden(java.awt.event.ComponentEvent componentEvent) {
    }

    /** (N�o est� implementado. Necess�rio para implementar ComponentListener.)
     * @param componentEvent Evento do componente.
     */    
    public void componentMoved(java.awt.event.ComponentEvent componentEvent) {
    }

    /** (N�o est� implementado. Necess�rio para implementar ComponentListener.)
     * @param componentEvent Evento do componente.
     */    
    public void componentShown(java.awt.event.ComponentEvent componentEvent) {
    }    
  
    /** Redimensiona o objeto.
     * @param componentEvent Evento do componente.
     */    
    public void componentResized(java.awt.event.ComponentEvent componentEvent) {
        altura = getHeight() - 2;
    }
    
    /** Ativa (habilita) o objeto.
     */    
    public void habilitar() {
        ativado = true;
        repaint();
    }
    
    /** Desativa (desabilita) o objeto.
     */    
    public void desabilitar() {
        ativado = false;
        repaint();
    }
    
}

