/**
** <Simplifier.java> -- Functions to simplify a type 2 grammar
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


package jaguar.grammar.tipo2.util;

import jaguar.grammar.tipo2.*;
import jaguar.grammar.tipo2.structures.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.grammar.structures.*;
import jaguar.machine.util.*;
import jaguar.util.*;
import java.io.*;
import java.util.*;

/** 
 * Esta clase tiene los algoritmos para la simplificación de gramáticas
 * 
 * @author Ivan Hernández Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class Simplifier {
    
    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     *
     */
    protected void showStatus(String msg){
	showStatus(msg,Debug.DEBUG_ON);	
    }
    
    /** 
     * Muestra el status de la conversión cada que se le llama.
     * Dependiendo del tipo de convertidor (texto o gráfico) esta función decide como enviar los mensajes que se van dando en el proceso de conversión  .
     *
     * @param msg El mensaje para el status actual.
     * @param debug_level para ver que tan verbose debemos hacer el status 
     */
    protected void showStatus(String msg, int debug_level){
	Debug.print(msg,debug_level);	
    }

     /**
      * El nivel de debugeo que queremos
      */
    protected  int debug_level=Debug.DEBUG_OFF;
    /**
     * funcion de acceso para obtener el valor de debug_level
     * @return el valor actual de debug_level
     * @see #debug_level
     */
    public int getDebug_level(){
	return debug_level;
    }
    /**
     * funcion de acceso para modificar debug_level
     * @param new_debug_level el nuevo valor para debug_level
     * @see #debug_level
     */
    public void setDebug_level(int new_debug_level){
	debug_level=new_debug_level;
    }    
    /**
     * Elimina los símbolos "muertos" de la gramática <code>gt2</code>
     *
     * @param gt2 la gramática con posibles símbolos "muertos"
     * @return una gramatica equivalente a gt2 sin símbolos muertos y sin las producciones que no producen.
     **/
    public Gtipo2 eliminaSimbolosMuertos(Gtipo2 gt2){
	setDebug_level(debug_level);
	showStatus("\nELIMINANDO SIMBOLOS MUERTOS",debug_level);
	showStatus("\nGramática de entrada: \n"+ gt2,debug_level);
	try{
	    Alphabet ViejoN = new Alphabet();
	    showStatus("\n\tViejoN = " + ViejoN,debug_level);
	    Alphabet NuevoN = getAntecedentWithConsecuentInAlphabet(gt2.getP(),gt2.getT());
	    showStatus("\n\tNuevoN = " + NuevoN,debug_level);
	    
	    while(! ViejoN.equals(NuevoN)){
		ViejoN = (Alphabet)NuevoN.clone();
		NuevoN = ViejoN.union(getAntecedentWithConsecuentInAlphabet(gt2.getP(),gt2.getT().union(ViejoN)));
		showStatus("\n\tViejoN = " + ViejoN,debug_level);
		showStatus("\n\tNuevoN = " + NuevoN+"\n",debug_level);
	    }
	    showStatus("\nEl nuevo conjunto N es " + NuevoN,debug_level);
	    gt2.setN(NuevoN);
	    showStatus("\nGenerando el conjunto de producciones con el antecedente en el conjunto NUT ",debug_level);
	    gt2.setP(getProductionsWithAntecedenteConsecuentInAlphabet(gt2.getP(),gt2.getN().union(gt2.getT())));
	    showStatus("\nLa gramática resultante es  "+gt2,debug_level);
	    return gt2;

	}catch( CloneNotSupportedException e){
	    System.err.println("["+(new java.util.Date()).toString()+"]  jaguar.grammar.tipo2.util.Simplifier: No se pudo clonar  " ); 
	    e.printStackTrace(); 
	    System.exit(1);
	}
	return null;
    }

    public Gtipo2 eliminaSimbolosInalcanzables(Gtipo2 gt2){
	try{
	    showStatus("\nELIMINANDO SIMBOLOS INALCANZABLES",debug_level);
	    showStatus("\nGramática de entrada: \n"+ gt2,debug_level);
	    Alphabet ViejoN = new Alphabet();
	    showStatus("\n\tViejoN = " + ViejoN,debug_level);
	    Alphabet NuevoN = new Alphabet(gt2.getS());
	    showStatus("\n\tNuevoN = " + NuevoN,debug_level);
	    
	    Alphabet ViejoT = new Alphabet();
	    showStatus("\n\tViejoT = " + ViejoT,debug_level);
	    Alphabet NuevoT = new Alphabet();
	    showStatus("\n\tNuevoT = " + NuevoT,debug_level);
	    ProductionT2Set currentPSet;
	    Alphabet auxA;
	    while((!ViejoN.equals(NuevoN)) || (!ViejoT.equals(NuevoT))){
		ViejoN = (Alphabet)NuevoN.clone();
		ViejoT = (Alphabet)NuevoT.clone();

		Object aN[] = NuevoN.toArray();

		for(int i = 0 ; i < aN.length ; i++){
		    Str currentAntecedente = new Str((Symbol)aN[i]);
		    currentPSet = getProductionsWithAntecedent(currentAntecedente,gt2.getP());
		    Object prodArray [] = currentPSet.toArray();
		    for(int j = 0 ; j < prodArray.length; j++){
			NuevoN = NuevoN.union(gt2.getN().getSubsetInStr(((ProductionT2)prodArray[j]).getConsecuente()));

			NuevoT = NuevoT.union(gt2.getT().getSubsetInStr(((ProductionT2)prodArray[j]).getConsecuente()));
			
		    }
		}
		showStatus("\n\tViejoN = " + ViejoN,debug_level);
		showStatus("\n\tNuevoN = " + NuevoN,debug_level);
		showStatus("\n\tViejoT = " + ViejoT,debug_level);
		showStatus("\n\tNuevoT = " + NuevoT,debug_level);
	    }
	    gt2.setN(NuevoN);
	    showStatus("\nEl nuevo conjunto N es " + NuevoN,debug_level);
	    gt2.setT(NuevoT);
	    showStatus("\nEl nuevo conjunto T es " + NuevoT,debug_level);
	    showStatus("\nGenerando el conjunto de producciones con el antecedente y consecuente en NUT ",debug_level);
	    gt2.setP(getProductionsWithAntecedenteConsecuentInAlphabet(gt2.getP(),gt2.getN().union(gt2.getT())));
	    showStatus("\nLa gramática resultante es  "+gt2,debug_level);
	    return gt2;

	}catch( CloneNotSupportedException e){
	    System.err.println("["+(new java.util.Date()).toString()+"]  jaguar.grammar.tipo2.util.Simplifier: No se pudo clonar  " ); 
	    e.printStackTrace(); 
	    System.exit(1);
	}
	return null;
    }

    /**
     ** Regresa una gramática equivalente a la de entrada sin producciones epsilon
     ** @param gt2 La gramática de entrada a la cual le quitaremos las producciones epsilon
     ** @return una gramática tipo 2 sin producciones epsilon
     **/ 
    public Gtipo2 eliminaProduccionesEpsilon(Gtipo2 gt2){
	showStatus("\nELIMINANDO PRODUCCIONES EPSILON",debug_level);
	showStatus("\nGramática de entrada: \n"+ gt2,debug_level);
	Gtipo2 res = eliminaSimbolosNulificables(gt2);
	res.getP().remove(new ProductionT2(new Str(gt2.getS()),new Str()));
	return res;
    }

    public Gtipo2 eliminaSimbolosNulificables(Gtipo2 gt2){
	try{
	    showStatus("\n\tDeterminando SíMBOLOS NULIFICABLES",debug_level);

	    Alphabet NulifV = new Alphabet();
	    showStatus("\n\tNulifV = " + NulifV,debug_level);
	    Alphabet NulifN = getNoTerminalesWithConsecuente(new Str(), gt2.getP());
	    showStatus("\n\tNulifN = " + NulifN,debug_level);
	    Alphabet Aux;
	    Str antecedenteS = new Str(gt2.getS());
	    /** Si la produccion A -> <epsilon> está en P, entoces A es nulificable **/
	    while(!NulifV.equals(NulifN)){
		NulifV = (Alphabet) NulifN.clone();
		NulifN = NulifN.union(getNulif(gt2.getP(),NulifV,antecedenteS));
		showStatus("\n\tNulifV = " + NulifV,debug_level);
		showStatus("\n\tNulifN = " + NulifN,debug_level);
	    }

// 	    /** Ahora construimos el conjunto de producciones P' **/
	    ProductionT2Set pPrima = getProductionsWithoutConsecuente(new Str(), antecedenteS, gt2.getP());
	    showStatus("\nConstruyendo en nuevo conjunto de producciones \"pPrima\" sin producciones epsilon y eliminando los simbolos nulificables\npPrima = " + pPrima,debug_level);
	    /** Ahora seleccionamos el conjunto de producciones que tienen simbolos nulificables en el consecuente **/
	    ProductionT2Set prodWithSomeNulifInConsecuente = getProductionsWithSomeSymbolInConsecuente(gt2.getP(),NulifN);
	    Object prodWithSomeNulifInConsecuenteArray[] = prodWithSomeNulifInConsecuente.toArray();
	    for(int i = 0 ; i < prodWithSomeNulifInConsecuenteArray.length ; i++){
		pPrima=(ProductionT2Set)pPrima.union(getCombinacionesDeConsecuente((ProductionT2)prodWithSomeNulifInConsecuenteArray[i],NulifN));
		showStatus("\npPrima = " + pPrima,debug_level);
	    }
	    gt2.setP(pPrima);
	    showStatus("\nLa gramática resultante es  "+gt2,debug_level);
	    return gt2;
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	    return null;
	}
    }

    public Gtipo2 eliminaProduccionesUnitarias(Gtipo2 gt2){
	showStatus("\nELIMINANDO PRODUCCIONES UNITARIAS",debug_level);
	showStatus("\nGramática de entrada: \n"+ gt2,debug_level);
	ProductionT2Set produccionesUnit = getProduccionesUnitarias((ProductionT2Set)gt2.getP());
	showStatus("\nLas producciones unitarias son: \n"+ produccionesUnit,debug_level);
	ProductionT2Set pNueva = new ProductionT2Set();
	pNueva.addAll(gt2.getP());
	// metemos todas las producciones no unitarias
	//Debug.println("\nunit productions: " + produccionesUnit);	
	pNueva.removeAll(produccionesUnit);
	showStatus("\nRemoviendo las producciones unitarias. \nEl nuevo conjunto de producciones sin unitarias \"pNueva\" =" + pNueva,debug_level);

	/**
	 ** Tenemos que construir un AF con un estado por cada símbolo no terminal que aparece en una producción unitaria. A
	 ** continuación marcamos una transición-<epsilon> entre dos estados  (símbolos no terminales) A y B si la producción
	 ** unitaria A-->B \in P; y transiciones etiquetadas con \alpha a un estado final \phi desde el estado A si |\alpha| > 1 y la
	 ** producción A-->\alpha \in P
	 **/
	SimpleGraph sg = new SimpleGraph();
	ProductionT2 currentUnit;
	ProductionT2Set pSetUnitAnt;
 	Str currAnt, currCons;
 	Object pUnit[]=produccionesUnit.toArray();
 	Object pSetUnitAntA[];
	Str epsilon = new Str();
	Str phiFinal = new Str(new Symbol("_phiFinal_"));
	Str consHopefullyNotUnit;
 	for(int i = 0 ; i < pUnit.length; i++){
 	    currentUnit = (ProductionT2)pUnit[i];
 	    currAnt = currentUnit.getAntecedente();
 	    currCons = currentUnit.getConsecuente();
	    if(gt2.getN().strOnAlphabet(currCons))
		sg.addArista(currAnt,currCons, epsilon);
	    else // significa que tenemos una produccion a un terminal
		sg.addArista(currAnt,phiFinal,currCons);
	    
 	    pSetUnitAnt = getProductionsWithAntecedent(currAnt, gt2.getP());
 	    pSetUnitAntA = pSetUnitAnt.toArray();
 	    for(int j = 0 ; j < pSetUnitAntA.length; j ++){
 		consHopefullyNotUnit = ((ProductionT2)pSetUnitAntA[j]).getConsecuente();
 		if(consHopefullyNotUnit.length() > 1)
 		    sg.addArista(currAnt,phiFinal,consHopefullyNotUnit);
 	    }
 	}
	showStatus("\nLa gráfica sg es: "+ sg,debug_level);
	
	Set estados = sg.getEstados();
	estados.remove(phiFinal);
	Object estadosA [] = estados.toArray();
	HashSet currentEpsilonCerradura;
	Object currentEpsilonCerraduraA[];
	Vector labelsToPhiFinal;
	for(int k = 0 ; k < estadosA.length ; k++){
	    currentEpsilonCerradura = sg.getReachabilityFromSourceWithLabel(estadosA[k],epsilon);
	    currentEpsilonCerraduraA = currentEpsilonCerradura.toArray();
	    showStatus("\nLa epsilon cerradura de "+estadosA[k] +" = " + currentEpsilonCerradura,debug_level);
	    for(int j = 0 ; j < currentEpsilonCerraduraA.length; j ++){
		labelsToPhiFinal = sg.getLabels(currentEpsilonCerraduraA[j],phiFinal);
		for(int i = 0 ; i < labelsToPhiFinal.size(); i ++)
		    pNueva.add(new ProductionT2((Str)estadosA[k],(Str)labelsToPhiFinal.get(i)));
	    }
	    showStatus("\n pNueva = "+pNueva,debug_level);
	}
	gt2.setP(pNueva);
	showStatus("\nLa gramática resultante es  "+gt2,debug_level);
	return gt2;
	
    }


    /** 
     * Dada una gramática la transforma en su Forma Normal de Chomsky (FNC).
     *
     * @param gt2 Una gramática tipo 2, la cual convertiremos en FNC.
     * @return Una gramática en FNC equivalente a la de entrada.
     *
     */
    public Gtipo2 transform2Chomsky(Gtipo2 gt2) throws StrIndexOutOfBoundsException{
	return FNC2ndStep(FNC1stStep(gt2));
    }
 
    /** 
     * El primer paso para pasar a FNC.
     *
     * Regresa una gramática tipo 2 donde todas las producciones sonde alguna de las siguientes formas:
     * <ul>
     *    <li> A --> a </li>
     *    <li> A --> X1 X2 ... Xn, &nbsp;&nbsp; n >= 2, &nbsp; Xi en los no terminales de la nueva gramática </li>
     * </ul> 
     *
     * @param gt2 La gram'atica de entrada.
     * @return Regresa una gramática como la descrita anteriormente.
     *
     */
    protected Gtipo2 FNC1stStep(Gtipo2 gt2) throws StrIndexOutOfBoundsException{	    
	showStatus("\n>>>>>>>>>> FNC 1a parte <<<<<<<<<<<",debug_level);
	Alphabet NPrima = new Alphabet(gt2.getN());
	    ProductionT2Set PPrima = new ProductionT2Set();
	    debug_level= Debug.DEBUG_ON;

	    showStatus("\nBuscando producciones A->a..... ",debug_level);
	    Object To[] = gt2.getT().toArray();
	    for(int i = 0 ; i < To.length; i++)
		PPrima.addAll(getProductionsWithConsecuente(new Str((Symbol)To[i],false),gt2.getP()));
	    showStatus("\nLas producciones A->a..... Done!",debug_level);
	    
	    ProductionT2Set consMayor1 = new ProductionT2Set();
	    consMayor1.addAll(gt2.getP());
	    consMayor1.removeAll(PPrima);

	    Object prodsA[] = consMayor1.toArray();
	    ProductionT2 currP,auxP;
	    Str currCons, newCons;
	    Symbol newSym;
	    ProductionT2Set auxPSet = new ProductionT2Set(), fooPSet;
	    showStatus("\nAntes del entrar al 1er for....!",debug_level);
	    for(int i = 0; i< prodsA.length; i++){
		currP = (ProductionT2)prodsA[i];
		currCons=currP.getConsecuente();
		newCons = new Str();
		showStatus("\n\tFor cambiando consecuentes para ["+ currP +"] ....!",debug_level);
		for(int j = 0 ; j < currCons.length() ; j++){
		    if(gt2.getN().contains(currCons.getSymbol(j))){
			showStatus("\n====\t\t " + currCons.getSymbol(j) +" en N, lo dejamos");			
			newCons.append(currCons.getSymbol(j));
		    }else{
			showStatus("\n<<>>\t\t " + currCons.getSymbol(j) +" no esta en N, generando nuevo Simbolo ",debug_level);
			fooPSet = getProductionsWithConsecuente(new Str(currCons.getSymbol(j),false),auxPSet);
			if(!fooPSet.isEmpty()){
			    auxP = (ProductionT2)fooPSet.toArray()[0];
			    newCons.concat(auxP.getAntecedente());
			}else{			    
			    newSym = genNewSymbol(NPrima.union(gt2.getT()));
			    showStatus(" ["+newSym+"]");
			    NPrima.add(newSym);
			    newCons.append(newSym);			    
			    auxPSet.add(new ProductionT2(new Str(newSym,false), new Str(currCons.getSymbol(j),false)));
			    PPrima.add(new ProductionT2(new Str(newSym,false), new Str(currCons.getSymbol(j),false)));
			}
		    }
		}
		showStatus("\n\t.... Done... result ["+ new ProductionT2(currP.getAntecedente(),newCons)+"]\n", debug_level);	
		PPrima.add(new ProductionT2(currP.getAntecedente(),newCons));
	    }
	    showStatus("\nTodo el For ... Done! \n", debug_level);	    
	    gt2.setN(NPrima);
	    gt2.setP(PPrima);
	    return gt2;
    }

     /** 
      * El segundo paso para generar una gramática en FNC .
      * Dada la gramática de entrada, la cual asumimos que es la resultante de la función <code>FNC1Step</code>, la salida de esta ya está en FNC.
      *
      * @param gt2 La gramática de entrada, resultante del algoritmo FNC1stStep.
      * @return La gramática en FNC.
      *
      * @see #FNC1stStep
      */
    protected Gtipo2 FNC2ndStep(Gtipo2 gt2) throws StrIndexOutOfBoundsException{
	ProductionT2Set P2Prima = new ProductionT2Set();
	debug_level= Debug.DEBUG_ON;
	Object To[] = gt2.getT().toArray();
	showStatus("\n>>>>>>>>>> FNC 2a parte <<<<<<<<<<<",debug_level);
	showStatus("\nAgregando a P2Prima las producciones de la forma A-->a y los de la forma A-->BC\ni.e. los que ya estan en FNC",debug_level);
	for(int i = 0 ; i < To.length; i++)
	    P2Prima.addAll(getProductionsWithConsecuente(new Str((Symbol)To[i]),gt2.getP()));
	// Agregamos todas las que ya están en FNC
	P2Prima.addAll(getProduccionesConsecuenteLength(2,(ProductionT2Set)gt2.getP()));
	
	ProductionT2Set faltanAgregar = new ProductionT2Set();
	faltanAgregar.addAll(gt2.getP());
	faltanAgregar.removeAll(P2Prima);
	
	Object pArray[] = faltanAgregar.toArray();
	Str currAnt, currCons, nuevoAnt, nuevoCons;
	Alphabet nuevosSymbols = new Alphabet();
	Symbol newSym;
	showStatus("\n",debug_level);
	for(int i = 0 ; i < pArray.length; i++){
	    currAnt = ((ProductionT2)pArray[i]).getAntecedente();
	    currCons = ((ProductionT2)pArray[i]).getConsecuente();
	    showStatus("\nPartiendo la producción ["+pArray[i]+"] en las siguientes producciones:",debug_level);
	    do{
		nuevoCons = new Str();
		nuevoCons.append(currCons.getSymbol(0));
		newSym = genNewSymbol(gt2.getN().union(gt2.getT()).union(nuevosSymbols));
		nuevoCons.append(newSym);
		nuevosSymbols.add(newSym);		

		P2Prima.add(new ProductionT2(currAnt,nuevoCons));
		showStatus("\n\t ["+ (new ProductionT2(currAnt,nuevoCons)) +"] ",debug_level);
		currAnt = new Str(newSym,false);
		currCons = currCons.substring(1);
	    }while(currCons.length() != 2);

	    P2Prima.add(new ProductionT2(currAnt,currCons));	    
	    showStatus("\n\t ["+ (new ProductionT2(currAnt,currCons)) +"] ",debug_level);
	}
	
	gt2.setN(gt2.getN().union(nuevosSymbols));
	gt2.setP(P2Prima);
	return gt2;	
    }
    
    /** 
     * Dada una gramática la transforma en su Forma Normal de Greibach (FNG).
     *
     * @param gt2 Una gramática tipo 2, la cual convertiremos en su FNG.
     * @return Una gramática en FNG equivalente a la de entrada.
     *
     */
    public Gtipo2 transform2Greibach(Gtipo2 gt2) throws StrIndexOutOfBoundsException{
	debug_level= Debug.DEBUG_ON;
	Vector vN = new Vector(gt2.getN().getSigma());
	return eliminaRecursionIzquierda(transform2Greibach2ndPart(transform2Greibach1stPart(gt2,vN),vN));
    }


    /** 
     * Lleva a cabo la primera parte del algoritmo para convertir a una gram'atica a su FNG
     * El resultado de esta fuinción es llevar a todas las producciones a que comiencen con un símbolo
     * terminal o con un no terminal con numeración mayor que la propia     
     *
     * @param gt2 Una gramática tipo 2, la cual convertiremos en su FNG.
     * @param vN el vector de no terminales que usaremos para llevar la numeración de estos
     * @return una gramática resultante de la primera parte del algoritmo para pasar a FNG
     *
     */
    protected Gtipo2 transform2Greibach1stPart(Gtipo2 gt2, Vector vN) throws StrIndexOutOfBoundsException{
	showStatus("\n>>>>>>>>>> FNG 1a parte <<<<<<<<<<<",debug_level);
	showStatus("\nEl orden que usamos para los simbolos no terminales es el siguiente: "+ vN,debug_level);
	ProductionT2Set nuevasProdsAgregadas;
	ProductionT2Set currProds;
	ProductionT2Set nuevaP = (ProductionT2Set)gt2.getP();
	Alphabet N = gt2.getN();
	
	Object currProdsA[];
	int firstSymbolConsIndex;
	for(int k = 0 ; k < vN.size() ; k++){
	    showStatus("\nRevisando el simbolo ["+k+"] => "+ (Symbol)vN.get(k),debug_level);
	    currProds = getProductionsWithAntecedent(new Str((Symbol)vN.get(k)),nuevaP);
	    currProdsA = currProds.toArray();
	    for(int i = 0 ; i < currProdsA.length ; i++){

		showStatus("\n\tChecando la producción => "+((Production)currProdsA[i]),debug_level);

		Str cons = ((Production)currProdsA[i]).getConsecuente();
		Symbol symbol0 = cons.getFirst();
		if(!gt2.getT().contains(symbol0) && vN.indexOf(symbol0) < k){
		    // Tenemos que fusionar las producciones
		    showStatus("\n\t El índice del primer símbolo del consecuente ["+
			       symbol0+","+vN.indexOf(symbol0)+"] es menor que el del símbolo k ["+vN.get(k)+","+k+"] a fusionar",debug_level);
		   nuevaP=fusionDeProducciones(nuevaP,(ProductionT2)currProdsA[i],getProductionsWithAntecedent(new Str(symbol0),nuevaP));
		}else if(vN.indexOf(symbol0) == k){
		    showStatus("\n\t El índice del primer símbolo ["+vN.indexOf(symbol0)+"] es igual que [k,"+k+"] a eliminar recursion izquierda",
			       debug_level);
		    // Tenemos que eliminar la recursión izquierda
		    nuevaP = eliminaRecursionIzquierda(nuevaP,symbol0,N,gt2.getT());
		}
	    }
	}
	gt2.setN(N);
	showStatus("\n\t La nueva N="+N,debug_level);
	gt2.setP(nuevaP);
	showStatus("\n\t La nueva P="+nuevaP,debug_level);
	gt2 = eliminaRecursionIzquierda(gt2);	
	showStatus("\n\t Eliminamos la recursión izquierda ="+gt2,debug_level);
	return eliminaProduccionesUnitarias(gt2);	
    }

    /** 
     * Lleva a cabo la segunda parte del algoritmo para convertir a una gram'atica a su FNG
     * El resultado de esta fuinción es llevar a todas las producciones a que comiencen con un símbolo
     * terminal
     *
     * @param gt2 Una gramática tipo 2, resultante de <code>transform2Greibach1stPart<code>, la cual convertiremos en su FNG.
     * @param vN el vector de no terminales que usaremos para llevar la numeración de estos
     * @return una gramática en FNG
     * @see #transform2Greibach1stPart
     */
    protected Gtipo2 transform2Greibach2ndPart(Gtipo2 gt2, Vector vN) throws StrIndexOutOfBoundsException{    
	showStatus("\n>>>>>>>>>> FNG 2a parte <<<<<<<<<<<",debug_level);
	showStatus("\nEl orden que usamos para los simbolos no terminales es el siguiente: "+ vN,debug_level);
	ProductionT2Set nuevaP = (ProductionT2Set)gt2.getP();

	Symbol Ak;	
	Object currProdsA[];
	
	for(int k = vN.size() - 2; k >= 0 ; k--){
	    showStatus("\nRevisando el simbolo ["+k+"] => "+ (Symbol)vN.get(k),debug_level);
	    Ak = (Symbol)vN.get(k);
	    currProdsA = getProductionsWithAntecedent(new Str((Symbol)vN.get(k)),nuevaP).toArray();
	    for(int i = 0 ; i < currProdsA.length ; i++){
		showStatus("\n\tChecando la producción => "+((Production)currProdsA[i]),debug_level);
		Str cons = ((Production)currProdsA[i]).getConsecuente();
		Symbol symbol0 = cons.getFirst();
		if(! gt2.getT().contains(symbol0)){
		    if(vN.indexOf(symbol0) > k){
			showStatus("\n\t El índice del primer símbolo ["+vN.indexOf(symbol0)+"] es mayor que [k,"+k+"] a fusionar",debug_level);
			// Tenemos que fusionar las producciones
		nuevaP=fusionDeProducciones(nuevaP,(ProductionT2)currProdsA[i],getProductionsWithAntecedent(new Str(symbol0),nuevaP));
		    }
		}
	    }
	}
	gt2.setP(nuevaP);
	showStatus("\n\t La nueva P="+nuevaP,debug_level);
	return gt2;	
    }
    
    protected Gtipo2 eliminaRecursionIzquierda(Gtipo2 gt2)throws StrIndexOutOfBoundsException{
	Object []arA =  getSymbolsConRecursivasIzquierdas((ProductionT2Set)gt2.getP()).toArray();
	ProductionT2Set nuevaP  = new ProductionT2Set();
	nuevaP.addAll(gt2.getP());
	
	for(int i = 0 ; i < arA.length; i++)
	    nuevaP = eliminaRecursionIzquierda(nuevaP,(Symbol)arA[i],gt2.getN(),gt2.getT());	    
	gt2.setP(nuevaP);
	return gt2;
    }
     /** 
      * Elimina la recursión izquieda generada por el símbolo no terminal dado.
      *
      * @param P El conjunto de producciones que contiene a las producciones con recursión izquierda generada por el símbolo dado.
      * @param symbol El símbolo que genera producciones recursivas izquierdas .
      * @return El conjunto de producciones tipo 2 sin las producciones con recursión izquierda generada por el símbolo dado  .
      *
      */
    public ProductionT2Set eliminaRecursionIzquierda(ProductionT2Set P, Symbol symbol, Alphabet N, Alphabet T){
	showStatus("\n\t\t\teliminaRecursionIzquierda("+P+","+symbol+","+N+","+T+")",debug_level);
	/// Todas las producciones que comienzan con el symbol symbol y que sabemos que tienen recursión izquierda  [1]
	ProductionT2Set prodsWithAntSymbol = getProductionsWithAntecedent(new Str(symbol),P);

	// Solo las producciones recursivaz izquierdas [2]
	ProductionT2Set prodsRecIzq =  getProduccionesConRecursionIzquierda(prodsWithAntSymbol);

	// Las producciones sin recursion izquierda y con antecedente igual a symbol  [3]
	ProductionT2Set prodsSinRecIzq = new ProductionT2Set();
	prodsSinRecIzq.addAll(prodsWithAntSymbol);
	prodsSinRecIzq.removeAll(prodsRecIzq);
	showStatus("\n\t\t\tA eliminar:"+prodsRecIzq,debug_level);
	// En este vector tenemos los consecuentes de [3] 
	Vector vconsecuentesSinRecIzq = new Vector();
	Object prodsSinRecIzqA[] = prodsSinRecIzq.toArray();
	for(int i = 0; i < prodsSinRecIzqA.length ; i++)
	    vconsecuentesSinRecIzq.add(((ProductionT2)prodsSinRecIzqA[i]).getConsecuente());	

	// Ahora si comenzamos a explorar las producciones recursivas izquierdas [2]
	ProductionT2 currProd;
	Str currCons, currAnt, restoCons;	
	//Un nuevo símbolo no terminal
	Symbol Bk;
	Str BkStr;	
	Object prodsRecIzqA[] = prodsRecIzq.toArray();
	for(int i = 0 ; i < prodsRecIzqA.length; i++){
	    currProd = (ProductionT2)prodsRecIzqA[i];
	    currAnt = currProd.getAntecedente();
	    currCons = currProd.getConsecuente();
	    // alpha
	    restoCons = currCons.substring(1);
	    
	    Bk = genNewSymbol(N.union(T), "ERI");
	    BkStr = new Str(Bk);	    
	    N.add(Bk);
	    P.add(new ProductionT2(BkStr,restoCons));
	    showStatus("\n\t\t\tAgregando: "+new ProductionT2(BkStr,restoCons),debug_level);
	    P.add(new ProductionT2(BkStr,Str.concat(BkStr,restoCons)));
	    showStatus("\n\t\t\tAgregando: "+new ProductionT2(BkStr,Str.concat(restoCons,BkStr)),debug_level);
	    P.remove(currProd);
	    showStatus("\n\t\t\tEliminando:"+currProd,debug_level);
	    
	    for(int j = 0 ; j < vconsecuentesSinRecIzq.size(); j++){		
		showStatus("\n\t\t\tAgregando: "+new ProductionT2(currAnt,Str.concat((Str)vconsecuentesSinRecIzq.get(j),BkStr)),debug_level);
		P.add(new ProductionT2(currAnt,Str.concat((Str)vconsecuentesSinRecIzq.get(j),BkStr)));
	    }
	}
	return P;
    }

     /** 
      * Obtiene producciones con recursión izquierda, de un conjunto dado
      *
      * @param P  el conjunto de producciones tipo2 de donde obtendremos las producciones recursivas izquierdas
      * @return Un subconjunto del conjunto de producciones tipo 2 dado, donde solo están las producciones tipo2 recursivas izquierdas
      *
      */
    public ProductionT2Set getProduccionesConRecursionIzquierda(ProductionT2Set P){
	ProductionT2Set result = new ProductionT2Set();
	Str currAnt, currCons;
	Object pA[] = P.toArray();
	for(int i =0 ; i < pA.length; i++)
	    if(((ProductionT2)pA[i]).getConsecuente().length() >= 1
	       && ((ProductionT2)pA[i]).getAntecedente().getFirst().equals(((ProductionT2)pA[i]).getConsecuente().getFirst()))
		result.add((ProductionT2)pA[i]);
	return result;	
    }

     /** 
      * Realiza la fusi'on de las producciones indicadas.
      *
      * @param P El conjunto de producciones donde realizaremos la fusión.
      * @param prodToFusion Esta es la produccion que vamos a fusionar y que será eliminada.
      * @param prodsWithConsecuentToFussion El conjunto de producciones que tienen el Antecendete que queremos fusionar y cuyo consecuente pondremos en el lugar de consecuente de la produccción <code>prodToFusion</code>.
      * @return El conjunto de producciones con la fusion realizada.
      */
    public ProductionT2Set fusionDeProducciones(ProductionT2Set P, ProductionT2 prodToFusion,
						 ProductionT2Set prodsWithConsecuentToFussion ){
	showStatus("\n\n\t\tfusionDeProducciones("+P+","+prodToFusion+","+prodsWithConsecuentToFussion+")",debug_level);

	ProductionT2Set result = new ProductionT2Set();
	result.addAll(P);
	result.remove(prodToFusion);
	showStatus("\n\t\tEliminando: "+ prodToFusion,debug_level);
	    
	Str antecedente = prodToFusion.getAntecedente();
	Str nuevoConsecuentePostFijo = prodToFusion.getConsecuente();
	nuevoConsecuentePostFijo.removeFirst();
	
	Object prodsWithConsecuentToFussionA[] = prodsWithConsecuentToFussion.toArray();
	for(int i = 0 ; i < prodsWithConsecuentToFussionA.length ; i++){
	    showStatus("\n\t\tAgregando: "+ new ProductionT2(antecedente,
					Str.concat(((ProductionT2)prodsWithConsecuentToFussionA[i]).getConsecuente(),
						   nuevoConsecuentePostFijo))
		       ,debug_level);
	    result.add(new ProductionT2(antecedente,
					Str.concat(((ProductionT2)prodsWithConsecuentToFussionA[i]).getConsecuente(),
						   nuevoConsecuentePostFijo)));
	}
	showStatus("\n\t\tLas producciones resultantes son: "+result+"\n",debug_level);
	return result;	
    }
    
    
     /** 
      * regresa un alfabeto subconjunto de N (no terminales) que tienen producciones recursivas izquierdas.
      *
      * @param P El conjunto de producciones tipo 2 del cual queremos ver que simbolos no terminales tienen producciones recursivas izquierdas       
      * @return Un subconjunto de N, los cuales tienen producciones recursivas izquiedas   
      *
      */
    public Alphabet getSymbolsConRecursivasIzquierdas(ProductionT2Set P)throws StrIndexOutOfBoundsException{
	Alphabet result = new Alphabet();
	Str currAnt, currCons;
	Object pA[] = P.toArray();
	for(int i =0 ; i < pA.length; i++){
	    currAnt = ((ProductionT2)pA[i]).getAntecedente();
	    currCons = ((ProductionT2)pA[i]).getConsecuente();
	    if(currCons.length() >= 1 && currAnt.getFirst().equals(currCons.getFirst()))
		result.add(currAnt.getFirst());
	}
	return result;	
    }
    
    
    /**
     ** Genera un nuevo simbolo que no este presente en ninguno de los conjuntos especificados
     ** @param A el alfabeto donde no queremos que este el nuevo símbolo
     ** @return un nuevo símbolo que no este en A
     **/    
    public Symbol genNewSymbol(Alphabet A, String prefix){
	Symbol newS;
	newS= new Symbol(prefix);
	if(!A.contains(newS))
	    return newS;	
	Random r = new Random();
	int counter = 0;
	do{
	    if((counter%2) == 0)
		newS = new Symbol("_"+prefix+"_"+r.nextInt(1000)+"_",false);	    
	    else newS = new Symbol("-"+prefix+"-"+r.nextInt(1000)+"-",false);
	}while(A.contains(newS));
	return newS;
    }
    
    /**
     ** Genera un nuevo simbolo que no este presente en ninguno de los conjuntos especificados
     ** @param A el alfabeto donde no queremos que este el nuevo símbolo
     ** @return un nuevo símbolo que no este en A
     **/    
    public Symbol genNewSymbol(Alphabet A){
	return genNewSymbol(A,"JNS");	
    }

	
     
    /**
     ** Dado un conjunto de producciones regresa un subconjunto de producciones unitarias
     ** @param  P el conjunto de producciones de donde buscaremos las unitarias 
     **/
    public ProductionT2Set getProduccionesUnitarias(ProductionT2Set P){
	return getProduccionesConsecuenteLength(1,P);	
    }

    public ProductionT2Set getProduccionesConsecuenteLength(int consecuenteLength, ProductionT2Set P){
	Object pArray[] = P.toArray();
	ProductionT2Set result = new ProductionT2Set();
	for(int i = 0; i < pArray.length ; i++)
	    if((((ProductionT2)pArray[i]).getAntecedente().length() == 1) &&
	       (((ProductionT2)pArray[i]).getConsecuente().length() == consecuenteLength))
		result.add((ProductionT2)pArray[i]);
	return result;
    }
    
    /**
     * Este regresa un conjunto de producciones que se generan a partir de los nulificables
     */
    public ProductionT2Set getCombinacionesDeConsecuente(ProductionT2 P, Alphabet nulificables)
	throws StrIndexOutOfBoundsException{
	ProductionT2Set result = new ProductionT2Set();
	Str currentConsecuente = P.getConsecuente();
	Alphabet noNulificables = currentConsecuente.getAlphabetMinimo().minus(nulificables);
	HashSet combinacionesConsecuente = generaCombinaciones(currentConsecuente);
	Object combinacionesArray[] = combinacionesConsecuente.toArray();
	for(int i = 0 ; i < combinacionesArray.length ; i ++)
	    if(((Str)combinacionesArray[i]).containsSymbols(noNulificables))
		result.add(new ProductionT2(P.getAntecedente(),(Str)combinacionesArray[i]));
	return result;
    }
    
    /**
     ** Genera todas las posibles combinaciones de la cadena s, todas como se har'ian si todos los simbolos que
     ** forman <code>s</code> fuesen nulificables. Una vez que tenemos esto otro proceso decide cuales se quedan.
     **/
    public HashSet generaCombinaciones(Str s){
	int len = s.length();
	HashSet v = new HashSet();
	if(len == 1){
	    v.add(s); 
	}else{	    
	    try{
		Str tmp;
		for(int i = 0 ; i < len ; i++){
		    tmp = new Str();
		    for(int j = 0 ; j < len; j++)
			if(i != j){
			    tmp.append(s.getSymbol(j));
			}
		    v.add(tmp);
		    v.addAll(generaCombinaciones(tmp));
		}
	    }catch( StrIndexOutOfBoundsException ouch){
		System.err.println("["+(new java.util.Date()).toString()+"] " ); 
		ouch.printStackTrace();
		System.exit(1);
	    }
	}
	return v;
    }
    
    /**
     * Regresa un conjunto de simbolos (<code>Alphabet</code>) que cumple con la siguiente condición<br>
     * <code>
     * {B | B -> alpha para alguna alpha in A^{*} }
     * </code>
     *
     * Esta función la usaremos para obtener el conjunto de no terminales cuyo consecuente esta en A^{*}.
     * Esta funcion es muy útil, en particular, para la implementación de símbolos muertos.
     * @param P el conjunto de producciones original sobre el que trabajaremos
     * @param A el alfabeto sobre el cual queremos que esten los consecuentes
     * @return un alfabeto con todos los antecedentes cuyo consecuente esté en A^{*}
     */
    public Alphabet getAntecedentWithConsecuentInAlphabet(ProductionSet P, Alphabet A){
	Alphabet result = new Alphabet();
	Object pArray[] = P.toArray();
	for(int i = 0 ; i < pArray.length ; i++)
	    if(A.strOnAlphabet(((ProductionT2)pArray[i]).getConsecuente()))
		result.add(((ProductionT2)pArray[i]).getAntecedente().getFirst());
	return result;
    }

    /**
     * Regresa un conjunto de producciones cuyos símbolos están en A.
     *
     * Checa que ambos, antecedente y consecuente, estén sobre el alfabeto A
     * @param P el conjunto de producciones original sobre el que trabajaremos
     * @param A el alfabeto sobre el cual queremos que esten los consecuentes y antecedentes
     * @return un conjunto de producciones cuyos antecedentes y consecuentes estén sobre el alfabeto A
     */
    public ProductionT2Set getProductionsWithAntecedenteConsecuentInAlphabet(ProductionSet P, Alphabet A){    
	ProductionT2Set result = new ProductionT2Set();
	Object pArray[] = P.toArray();

	for(int i = 0 ; i < pArray.length ; i++)
	    if(A.strOnAlphabet(((ProductionT2)pArray[i]).getConsecuente()) &&
	       A.strOnAlphabet(((ProductionT2)pArray[i]).getAntecedente()))
		result.add((ProductionT2)pArray[i]);
	return result;
    }


    /**
     * Regresa un conjunto de producciones del conjunto dado con todas las que tengan el antecedente igual al antecedente dado
     * @param antecedente el antecedente que queremos casar
     * @param P el conjunto de producciones de donde vamos a buscar el patron
     **/ 
    public ProductionT2Set getProductionsWithAntecedent(Str antecedente, ProductionSet P){
	ProductionT2Set result  = new ProductionT2Set();
	Object pArray[] = P.toArray();
	for(int i = 0; i < pArray.length ; i++)
	    if(antecedente.equals(((ProductionT2)pArray[i]).getAntecedente()))
		result.add((ProductionT2)pArray[i]);
	return result;
    }

    /**
     * Regresa un conjunto de producciones del conjunto dado con todas las que tengan el consecuente igual al consecuente dado
     * @param consecuente el consecuente que queremos casar
     * @param P el conjunto de producciones de donde vamos a buscar el patron
     **/ 
    public ProductionT2Set getProductionsWithConsecuente(Str consecuente, ProductionSet P){
	ProductionT2Set result  = new ProductionT2Set();
	Object pArray[] = P.toArray();
	for(int i = 0; i < pArray.length ; i++)
	    if(consecuente.equals(((Production)pArray[i]).getConsecuente()))
		result.add((Production)pArray[i]);
	return result;
    }

    /**
     * Hace lo contrario que <code>getProductionsWithConsecuente</code> cuya explicación se anexa
     * 
     * Regresa un conjunto de producciones del conjunto dado con todas las que tengan el consecuente igual al consecuente dado
     * @param consecuente el consecuente que queremos casar
     * @param P el conjunto de producciones de donde vamos a buscar el patron
     **/ 
    public ProductionT2Set getProductionsWithoutConsecuente(Str consecuente, Str except, ProductionSet P){
	ProductionT2Set result  = new ProductionT2Set();
	Object pArray[] = P.toArray();
	for(int i = 0; i < pArray.length ; i++)
	    if(!consecuente.equals(((Production)pArray[i]).getConsecuente()))
		result.add((Production)pArray[i]);
	    else if(except.equals(((Production)pArray[i]).getAntecedente()))
		result.add((Production)pArray[i]);
	return result;
    }
    

    /**
     * Regresa un conjunto de no terminales del conjunto de producciones dado con todas las que tengan el consecuente igual al consecuente dado
     * @param consecuente el consecuente que queremos casar
     * @param P el conjunto de producciones de donde vamos a buscar el patron
     **/ 
    public Alphabet getNoTerminalesWithConsecuente(Str consecuente, ProductionSet P){
	Alphabet result  = new Alphabet();
	Object pArray[] = P.toArray();
	for(int i = 0; i < pArray.length ; i++)
	    if(consecuente.equals(((Production)pArray[i]).getConsecuente()))
		result.add(((Production)pArray[i]).getAntecedente().getFirst());
	return result;
    }

    /**
     * Obtiene todos los no terminales que son nulificables dado un conjunto simbolos de nulificables 
     * @param P el conjunto de producciónes.
     * @param NulifV
     * @param antecedenteS
     */
    public Alphabet getNulif(ProductionSet P, Alphabet NulifV, Str antecedenteS){
	try{ 

	    Alphabet result = new Alphabet();
	    Object pArray[] = P.toArray();
	    Str epsilon = new Str();
	    for(int i = 0; i < pArray.length ; i++){
		if(((Production)pArray[i]).getConsecuente().equals(epsilon) &&
		   (! ((Production)pArray[i]).getAntecedente().equals(antecedenteS)))
		    result.add(((Production)pArray[i]).getAntecedente().getFirst());
		else{
		    Str currentCons = ((Production)pArray[i]).getConsecuente();
		    boolean allNulif = true;
		    for(int j = 0 ; j < currentCons.length() && allNulif ; j++)
			if(! NulifV.contains(currentCons.getSymbol(j)))
			    allNulif = false;
		    if(allNulif)
			result.add(((Production)pArray[i]).getAntecedente().getFirst());
		}
	    }
	    return result;	

	}catch(StrIndexOutOfBoundsException ouch){
	    System.err.println("["+(new java.util.Date()).toString()+"]  te pasaste en el indice: " ); 
	    ouch.printStackTrace(); 

	    System.exit(1);
	    return null;
	}
    }

    /**
     ** Regresamos un conjunto de producciones cuyos antecedentes sean <str><sym>Ai</sym></str> para cada Ai \in A
     ** @param P el conjunto de producciones de donde obtendremos un subconjunto de producciones
     ** @param A el alfabeto de donde tomaremos los consecuentes
     */
    public ProductionT2Set getProductionSetWithAntecedenteInNoTerminalSet(ProductionSet P, Alphabet A){
	Object [] aArray = A.toArray();
	Object [] pArray = P.toArray();
	ProductionT2Set result = new ProductionT2Set();
	for(int i = 0 ; i < aArray.length ; i++){
	    Str currentAntecedente = new Str((Symbol)aArray[i]);
	    for(int j = 0 ; j < pArray.length ; j++){
		if(((Production)pArray[j]).getAntecedente().equals(currentAntecedente))
		    result.add((Production)pArray[j]);
	    }
	}
	return result;
    }

    /** Regresa un conjunto de producciones que contienen en su consecuente símbolos nulificables dados en el Alphabet <code>A</code>
     *@param P el conjunto de producciones de donde tomaremos las que cumplan en contener un simbolo del Alphabet <code>A</code> en su consecuente
     *@param A el conjunto de simbolos no terminales nulificables
     *@return un <code>ProductionT2Set</code> con todas las producciones cuyo consecuente contenga al menos un  símbolo del  Alphabet <code>A</code>
     */
    public ProductionT2Set getProductionsWithSomeSymbolInConsecuente(ProductionSet P, Alphabet A){
	Object [] aArray = A.toArray();
	Object [] pArray = P.toArray();
	ProductionT2Set result = new ProductionT2Set();
	for(int i = 0 ; i < aArray.length ; i++)
	    for(int j = 0 ; j < pArray.length ; j++)
		if(((Production)pArray[j]).getConsecuente().containsSymbol((Symbol)aArray[i]))
		    result.add((Production)pArray[j]);
	return result;
    }

}

/* Simplifier.java ends here. */
