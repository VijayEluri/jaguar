/**
** <Ndfa2Dfa.java> -- Converts a NDFA into a DFA
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

 /** 
 * Convierte un NDFA a DFA
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
import jaguar.machine.dfa.*;
import jaguar.structures.*;
import jaguar.machine.structures.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.exceptions.*;
import jaguar.util.*;
import java.util.*;

public class Ndfa2Dfa{
    /**
     * El nombre del archivo donde se especifica el NDFA a convertir
     */
    protected String ndfaFileName;
    /**
     * El valor por omisión para ndfaFileName
     */
    public static final String DEFAULT_NDFAFILENAME=null;
    /**
     * funcion de acceso para obtener el valor de ndfaFileName
     * @return el valor actual de ndfaFileName
     * @see #ndfaFileName
     */
    public String getNdfaFileName(){
	return ndfaFileName;
    }
    /**
     * funcion de acceso para modificar ndfaFileName
     * @param new_ndfaFileName el nuevo valor para ndfaFileName
     * @see #ndfaFileName
     */
    public void setNdfaFileName(String new_ndfaFileName){
	ndfaFileName=new_ndfaFileName;
    }

    /**
     * El nombre del archivo donde se especificará el DFA resultante 
     */
    protected String dfaFileName;
    /**
     * El valor por omisión para dfaFileName
     */
    public static final String DEFAULT_DFAFILENAME=null;
    /**
     * funcion de acceso para obtener el valor de dfaFileName
     * @return el valor actual de dfaFileName
     * @see #dfaFileName
     */
    public String getDfaFileName(){
	return dfaFileName;
    }
    /**
     * funcion de acceso para modificar dfaFileName
     * @param new_dfaFileName el nuevo valor para dfaFileName
     * @see #dfaFileName
     */
    public void setDfaFileName(String new_dfaFileName){
	dfaFileName=new_dfaFileName;
    }
    
    /**
     * Conjunto de estados contenido por Q del NDFA
     */
    protected StateSet S;
    /**
     * El valor por omisión para S
     */
    public static final StateSet DEFAULT_S=null;
    /**
     * funcion de acceso para obtener el valor de S
     * @return el valor actual de S
     * @see #S
     */
    public StateSet getS(){
	return S;
    }
    /**
     * funcion de acceso para modificar S
     * @param new_S el nuevo valor para S
     * @see #S
     */
    public void setS(StateSet new_S){
	S=new_S;
    }
    /**
     * El DFA resultante de la transformación del NDFA dado 
     */
    protected DFA dfa;
    /**
     * El valor por omisión para dfa
     */
    public static final DFA DEFAULT_DFA=null;
    /**
     * funcion de acceso para obtener el valor de dfa
     * @return el valor actual de dfa
     * @see #dfa
     */
    public DFA getDfa(){
	return dfa;
    }
    /**
     * funcion de acceso para modificar dfa
     * @param new_dfa el nuevo valor para dfa
     * @see #dfa
     */
    public void setDfa(DFA new_dfa){
	dfa=new_dfa;
    }
    /**
     * El NDFA que nos da para convertir
     */
    protected NDFA ndfa;
    /**
     * El valor por omisión para ndfa
     */
    public static final NDFA DEFAULT_NDFA=null;
    /**
     * funcion de acceso para obtener el valor de ndfa
     * @return el valor actual de ndfa
     * @see #ndfa
     */
    public NDFA getNdfa(){
	return ndfa;
    }
    /**
     * funcion de acceso para modificar ndfa
     * @param new_ndfa el nuevo valor para ndfa
     * @see #ndfa
     */
    public void setNdfa(NDFA new_ndfa){
	ndfa=new_ndfa;
    }
    /**
     * Constructor.
     * Recibe los valores para ndfa.
     * Para el resto de los campos usa el valor por omision.
     * @param ndfa el valor con el que se inicalizará el campo ndfa
     * @see #ndfa
     * @see #DEFAULT_DFA
     * @see #DEFAULT_S
     */
    public Ndfa2Dfa (NDFA ndfa){
	this.ndfa=ndfa;
	this.dfa=DEFAULT_DFA;
	this.S=DEFAULT_S;
	this.ndfaFileName= DEFAULT_NDFAFILENAME;
	this.dfaFileName = DEFAULT_DFAFILENAME;	
    }

    /**
     * Constructor recibe el nombre  del archivod donde se especifica el NDFA  a convertir
     * Para el resto de los campos usa el valor por omisión
     * @param ndfaFileName El nombre del archivo donde se especifica el NDFA
     */
    public Ndfa2Dfa(String ndfaFileName)throws Exception{
	this();
	this.ndfaFileName=ndfaFileName;
	setNdfa(new NDFA(getNdfaFileName()));
    }
    
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_NDFA
     * @see #DEFAULT_DFA
     * @see #DEFAULT_S
     */
    public Ndfa2Dfa (){
	this.ndfa=DEFAULT_NDFA;
	this.dfa=DEFAULT_DFA;
	this.S=DEFAULT_S;

    }

    /**
     ** Realiza la conversión del NDFA dado a un DFA el cual es asignado al  DFA de la instancia por medio
     ** del método setDfa()
     */
    public void doConvertion(){
	System.err.println("Ndfa2Dfa.doConvertion");
	doConvertion(Debug.DEBUG_ON);
    }

    
    public void doConvertion(int debug_level){
	if(getNdfa() == null)  
	    return ;
	showStatus("\n" + getNdfa().toString(),debug_level);	
	StateSet S = getNdfa().getQ0();
	Vector vSigma = getNdfa().getSigma().toVector();	
	NDfaDelta ndel = (NDfaDelta)getNdfa().getDelta();	
	LinkedList toAnalize = new LinkedList();	
	Hashtable transTable = new Hashtable();
	Vector vEntry;
	StateSet resultingSts;
	if(! S.isEmpty())
	    toAnalize.add(S);
	showStatus("\n=>> toAnalize =>  "+toAnalize, debug_level);
	while(! toAnalize.isEmpty()){
	    StateSet currentSts = (StateSet)toAnalize.removeFirst();

	    showStatus("\n=>> Processing  "+ currentSts, debug_level);	    
	    if( ! transTable.containsKey(currentSts)){		
		Object [] oSts = currentSts.toArray();
		vEntry = new Vector();	    
		for (int j = 0; j < vSigma.size(); j++){
		    resultingSts = new StateSet();
		    for(int i  = 0 ; i < oSts.length; i++){
			Collection c =  ndel.apply((State)oSts[i],(Symbol)vSigma.elementAt(j));
			if( c != null)
			    resultingSts.addAll(c);
		    }
		    vEntry.add(resultingSts);
		    Debug.println("\n\t\t"+ vSigma.elementAt(j) + " => " + resultingSts,debug_level); 
		    showStatus("\n\t " + currentSts  +" with " +((Symbol)vSigma.elementAt(j)).toStringNoTags() + " = " + resultingSts +"."); 
		    if(! transTable.containsKey(resultingSts) && !toAnalize.contains(resultingSts) && !currentSts.equals(resultingSts)){	
			toAnalize.add(resultingSts);
			showStatus(" Add  " + resultingSts + " a toAnalize",debug_level);
//			showStatus(" Agregando  " + resultingSts + " a toAnalize",debug_level);
		    }else showStatus(" " + resultingSts + " already registered, skipping ",debug_level);
		}
		transTable.put(currentSts,vEntry);
	    }else showStatus("\n " + currentSts + " already proccesed,  skipping ",debug_level);
	    showStatus("\n=>> toAnalize =>  "+toAnalize,debug_level);	
	}
	showStatus("\n\n The result is: \n\n " + toStringNewTrans(transTable,vSigma.size()),debug_level);

	showStatus("\nRenaming and mark as final process ... ",debug_level);
	showStatus("\n=>> NDFA.F "+getNdfa().getF(),debug_level);
	
	Object [] oKeys = transTable.keySet().toArray();
	Hashtable mapNewNamesTable = new Hashtable();
	StateSet QQ = new StateSet();
	StateSet FF = new StateSet();
	for (int k = 0 ; k < oKeys.length ; k++){
	    State currentNewSt = new State("q"+k);	    
	    mapNewNamesTable.put(oKeys[k],currentNewSt);
	    if(((StateSet)oKeys[k]).intersect(getNdfa().getF())){		
		currentNewSt.markAsFinal();
		showStatus("\n\t" + ((StateSet)oKeys[k]) +" contains Final State.  Marking '"+ currentNewSt+"' as final.x ",debug_level);
		FF.add(currentNewSt);		
	    }
	    QQ.add(currentNewSt); 
	}
	showStatus("\n\nApplied State Map\t" + mapNewNamesTable,debug_level);
	/** El estado inicial para el DFA **/
	State q0 = (State)mapNewNamesTable.get(S);	
	Delta d = new DfaDelta();
	for(int i = 0 ; i < oKeys.length; i++){
	    Vector vCurrent = (Vector)transTable.get(oKeys[i]);
	    for(int j = 0 ; j < vSigma.size(); j ++ )
		((DfaDelta)d).addTransition((State)mapNewNamesTable.get(oKeys[i]),
					    (Symbol)vSigma.get(j),
					    (State)mapNewNamesTable.get((StateSet)vCurrent.get(j)));
	}
	setDfa(new DFA(getNdfa().getSigma(),QQ,FF,(DfaDelta)d,q0));	
    }
    
    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     *
     */
    protected void showStatus(String msg){
	showStatus(msg,Debug.DEBUG_ON);	
    }

    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    protected void showStatus(String msg, int debug_level){
	Debug.print(msg,debug_level);	
    }

    
    public String toStringNewTrans(Hashtable ht, int sigmaSize){
	Object []os = ht.keySet().toArray();
	String r = "";
	Vector vPair;
	for(int i = 0 ; i< os.length; i++){
	    vPair = (Vector)ht.get(os[i]);	    
	    r += "\n\t" + os[i]  + "\n\t\t\t\t( ";  
	    for( int j =0 ; j < sigmaSize; j ++){
		r += vPair.get(j) ;
		if((j+1) < sigmaSize )
		    r+= ", ";
	    }
	    r += " ) ";	    
	}
	return r;	
    }
    /**
     * Regresa una cadena con una representación del objeto.
     * Imprime la representación del NDFA y la del DFA resultante
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	return "NDFA => \n "  + ndfa.toString() + "\n\nDFA => \n  " + dfa.toString();
    }

    public void DfaToFile(String filename){
	getDfa().toFile(filename);	
    }
}

/* Ndfa2dfa.java ends here. */

