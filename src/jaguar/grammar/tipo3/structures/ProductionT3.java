/**
** <ProductionT3.java> -- The structure to represent productions for type 3 grammars
** 
** Copyright (C) 2002 by  Ivan Hern�ndez Serrano
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
** Author: Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
** 
**/


package jaguar.grammar.tipo3.structures;

import jaguar.util.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import java.io.*;


/** 
 * Las producciones para gramaticas de Tipo 3, como todas las especializaciones de gram'aticas implementa el m�todo <code>validate</code>  que una vez leida la gramatica que la producci�n sea una de tipo 3 v�lida
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class ProductionT3 extends Production{

    /**
     * La longitud del antecedente
     **/
    public static final int ANTECEDENTE_LENGTH = 1;
    /**
     * La longitud m�nima del consecuente
     **/
    public static final int CONSECUENTE_MIN_LENGTH = 0;
    /**
     * La longitud m�xima del consecuente
     **/
    public static final int CONSECUENTE_MAX_LENGTH = 2;
    /**
     * Variable para la identificar producci�n  l�neal izquierda <code>A --> Ba </code>
     */
    public static final int LINEAL_IZQUIERDA = 3;
    /**
     * Variable para la identificar producci�n  l�neal derecha <code>A --> aB </code>
     */
    public static final int LINEAL_DERECHA = 4;
    /**
     * Variable para la identificar una producci�n de la forma "a terminal" <code>A --> a </code>
     */
    public static final int A_TERMINAL = 5;
    /**
     * Variable para la identificar una producci�n que no es de tipo 3
     */
    public static final int A_EPSILON = 6;
    /**
     * Variable para la identificar una producci�n que no es de tipo 3
     */
    public static final int NO_TIPO3 = 7;

  /**
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     */
    public ProductionT3 (){
	super();	
    }
    /**
     * Constructor.
     * Recibe los valores para antecedente  y consecuente.
     * Para el resto de los campos usa el valor por omision.
     * @param _antecedente el valor con el que se inicalizar� el campo antecedente
     * @param _consecuente el valor con el que se inicalizar� el campo consecuente
     */
    public ProductionT3 (Str _antecedente, Str _consecuente){
	super(_antecedente, _consecuente);	
    }
    /**
     ** Construye un ProductionT3 dado el documento DOM
     **/
    public ProductionT3(org.w3c.dom.Node domNode)throws Exception{
	
	super(domNode);
    }	
    
    /** 
     * Indica si otro <code>Object</code> es "igual" a este. 
     * <br>
     * El m�todo <code>equals</code> implementa una relaci�n de equivalencia: 
     *    <ul>
     *    <li> Es reflexiva: para toda referencia x, x.equals(x) debe regresar <code>true</code></li>
     *    <li> Es sim�trica: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     *    <code>x.equals(y)</code> debe regresar <code>true</code> si y solo si
     *    <code>y.equals(x)</code> regresa <code>true</code></li>
     *    <li> Es transitiva: Para cualesquiera tres referencias <code>x,y,z</code> si
     *    <code>x.equals</code> regresa <code>true</code> y <code>y.equals(z)</code>
     *    regresa <code>true</code>, entonces <code>x.equals(z)</code> debe regresar <code>true</code>.</li>
     *    <li> Es consistente: para cualesquiera dos referencias <code>x</code> y <code>y</code>,
     *    m�ltiples llamadas de <code>x.equals(y)</code> deben consistentemente regresar <code>true</code> o
     *    consistentemente regresar <code>false</code>, siempre y cuando no cambie en el objeto la informaci�n usada
     *    en las comparaciones de <code>equals</code> .</li>
     *    <li> Para toda referencia no nula <code>x</code>,<code>x.equals(null)</code> debe regresar <code>false</code>.</li> 
     *    </ul>
     * 
     *    El m�todo <code>equals</code> para la clase ProductionT3 es implementado mediante el uso de == para los campos de tipos b�sicos
     *    y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     *    @param o el objeto con el que voy a comparar a este.
     * 
     *    @return <code>true<code> si este objeto es igual a <code>o</code>.
     *    @see java.lang.Object#equals
     */ 
    public boolean equals(Object o){
	if (o instanceof ProductionT3)
	    return super.equals(o);
// 	else{
// 	    Debug.println("\t\t\t\tProductionT3.equals =>>>>>>>>>"+ o + " NO es instancia de ProductionT3");
// 	}
	return false;
    }

    /** 
     *
     * Esta es la funci�n que debe de implementar cada una de los
     * tipos de producci�n dependiendo el tipo de gramatica que se
     * quiere implementar .
     *
     * Las producciones para gram�ticas regulares o tipo 3 se
     * caracterizan por tener del lado derecho a lo m�s 2 s�mbolos,
     * uno terminal y otro no terminal. <p>
     *
     * Si la producci�n es de la forma<br>
     *    <code>A  --> aB</code> o  <code>A  --> a</code> con <code>A,B</code> no terminales y <code>a</code> terminal<br>
     * se trata de una  producci�n <em>lineal a la derecha</em> y si es de la forma <br>
     *    <code>A  --> Ba</code> o  <code>A  --> a</code> <br>
     * se trata de una producci�n <em>lineal a la  izquierda</em> 
     *
     * @param N el alfabeto de s�mbolos no terminales
     * @param T el alfabeto de s�mbolos terminales
     * @param S el s�mbolo inicial 
     * @return Regresa verdadero si esta bien "creada" la produccion con respecto al tipo de gramatica que se esta describiendo.
     * */
    public boolean validate(Alphabet N, Alphabet T, Symbol S, boolean containsSToEpsilonProduction)
	throws ProductionNotValidTypeException{


	if(containsSToEpsilonProduction && consecuente.containsSymbol(S))
	    throw new ProductionNotValidTypeException("La producci�n S --> <epsilon> est� presente en \n\t\tel conjunto de produccionies y la producci�n ["+toString()+"] tambi�n, \n\t\tpero S no puede estar del lado derecho de ninguna producci�n");

	if(antecedente.length() == ANTECEDENTE_LENGTH && antecedente.isOnAlphabet(N) &&
	   CONSECUENTE_MIN_LENGTH <= consecuente.length() && consecuente.length() <= CONSECUENTE_MAX_LENGTH){
	    int tipoConsecuente = getTipoConsecuente(N,T);
	    if(tipoConsecuente == LINEAL_IZQUIERDA ||
	       tipoConsecuente == LINEAL_DERECHA ||
	       tipoConsecuente == A_TERMINAL){		
		return true;
	    }else if(tipoConsecuente == A_EPSILON){
		if(antecedente.equals(new Str(S,false))){
		    return true;		   
		}else{
		    throw new ProductionNotValidTypeException("Tenemos la producci�n ["+toString()+"] en las tipo 3 solo podemos tener S--><epsilon> y si est� presente no puede estar del lado derecho de ninguna producci�n");
		}
	    }
	} throw new ProductionNotValidTypeException("La producci�n ["+toString()+"] no es tipo 3. \n\t\tno es ninguna de las siguientes: lineal izq, lineal derecha. ni a un s�mbolo  terminal ");
    }

    /**
     * Una vez que ya sabemos que la producci�n es de la forma <code>A -> x </code> con <code> 1 <= |x| <= 2  </code>
     * esta funci�n decide si l�neal  derecha, l�neal  izquierda, de la forma <code>A -> a </code> o si no es de tipo 3.
     * @param N el alfabeto de s�mbolos no terminales
     * @param T el alfabeto de s�mbolos terminales
     * @return Regresa uno de los siguientes valores<br>
     *              #see LINEAL_IZQUIERDA
     *              #see LINEAL_DERECHA
     *              #see A_TERMINAL
     *              #see NO_TIPO3
     **/  
    public int getTipoConsecuente(Alphabet N, Alphabet T){
	if(consecuente.length() == CONSECUENTE_MIN_LENGTH)
	    return A_EPSILON;
	if(consecuente.length() == 1){
	    if(consecuente.isOnAlphabet(T))
		return A_TERMINAL;		
	    else return NO_TIPO3;
	}else if(consecuente.length() == CONSECUENTE_MAX_LENGTH){
	    Symbol S1=null, S2=null;
	    try{ 
		S1 = consecuente.getSymbol(0);
		S2 = consecuente.getSymbol(1);
	    }catch(StrIndexOutOfBoundsException ouch){
		System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				   + " StrIndexOutOfBoundsException: " ); 
		ouch.printStackTrace(); 
	    }
	    if(T.contains(S1) && N.contains(S2))
		return LINEAL_DERECHA;
	    else if(N.contains(S1) && T.contains(S2))
		return LINEAL_IZQUIERDA;
	}
	return NO_TIPO3;	
    }
}

/* ProductionT3.java ends here. */
