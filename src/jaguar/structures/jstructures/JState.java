/**
** <JState.java> -- The State's graphical extension
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

import java.awt.*;
import java.awt.geom.*;
import jaguar.util.Debug;
import java.util.Vector;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import java.io.File;
import java.lang.Math;

/**
 * JState.java
 *
 *
 * Created: Sat Feb 10 15:20:26 2001
 * @author <a href="mailto:ivanx@users.sourceforge.net">Ivan Hernandez Serrano</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/01/31 19:25:05 $
 */

public class JState extends jaguar.structures.State{

    /**
     ** El diametro del circulo circunscrito que identificará gráficamente a un estado inicial
     **/
    public static final double DIAMETRO_INITIAL_STATE = 15;

    /**
     * El diametro de un JState, son graficos 50 pixels
     **/
    public static final double DIAMETRO = 50;
    /**
     *El radio de un JState
     */
    public static final double RADIO  = Math.sqrt(50);
    /**
     *  Elipse 2D que usamos para el chequeo de puntos dentro de un JState, la usamos para el drag & drop, 
     *  para los tooltips, y claro, para dibujar los estados
     **/
    private Ellipse2D e2d;

    /**
     * El circulo cinscuncrito con el que identificaremos a los estados iniciales
     */
    protected Ellipse2D circuloCinscunscritoEstadoInicial;

    /**
     * funcion de acceso para obtener el valor de circuloCinscunscritoEstadoInicial
     * @return el valor actual de circuloCinscunscritoEstadoInicial
     * @see #circuloCinscunscritoEstadoInicial
     */
    protected Ellipse2D getCirculoCinscunscritoEstadoInicial(){
	return circuloCinscunscritoEstadoInicial;
    }
    /**
     * funcion de acceso para modificar circuloCinscunscritoEstadoInicial
     * @param new_circuloCinscunscritoEstadoInicial el nuevo valor para circuloCinscunscritoEstadoInicial
     * @see #circuloCinscunscritoEstadoInicial
     */
    protected void setCirculoCinscunscritoEstadoInicial(Ellipse2D new_circuloCinscunscritoEstadoInicial){
	circuloCinscunscritoEstadoInicial=new_circuloCinscunscritoEstadoInicial;
    }

    /**
     * El punto dode se localiza en el componente gráfico
     **/
    protected Point location;
    
    /**
     * Get the value of location.
     * @return value of location.
     */
    public Point getLocation() {
	return location;
    }
    
    /**
     * Set the value of location.
     * @param p Value to assign to location.
     */
    public void setLocation(Point  p) {
	location = p;
	updateEllipse2D();
    }


    /**
     * Set the value of location.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setLocation(double x, double y) {
	location.setLocation(x,y);
	updateEllipse2D();
    }


    protected static double  ZOOM_FACTOR;
    
    /**
       * Get the value of ZOOM_FACTOR.
       * @return Value of ZOOM_FACTOR.
       */
    public double getZOOM_FACTOR() {return ZOOM_FACTOR;}
    
    /**
       * Set the value of ZOOM_FACTOR.
       * @param v  Value to assign to ZOOM_FACTOR.
       */
    public void setZOOM_FACTOR(double  v) {this.ZOOM_FACTOR = v;}
    
    private void updateEllipse2D(){
	e2d = new Ellipse2D.Double(getX(),getY(),DIAMETRO,DIAMETRO);
	circuloCinscunscritoEstadoInicial = new Ellipse2D.Double(getX()+5+DIAMETRO/4,getY()+5+DIAMETRO/4,
								 DIAMETRO_INITIAL_STATE,DIAMETRO_INITIAL_STATE);
    }

    protected Ellipse2D getEllipse(){
	return e2d;	
    }
    
    /**
     * Get the value of x in the <code>location</code> point
     * @return value of x.
     */
    public double getX() {
	return(double) ((Point2D)location).getX();
    }
    
    /**
     * Set the value of x in the <code>location</code> point
     * @param x  Value to assign to the x coordinate
     */
    public void setX(double  x) {
	location.setLocation(x,getY());
    }

    /**
     * Get the value of y in the <code>location</code> point
     * @return value of y.
     */
    public double getY() {
	return(double) ((Point2D)location).getY();
    }
    
    /**
     * Set the value of y in the <code>location</code> point
     * @param y  Value to assign to the y coordinate
     */
    public void setY(double  y) {
	location.setLocation(getX(),y);
    }        

    /**
     * El color por default es orange
     **/
    public static final Color DEFAULT_BG_COLOR = new Color(102, 153,255);// Color.orange;


    /**
     * El color por default es orange
     **/
    public static final Color DEFAULT_FINAL_BG_COLOR = new Color(255, 102,0);// Color.orange;
    
    /**
     * El color por default es orange
     **/
    public static final Color DEFAULT_FG_COLOR = Color.black;

    /**
     * La constante booleana para pintar un estado current
     **/
    public static final boolean DEFAULT_CURRENT_STATE = true;
    /**
     * El color para pintar un estado current
     **/
    public static final Color DEFAULT_BG_CURRENT_STATE_COLOR = new Color(0,102,204);

    /**
     * El color para pintar el centro de un  estado inicial
     **/
    public static final Color DEFAULT_POINT_INITIAL_STATE = new Color(252,235,5);
    
    /**
     * El color por default es orange
     **/
    private Color c = DEFAULT_BG_COLOR;
    
    /**
     * Get the value of c, the color of this state 
     * @return value of c.
     */
    public Color getC() {
	return c;
    }
    
    /**
     * Set the value of c.
     * @param _c  Value to assign to c.
     */
    public void setC(Color  _c) {
	c = _c;
    }

    private JState(){
	this("",false);
    }

    public JState(State st){
	this(st.getLabel(), st.getIsInF());	
    }

    
    /**
     * Constructora que recibe la etiqueta del estado, con esta
     * constructora el punto del estado  es el (0,0) y no es estado final, es equivalente a usar <code> JState(label,false)</code>
     *@param label La etiquta del estado
     **/
    public JState(String label){
	this(label,false);
    }

    /**
     * Constructora que recibe la etiqueta del estado, con esta
     * constructora el punto del estado  es el (0,0)
     *@param label La etiquta del estado
     *@param _isInF verdadero si es un estado final, falso si no lo es
     **/
    public JState(String label, boolean _isInF){
	this(label,0,0, _isInF);
    }

    /**
     * Construye un estado gráfico con su posición, por omision no es estado final es equivalente a usar <code>JState(label,x,y,false)</code>
     *@param label La etiquta del estado
     *@param x posicion sobre el eje X
     *@param y posicion sobre el eje Y
     **/
    public JState(String label, double x, double y){
	this(label,x,y,false);
    }

    /**
     * Construye un estado gráfico con su posición y hace explicito si es un estado final o no
     *@param label La etiquta del estado
     *@param x posicion sobre el eje X
     *@param y posicion sobre el eje Y
     *@param _isInF verdadero si es un estado final, falso si no lo es
     **/
    public JState(String label, double x, double y, boolean _isInF){
	super(label,_isInF);
	location = new Point();
	setLocation(x,y);
	e2d = new Ellipse2D.Double(getX(),getY(),DIAMETRO,DIAMETRO);
	circuloCinscunscritoEstadoInicial= new Ellipse2D.Double(getX()+5+DIAMETRO/4,getY()+5+DIAMETRO/4,
								DIAMETRO_INITIAL_STATE,DIAMETRO_INITIAL_STATE);
    }
    

    public String toString() {
	return super.toString();
    }

    /**
     * Este nos dice si es un estado inicial para identificarlo gráficamente 
     */
    protected boolean esEstadoInicial;
    /**
     * El valor por omisión para esEstadoInicial
     */
    public static final boolean DEFAULT_ESESTADOINICIAL=false;
    /**
     * funcion de acceso para obtener el valor de esEstadoInicial
     * @return el valor actual de esEstadoInicial
     * @see #esEstadoInicial
     */
    public boolean getEsEstadoInicial(){
	return esEstadoInicial;
    }
    /**
     * funcion de acceso para modificar esEstadoInicial
     * @param new_esEstadoInicial el nuevo valor para esEstadoInicial
     * @see #esEstadoInicial
     */
    public void setEsEstadoInicial(boolean new_esEstadoInicial){
	esEstadoInicial=new_esEstadoInicial;
    }

    /**
     * Pinta un estado en un contexto gráfico dependiendo del parametro lo pinta
     * en tercera dimension levantado o presionado
     * @param g El contexto gráfico sobre el que se pintará el estado
     * @param esEstadoActual si es verdadero lo hace levantado, si es falso lo dibuja aplastado
     */
    public void paint(Graphics g, boolean esEstadoActual){
	Graphics2D g2d = (Graphics2D) g;
	Stroke origS = g2d.getStroke();
	Color c;	
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	g.setColor(DEFAULT_BG_COLOR);
	if(esEstadoActual==DEFAULT_CURRENT_STATE)// Si no lo quieren raise entonces lo quieren m'as obscuro
	    g.setColor(DEFAULT_BG_CURRENT_STATE_COLOR);
	g2d.fill(getEllipse());	
	if(getIsInF()){
	    g.setColor(DEFAULT_FINAL_BG_COLOR);
	    g2d.setStroke(new BasicStroke(3.0f));
	    g2d.draw(getEllipse());	    
	}
	if(getEsEstadoInicial()){
	    g.setColor(DEFAULT_POINT_INITIAL_STATE);
	    g2d.fill(getCirculoCinscunscritoEstadoInicial());
	}
	g.setColor(DEFAULT_FG_COLOR);
	g2d.drawString(getLabel(),(int)(getX()+3),(int)(getY()+(DIAMETRO/2)));
	g2d.setStroke(origS);
    }

    /**
     * Pinta un estado en un contexto gráfico con el background del
     * color definido en <code>DEFAULT_BG_COLOR</code> 
     * @param g El contexto gráfico sobre el que se pintará el estado
     * @see #DEFAULT_BG_COLOR
     */
    public void paint(Graphics g){
	paint(g,!DEFAULT_CURRENT_STATE);
    }

    /**
     * Regresa el centro del JState
     **/
    public Point getCentro(){
	Point tmp = new Point();
	tmp.setLocation(e2d.getCenterX(),e2d.getCenterY());
	return tmp;	
    }

    public Point getQuadCurveP1(){
	Point tmp = new Point();
	tmp.setLocation((getX()+(DIAMETRO/4)),(getY()+(DIAMETRO/4)));
	return tmp;	
    }

    public Point getQuadCurveP2(){
	Point tmp = new Point();
	tmp.setLocation((getX()+(DIAMETRO - DIAMETRO/4)),(getY()+(DIAMETRO/4)));
	return tmp;	
    }

    public Point getQuadCurveCP(){
	Point tmp = new Point();
	tmp.setLocation((getX()+(DIAMETRO - DIAMETRO/4)),(getY() - DIAMETRO));	
	return tmp;	
    }

    
    public static void main(String argv[]){
	
	JState kk = new JState("HOLA", 5.5, 5.9);
	Debug.println("\n"+kk+"\n");	
    }

    /**
     *  Checa si un punto (x,y) esta en la circunferencia del JState
     * @param x la coordenada en el eje X
     * @param y la coordenada en el eje Y
     */       
    public boolean contains(int x, int y){
	return contains((double)x, (double)y);
    }

    /**
     *  Checa si un punto (x,y) esta en la circunferencia del JState
     * @param x la coordenada en el eje X
     * @param y la coordenada en el eje Y
     */       
    public boolean contains(double x, double y){
	return e2d.contains(x,y);
    }

    
} // JState
