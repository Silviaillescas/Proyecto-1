import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Prompt {

	private Scanner entrada;

	private String lastexpression;

	private static int linenumber=1;



	public Prompt()
	{
		this.entry = new Scanner(System.in);
	}

	/**
	 * @param uservariables
	 */
	public void Escuchar(Hashtable<String, Atomo> uservariables) {

		System.out.print(numeroDeLinea+" Escriba aqui: ");
		
		String newexpressionn = "";
		boolean finishedreading = false;
		int numeroDeParentesisAbiertos = 0;
		int numeroDeParentesisCerrados = 0;
		
		do{
			newexpression += " "+entry.nextLine().replace('\n', ' ');
			
			if (uservariables.containsKey(newexpression.trim())){
				finishedreading = true;
			}
				
			numeroDeParentesisAbiertos = contarCaracterEn('(',newexpression);
			numeroDeParentesisCerrados = contarCaracterEn(')',newexpression);
			
			if ((numeroDeParentesisAbiertos == numeroDeParentesisCerrados) && (numeroDeParentesisAbiertos !=0))
				finishedreading = true;
			
		} while (!finishedreading);
		
		this.lastexpression = newexpression;
		this.linenumber++;
	}

	/**
	 * @param caracterBuscando 
	 * @param cadena 
	 * @return 
	 */
	private int contarCaracterEn(char caracterBuscando, String cadena) {
		int howoften = 0;

		for (int i = 0 ; i < cadena.length() ; i++){
			if (cadena.charAt(i)==caracterBuscando)
				howoften++;
		}
		
		return howoften;
	}


	/**
	 * @return 
	 */
	public String getlastexpression() {
		return this.lastexpression;
	}
}