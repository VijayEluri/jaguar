/**
** <JConverterFrame.java> -- The JConverter's generic frame
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


package jaguar.util.jutil;

import jaguar.machine.Machine;
import jaguar.util.*;
import jaguar.machine.util.jutil.JMachineCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JConverter
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
abstract public class JConverterFrame extends JFrame implements ComponentListener, ActionListener{
    protected JButton convertButton;
    protected JButton detailsButton;
    protected JFileChooser fc;
    protected JDesktopPane desktop;
    protected JScrollPane textScroll;
    protected JTextArea detailsArea;    
    /**
     ** El tamaño por omisión para el convertidor.
     **/
    public static final Dimension DEFAULT_SIZE  = new Dimension(650,465);

    public static final int SPLIT_DIVIDER_LOCATION = 300;

    public static final Dimension MACHINE_SIZE = new Dimension(300,300);
    
    /**
     * El número de objeto cargado sobre el que estamos trabajando actualmente, este número se incrementa cada que cargamos un nuevo objeto a convertir
     * este número también se asocia al resultado
     */
    protected int currentObjectToConvert = 0;
    /**
     * funcion de acceso para obtener el valor de currentObjectToConvert
     * @return el valor actual de currentObjectToConvert
     * @see #currentObjectToConvert
     */
    public int getCurrentobjecttoconvert(){
	return currentObjectToConvert;
    }
    /**
     * funcion de acceso para modificar currentObjectToConvert
     * @param new_currentObjectToConvert el nuevo valor para currentObjectToConvert
     * @see #currentObjectToConvert
     */
    public void setCurrentobjecttoconvert(int new_currentObjectToConvert){
	currentObjectToConvert=new_currentObjectToConvert;
    }

     /**
 * El motor para la conversión
 */
 protected JConverter jEngineConverter;
 /**
 * funcion de acceso para obtener el valor de jEngineConverter
 * @return el valor actual de jEngineConverter
 * @see #jEngineConverter
 */
 public JConverter getJengineconverter(){
 return jEngineConverter;
 }
 /**
 * funcion de acceso para modificar jEngineConverter
 * @param new_jEngineConverter el nuevo valor para jEngineConverter
 * @see #jEngineConverter
 */
 public void setJengineconverter(JConverter new_jEngineConverter){
 jEngineConverter=new_jEngineConverter;
 }    
    
    JSplitPane splitPane;

    public JConverterFrame(String title, String detailsAMessage){	
	super(title);
	setSize(DEFAULT_SIZE);	
	setJMenuBar(createMenu());
	fc = new JFileChooser();
	detailsArea = new JTextArea(detailsAMessage);
	detailsArea.setFont(new Font("Serif", Font.PLAIN, 12));
	detailsArea.setLineWrap(true);
	detailsArea.setEditable(false);
	textScroll = new JScrollPane(detailsArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
	desktop =new JDesktopPane();
	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,desktop,textScroll);
	splitPane.setOneTouchExpandable(true);
	splitPane.setDividerLocation(SPLIT_DIVIDER_LOCATION);
	splitPane.setPreferredSize(DEFAULT_SIZE);//new Dimension(200,100));
	getContentPane().add(splitPane);
	desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }
    
    protected void setControls(String convertionButtonName){
	JToolBar toolbar = new JToolBar("Controls");
	toolbar.setFloatable(true);
	convertButton = new JButton(convertionButtonName);
	convertButton.setEnabled(false);	
	detailsButton = new JButton("Detalles...");

	toolbar.add(convertButton);
	toolbar.add(detailsButton);
    }

    protected JMenuItem doConvertionMenuItem;
    protected JMenuItem saveResultMenuItem;    
    
    abstract protected JMenuBar createMenu();
    /** Este es para el menufile **/
    protected JMenu menu;
    
    /**
     ** Crea el menú con las configuraciones básicas de este Frame
     **/
    protected JMenuBar createMenu(String messageLoad,String messageTipLoad,String messageConvertion,String messageTipConvertion,
				  String messageSaveResult, String messageTipSaveResult) {
	JMenuBar mb = new JMenuBar();	
	menu = new JMenu("File");
	menu.setMnemonic(KeyEvent.VK_F);
	JMenuItem loadOrig = new  JMenuItem(messageLoad,KeyEvent.VK_L);
	loadOrig.getAccessibleContext().setAccessibleDescription(messageTipLoad);
	loadOrig.addActionListener(this);
	loadOrig.setActionCommand("loadorig");
	loadOrig.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
	menu.add(loadOrig);

	doConvertionMenuItem = new JMenuItem(messageConvertion, KeyEvent.VK_D);	
	doConvertionMenuItem.getAccessibleContext().setAccessibleDescription(messageTipConvertion);
	doConvertionMenuItem.addActionListener(this);
	doConvertionMenuItem.setActionCommand("doconvertion");
	doConvertionMenuItem.setEnabled(false);
	doConvertionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
	menu.add(doConvertionMenuItem);

	saveResultMenuItem = new JMenuItem(messageSaveResult, KeyEvent.VK_S);
	saveResultMenuItem.getAccessibleContext().setAccessibleDescription(messageTipSaveResult);
	saveResultMenuItem.addActionListener(this);
	saveResultMenuItem.setActionCommand("saveconvertionresult");
	saveResultMenuItem.setEnabled(false);
// para activar el save del principal decomentar esto 
//	menu.add(saveResultMenuItem);
	
	JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });
	quit.setMnemonic(KeyEvent.VK_Q);
	menu.add(quit);
	mb.add(menu);	

	return mb;
    }
    

    public void componentHidden(ComponentEvent e) {}
    
    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {}

    public void actionPerformed(ActionEvent e){
	if (e.getActionCommand().equals("loadorig"))
	    if(loadOrigFromFile()){		
		doConvertionMenuItem.setEnabled(true);
	    }
        if(e.getActionCommand().equals("doconvertion")){
	    if(doConvertion())
		saveResultMenuItem.setEnabled(true);
	}
	if(e.getActionCommand().equals("saveconvertionresult"))
	    saveResultFromConvertion();	    	
    }

    abstract public void saveResultFromConvertion();
   	
    protected void saveResultFromConvertion(String dialogTitlePrefix){
	fc.setDialogTitle(dialogTitlePrefix + getCurrentobjecttoconvert());	
	int returnVal = fc.showSaveDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();		
	    try{
		toFile(new FileWriter(file));
		JOptionPane.showMessageDialog(this,"El resultado ha sido guardado!","Saving status",JOptionPane.INFORMATION_MESSAGE);
	    }catch( IOException ouch){
		System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
				   + " Not saved: " ); 
		ouch.printStackTrace();
		JOptionPane.showMessageDialog(this,"El resultado NO fue guardado!","Saving status",JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    abstract public void toFile(FileWriter fw);
    
    abstract public boolean doConvertion();
    
    abstract protected void loadOrigFromFile(File file)  throws Exception;
    
    abstract protected boolean loadOrigFromFile();

    public boolean loadOrigFromFile(String dialogTitle){
	fc.setDialogTitle(dialogTitle);	
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    try{
		loadOrigFromFile(file);
//		desktop.add(loadOrigFromFile(file));
	    }catch(Exception e){
		e.printStackTrace();		
		return false;		
	    }
	    return true;
	}
	return false;	
    }

    public class JInternalMachineFrame extends JInternalFrame implements ActionListener{
	JMachineCanvas gCanvas;

	public JInternalMachineFrame(JMachineCanvas gCanvas, String title){
	    this(gCanvas,title,true,true,true,true);
	}
	public JInternalMachineFrame(JMachineCanvas gCanvas, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable){
	    super(title, resizable, closable, maximizable, iconifiable);
	    this.gCanvas = gCanvas;	    
	    JMenuBar menuBar = new JMenuBar();
	    JMenu fileMenu = new JMenu("File");
	    JMenuItem saveCurrent = new JMenuItem("Save Machine....",KeyEvent.VK_S);
	    saveCurrent.getAccessibleContext().setAccessibleDescription("Guarda esta máquina");
	    saveCurrent.addActionListener(this);
	    saveCurrent.setActionCommand("save");
	    saveCurrent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0));		
	    fileMenu.add(saveCurrent);
	    
	    menuBar.add(fileMenu);
	    
	    setJMenuBar(menuBar);
	}
	
	public void actionPerformed(ActionEvent e) {
	    Debug.println("ActionEvent"+ e);
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
		    ((Machine)(gCanvas.getJMachine())).toFile(new FileWriter(file));
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
    }
}

/* JConverterFrame.java ends here. */
