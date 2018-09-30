/*
 * Info.java
 * Vers�o: 2004-07-04
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */
package grafo;

import javax.swing.*;
import java.io.*;

/** Interface que estabelece m�todos padr�es para gerenciamento de informa��es
 * sobre n�s ou arestas de um grafo.
 */
public interface Info 
{

//    /** Informa se um objeto especificado � igual ao objeto em quest�o.
//     * @param obj Objeto a ser comparado.
//     * @return True se o objeto for igual, false caso contr�rio.
//     */
//    public boolean equals(Object obj);
//
//    /** Retorna string que representa a informa��o.
//     * @return Retorna string que represente a informa��o.
//     */
//    public String toString();

    /** Retorna componente visual com representa��o visual de informa��o.
     * @return Retorna um JComponent que cont�m a representa�ao visual de informa��o.
     * Se a informa��o for um texto, pode retornar um panel contendo esse texto, por
     * exemplo. Em outro exemplo, a informa��o pode ser uma imagem ou um gr�fico, e
     * o panel retornado pode representar esses elementos.
     */
    public JComponent retornarInformacao();

}

