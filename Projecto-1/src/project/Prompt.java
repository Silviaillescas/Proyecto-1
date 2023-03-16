import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Prompt {

	private Scanner entry;

	private String lastexpression;

	private static int linenumber=1;



	public Prompt()
	{
		this.entry = new Scanner(System.in);
	}

	/**
	 * @param uservariables
	 */
	public void Escuchar(Hashtable<String, Atom> uservariables) {

		System.out.print(linenumber+" Escriba aqui: ");
		
		String newexpressionn = "";
		boolean finishedreading = false;
		int numeroDeParentesisAbiertos = 0;
		int numeroDeParentesisCerrados = 0;
		
		do{
			newexpressionn += " "+entry.nextLine().replace('\n', ' ');
			
			if (uservariables.containsKey(newexpressionn.trim())){
				finishedreading = true;
			}
				
			numeroDeParentesisAbiertos = contarCaracterEn('(',newexpressionn);
			numeroDeParentesisCerrados = contarCaracterEn(')',newexpressionn);
			
			if ((numeroDeParentesisAbiertos == numeroDeParentesisCerrados) && (numeroDeParentesisAbiertos !=0))
				finishedreading = true;
			
		} while (!finishedreading);
		
		this.lastexpression = newexpressionn;
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