/**
** <ProductionT2Set.java> -- The production set to type 2 grammars
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


package jaguar.grammar.tipo2.structures;


import java.util.HashSet;
import java.io.*;
import jaguar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.grammar.structures.*;
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
 * @version 0.1
 */
public class ProductionT2Set extends ProductionSet{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public ProductionT2Set (){
	super();	
    }
    /**
     * Crea un conjunto de producciones con un elemento
     * @param p el elemento con el que se inicializa el conjunto de estados
     */
    public ProductionT2Set(Production p){
	super(p);	
    }
    
    /**
     * Construye un ProductionSet dado el documento DOM
     */
    public ProductionT2Set(org.w3c.dom.Node domNode) throws Exception{
	this();
	NodeList domPList = domNode.getChildNodes();
	for(int i = 0 ; i < domPList.getLength() ; i++)
	    if(domPList.item(i).getNodeType() == Node.ELEMENT_NODE)
		add(new ProductionT2(domPList.item(i)));
    }

    /** 
     *
     * Esta es la función que debe de implementar cada una de los
     * tipos de conjuntos de producción dependiendo el tipo de gramatica que se
     * quiere implementar .
     *
     * Las producciones para gramáticas regulares o tipo 2 se
     * caracterizan por tener del lado derecho a lo más 2 símbolos,
     * uno terminal y otro no terminal. <p>
     *
     * Si la producción es de la forma<br>
     *    <code>A  --> aB</code> o  <code>A  --> a</code> con <code>A,B</code> no terminales y <code>a</code> terminal<br>
     * se trata de una  producción <em>lineal a la derecha</em> y si es de la forma <br>
     *    <code>A  --> Ba</code> o  <code>A  --> a</code> <br>
     * se trata de una producción <em>lineal a la  izquierda</em> 
     *
     * @param N el alfabeto de símbolos no terminales
     * @param T el alfabeto de símbolos terminales
     * @param S el símbolo inicial 
     * @return Regresa verdadero si todas las producciones en el conjunto están
     * bien "creadas" con respecto al tipo de gramatica que se esta describiendo.
     */
    public boolean validate(Alphabet N, Alphabet T, Symbol S) throws ProductionNotValidTypeException{
	Object oarray [] = toArray();
	ProductionT2 foo = new ProductionT2(new Str(S,false),new Str());
	boolean containsSToEpsilonProduction = this.contains(foo);
	for(int i = 0; i < oarray.length; i ++)
	    ((ProductionT2)oarray[i]).validate(N,T,S,containsSToEpsilonProduction);
	return true;
    }

    public boolean validateLazy(Alphabet N, Alphabet T, Symbol S) throws ProductionNotValidTypeException{
	Object oarray [] = toArray();
	ProductionT2 foo = new ProductionT2(new Str(S,false),new Str());
	boolean containsSToEpsilonProduction = this.contains(foo);
	for(int i = 0; i < oarray.length; i ++)
	    ((ProductionT2)oarray[i]).validateLazy(N,T,S);
	return true;
    }
    

    public boolean contains(Object o){
	Object [] oarray = this.toArray();
	for (int i = 0; i < oarray.length; i++)
	    if(((ProductionT2)oarray[i]).equals(o))
		return true;
	return false;
    }
    
    public boolean add(ProductionT2 o){
	if(!(o instanceof ProductionT2))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden agregar Producciones Tipo 2 a este conjunto, ["+o+", "+ o.getClass().getName()+"]  AGREGADO!!");
	    return super.add(o);
    }
    
    public boolean remove(Object o) {
	if(!(o instanceof ProductionT2))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden eliminar  ProductionT2 de este conjunto, ["+o+", "+ o.getClass().getName()+"]  ELIMINANDO!!");
	return super.remove(o);
    }
    /** 
     * Indica si otro <code>Object</code> es "igual" a este. 
     * <br>
     * El método <code>equals</code> implementa una relación de equivalencia: 
     *    <ul>
     *    <li> Es reflexiva: para toda referencia x, x.equals(x) debe regresar <code>true</code></li>
     *    <li> Es simétrica: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     *    <code>x.equals(y)</code> debe regresar <code>true</code> si y solo si
     *    <code>y.equals(x)</code> regresa <code>true</code></li>
     *    <li> Es transitiva: Para cualesquiera tres referencias <code>x,y,z</code> si
     *    <code>x.equals</code> regresa <code>true</code> y <code>y.equals(z)</code>
     *    regresa <code>true</code>, entonces <code>x.equals(z)</code> debe regresar <code>true</code>.</li>
     *    <li> Es consistente: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     *    múltiples llamadas de <code>x.equals(y)</code> deben consistentemente regresar <code>true</code> o
     *    consistentemente regresar <code>false</code>, siempre y cuando no cambie en el objeto la información usada
     *    en las comparaciones de <code>equals</code> .</li>
     *    <li> Para toda referencia no nula <code>x</code>,<code>x.equals(null)</code> debe regresar <code>false</code>.</li> 
     *    </ul>
     * 
     *    El método <code>equals</code> para la clase ProductionSet es implementado mediante el uso de == para los campos de tipos básicos
     *    y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     *    @param o el objeto con el que voy a comparar a este.
     * 
     *    @return <code>true<code> si este objeto es igual a <code>otro</code>.
     *    @see java.lang.Object#equals
     */ 
    public boolean equals(Object o){
	if (o instanceof ProductionT2Set
	    && super.equals(o))
	    return true;
	return false;
    }


    /** 
     * Rutinas de prueba para la clase ProductionSet.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase ProductionSet. \n"
			   +"Comentario: El conjunto de producciones que va a tener una gramatica\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
	System.err.println("Las producciones son pt2s");	
    }

}

/* ProductionSet.java ends here. */
