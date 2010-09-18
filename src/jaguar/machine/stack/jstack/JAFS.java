/**
** <JAFS.java> -- The AFS's graphical extension
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

import jaguar.machine.*;
import jaguar.machine.stack.*;
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.stack.exceptions.*;
import jaguar.machine.stack.structures.*;
import jaguar.machine.stack.structures.exceptions.*;
import jaguar.machine.stack.jstack.jstructures.*;
import java.io.*;
import java.util.*;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.event.TableModelEvent;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JList;
import java.lang.Math;
import java.awt.event.ActionEvent;

 /**
 * La extensión gráfica de la máquina de stack
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JAFS extends AFS implements JMachine{
    /**
     * El contexto gráfico sobre el cual se dibujará  el AFS
     */
    protected Graphics g;
    /**
     * El valor por omisión para g
     */
    public static final Graphics DEFAULT_G=null;
    /**
     * funcion de acceso para obtener el valor de g
     * @return el valor actual de g
     * @see #g
     */
    public Graphics getG(){
        return g;
    }
    /**
     * funcion de acceso para modificar g
     * @param new_g el nuevo valor para g
     * @see #g
     */
    public void setG(Graphics new_g){
        g = new_g;
    }

    private JAfsFrame afsframe;

    /**
     * Get the value of dfaframe.
     * @return value of dfaframe.
     */
    public JMachineFrame getMachineFrame() {
        return afsframe;
    }

    /**
     * Set the value of dfaframe.
     * @param v  Value to assign to dfaframe.
     */
    public void setJMachineFrame(JMachineFrame  v) {
        this.afsframe = (JAfsFrame)v;
    }

    /**
     * La cadena que vamos a pasarle al AFS P para ver si pertenece a L(P)
     */
    protected JStr strToTestOrig;
    /**
     * El valor por omisión para strToTestOrig
     */
    public static final JStr DEFAULT_STRTOTESTORIG=null;
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
        strToTestOrig = new_strToTestOrig;
    }

    /**
     * La cadena sobre la que estamos ejecutando el AFS, en un instante dado
     */
    protected JStr strToTest;
    /**
     * El valor por omisión para strToTest
     */
    public static final JStr DEFAULT_STRTOTEST=null;
    /**
     * funcion de acceso para obtener el valor de strToTest
     * @return el valor actual de strToTest
     * @see #strToTest
     */
    public JStr getStrToTest(){
        return strToTest;
    }
    /**
     * funcion de acceso para modificar strToTest
     * @param new_strToTest el nuevo valor para strToTest
     * @see #strToTest
     */
    public void setStrToTest(JStr new_strToTest){
        strToTest = new_strToTest;
    }


    /**
     * La subcadena de la cadena de entrada que sobre la cual ya fue ejecutado el AFS
     */
    protected JStr subStrTested;
    /**
     * El valor por omisión para subStrTested
     */
    public static final JStr DEFAULT_SUBSTRTESTED=null;
    /**
     * funcion de acceso para obtener el valor de subStrTested
     * @return el valor actual de subStrTested
     * @see #subStrTested
     */
    public JStr getSubStrTested(){
        return subStrTested;
    }
    /**
     * funcion de acceso para modificar subStrTested
     * @param new_subStrTested el nuevo valor para subStrTested
     * @see #subStrTested
     */
    public void setSubStrTested(JStr new_subStrTested){
        subStrTested = new_subStrTested;
    }

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
        previousNotNullCurrentState = new_previousNotNullCurrentState;
    }

    /**
     * El estado actual en el que se encuentra la máquina
     */
    protected State currentState;
    /**
     * El valor por omisión para currentState
     */
    public static final State DEFAULT_CURRENTSTATE=null;
    /**
     * funcion de acceso para obtener el valor de currentState
     * @return el valor actual de currentState
     * @see #currentState
     */
    public State getCurrentState(){
        return currentState;
    }
    /**
     * funcion de acceso para modificar currentState
     * @param new_currentState el nuevo valor para currentState
     * @see #currentState
     */
    public void setCurrentState(State new_currentState){
        currentState = new_currentState;
    }


    public void resetMachine(){
        ((JStackDelta)delta).setCurrent_p(new JState("resetP"));
        ((JStackDelta)delta).setCurrent_q(new JState("resetQ"));
        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
        setStrToTest(getStrToTestOrig());
        setSubStrTested(new JStr());
        setSobranteStr(getStrToTestOrig());
        setStack(new Stack<Symbol>());
        stack.push(getZ0());
              ((JAfsFrame)afsframe).setJPanelStackContents(this);
        ((JAfsFrame)afsframe).getReglalabel().setText("");
        ((JStackDelta)delta).setCurrent_sym(new Symbol("reset"));
        ((JAfsFrame)getMachineFrame()).getJdc().repaint();
    }

    /**
     * Aplica la función delta con los parametros dados
     * @param currentS el estado sobre el que estamos
     * @param cad la cadena sobre cuyo primer símbolo aplicaremos la delta
     * @param sym el símbolo en el tope del stack
     * @return el valor de  delta(currentS,cad.getFirst())
     */
    protected State doTransition(State currentS, JStr cad, Symbol sym){
        System.err.println("doTransition");
        State result ;
        result  = super.doTransition(currentS,(Str)cad);
        ((jaguar.machine.stack.jstack.jstructures.JStackDelta)delta).setCurrent_p(currentS);
        ((JStackDelta)delta).setCurrent_sym(cad.getFirst());
        if(!stack.empty())
            ((JStackDelta)delta).setCurrent_zsym((Symbol)stack.peek());
        ((jaguar.machine.stack.jstack.jstructures.JStackDelta)delta).setCurrent_q(result);
        return result;
    }

    /**
     * Regresa verdadero si podemo hacer un paso más o falso si no podemos
     * @return <code>true</code> - si podemos seguir aplicando la
     * función de transición delte, i.e. si la cadena a checar no es
     * <epsilon> y si el estado en el que estamos es distinto de
     * <code>null</code>. <br> <code>false</code> - en otro caso.
     */
    public boolean nextStep(){
        if (!strToTest.isEpsilon() && !stack.empty() && currentState!=null) {
            getSubStrTested().concat(new JStr(strToTest.getFirst()));
            setPreviousNotNullCurrentState(currentState);
            ((jaguar.machine.stack.jstack.jstructures.JStackDelta)delta).setCurrent_p(currentState);
            ((jaguar.machine.stack.jstack.jstructures.JStackDelta)delta).setCurrent_sym(strToTest.getFirst());
            currentState = doTransition(currentState,strToTest);
            ((jaguar.machine.stack.jstack.jstructures.JStackDelta)delta).setCurrent_q(currentState);
            setStrToTest((JStr)strToTest.substring(1));
            //       if(!stack.empty())
            //     Debug.println("===>>>> " + stack +"<<<=== En el tope tenemos ["+stack.peek()+"]");
            //       else
            //     Debug.println("===>>>> [] <<<=== El stack ya esta vacio");
            if(getStrToTest().isEpsilon() || stack.empty() || getAppliedRule() == null)
                return false;
            return true;
        }//else Debug.println("===>>>> strToTest:" + strToTest + "\n\tstack "+ stack + "\n\tcurrentState: " + currentState+" <<<===");
        return false;
    }

    public void displayResult(){
        if(currentState == null)
            JOptionPane.showMessageDialog((JFrame)afsframe,"The SFA DOES NOT accept the string "+getStrToTestOrig(),"SFA Result", JOptionPane.INFORMATION_MESSAGE);
        else if (getF().size() ==0) {
            if (stack.empty() && getStrToTest().isEpsilon())
                JOptionPane.showMessageDialog((JFrame)afsframe,"Criterion EMPTY STACK:  The SFA accepts the string "+getStrToTestOrig(), "SFA Result",JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog((JFrame)afsframe,"Criterion EMPTY STACK:  The SFA DOES NOT accept the string "+getStrToTestOrig(), "SFA Result",JOptionPane.INFORMATION_MESSAGE);
        } else
            JOptionPane.showMessageDialog((JFrame)afsframe,"Criterion FINAL STATE: The FSA "+(getCurrentState().getIsInF()?" accepts ":" DOES NOT accepts ") + "the string "+getStrToTestOrig(), "FSA Result",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pinta el estado como el estado actual, bajo el contexto de la aplicación más reciente de la función de transición
     * delta, bajo el contexto delta(p,s) = q
     **/
    protected void displayTransResult(State q){
        ((JState)q).paint(afsframe.getJdc().getGraphics(),JState.DEFAULT_CURRENT_STATE);
    }

    /**
     * Esta función se usa para asignar posiciones a los centros de los  JStates.  Estas posiciones,
     * están alrededor de un circulo de radio <code>r</code>,  dividiendo y
     * encontramos la posición de cada estado por medio de coordenadas polares (<code>(x,y) =  (r*cos*theta, r*sin*theta)</code>).
     * Donde la  theta es cada uno de los intervalos de dividir 360 entre la cardinalidad de Q y r es el minimo entre el ancho y alto del
     * canvas entre dos.
     */
    public  void initStatesPosition(Dimension d){
        int cardinalidadQ = getQ().size();
        if(cardinalidadQ > 0){
            double radio;
            if(d.getWidth() != 300 && d.getHeight() != 300)
                radio = Math.min((d.getWidth() - 250)/2,(d.getHeight() - 250)/2);
            else radio = Math.min((d.getWidth() - 75)/2,(d.getHeight() - 75 )/2);
            radio +=10;
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


    public JAFS(StateSet _Q,Alphabet _Sigma, Alphabet _Gamma, StackDelta _delta, State _q0, Symbol _Z0, StateSet _F, Graphics _g){
        this( _Q, _Sigma, _Gamma, _delta, _q0, _Z0,  _F);
        setG(_g);
    }

    public JAFS(StateSet _Q,Alphabet _Sigma, Alphabet _Gamma, StackDelta _delta, State _q0, Symbol _Z0, StateSet _F){
        super( _Q, _Sigma,  _Gamma, _delta, _q0, _Z0, _F);
        setCurrentState(_q0);
        setPreviousNotNullCurrentState(_q0);
    }

    protected JAFS(){
        super();
    }

    /**
     * Constructora que construye un JAFS a partir del nombre de un archivo que es valido segun el DTD de los AFSs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public JAFS(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un JAFS a partir del nombre de un archivo que es valido segun el DTD de los AFSs
     * @param jafsframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public JAFS(String filename, JAfsFrame jafsframe)throws Exception{
        this(new File(filename),jafsframe);
    }

    /**
     * Constructora que construye un JAFS a partir del nombre de un archivo que es valido segun el DTD de los AFSs
     * @param jafsframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public JAFS(File file, JAfsFrame jafsframe)throws Exception{
        this(file);
        setJMachineFrame(jafsframe);
        initStatesPosition(afsframe.getJScrollPaneCanvas().getViewport().getViewSize());
        afsframe.getJdc().repaint();
    }

    /**
     * Constructora que construye un JAFS a partir del nombre de un archivo que es valido segun el DTD de los AFSs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/afs.dtd">afs.dtd</a>
     */
    public JAFS(File file)throws Exception{
        super(file);
        setQ(new JStateSet(getQ()));
        setQ0(new JState(getQ0()));
        setF(new JStateSet(getF()));
        setDelta(new JStackDelta((StackDelta)getDelta(),(JStateSet)getQ()));

        setCurrentState((JState)getQ0());
        setPreviousNotNullCurrentState((JState)getQ0());
        makeStateReferences();
    }


    /**
     * funcion de acceso para obtener el valor de tableVector
     * @return el valor actual de tableVector, donde la entrada tableVector.get(0) es el header y tableVector.get(1) es un vector que contiene los renglones
     */
    public Vector getTableVector(){return null;}

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
        Symbol[] aSigma = getSigma().toArray();
        String[] colNames = new String[getSigma().size() + 4];
        colNames[0] = "Q";
        colNames[1] = "Gamma";
        for (int i = 0; i < getSigma().size(); i++ ) {
            colNames[i+2] = aSigma[i].getSym();
        }
        colNames[aSigma.length+2] = "Initial";
        colNames[aSigma.length+3] = "Final";
        return colNames;
    }

    public Object[][] getData() {
        State[] aQ = getQ().toArray();
        Symbol[] aSigma = getSigma().toArray();
        Symbol[] aGamma = getGamma().toArray();
        Object[][] data = new Object[aQ.length*aGamma.length][aSigma.length+4];
        int k = 0;
        int l = 2;
        QxGammaStarSet entry;
        ButtonGroup inicialSelector = new ButtonGroup();
        for (State i : aQ) {
            data[k][0] = i.toString();
            JRadioButton inicial = new JRadioButton("",esInicial(i));
            inicialSelector.add(inicial);
            data[k][aSigma.length+2] = inicial;
            data[k][aSigma.length+3] = new Boolean(i.getIsInF());
            for (Symbol h : aGamma) {
                data[k][1] = h.toString();

                for (Symbol j : aSigma) {
                    entry = ((StackDelta) getDelta()).apply(i,j,h);
                    data[k][l] = (entry != null) ? entry.toString() : null;
                    ++l;
                }
                ++k;
                l = 2;
            }
        }
        return data;
    }

    /**
     * Rutinas de prueba para la clase JAFS.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase JAFS. \n"
               +"Comentario: La extensión gráfica de la máquina de stack\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
    }

    /**
     ** Dado un estado dice si es o no es un estado inicial
     ** @param p el estado sobre el cual preguntaremos si es o no inicial en ésta máquina
     ** @return true si <code>p</code> es estado inicial
     **/
    public boolean esInicial(State p){
        return p.equals(getQ0());
    }

    public void print(Graphics g){
        afsframe.getJdc().paint(g);
    }


    public void tableChanged(TableModelEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        if ("add_state".equals(e.getActionCommand())) {
            // JState newState = new JState("q"+Q.size());
            // Q.add(newState);
            // newState.setLocation(50,50);
            // dfaframe.getJdc().getJeList().add(newState);
            // //initStatesPositions();
            // dfaframe.showTabular();
            // dfaframe.getJdc().repaint();
            return;
        }

        if ("remove_state".equals(e.getActionCommand())) {
            // // Ask for confirmation first
            // // find wich state is selected and delete it.
            // State[] states = Q.toArray();
            // int idx = dfaframe.getSelectedRowInTTM();
            // if (idx >= 0) {
            //     JState state = (JState)states[idx];
            //     int n = JOptionPane.showConfirmDialog(dfaframe,
            //         "Are you sure that you want to delete the state "
            //         + state + "?",
            //         "Confirm deletion",
            //         JOptionPane.YES_NO_OPTION,
            //         JOptionPane.WARNING_MESSAGE);
            //     if (n != 0) {
            //         return;
            //     }
            //     dfaframe.getJdc().getJeList().remove(state);
            //     Q.remove(state);
            //     Hashtable<State,Hashtable<Symbol,State>> deltaHash = delta.getD();
            //     deltaHash.remove(state);
            //
            //     for(Enumeration enu = deltaHash.keys();  enu.hasMoreElements() ;) {
            //         // Ahora para cada estado de estos tenemos que sacar todas sus transiciones
            //         JState q = (JState)enu.nextElement();
            //         Hashtable<Symbol,State> toHash= deltaHash.get(q);
            //         for(Enumeration f = toHash.keys();  f.hasMoreElements() ;) {
            //             Symbol s = (Symbol)f.nextElement();
            //             if (toHash.get(s).equals(state)) {
            //                 toHash.remove(s);
            //             }
            //         }
            //     }
            //
            //     dfaframe.showTabular();
            //     dfaframe.getJdc().repaint();
            // }
        }
    }
}

/* JAFS.java ends here. */
