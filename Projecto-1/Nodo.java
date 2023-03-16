ublic class Nodo <H> {


	private Object elem;
	private Nodo<H> Selem;

	public Nodo(Object elemento, Nodo<H> next) {
		elem = elemento; 
		Selem = next; 
	}

	public Nodo<H> N() {
		return Selem; 
	}

	public void setN(Nodo<H> next){
		Selem = next; 
	}

	public Object Val() {
		return elem; 
	} 
	
	
}