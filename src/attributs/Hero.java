package attributs;

public class Hero extends Attributs {
	
	private int idHero;
	
	public Hero(int id) {
		idHero = id;
	}
	
	@Override
	public String getAttribut() {
		return "Hero "+idHero;
	}
}
