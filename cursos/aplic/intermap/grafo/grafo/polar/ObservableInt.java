/*
 * ObservableInt.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.polar;
import java.util.Observable;

/** Classe respons�vel pela cria��o de um objeto que registre um valor inteiro.
 */
public class ObservableInt extends Observable {
    

    /** Valor inteiro armazenado pelo objeto. */    
    private int valor = 0;
    
    /** Cria uma nova inst�ncia de ObservableInt. */
    public ObservableInt() {
        this(0);
    }
    
    /** Cria uma nova inst�ncia de ObservableInt, armazenando o valor especificado.
     * @param valor Valor a ser armazenado.
     */    
    public ObservableInt(int valor) {
        this.valor = valor;
    }
    
    /** Retorna o valor inteiro armazenado pelo objeto.
     * @return Valor armazenado pelo objeto.
     */    
    public int getValue() {
        return valor;
    }
    
    /** Modifica o valor inteiro armazenado pelo objeto.
     * @param valor Novo valor a ser armazenado.
     */    
    public void setValue(int valor) {
        this.valor = valor;
        setChanged();
        notifyObservers();
    }
    
    /** Adiciona um valor ao n�mero inteiro armazenado no objeto.
     * @param delta Valor a ser adicionado ao n�mero armazenado.
     */    
    public void add(int delta) {
        setValue(valor + delta);
    }

    /** Subtrai um valor do n�mero inteiro armazenado no objeto.
     * @param delta Valor a ser subtra�do do n�mero armazenado.
     */    
    public void sub(int delta) {
        setValue(valor - delta);
    }
    
}
