package parser;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import subscription.Subscription;
import subscription.SingleSubscription;

/*
 * Esta clase implementa el parser del  archivo de suscripcion (json)
 * Leer https://www.w3docs.com/snippets/java/how-to-parse-json-in-java.html
 * */
public class SubscriptionParser extends GeneralParser {

	@Override
	public Object parser(String source) throws Exception {
		try {
			//Obtener el contenido del json
			String jsonString = new String(Files.readAllBytes(Paths.get(source)));
			//Crear una nueva suscripción
			Subscription subscription = new Subscription(source);
			
			// //Crear un objeto JSON a partir del contenido
			
			// //parsear el contenido
			
			
			JSONArray subscriptionsArray = new JSONArray(jsonString);

			//Recorrer el array de suscripciones
			for (int i = 0; i < subscriptionsArray.length(); i++) {
				JSONObject subObj = subscriptionsArray.getJSONObject(i); 
				
				//Extraer los campos necesarios
				String objUrl = subObj.getString("url");
				String objUrlType = subObj.getString("urlType");
				
				//Crear un nuevo SinlgeSubscription
				SingleSubscription singleSubs = new SingleSubscription(objUrl, null, objUrlType);
				
				//Añadir todos los parámetros
				JSONArray paramsArray = subObj.getJSONArray("urlParams");
				for (int j = 0; j < paramsArray.length(); j++) {
					singleSubs.setUlrParams(paramsArray.getString(j));
				}
				//Añadir la suscripción individual a la lista
				subscription.addSingleSubscription(singleSubs);
			}
			
			return subscription;
		} catch (JSONException e) {
			logError("Error parsing JSON: " + e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logError("Error reading file: " + e.getMessage(), e);
			return null;
		}
	}

}
	
