/**
** <JMachineFrame.java> -- The generic machine frame
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

import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import jaguar.util.Debug;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import jaguar.machine.JMachine;
import jaguar.machine.Machine;
import java.io.File;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.util.jutil.*;

/**
 * JDfaFrame.java
 *
 *
 * Created: Sun Feb 11 20:21:47 2001
 *
  * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
  * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:04 $
 */

abstract public class JMachineFrame extends JFrame implements ComponentListener, ActionListener, Printable{
    private Dimension dimension;
    protected JMachine jmachine; //DFA jdfa;    

    protected JLabel currentStateLabel;
    protected JTextPane textPane;
    protected MachineGrammarStyledDocument sssd;
    protected JFileChooser fc;
    protected JMenuItem loadTest, consTest, tabular, printM,descriptionMI;
    
    Thread thread;
   
    public void setLabelStringSSSD(JStr js){
	sssd.insertStr(js);
    }

    protected boolean stopExecution = true;      
    
    protected JScrollPane jScrollPaneCanvas;
    
    /**
       * Get the value of jScrollPaneCanvas.
       * @return Value of jScrollPaneCanvas.
       */
    public JScrollPane getJScrollPaneCanvas() {return jScrollPaneCanvas;}
    
    /**
       * Set the value of jScrollPaneCanvas.
       * @param v  Value to assign to jScrollPaneCanvas.
       */
    public void setJScrollPaneCanvas(JScrollPane  v) {this.jScrollPaneCanvas = v;}
    
    /**
       * Get the value of jdfa.
       * @return Value of jdfa.
       */
    public JMachine getJMachine() {return jmachine;}
    
    /**
       * Set the value of jdfa.
       * @param v  Value to assign to jdfa.
       */
    public void setJMachine(JMachine  v) {this.jmachine = v;}
    

    /** Botones de control para el marco generico **/
    protected JButton nextButton,resetButton, runAllButton, stopButton, quickTestButton;

    public static final Dimension DEFAULT_SIZE  = new Dimension(700,700);    

    public JMachineFrame(){
	this((JMachine)null);	
    }

    public JMachineFrame(JMachine _jmachine){
	this(_jmachine,"JMachineFrame");
    }

    public JMachineFrame(String title){
	super(title);
    }
    
    public JMachineFrame(JMachine _jmachine, String title){
	this(_jmachine,title,DEFAULT_SIZE);
    }

    public JMachineFrame(JMachine _jmachine, String title, Dimension _dimension){
	super(title);
	jmachine = _jmachine;
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {dispose();}
		public void windowOpened(WindowEvent e) {}
	    });
	
	addComponentListener(this);			    

	setJMenuBar(createMenu());
	
	setSize(_dimension);	    
	fc = new JFileChooser();
    }
    
    
    protected String THIS_MACHINE_TYPE;
    
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
	
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	p.add(nextButton);
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	p.add(runAllButton);
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	p.add(stopButton);
	p.add(Box.createHorizontalGlue());
        p.add(quickTestButton);
	p.add(Box.createHorizontalGlue());
	p.add(resetButton);
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	getContentPane().add(p, BorderLayout.SOUTH);
    }

    public static final int MAX_CHARS = 1000;

    /**
     * Pone las etiquetas de estado actual y la cadena actual con la configuraci'on de symbolo actual  en otro color, si es la primera vez, la cadena es epsilon]
     */
    protected void setLabels(){
        setLabels(DEFAULT_LABEL_STRING,DEFAULT_LABEL_ESTADO);
    }
    /** Usado por setLabel para el componente que tiene la cadena**/    
    final static String DEFAULT_LABEL_STRING="String";
    /** Usado por setLabel para el componente que tiene el estado actual**/    
    final static String DEFAULT_LABEL_ESTADO="Current State";
    
    protected void setLabels(String labelString){
        setLabels(labelString,DEFAULT_LABEL_ESTADO);
    }

    protected void setLabels(String labelString, String labelEstado){
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
	sssd = new  MachineGrammarStyledDocument(MAX_CHARS);
	textPane = new JTextPane(sssd);
	textPane.setEditable(false);	
	if(jmachine != null){
	    currentStateLabel = new JLabel(jmachine.getCurrentState().toString());
	    sssd.insertStr(jmachine.getStrToTest());	    
	}else {
	    currentStateLabel = new JLabel("");
	    sssd.insertString("");	    
	}
	currentStateLabel.setToolTipText("Current State in the execution");	
	    
	p.add(Box.createRigidArea(new Dimension(10, 0)));
	
	p.add(createJPanelBorder(currentStateLabel,labelEstado));
	p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(createJPanelBorder(textPane,labelString));	
	p.add(Box.createHorizontalGlue());

	getContentPane().add(p, BorderLayout.NORTH);
    }
    
    public Dimension getMinimumSize(){
	return new Dimension(300,300);
    }
    
    public void nextStep(boolean b){
	nextButton.setEnabled(b);	
    }

    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("next")) {
	    nextStep();
        }	
	if (e.getActionCommand().equals("reset")) {
	    resetMachine();	    
        }
	if (e.getActionCommand().equals("load")) {
	    loadMachineFromFile();	    
        }
	if (e.getActionCommand().equals("loadtest")) {
	    loadTestStringFromFile();	    
        }
	if (e.getActionCommand().equals("constest")){
	    JStrConstructor jcons = new JStrConstructor(this);
//	    Debug.println("JStrConstructor - construyeron => " + jcons.getConstructedJStr());
	    loadTestString(jcons.getConstructedJStr());
	}
	
	if (e.getActionCommand().equals("rundfa")){
	    stopExecution = false;
	    runAllButton.setEnabled(false);
            quickTestButton.setEnabled(false);
	    stopButton.setEnabled(true);
	    run();
	}
	if (e.getActionCommand().equals("stopdfa")){
	    stopExecution  = true;
	    runAllButton.setEnabled(true);
            quickTestButton.setEnabled(true);
	    stopButton.setEnabled(false);
	    Debug.println("STOOOOOOOOOOOOOP!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	if (e.getActionCommand().equals("quickTest")){
            makeQuickTest();
	}        
        if (e.getActionCommand().equals("tabular")) {
//	    Debug.println("La representacion tabular");
	    showTabular();
        }
        if (e.getActionCommand().equals("print")) {
	    Debug.println("Printing");
	    print();
        }
	if(e.getActionCommand().equals("showDescription"))
	    JOptionPane.showMessageDialog(this,THIS_MACHINE_TYPE +": "+ ((((Machine)jmachine).getMachineDescription().trim().length()==0)?"No provided":((Machine)jmachine).getMachineDescription()),THIS_MACHINE_TYPE+"'s description", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Checa <em>rápidamente</em> si la cadena que está cargada en la
     * máquina pertenece al lenguaje L(jmachine) y despliega un
     * dialogo con el resultado, no muestra los movimientos en ningun
     * otro componente y después de mostrar el resultado, hace un <code>resetMachine</code>  
     * @see #resetMachine
     * @see JMachine#getStrToTestOrig
     */
    public void makeQuickTest(){
        resetMachine();
        nextButton.setEnabled(false);
        runAllButton.setEnabled(false);
	stopButton.setEnabled(false);
        JOptionPane.showMessageDialog(this,"The "+THIS_MACHINE_TYPE+ ((((Machine)jmachine).runMachine(jmachine.getStrToTestOrig()))?" ACCEPTS ":" DOESN'T ACCEPT ") +" the string "+ jmachine.getStrToTestOrig(),THIS_MACHINE_TYPE+"Result", JOptionPane.INFORMATION_MESSAGE);
        resetMachine();
    }
    
    
    public void print(){
	PrinterJob printJob = PrinterJob.getPrinterJob();
	printJob.setPrintable(this);
	if (printJob.printDialog()) {
	    try {
		printJob.print();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    public int print(Graphics g, PageFormat pf, int pi)
	throws PrinterException {
	if (pi >= 1) {
	    return Printable.NO_SUCH_PAGE;
	}
	jmachine.print((Graphics2D) g);
	return Printable.PAGE_EXISTS;
    }
    
    public boolean loadMachineFromFile(){
	fc.setDialogTitle("Open Machine Definition");	
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    initJMachine(file);
	    sssd.reset();
	}
	return true;	
    }

    /**
     ** Realiza un paso en la ejecución de la Máquina y regresa verdarero o falso dependiendo si puede seguir con la ejecución o no
     ** @return boolean <code>true</code> si se puede continuar con la ejecución de la máquina, <code>false</code> en  otro caso.
     **/
    abstract public boolean nextStep();

    public boolean loadTestStringFromFile(){
	fc.setDialogTitle("Open String to execution");
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    try{
		JStr jstr = new JStr(file, false);
		Debug.println("cadena de archivo => " + jstr);
		loadTestString(jstr);
	    }catch(Exception ex){
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null,"Error loading  JStr file "+file,"JDfa",JOptionPane.ERROR_MESSAGE);
	    }		    
	}
	return true;	
    }

    

    protected void loadTestString(JStr jstr){
	if(jstr ==null)
	    return;	
	jmachine.setStrToTest(jstr);
	jmachine.setStrToTestOrig(jstr);
	resetMachine();
//	jmachine.resetMachine();
    }


    /**
     ** Pone todos los componentes en un estado inicial Entre los
     ** compononentes que resetea están: la cadena en el componente
     ** "String", "Current State" regresa al estado inicial; habilita
     ** los botones "next", "run", "quick test" y deshabilita el botón
     ** "stop".
     **/
    protected void resetMachine(){
        jmachine.resetMachine();
        sssd.reset();
        sssd.insertStr(jmachine.getStrToTestOrig());
	currentStateLabel.setText(jmachine.getCurrentState().toString());
 	nextButton.setEnabled(true);
        runAllButton.setEnabled(true);
        quickTestButton.setEnabled(true);
        stopButton.setEnabled(false);
    }
    
    /**
     * Crea una instancia de JMachine a partir de la descripción en un archivo, ademlas inicializa los botones y la configuración en general del
     * JMachineFrame
     * @param file es el archivo donde esta la descripción de la máquina en cuestión.
     */
    abstract protected void initJMachine(File file);

    /**
     * Crea el menú con las configuraciones básicas de este Frame
     */
    protected JMenuBar createMenu() {
	JMenuBar mb = new JMenuBar();
	JMenu menu = new JMenu("File");
	menu.setMnemonic(KeyEvent.VK_F);
	
	JMenuItem loadJdfa = new  JMenuItem("Load Machine...",KeyEvent.VK_L);
	loadJdfa.getAccessibleContext().setAccessibleDescription("Loads a new Machine");
	loadJdfa.addActionListener(this);	
	loadJdfa.setActionCommand("load");
	loadJdfa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));	
	menu.add(loadJdfa);

	tabular = new  JMenuItem("Show tabular ...",KeyEvent.VK_S);
	tabular.getAccessibleContext().setAccessibleDescription("Muestra la representación tabular de la Máquina");
	tabular.addActionListener(this);
	tabular.setActionCommand("tabular");
	tabular.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,0));
	tabular.setEnabled(false);
	menu.add(tabular);

	descriptionMI = new  JMenuItem("Machine's Description...",KeyEvent.VK_D);
	descriptionMI.getAccessibleContext().setAccessibleDescription("Shows this Machine's description, if it's available");
	descriptionMI.addActionListener(this);
	descriptionMI.setActionCommand("showDescription");
	descriptionMI.setEnabled(false);		
	menu.add(descriptionMI);	

	printM = new  JMenuItem("Print Machine...",KeyEvent.VK_P);
	printM.getAccessibleContext().setAccessibleDescription("Imprime la Máquina");
	printM.addActionListener(this);
	printM.setActionCommand("print");
	printM.setEnabled(false);

	JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });
	quit.setMnemonic(KeyEvent.VK_Q);
	menu.add(quit);
	
	JMenu mtest = new JMenu("Test...");
	mtest.setMnemonic(KeyEvent.VK_T);
	loadTest = new  JMenuItem("Load test String...",KeyEvent.VK_S);
	loadTest.getAccessibleContext().setAccessibleDescription("Loads a new JStr");
	loadTest.addActionListener(this);	
	loadTest.setActionCommand("loadtest");
	loadTest.setEnabled(false);	
	mtest.add(loadTest);

	consTest = new  JMenuItem("Build  test String...",KeyEvent.VK_C);
	consTest.getAccessibleContext().setAccessibleDescription("Builts a new  JStr");
	consTest.addActionListener(this);	
	consTest.setActionCommand("constest");
	consTest.setEnabled(false);
	consTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
	mtest.add(consTest);




	mb.add(menu);
	mb.add(mtest);
	return mb;
    }

    static public JPanel createJPanelBorder(Component c, String borderTitle){
	Border border = BorderFactory.createEtchedBorder();
	TitledBorder title = BorderFactory.createTitledBorder(border,borderTitle);
	title.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
	title.setTitlePosition(TitledBorder.DEFAULT_POSITION);
	JPanel bcomp = new JPanel(false);	
        bcomp.setLayout(new GridLayout(1, 1));
        bcomp.add(c);
        bcomp.setBorder(title);
	return bcomp;	
    }
    
    
    Timer t = new Timer();	
    public void run() {
	TimerTask tt = new TimerTask(){
		public void run(){
		    boolean maspasos = true;		    
		    try{	    
			if(!stopExecution && maspasos ){
			    Debug.println("HOLA 1");		
			    maspasos = nextStep();
			    repaint();
			    Debug.println(jmachine.getStrToTest().toString());		
			}
		    }catch(Exception e){
			e.printStackTrace();
		    }
		}
	    };
	t.schedule(tt,0,6000);
    }

    public void showTabular(){
//	Debug.println(jmachine.getTableVector().toString());
	TabularMachine ttm = new TabularMachine(jmachine.getTableVector());
    }

    protected class TabularMachine extends JFrame{
	TabularMachine(Vector v){
	    super("Tabular Machine");
	    if(v==null) return;
	    Vector columnNames = (Vector)v.get(0);
	    Vector data = (Vector)v.get(1);
	    MyJTable table = new MyJTable(data,columnNames);
	    table.setPreferredScrollableViewportSize(new Dimension(650, 95));
 	    table.getTableHeader().setReorderingAllowed(false);
	    table.setFont(new Font("SansSerif",Font.BOLD,10));
	    JScrollPane scrollPane = new JScrollPane(table);
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
			dispose();
		    }
		});
	    pack();
	    setVisible(true);
	}
    }
    protected class MyJTable extends JTable{
	MyJTable(Vector v1, Vector v2){
	    super(v1,v2);
	}
	public boolean isCellEditable(int row,int column){
	    return false;
	}
    }
    
} // JMachineFrame
