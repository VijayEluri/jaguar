/**
** <Dfa2Gtipo3.java> -- The Dfa to type 3 grammar converter 
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


package jaguar.machine.dfa.converters;

import jaguar.machine.dfa.*;
import jaguar.machine.dfa.structures.*;
import jaguar.structures.*;
import jaguar.machine.structures.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.tipo3.structures.*;
import jaguar.grammar.tipo3.*;
import jaguar.util.*;
import jaguar.grammar.structures.exceptions.ProductionNotValidTypeException;
import java.util.*;
import java.io.*;

/** 
 * Esta clase toma un DFA y genera gram�tica tipo 3 equivalente.
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Dfa2Gtipo3{
    /**
     * El DFA del cual generaremos una gram�tica tipo 3 equivalente.
     */
    protected DFA dfa;
    
    /**
     * El valor por omisi�n para dfa
     */
    public static final DFA DEFAULT_DFA = null;
    
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
     * La gram�tica resultante 
     */
    protected Gtipo3 gt3;
    /**
     * El valor por omisi�n para gt3
     */
    public static final Gtipo3 DEFAULT_GT3=null;
    /**
     * funcion de acceso para obtener el valor de gt3
     * @return el valor actual de gt3
     * @see #gt3
     */
    public Gtipo3 getGt3(){
	return gt3;
    }
    /**
     * funcion de acceso para modificar gt3
     * @param new_gt3 el nuevo valor para gt3
     * @see #gt3
     */
    public void setGt3(Gtipo3 new_gt3){
	gt3=new_gt3;
    }
    
    
    /**
     * Constructor.
     * Recibe los valores para dfa.
     * Para el resto de los campos usa el valor por omision.
     * @param dfa el valor con el que se inicalizar� el campo dfa
     * @see #dfa
     * @see #DEFAULT_GT3
     */
    public Dfa2Gtipo3 (DFA dfa){
	this.dfa=dfa;
	this.gt3=DEFAULT_GT3;
	
    }
    
    
    /**
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_GT3
     * @see #DEFAULT_DFA
     */
    public Dfa2Gtipo3 (){
	this.gt3=DEFAULT_GT3;
	this.dfa=DEFAULT_DFA;
    }
    
    /**
     * Regresa una cadena con una representaci�n del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
	String salida="";
	return salida; 
    }

        /**
     * Esta funci�n hace la conversi�n de una gram�tica tipo 3 lineal derecha a un NDFA. El NDFA resultante
     * est� disponible usando la funci�n getAf().
     */
    public void doConvertion(){
	System.err.println("Dfa2Gtipo3.doConvertion");
	doConvertion(Debug.DEBUG_ON); 
    }

    public void doConvertion(int debug_level) {
	Alphabet N = new Alphabet();
	Object qArray[] = getDfa().getQ().toArray();
	for(int i = 0 ; i < qArray.length ; i++)
	    N.add(new Symbol(((State)qArray[i]).getLabel()));
	showStatus("\n Alphabet N "+ N, debug_level);

	Alphabet T = new Alphabet(getDfa().getSigma());
	showStatus("\nAlphabet T "+ T, debug_level);
	Symbol S  = new Symbol(getDfa().getQ0().getLabel());
	showStatus("\nS = "+ S, debug_level);

	showStatus("\nGenerating production set P", debug_level);
	ProductionT3Set P = new ProductionT3Set();
	// Ahora vamos a generar las producciones
	DfaDelta d = (DfaDelta)getDfa().getDelta();
	Object deltaKeys[] = d.keySet().toArray();
	State currentE, eDestino;
	Vector eTransitions, vij;
	Str consTmp;
 	for(int  i = 0 ; i < deltaKeys.length ; i++){
 	    currentE = (State)deltaKeys[i];
 	    eTransitions =  d.getTransitions(currentE);
 	    for(int j = 0 ; j < eTransitions.size(); j++){
 		vij = (Vector)eTransitions.get(j);
		eDestino = (State)vij.get(1);	
 		consTmp = new Str(new Symbol(((Symbol)vij.get(0)).getSym()));
 		if(getDfa().getF().contains(eDestino))
 		    P.add(new ProductionT3(new Str(new Symbol(currentE.getLabel())),
 					   consTmp));
 		P.add(new ProductionT3(new Str(new Symbol(currentE.getLabel())),
 				       Str.concat(consTmp,new Str(new Symbol(eDestino.getLabel())))));
      	
 	    }
 	}
	showStatus("\n\nP "+ P, debug_level);
	try{	  
	    setGt3(new Gtipo3(S,P,T,N));
	}catch(	ProductionNotValidTypeException e){
	    showStatus(e.getMessage());
	}
    }
    /** 
     * Muestra el status de la conversi�n cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gr�fico) esta funci�n decide como enviar los mensajes que se van dando en el proceso de conversi�n  .
     *
     * @param msg El mensaje para el status actual.
     *
     */
    protected void showStatus(String msg){
	showStatus(msg,Debug.DEBUG_ON);	
    }
    
    /**  
     * Muestra el status de la conversi�n cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gr�fico) esta funci�n decide como enviar los mensajes que se van dando en el proceso de conversi�n  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    protected void showStatus(String msg, int debug_level){
	Debug.print(msg,debug_level);	
    }
    /** 
     * Rutinas de prueba para la clase Dfa2Gtipo3.
     * La implementaci�n por omisi�n simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase Dfa2Gtipo3. \n"
			   +"Comentario: Esta clase toma un DFA y genera gram�tica tipo 3 equivalente.\n"
			   +"Autor: Ivan Hern�ndez Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
	try{ 
	    
	    Dfa2Gtipo3 foo = new Dfa2Gtipo3(new DFA(args[0]));
	    System.out.println(" \n\nEl DFA de entrada es " + foo.getDfa());
	    foo.doConvertion();
	    System.out.println(" \n\nLa Gtipo3 resultante es " + foo.getGt3());
	    
	}catch( Exception ouch){
	    ouch.printStackTrace(); 
	}

    }

}

/* Dfa2Gtipo3.java ends here. */
