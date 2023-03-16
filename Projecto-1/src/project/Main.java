public class Main {

	static Interprete interprete;

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		interprete = new Interprete();
		
		Prompt prompt = new Prompt();
		boolean salio = false;
		while (!salio){
			prompt.Escuchar(interprete.variablesDelUsuario);

			if (prompt.getlastexpression().trim().compareTo("(exit)")==0){
				salio = true;
				return;
			}
			
			String ultimaExpresion = prompt.getlastexpression().trim();
					
			try {
			} finally{

				try {
					Atom atomoevaluar = interprete.parsearExpresion(ultimaExpresion, false, false);
					System.out.println(interprete.evaluar(atomoevaluar).toString());
				} finally {

                }
			}
		}
	}

}
