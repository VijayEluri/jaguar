/**
** <JState.java> -- The State's graphical extension
**
** Copyright (C) 2002 by  Ivan HernÃ¡ndez Serrano
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
** Author: Ivan HernÃ¡ndez Serrano <ivanx@users.sourceforge.net>
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
 * @version $Revision: 1.1 $ $Date: 2005/01/31 19:25:05 $
 */

public class JState extends jaguar.structures.State {


    /**
     * El diametro del circulo circunscrito que identificarÃ¡ grÃ¡ficamente a un estado inicial
     */
    public static final double DIAMETRO_FINAL_STATE = 42;

    /**
     * El diametro de un JState, son graficos 50 pixels
     */
    public static final double DIAMETRO = 50;

    /**
     *  Elipse 2D que usamos para el chequeo de puntos dentro de un JState, la usamos para el drag & drop,
     *  para los tooltips, y claro, para dibujar los estados
     **/
    private Ellipse2D e2d;

    /**
     * El circulo cinscuncrito con el que identificaremos a los estados iniciales
     */
    protected Ellipse2D circuloCinscunscritoEstadoFinal;

    /**
     * funcion de acceso para obtener el valor de circuloCinscunscritoEstadoFinal
     * @return el valor actual de circuloCinscunscritoEstadoFinal
     * @see #circuloCinscunscritoEstadoFinal
     */
    protected Ellipse2D getCirculoCinscunscritoEstadoFinal() {
        return circuloCinscunscritoEstadoFinal;
    }
    /**
     * funcion de acceso para modificar circuloCinscunscritoEstadoFinal
     * @param new_circuloCinscunscritoEstadoFinal el nuevo valor para circuloCinscunscritoEstadoFinal
     * @see #circuloCinscunscritoEstadoFinal
     */
    protected void setCirculoCinscunscritoEstadoFinal(Ellipse2D new_circuloCinscunscritoEstadoFinal) {
        circuloCinscunscritoEstadoFinal=new_circuloCinscunscritoEstadoFinal;
    }

    /**
     * Set the value of location.
     * @param p Value to assign to location.
     */
    public void setLocation(Point  p) {
        super.setLocation(p);
        updateEllipse2D();
    }

    /**
     * Set the value of location.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setLocation(double x, double y) {
        super.setLocation(x,y);
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
        circuloCinscunscritoEstadoFinal = new Ellipse2D.Double(
                                                    getX()+(DIAMETRO-DIAMETRO_FINAL_STATE)/2,
                                                    getY()+(DIAMETRO-DIAMETRO_FINAL_STATE)/2,
                                                    DIAMETRO_FINAL_STATE,DIAMETRO_FINAL_STATE);
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
     * El color por default es blue
     **/
    public static final Color DEFAULT_BG_COLOR = Color.white;


    /**
     * El color por default es orange
     **/
    public static final Color DEFAULT_FINAL_BG_COLOR = new Color(255, 102,0);

    /**
     * El color por default es orange
     **/
    public static final Color DEFAULT_STROKE_COLOR = Color.black;

    public static final Color CURRENT_STROKE_COLOR = Color.white;

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

    private JState() {
        this("",false);
    }

    public JState(State st) {
        this(st.getLabel(), st.getLocation().getX(), st.getLocation().getY(), st.getIsInF() );
    }


    /**
     * Constructora que recibe la etiqueta del estado, con esta
     * constructora el punto del estado  es el (0,0) y no es estado final, es equivalente a usar <code> JState(label,false)</code>
     *@param label La etiquta del estado
     **/
    public JState(String label) {
        this(label,false);
    }

    /**
     * Constructora que recibe la etiqueta del estado, con esta
     * constructora el punto del estado  es el (0,0)
     *@param label La etiquta del estado
     *@param _isInF verdadero si es un estado final, falso si no lo es
     **/
    public JState(String label, boolean _isInF) {
        this(label,0,0, _isInF);
    }

    /**
     * Construye un estado grÃ¡fico con su posiciÃ³n, por omision no es estado final es equivalente a usar <code>JState(label,x,y,false)</code>
     *@param label La etiquta del estado
     *@param x posicion sobre el eje X
     *@param y posicion sobre el eje Y
     **/
    public JState(String label, double x, double y) {
        this(label,x,y,false);
    }

    /**
     * Construye un estado grÃ¡fico con su posiciÃ³n y hace explicito si es un estado final o no
     *@param label La etiquta del estado
     *@param x posicion sobre el eje X
     *@param y posicion sobre el eje Y
     *@param _isInF verdadero si es un estado final, falso si no lo es
     **/
    public JState(String label, double x, double y, boolean _isInF) {
        super(label,x,y,_isInF);
        e2d = new Ellipse2D.Double(getX(),getY(),DIAMETRO,DIAMETRO);
        circuloCinscunscritoEstadoFinal= new Ellipse2D.Double(
                                                    getX()+(DIAMETRO-DIAMETRO_FINAL_STATE)/2,
                                                    getY()+(DIAMETRO-DIAMETRO_FINAL_STATE)/2,
                                                    DIAMETRO_FINAL_STATE,DIAMETRO_FINAL_STATE);
    }


    public String toString() {
        return super.toString();
    }

    /**
     * Este nos dice si es un estado inicial para identificarlo grÃ¡ficamente
     */
    protected boolean esEstadoInicial;
    /**
     * El valor por omisiÃ³n para esEstadoInicial
     */
    public static final boolean DEFAULT_ESESTADOINICIAL=false;
    /**
     * funcion de acceso para obtener el valor de esEstadoInicial
     * @return el valor actual de esEstadoInicial
     * @see #esEstadoInicial
     */
    public boolean getEsEstadoInicial() {
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
     * Pinta un estado en un contexto grÃ¡fico dependiendo del parametro lo pinta
     * en tercera dimension levantado o presionado
     * @param g El contexto grÃ¡fico sobre el que se pintarÃ¡ el estado
     * @param esEstadoActual si es verdadero lo hace levantado, si es falso lo dibuja aplastado
     */
    public void paint(Graphics g, boolean esEstadoActual){
        Graphics2D g2d = (Graphics2D) g;
        Stroke origS = g2d.getStroke();
        Color c;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(DEFAULT_BG_COLOR);
        if (esEstadoActual==DEFAULT_CURRENT_STATE) {// Si no lo quieren raise entonces lo quieren mÃ¡s obscuro
            g.setColor(DEFAULT_BG_CURRENT_STATE_COLOR);
        }
        g2d.fill(getEllipse()); // Comment to debug arcs and loops
        g.setColor(DEFAULT_STROKE_COLOR);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.draw(getEllipse());
        if (getIsInF()) { // es final
            g.setColor(DEFAULT_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.draw(getCirculoCinscunscritoEstadoFinal());
        }
        if (getEsEstadoInicial()) { // es inicial
            paintArrowHead(g2d,DEFAULT_STROKE_COLOR);
        }
        // Dibuja la etiqueta
        if (esEstadoActual==DEFAULT_CURRENT_STATE) {
            g.setColor(CURRENT_STROKE_COLOR);
        } else {
            g.setColor(DEFAULT_STROKE_COLOR);
        }
        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.drawString(getLabel(),
            (int)(getX()+(DIAMETRO/2)-(fontMetrics.stringWidth(getLabel())/2)),
            (int)(getY()+(DIAMETRO/2)+6));
        g2d.setStroke(origS);
    }

    public void paintArrowHead(Graphics2D g2d, Color c) {
            Point center = getCentro().getLocation();
            double stateRadius = JState.DIAMETRO/2;
            Point tip = new Point(center);
            tip.translate((int) -stateRadius,0);

            Point start = new Point(tip);
            start.translate((int) -stateRadius*2,0);
            double headWidth = 5;
            double headLength = 40;
            java.awt.geom.Line2D.Double l2d = new java.awt.geom.Line2D.Double();
            g2d.setColor(c);
            g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

            l2d.setLine(start,tip);
            g2d.draw(l2d);

            start = new Point(tip);
            start.translate((int) -headLength/2,(int) headWidth);
            l2d.setLine(start,tip);
            g2d.draw(l2d);

            start = new Point(tip);
            start.translate((int) -headLength/2,(int) -headWidth);
            l2d.setLine(start,tip);
            g2d.draw(l2d);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);

        }


    /**
     * Pinta un estado en un contexto grÃ¡fico con el background del
     * color definido en <code>DEFAULT_BG_COLOR</code>
     * @param g El contexto grÃ¡fico sobre el que se pintarÃ¡ el estado
     * @see #DEFAULT_BG_COLOR
     */
    public void paint(Graphics g) {
        paint(g,!DEFAULT_CURRENT_STATE);
    }

    /**
     * Regresa el centro del JState
     **/
    public Point getCentro() {
        Point tmp = new Point();
        tmp.setLocation(e2d.getCenterX(),e2d.getCenterY());
        return tmp;
    }

    public Point getQuadCurveP1() {
        Point tmp = new Point();
        tmp.setLocation((getX()+(DIAMETRO/4)),(getY()+(DIAMETRO/4)));
        return tmp;
    }

    public Point getQuadCurveP2() {
        Point tmp = new Point();
        tmp.setLocation((getX()+(DIAMETRO - DIAMETRO/4)),(getY()+(DIAMETRO/4)));
        return tmp;
    }

    public Point getQuadCurveCP() {
        Point tmp = new Point();
        tmp.setLocation((getX()+(DIAMETRO - DIAMETRO/4)),(getY() - DIAMETRO));
        return tmp;
    }


    public static void main(String argv[]) {
        JState kk = new JState("HOLA", 5.5, 5.9);
        Debug.println("\n"+kk+"\n");
    }

    /**
     *  Checa si un punto (x,y) esta en la circunferencia del JState
     * @param x la coordenada en el eje X
     * @param y la coordenada en el eje Y
     */
    public boolean contains(int x, int y) {
        return contains((double)x, (double)y);
    }

    /**
     *  Checa si un punto (x,y) esta en la circunferencia del JState
     * @param x la coordenada en el eje X
     * @param y la coordenada en el eje Y
     */
    public boolean contains(double x, double y) {
        return e2d.contains(x,y);
    }


} // JState
