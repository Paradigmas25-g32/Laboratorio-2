import httpRequest.httpRequester;
import parser.SubscriptionParser;
import parser.RssParser;
import subscription.*;
import namedEntity.*;
import namedEntity.heuristic.QuickHeuristic;

import java.util.ArrayList;
import java.util.HashMap;
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

}
