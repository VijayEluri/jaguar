/**
* <JStateSet.java> -- The StateSet's graphical extension
* 
* Copyright (C) 2002 by  Ivan Hernández Serrano
*
* This file is part of JAGUAR
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
* 
* Author: Ivan Hernández Serrano <ivanx@users.sourceforge.net>
* 
* @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:05 $
**/


package jaguar.structures.jstructures;

import java.io.File;
import jaguar.structures.*;
import jaguar.util.*;
import jaguar.structures.exceptions.StateSetNotFoundException;

public class JStateSet extends StateSet{

    /**
     * Crea un conjunto vacio de estados
     */
    public JStateSet(){
	super();
    }

    
    public JStateSet(StateSet sts){
	this();
	Object o[] = sts.toArray();
	for(int i = 0 ; i < o.length; i++)
	    add(new JState((State)o[i]));	    
    }
    /**
     * Agrega un estado al conjunto de estados
     */
    public boolean add(JState e){
	return super.add(e);
    }
    /**
     * Agregar un objeto al conjutno. NO USAR. generará el siguiente mensaje
     * "dfa.structures.JStateSet.add(Object):Warning: Solo se pueden agregar States a este conjunto"
     */
    public boolean add(Object o){
	System.err.println("dfa.structures.JStateSetWarning: Solo se pueden agregar States a este conjunto");
	return false;
    }

    /**
     * Remueve el estado <code>e</code> del conjunto si esta presente
     */
    public boolean remove(State e){
	return super.remove(e);
    }
    /** Removes the given element from this set if it is present. NO USAR. generará el siguiente mensaje
     * "dfa.structures.JStateSet.Remove(Object):Warning: Solo se pueden remover States d este conjunto"
     */
    public boolean remove(Object o) {
	return super.remove(o);
  }
}
    

/* StatesSet.java ends here */
