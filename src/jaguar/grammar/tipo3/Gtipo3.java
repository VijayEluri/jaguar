/**
** <Gtipo3.java> -- The type 3 grammar with all its restrictions
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


package jaguar.grammar.tipo3;

import jaguar.grammar.*;
import jaguar.structures.*;
import jaguar.grammar.structures.*;
import jaguar.grammar.structures.exceptions.*;
import jaguar.grammar.tipo3.structures.*;
import jaguar.grammar.tipo3.structures.exceptions.*;
import jaguar.grammar.exceptions.*;
import jaguar.util.*;
import java.io.*;
import java.util.*;
import jaguar.grammar.jgrammar.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
/**
 * Esta es la clase que representa las gramaticas tipo 3
 *
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Gtipo3 extends Grammar{
    /**
     * Constructor sin parámetros.
     * Inicializa el objeto usando los valores por omision.
     */
    public Gtipo3 (){
        super();
        P = new ProductionT3Set();
    }
    public Gtipo3 (Symbol _S, ProductionSet _P, Alphabet _T, Alphabet _N) throws ProductionNotValidTypeException{
        super(_S, _P, _T, _N);
        validate();
    }
    /**
     * Constructora que construye una <code>Gtipo3</code> a partir del nombre de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     */
    public Gtipo3(String filename)throws Exception{
        this(new File(filename));
    }

    /**
     ** Constructora que construye un <code>Gtipo3</code> a partir del <code>File</code> de un archivo que es valido segun el DTD de las Grammar
     * @see <a href="http://ijaguar.sourceforge.net/DTD/grammar.dtd">grammar.dtd</a>
     **/
    public Gtipo3(File file)throws Exception{
        this();
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        setupGrammar(factory.newDocumentBuilder().parse(file),this);
        validate();
    }

    /* * funcion de acceso para obtener el valor de linealidad
     * @return el valor actual de linealidad
     * @see #linealidad
     */
    public int getLinealidad(){
        return ((ProductionT3Set)P).getLinealidad();
    }


    public boolean validate() throws ProductionNotValidTypeException{
        return P.validate(N,T,S);
    }

    /**
     * Regresa una cadena con una representación del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString(){
        return  "G \n\n\tN = " + getN() + "\n\tT = "+ getT() + "\n\tS = " + getS() + "\n\tP = " + getP() + "\n";
    }


    /**
     * Esta función checa si la gramática puede generar la cadena <code>str</code>
     *@param str la cadena a generar
     *@return <code>true</code> si la gramática genera la cadena <code>str</code>, <code>false</code> e.o.c.
     **/
    public boolean genera(Str str){
        Str antecedente = new Str(getS());
        if(str.isEpsilon())
            return (P.contains(new ProductionT3(antecedente,new Str())));
        Str cadena = null;
        try{
            cadena = (Str)str.clone();
        }catch( CloneNotSupportedException ouch){
            System.err.println("["+(new java.util.Date()).toString()+"]"+this.getClass().getName()
                   + " e: " );
            ouch.printStackTrace();
        }
        return genera(cadena,antecedente, new LinkedList());
    }

    private boolean genera(Str currentStr, Str antecedente, LinkedList toApply){
        if(currentStr.length() == 1){
            toApply.add(new ProductionT3(antecedente,currentStr));
            return (getP().contains(new ProductionT3(antecedente,currentStr)));
        }
        ProductionT3Set nextProds;
        ProductionT3 currentP;
        Str currentA, currentC;
        boolean result = false;
        nextProds=((ProductionT3Set)getP()).getProductionsStartWith(antecedente,currentStr.getFirst());
        if(!nextProds.isEmpty()){
            Object oArray[] = nextProds.toArray();
            for(int i = 0 ; i < oArray.length ; i++){
                if (((ProductionT3)oArray[i]).getConsecuente().length() > 1) {
                    currentP= (ProductionT3)oArray[i];
                    currentA = currentP.getAntecedente();
                    currentC = currentP.getConsecuente();
                    if (currentStr.substring(1).length()==1 && (getP().contains(new ProductionT3(currentC.substring(1),currentStr)))) {
                        toApply.add(new ProductionT3(currentC.substring(1),currentStr));
                        return true;
                    }
                    if (result = (result || genera(currentStr.substring(1),currentC.substring(1),toApply))) {
                        toApply.add(currentP);
                        setPath(new LinkedList(toApply));
                        return true;
                    }
                }
            }
            return result;
        }
        return false;
    }


    /**
     * El camino que tenemos que recorrer para generar las palabras, este camino siempre estará en orden inverso con respecto a como es que se tienen
     * que aplicar las producciones, así que si se quieren leer en el orden adecuado se tiene que usar la función <code>reversePath</code>
     * @see #reversePath
     */
    protected LinkedList path;
    /**
     * funcion de acceso para obtener el valor de path
     * @return el valor actual de path
     * @see #path
     */
    public LinkedList getPath(){
        return path;
    }
    /**
     * funcion de acceso para modificar path
     * @param new_path el nuevo valor para path
     * @see #path
     */
    public void setPath(LinkedList new_path){
        path=new_path;
    }

    /**
     * Regresa la secuencia adecuada de producciones que se deben de aplicar para aceptar la última cadena que se probo.
     *
     * @return La secuencia de producciones enn el orden adecuado, i.e. en este orden se deben de aplicar las producciones.
     *
     */
    public LinkedList reversePath(){
        LinkedList result = new LinkedList();
        LinkedList aux =  (LinkedList)path.clone();
        int len = path.size();
        for(int i = 0 ; i < len ; i++)
            result.add(aux.removeLast());
        return result;
    }


    /**
     * Rutinas de prueba para la clase Gtipo3.
     * La implementación por omisión simplemente imprime el nombre de la clase.
     *
     * @param args los argumentos de la linea de comandos.
     */
    public static void main(String[] args){
        try{
            System.out.println("\n\n\tLeyendo la gramática de: " + args[0] + "\n\tLeyendo la cadena de: "+ args[1]+"\n");
            Gtipo3 gt3 = new Gtipo3(args[0]);

            System.err.println("\n \tgt3: " + gt3);

            if(gt3.getLinealidad() == ProductionT3Set.L_IZQUIERDA){
                System.err.println("\n\nEste motor, para generar cadenas solo puede usarse en gramáticas lineales derechas y la Gramática G\n"
                    + gt3 + "\nEs lineal izquierda");
                System.exit(0);
            }
            Str str = new Str(args[1],false);
            Debug.println("La gramática: " + gt3 + "\n\n\tCadena a generar: "+ str);
            boolean result = gt3.genera(str);
            Debug.println(((result)?"SI":"NO")+" genera la cadena "  + str);
            if(result)
                Debug.println("La secuencia de producciones es: "+gt3.reversePath()+"\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

/* Gtipo3.java ends here. */
