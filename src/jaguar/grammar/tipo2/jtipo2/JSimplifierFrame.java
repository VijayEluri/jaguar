/**
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


package jaguar.grammar.tipo2.jtipo2;

import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.grammar.jgrammar.*;
import jaguar.grammar.tipo2.*;
import jaguar.structures.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.jdfa.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JDfa2Gtipo2
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JSimplifierFrame extends JConverterFrame{    
    /**
     * El motor para la conversion
     */
    protected JSimplifier engineConverter;
    /**
     * funcion de acceso para obtener el valor de engineConverter
     * @return el valor actual de engineConverter
     * @see #engineConverter
     */
    public JSimplifier getEngineConverter(){
	return engineConverter;
    }
    /**
     * funcion de acceso para modificar engineConverter
     * @param new_engineConverter el nuevo valor para engineConverter
     * @see #engineConverter
     */
    public void setEngineConverter(JSimplifier new_engineConverter){
	engineConverter=new_engineConverter;

    }


    /**
     * La gramática que convertiremos en AF
     */
    protected Gtipo2 grammarOrig;
    /**
     * funcion de acceso para obtener el valor de grammarOrig
     * @return el valor actual de grammarOrig
     * @see #grammarOrig
     */
    public Gtipo2 getGrammarorig(){
	return grammarOrig;
    }
    /**
     * funcion de acceso para modificar grammarOrig
     * @param new_grammarOrig el nuevo valor para grammarOrig
     * @see #grammarOrig
     */
    public void setGrammarorig(Gtipo2 new_grammarOrig){
	grammarOrig=new_grammarOrig;
    }
    

    /**
     * La gramática tipo 2 resultante
     */
    protected Gtipo2 grammarResult;
    /**
     * funcion de acceso para obtener el valor de grammarResult
     * @return el valor actual de grammarResult
     * @see #grammarResult
     */
    public Gtipo2 getGrammarresult(){
	return grammarResult;
    }
    /**
     * funcion de acceso para modificar grammarResult
     * @param new_grammarResult el nuevo valor para grammarResult
     * @see #grammarResult
     */
    public void setGrammarresult(Gtipo2 new_grammarResult){
	grammarResult=new_grammarResult;
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


    /** El canvas que se envargar'a de mostrar la gramatica de entrada, i.e. la que vamos a convertir **/
    protected JGrammarCanvas jgrammarCanvasResult;
    /**
     * funcion de acceso para obtener el valor de jgrammarCanvasResult
     * @return el valor actual de jgrammarCanvasResult
     * @see #jgrammarCanvasResult
     */
    public JGrammarCanvas getJgrammarcanvasresult(){
	return jgrammarCanvasResult;
    }
    /**
     * funcion de acceso para modificar jgrammarCanvasResult
     * @param new_jgrammarCanvasResult el nuevo valor para jgrammarCanvasResult
     * @see #jgrammarCanvasResult
     */
    public void setJgrammarcanvasresult(JGrammarCanvas new_jgrammarCanvasResult){
	jgrammarCanvasResult=new_jgrammarCanvasResult;
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
    
    public JSimplifierFrame(String title){
	super(title,"Waiting for a Gtipo2 to standardize,... ");
	standardizeSelectionDialog = new JSelectNormalizations(this);
	menu.remove(saveResultMenuItem);	
    }
    
    public JSimplifierFrame(){
	this("Gramática T2 standardizer");
	standardizeSelectionDialog = new JSelectNormalizations(this);
    }

    JSelectNormalizations standardizeSelectionDialog;
    

    protected void setControls(){
	setControls("Standardize Grammar T2");
    }    

    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected JMenuBar createMenu() {
	return createMenu("Load Grammar T2 to standardize...","Loads a new Grammar T2 to standardize into a AF ","Standardize...",
			  "Standardize the loaded Grammar T2","Save  resulting GT2...", "Save the resulting GT2");
    }

    public void saveResultFromConvertion(){
	saveResultFromConvertion("Save Resulting GT2 ");
    }


    public boolean doConvertion(){
	JOptionPane.showMessageDialog(null,"The algorithms will be done in the order that they are "+"\nlisted (from top to bottom)."+
				      " If some normalization requires"+"\n a previous one, it will be done.",
				      "Gtipo2 Normalization", JOptionPane.INFORMATION_MESSAGE);
	standardizeSelectionDialog = new JSelectNormalizations(this,currentObjectToConvert);
	standardizeSelectionDialog.show();
	setEngineConverter(new JSimplifier(getGrammarorig(),detailsArea));
	//  este es el contador para el posicionamiento
	int contador = 0 ;
	
	Gtipo2  gt2M = engineConverter.getOriginalGt2(), gt2Inal = engineConverter.getOriginalGt2(), gt2ProdEps = engineConverter.getOriginalGt2(), gt2Unit=null, gt2FNC=null, gt2FNG=null;
	if(standardizeSelectionDialog.getWasCancel())
	    return false;
	
	if(standardizeSelectionDialog.doMuertos() || standardizeSelectionDialog.doUnitarias() || standardizeSelectionDialog.doChomsky() || standardizeSelectionDialog.doGreibach()){

	    gt2M = engineConverter.eliminaSimbolosMuertos(gt2M);

	    showResult(gt2M,"Without Dead Symbols ",contador);
	    contador++;

	}
	if(standardizeSelectionDialog.doInalcanzables() || standardizeSelectionDialog.doUnitarias() || standardizeSelectionDialog.doChomsky() || standardizeSelectionDialog.doGreibach()){
	    gt2Inal = engineConverter.eliminaSimbolosInalcanzables(gt2M);

	    showResult(gt2Inal,"Without Unreachable Symbols ",contador);
	    contador++;	    

	}
	if(standardizeSelectionDialog.doProdEpsilon() || standardizeSelectionDialog.doUnitarias() || standardizeSelectionDialog.doChomsky() || standardizeSelectionDialog.doGreibach()){
	    gt2ProdEps = engineConverter.eliminaProduccionesEpsilon(gt2Inal);

	    showResult(gt2ProdEps,"Without Epsilon-Productions ",contador);
	    contador++;
	}
	if(standardizeSelectionDialog.doUnitarias() || standardizeSelectionDialog.doChomsky()||standardizeSelectionDialog.doGreibach()){
	    gt2Unit = engineConverter.eliminaProduccionesUnitarias(gt2ProdEps);

	    showResult(gt2Unit,"Without Unary Productions ",contador);
	    contador++;
	}
	if(standardizeSelectionDialog.doChomsky()|| standardizeSelectionDialog.doGreibach()){
	    try{		
		gt2FNC = engineConverter.transform2Chomsky(gt2Unit);

		showResult(gt2FNC,"Chomsky  Normal Form ",contador);
		contador++;
	    }catch(jaguar.structures.exceptions.StrIndexOutOfBoundsException stre){
		JOptionPane.showMessageDialog(null,"FNC","Error al convertir a FNC:\n"+ stre.getMessage(),JOptionPane.ERROR_MESSAGE);
	    }
	}
	if(standardizeSelectionDialog.doGreibach()){
	    try{		
		gt2FNG = engineConverter.transform2Greibach(gt2FNC);

		showResult(gt2FNG,"Greibach Normal  Form ",contador);
		contador++;
	    }catch(jaguar.structures.exceptions.StrIndexOutOfBoundsException stre){
		JOptionPane.showMessageDialog(null,"FNG","Error al convertir a FNG:\n"+ stre.getMessage(),JOptionPane.ERROR_MESSAGE);
	    }
	}
	return true;	
    }

    public void showResult(Gtipo2 gt2, String titleOperation, int yMultiplode10){
	JGrammarCanvas resultCanvas = new JGrammarCanvas(MACHINE_SIZE,gt2);

	JInternalFrame jgrammarframe = new JInternalGrammarFrame(resultCanvas,titleOperation + getCurrentobjecttoconvert(),true,true,true,true);
	jgrammarframe.getContentPane().add(new JScrollPane(resultCanvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jgrammarframe.setSize(MACHINE_SIZE);
	jgrammarframe.setLocation(new Point((int)MACHINE_SIZE.getWidth(),25*yMultiplode10));
	desktop.add(jgrammarframe);
	jgrammarframe.show();
	jgrammarframe.setVisible(true);
    }

    public void showResult(Gtipo2 gt2, String titleOperation){
	showResult(gt2, titleOperation,0);	
    }
    

    protected boolean loadOrigFromFile(){
	return loadOrigFromFile("Load GT2 to standardize");
    }


    protected void loadOrigFromFile(File file)  throws FileNotFoundException{
	try{ 
	    grammarOrig = new Gtipo2(file);	
	}catch( Exception ouch){
	    ouch.printStackTrace();
	    JOptionPane.showMessageDialog(null,"Error loading Grammar T2 from file " + file +"\n\""+ouch.getMessage()+"\" ","Gtipo3 -> AF",JOptionPane.ERROR_MESSAGE);
	    return;
	}

	jgrammarCanvasOrig = new JGrammarCanvas(MACHINE_SIZE,grammarOrig);
	currentObjectToConvert++;		

	jorigframe = new JInternalGrammarFrame(jgrammarCanvasOrig,"JGrammar Tipo2 Orig " + getCurrentobjecttoconvert(),true,true,true,true);
	jorigframe.getContentPane().add(new JScrollPane(jgrammarCanvasOrig, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	jorigframe.setSize(MACHINE_SIZE);
	desktop.add(jorigframe);
	jorigframe.show();
	jorigframe.setVisible(true);
    }

    public void toFile(FileWriter fw){

    }
    
    public static void main(String[] argv){
	JSimplifierFrame f = new JSimplifierFrame();
	f.show();	
    }

    public class JInternalGrammarFrame extends JInternalFrame implements ActionListener{    
	JButton launcherB;	
	JGrammarCanvas gCanvas;
	public JInternalGrammarFrame(JGrammarCanvas _gCanvas, String title){
	    this(_gCanvas,title,true,true,true,true);
	}
	
	public JInternalGrammarFrame(JGrammarCanvas _gCanvas, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable){
	    super(title, resizable, closable, maximizable, iconifiable);
	    gCanvas = _gCanvas;	    
	    JMenuBar menuBar = new JMenuBar();
	    JMenu utilMenu = new JMenu("Util");
	    JMenuItem replaceSymbol = new JMenuItem("Replace Symbols...",KeyEvent.VK_R);
	    replaceSymbol.getAccessibleContext().setAccessibleDescription("Replace symbols in the grammar");	    
	    replaceSymbol.addActionListener(this);
	    replaceSymbol.setActionCommand("replace");
	    replaceSymbol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0));		
	    utilMenu.add(replaceSymbol);

	    JMenu fileMenu = new JMenu("File");
	    JMenuItem saveCurrent = new JMenuItem("Save Grammar....",KeyEvent.VK_S);
	    saveCurrent.getAccessibleContext().setAccessibleDescription("Guarda esta gramática");
	    saveCurrent.addActionListener(this);
	    saveCurrent.setActionCommand("save");
	    saveCurrent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0));		
	    fileMenu.add(saveCurrent);
	    
	    menuBar.add(fileMenu);
	    menuBar.add(utilMenu);
	    
	    setJMenuBar(menuBar);	    
	}
	public JButton getLauncher(){
	    return launcherB;
	}
	
	public void actionPerformed(ActionEvent e) {
	    Debug.println("ActionEvent"+ e);	    
	    if (e.getActionCommand().equals("replace")) {
		replaceSymbols();	    

	    }
	    if (e.getActionCommand().equals("save")) {
		saveResultFromConvertion("Save "+ getTitle());		
	    }	    
	}

	protected void saveResultFromConvertion(String dialogTitlePrefix){
	    JFileChooser fc = new JFileChooser();	    
	    fc.setDialogTitle(dialogTitlePrefix + getCurrentobjecttoconvert());	
	    int returnVal = fc.showSaveDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile();		
		try{
		    gCanvas.getGrammar().toFile(new FileWriter(file));
		    JOptionPane.showInternalMessageDialog(this,"El resultado ha sido guardado!",
							  "Saving status",JOptionPane.INFORMATION_MESSAGE);
		}catch( IOException ouch){
		    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				       + " Not saved: " ); 
		    ouch.printStackTrace();
		    JOptionPane.showInternalMessageDialog(this,"El resultado NO fue guardado!","Saving status",JOptionPane.ERROR_MESSAGE);
		}
	    }
	}

	
	protected void replaceSymbols(){
	    JGrammarSymbolReplacer jgsr = new JGrammarSymbolReplacer(gCanvas.getGrammar().getN(),gCanvas.getGrammar().getT());
	    JScrollPane sp = new JScrollPane(jgsr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    
	    Debug.println("\nAntes: "+gCanvas.getGrammar());	    
	    Object[] options = { "OK", "CANCEL" };

	    int response = JOptionPane.showInternalOptionDialog(this,sp,"Replacer "+getTitle(),
							JOptionPane.DEFAULT_OPTION,

							JOptionPane.PLAIN_MESSAGE,
							null, options, options[0]);
	    

	    if(response == JOptionPane.OK_OPTION){	    
		Hashtable symbolsToReplace = jgsr.getPairsToReplace();
		Object a[] = symbolsToReplace.keySet().toArray();
		for(int i = 0 ; i < a.length; i++)
		    gCanvas.getGrammar().replaceSymbol((Symbol)a[i],(Symbol)symbolsToReplace.get(a[i]));
		gCanvas.displayGrammar();
		getContentPane().validate();
	    }
	    Debug.println("\nDespués: "+gCanvas.getGrammar());	    
	}
    }

    protected class JSelectNormalizations extends JDialog implements ActionListener{
	JCheckBox muertos, inalcanzables, prodEpsilon, unitarias, chomsky, greibach;
	JButton ok;
	JButton cancel;
	private  Dimension theSize;

	protected JSelectNormalizations(JSimplifierFrame jfs){
	    this(jfs, 0);	    
	}
	
	protected JSelectNormalizations(JSimplifierFrame jfs, int currentNumber){
//	    super(jfs,"Selecciona las estandarizaciones a realizar ["+currentNumber+"]", true);
	    super(jfs,"Select the normalizations to perform ["+currentNumber+"]", true);
	    theSize = new Dimension(295,200);
	    constructDialog();
	}

	protected void constructDialog(){
	    JPanel options = new JPanel();
	    JPanel buttons = new JPanel();	    
	    muertos = new JCheckBox("Eliminate Dead Symbols",false);
	    inalcanzables = new JCheckBox("Eliminate Unreachable Symbols",false);
	    prodEpsilon = new JCheckBox("Eliminate Epsilon-Productions",false);
	    unitarias = new JCheckBox("Eliminate Unary-Productions", false);
	    chomsky = new JCheckBox("Chomsky Normal Form", false);
	    greibach = new JCheckBox("Greibach Normal Forma", false);

	    
	    options.setLayout(new BoxLayout(options,BoxLayout.Y_AXIS));
	    options.add(Box.createRigidArea(new Dimension(0, 10)));
	    options.add(muertos);
	    options.add(inalcanzables);
	    options.add(prodEpsilon);
	    options.add(unitarias);
	    options.add(chomsky);
	    options.add(greibach);
	    options.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	    options.add(Box.createRigidArea(new Dimension(0, 10)));	    

	    buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
	    ok = new JButton("ok");
	    ok.setMnemonic(KeyEvent.VK_O);
	    ok.setActionCommand("ok");
	    ok.addActionListener(this);

	    cancel = new JButton("cancel");
	    cancel.setMnemonic(KeyEvent.VK_C);
	    cancel.setActionCommand("cancel");
	    cancel.addActionListener(this);	    

	    buttons.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttons.add(ok);
	    buttons.add(Box.createHorizontalGlue());
	    buttons.add(cancel);
	    buttons.add(Box.createRigidArea(new Dimension(10, 0)));	    
	    buttons.setAlignmentX(JComponent.CENTER_ALIGNMENT);


	    getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
	    getContentPane().add(options);	    
	    getContentPane().add(Box.createVerticalGlue());
	    getContentPane().add(buttons);
	    getContentPane().add(Box.createRigidArea(new Dimension(0, 10)));
	    
	    setSize(theSize);
	}

	/**
	 * Es verdadero si la opción fue cancel, sin importar las opciones seleccionadas en los checkbox
	 */
	protected boolean wasCancel = false;
	/**
	 * funcion de acceso para obtener el valor de wasCancel
	 * @return el valor actual de wasCancel
	 * @see #wasCancel
	 */
	public boolean getWasCancel(){
	    return wasCancel;
	}
	/**
	 * funcion de acceso para modificar wasCancel
	 * @param new_wasCancel el nuevo valor para wasCancel
	 * @see #wasCancel
	 */
	private void setWasCancel(boolean new_wasCancel){
	    wasCancel=new_wasCancel;
	}
	
	
	public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand().equals("ok")) {
		if(! standardizeSelectionDialog.checkAlmostOneOption())
		    JOptionPane.showMessageDialog(null,"You must select at least one option","Type2 Grammar: Normalization",
						  JOptionPane.ERROR_MESSAGE);

		else{
		    setWasCancel(false);
		    setVisible(false);
		}
	    }
	    if (e.getActionCommand().equals("cancel")) {
		setWasCancel(true);
		setVisible(false);
	    }
	}

	
	public Dimension getMinimiumSize(){return theSize;}
	    
	public void reset(){
	    setWasCancel(false);
	    muertos.setSelected(false);
	    inalcanzables.setSelected(false);
	    prodEpsilon.setSelected(false);
	    unitarias.setSelected(false);
	    chomsky.setSelected(false);
	    greibach.setSelected(false);
	}
	public boolean doMuertos(){
	    return muertos.isSelected();
	}
	public boolean doInalcanzables(){
	    return inalcanzables.isSelected();
	}
	public boolean doProdEpsilon(){
	    return prodEpsilon.isSelected();
	    
	}
	public boolean doUnitarias(){
	    return unitarias.isSelected();
	}
	public boolean doChomsky(){
	    return chomsky.isSelected();
	}
	public boolean doGreibach(){
	    return greibach.isSelected();
	}

	public boolean checkAlmostOneOption(){
	    return (muertos.isSelected() || inalcanzables.isSelected() || prodEpsilon.isSelected() || unitarias.isSelected() || chomsky.isSelected() || greibach.isSelected());
	}
    }
    
}

/* Jsimplifierframe.java ends here. */
