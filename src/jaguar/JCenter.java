/**
 * <JCenter.java> -- The control center to execute all the jaguar components
 *
 * Copyright (C) 2002 by  Ivan Hernández Serrano
 *
 * This file is part of JAGUAR
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Author: Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 *
 */

package jaguar;

import jaguar.machine.dfa.jdfa.*;
import jaguar.machine.dfa.jdfa.jconverters.*;
import jaguar.machine.stack.jstack.*;
import jaguar.machine.turing.jturing.*;
import jaguar.grammar.tipo2.jtipo2.*;
import jaguar.grammar.tipo3.jtipo3.*;
import jaguar.grammar.jgrammar.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * El centro de control para todas las opciones gráficas
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JCenter extends JPanel implements ComponentListener, ActionListener {

    protected JTabbedPane tabbedPane;


    /**
     * El valor por omisión para  tabbedPane
     */
    public static final JTabbedPane DEFAULT_TABBEDPANE = null;


    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_TABBEDPANE
     */
    public JCenter (){
        tabbedPane= new JTabbedPane();
        Component cdfa = makeDfaPanel();
        tabbedPane.addTab("fa", null, cdfa, "Operations on DFAs & NFAs");
        tabbedPane.setSelectedIndex(0);

        Component cstack = makeStackPanel();
        tabbedPane.addTab("fsa", null, cstack, "Operations on FSAs");

        Component cgrammar = makeGrammarPanel();
        tabbedPane.addTab("grammar", null, cgrammar, "Operations on Grammars");

        Component cturing = makeTuringPanel();
        tabbedPane.addTab("tm", null, cturing, "A Turing Machine");

        setLayout(new GridLayout(1,1));
        add(tabbedPane);
    }


    protected Component makeDfaPanel(){
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(0,2));

        JButton jdfaButton = new JButton("DFA");
        jdfaButton.setActionCommand("jdfa");
        jdfaButton.setToolTipText("Opens a graphic DFA");
        jdfaButton.addActionListener(this);

        panel.add(jdfaButton);

        JButton jminimizerButton = new JButton("DFA Minimizer");
        jminimizerButton.setActionCommand("jminimizer");
        jminimizerButton.setToolTipText("Minimize a DFA");
        jminimizerButton.addActionListener(this);

        panel.add(jminimizerButton);

        JButton jndfaButton = new JButton("NFA");
        jndfaButton.setActionCommand("jndfa");
        jndfaButton.setToolTipText("Opens a graphic NFA");
        jndfaButton.addActionListener(this);

        panel.add(jndfaButton);

        JButton jndfa2dfaButton = new JButton("NFA 2 DFA");
        jndfa2dfaButton.setActionCommand("jndfa2dfa");
        jndfa2dfaButton.setToolTipText("Transform a NFA into an equivalent DFA");
        jndfa2dfaButton.addActionListener(this);

        panel.add(jndfa2dfaButton);

        JButton jdfa2gt3Button = new JButton("DFA 2 T3 Grammar");
        jdfa2gt3Button.setActionCommand("jdfa2gt3");
        jdfa2gt3Button.setToolTipText("Transforms a  DFA into an equivalent  Grammar T3");
        jdfa2gt3Button.addActionListener(this);

        panel.add(jdfa2gt3Button);

        return panel;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("jdfa")) {
            JDfaFrame jdfaf = new JDfaFrame();
            jdfaf.show();
        }
        if (e.getActionCommand().equals("jndfa")) {
            JNDfaFrame jndfaf = new JNDfaFrame();
            jndfaf.show();
        }
        if (e.getActionCommand().equals("jminimizer")) {
            JMinimizerFrame jminimizer = new JMinimizerFrame();
            jminimizer.show();
        }
        if (e.getActionCommand().equals("jndfa2dfa")) {
            JNdfa2DfaFrame jndfa2dfa = new JNdfa2DfaFrame();
            jndfa2dfa.show();
        }
        if (e.getActionCommand().equals("jafs")) {
            JAfsFrame jafs = new JAfsFrame();
            jafs.show();
        }
        if (e.getActionCommand().equals("gtipo3")) {
            JGrammarFrameT3 jgt3 = new JGrammarFrameT3();
            jgt3.show();
        }
        if (e.getActionCommand().equals("gtipo2Stand")) {
            JSimplifierFrame jgt2Simp = new JSimplifierFrame();
            jgt2Simp.show();
        }
        if (e.getActionCommand().equals("jdfa2gt3")) {
            JDfa2Gtipo3Frame jdfa2gt3 = new JDfa2Gtipo3Frame();
            jdfa2gt3.show();
        }
        if (e.getActionCommand().equals("gtipo3ToAf")) {
            JGtipo32AFFrame jgt32af = new JGtipo32AFFrame();
            jgt32af.show();
        }
        if (e.getActionCommand().equals("jtm")) {
            JTuringFrame jtmf = new JTuringFrame();
            jtmf.show();
        }
    }


    protected Component makeStackPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(0,2));
        JButton jstackButton = new JButton("FSA");
        jstackButton.setActionCommand("jafs");
        jstackButton.setToolTipText("Opens a graphic FSA");
        jstackButton.addActionListener(this);

        panel.add(jstackButton);
        return panel;
    }


    protected Component makeTuringPanel(){
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(0,2));
        JButton jturingButton = new JButton("JTM");
        jturingButton.setActionCommand("jtm");
        jturingButton.setToolTipText("Opens a graphic TM");
        jturingButton.addActionListener(this);

        panel.add(jturingButton);
        return panel;
    }


    protected Component makeGrammarPanel(){
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(0,2));

        JButton gtipo3Button = new JButton("Type 3 Grammar");
        gtipo3Button.setActionCommand("gtipo3");
        gtipo3Button.setToolTipText("Opens a graphic  type 3 grammar");
        gtipo3Button.addActionListener(this);

        panel.add(gtipo3Button);

        JButton gtipo2Button = new JButton("Type 2 grammar Standardizer");
        gtipo2Button.setActionCommand("gtipo2Stand");
        gtipo2Button.setToolTipText("Standardize a Type 2 grammar");
        gtipo2Button.addActionListener(this);

        panel.add(gtipo2Button);

        JButton gt3ToAFButton = new JButton("Type 3 grammar 2 FA");
        gt3ToAFButton.setActionCommand("gtipo3ToAf");
        gt3ToAFButton.setToolTipText("Transform a grammar into an equivalente FA");
        gt3ToAFButton.addActionListener(this);

        panel.add(gt3ToAFButton);

        return panel;
    }


    /**
     * Rutinas de prueba para la clase JCenter.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("jaguar version 1, Copyright (C) 2001 Ivan Hernández Serrano\n" +
               "comes with ABSOLUTELY NO WARRANTY\n" +
               "This is free software, and you are welcome to redistribute it under certain conditions\n" +
               "for details see the license provided with this distribution\n");


        JFrame frame = new JFrame("jaguar.JCenter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new JCenter(), BorderLayout.CENTER);
        frame.setSize(400, 125);
        frame.setVisible(true);
    }


    public void componentHidden(ComponentEvent e) {};


    public void componentMoved(ComponentEvent e) {};


    public void componentResized(ComponentEvent e) {};


    public void componentShown(ComponentEvent e) {};
}

/* JCenter.java ends here. */
