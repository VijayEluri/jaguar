/**
** <Symbol.java> -- to use a symbol
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


/**
 * Symbol.java
 *
 *
 * Created: Wed Feb  7 00:36:45 2001
 *
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
 */
package jaguar.structures;

import java.io.FileWriter;
import java.io.File;
import jaguar.util.*;
import jaguar.structures.exceptions.*;
import org.w3c.dom.*;

public class Symbol{
    /**
     * La etiqueta del simbolo
     **/
    private String sym;
    /**
     * El tag con el que se define el inicio del objeto de un
     * en un archivo
     */
    public static final String BEG_TAG = "<sym>";
    /**
     * El tag con el que se define el fin de un simbolo
     * en un archivo
     */
    public static final String END_TAG = "</sym>";
    /**
     ** La etiqueta para los tags
     **/
    public static final boolean WITH_TAGS = true;
    /**
     ** La etiqueta para no tener tags
     **/
    public static final boolean WITHOUT_TAGS = false;
    /**
     * Get the value of sym.
     * @return value of sym.
     */
    public String getSym(){
        return sym;
    }

    /**
     * Set the value of sym.
     * @param _sym  Value to assign to sym.
     */
    public void setSym(String _sym) {
        sym = _sym;
    }

    /**
     * La representación de como cadena de este símbolo, el
     * comportamiento de esta funcion depende de la constructora que
     * se haya usado
     * @see #Symbol(String, boolean)
     */
    public String toString(){
        if(showTags==WITH_TAGS)
            return BEG_TAG + sym + END_TAG;
        return sym;
    }

    /**
     * Escribe la representación del Symbol en un archivo con el formato definido por el DTD correspondiente
     * Escribe el Symbol con su representación correspondiente con tags.
     *
     * @param fw El FileWriter donde se guardará el Symbol.
     */
    public void toFile(FileWriter fw){
        try{
            fw.write(BEG_TAG + sym + END_TAG);
        }catch( Exception ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + "Trying to toFile: " );
            ouch.printStackTrace();
        }
    }
    /**
     * La representación de como cadena de este símbolo, el
     * comportamiento de esta funcion depende de la constructora que
     * se haya usado
     * @see #Symbol(String,boolean)
     */
    public String toStringNoTags(){
        return sym;
    }


    /**
     * Construye un símbolo con la cadena _sym  <code>_sym</code>
     * Por omisión mostrara los tags con toString, es equivalente a
     * usar <code>Symbol(_sym,false)</code>
     * @param _sym la etiqueta que tendrá el <code>Symbol</code>
     */
    public Symbol (String _sym){
        this(_sym,WITHOUT_TAGS);
    }

    /**
     * Construye un símbolo con la cadena _sym  <code>_sym</code>
     * y define si se deberán de mostrar los tags en el toString
     * @param _sym la etiqueta que tendrá el <code>Symbol</code>
     * @param tags <code>true</code> - mostrará los tags,
     * <code>false</code> no mostrará los tags
     */
    public Symbol (String _sym, boolean tags){
        sym =_sym;
        showTags = tags;
    }
    /**
     * Construye un símbolo igual al definido por el <code>Symbol _sym</code>
     * Por omisión mostrara los tags con toString, es equivalente a
     * usar <code>Symbol(_sym,false)</code>
     * @param _sym  el <code>Symbol</code> a partir del cual se creará
     * esta instacia
     */
    public Symbol (Symbol _sym){
        this( _sym.getSym(), _sym.getShowTags());
    }
    /**
     * Construye un símbolo igual al definido   por el <code>Symbol _sym</code>
     * y define si se deberán de mostrar los tags en el toString
     * @param _sym  el <code>Symbol</code> a partir del cual se crea
     * esta instancia
     * @param tags <code>true</code> - mostrará los tags,
     * <code>false</code> no mostrará los tags
     */
    public Symbol (Symbol _sym, boolean tags){
        this(_sym);
        showTags = tags;
    }

    /**
     * Bandera para mostrar el simbolo con tags
     */
    private boolean showTags=WITH_TAGS;

    /**
       * Get the value of showTags.
       * @return Value of showTags.
       */
    public boolean getShowTags() {return showTags;}

    /**
       * Set the value of showTags.
       * @param v  Value to assign to showTags.
       */
    public void setShowTags(boolean  v) {this.showTags = v;}

    /**
     ** Construye un símbolo dado el documento DOM
     **/
    public Symbol(org.w3c.dom.Node domNode){
        this(domNode.getChildNodes().item(0).getNodeValue());
    }

    /**
     * Obtiene el hash code de este <code>Symbol</code>
     * @return el hash code de este símbolo
     */
    public int hashCode(){
        return getSym().hashCode();
    }

    public boolean equals(Object o){
        if(o instanceof Symbol && getSym().equals(((Symbol)o).getSym())){
            return true;
        }
        return false;
    }

}// Symbol
