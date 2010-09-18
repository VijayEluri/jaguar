/**
** <JStr.java> -- The Str's graphical extension
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


package jaguar.structures.jstructures;

/**
 * JStr.java
 *
 *
 * Created: Mon Feb 26 20:17:52 2001
 *
 * @author
 * @version
 */

import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import java.awt.geom.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class JStr extends Str {

    public JStr() {
        super();
    }

    public JStr (boolean tags){
        super(tags);
    }

    public JStr (Symbol s){
        super(s);
    }

    public JStr (Symbol s, boolean tags){
        super(s,tags);
    }


    public JStr (Str aStr){
        super(aStr,new Alphabet());
    }

    public JStr (JStr aStr, Alphabet _Sigma){
        super(aStr,_Sigma);
    }

    public JStr (JStr aStr, Alphabet _Sigma, boolean tags){
        super(aStr,_Sigma,tags);
    }

    /**
     * Constructora que construye un JStr a partir del nombre de un archivo que es valido segun el DTD de los JStrs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JStr(String filename)throws Exception{
        this(new File(filename),false);
    }

    /**
     * Constructora que construye un JStr a partir del nombre de un archivo que es valido segun el DTD de los JStrs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JStr(String filename, boolean tags)throws Exception{
        this(new File(filename),tags);
    }

    /**
     * Constructora que construye un JStr a partir del nombre de un archivo que es valido segun el DTD de los JStrs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JStr(File file)throws Exception{
        this(file,false);
    }

    /**
     * Constructora que construye un JStr a partir del nombre de un archivo que es valido segun el DTD de los JStrs
     * @see <a href="http://ijaguar.sourceforge.net/DTD/dfa.dtd">dfa.dtd</a>
     */
    public JStr(File file, boolean tags)throws Exception{
        super(file,tags);
    }

    public JStr(Symbol nuevoStr[], Alphabet _Sigma){
        super(nuevoStr,_Sigma);
    }

    public JStr(Symbol nuevoStr[], Alphabet _Sigma, boolean tags){
        super(nuevoStr,_Sigma,tags);
    }

    public JStr(Vector<Symbol> vSymbols, Alphabet _Sigma){
        super(vSymbols,_Sigma);
    }

    public JStr(Vector<Symbol> vSymbols, Alphabet _Sigma, boolean tags){
        super(vSymbols,_Sigma,tags);
    }

    public void paint(Graphics g){
        g.setColor(Color.blue);
        g.fill3DRect(10,10,10,10,true);
    }


} // JStr
