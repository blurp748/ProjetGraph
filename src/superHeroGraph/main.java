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
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class main extends Application {
	
	//FAIRE PRESENTATION POWERPOINT
	
	static final int HAUTEUR = 800;
	static final int LARGEUR = 1200;
	static final int NBHEROES = 10;
	
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

        ListView<Button> listViewReference = new ListView<Button>();
        
        //A ameliorer
        ArrayList<String> relationsName = new ArrayList<String>();
        relationsName.add("all");
        relationsName.add("ako");
        relationsName.add("instance");
        relationsName.add("gender");
        relationsName.add("intelligence");
        relationsName.add("strength");
        relationsName.add("speed");
        relationsName.add("durability");
        relationsName.add("power");
        relationsName.add("combat");
        relationsName.add("publisher");
        relationsName.add("alignment");
        relationsName.add("group");
        
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
        

        
        listViewReference.setOrientation(Orientation.VERTICAL);
        
        root.setLeft(listViewReference);
        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents("all");

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
       // String race = heroAppearance.get("race").toString();
        String publisher = heroBiography.get("publisher").toString();
        String alignment = heroBiography.get("alignment").toString();
        String alterEgos = heroBiography.get("alterEgos").toString();
        String groupAffiliationComplete = heroConnections.get("groupAffiliation").toString();
        String[] groupAffiliation = groupAffiliationComplete.split(";|,|and|ally of|formerly partner of");
        
        Hero newHero = new Hero(
        		heroName,
        		gender,
        		null,
        		Integer.parseInt(intelligence),
        		Integer.parseInt(strength),
        		Integer.parseInt(speed),
        		Integer.parseInt(durability),
        		Integer.parseInt(power),
        		Integer.parseInt(combat),
        		publisher,
        		alignment,
        		alterEgos,
        		groupAffiliation);
        
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
         //Node heroRace = new Node(newHero.getRace());
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
        	 if(tmp.isBlank() == false)
        	 {
        		 heroGroup.add(new Node(tmp));
        	 }
         }
          
         knowGraph.add(entity);
         knowGraph.add(heros);
         knowGraph.add(heroName);
         //knowGraph.add(heroRace);
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
         
         //knowGraph.addRelation(heroName,heroRace, "race");
         knowGraph.addRelation(entity,heros, "ako");
         knowGraph.addRelation(heros,heroName, "instance");
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
		
	}
}
