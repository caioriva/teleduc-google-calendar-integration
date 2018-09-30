/*
 * StringInfo.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;

import javax.swing.*;
import java.io.*;

/** Classe que implementa Info para criar objetos que armazenem informa��o 
 * textual a ser usada por um n� ou uma aresta do grafo.
 */
public class StringInfo implements Info 
{
    
    /** Texto que esta classe armazena.
     */
    public String texto="";

    /** Cria uma nova inst�ncia de StringInfo. */
    public StringInfo() {
        texto = "";
    }
    
    /** Cria uma nova inst�ncia de StringInfo.
     * @param texto Informa��o textual (String) a ser armazenada.
     */
    public StringInfo(String texto) {
        this.texto = texto;
    }
    
//    /** Informa se um objeto especificado � igual ao objeto em quest�o.
//     * Dois objetos StringInfo s�o iguais se apresentam o mesmo valor da
//     * propriedade texto.
//     * @param obj Objeto a ser comparado.
//     * @return True se o objeto for igual, false caso contr�rio.
//     */
//    public boolean equals(Object obj) {
//        if (obj.getClass() != this.getClass()) return false;
//        return ((StringInfo)obj).texto.equals(texto);
//    }
    
    /** Retorna string que representa a informa��o.
     * @return Retorna string que represente a informa��o.
     */
    public String toString() {
        return texto;
    }
    
    /** Retorna representa��o visual de informa��o.
     * @return Retorna um JTextPane contendo a informa��o armazenada por este objeto.
     */
    public JComponent retornarInformacao() {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setText(texto);
        return pane;
    }
    
}

