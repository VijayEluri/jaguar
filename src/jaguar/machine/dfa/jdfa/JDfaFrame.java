/**
** <JDfaFrame.java> -- The frame to show a DFA
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
import javax.swing.table.TableCellRenderer;
import javax.swing.border.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.jdfa.jstructures.*;
import jaguar.machine.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.structures.*;
import jaguar.util.Debug;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.machine.*;
import java.io.File;


/**
 * JDfaFrame.java
 *
 *
 * Created: Sun Feb 11 20:21:47 2001
 *
  * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version 0.1
 */

//public class JDfaFrame extends JFrame implements ComponentListener, ActionListener, Runnable{
public class JDfaFrame extends JMachineFrame{

    public static final String MACHINE_TYPE = "DFA";

    private JDfaCanvas jdc;

    /**
     * Get the value of jdc.
     * @return value of jdc.
     */
    public JDfaCanvas getJdc() {
        return jdc;
    }

    /**
     * Set the value of jdc.
     * @param v  Value to assign to jdc.
     */
    public void setJdc(JDfaCanvas  v) {
        jdc = v;
    }

    public JDfaFrame(){
        this(null);
    }
    public static final String JDFAFRAME_NAME = "JDFA Frame";

    public JDfaFrame(JDFA _jdfa){
        super(JDFAFRAME_NAME);

        jmachine = _jdfa;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {dispose();}
            public void windowOpened(WindowEvent e) {}
        });

        addComponentListener(this);

        setJMenuBar(createMenu());

        setSize(DEFAULT_SIZE);
        if(jmachine != null)
            jdc = new JDfaCanvas(DEFAULT_SIZE,(JDFA)jmachine);
        else jdc = new JDfaCanvas(DEFAULT_SIZE);

        jScrollPaneCanvas = new JScrollPane(jdc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
        jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);

        setLabels();
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
    public boolean nextStep() {
        //jdfa.nextStep();
        boolean masTrans=jmachine.nextStep();
        nextStep(masTrans);
        if (!jmachine.getStrToTestOrig().isEpsilon()) {
            JStr foo = jmachine.getSubStrTested();
            if (jmachine.getCurrentState() != null)
                currentStateLabel.setText(jmachine.getCurrentState().toString());
            Str pre, post;
            Symbol curr;
            pre = foo.substring(0,foo.length()-1);
            curr = foo.getLast();
            post = jmachine.getStrToTest();
            sssd.insertStr(pre,curr,post);
        }

        if (!masTrans) {
            stopExecution = true;
            runAllButton.setEnabled(false);
            quickTestButton.setEnabled(false);
            jmachine.displayResult();
        }//else Debug.println("mmm there are more transitions!!!");

        return masTrans;
    }



    protected JMachine createNew() {
        // return new JDfa();
        return null;
    }

    protected void initJMachine(File file){
        System.err.println("\nLoading...  " + file +" \n");
        try {
            jmachine = new JDFA(file,this);
            //      System.err.println(jmachine);
            jdc.initJMachineCanvas((JDFA)jmachine);
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
        } catch (Exception ex) {
            jmachine = null;
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error loading  JDFA file "+file,"JDFA",JOptionPane.ERROR_MESSAGE);
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
        JDfaFrame f = new JDfaFrame();
        f.show();
    }

    private static class RadioButtonRenderer implements TableCellRenderer {
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
          return null;
        return (Component) value;
      }
    }

    private static class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
      private JRadioButton button;

      public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
      }

      public Component getTableCellEditorComponent(JTable table, Object value,
          boolean isSelected, int row, int column) {
        if (value == null)
          return null;
        button = (JRadioButton) value;
        button.addItemListener(this);
        return (Component) value;
      }

      public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
      }

      public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
      }
    }

    public void showTabular(){
        TabularMachine ttm = new TabularMachine(jmachine.getColumnNames(), jmachine.getData(), jmachine);
        MyJTable table = ttm.getMyJTable();
        table.getColumn("Initial").setCellRenderer(
                new RadioButtonRenderer());
        table.getColumn("Initial").setCellEditor(
                new RadioButtonEditor(new JCheckBox()));
    }

} // JDfaFrame
