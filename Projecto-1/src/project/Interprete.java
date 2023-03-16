import java.util.Hashtable;
import java.util.StringTokenizer;


public class Interprete {

	// Se declaran las variables
	Prompt prompt;
	Lista listaOperacionesHaImplementar;
	Lista listaDePred;
	Hashtable<String,Atom> variablesDelUsuario = new Hashtable<String,Atom>();
	Hashtable<String,Atom> funcionesDelUsuario = new Hashtable<String,Atom>();

	/**
	 * Las diferentes definiciones a utilizar
	 */
	public Interprete(){
		definirOperacionesImplementadas();
		definirPred();
	}

	/**
	 * Constructor de  definiciones 
	 */
	private void definirPred() {
		this.listaDePred = new Lista();
		this.listaDePred.AgregarAlFinal(new Atom("="));
        this.listaDePred.AgregarAlFinal(new Atom("<"));
		this.listaDePred.AgregarAlFinal(new Atom("<="));
		this.listaDePred.AgregarAlFinal(new Atom(">"));
		this.listaDePred.AgregarAlFinal(new Atom(">="));
		this.listaDePred.AgregarAlFinal(new Atom("/="));
	
	}


	/**
	 * Operaciones a implementar en Lisp
	 */
	private void definirOperacionesImplementadas() {

		this.listaOperacionesHaImplementar = new Lista();
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("car"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("cdr"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("cons"));
        this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("+"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("*"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("-"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("/"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("equal"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("atom"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("cond"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("list"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("="));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("/="));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("<"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("<="));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom(">"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom(">="));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("defun"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("list-length"));
		this.listaOperacionesHaImplementar.AgregarAlFinal(new Atom("setq"));
	
	}

	/**
	 * @param expresionAParsear 
	 * @param estaDefiniendoUnaLista 
	 * @param esClausula
	 * @return 
	 */
	public Atom parsearExpresion(String expresionAParsear,boolean estaDefiniendoUnaLista,boolean esClausula){

		expresionAParsear = expresionAParsear.trim();

		if (expresionAParsear.compareTo("")==0)
			return new Atom();

		if (!this.estaBienBalanceada(expresionAParsear))
			return new Atom();

		Atom atomoDeRespuesta = new Atom();

		StringTokenizer separador = new StringTokenizer(expresionAParsear);
		
		String primerToken = separador.nextToken();
		Atom atomoIngresado = new Atom(primerToken);

		if ((separador.countTokens()==0)){

			if ((this.variablesDelUsuario.containsKey(primerToken)) && (!estaDefiniendoUnaLista))
				return this.variablesDelUsuario.get(primerToken);
			
			if ((primerToken.compareTo("'()")==0) || ((primerToken.compareTo("()")==0)))
				return new Atom();

			if (primerToken.charAt(0)=='\''){
				if ((primerToken.charAt(1)!='(') && (primerToken.charAt(primerToken.length()-1)!=')'))
					return new Atom(primerToken.substring(1));
			}
		}

		if ((new Atom(primerToken).comienzaCon("'("))){
			estaDefiniendoUnaLista = true;
			atomoDeRespuesta = new Atom(new Lista());
			atomoDeRespuesta.list.esOperacion = false;

			int desdeDondeCortar = expresionAParsear.indexOf('(') + 1;
			int hastaDondeCortar = this.obtenerIndiceDelParentesisQueCierraAlPrimeroEn(expresionAParsear);

			if ((hastaDondeCortar==expresionAParsear.lastIndexOf(')')) && (!(hastaDondeCortar==expresionAParsear.length()-1)))
				return new Atom();
			
			expresionAParsear = expresionAParsear.substring(desdeDondeCortar, hastaDondeCortar);			
		} else if (primerToken.charAt(0)=='('){

			int desdeDondeCortar = expresionAParsear.indexOf('(') + 1;
			int hastaDondeCortar = this.obtenerIndiceDelParentesisQueCierraAlPrimeroEn(expresionAParsear);
			
			expresionAParsear = expresionAParsear.substring(desdeDondeCortar,hastaDondeCortar);
			atomoDeRespuesta = new Atom(new Lista());
			
			
			separador = new StringTokenizer(expresionAParsear);
			String primerTokenDeLaLista = separador.nextToken();
			
			boolean esFuncion = (esOperacionImplementada(primerTokenDeLaLista)) || this.funcionesDelUsuario.containsKey(primerTokenDeLaLista);
			
			if ((!estaDefiniendoUnaLista) && (!esFuncion)){

				return new Atom();
			}	
			
			if (estaDefiniendoUnaLista){
				if (!esClausula)
					atomoDeRespuesta.list.esOperacion = false;
				else
					atomoDeRespuesta.list.esOperacion = true;
			}else {
				atomoDeRespuesta.list.esOperacion = true;
			}
		}		

		if (!atomoDeRespuesta.islist())
			return new Atom(expresionAParsear);
		
		separador = new StringTokenizer(expresionAParsear);
		
		while (separador.hasMoreTokens()) {
			Atom atomoActual = new Atom(separador.nextToken());

			if ((atomoActual.comienzaCon("'(")) || (atomoActual.comienzaCon("("))){

				int desdeDondeCortar = expresionAParsear.indexOf(atomoActual.toString());
				expresionAParsear = expresionAParsear.substring(desdeDondeCortar);
				int hastaDondeCortar = this.obtenerIndiceDelParentesisQueCierraAlPrimeroEn(expresionAParsear);
				String expresionDeLaListaInterna = expresionAParsear.substring(0, hastaDondeCortar+1);
				Atom atomoConLaListaInterna = new Atom();
				
				boolean esDefun = false;
				boolean esCOND = false;
				
				if (atomoActual.comienzaCon("'("))
					atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true, esClausula);
				else {
					if (atomoDeRespuesta.list.size() > 0){
						if (atomoDeRespuesta.islistwithoperation()){
							String operacionDeLaLista = atomoDeRespuesta.list.getOperacion().toString();

							if ((operacionDeLaLista.compareToIgnoreCase("defun")==0) && (atomoDeRespuesta.list.size()==2)){
								atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true,false);
								esDefun = true;
							}

							if (operacionDeLaLista.compareToIgnoreCase("cond")==0){
								esCOND = true;
								atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, true,true);
							}
						}
					}
					
					if ((!esDefun) && (!esCOND))
						atomoConLaListaInterna = this.parsearExpresion(expresionDeLaListaInterna, estaDefiniendoUnaLista, esClausula);
					
				}
				
				atomoDeRespuesta.list.AgregarAlFinal(atomoConLaListaInterna);
				String expresionDespuesDeLaListaInterna = expresionAParsear.substring(hastaDondeCortar + 1);
				expresionAParsear = expresionDespuesDeLaListaInterna;
				
				if (!esDefun){

					separador = new StringTokenizer(expresionDespuesDeLaListaInterna);
				} else {

					String nombreDeLaFuncion = atomoDeRespuesta.list.getAtomoEn(1).toString();
					this.funcionesDelUsuario.put(nombreDeLaFuncion,new Atom());
					
					Atom atomoConLaOperacion = this.parsearExpresion(expresionAParsear.trim(), false,false);
					atomoDeRespuesta.list.AgregarAlFinal(atomoConLaOperacion);
					separador = new StringTokenizer("");
					
					this.funcionesDelUsuario.remove(nombreDeLaFuncion);
				}
			} else{

				int desdeDondeCortar = expresionAParsear.indexOf(atomoActual.toString());
				expresionAParsear = expresionAParsear.substring(desdeDondeCortar);
				
				if (atomoActual.comienzaCon("'"))

					atomoActual = new Atom(atomoActual.toString().substring(1));
					
				atomoDeRespuesta.list.AgregarAlFinal(atomoActual);
			}
		}

		return atomoDeRespuesta;
	}

	private boolean esOperacionImplementada(String posibleOperacion){
		return this.listaOperacionesHaImplementar.existe(new Atom(posibleOperacion));
	}

	/**
	 * @param posibleOperacion 
	 * @return 
     * 

	/**
	 * @param expresion 
	 * @return 
	 */
	public boolean estaBienBalanceada(String expresion) {
			int parentesisAbiertos = 0;
			int parentesisCerrados = 0;

			for (int i=0 ; i < expresion.length() ; i++){
				if (expresion.charAt(i) == '(')
					parentesisAbiertos++;
				else if (expresion.charAt(i) == ')')
					parentesisCerrados++;
			}
			
			return parentesisAbiertos == parentesisCerrados;
		}


	/**
	 * @param atomoAEvaluar parametro tipo atomo, a evaluar
	 * @return el atomo de respuesta
	 */
	public Atom evaluar(Atom atomoAEvaluar){
		Atom AtomoDeRespuesta = new Atom();

		if (atomoAEvaluar.isnull)
			return new Atom();
		
		if (atomoAEvaluar.islist()){

			Lista listaevaluar = atomoAEvaluar.list;
			
			if (listaevaluar.esOperacion){

				String operacionDeLaLista = listaevaluar.getOperacion().toString(); 
				if ((operacionDeLaLista.compareToIgnoreCase("defun")!=0) && (operacionDeLaLista.compareToIgnoreCase("cond")!=0)) 
					for (int i=0 ; i < listaevaluar.size() ; i++){
						if (listaevaluar.getAtomoEn(i).islistwithoperation()){
							Atom listaEvaluada = this.evaluar(listaevaluar.getAtomoEn(i));
							listaevaluar.remplazarEn_Por(i, listaEvaluada);
						} else if (this.variablesDelUsuario.containsKey(listaevaluar.getAtomoEn(i).toString())){
							Atom valorDelAtomo = this.variablesDelUsuario.get(listaevaluar.getAtomoEn(i).toString());						

							if (listaevaluar.esOperacion){
								if (!((i==1) && (listaevaluar.getOperacion().toString().compareTo("setq")==0)))
									listaevaluar.remplazarEn_Por(i, valorDelAtomo);
							} else 
								listaevaluar.remplazarEn_Por(i, valorDelAtomo);	
						}
					}								
			} else {
				return new Atom(listaevaluar);
			}

			String operacion = listaevaluar.getOperacion().toString();
			if (this.esPredicado(operacion)){
				return this.evaluarPredicado(listaevaluar);
			} else if (operacion.compareToIgnoreCase("cdr")==0){
				return this.cdr(listaevaluar);
			} else if (operacion.compareToIgnoreCase("car")==0){
				return this.car(listaevaluar);
			} else if (operacion.compareToIgnoreCase("list-length")==0){
				return this.list_length(listaevaluar);
			} else if (operacion.compareToIgnoreCase("cons")==0){
				return this.cons(listaevaluar);
			} else if (operacion.compareToIgnoreCase("+")==0){
				return this.sumar(listaevaluar);
			} else if (operacion.compareToIgnoreCase("*")==0){
				return this.multiplicar(listaevaluar);
			} else if (operacion.compareToIgnoreCase("-")==0){
				return this.restar(listaevaluar);
			} else if (operacion.compareToIgnoreCase("/")==0){
				return this.dividir(listaevaluar);
			} else if (operacion.compareToIgnoreCase("defun")==0){
				return this.defun(listaevaluar);
			} else if (this.funcionesDelUsuario.containsKey(operacion)){
				return this.operarFuncionDelUsuario(listaevaluar);
			} else if (operacion.compareToIgnoreCase("equal")==0) {
				return this.equal(listaevaluar);
			} else if (operacion.compareToIgnoreCase("setq")==0) {
				return this.setq(listaevaluar);
			} else if (operacion.compareToIgnoreCase("atom")==0){
				return this.type_of(listaevaluar);
			} else if (operacion.compareToIgnoreCase("cond")==0){
				return this.cond(listaevaluar);
			} else if (operacion.compareToIgnoreCase("list")==0){
				return this.list(listaevaluar);
			}
		} else{

			if (this.variablesDelUsuario.containsKey(atomoAEvaluar.toString()))
				AtomoDeRespuesta = this.variablesDelUsuario.get(atomoAEvaluar.toString());
			else

				AtomoDeRespuesta = atomoAEvaluar;	
		}
		
		return AtomoDeRespuesta;
	}

	/**
	 * @param listaevaluar 
	 * @return
	 */
	private Atom list(Lista listaevaluar) {
		Lista lista = new Lista();
		
		for(int i = 1; i < listaevaluar.size(); i++) {
			lista.AgregarAlFinal(this.evaluar(listaevaluar.getAtomoEn(i)));
		}
		
		return new Atom(lista);
	}

	/**
	 * @param listaevaluar recibe de parametro la lista a evaluar
	 * @return retorna el atomo
	 */
	private Atom evaluarPredicado(Lista listaevaluar){
		Atom atomoDeRespuesta = new Atom();
		
		String predicadoAEvaluar = listaevaluar.getAtomoEn(0).toString();
		if (!this.esPredicado(predicadoAEvaluar))
            return new Atom();
		if (predicadoAEvaluar.compareTo("=")==0){
			if (!(listaevaluar.size() > 1))
                return new Atom();
			boolean sonIguales = true;
			for (int i = 1 ; i < listaevaluar.size()-1 ; i++){
				sonIguales = listaevaluar.getAtomoEn(i).equals(listaevaluar.getAtomoEn(i+1));
				
				if (!sonIguales)
					return new Atom(sonIguales);
					
			}

			return new Atom(sonIguales);
		} else if (predicadoAEvaluar.compareTo("/=")==0){
			if (!(listaevaluar.size() > 1))
				return new Atom();
			
			boolean sonDiferentes = true;
			
			for (int i = 1 ; i < listaevaluar.size()-1 ; i++){
				sonDiferentes = !listaevaluar.getAtomoEn(i).equals(listaevaluar.getAtomoEn(i+1));
				
				if (!sonDiferentes)
					return new Atom(false);
			}
			return new Atom(sonDiferentes);
		} else if (predicadoAEvaluar.compareTo("<")==0){
			if (!(listaevaluar.size() > 1))
				return new Atom();
			
			boolean esMenorQue = true;
			
			for (int i = 1; i <listaevaluar.size()-1 ; i++){
				
				if (!(listaevaluar.getAtomoEn(i).isnumber()))
					return new Atom();
				
				esMenorQue = listaevaluar.getAtomoEn(i).getnumber() < listaevaluar.getAtomoEn(i+1).getnumber();
				
				if (!esMenorQue)
					return new Atom (false);
			}
			
			return new Atom(esMenorQue);
		} else if (predicadoAEvaluar.compareTo("<=")==0){
			if (!(listaevaluar.size() > 1))
				return new Atom();
			
			boolean esMenorIgualQue = true;
			
			for (int i = 1; i <listaevaluar.size()-1 ; i++){
				if (!(listaevaluar.getAtomoEn(i).isnumber()))
                    return new Atom();
				
				esMenorIgualQue = listaevaluar.getAtomoEn(i).getnumber() <= listaevaluar.getAtomoEn(i+1).getnumber();
				if (!esMenorIgualQue)
					return new Atom (false);
			}
			return new Atom(esMenorIgualQue);
		} else if (predicadoAEvaluar.compareTo(">")==0){
			if (!(listaevaluar.size() > 1))
                return new Atom();
			
			boolean esMayorQue = true;
			
			for (int i = 1; i <listaevaluar.size()-1 ; i++){
				
				if (!(listaevaluar.getAtomoEn(i).isnumber()))
                    return new Atom();
				
				esMayorQue = listaevaluar.getAtomoEn(i).getnumber() > listaevaluar.getAtomoEn(i+1).getnumber();
				
				if (!esMayorQue)
					return new Atom (false);
			}
			
			return new Atom(esMayorQue);
		} else if (predicadoAEvaluar.compareTo(">=")==0){
			if (!(listaevaluar.size() > 1))
                return new Atom();
			
			boolean esMayorIgualQue = true;
			
			for (int i = 1; i <listaevaluar.size()-1 ; i++){
				if (!(listaevaluar.getAtomoEn(i).isnumber()))
                    return new Atom();
				
				esMayorIgualQue = listaevaluar.getAtomoEn(i).getnumber() >= listaevaluar.getAtomoEn(i+1).getnumber();
				if (!esMayorIgualQue)
					return new Atom (false);
			}
			return new Atom(esMayorIgualQue);
		}

        return new Atom();
	}

	/**
	 * @param operacion 
	 * @return 
	 */
	private boolean esPredicado(String operacion) {

		return this.listaDePred.existe(new Atom(operacion));
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom cond(Lista listaevaluar){
		if (listaevaluar.size()<2)
			return new Atom();

		int revisandoSubListaIndex = 1;
		boolean haEncontradoRespuesta = false;
		Atom atomoDeRespuesta = new Atom();
		
		while ((revisandoSubListaIndex < listaevaluar.size()) && (!haEncontradoRespuesta)){

			if (!listaevaluar.getAtomoEn(revisandoSubListaIndex).islist()) 
				return new Atom();
			else {
				
			}
			
			Lista evaluandoSubLista = listaevaluar.getAtomoEn(revisandoSubListaIndex).list;

			Atom primerAtomo = evaluandoSubLista.getAtomoEn(0);

			if (!((atomoDeRespuesta.isnull) && (revisandoSubListaIndex==listaevaluar.size()-1))) {
				if ((!primerAtomo.islist()) && (!this.variablesDelUsuario.containsKey(primerAtomo.toString())))
					return new Atom();
			}
			
			Atom primerAtomoEvaluado = this.evaluar(primerAtomo);

			if (!primerAtomoEvaluado.isnull){
				haEncontradoRespuesta = true;
					
				if (evaluandoSubLista.size() > 1)

					atomoDeRespuesta = this.evaluar(evaluandoSubLista.getAtomoEn(evaluandoSubLista.size()-1));
				else

					atomoDeRespuesta = primerAtomoEvaluado; 
			} 
			
			revisandoSubListaIndex ++;
		}
		
		
		return atomoDeRespuesta;
	}
	/**
	 * @param listaevaluar 
	 * @return atomo
	 */
	private Atom type_of(Lista listaevaluar){
		if (listaevaluar.size()!=2)
			return new Atom();
		
		return new Atom(listaevaluar.getAtomoEn(1).getTipo());
	}

	/**
	 * @param listaevaluar 
	 * @return atomo
	 */
	private Atom equal(Lista listaevaluar){

		if (listaevaluar.size()!=3)
			return new Atom();

		return new Atom(listaevaluar.getAtomoEn(1).toString().compareTo(listaevaluar.getAtomoEn(2).toString())==0);
	}

	/**
	 * @param listaevaluar lista a evaluar tipo Lista
	 * @return atomo de la operacion
	 */
	private Atom operarFuncionDelUsuario(Lista listaevaluar){

		int numeroDeParametrosQueIngreso = listaevaluar.size()-1;
		
		Lista listaDeLosParametros = this.funcionesDelUsuario.get(listaevaluar.getOperacion().toString()).list.getAtomoEn(0).list;
		
		int numeroDeParametrosDeLaFuncion = listaDeLosParametros.size();
		
		if (numeroDeParametrosDeLaFuncion!=numeroDeParametrosQueIngreso)
			return new Atom();

		Hashtable<String,Atom> mapadeparametrosvalores = new Hashtable<String,Atom>();
		
		for (int i =1 ; i <= numeroDeParametrosQueIngreso ; i++){
			mapadeparametrosvalores.put(listaDeLosParametros.getAtomoEn(i-1).toString(), listaevaluar.getAtomoEn(i));
		}

		Atom atomoDeLaOperacion = this.funcionesDelUsuario.get(listaevaluar.getOperacion().toString()).list.getAtomoEn(1);
		
		if (atomoDeLaOperacion.islist()){
			Atom listaAEvaluarConParametrosMapeados = this.mapearParametrosEn(mapadeparametrosvalores, new Atom(atomoDeLaOperacion.list)); 
			return this.evaluar(listaAEvaluarConParametrosMapeados);
		} else
			return atomoDeLaOperacion;
		
	}
	/**
	 * @param mapadeparametrosvalores
	 * @param atomoEnDondeMapear 
	 * @return 
	 */
	private Atom mapearParametrosEn(Hashtable<String, Atom> mapadeparametrosvalores, Atom atomoEnDondeMapear){

		Lista listaevaluar = new Lista(atomoEnDondeMapear.list);
		listaevaluar.esOperacion = atomoEnDondeMapear.list.esOperacion;	

		for (int i= 0 ; i < listaevaluar.size(); i++){
			Atom atomoActual = listaevaluar.getAtomoEn(i);
			
			if (atomoActual.islist()){
				listaevaluar.remplazarEn_Por(i,this.mapearParametrosEn(mapadeparametrosvalores, atomoActual));
			} else{
				if (mapadeparametrosvalores.containsKey(atomoActual.toString()))
					listaevaluar.remplazarEn_Por(i,mapadeparametrosvalores.get(atomoActual.toString()));
			}
		}
		
		return new Atom(listaevaluar);
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom defun(Lista listaevaluar){

		if (listaevaluar.size()!=4)
			return new Atom();
		else{
			if (listaevaluar.getAtomoEn(1).islist())
				return new Atom();

			if (!listaevaluar.getAtomoEn(2).islist())
				return new Atom();	
			
		}
		String nombreDeLaFuncion = listaevaluar.getAtomoEn(1).toString();
		if (!esNombreDeFuncionValida(nombreDeLaFuncion))
			return new Atom();

		Atom atomoDeLaFuncion = new Atom(new Lista(listaevaluar.subList(2, 4)));
		this.funcionesDelUsuario.put(nombreDeLaFuncion, new Atom(new Lista(listaevaluar.subList(2, 4))));
		
		return new Atom(nombreDeLaFuncion);
	}

	/**
	 * @param nombreDeLaFuncion 
	 * @return 
	 */
	private boolean esNombreDeFuncionValida(String nombreDeLaFuncion){

		if (esOperacionImplementada(nombreDeLaFuncion))
			return false;

		String primerCaracter = Character.toString(nombreDeLaFuncion.charAt(0)); 
		try {
			int posibleNumero = Integer.parseInt(primerCaracter);
			return false;
		} catch(NumberFormatException noEmpiezaConNumero){
			return true;
		}
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom sumar(Lista listaevaluar) {
		float suma = 0;
		boolean todosSonEnteros = true;
		
		for (int i=1; i < listaevaluar.size() ; i++){
			if (listaevaluar.getAtomoEn(i).isnumber()){
				if (!listaevaluar.getAtomoEn(i).esEntero())
					todosSonEnteros = false;
				
				suma += listaevaluar.getAtomoEn(i).getnumber();
			} else
                return new Atom();
		}
		
		if (todosSonEnteros) 
			return new Atom((int) suma);
		else 
			return new Atom(suma);
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom restar(Lista listaevaluar){
		float resta;
		if (listaevaluar.getAtomoEn(1).isnumber())
			resta = listaevaluar.getAtomoEn(1).getnumber();
		else
			return new Atom();

		boolean todosSonEnteros = true;
		
		for (int i=2; i < listaevaluar.size() ; i++){
			if (listaevaluar.getAtomoEn(i).isnumber()){
				if (!listaevaluar.getAtomoEn(i).esEntero())
					todosSonEnteros = false;
				
				resta -= listaevaluar.getAtomoEn(i).getnumber();
			} else
				return new Atom();
		}
		
		if (todosSonEnteros) 
			return new Atom((int) resta);
		else 
			return new Atom(resta);
	}

/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom dividir(Lista listaevaluar) {
		float division;
		if (listaevaluar.getAtomoEn(1).isnumber())
			division = listaevaluar.getAtomoEn(1).getnumber();
		else
			return new Atom();
		
		for (int i=2; i < listaevaluar.size() ; i++){
			if (listaevaluar.getAtomoEn(i).isnumber()){
			
				division /= listaevaluar.getAtomoEn(i).getnumber();
			} else
			    return new Atom();
		}

		return new Atom(division);
	}
	/**
	 * @param listaevaluar 
	 * @return 
	 */
	private Atom multiplicar(Lista listaevaluar){
		float Multiplicacion = 1;
		boolean todosSonEnteros = true;
		
		for (int i=1; i < listaevaluar.size() ; i++){
			if (listaevaluar.getAtomoEn(i).isnumber()){
				if (!listaevaluar.getAtomoEn(i).esEntero())
					todosSonEnteros = false;
			
				Multiplicacion *= listaevaluar.getAtomoEn(i).getnumber();
			} else
                return new Atom();
		}
		
		if (todosSonEnteros)
			return new Atom((int) Multiplicacion);
		else
			return new Atom(Multiplicacion);
	}

    	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public Atom setq(Lista listaevaluar) {
		
		if ((listaevaluar.size() != 3))
            return new Atom();

		if (((Atom)listaevaluar.get(1)).islist())
            return new Atom();

		String variableDeAsignacion = ((Atom) listaevaluar.get(1)).toString(); 
		Atom atomoAsignado = ((Atom) listaevaluar.get(2));

		atomoAsignado = this.evaluar(atomoAsignado);

		this.variablesDelUsuario.put(variableDeAsignacion, atomoAsignado);
		return atomoAsignado;
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public Atom cons(Lista listaevaluar){
		if (listaevaluar.size() != 3)
            return new Atom();
		
		Lista listaConstruida = new Lista();
		listaConstruida.AgregarAlFinal(listaevaluar.getAtomoEn(1));

		if (listaevaluar.getAtomoEn(2).islist()){
			for (int i = 0; i < listaevaluar.getAtomoEn(2).list.size(); i++){
				if (!listaevaluar.getAtomoEn(2).list.getAtomoEn(i).isnull)
					listaConstruida.AgregarAlFinal(listaevaluar.getAtomoEn(2).list.getAtomoEn(i));
			}
		} else
			listaConstruida.AgregarAlFinal(listaevaluar.getAtomoEn(2));
		
		return new Atom(listaConstruida);
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public Atom list_length(Lista listaevaluar) {
		
		if (!this.operandoEsLista(listaevaluar))
            return new Atom();
		
		Atom listaoperanda = this.evaluar(listaevaluar.getAtomoEn(1));
		return new Atom(listaoperanda.list.size());
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public Atom car(Lista listaevaluar){
		if (!this.operandoEsLista(listaevaluar))
            return new Atom();
		
		Lista listaoperanda = this.evaluar(listaevaluar.getAtomoEn(1)).list;
		
		if (listaoperanda.estaVacia())
			return new Atom();
		
		Atom primerAtomo = listaoperanda.getAtomoEn(0);
		
		if (primerAtomo.islistwithoperation())
			primerAtomo = this.evaluar(primerAtomo);
		
		return primerAtomo;
	}
	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public Atom cdr(Lista listaevaluar){
		
		if (!this.operandoEsLista(listaevaluar))
            return new Atom();
		
		Lista listaoperanda = this.evaluar(listaevaluar.getAtomoEn(1)).list;
		
		if (listaoperanda.size()<2)
			return new Atom();
		
		Lista subLista = new Lista(listaoperanda.subList(1, listaoperanda.size()));
		
		return new Atom(subLista);
	}

	/**
	 * @param listaevaluar 
	 * @return 
	 */
	public boolean operandoEsLista(Lista listaevaluar){
		if (!listaevaluar.esOperacion)
			return false;
		
		if (listaevaluar.size()>2)
			return false;

		if (this.variablesDelUsuario.containsKey(listaevaluar.getAtomoEn(1).toString()))
			return true;
			
		if (!listaevaluar.getAtomoEn(1).islist())
			return false;
		
		return listaevaluar.esOperacion;
	}

	/**
	 * @param expresion 
	 * @return 
	 */
	public int obtenerIndiceDelParentesisQueCierraAlPrimeroEn(String expresion){
		expresion = expresion.trim();
		
		if (!((new Atom(expresion).comienzaCon("'("))  ||  (new Atom(expresion).comienzaCon("("))))
			return 0;

		int indice = expresion.indexOf('(')+1;
		int cantidadparentesisabiertosporcerrar = 1;
		
		while (indice < expresion.length()){
			if (expresion.charAt(indice)=='('){
				cantidadparentesisabiertosporcerrar++;
			} else if (expresion.charAt(indice)==')'){
				cantidadparentesisabiertosporcerrar--;
				if (cantidadparentesisabiertosporcerrar==0)
					return indice;
			}
			indice ++;
		}

		return 0;
	}
}