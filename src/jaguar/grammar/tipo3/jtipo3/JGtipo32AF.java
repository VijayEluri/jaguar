/**
** <JGtipo32AF.java> -- All the graphical extensions to Gtipo32AF 
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


package jaguar.grammar.tipo3.jtipo3;

import jaguar.util.*;
import jaguar.util.jutil.*;
import java.io.*;
import jaguar.grammar.tipo3.*;
import javax.swing.*;

/** 
 * La extensi'on gr'afica para el convertidor Gtipo3 -> AF
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JGtipo32AF extends Gtipo32AF implements JConverter{
    /**
     * El 'area donde escribiremos el estado actual
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
     * Constructor.
     * Recibe los valores para detailsArea.
     * Para el resto de los campos usa el valor por omision.
     * @param detailsArea el valor con el que se inicalizar� el campo detailsArea
     * @see #detailsArea
     */
    public JGtipo32AF (JTextArea detailsArea){
	this();
	this.detailsArea=detailsArea;

    }
    /**
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_DETAILSAREA
     */
    public JGtipo32AF (){
	super();
	this.detailsArea=DEFAULT_DETAILSAREA;
    }

    public JGtipo32AF(Gtipo3 gt3){
	this(gt3,null);
    }

    public JGtipo32AF(Gtipo3 gt3, JTextArea details){
	super(gt3);
	detailsArea =details;
    }

    public void doConvertion(){
//	System.err.println("JGtipo32AF.doConvertion");
	doConvertion(Debug.DEBUG_OFF);	
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
	else Debug.println("\n\nWARNING: the details area in the JDfa2Gtipo3 engine has not been initialized!!!");	
    }

}

/* JGtipo32AF.java ends here. */
