/**
** <MachineGrammarStyledDocument.java> -- To format  the string's  look and feel on the frames
**
** Copyright (C) 2002 by  Ivan Hernández Serrano
**
** This file is part of JAGUAR
**
** This program is free software; you can redistribute it and/or
** modify it under the terms of the GNU General Public License
** as published by the Free Software Foundation; either version 2
** of the License, or (at your option) any later version.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with this program; if not, write to the Free Software
** Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
**
** Author: Ivan Hernández Serrano <ivanx@users.sourceforge.net>
**
**/


package jaguar.util.jutil;

import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import jaguar.structures.*;
import java.util.LinkedList;

/**
 * La clase que tiene estilos para areas de texto para representar gramaticas, maquinas, etc
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class MachineGrammarStyledDocument extends DefaultStyledDocument{
    /**
     * El atributo plano, sin colorcitos ni nada lindo
     */
    protected SimpleAttributeSet plainAtt;

    /**
     * El atributo con background moradito java
     */
    protected SimpleAttributeSet segundoAtt;
    /**
     * El atributo con el background rojito claro
     */
    protected SimpleAttributeSet primeroAtt;
     /**
      * El background es blanco y el foreground es rojo
      */
    private SimpleAttributeSet currentAtt;
    /**
     * El bg verde pistache y el fg azul marino
     */
    private SimpleAttributeSet estadoTuringAtt;

    /** El maximo numero de caracteres **/
    protected int maxCharacters;
    /**
     * Constructor.
     * Recibe los valores para plainAtt, segundoAtt, primeroAtt y maxCharacters.
     * Para el resto de los campos usa el valor por omision.
     * @param plainAtt el valor con el que se inicalizará el campo plainAtt
     * @param segundoAtt el valor con el que se inicalizará el campo segundoAtt
     * @param primeroAtt el valor con el que se inicalizará el campo primeroAtt
     * @param maxCharacters el valor con el que se inicalizará el campo maxCharacters
     * @see #plainAtt
     * @see #segundoAtt
     * @see #primeroAtt
     * @see #maxCharacters
     */
    public MachineGrammarStyledDocument (SimpleAttributeSet plainAtt, SimpleAttributeSet segundoAtt,
           SimpleAttributeSet primeroAtt, int maxCharacters){
               this.plainAtt=plainAtt;
               this.segundoAtt=segundoAtt;
               this.primeroAtt=primeroAtt;
               this.maxCharacters=maxCharacters;
    }

    /**
     * Constructor.
     * Recibe los valores para maxCharacters.
     * Para el resto de los campos usa el valor por omision.
     * @param maxCharacters el valor con el que se inicalizará el campo maxCharacters
     * @see #maxCharacters
     * @see #plainAtt
     * @see #segundoAtt
     * @see #primeroAtt
     */
    public MachineGrammarStyledDocument (int maxCharacters){
        this.maxCharacters = maxCharacters;

        primeroAtt = new SimpleAttributeSet();
        StyleConstants.setFontFamily(primeroAtt, "SansSerif");
        StyleConstants.setFontSize(primeroAtt, 12);
        StyleConstants.setBold(primeroAtt,true);
        StyleConstants.setForeground(primeroAtt, Color.black);
        StyleConstants.setBackground(primeroAtt, new Color(193,188,255)); // moradito java
        //      StyleConstants.setBackground(primeroAtt, new Color(122,204,255)); // azul clarito
        // Azulito    StyleConstants.setBackground(primeroAtt, new Color(140,159,255));
        //      StyleConstants.setBackground(primeroAtt, Color.yellow);

        segundoAtt = new SimpleAttributeSet();
        StyleConstants.setFontFamily(segundoAtt, "SansSerif");
        StyleConstants.setFontSize(segundoAtt, 12);
        StyleConstants.setBold(segundoAtt,true);
        StyleConstants.setForeground(segundoAtt, Color.black);
        StyleConstants.setBackground(segundoAtt, new Color(255,191,191)); // rojito claro
        //      StyleConstants.setBackground(segundoAtt, new Color(161,255,158)); // verde pistache
        //Rojo-rosa    StyleConstants.setBackground(segundoAtt, new Color(255,140,151));
        //      StyleConstants.setBackground(segundoAtt, Color.orange);

        estadoTuringAtt = new SimpleAttributeSet();
        StyleConstants.setFontFamily(estadoTuringAtt, "SansSerif");
        StyleConstants.setFontSize(estadoTuringAtt, 12);
        StyleConstants.setBold(estadoTuringAtt ,true);
        StyleConstants.setForeground(estadoTuringAtt, new Color(18,24,188)); // azul marino
        StyleConstants.setBackground(estadoTuringAtt, new Color(167,255,135)); // verde pistache

        plainAtt = new SimpleAttributeSet();
        StyleConstants.setFontFamily(plainAtt, "SansSerif");
        StyleConstants.setFontSize(plainAtt, 12);
        StyleConstants.setBold(plainAtt,true);
        StyleConstants.setForeground(plainAtt, Color.black);
        StyleConstants.setBackground(plainAtt, Color.white);

        currentAtt = new SimpleAttributeSet();
        StyleConstants.setFontFamily(currentAtt, "SansSerif");
        StyleConstants.setFontSize(currentAtt, 12);
        StyleConstants.setBold(currentAtt,true);
        StyleConstants.setForeground(currentAtt, Color.red);
    }

    public MachineGrammarStyledDocument(String s) {
        this(s.length()+10);
        try{
            insertString(0,s,plainAtt);
        }catch( Exception ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " s: " +s);
            ouch.printStackTrace();
        }
    }

    public MachineGrammarStyledDocument(Str str) {
        this((str.length()*15)+10);
        try {
            if(str.isEpsilon()) {
                insertString(0,str.toString(),primeroAtt);
            } else {
                for(int i = str.length()-1; i>=0 ; i--) {
                    insertString(0,str.getSymbol(i).getSym(),((i%2)==0)?segundoAtt:primeroAtt);
                }
            }
        } catch( Exception ouch) {
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " Str: " +str);
            ouch.printStackTrace();
        }
    }

    public MachineGrammarStyledDocument(LinkedList turingTape) {
        this((turingTape.size()*15)+10);
        try {
            if(turingTape.isEmpty())
                insertString(0,"",primeroAtt);
            else
            for(int i = turingTape.size()-1; i>=0 ; i--) {
                if(turingTape.get(i) instanceof State){
                    insertString(0,((State)turingTape.get(i)).getLabel(),estadoTuringAtt);
                } else {
                    insertString(0,((Symbol)turingTape.get(i)).getSym(),((i%2)==0)?segundoAtt:primeroAtt);
                }
            }
        } catch (Exception ouch) {
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                + " turingTape: " +turingTape);
            ouch.printStackTrace();
        }
    }

    public MachineGrammarStyledDocument(Alphabet A) {
        this((A.size()*10)+11);
        try{
            insertString(0,A.END_TAG,plainAtt);
            Object oA[] = A.toArray();
            for(int i = 0; i < oA.length; i++){
                insertString(0,((Symbol)oA[i]).getSym(),((i%2)==0)?primeroAtt:segundoAtt);
                if(i+1 < oA.length)
                    insertString(0," , " , plainAtt);
            }
            insertString(0,A.BEG_TAG,plainAtt);
        }catch( Exception ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " A: " +A);
            ouch.printStackTrace();
        }
    }

    public void insertTuringTape(LinkedList turingTape){
        try{
            remove(0,getLength());
            if(turingTape.isEmpty())
                insertString(0,"",primeroAtt);
            else
          for (int i = turingTape.size()-1; i>=0 ; i--) {
              if(turingTape.get(i) instanceof State)
                  insertString(0,((State)turingTape.get(i)).getLabel(),estadoTuringAtt);
              else insertString(0,((Symbol)turingTape.get(i)).getSym(),((i%2)==0)?segundoAtt:primeroAtt);
          }
        } catch (Exception ouch) {
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " turingTape: " +turingTape);
            ouch.printStackTrace();
        }
    }

    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException {
        //This rejects the entire insertion if it would make
        //the contents too long. Another option would be
        //to truncate the inserted string so the contents
        //would be exactly maxCharacters in length.
        if ((getLength() + str.length()) <= maxCharacters)
            super.insertString(offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

    public void insertStr(Str str){
        try{
             if(!str.isEpsilon())
          for(int i = str.length()-1; i>=0 ; i--)
              insertString(0,str.getSymbol(i).getSym(),((i%2)==0)?segundoAtt:primeroAtt);

        }catch( Exception ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " Str : " + str);
            ouch.printStackTrace();
        }
    }


    public void reset(){
        try{
            remove(0,getLength());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void insertStr(Str pre, Symbol curr, Str post){
        try{
            remove(0,getLength());
            insertStr(post);
            insertString(0,curr.getSym(),currentAtt);
            insertStr(pre);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertStrStatus(String pre, String current, String post){
        try{
            remove(0,getLength());
            insertString(0,post,segundoAtt);
            insertString(0,current,primeroAtt);
            insertString(0,pre,segundoAtt);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertString(String post){
        try{
            remove(0,getLength());
            insertString(0,post,segundoAtt);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public MachineGrammarStyledDocument (){
        this(100);
    }

    /**
     * Rutinas de prueba para la clase MachineGrammarStyledDocument.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase MachineGrammarStyledDocument. \n"
               +"Comentario: La clase que tiene estilos para areas de texto para representar gramaticas, maquinas, etc\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
    }

}

/* MachineGrammarStyledDocument.java ends here. */
