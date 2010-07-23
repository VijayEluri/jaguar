/**
** <DFA.java> -- The Deterministic Finite Automata
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


package jaguar.machine.dfa;

/**
 * DFA.java
 *
 *
 * Created: Wed Dec 20 21:33:00 2000
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hern'andez</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:03 $
 */

import java.util.*;
import jaguar.machine.Machine;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.dfa.structures.*;
import jaguar.machine.dfa.exceptions.DFANotFoundException;
import jaguar.machine.dfa.structures.exceptions.*;
import jaguar.util.Debug;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import org.w3c.dom.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.net.URL;
public class DFA extends Machine implements Cloneable{
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "dfa";
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
     * El estado inicial del automata
     */
    protected State q0;

    /**
       * Get the value of q0.
       * @return Value of q0.
       */
    public State getQ0() {return q0;}

    /**
       * Set the value of q0.
       * @param v  Value to assign to q0.
       */
    public void setQ0(State  v) {this.q0 = v;}

    public DFA(Alphabet _Sigma, StateSet _Q, StateSet _F, DfaDelta _delta,  State _q0){
        Sigma=_Sigma;
        Q=_Q;
        if((F = Q.makeSubSetReferences(_F)) == null){
            Debug.println("Q = " + getQ() + " no contiene a F = " + _F);
            System.exit(1);
        }
        delta=_delta;
        if((q0 = Q.makeStateReference(_q0)) == null){
            Debug.println("Q = " + getQ() + " no tiene a  q0 = " + _q0);
            System.exit(1);
        }
    }
    /**
     * Constructora sin parametros
     */
    public DFA(){
        super();
    }

    /**
     * Constructora que construye un DFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public DFA(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un DFA a partir del <code>File</code> de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public DFA(File file)throws Exception{
        this();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL schemaURL = this.getClass().getClassLoader().getResource("schema/dfa.xsd");
        File schemaFile = new File(schemaURL.toURI());
        Schema schema = schemaFactory.newSchema(schemaFile);
        factory.setSchema(schema);
        setupDFA(factory.newDocumentBuilder().parse(file),this);
    }

    /**
     * Constructora que construye un DFA a partir del nombre de un archivo que es valido segun el DTD de los DFAs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public DFA(org.w3c.dom.Document document)throws Exception{
        this();
        setupDFA(document,this);
    }

    /**
     * Configura todos los campos del DFA dado a partir del documento valido que le pasamos
     * @param document es un documento DOM que cumple con la especificación DTD para los DFAs
     * @param r el DFA que configuramos
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public void setupDFA(org.w3c.dom.Document document,DFA r){
        NodeList elementsList = document.getElementsByTagName(ELEMENT_NAME).item(0).getChildNodes();
        int howManyElements = 6;
        int j = (document.getElementsByTagName(DESCRIPTION_ELEMENT_NAME).getLength()==0)?1:0;
        Node dfaNode;
        for(int i =  0; j < howManyElements; i++){
            dfaNode = elementsList.item(i);
            if(dfaNode.getNodeType() == Node.ELEMENT_NODE){
            switch(j){
            case 0: r.setMachineDescription(dfaNode.getChildNodes().item(0).getNodeValue()); break;
            case 1: r.setQ(new StateSet(dfaNode)); break;// Q
            case 2: r.setSigma(new Alphabet(dfaNode)); break;//Sigma
            case 3: r.setDelta(new DfaDelta(dfaNode,r.getQ())); break;//delta
            case 4: r.setQ0(r.getQ().makeStateReference(new State(dfaNode)));break;//q0
            case 5: {
                r.setF(r.getQ().makeSubSetReferences(new StateSet(dfaNode)));
                r.getF().markAsFinal();
            };break;//F
            }
            j++;
            }
        }
    }

    /**
     ** Ejecuta el DFA sobre la cadena dada
     ** @param str la cadena sobre la que correra el DFA
     ** @return true si es aceptada por el DFA, e.o.c. false
     **/
    public boolean runMachine(Str str){
        State st;
        displayDeltaPSymb(q0,str);
        if ((st = doTransitions(q0,str)) != null && F.contains(st))
            return true;
        return false;
    }

    /** Lleva a cabo todas las transiciones posibles desde <code>currentSt</code> sobre  la cadena <code>cad</code>
     ** @param currentSt el estado desde donde comenzar'a a hacer las transiciones
     ** @return <code>currentSt</code> si la cadena es vacia, el últomo estado al que pueda llegar haciendo las transiciones a partir de <code>currentSt</code> , null si no puede llevar a cabo la transici'on desde currentSt con algún símbolo de <code>cad</code>
     **/
    protected State doTransitions(State currentSt, Str cad){
        if(cad.length() == 0)
            return currentSt;
        State nextSt;
        if ((nextSt = ((DfaDelta)delta).apply(currentSt,cad.getFirst())) != null) {
            displayTransResult(nextSt);
            if(cad.substring(1).length()==0)
            displayTransResult(nextSt);
            else displayDeltaPSymb(nextSt,cad.substring(1));
             return  doTransitions(nextSt,cad.substring(1));
        } else Debug.print("\n " + nextSt);
        return null;
    }

    protected State doTransition(State currentSt, Str cad){
        if(cad.length() == 0)
            return currentSt;
        return ((DfaDelta)delta).apply(currentSt,cad.getFirst());
    }

    /**
     * Desplieqga d(p,s) =
     */
    protected void displayDeltaPSymb(State p, Str s){
        System.out.print("d( "+p+" , "+ s +" ) = ");
    }
    /**
     * imprime q, bajo el contexto d(p,s) = q
     **/
    protected void displayTransResult(State q){
        System.out.println(q);
    }


    /**
     * Run the DFA on the given string
     * @param str the string over Sigma
     **/
    public void executeDfa(Str str){
        Debug.println("\nEl DFA " +
              (runMachine(str)?"SI":"NO") +
              " acepta " + str);
    }

    /**
     * Regresa la representación como cadena del DFA
     **/
    public String toString(){
        return "DFA " + super.toString() + "M = \n\tQ=" + getQ() + "\n\tSigma = "+ getSigma() + "\n\tdelta = "+ getDelta() + "\n\tq0 = "+getQ0()+"\n\tF= " + getF();
    }
    /**
     *  Guarda la representación de la Máquina en un archivo XML válido con respecto al DTD de cada una de las máquinas
     * Escribe la máquina en formato XML
     *
     * @param fw El FileWriter donde se guardará el DFA.
     */
    public void toFile(FileWriter fw){
        try{
            fw.write("<?xml version='1.0' encoding=\"iso-8859-1\" ?>"+"\n");
            fw.write(BEG_TAG);
            if (machineDescription.trim().length() > 0) {
                fw.write("\n\n <!-- Descripción --> \n");
                fw.write(DESCRIPTION_BEG_TAG);
                fw.write(getMachineDescription());
                fw.write(DESCRIPTION_END_TAG+"\n");
            }

            fw.write("\n\n <!-- Conjunto de States Q --> \n");
            getQ().toFile(fw);
            fw.write("\n\n <!-- Alphabet de entrada Sigma --> \n");
            getSigma().toFile(fw);
            fw.write("\n\n <!-- Función de Transición delta --> \n");
            getDelta().toFile(fw);
            fw.write("\n\n <!-- State inicial q0 --> \n");
            getQ0().toFile(fw);
            fw.write("\n\n <!-- Conjunto de estados finales F --> \n");
            getF().toFile(fw);
            fw.write("\n"+ END_TAG);
            fw.flush();
        }catch( Exception ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                       + "Trying to toFile: " );
            ouch.printStackTrace();
        }
    }

    /**
     * Crea y regresa una copia de este objeto
     * @return crea y regresa la copia de este objeto
     */
    public Object clone() throws CloneNotSupportedException{
        try{
            DFA nuevo = new DFA();
            nuevo.setSigma((Alphabet)Sigma.clone());
            nuevo.setQ((StateSet)Q.clone());
            nuevo.setF((StateSet)F.clone());
            nuevo.setDelta((DfaDelta)delta.clone());
            nuevo.setQ0(new State(q0));
            return nuevo;
        }
        catch (CloneNotSupportedException e){
            throw new InternalError(e.toString());
        }
    }


    /**
     ** Hace la referencia de todos los estados con respecto a <code>Q</code>
     ** Es útil cuando leemos por separado los estados y tenemos que hacer las referencias
     **/
    public void makeStateReferences(){
        F = Q.makeSubSetReferences(getF());
        q0 = Q.makeStateReference(getQ0());
    }

    public static void main(String []argv){
        try{
            DFA Paridad = new DFA(argv[0]);
            System.err.println(Paridad);
                Str str = new Str(argv[1],false);
            Paridad.executeDfa(str);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

/* DFA.java ends here */
