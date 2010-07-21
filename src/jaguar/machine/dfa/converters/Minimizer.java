/**
** <Minimizer.java> -- To minimize a DFA
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


package jaguar.machine.dfa.converters;

import jaguar.machine.dfa.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.structures.exceptions.*;
import jaguar.util.*;
import java.util.*;
/** 
 * Esta clase minimiza autómatas finitos. Dado un DFA M1 esta clase genera un DFA M2 reducido, i.e. sin estados equivalentes
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Minimizer{
    /**
     * El nombre del archivo donde se especifica el DFA a minimizar
     */
    protected String dfaToMinimizeFileName;
    /**
     * funcion de acceso para obtener el valor de dfaToMinimizeFileName
     * @return el valor actual de dfaToMinimizeFileName
     * @see #dfaToMinimizeFileName
     */
    public String getDfaToMinimizeFileName(){
	return dfaToMinimizeFileName;
    }
    /**
     * funcion de acceso para modificar dfaToMinimizeFileName
     * @param new_dfaToMinimizeFileName el nuevo valor para dfaToMinimizeFileName
     * @see #dfaToMinimizeFileName
     */
    public void setDfaToMinimizeFileName(String new_dfaToMinimizeFileName){
	dfaToMinimizeFileName=new_dfaToMinimizeFileName;
    }

    /**
     * El nombre del archivo donde se especifica el DFA resultante minimizado
     */
    protected String dfaMinimizedFileName;
    /**
     * funcion de acceso para obtener el valor de dfaminimizedFileName
     * @return el valor actual de dfaminimizedFileName
     * @see #dfaMinimizedFileName
     */
    public String getDfaMinimizedFileName(){
	return dfaMinimizedFileName;
    }
    /**
     * funcion de acceso para modificar dfaminimizedFileName
     * @param new_dfaMinimizedFileName el nuevo valor para dfaminimizedFileName
     * @see #dfaMinimizedFileName
     */
    public void setDfaMinimizedFileName(String new_dfaMinimizedFileName){
	dfaMinimizedFileName=new_dfaMinimizedFileName;
    }
    /**
     * El DFA de entrada que minimizaremos
     */
    protected DFA dfaOrig;
    /**
     * funcion de acceso para obtener el valor de dfaOrig
     * @return el valor actual de dfaOrig
     * @see #dfaOrig
     */
    public DFA getDfaOrig(){
	return dfaOrig;
    }
    /**
     * funcion de acceso para modificar dfaOrig
     * @param new_dfaOrig el nuevo valor para dfaOrig
     * @see #dfaOrig
     */
    public void setDfaOrig(DFA new_dfaOrig){
	dfaOrig=new_dfaOrig;
    }
    /**
     * El DFA resultante del proceso de minimización
     */
    protected DFA dfaMinimized;
    /**
     * funcion de acceso para obtener el valor de dfaMinimized
     * @return el valor actual de dfaMinimized
     * @see #dfaMinimized
     */
    public DFA getDfaMinimized(){
	return dfaMinimized;
    }
    /**
     * funcion de acceso para modificar dfaMinimized
     * @param new_dfaMinimized el nuevo valor para dfaMinimized
     * @see #dfaMinimized
     */
    public void setDfaMinimized(DFA new_dfaMinimized){
	dfaMinimized=new_dfaMinimized;
    }
    
    /**
     * Constructor, recibe el <code>DFA</code> a minimizar 
     * Inicializa el objeto usando los valores por omision.
     * @param dfa el  <code>DFA</code> a minimizar 
     */
    public Minimizer (DFA dfa){
	dfaOrig = dfa;
    }

    public Minimizer (){
	super();
    }    
    
    /**
     * Constructor recibe el nombre  del archivod donde se especifica el DFA  a minimizar
     * Para el resto de los campos usa el valor por omisión
     * @param _dfaToMinimizeFileName el nombre del archivo donde se especifica el NDFA
     */
    public Minimizer(String _dfaToMinimizeFileName)throws Exception{
	dfaToMinimizeFileName=_dfaToMinimizeFileName;
	setDfaOrig(new DFA(getDfaToMinimizeFileName()));
    }


    public void doConvertion(){
	Debug.println("Minimizer.doMinimization");
	doConvertion(Debug.DEBUG_ON);
    }

    public void doConvertion(int debug_level){
	if(getDfaOrig() == null)
	    return;
	DFA tmp = new DFA();
	try{ 
	    
	    tmp= (DFA) getDfaOrig().clone();
	    
	}catch( CloneNotSupportedException ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + " No: " ); 
	    ouch.printStackTrace(); 
	}

	showStatus("\n tmp.equals(getDfaOrig()) => " + tmp.equals(getDfaOrig()));
	showStatus("\n " + tmp.toString(), debug_level);
	// Eliminamos los estados inalcanzables
	showStatus("\n\nEliminating unattainable states");

	StateSet alcanzables = eliminaEstadosInalcanzables(tmp);
	StateSet inalcanzables = (StateSet)tmp.getQ().clone();
	inalcanzables.removeAll(alcanzables);
	showStatus("Eliminating from Q the subset = " + inalcanzables);

	showStatus("Updating the funcion delta");

	tmp.setDelta(generaNuevaDelta(alcanzables,(DfaDelta)tmp.getDelta()));
	tmp.getQ().removeAll(inalcanzables);
	tmp.getF().removeAll(inalcanzables);
	showStatus("Detecting  equivalent states");
	StateSet particiones = getParticiones(tmp);
	showStatus("\nThe partitions are:" + particiones+"\n");

	// Ahora comenzamos a renombrar
	Hashtable<StateSet, State> mapNewNamesTable = new Hashtable<StateSet, State>();
	Object []oKeys = particiones.toArray();

	StateSet currentClass;
	State unEspecimen, currentNewState;
	StateSet QQ = new StateSet(), FF = new StateSet();
	for(int k = 0, numero= 0 ; k < oKeys.length; k++){
	    currentClass = (StateSet) oKeys[k];
	    showStatus("\n\tThe class: \""+currentClass+"\"");

	    if(!mapNewNamesTable.contains(currentClass)){
		currentNewState = new State("B"+numero);
		mapNewNamesTable.put(currentClass,currentNewState);
		showStatus(" Not present, mapping to B"+numero);

		numero++;
		if(((StateSet)oKeys[k]).intersect(tmp.getF())){
		    showStatus("\n\t\t\t" + ((StateSet)oKeys[k]) +" contains Final State.  Marking '"+ currentNewState+"' as final. ",debug_level);
		    currentNewState.markAsFinal();
		    FF.add(currentNewState);
		}
		QQ.add(currentNewState);
	    }else{
		showStatus(" already present");
	    }

	}

	showStatus("\n\nNew states map\n"+mapNewNamesTable+"\n");
	// Construyendo el nuevo DFA
	State nuevoQ0 = (State)mapNewNamesTable.get(getClaseDeEquivalencia(tmp.getQ0(),particiones));	
	Delta d = new DfaDelta();
	Vector vSigma = tmp.getSigma().toVector();
	State eRes;
	Vector vCurrent,vPairSymRes;
	for(int i = 0 ; i < oKeys.length; i++){
	    currentClass = (StateSet) oKeys[i];
	    unEspecimen = (State)currentClass.toArray()[0];
	    showStatus("\n\ta specimen: "+unEspecimen);


	    vCurrent = ((DfaDelta)tmp.getDelta()).getTransitions(unEspecimen);

	    for(int j = 0 ; j < vCurrent.size(); j++){
		vPairSymRes = (Vector)vCurrent.get(j);
		((DfaDelta)d).addTransition((State)mapNewNamesTable.get(oKeys[i]),
					    (Symbol)vPairSymRes.get(0),
					    (State)mapNewNamesTable.get(getClaseDeEquivalencia((State)vPairSymRes.get(1),particiones)));
	    }

	}
	setDfaMinimized(new DFA(tmp.getSigma(),QQ,FF,(DfaDelta)d,nuevoQ0));
    }
    
    /** 
     * Muestra el status de minimización cada que se le llama.
     * Dependiendo del tipo de minimizador (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     *
     */
    protected void showStatus(String msg){
	showStatus(msg,Debug.DEBUG_ON);	
    }
    
    /** 
     * Muestra el status de la minimización cada que se le llama.
     * Dependiendo del tipo de minimizador (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    protected void showStatus(String msg, int debug_level){
	Debug.print(msg,debug_level);	
    }

    
    /**
     * Genera una nueva función de transición delta a partir de un <code>s</code> subconjunto propio de <code>Q</code> de un <code>dfa</code>
     *
     * @param s un subconjunto propio de un conjunto de estados <code>Q</code> de un DFA
     * @param d la función de transición <code>delta</code> de un DFA dado
     * @return una nueva función de transición <code>delta</code> tal que todos los estados a los que hace referencia están en <code>s</code>
     * #see jaguar.machine.dfa.structures.DfaDelta
     */
    public DfaDelta generaNuevaDelta(StateSet s, DfaDelta d){
	DfaDelta dr = new DfaDelta();
	Object sArray[] = s.toArray();
	Vector vCurrentStateTransitions;
	Vector vPair;	
	for(int i = 0; i < sArray.length ; i++){
	    vCurrentStateTransitions = d.getTransitions((State) sArray[i]);
	    for(int j = 0 ; j < vCurrentStateTransitions.size(); j++){
		vPair = (Vector)vCurrentStateTransitions.get(j);
		if(s.contains((State)vPair.get(1)))
		    dr.addTransition((State)sArray[i],(Symbol)vPair.get(0),(State)vPair.get(1));
	    }
	}
	return dr;
    }
    
    protected StateSet getParticiones(){
	return getParticiones(getDfaOrig());
    }
    /**
     * Obtiene las particiones de equivalencia
     */
    protected StateSet getParticiones(DFA dfa){
	StateSet nuevoP = ceroDistinguibles(dfa);
	StateSet viejoP = new StateSet();
	StateSet aux, tmp;
	Object oArray[];
	while(! nuevoP.equals(viejoP)){
	    viejoP = (StateSet)nuevoP.clone();
	    oArray = nuevoP.toArray();
	    aux = new StateSet();
	    for(int i = 0 ; i < oArray.length ; i++){
		tmp = refinaClass(dfa,(StateSet)oArray[i],viejoP);
		aux.addAll(tmp);
	    }
	    nuevoP = aux;
	}
	return nuevoP;
    }


    private StateSet refinaClass(DFA dfa, StateSet currentClass, StateSet previousClasses){
	Object sSetA[]  = currentClass.toArray();
	State ej, ek;
	StateSet nuevaClase;
	StateSet currentClassModified = (StateSet) currentClass.clone();
	StateSet result = new StateSet();
	for(int j = 0 ; j < sSetA.length ; j++){
	    ej = (State) sSetA[j];
	    if(getClaseDeEquivalencia(ej,result) == null ){
		nuevaClase= (StateSet) currentClassModified.clone();
		for(int k = j+1 ; k < sSetA.length ; k++){
		    ek = (State) sSetA[k];
		    if(distinguibles(dfa,ej,ek,previousClasses))
			nuevaClase.remove(ek);
		}
		currentClassModified.removeAll(nuevaClase);
		result.addAll(nuevaClase);
	    }
	}
	return result;
    }


     /** 
      * Prueba si los estados son distinguibles aplicando ambos a cada uno de los simbolos.
      * @param p el estado a distinguir.
      * @param q el estado a distinguir.
      * @param previousClasses la particion de equivalencia anterior
      * @return <code>true</code> si  son distinguibles, <code>false</code> e.o.c..
      */
    protected boolean distinguibles(DFA dfa, State p ,State q, StateSet previousClasses){
	DfaDelta d = (DfaDelta)dfa.getDelta();
	Alphabet A = dfa.getSigma();
	Object aArray[] = A.toArray();
	Symbol si;
	State rp, rq;
	for(int i = 0 ; i < aArray.length ; i++){
	    si = (Symbol)aArray[i];
	    if(!getClaseDeEquivalencia(d.apply(p,si),previousClasses).equals(getClaseDeEquivalencia(d.apply(q,si),previousClasses))){
		return true;
	    }
	}
	return false;
    }

     /** 
      * Genera las dos primeras clases de equivalencia, en una los que son 
      * estados terminales y en otra los que no lo son .
      *
      * @param dfa El DFA de quien sacaremos la primer partición.
      * @return Regresa una partición de equivalencia (<code>HashSet</code>) de 
      * <code>Q</code>, tiene dos clases de equivalencia (<code>StateSet</code>) 
      * las clases 1-distinguibles.
      *
      */
    protected StateSet ceroDistinguibles(DFA dfa){    
	StateSet finales, nofinales;
	finales = (StateSet) dfa.getF().clone();
	nofinales = (StateSet)dfa.getQ().clone();
	nofinales.removeAll(finales);
	StateSet result = new StateSet();
	result.addAll(finales);
	result.addAll(nofinales);
	return result;
    }


    /**
     * Regresa la clase de equivalencia a la que pertenece un conjunto <code>p</code> 
     * dada la particion de equivalencia <code>previousClasses</code>
     *
     * @param p el estado del cual queremos saber a que clase de equivalencia pertenece
     * @param particionDeEquivalencia la particion de equivalencia dada
     * @return un conjunto de States que es la clase de equivalencia a la que pertenece 
     * <code>p</code>, si no se encuentra regresa <code>null</code> 
     */
    protected StateSet getClaseDeEquivalencia(State p, StateSet particionDeEquivalencia){
	Object oArray [] =  particionDeEquivalencia.toArray();
	for(int i = 0 ; i < oArray.length ; i++)
	    if(((StateSet)oArray[i]).contains(p)){
		return (StateSet)oArray[i];
	    }
	return null;
    }

    
     /** 
      * Elimina los estados inalcanzables de un conjunto de estados.
      *
      * @return El conjunto de estados alcanzables.
      *
      */
    protected StateSet eliminaEstadosInalcanzables(DFA dfa){	
	DfaDelta d = (DfaDelta)dfa.getDelta();
	StateSet Q = dfa.getQ();
	Object qArray[];
	StateSet resultV = new StateSet();
	StateSet resultN = new StateSet();
	resultN.add(dfa.getQ0());
	while(! resultN.equals(resultV)){
	    resultV = (StateSet) resultN.clone();
	    qArray = resultN.toArray();
	    for(int i = 0 ; i < qArray.length ; i++)
		resultN.addAll(alcanzablesDesde((State)qArray[i],(DfaDelta)dfa.getDelta()));
	}
	return resultN;
    }

    /**
     * Regresa el conjunto de estados alcanzables desde <code>p</code> dada la función de transición DfaDelta <code>d</code>
     * @param p El estado desde donde queremos alcanzar
     * @param d la función de transición DfaDelta
     * @return un <code>StateSet</code> con los estados alcanzables
     */
    protected StateSet alcanzablesDesde(State p, DfaDelta d){
	StateSet result = new StateSet();
	Vector transiciones = d.getTransitions(p);
	Vector vi;
	for ( int i =  0 ; i < transiciones.size(); i++)
	    result.add((State)((Vector)transiciones.get(i)).get(1));
	return result;
    }
    
    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	String salida="";
	return salida; 
    }
}

/* Minimizer.java ends here. */
