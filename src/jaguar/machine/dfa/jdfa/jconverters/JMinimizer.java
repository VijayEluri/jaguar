/**
** <JMinimizer.java> -- The graphical extension to the minimizer's engine
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


package jaguar.machine.dfa.jdfa.jconverters;

import jaguar.machine.dfa.jdfa.*;
import jaguar.machine.dfa.converters.*;
import jaguar.machine.dfa.*;
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import java.io.*;
import javax.swing.JTextArea;

 /** 
 * La extensi�n gr�fica para el convertidor de NDFA a DFA
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JMinimizer extends Minimizer implements JConverter{
     /**
      * El area donde escribiremos el estado actual
      */
    protected JTextArea detailsArea;
    /**
     * El valor por omisi�n para detailsArea
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
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JMinimizer (){
	super();	
    }

    public JMinimizer(JTextArea details){
	this();	
	detailsArea = details;	
    }
    
    /**
     * Constructor recibe el nombre  del archivo donde se especifica el NDFA  a convertir
     * Para el resto de los campos usa el valor por omisi�n
     * @param dfaOrigFileName El nombre del archivo donde se especifica el NDFA
     */
    public JMinimizer(String dfaOrigFileName)throws Exception{
	this(dfaOrigFileName, null);	
    }
        /**
     * Constructor recibe el nombre  del archivo donde se especifica el NDFA  a convertir
     * Para el resto de los campos usa el valor por omisi�n
     * @param dfaOrigFileName el nombre del archivo donde se especifica el NDFA
     */
    public JMinimizer(String dfaOrigFileName, JTextArea details)throws Exception{
	super(dfaOrigFileName);
	detailsArea = details;	
    }
        /**
     * Constructor.
     * Recibe los valores para ndfa.
     * Para el resto de los campos usa el valor por omision.
     * @param dfaToMinimize el valor con el que se inicalizar� el campo dfaOrig
     * @see #dfaOrig
     */
    public JMinimizer (DFA dfaToMinimize){
	this(dfaToMinimize,null);
    }
    
    /**
     * Constructor.
     * Recibe los valores para ndfa.
     * Para el resto de los campos usa el valor por omision.
     * @param dfaToMinimize el valor con el que se inicalizar� el campo dfaOrig
     * @see #dfaOrig
     */
    public JMinimizer (DFA dfaToMinimize,JTextArea details){
	super(dfaToMinimize);
	detailsArea = details;	
    }

    public void doConvertion(){
	System.err.println("JMinimizer.doConvertion");	
	doConvertion(Debug.DEBUG_OFF);	
	JDFA foo  = new JDFA(getDfaMinimized());
	setDfaMinimized(foo);
    }

    /** 
     * Muestra el status de la conversi�n cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gr�fico) esta funci�n decide como enviar los mensajes que se van dando en el proceso de conversi�n  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    public void showStatus(String msg, int debug_level){
	if(detailsArea != null)
	    detailsArea.append(msg);
	else Debug.println("\n\nWARNING: the details area in the JMinimizer engine has not been initialized!!!");	
    }
}

/* JMinimizer.java ends here. */
