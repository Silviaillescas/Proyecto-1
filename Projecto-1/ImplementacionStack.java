public class ImplementacionStack<H> implements Istack<H> {

	public int cont = 0;;
	public Nodo<H> n = null;;

	public ImplementacionStack(){
			
	}

	public boolean em() {
		if (cont==0)
			return true;
		else
			return false;
	}


	public boolean full() {
		if (cont==100)
			return true;
		else{
			return false;
		}
	}

	public void push(Object element){
		Nodo<H> nuevoN = new Nodo<H> (element, null);
		cont++;
		if (n != null){
			Nodo<H> f = n;
			while (f.N() != null){
				f = f.N();
			}
			f.setN(nuevoN);
		}
		else
			n = nuevoN;
				
		}

	public H top() {
		Nodo<H> f = n; 
		
		while (f.N() != null){ 
			f = f.N(); 
		}

		return (H)f.Val();  
	}

	public H pop (){

		Nodo<H> f = n; 
		Nodo<H> prev = null; 
		
		while (f.N() != null){ 
			prev = f; 
			f = f.N(); 
		} 

		if (prev == null) { 
			n = null; 
		} 
		else { 
			prev.setN(null); 
		} 
		cont--; 
		return (H)f.Val();  

	}

	public H retu(int pos) {
		if (pos>=cont)
			return (H) ("Dis"+Integer.toString(pos+1));
		else{
			
			int recorrido=0;
			Nodo<H> f = n; 
			while (recorrido<pos){
				f = f.N(); 
				recorrido++;
			} 
			return (H)f.Val();  	
		}
	}
	
}