/**
** <JTuringFrame.java> -- The frame to show a Turing Machine
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


package jaguar.machine.turing.jturing;

import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import jaguar.machine.structures.*;
import jaguar.machine.turing.structures.*;
import jaguar.machine.turing.jturing.jstructures.*;
import jaguar.util.Debug;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.*;
import java.io.File;

/**
 * JTuringFrame.java
 *
 *
 * Created: Sun Feb 11 20:21:47 2001
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
 **/
public class JTuringFrame extends JMachineFrame{

    public static final String MACHINE_TYPE = "TM";

    private JTuringCanvas jdc;

    /**
     * Get the value of jdc.
     * @return value of jdc.
     */
    public JTuringCanvas getJdc() {
        return jdc;
    }

    /**
     * Set the value of jdc.
     * @param v  Value to assign to jdc.
     */
    public void setJdc(JTuringCanvas  v) {
        jdc = v;
    }

    public JTuringFrame(){
        this(null);
    }
    public static final String JTURINGFRAME_NAME = "JTuring Frame";
    public JTuringFrame(JTuring _jturing) {
        super(JTURINGFRAME_NAME);
        jmachine = _jturing;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {dispose();}
            public void windowOpened(WindowEvent e) {}
        });

        addComponentListener(this);

        setJMenuBar(createMenu());

        setSize(DEFAULT_SIZE);
        if (jmachine != null)
            jdc = new JTuringCanvas(DEFAULT_SIZE,(JTuring)jmachine);
        else jdc = new JTuringCanvas(DEFAULT_SIZE);
        jScrollPaneCanvas = new JScrollPane(jdc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
        jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);

        setLabels("Tape");
        getContentPane().add(createJPanelBorder(jScrollPaneCanvas,"Configuration"), BorderLayout.CENTER);
        setControls(MACHINE_TYPE);
        if (jmachine == null) {
            nextButton.setEnabled(false);
            resetButton.setEnabled(false);
            runAllButton.setEnabled(false);
            quickTestButton.setEnabled(false);
            stopButton.setEnabled(false);
            loadTest.setEnabled(false);
            consTest.setEnabled(false);
            tabular.setEnabled(false);
            printM.setEnabled(false);
            descriptionMI.setEnabled(false);
        }
        fc = new JFileChooser();
    }

    /**
     ** Realiza un paso en la ejecución de la Máquina y regresa verdarero o falso dependiendo si puede seguir con la ejecución o no
     ** @return boolean <code>true</code> si se puede continuar con la ejecución de la máquina, <code>false</code> en  otro caso.
     **/
    public boolean nextStep(){
        boolean masTrans=jmachine.nextStep();
        nextStep(masTrans);
        sssd.insertTuringTape(((JTuring)jmachine).getTape());

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
            jmachine = new JTuring(file,this);
            //      System.err.println("\nJTuring = " + jmachine);
            jdc.initJMachineCanvas((JTuring)jmachine);
            printM.setEnabled(true);
            tabular.setEnabled(true);
            nextButton.setEnabled(true);
            resetButton.setEnabled(true);
            runAllButton.setEnabled(true);
            quickTestButton.setEnabled(true);
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
            JOptionPane.showMessageDialog(null,"Error loading  JTuring file "+file+"\n " +ex.getMessage(),"JTuring.",JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void resetMachine(){
        jmachine.resetMachine();
        sssd.reset();
        sssd.insertTuringTape(((JTuring)jmachine).getTape());
        currentStateLabel.setText(jmachine.getCurrentState().toString());
        nextButton.setEnabled(true);
        runAllButton.setEnabled(true);
        quickTestButton.setEnabled(true);
        stopButton.setEnabled(false);
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
        JTuringFrame f = new JTuringFrame();
        f.setVisible(true);
    }

    protected JMachine createNew() {
        // return new JDfa();
        return null;
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
} // JTuringFrame
