/**
** <JMachine.java> -- The graphical Machine's features
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


package jaguar.machine;

/**
 * JDFA.java
 *
 *
 * Created: Wed Feb 21 00:15:15 2001
 *
 * @author <a href="mailto: "</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:03 $
 */

import java.awt.Graphics;
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.jstructures.*;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.lang.Math;

public  interface JMachine {
    /**
     * Get the value of g.
     * @return value of g.
     */
    public Graphics getG();
    
    
    /**
     * Set the value of g.
     * @param v  Value to assign to g.
     */
    public void setG(Graphics  v);
    
    
    /**
     * Get the value of machineframe.
     * @return value of machineframe.
     */
    public JMachineFrame getMachineFrame();    

    /**
     * Set the value of dfaframe.
     * @param v  Value to assign to dfaframe.
     */
    public void setJMachineFrame(JMachineFrame  v);
    
    /**
     * Get the value of strToTestOrig, que es la cadena original que
     * fue cargada para checar si pertenece a L(this) 
     * @return value of strToTestOrig.
     */
    public JStr getStrToTestOrig();
    
    
    /**
     * Set the value of strToTestOrig.
     * @param v  Value to assign to strToTestOrig.
     */
    public void setStrToTestOrig(JStr  v);
    
    
    /**
     * Get the value of strToTest.
     * @return value of strToTest.
     */
    public JStr getStrToTest();
    
    
    /**
     * Set the value of strToTest.
     * @param v  Value to assign to strToTest.
     */
    public void setStrToTest(JStr  v);
    
    /**
     * Get the value of subStrTested.
     * @return Value of subStrTested.
     */
    public JStr getSubStrTested();
    
    
    /**
     * Set the value of subStrTested.
     * @param v  Value to assign to subStrTested.
     */
    public void setSubStrTested(JStr  v);    
    
    
    /**
     * Get the value of currentState.
     * @return value of currentState.
     */
    public State getCurrentState();
    
    
    /**
     * Set the value of currentState.
     * @param v  Value to assign to currentState.
     */
    public void setCurrentState(State  v);
    
    
    /**
     * Reinicializa el JDFA a los valores de inicio<br>
     * Los valores reinicializados son:<br>
     * <ul>
     * <li>Al estado actual le asigna <b>q0</b></li>
     * <li>A la cadena para probar le asigna la cadena original a probar asignada por medio de
     *       <code>setStrToTestOrig(JStr)</code></li>
     */     
    public void resetMachine();
    

    /**
     * Regresa verdadero si podemo hacer un paso más o falso si no
     * podemos
     * @return <code>true</code> - si podemos seguir aplicando la
     * función de transición delte, i.e. si la cadena a checar no es
     * <epsilon> y si el estado en el que estamos es distinto de
     * <code>null</code>. <br> <code>false</code> - en otro caso.
     */
    public boolean nextStep();
    
    
    /**
     * Despliega el resultado de la ejecución del autómata como un cuadrito en el <code>dfaframe</code>
     * asociado
     */
    public void displayResult();
    
    
    /**
     * Esta función se usa para asignar posiciones a los centros de
     * los JStates.  Estas posiciones, están alrededor de un circulo
     * de radio <code>r</code>, dividiendo y encontramos la posición
     * de cada estado por medio de coordenadas polares (<code>(x,y) =
     * (r*cos*theta, r*sin*theta)</code>).  Donde la theta es cada uno
     * de los intervalos de dividir 360 entre la cardinalidad de Q y r
     * es el minimo entre el ancho y alto del canvas entre dos.
     */
    public Alphabet getSigma();
    
    /**
     * funcion de acceso para obtener el valor de tableVector
     * @return el valor actual de tableVector, donde la entrada
     * tableVector.get(0) es el header y tableVector.get(1) es un
     * vector que contiene los renglones
     */
    public Vector getTableVector();

    
    /**
     * funcion de acceso para obtener el valor de previousNotNullCurrentState
     * @return el valor actual de previousNotNullCurrentState
     */
    public State getPreviousNotNullCurrentState();
    
    
    /**
     * Dado un estado dice si es o no es un estado inicial
     * @param p el estado sobre el cual preguntaremos si es o no inicial en ésta máquina
     * @return <code>true</code> si <code>p</code> es estado inicial
     */
    public boolean esInicial(State p);    
    
    
    /**
     * Imprime la jmachine en un ambiente gráfico
     */
    public void print(Graphics g);
}
