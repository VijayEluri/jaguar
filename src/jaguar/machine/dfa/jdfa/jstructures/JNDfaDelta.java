/**
** <JNDfaDelta.java> -- The graphical extension to the NDfaDelta
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


package jaguar.machine.dfa.jdfa.jstructures;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

import jaguar.util.Debug;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.structures.exceptions.*;
import java.io.IOException;
import java.io.File;
import jaguar.util.jutil.*;
import jaguar.machine.util.jutil.*;
/**
 * JNDfaDelta.java
 *
 *
 * Created: Sun Feb 11 23:47:13 2001
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version 0.1
 */

public class JNDfaDelta extends NDfaDelta implements JDeltaGraphic{
    /**
     * El que pinta los diagramas de las deltas
     */
    protected JNDfaDeltaPainter jdeltaPainter;
    /**
     * funcion de acceso para obtener el valor de jdeltaPainter
     * @return el valor actual de jdeltaPainter
     * @see #jdeltaPainter
     */
    public JDeltaPainter getJdeltapainter(){
	return jdeltaPainter;
    }
    /**
     * funcion de acceso para modificar jdeltaPainter
     * @param new_jdeltaPainter el nuevo valor para jdeltaPainter
     * @see #jdeltaPainter
     */
    public void setJdeltapainter(JDeltaPainter new_jdeltaPainter){
	jdeltaPainter=(JNDfaDeltaPainter)new_jdeltaPainter;
    }

    public JNDfaDelta (){
	super();
	jdeltaPainter = new JNDfaDeltaPainter();
    }


    public JNDfaDelta(NDfaDelta d, JStateSet sts){
	this();
	Object o [] = sts.toArray();
	Vector trans, atran;	
	Symbol s;
	JStateSet jsts;
	for(int i  = 0 ; i < o.length ; i++){
	    trans = d.getTransitions((State)o[i]);
	    if(trans != null)
		for(int j  = 0 ; j < trans.size(); j++){
		    atran = (Vector) trans.get(j);
		    s = (Symbol)atran.get(0);
		    jsts = new JStateSet((StateSet)atran.get(1));
		    
		    addTransition((State)o[i],new Symbol(s,Symbol.WITHOUT_TAGS),sts.makeStateReference(jsts));
		}
	}
	setJdeltapainter(new JNDfaDeltaPainter(this));
    }


    /**
     * Parte de la tercia d(p,sym) = q
     */
    private State current_p;
    /**
     * Get the value of current_p.
     * @return value of current_p.
     */
    public State getCurrent_p() {
	return current_p;
    }
    /**
     * Set the value of current_p.
     * @param v  Value to assign to current_p.
     */
    public void setCurrent_p(State  v) {
	this.current_p = v;
    }
    
    /**
     * Parte de la tercia d(p,sym) = q
     */
    private Symbol current_sym;
    
    /**
     * Get the value of current_sym.
     * @return value of current_sym.
     */
    public Symbol getCurrent_sym() {
	return current_sym;
    }
    
    /**
     * Set the value of current_sym.
     * @param v  Value to assign to current_sym.
     */
    public void setCurrent_sym(Symbol  v) {
	this.current_sym = v;
    }
    
    /**
     * Parte de la tercia d(p,sym) = q
     */
    private State current_q;
    /**
     * Get the value of current_q.
     * @return value of current_q.
     */
    public State getCurrent_q() {
	return current_q;
    }
    
    /**
     * Set the value of current_q.
     * @param v  Value to assign to current_q.
     */
    public void setCurrent_q(State  v) {
	this.current_q = v;
    }
    
    public void paint(Graphics g, State _p, Symbol sym, State _q){
	current_p = _p;
	current_sym = sym;
	current_q = _q;
	paint(g);
    }

    public void paint(Graphics g){
	jdeltaPainter.paint(g);
    }
    
    

    public String getLabelString(JState src, JState dest){
	Vector v = getTransitions(src);
	Vector vp;	
	String res = "";
	StateSet qs;
	Object oA[];
	for(int i = 0; i < v.size(); i++){
	    vp = (Vector) v.elementAt(i);
	    oA = ((StateSet)vp.elementAt(1)).toArray();
	    for(int j =0 ; j < oA.length ; j++)
		if(oA[j].equals(dest))
		    res += ((Symbol)vp.elementAt(0))+";";
	}
	return res.substring(0,res.length()-1);
    }
    
	
}// JNDfaDelta
