/**
** <JGtipo32AFFrame.java> -- The converter frame to show the convertion
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


package jaguar.grammar.tipo3.jtipo3;

import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.grammar.jgrammar.*;
import jaguar.grammar.tipo3.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.jdfa.*;
import java.io.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JDfa2Gtipo3
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JGtipo32AFFrame extends JConverterFrame{    
    /**
     * El motor para la conversion
     */
    protected JGtipo32AF engineConverter;
    /**
     * funcion de acceso para obtener el valor de engineConverter
     * @return el valor actual de engineConverter
     * @see #engineConverter
     */
    public JGtipo32AF getEngineConverter(){
	return engineConverter;
    }
    /**
     * funcion de acceso para modificar engineConverter
     * @param new_engineConverter el nuevo valor para engineConverter
     * @see #engineConverter
     */
    public void setEngineConverter(JGtipo32AF new_engineConverter){
	engineConverter=new_engineConverter;

    }


    /**
     * La gramática que convertiremos en AF
     */
    protected Gtipo3 grammarOrig;
    /**
     * funcion de acceso para obtener el valor de grammarOrig
     * @return el valor actual de grammarOrig
     * @see #grammarOrig
     */
    public Gtipo3 getGrammarorig(){
	return grammarOrig;
    }
    /**
     * funcion de acceso para modificar grammarOrig
     * @param new_grammarOrig el nuevo valor para grammarOrig
     * @see #grammarOrig
     */
    public void setGrammarorig(Gtipo3 new_grammarOrig){
	grammarOrig=new_grammarOrig;
    }
    
    /**
     * El AF resultante
     */
    protected JNDFA jndfaR;
    /**
     * funcion de acceso para obtener el valor de jndfaR
     * @return el valor actual de jndfaR
     * @see #jndfaR
     */
    public JNDFA getJndfar(){
     return jndfaR;
    }
    /**
     * funcion de acceso para modificar jndfaR
     * @param new_jndfaR el nuevo valor para jndfaR
     * @see #jndfaR
     */
    public void setJndfar(JNDFA new_jndfaR){
	jndfaR=new_jndfaR;
    }



    /** El canvas que se envargar'a de mostrar la gramatica de entrada, i.e. la que vamos a convertir **/
    protected JGrammarCanvas jgrammarCanvasOrig;
    /**
     * funcion de acceso para obtener el valor de jgrammarCanvasOrig
     * @return el valor actual de jgrammarCanvasOrig
     * @see #jgrammarCanvasOrig
     */
    public JGrammarCanvas getJgrammarcanvasorig(){
	return jgrammarCanvasOrig;
    }
    /**
     * funcion de acceso para modificar jgrammarCanvasOrig
     * @param new_jgrammarCanvasOrig el nuevo valor para jgrammarCanvasOrig
     * @see #jgrammarCanvasOrig
     */
    public void setJgrammarcanvasorig(JGrammarCanvas new_jgrammarCanvasOrig){
	jgrammarCanvasOrig=new_jgrammarCanvasOrig;
    }
    
    /**
     * El canvas donde mostraremos el AF resultante
     */
    protected JNDfaCanvas jndfaCanvas;
    /**
     * funcion de acceso para obtener el valor de jndfaCanvas
     * @return el valor actual de jndfaCanvas
     * @see #jndfaCanvas
     */
    public JNDfaCanvas getJndfacanvas(){
	return jndfaCanvas;
    }
    /**
     * funcion de acceso para modificar jndfaCanvas
     * @param new_jndfaCanvas el nuevo valor para jndfaCanvas
     * @see #jndfaCanvas
     */
    public void setJndfacanvas(JNDfaCanvas new_jndfaCanvas){
	jndfaCanvas=new_jndfaCanvas;
    }
    
    
    /**
     * El frame donde se mostrará el ndfa
     */
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
     * El frame donde pondremos el canvas del AF resultante ;
    **/
     protected JInternalFrame jresultFrame;
    /**
     * funcion de acceso para obtener el valor de jresultFrame
     * @return el valor actual de jresultFrame
     * @see #jresultFrame
     */
    public JInternalFrame getJresultframe(){
	return jresultFrame;
    }
    /**
     * funcion de acceso para modificar jresultFrame
     * @param new_jresultFrame el nuevo valor para jresultFrame
     * @see #jresultFrame
     */
    public void setJresultframe(JInternalFrame new_jresultFrame){
	jresultFrame=new_jresultFrame;
    }

    
    JSplitPane splitPane;    

    public JGtipo32AFFrame(String title){
	super(title,"Waiting for a Gtipo3 to transform... ");
    }
    
    public JGtipo32AFFrame(){
	this("Gramática T3 --> DFA");
    }

    
    protected void setControls(){
	setControls("Transform Grammar T3");
    }    

    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected JMenuBar createMenu() {
	return createMenu("Load Grammar T3 to Tranform...","Loads a new Grammar T3 to transform into a AF ","Transform",
			  "Transform the loaded Grammar T3 to an equivalent AF","Save  AF...", "Save the resulting AF");
    }

    public void saveResultFromConvertion(){
	saveResultFromConvertion("Save Resulting AF ");
    }
    
    public boolean doConvertion(){
	setEngineConverter(new JGtipo32AF(getGrammarorig(),detailsArea));
	if(getEngineConverter() == null)
	    return false;	
	engineConverter.doConvertion();
	setJndfar(new JNDFA(engineConverter.getAf()));
	detailsArea.append("\n\nResulting AF:\n" + getJndfar());
	jndfaCanvas = new JNDfaCanvas(MACHINE_SIZE,getJndfar());
	jndfaR.initStatesPosition(MACHINE_SIZE);
	jresultFrame = new JInternalFrame("AF " + getCurrentobjecttoconvert(),true,true,true,true);
	jresultFrame.getContentPane().add(new JScrollPane(jndfaCanvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jresultFrame.setSize(MACHINE_SIZE);
	jresultFrame.setLocation(new Point((int)MACHINE_SIZE.getWidth(),0));
	desktop.add(jresultFrame);
	jresultFrame.show();
	jresultFrame.setVisible(true);
	saveResultMenuItem.setText("Save T3 Grammar " + getCurrentobjecttoconvert());	
	return true;	
    }

    protected boolean loadOrigFromFile(){
	return loadOrigFromFile("Load DFA to minimize");
    }

    protected void loadOrigFromFile(File file)  throws FileNotFoundException{
	try{ 
	    grammarOrig = new Gtipo3(file);	
	}catch( Exception ouch){
	    ouch.printStackTrace();
	    JOptionPane.showMessageDialog(null,"Error loading Grammar T3 from file " + file +"\n\""+ouch.getMessage()+"\" ","Gtipo3 -> AF",JOptionPane.ERROR_MESSAGE);
	    return;
	}

	jgrammarCanvasOrig = new JGrammarCanvas(MACHINE_SIZE,grammarOrig);
	currentObjectToConvert++;		
	jorigframe = new JInternalFrame("JGrammar Tipo3 Orig " + getCurrentobjecttoconvert(),true,true,true,true);
	jorigframe.getContentPane().add(new JScrollPane(jgrammarCanvasOrig, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jorigframe.setSize(MACHINE_SIZE);
	desktop.add(jorigframe);

	jorigframe.show();
	jorigframe.setVisible(true);

    }

    public void toFile(FileWriter fw){
	getJndfar().toFile(fw);
    }
    public static void main(String[] argv){
	JGtipo32AFFrame f = new JGtipo32AFFrame();
	f.show();	
    }
}

/* JGtipo32AFFrame.java ends here. */
