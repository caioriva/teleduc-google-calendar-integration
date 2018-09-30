/*
 * Util.java
 * Vers�o: 2004-07-04
 * Autor: Celmar Guimar�es da Silva
 */

package grafo.xgmml;
import java.io.*;
/**
 * Classe contendo m�todos est�ticos para gerenciar a leitura e escrita de tags
 * XGMML. Essa classe faz a associa��o entre qual classe do pacote grafo.xgmml
 * conhece as tags de qual classe do pacote grafo.
 */
public class Util {
    
    /** Construtor privado, para que esta classe n�o tenha subclasses. */
    private Util() {
    }
    
    /**
     * Invoca a classe adequada no pacote xgmml para efetuar a escrita das tags do
     * objeto obj em arquivo.
     * @param o Arquivo no qual as tags devem ser escritas.
     * @param obj Objeto a ser descrito.
     * @return True se a opera��o de leitura foi bem sucedida, false caso contr�rio.
     */    
    public static boolean escreverAtributosXGMML(FileWriter o, Object obj) {
        boolean status = false;
        String nomeClasseObj = obj.getClass().getName();
        // Corta "grafo." fora do nome da classe, e substitui por "grafo.xgmml."
        nomeClasseObj = "grafo.xgmml."+nomeClasseObj.substring(6);
        //System.out.println(nomeClasseObj);
        try {
            Class c = Class.forName(nomeClasseObj);
            Object inst = c.newInstance();
            ((Io)inst).escreverAtributosXGMML(o,obj);
        } catch (ClassNotFoundException e) {
            System.out.println ("Classe "+nomeClasseObj+" n�o encontrada.");
        } catch (Exception e) {
            System.out.println ("Exce��o n�o esperada.");
        }
        return(status);
    }
    
    /**
     * Invoca a classe adequada no pacote xgmml para efetuar a leitura dos atributos do
     * n� DOM (raiz) informado e ajustar o objeto obj.
     * @param raiz N� DOM a partir do qual ser�o lidas as tags.
     * @param obj Objeto a ser ajustado.
     * @return True se a opera��o de leitura foi bem sucedida, false caso contr�rio.
     */    
    public static boolean lerAtributosXGMML(org.w3c.dom.Node raiz, Object obj) {
        boolean status = false;
        String nomeClasseObj = obj.getClass().getName();
        // Corta "grafo." fora do nome da classe, e substitui por "grafo.xgmml."
        nomeClasseObj = "grafo.xgmml."+nomeClasseObj.substring(6);
        //System.out.println(nomeClasseObj);
        try {
            Class c = Class.forName(nomeClasseObj);
            Object inst = c.newInstance();
            ((Io)inst).lerAtributosXGMML(raiz, obj);
            status=true;
        } catch (ClassNotFoundException e) {
            System.out.println ("Classe "+nomeClasseObj+" n�o encontrada.");
        } catch (Exception e) {
            System.out.println ("Exce��o n�o esperada.");
        }
        return(status);
    }    
}
