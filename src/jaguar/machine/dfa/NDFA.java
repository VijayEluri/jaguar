/**
 ** <NDFA.java> -- The No Deterministic Finite Automata
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


package jaguar.machine.dfa;

import java.util.*;
import jaguar.machine.Machine;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.dfa.exceptions.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.structures.exceptions.*;
import jaguar.util.Debug;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/**
 * DFA.java
 *
 *
 * Created: Wed Dec 20 21:33:00 2000
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hern'andez</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:03 $
 */

public class NDFA extends Machine {
    /**
     * El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     * El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     */
    static final public String ELEMENT_NAME = "ndfa";


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
     * El conjunto de estados iniciales del NDFA
     */
    protected StateSet Q0;
    
    
    /**
     * El valor por omisión para Q0
     */
    public static final StateSet DEFAULT_Q0=null;
    
    
    /**
     * funcion de acceso para obtener el valor de Q0
     * @return el valor actual de Q0
     * @see #Q0
     */
    public StateSet getQ0(){
	return Q0;
    }

    
    /**
     * funcion de acceso para modificar Q0
     * @param new_Q0 el nuevo valor para Q0
     * @see #Q0
     */
    public void setQ0(StateSet new_Q0){
	Q0 = new_Q0;
    }
    
    /**
     ** La lista de estados  desde los cuales tenemos que hacer transiciones
     **/    
    LinkedList statesToApply;
    
    /**
     * Get the value of statesToApply.
     * @return Value of statesToApply.
     */
    public LinkedList getStatesToApply() {return statesToApply;}
    
    /**
     * Set the value of statesToApply.
     * @param v  Value to assign to statesToApply.
     */
    public void setStatesToApply(LinkedList  v) {this.statesToApply = v;}
    
    
    
    public NDFA(Alphabet _Sigma, StateSet _Q,
		StateSet _F, NDfaDelta _delta,  StateSet _Q0){
	Sigma = _Sigma;
	Q = _Q;
	F = _F;
	delta = _delta;
	Q0 = _Q0;
    }
    
    protected NDFA(){
	super();
    }
    
    /**
     * Constructora que construye un NDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/ndfa.dtd">ndfa.dtd</a>
     */
    public NDFA(String filename)throws Exception{
	this(new File(filename));
    }

    /**
     * Constructora que construye un NDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/ndfa.dtd">ndfa.dtd</a>
     */
    public NDFA(File file)throws Exception{
	this();
	setupNDFA(factory.newDocumentBuilder().parse(file),this);
    }
    
    /**
     * Constructora que construye un NDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/ndfa.dtd">ndfa.dtd</a>
     */
    public NDFA(org.w3c.dom.Document document)throws Exception{
	this();
	setupNDFA(document,this);
    }

    /**
     * Configura todos los campos del NDFA dado a partir del documento valido que le pasamos
     * @param document es un documento DOM que cumple con la especificación DTD para los NDFAs
     * @param r el DFA que configuramos
     * @see <a href="http://ijaguar.sourceforge.net/DTD/ndfa.dtd">ndfa.dtd</a>
     */
    public void setupNDFA(org.w3c.dom.Document document,NDFA r){
	NodeList elementsList = document.getElementsByTagName(ELEMENT_NAME).item(0).getChildNodes();	
	int howManyElements = 6;
	int j = (document.getElementsByTagName(DESCRIPTION_ELEMENT_NAME).getLength()==0)?1:0;
	Node ndfaNode;
	for(int i =  0; j < howManyElements; i++){	   
	    ndfaNode = elementsList.item(i);
	    if(ndfaNode.getNodeType() == Node.ELEMENT_NODE){		
		switch(j){
		case 0: r.setMachineDescription(ndfaNode.getChildNodes().item(0).getNodeValue()); break;		    
		case 1: r.setQ(new StateSet(ndfaNode)); break;// Q
		case 2: r.setSigma(new Alphabet(ndfaNode)); break;//Sigma
		case 3: r.setDelta(new NDfaDelta(ndfaNode)); break;//delta
		case 4: r.setQ0(new StateSet(ndfaNode)); break;//q0
		case 5: {
		    r.setF(new StateSet(ndfaNode));
		    r.setF(r.getQ().makeSubSetReferences(r.getF()));
		    r.getF().markAsFinal();
		}; break;//F
		}
		j++;
	    }
	}
    }
    
	

    public boolean runMachine(Str str){
 	State st;
	setStatesToApply(new LinkedList());	
	return doTransitions(Q0,str);
    }

    public boolean doTransitions(StateSet st, Str cad){
	boolean res = false;
	State p;
	Debug.println("Q0 = " + st);
	for(Iterator it = st.iterator(); it.hasNext();){
	    p = (State)it.next();
	    displayDeltaPSymb(p,cad);
	    if(res = (res || doTransitions(p,cad)))
		return true;	    	    
	}
	return false;	
    }

    protected boolean doTransitions(State currentSt, Str cad){
	if(cad.length() == 0){
	    return F.contains(currentSt);
	}
	StateSet nextSts;
 	if((nextSts = ((NDfaDelta)delta).apply(currentSt,cad.getFirst())) != null){
	    State ndfast;
	    statesToApply.addAll(nextSts);
	    displayTransResult(nextSts);
	    boolean result = false;	   	    
	    for(Iterator it = nextSts.iterator(); it.hasNext();){		
		ndfast = (State)it.next();

 		if(cad.substring(1).length()==0)
 		    displayTransResult(ndfast);
 		else displayDeltaPSymb(ndfast,cad.substring(1));

 		if(cad.substring(1).length()== 0 && F.contains(ndfast))
		    return true;

		if(result = result||doTransitions(ndfast,cad.substring(1)))
		    return true;		
	    }
	    return result;
 	}else Debug.print(nextSts +"\n");	
	return false;
    }


    
    protected State doTransition(State currentSt, Str cad){
	if(cad.length() == 0)
	    return currentSt;
	StateSet nextSts;
 	if((nextSts = ((NDfaDelta)delta).apply(currentSt,cad.getFirst())) != null){
 	    Iterator it = nextSts.iterator();
  	    State ndfast = (State)it.next();
 	    return  ndfast;
 	}
	return null;
    }

    /**
     * Desplieqga d(p,s) =
     */
    protected void displayDeltaPSymb(State p, Str s){
	System.out.print("d( "+p+" , "+ s +" ) = ");	
    }
    /**
     * imprime q, bajo el contexto d(p,s) = q
     **/
    protected void displayTransResult(State q){
	System.out.println(q);	
    }
    protected void displayTransResult(StateSet sts){
	System.out.println(sts);	
    }    
    

    /**
     * Run the NDFA on the given string
     * @param str the string over Sigma
     **/
    public void executeNdfa(Str str){
    	Debug.println("\nEl NDFA " +
		      (runMachine(str)?"SI":"NO") +
		      " acepta " + str);
    }

    /**
     * Regresa la representación como cadena del NDFA
     **/
    public String toString(){
	return "NDFA " + super.toString() + "N = \n\tQ=" + getQ() + "\n\tSigma = "+ getSigma() + "\n\tdelta = "+ getDelta() + "\n\tQ0 = "+getQ0()+"\n\tF= " + getF();
    }
    
    /** 
     * Guarda la representación del DFA en un archivo con el formato definido por el DTD correspondiente
     * Escribe el DFA con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el NDFA.
     */
    public void toFile(FileWriter fw){
	try{

	    fw.write("<?xml version='1.0' encoding=\"iso-8859-1\" ?>"+"\n");	    
	    fw.write("<!DOCTYPE ndfa SYSTEM \"ndfa.dtd\">"+"\n");
	    
	    if(machineDescription.trim().length() > 0){
		fw.write(DESCRIPTION_BEG_TAG);
		fw.write(getMachineDescription());
		fw.write(DESCRIPTION_END_TAG+"\n");
	    }	    
	    fw.write(BEG_TAG);
	    fw.write("\n\n <!-- Conjunto de States Q --> \n");
	    getQ().toFile(fw);
	    fw.write("\n\n <!-- Alphabet de entrada Sigma --> \n");
	    getSigma().toFile(fw);
	    fw.write("\n\n <!-- Función de Transición delta --> \n");
	    getDelta().toFile(fw);
	    fw.write("\n\n <!-- Conjunto de States iniciales Q0 --> \n");
	    getQ0().toFile(fw);
	    fw.write("\n\n <!-- Conjunto de estados finales F --> \n");
	    getF().toFile(fw);
	    fw.write("\n"+ END_TAG);
	    fw.flush();
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }

    /**
     ** Hace la referencia de todos los estados con respecto a <code>Q</code>
     ** Es útil cuando leemos por separado los estados y tenemos que hacer las referencias
     **/
    public void makeStateReferences(){
	F = Q.makeSubSetReferences(getF());
	Q0 = Q.makeSubSetReferences(getQ0());	
    }
    
    public static void main(String []argv){
	Symbol cero = new Symbol("0");
	Symbol uno = new Symbol("1");
	Symbol as[] = {cero, uno, uno, cero,  uno, uno};
	try{
	    NDFA Paridad = new NDFA(argv[0]);
	    System.err.println(Paridad);
            Str str = new Str(argv[1],false);
	    System.err.println("\nInput String: " + str +"\n");	    
	    Paridad.executeNdfa(str);
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
    
    
}

/* NDFA.java ends here */
