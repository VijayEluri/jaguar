/**
** <StackDelta.java> -- The AFS's specific delta
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


package jaguar.machine.stack.structures;

import jaguar.machine.*;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.stack.structures.exceptions.*;
import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/** 
 * Función de transición delta<p> d: Q x Sigma x Gamma --> <em>P</em> (Q x Gamma^{*})
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class StackDelta extends Delta{


    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public StackDelta (){
	super();	
    }

    public StackDelta(org.w3c.dom.Node node)throws Exception{
	NodeList transitions = node.getChildNodes();
	Node pNode=null,ssNode=null,gsNode=null,qxgssNode=null;
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
			ssNode = currentTransition.item(j);
			itemFound = true;
		    }
		itemFound = false;	
		for( ; !itemFound &&  j < currentTransition.getLength(); j++)
		    if(currentTransition.item(j).getNodeType() == Node.ELEMENT_NODE){
			gsNode = currentTransition.item(j);
			itemFound = true;
		    }
		itemFound = false;	
		for( ; !itemFound &&  j < currentTransition.getLength(); j++)
		    if(currentTransition.item(j).getNodeType() == Node.ELEMENT_NODE){
			qxgssNode = currentTransition.item(j);
			itemFound = true;
		    }
		addTransition(new State(pNode),new Symbol(ssNode),new Symbol(gsNode),new QxGammaStarSet(qxgssNode));
	    }
	}
    }    

    public String toString(){
	String s  = "";
	Object oKeys [] = delta.keySet().toArray();
	Object oKeysSub [];	
	for(int i = 0 ; i < oKeys.length;  i ++){
	    s += "\n\t\t "+ oKeys[i];
	    oKeysSub =  ((Hashtable)delta.get(oKeys[i])).keySet().toArray();
	    for(int j = 0 ; j < oKeysSub.length;  j ++){
		s += "\n\t\t\t " + oKeysSub[j] +" " + ((Hashtable)delta.get(oKeys[i])).get(oKeysSub[j]);
	    }
	}
	return s;	
    }
    
    /** 
     ** Agrega una transición a la función de transición delta.
     ** Checa que la cadena <code>strGamma</code> este sobre Gamma^{*}.
     **
     ** Con los dos últimos parametros es como si le dieramos solo un elemento del conjunto {QxGamma^{*}}
     ** @param p primer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param sigmaSym segunda entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param gammaSym tercer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param q primer entrada del par ordenado  Q x Gamma^{*}.
     ** @param gammaStr segunda entrada del par ordenado  Q x Gamma^{*}, si la transición ya estaba definida se agrega este elemento al conjunto
     **
     **/
    public void addTransition(State p, Symbol sigmaSym, Symbol gammaSym, State q, Str gammaStr){
	addTransition(p, sigmaSym, gammaSym, new QxGammaStar(q,gammaStr));
    }

    /** 
     ** Agrega una transición a la función de transición delta.
     ** Checa que la cadena <code>strGamma</code> este sobre Gamma^{*}.
     **
     ** @param p primer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param sigmaSym segunda entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param gammaSym tercer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param qtgStar es un elemento del conjunto {QxGamma^{*}},si la transición ya estaba definida se agrega este elemento al conjunto
     **
     **/
    public void addTransition(State p, Symbol sigmaSym, Symbol gammaSym, QxGammaStar qtgStar){
	QxGammaStarSet tmp = new QxGammaStarSet();
	tmp.add(qtgStar);
	addTransition(p, sigmaSym, gammaSym, tmp);	
    }

    /** 
     ** Agrega una transición a la función de transición delta.
     ** Checa que la cadena <code>strGamma</code> este sobre Gamma^{*}.
     **
     ** @param p primer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param sigmaSym segunda entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param gammaSym tercer entrada de la tercia ordenada Q x Sigma x Gamma.
     ** @param qtgStarSet el conjunto {QxGamma^{*}}, si la transición ya estaba definida se hace la union entre el anterior y este conjunto 
     **
     **/
    public void addTransition(State p, Symbol sigmaSym, Symbol gammaSym, QxGammaStarSet qtgStarSet){	
	Hashtable tQ = (Hashtable)delta.get(p);
	if(tQ == null)
	    tQ = new Hashtable();
	Hashtable tSigmaSym = (Hashtable)tQ.get(sigmaSym);
	if(tSigmaSym == null)
	    tSigmaSym = new Hashtable();
	QxGammaStarSet transicionesHS = (QxGammaStarSet)tSigmaSym.get(gammaSym);
	if(transicionesHS == null)
	    transicionesHS = new QxGammaStarSet();
	
	transicionesHS.addAll(qtgStarSet);
	tSigmaSym.put(gammaSym,transicionesHS);
	tQ.put(sigmaSym,tSigmaSym);
	delta.put(p,tQ);
    }

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
			((Symbol)vtrans.get(1)).toFile(fw);
			((QxGammaStarSet)vtrans.get(2)).toFile(fw);			
			fw.write(TRANS_END_TAG);
		    }
		}
	    }
	    fw.write("\n" + END_TAG+"\n");
	    fw.flush();	    
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}

    }

    /**
     ** Regresa un estado con todas sus transiciones
     ** @param p el estado del cual queremos conocer todas sus transiciones 
     ** @return v un vector <code>v</code> donde cada entrada es un vector <code>vi</code> de tamaño 3 con la siguiente estructura: <p>
     ** <ul>
     **   <li> <code> v.elelemAt(0) </code> tiene el símbolo <code>s0</code> en Sigma, segundo elemento del par ordenado d:Q x Sigmma  x Gamma</li>
     **   <li> <code> v.elelemAt(1) </code> tiene el símbolo <code>s1</code> en Gamma, tercer elemento del par ordenado d:Q x Sigmma  x Gamma</li>
     **   <li> <code> v.elelemAt(2) </code> tiene el conjunto de transiciones <code>QxGammaStarSet</code> resultante de aplicar
     **          la función de transición delta d(<code>p,s0,s1</code>), un subconjunto de <em>P(</em>Q x Gamma^{*}<em>)</em> </li>
     ** </ul>
     ** el tamaño de v es el número de transiciones definidas desde el  estado  <code>p</code>
     **/
    public Vector getTransitions(State p){
	Vector v = new Vector(), vi;
	Hashtable htQ = (Hashtable)delta.get(p);
	if(htQ == null) return v;
	Object [] sigmaSymKeys = htQ.keySet().toArray();
	Object [] gammaSymKeys;	
	Symbol sSigma;
	Hashtable htGammaSym;
	QxGammaStar qtgStar;	
	for(int i = 0 ; i < sigmaSymKeys.length; i++){
	    sSigma = (Symbol)sigmaSymKeys[i];	    
	    htGammaSym = (Hashtable) htQ.get(sSigma);	    
	    gammaSymKeys = htGammaSym.keySet().toArray();	    
	    for( int j = 0 ; j < gammaSymKeys.length; j++){
		vi = new Vector();
		vi.add(sSigma);
		vi.add(gammaSymKeys[j]);
		vi.add(htGammaSym.get(gammaSymKeys[j]));
		v.add(vi);		
	    }
	}
	return v;	
    }

    /**
     * Regresa el valor de la aplicar la función de transición delta <em>d</em>(<code>p,symSigma,symGamma</code>)
     * @param p el primer elemento de la tercía ordenada d: Q x Sigma x Gamma
     * @param symSigma el segundo elemento de la tercía ordenada d: Q x Sigma x Gamma
     * @param symGamma el tercer elemento de la tercía ordenada d: Q x Sigma x Gamma
     * @return QxGammaStarSet StateSet un conjunto de estados que es todos los estaodos  a los que se transifiere
     * el automata con el símbolo symb en el estado q
     */
    public QxGammaStarSet apply(State p, Symbol symSigma, Symbol symGamma){
	Hashtable tP = (Hashtable)delta.get(p);
	if(tP == null)
	    return null;
	Hashtable tsymSigma = (Hashtable)tP.get(symSigma);
	if(tsymSigma == null)
	    return null;
	return (QxGammaStarSet)tsymSigma.get(symGamma);
    }

    public String getStringTransitions(State p){
	Vector v = getTransitions(p);
	Vector vp;	
	String res = "";
	QxGammaStar qtgs;	
	for(int i = 0; i < v.size(); i++){
	    vp = (Vector) v.elementAt(i);	    
	    res += "<br> &nbsp; d(<font color=blue>"+p.getLabel()+"</font>,<font color=red>"+((Symbol)vp.elementAt(0))+"</font>,<font color=red>"+((Symbol)vp.elementAt(1))+"</font>) = <font color=blue>"+ vp.elementAt(2)+"</font> &nbsp;";
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


}


/* StackDelta.java ends here. */
