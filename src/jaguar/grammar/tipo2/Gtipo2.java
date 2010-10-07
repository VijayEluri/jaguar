/**
** <Gtipo2.java> -- A type 2 grammar with all its restrictions
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


package jaguar.grammar.tipo2;

import jaguar.grammar.*;
import jaguar.structures.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.grammar.exceptions.*;
import jaguar.grammar.tipo2.structures.*;
import jaguar.grammar.tipo2.structures.exceptions.*;
import jaguar.structures.exceptions.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

/**
 * Esta es la clase que representa las gramaticas tipo 2
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Gtipo2 extends Grammar{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public Gtipo2 ()throws Exception{
        super();
        P = new ProductionT2Set();
    }
    public Gtipo2 (Symbol _S, ProductionSet _P, Alphabet _T, Alphabet _N)throws Exception{
        super(_S, _P, _T, _N);
    }


    /**
     * Constructora que construye una <code>Gtipo2</code> a partir del nombre de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Gtipo2(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un <code>Gtipo2</code> a partir del <code>File</code> de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Gtipo2(File file)throws Exception{
        this();
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        setupGrammar(factory.newDocumentBuilder().parse(file),this);
    }

    public boolean validate() throws ProductionNotValidTypeException{
        return P.validate(N,T,S);
    }

    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
        return  "G \n\n\tN = " + getN() + "\n\tT = "+ getT() + "\n\tS = " + getS() + "\n\tP = " + getP() + "\n";
    }

    /**
     * Rutinas de prueba para la clase Gtipo2.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase Gtipo2. \n"
               +"Comentario: Esta es la clase que representa las gramaticas tipo 2\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
        try{
            Gtipo2 gt2 = new Gtipo2(args[0]);
            System.out.println("\n\n\tgt2. \n" + gt2 + "\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

/* Gtipo2.java ends here. */
