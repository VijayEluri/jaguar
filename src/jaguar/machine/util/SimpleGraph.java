/**
** <SimpleGraph.java> -- A simple graph
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


package jaguar.machine.util;

import java.util.*;
import jaguar.machine.structures.*;
import jaguar.structures.*;

/** 
 * Implementa una grafica dirigida con listas de adyacencia
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class SimpleGraph implements Cloneable{

    /**
      * La lista de adyacencias para la gráfica
      */
    protected Hashtable adjacencyL;
    /**
     * funcion de acceso para obtener el valor de adjacencyL
     * @return el valor actual de adjacencyL
     * @see #adjacencyL
     */
    public Hashtable getAdjacencyL(){
	return adjacencyL;
    }
    /**
     * funcion de acceso para modificar adjacencyL
     * @param new_adjacencyL el nuevo valor para adjacencyL
     * @see #adjacencyL
     */
    public void setAdjacencyL(Hashtable new_adjacencyL){
	adjacencyL=new_adjacencyL;
    }

    /**
     ** Regresa las etiquetas de una arista dada
     ** @param source el vertice origen
     ** @param dest el vertice destino
     **/
    public Vector getLabels(Object source, Object dest){
	Vector currentAdjacentsToSource = (Vector)adjacencyL.get(source);
	if(currentAdjacentsToSource == null)
	    return new Vector();
	int idx = -1;
	for(int i = 0 ; i < currentAdjacentsToSource.size(); i++)
	    if(currentAdjacentsToSource.get(i).equals(dest))
		idx = i;
	if(idx < 0)
	    return new Vector();
	Arista a = (Arista)currentAdjacentsToSource.get(idx);
	return a.getLabel();
    }
    
    /**
     ** Regresa el conjunto de vertices de la gráfica
     **/
    public Set getEstados(){
	return adjacencyL.keySet();
    }
    
    /**
     ** Agrega una arista <source,dest> con la etiqueta label
     **
     ** Si la arista ya existe, entonces agregamos la etiqueta label al vector de etiquetas de la arista
     ** @param source el vertice fuente
     ** @param dest el vertice destino
     ** @param label la etiqueta de la arista
     **/ 
    public void addArista(Object source, Object dest, Object label){
	Vector currentAdjacentsOfSource = (Vector)adjacencyL.get(source);
	if(currentAdjacentsOfSource == null){
	    Vector newAdjacentOfSource = new Vector();
	    Vector LabelList = new Vector();
	    LabelList.add(label);
	    newAdjacentOfSource.add(new Arista(dest, LabelList));	    
	    adjacencyL.put(source,newAdjacentOfSource);
	    return;
	}
	int idx=-1;
	for(int i = 0 ; i < currentAdjacentsOfSource.size(); i++)
	    if(currentAdjacentsOfSource.get(i).equals(dest))
		idx = i;
	Arista a;
	Vector augmentedLabel;
 	if(idx >= 0){
 	    a = (Arista)currentAdjacentsOfSource.get(idx);
 	    augmentedLabel = a.getLabel();
	    if(! augmentedLabel.contains(label)){
		augmentedLabel.add(label);
		a.setLabel(augmentedLabel);
		currentAdjacentsOfSource.set(idx,a);
	    }
 	}		
	else{
	    augmentedLabel = new Vector();
	    augmentedLabel.add(label);
	    a = new Arista(dest,augmentedLabel);
	    currentAdjacentsOfSource.add(a);	    
	}
    }


    /**
     ** Regresa todos un conjunto de  todos los vertices que son alcanzables desde el vertice source usando solo caminos etiquetados con label
     **
     ** Regresa el conjunto de vertices que son alcanzables desde el vertice <code>source</code> usando solo aristas etiquetadas con <code>label</code>
     ** @param source el vertice desde donde queremos encontrar los alcanzables
     ** @param label la etiqueta que deben de tener las aristas que podemos usar
     ** @return un conjunto de vertices que son alcanzables desde <code>source</code> usando solo vertices etiquetados con <code>label</code>
     **/
    public HashSet getReachabilityFromSourceWithLabel(Object source, Object label){
	HashSet result = getReach(source,label,new LinkedList(), new HashSet());
	result.add(source);
	return result;
    }

    /**
     ** El motor recursivo para la funci'on @see getReachabilityFromSourceWithLabel
     **/
    protected HashSet getReach(Object source, Object label, LinkedList visited, HashSet result){	
	Vector currentList = (Vector)adjacencyL.get(source);
	if(currentList == null)
	    return result;
	Vector aplicables = new Vector();
	for(int i = 0 ; i < currentList.size() ; i++){
	    Arista currentA = (Arista)currentList.get(i);
	    if(currentA.getLabel().contains(label))
		aplicables.add(currentA);
	}
	LinkedList newVisited = new LinkedList(visited);
	for(int k=0; k < aplicables.size(); k++){
	    newVisited.add(((Arista)aplicables.get(k)).getDest());
	}
	aplicables.removeAll(visited);
	for(int k=0; k < aplicables.size(); k++){
	    result.add(((Arista)aplicables.get(k)).getDest());
	}
	HashSet newResult = new HashSet(result);
	for(int j = 0 ; j < aplicables.size(); j++)
	    newResult.addAll(getReach(((Arista)aplicables.get(j)).getDest(),label,newVisited,result));
	return newResult;
    }

    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public SimpleGraph (){
	adjacencyL = new Hashtable();
    }

    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	return adjacencyL.toString();
    }
    
    /**
     * Con esta clase representamos una arista de la gráfica con etiquetas
     */
    class Arista {
	/**
	 * El vector de etiquetas asociadas a la arista <source,dest>
	 */
	Vector label;
	/**
	 * La arista destino
	 */
	Object dest;

	/**
	 * Regresa una cadena <destino, etiqueta>
	 */
	public String toString(){
	    return "<" + dest + ", " + label+ ">";
	    
	}
	/**
	 * La constructora de una arista con un vector de etiquetas 
	 *@param _dest El vertice destino
	 *@param _label El conjunto de etiquetas asociadas a la arista <source, _dest>
	 */
	public Arista(Object _dest, Vector _label){
	    dest = _dest;
	    label = _label;	    
	}

	/**
	 * Regresa el vertice destino
	 */
	public Object getDest(){
	    return dest;
	}
	/**
	 * Asigna un nuevo vector de etiquetas asociadas a la arista <source,this.dest>
	 */
	public void setLabel(Vector _label){
	    label=_label;
	}
	/**
	 * Regresa el conjunto de etiquetas
	 */
	public Vector getLabel(){
	    return label;
	}
	/**
	 * La arista es la misma si el destino es el mismo
	 */
	public boolean equals(Object o){
	    return dest.equals(o);
	}
    }

	
}

/* SimpleGraph.java ends here. */
