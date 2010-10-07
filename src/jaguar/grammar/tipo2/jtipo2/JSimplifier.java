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


package jaguar.grammar.tipo2.jtipo2;

import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.grammar.tipo2.*;
import jaguar.grammar.tipo2.util.*;
import java.io.*;
import javax.swing.*;

/**
 * La extensi'on gr'afica para el convertidor Gtipo3 -> AF
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JSimplifier extends Simplifier implements JConverter{
    /**
     * El 'area donde escribiremos el estado actual
     */
    protected JTextArea detailsArea;
    /**
     * El valor por omisión para detailsArea
     */
    public static final JTextArea DEFAULT_DETAILSAREA=null;
    /**
     * funcion de acceso para obtener el valor de detailsArea
     * @return el valor actual de detailsArea
     * @see #detailsArea
     */
    public JTextArea getDetailsarea(){
        return detailsArea;
    }
    /**
     * funcion de acceso para modificar detailsArea
     * @param new_detailsArea el nuevo valor para detailsArea
     * @see #detailsArea
     */
    public void setDetailsarea(JTextArea new_detailsArea){
        detailsArea=new_detailsArea;
    }
    /**
     * Constructor.
     * Recibe los valores para detailsArea.
     * Para el resto de los campos usa el valor por omision.
     * @param detailsArea el valor con el que se inicalizará el campo detailsArea
     * @see #detailsArea
     */
    public JSimplifier (JTextArea detailsArea){
        this();
        this.detailsArea=detailsArea;
    }
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     * @see #DEFAULT_DETAILSAREA
     */
    public JSimplifier (){
        //  super();
        this.detailsArea=DEFAULT_DETAILSAREA;
    }

    public JSimplifier(Gtipo2 gt2){
        this(gt2,null);
    }

    public JSimplifier(Gtipo2 gt2, JTextArea details){
        //  super();
        originalGt2 = gt2;
        detailsArea =details;
    }

     /**
      * La gramática tipo 2 que nos dan como entrada
      */
    protected Gtipo2 originalGt2;
    /**
     * funcion de acceso para obtener el valor de originalGt2
     * @return el valor actual de originalGt2
     * @see #originalGt2
     */
    public Gtipo2 getOriginalGt2(){
        return originalGt2;
    }
    /**
     * funcion de acceso para modificar originalGt2
     * @param new_originalGt2 el nuevo valor para originalGt2
     * @see #originalGt2
     */
    public void setOriginalGt2(Gtipo2 new_originalGt2){
        originalGt2=new_originalGt2;
    }

    /**
     * La gramática sin símbolos muertos
     */
    protected Gtipo2 noMuertosGt2;
    /**
     * funcion de acceso para obtener el valor de noMuertosGt2
     * @return el valor actual de noMuertosGt2
     * @see #noMuertosGt2
     */
    public Gtipo2 getNomuertosgt2(){
        return noMuertosGt2;
    }
    /**
     * funcion de acceso para modificar noMuertosGt2
     * @param new_noMuertosGt2 el nuevo valor para noMuertosGt2
     * @see #noMuertosGt2
     */
    public void setNomuertosgt2(Gtipo2 new_noMuertosGt2){
        noMuertosGt2=new_noMuertosGt2;
    }

    /**
     * La gramática sin producciones epsilon
     */
    protected Gtipo2 noProduccionesEpsilon;
    /**
     * funcion de acceso para obtener el valor de noProduccionesEpsilon
     * @return el valor actual de noProduccionesEpsilon
     * @see #noProduccionesEpsilon
     */
    public Gtipo2 getNoproduccionesepsilon(){
        return noProduccionesEpsilon;
    }
    /**
     * funcion de acceso para modificar noProduccionesEpsilon
     * @param new_noProduccionesEpsilon el nuevo valor para noProduccionesEpsilon
     * @see #noProduccionesEpsilon
     */
    public void setNoproduccionesepsilon(Gtipo2 new_noProduccionesEpsilon){
        noProduccionesEpsilon=new_noProduccionesEpsilon;
    }

    /**
     * La gramática sin producciones unitarias
     */
    protected Gtipo2 noProduccionesUnitarias;
    /**
     * funcion de acceso para obtener el valor de noProduccionesUnitarias
     * @return el valor actual de noProduccionesUnitarias
     * @see #noProduccionesUnitarias
     */
    public Gtipo2 getNoproduccionesunitarias(){
        return noProduccionesUnitarias;
    }
    /**
     * funcion de acceso para modificar noProduccionesUnitarias
     * @param new_noProduccionesUnitarias el nuevo valor para noProduccionesUnitarias
     * @see #noProduccionesUnitarias
     */
    public void setNoproduccionesunitarias(Gtipo2 new_noProduccionesUnitarias){
        noProduccionesUnitarias=new_noProduccionesUnitarias;
    }

    public void doConvertion(){
        System.err.println("JSimplifier.doConvertion");
        //  doConvertion(Debug.DEBUG_OFF);
    }

    /**
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status
     */
    public void showStatus(String msg, int debug_level){
        if(detailsArea != null)
            detailsArea.append(msg);
        else Debug.println("\n\nWARNING: the details area in the JDfa2Gtipo3 engine has not been initialized!!!");
    }

    /**
     * Rutinas de prueba para la clase JSimplifier.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase JSimplifier. \n"
               +"Comentario: La extensi'on gr'afica para el convertidor Gtipo3 -> AF\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");
    }
}

/* JSimplifier.java ends here. */
