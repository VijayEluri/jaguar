/**
** <JDfa2Gtipo3.java> -- The graphical extension to the converter's engine
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


package jaguar.machine.dfa.jdfa.jconverters;

import jaguar.machine.dfa.jdfa.*;
import jaguar.machine.dfa.converters.*;
import jaguar.machine.dfa.*;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.util.*;
import jaguar.util.jutil.*;
import java.io.*;
import javax.swing.JTextArea;

/** 
 * La extensión gráfica para el convertidor de DFA a Gramatica Tipo 3
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JDfa2Gtipo3 extends Dfa2Gtipo3 implements JConverter {
     /**
      * El area donde escribiremos el estado actual
      */
    protected JTextArea detailsArea;
    
    
    /**
     * El valor por omisión para detailsArea
     */
    public static final JTextArea DEFAULT_DETAILSAREA=null;
    

    /**
     * funcion de acceso para obtener el valor de detailsArea
     * @return el valor actual de detailsArea
     * @see #detailsArea
     */
    public JTextArea getDetailsarea(){
	return detailsArea;
    }
    
    
    /**
     * funcion de acceso para modificar detailsArea
     * @param new_detailsArea el nuevo valor para detailsArea
     * @see #detailsArea
     */
    public void setDetailsarea(JTextArea new_detailsArea){
	detailsArea=new_detailsArea;
    }
    
    
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JDfa2Gtipo3 (){
	super();	
    }
    
    
    public JDfa2Gtipo3(JTextArea details){
	this();	
	detailsArea = details;	
    }
    
    
    /**
     * Constructor.
     * Recibe los valores para ndfa.
     * Para el resto de los campos usa el valor por omision.
     * @param dfaToConvert el valor con el que se inicalizará el campo dfaOrig
     * @see #dfa
     * @see #DEFAULT_DFA
     */
    public JDfa2Gtipo3 (DFA dfaToConvert){
	this(dfaToConvert,null);
    }
    /**
     * Constructor.
     * Recibe los valores para dfa.
     * Para el resto de los campos usa el valor por omision.
     * @param dfaToConvert el valor con el que se inicalizará el campo dfaOrig
     * @see #dfa
     * @see #DEFAULT_DFA
     */
    public JDfa2Gtipo3 (DFA dfaToConvert,JTextArea details){
	super(dfaToConvert);
	detailsArea = details;	
    }
    
    public void doConvertion(){
	System.err.println("JDfa2Gtipo3.doConvertion");	
	doConvertion(Debug.DEBUG_OFF);	
    }

    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    public void showStatus(String msg, int debug_level){
	if(detailsArea != null)
	    detailsArea.append(msg);
	else Debug.println("\n\nWARNING: the details area in the JDfa2Gtipo3 engine has not been initialized!!!");	
    }
    
    /** 
     * Rutinas de prueba para la clase JDfa2Gtipo3.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase JDfa2Gtipo3. \n"
			   +"Comentario: La extensión gráfica para el convertidor de NDFA a DFA\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
 	  
    }

}

/* JDfa2Gtipo3.java ends here. */
