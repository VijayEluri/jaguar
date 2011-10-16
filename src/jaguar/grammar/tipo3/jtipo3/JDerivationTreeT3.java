/**
** <JDerivationTreeT3.java> -- Shows a derivationt tree
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


package jaguar.grammar.tipo3.jtipo3;

import java.util.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jaguar.grammar.*;
import jaguar.grammar.structures.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.jgrammar.*;
/**
 * Jgen.java
 *
 *
 * Created: Wed Aug 29 22:39:49 2001
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */

public class JDerivationTreeT3 extends JFrame {
     /**
      * Esta es la secuencia de producciones como deben de ser aplicadas para obtener una cadena previamente definida
      */
    protected LinkedList prodSeq;
    /**
     * funcion de acceso para obtener el valor de prodSeq
     * @return el valor actual de prodSeq
     * @see #prodSeq
     */
    public LinkedList getProdseq(){
	return prodSeq;
    }
    /**
     * funcion de acceso para modificar prodSeq
     * @param new_prodSeq el nuevo valor para prodSeq
     * @see #prodSeq
     */
    public void setProdseq(LinkedList new_prodSeq){
	prodSeq=new_prodSeq;
    }


     /**
 * La cadena que generamos y cuyo árbol dibujaremos 
 */
 protected Str cadena;
 /**
 * funcion de acceso para obtener el valor de cadena
 * @return el valor actual de cadena
 * @see #cadena
 */
 public Str getCadena(){
 return cadena;
 }
 /**
 * funcion de acceso para modificar cadena
 * @param new_cadena el nuevo valor para cadena
 * @see #cadena
 */
 public void setCadena(Str new_cadena){
 cadena=new_cadena;
 }

    
    class Canvas extends JPanel {
	Dimension prefSize;
	public Canvas ()  {
	    int size = ((prodSeq.size()+2) * ARROW_WIDTH ) + INIT_XPOS ;
	    setPreferredSize(new Dimension(size,size));
	    setSize(getPreferredSize());
	    Canvas.this.setBackground(Color.white);
	}
	public void setPreferredSize(Dimension d)  {
	    prefSize = d;
	}

	public Dimension getPreferredSize()  {
	    return prefSize;
	}
	public static final int INIT_XPOS = 50;
	public static final int INIT_YPOS = 50;
	public static final int ARROW_WIDTH = 56;
	public static final int SPACE_BETWEN_ARROWS = 10;
	public static final int FONT_HEIGHT = 10;
	public static final String LEFT_ARROW_IMAGE_FILE = "./jaguar/grammar/images/leftArrow.gif";
	public static final String RIGHT_ARROW_IMAGE_FILE = "./jaguar/grammar/images/rightArrow.gif";
	public final Image leftarrow = Toolkit.getDefaultToolkit().getImage(LEFT_ARROW_IMAGE_FILE);
	public final Image rightarrow = Toolkit.getDefaultToolkit().getImage(RIGHT_ARROW_IMAGE_FILE);


	public void paintComponent(Graphics g)  {
	    super.paintComponent(g);
	    g.drawString(cadena.toString(),((int)INIT_XPOS/2), ((int)INIT_YPOS/2));
	    paintTree(g);
	}

	public void paintTree(Graphics g){
	    int totalProd = prodSeq.size();
	    Production currentP;
	    int xpos = INIT_XPOS, ypos = INIT_YPOS;
	    int xincrement =  ARROW_WIDTH + SPACE_BETWEN_ARROWS;
	    int yincrement = ARROW_WIDTH + FONT_HEIGHT;
	    for(int i = 0 ;  i < totalProd  ; i++){
		currentP = (Production)prodSeq.get(i);
		g.drawImage(leftarrow,xpos,ypos,this);
		if(currentP.getConsecuente().length() == 2)
		    g.drawImage(rightarrow,xpos + SPACE_BETWEN_ARROWS + ARROW_WIDTH ,ypos,this);
		// Esta pone la etiqueta de la raiz
		g.drawString(currentP.getAntecedente().toString(),
			     xpos + ARROW_WIDTH + ((int)(SPACE_BETWEN_ARROWS/2)),
			     ypos);
		// El subarbol izquierdo
 		try{
 		    g.drawString(currentP.getConsecuente().getSymbol(0).toString(),
				 xpos + ARROW_WIDTH - ARROW_WIDTH,
				 ypos + ARROW_WIDTH +FONT_HEIGHT);
 		}catch( StrIndexOutOfBoundsException ouch){
 		    System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName() 
 				       + " Imposible!! => "+ currentP.getConsecuente() ); 
 		    ouch.printStackTrace(); 
 		}
		xpos += xincrement;
		ypos += yincrement;
	    }
	}
    }
    public Canvas mycanvas;
    
    public JDerivationTreeT3() {
	this(new LinkedList(),new Str());
    }

    public JDerivationTreeT3(LinkedList _prodSeq, Str _cadena) {
	super("jgenerator");
	prodSeq = _prodSeq;
	cadena = _cadena;

	setSize(350, 350);
	addWindowListener(new WindowAdapter()  {

		public void windowClosing(WindowEvent e) {dispose();}
		public void windowOpened(WindowEvent e) {}
	    });
	setJMenuBar(createMenu());
	mycanvas = new Canvas();
	JScrollPane jScrollPaneCanvas;
	jScrollPaneCanvas = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	jScrollPaneCanvas.getHorizontalScrollBar().setUnitIncrement(20);
	jScrollPaneCanvas.getVerticalScrollBar().setUnitIncrement(20);
	getContentPane().add(jScrollPaneCanvas);
	jScrollPaneCanvas.setViewportView(mycanvas);

    }
    
    public static void main(String[] args)  {
	
	JDerivationTreeT3 f = new JDerivationTreeT3();
	f.show();
	
    }
    
    protected JMenuBar createMenu()  {
	JMenuBar mb = new JMenuBar();
	JMenu menu = new JMenu("File");
	menu.setMnemonic(KeyEvent.VK_F);
	JMenuItem quit = new JMenuItem(new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });
	quit.setMnemonic(KeyEvent.VK_Q);
	menu.add(quit);
	mb.add(menu);
	return mb;
    }
    
} // JDerivationTreeT3
