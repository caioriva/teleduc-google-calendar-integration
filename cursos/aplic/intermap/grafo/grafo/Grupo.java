/*
 * Grupo.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */
package grafo;

import java.io.*;

/** Essa classe define um grupo de n�s de um grafo.
 * Os n�s que comp�em o grupo n�o s�o armazenados nesta classe; ao contr�rio, o
 * pr�prio n� sabe a que grupo ele pertence.
 */
public class Grupo 
{

    /** Nome do grupo.
     */
    public String nome;

    /** Cor do grupo.
     */
    public CorNo cor;

    /** Cria uma nova inst�ncia da classe Grupo.
     * @param nome Nome do grupo.
     * @param cor Conjunto de cores dos n�s desse grupo.
     */
    public Grupo(String nome, CorNo cor) {
        this.nome = nome;
        if (cor==null) {
            this.cor = new CorNo();
        } else {
            this.cor = cor;
        }
    }

    /** Transforma o grupo em uma string. No caso, simplesmente retorna o nome do grupo.
     * @return Retorna o nome do grupo.
     */
    public String toString() {
        return nome;
    }
    
}

