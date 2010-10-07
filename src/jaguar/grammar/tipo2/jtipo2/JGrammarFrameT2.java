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


import jaguar.grammar.tipo2.*;
import java.io.*;
import jaguar.util.Debug;
import jaguar.grammar.exceptions.*;
import jaguar.grammar.structures.exceptions.*;
import javax.swing.JOptionPane;
import jaguar.grammar.jgrammar.*;
/**
 * El frame para las gramaticas tipo 2
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JGrammarFrameT2 extends JGrammarFrame{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JGrammarFrameT2 (){
        super("JGrammarFrameT2");
    }

    protected void initGrammar(File file){
        try{
            setGrammar(new Gtipo2Lazy(file));
            Debug.println("La  gramatica es =>  " + getGrammar());
            displayGrammar();
            replaceSymbol.setEnabled(true);
        }catch( FileNotFoundException ouch){
            JOptionPane.showMessageDialog(null,"Error loading Grammar T2 from file "+file+"\n\""+ouch.getMessage()+"\"","JGrammarFrameT2",JOptionPane.ERROR_MESSAGE);
        }
        catch(GrammarNotFoundException gnf){
            JOptionPane.showMessageDialog(null,"Error loading Grammar T2 from file "+file+"\n\""+gnf.getMessage()+"\"","JGrammarFrameT2",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error loading Grammar T2 from file "+file+"\n\""+e.getMessage()+"\"","JGrammarFrameT2",JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
        String salida="";
        return salida;
    }
    /**
     * Rutinas de prueba para la clase JGrammarFrameT2.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        System.out.println("Esta es la clase JGrammarFrameT2. \n"
               +"Comentario: El frame para las gramaticas tipo 2\n"
               +"Autor: Ivan Hernández Serrano\n"
               +"E-mail: ivanx@users.sourceforge.net\n");

        JGrammarFrameT2 jgf = new JGrammarFrameT2();
        jgf.show();
    }

}

/* JGrammarFrameT2.java ends here. */
