/**
** <Gtipo32AF.java> -- Converts a type 2 grammar into an Finite Automata
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


package jaguar.grammar.tipo3;

import jaguar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.structures.*;
import jaguar.util.*;
import jaguar.grammar.tipo3.structures.*;
import java.io.*;

/** 
 * Dada una gramática tipo 3 lineal a la izquierda, Esta clase genera un AFN equivalente.
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Gtipo32AF{
    /**
     * El autómata finito equivalente a la gramática <code>gt3</code>
     */
    protected NDFA af;
    /**
     * El valor por omisión para af
     */
    public static final NDFA DEFAULT_AF=null;
    /**
     * funcion de acceso para obtener el valor de af
     * @return el valor actual de af
     * @see #af
     */
    public NDFA getAf(){
	return af;
    }
    /**
     * funcion de acceso para modificar af
     * @param new_af el nuevo valor para af
     * @see #af
     */
    public void setAf(NDFA new_af){
	af=new_af;
    }

    /**
     * La gramática que convertiremos a Autómata Finito
     */
    protected Gtipo3 gt3;
    /**
     * El valor por omisión para gt3
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
     * Recibe los valores para gt3.
     * Para el resto de los campos usa el valor por omision.
     * @param gt3 el valor con el que se inicalizará el campo gt3
     * @see #gt3
     * @see #DEFAULT_AF
     */
    public Gtipo32AF (Gtipo3 gt3){
	this.gt3=gt3;
	this.af=DEFAULT_AF;
    }
    
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_GT3
     * @see #DEFAULT_AF
     */
    public Gtipo32AF (){
	this(DEFAULT_GT3);	
    }

    /**
     * Esta función hace la conversión de una gramática tipo 3 lineal derecha a un NDFA. El NDFA resultante
     * está disponible usando la función getAf().
     */
    public void doConvertion(){
//	System.err.println("Gtipo32Af.doConvertion");
	doConvertion(Debug.DEBUG_ON);	 
    }

    public void doConvertion(int debug_level){

	showStatus("Construyendo un AFN equivalente", debug_level);
	Alphabet Sigma = new Alphabet(getGt3().getT());
	showStatus("\n Sigma = "+Sigma, debug_level);
	StateSet Q = new StateSet();
	Object nArray[] = getGt3().getN().toArray();
	for(int i = 0 ; i < nArray.length ; i++)
	    Q.add(new State(((Symbol)nArray[i]).getSym()));
	State S = new State(getGt3().getS().getSym());
	Q.add(S);
	showStatus("\nQ =" + Q, debug_level);
	State Z;
	int idx = 0;
	do{
	    Z = new State("Zf_"+idx);
	}while(Q.contains(Z));
	Q.add(Z);
	showStatus("\n Nuevo estado final Z =" + Z, debug_level);
	
	StateSet F = new StateSet(Z);
	StateSet Q0 = new StateSet(S);
	showStatus("\n Q0 ="+Q0, debug_level);
	showStatus("\n Construyendo la delta", debug_level);
	NDfaDelta d = new NDfaDelta();
	ProductionT3 currentProd;
	Object pArray[] = getGt3().getP().toArray();
	for(int i = 0 ; i < pArray.length ; i ++){
	    currentProd = (ProductionT3)pArray[i];
	    switch(currentProd.getConsecuente().length()){
	    case 2:{
		try{ 
		    d.addTransition(new State(currentProd.getAntecedente().getSymbol(0).getSym()),
				    currentProd.getConsecuente().getSymbol(0),
				    new StateSet(new State(currentProd.getConsecuente().getSymbol(1).getSym())));
		}catch( StrIndexOutOfBoundsException ouch){
		    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				       + " Out: " ); 
		    ouch.printStackTrace(); 
		}

	    }break;	    
	    case 1:{
		try{ 
		    d.addTransition(new State(currentProd.getAntecedente().getSymbol(0).getSym()),
				    currentProd.getConsecuente().getSymbol(0),
				    new StateSet(Z));
	    }catch( StrIndexOutOfBoundsException ouch){
		System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				   + " Out: " ); 
		ouch.printStackTrace(); 
	    }
	    }break;
	    }
	}
	showStatus(" d = ", debug_level);
	State pozo;
	idx = 0;
	do{
	    pozo = new State("Pozo_"+idx);
	}while(Q.contains(pozo));
	Q.add(pozo);
	showStatus("\n Generando estado nuevo pozo = " + pozo+"\n", debug_level);
	StateSet pozoStateSet = new StateSet(pozo);
	Object aArray [] = Sigma.toArray();
	for(int i = 0 ; i < aArray.length; i++){
	    d.addTransition(Z,(Symbol)aArray[i],pozoStateSet);
	    d.addTransition(pozo,(Symbol)aArray[i],pozoStateSet);
	}

	if(getGt3().getP().contains(new ProductionT3(new Str(getGt3().getS()),new Str())))
	    F.add(S);

	setAf(new NDFA(Sigma,Q,F,d,Q0));
    }

    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta
     * función decide como enviar los mensajes que se van dando en el
     * proceso de conversión .
     *
     * @param msg El mensaje para el status actual.
     *
     */
    protected void showStatus(String msg){
	showStatus(msg,Debug.DEBUG_ON);	
    }
    
    /**  
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta
     * función decide como enviar los mensajes que se van dando en el
     * proceso de conversión .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    protected void showStatus(String msg, int debug_level){
	Debug.print(msg,debug_level);	
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
    /** 
     * Rutinas de prueba para la clase Gtipo32AF.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase Gtipo32AF. \n"
			   +"Comentario: Dada una gramática tipo 3 lineal a la izquierda, Esta clase genera un AFN equivalente.\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
 	  
	try{
	    System.out.println("\n\n\tLeyendo la gramática de: " + args[0] + "\n");
	    Gtipo32AF gt32af = new Gtipo32AF(new Gtipo3(args[0]));
	    if(gt32af.getGt3().getLinealidad() == ProductionT3Set.L_IZQUIERDA){
		System.err.println("\n\nEste motor, solo convierte en Automata las gramáticas lineales izquierdas. La gramática:"
				   + gt32af.getGt3() + "\nEs lineal izquierda");
		System.exit(0);
	    }
	    System.err.println("Gramática de entrada:\n"+gt32af.getGt3()+"\n");
	    gt32af.doConvertion();
	    System.err.println("El AF equivalente es:\n"+gt32af.getAf()+"\n");
	    gt32af.getAf().toFile(new FileWriter("g32Af.ndfa"));
	}catch(Exception e){
	    e.printStackTrace();	    
	}
    }   
}

/* Gtipo32AF.java ends here. */
