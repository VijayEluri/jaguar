/**
** <Production.java> -- The structure to represent a production  
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


import jaguar.util.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.grammar.exceptions.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/** 
 * Representa las producciones de la grámatica, para cada una de las gramaticas  especializa estas producciones
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:02 $
 */
abstract public class Production implements Cloneable{
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "p";    
    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String BEG_TAG = "<"+ELEMENT_NAME+">";
    /**
     * El tag con el que se define el fin de un simbolo
     * en un archivo
     */
    public static final String END_TAG = "</"+ELEMENT_NAME+">";    

    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ANTECEDENTE_ELEMENT_NAME = "left";    
    /**
     * El tag con el que se define el inicio del objeto de un antecedente
     * en un archivo
     */
    public static final String ANTECEDENTE_BEG_TAG = "<"+ANTECEDENTE_ELEMENT_NAME+">";
    /**
     * El tag con el que se define el fin de un simbolo
     * en un archivo
     */
    public static final String ANTECEDENTE_END_TAG = "</"+ANTECEDENTE_ELEMENT_NAME+">";
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String CONSECUENTE_ELEMENT_NAME = "right";    
        /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String CONSECUENTE_BEG_TAG = "<"+CONSECUENTE_ELEMENT_NAME+">";
    /**
     * El tag con el que se define el fin de un simbolo
     * en un archivo
     */
    public static final String CONSECUENTE_END_TAG = "</"+CONSECUENTE_ELEMENT_NAME+">";
//    public static final String CONSECUENTE_END_TAG = "</der>";
    /**
     ** Para ver si ya leimos los tags 
     */
    public static final boolean TAGS_LEIDOS = true;
    /**
     ** Para ver si ya leimos los tags 
     */
    public static final boolean TAGS_NO_LEIDOS = false;
     /**
      * el antecedente de una producción
      */
    protected Str antecedente;
    /**
     * funcion de acceso para obtener el valor de antecedente
     * @return el valor actual de antecedente
     * @see #antecedente
     */
    public Str getAntecedente(){
	return antecedente;
    }
    /**
     * funcion de acceso para modificar antecedente
     * @param new_antecedente el nuevo valor para antecedente
     * @see #antecedente
     */
    public void setAntecedente(Str new_antecedente){
	antecedente=new_antecedente;
    }

    /**
     * El consecuente de la producción
     */
    protected Str consecuente;
    /**
     * funcion de acceso para obtener el valor de consecuente
     * @return el valor actual de consecuente
     * @see #consecuente
     */
    public Str getConsecuente(){
	return consecuente;
    }
    /**
     * funcion de acceso para modificar consecuente
     * @param new_consecuente el nuevo valor para consecuente
     * @see #consecuente
     */
    public void setConsecuente(Str new_consecuente){
	consecuente=new_consecuente;
    }
    /**
     * Constructor.
     * Recibe los valores para antecedente  y consecuente.
     * Para el resto de los campos usa el valor por omision.
     * @param _antecedente el valor con el que se inicalizará el campo antecedente
     * @param _consecuente el valor con el que se inicalizará el campo consecuente
     * @see #consecuente
     * @see #antecedente
     */
    public Production (Str _antecedente, Str _consecuente){
	antecedente = _antecedente;
	consecuente = _consecuente;	
    }
    protected Production(){
	this(new Str(), new Str());	
    }

    /**
     ** Construye un Production dado el documento DOM
     **/
    public Production(org.w3c.dom.Node domNode)throws Exception{
	this();	
	NodeList domPList = domNode.getChildNodes();
	NodeList secondList;
	
	int i = 0;
	boolean found = false;	

 	for(; !found && i < domPList.getLength() ; i++)
 	    if(domPList.item(i).getNodeType() == Node.ELEMENT_NODE){		
		secondList = domPList.item(i).getChildNodes();		
		for(int j = 0 ; !found && j < secondList.getLength(); j++){
		    if(secondList.item(j).getNodeType() == Node.ELEMENT_NODE){
			setAntecedente(new Str(secondList.item(j)));
			found=true;
		    }
		}
	    }
 	found = false;	
 	for(; !found && i < domPList.getLength() ; i++)
 	    if(domPList.item(i).getNodeType() == Node.ELEMENT_NODE){		
		secondList = domPList.item(i).getChildNodes();		
		for(int j = 0 ; !found && j < secondList.getLength(); j++){
		    if(secondList.item(j).getNodeType() == Node.ELEMENT_NODE){
			setConsecuente(new Str(secondList.item(j)));
			found=true;
		    }
		}
	    }
    }
    
    /** 
     * Guarda la representación de la Producción en un archivo con el formato definido por el DTD correspondiente
     * Escribe la máquina con su representación correspondiente con tags.
     *
     * @param FileName El nombre del archivo donde se guardará el DFA.
     */
    public void toFile(String FileName){
	toFile(FileName,"");	
    }
    
    /** 
     * Guarda la representación de la Producción en un archivo con el formato definido por el DTD correspondiente
     * Escribe la máquina con su representación correspondiente con tags y con un comentario al principio de la descripción.
     *
     * @param FileName El nombre del archivo donde se guardará el DFA.
     * @param comment Comentario que se escribirá al principio de la descripción de la Máquina.
     *
     */
    public void toFile(String FileName, String comment ){
	try{ 
	    toFile(new FileWriter(FileName),comment);	
	}catch(Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "El archivo " +  FileName + " no se pudo abrir: " ); 
	    ouch.printStackTrace(); 
	}
    }
    /** 
     * Guarda la representación de la Producción en un archivo con el formato definido por el DTD correspondiente
     * Escribe la máquina con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el DFA.
     * @param comment Comentario que se escribirá al principio de la descripción de la Máquina.
     */
    public void toFile(FileWriter fw, String comment){
	try{ 
	    fw.write(comment);
	    toFile(fw);	
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
    /** 
     * Guarda la representación de la Producción en un archivo con el formato definido por el DTD correspondiente
     * Escribe la máquina con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará la Máquina.
     */
    public void toFile(FileWriter fw){
	try{
	    fw.write("\n");	    
	    fw.write(BEG_TAG);
	    fw.write(" "+ANTECEDENTE_BEG_TAG+" ");
	    getAntecedente().toFile(fw);
	    fw.write(" "+ANTECEDENTE_END_TAG+" "+CONSECUENTE_BEG_TAG+" ");	    
	    getConsecuente().toFile(fw);	    
	    fw.write(" "+CONSECUENTE_END_TAG+" " + END_TAG);
	}catch(Exception e){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " );
	    e.printStackTrace();	    
	}
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
     *    El método <code>equals</code> para la clase Production es implementado mediante el uso de == para los campos de tipos básicos
     *    y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     *    @param o el objeto con el que voy a comparar a este.
     * 
     *    @return <code>true<code> si este objeto es igual a <code>otro</code>.
     *    @see java.lang.Object#equals
     */ 
    public boolean equals(Object o){
	if (o instanceof Production){
	    if( antecedente.equals(((Production)o).getAntecedente())
		&& consecuente.equals(((Production)o).getConsecuente())){
		return true;
	    }
	}
	return false;
    }
    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	return "" + getAntecedente() + " --> " + getConsecuente();
    }

    public int hashCode(){
	String s = "";
	s = getAntecedente().stringToHashCode() + getConsecuente().stringToHashCode();
	return s.hashCode();
    }
    /** 
     * Esta es la función que debe de implementar cada una de los tipos de producción dependiendo el tipo de gramatica que se quiere implementar .
     *
     * @param containsSToEpsilonProduction <code>true</code> si la producción <code>S -> <epsilon> </code> está presente en el conjunto de producciones, e.o.c. <code>false</code>
     * @return Regresa verdadero si esta bien "creada" la produccion con respecto al tipo de gramatica que se esta describiendo.
     *
     */
    abstract public boolean validate(Alphabet N, Alphabet T, Symbol S, boolean containsSToEpsilonProduction)
	throws ProductionNotValidTypeException;
    /**
     * Crea y regresa una copia de este objeto
     * @return la copia de este objeto
     */
    public Object clone() throws CloneNotSupportedException{
	try{
	    Production nueva = (Production)super.clone();
	    nueva.antecedente = (Str)antecedente.clone();
	    nueva.consecuente = (Str)consecuente.clone();
	    return nueva;
	}
	catch (CloneNotSupportedException e){
	    throw new InternalError(e.toString());
	}
    }

}

/* Production.java ends here. */
