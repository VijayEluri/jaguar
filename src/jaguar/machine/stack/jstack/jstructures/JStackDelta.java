/**
** <JStackDelta.java> -- The AFS's specific delta
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


package jaguar.machine.stack.jstack.jstructures;

import jaguar.machine.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.structures.*;
import jaguar.machine.stack.structures.*;
import jaguar.machine.stack.structures.exceptions.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

 /** 
 * La extensión gráfica de la función de transición delta de un AFS 
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JStackDelta extends StackDelta implements JDeltaGraphic{
    /**
     * El que pinta los diagramas de las deltas
     */
    protected JDeltaPainter jdeltaPainter;
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
	jdeltaPainter=new_jdeltaPainter;
    }
    
    public JStackDelta(){
	super();
	setJdeltapainter(new JDeltaPainter());
    }

    public JStackDelta(StackDelta d, JStateSet sts){
	this();
	Object o [] = sts.toArray();
	Vector trans, atran;	
	Symbol sSigma, sGamma;
	JState jst;
	QxGammaStarSet qxgsSet;
	QxGammaStar qxgs;
	Object oQtimesGSSet[];	
	for(int i  = 0 ; i < o.length ; i++){
	    trans = d.getTransitions((State)o[i]);
	    for(int j  = 0 ; j < trans.size(); j++){
		atran = (Vector) trans.get(j);
		sSigma = (Symbol)atran.get(0);
		sGamma = (Symbol)atran.get(1);
		qxgsSet = (QxGammaStarSet)atran.get(2);
		oQtimesGSSet = qxgsSet.toArray();		
		for(int k = 0 ; k < oQtimesGSSet.length; k++){
		    qxgs = (QxGammaStar)oQtimesGSSet[k];
		    addTransition(sts.makeStateReference((State)o[i]),
				  sSigma, sGamma,
				  sts.makeStateReference(qxgs.getQ()),
				  qxgs.getGammaStar());
		    
		}
	    }
	}
	setJdeltapainter(new JDeltaPainter(this));
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
     * El símbolo actual en el tope del stack
     */
    protected Symbol current_zsym;
    /**
     * El valor por omisión para current_zsym
     */
    public static final Symbol DEFAULT_CURRENT_ZSYM=null;
    /**
     * funcion de acceso para obtener el valor de current_zsym
     * @return el valor actual de current_zsym
     * @see #current_zsym
     */
    public Symbol getCurrent_zsym(){
	return current_zsym;
    }
    /**
     * funcion de acceso para modificar current_zsym
     * @param new_current_zsym el nuevo valor para current_zsym
     * @see #current_zsym
     */
    public void setCurrent_zsym(Symbol new_current_zsym){
	current_zsym=new_current_zsym;
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
	QxGammaStarSet trans;
	QxGammaStar ctrans;
	Object aTrans[];
	String res = "";
	for(int i = 0; i < v.size(); i++){
	    vp = (Vector) v.elementAt(i);
	    trans = (QxGammaStarSet)vp.elementAt(2);
	    aTrans = trans.toArray();
	    for(int j = 0 ; j < aTrans.length; j ++){
		ctrans = (QxGammaStar)aTrans[j];
		if(ctrans.getQ().equals(dest))
		    res += ((Symbol)vp.elementAt(0))+"/"+ ((Symbol)vp.elementAt(1))+", "+ctrans.getGammaStar() +";";
	    }
	}
	res = res.substring(0,res.length()-1);
	return res;
    }
}

/* JAfsDelta.java ends here. */
