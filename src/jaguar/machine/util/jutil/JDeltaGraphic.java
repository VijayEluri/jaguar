/**
** <JDeltaGraphic.java> -- The interface that every delta graphic must implement to be painted by the delta painter
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


package jaguar.machine.util.jutil;

import javax.swing.*;
import java.awt.*;
import jaguar.structures.State;
import jaguar.structures.Symbol;
import jaguar.structures.jstructures.JState;


/** 
 * Las deltas que podemos dibujar en un canvas
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public interface JDeltaGraphic{

    public State getCurrent_p();

    public Symbol getCurrent_sym();

    public State getCurrent_q();

    public String getLabelString(JState src, JState dest);

    public void setJdeltapainter(JDeltaPainter new_jdeltaPainter);

    public void paint(Graphics g);
}

/* JDeltaGraphic.java ends here. */
