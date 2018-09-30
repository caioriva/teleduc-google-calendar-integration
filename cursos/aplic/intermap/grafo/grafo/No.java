/*
 * No.java
 * Vers�o: 2004-07-22
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */
package grafo;

import java.util.*;
import java.io.*;
import java.awt.*;

/** Classe para armazenar um n� de um grafo.
 * Da forma como est� implementada, essa classe n�o sabe desenhar o n�.
 * Um elemento desta classe n�o conhece o grafo ao qual pertence, embora
 * saiba quais arestas incidem sobre ele.
 */
public abstract class No
{

    /** Indica a �rea externa ao n�.
     */
    public static int AREA_EXTERIOR = 0;

    /** Indica a �rea interna de um n�. 
     */
    public static int AREA_INTERIOR = 1;
    
    /** Conjunto de cores do n�. */
    public CorNo cor;

    /** Conjunto default de cores. */
    private CorNo corDefault = new CorNo();
    
    /** Indica se o n� deve ou n�o usar seu conjunto individual de cores. */
    private boolean usarCorIndividual;

    /** Indica se o n� est� marcado, ou seja, se ele foi selecionado pelo usu�rio por
     * meio de um clique.
     */
    private boolean marcado;
    
    /** Indica se o n� est� escondido, ou seja, se ele nao deve ser mostrado
     */
    private boolean escondido;

    /** Indica se o n� foi "escolhido", ou seja, se o usu�rio est� sobre o n�. */
    private boolean escolhido;

    /** Informa quantas arestas selecionadas atingem o n�. */
    private int atingido;

    /** Grupo ao qual o n� pertence. */
    public Grupo grupo;
    
    /** Abscissa atual do n�. */
    public double x = 0;
    
    /** Ordenada atual do n�. */
    public double y = 0;
    
    /** Largura do texto mostrado pelo n�. */
    private int larguraTexto;
    
    /** Altura do texto mostrado pelo n�. */    
    private int alturaTexto;

    /** Largura do n�. */
    private int larguraNo;
    
    /** Altura do n�. */    
    private int alturaNo;
    
    /** Margem horizontal entre o texto do n� e sua borda. */    
    private final int margemX = 4;
    
    /** Margem vertical entre o texto do n� e sua borda. */    
    private final int margemY = 2;
    
    /** Lista das arestas que incidem neste n� ou saem dele.
     */
    public ArrayList arestas;

    /** Peso do n�.
     */
    public int peso;
    
    /** Informa��o sobre este n�.
     */
    public Info info;

    /** Nome do n�.
     */
    public String nome;

    /** Determina se o nome do n� deve ou n�o ser abreviado quando o n� estiver 
     * marcado.
     */
    public boolean abreviarSeNaoMarcado;
    
    /** Informa se o n� est� sendo movido. */
    private boolean movendo;
    
    /** C�digo identificador do n� */
    private String id = null;
    
//    private static final Font fonte = new Font("Arial", Font.PLAIN, 14);
    
    /** Cria uma nova inst�ncia de No, criando um n� sem peso nem informa��o 
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     */
    public No(String nome) {
        this(nome, 0, null);
    }

    /** Cria uma nova inst�ncia de No, criando um n� com peso mas sem informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param peso Peso do n�.
     */
    public No(String nome, int peso) {
        this(nome, peso, null);
    }

    /** Cria uma nova inst�ncia de No, criando um n� sem peso mas com informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param info Informa��o associada ao n�.
     */
    public No(String nome, Info info) {
        this(nome, 0, info);
    }

    /** Cria uma nova inst�ncia de No, criando um n� com peso e com informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param peso Peso do n�.
     * @param info Informa��o associada ao n�.
     */
    public No(String nome, int peso, Info info) {
        arestas = new ArrayList();
        this.nome = nome;
        this.peso = peso;
        this.info = info;
        cor = new CorNo(); // Usa o padr�o de cores definido em CorNo.
        marcado = false;
        atingido = 0;
        escondido = false;
        removerCorIndividual();
        movendo = false;        
        abreviarSeNaoMarcado = true;
    }
    
    /** Cria uma nova inst�ncia de No, duplicando uma inst�ncia j� existente.
     * @param no N� a ser duplicado.
     */    
    public No(No no) {
        this(no.nome, no.peso, no.info);
        movendo = no.movendo;
        usarCorIndividual = no.usarCorIndividual;
        
    }

    /** Informa se o n� � o n� que est� em movimento ou n�o.
     * @return True se o n� � o no que est� em movimento (noMovendo), false 
     * caso contr�rio.
     */    
    public boolean eMovendo() {
        return movendo;
    }

    /** Registra n� que est� em movimento e marca as arestas das quais ele �
     * extremidade.
     */
    public void marcarComoMovendo() {
        movendo = true;
        if (!escolhido && !marcado) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.atingir();
            }
        }
    }

    /** Desfaz o registro de qual n� est� em movimento e tenta desmarcar as arestas das
     * quais ele � extremidade.  As arestas somente s�o desmarcadas se ambos os seus
     * n�s n�o est�o marcados.
     */
    public void desmarcarComoMovendo() {
        movendo = false;
        if (!escolhido && !marcado) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.desfazerAtingirSePossivel();
            }
        }
    }
    
    
    /** Retorna o conjunto de cores utilizado por este n�.
     * @return Conjunto de cores utilizado pelo n�.
     */    
    public CorNo retornarConjuntoDeCores() {
        CorNo corno = null;
        if (usarCorIndividual) {
            corno = cor;
        } else {
            if (grupo != null) {
                corno = grupo.cor;
            } else {
                corno = corDefault;
            }
        }
        return corno;
    }    
    
    /** Retorna cor do texto do n�.
     * @return Cor do texto do n�.
     */
    public Color retornarCorDeTexto() {
        CorNo corNo = retornarConjuntoDeCores();
        Color cor;
        if (corNo!=null) {
            if (escolhido || marcado || movendo || atingido > 0) {
                cor = corNo.corMarcado;
            } else {
                cor = corNo.corNormal;
            }
        } else {
            System.out.println("Erro em grafo.retornarCorDeTexto().");
            cor = Color.red; // problemas...
        }
        return cor;
    }
    
    /** Retorna cor de fundo do n�.
     * @return Cor de fundo do n�.
     */
    public Color retornarCorDeFundo() {
        CorNo corNo = retornarConjuntoDeCores();
        Color cor;
        if (corNo!=null) {
            if (escolhido || marcado || movendo || atingido > 0) {
                cor = corNo.corDeFundoMarcado;
            } else {
                cor = corNo.corDeFundoNormal;
            }
        } else {
            System.out.println("Erro em no.retornarCorDeFundo().");
            cor = Color.red; // problemas...
        }
        return cor;
    }

    /** Retorna o nome a ser mostrado no display.
     * Decide se o nome vai ser mostrado completamente ou abreviado.
     * @return Nome a ser mostrado.
     */
    public String nomeAMostrar() {
        String resposta;
        if (eMarcado() || nome.length()<7 || !abreviarSeNaoMarcado) {
            resposta = nome;
        } else {
            resposta = nome.substring(0,3) + "...";
        }
        return resposta;
    }
    
    /** Desenha o n�.
     * @param grafo Grafo ao qual o n� pertence. O n� precisa saber disso, uma vez que ele precisa
     * saber qual � o centro do grafo para, assim, poder se desenhar.
     * @param g Onde o n� ser� desenhado.
     */
    public void desenhar(Grafo grafo, Graphics g) {
        //Point2D.Double centro = new Point2D.Double((int)grafo.centro.x, (int)grafo.centro.y);
        String textoaux = nomeAMostrar();
        desenharNo(x, y, textoaux, g);
    }

    /** Atualiza os indicadores de altura e largura do texto de um n�.
     * @param g Onde o n� ser� desenhado.
     * @param texto Texto a ser desenhado no n�.
     */    
    public void atualizarDimensoesTexto(Graphics g, String texto) {
        FontMetrics fontMetrics = g.getFontMetrics();
        larguraTexto = (int) fontMetrics.stringWidth(texto);
        alturaTexto = (int) fontMetrics.getHeight();
    }
    
    /** M�todo interno para desenho do n�.
     * @param x1 Abscissa do meio do n�.
     * @param y1 Ordenada do meio do n�.
     * @param texto Texto do n�.
     * @param g Onde o n� ser� desenhado.
     */    
    public void desenharNo(double x1, double y1, String texto, Graphics g) {
        //larguraNo = 10; 
        larguraNo = larguraTexto + 2*margemX;
        //alturaNo = 10; 
        alturaNo = alturaTexto + 2*margemY;

        int x = (int)x1;
        int y = (int)y1;
        if (!escondido) {

//            Font font = g.getFont();
            atualizarDimensoesTexto(g, texto);
            g.setColor(retornarCorDeFundo());
            g.fillRect(x-larguraTexto/2-margemX,y-alturaTexto/2-margemY,larguraTexto+2*margemX,alturaTexto+2*margemY);
            if (marcado) {
                g.setColor(Color.black);
                g.drawRect(x-larguraTexto/2-margemX,y-alturaTexto/2-margemY,larguraTexto+2*margemX,alturaTexto+2*margemY);
                g.setColor(Color.white);
                g.drawRect(x-larguraTexto/2-margemX+1,y-alturaTexto/2-margemY+1,larguraTexto+2*margemX-2,alturaTexto+2*margemY-2);
                g.setColor(Color.black);
                g.drawRect(x-larguraTexto/2-margemX+2,y-alturaTexto/2-margemY+2,larguraTexto+2*margemX-4,alturaTexto+2*margemY-4);
            }
            g.setColor(retornarCorDeTexto());
            g.drawString(texto, x-larguraTexto/2, y+alturaTexto/2-3);

            

//            g.setColor(retornarCorDeFundo());
//            g.fillRect(x-5,y-5,10,10);
//            g.setColor(Color.black);
//            g.drawRect(x-5,y-5,10,10);
//            
//            if (marcado) {
//                Font font = g.getFont();
//                atualizarDimensoesTexto(g, texto);
//                g.setColor(Color.black);
//                g.drawRect(x-5,y-5,10,10);
//                g.setColor(Color.white);
//                g.drawRect(x-6,y-6,12,12);
//                g.setColor(Color.black);
//                g.drawRect(x-7,y-7,14,14);
//                g.setFont(fonte);
//                g.setColor(Color.white);
//                g.drawString(texto, x+larguraNo+3-1, y+alturaTexto/2+1);
//                g.setColor(retornarCorDeTexto());
//                g.drawString(texto, x+larguraNo+3, y+alturaTexto/2);
//            }
//            
        }
        
    }
    
    /** Testa se o ponto (x,y) informado se encontra dentro do n�.
     * @param x Abscissa do ponto a ser verificado.
     * @param y Ordenada do ponto a ser verificado.
     * @return Retorna AREA_INTERIOR se o ponto est� dentro do n�, e AREA_EXTERIOR caso
     * contr�rio.
     */
    public int areaEm(int x, int y) {
        //double tx = this.x-larguraTexto/2-margemX;
        //double ty = this.y-alturaTexto/2-margemY;
        double tx = this.x-larguraNo/2;
        double ty = this.y-alturaNo/2;
        int resposta;
        //if (x >= tx && y >= ty && (x-tx) < (larguraTexto+2*margemX) && (y-ty) < (alturaTexto+2*margemY)) {
        if (x >= tx && y >= ty && (x-tx) < (larguraNo) && (y-ty) < (alturaNo)) {        
            resposta = AREA_INTERIOR;
        } else {
            resposta = AREA_EXTERIOR;
        }
        return resposta;
    }
    
    /** Informa se o n� est� marcado ou n�o.
     * @return True se o n� est� marcado, false caso contr�rio.
     */    
    public boolean eMarcado() {
        return marcado;
    }

    /** Informa se o n� � um n� "escolhido" (mouse sobre n�) ou n�o.
     * @return True se o n� est� sob o mouse, false caso contr�rio.
     */
    public boolean eEscolhido() {
        return escolhido;
    }

    /** Informa se o n� est� escondido ou n�o.
     * @return True se o n� est� escondido, false caso contr�rio.
     */
    public boolean eEscondido() {
        return escondido;
    }

    /** Informa se o n� � um n� atingido ou n�o.
     * @return True se o n� est� sendo atingido, false caso contr�rio.
     */    
    public boolean eAtingido() {
        return atingido > 0;
    }

    /** Informa se o n� pertence ou n�o ao anel central.
     * @return True se o n� pertence ao anel central, false caso contr�rio.
     */    
    public boolean usandoCorIndividual() {
        return usarCorIndividual;
    }
    
    /** Marca n� e as arestas das quais ele � extremidade. */
    public void marcar() {
        marcado = true;
        if (!escolhido && !movendo) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.atingir();
            }
        }
    }
    /** Desmarca n� e as arestas das quais ele � extremidade.
     * As arestas somente s�o desmarcadas se ambos os seus n�s n�o est�o marcados.
     */
    public void desmarcar() {
        marcado = false;
        if (!escolhido && !movendo) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.desfazerAtingirSePossivel();
            }
        }
    }
    
    /** Marca o n� como "escolhido" (mouse est� sobre ele) e as arestas das quais ele �
     * extremidade.
     */
    public void escolher() {
        escolhido = true;
        if (!marcado && !movendo) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.atingir();
            }
        }
    }
    /** Desmarca n� como "escolhido" (mouse j� n�o est� sobre ele) e tenta desmarcar as
     * arestas das quais ele � extremidade. As arestas somente s�o desmarcadas se ambos
     * os seus n�s n�o est�o marcados.
     */
    public void desfazerEscolher() {
        escolhido = false;
        if (!marcado && !movendo) {
            ListIterator li = arestas.listIterator();
            while(li.hasNext()) {
                Aresta aresta = (Aresta) li.next();
                aresta.desfazerAtingirSePossivel();
            }
        }
    }

    /** Marca o n� como escondido. */
    public void esconder() {
        escondido = true;
        // esconder arestas relacionadas a esse n�
        ListIterator li = arestas.listIterator();
        Aresta aresta;
        while(li.hasNext()) {
            aresta = (Aresta)li.next();
            aresta.esconder();
        }
    }
    
    /** Marca o n� como n�o escondido. */
    public void mostrar() {
        escondido = false;
        ListIterator li = arestas.listIterator();
        while(li.hasNext()) {
            Aresta aresta = (Aresta) li.next();
            // Se ambos os n�s da aresta n�o s�o escondidos
            // ent�o posso mostrar a aresta.
            if (!aresta.no1.eEscondido() && !aresta.no2.eEscondido()) {
                aresta.mostrar();
            }
        }
    }

    /** Marca o n� como atingido, aumentando o contador de n�mero de arestas
     * selecionadas das quais ele � extremidade.
     */
    public void atingir() {
        atingido++;
    }
    
    /** Desmarca um n� como atingido, diminuindo o contador de n�mero de arestas
     * selecionadas das quais ele � extremidade.
     */    
    public void desfazerAtingir() {
        atingido--;
        if (atingido<0) {
            System.out.println("ERRO: O n�mero de arestas que atingem o n� "+this+" � tido como negativo: "+atingido+".");
            System.out.println("      Ajustando para zero.");
            atingido = 0;
        }

    }

    /** Zera o contador de arestas selecionadas das quais este n� � extremidade. */    
    public void anularAtingir() {
        atingido = 0;
    }
    
    /** Atribui um conjunto espec�fico de cores para este n�. O conjunto atribu�do
     * substitui o conjunto de cores do grupo ao qual este n� pertence.
     * @param cor Conjunto de cores a ser atribu�do a este n�.
     */    
    public void ajustarCorIndividual(CorNo cor) {
        usarCorIndividual = true;
        this.cor = cor;
    }
    
    /** Ignora o conjunto de cores atribu�do especificamente para este n�, usando ent�o
     * o conjunto de cores do grupo ao qual o n� pertence.
     */    
    public void removerCorIndividual() {
        usarCorIndividual = false;
    }
    
    /** Transfere o n� para um determinado grupo.
     * @param grupo Grupo para o qual o n� est� sendo transferido.
     */    
    public void moverParaGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
    
    /** Atribui uma nova informa��o ao n�, substituindo a anterior.
     * @param info Nova informa��o a ser associada ao n�.
     */    
    public void ajustarInfo(Info info) {
        this.info = info;
    }
    
    /** Retorna a informa��o associada atualmente ao n�.
     * @return Retorna a informa��o associada atualmente ao n�.
     */    
    public Info retornarInfo() {
        return info;
    }

    /** Informa a largura do n�.
     * @return Retorna a largura do n�.
     */    
    public int getWidth() {
        return (larguraTexto + margemX * 2);
    }
    
    /** Informa a altura do n�.
     * @return Retorna a altura do n�.
     */    
    public int getHeight() {
        return (alturaTexto + margemY * 2);
    }
    
    /** Retorna o c�digo do n� .
     * @return c�digo do n�.
     */
    public String retornarId() {
        return id;
    }
    
    /** Registra o c�digo do n�.
     * @param id c�digo do n�.
     */
    public void ajustarId(String id) {
        this.id = id;
    }

    /** Retorna uma string que caracteriza o n�. 
     * @return String no formato "nome(id)", onde nome � o nome do n� e id �
     * seu c�digo.
     */
    public String toString() {
        return nome+"("+id+")";
    }
}

