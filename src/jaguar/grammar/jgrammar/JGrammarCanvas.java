/**
** <JGrammarCanvas.java> -- The canvas to show grammars
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


import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.*;
import jaguar.grammar.structures.*;
import jaguar.structures.*;
import jaguar.grammar.*;
import jaguar.util.*;
import jaguar.util.jutil.*;

/** 
 * El canvas donde se desplegarán las gramáticas
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:02 $
 */

public class JGrammarCanvas extends JPanel {
    /**
     * La gramática que dibujará el canvas
     */
    protected Grammar grammar;
    /**
     * El valor por omisión para grammar
     */
    public static final Grammar DEFAULT_GRAMMAR=null;
    /**
     * funcion de acceso para obtener el valor de grammar
     * @return el valor actual de grammar
     * @see #grammar
     */
    public Grammar getGrammar(){
	return grammar;
    }
    /**
     * funcion de acceso para modificar grammar
     * @param new_grammar el nuevo valor para grammar
     * @see #grammar
     */
    public void setGrammar(Grammar new_grammar){
	grammar=new_grammar;
    }

    /** 
     * Se encarga de desplegar gráficamente la gramática asociada.
     */

    public void displayGrammar(){
	if(getGrammar() ==  null){
	    Debug.println("\nNo grammar loaded");
	    return;
	}
	removeAll();
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	JTextPane textPane = new JTextPane(new MachineGrammarStyledDocument(getGrammar().getN()));	
//	JTextPane textPane = new JTextPane(new GrammarStyledDocument(getGrammar().getN()));	
	textPane.setEditable(false);
//	JPanel auxPanel = JGrammarFrame.createJPanelBorder(new JLabel(getGrammar().getN().toString()), "N");
	JPanel auxPanel = JGrammarFrame.createJPanelBorder(textPane, "N");
	auxPanel.setBackground(Color.white);
	auxPanel.setToolTipText("Press F7 to replace symbols");	
 	add(auxPanel);
	textPane = new JTextPane(new MachineGrammarStyledDocument(getGrammar().getT()));	
	textPane.setEditable(false);
 	auxPanel = JGrammarFrame.createJPanelBorder(textPane, "T");	

	auxPanel.setBackground(Color.white);	
	auxPanel.setToolTipText("Press F7 to replace symbols");
 	add(auxPanel);	

  	auxPanel = JGrammarFrame.createJPanelBorder(new JLabel(" " + getGrammar().getS().toString()), "S");
	auxPanel.setBackground(Color.white);	
 	add(auxPanel);

 	JPanel producciones = new JPanel();
	producciones.setLayout(new BoxLayout(producciones, BoxLayout.Y_AXIS));
 	Object oArray[] = getGrammar().getP().toArray();	
 	for(int i = 0 ; i < oArray.length ; i++){
 	    producciones.add(createProduction((Production)oArray[i]));

 	}
	JScrollPane jScrollPaneProductions = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	jScrollPaneProductions.getHorizontalScrollBar().setUnitIncrement(20);
	jScrollPaneProductions.getVerticalScrollBar().setUnitIncrement(20);
	jScrollPaneProductions.setViewportView(producciones);
 	add(JGrammarFrame.createJPanelBorder(jScrollPaneProductions,"P"));
    }    

    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JGrammarCanvas (){
	super();	
    }

    private Dimension size = new Dimension(500,500);
    
    public JGrammarCanvas(Dimension _dimension){
	this();
	setBackground(Color.white);
	size = _dimension;
    }

    public JGrammarCanvas(Dimension _dimension, Grammar grammar){
	this(_dimension);
	setGrammar(grammar);
	displayGrammar();
    }

    public Dimension getPreferredSize(){ return size;}
    
    protected Component createProduction(Production p){
	JPanel jprod = new JPanel();
	jprod.setLayout(new BoxLayout(jprod, BoxLayout.X_AXIS));
	jprod.setBackground(Color.white);
	JTextPane textPane = new JTextPane(new MachineGrammarStyledDocument(p.getAntecedente()));

	textPane.setEditable(false);
	JPanel auxPanel = JGrammarFrame.createJPanelBorder(textPane,null,BorderFactory.createLineBorder(Color.blue));

	auxPanel.setBackground(Color.white);	
	jprod.add(auxPanel);

	jprod.add(Box.createRigidArea(new Dimension(5,0)));

	jprod.add(new JLabel("-->"));
	jprod.add(Box.createRigidArea(new Dimension(5,0)));

	textPane = new JTextPane(new MachineGrammarStyledDocument(p.getConsecuente()));

	textPane.setEditable(false);	
	auxPanel = JGrammarFrame.createJPanelBorder(textPane,null,BorderFactory.createLineBorder(Color.red));

	auxPanel.setBackground(Color.white);	
	jprod.add(auxPanel);
	jprod.add(Box.createHorizontalGlue());
	return jprod;
    }




    
    /** 
     * Rutinas de prueba para la clase JGrammarCanvas.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
	System.out.println("Esta es la clase JGrammarCanvas. \n"
			   +"Comentario: El canvas donde se desplegarán las gramáticas\n"
			   +"Autor: Ivan Hernández Serrano\n"
			   +"E-mail: ivanx@users.sourceforge.net\n");
 	  
    }

    class MyGListener implements MouseListener{
	public void mouseClicked(MouseEvent e){
	    
	};
	public void mouseEntered(MouseEvent e) {
	};
	public void mouseExited(MouseEvent e){
	};	
	public void mousePressed(MouseEvent e){
	};
	public void mouseReleased(MouseEvent e){
	};
    }
}

/* JGrammarCanvas.java ends here. */
