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


package jaguar.grammar.tipo3.structures.exception;

 /**
  * representa la situacion en la que  
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class NotAProductionT3Exception extends Exception{
    /**
     * Constructor sin parámetros.
     * La excepcion presenta el mensaje por omisión, el cual es
     */ 
    public NotAProductionT3Exception(){
	super();
    }
    /**
     * Constructor.
     * Usa el mensaje especificado como parametro del constructor de la superclase.
     *
     * @param mensaje que contiene esta excepción.
     */
    public NotAProductionT3Exception(String mensaje){
	super(mensaje);
    }
}
/* NotAProductionT3Exception.java ends here. */
