package superHeroGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class main {

	public static void main(String[] args) {
		
		JSONParser jsonParser = new JSONParser();
		
        try (FileReader reader = new FileReader("superHeroes.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            // Max Heroes : 563
            JSONArray heroesList = (JSONArray) obj;
            for(int i = 0; i < 100 ; i++)
            {
                JSONObject hero = (JSONObject)heroesList.get(i);
                
                System.out.println(hero.get("name"));
                
                JSONObject heroStats = (JSONObject)hero.get("powerstats");
                System.out.println(heroStats.get("intelligence"));
                System.out.println(heroStats.get("strength"));
                System.out.println(heroStats.get("speed"));
                System.out.println(heroStats.get("durability"));
                System.out.println(heroStats.get("power"));
                System.out.println(heroStats.get("combat"));

                JSONObject heroAppearance = (JSONObject)hero.get("appearance");
                System.out.println(heroAppearance.get("gender"));
                System.out.println(heroAppearance.get("race"));
                
                JSONObject heroBiography = (JSONObject)hero.get("biography");
                System.out.println(heroBiography.get("publisher"));
                System.out.println(heroBiography.get("alignment"));
                System.out.println(heroBiography.get("alterEgos"));
                
                JSONObject heroConnections = (JSONObject)hero.get("connections");
                System.out.println(heroConnections.get("groupAffiliation"));
            } 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }	
	}

}
