/**
** <ProductionT2.java> -- The production to type 2 grammars
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

import jaguar.util.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import java.io.*;


/** 
 * Las producciones para gramaticas de Tipo 2, como todas las especializaciones de gram'aticas implementa el método <code>validate</code>  que una vez leida la gramatica que la producción sea una de tipo 2 válida
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class ProductionT2 extends Production{

    /**
     * La longitud del antecedente
     **/
    public static final int ANTECEDENTE_LENGTH = 1;
    /**
     * La longitud mínima del consecuente
     **/
    public static final int CONSECUENTE_MIN_LENGTH = 0;
    /**
     * La longitud máxima del consecuente
     **/
    /**
     * Variable para la identificar producción  líneal izquierda <code>A --> Ba </code>
     */
    public static final int LINEAL_IZQUIERDA = 3;
    /**
     * Variable para la identificar producción  líneal derecha <code>A --> aB </code>
     */
    public static final int LINEAL_DERECHA = 4;
    /**
     * Variable para la identificar una producción de la forma "a terminal" <code>A --> a </code>
     */
    public static final int A_TERMINAL = 5;
    /**
     * Variable para la identificar una producción que no es de tipo 2
     */
    public static final int A_EPSILON = 6;
    /**
     * Variable para la identificar una producción que no es de tipo 2
     */
    public static final int NO_TIPO2 = 7;

    /**
     * Variable para la identificar una cadena que esta sobre (N U T)^{*}
     */
    public static final int N_UNION_T_PLUS = 8;
    
  /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public ProductionT2 (){
	super();	
    }
    /**
     * Constructor.
     * Recibe los valores para antecedente  y consecuente.
     * Para el resto de los campos usa el valor por omision.
     * @param _antecedente el valor con el que se inicalizará el campo antecedente
     * @param _consecuente el valor con el que se inicalizará el campo consecuente
     */
    public ProductionT2 (Str _antecedente, Str _consecuente){
	super(_antecedente, _consecuente);	
    }
    /**
     ** Construye un ProductionT2 dado el documento DOM
     **/
    public ProductionT2(org.w3c.dom.Node domNode)throws Exception{
	super(domNode);
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
     *    El método <code>equals</code> para la clase ProductionT2 es implementado mediante el uso de == para los campos de tipos básicos
     *    y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     *    @param o el objeto con el que voy a comparar a este.
     * 
     *    @return <code>true<code> si este objeto es igual a <code>o</code>.
     *    @see java.lang.Object#equals
     */ 
    public boolean equals(Object o){
	if (o instanceof ProductionT2)
	    return super.equals(o);
	return false;
    }

    /** 
     *
     * Esta es la función que debe de implementar cada una de los
     * tipos de producción dependiendo el tipo de gramatica que se
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
     * @return Regresa verdadero si esta bien "creada" la produccion con respecto al tipo de gramatica que se esta describiendo.
     * */
    public boolean validate(Alphabet N, Alphabet T, Symbol S, boolean containsSToEpsilonProduction)
 	throws ProductionNotValidTypeException{
	validateLazy(N,T,S);
	int tipoConsecuente = getTipoConsecuente(N,T);
 	if(tipoConsecuente == A_EPSILON && !antecedente.equals(new Str(S)))
 	    throw new ProductionNotValidTypeException("La producción ["+toString()+"] no es tipo 2. \n\t\tSi el consecuente es <epsilon> el antecedente debe de ser S");
	return true;
    }
    
    /*
    * Hace un chequeo flojo, básicamente solo checa que el antecedente sea de longitud 1 y que el consecuente esté en (N U T)^{*}
    * @param N el alfabeto de símbolos no terminales
    * @param T el alfabeto de símbolos terminales
    * @param S el símbolo inicial 
    * @return Regresa verdadero si esta bien "creada" la produccion con respecto al tipo de gramatica que se esta describiendo.
     * */
    public boolean validateLazy(Alphabet N, Alphabet T, Symbol S)
	throws ProductionNotValidTypeException{
	if(antecedente.length() != 1)   
	    throw new ProductionNotValidTypeException("La producción ["+toString()+"] no es tipo 2. \n\t\tEl antecedente no es de longitud 1");
	int tipoConsecuente = getTipoConsecuente(N,T);
	if(tipoConsecuente == A_EPSILON || tipoConsecuente == N_UNION_T_PLUS)
	    return true;
	throw new ProductionNotValidTypeException("La producción ["+toString()+"] no es tipo 2. \n\t\tEl consecuente no está en (N U T)^{*}");	
    }

    
    
    /**
     * Una vez que ya sabemos que la producción es de la forma <code>A -> x </code> con <code> 1 <= |x| <= 2  </code>
     * esta función decide si líneal  derecha, líneal  izquierda, de la forma <code>A -> a </code> o si no es de tipo 2.
     * @param N el alfabeto de símbolos no terminales
     * @param T el alfabeto de símbolos terminales
     * @return Regresa uno de los siguientes valores<br>
     *              #see LINEAL_IZQUIERDA
     *              #see LINEAL_DERECHA
     *              #see A_TERMINAL
     *              #see NO_TIPO2
     **/  
    public int getTipoConsecuente(Alphabet N, Alphabet T){
//	Debug.println("\nla longitud del consecuente ["+consecuente+"] es >>" + consecuente.length()+"<< ]]"+ consecuente.getSeq().size()+"[[");
	if(consecuente.length() == CONSECUENTE_MIN_LENGTH)
	    return A_EPSILON;
//	Debug.println("\n N.union(T) = ["+N.union(T)+"] \t consecuente = ["+consecuente+"]\n");
	if(N.union(T).strOnAlphabet(consecuente))
	    return N_UNION_T_PLUS;
	return NO_TIPO2;	
    }
}

/* ProductionT2.java ends here. */
