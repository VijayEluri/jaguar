/**
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


package jaguar.structures.excepciones;

 /** Esta excepción ocurre cuando dos conjuntos que esperamos esten contenidos no cumplen dicha propiedad
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */

public class StateSetNotASubSet extends Exception{

    private String msg;
    /**
    * El valor por omisión para msg
    */
    public static final String DEFAULT_MSG="";
    /**
    * Constructor.
    * Recibe los valores para msg.
    * Para el resto de los campos usa el valor por omision.
    * @param msg el valor con el que se inicalizar� el campo msg
    * @see #msg
    */
    public StateSetNotASubSet (String msg){
        this.msg=msg;
    }
    /**
    * Constructor.
    * Recibe los valores para .
    * Para el resto de los campos usa el valor por omision.
    * @see #DEFAULT_MSG
    */
    public StateSetNotASubSet (){
        this.msg=DEFAULT_MSG;
    }

}/* StateSetNotASubSet.java ends here. */
