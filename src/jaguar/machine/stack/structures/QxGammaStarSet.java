/**
** <QxGammaStarSet.java> -- To represent part of the AFS's delta transition
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
import jaguar.machine.structures.*;
import jaguar.structures.*;
import jaguar.structures.jstructures.*;
import java.io.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.exceptions.*;
import jaguar.machine.util.*;
import jaguar.util.*;
import jaguar.machine.stack.structures.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 *
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
//public class QxGammaStarSet extends HashSet{
public class QxGammaStarSet extends LinkedHashSet<QxGammaStar> {
    /**
     ** El nombre del elemento que debe de ser igual al especificado en su respectivo DTD
     ** El valor de esta variable debe de ser igual a org.w3c.dom.Node.getNodeName()
     **/
    static final public String ELEMENT_NAME = "QxGammaStarSet";
    /**
     * El tag con el que se define el fin del objeto en un archivo
     */
    public static final String END_TAG="</"+ELEMENT_NAME+">";
    /**
     * El tag con el que se define el inicio del objeto en un archivo
     */
    public static final String BEG_TAG="<"+ELEMENT_NAME+">";

    protected QxGammaStarSet() {
        super();
    }

    /**
     ** Construye una instancia de QxGammaStarSet dado el documento DOM
     **/
    public QxGammaStarSet(org.w3c.dom.Node domNode)throws Exception{
        this();
        NodeList domSymList = domNode.getChildNodes();
        for(int i = 0; i < domSymList.getLength() ; i++)
            if(domSymList.item(i).getNodeType() == Node.ELEMENT_NODE)
            add(new QxGammaStar(domSymList.item(i)));
    }

    public void toFile(FileWriter fw){
        Object [] elements = toArray();
        try{
            fw.write(BEG_TAG);
            for(int i = 0 ; i < elements.length; i++)
                ((QxGammaStar)elements[i]).toFile(fw);
            fw.write(END_TAG);
        }catch( IOException ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + "  Trying toFile: " );
            ouch.printStackTrace();
        }
    }
    /**
     ** Hace la referencia a los estados que estan en el conjunto sts
     ** aún no se si esto deba de ir aqui, pero parece que si :) por el momento aqui se queda
     ** por cierto lo que más probablemente no deba de ir aqui es la conversión de
     ** Str a JStr, de hecho debe de estar en la extensión JQxGammaStar
     */
    public QxGammaStarSet makeStateReference(StateSet sts){
        Object o[] = toArray();
        QxGammaStar qtgs, nqtgs;
        QxGammaStarSet nset = new QxGammaStarSet();
        State je;
        for(int i = 0; i < o.length; i++){
            qtgs = (QxGammaStar)o[i];
            je = sts.makeStateReference(qtgs.getQ());
            if(je == null)
          return null;
            nset.add(new QxGammaStar(je,new JStr(qtgs.getGammaStar())));
        }
        return nset;
    }

    /**
     * Rutinas de prueba para la clase QxGammaStarSet.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase QxGammaStarSet. \n"
               +"Comentario: HashSet\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
    }

}

/* QxGammaStarSet.java ends here. */


