/**
** <Gtipo2Lazy.java> -- The lazy type 2 grammars, this is used as input to the standardizer procedures
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

/**
 * Esta es la clase que representa las gramaticas tipo 2
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Gtipo2Lazy extends Gtipo2{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public Gtipo2Lazy ()throws Exception{
        super();
        validate();
    }
    public Gtipo2Lazy (Symbol _S, ProductionSet _P, Alphabet _T, Alphabet _N)throws Exception{
        super(_S, _P, _T, _N);
        validate();
    }

    /**
     * Constructora que construye una <code>Gtipo2Lazy</code> a partir del nombre de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Gtipo2Lazy(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     * Constructora que construye un <code>Gtipo2Lazy</code> a partir del <code>File</code> de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Gtipo2Lazy(File file)throws Exception{
        super(file);
        validate();
    }



    public boolean validate() throws ProductionNotValidTypeException{
        return ((ProductionT2Set)P).validateLazy(N,T,S);
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
     * Rutinas de prueba para la clase Gtipo2Lazy.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase Gtipo2Lazy. \n"
               +"Comentario: Esta es la clase que representa las gramaticas tipo 2\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
        try{
            String lazy = args[0];
            Gtipo2Lazy gt2l = new Gtipo2Lazy(lazy);
            gt2l.validate();
            System.out.println("\n\n\tgt2l. \n" + gt2l + "\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

/* Gtipo2Lazy.java ends here. */
