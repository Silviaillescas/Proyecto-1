import java.util.ArrayList;
import java.util.List;


public class List extends ArrayList implements List{


	public boolean esOperacion;


	public List() {
		super();
	}

	/**
	 * 
	 * @param list 
	 */
	public List(List list){
		super();
		
		if (list == null){
			list = new List();
		}
		
		int i = 0;
		
		while (i < list.size()){
			this.add(list.get(i));
			i++;
		}
	}

	/**
	 * @param atomoIngresado 
	 */
	public List(Atom atomoIngresado) {
		super();
		
		this.add(atomoIngresado);
	}

	/**
	 * @return 
	 */
	public boolean estaVacia(){

		return this.size()==0;
	}

	/**
	 * @param atom 
	 */
	public void AgregarAlFinal(Atom atom){
			this.add(atom);
	}

	/**
	 * 
	 * @param indice 
	 * @param atom 
	 */
	public void AgregarEn(int indice, Atom atom){
		this.add(indice, atom);
	}

	/**
	 * 
	 * @param atom
	 * @return
	 */
	public boolean existe(Atom atom){
		int i = 0;
		
		while (i <= this.size() -1){
			if (this.get(i).equals(atom))
				return true;
			i++;
		}
		
		return false;
	}


	/**
	 * @return 
	 */
	public Atom getOperacion(){
		if (!this.esOperacion)
			return new Atom();
		
		return (Atom)this.get(0);
	}

	/**
	 * @param i 
	 * @return 
	 */
	public Atom getAtomoEn(int i) {
		return (Atom) this.get(i);
	}

	/**
	 *
	 * @param i 
	 * @return 
	 */
	private Atom removeAtomoEn(int i) {
		return (Atom) this.remove(i);
	}


	/**
	 * @param listobject
	 * @return 
	 */
	public boolean equals(Object listobject){
		List otherlist = (List)listobject;
		
		if (this.size()!=otherlist.size())
			return false;
		
		int indice = 0;
		while (indice < this.size()){
			if (!this.get(indice).equals(otherlist.get(indice)))
				return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @param i 
	 * @param replacementatom
	 * @return 
	 */
	public Atom remplazarEn_Por(int i, Atom replacementatom) {
		Atom replacedatom = this.removeAtomoEn(i);
		this.AgregarEn(i, replacementatom);
		
		return replacedatom;
	}
}
