/**
** <JStrConstructor.java> -- The generic str constructor
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


package jaguar.machine.util.jutil;


import jaguar.machine.structures.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.JOptionPane;
import jaguar.util.*;

/**
 * JStrConstructor.java
 *
 *
 * Created: Wed Mar 21 23:17:44 2001
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */


public class JStrConstructor extends JDialog implements ActionListener, ListSelectionListener {

    JMachineFrame jmframe;
    JFrame jframe;    
    JList symbolList;
    String toAdd;
    Vector vconstructedJStr;
    JButton addSymb;
    JButton backSpace;
    JButton ok;
    JTextArea  jconstructedJStr;
    
    public static final int ROWS = 1;
    public static final int COLS = 100;

    
    JStr constructedJStr;
    
    /**
       * Get the value of constructedJStr.
       * @return Value of constructedJStr.
       */
    public JStr getConstructedJStr() {return constructedJStr;}
    
    /**
       * Set the value of constructedJStr.
       * @param v  Value to assign to constructedJStr.
       */
    private void setConstructedJStr(JStr  v) {this.constructedJStr = v;}
    
    /**
     * El alfabeto sobre el cual trabajaremos
     */
    protected Alphabet sigmaConstructor;
    /**
     * funcion de acceso para obtener el valor de sigmaConstructor
     * @return el valor actual de sigmaConstructor
     * @see #sigmaConstructor
     */
    public Alphabet getSigmaConstructor(){
	return sigmaConstructor;
    }
    /**
     * funcion de acceso para modificar sigmaConstructor
     * @param new_sigmaConstructor el nuevo valor para sigmaConstructor
     * @see #sigmaConstructor
     */
    public void setSigmaConstructor(Alphabet new_sigmaConstructor){
	sigmaConstructor=new_sigmaConstructor;
    }

    public JStrConstructor(JMachineFrame jmframe) {
	super(jmframe,"Building a test string",true);
	this.jmframe =	jmframe;
	setControls();
	setSigmaConstructor(this.jmframe.getJMachine().getSigma());
	createJList();
	createJTextArea();	
	toAdd = null;
	vconstructedJStr = new Vector();
	setSize(new Dimension(325,180));	
	show();
    }

    public final static String JSTRCONSRUCTOR_TITLE= "Building a test string for ";
    
    public JStrConstructor(JFrame jframe, Alphabet alfa, String title) {
	super(jframe,JSTRCONSRUCTOR_TITLE+title,true);
	this.jframe =	jframe;
	setControls();
	setSigmaConstructor(alfa);
	createJList();
	createJTextArea();	
	toAdd = null;
	vconstructedJStr = new Vector();
	setSize(new Dimension(325,220));
	show();
    }
    
    public Dimension getMinimunSize(){
	return new Dimension(320,150);	
    }
    
    private void setControls(){
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
	
	addSymb	= new JButton("Add symbol");
	addSymb.setMnemonic(KeyEvent.VK_A);
	addSymb.setActionCommand("add");
	addSymb.setToolTipText("Add the selected Symbol to the JStr");
	addSymb.addActionListener(this);

	backSpace = new JButton("Backspace");
	backSpace.setMnemonic(KeyEvent.VK_B);
	backSpace.setActionCommand("backspace");
	backSpace.setToolTipText("Delete the last Symbol to the JStr");
	backSpace.addActionListener(this);

	ok = new JButton("ok");
	ok.setMnemonic(KeyEvent.VK_O);
	ok.setActionCommand("ok");
	ok.setToolTipText("instance the constructed JStr");
	ok.addActionListener(this);
	
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	p.add(addSymb);
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	p.add(backSpace);
	p.add(Box.createRigidArea(new Dimension(15, 0)));
	p.add(ok);
	p.add(Box.createHorizontalGlue());
	getContentPane().add(p, BorderLayout.NORTH);
    }

    private void createJList(){
	createJList(getSigmaConstructor());
    }
    private void createJList(Alphabet sigma){
	Symbol sym;
	Vector v = new Vector();
	for (Iterator i = sigma.iterator() ; i.hasNext() ;){	    
	    sym = (Symbol)i.next();
	    v.add(sym.toStringNoTags());	    
	}
	symbolList = new JList(v);
	symbolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	symbolList.addListSelectionListener(this);
	
	MouseListener mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2)
			addSymbol();					    
		}
	    };
	symbolList.addMouseListener(mouseListener); 
	
	JScrollPane jscrollp = new JScrollPane(symbolList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	getContentPane().add(JMachineFrame.createJPanelBorder(jscrollp,"Sigma"), BorderLayout.CENTER);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        JList theList = (JList)e.getSource();
        if (theList.isSelectionEmpty()) {
            toAdd = null;
        } else {
	    toAdd = (String)theList.getSelectedValue();
//	    Debug.println("Selected => " + toAdd);
        }
    }

    private void createJTextArea(){
	jconstructedJStr = new JTextArea();
	jconstructedJStr.setEditable(false);	
	jconstructedJStr.setFont(new Font("SansSerif", Font.BOLD, 12));
	JScrollPane areaScrollPane = new JScrollPane(jconstructedJStr,
						     JScrollPane.VERTICAL_SCROLLBAR_NEVER,
						     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);	
	getContentPane().add(JMachineFrame.createJPanelBorder(areaScrollPane,"String built"), BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add")) {
	    addSymbol();	    
	}
	if (e.getActionCommand().equals("backspace")) {
	    if(vconstructedJStr.size() == 0)
		JOptionPane.showMessageDialog(null,"There are no symbols to delete","Ops!",JOptionPane.INFORMATION_MESSAGE);
	    else{
		try{		
		    String removed = ((Symbol)vconstructedJStr.remove(vconstructedJStr.size()-1)).toString();		
		    int end = jconstructedJStr.getLineEndOffset(0);
//		    end --;
//		    Debug.println("jconstructedJStr.replaceRange(\"\","+(end - removed.length())+","+end+");");
		    jconstructedJStr.replaceRange("",(end - removed.length()),end);
		}catch(BadLocationException ble){
		    ble.printStackTrace();		    
		}   
	    }
	}
	if (e.getActionCommand().equals("ok")) {
	    setConstructedJStr(new JStr(vconstructedJStr,getSigmaConstructor()));
	    if(constructedJStr.isEpsilon())
		JOptionPane.showMessageDialog(null,"String built: "+constructedJStr,"Ok",JOptionPane.INFORMATION_MESSAGE);
	    setVisible(false);

	}
    }
    private void addSymbol(){
	if(toAdd == null)
	   JOptionPane.showMessageDialog(null,"You must select a symbol from Sigma","Error in construction",JOptionPane.ERROR_MESSAGE);
	else{		
	    vconstructedJStr.add(new Symbol(toAdd,false));
	    jconstructedJStr.append(toAdd);
	}	
    }   
} // JStrConstructor
