/**
** <JGrammarFrameT3.java> -- A frame for type 3 grammars
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

import jaguar.grammar.tipo3.*;
import jaguar.grammar.jgrammar.*;
import java.io.*;
import jaguar.util.Debug;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.JStrConstructor;
import jaguar.grammar.exceptions.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import javax.swing.*;
import java.awt.event.*;

/** 
 * El frame para las gramaticas tipo 3
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JGrammarFrameT3 extends JGrammarFrame{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JGrammarFrameT3 (){
	super("JGrammarFrameT3");
    }
    protected void initGrammar(File file){
	 try{
	     setGrammar(new Gtipo3(file));
	     Debug.println("The Grammar is =>  " + getGrammar());
	     displayGrammar();
	     loadTest.setEnabled(true);
	     consTest.setEnabled(true);
	     replaceSymbol.setEnabled(true);
	 }catch( FileNotFoundException ouch){
	     System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				+ " file: " + file + " no existe !!!" ); 
	     ouch.printStackTrace(); 
	 }catch(ProductionNotValidTypeException pnv){
	     JOptionPane.showMessageDialog(null,"Error loading  Grammar T3 from file "+file+"\n\""+pnv.getMessage()+"\"","JGrammarFrameT3",JOptionPane.ERROR_MESSAGE);
	 }
	 catch(GrammarNotFoundException gnf){
	     JOptionPane.showMessageDialog(null,"Error loading Grammar T3 from file "+file+"\n\""+gnf.getMessage()+"\"","JGrammarFrameT3",JOptionPane.ERROR_MESSAGE);
	 }catch(Exception ouch){
	     JOptionPane.showMessageDialog(null,"Error loading Grammar T3 from file "+file+"\n\""+ouch.getMessage()+"\"","JGrammarFrameT3",JOptionPane.ERROR_MESSAGE);
	 }
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
    JMenuItem loadTest, consTest;
    
    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected void createMenu(){
	super.createMenu();
	JMenu mtest = new JMenu("Test...");
	mtest.setMnemonic(KeyEvent.VK_T);
	loadTest = new  JMenuItem("Load test String...",KeyEvent.VK_S);
	loadTest.getAccessibleContext().setAccessibleDescription("Loads a new JStr to test");
	loadTest.addActionListener(this);	
	loadTest.setActionCommand("loadtest");
	loadTest.setEnabled(false);	
	mtest.add(loadTest);
	
	consTest = new  JMenuItem("Build  test String...",KeyEvent.VK_C);
	consTest.getAccessibleContext().setAccessibleDescription("Build a new JStr to test");
	consTest.addActionListener(this);	
	consTest.setActionCommand("constest");
	consTest.setEnabled(false);
	consTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
	mtest.add(consTest);

	menuBar.add(mtest);
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("loadtest")) {
	    loadTestStringFromFile();	    
        }
	if (e.getActionCommand().equals("constest")){
	    JStrConstructor jcons = new JStrConstructor(this,getGrammar().getT(),"Grammar T3");
	    Debug.println("JStrConstructor - construyeron => " + jcons.getConstructedJStr());
	    loadTestString(jcons.getConstructedJStr());
	}
	super.actionPerformed(e);	
    }

    public boolean loadTestStringFromFile(){
	fc.setDialogTitle("Open String to execution");
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    try{
		Str str = new JStr(file, false);
		Debug.println("String from file => " + str);
		loadTestString(str);
	    }catch(Exception ex){
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null,"Error loading  Str file "+file,"JGrammarFrameT3",JOptionPane.ERROR_MESSAGE);
	    }
	}
	return true;	
    }

    protected void loadTestString(Str str){
	Gtipo3 currentGrammar  = (Gtipo3)getGrammar();
	if(! currentGrammar.genera(str)){
	    JOptionPane.showMessageDialog(null,"The string "+str+"\nisn't generated by the grammar","JGrammarFrameT3",JOptionPane.INFORMATION_MESSAGE);
	    return;
	}
	JOptionPane.showMessageDialog(null,"The string "+str+"\n is generated by the grammar, now i'll show you its derivation tree","JGrammarFrameT3",JOptionPane.INFORMATION_MESSAGE);
	JDerivationTreeT3 jdt = new JDerivationTreeT3(currentGrammar.reversePath(),str);
	jdt.show();
    }

    
    /** 
     * Rutinas de prueba para la clase JGrammarFrameT3.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase JGrammarFrameT3. \n"
			   +"Comentario: El frame para las gramaticas tipo 3\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
 	  
	JGrammarFrameT3 jgf = new JGrammarFrameT3();
	jgf.show();
    }

}

/* JGrammarFrameT3.java ends here. */
