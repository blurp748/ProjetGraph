package attributs;

public class PowerStats extends Attributs {
	private int intelligence, strength, speed, durability, power, combat;
	
	public PowerStats(int intel, int str, int speed, int dura, int pow, int combat) {
		this.intelligence = intel;
		this.strength = str;
		this.speed = speed;
		this.durability = dura;
		this.power = pow;
		this.combat = combat;
	}
	@Override
	public String getAttribut() {
		
		return 
		"Int : "+intelligence
		+"Str : "+strength
		+"Speed : "+speed
		+"Dura : "+durability
		+"Pow : "+power
		+"Combat : "+combat
		;
	}
}
