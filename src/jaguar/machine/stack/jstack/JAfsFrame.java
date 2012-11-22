/**
** <JAfsFrame.java> -- The frame to show JAFSs
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


package jaguar.machine.stack.jstack;

import java.util.Vector;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import jaguar.machine.structures.*;
import jaguar.machine.stack.structures.*;
import jaguar.machine.stack.jstack.jstructures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.util.Debug;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.machine.*;
import java.io.File;


/**
 * JAfsFrame.java
 *
 *
 * Created: Sun Feb 11 20:21:47 2001
 *
  * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version 0.1
 */

public class JAfsFrame extends JMachineFrame{

    public static final String MACHINE_TYPE = "FSA";

    private JAfsCanvas jdc;

    /**
     * Get the value of jdc.
     * @return value of jdc.
     */
    public JAfsCanvas getJdc() {
        return jdc;
    }

    /**
     * Set the value of jdc.
     * @param v  Value to assign to jdc.
     */
    public void setJdc(JAfsCanvas  v) {
        jdc = v;
    }


    /**
     * La representación gráfica del stack
      */
    protected JPanel jPanelStack;

    /**
     * f uncion de acceso para obtener el valor de gStack
     * @return el valor actual de gStack
     */
    public JPanel getJPanelStack(){
        return jPanelStack;
    }
    /**
     * funcion de acceso para modificar gStack.
     * @param _jPanelStack el nuevo valor para _getJPanelStack
     */
    public void setJPanelStack(JPanel _jPanelStack){
        jPanelStack=_jPanelStack;
    }


     /**
      * La representación gráfica del stack
      */
    protected JList gStack;

    /**
     * f uncion de acceso para obtener el valor de gStack
     * @return el valor actual de gStack
     * @see #gStack
     */
    public JList getGstack(){
        return gStack;
    }
    /**
     * funcion de acceso para modificar gStack
     * @param new_gStack el nuevo valor para gStack
     * @see #gStack
     */
    public void setGstack(JList new_gStack){
        gStack = new_gStack;
    }

     /**
      * La etiqueta que nos mostrará la regla que usamos en el paso actual
      */
    protected JLabel reglaLabel;
    /**
     * funcion de acceso para obtener el valor de reglaLabel
     * @return el valor actual de reglaLabel
     * @see #reglaLabel
     */
    public JLabel getReglalabel(){
        return reglaLabel;
    }
    /**
     * funcion de acceso para modificar reglaLabel
     * @param new_reglaLabel el nuevo valor para reglaLabel
     * @see #reglaLabel
     */
    public void setReglalabel(JLabel new_reglaLabel){
        reglaLabel=new_reglaLabel;
    }

    public JAfsFrame(){
        this(null);
    }
    /** La dimensión de cada una de las celdas del stack **/
    public static final Dimension STACK_CELL_DIMENSION = new Dimension(75,20);

    /** Esta es para la primera vez **/
    private void createJPanelStack(JAFS jafs){
        if(jafs!=null)
                  setJPanelStackContents(jafs);
              else{
                  jPanelStack = new JPanel();
                  jPanelStack.setLayout(new BoxLayout(jPanelStack,BoxLayout.Y_AXIS));
            jPanelStack.add(Box.createRigidArea(new Dimension(70,0)));
            jPanelStack.setBackground(Color.white);
                  jPanelStack.setMinimumSize(new Dimension(85,20));
        }
        JScrollPane jscrollp=new JScrollPane(jPanelStack,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscrollp.getVerticalScrollBar().setUnitIncrement(20);
        jscrollp.getHorizontalScrollBar().setUnitIncrement(10);
        getContentPane().add(JMachineFrame.createJPanelBorder(jscrollp,"Stack"), BorderLayout.EAST);
    }

    protected void setJPanelStackContents(JAFS jafs){
        Stack srev = (Stack)jafs.getStack().clone();
        if (jPanelStack == null) {
            jPanelStack = new JPanel();
            jPanelStack.setLayout(new BoxLayout(jPanelStack,BoxLayout.Y_AXIS));
            jPanelStack.add(Box.createRigidArea(new Dimension(70,0)));
            jPanelStack.setBackground(Color.white);
            jPanelStack.setMinimumSize(new Dimension(85,20));
        }//else jPanelStack.removeAll();

        jPanelStack.removeAll();
        JTextField jt=null;
        jPanelStack.add(Box.createVerticalGlue());
        while (!srev.isEmpty()) {
            jt = new JTextField(srev.pop().toString());
            jt.setHorizontalAlignment(JTextField.CENTER);
            jt.setBackground(Color.white);
            jt.setEditable(false);
            jt.setPreferredSize(STACK_CELL_DIMENSION);
            jt.setMinimumSize(STACK_CELL_DIMENSION);
            jt.setMaximumSize(STACK_CELL_DIMENSION);
            jPanelStack.add(jt);
        }
        jPanelStack.add(Box.createRigidArea(new Dimension(70,0)));
        jPanelStack.repaint();
    }

    public static final Dimension JAFSFRAME_DEFAULT_SIZE  = new Dimension(700,600);

    public static final String JAFSFRAME_NAME = "JSFA Frame";

    public JAfsFrame(JAFS _jafs){
        super(JAFSFRAME_NAME);
        jmachine = _jafs;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {dispose();}
            public void windowOpened(WindowEvent e) {}
        });

        addComponentListener(this);

        setJMenuBar(createMenu());

        setSize(JAFSFRAME_DEFAULT_SIZE);
        if (jmachine != null) {
            jdc = new JAfsCanvas(DEFAULT_SIZE,(JAFS)jmachine);
        } else {
            jdc = new JAfsCanvas(DEFAULT_SIZE);
        }

        jScrollPaneCanvas = new JScrollPane(jdc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
        jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);

        createJPanelStack((JAFS)jmachine);

        setLabels();
        getContentPane().add(createJPanelBorder(jScrollPaneCanvas,"Configuration"), BorderLayout.CENTER);
        setControls(MACHINE_TYPE);
        if (jmachine == null) {
            printM.setEnabled(false);
            nextButton.setEnabled(false);
            resetButton.setEnabled(false);
            runAllButton.setEnabled(false);
            quickTestButton.setEnabled(false);
            stopButton.setEnabled(false);
            loadTest.setEnabled(false);
            consTest.setEnabled(false);
            descriptionMI.setEnabled(false);
        }
        fc = new JFileChooser();
    }

    /**
     * Pone las etiquetas de estado actual y la cadena actual con la configuraci'on de symbolo actual  en otro color, si es la primera vez, la cadena es epsilon]
     */
    protected void setLabels(){
        JPanel pEstadoRegla = new JPanel();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        pEstadoRegla.setLayout(new BoxLayout(pEstadoRegla,BoxLayout.X_AXIS));
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
        reglaLabel = new JLabel("");
        reglaLabel.setToolTipText("Used rule");
        pEstadoRegla.add(Box.createRigidArea(new Dimension(10, 10)));
        pEstadoRegla.add(createJPanelBorder(currentStateLabel,"State"));
        pEstadoRegla.add(Box.createRigidArea(new Dimension(10, 10)));
        pEstadoRegla.add(createJPanelBorder(reglaLabel,"Rule"));


        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(pEstadoRegla);
        p.add(Box.createRigidArea(new Dimension(10, 0)));
        p.add(createJPanelBorder(textPane,"String"));
        p.add(Box.createHorizontalGlue());

        getContentPane().add(p, BorderLayout.NORTH);
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
            reglaLabel.setText(""+((JAFS)jmachine).getAppliedRule());

            Str pre, post;
            Symbol curr;
            pre = foo.substring(0,foo.length()-1);
            curr = foo.getLast();
            post = jmachine.getStrToTest();
            sssd.insertStr(pre,curr,post);
        }
              setJPanelStackContents((JAFS)jmachine);

        getJdc().repaint();
        if(!masTrans){
            stopExecution = true;
            runAllButton.setEnabled(false);
                  quickTestButton.setEnabled(false);
            jmachine.displayResult();
        }//else Debug.println("mmm there are more transitions!!!");
        return masTrans;
    }


    protected void initJMachine(File file){
        System.err.println("\nLoading...  " + file +" \n");
        try{
            jmachine = new JAFS(file,this);
            //      System.err.println("\nJFSA = " + jmachine);
            jdc.initJMachineCanvas((JAFS)jmachine);
            printM.setEnabled(true);
            nextButton.setEnabled(true);
            resetButton.setEnabled(true);
            runAllButton.setEnabled(true);
            save.setEnabled(true);
            quickTestButton.setEnabled(true);
            tabular.setEnabled(true);
            stopButton.setEnabled(false);
            currentStateLabel.setText(jmachine.getCurrentState().toString());
            reglaLabel.setText("");
            jmachine.setStrToTest(new JStr());
            jmachine.setStrToTestOrig(new JStr());
            sssd.insertStr(jmachine.getStrToTest());
            loadTest.setEnabled(true);
            consTest.setEnabled(true);
            jScrollPaneCanvas.getViewport().setViewPosition(new Point(0,0));
            setJPanelStackContents((JAFS)jmachine);

            jmachine.resetMachine();
            descriptionMI.setEnabled(true);
        } catch(Exception ex) {
            jmachine = null;
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error loading  JAFS file "+file,"JAFS",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
        if (jdc !=null) {
            jdc.setSize(e.getComponent().getSize());
        }
    }

    public void componentShown(ComponentEvent e) {}


    public static void main(String []argv){
        JAfsFrame f = new JAfsFrame();
        f.setVisible(true);
    }

    protected JMachine createNew() {
        String alphabetStr = (String)JOptionPane.showInputDialog(
                            this,
                            "You must enter a comma separated list of symbols for use as input alphabet",
                            "Provide input alphabet",
                            JOptionPane.PLAIN_MESSAGE);

        Alphabet sigma = new Alphabet(alphabetStr);

        String gammaStr = (String)JOptionPane.showInputDialog(
                            this,
                            "You must enter a comma separated list of symbols for use as the stack alphabet, the first symbol will be the initial.",
                            "Provide stack alphabet",
                            JOptionPane.PLAIN_MESSAGE);

        Alphabet gamma = new Alphabet(gammaStr);

        String stateNumbStr = (String)JOptionPane.showInputDialog(
                            this,
                            "You must enter a number to set how many states your new AFS will have.",
                            "How many states?",
                            JOptionPane.PLAIN_MESSAGE);

        int totalStates = Integer.parseInt(stateNumbStr);
        JStateSet states = new JStateSet();
        JState initial = new JState("q0");
        states.add(initial);

        for (int i = 1; i < totalStates; ++i) {
            states.add(new JState("q" + i));
        }

        jmachine = new JAFS(states, sigma, gamma, new JStackDelta(),  initial, gamma.toArray()[0], new JStateSet());

        jdc.initJMachineCanvas((JAFS)jmachine);
        printM.setEnabled(true);
        tabular.setEnabled(true);
        save.setEnabled(true);
        nextButton.setEnabled(true);
        resetButton.setEnabled(true);
        runAllButton.setEnabled(true);
        quickTestButton.setEnabled(true);
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

        ((JAFS) jmachine).initStatesPosition(getJScrollPaneCanvas().getViewport().getViewSize());
        file = null;

        return jmachine;
    }

    public void showTabular(){
        super.showTabular();
        MyJTable table = ttm.getMyJTable();
        table.setDefaultRenderer(String.class, new StrRenderer(jmachine));
        table.getColumn("Initial").setCellRenderer(
                new ButtonRenderer<JRadioButton>());
        table.getColumn("Initial").setCellEditor(
                new ButtonEditor<JRadioButton>(new JCheckBox()));
        table.getColumn("Final").setCellRenderer(
                new ButtonRenderer<JCheckBox>());
        table.getColumn("Final").setCellEditor(
                new ButtonEditor<JCheckBox>(new JCheckBox()));
    }

} // JAfsFrame
