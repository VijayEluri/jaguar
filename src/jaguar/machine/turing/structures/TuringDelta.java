/**
** <TuringDelta.java> -- The Turing Machine's specific delta
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


package jaguar.machine.turing.structures;

import jaguar.machine.structures.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import jaguar.machine.util.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.turing.structures.exceptions.*;
import jaguar.util.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.turing.structures.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/** 
 * La función de transiciones de las MTs
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:05 $
 */
public class TuringDelta extends Delta{
     /**
      * Constructor sin parámetros.
      * Inicializa el objeto usando los valores por omision.
      */
     public TuringDelta (){
	 super();
     }


    public TuringDelta(org.w3c.dom.Node node)throws Exception{
	NodeList transitions = node.getChildNodes();
	Node pNode=null,sNode=null,qxgxdNode=null;
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
			qxgxdNode = currentTransition.item(j);
			itemFound = true;
		    }
		addTransition(new State(pNode),new Symbol(sNode),new QxGammaxDirection(qxgxdNode));
	    }
	}
    }    


     /** 
      * Agrega una transición a la función de transición delta.
      * Agrega a la función delta una transición que da el siguiente movimiento y está definida de la siguiente manera:<br> delta: QxT --> QxTx{LEFT,RIGHT}<br>donde {LEFT,RIGHT} denotan la dirección del movimiento de la cabeza lectora de la cinta..
      *
      * @param Qin Es un estado del conjunto Q de la MT, es el primer elemento del par ordenado de entrada.
      * @param Tin Es un símbolo del alfabeto T (gamma) de la MT, es el segundo elemento del par ordenado de entrada.
      * @param Qresult Es un estado del conjunto Q de la MT, es el primer elemento de la tercia ordenada resultante
      * @param Tresult Es un símbolo del alfabeto T (gamma) de la MT, es el segundo elemento de la tercia ordenada resultante.
      * @param direction La dirección en que se mueve la cabeza lectora de la cinta, debe de ser uno de los dos siguientes valores: <ul> <li><code>Turing.LEFT</code></li><li><code>Turing.LEFT</code></li></ul>.
      *
      * @see jaguar.machine.turing.Turing#LEFT
      * @see jaguar.machine.turing.Turing#RIGHT
      */
    public void addTransition(State Qin, Symbol Tin, State Qresult, Symbol Tresult, int direction){
	 addTransition(Qin, Tin, new QxGammaxDirection(Qresult,Tresult,direction));
     }
     
     protected void addTransition(State Qin, Symbol Tin, QxGammaxDirection terciaResultante){
	 Hashtable t = (Hashtable)delta.get(Qin);
	 if(t == null)
	     t = new Hashtable();
	 QxGammaxDirection res = (QxGammaxDirection) t.get(Tin);
	 if(res == null)
	     t.put(Tin,terciaResultante);
	 else{
	     Debug.println("\n\tTuringDelta.addTransition: WARNING: La transición d("+Qin+","+Tin+
			   ") ya estaba definida como \"" + Qin + "\" se redefinio como \"" + terciaResultante + "\" ");
	     t.put(Tin,terciaResultante);
	 }
	 delta.put(Qin,t);
     }
     

     public QxGammaxDirection apply(State q, Symbol symb){
	 Hashtable t = (Hashtable)delta.get(q);
	 if(t == null)
	     return null;	
	 return (QxGammaxDirection)t.get(symb);
     }
     

    /**
     ** Regresa un vecto con todas las transiciones del estado dado
     ** @param p el estado del cual queremos conocer todas sus transiciones 
     ** @return v un vector <code>v</code> donde cada entrada es un vector <code>vi</code> de tamaño 2 con la siguiente estructura: <p>
     ** <ul>
     **   <li> <code> vi.elelemAt(0) </code> tiene el símbolo <code>s</code>, segundo elemento del par ordenado d:Q x Gamma  </li>
     **   <li> <code> vi.elelemAt(1) </code> tiene la tercia ordenada QxGammaxDirection resultante de aplicar la función de transición delta d(<code>p,s</code>) </li>
     ** </ul>
     ** el tamaño de v es el número de transiciones definidas desde el  estado  <code>p</code>, que en la MT deberia de ser tamaño 1
     **/
    public Vector getTransitions(State p){
	Vector v = new  Vector();
	Hashtable t = (Hashtable)delta.get(p);
	if(t == null){
	    return v;
	}
	Symbol s;
	QxGammaxDirection qgd;	
	for(Enumeration e = t.keys(); e.hasMoreElements(); ){
	    s = (Symbol)e.nextElement();
	    qgd = (QxGammaxDirection)t.get(s);
	    Vector vp = new Vector();
	    vp.add(s);
	    vp.add(qgd);	    
	    v.add(vp);
	}
	return v;
    }
    

    /** 
      * Regresa la representación como cadena en html de las transiciones del estado <code>p</code> para los tooltips .
      *
      * @param p El estado del cual queremos todas las transiciones definidas.
      * @return la cadena en formato html para los tooltips.
      *
      */
    public String getStringTransitions(State p ){
	Vector v = getTransitions(p);
	Vector vp;	
	String res = "";
	for(int i = 0; i < v.size(); i++){
	    vp = (Vector) v.elementAt(i);
	    res += "<br> &nbsp; d(<font color=blue>"+p.getLabel()+"</font>,<font color=red>"+((Symbol)vp.elementAt(0))+"</font>) = <font color=blue>"+ ((QxGammaxDirection)vp.elementAt(1))+"</font> &nbsp;";
	}
	return res;
    }

    /**
     * Regresa una cadena con formato  html para los tooltips que nos muestra todas las transiciones deifinidas para el estado dado 
     * @param p el estado de quien queremos ver todas sus transiciones definidas
     **/
    public String getToolTipString(State p){
	return "<html> &nbsp; <font color=blue>" + p.toString() +  (p.getIsInF()?" Final State ":"") + "</font>" + getStringTransitions(p)+"</html>";
    }
    

    /** 
     * Escribe la representación de la función de trnasición delta en un archivo con el formato definido por el DTD correspondiente
     * Escribe la delta con su representación correspondiente con tags.
     *
     * @param fw El <code>FileWriter</code> donde se guardará la delta
     */
    public void toFile(FileWriter fw){
	try{ 
	    fw.write("\n"+BEG_TAG);
	    Object [] oKeys = delta.keySet().toArray();
	    State currentSt;
	    Symbol currentSym;
	    /** El estado resultante de la combinación QxSigma **/
	    QxGammaxDirection currentResSt;
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
			((QxGammaxDirection)vtrans.get(1)).toFile(fw);
			fw.write(" "+TRANS_END_TAG);
		    }
		}
	    }
	    fw.write("\n" + END_TAG+"\n");
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
 }

/* TuringDelta.java ends here. */
