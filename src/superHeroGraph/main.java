package superHeroGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class main extends Application {
	
	//FAIRE PRESENTATION POWERPOINT
	
	static final int HAUTEUR = 800;
	static final int LARGEUR = 1200;
	static final int NBHEROES = 3;
	
	Graph graph = new Graph();
	KnowledgeGraph knowGraph = new KnowledgeGraph();
	
	/********************************************************************************************************/
	/****************************************PROGRAMME PRINCIPAL*********************************************/
	/********************************************************************************************************/
	
	public static void main(String[] args) {
        
        launch(args);
	}
	
    /********************************************************************************************************/
    /**********************************FONCTIONS LIEES AU CANVAS*********************************************/
    /********************************************************************************************************/
	
	@Override
	public void start(Stage primaryStage) {		
		
        BorderPane root = new BorderPane();

        addGraphComponents("all");
        
        ListView<Button> listViewReference = new ListView<Button>();
        
        //A ameliorer
        ArrayList<String> relationsName = new ArrayList<String>();
        relationsName.add("all");
        for(Node node : knowGraph.getGraph())
        {
        	node.getRelations().forEach((key, value) -> {
        		if(!relationsName.contains(value))
        		{
        			relationsName.add(value);
        		}
        	});
        }
        
        for(String relation : relationsName)
        {
            Button button = new Button();
            if(relation == "all")
            {
            	button.setText("See all");
            }else {
            	button.setText("Only " + relation);
            }
            
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                {
                	graph = new Graph();
                	root.setCenter(graph.getScrollPane());
                	addGraphComponents(relation);
                    Layout layout = new RandomLayout(graph);
                    layout.execute();
                }
            };
            button.setOnAction(event);
            listViewReference.getItems().add(button);
        }
        
        //Save JSON
      //------------------------------------------------------------------
        Button imgView = new Button();
        imgView.setText("Sauvegarder en JSON");
        //Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        //Adding action on the menu item
        imgView.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {
        	   JFileChooser chooser = new JFileChooser();
        	   int retrival = chooser.showSaveDialog(null);
              if (retrival == JFileChooser.APPROVE_OPTION) {
					try (Writer writer = new FileWriter(chooser.getSelectedFile()+".json")) {
					    Gson gson = new GsonBuilder().create();
					    gson.toJson(knowGraph.getGraph(), writer);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
              }
           }
        });
        listViewReference.getItems().add(imgView);
       //------------------------------------------------------------------
        
        //Common
        //------------------------------------------------------------------
          Button commonButton = new Button();
          commonButton.setText("Common");
          
          EventHandler<ActionEvent> eventCommon = new EventHandler<ActionEvent>() {
              public void handle(ActionEvent e)
              {
            	System.out.println("ok");
				graph = new Graph();
				root.setCenter(graph.getScrollPane());
				addGraphComponentsCommon(root);
				Layout layout = new RandomLayout(graph);
				layout.execute();
	          }
          };
          commonButton.setOnAction(eventCommon);
          listViewReference.getItems().add(commonButton);
         //------------------------------------------------------------------
          
      //Search
      //------------------------------------------------------------------
        
        TextField textField = new TextField();
        
        Button searchButton = new Button();
        searchButton.setText("Search");
        
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
            	graph = new Graph();
            	root.setCenter(graph.getScrollPane());
            	addGraphComponentsSearch(textField.getText());
                Layout layout = new RandomLayout(graph);
                layout.execute();
            }
        };
        searchButton.setOnAction(event);
        HBox hbox = new HBox();
        TitledPane res = new TitledPane();
        hbox.getChildren().add(res);
        hbox.getChildren().add(textField);
        hbox.getChildren().add(searchButton);
       //------------------------------------------------------------------
        
        //PathFinding
        //------------------------------------------------------------------
          
          TextField textField2 = new TextField();
          TextField textField3 = new TextField();
          
          Button pathButton = new Button();
          
          VBox content = new VBox();
          
          pathButton.setText("Search Path");
          
          EventHandler<ActionEvent> eventPath = new EventHandler<ActionEvent>() {
              public void handle(ActionEvent e)
              {
            	int node1int = knowGraph.exists(textField2.getText());
            	int node2int = knowGraph.exists(textField3.getText());
            	
            	if(node1int != -1 && node2int != -1)
            	{
              		Node node1 = knowGraph.getGraph().get(node1int); 
            		Node node2 = knowGraph.getGraph().get(node2int); 
            		
            		ArrayList<Node> array = new ArrayList<Node>();
            		
            		ArrayList<String> show = pathNodes(node1,node2,node1,array,"");
            		
            		for(String i : show)
            		{
            			content.getChildren().add(new Label(i));
            		}
            		System.out.println(show);
            	}
            	res.setContent(content);
            	res.setExpanded(true);
              }
          };
          pathButton.setOnAction(eventPath);
          hbox.getChildren().add(textField2);
          hbox.getChildren().add(textField3);
          hbox.getChildren().add(pathButton);
         //------------------------------------------------------------------

        
        listViewReference.setOrientation(Orientation.VERTICAL);
        

        
        root.setTop(hbox);
        root.setLeft(listViewReference);
        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        Layout layout = new RandomLayout(graph);
        layout.execute();
		
	}
	
    private void addGraphComponents(String relations) {
		
		makeParse(knowGraph,NBHEROES);

        Model model = graph.getModel();

        graph.beginUpdate();
        
        for(Node node : knowGraph.getGraph())
        {
        	if(relations != "all")
        	{
        		if(node.getRelations().containsValue(relations))
        		{
        			model.addCell(node.getName(), CellType.RECTANGLE); // Crée les nodes
        		}
        	}else {
        		model.addCell(node.getName(), CellType.RECTANGLE); // Crée les nodes
        	}
        }
        
        for(Node node : knowGraph.getGraph())
        {
        	node.getRelations().forEach((key, value) -> {
        		if(relations != "all")
        		{
        			if(value.equals(relations))
        			{
        				model.addEdge(node.getName(), key.getName(), value); //Crée les différentes relations
        			}
        		}else
        		{
        			model.addEdge(node.getName(), key.getName(), value); //Crée les différentes relations
        		}
        		
        	});
        }

        graph.endUpdate();

    }

    private void addGraphComponentsCommon(BorderPane root) {
		
		makeParse(knowGraph,NBHEROES);

        Model model = graph.getModel();

        graph.beginUpdate();
        
        for(Node node : knowGraph.getGraph())
        {
        	model.addCell(node.getName(), CellType.RECTANGLE); // Crée les nodes
        }
        
        for(Node node : knowGraph.getGraph())
        {
        	node.getRelations().forEach((key, value) -> {
        		model.addEdge(node.getName(), key.getName(), value); //Crée les différentes relations
        	});
        }
        
        ArrayList<Node> listNode = new ArrayList<Node>();
        for(Node node : knowGraph.getGraph())
        {
        	if(node.getName().equals("Person"))
        	{
        		node.getRelations().forEach((key,value) -> {
        			listNode.add(key);
        		});
        		listNode.add(node);
        	}
        	if(node.getRelations().containsValue("isa") && !node.getName().equals("superHeroGraph.Hero") && !node.getName().equals("Person") )
        	{
        		System.out.println("Tentative sur : " + node.getName());
            	int deleteOrNot = verifyNode(node,node,node,node.getName(),0);
            	System.out.println("result " + node.getName() +" : " + deleteOrNot);
            	if(deleteOrNot == 0)
            	{
            		listNode.add(node);
            	}
        	}else if (node.getRelations().size() == 1 || node.getName().equals("superHeroGraph.Hero"))
        	{
        		listNode.add(node);
        	}
        }
        
        listNode.forEach(node -> {
        	node.removeRelations();
        	knowGraph.delete(node);
        });

        graph.endUpdate();
        
        graph = new Graph();
        Model model2 = graph.getModel();
        root.setCenter(graph.getScrollPane());
        
        graph.beginUpdate();
        
        for(Node node : knowGraph.getGraph())
        {
        	System.out.println(node.getName());
        	model2.addCell(node.getName(), CellType.RECTANGLE); // Crée les nodes
        }
        
        for(Node node : knowGraph.getGraph())
        {
        	node.getRelations().forEach((key, value) -> {
        		model2.addEdge(node.getName(), key.getName(), value); //Crée les différentes relations
        	});
        }

        graph.endUpdate();
        
        

    }
    
    private void addGraphComponentsSearch(String search) {
		
		makeParse(knowGraph,NBHEROES);

        Model model = graph.getModel();

        graph.beginUpdate();
        
        for(Node node : knowGraph.getGraph())
        {
        	if(search.equals(node.getName()))
        	{
        		model.addCell(node.getName(), CellType.RECTANGLE); // Crée les nodes
        		break;
        	}
        }
        
        for(Node node : knowGraph.getGraph())
        {
        	if(search.equals(node.getName()))
        	{
            	node.getRelations().forEach((key, value) -> {
            		model.addCell(key.getName(), CellType.RECTANGLE);
        			model.addEdge(node.getName(), key.getName(), value); //Crée les différentes relations
            	});
            	break;
        	}
        }
        
        graph.endUpdate();

    }
    
    private int verifyNode(Node node, Node tmpNode, Node previousNode, String path, int res)
    {
    	System.out.println(tmpNode.getName());
    	if(tmpNode.getRelations().containsValue("isa") && !node.getName().equals(tmpNode.getName()) && !tmpNode.getName().equals("superHeroGraph.Hero"))
    	{
    		System.out.println("true : " + tmpNode.getName() + " , path : " + path);
    		res++;
    	}else
    	{
    		ArrayList<Node> listNode = new ArrayList<Node>();
    		tmpNode.getRelations().forEach((key,value) -> {
    			if(!key.equals(previousNode) && !key.getName().equals("superHeroGraph.Hero"))
    			{
    				listNode.add(key);
    			}
    		});
    		for(Node nodes : listNode)
    		{
    			res = verifyNode(node,nodes,tmpNode,path+"->"+nodes.getName(),res);
    		}
    		
    	}
		return res;
    }
    
    private ArrayList<String> pathNodes(Node node1, Node node2, Node actualNode, ArrayList<Node> listNode,String path)
    {
    	if(actualNode.equals(node2))
    	{
    		System.out.println(path);
    		ArrayList<String> res = new ArrayList<String>();
    		res.add(path);
    		return res;
    	}else
    	{
    		ArrayList<Node> tmpList = new ArrayList<Node>();
    		actualNode.getRelations().forEach((key,value) -> {
    			if(!listNode.contains(key))
    			{
    				tmpList.add(key);
    			}
    		});
    		listNode.add(actualNode);
    		ArrayList<String> res = new ArrayList<String>();
    		for(Node nodes : tmpList)
    		{
    			res.addAll(pathNodes(node1,node2,nodes,listNode,path+"->"+nodes.getName()));
    		}
    		return res;
    	}
    }
	
    /********************************************************************************************************/
    /**********************************FONCTIONS LIEES A L'API***********************************************/
    /********************************************************************************************************/
    
	/*
	 * Gère la connexion avec l'API à distance en remplissant le graphe pour tout les héros 
	 * de 1 à 563 définit par nbHeroes
	 * 
	 * Max Heroes : 563
	 */
	public static void makeParse(KnowledgeGraph knowGraph, int nbHeroes)
	{
		// RECUPERATION DES HEROS
		JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("superHeroes.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray heroesList = (JSONArray) obj;
            for(int i = 0; i < nbHeroes ; i++)
            {
            	//VOIR VOCABULAIRE W3C
                JSONObject hero = (JSONObject)heroesList.get(i);
                
                Hero newHero = createHero(hero);
                updateGraph(newHero,knowGraph);
            }  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
	
	/*
	 * Créer un héros depuis l'API
	 */
	public static Hero createHero(JSONObject hero)
	{
        JSONObject heroStats = (JSONObject)hero.get("powerstats");
        JSONObject heroAppearance = (JSONObject)hero.get("appearance");
        JSONObject heroBiography = (JSONObject)hero.get("biography");
        JSONObject heroConnections = (JSONObject)hero.get("connections");
        
        String heroName = hero.get("name").toString();
        String intelligence = heroStats.get("intelligence").toString();
        String strength = heroStats.get("strength").toString();
        String speed = heroStats.get("speed").toString();
        String durability = heroStats.get("durability").toString();
        String power = heroStats.get("power").toString();
        String combat = heroStats.get("combat").toString();
        String gender = heroAppearance.get("gender").toString();
        Object race = heroAppearance.get("race");
        String raceString = null;
        if(race != null)
        {
        	raceString = race.toString();
        }
        String publisher = heroBiography.get("publisher").toString();
        String alignment = heroBiography.get("alignment").toString();
        String alterEgos = heroBiography.get("alterEgos").toString();
        String groupAffiliationComplete = heroConnections.get("groupAffiliation").toString();
        String relativesComplete = heroConnections.get("relatives").toString();
        String[] relatives = relativesComplete.split(",|;");
        String[] groupAffiliation = groupAffiliationComplete.split(";|,|and|ally of|formerly partner of");
        
        Hero newHero = new Hero(
        		heroName,
        		gender,
        		raceString,
        		Integer.parseInt(intelligence),
        		Integer.parseInt(strength),
        		Integer.parseInt(speed),
        		Integer.parseInt(durability),
        		Integer.parseInt(power),
        		Integer.parseInt(combat),
        		publisher,
        		alignment,
        		alterEgos,
        		groupAffiliation,
        		relatives);
        
        return newHero;
	}
	
	/*
	 * Update le graph en ajoutant les différentes relations d'un héros
	 */
	public static void updateGraph(Hero newHero,KnowledgeGraph knowGraph)
	{
		 Node heros = new Node(newHero.getClass().getName());
         Node entity = new Node(newHero.getClass().getSuperclass().getName());
         Node heroName = new Node(newHero.getName());
         Node heroRace = null;
         if(newHero.getRace() != null)
         {
        	 heroRace = new Node(newHero.getRace()); 
         }
         
         Node heroGender = new Node(newHero.getGender());
         Node heroIntelligence = new Node(newHero.getIntelligence());
         Node heroStrength = new Node(newHero.getStrength());
         Node heroSpeed = new Node(newHero.getSpeed());
         Node heroDurability = new Node(newHero.getDurability());
         Node heroPower = new Node(newHero.getPower());
         Node heroCombat = new Node(newHero.getCombat());
         Node heroPublisher = new Node(newHero.getPublisher());
         Node heroAlignement = new Node(newHero.getAlignment());
         Node heroAlterEgos = new Node(newHero.getAlterEgos());
         
         ArrayList<Node> heroGroup = new ArrayList<Node>();
         for(int i = 0; i < newHero.getGroupAffiliation().length;i++)
         {
        	 String tmp = newHero.getGroupAffiliation()[i];
        	 if (tmp.isEmpty() == false && tmp.charAt(0) == ' ')
        	 {
        		 tmp = tmp.replaceFirst(" ", "");
        	 }
        	 if(tmp.isBlank() == false && !tmp.equals("-"))
        	 {
        		 heroGroup.add(new Node(tmp));
        	 }
         }
          
         knowGraph.add(entity);
         knowGraph.add(heros);
         knowGraph.add(heroName);
         if(heroRace != null)
         {
        	 knowGraph.add(heroRace);
         }
         knowGraph.add(heroGender);
         knowGraph.add(heroIntelligence);
         knowGraph.add(heroStrength);
         knowGraph.add(heroSpeed);
         knowGraph.add(heroDurability);
         knowGraph.add(heroPower);
         knowGraph.add(heroCombat);
         knowGraph.add(heroPublisher);
         knowGraph.add(heroAlignement);
         
         if(!(heroAlterEgos.name.equals("No alter egos found.")))
         {
        	 knowGraph.add(heroAlterEgos);
        	 knowGraph.addRelation(heroName,heroAlterEgos, "alterEgos");
         }
         heroGroup.forEach(node -> knowGraph.add(node));
         
         if(heroRace != null)
         {
        	 knowGraph.addRelation(heroName,heroRace, "race");
         }
         knowGraph.addRelation(entity,heros, "ako");
         knowGraph.addRelation(heroName,heros, "isa");
         knowGraph.addRelation(heroName,heroGender, "gender");
         knowGraph.addRelation(heroName,heroIntelligence, "intelligence");
         knowGraph.addRelation(heroName,heroStrength, "strength");
         knowGraph.addRelation(heroName,heroSpeed, "speed");
         knowGraph.addRelation(heroName,heroDurability, "durability");
         knowGraph.addRelation(heroName,heroPower, "power");
         knowGraph.addRelation(heroName,heroCombat, "combat");
         knowGraph.addRelation(heroName,heroPublisher, "publisher");
         knowGraph.addRelation(heroName,heroAlignement, "alignment");
         heroGroup.forEach(node -> knowGraph.addRelation(heroName,node,"group"));
         
         Node personNode = null;
         if(newHero.getRelatives().length > 0)
         {
        	 personNode = new Node("Person");
        	 knowGraph.add(personNode);
			 knowGraph.addRelation(entity,personNode, "ako");
         }
         for(int i = 0; i < newHero.getRelatives().length;i++)
         {
        	 String tmp = newHero.getRelatives()[i];
        	 if (tmp.isEmpty() == false && tmp.charAt(0) == ' ')
        	 {
        		 tmp = tmp.replaceFirst(" ", "");
        	 }
        	 if(tmp.isBlank() == false && !tmp.equals("-") && tmp.endsWith(")"))
        	 {
        		 String[] tmpList = tmp.split("\\(");
        		 if(tmpList.length == 2)
        		 {
            		 tmpList[1] = tmpList[1].replace(")", "");
            		 Node newNode = new Node(tmpList[0]);
            		 if(!knowGraph.getGraph().contains(newNode))
            		 {
            			 knowGraph.add(newNode);
            			 knowGraph.addRelation(heroName,newNode, tmpList[1]);
            			 if(personNode != null)
            			 {
            				 knowGraph.addRelation(newNode,personNode, "isa");
            			 }
            		 } 
        		 }
        	 }
         }
		
	}
}
