/**
** <ProductionT3Set.java> -- Production Set for type 3 grammars
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


package jaguar.grammar.tipo3.structures;


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
public class ProductionT3Set extends ProductionSet{
    public static final int L_IZQUIERDA = 0;
    public static final int L_DERECHA = 1;
    public static final int L_A_FINALES = 2;
    public static final int L_UNDEFINED = 3;    
    /**
     * Define el tipo de producciones que tiene este conjunto de tipo 3
     */
    protected int linealidad = L_UNDEFINED;
    /**
     * funcion de acceso para obtener el valor de linealidad
     * @return el valor actual de linealidad
     * @see #linealidad
     */
    public int getLinealidad(){
	return linealidad;
    }
    /**
     * funcion de acceso para modificar linealidad
     * @param new_linealidad el nuevo valor para linealidad
     * @see #linealidad
     */
    public void setLinealidad(int new_linealidad){
	linealidad=new_linealidad;
    }
    
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public ProductionT3Set (){
	super();	
    }
    /**
     * Crea un conjunto de producciones con un elemento
     * @param p el elemento con el que se inicializa el conjunto de estados
     */
    public ProductionT3Set(Production p){
	super(p);	
    }

    /**
     ** Construye un ProductionSet dado el documento DOM
     **/
    public ProductionT3Set(org.w3c.dom.Node domNode)throws Exception{
	this();
	NodeList domPList = domNode.getChildNodes();
	ProductionT3 tmpP;	
	for(int i = 0 ; i < domPList.getLength() ; i++)
	    if(domPList.item(i).getNodeType() == Node.ELEMENT_NODE){
		add(new ProductionT3(domPList.item(i)));
	    }
    }	

    /** 
     *
     * Esta es la función que debe de implementar cada una de los
     * tipos de conjuntos de  producción dependiendo el tipo de gramatica que se
     * quiere implementar .
     *
     * Las producciones para gramáticas regulares o tipo 3 se
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
     * */
    public boolean validate(Alphabet N, Alphabet T, Symbol S) throws ProductionNotValidTypeException{
	Object oarray [] = toArray();
	ProductionT3 foo = new ProductionT3(new Str(S,false),new Str());
	boolean containsSToEpsilonProduction = this.contains(foo);
	int productionType;
	boolean leftL = false, rightL=false;
	for(int i = 0; i < oarray.length; i ++){	    
	    if((productionType=((ProductionT3)oarray[i]).getTipoConsecuente(N,T))==ProductionT3.LINEAL_IZQUIERDA)
		leftL = true;
	    else if(productionType==ProductionT3.LINEAL_DERECHA)
		rightL= true;
  	    if(!((ProductionT3)oarray[i]).validate(N,T,S,containsSToEpsilonProduction))
  		return false;
	}
	if(leftL && rightL)
	    throw
		new ProductionNotValidTypeException("¡¡La gramática no es tipo 3, pues tiene producciones lineales izquierdas y lineales derechas!!");
	if(leftL)
	    setLinealidad(L_IZQUIERDA);
	else if(rightL)
	    setLinealidad(L_DERECHA);	
	return true;
    }

    public boolean contains(Object o){
	Object [] oarray = this.toArray();
	for (int i = 0; i < oarray.length; i++)
	    if(((ProductionT3)oarray[i]).equals(o))
		return true;
	return false;
    }
    
    public boolean add(Object o){
	if(!(o instanceof ProductionT3))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden agregar Producciones Tipo 3 a este conjunto, ["+o+", "+ o.getClass().getName()+"]  AGREGADO!!");
	return super.add(o);
    }
    
    public boolean remove(Object o) {
	if(!(o instanceof ProductionT3))
	    Debug.println("dfa.structures.ProductionSet WARNING: Solo se pueden eliminar  ProductionT3 de este conjunto, ["+o+", "+ o.getClass().getName()+"]  ELIMINANDO!!");
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
	if (o instanceof ProductionT3Set
	    && super.equals(o))
	    return true;
	return false;
    }

    /**
     * Regresa un subconjunto de esta instancia que contiene a todas las <code>ProductionT3</code> cuyo primer símbolo es el símbolo <code>s</code>
     * sin importar el antecedente
     *@param s el símbolo con el que queremos que comiencen las producciones
     */
    public ProductionT3Set getProductionsStartWith(Symbol s){
	return getProductionsStartWith(null,s);
    }


    /**
     * Regresa un subconjunto de esta instancia que contiene a todas las <code>ProductionT3</code> cuyo primer símbolo es el símbolo <code>s</code>
     * y cuyo antecedente es el símbolo <code>ant</ant>
     *@param s el símbolo con el que queremos que comiencen las producciones
     *@param ant la cadena igual al antecedente
     */
    public ProductionT3Set getProductionsStartWith(Str ant, Symbol s){
	ProductionT3Set result = new  ProductionT3Set();
	Object Oarray[] = toArray();
	ProductionT3 currentp;
	Str currentCons;
	for ( int i = 0 ; i < Oarray.length ; i++){
	    currentp = (ProductionT3)Oarray[i];
	    if(ant == null || currentp.getAntecedente().equals(ant)){
		currentCons = currentp.getConsecuente();
		if((!currentCons.isEpsilon()) && currentCons.getFirst().equals(s))
		    result.add(currentp);
	    }
	}
	return result;
    }
}

/* ProductionSet.java ends here. */
