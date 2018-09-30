/*
 * LinkInfo.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;

import javax.swing.*;
import java.io.*;
import java.awt.Color;
import java.awt.Cursor;

/** Classe que implementa Info para criar objetos que armazenem um link
 * a ser usado por um n� ou uma aresta do grafo.
 */
public class LinkInfo implements Info {
    
    /** Nome do link. */
    public String nome;
    
    /** URL do link. */
    public String url;
    
    /** Nome da janela em que ele deve aparecer. */
    public String janela;
    
    /** Applet que pode ser afetado pelo clique do link */
    public javax.swing.JApplet applet;
    
    /** Cria uma nova inst�ncia de LinkInfo. */    
    public LinkInfo() {
        this("","",null,"_blank");
    }    
    
    /** Cria uma nova inst�ncia de LinkInfo.
     * @param nome Nome que ir� aparecer como link.
     * @param url URL do link.
     */
    public LinkInfo(String nome, String url) {
        this(nome,url,null,"_blank");
    }

    /** Cria uma nova inst�ncia de LinkInfo.
     * @param nome Nome que ir� aparecer como link.
     * @param url URL do link.
     * @param applet Applet em que o link ir� aparecer.
     */
    public LinkInfo(String nome, String url, JApplet applet) {
        this(nome,url,applet,"_blank");        
    }
    
    /** Cria uma nova inst�ncia de LinkInfo.
     * @param nome Nome que ir� aparecer como link.
     * @param url URL do link.
     * @param applet Applet em que o link ir� aparecer.
     * @param janela Janela do navegador (browser) em que a p�gina indicada pela
     * URL dever� aparecer.
     */
    public LinkInfo(String nome, String url, JApplet applet, String janela) {
        this.nome = nome;
        this.url = url;
        this.applet = applet;
        this.janela = janela;
    }
    
    /** Retorna string que representa a informa��o.
     * @return Retorna string que represente a informa��o, no formato "nome:url",
     * onde nome � o nome do link e url a sua URL.
     */
    public String toString() {
        return nome+":"+url;
    }
    
    /** Retorna representa��o visual de informa��o.
     * @return Retorna um JTextPane contendo a informa��o armazenada por este objeto.
     */
    public JComponent retornarInformacao() {
        JLabel label = new JLabel("<html><u>"+nome+"</u>");
        label.setForeground(Color.blue);
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarJanelaLink();
            }
        });
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }
    
    /** Faz com que a p�gina da URL especificada pelo link seja aberta no 
     * navegador em que o applet est� sendo executado, e na janela determinada 
     * pelo link.
     */
    private void mostrarJanelaLink() {
        if (applet != null) {
            java.net.URL u;
            try {
                u = new java.net.URL(url);
                applet.getAppletContext().showDocument(u, janela);
                
            } catch (java.net.MalformedURLException e) {
                System.out.println("Erro: URL mal formada. URL acessada: \""+url+"\"");
            }
        }
    }
    
}