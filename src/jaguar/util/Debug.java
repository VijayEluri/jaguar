/**
** <Debug.java> -- To send debug messages 
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


/*
** <Debug.java> ---- Para Debugear
**
** Copyright (C) 2000 by Free Software Foundation, Inc. 
**
** Author:  <ivanx@localhost.localdomain>
** Commentary:
*/

package jaguar.util;

import java.util.Hashtable;
import java.util.Enumeration;

public class Debug{
    public static final int DEBUG_ON = 1;
    public static final int DEBUG_OFF = 2;    
    
    static public void println(String str, int debug_level){
	if(debug_level == DEBUG_ON)
	    System.err.println(str);
    }
    static public void println(String str){
	println(str,DEBUG_ON);
    }

    static public void print(String str, int debug_level){
	if(debug_level == DEBUG_ON)
	    System.err.print(str);
    }

    static public void print(String str){
	print(str,DEBUG_ON);
    }


    static public void println(String s, Hashtable d, int debug_level){
	if(debug_level == DEBUG_OFF)
	    return;
	
	System.err.println(s);
	for (Enumeration e = d.keys() ; e.hasMoreElements() ;) {
	    System.err.println(e.nextElement());
	}
	System.err.println("");
    }

    static public void println(String s, Hashtable d){
	println( s, d, DEBUG_ON);
    }    
}
/* Debug.java ends here */
