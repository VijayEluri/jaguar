/**
** 
** Copyright (C) 2002 by  Ivan Hern�ndez Serrano
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
** Author: Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
** 
**/


package jaguar.structures.exceptions;

/**
 * StateNotFoundException.java
 *
 *
 * Created: Sat Mar  3 18:59:58 2001
* @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
* @version 0.1
 */

public class StateNotFoundException extends Exception {
    
    public StateNotFoundException() {
	super();	
    }

    public StateNotFoundException(String msg) {
	super(msg);	
    }
    
} // StateNotFoundException
