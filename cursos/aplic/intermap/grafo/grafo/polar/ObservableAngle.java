/*
 * ObservableAngle.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.polar;
import java.util.Observable;
import grafo.*;

/** Classe respons�vel pela cria��o de um objeto que registre o valor de um 
 * �ngulo. 
 */
public class ObservableAngle extends Observable {
    
    /** Valor do �ngulo em graus ( 0 <= angulo < 360 ). */    
    private double valor = 0;
    
    /** Cria uma nova inst�ncia de ObservableAngle. */
    public ObservableAngle() {
        this(0);
    }
    
    /** Cria uma nova inst�ncia de ObservableAngle cujo angulo tenha o valor
     * especificado.
     * @param valor Valor do �ngulo com o qual o objeto deve ser criado.
     */
    public ObservableAngle(double valor) {
        this.valor = Util.normalizarAngulo(valor);
    }

    /** Retorna o valor do �ngulo armazenado.
     * @return Retorna o �ngulo armazenado pelo objeto.
     */
    public double getValue() {
        return valor;
    }
    
    /** Ajusta o valor do �ngulo armazenado, e notifica seus observadores.
     * @param valor Novo valor do �ngulo.
     */
    public void setValue(double valor) {
        this.valor = Util.normalizarAngulo(valor);
        setChanged();
        notifyObservers();
    }
    
}
