import httpRequest.httpRequester;
import parser.SubscriptionParser;
import parser.RssParser;
import subscription.*;
import namedEntity.*;
import namedEntity.heuristic.QuickHeuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


import feed.Article;
import feed.Feed;

public class FeedReaderMain {

	private static void printHelp(){
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}
	
	public static void main(String[] args) {
		System.out.println("************* FeedReader version 1.0 *************");
		if (args.length == 0) {

			/*
			Leer el archivo de suscription por defecto;
			Llamar al httpRequester para obtenr el feed del servidor
			Llamar al Parser especifico para extrar los datos necesarios por la aplicacion 
			Llamar al constructor de Feed
			LLamar al prettyPrint del Feed para ver los articulos del feed en forma legible y amigable para el usuario
			*/

			SingleSubscription singleSub = readSubscriptionFile(DEFAULT_SUBSCRIPTION_FILE);
			httpRequester FeedRssXml = new httpRequester();
			String rssParser = FeedRssXml.getFeedRss(singleSub.getFeedToRequest(0));
			//RssParser feedParser = (RssParser) new RssParser().parser(rssParser);
			Feed feed = null;
			try {
				feed = (Feed) new RssParser().parser(rssParser);

				if (feed != null) {
					feed.prettyPrint();
				}
			} catch (Exception e) {
				System.out.println("Error parsing RSS feed: " + e.getMessage());
				e.printStackTrace();
			}
			
		} else if (args.length == 1){
			
			/*
			Leer el archivo de suscription por defecto;
			Llamar al httpRequester para obtenr el feed del servidor
			Llamar al Parser especifico para extrar los datos necesarios por la aplicacion 
			Llamar al constructor de Feed
			Llamar a la heuristica para que compute las entidades nombradas de cada articulos del feed
			LLamar al prettyPrint de la tabla de entidades nombradas del feed.
			 */

			if (args[0].equals("-ne")) {
				SingleSubscription singleSub = readSubscriptionFile(DEFAULT_SUBSCRIPTION_FILE);
				httpRequester FeedRssXml = new httpRequester();
				String rssParser = FeedRssXml.getFeedRss(singleSub.getFeedToRequest(0));
				
				Feed feed = null;
				try {
					feed = (Feed) new RssParser().parser(rssParser);

					if (feed != null) {
						// feed.prettyPrint();
					}
				} catch (Exception e) {
					System.out.println("Error parsing RSS feed: " + e.getMessage());
					e.printStackTrace();
				}

				// Heuristica
			
				Map<String, NamedEntity> entityMap = new HashMap<>();
				Map<String, Integer> entityCategoryMap = new HashMap<>();
				int totalEntities = 0;
				List<String> entityCategories = new ArrayList<>();
				RandomHeuristic qh = new RandomHeuristic();
				for (int i = 0; i < feed.getNumberOfArticles(); i++) {

					Article article = feed.getArticle(i);
					
					article.computeNamedEntities(qh);
					
					for (NamedEntity ne : article.getNamedEntitiesList()){
						String entityCategory = (ne.getCategory() != null) ?
							ne.getCategory() : qh.getCategory(ne.getName());
						if (entityCategory == null) entityCategory = "Other";

						entityMap = entityMapContains(entityMap, ne);
						entityCategoryMap = categoryMapContains(entityCategoryMap, ne);
						
						if (!entityCategories.contains(entityCategory)){
								entityCategories.add(entityCategory);
							}

						totalEntities += ne.getFrequency();
					}
					
				}
				for (NamedEntity namedEntity : entityMap.values()){
					
					System.out.println("Entity: " + namedEntity.getName());
					System.out.println("Category: " + namedEntity.getCategory());
					System.out.println("Frequency: " + namedEntity.getFrequency());
					System.out.println("*".repeat(20));
				}
				for (String nameCategory : entityCategories){
					System.out.println(nameCategory + ": " + entityCategoryMap.get(nameCategory));
				}
				System.out.println("Total Entities: " + totalEntities);
			} else {printHelp();}
			
		}else {
			printHelp();
		}
	}

	private static SingleSubscription readSubscriptionFile(String subscriptionFile) {
		SingleSubscription singleSubs = null;

		try {
			//Leer el archivo de subscription
			Subscription subs = (Subscription) new SubscriptionParser().parser(subscriptionFile);
     		List<SingleSubscription> listSubs = subs.getSubscriptionsList();
			
			singleSubs = listSubs.get(0);

		} catch (Exception e) {
			System.out.println("Error reading subscription file: " + e.getMessage());
			e.printStackTrace();
		}
		
		return singleSubs;
	}

	private static Map<String,NamedEntity> entityMapContains (Map<String, NamedEntity> entityMap, NamedEntity ne){

		QuickHeuristic qh = new QuickHeuristic();
		String entityName = ne.getName();
		String entityCategory = (ne.getCategory() != null) ?
							ne.getCategory() : qh.getCategory(ne.getName());
		if (entityCategory == null) entityCategory = "Other";

		if (entityMap.containsKey(entityName)){
			entityMap.get(entityName).setFrequency(
				entityMap.get(entityName).getFrequency() + ne.getFrequency());
		}else{
			entityMap.put(entityName, new NamedEntity(entityName, entityCategory, ne.getFrequency()));
		}
		return entityMap;
	}

	private static Map<String, Integer> categoryMapContains(Map<String, Integer> entityCategoryMap, NamedEntity ne) {
		QuickHeuristic qh = new QuickHeuristic();
		String entityCategory = (ne.getCategory() != null) ?
							ne.getCategory() : qh.getCategory(ne.getName());
		if (entityCategory == null) entityCategory = "Other";

		if (entityCategoryMap.containsKey(entityCategory)) {
			entityCategoryMap.replace(entityCategory, entityCategoryMap.get(entityCategory) + 1);
		} else {
			entityCategoryMap.put(entityCategory, 1);
		}
		return entityCategoryMap;	
	}

}
