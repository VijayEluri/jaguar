/**
** <ProductionSet.java> -- The productions set
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


package jaguar.grammar.structures;


import java.util.HashSet;
import java.io.*;
import jaguar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.util.*;
import java.lang.Class;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/** 
 * El conjunto de producciones que va a tener una gramatica
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:02 $
 */
abstract public class ProductionSet extends HashSet implements Cloneable{
    /**
     ** Para ver si ya leimos los tags 
     */
    public static final boolean TAGS_LEIDOS = true;
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "productionSet";
    /**
     ** Para ver si ya leimos los tags 
     */
    public static final boolean TAGS_NO_LEIDOS = false;
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
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public ProductionSet (){
	super();	
    }
    
    /**
     * Crea un conjunto de producciones con un elemento
     * @param p el elemento con el que se inicializa el conjunto de estados
     */
    public ProductionSet(Production p){
	this();
	this.add(p);
    }

    /**
     * Crea un conjunto de producciones con un elemento
     * @param p el conjunto con el que se inicializa el conjunto de estados
     */
    public ProductionSet(ProductionSet p){
	this();
	this.addAll(p);
    }

    
    public boolean add(Object o){
	if(!(o instanceof Production))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden agregar Producciones a este conjunto, ["+o+", "+ o.getClass().getName()+"]  AGREGADO!!");
	return super.add(o);
    }

    public ProductionSet minus(ProductionSet P){
	Object pArray[] = P.toArray();
	ProductionSet result  = ( ProductionSet)clone();
	for(int i = 0 ; i < pArray.length ; i++)
	    result.remove(pArray[i]);
	return result;
    }
    
    public ProductionSet union(ProductionSet P){
	ProductionSet pp =  (ProductionSet) clone();
	if(!pp.addAll(P))
	    Debug.println("dfa.structures.ProductionSet WARNING: Un error al intentar pp.addAll(P),\n\t\tcon pp=["+pp+"] y P = ["+P+"] \n");
	return pp;
    }
    
    public boolean remove(Object o) {
	if(!(o instanceof Production))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden eliminar  Production de este conjunto, ["+o+", "+ o.getClass().getName()+"]  ELIMINANDO!!");
	return super.remove(o);
    }
    /** 
     * Indica si otro <code>Object</code> es "igual" a este. 
     * <br>
     * El método <code>equals</code> implementa una relación de equivalencia: 
     * <ul>
     * <li> Es reflexiva: para toda referencia x, x.equals(x) debe regresar <code>true</code></li>
     * <li> Es simétrica: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     * <code>x.equals(y)</code> debe regresar <code>true</code> si y solo si
     * <code>y.equals(x)</code> regresa <code>true</code></li>
     * <li> Es transitiva: Para cualesquiera tres referencias <code>x,y,z</code> si
     * <code>x.equals</code> regresa <code>true</code> y <code>y.equals(z)</code>
     * regresa <code>true</code>, entonces <code>x.equals(z)</code> debe regresar <code>true</code>.</li>
     * <li> Es consistente: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     * múltiples llamadas de <code>x.equals(y)</code> deben consistentemente regresar <code>true</code> o
     * consistentemente regresar <code>false</code>, siempre y cuando no cambie en el objeto la información usada
     * en las comparaciones de <code>equals</code> .</li>
     * <li> Para toda referencia no nula <code>x</code>,<code>x.equals(null)</code> debe regresar <code>false</code>.</li> 
     * </ul>
     * 
     * El método <code>equals</code> para la clase ProductionSet es implementado mediante el uso de == para los campos de tipos básicos
     * y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     * @param o el objeto con el que voy a comparar a este.
     * 
     * @return <code>true<code> si este objeto es igual a <code>otro</code>.
     * @see java.lang.Object#equals
     */ 
    public boolean equals(Object o){
	if (o instanceof ProductionSet
	    && super.equals(o))
	    return true;
	return false;
    }

    /** 
     * Esta es la función que debe de implementar cada una de los tipos de producción dependiendo el tipo de gramatica que se quiere implementar .
     *
     * @return Regresa verdadero si esta bien "creada" la produccion con respecto al tipo de gramatica que se esta describiendo.
     *
     */
    abstract public boolean validate(Alphabet N, Alphabet T, Symbol S) throws ProductionNotValidTypeException;
    
    
    public boolean contains(Object o){
	return super.contains(o);	
    }
    
    /** 
     * Guarda la representación de el conjunto de producciones
     * Gramática en un archivo con el formato definido por el DTD correspondiente
     *
     *Escribe el conjunto de producciones con su representación correspondiente con
     *tags.
     *
     * @param fw El FileWriter donde se escribirá el conjunto de producciones.
     */
    public void toFile(FileWriter fw){
	try{
	    Object o[] = toArray();
	    fw.write(BEG_TAG);
	    for(int i = 0 ; i < o.length ; i++){
		fw.write("\n");
		((Production)o[i]).toFile(fw);
	    }
	    fw.write("\n"+ END_TAG);
	    fw.flush();
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }

    
}

/* ProductionSet.java ends here. */
