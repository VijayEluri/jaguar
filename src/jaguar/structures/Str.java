/**
** <Str.java> -- To use strings
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


package jaguar.structures;
/**
 *
 * @author <a href="mailto: ivanx@users.sourceforge.net">Ivan Hernández Serrano</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:05 $
 */

import java.util.*;

import jaguar.util.*;
import java.io.File;
import java.io.FileWriter;
import jaguar.structures.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class Str implements Cloneable {
    /**
     * Estructura para manejar la secuencia de <code>Symbol</code>s
     */
    private LinkedList symSeq = new LinkedList();
    /**
     * <code>Alphabet</code> sobre debe de estar definida la cadena
     * @see dfa.structures.Alphabet
     */
    private Alphabet Sigma;
    /**
     * Bandera que indica si debemos de mostrar los tags a la hora de imprimir
     **/
    protected boolean show_tags = true;
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "str";
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
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String EPSILON_ELEMENT_NAME = "epsilon";
    /**
     * El tag con el que se define una cadena vacia en un archivo 
     */
    public static final String EPSILON_TAG = "<"+EPSILON_ELEMENT_NAME+"/>";    

    public static final boolean WITH_TAGS = false;
    public static final boolean WITHOUT_TAGS = false;
    /**
     ** Para que tengan la capacidad de leer checando la entrada con el DTD
     **/
    protected DocumentBuilderFactory factory;
    
    /**
     * Crea una cadena de longitud cero. Por omisión no muestra los
     * tags, es equivalente a usar <code>Str(false)</code>
     * @see #Str(boolean)
     */
    public Str (){
	this(false);	
    }

    /**
     * Crea un cadena default longitud cero definiendo si deben default mostrarse los tags default inicio y final
     * al usar el método <code>toString</code>.
     * @param tags, <code>true</code> se mostraran los tags, <code>false</code>  no se mostrarán los tags
     * @see #toString
     */
    public Str (boolean tags){	
	symSeq = new LinkedList();
	Sigma = new Alphabet();
	show_tags = tags;
	factory = DocumentBuilderFactory.newInstance();
	factory.setValidating(true);	
    }

    /**
     * Construye una cadena a partir default un símbolo, i.e. una cadena default longitud 1, por omisión
     * mostrará los tags. Es equivalente a usar <code>Str(s,true)</code> 
     * @param s, la cadena de longitud uno
     * @see #Str(Symbol,boolean)
     */
    public Str (Symbol s){
	this(s,true);
    }

    /**
     * Construye una cadena a partir default un símbolo, i.e. una cadena default longitud 1, por omisión
     * mostrará los tags. Es equivalente a usar <code>Str(s,true)</code> 
     * @param s, la cadena de longitud uno
     * @param tags <code>true</code> - mostrará los tags, <code>false</code> no mostrará los tags
     */
    public Str (Symbol s, boolean tags){
	symSeq = new LinkedList();
	Sigma = new Alphabet();
	symSeq.add(s);
	length();
	show_tags = tags;
    }    
    /**
     *  Construye una cadena a partir default <code>Str aStr</code> y checa este definida sobre else <code>Alphabet _Sigma</code>
     *  Por omisión se mostrarán los tags de cadena, es equivalente a usar <code>Str(aStr,_Sigma,true)</code>
     *  @param aStr la cadena a partir de la cual se creará esta instancia
     * @param _Sigma alfabeto sobre el cual deberá estar definida esta instancia
     * @see #Str(Str,Alphabet,boolean)
     */
    public Str (Str aStr, Alphabet _Sigma){
 	this(aStr,_Sigma,true);
    }

    /**
     *  Construye una cadena a partir default <code>Str aStr</code> y checa este definida sobre else <code>Alphabet _Sigma</code>
     *  Por omisión se mostrarán los tags de cadena, es equivalente a usar <code>Str(aStr,_Sigma,true)</code>
     *  @param aStr la cadena a partir de la cual se creará esta instancia
     * @param _Sigma alfabeto sobre el cual deberá estar definida esta instancia
     * @param tags <code>true</code> mostrará los tags de Str. <code>false</code> no los mostrará
     */
    public Str (Str aStr, Alphabet _Sigma, boolean tags){
 	this();
	try{	    
	    symSeq = (LinkedList)aStr.getSeq().clone();
	    Sigma = (Alphabet)_Sigma.clone();
	    show_tags = tags;
	}catch(Exception e){
	    e.printStackTrace();
	}
    }

    public Str(Vector vSymbols){
	this(vSymbols,new Alphabet());
    }
    /**
     *  Construye una cadena a partir de la secuencia de símbolos definida en el arrego de simbolos
     * <code>Symbol nuevoStr[]</code> y checa este definida sobre else <code>Alphabet _Sigma</code>
     *  Por omisión se mostrarán los tags de cadena, es equivalente a usar <code>Str(nuevoStr,_Sigma,true)</code>
     *  @param nuevoStr[]  la secuencia de símbolos a partir de la cual se creará esta instancia
     * @param _Sigma alfabeto sobre el cual deberá estar definida esta instancia
     */
    public Str(Vector vSymbols, Alphabet _Sigma){
	this();		    
	try{
	    for(int i =0; i<vSymbols.size() ; i++){
		if(vSymbols.elementAt(i) instanceof Symbol)
		    symSeq.add(vSymbols.elementAt(i));		
		else{
		    Debug.println("Str(Vector,Alphabet): el vector tiene instancias distitas de Symbol");
		    return;		    
		}
	    }
	    Sigma = (Alphabet)_Sigma.clone();
	}catch(Exception e){
	    e.printStackTrace();	  
	}	
    }
    
    public Str(Vector vSymbols, Alphabet _Sigma, boolean show_tags){
	this(vSymbols,_Sigma);
	this.show_tags = show_tags;
	for(int i = 0; i < symSeq.size(); i++)
	    ((Symbol)symSeq.get(i)).setShowTags(show_tags);
    }

    
    /**
     *  Construye una cadena a partir de la secuencia de símbolos definida en el arrego de simbolos
     * <code>Symbol nuevoStr[]</code> y checa este definida sobre else <code>Alphabet _Sigma</code>
     *  Por omisión se mostrarán los tags de cadena, es equivalente a usar <code>Str(nuevoStr,_Sigma,true)</code>
     *  @param nuevoStr[]  la secuencia de símbolos a partir de la cual se creará esta instancia
     * @param _Sigma alfabeto sobre el cual deberá estar definida esta instancia
     */
    public Str(Symbol nuevoStr[], Alphabet _Sigma){
	this();		    
	try{
	    for(int i =0; i<nuevoStr.length ; i++)
		symSeq.add(nuevoStr[i]);
	    Sigma = (Alphabet)_Sigma.clone();
	}catch(Exception e){
	    e.printStackTrace();	  
	}	
    }

    
    /**
     *  Construye una cadena a partir de la secuencia de símbolos definida en el arrego de simbolos
     * <code>Symbol nuevoStr[]</code> y checa este definida sobre else <code>Alphabet _Sigma</code>
     *  @param nuevoStr[]  la secuencia de símbolos a partir de la cual se creará esta instancia
     * @param _Sigma alfabeto sobre el cual deberá estar definida esta instancia
     * @param tags <code>true</code> mostrará los tags de Str. <code>false</code> no los mostrará
     */
    public Str(Symbol nuevoStr[], Alphabet _Sigma, boolean tags){
	this( nuevoStr,  _Sigma);
	show_tags = tags;
	for(int i = 0; i < symSeq.size(); i++)
	    ((Symbol)symSeq.get(i)).setShowTags(show_tags);
    }

    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(String filename)throws Exception{
	this(new File(filename),false);
    }
    
    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(String filename, boolean tags)throws Exception{
	this(new File(filename),tags);
    }

    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(File file)throws Exception{
	this(file,false);
    }
    
    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(File file, boolean tags)throws Exception{
	this(tags);
	Document document = factory.newDocumentBuilder().parse(file);
	setupStr(document.getElementsByTagName("str").item(0),this);
    }


    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(org.w3c.dom.Node domNode,boolean tags)throws Exception{
	this(tags);
	setupStr(domNode,this);
    }

    /**
     ** Constructora que construye un Str a partir del nombre de un archivo que es valido segun el DTD de los Strs
     ** @see #DFA_DTD
     **/
    public Str(org.w3c.dom.Node domNode)throws Exception{
	this(domNode,true);
    }

    
    /**
     ** Construye un <code>Str</code> dado el documento DOM
     **/
    public void setupStr(org.w3c.dom.Node domNode, Str str)throws Exception{
	NodeList domSymList = domNode.getChildNodes();
	for(int i = 0; i < domSymList.getLength() ; i++)
	    if(domSymList.item(i).getNodeType() == Node.ELEMENT_NODE)
		if(domSymList.item(i).getNodeName().equals("epsilon")){
		    str = new Str();
		    return;		    
		}else str.append(new Symbol(domSymList.item(i)));
	    
    }
    
    /**
     * Regresa verdadero si la cadena es epsilon o falso de no serlo
     * @returns <code>true</code> si la cadena es de longitud cero, <code>false</code> en otro caso.
    */
    public boolean isEpsilon(){
	return (symSeq.size() == 0);
    }

    /**
     * Regresa verdadero si la cadena es epsilon o falso de no serlo
     * @returns <code>true</code> si la cadena es de longitud cero, <code>false</code> en otro caso.
    */
    public boolean getEpsilon(){
	return isEpsilon();
    }

    /**
     * Regresa una cadena que es la  concatenación de  la cadena <code>s1</code> con la cadena <code>s2</code>
     * @returns una nueva instancia de <code>Str</code> que es el resultado de concatenar <code>s1</code> con <code>s2</code>
     */
    static public Str concat(Str s1, Str s2){
	Str r  = new Str();
	r.getSeq().addAll(s1.getSeq());
	r.getSeq().addAll(s2.getSeq());
	return r;	
    }

    /**
     * Concatena la cadena actual con la cadena <code>s</code>, el resultado se quedá en está instancia
     */
    public void concat(Str s){
	symSeq.addAll(s.getSeq());
    }

    /**
     * Agrega el simbolo <code>s</code> al final de la cadena
     **/
    public void append(Symbol s){ symSeq.add(s);}

    /**
     * La reversa de esta cadena, la cadena original no se altera
     */
    public Str reverse(){
	Str tmp = null;
	LinkedList l = new LinkedList();
	try{ 
	    tmp = (Str)this.clone();
	}catch( CloneNotSupportedException ouch){	
	    ouch.printStackTrace(); 
	}
	int len = length();	
	for(int i = 0 ; i < len ; i++)
	    l.add(tmp.getSeq().removeLast());
	tmp.symSeq = l;
	return tmp;	
    }

    /**
     * La reversa de la cadena <code>s<|code>
     * @param s la cadena de la cual regresaremos la reversa
     * @return la reversa de la cadena <code>s<|code>
     */
    static Str reverse(Str s){
	Str tmp = null;
	LinkedList l = new LinkedList();
	try{ 
	    tmp = (Str)s.clone();
	}catch( CloneNotSupportedException ouch){	
	    ouch.printStackTrace(); 
	}
	int len = s.length();	
	for(int i = 0 ; i < len ; i++)
	    l.add(tmp.getSeq().removeLast());	
	tmp.symSeq = l;
	return tmp;	
    }

    

    /**
     * Crea y regresa una copia de este objeto
     * @returns crea y regresa la copia de este objeto
     */
    public Object clone() throws CloneNotSupportedException{
         try{
	     Str nuevo = (Str)super.clone();
	     nuevo.symSeq = (LinkedList)symSeq.clone();
	     return nuevo;
         }
         catch (CloneNotSupportedException e){
             throw new InternalError(e.toString());
         }
    }

    /**
     * Regresa la representación como cadena de este objeto
     * @returns la representación como cadena de este objeto según el valor de <code>show_tags</code> se agregarán, o no, los tags
     */
    public String toString() {
	String r="";
	if(isEpsilon()){
	    r = EPSILON_TAG;
	}
	else
	    for(int i = 0 ; i< symSeq.size() ; i++)
		r+= ((Symbol)symSeq.get(i));
	if(show_tags)
	    r = BEG_TAG + r + END_TAG;
	return  r;
    }

    /** 
     * Escribe la representación del <code>Str</code> en un archivo con el formato definido por el DTD correspondiente
     * Escribe el Str con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el Str.
     */
    public void toFile(FileWriter fw){
	try{ 
	    fw.write(BEG_TAG);
	    if(isEpsilon())
		fw.write(EPSILON_TAG);
	    else
		for(int i = 0 ; i< symSeq.size() ; i++)
		    ((Symbol)symSeq.get(i)).toFile(fw);
	    fw.write(END_TAG);
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
    

    public Str substring(int beginIndex, int endIndex){
	    try{	    
		Str r = (Str)this.clone();
		int origLen = r.length();	    
		for(int i = 0 ; i < beginIndex && i <origLen; i++)
		    r.removeFirst();		
		for(int i = 0 ; i< (origLen - endIndex)   && i < origLen; i++)
		    r.removeLast();
		return r;	
	    }catch(CloneNotSupportedException cnse){
		cnse.printStackTrace();	    
	    }
	return null;
    }

    public Str substring(int beginIndex){
	return substring(beginIndex,this.length());
    }
	
    /**
     * Elimina el primer caracter de la cadena actual
     */
    public void removeFirst(){
	symSeq.removeFirst();
    }

    /**
     * Elimina el primer caracter de la cadena actual
     */
    public void removeLast(){
	symSeq.removeLast();
    }
    /**
     * Regresa la longitud de la cadena actual
     */
    public int length(){
	return symSeq.size();	
    }

    /**
     *  Regresa la representación de la cadena actual como lista ligada
     *  @returns LinkedList  regresa la representación de la cadena actual como lista ligada que es la secuencia de los símbolos que la forman
     */
    public LinkedList getSeq(){
	return symSeq;
    }

    /**
     * Obtiene el primer símbolo de la  cadena actual
     * @returns el primer símbolo de la  cadena actual
     */
    public Symbol getFirst(){
	try{
	    return (Symbol) symSeq.getFirst();
	}catch(NoSuchElementException e){
	    return null;	    
	}
    }

    /**
     * Obtiene el último símbolo de la  cadena actual
     * @returns el último símbolo de la  cadena actual
     */
    public Symbol getLast(){
	try{
	    return (Symbol) symSeq.getLast();
	}catch(NoSuchElementException e){
	    return null;	    
	}
    }

    /**
     * Obtiene el símbolo en la posición especifícada
     * @param index índice del símbolo que se quiere obtener
     * @returns el símbolo en la posición especifícada
     * @throws StrIndexOutOfBoundsException si el índice especificado esta fuera del rang (index < 0 || index >= length()).
     */
    public Symbol getSymbol(int index) throws StrIndexOutOfBoundsException{
	if(index < 0 || index >= length())
	    throw new StrIndexOutOfBoundsException("El índice dado no está en el rango válido, index -->> "+index );
	return (Symbol) symSeq.get(index);
    }
    /**
     * Checa si la cadena esta sobre el alfabeto dado
     *
     * Checa si esta cadena se puede formar de símbolos del alfabeto dado
     * @param Alf El alfabeto donde checaremos si esta definida la cadena
     * @returns <code>true<code> si la cadena está sobre <code>Alf</code>. <code>false</code> e.o.c.
     */
    public boolean isOnAlphabet(Alphabet A){	
	try{ 
	    for(int i = 0 ; i <  length(); i++)
		if(! A.contains(getSymbol(i)))
		    return false;
	    return  true;	
	}catch( StrIndexOutOfBoundsException ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + " No cazan la longitud regresada por length() y el getSymbol(i) : " ); 
	    ouch.printStackTrace(); 
	    return false;	    
	}
    }
    
    public static void main(String argv[]){
	try{
	    Str str = new Str(new Symbol("a"));
	    str.concat(new Str(new Symbol("b")));
	    str.concat(new Str(new Symbol("c")));
	    System.out.println("\n" + str);
	    System.out.println("\nreverse" + str.reverse()+"\n");
	    System.out.println("\n" + str);
	}catch (Exception e){
	    e.printStackTrace();
	}
    }
    
    public boolean containsSymbol(Symbol s){
	return symSeq.contains(s);	
    }

    /**
     * Regresa verdadero si contiene  todos los símbolos del alfabeto A
     */
    public boolean containsSymbols(Alphabet A){
	Object [] oArray = A.toArray();
	for(int i = 0 ; i < oArray.length ; i++)
	    if(!containsSymbol((Symbol)oArray[i]))
		return false;
	return true;
    }

    public int hashCode(){
	String  s = "";
	for(int i = 0 ; i < length() ; i++)
	    s += symSeq.get(i);
	return s.hashCode();
    }

    public String stringToHashCode(){
	String  s = "";
	for(int i = 0 ; i < length() ; i++)
	    s += symSeq.get(i);
	return s;
    }
    
    public boolean equals(Object o){
	if(o instanceof Str){
	    if(symSeq.equals(((Str)o).getSeq())){
		return true;		
	    }
	}
	return false;	
    }

    public Alphabet getAlphabetMinimo(){
	Alphabet result = new Alphabet();
	for(int i = 0 ; i < length(); i++)
	    result.add((Symbol)symSeq.get(i));
	return result;
    }
    /** 
     * Regresa una nueva instancia de <code>Str</code> reemplazando todas las apariciones de  <code>viejo</code> por <code>nuevo</code> .
     *
     * Regresa una nueva instancia de <code>Str</code> con todas las apariciones de un símbolo <code>viejo</code> cambiadas
     * por un símbolo <code>nuevo</code>.
     *
     * @param viejo El símbolo que vamos a sustituir.
     * @param nuevo El nuevo símbolo que aparecerá en lugar de los símbolos viejos.
     * @return Regresa una nueva instancia con todos los simbolos cambiados como se específica antes.
     *
     */
    public Str replaceSymbol(Symbol viejo, Symbol nuevo){
	Str result = new Str();
	try{ 
	    for(int i = 0 ; i < length() ; i++){
		if(viejo.equals(getSymbol(i)))
		    result.append(nuevo);
		else result.append(getSymbol(i));
	    }
	}catch(StrIndexOutOfBoundsException ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + " Str.replaceSymbol("+viejo+","+nuevo+") se rompio, al parecer nos pasamos en el indice: " ); 
	    ouch.printStackTrace(); 
	}
	return result;
    }

}// Str
