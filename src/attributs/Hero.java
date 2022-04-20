package attributs;

public class Hero extends Entity {
	
	private int intelligence, strength, speed, durability, power, combat;
	private String publisher;
	private String alignment;
	private String alterEgos;
	private String groupAffiliation;
	
	public Hero(
			String name,
			String gender,
			String race,
			int intelligence,
			int strength,
			int speed,
			int durability,
			int power,
			int combat,
			String publisher,
			String alignment,
			String alterEgos,
			String groupAffiliation
			)
	{
		this.setName(name);
		this.setGender(gender);
		this.setRace(race);
		this.setIntelligence(intelligence);
		this.setStrength(strength);
		this.setSpeed(speed);
		this.setDurability(durability);
		this.setPower(power);
		this.setCombat(combat);
		this.setPublisher(publisher);
		this.setAlignment(alignment);
		this.setAlterEgos(alterEgos);
		this.setGroupAffiliation(groupAffiliation);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getCombat() {
		return combat;
	}

	public void setCombat(int combat) {
		this.combat = combat;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getAlterEgos() {
		return alterEgos;
	}

	public void setAlterEgos(String alterEgos) {
		this.alterEgos = alterEgos;
	}

	public String getGroupAffiliation() {
		return groupAffiliation;
	}

	public void setGroupAffiliation(String groupAffiliation) {
		this.groupAffiliation = groupAffiliation;
	}

}
