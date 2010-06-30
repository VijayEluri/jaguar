/**
** <JDeltaPainter.java> -- To paint deltas
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
** Author: Juan Germ�n Casta�eda Echevarr�a <juanger@ciencias.unam.mx>
**
**/


package jaguar.machine.util.jutil;

import java.awt.*;
import java.awt.geom.*;
import jaguar.structures.*;
import jaguar.machine.structures.*;
import jaguar.machine.stack.structures.*;
import jaguar.machine.turing.structures.*;
import jaguar.structures.jstructures.*;
import jaguar.util.Debug;
import java.util.*;

/**
 * Pinta las interfaces DeltaPaint
 *
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @author Juan Germ�n Casta�eda Echevarr�a <juanger@ciencias.unam.mx>
 * @version 0.1
 */
public class JDeltaPainter{
    /**
     * El color del cual se pintar�n las transiciones
     **/
    public static final Color CURRENT_TRANSITION_COLOR = Color.green;

    /**
     * El color del cual se pintar�n las etiquetas de las transiciones
     **/
    public static final Color ARROW_LABEL_COLOR = Color.black;

    public static final Font ARROW_LABEL_FONT = new Font("Sanserif",Font.BOLD,12);

    /**
     * El color del cual se pintar�n las etiquetas
     **/
    public static final Color ARROW_COLOR = Color.black;

     /**
      * El diagrama que define la delta
      */
    protected JDeltaGraphic jdelta;
    /**
     * funcion de acceso para obtener el valor de jdelta
     * @return el valor actual de jdelta
     * @see #jdelta
     */
    public JDeltaGraphic getJDelta() {
        return jdelta;
    }

    /**
     * funcion de acceso para modificar jdelta
     * @param new_jdelta el nuevo valor para jdelta
     * @see #jdelta
     */
    public void setJDelta(JDeltaGraphic new_jdelta) {
        jdelta=new_jdelta;
    }

    public JDeltaPainter (){
        super();
    }

    /**
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JDeltaPainter (JDeltaGraphic _jdelta) {
  this();
  jdelta = _jdelta;
    }

    public void paintArrowHead(Graphics2D g2d, Point orig, Point dest) {
        paintArrowHead(g2d, orig, dest, ARROW_COLOR, "una etiqueta");
    }
    // Este es el rud�simo (juanger)
    public void paintArrowHead(Graphics2D g2d, Point orig, Point dest, Color c, String label) {
        double slope = (orig.getY()-dest.getY())/(orig.getX()-dest.getX());
        double headWidth = 5;
        double headLength = 40;

        // Perpendicular line
        Point p1 = new Point();
        p1.setLocation(0.0,0.0);
        Point p2 = new Point();
        p2.setLocation(1.0, (-1/slope)*(1-p1.getX()) + p1.getY());

        // Change in original line
        double dx = (orig.getX()-dest.getX());
        double dy = (orig.getY()-dest.getY());
        double dr = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double ratio = 1/dr;
        // Intersection of line with state where arrow tip will be drawn
        Point tip = new Point(dest);
        double stateRadius = JState.DIAMETRO/2;
        tip.translate((int) (dx*ratio*stateRadius),(int) (dy*ratio*stateRadius));
        // Point at base of head
        Point p3 = new Point(dest);
        p3.translate((int) (dx*ratio*headLength),(int) (dy*ratio*headLength));

        // Change in perpendicular line
        dx = (p1.getX()-p2.getX());
        dy = (p1.getY()-p2.getY());
        dr = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        ratio = 1/dr;

        java.awt.geom.Line2D.Double l2d = new java.awt.geom.Line2D.Double();

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2d.setColor(c);

        Point tmp = new Point(p3);
        tmp.translate((int) (dx*ratio*headWidth),(int) (dy*ratio*headWidth));
        l2d.setLine(tmp,tip);
        g2d.draw(l2d);

        tmp = new Point(p3);
        tmp.translate((int) (dx*ratio*-headWidth),(int) (dy*ratio*-headWidth));
        l2d.setLine(tmp, tip);
        g2d.draw(l2d);

        double nx = (orig.getX()+ dest.getX())/2 , ny=(orig.getY()+ dest.getY())/2;
        g2d.setColor(ARROW_LABEL_COLOR);
        g2d.drawString(label,(float)nx,(float)ny);
        g2d.setColor(ARROW_COLOR);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

    }

    ////este es el de salva
    public void paintArrowHead2(Graphics2D g2d, Point orig, Point dest, Color c) {
        Point newp   = new Point();
        double nx = (orig.getX()+ dest.getX())/2 , ny=(orig.getY()+ dest.getY())/2;
        newp.setLocation((nx+dest.getX())/2,(ny+dest.getY())/2);
         Point newdest = new Point();
        newdest.setLocation(dest.getX(),dest.getY());
        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2d.setColor(c);
        java.awt.geom.Line2D.Double l2d = new java.awt.geom.Line2D.Double();
        l2d.setLine(newp,newdest);
        g2d.draw(l2d);

        Point newpp = new Point();
        newpp.setLocation(newp.getX()+5,newp.getY()+5);
        l2d.setLine(newpp, newdest);
        g2d.draw(l2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void paintLabelSelfState(Graphics2D g2d, Point p, Symbol sym) {
        paintLabelSelfState(g2d, p, sym.toString());
    }

    public void paintLabelSelfState(Graphics2D g2d, Point p, String label) {
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        java.awt.geom.Line2D.Double l2d = new java.awt.geom.Line2D.Double();
        Point p1 = new Point();
        p1.setLocation((float)p.getX(), (float)p.getY()+40);
        Point p2 = new Point();
        p2.setLocation((float)p.getX(), (float)p.getY()+53);
        l2d.setLine(p1,p2);
        g2d.draw(l2d);
        p1.translate(-5,0);
        l2d.setLine(p1,p2);
        g2d.draw(l2d);


        g2d.setColor(ARROW_LABEL_COLOR);
        g2d.drawString(label,(float)p.getX(), (float)p.getY()+30);
        g2d.setColor(ARROW_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
    }


    public void paint(Graphics g){
        Hashtable centrosp1 = new Hashtable();
        Hashtable currentp=null, htmp=null;
        HashSet currentDestinos;
        JState jq, jp;
        Point x,y;
        boolean first_check ;
        // las transiciones van de q -> p
        Vector vaux, vpair;
        g.setFont(ARROW_LABEL_FONT);
        // Este for nos va a dar los estados a partir de donde salen las transiciones,
        // i.e. este enumeration nos da los estados de los cuales podemos hacer transiciones
        // con algun simbolo...
        for(Enumeration e = ((Delta)jdelta).keys();  e.hasMoreElements() ;) {
            g.setColor(ARROW_COLOR);
            first_check = false;
            // Ahora para cada estado de estos tenemos que sacar todas sus transiciones
            JState q = (JState)e.nextElement();
            vaux = ((Delta)jdelta).getTransitions(((State) q));
            currentDestinos = new HashSet();
            for(int i = 0 ; i < vaux.size(); i++){
                vpair = (Vector)vaux.elementAt(i);
                Symbol sym = (Symbol)vpair.elementAt(0);
                State p;
                if(jdelta instanceof StackDelta){
                    p = ((QxGammaStar)((QxGammaStarSet)vpair.elementAt(2)).toArray()[0]).getQ();
                } else if(jdelta instanceof TuringDelta){
                    p = ((QxGammaxDirection)vpair.elementAt(1)).getQ();
                } else {
                    p = (State)vpair.elementAt(1);
                }

                if(!currentDestinos.contains(p)){
                    currentDestinos.add(p);
                    Graphics2D g2d = (Graphics2D) g;
                    Stroke origS = g2d.getStroke();
                    if(q.equals(jdelta.getCurrent_p()) && p.equals(jdelta.getCurrent_q()) && sym.equals(jdelta.getCurrent_sym())) {
                        g.setColor(CURRENT_TRANSITION_COLOR);
                    }

                    g2d.setStroke(new BasicStroke(2.0f));
                    if(q.equals(p)){
                        if(q.equals(jdelta.getCurrent_p()) && p.equals(jdelta.getCurrent_q()))
                            g.setColor(CURRENT_TRANSITION_COLOR);
                        java.awt.geom.QuadCurve2D.Double qc2d = new java.awt.geom.QuadCurve2D.Double();
                        qc2d.setCurve(((JState)q).getQuadCurveP1(), ((JState)q).getQuadCurveCP(), ((JState)q).getQuadCurveP2());
                        g2d.draw(qc2d);
                        paintLabelSelfState(g2d, ((JState)q).getQuadCurveCP(), jdelta.getLabelString((JState)q,(JState)p));
                    } else {
                        java.awt.geom.Line2D.Double l2d = new java.awt.geom.Line2D.Double();
                        jq = (JState) q;
                        jp = (JState) p;
                        x = jq.getCentro().getLocation();
                        y = jp.getCentro().getLocation();
                        if((currentp = (Hashtable)centrosp1.get(x)) != null){
                            if(currentp.get(y) !=null){
                                x.translate(10,10);
                                y.translate(10,10);
                                first_check = true;
                            } else {
                                currentp.put(y,new Boolean(true));
                            }
                        } if (!first_check) {
                            x = jq.getCentro().getLocation();
                            y = jp.getCentro().getLocation();
                            if ((currentp = (Hashtable)centrosp1.get(y)) != null) {
                                if (currentp.get(y) !=null) {
                                    x.translate(10,10);
                                    y.translate(10,10);
                                    first_check = true;
                                } else {
                                    currentp.put(x,new Boolean(true));
                                }
                            } else {
                                htmp = new Hashtable();
                                htmp.put(x,new Boolean(true));
                                centrosp1.put(y,htmp);
                            }
                        }
                        l2d.setLine(x,y);
                        g2d.draw(l2d);

                        paintArrowHead(g2d,x,y,g.getColor(), jdelta.getLabelString((JState)q,(JState)p));
                    }
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setStroke(origS);
                }
            }
        }
    }


}

/* JDeltaPainter.java ends here. */
