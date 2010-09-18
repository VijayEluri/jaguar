/**
** <JMachineCanvas.java> -- The generic machine canvas
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
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import jaguar.machine.structures.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.util.jutil.*;
import jaguar.util.Debug;
import jaguar.machine.Machine;
import jaguar.machine.JMachine;

/**
 * Esta clase es la encargada de llamar a los painters de la máquina, ademas de detectar los movimientos  en el lienzo
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
abstract public class JMachineCanvas extends JPanel{
    /**
     * La dimensión del canvas para el Canvas
     **/
    private Dimension size;

    private JState currentJeDragged=null;
    private JState currentJeToolTip=null;

    public JMachine getJMachine(){
        return jmachine;
    }
    private JMachine jmachine;
    protected Delta jdelta;

    protected static  double ZOOM_FACTOR = 1;

    /**
       * Get the value of ZOOM_FACTOR.
       * @return Value of ZOOM_FACTOR.
       */
    public double getZOOM_FACTOR() {return ZOOM_FACTOR;}

    /**
       * Set the value of ZOOM_FACTOR.
       * @param v  Value to assign to ZOOM_FACTOR.
       */
    public void setZOOM_FACTOR(double  v) {
        this.ZOOM_FACTOR = v;
    }

    /**
     * La dimensión mínima del DfaCanvas
     */
    public static Dimension MINIMUM_DIMENSION = new Dimension(300,300);

    /**
     * La dimensión adecuada (preferred) del DfaCanvas
     */
    public static Dimension PREFERRED_DIMENSION = new Dimension(600,600);

    /**
     * La dimensión máxima del DfaCanvas
     */
//    public static Dimension MAXIMUM_DIMENSION = new Dimension(750,750);
    public static Dimension MAXIMUM_DIMENSION = new Dimension(1500,1500);


    /**
     * La lista de los JStates
     **/
    private Vector<JState> jeList;

    /**
     * Get the value of jestadosList.
     * @return value of jestadosList.
     */
    public Vector<JState> getJeList() {
        return jeList;
    }

    /**
     * Set the value of jestadosList.
     * @param v  Value to assign to jestadosList.
     */
    public void setJeList(Vector<JState>  v) {
        jeList = v;
    }

    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JMachineCanvas (){
        this(PREFERRED_DIMENSION);
    }

    public JMachineCanvas(Dimension _dimension){
        super();
        setBackground(Color.white);
        MyListener myListener = new MyListener();
        addMouseListener(myListener);
              addMouseMotionListener(myListener);
        size = _dimension;
        super.setSize(size);
        jdelta = null;
        getAccessibleContext().setAccessibleName("Delta Dfa Info setAccssN");
        setToolTipText(null);
    }

    public JMachineCanvas(Dimension _dimension, JMachine _jmachine){
        this(_dimension);
        initJMachineCanvas(_jmachine);
    }

    public void initJMachineCanvas(JMachine _jmachine){
        jmachine = _jmachine;
        Vector<JState> v = new Vector<JState>();
        JState jeCurrent;
        for (Iterator i = ((Machine)jmachine).getQ().iterator() ; i.hasNext() ;) {
            jeCurrent = (JState)i.next();
            jeCurrent.setEsEstadoInicial(jmachine.esInicial(jeCurrent));
            v.add(jeCurrent);
        }
        setJeList(v);
        setJDelta(((Machine)jmachine).getDelta());
    }

    public void setJDelta(Delta _jdelta){
        jdelta = _jdelta;
        ((JDeltaGraphic)jdelta).setJdeltapainter(new JDeltaPainter((JDeltaGraphic)jdelta));
    }

    public void setSize(Dimension d){
        if(size.getWidth() < d.getWidth())
            super.setSize(new Dimension((int)d.getWidth(),(int)size.getHeight()));
        if(size.getHeight() < d.getHeight())
            super.setSize(new Dimension((int)size.getWidth(),(int)d.getHeight()));
        size = super.getSize();
    }

    public void paint(Graphics g){
        fillBG(g);
        setBackground(Color.white);
        if(jmachine != null){
            ((JDeltaGraphic)jdelta).paint(g);
            JState je;
            for(int i = 0 ; i <jeList.size(); i++){
                je = (JState) jeList.elementAt(i);
                je.paint(g);
            }
            if(jmachine.getCurrentState()!=null)
                ((JState)jmachine.getCurrentState()).paint(g,JState.DEFAULT_CURRENT_STATE);
            else ((JState)jmachine.getPreviousNotNullCurrentState()).paint(g,JState.DEFAULT_CURRENT_STATE);
        }
    }


    /** maximumLayoutSize**/
    public Dimension maximumLayoutSize() {
        return MAXIMUM_DIMENSION;
    }
    /**
     * minimumLayoutSize
     */
    public Dimension minimumLayoutSize() {
        return MINIMUM_DIMENSION;
    }
    /**
     * Regresa la dimensión adecuada al layout manager
     */
     public Dimension getPreferredSize(){
   return super.getSize();
     }
    /**
     * Regresa la dimensión adecuada al layout manager
     */
    public Dimension getMinimumSize(){
        return MINIMUM_DIMENSION;
    }


    public JState someJeContains(int x , int y){
        JState je;
        for(int i = 0 ; i <jeList.size(); i++){
            je = (JState) jeList.elementAt(i);
            if(je.contains(x,y))
                return je;
        }
        return null;
    }

    public void fillBG(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0,0,(int)super.getSize().getWidth(),(int)super.getSize().getHeight());
    }

    /** Clase para manejar los drags & drops sobre los estados **/
    class MyListener extends MouseInputAdapter implements MouseMotionListener{
        public void mousePressed(MouseEvent e) {
            if(jmachine!= null)
                currentJeDragged = someJeContains(e.getX(),e.getY());
        }

        public void mouseDragged(MouseEvent e) {
            if(currentJeDragged != null)
                updateG(e);
        }

        public void mouseReleased(MouseEvent e) {
            if(currentJeDragged != null)
                updateG(e);
        }

        void updateG(MouseEvent e) {
            if(currentJeDragged != null){
                currentJeDragged.setLocation(new Point(e.getX(),e.getY()));
                repaint();
            }
        }

        /**
         * Si el mouse esta sobre un estado mostramos sus transiciones
         **/
        public void mouseMoved(MouseEvent e){
            if(jmachine != null && (currentJeToolTip = someJeContains(e.getX(), e.getY()))!=null)
                setToolTipText(jdelta.getToolTipString(currentJeToolTip));
            else
                setToolTipText(null);
        }
    }

}

/* JMachineCanvas.java ends here. */
