/*
 * Grafo.java
 * Vers�o: 2004-07-04
 * Autores: Celmar Guimar�es da Silva e Petr Stukjunger
 */

package grafo;

import java.awt.Color;
import java.io.*;

/** Classe que define as cores de um n�.
 */
public class CorNo 
{

    /** Cor do texto do n� que n�o est� marcado.
     */    
    public Color corNormal             = Color.black;
    /** Cor do texto do n� que est� marcado.
     */    
    public Color corMarcado            = Color.black;
    /** Cor de fundo do n� que n�o est� marcado.
     */    
        
    public Color corDeFundoNormal    = new Color(0xA7F498);    //new Color(0x008F00);
    /** Cor de fundo do n� que est� marcado.
     */    
    public Color corDeFundoMarcado   = Color.green; //new Color(0x72FF51);

    /** Cria uma nova inst�ncia de CorNo.
     */    
    public CorNo() {}

    /** Cria uma nova inst�ncia de CorNo, usando as cores de frente definidas no
     * par�metro cor.
     * @param cor Define as cores de frente que ser�o usadas nesta inst�ncia de CorNo.
     */    
    public CorNo(CorNo cor) {
        corNormal  = cor.corNormal;
        corMarcado = cor.corMarcado;
   }
    
}

