package attributs;

import java.util.ArrayList;

public abstract class Attributs {
	
	private ArrayList<Attributs> listAttributs;

	public ArrayList<Attributs> getListAttributs() {
		return this.listAttributs;
	}

	public void addListToListAttributs(ArrayList<Attributs> listAttributs) {
		this.listAttributs.addAll(listAttributs);
	}
	
	public void addAttributToListAttributs(Attributs attribut) {
		this.listAttributs.add(attribut);
	}
	
	public abstract String getAttribut();
}
