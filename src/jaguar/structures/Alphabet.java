/**
** <Alphabet.java> -- To use alphabets
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


/**
 * Alphabet.java
 *
 *
 * Created: Thu Feb  8 07:18:18 2001
 *
 * @author <a href="mailto: "</a>
 * @version
 */
package jaguar.structures;

import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import jaguar.util.*;
import java.util.*;
import jaguar.structures.exceptions.*;
import org.w3c.dom.*;

public class Alphabet implements Cloneable {
    /**
     ** Para ver si ya leimos los tags
     */
    public static final boolean TAGS_LEIDOS = true;
    /**
     ** Para ver si ya leimos los tags
     */
    public static final boolean TAGS_NO_LEIDOS = false;
    /**
     * En este conjunto guardaremos la colección de símbolos
     */
    private HashSet<Symbol> Sigma;

    /**
     * Regresa la representación en arreglo del Alphabet, regresa un Object[]
     */
    public Symbol[] toArray(){
        Symbol[] ary = new Symbol[size()];
        return Sigma.toArray(ary);
    }

    /**
     * Regresa la union de esta instancia con el alfabeto dado
     **/
    public Alphabet union(Alphabet A){
  Alphabet result = new Alphabet(this);
  result.getSigma().addAll(A.getSigma());
  return result;
    }

    /**
     * La diferencia de conjuntos
     */
    public Alphabet minus(Alphabet A){
  Alphabet result = new Alphabet(this);
  Object [] oArray = A.toArray();
  for(int i = 0 ; i < oArray.length ; i++)
      result.remove((Symbol)oArray[i]);
  return result;
    }

    public boolean remove(Symbol s){
  return Sigma.remove(s);
    }

    /**
     * Checa si la cadena dada se puede crear con símbolos de este alfabeto
     */
    public boolean strOnAlphabet(Str s){
  try{
      for(int i = 0 ; i < s.length() ; i ++)
    if(!contains(s.getSymbol(i)))
        return false;
      return true;
  }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
      return false;
  }
    }
    /**
     * Regresa la representación en vector del Alphabet
     */
    public Vector toVector(){
  Object [] oArray =  Sigma.toArray();
  Vector v = new Vector();
  for (int i = 0 ; i < oArray.length ; i++)
      v.add(oArray[i]);
  return v;
    }

    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "alph";
    /**
     * El tag con el que se define el inicio del objeto de un
     * en un archivo
     */
    public static final String BEG_TAG = "<"+ELEMENT_NAME+">";
    /**
     * El tag con el que se define el fin del objeto de un
     * en un archivo
     */
    public static final String END_TAG = "</"+ELEMENT_NAME+">";

    /**
     * Creamos un alfabeto vacio
     */
    public Alphabet (){
  Sigma = new HashSet();
    }

    /**
     * Creamos un alfabeo a partir del <code>Alphabet s</code>
     * @param S  el alfabeto a partir del cual crearemos uno nuevo
     */
    public Alphabet (Alphabet S){
  Sigma = (HashSet)S.getSigma().clone();
    }

    public Alphabet(Symbol s){
  this();
  add(s);
    }

    /**
     ** Construye un alfabeto dado el documento DOM
     **/
    public Alphabet(org.w3c.dom.Node domNode){
  this();
  NodeList domSymList = domNode.getChildNodes();
  for(int i = 0; i < domSymList.getLength() ; i++)
      if(domSymList.item(i).getNodeType() == Node.ELEMENT_NODE)
    add(new Symbol(domSymList.item(i)));
    }


    /**
     * Regresa un <code>Iterator</code> sobre los elementos de este conjunto. Los elementos
     * son regresados no tienen un orden en particular
     */
    public Iterator iterator(){
  return Sigma.iterator();
    }

    /**
     * Agrega un nuevo símbolo al alfabeto
     * @param sym un nuevo símbolo en nuestro alfabeto
     */
    public void add(Symbol sym){
  Sigma.add(sym);
    }


    public boolean contains(Symbol s){
  return Sigma.contains(s);
    }
    /**
     * Regresa la representación de nuestro alfabeto como
     * <code>HashSet</code>
     * @return la representación de <code>HashSet</code> del <code>Alphabet</code>.
     */
    public HashSet getSigma(){
  return Sigma;
    }
    protected void setSigma(HashSet hs){
  Sigma = hs;
    }


    /**
     * Crea y regresa una copia de este objeto
     */
    public Object clone() throws CloneNotSupportedException{
  try{
      Alphabet nuevo = (Alphabet)super.clone();
      nuevo.Sigma = (HashSet)Sigma.clone();
      return nuevo;
  }
  catch (CloneNotSupportedException e){
      throw new InternalError(e.toString());
  }
    }

    /**
     * Regresa la representación en cadena del Alphabet
     */
    public String toString(){
  String s="";
  for (Iterator i = Sigma.iterator() ; i.hasNext() ;)
      s += i.next() ;
  return s;
  //  return BEG_TAG + s + END_TAG;

    }


    /**
     * Guarda la representación del Alphabet en un archivo XML
     * Escribe el alfabeto con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el Alphabet.
     */
    public void toFile(FileWriter fw){
  try{
      fw.write(BEG_TAG);
      for (Iterator i = Sigma.iterator() ; i.hasNext() ;)
    ((Symbol)i.next()).toFile(fw);
      fw.write(END_TAG);
  }catch( Exception ouch){
      System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
             + "Trying to toFile: " );
      ouch.printStackTrace();
  }
    }

    public int size(){ return Sigma.size();}

    public boolean isEmpty(){ return (0 == size());};


    public boolean contains(Alphabet A){
  return Sigma.containsAll(A.getSigma());
    }

    public boolean equals(Object o){
  return (o instanceof Alphabet &&
    ((Alphabet)o).size() == size() &&
    ((Alphabet)o).contains(this));
    }

    /**
     * Reemplaza la aparición de   <code>viejo</code> por <code>nuevo</code> en esta instancia del <code>Alphabet</code>
     *
     * Reemplaza en esta  instancia de <code>Alphabet</code>  la aparición de el símbolo <code>viejo</code>
     * por el símbolo <code>nuevo</code>.
     *
     * @param viejo El símbolo que vamos a sustituir.
     * @param nuevo El nuevo símbolo que aparecerá en lugar de  <code>viejo</code>.
     *
     */
    public void replaceSymbol(Symbol viejo, Symbol nuevo){
  if(remove(viejo))
      add(nuevo);
    }


    /**
     * Regresa un alfabeto que es subconjunto de esta instancia, este alfabeto está formado
     * de todos los simbolos que aparecen en <code>s</code> y están en esta instancia de Alfaeto
     */
    public Alphabet getSubsetInStr(Str s){
  try{

      Alphabet result = new Alphabet();
      for ( int i = 0 ; i < s.length() ; i++)
    if(contains(s.getSymbol(i)))
        result.add(s.getSymbol(i));
      return result;

  }catch( StrIndexOutOfBoundsException ouch){
      System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName());
      ouch.printStackTrace();
      System.exit(1);
  }
  return null;
    }


}// Alphabet
