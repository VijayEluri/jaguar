/**
** <JTuring.java> -- The Turing's graphical extension
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

/**
 * La clase para la TM con componentes gráficos
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
import java.awt.Graphics;
import java.awt.Dimension;
import jaguar.machine.turing.*;
import jaguar.machine.JMachine;
import jaguar.machine.turing.exceptions.*;
import jaguar.util.*;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.turing.structures.*;
import jaguar.machine.turing.jturing.jstructures.*;
import jaguar.machine.turing.structures.exceptions.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.event.TableModelEvent;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.awt.event.ActionEvent;

public class JTuring extends Turing implements JMachine {
    /**
     * La cadena sobre la que estamos ejecutando la TM, está puede cambiar conforme vamos ejecutando la TM
     */
    protected JStr strToTest;
    /**
     * funcion de acceso para obtener el valor de strToTest
     * @return el valor actual de strToTest
     */
    public JStr getStrToTest(){
        return strToTest;
    }
    /**
     * funcion de acceso para modificar strToTest
     * @param new_strToTest el nuevo valor para strToTest
     */
    public void setStrToTest(JStr new_strToTest){
        strToTest=new_strToTest;
    }

    /**
     * La cadena a checar si pertenece a L(TM)
     */
    protected JStr strToTestOrig;

    /**
     * funcion de acceso para obtener el valor de strToTestOrig
     * @return el valor actual de strToTestOrig
     * @see #strToTestOrig
     */
    public JStr getStrToTestOrig(){
        return strToTestOrig;
    }
    /**
     * funcion de acceso para modificar strToTestOrig
     * @param new_strToTestOrig el nuevo valor para strToTestOrig
     * @see #strToTestOrig
     */
    public void setStrToTestOrig(JStr new_strToTestOrig){
        strToTestOrig=new_strToTestOrig;
    }
    /**
     * El frame donde se mostrará está TM
     */
    protected JTuringFrame turingFrame;
    /**

    /**
     * funcion de acceso para obtener el valor de turingFrame
     * @return el valor actual de turingFrame
     * @see #turingFrame
     */
    public JMachineFrame getMachineFrame(){
        return turingFrame;
    }
    /**
     * funcion de acceso para modificar turingFrame
     * @param new_turingFrame el nuevo valor para turingFrame
     * @see #turingFrame
     */
    public void setJMachineFrame(JMachineFrame new_turingFrame){
        this.turingFrame=(JTuringFrame)new_turingFrame;
    }
    /**
     * El contexto gráfico sobre el cual se dibujará
     */
    protected Graphics g;
    /**
     * funcion de acceso para obtener el valor de g
     * @return el valor actual de g
     */
    public Graphics getG(){
        return g;
    }
    /**
     * funcion de acceso para modificar g
     * @param new_g el nuevo valor para g
     */
    public void setG(Graphics new_g){
        g=new_g;
    }
    /**
     * La subcadena de la cadena de entrada que ya ha sido probada
     */
    JStr subStrTested;

    /**
       * Get the value of subStrTested.
       * @return Value of subStrTested.
       */
    public JStr getSubStrTested() {return subStrTested;}

    /**
       * Set the value of subStrTested.
       * @param v  Value to assign to subStrTested.
       */
    public void setSubStrTested(JStr  v) {this.subStrTested = v;}

    /**
      * El estado anterior antes de ser nulo, esto nos sirve para dar un último estado y checar si esta en los finales, en caso de tener que hacer este chequeo
      */
    protected State previousNotNullCurrentState;
    /**
     * funcion de acceso para obtener el valor de previousNotNullCurrentState
     * @return el valor actual de previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public State getPreviousNotNullCurrentState(){
        return previousNotNullCurrentState;
    }
    /**
     * funcion de acceso para modificar previousNotNullCurrentState
     * @param new_previousNotNullCurrentState el nuevo valor para previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public void setPreviousNotNullCurrentState(State new_previousNotNullCurrentState){
        previousNotNullCurrentState=new_previousNotNullCurrentState;
    }
    /**
     * Reinicializa la JTuring a los valores de inicio<br>
     * Los valores reinicializados son:<br>
     * <ul>
     * <li>Al estado actual le asigna <b>q0</b></li>
     * <li>A la cadena para probar le asigna la cadena original a probar asignada por medio de
     *       <code>setStrToTestOrig(JStr)</code></li>
     */
    public void resetMachine(){
        ((JTuringDelta)delta).setCurrent_p(new JState("resetP"));
        ((JTuringDelta)delta).setCurrent_q(new JState("resetQ"));
        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
        setStrToTest(getStrToTestOrig());
        setSubStrTested(new JStr());
        ((JTuringDelta)delta).setCurrent_sym(new Symbol("reset"));
        ((JTuringFrame)getMachineFrame()).getJdc().repaint();
        initializeMachine(getStrToTestOrig());
    }

    public JTuring(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, TuringDelta _delta, State _q0, Symbol _blanco,
       StateSet _F, Graphics _g){
           this(_Q, _Sigma, _Gamma, _delta, _q0, _blanco, _F);
           setG(_g);
    }

    public JTuring(Turing turing){
        this(new JStateSet(turing.getQ()),turing.getSigma(),turing.getGamma(), (TuringDelta)turing.getDelta(),
             new JState(turing.getQ0()), turing.getBlanco(), turing.getF());
        setDelta(new JTuringDelta((TuringDelta)getDelta(),(JStateSet)getQ()));
    }

    public JTuring(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, TuringDelta _delta, State _q0, Symbol _blanco,
       StateSet _F){
           super(_Q,_Sigma,_Gamma,_delta,_q0,_blanco,_F);
           setCurrentState(getQ0());
           setPreviousNotNullCurrentState(getQ0());
    }

    protected JTuring(){
        super();
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las Máquinas de Turing
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el las Máquinas de Turing
     * @param jturingframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(String filename,JTuringFrame jturingframe)throws Exception{
        this(new File(filename),jturingframe);
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las Máquinas de Turing
     * @param jturingframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(File file,JTuringFrame jturingframe)throws Exception{
        this(file);
        setJMachineFrame(jturingframe);
        initStatesPosition(turingFrame.getJScrollPaneCanvas().getViewport().getViewSize());
        turingFrame.getJdc().repaint();
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las Máquinas de Turing
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(File file)throws Exception{
        super(file);
        setQ(new JStateSet(getQ()));
        setQ0(new JState(getQ0()));
        setF(new JStateSet(getF()));
        setDelta(new JTuringDelta((TuringDelta)getDelta(),(JStateSet)getQ()));

        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
        makeStateReferences();
    }


    public void print(Graphics g){
        turingFrame.getJdc().paint(g);
    }
    /**
     * Esta función se usa para asignar posiciones a los centros de
     * los JStates.  Estas posiciones, están alrededor de un circulo
     * de radio <code>r</code>, dividiendo y encontramos la posición
     * de cada estado por medio de coordenadas polares (<code>(x,y) =
     * (r*cos*theta, r*sin*theta)</code>).  Donde la theta es cada uno
     * de los intervalos de dividir 360 entre la cardinalidad de Q y r
     * es el minimo entre el ancho y alto del canvas entre dos.
     */
    public  void initStatesPosition(Dimension d){
        int cardinalidadQ = getQ().size();
        if(cardinalidadQ > 0){
            double radio;
            if(d.getWidth() != 300 && d.getHeight() != 300)
                radio = Math.min((d.getWidth() - 250)/2,(d.getHeight() - 250)/2);
            else radio = Math.min((d.getWidth() - 75)/2,(d.getHeight() - 75 )/2);
                  radio+=20;
            JState current;
            double intervalo = 360 / cardinalidadQ;
            double currentIntervalo = 0;
            for (Iterator i = getQ().iterator() ; i.hasNext() ;) {
                current = (JState)i.next();
                current.setLocation(radio * Math.cos(Math.toRadians(currentIntervalo))+radio,
                        radio * Math.sin(Math.toRadians(currentIntervalo))+radio);
                currentIntervalo += intervalo;
            }
        }
    }


    /**
     * Regresa verdadero si podemo hacer un paso más o falso si no podemos
     * @return <code>true</code> - si podemos seguir aplicando la función de transición delte, i.e. si la cadena a checar
     * no es <epsilon>  y si el estado en el que estamos es distinto de <code>null</code>. <br>
     * <code>false</code> - en otro caso.
     */
    public boolean nextStep(){
        setPreviousNotNullCurrentState(currentState);
        doTransition();
        ((JTuringDelta)delta).setCurrent_p(previousNotNullCurrentState);
        ((JTuringDelta)delta).setCurrent_sym(getLastTransitionSymbol());
        ((JTuringDelta)delta).setCurrent_q(currentState);
        turingFrame.getJdc().repaint();
        return !getHalt();
    }
    /**
     * Despliega el resultado de la ejecución del autómata como un cuadrito en el <code>turingframe</code>
     * asociado
     */
    public void displayResult(){
        if(currentState == null)
            JOptionPane.showMessageDialog((JFrame)turingFrame,"The TM DOES NOT accepts the string "+getStrToTestOrig(),"TM Result", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog((JFrame)turingFrame,"The TM "+(getCurrentState().getIsInF()?" accepts ":" DOES NOT accept ") + " the string "+getStrToTestOrig(), "TM Result",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * La representación en vector de la función de transición delta
     */
    protected Vector tableVector = DEFAULT_TABLEVECTOR;
    /**
     * El valor por omisión para tableVector
     */
    public static final Vector DEFAULT_TABLEVECTOR=null;

    public int getFirstEditableColumn() {
        return 1;
    }

    public Class getColumnClass(int c) {
        int idx = c - (getSigma().size()+2);
        switch (idx) {
            case 0:
                return JRadioButton.class;
            case 1:
                return Boolean.class;
        }
        return String.class;
    }

    public String[] getColumnNames() {
        Symbol[] aGamma = getGamma().toArray();
        String[] colNames = new String[aGamma.length + 3];
        colNames[0] = "Q";
        for (int i = 0; i < aGamma.length; i++ ) {
            colNames[i+1] = aGamma[i].getSym();
        }
        colNames[aGamma.length+1] = "Initial";
        colNames[aGamma.length+2] = "Final";
        return colNames;
    }

    public Object[][] getData() {
        State[] aQ = getQ().toArray();
        Symbol[] aSigma = getSigma().toArray();
        Symbol[] aGamma = getGamma().toArray();
        Object[][] data = new Object[aQ.length][aGamma.length+3];
        int k = 0;
        int l = 1;
        QxGammaxDirection entry;
        ButtonGroup inicialSelector = new ButtonGroup();
        for (State i : aQ) {
            data[k][0] = i.toString();
            JRadioButton inicial = new JRadioButton("",esInicial(i));
            inicialSelector.add(inicial);
            data[k][aGamma.length+1] = inicial;
            data[k][aGamma.length+2] = new JCheckBox("",i.getIsInF());
            for (Symbol h : aGamma) {
                entry = ((TuringDelta) getDelta()).apply(i,h);
                data[k][l] = (entry != null) ? entry.toCommaSeparatedList() : null;
                ++l;
            }
            ++k;
            l = 1;
        }

        return data;
    }

    /**
     ** Dado un estado dice si es o no es un estado inicial
     ** @param p el estado sobre el cual preguntaremos si es o no inicial en ésta máquina
     ** @return true si <code>p</code> es estado inicial
     **/
    public boolean esInicial(State p){
        return p.equals(getQ0());
    }

    public void tableChanged(TableModelEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        if ("add_state".equals(e.getActionCommand())) {
            JState newState = new JState("q"+Q.size());
            Q.add(newState);
            newState.setLocation(50,50);
            turingFrame.getJdc().getJeList().add(newState);
            // initStatesPositions();
            turingFrame.showTabular();
            turingFrame.getJdc().repaint();
            return;
        }else if ("remove_state".equals(e.getActionCommand())) {
            // Ask for confirmation first
            // find wich state is selected and delete it.
            State[] states = Q.toArray();
            int rowIdx = turingFrame.getSelectedRowInTTM();
            int idx = rowIdx/3;
            if (idx >= 0) {
                JState state = (JState)states[idx];
                int n = JOptionPane.showConfirmDialog(turingFrame,
                    "Are you sure that you want to delete the state "
                    + state + "?",
                    "Confirm deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (n != 0) {
                    return;
                }
                turingFrame.getJdc().getJeList().remove(state);
                Q.remove(state);
                Hashtable<State,Hashtable<Symbol,QxGammaxDirection>> deltaHash = ((TuringDelta)delta).getD();
                deltaHash.remove(state);

                for(Enumeration enu = deltaHash.keys();  enu.hasMoreElements() ;) {
                    // Ahora para cada estado de estos tenemos que sacar todas sus transiciones
                    JState q = (JState)enu.nextElement();
                    Hashtable<Symbol,QxGammaxDirection> toHash = deltaHash.get(q);
                    for(Symbol s : toHash.keySet()) {
                        QxGammaxDirection qxgxd = toHash.get(s);
                        if (qxgxd.getQ().equals(state)) {
                            toHash.remove(s);
                        }
                    }
                }

                turingFrame.showTabular();
                turingFrame.getJdc().repaint();
            }
        }
    }
}

/* JTuring.java ends here. */
