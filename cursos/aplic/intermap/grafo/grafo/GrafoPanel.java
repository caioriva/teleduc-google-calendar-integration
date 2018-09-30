/*
 * GrafoPanel.java
 * Vers�o: 2004-07-04
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.List;

/** Cria um panel em que o grafo e seus respectivos controles s�o desenhados. */
public abstract class GrafoPanel extends JPanel
implements ComponentListener 
{
    
    /** Grafo a ser desenhado. */    
    private Grafo grafo;
    
    /** Display em que o grafo � mostrado. */    
    private GrafoDisplay display;
    
    /** Textos a serem utilizados pelo panel e pelo seu display. */
    public String[] textos;
    
    /** Cria uma nova inst�ncia GrafoPanel.
     * @param grafo Grafo a ser mostrado pelo objeto.
     */
    public GrafoPanel(Grafo grafo) {
        definirTextosDefault();
        
        //usarGrafo(grafo);
        this.grafo = grafo;
        
        // definindo a interface
        display = criarDisplay(grafo);
        if (display!=null) {
            
            display.setAlignmentX(0.0F);
            display.setAlignmentY(0.0F);
            inserirLayoutEControles(display, grafo);
            
        } else {
            System.out.println("Erro. Display n�o p�de ser inicializado.");
            System.out.println("Error. Display could not be initialized.");            
        }
        addComponentListener(this);

    }

    /**
     * Cria e insere layout e controles (se existirem) no GrafoPanel.
     * @param display Display em que o grafo ser� mostrado.
     * @param grafo Grafo a ser mostrado.
     */    
    public abstract void inserirLayoutEControles(GrafoDisplay display, Grafo grafo);
    
    /**
     * Cria um display para mostrar o grafo informado.
     * @param grafo Grafo a ser mostrado.
     * @return Display, cujo tipo varia de acordo com o tipo do grafo informado.
     */    
    public abstract GrafoDisplay criarDisplay(Grafo grafo);
    
    /** Modifica textos utilizados em menus e di�logos. �til para definir um novo
     * conjunto de textos, como seria necess�rio no caso de textos em outro idioma.
     * @param textosNovos Textos que ir�o substituir os 
     * textos atuais, tanto no panel quanto em seu display.
     */
    public void modificarTextos(List textosNovos) {
        // O GrafoPanel em si n�o utiliza nenhum texto.
        // Logo, repassa para o display todos os textos recebidos.
        if (textosNovos!=null) {
            if (textosNovos.size() >= textos.length) {
                List textosNovosDoPanel = textosNovos.subList(0, textos.length);
                textos = (String[])textosNovosDoPanel.toArray(textos);
                if (display!=null) {
                    List textosNovosDoDisplay = textosNovos.subList(textos.length, textosNovos.size());
                    display.modificarTextos(textosNovosDoDisplay);
                } else {
                    System.out.println("Erro. N�o foi poss�vel modificar os textos do display, porque o display do grafo ainda n�o estava pronto.");
                    // System.out.println("Error. The display texts could not be modified because the graph display was not ready.");
                }
            } else {
                System.out.println("Erro. N�o foi poss�vel modificar os textos do panel, porque a lista contendo os novos textos � de tamanho diferente da lista original de textos.");
            }
        } else {
            System.out.println("N�o foi poss�vel modificar os textos do panel, porque a lista contendo os novos textos � nula.");
        }
        
    }

    /** Modifica o nome que descreve o peso dos n�s. Inicialmente esse nome � "Peso",
     * mas pode haver interesse em modific�-lo para especificar que esse peso se refere
     * a uma determinada medida (por exemplo, n�mero de mensagens recebidas).
     * Esse nome � independente do nome do peso das arestas.
     * @param nome Novo nome a ser utilizado.
     */        
    public void renomearPesoDosNosPara(String nome) {
        display.renomearPesoDosNosPara(nome);
    }
    
    /** Modifica o nome que descreve o peso das arestas. Inicialmente esse nome � "Peso",
     * mas pode haver interesse em modific�-lo para especificar que esse peso se refere
     * a uma determinada medida (por exemplo, n�mero de mensagens representadas pela
     * aresta).
     * Esse nome � independente do nome do peso dos n�s.
     * @param nome Novo nome a ser utilizado.
     */    
    public void renomearPesoDasArestasPara(String nome) {
        display.renomearPesoDasArestasPara(nome);
    }
    
    
    /** Salva um arquivo contendo a imagem do display mostrando o grafo.
     * @param nomeArquivo Nome do arquivo
     * @param tipoArquivo Tipo do arquivo (ao menos jpeg e png s�o suportados).
     * @return True se a opera��o foi bem sucedida, False caso contr�rio.
     */    
    public boolean salvarDisplay(String nomeArquivo, String tipoArquivo) {
        boolean resposta = false;
        if (display!=null) {
            File arquivo = new File(nomeArquivo);
            resposta = display.salvarImagem(arquivo, tipoArquivo);
        } 
        return resposta;
    }

    /**
     * Informa se j� foi definido algum display para este GrafoPanel.
     * @return True se um display foi definido, False caso contr�rio.
     */    
    public boolean displayEstaDefinido() {
        return (display!=null);
    }
    
    /** Define quais textos ser�o utilizados pelo panel. */
    public void definirTextosDefault() {
        textos = new String[0];
    }
    
    /** Sorteia a posi��o dos n�s de acordo com o tamanho atual do panel.
     */
    synchronized public void disporNosAleatoriamente() {
        if (display != null) {
            display.disporNosAleatoriamente();
        }
    }
    
    /** Sorteia a posi��o dos n�s de acordo com uma dimens�o especificada.
     * @param d Dimens�o dentro da qual as posi��es dos n�s devem ser sorteadas.
     */
    synchronized public void disporNosAleatoriamente(Dimension d) {
        if (display != null) {
            display.disporNosAleatoriamente(d);
        }
    }    
    
    /** Permite que os n�s executem anima��es.
     */
    public void habilitarAnimacao() {
        display.habilitarAnimacao();
    }

    /** N�o permite que os n�s executem anima��es.
     */
    public void desabilitarAnimacao() {
        display.desabilitarAnimacao();
    }

    /** Ajusta propriedades do grafo quando ele � exibido. 
     * @param componentEvent Evento.
     */
    public void componentShown(java.awt.event.ComponentEvent componentEvent) {
        display.ajustarLimitesDeEspacoDoGrafo();
    }
    
    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */    
    public void componentMoved(java.awt.event.ComponentEvent componentEvent) {
    }
    
    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */    
    public void componentResized(java.awt.event.ComponentEvent componentEvent) {
    }

    /** N�o est� sendo utilizado.
     * @param componentEvent Evento.
     */    
    public void componentHidden(java.awt.event.ComponentEvent componentEvent) {
    }
        
}