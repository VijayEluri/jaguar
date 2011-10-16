/**
** <JMinimizerFrame.java> -- The Frame to show all the minimization process
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
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.jdfa.*;
import java.io.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JMinimizer
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JMinimizerFrame extends JConverterFrame{    
    /**
     * El motor para la conversion
     */
    protected JMinimizer engineConverter;
    /**
     * funcion de acceso para obtener el valor de engineConverter
     * @return el valor actual de engineConverter
     * @see #engineConverter
     */
    public JMinimizer getEngineConverter(){
	return engineConverter;
    }
    /**
     * funcion de acceso para modificar engineConverter
     * @param new_engineConverter el nuevo valor para engineConverter
     * @see #engineConverter
     */
    public void setEngineConverter(JMinimizer new_engineConverter){
	engineConverter=new_engineConverter;
    }
    /**
     * El JDFA  que vamos a minimizar
     */
    protected JDFA jdfaOrig;
    /**
     * funcion de acceso para obtener el valor de jdfaOrig
     * @return el valor actual de jdfaOrig
     * @see #jdfaOrig
     */
    public JDFA getJdfaorig(){
	return jdfaOrig;
    }
    /**
     * funcion de acceso para modificar jdfaOrig
     * @param new_jdfaOrig el nuevo valor para jdfaOrig
     * @see #jdfaOrig
     */
    public void setJdfaorig(JDFA new_jdfaOrig){
	jdfaOrig=new_jdfaOrig;
    }

    /**
     * El JDFA equivalente resultante del proceso de minimización
     */
    protected JDFA jdfaMinimized;
    /**
     * funcion de acceso para obtener el valor de jdfaMinimized
     * @return el valor actual de jdfaMinimized
     * @see #jdfaMinimized
     */
    public JDFA getJdfaminimized(){
	return jdfaMinimized;
    }
    /**
     * funcion de acceso para modificar jdfaMinimized
     * @param new_jdfaMinimized el nuevo valor para jdfaMinimized
     * @see #jdfaMinimized
     */
    public void setJdfaminimized(JDFA new_jdfaMinimized){
	jdfaMinimized=new_jdfaMinimized;
    }

    /**
     * El canvas donde solo dibujaremos el DFA original que minimizaremos 
     */
    protected JDfaCanvas jdfacanvasOrig;
    /**
     * funcion de acceso para obtener el valor de jdfacanvasOrig
     * @return el valor actual de jdfacanvasOrig
     * @see #jdfacanvasOrig
     */
    public JDfaCanvas getJdfacanvasorig(){
	return jdfacanvasOrig;
    }
    /**
     * funcion de acceso para modificar jdfacanvasOrig
     * @param new_jdfacanvasOrig el nuevo valor para jdfacanvasOrig
     * @see #jdfacanvasOrig
     */
    public void setJdfacanvasorig(JDfaCanvas new_jdfacanvasOrig){
	jdfacanvasOrig=new_jdfacanvasOrig;
    }

    /**
     * El canvas donde solo dibujaremos el DFA equivalente resultante del proceso de minimización 
     */
    protected JDfaCanvas jdfacanvasMinimized;
    /**
     * funcion de acceso para obtener el valor de jdfacanvasMinimized
     * @return el valor actual de jdfacanvasMinimized
     * @see #jdfacanvasMinimized
     */
    public JDfaCanvas getJdfacanvasminimized(){
	return jdfacanvasMinimized;
    }
    /**
     * funcion de acceso para modificar jdfacanvasMinimized
     * @param new_jdfacanvasMinimized el nuevo valor para jdfacanvasMinimized
     * @see #jdfacanvasMinimized
     */
    public void setJdfacanvasminimized(JDfaCanvas new_jdfacanvasMinimized){
	jdfacanvasMinimized=new_jdfacanvasMinimized;
    }
    
    /**
     * El frame donde se mostrará el ndfa
     */
//    protected JFrame jorigframe;
    protected JInternalFrame jorigframe;    
    
    /**
     * funcion de acceso para obtener el valor de jorigframe
     * @return el valor actual de jorigframe
     * @see #jorigframe
     */
    public JInternalFrame getJorigframe(){
	return jorigframe;
    }
    /**
     * funcion de acceso para modificar jorigframe
     * @param new_jorigframe el nuevo valor para jorigframe
     * @see #jorigframe
     */
    public void setJorigframe(JInternalFrame new_jorigframe){
	jorigframe=new_jorigframe;
    }

    /**
     * El frame donde va a mostrarse el dfa
     */
    protected JInternalFrame jminimizedframe;
    /**
     * funcion de acceso para obtener el valor de jminimizedframe
     * @return el valor actual de jminimizedframe
     * @see #jminimizedframe
     */
    public JInternalFrame getJminimizedframe(){
	return jminimizedframe;
    }
    /**
     * funcion de acceso para modificar jminimizedframe
     * @param new_jminimizedframe el nuevo valor para jminimizedframe
     * @see #jminimizedframe
     */
    public void setJminimizedframe(JInternalFrame new_jminimizedframe){
	jminimizedframe=new_jminimizedframe;
    }

    
    JSplitPane splitPane;    

    public JMinimizerFrame(String title){
	super(title,"Waiting for a DFA to minimize... ");
    }
    
    public JMinimizerFrame(){
	this("DFA Minimizer");
    }

    
    protected void setControls(){
	setControls("Minimize DFA");
    }    

    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected JMenuBar createMenu() {
	return createMenu("Load DFA to Minimize...","Loads a new DFA to Minimize","Do Minimization",
			  "Minimize the loaded DFA to an equivalent DFA","Save  Min DFA...", "Save the resulting Min DFA");
    }

    public void saveResultFromConvertion(){
	saveResultFromConvertion("Save DFA Minimized ");
    }
    
    public boolean doConvertion(){
	setEngineConverter(new JMinimizer(getJdfaorig(),detailsArea));
	if(getEngineConverter() == null)
	    return false;	
	engineConverter.doConvertion();
	setJdfaminimized((JDFA)engineConverter.getDfaMinimized());
	detailsArea.append("\n\nResulting DFA:\n" + getJdfaminimized().toString());
	jdfacanvasMinimized = new JDfaCanvas(MACHINE_SIZE,getJdfaminimized());
	jdfaMinimized.initStatesPosition(MACHINE_SIZE);
//	jminimizedframe = new JInternalFrame("DFA " + getCurrentobjecttoconvert(),true,true,true,true);
	jminimizedframe = new JInternalMachineFrame(jdfacanvasMinimized,"DFA " + getCurrentobjecttoconvert());
	jminimizedframe.getContentPane().add(new JScrollPane(jdfacanvasMinimized, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jminimizedframe.setSize(MACHINE_SIZE);
	jminimizedframe.setLocation(new Point((int)MACHINE_SIZE.getWidth(),0));
	desktop.add(jminimizedframe);
	jminimizedframe.show();
	jminimizedframe.setVisible(true);
	saveResultMenuItem.setText("Save DFA " + getCurrentobjecttoconvert());	
	return true;	
    }

    protected boolean loadOrigFromFile(){
	return loadOrigFromFile("Load DFA to minimize");
    }

    protected void loadOrigFromFile(File file)  throws Exception{
	jdfaOrig = new JDFA(file);
	jdfacanvasOrig = new JDfaCanvas(MACHINE_SIZE,jdfaOrig);
	jdfaOrig.initStatesPosition(MACHINE_SIZE);
	currentObjectToConvert++;		
	jorigframe = new JInternalFrame("DFA Orig " + getCurrentobjecttoconvert(),true,true,true,true);
	jorigframe.getContentPane().add(new JScrollPane(jdfacanvasOrig, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jorigframe.setSize(MACHINE_SIZE);
	desktop.add(jorigframe);
	jorigframe.show();
	jorigframe.setVisible(true);
    }

    public void toFile(FileWriter fw){
	getJdfaminimized().toFile(fw);
    }
    public static void main(String[] argv){
	JMinimizerFrame f = new JMinimizerFrame();
	f.show();	
    }
}

/* JMinimizerFrame.java ends here. */
