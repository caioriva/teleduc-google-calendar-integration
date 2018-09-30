/*
 * NoSimples.java
 * Vers�o: 2004-08-26
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */
package grafo.simples;

import java.util.*;
import java.io.*;
import java.awt.*;

import grafo.*;

/** Classe para armazenar um n� de um grafo.
 * Da forma como est� implementada, essa classe n�o sabe desenhar o n�.
 * Um elemento desta classe n�o conhece o grafo ao qual pertence, embora
 * saiba quais arestas incidem sobre ele.
 */
public class NoSimples extends No {

    /** Cria uma nova inst�ncia de NoSimples, criando um n� sem peso nem informa��o 
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     */
    public NoSimples(String nome) {
        this(nome, 0, null);
    }

    /** Cria uma nova inst�ncia de NoSimples, criando um n� com peso mas sem informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param peso Peso do n�.
     */
    public NoSimples(String nome, int peso) {
        this(nome, peso, null);
    }

    /** Cria uma nova inst�ncia de NoSimples, criando um n� sem peso mas com informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param info Informa��o associada ao n�.
     */
    public NoSimples(String nome, Info info) {
        this(nome, 0, info);
    }    
    /** Cria uma nova inst�ncia de NoSimples, criando um n� com peso e com informa��o
     * associada.
     * @param nome Nome do n� (nome mostrado no n�).
     * @param peso Peso do n�.
     * @param info Informa��o associada ao n�.
     */
    public NoSimples(String nome, int peso, Info info) {
        super(nome, peso, info);
    }
        

    
}