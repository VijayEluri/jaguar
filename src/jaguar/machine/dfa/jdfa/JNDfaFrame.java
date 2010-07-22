/**
** <JNDfaFrame.java> -- The frame to show a NDFA
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


package jaguar.machine.dfa.jdfa;

import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.structures.*;
import jaguar.machine.dfa.jdfa.jstructures.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.util.Debug;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.machine.*;
import java.io.File;

/**
 * JNDfaFrame.java
 *
 *
 * Created: Sun Feb 11 20:21:47 2001
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:04 $
 **/

public class JNDfaFrame extends JMachineFrame{

    public static final String MACHINE_TYPE = "DFA";
    
    private JNDfaCanvas jdc;

    /**
     * Get the value of jdc.
     * @return value of jdc.
     */
    public JNDfaCanvas getJdc() {
	return jdc;
    }
    
    /**
     * Set the value of jdc.
     * @param v  Value to assign to jdc.
     */
    public void setJdc(JNDfaCanvas  v) {
	jdc = v;
    }

    public JNDfaFrame(){
	this(null);	
    }
    
    public JNDfaFrame(JNDFA _jdfa){
	super("JNDfa Frame");
	jmachine = _jdfa;
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {dispose();}
		public void windowOpened(WindowEvent e) {}
	    });
	
	addComponentListener(this);			    

	setJMenuBar(createMenu());
	
	setSize(DEFAULT_SIZE);
	if(jmachine != null)
	    jdc = new JNDfaCanvas(DEFAULT_SIZE,(JNDFA)jmachine);
	else jdc = new JNDfaCanvas(DEFAULT_SIZE);
	
	jScrollPaneCanvas = new JScrollPane(jdc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
	jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
	jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);

	setLabels();	

	getContentPane().add(createJPanelBorder(jScrollPaneCanvas,"Configuration"), BorderLayout.CENTER);
	setControls(MACHINE_TYPE);		
	if(jmachine == null){
	    nextButton.setEnabled(false);
	    resetButton.setEnabled(false);
	    runAllButton.setEnabled(false);
	    stopButton.setEnabled(false);	   
	    loadTest.setEnabled(false);
	    consTest.setEnabled(false);
	    descriptionMI.setEnabled(false);	    
	}
	fc = new JFileChooser();
    }


    /**
     * Pone botones los botones "Next", "Reset", "Run" y "Stop" para manejar las configuraciones del automata
     */
    protected void setControls(String machineType){
        THIS_MACHINE_TYPE=machineType;
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
	
	nextButton = new JButton("Next Step");
	nextButton.setMnemonic(KeyEvent.VK_X);
	nextButton.setActionCommand("next");
	nextButton.setToolTipText("Click this button to execute the next step");
	nextButton.addActionListener(this);

	resetButton = new JButton("Reset " + machineType);
	resetButton.setMnemonic(KeyEvent.VK_R);
	resetButton.setActionCommand("reset");
	resetButton.setToolTipText("Click this button to reset the "  + machineType);
	resetButton.addActionListener(this);
	
	runAllButton = new JButton("Run " + machineType);
	runAllButton.setMnemonic(KeyEvent.VK_U);
	runAllButton.setActionCommand("rundfa");
	runAllButton.setToolTipText("Click this button to run the " + machineType);
	runAllButton.addActionListener(this);

        /** Para que no este jorobando el Vaz **/
	quickTestButton = new JButton("Quick Test");
	quickTestButton.setMnemonic(KeyEvent.VK_K);
	quickTestButton.setActionCommand("quickTest");
	quickTestButton.setToolTipText("Click this button to performe a quick test");
        quickTestButton.addActionListener(this);
        
	stopButton  = new JButton("Stop "  + machineType);
	stopButton.setMnemonic(KeyEvent.VK_S);
	stopButton.setActionCommand("stopdfa");
	stopButton.setToolTipText("Click this button to stop " + machineType);
	stopButton.addActionListener(this);
	
	p.add(Box.createHorizontalGlue());
        p.add(quickTestButton);
	p.add(Box.createHorizontalGlue());
	getContentPane().add(p, BorderLayout.SOUTH);
    }

    
    /**
     ** Realiza un paso en la ejecución de la Máquina y regresa verdarero o falso dependiendo si puede seguir con la ejecución o no
     ** @return boolean <code>true</code> si se puede continuar con la ejecución de la máquina, <code>false</code> en  otro caso.
     **/
    public boolean nextStep(){
	boolean masTrans=jmachine.nextStep();	    
	nextStep(masTrans);	    
	if(!jmachine.getStrToTestOrig().isEpsilon()){
	    JStr foo = jmachine.getSubStrTested();
	    if(jmachine.getCurrentState() != null)
		currentStateLabel.setText(jmachine.getCurrentState().toString());
	    Str pre, post;
	    Symbol curr;
	    pre = foo.substring(0,foo.length()-1);
	    curr = foo.getLast();
	    post = jmachine.getStrToTest();
	    sssd.insertStr(pre,curr,post);
	}
	if(!masTrans){
	    stopExecution = true;
	    runAllButton.setEnabled(false);
	    jmachine.displayResult();
	}//else Debug.println("mmm there are more transitions!!!");
	
	return masTrans;
    }
    
    protected void initJMachine(File file){
	System.err.println("\nLoading...  " + file +" \n");
	try{		    
	    jmachine = new JNDFA(file,this);
//	    System.err.println("\nJNDFA = " + jmachine);
	    jdc.initJMachineCanvas((JNDFA)jmachine);	    
	    nextButton.setEnabled(true);
	    resetButton.setEnabled(true);
	    runAllButton.setEnabled(true);
	    tabular.setEnabled(true);
        save.setEnabled(true);
	    stopButton.setEnabled(false);	    
	    currentStateLabel.setText(jmachine.getCurrentState().toString());
	    jmachine.setStrToTest(new JStr());
	    jmachine.setStrToTestOrig(new JStr());
	    sssd.insertStr(jmachine.getStrToTest());
	    jmachine.setJMachineFrame(this);
	    loadTest.setEnabled(true);
	    consTest.setEnabled(true);
	    jScrollPaneCanvas.getViewport().setViewPosition(new Point(0,0));	    
	    jmachine.resetMachine();
	    descriptionMI.setEnabled(true);
	}catch(Exception ex){
	    jmachine = null;
	    ex.printStackTrace();
	    JOptionPane.showMessageDialog(null,"Error loading  JNDFA file "+file,"JNDFA",JOptionPane.ERROR_MESSAGE);
	}	
    }
    
    public void componentHidden(ComponentEvent e) {}
    
    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
	if(jdc !=null){
	    jdc.setSize(e.getComponent().getSize());
	}
    }

    public void componentShown(ComponentEvent e) {}


    public static void main(String []argv){
	JNDfaFrame f = new JNDfaFrame();
	f.show();	
    }

} // JNDfaFrame
