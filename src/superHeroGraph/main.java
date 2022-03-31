package superHeroGraph;

import attributs.Attributs;
import attributs.Hero;

public class main {

	public static void main(String[] args) {
		System.out.println("hello");
		Attributs attr = new Hero(1);
		Attributs attr2 = new Hero(2);
		System.out.println(attr.getAttribut());
		System.out.println(attr2.getAttribut());
		
		attr.addAttributToListAttributs(attr2);
		
	}
}
