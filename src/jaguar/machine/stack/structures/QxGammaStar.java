/**
** <QxGammaStar.java> -- To represent part of the AFS's delta transition
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


package jaguar.machine.stack.structures;

import java.util.*;
import java.io.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.util.*;
import jaguar.util.*;
import jaguar.machine.stack.structures.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 * La clase
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
//public class QxGammaStar{
public class QxGammaStar{
    /**
     ** El estado <em>p</em> del par ordenado QxGamma^{*}
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
     ** La segunda entrada <em>cadena</em> del par ordenado QxGamma^{*}
     **/
    protected Str gammaStar;
    /**
     ** funcion de acceso para obtener el valor de gammaStar
     ** @return el valor actual de gammaStar
     ** @see #gammaStar
     **/
    public Str getGammaStar(){
        return gammaStar;
    }
    /**
     ** funcion de acceso para modificar gammaStar
     ** @param new_gammaStar el nuevo valor para gammaStar
     ** @see #gammaStar
     **/
    public void setGammastar(Str new_gammaStar){
        gammaStar = new_gammaStar;
    }
    protected QxGammaStar(){
        q = null;
        gammaStar = null;
    }

    public QxGammaStar(State _q, Str _gammaStar){
        q = _q;
        gammaStar = _gammaStar;
    }

    public QxGammaStar(org.w3c.dom.Node node)throws Exception{
        NodeList transitions = node.getChildNodes();
        Node qNode=null,strNode=null;
        int i=0;
        boolean itemFound = false;
        for (i = 0 ; !itemFound && i < transitions.getLength() ; i++) {
            if (transitions.item(i).getNodeType() == Node.ELEMENT_NODE) {
                qNode = transitions.item(i);
                itemFound = true;
            }
        }
        itemFound = false;
        for ( ; !itemFound && i < transitions.getLength() ; i++) {
            if(transitions.item(i).getNodeType() == Node.ELEMENT_NODE){
                strNode = transitions.item(i);
                itemFound = true;
            }
        }
        setQ(new State(qNode));
        setGammastar(new Str(strNode));
    }

    public String toString(){
        return "( " + ((q==null)?"null":q.toString()) + " , " + ((gammaStar == null)? "null": gammaStar.toString()) + " ) " ;
    }

    public void toFile(FileWriter fw){
        try{
            fw.write(BEG_TAG);
            q.toFile(fw);
            gammaStar.toFile(fw);
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
    public static final String ELEMENT_NAME = "QxGammStar";

    public static final String BEG_TAG = "<"+ELEMENT_NAME+">";

    public static final String END_TAG = "</"+ELEMENT_NAME+">";

    /**
     * Rutinas de prueba para la clase QxGammaStar.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase QxGammaStar. \n"
               +"Comentario: La clase\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
    }

}

/* QxGammaStar.java ends here. */


