/*
 * GrafoDisplay.java
 * Vers�o: 2004-08-24
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;
import java.awt.*;
import java.awt.event.*;

import java.util.Collection;   // Foram discriminados Collection e
import java.util.Iterator;     // Iterator porque havia conflito se us�ssemos
import javax.swing.*;          // java.util.* , j� que existem java.util.Timer e
import java.io.*;              // javax.swing.Timer.
import javax.imageio.ImageIO;
import java.awt.image.*;

/** Panel em que o Grafo ser� mostrado. Cuida de todos os cliques efetuados
 * nessa estrutura visual.
 */

public abstract class GrafoDisplay extends JPanel
implements MouseMotionListener, MouseListener, ActionListener, ComponentListener {
    
    /** Textos de menus e di�logos.
     */
    public String[] textos = {
        "Mostrar informa��o",           // 0
        "Esconder n�",
        "Esconder n�s marcados",
        "Esconder n�s n�o marcados",
        "Mostrar n�s escondidos",
        "Dados",                        // 5
        "Tipo",
        "Peso", // Peso do n�
        "N�",
        "Aresta",
        "Elementos interligados",       // 10
        "Informa��es sobre",
        "Peso" // Peso da aresta
    };
    
    /** Menu popup usado quando se clica em cima de um n�.
     */
    private JPopupMenu nopPopup;
    
    /** Elemento de menu popup.
     */
    private JMenuItem nopEsconder;
    
    /** Elemento de menu popup.
     */
    private JMenuItem nopMostrarInfo;
    
    /** Elemento de menu popup.
     */
    private JMenuItem nopEsconderEscolhidos;
    
    /** Elemento de menu popup.
     */
    private JMenuItem nopEsconderNaoEscolhidos;
    
    /** Elemento de menu popup.
     */
    private JMenuItem nopMostrarEscondidos;
    
    /** Menu popup usado quando se clica no fundo do grafo.
     */
    private JPopupMenu aePopup;
    
    /** Elemento de menu popup.
     */
    private JMenuItem aeEsconderEscolhidos;
    
    /** Elemento de menu popup.
     */
    private JMenuItem aeEsconderNaoEscolhidos;
    
    /** Elemento de menu popup.
     */
    private JMenuItem aeMostrarEscondidos;
    
    /** Janela de di�logo contendo informa��es sobre um n� ou aresta.
     */
    private JDialog dialogInfo;
    
    /** Bot�o OK no menu de di�logo citado anteriormente.
     */
    private JButton okInfo;
    
    /** N� em foco, ou seja, n� sobre o qual est� o mouse.
     */
    protected No noFoco; // DEVERIA SER PRIVATE!!!!
    
    /** Abscissa do mouse.
     */
    private int mx;
    
    /** Ordenada do mouse.
     */
    private int my;
    
    /** N� que foi clicado com o bot�o direito do mouse para mostrar menu de contexto.
     */
    private No noEmPopup;
    
    /** N� que est� sendo movido pelo usu�rio ou que est� sendo reposicionado pelo
     * pr�prio sistema.
     */
    //public No noMovendo;
    
    private boolean animacaoHabilitada = true;
    
    /** O grafo em si.
     */
    private Grafo grafo;
    
    /** Cria uma nova inst�ncia de GrafoDisplay.
     * @param grafo Grafo a ser desenhado no display. */
    public GrafoDisplay(Grafo grafo) {
        //usarGrafo(grafo);
        this.grafo = grafo;
        
        noFoco = null;
        //noMovendo = null;
        
        // Menu popup
        nopPopup = new JPopupMenu();
        
        /* Frase 0 - Mostrar informa��o */
        nopMostrarInfo = new JMenuItem(textos[0]);
        nopMostrarInfo.addActionListener(this);
        nopPopup.add(nopMostrarInfo);
        
        /* Frase 1 - Esconder n� */
        nopEsconder = new JMenuItem(textos[1]);
        nopEsconder.addActionListener(this);
        nopPopup.add(nopEsconder);
        
        /* Frase 2 - Esconder n�s marcados */
        nopEsconderEscolhidos = new JMenuItem(textos[2]);
        nopEsconderEscolhidos.addActionListener(this);
        nopPopup.add(nopEsconderEscolhidos);
        
        /* Frase 3 - Esconder n�s n�o marcados */
        nopEsconderNaoEscolhidos = new JMenuItem(textos[3]);
        nopEsconderNaoEscolhidos.addActionListener(this);
        nopPopup.add(nopEsconderNaoEscolhidos);
        
        /* Frase 4 - Mostrar n�s escondidos */
        nopMostrarEscondidos = new JMenuItem(textos[4]);
        nopMostrarEscondidos.addActionListener(this);
        nopPopup.add(nopMostrarEscondidos);
        
        // Menu popup
        aePopup       = new JPopupMenu();
        
        /* Frase 2 - Esconder n�s marcados */
        aeEsconderEscolhidos = new JMenuItem(textos[2]);
        aeEsconderEscolhidos.addActionListener(this);
        aePopup.add(aeEsconderEscolhidos);
        
        /* Frase 3 - Esconder n�s n�o marcados */
        aeEsconderNaoEscolhidos = new JMenuItem(textos[3]);
        aeEsconderNaoEscolhidos.addActionListener(this);
        aePopup.add(aeEsconderNaoEscolhidos);
        
        /* Frase 4 - Mostrar n�s escondidos */
        aeMostrarEscondidos = new JMenuItem(textos[4]);
        aeMostrarEscondidos.addActionListener(this);
        aePopup.add(aeMostrarEscondidos);
        
        // Listeners
        addComponentListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        //setPreferredSize(new Dimension(300,300));
        
    }
    
    /** Desenha o grafo (m�todo padr�o do Java).
     * @param g Onde o grafo ser� desenhado.
     */
    public void paint(Graphics g) {
        desenhar((Graphics2D)g);
    }
    
    /** Desenha o grafo.
     * @param g Onde o grafo ser� desenhado.
     */
    public void desenhar(Graphics2D g) {
        g.setColor(grafo.corDeFundo);
        //g.fillRect((int) (grafo.centro.x - grafo.largura / 2),
        //(int) (grafo.centro.y - grafo.altura / 2),
        //(int) grafo.largura, (int) grafo.altura);
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(Color.black);
        //g.drawRect((int) (grafo.centro.x - grafo.largura / 2),
        //(int) (grafo.centro.y - grafo.altura / 2),
        //(int) grafo.largura, (int) grafo.altura);
        g.drawRect(0,0, getWidth(), getHeight());
        
        desenhaElementosEmSegundoPlano((Graphics2D)g);
        
        grafo.desenhar((Graphics2D)g);
        // borda preta
        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth() - 1 , getHeight() - 1);
        ajustarCursor();
        
    }
    
    /** Desenha elementos que ficar�o em segundo plano.
     * @param g Objeto Graphics em que o desenho ser� feito.
     */
    public abstract void desenhaElementosEmSegundoPlano(Graphics2D g);
    
    /** Retorna um valor informando qual tipo de �rea do grafo est� sob o mouse
     * @return  Retorna AREA_DE_NO - se o ponto est� sobre um n�;
     *          AREA_INTERIOR - se est� dentro do ret�ngulo mas fora de um n�;
     *          AREA_EXTERIOR - se est� fora do ret�ngulo.
     */
    public int areaSobMouse() {
        return(grafo.areaEm(mx, my));
    }
    
    /** Informa qual o n� que est� sob o mouse
     * @return  Retorna o n�, ou null se o n� n�o existir.
     */
    public No noSobMouse() {
        return(grafo.noEm(mx,my));
    }
    
    /** Atualiza vari�veis internas que guardam as coordenadas do mouse.
     * Garante que a coordenada armazenada estar� dentro dos limites atuais
     * da dimens�o do display.
     * @param e Evento do mouse.
     */
    public void atualizarCoordenadasDoMouseInternas(MouseEvent e) {
        mx = e.getX(); my = e.getY();
        Dimension d = getSize();
        if (mx>d.width) {
            mx=d.width;
        }
        if (my>d.height) {
            my=d.height;
        }
        if (mx<0) {
            mx=0;
        }
        if (my<0) {
            my=0;
        }
        
    }
    
    /** Ajusta tipo de cursor, de acordo com a posi��o do mouse.
     */
    protected void ajustarCursor() { // NAO DEVERIA SER PRIVATE?
        int gc = areaSobMouse();
        switch (gc) {
            case Grafo.AREA_DE_NO:
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                break;
            case Grafo.AREA_EXTERIOR:
            case Grafo.AREA_INTERIOR:
            default:
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
        }
    }
    
    /** Trata dos casos em que o mouse est� arrastando algo que come�ou a
     * ser arrastado dentro do objeto (tipicamente � o caso de um n�.)
     * @param e Evento do mouse.
     */
    public void mouseDragged(MouseEvent e) {
        atualizarCoordenadasDoMouseInternas(e);
        if (grafo.retornarNoMovendo() != null) {
            grafo.moverNoMovendo(mx,my);
            repaint();
        }
    }
    
    /** Trata dos casos em que o mouse est� se movendo sobre o objeto.
     * Estabelece qual � o n� sobre o qual o mouse est� (noFoco).
     * @param e Evento do mouse.
     */
    public void mouseMoved(MouseEvent e) {
        atualizarCoordenadasDoMouseInternas(e);
        No no = noSobMouse();
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
    }
    
    /**
     * Marca o n� que estiver debaixo da �ltima coordenada do mouse verificada.
     * @return Retorna True se a opera��o teve sucesso, False caso contr�rio.
     */
    public boolean marcarNoSobMouse() {
        No no = noSobMouse();
        boolean ok = (no!=null);
        if (ok) {
            if (noFoco != null) {
                noFoco.desfazerEscolher();
                noFoco = null;
            }
            if (no.eMarcado()) {
                no.desmarcar();
            } else {
                no.marcar();
            }
        }
        return(ok);
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
        atualizarCoordenadasDoMouseInternas(e);
        int btn = e.getModifiers();
        
        // Bot�o principal do mouse foi pressionado:
        // Se cursor est� dentro da �rea em que o grafo est� desenhado,
        // verifica se est� sobre algum n�. Se estiver, o usu�rio deseja
        // mover esse n�.
        // Se curor n�o est� sobre nenhum n�, verificar se � o caso de
        // mostrar um menu de contexto.
        
        if (btn  == InputEvent.BUTTON1_MASK) {
            if (grafo.areaEm(mx, my) != Grafo.AREA_EXTERIOR) {
                No no = noSobMouse();
                if (no != null) {
                    if (noFoco != null) {
                        noFoco.desfazerEscolher();
                        noFoco = null;
                    }
                    grafo.registrarNoMovendo(no);
                    repaint();
                }
            }
            
        } else {
            // Verifica se deve mostrar menu de contexto
            testarPopup(e);
        }
    }
    
    /** Funcao de MouseListener.
     * @param e Evento do mouse.
     */
    public void mouseReleased(MouseEvent e) {
        int btn = e.getModifiers();
        
        // Se bot�o principal foi solto
        
        if (btn  == InputEvent.BUTTON1_MASK) {
            
            //if (noMovendo != null) {
            if (grafo.retornarNoMovendo()!=null) {
                grafo.desfazerRegistrarNoMovendo();
                mouseMoved(e);
                repaint();
            }
            
        }
        testarPopup(e);
        
    }
    
    /** Testar se se encontrou um click para mostrar menu de contexto.
     * Se mouse est� em um n�, mostra menu de contexto de n�.
     * Se mouse est� fora de um n�, mostra menu de contexto geral.
     * @param e Evento do mouse.
     */
    public void testarPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            
            // Bot�o secund�rio do mouse foi pressionado:
            // Verifica se usu�rio clicou em �rea do grafo e que n�o seja
            // �rea de n�. Se sim, verificar se usu�rio clicou em aresta.
            
            int contem = areaSobMouse();
            switch (contem) {
                
                case Grafo.AREA_DE_NO:
                    
                    No no = noSobMouse();
                    if (no != null) {
                        // mostra menu de contexto do n� clicado.
                        noEmPopup = no;
                        nopEsconderEscolhidos.setEnabled(grafo.retornarEscolhidos().size()>0);
                        nopEsconderNaoEscolhidos.setEnabled(grafo.retornarNaoEscolhidos().size()>0);
                        nopMostrarEscondidos.setEnabled(grafo.retornarEscondidos().size()>0);
                        nopPopup.show(e.getComponent(),e.getX(), e.getY());
                    }
                    break;
                    
                case Grafo.AREA_INTERIOR:
                    
                    Collection arestasSobMouse;
                    arestasSobMouse = grafo.retornarConjuntoArestasEm(mx,my);
                    Iterator i = arestasSobMouse.iterator();
                    Aresta aresta;
                    
                    // se existe aresta, mostrar informacao sobre ela.
                    // senao, mostra menu de contexto geral.
                    if (i.hasNext()) {
                        aresta = (Aresta)(i.next());
                        mostrarDialogoInformacaoAresta(aresta);
                    } else {
                        aeEsconderEscolhidos.setEnabled(grafo.retornarEscolhidos().size()>0);
                        aeEsconderNaoEscolhidos.setEnabled(grafo.retornarNaoEscolhidos().size()>0);
                        aeMostrarEscondidos.setEnabled(grafo.retornarEscondidos().size()>0);
                        aePopup.show(e.getComponent(),e.getX(), e.getY());
                        //repaint();
                    }
                    break;
                    
                default:
                    // nao faz nada.
                    
            }
        }
    }
    
    
    
    /** Trata diversas a��es referentes a diversos eventos, como
     * @param e Evento do mouse.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == nopEsconder) { // popup menu item
            if (noFoco != null) {
                noFoco.desfazerEscolher();
            }
            noFoco = null;
            grafo.esconderNo(noEmPopup);
            repaint();
        } else
            
            if (source == nopMostrarInfo) {  // popup menu item
                mostrarDialogoInformacaoNo(noEmPopup);
            } else
                
                if (source == nopEsconderEscolhidos || source == aeEsconderEscolhidos) {  // popup menu item
                    if (noFoco != null) {
                        noFoco.desfazerEscolher();
                    }
                    noFoco = null;
                    //            Iterator li = grafo.retornarEscolhidos().iterator();
                    //            while(li.hasNext())
                    //                grafo.esconderNo((No)li.next());
                    grafo.esconderNos(grafo.retornarEscolhidos());
                    repaint();
                } else
                    
                    if (source == nopEsconderNaoEscolhidos || source == aeEsconderNaoEscolhidos) {  // popup menu item
                        if (noFoco != null) {
                            noFoco.desfazerEscolher();
                        }
                        noFoco = null;
                        //Iterator li = grafo.retornarNaoEscolhidos().iterator();
                        //while(li.hasNext())
                        //    grafo.esconderNo((No) li.next());
                        grafo.esconderNos(grafo.retornarNaoEscolhidos());
                        
                        repaint();
                    } else
                        
                        if (source == nopMostrarEscondidos || source == aeMostrarEscondidos) {  // popup menu item
                            if (noFoco != null) {
                                noFoco.desfazerEscolher();
                            }
                            noFoco = null;
                            grafo.mostrarTodosOsNos();
                            repaint();
                        } else
                            
                            if (source == okInfo) {
                                dialogInfo.dispose(); // button de info dialogo
                                repaint();
                            }
    }
    
    /** Mostra janela de informa��es sobre um n� espec�fico.
     * @param no N� cujas informa��es devem ser mostradas.
     */
    private void mostrarDialogoInformacaoNo(No no) {
        if (no!=null) {
            dialogInfo = new JDialog();
            /* Organiza�ao de dialogInfo:
             * Norte:  JPanel panelCabecalho contendo
             *          Norte: Cabe�alho (JLabel linhaNomeNo)
             *          Sul: separador (Jpanel)
             * Centro: Conte�do (JPanel panelCentral), composto de:
             *          Norte:  panelInformacao (se existir)
             *          Centro: JLabel sobre grupo
             *          Sul:    JLabel sobre peso
             * Sul:    JPanel panelBotaoOK, contendo Bot�o OK.
             */
            int alturaDialogInfo=0;
            
            /* Frase 11 - Inform��es sobre */
            dialogInfo.setTitle(textos[11] + "...");
            dialogInfo.setModal(true);
            
            dialogInfo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel conteudo = new JPanel();
            conteudo.setLayout(new BorderLayout());
            dialogInfo.setContentPane(conteudo);
            
            JPanel panelCabecalho = new JPanel(new BorderLayout());
            /* Frase 11 - Informa��es sobre */
            JLabel linhaNomeNo = new JLabel(textos[11] +" "+ no.nome);
            panelCabecalho.add(linhaNomeNo, BorderLayout.NORTH);
            
            JPanel separador = new JPanel();
            separador.setSize(100, 20);
            panelCabecalho.add(separador, BorderLayout.SOUTH);
            
            conteudo.add(panelCabecalho, BorderLayout.NORTH);
            
            alturaDialogInfo += panelCabecalho.getHeight();
            
            JPanel panelCentral = new JPanel(new BorderLayout());
            if (no.info != null) {
                JPanel panelInformacao = new JPanel(new BorderLayout());
                
                /* Frase 5 - Dados */
                //JLabel labelInformacao=new JLabel(textos[5]+":");
                //labelInformacao.setFont(Font.getFont("Plain"));
                //panelInformacao.add(labelInformacao, BorderLayout.NORTH);
                
                JComponent infoComponente = no.info.retornarInformacao();
                panelInformacao.add(infoComponente, BorderLayout.CENTER);
                
                panelCentral.add(panelInformacao, BorderLayout.NORTH);
            }
            /* Frase 6 - Grupo: */
            if (no.grupo!=null) {
                JLabel labelGrupo = new JLabel(textos[6] +": "+ no.grupo);
                labelGrupo.setFont(Font.getFont("Plain"));
                panelCentral.add(labelGrupo, BorderLayout.CENTER);
            }
            
            if (grafo.nosPossuemPeso()) {
                /* Frase 7 - Peso: */
                JLabel labelPeso = new JLabel(textos[7] +": "+ no.peso);
                labelPeso.setFont(Font.getFont("Plain"));
                panelCentral.add(labelPeso, BorderLayout.SOUTH);
            }
            
            conteudo.add(panelCentral, BorderLayout.CENTER);
            
            alturaDialogInfo += panelCentral.getHeight();
            
            okInfo = new JButton("OK");
            okInfo.setMnemonic(KeyEvent.VK_O);
            okInfo.addActionListener(this);
            dialogInfo.getRootPane().setDefaultButton(okInfo);
            
            JPanel panelBotaoOK = new JPanel();
            panelBotaoOK.add(okInfo);
            
            alturaDialogInfo += panelBotaoOK.getHeight();
            
            conteudo.add(panelBotaoOK, BorderLayout.SOUTH);
            
            dialogInfo.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        dialogInfo.dispose();
                    }
                }
            } );
            
            Point delta;
            try {
                delta = getLocationOnScreen();
            } catch (IllegalComponentStateException e) {
                delta = new Point(0,0);
            }
            dialogInfo.setBounds(delta.x + mx, delta.y + my, 200, alturaDialogInfo);
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
    }
    
    /** Mostra janela de informa��es sobre uma aresta espec�fica.
     * @param aresta Aresta cujas informa��es devem ser mostradas.
     */
    private void mostrarDialogoInformacaoAresta(Aresta aresta) {
        
        dialogInfo = new JDialog();
        /* Organiza�ao de dialogInfo:
         * Norte:  Cabe�alho (JPanel panelCabecalho), composto de
         *          Norte: JLabel com frase "Aresta interligando os seguintes n�s:"
         *          Centro: JPanel panelNos contendo
         *              Norte: JLabel com no1;
         *              Sul: JLabel com no2;
         *          Sul: separador (JPanel)
         * Centro: Conte�do (JPanel panelCentral), composto de:
         *          Norte:  panelInformacao (se existir)
         *          Sul:    JLabel sobre peso
         * Sul:    JPanel panelBotaoOK, contendo Bot�o OK.
         */
        int alturaDialogInfo=0;
        
        /* Frase 11 - Informa��es sobre */
        dialogInfo.setTitle(textos[11] + "...");
        dialogInfo.setModal(true);
        
        dialogInfo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BorderLayout());
        dialogInfo.setContentPane(conteudo);
        
        
        JPanel panelCabecalho = new JPanel(new BorderLayout());
        /* Frase 10 - Elementos interligados */
        JLabel linhaNomeAresta = new JLabel(textos[10]+" :");
        panelCabecalho.add(linhaNomeAresta, BorderLayout.NORTH);
        JPanel panelNos = new JPanel(new BorderLayout());
        JLabel linhaNomeNo1 = new JLabel("- "+((No)aresta.no1).nome);
        panelNos.add(linhaNomeNo1, BorderLayout.NORTH);
        JLabel linhaNomeNo2 = new JLabel("- "+((No)aresta.no2).nome);
        panelNos.add(linhaNomeNo2, BorderLayout.SOUTH);
        panelCabecalho.add(panelNos, BorderLayout.CENTER);
        JPanel separador = new JPanel();
        separador.setSize(100, 20);
        panelCabecalho.add(separador, BorderLayout.SOUTH);
        conteudo.add(panelCabecalho, BorderLayout.NORTH);
        
        alturaDialogInfo += panelCabecalho.getHeight();
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        if (aresta.info != null) {
            JPanel panelInformacao = new JPanel(new BorderLayout());
            
            /* Frase 5 - Dados */
            // JLabel labelInformacao=new JLabel(textos[5] + ":");
            // labelInformacao.setFont(Font.getFont("Plain"));
            // panelInformacao.add(labelInformacao, BorderLayout.NORTH);
            
            JComponent infoComponente = aresta.info.retornarInformacao();
            panelInformacao.add(infoComponente, BorderLayout.CENTER);
            
            panelCentral.add(panelInformacao, BorderLayout.NORTH);
        }
        
        /* Frase 12 - Peso: */
        if (grafo.arestasPossuemPeso()) {
            JLabel labelPeso = new JLabel(textos[12] +": "+ aresta.peso);
            labelPeso.setFont(Font.getFont("Plain"));
            panelCentral.add(labelPeso, BorderLayout.SOUTH);
        }
        conteudo.add(panelCentral, BorderLayout.CENTER);
        
        alturaDialogInfo += panelCentral.getHeight();
        
        okInfo = new JButton("OK");
        okInfo.setMnemonic(KeyEvent.VK_O);
        okInfo.addActionListener(this);
        dialogInfo.getRootPane().setDefaultButton(okInfo);
        
        JPanel panelBotaoOK = new JPanel();
        panelBotaoOK.add(okInfo);
        
        alturaDialogInfo += panelBotaoOK.getHeight();
        
        conteudo.add(panelBotaoOK, BorderLayout.SOUTH);
        
        dialogInfo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dialogInfo.dispose();
                }
            }
        } );
        
        Point delta;
        try {
            delta = getLocationOnScreen();
        } catch (IllegalComponentStateException e) {
            delta = new Point(0,0);
        }
        dialogInfo.setBounds(delta.x + mx, delta.y + my, 200, alturaDialogInfo);
        dialogInfo.pack();
        dialogInfo.setVisible(true);
        
    }
    
    /** Modifica os textos de menus e di�logos.
     * @param textosNovos Todos os textos que ir�o substituir os textos atuais.
     */
    public void modificarTextos(java.util.List textosNovos) {
        if (textosNovos.size() == textos.length) {
            textos = (String[])textosNovos.toArray(textos);
            nopMostrarInfo.setText(textos[0]);
            nopEsconder.setText(textos[1]);
            nopEsconderEscolhidos.setText(textos[2]);
            nopEsconderNaoEscolhidos.setText(textos[3]);
            nopMostrarEscondidos.setText(textos[4]);
            aeEsconderEscolhidos.setText(textos[2]);
            aeEsconderNaoEscolhidos.setText(textos[3]);
            aeMostrarEscondidos.setText(textos[4]);
        } else {
            System.out.println("Erro. N�o foi poss�vel modificar os textos, porque a lista contendo os novos textos � de tamanho diferente da lista original de textos.");
        }
    }
    
    /** Modifica o nome que descreve o peso dos n�s. Inicialmente esse nome � "Peso",
     * mas pode haver interesse em modific�-lo para especificar que esse peso se refere
     * a uma determinada medida (por exemplo, n�mero de mensagens recebidas).
     * Esse nome � independente do nome do peso das arestas.
     * @param nome Novo nome a ser utilizado.
     */
    public void renomearPesoDosNosPara(String nome) {
        textos[7] = nome;
    }
    
    /** Modifica o nome que descreve o peso das arestas. Inicialmente esse nome � "Peso",
     * mas pode haver interesse em modific�-lo para especificar que esse peso se refere
     * a uma determinada medida (por exemplo, n�mero de mensagens representadas pela
     * aresta).
     * Esse nome � independente do nome do peso dos n�s.
     * @param nome Novo nome a ser utilizado.
     */
    public void renomearPesoDasArestasPara(String nome) {
        textos[12] = nome;
    }
    
    /** Salva uma imagem do display em um arquivo especificado.
     * @param arquivo Arquivo no qual a imagem ser� armazenada.
     * @param tipoArquivo Tipo do arquivo a ser gerado. Ao menos os seguintes
     * formatos s�o suportados para tipoArquivo: jpeg e png.
     * @return True se a opera��o foi bem sucedida, false caso contr�rio.
     */
    public boolean salvarImagem(File arquivo, String tipoArquivo) {
        boolean res = false;
        if (arquivo!=null) {
            BufferedImage i = (BufferedImage)createImage(getWidth(), getHeight());
            paint(i.getGraphics());
            //        File arquivo = new File(nomeArquivo);
            try {
                ImageIO.write(i, tipoArquivo, arquivo);
                res = true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return res;
    }
    
    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */
    public void componentHidden(java.awt.event.ComponentEvent componentEvent) {
    }
    
    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */
    public void componentMoved(java.awt.event.ComponentEvent componentEvent) {
    }
    
    /** Atualiza indicadores de largura e altura quanto o display � redimensionado.
     * @param componentEvent Evento.
     */
    public void componentResized(java.awt.event.ComponentEvent componentEvent) {
        ajustarLimitesDeEspacoDoGrafo();
    }
    
    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */
    public void componentShown(java.awt.event.ComponentEvent componentEvent) {
        ajustarLimitesDeEspacoDoGrafo();
    }
    
    /** Informa para o grafo quais os novos limites de tela que seus elementos
     * podem utilizar.
     */
    public void ajustarLimitesDeEspacoDoGrafo() {
        Dimension d = new Dimension(getWidth(), getHeight());
        grafo.ajustarDimensaoDaTela(d);
    }

    /** Sorteia a posi��o dos n�s de acordo com a �ltima dimens�o registrada
     * pelo grafo.
     */
    public void disporNosAleatoriamente() {
        disporNosAleatoriamente(grafo.retornarDimensaoDaTela());
    }
    
    /** Sorteia a posi��o dos n�s de acordo com uma dimens�o especificada.
     * @param d Dimens�o dentro da qual as posi��es dos n�s devem ser sorteadas.
     */    
    synchronized public void disporNosAleatoriamente(Dimension d) {
        int margem = 30;
        if (grafo.retornarQuantidadeDeNos()>0) {
            Iterator li = grafo.nos.iterator();
            while(li.hasNext()) {
                No no = (No)li.next();
                no.x = (int)(Math.random()*(d.width-2*margem)+margem);
                no.y = (int)(Math.random()*(d.height-2*margem)+margem);
            }
        }
    }
    
    /** Permite que os n�s executem anima��es.
     */    
    public void habilitarAnimacao() {
        animacaoHabilitada = true;
    }

    /** N�o permite que os n�s executem anima��es.
     */
    public void desabilitarAnimacao() {
        animacaoHabilitada = false;
    }

    /** Informa se o display permite ou n�o que os n�s executem anima��es.
     * @return True se o display atualmente permite anima��es, false caso 
     * contr�rio.
     */
    public boolean animacaoEstaHabilitada() {
        return animacaoHabilitada;
    }
}
