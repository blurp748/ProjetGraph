package superHeroGraph;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeGraph {

	private List<Node> graph;
	
	public KnowledgeGraph()
	{
		this.graph = new ArrayList<Node>();
	}

	public List<Node> getGraph() {
		return graph;
	}

	public void add(Node node) {
		if(this.exists(node.name) == -1)
		{
			graph.add(node);
		}
		
	}
	
	public int exists(String name)
	{
		int res = -1;
		
		for (int i = 0; i < graph.size(); i++) 
		{
			Node tmp = graph.get(i);
			if (tmp != null )
			{
				if(tmp.name.equals(name))
				{
					res = i;
				}
			}
		}
		return res;
	}
	
	public void addRelation(Node node1, Node node2, String name)
	{
		Node node1bis = this.graph.get(this.exists(node1.name)); 
		Node node2bis = this.graph.get(this.exists(node2.name)); 
		if (node1bis != null && node2bis != null)
		{
			node1bis.addRelation(node2bis, name);
		}
	}
	
	public void showRelations(String name)
	{
		int nodePlace = this.exists(name);
		
		if(nodePlace == -1)
		{
			System.out.println("Cette Node n'existe pas");
		}else
		{
			Node node = this.graph.get(nodePlace);
			System.out.println("*******************************************************");
			System.out.println("*******************************************************");
			System.out.println("**************** Pour " + name + " ****************");
			System.out.println("  ");
			node.relations.forEach((k,v) ->
			System.out.println("Relation : " + v + " avec " + k.name));
			System.out.println("  ");
			System.out.println("*******************************************************");
		}
	}
	
}
