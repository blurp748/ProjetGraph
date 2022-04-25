package superHeroGraph;

import java.util.HashMap;

public class Node {
	
	public String name;
	public HashMap<Node,String> relations;
	
	public Node(String name)
	{
		this.name = name;
		this.relations = new HashMap<Node, String>();
	}
	
	public Node(int name)
	{
		this.name = Integer.toString(name);
		this.relations = new HashMap<Node, String>();
	}
	
	public void addRelation(Node node, String reason)
	{
		relations.putIfAbsent(node, reason);
		node.getRelations().putIfAbsent(this, reason);
	}
	
	public void removeRelations()
	{
		this.relations.forEach((key,value) -> {
			key.getRelations().remove(this);
		}
		);
	}
	
	public HashMap<Node,String> getRelations()
	{
		return this.relations;
	}
	
	public String getName()
	{
		return this.name;
	}

}
