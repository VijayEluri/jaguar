/**
** <Machine.java> -- The common Machine's features 
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

import java.util.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.util.Debug;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 * Machine.java
 *
 *
 * Created: Wed Dec 20 21:33:00 2000
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hern'andez</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:03 $
 */

abstract public class Machine {
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "machine";
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
     ** El nombre del elemento descripción, debe de ser igual al descrito en las DTDs de máquinas
     **/
    public static final String DESCRIPTION_ELEMENT_NAME = "description";
    /**
     * El tag con el que se define el inicio del objeto de un 
     * en un archivo
     */
    public static final String DESCRIPTION_BEG_TAG = "<"+DESCRIPTION_ELEMENT_NAME+">";
    /**
     * El tag con el que se define el fin del objeto de un 
     * en un archivo
     */
    public static final String DESCRIPTION_END_TAG = "</"+DESCRIPTION_ELEMENT_NAME+">";
    /**
     ** Para que tengan la capacidad de leer checando la entrada con el DTD
     **/
    protected DocumentBuilderFactory factory;

    /**
     * Una descripción de la máquina
     */
    protected String machineDescription=" ";
    /**
     * funcion de acceso para obtener el valor de machineDescription
     * @return el valor actual de machineDescription
     * @see #machineDescription
     */
    public String getMachineDescription(){
	return machineDescription;
    }
    /**
     * funcion de acceso para modificar machineDescription
     * @param new_machineDescription el nuevo valor para machineDescription
     * @see #machineDescription
     */
    public void setMachineDescription(String new_machineDescription){
	machineDescription=new_machineDescription;
    }
    
    
    /**
     * El conjunto de estados finales  de la máquina
     **/
    protected StateSet F;
    /**
       * Get the value of F.
       * @return Value of F.
       */
    public StateSet getF() {return F;}
    
    /**
       * Set the value of F.
       * @param v  Value to assign to F.
       */
    public void setF(StateSet  v) {this.F = v;}
    
    /**
     * El alfabeto de entrada de la máquina
     */
    protected Alphabet Sigma;
    
    /**
       * Get the value of Sigma.
       * @return Value of Sigma.
       */
    public Alphabet getSigma() {return Sigma;}
    
    /**
       * Set the value of Sigma.
       * @param v  Value to assign to Sigma.
       */
    public void setSigma(Alphabet  v) {this.Sigma = v;}
    
    /**
     * La funci'on de transici'on delta
     **/
    protected Delta delta;

    /**
     * Get the value of delta.
     * @return value of delta.
     */
    public Delta getDelta() {
	return delta;
    }
    
    /**
     * Set the value of delta.
     * @param v  Value to assign to delta.
     */
    public void setDelta(Delta  v) {
	delta = v;
    }
    
    /**
     * El conjunto de estados de la máquina
     */
    protected StateSet Q;
    
    /**
     * Get the value of Q.
     * @return value of Q.
     */
    public StateSet getQ() {
	return Q;
    }
    
    /** 
     * Set the value of Q.
     * @param v  Value to assign to Q.
     */
    public void setQ(StateSet  v) {
	Q = v;
    }            

    /**
     *Regresa el valor booleano  de la máquina, acepta o no acepta
     */
    abstract public boolean runMachine(Str str);

    /** 
     *  Guarda la representación de la Máquina en un archivo XML válido con respecto al DTD de cada una de las máquinas 
     * Escribe la máquina en formato XML 
     * @param FileName El nombre del archivo donde se guardará el DFA.
     */
    public void toFile(String FileName){
	toFile(FileName,"");	
    }
    
    /** 
     *  Guarda la representación de la Máquina en un archivo XML válido con respecto al DTD de cada una de las máquinas 
     * Escribe la máquina en formato XML 
     *
     * @param FileName El nombre del archivo donde se guardará el DFA.
     * @param comment Comentario que se escribirá al principio de la descripción de la Máquina.
     *
     */
    public void toFile(String FileName, String comment ){
	try{ 
	    toFile(new FileWriter(FileName),comment);	
	}catch(Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "El archivo " +  FileName + " no se pudo abrir: " ); 
	    ouch.printStackTrace(); 
	}
    }
    
    /**
     *  Guarda la representación de la Máquina en un archivo XML válido con respecto al DTD de cada una de las máquinas 
     * Escribe la máquina en formato XML 
     *
     * @param fw El FileWriter donde se guardará el DFA.
     * @param comment Comentario que se escribirá al principio de la descripción de la Máquina.
     */
    public void toFile(FileWriter fw, String comment){
	try{ 
	    toFile(fw);	
	} catch (Exception ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
			       + "Trying to toFile: " ); 
	    ouch.printStackTrace(); 
	}
    }
    
    /** 
     * Guarda la representación de la Máquina en un archivo XML válido con respecto al DTD de cada una de las máquinas 
     * Escribe la máquina en formato XML 
     *
     * @param fw El FileWriter donde se guardará la Máquina.
     */
    abstract public void toFile(FileWriter fw);    

    /**
     * Regresa la representación como cadena de cada Máquina,.
     * En particular esta implementación regresa la descripción de la Máquina si es que  existe
     */
    public String toString(){
	if(machineDescription.trim().length()==0)
	    return "";
	return "Description: " + machineDescription.trim() + "\n";	
    }
    
    /**
     * Solo inicializa el <code>DocumentBuilderFactory</code> para que cheque la entrada contra el DTD asociado a cada máquina
     */
    protected Machine(){
	factory = DocumentBuilderFactory.newInstance();
	factory.setValidating(true);
    }   

    /**
     * Hace la referencia de todos los estados con respecto a <code>Q</code>
     * Es útil cuando leemos por separado los estados y tenemos que hacer las referencias
     */
    abstract public void makeStateReferences();
}

/* Machine.java ends here */
