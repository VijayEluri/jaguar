/**
* <State.java> -- to use a state
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
* @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
**/


package jaguar.structures;

import java.awt.Point;
import java.io.FileWriter;
import java.io.File;
import jaguar.structures.exceptions.StateNotFoundException;
import org.w3c.dom.*;

public class State{
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "state";
    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String BEG_TAG = "<"+ELEMENT_NAME+">";

    /**
     * El tag con el que se define el fin del objeto de un 
     * en un archivo
     */
    public static final String END_TAG = "</"+ELEMENT_NAME+">";
    
    /**
     * El estado por default no es un estado final
     **/
    protected boolean isInF  = false;
    /**
     * Get the value of isInF.
     * @return value of isInF.
     */
    public boolean getIsInF() {
	return isInF;
    }
    
    /**
     * Set the value of isInF.
     * @param _isInF  Value to assign to isInF.
     */
    public void setIsInF(boolean  _isInF) {
	isInF = _isInF;
    }
    /**
     * Marca este  estado como un estado final
     */
    public void markAsFinal(){
	setIsInF(true);	
    }
    
    /**
     * La etiqueta del estado
     **/
    protected String label = null;    
    /**
     * Get the value of label.
     * @return value of label.
     */
    public String getLabel() {
	return label;
    }
    
    /**
     * Set the value of label.
     * @param v  Value to assign to label.
     */
    public void setLabel(String  v) {
	this.label = v;
    }       
    /**
     * Función equals
     **/
    public boolean equals(Object o){
	if(o instanceof State && label.equals(((State)o).getLabel()))
	    return true;
	return false;
    }

     /**
      * Regresa el hashCode, basado en <code>label</code>
      **/
     public int hashCode(){
	 return getLabel().hashCode();
     }

    /**
     * La constructura con la etiqueta como parametro, por default es
     * un estado no final, es equivalente a usar la construtora <code>State(_label,false)</code>
     * @param _label la etiqueta del estado
     */
    private State(){
	this("",false);
    }

    public State(State p){
	this(p.getLabel(),p.getIsInF());
    }

    /**
     ** Construye un state dado el documento DOM
     **/
    public State(org.w3c.dom.Node domNode){
        this(domNode.getChildNodes().item(0).getNodeValue());
        NamedNodeMap attributes = domNode.getAttributes();
        if (attributes.getLength() > 0) {
            int xpos = Integer.parseInt(attributes.getNamedItem("xpos").getNodeValue());
            int ypos = Integer.parseInt(attributes.getNamedItem("ypos").getNodeValue());
            this.setLocation(xpos, ypos);
        }
    }
    
    /**
     * La constructura con la etiqueta como parametro, por default es
     * un estado no final, es equivalente a usar la construtora <code>State(_label,false)</code>
     * @param _label la etiqueta del estado
     */
    public State(String _label){
	this(_label,false);
    }

    /**
     * La constructura con la etiqueta como parametro, y un booleano
     * que indica si es o no es un estado final  
     * @param _label la etiqueta del estado
     * @param _isInF indica si es un estado final
     */
    public State(String _label, boolean _isInF){
        this(_label, 0, 0, _isInF);
    }

    /**
     * La constructura con la etiqueta como parametro, y un booleano
     * que indica si es o no es un estado final  
     * @param _label la etiqueta del estado
     * @param _isInF indica si es un estado final
     */
    public State(String _label, double x, double y, boolean _isInF){
        label = _label;
        isInF = _isInF;
        location = new Point();
        setLocation(x,y);
    }    

    
    /**
     * Función que regresa la representación como cadena de
     * <code>State</code>
     **/
    public String toString(){
	return getLabel();
    }


    /** 
     * Escribe la representación del <code>State</code> en un archivo con el formato definido por el DTD correspondiente
     * Escribe el State con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el State.
     */
    public void toFile(FileWriter fw, boolean withLocation){
	try{ 
	    if (withLocation) {
            fw.write("<" + ELEMENT_NAME + " xpos=\"" + ((int) location.getX()) + "\" " + "ypos=\"" + ((int) location.getY()) + "\" >" + getLabel() + END_TAG);
	    } else {
            fw.write(BEG_TAG + getLabel() + END_TAG);
	    }
	    
	}catch( Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
    
     public void toFile(FileWriter fw) {
         toFile(fw, false);
     }
    
    /**
     * El punto dode se localiza en el componente gráfico
     **/
    protected Point location;

    /**
     * Get the value of location.
     * @return value of location.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Set the value of location.
     * @param p Value to assign to location.
     */
    public void setLocation(Point  p) {
        location = p;
    }

    /**
     * Set the value of location.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setLocation(double x, double y) {
        location.setLocation(x,y);
    }
}

/* State.java ends here */
