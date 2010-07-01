/**
** <DfaDelta.java> -- The DFA's  specific delta
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


package jaguar.machine.dfa.structures;
/**
 * <Delta.java> ----
 *
 * Copyright (C) 2000 by Free Software Foundation, Inc.
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:04 $
 */

import jaguar.machine.structures.*;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import jaguar.util.*;
import jaguar.machine.dfa.structures.exceptions.DfaDeltaNotFoundException;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import org.w3c.dom.*;

public class DfaDelta extends Delta{

    /**
     * Iinicializa una función de transición delta
     */
    public DfaDelta(){
  super();
    }

    public DfaDelta(org.w3c.dom.Node node, StateSet Q){
  NodeList transitions = node.getChildNodes();
  Node pNode=null,sNode=null,qNode=null;
  int j;
  boolean itemFound = false;
  for(int i = 0 ; i < transitions.getLength() ; i++){
      if(transitions.item(i).getNodeType() == Node.ELEMENT_NODE){
    itemFound = false;
    NodeList currentTransition = transitions.item(i).getChildNodes();
    for(j = 0 ; !itemFound &&  j < currentTransition.getLength(); j++)
        if(currentTransition.item(j).getNodeType() == Node.ELEMENT_NODE){
      pNode = currentTransition.item(j);
      itemFound = true;
        }
    itemFound = false;
    for( ; !itemFound &&  j < currentTransition.getLength(); j++)
        if(currentTransition.item(j).getNodeType() == Node.ELEMENT_NODE){
      sNode = currentTransition.item(j);
      itemFound = true;
        }
    itemFound = false;
    for( ; !itemFound &&  j < currentTransition.getLength(); j++)
        if(currentTransition.item(j).getNodeType() == Node.ELEMENT_NODE){
      qNode = currentTransition.item(j);
      itemFound = true;
        }
    addTransition(Q.makeStateReference(new State(pNode)),
            new Symbol(sNode),
            Q.makeStateReference(new State(qNode)));
      }
  }
    }

    /**
     * Agrega una transición de la forma delta(q,symb)=transition
     */
    public void addTransition(State q, Symbol symb, State transition){
        Hashtable t = (Hashtable)delta.get(q);
        if (t == null)
            t = new Hashtable();
        State res = (State)t.get(symb);
        t.put(symb,transition);
        delta.put(q,t);
    }

    public void removeTransition(State q, Symbol symb) {
        Hashtable t = (Hashtable)delta.get(q);
        if (t == null) {
            return;
        }
        State res = (State)t.get(symb);
        if (res == null) {
            return;
        }
        t.remove(symb);
    }

    /**
     * Regresa el valor de la aplicar la función de transición delta.
     *
     * @param q el <code>State</code> en el que se encuentra el autómata.
     * @param symb el <code>Symbol</code> que está leyendo.
     * @return el <code>State</code> al que se transfiere el automata con el
     * símbolo <code>symb</code> en el estado <code>q</code>
     */
    public State apply(State q, Symbol symb){
      Hashtable t = (Hashtable)delta.get(q);
      if(t == null)
      return null;
  return (State)t.get(symb);
    }



    /**
     ** Regresa un estado con todas sus transiciones
     ** @param p el estado del cual queremos conocer todas sus transiciones
     ** @return v un vector <code>v</code> donde cada entrada es un vector <code>vi</code> de tamaño 2 con la siguiente estructura: <p>
     ** <ul>
     **   <li> <code> vi.elelemAt(0) </code> tiene el símbolo <code>s</code>, segundo elemento del par ordenado d:Q x Sigmma  </li>
     **   <li> <code> vi.elelemAt(1) </code> tiene el estado resultante de aplicar la función de transición delta d(<code>p,s</code>) </li>
     ** </ul>
     ** el tamaño de v es el número de transiciones definidas desde el  estado  <code>p</code>
     **/
    public Vector getTransitions(State p){
  Vector v = new  Vector();
  Hashtable t = (Hashtable)delta.get(p);
  if(t == null)
      return v;
  Symbol s;
  State st;
  for(Enumeration e = t.keys(); e.hasMoreElements(); ){
      s = (Symbol)e.nextElement();
      st = (State)t.get(s);
      Vector vp = new Vector();
      vp.add(s);
      vp.add(st);
      v.add(vp);
  }
  return v;
    }

    /**
     * Escribe la representación de la función de transición delta en un archivo con el formato definido por el DTD correspondiente
     * Escribe la delta con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se escribirá la delta
     */
    public void toFile(FileWriter fw){
  try{
      fw.write("\n"+BEG_TAG);
      Object [] oKeys = delta.keySet().toArray();
      State currentSt;
      Symbol currentSym;
      /** El estado resultante de la combinación QxSigma **/
      State currentResSt;
      Vector vaux, vtrans;
      for(int i = 0 ; i < oKeys.length; i++){
    currentSt = (State)oKeys[i];
    vaux = getTransitions(currentSt);
    for(int j = 0 ; j < vaux.size(); j ++){
        /** checamos que el resultado de la transición sea distinto de nulo **/
        vtrans=(Vector)vaux.get(j);
        if(vtrans.get(1) != null){
      fw.write("\n\t"+TRANS_BEG_TAG);
      currentSt.toFile(fw);
      ((Symbol)vtrans.get(0)).toFile(fw);
      ((State)vtrans.get(1)).toFile(fw);
      fw.write(TRANS_END_TAG);
        }else Debug.println("\n\t\tDfaDelta.toFile: OOPS, para currentSt ["+ currentSt+"] casí me rompo => vtrans[0,1] = (" + vtrans.get(0) +  ", " +vtrans.get(1) + ")\n");
    }
      }
      fw.write("\n" + END_TAG+"\n");
  }catch( Exception ouch){
      System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
             + "Trying to toFile: " );
      ouch.printStackTrace();
  }
    }

    public String getStringTransitions(State p){
  Vector v = getTransitions(p);
  Vector vp;
  String res = "";
  for(int i = 0; i < v.size(); i++){
      vp = (Vector) v.elementAt(i);
      res += "<br> &nbsp; d(<font color=blue>"+p.getLabel()+"</font>,<font color=red>"+((Symbol)vp.elementAt(0))+"</font>) = <font color=blue>"+ ((State)vp.elementAt(1))+"</font> &nbsp;";
  }
  return res;
    }

    /**
     * Regresa una cadena con formato  html para los tooltips que nos muestra
     * todas las transiciones deifinidas para el estado dado.
     * @param p el estado de quien queremos ver todas sus transiciones
     * definidas.
     */
    public String getToolTipString(State p){
  return "<html> &nbsp; <font color=blue>" + p.toString() +  (p.getIsInF()?" Final State ":"") + "</font>" + getStringTransitions(p)+"</html>";
    }

}

/* Delta.java ends here */
