/**
* <StateSet.java> -- To use state sets
* 
* Copyright (C) 2002 by  Ivan Hernández Serrano
*
* This file is part of JAGUAR
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
* 
* Author: Ivan Hernández Serrano <ivanx@users.sourceforge.net>
* 
* @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
**/


package jaguar.structures;

import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import jaguar.structures.exceptions.StateSetNotFoundException;
import jaguar.util.*;
import java.lang.Class;
import org.w3c.dom.*;

public class StateSet extends HashSet{
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "stateSet";
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
     * Crea un conjunto vacio de estados
     */
    public StateSet(){
	super();
    }

    /**
     * Crea un conjunto de estados con un elemento
     * @param st el elemento con el que se inicializa el conjunto de estados
     */
    public StateSet(State st){
	this();
	this.add(st);	
    }

    /**
     ** Construye un conjunto de estados dado el documento DOM
     **/
    public StateSet(org.w3c.dom.Node domNode){
	this();
	NodeList domSymList = domNode.getChildNodes();
	for(int i = 0; i < domSymList.getLength() ; i++)
	    if(domSymList.item(i).getNodeType() == Node.ELEMENT_NODE)
		add(new State(domSymList.item(i)));
    }

    

     /**
      * Agrega un estado al conjunto de estados
      */
     public boolean add(State e){
 	return super.add(e);
     }
    /**
     * Agregar un objeto al conjutno. NO USAR. generará el siguiente mensaje
     * "dfa.structures.StateSet.add(Object):Warning: Solo se pueden agregar States a este conjunto"
     * Al final si agregaremos con este método, pues lo usa el método addAll, además omitiremos el mensaje de Warning
     */
    public boolean add(Object o){
	return super.add(o);
    }
    /** Removes the given element from this set if it is present. NO USAR. generará el siguiente mensaje
     * "dfa.structures.StateSet.Remove(Object):Warning: Solo se pueden remover States d este conjunto"
     */
    public boolean remove(Object o) {
	return super.remove(o);
    }

    /**
     ** Hace las referencias necesarias para unificar Conjuntos de States
     ** @param subset es un subconjunto de la instancia
     */ 

    public StateSet makeSubSetReferences(StateSet subset){	
	StateSet newSharedReferencesStateSet = new StateSet();
	State superItem;
	for (Iterator i = this.iterator() ; i.hasNext() && !subset.isEmpty() ;) {
	    superItem  = (State) i.next();
	    if(subset.remove(superItem))
		newSharedReferencesStateSet.add(superItem);
	}
	if(subset.isEmpty())
	    return newSharedReferencesStateSet;
	// Aqui debemos lanzar una excepcion!!!!!!
//	Debug.println("StateSet.makeSubSetReferences: el subset " + subset + " es distinto del vacio!!!!! " );
	return null;	
    }

    /**
     * Checa si el estado q esta presente en el conjunto y de ser así hace la referencia pertinente
     */
    public State makeStateReference(State q){
	State superItem;
	for (Iterator i = this.iterator() ; i.hasNext() ;) {
	    superItem  = (State) i.next();
	    if(superItem.equals(q)){
		return superItem;
	    }
	}
	return null;
    }

    public StateSet makeStateReference(StateSet sts){
	State superItem;
	StateSet result = new StateSet();
	for (Iterator i = sts.iterator() ; i.hasNext() ;) {
	    superItem = (State) i.next();
	    superItem = this.makeStateReference(superItem);
	    if(superItem != null)
		result.add(superItem);
	}
	return result;
    }

    /**
     ** marca al conjunto de estados como un conjunto de estados finales, esto
     ** lo hace marcando a cada uno de los estados en el conjunto como un estado final
     */
    public void markAsFinal(){	
	for (Iterator i = this.iterator() ; i.hasNext() ;)
	    ((State) i.next()).setIsInF(true);
    }

    /** 
     * Regresa verdadero si contiene un estado final, de otra forma regresa falso.
     *
     * @return <code>true</code> si contiene al menos un estado final, <code>false</code> en cualquier otro caso.
     *
     */
    public boolean containsFinalState(){
	State st;	
	for (Iterator i = iterator() ; i.hasNext() ;) {
	    st = (State) i.next();
	    Debug.println("\t\tst = " + st + " isfinal? " + st.getIsInF());	    
	    if(st.getIsInF())
		return true;	    
	}
	return false;	
    }       
    /** 
     * Escribe la representación del <code>StateSet</code> en un archivo con el formato definido por el DTD correspondiente
     * Escribe el StateSet con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el StateSet.
     */
    public void toFile(FileWriter fw){
	try{ 
	    fw.write(BEG_TAG);
	    for (Iterator i = this.iterator() ; i.hasNext() ;)
		((State) i.next()).toFile(fw);
	    fw.write(END_TAG);
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
     /** 
      * Regresa si  este conjunto se intersecta con el conjunto que le pasamos  como parametro.
      *
      * @param sts El conjunto de estados con quien haremos la intesección .
      * @return <code>true</code> si los conjuntos se intersectan <code>false</code> de otra forma
      *
      */
    public boolean intersect(StateSet sts ){
	StateSet nsts = new StateSet();
	Object es[];
	if(sts != null){
	    es =  sts.toArray();
	    for(int i =0 ; i < es.length; i++)
		if(contains(es[i]))
		   return true;
	}
	return false;
    }

}
    

/* StatesSet.java ends here */
