/**
** <QxGammaxDirection.java> -- To represent part of the Turing Machine's delta transition
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


package jaguar.machine.turing.structures;

import java.util.*;
import java.io.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.turing.structures.exceptions.*;
import jaguar.machine.util.*;
import jaguar.util.*;
import jaguar.machine.turing.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/** 
 * La clase
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class QxGammaxDirection{
    /**
     ** El estado <em>q</em> del la tercia ordenada QxTx{LEFT,RIGTH}
     **/
    protected State q;
    /**
     ** funcion de acceso para obtener el valor de q
     ** @return el valor actual de q
     ** @see #q
     **/
    public State getQ(){
	return q;
    }
    /**
     ** funcion de acceso para modificar q
     ** @param new_q el nuevo valor para q
     ** @see #q
     **/
    public void setQ(State new_q){
	q=new_q;
    }
    /**
     ** La segunda entrada <em>sym</em> de la tercia ordenada QxTx{LEFT,RIGTH}
     **/
    protected Symbol aGamma;
    /**
     * funcion de acceso para obtener el valor de aGamma
     * @return el valor actual de aGamma
     * @see #aGamma
     */
    public Symbol getAGamma(){
	return aGamma;
    }
    /**
     * funcion de acceso para modificar aGamma
     * @param new_aGamma el nuevo valor para aGamma
     * @see #aGamma
     */
    public void setAGamma(Symbol new_aGamma){
	aGamma=new_aGamma;
    }

     /**
      * La tercer entrada <em>direction</em> de la tercia ordenada QxTx{LEFT,RIGTH}
      */
    protected int direction;
    /**
     * funcion de acceso para obtener el valor de direction
     * @return el valor actual de direction
     * @see #direction
     */
    public int getDirection(){
	return direction;
    }
    /**
     * funcion de acceso para modificar direction
     * @param new_direction el nuevo valor para direction
     * @see #direction
     */
    public void setDirection(int new_direction){
	direction=new_direction;
    }

    protected QxGammaxDirection(){
	q = null;
	aGamma = null;
	direction = Turing.INVALID_DIRECTION;
    }

    public QxGammaxDirection(org.w3c.dom.Node node)throws Exception{
	NodeList transitions = node.getChildNodes();
	Node qNode=null,sNode=null, dNode=null;	
	int i=0;
	boolean itemFound = false;	
	for(i = 0 ; !itemFound && i < transitions.getLength() ; i++)
	    if(transitions.item(i).getNodeType() == Node.ELEMENT_NODE){
		qNode = transitions.item(i);
		itemFound = true;
	    }
	itemFound = false;
	for( ; !itemFound && i < transitions.getLength() ; i++)
	    if(transitions.item(i).getNodeType() == Node.ELEMENT_NODE){
		sNode = transitions.item(i);
		itemFound = true;
	    }
	itemFound = false;
	for( ; !itemFound && i < transitions.getLength() ; i++)
	    if(transitions.item(i).getNodeType() == Node.ELEMENT_NODE){
		dNode = transitions.item(i);
		itemFound = true;
	    }		
//	System.err.println("\n\t\tqNode: " + qNode);
//	System.err.println("\t\tstrNode: " + strNode);		
	setQ(new State(qNode));
	setAGamma(new Symbol(sNode));
	setDirection((dNode.getNodeName().equals(Turing.RIGHT_ELEMENT_NAME))?1:-1);	
    }

    public QxGammaxDirection(State _q, Symbol _aGamma, int _direction){
	q = _q;
	aGamma = _aGamma;
	direction = _direction;
    }


    public String toString(){
	return "( " + ((q==null)?"null":q.toString()) + " , " +
	    ((aGamma == null)? "null": aGamma.toString()) + " , " + Turing.getStringDirection(direction)+ " ) ";
    }

    public void toFile(FileWriter fw){
 	try{ 
 	    fw.write(" "+BEG_TAG+" ");
 	    q.toFile(fw);
	    fw.write(" ");
 	    aGamma.toFile(fw);
	    fw.write(" "+ Turing.getStringDirection(direction));	    
	    fw.write(" ");
 	    fw.write(END_TAG);
 	}catch( IOException ouch){
 	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
 			       + "  Trying toFile: " ); 
 	    ouch.printStackTrace(); 
 	}
    }

    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "QxGammaxDirection";
    public static final String BEG_TAG = "<"+ELEMENT_NAME+">";
    public static final String END_TAG = "</"+ELEMENT_NAME+">";
	
}

/* QxGammaxDirection.java ends here. */


