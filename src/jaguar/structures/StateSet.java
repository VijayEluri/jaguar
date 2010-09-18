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

import java.util.LinkedHashSet;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import jaguar.structures.exceptions.StateSetNotFoundException;
import jaguar.util.*;
import java.lang.Class;
import org.w3c.dom.*;

public class StateSet extends LinkedHashSet<State> {
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
     * Crea un conjunto de estados vacío
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
        for(int i = 0; i < domSymList.getLength() ; i++) {
            if(domSymList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                add(new State(domSymList.item(i)));
            }
        }
    }

    /**
     ** Hace las referencias necesarias para unificar Conjuntos de States
     ** @param subset es un subconjunto de la instancia
     */
    public StateSet makeSubSetReferences(StateSet subset){
        StateSet newSharedReferencesStateSet = new StateSet();
        State superItem;
        for (State item : this) {
            if(subset.remove(item)) {
                newSharedReferencesStateSet.add(item);
            }
        }

        if(subset.isEmpty()) {
            return newSharedReferencesStateSet;
        }
        // Aqui debemos lanzar una excepcion!!!!!!
        //  Debug.println("StateSet.makeSubSetReferences: el subset " + subset + " es distinto del vacio!!!!! " );
        return null;
    }

    /**
     * Checa si el estado q esta presente en el conjunto y de ser así hace la referencia pertinente
     */
    public State makeStateReference(State q){
        State superItem;
        for (State item : this) {
            if(item.equals(q)){
                return item;
            }
        }

        return null;
    }

    public StateSet makeStateReference(StateSet sts){
        State superItem;
        StateSet result = new StateSet();
        for (State item : this) {
            item = this.makeStateReference(item);
            if(item != null) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     ** marca al conjunto de estados como un conjunto de estados finales, esto
     ** lo hace marcando a cada uno de los estados en el conjunto como un estado final
     */
    public void markAsFinal(){
        for (State state : this) {
            state.setIsInF(true);
        }
    }

    /**
     * Regresa verdadero si contiene un estado final, de otra forma regresa falso.
     *
     * @return <code>true</code> si contiene al menos un estado final, <code>false</code> en cualquier otro caso.
     *
     */
    public boolean containsFinalState(){
        for (State state : this) {
            Debug.println("\t\tst = " + state + " isfinal? " + state.getIsInF());
            if(state.getIsInF()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Escribe la representación del <code>StateSet</code> en un archivo con el formato definido por el DTD correspondiente
     * Escribe el StateSet con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el StateSet.
     */
    public void toFile(FileWriter fw) {
        toFile(fw, false);
    }

    public void toFile(FileWriter fw, boolean withLocation) {
        try {
            fw.write(BEG_TAG);
            for (State state : this) {
                state.toFile(fw, withLocation);
            }
            fw.write(END_TAG);
        } catch( Exception ouch ){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                + "Trying to toFile: " );
            ouch.printStackTrace();
        }
    }

    /**
     * Regresa si este conjunto se intersecta con el conjunto que le pasamos como parametro.
     *
     * @param sts El conjunto de estados con quien haremos la intesección .
     * @return <code>true</code> si los conjuntos se intersectan <code>false</code> de otra forma
     *
     */
    public boolean intersect(StateSet sts){
        if(sts != null){
            for (State state : sts) {
                if (contains(state)) {
                    return true;
                }
            }
        }
        return false;
    }

    public State[] toArray() {
        State[] ary = new State[size()];
        return toArray(ary);
    }

    public String toCommaSeparatedList() {
        String s = toString();
        int i = s.length();
        return s.substring(1,i-1);
    }

}


/* StatesSet.java ends here */
