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


package jaguar.machine.exceptions;

/**
 * MachineNotFoundException.java
 *
 *
 * Created: Sat Apr 14 16:58:03 2001
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:04 $
 */

public class MachineNotFoundException extends Exception {
    
    public MachineNotFoundException() {
	super();	
    }

    public MachineNotFoundException(String s){
	super(s);
    }
} // MachineNotFoundException
