/**
** <AFS.java> -- The push down automata engine
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


package jaguar.machine.stack;

 /** 
  * Un Autómata Finito de Stack P es un sistema <p> P = (Q,Sigma,Gamma,delta,q0,Z0,F) <p> donde: <p>
  * Gamma es un alfabeto, llamado alfabeto de entrada; Z0 es el símbolo en el fondo del stack al empezar a funcionar el AFS;
  * y todos los demas elementos del sistema son como se define en <code>machine</code>
  * 
  * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
  * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:04 $
  **/

import java.util.*;
import java.io.*;
import jaguar.machine.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.machine.exceptions.*;
import jaguar.util.*;
import jaguar.machine.stack.structures.*;
import jaguar.machine.stack.structures.exceptions.*;
import jaguar.machine.stack.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class AFS extends Machine{
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "stack";
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
     * El stack asociado al AFS
     */
    protected Stack stack;
    /**
     * funcion de acceso para obtener el valor de stack
     * @return el valor actual de stack
     * @see #stack
     */
    public Stack getStack(){
	return stack;
    }

    public Stack getStackReverse(){	
 	Stack auxStack = (Stack)stack.clone();
	Stack tmpStack = new Stack();
	while(! auxStack.empty())
	    tmpStack.push(auxStack.pop());
	return tmpStack;				
    }
    /**
     * funcion de acceso para modificar stack
     * @param new_stack el nuevo valor para stack
     * @see #stack
     */
    protected void setStack(Stack new_stack){
	stack=new_stack;
    }

    
    /**
     * El estado inicial del AFS
     */
    protected State q0;
    /**
     * funcion de acceso para obtener el valor de q0
     * @return el valor actual de q0
     * @see #q0
     */
    public State getQ0(){
	return q0;
    }
    /**
     * funcion de acceso para modificar q0
     * @param new_q0 el nuevo valor para q0
     * @see #q0
     */
    public void setQ0(State new_q0){
	q0=new_q0;
    }

    /**
     * Es el símbolo en el fondo del stack al empezar a funcionar el AFS
     */
    protected Symbol Z0;
    /**
     * El valor por omisión para Z0
     */
    public static final Symbol DEFAULT_Z0=null;
    /**
     * funcion de acceso para obtener el valor de Z0
     * @return el valor actual de Z0
     * @see #Z0
     */
    public Symbol getZ0(){
	return Z0;
    }
    /**
     * funcion de acceso para modificar Z0
     * @param new_Z0 el nuevo valor para Z0
     * @see #Z0
     */
    public void setZ0(Symbol new_Z0){
	Z0=new_Z0;
    }
    /**
     * es un alfabeto, llamado alfabeto del stack
     */
    protected Alphabet Gamma;
    /**
     * El valor por omisión para Gamma
     */
    public static final Alphabet DEFAULT_GAMMA=null;
    /**
     * funcion de acceso para obtener el valor de Gamma
     * @return el valor actual de Gamma
     * @see #Gamma
     */
    public Alphabet getGamma(){
	return Gamma;
    }
    /**
     * funcion de acceso para modificar Gamma
     * @param new_Gamma el nuevo valor para Gamma
     * @see #Gamma
     */
    public void setGamma(Alphabet new_Gamma){
	Gamma=new_Gamma;
    }
    /**
     ** Constante que usamos para señalar el modo de aceptar por stack vacío
     **/
    public static final boolean ACCEPT_BY_EMPTY_STACK = false;
    /**
     ** Constante que usamos para señalar el modo de aceptar por estado final
     **/
    public static final boolean ACCEPT_BY_FINAL_STATE = true;
    /**
     * Esta variable indica la manera en que el AFS decide si acepta o no una palabra
     */
    protected boolean acceptMode;
    /**
     * El valor por omisión para acceptMode
     */
    public static final boolean DEFAULT_ACCEPT_MODE=ACCEPT_BY_FINAL_STATE;
    /**
     * funcion de acceso para obtener el valor de acceptMode
     * @return el valor actual de acceptMode
     * @see #acceptMode
     */
    public boolean getAcceptMode(){
	return acceptMode;
    }
    /**
     * funcion de acceso para modificar acceptMode
     * @param new_acceptMode el nuevo valor para acceptMode
     * @see #acceptMode
     */
    private void setAcceptMode(boolean new_acceptMode){
	acceptMode=new_acceptMode;
    }

     /**
      * La regla que aplicamos actualmente para la ejecución de la máquina
      */
    protected QxGammaStar appliedRule;
    /**
     * funcion de acceso para obtener el valor de appliedRule
     * @return el valor actual de appliedRule
     * @see #appliedRule
     */
    public QxGammaStar getAppliedRule(){
	return appliedRule;
    }
    /**
     * funcion de acceso para modificar appliedRule
     * @param new_appliedRule el nuevo valor para appliedRule
     * @see #appliedRule
     */
    public void setAppliedRule(QxGammaStar new_appliedRule){
	appliedRule=new_appliedRule;
    }

    
    /**
     ** Construye un AFS con los parematros dados
     ** Por omisión asume que el AFS acepta por estado final
     **/
    public AFS(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, StackDelta _delta, State _q0, Symbol _Z0, StateSet _F){
	this(DEFAULT_ACCEPT_MODE, _Q, _Sigma, _Gamma, _delta, _q0, _Z0, _F);
    }
    
    public AFS(boolean _acceptMode, StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, StackDelta _delta, State _q0, Symbol _Z0, StateSet _F){
	this();	
	Q =  _Q;
	Sigma = _Sigma;
	Gamma = _Gamma;
	delta = _delta;
	q0 = _q0;
	Z0 = _Z0;
	F =  _F;
	acceptMode = DEFAULT_ACCEPT_MODE;
	stack.push(_Z0);	
    }

    public AFS (){
	this(DEFAULT_ACCEPT_MODE);	
    }

    public AFS (boolean _acceptMode){
	super();
	acceptMode =  _acceptMode;
	stack = new Stack();	
    }

    /**
     * Configura todos los campos del AFS dado a partir del documento valido que le pasamos
     * @param document es un documento DOM que cumple con la especificación DTD para los AFSs
     * @param r el AFS que configuramos
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public void setupAFS(org.w3c.dom.Document document,AFS r)throws Exception{
	NodeList elementsList = document.getElementsByTagName(ELEMENT_NAME).item(0).getChildNodes();	
	int howManyElements = 8;
	int j = (document.getElementsByTagName(DESCRIPTION_ELEMENT_NAME).getLength()==0)?1:0;
	Node afsNode;
	for(int i =  0; j < howManyElements; i++){	   
	    afsNode = elementsList.item(i);
	    if(afsNode.getNodeType() == Node.ELEMENT_NODE){		
		switch(j){
		case 0: r.setMachineDescription(afsNode.getChildNodes().item(0).getNodeValue()); break;		    
		case 1: r.setQ(new StateSet(afsNode)); break;// Q
		case 2: r.setSigma(new Alphabet(afsNode)); break;//Sigma
		case 3: r.setGamma(new Alphabet(afsNode)); break;//Gamma
		case 4: r.setDelta(new StackDelta(afsNode)); break;//delta
		case 5: r.setQ0(new State(afsNode)); break;//q0
		case 6: r.setZ0(new Symbol(afsNode)); break;//z0
		case 7: {
		    r.setF(new StateSet(afsNode));
		    r.setF(r.getQ().makeSubSetReferences(r.getF()));
		    r.getF().markAsFinal();
		}break;//F
		}
		j++;
	    }
	}
    }
    
    
    /**
     * Construye un AFS a partir de un archivo como se especifica en la documentación, hay que hacer 
     * asignaciones posteriores del jafsframe sociado.
     */
    public AFS(String filename)throws Exception{
	this(filename,DEFAULT_ACCEPT_MODE);
    }

    /**
     * Construye un AFS a partir de un archivo como se especifica en la documentación, hay que hacer 
     * asignaciones posteriores del jafsframe sociado.
     */
    public AFS(String filename, boolean _acceptMode)throws Exception{
	this(new File(filename),_acceptMode);
    }

    /**
     * Construye un AFS a partir de un archivo como se especifica en la documentación, hay que hacer 
     * asignaciones posteriores del jafsframe sociado.
     */
    public AFS(File file)throws Exception{
	this(file,DEFAULT_ACCEPT_MODE);
    }

    /**
     * Construye un AFS a partir de un archivo como se especifica en la documentación, hay que hacer 
     * asignaciones posteriores del jafsframe sociado.
     */
    public AFS(File file, boolean _acceptMode)throws Exception{
	this(_acceptMode);
	setupAFS(factory.newDocumentBuilder().parse(file),this);
    }

    /**
     * Construye un AFS a partir del nombre de un archivo que es valido segun el DTD de los AFSs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public AFS(org.w3c.dom.Document document)throws Exception{
	this();
	setupAFS(document,this);
    }

     /**
      * Lo que sobró de la cadena de entrada cuando terminó la ejecución del AFS
      */
    protected Str sobranteStr;
    /**
     * funcion de acceso para obtener el valor de sobranteStr
     * @return el valor actual de sobranteStr
     * @see #sobranteStr
     */
    public Str getSobranteStr(){
	return sobranteStr;
    }
    /**
     * funcion de acceso para modificar sobranteStr
     * @param new_sobranteStr el nuevo valor para sobranteStr
     * @see #sobranteStr
     */
    public void setSobranteStr(Str new_sobranteStr){
	sobranteStr=new_sobranteStr;
    }
    
    public boolean runMachine(Str str){
 	State st;
	setSobranteStr(str);
	setStack(new Stack());
	stack.push(getZ0());	
	System.out.println("\nConfiguraciones\t\t\tRegla");	
	displayConfiguration(q0,str);
	displayDeltaPSymb(q0,str,(Symbol)stack.peek());
	if(!getF().isEmpty()){
	    if((st = doTransitions(q0,str)) != null && F.contains(st))
		
		return true;
	    return false;
	}else {/** tenemos que ACCEPT_BY_EMPTY_STACK **/
	    st = doTransitions(q0,str);
	    System.out.println("\nCriterio para aceptar EMPTY_STACK\n\nEl estado del Stack es: " + (stack.empty()?"":"no ") + "vacio");
	    System.out.println("La cadena de entrada es: " + getSobranteStr());	    
	    if(stack.empty() && getSobranteStr().isEpsilon())
		return true;
	    return false;	    
	}
    }
    
    protected State doTransitions(State currentSt, Str cad){
	if(cad.length() == 0 || stack.empty()){	    
	    Debug.print("\n =>>> stack empty");	
	    setSobranteStr(cad);	
	    return currentSt;
	}
	
	QxGammaStarSet nextSts;
 	if((nextSts = ((StackDelta)delta).apply(currentSt,cad.getFirst(),(Symbol)stack.peek())) != null){
 	    Iterator it = nextSts.iterator();
  	    QxGammaStar qtgStar = (QxGammaStar)it.next();	    
  	    State afsst = qtgStar.getQ();
	    Str nStrOnTop = qtgStar.getGammaStar();	    
//	    Debug.println("Sacando el tope del stack i.e. pop >" + stack.pop()+ "<");	    
	    displayTransResult(afsst,nextSts);
	    
	    if(! nStrOnTop.isEpsilon())
		stack.addAll(nStrOnTop.reverse().getSeq());
	    if(cad.substring(1).length()==0)
		displayConfiguration(afsst,cad.substring(1));
	    else{
		displayConfiguration(afsst,cad.substring(1));
		if(stack.empty())
		    displayDeltaPSymb(afsst,cad.substring(1),Str.EPSILON_TAG);
		else
		    displayDeltaPSymb(afsst,cad.substring(1),(Symbol)stack.peek());
	    }
 	    return doTransitions(afsst,cad.substring(1));
 	}else Debug.println(" Ø");	    
	
	return null;
    }

    protected State doTransition(State currentSt, Str cad){
	if(cad.length() == 0 || stack.empty()){	    
	    setSobranteStr(cad);
	    return currentSt;
	}
	
	QxGammaStarSet nextSts;
 	if((nextSts = ((StackDelta)delta).apply(currentSt,cad.getFirst(),(Symbol)stack.peek())) != null){
 	    Iterator it = nextSts.iterator();
  	    QxGammaStar qtgStar = (QxGammaStar)it.next();
	    setAppliedRule(qtgStar);
  	    State afsst = qtgStar.getQ();
	    Str nStrOnTop = qtgStar.getGammaStar();	    
//	    Debug.println("Sacando el tope del stack i.e. pop >" + stack.pop()+ "<");	    
	    stack.pop();	    
	    if(! nStrOnTop.isEpsilon())
		stack.addAll(nStrOnTop.reverse().getSeq());
	    return afsst;	    
	}
	setAppliedRule(null);
	return null;
    }

    /**
     * Run the AFS on the given string
     * @param str the string over Sigma
     **/
    public void executeAfs(Str str){
    	Debug.println("\nEl AFS " +
		      (runMachine(str)?"SI":"NO") +
		      " acepta " + str);
    }
    /**
     * Regresa una cadena con la configuración del AFS
     */
    protected void displayConfiguration(State p, Str s){
	System.out.println(" ( " + p + " , " + s + ", "+ stackToString() +  " )" );	
    }
    /**
     * Desplieqga d(p,s,topeStack) =
     */
    protected void displayDeltaPSymb(State p, Str s, Symbol topeStack){
	System.out.print("\t\t\t\tdelta( "+p+" , "+ s.substring(0,1) +" , " + topeStack +" ) =");	
    }

    protected void displayDeltaPSymb(State p, Str s, String topeStack){
	System.out.print("\t\t\t\tdelta( "+p+" , "+ s.substring(0,1) +" , " + topeStack +" ) =");	
    }

    private String stackToString(){
	String s = ""  ;	 
	for(int i = 0 ; i < stack.size(); i++)
	    s += stack.get(i);
	return s;	
    }
    
    /**
     * imprime q, bajo el contexto d(p,s, topeStack) = (q, topeStack)
     **/
    protected void displayTransResult(State q, QxGammaStarSet qtgsSet){
	System.out.println(" ( " + q + " , " + qtgsSet + " )" );	
    }    

    /** 
     * Muestra el estado del stack.
     */
    protected void displayStackState(){	
	for(int i = 0 ; i < stack.size() ; i++){
	    if(i == 0)
		System.out.println("   Tope\t\t" + stack.get(i));
	    else if(i == (stack.size() - 1))
		System.out.println("   Fondo\t" + stack.get(i));
	    else System.out.println("\t\t" + stack.get(i));
	}
    }

    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	return "SFA " + super.toString() + "P = \n\tQ=" + getQ() + "\n\tSigma = "+ getSigma() + "\n\tGamma = " + getGamma() + "\n\tdelta = "+ getDelta() + "\n\tq0 = "+getQ0()+"\n\tZ0 = " + getZ0() + "\n\tF= " + getF();	
    }

    /** 
     * Escribe la representación del AFS en un archivo con el formato definido por el DTD correspondiente
     * Escribe el AFS con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el AFS.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public void toFile(FileWriter fw){
	try{ 
	    fw.write("<?xml version='1.0' encoding=\"iso-8859-1\" ?>"+"\n");
	    fw.write("<!DOCTYPE stack SYSTEM \"afs.dtd\">"+"\n");	    

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
	    fw.write("\n\n <!-- Alphabet del  Stack Gamma -->  \n");
	    getGamma().toFile(fw);
	    fw.write("\n\n <!-- Función de Transición delta --> \n");
	    getDelta().toFile(fw);
	    fw.write("\n\n <!-- Inicial State q0 --> \n");
	    getQ0().toFile(fw);
	    fw.write("\n\n <!-- Z0 Símbolo en el fondo del Stack cuando el AFS conmienza  su ejecución -->\n");
	    getZ0().toFile(fw);
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
	q0 = Q.makeStateReference(getQ0());	
    }
}

/* AFS.java ends here. */
