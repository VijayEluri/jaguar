/**
** <Turing.java> -- The Turing Machine's engine
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


package jaguar.machine.turing;

import java.util.*;
import jaguar.machine.Machine;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.machine.turing.structures.*;
import jaguar.machine.turing.structures.exceptions.*;
import jaguar.machine.turing.exceptions.*;
import jaguar.util.Debug;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 * La máquina de Turing
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
 */
public class Turing extends Machine implements Cloneable{
    /**
     * El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     * El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     */
    static final public String ELEMENT_NAME = "turing";


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
     * El estado inicial de la MT
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

     /**
      * El conjunto finito de símbolos de cinta permitidos
      */
    protected Alphabet Gamma;
    /**
     * funcion de acceso para obtener el valor de Gamma
     * @return el valor actual de Gamma
     * @see #Gamma
     */
    public Alphabet getGamma(){
        return Gamma;
    }
    /**
     * funcion de acceso para modificar Gamma
     * @param new_Gamma el nuevo valor para Gamma
     * @see #Gamma
     */
    public void setGamma(Alphabet new_Gamma){
        Gamma=new_Gamma;
    }

     /**
      * Un simbolo de <code>Gamma</code>, es el blanco de la cinta
      * @see #Gamma
      */
    protected Symbol blanco;
    /**
     * funcion de acceso para obtener el valor de blanco
     * @return el valor actual de blanco
     * @see #blanco
      * @see #Gamma
     */
    public Symbol getBlanco(){
        return blanco;
    }
    /**
     * funcion de acceso para modificar blanco
     * @param new_blanco el nuevo valor para blanco
     * @see #Gamma
     * @see #blanco
     */
    public void setBlanco(Symbol new_blanco){
        blanco=new_blanco;
    }

    /**
     * El símbolo con el que se hizo la más reciente transición
     */
    protected Symbol lastTransitionSymbol;
    /**
     * funcion de acceso para obtener el valor de lastTransitionSymbol
     * @return el valor actual de lastTransitionSymbol
     * @see #lastTransitionSymbol
     */
    public Symbol getLastTransitionSymbol(){
        return lastTransitionSymbol;
    }
    /**
     * funcion de acceso para modificar lastTransitionSymbol
     * @param new_lastTransitionSymbol el nuevo valor para lastTransitionSymbol
     * @see #lastTransitionSymbol
     */
    public void setLastTransitionSymbol(Symbol new_lastTransitionSymbol){
        lastTransitionSymbol=new_lastTransitionSymbol;
    }


    /**
     ** El nombre del elemento izquierdo
     */
    public static final String LEFT_ELEMENT_NAME =  "left" ;
    /**
     ** la dirección del movimiento a la izquierda
     */
    public static final int LEFT =  -1 ;
    /**
     ** El tag asociado a la dirección  izquierda
     */
    public static final String LEFT_TAG = "<"+LEFT_ELEMENT_NAME+"/>";
    /**
     ** El nombre del elemento derecho
     */
    public static final String RIGHT_ELEMENT_NAME =  "right" ;
    /**
     ** la dirección del movimiento a la derecha
     */
    public static final int RIGHT =  1 ;
    /**
     ** El tag asociado a la dirección derecha
     */
    public static final String RIGHT_TAG = "<"+RIGHT_ELEMENT_NAME+"/>";

    /**
     ** la dirección del movimiento a la derecha
     */
    public static final int INVALID_DIRECTION =  0 ;

    /**
     * Regresa el TAG asociado a la dirección
     *@see #LEFT
     *@see #RIGHT
     **/
    static public String getStringDirection(int _direction){
        if(_direction == LEFT)
            return LEFT_TAG;
        if(_direction == RIGHT)
            return RIGHT_TAG;
        return "INVALID TAG!!!";
    }


    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public Turing (){
        super();
        tape = new LinkedList<Symbol>();
    }

    public Turing(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, TuringDelta _delta,  State _q0, Symbol _blanco, StateSet _F){
        Q=_Q;
        Sigma=_Sigma;
        Gamma = _Gamma;
        if(!Gamma.contains(Sigma) || Sigma.contains(Gamma)){
            Debug.println("Gamma = " + Gamma + " no contiene propiamente al alfabeto Sigma = " + Sigma);
            System.exit(1);
        }
        delta=_delta;

        if((q0 = Q.makeStateReference(_q0)) == null){
            Debug.println("Q = " + getQ() + " no tiene a  q0 = " + _q0);
            System.exit(1);
        }
        if(! Gamma.contains(_blanco)){
            Debug.println("El blanco \""+ _blanco + "\" no es parte del alfabeto Gamma = " + Gamma);
            System.exit(1);
        }
        blanco = _blanco;
        if((F = Q.makeSubSetReferences(_F)) == null){
            Debug.println("Q = " + getQ() + " no contiene a F = " + _F);
            System.exit(1);
        }
    }

     /**
      * La cinta "<em>infinita</em>" de la TM
      */
    protected LinkedList<Symbol> tape;

    public LinkedList<Symbol> getTape(){
        return tape;
    }

    /**
     * Constructora que construye un TM a partir del nombre de un archivo que es valido segun el DTD de los TMs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public Turing(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un TM a partir del nombre de un archivo que es valido segun el DTD de los TMs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public Turing(File file)throws Exception{
        this();
        setupTuring(factory.newDocumentBuilder().parse(file),this);
    }

    /**
     * Constructora que construye un TM a partir del nombre de un archivo que es valido segun el DTD de los TMs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public Turing(org.w3c.dom.Document document)throws Exception{
        this();
        setupTuring(document,this);
    }

    /**
     * Configura todos los campos del TM dado a partir del documento valido que le pasamos
     * @param document es un documento DOM que cumple con la especificación DTD para los TMs
     * @param r el TM que configuramos
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public void setupTuring(org.w3c.dom.Document document,Turing r)throws Exception{
        NodeList elementsList = document.getElementsByTagName(ELEMENT_NAME).item(0).getChildNodes();
        int howManyElements = 8;
        int j = (document.getElementsByTagName(DESCRIPTION_ELEMENT_NAME).getLength()==0)?1:0;
        Node tmNode;
        for(int i =  0; j < howManyElements; i++){
            tmNode = elementsList.item(i);
            if(tmNode.getNodeType() == Node.ELEMENT_NODE){
                switch(j){
                case 0: r.setMachineDescription(tmNode.getChildNodes().item(0).getNodeValue()); break;
                case 1: r.setQ(new StateSet(tmNode)); break;// Q
                case 2: r.setSigma(new Alphabet(tmNode)); break;//Sigma
                case 3: r.setGamma(new Alphabet(tmNode)); break;//Gamma
                case 4: r.setDelta(new TuringDelta(tmNode)); break;//delta
                case 5: r.setQ0(new State(tmNode)); break;//q0
                case 6: r.setBlanco(new Symbol(tmNode)); break;//q0
                case 7: {
                    r.setF(new StateSet(tmNode));
                    r.setF(r.getQ().makeSubSetReferences(r.getF()));
                    r.getF().markAsFinal();
                    }
                    break;//F
                }
                j++;
            }
        }
    }



    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
    return "TM " + super.toString() + "M = \n\tQ=" + getQ() + "\n\tSigma = "+ getSigma() +
        "\n\tGamma = "+ getGamma() + "\n\tdelta = "+ getDelta() + "\n\tq0 = "+getQ0()+
        "\n\tblanco = "+ getBlanco() +"\n\tF= " + getF();
    }
    public void toFile(FileWriter fw){
        try{
            fw.write("<?xml version='1.0' encoding=\"iso-8859-1\" ?>"+"\n");
            fw.write("<!DOCTYPE turing SYSTEM \"turing.dtd\">"+"\n");
            if(machineDescription.trim().length() > 0){
                fw.write(DESCRIPTION_BEG_TAG);
                fw.write(getMachineDescription());
                fw.write(DESCRIPTION_END_TAG+"\n");
            }
            fw.write(BEG_TAG);
            fw.write("\n\n <!-- Conjunto de States Q --> \n ");
            getQ().toFile(fw);
            fw.write("\n\n <!-- Alphabet de entrada Sigma --> \n ");
            getSigma().toFile(fw);
            fw.write("\n\n <!-- Alphabet de entrada Gamma --> \n ");
            getGamma().toFile(fw);
            fw.write("\n\n <!--  Función de Transición delta --> \n ");
            getDelta().toFile(fw);
            fw.write("\n\n <!-- State inicial q0 --> \n ");
            getQ0().toFile(fw);
            fw.write("\n\n <!-- El blanco --> \n ");
            getBlanco().toFile(fw);
            fw.write("\n\n <!-- Conjunto de estados finales F -->\n ");
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
      * Nos dice si la máquina ha detenido su funcionamiento o si puede seguir ejecutando transiciones
      */
    protected boolean halt;
    /**
     * funcion de acceso para obtener el valor de halt
     * @return el valor actual de halt
     * @see #halt
     */
    public boolean getHalt(){
        return halt;
    }
    /**
     * funcion de acceso para modificar halt
     * @param new_halt el nuevo valor para halt
     * @see #halt
     */
    public void setHalt(boolean new_halt){
        halt=new_halt;
    }
    /**
     * La posición del estado en la cinta de la TM
     **/
    protected int statePos;

    /**
     * El estado actual
     **/
    protected State currentState;

    /**
     * Get the value of currentState.
     * @return value of currentState.
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Set the value of currentState.
     * @param v  Value to assign to currentState.
     */
    public void setCurrentState(State  v) {
        this.currentState = v;
    }


    public void initializeMachine(Str str){
        halt = false;
        currentState = getQ0();
        statePos = 0;
        tape = new LinkedList<Symbol>();
        tape.add(getQ0().toSymbol());
        tape.addAll(str.getSeq());
        tape.add(getBlanco());
    }

    public boolean runMachine(Str str){
        if(! str.isOnAlphabet(getSigma())) {
            return false;
        }
        initializeMachine(str);

        Debug.println("   " + tape);
        while(!halt){
            doTransition();
        }
        return F.contains(currentState);
    }

    public void doTransition(){
        Symbol viendoSym;
        QxGammaxDirection qgdRes;
        if((statePos+1) == tape.size()){
            // En este caso estamos al final de la cinta, lo que equivale a estar viendo un blanco
            tape.add(getBlanco());
        }
        viendoSym = (Symbol) tape.get(statePos+1);
        qgdRes = ((TuringDelta)getDelta()).apply(currentState,viendoSym);
        setLastTransitionSymbol(viendoSym);

        if(qgdRes == null)
            halt = true;
        else {
            currentState = qgdRes.getQ();
            if(RIGHT == qgdRes.getDirection()) {
                tape.set(statePos,qgdRes.getAGamma());
                tape.set(statePos+1,currentState.toSymbol());
                statePos += RIGHT;
            } else if(LEFT == qgdRes.getDirection()) {
                if(statePos == 0) {
                    halt = true;
                } else {
                    tape.set(statePos+1,qgdRes.getAGamma());
                    tape.set(statePos,tape.get(statePos-1));
                    tape.set(statePos-1,currentState.toSymbol());
                    statePos += LEFT;
                }
            }
        }
        displayMessage("|- " + tape +"\n");
    }

    /**
     ** Hace la referencia de todos los estados con respecto a <code>Q</code>
     ** Es útil cuando leemos por separado los estados y tenemos que hacer las referencias
     **/
    public void makeStateReferences(){
        F = Q.makeSubSetReferences(getF());
        q0 = Q.makeStateReference(getQ0());
    }

    /**
     * Run the TM on the given string
     * @param str the string over Sigma
     **/
    public void executeTM(Str str){
        Debug.println("\nLa TM " +
              (runMachine(str)?"SI":"NO") +
              " acepta " + str);
    }

    /**
     * Rutinas de prueba para la clase Turing.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        try{
            Turing tm = new Turing(args[0]);
            System.err.println(tm);
            Str str = new Str(args[1],false);
            tm.executeTM(str);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void displayMessage(String message){
        Debug.print(message);
    }
}

/* Turing.java ends here. */
