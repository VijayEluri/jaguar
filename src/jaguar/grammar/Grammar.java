/**
 * <Grammar.java> -- The grammar super class, defines all the grammar features
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
 */

package jaguar.grammar;

import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.exceptions.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.tipo2.structures.ProductionT2Set;
import jaguar.grammar.tipo3.structures.ProductionT3Set;
import jaguar.grammar.structures.exceptions.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/** 
 * Esta es la clase generica de gramática, cada uno de los tipos de gramatica va
 * a especializar esta gramática.
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:02 $
 */
abstract public class Grammar {
    
    /**
     * El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     * El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "gram";

    
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
     * El nombre del elemento descripción, debe de ser igual al descrito en las DTDs de máquinas
     **/
    public static final String DESCRIPTION_ELEMENT_NAME = "description";

    
    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String DESCRIPTION_BEG_TAG = "<"+DESCRIPTION_ELEMENT_NAME+">";


    /**
     * El tag con el que se define el fin del objeto de un 
     * en un archivo
     */
    public static final String DESCRIPTION_END_TAG = "</"+DESCRIPTION_ELEMENT_NAME+">";


    /**
     * Para que tengan la capacidad de leer checando la entrada con el DTD
     */
    protected DocumentBuilderFactory factory;


    /**
     * Una descripción de la gramática
     */
    protected String grammarDescription = " ";


    /**
     * funcion de acceso para obtener el valor de grammardescription
     * @return el valor actual de grammardescription
     * @see #grammarDescription
     */
    public String getGrammarDescription(){
	return grammarDescription;
    }
    
    
    /**
     * funcion de acceso para modificar grammardescription
     * @param new_grammardescription el nuevo valor para grammardescription
     * @see #grammarDescription
     */
    public void setGrammarDescription(String new_grammardescription){
	grammarDescription=new_grammardescription;
    }
    
    
    /**
     * El símbolo inicial de lal gramatica
     */
    protected Symbol S;

    
    /**
     * funcion de acceso para obtener el valor de S
     * @return el valor actual de S
     * @see #S
     */
    public Symbol getS(){
	return S;
    }

    
    /**
     * funcion de acceso para modificar S
     * @param new_S el nuevo valor para S
     * @see #S
     */
    public void setS(Symbol new_S){
	S = new_S;
    }
    
    
    /**
     * El conjunto de producciones
     */
    protected ProductionSet P;


    /**
     * funcion de acceso para obtener el valor de P
     * @return el valor actual de P
     * @see #P
     */
    public ProductionSet getP(){
	return P;
    }

    
    /**
     * funcion de acceso para modificar P
     * @param new_P el nuevo valor para P
     * @see #P
     */
    public void setP(ProductionSet new_P){
	P = new_P;
    }

    
    /**
     * El conjunton de símbolos terminales de la gramatica
     */
    protected Alphabet T;
    
    
    /**
     * funcion de acceso para obtener el valor de T
     * @return el valor actual de T
     * @see #T
     */
    public Alphabet getT(){
	return T;
    }

    
    /**
     * funcion de acceso para modificar T
     * @param new_T el nuevo valor para T
     * @see #T
     */
    public void setT(Alphabet new_T){
	T=new_T;
    }

    
    /**
     * El conjunto de No terminales de la gramatica
     */
    protected Alphabet N;
    /**
     * funcion de acceso para obtener el valor de N
     * @return el valor actual de N
     * @see #N
     */
    public Alphabet getN(){
	return N;
    }
    /**
     * funcion de acceso para modificar N
     * @param new_N el nuevo valor para N
     * @see #N
     */

    public void setN(Alphabet new_N){
	N=new_N;
    }

    protected Grammar(){
	super();	
	N = new Alphabet();
	T = new Alphabet();
    }
    
    /**
     * Constructor.
     * Recibe los valores para S, P, T y N.
     * Para el resto de los campos usa el valor por omision.
     * @param _S el valor con el que se inicalizará el campo S
     * @param _P el valor con el que se inicalizará el campo P
     * @param _T el valor con el que se inicalizará el campo T
     * @param _N el valor con el que se inicalizará el campo N
     * @see #S
     * @see #P
     * @see #T
     * @see #N
     */
    public Grammar (Symbol _S, ProductionSet _P, Alphabet _T, Alphabet _N){
	S=_S;
	P=_P;
	T=_T;
	N=_N;
    }
    /**
     * Constructora que construye una <code>Grammar</code> a partir del nombre de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Grammar(String filename)throws Exception{
	this(new File(filename));
    }

    /** 
     * Constructora que construye un <code>Grammar</code> a partir del <code>File</code> de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Grammar(File file)throws Exception{
	this();
	factory = DocumentBuilderFactory.newInstance();
	factory.setValidating(true);
	setupGrammar(factory.newDocumentBuilder().parse(file),this);
    }

    /**
     * Constructora que construye un <code>Grammar</code> a partir del nombre de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Grammar(org.w3c.dom.Document document)throws Exception{
	this();
	setupGrammar(document,this);
    }

    /**
     * Configura todos los campos de la <code>Grammar</code> dado a partir del documento valido que le pasamos
     * @param document es un documento DOM que cumple con la especificación DTD para las Grammar 
     * @param r el Grammar que configuramos
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public void setupGrammar(org.w3c.dom.Document document,Grammar r)throws Exception{
	NodeList elementsList = document.getElementsByTagName(ELEMENT_NAME).item(0).getChildNodes();	
	int howManyElements = 5;
	int j = (document.getElementsByTagName(DESCRIPTION_ELEMENT_NAME).getLength()==0)?1:0;
	Node grammarNode;
	for(int i =  0; j < howManyElements; i++){	   
	    grammarNode = elementsList.item(i);
	    if(grammarNode.getNodeType() == Node.ELEMENT_NODE){		
		switch(j){
		case 0: r.setGrammarDescription(grammarNode.getChildNodes().item(0).getNodeValue()); break;		    
		case 1: r.setN(new Alphabet(grammarNode)); break;// N
		case 2: r.setT(new Alphabet(grammarNode)); break;//T
		case 3: {
		    if(r.getP() instanceof ProductionT2Set){
			r.setP(new ProductionT2Set(grammarNode));
		    }else if(r.getP() instanceof ProductionT3Set){			
			r.setP(new ProductionT3Set(grammarNode));
		    }else{
			System.err.println("Neither ProductionT2Set or ProductionT3Set");
		    }		    
		}break;//P
		case 4: r.setS(new Symbol(grammarNode));break;//S
		}		
		j++;
	    }
	}
    }
    


    /**
     ** Esta la debe de implementar cada una de las gramáticas dependiendo del tipo que sean
     **/
    abstract public boolean validate()  throws ProductionNotValidTypeException;    
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
     *    El método <code>equals</code> para la clase Grammar es implementado mediante el uso de == para los campos de tipos básicos
     *    y llamadas a <code>equals</code> para los campos que son objetos.<br>
     * 
     *    @param otro el objeto con el que voy a comparar a este.
     * 
     *    @return <code>true<code> si este objeto es igual a <code>otro</code>.
     *    @see java.lang.Object#equals
     */ 
    public boolean equals(Object otro){
	if (otro instanceof Grammar && getN().equals(((Grammar)(otro)).getN())
	    && getT().equals(((Grammar)(otro)).getT())
	    && getP().equals(((Grammar)(otro)).getP())
	    && getS().equals(((Grammar)(otro)).getS()))
	    return true;
	return false;
    }


    /** 
     * Reemplaza la aparición de   <code>viejo</code> por <code>nuevo</code> en esta instancia de  <code>Grammar</code>
     *
     * Reemplaza en esta  instancia de <code>Grammar</code>  la aparición de el símbolo <code>viejo</code> 
     * por el símbolo <code>nuevo</code>.
     *
     * @param viejo El símbolo que vamos a sustituir.
     * @param nuevo El nuevo símbolo que aparecerá en lugar de  <code>viejo</code>.
     *
     */
    public void replaceSymbol(Symbol viejo, Symbol nuevo){
	Object pA[] = getP().toArray();
	for(int i = 0 ; i < pA.length ; i++){
	    ((Production)pA[i]).setAntecedente(((Production)pA[i]).getAntecedente().replaceSymbol(viejo,nuevo));
	    ((Production)pA[i]).setConsecuente(((Production)pA[i]).getConsecuente().replaceSymbol(viejo,nuevo));
	}
	getN().replaceSymbol(viejo,nuevo);
	getT().replaceSymbol(viejo,nuevo);
	if(S.equals(viejo))
	    S = nuevo;	
    }

    /** 
     * Guarda la representación de la Gramática en un archivo con el formato del DTD correspondiente
     * Escribe la gramática con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el Grammar.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public void toFile(FileWriter fw){
	try{ 
	    fw.write("<?xml version='1.0' encoding=\"iso-8859-1\" ?>"+"\n");
	    fw.write("<!DOCTYPE gram SYSTEM \"grammar.dtd\">"+"\n");	    
	    if(grammarDescription.trim().length() > 0){
		fw.write(DESCRIPTION_BEG_TAG);
		fw.write(getGrammarDescription());
		fw.write(DESCRIPTION_END_TAG+"\n");
	    }
	    fw.write(BEG_TAG);
	    fw.write("\n\n <!-- Alphabet N --> \n");
	    getN().toFile(fw);
	    fw.write("\n\n <!-- Alphabet T --> \n");
	    getT().toFile(fw);
	    fw.write("\n\n <!-- Producciones --> \n");
	    getP().toFile(fw);
	    fw.write("\n\n <!-- Símbolo  inicial S --> \n");
	    getS().toFile(fw);
	    fw.write("\n"+ END_TAG);
	    fw.flush();
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }


}

/* Grammar.java ends here. */
