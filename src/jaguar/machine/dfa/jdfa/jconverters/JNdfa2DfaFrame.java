/**
** <JNdfa2DfaFrame.java> -- The Frame to show all the convertion process
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


import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.jdfa.*;
import jaguar.machine.dfa.converters.*;
import java.io.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JNdfa2Dfa
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JNdfa2DfaFrame extends JConverterFrame{
    /**
     * El motor para la conversion
     */
    protected JNdfa2Dfa engineConverter;
    /**
     * funcion de acceso para obtener el valor de engineConverter
     * @return el valor actual de engineConverter
     * @see #engineConverter
     */
    public JNdfa2Dfa getEngineConverter(){
	return engineConverter;
    }
    /**
     * funcion de acceso para modificar engineConverter
     * @param new_engineConverter el nuevo valor para engineConverter
     * @see #engineConverter
     */
    public void setEngineConverter(JNdfa2Dfa new_engineConverter){
	engineConverter=new_engineConverter;
    }



    /**
     * El JNDFA que vamos a convertir a DFA
     */
    protected JNDFA jndfa;
    /**
     * funcion de acceso para obtener el valor de jndfa
     * @return el valor actual de jndfa
     * @see #jndfa
     */
    public JNDFA getJndfa(){
	return jndfa;
    }
    /**
     * funcion de acceso para modificar jndfa
     * @param new_jndfa el nuevo valor para jndfa
     * @see #jndfa
     */
    public void setJndfa(JNDFA new_jndfa){
	jndfa=new_jndfa;
    }

    /**
     * El JDFA equivalente resultante del proceso de conversion
     */
    protected JDFA jdfa;
    /**
     * funcion de acceso para obtener el valor de jdfa
     * @return el valor actual de jdfa
     * @see #jdfa
     */
    public JDFA getJdfa(){
	return jdfa;
    }
    /**
     * funcion de acceso para modificar jdfa
     * @param new_jdfa el nuevo valor para jdfa
     * @see #jdfa
     */
    public void setJdfa(JDFA new_jdfa){
	jdfa=new_jdfa;
    }

    /**
     * El canvas donde solo dibujaremos el DFA resultante
     */
    protected JDfaCanvas jdfacanvas;
    /**
     * funcion de acceso para obtener el valor de jdfacanvas
     * @return el valor actual de jdfacanvas
     * @see #jdfacanvas
     */
    public JDfaCanvas getJdfacanvas(){
	return jdfacanvas;
    }
    /**
     * funcion de acceso para modificar jdfacanvas
     * @param new_jdfacanvas el nuevo valor para jdfacanvas
     * @see #jdfacanvas
     */
    public void setJdfacanvas(JDfaCanvas new_jdfacanvas){
	jdfacanvas=new_jdfacanvas;
    }

    /**
     ** El canvas donde solo iluminaremos el JNDFA
     **/ 
    protected JNDfaCanvas jndfacanvas;
    /**
     * funcion de acceso para obtener el valor de jndfacanvas
     * @return el valor actual de jndfacanvas
     * @see #jndfacanvas
     */
    public JNDfaCanvas getJndfacanvas(){
	return jndfacanvas;
    }
    /**
     * funcion de acceso para modificar jndfacanvas
     * @param new_jndfacanvas el nuevo valor para jndfacanvas
     * @see #jndfacanvas
     */
    public void setJndfacanvas(JNDfaCanvas new_jndfacanvas){
	jndfacanvas=new_jndfacanvas;
    }

    
    /**
     * El frame donde se mostrará el ndfa
     */
    protected JInternalFrame jnframe;    
    
    /**
     * funcion de acceso para obtener el valor de jnframe
     * @return el valor actual de jnframe
     * @see #jnframe
     */
    public JInternalFrame getJnframe(){
	return jnframe;
    }
    /**
     * funcion de acceso para modificar jnframe
     * @param new_jnframe el nuevo valor para jnframe
     * @see #jnframe
     */
    public void setJnframe(JInternalFrame new_jnframe){
	jnframe=new_jnframe;
    }

    /**
     * El frame donde va a mostrarse el dfa
     */
    protected JInternalFrame jdframe;
    /**
     * funcion de acceso para obtener el valor de jdframe
     * @return el valor actual de jdframe
     * @see #jdframe
     */
    public JInternalFrame getJdframe(){
	return jdframe;
    }
    /**
     * funcion de acceso para modificar jdframe
     * @param new_jdframe el nuevo valor para jdframe
     * @see #jdframe
     */
    public void setJdframe(JInternalFrame new_jdframe){
	jdframe=new_jdframe;
    }

    
    JSplitPane splitPane;    

    public JNdfa2DfaFrame(String title){
	super(title,"Waiting for a NDFA to  convert... ");
    }

    public JNdfa2DfaFrame(){
	this("NDFA to DFA Converter");	
    }
    
    protected void setControls(){
	setControls("Convert NDFA");
    }
    

    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected JMenuBar createMenu(){
	return createMenu("Load NDFA to Convert...","Loads a new NDFA",
			  "Do Convertion", "Convert the loaded NDFA to an equivalent DFA",
			  "Save  DFA...", "Save the resulting DFA");
    }


    public void saveResultFromConvertion(){
	saveResultFromConvertion("Save DFA Equivalent ");
    }


    public boolean doConvertion(){
	setEngineConverter(new JNdfa2Dfa(getJndfa(),detailsArea));
	if(getEngineConverter() == null)
	    return false;	
	engineConverter.doConvertion();
	setJdfa((JDFA)engineConverter.getDfa());
	detailsArea.append("\n\nResulting DFA:\n" + getJdfa().toString());
	jdfacanvas = new JDfaCanvas(MACHINE_SIZE,jdfa);
	jdfa.initStatesPosition(MACHINE_SIZE);
	jdframe = new JInternalMachineFrame(jdfacanvas,"DFA " + getCurrentobjecttoconvert());
	jdframe.getContentPane().add(new JScrollPane(jdfacanvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jdframe.setSize(MACHINE_SIZE);
	jdframe.setLocation(new Point((int)MACHINE_SIZE.getWidth(),0));
	desktop.add(jdframe);
	jdframe.show();
	jdframe.setVisible(true);
	saveResultMenuItem.setText("Save DFA " + getCurrentobjecttoconvert());	
	return true;	
    }

    protected boolean loadOrigFromFile(){
	return loadOrigFromFile("Load NDFA to convert");
    }
    public void loadOrigFromFile(File file) throws Exception{
	jndfa = new JNDFA(file);
	jndfacanvas = new JNDfaCanvas(MACHINE_SIZE,jndfa);
	jndfa.initStatesPosition(MACHINE_SIZE);
	currentObjectToConvert++;		
	jnframe = new JInternalFrame("NDFA " + getCurrentobjecttoconvert(),true,true,true,true);
	jnframe.getContentPane().add(new JScrollPane(jndfacanvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jnframe.setSize(MACHINE_SIZE);
	desktop.add(jnframe);
	jnframe.show();
	jnframe.setVisible(true);
    }

    public void toFile(FileWriter fw){
	getJdfa().toFile(fw);
    }
    
    public static void main(String[] argv){
	JNdfa2DfaFrame f = new JNdfa2DfaFrame();
	f.show();	
    }
    
}

/* JNdfa2DfaFrame.java ends here. */
