/**
** <JGrammarFrame.java> -- The frame to show grammars
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


package jaguar.grammar.jgrammar;

import jaguar.structures.*;
import jaguar.grammar.*;
import jaguar.grammar.exceptions.*;
import jaguar.grammar.tipo2.*;
import jaguar.grammar.tipo3.*;
import jaguar.util.Debug;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.io.File;
import java.util.*;

/** 
 * El frame para las gramaticas.
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:02 $
 */
public abstract class JGrammarFrame extends JFrame implements ComponentListener, ActionListener {

    private Dimension dimension;

    
    protected JFileChooser fc;
    
    
    protected JScrollPane jScrollPaneCanvas;
    
    
    public static final Dimension DEFAULT_SIZE  = new Dimension(600,600);    

    /** 
     * El canvas donde desplegaremos la gramática 
     */
    protected JGrammarCanvas grammarCanvas;
    
    
    /**
     * Get the value of jScrollPaneCanvas.
     * @return Value of jScrollPaneCanvas.
     */
    public JScrollPane getJScrollPaneCanvas() {
	return jScrollPaneCanvas;
    }
    

    /**
     * Set the value of jScrollPaneCanvas.
     * @param v  Value to assign to jScrollPaneCanvas.
     */
    public void setJScrollPaneCanvas(JScrollPane  v) {this.jScrollPaneCanvas = v;}

    
    /**
     * funcion de acceso para obtener el valor de grammar
     * @return el valor actual de grammar
     * @see JGrammarCanvas#grammar
     */
    public Grammar getGrammar(){
	return grammarCanvas.getGrammar();
    }
    
    
    /**
     * funcion de acceso para modificar grammar
     * @param new_grammar el nuevo valor para grammar
     * @see JGrammarCanvas#grammar
     */
    public void setGrammar(Grammar new_grammar){
	grammarCanvas.setGrammar(new_grammar);
    }
    
    

    
    public JGrammarFrame(){
	this((Grammar)null);	
    }

    public JGrammarFrame(Grammar _grammar){
	this(_grammar,"JGrammarFrame");
    }

    public JGrammarFrame(String title){
	this(null,title);
    }
    
    public JGrammarFrame(Grammar _grammar, String title){
	super(title);
	grammarCanvas = new JGrammarCanvas();
	grammarCanvas.setBackground(Color.white);
	setGrammar(_grammar);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {dispose();}
		public void windowOpened(WindowEvent e) {}
	    });	
	addComponentListener(this);
	setSize(DEFAULT_SIZE);	    
	fc = new JFileChooser();
	createMenu();
	setJMenuBar(menuBar);

	jScrollPaneCanvas = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
	jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
	jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);
	getContentPane().add(createJPanelBorder(jScrollPaneCanvas,"G"), BorderLayout.CENTER);
	// Esto llama al canvas para que organice
	displayGrammar();	
	jScrollPaneCanvas.setViewportView(grammarCanvas);
 	getContentPane().validate();
    }

    
    /**
     * funcion de acceso para obtener el valor de grammarCanvas
     * @return el valor actual de grammarCanvas
     * @see #grammarCanvas
     */
    public JGrammarCanvas getGrammarcanvas(){
	return grammarCanvas;
    }
    
    /**
     * funcion de acceso para modificar grammarCanvas
     * @param new_grammarCanvas el nuevo valor para grammarCanvas
     * @see #grammarCanvas
     */
    public void setGrammarcanvas(JGrammarCanvas new_grammarCanvas){
	grammarCanvas = new_grammarCanvas;
    }
    
    public void displayGrammar(){
	grammarCanvas.setBackground(Color.white);	
	grammarCanvas.displayGrammar();
 	getContentPane().validate();
    }       
    
    /**
     * Crea una instancia de Gramar a partir de la descripción en un archivo, ademlas inicializa los botones y la configuración en general del
     * JGramarFrame
     * @param file es el archivo donde esta la descripción de la máquina en cuestión.
     */
    abstract protected void initGrammar(File file);
    
    public boolean loadGrammarFromFile(){
	fc.setDialogTitle("Open Grammar Definition");	
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    initGrammar(file);
	}
	return true;	
    }

    
    /**
     *  La barra de menu 
     */
    protected JMenuBar menuBar;
    
    
    /**
     * funcion de acceso para obtener el valor de menuBar
     * @return el valor actual de menuBar
     * @see #menuBar
     */
    public JMenuBar getMenubar(){
	return menuBar;
    }

    
    /**
     * funcion de acceso para modificar menuBar
     * @param new_menuBar el nuevo valor para menuBar
     * @see #menuBar
     */
    public void setMenubar(JMenuBar new_menuBar){
	menuBar=new_menuBar;
    }

    /**
     * Crea el menú con las configuraciones básicas de este Frame
     */
    protected void createMenu(){
	menuBar = new JMenuBar();
	JMenu menu = new JMenu("File");
	menu.setMnemonic(KeyEvent.VK_F);

	JMenuItem loadJgrammar = new  JMenuItem("Load Grammar...",KeyEvent.VK_L);
	loadJgrammar.getAccessibleContext().setAccessibleDescription("Loads a new Machine");
	loadJgrammar.addActionListener(this);	
	loadJgrammar.setActionCommand("load");
	loadJgrammar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));	
	menu.add(loadJgrammar);
	
	JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });
	quit.setMnemonic(KeyEvent.VK_Q);
	menu.add(quit);
	menuBar.add(menu);


	JMenu utilMenu = new JMenu("Util");
	menu.setMnemonic(KeyEvent.VK_U);
	replaceSymbol = new JMenuItem("Replace Symbols...",KeyEvent.VK_R);
	replaceSymbol.getAccessibleContext().setAccessibleDescription("Reemplaza símbolos de la gramática");
	replaceSymbol.addActionListener(this);
	replaceSymbol.setActionCommand("replace");
	replaceSymbol.setEnabled(false);	
	replaceSymbol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0));		

	utilMenu.add(replaceSymbol);
	menuBar.add(utilMenu);	
    }
    
    protected JMenuItem replaceSymbol;
    
    
    public void actionPerformed(ActionEvent e) {
	Debug.println("ActionEvent["+e.getActionCommand()+"]");

	if (e.getActionCommand().equals("load")) {
	    loadGrammarFromFile();
        }
	if (e.getActionCommand().equals("replace")) {
	    replaceSymbols();	    
        }
    }
    
    static public JPanel createJPanelBorder(Component c){
	return createJPanelBorder(c,null);
    }

    static public JPanel createJPanelBorder(Component c, String borderTitle){	
	return createJPanelBorder(c,borderTitle,BorderFactory.createEtchedBorder());
    }    

    static public JPanel createJPanelBorder(Component c, String borderTitle, Border border){
	JPanel bcomp = new JPanel(false);	
        bcomp.setLayout(new GridLayout(1, 1));
        bcomp.add(c);
	if(borderTitle!=null){
	    TitledBorder title = BorderFactory.createTitledBorder(border,borderTitle);
	    title.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
	    title.setTitlePosition(TitledBorder.DEFAULT_POSITION);
	    bcomp.setBorder(title);
	}
	else
	    bcomp.setBorder(border);
	return bcomp;	
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
     * Rutinas de prueba para la clase JGrammarFrame.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase JGrammarFrame. \n"
			   +"Comentario: el frame para las gramaticas\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
    }

    public void componentHidden(ComponentEvent e) {}
    
    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {}
    
    public void componentShown(ComponentEvent e) {}
    
    protected void replaceSymbols(){
	
	JGrammarSymbolReplacer jgsr = new JGrammarSymbolReplacer(getGrammar().getN(),getGrammar().getT());
	JScrollPane sp = new JScrollPane(jgsr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	Debug.println("\nAntes: "+getGrammar());	    
	Object[] options = { "OK", "CANCEL" };
 	int response = JOptionPane.showOptionDialog(this,sp,"Replacer",
 						    JOptionPane.DEFAULT_OPTION,
 						    JOptionPane.PLAIN_MESSAGE,
 						    null, options, options[0]);
	
	if(response == JOptionPane.OK_OPTION){	    
	    Hashtable symbolsToReplace = jgsr.getPairsToReplace();
	    Object a[] = symbolsToReplace.keySet().toArray();
	    for(int i = 0 ; i < a.length; i++)
		getGrammar().replaceSymbol((Symbol)a[i],(Symbol)symbolsToReplace.get(a[i]));
	    displayGrammar();
	}
	Debug.println("\nDespués: "+getGrammar());	    
    }
}

/* JGrammarFrame.java ends here. */
