/*
 * Util.java
 * Vers�o: 2004-08-26
 * Autor: Celmar Guimar�es da Silva
 */

package grafo;
import java.awt.Color;

/** Classe contendo fun��es gen�ricas utilizadas pelas demais classes.
 */
public class Util {
    
    /** Construtor de inst�ncia da classe Util. */
    private Util() {
        // N�o � utilizado. Deriva��o impedida.
    }
    
    /** Converte uma string para XML
     * Para isso, faz com que cada caractere especial encontrado no texto seja
     * convertido para sua forma correta em XML.
     * @param texto Texto a ser convertido.
     * @return Retorna o texto convertido.
     */    
    public static String ascii2xml(String texto) {
        String aux = "";
        int o; 
        for (int i=0; i<texto.length(); i++) {
            o = (int)(texto.charAt(i)); // ord
            if (o>127 || o<32) {
                aux+="&#"+Integer.toString(o)+";";
            } else {
                aux+=texto.charAt(i);
            }
        }
        return aux;
    }

    /** Retorna um n�mero hexadecimal de 6 d�gitos representando uma cor especificada.
     * @param c Cor.
     * @return N�mero hexadecimal de 6 digitos representando uma cor no espa�o RGB.
     */    
    public static String hexaRGBColor(Color c) {
        String hexa = Integer.toHexString(c.getRGB());
        if (hexa.length()>6) {
            hexa = hexa.substring(hexa.length()-6,hexa.length());
        }
        if (hexa.length()<6) {
            for (int i=0; i< 6 - hexa.length(); i++) {
                hexa = "0" + hexa;
            }
        }
        return hexa;
    }

    /** Deixa um �ngulo informado sempre entre 0(inclusive) e 360(exclusive)
     * @param angulo �ngulo a ser normalizado.
     * @return �ngulo normalizado.
     */
    public static double normalizarAngulo(double angulo) {
        double a = angulo;
        if (Math.abs(a) >= 360) { 
            a = a % 360; 
        }
        // agora -360 < a < 360
        // mas quero que 0 <= a < 360
        if (a < 0) { 
            a = a + 360; 
        }
        return a;
    }    
}
