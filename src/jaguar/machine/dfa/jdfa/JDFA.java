/**
** <JDFA.java> -- The DFA's  graphical extension  
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


package jaguar.machine.dfa.jdfa;

/**
 * JDFA.java
 *
 *
 * Created: Wed Feb 21 00:15:15 2001
 *
 * @author <a href="mailto: "</a>
 * @version
 */

import java.awt.Graphics;
import java.awt.Dimension;
import jaguar.machine.dfa.*;
import jaguar.machine.JMachine;
import jaguar.machine.dfa.exceptions.*;
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.jdfa.jstructures.*;
import jaguar.machine.dfa.structures.exceptions.*;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class JDFA extends DFA implements JMachine{
    /**
     * El contexto gráfico sobre el cual se dibujara (será el del DfaCanvas
    **/    
    private Graphics g;
    /**
     * Get the value of g.
     * @return value of g.
     */
    public Graphics getG() {
	return g;
    }
    
    /**
     * Set the value of g.
     * @param v  Value to assign to g.
     */
    public void setG(Graphics  v) {
	g = v;
    }

    private JDfaFrame dfaframe;
    
    /**
     * Get the value of dfaframe.
     * @return value of dfaframe.
     */
    public JMachineFrame getMachineFrame() {
	return dfaframe;
    }
    
    /**
     * Set the value of dfaframe.
     * @param v  Value to assign to dfaframe.
     */
    public void setJMachineFrame(JMachineFrame  v) {
	this.dfaframe = (JDfaFrame)v;
    }

    /**
     ** La cadena que vamos a checar si esta en el lenguaje L(M) de
     ** está máquina
     **/
    protected JStr strToTestOrig;
    
    /**
     * Get the value of strToTestOrig.
     * @return value of strToTestOrig.
     */
    public JStr getStrToTestOrig() {
	return strToTestOrig;
    }
    
    /**
     * Set the value of strToTestOrig.
     * @param v  Value to assign to strToTestOrig.
     */
    public void setStrToTestOrig(JStr  v) {
	strToTestOrig = v;
    }


    /**
     * La cadena que tenemos que ejecutar
     */
    private JStr strToTest;
    
    /**
     * Get the value of strToTest.
     * @return value of strToTest.
     */
    public JStr getStrToTest() {
	return strToTest;
    }
    
    /**
     * Set the value of strToTest.
     * @param v  Value to assign to strToTest.
     */
    public void setStrToTest(JStr  v) {
	this.strToTest = v;
    }

    
    /**
     * La subcadena de la cadena de entrada que ya ha sido probada
     */
    JStr subStrTested;
    
    /**
       * Get the value of subStrTested.
       * @return Value of subStrTested.
       */
    public JStr getSubStrTested() {return subStrTested;}
    
    /**
       * Set the value of subStrTested.
       * @param v  Value to assign to subStrTested.
       */
    public void setSubStrTested(JStr  v) {this.subStrTested = v;}

    /**
      * El estado anterior antes de ser nulo, esto nos sirve para dar
      * un último estado y checar si esta en los finales, en caso de
      * tener que hacer este chequeo
      */
    protected State previousNotNullCurrentState;
    /**
     * funcion de acceso para obtener el valor de previousNotNullCurrentState
     * @return el valor actual de previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public State getPreviousNotNullCurrentState(){
	return previousNotNullCurrentState;
    }
    /**
     * funcion de acceso para modificar previousNotNullCurrentState
     * @param new_previousNotNullCurrentState el nuevo valor para previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public void setPreviousNotNullCurrentState(State new_previousNotNullCurrentState){
	previousNotNullCurrentState=new_previousNotNullCurrentState;
    }
    
    private State currentState;
    
    /**
     * Get the value of currentState.
     * @return value of currentState.
     */
    public State getCurrentState() {
	return currentState;
    }
    
    /**
     * Set the value of currentState.
     * @param v  Value to assign to currentState.
     */
    public void setCurrentState(State  v) {
	this.currentState = v;
    }


    public JDFA(Alphabet _Sigma, StateSet _Q, StateSet _F, DfaDelta _delta,  State _q0, Graphics _g){
	this(_Sigma, _Q, _F, _delta, _q0);
	setG(_g);	
    }

    public JDFA(DFA dfa){
	this(dfa.getSigma(),new JStateSet(dfa.getQ()) ,new JStateSet(dfa.getF()), (DfaDelta)dfa.getDelta(),new JState(dfa.getQ0()));
	setDelta(new JDfaDelta((DfaDelta)getDelta(),(JStateSet)getQ()));
    }

    
    public JDFA(Alphabet _Sigma, StateSet _Q, StateSet _F, DfaDelta _delta,  State _q0){
	super(_Sigma, _Q, _F, _delta, _q0);
	setCurrentState(getQ0());
	setPreviousNotNullCurrentState(getQ0());	
    }

    protected JDFA(){
	super();	
    }

    /**
     * Constructora que construye un JDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JDFA(String filename)throws Exception{
	this(new File(filename));	
    }

    /**
     * Constructora que construye un JDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @param jdfaframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JDFA(String filename, JDfaFrame jdfaframe)throws Exception{
	this(new File(filename),jdfaframe);	
    }

    /**
     * Constructora que construye un JDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @param jdfaframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JDFA(File file, JDfaFrame jdfaframe)throws Exception{
	this(file);	
	setJMachineFrame(jdfaframe);
	initStatesPosition(dfaframe.getJScrollPaneCanvas().getViewport().getViewSize());
	dfaframe.getJdc().repaint();
    }      

    /**
     * Constructora que construye un JDFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JDFA(File file)throws Exception{
	super(file);	
	setQ(new JStateSet(getQ()));
	setQ0(new JState(getQ0()));
	setF(new JStateSet(getF()));
	setDelta(new JDfaDelta((DfaDelta)getDelta(),(JStateSet)getQ()));

	setPreviousNotNullCurrentState((JState)getQ0());
	makeStateReferences();	
	setCurrentState((JState)getQ0());
    }

    /**
     * Reinicializa el JDFA a los valores de inicio<br>
     * Los valores reinicializados son:<br>
     * <ul>
     * <li>Al estado actual le asigna <b>q0</b></li>
     * <li>A la cadena para probar le asigna la cadena original a probar asignada por medio de
     *       <code>setStrToTestOrig(JStr)</code></li>
     */
    public void resetMachine(){
	((JDfaDelta)delta).setCurrent_p(new JState("resetP"));	
	((JDfaDelta)delta).setCurrent_q(new JState("resetQ"));	
	setCurrentState(getQ0());	
	setPreviousNotNullCurrentState(getQ0());
	setStrToTest(getStrToTestOrig());
	setSubStrTested(new JStr());	
	((JDfaDelta)delta).setCurrent_sym(new Symbol("reset"));
	((JDfaFrame)getMachineFrame()).getJdc().repaint();
    }


    /**
     * Aplica la función delta con los parametros dados
     * @param currentS el estado sobre el que estamos
     * @param cad la cadena sobre cuyo primer símbolo aplicaremos la delta
     * @return el valor de  delta(currentS,cad.getFirst())
     */
    protected State doTransition(State currentS, JStr cad){
	State result;
	result  = super.doTransition(currentS,(Str)cad);
	((JDfaDelta)delta).setCurrent_p(currentS);
	((JDfaDelta)delta).setCurrent_sym(cad.getFirst());
	((JDfaDelta)delta).setCurrent_q(result);	
	dfaframe.getJdc().repaint();
	return result;	
    }

    /**
     * Regresa verdadero si podemo hacer un paso más o falso si no
     * podemos
     * @return <code>true</code> - si podemos seguir aplicando la
     * función de transición delte, i.e. si la cadena a checar no es
     * <epsilon> y si el estado en el que estamos es distinto de
     * <code>null</code>. <br> <code>false</code> - en otro caso.
     */
    public boolean nextStep(){
 	if(!strToTest.isEpsilon() && currentState!=null){
 	    getSubStrTested().concat(new JStr(strToTest.getFirst()));	    
	    setPreviousNotNullCurrentState(currentState);
 	    currentState = doTransition(currentState,strToTest);
 	    setStrToTest((JStr)strToTest.substring(1));
 	    if(getStrToTest().isEpsilon())
		return false;
	    return true;
	}
	return false;
    }


    
     /**
      * Despliega el resultado de la ejecución del autómata como un
      * cuadrito en el <code>dfaframe</code> asociado
      */
     public void displayResult(){
//	 System.err.println("[1] currentState => " + currentState);	 
	 if(currentState == null)
	     JOptionPane.showMessageDialog((JFrame)dfaframe,"The DFA  does NOT accept the string "+getStrToTestOrig(),"DFA Result", JOptionPane.INFORMATION_MESSAGE);
 	else
 	    JOptionPane.showMessageDialog((JFrame)dfaframe,"The DFA "+(getCurrentState().getIsInF()?"accepts":"does  NOT accept") + " the string "+getStrToTestOrig(), "DFA Result",JOptionPane.INFORMATION_MESSAGE);
//	 System.err.println("[2] currentState => " + currentState);	 
     }

    /**
     * Pinta el estado como el estado actual, bajo el contexto de la aplicación más reciente de la función de transición
     * delta, bajo el contexto delta(p,s) = q
     **/
    protected void displayTransResult(State q){
	((JState)q).paint(dfaframe.getJdc().getGraphics(),JState.DEFAULT_CURRENT_STATE);
    }   
    
    public void print(Graphics g){
	dfaframe.getJdc().paint(g);
    }
    
    /**
     * Esta función se usa para asignar posiciones a los centros de
     * los JStates.  Estas posiciones, están alrededor de un circulo
     * de radio <code>r</code>, dividiendo y encontramos la posición
     * de cada estado por medio de coordenadas polares (<code>(x,y) =
     * (r*cos*theta, r*sin*theta)</code>).  Donde la theta es cada uno
     * de los intervalos de dividir 360 entre la cardinalidad de Q y r
     * es el minimo entre el ancho y alto del canvas entre dos.
     */
    public  void initStatesPosition(Dimension d){
	int cardinalidadQ = getQ().size();
	if(cardinalidadQ > 0){	    
	    double radio;
	    if(d.getWidth() != 300 && d.getHeight() != 300)
		radio = Math.min((d.getWidth() - 250)/2,(d.getHeight() - 250)/2);
	    else radio = Math.min((d.getWidth() - 75)/2,(d.getHeight() - 75 )/2);
            radio +=10;
	    JState current;	    
	    double intervalo = 360 / cardinalidadQ;
	    double currentIntervalo = 0;	
	    for (Iterator i = getQ().iterator() ; i.hasNext() ;) {	    
		current = (JState)i.next();
		current.setLocation(radio * Math.cos(Math.toRadians(currentIntervalo))+radio,
				    radio * Math.sin(Math.toRadians(currentIntervalo))+radio);	    
		currentIntervalo += intervalo;	    
	    }
	}
    }
    
    /**
     * Regresa la representación como cadena del JDFA
     **/
    public String toString(){
	return "J" + super.toString();	
    }
    /**
     * La representación en vector de la función de transición delta
     */
    protected Vector tableVector = DEFAULT_TABLEVECTOR;
    /**
     * El valor por omisión para tableVector
     */
    public static final Vector DEFAULT_TABLEVECTOR=null;

    /**
     * funcion de acceso para obtener el valor de tableVector
     * @return el valor actual de tableVector, donde la entrada
     * tableVector.get(0) es el header y tableVector.get(1) es un
     * vector que contiene los renglones
     * @see #tableVector
     */
    public Vector getTableVector(){
	if(tableVector == DEFAULT_TABLEVECTOR){
	    Object aSigma[] = getSigma().toArray();
	    Object aQ[] = getQ().toArray();
	    Vector header = new Vector(), currentRow, data = new Vector();
	    Object entry;	    
	    for(int i = 0 ; i < aQ.length ; i++){
		currentRow = new Vector();
		for(int j = 0 ; j < aSigma.length ; j++){
		    entry = ((DfaDelta)getDelta()).apply((State)aQ[i],(Symbol)aSigma[j]);
		    currentRow.add((entry != null)?entry.toString():null);
		}
		currentRow.add(0,aQ[i]);
		data.add(currentRow);
	    }
	    for(int j = 0 ; j < aSigma.length ; j++)
		header.add(((Symbol)aSigma[j]).getSym());
	    header.add(0,"Q");
	    tableVector = new Vector();
	    tableVector.add(header);
	    tableVector.add(data);
	}
	return tableVector;
    }

    /**
     ** Dado un estado dice si es o no es un estado inicial
     ** @param p el estado sobre el cual preguntaremos si es o no inicial en ésta máquina
     ** @return true si <code>p</code> es estado inicial
     **/
    public boolean esInicial(State p){
	return p.equals(getQ0());
    }

}// JDFA
