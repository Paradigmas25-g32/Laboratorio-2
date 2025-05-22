package httpRequest;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import java.io.IOException;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequester {
	
	public String getFeedRss(String urlFeed){
		String feedRssXml = null;
		HttpURLConnection connection = null;
		try {
			// create object connection
			
			URL url = new URL(urlFeed);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			// connection.setRequestProperty("Content-Type", 
			// 	"application/json");
			//connection.setDoOutput(true);
			
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			// check response
			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) { // status == 200
				
				//read response
				BufferedReader in = new BufferedReader(	new InputStreamReader(connection.getInputStream()));

				String inputLine;
				StringBuffer content = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					
					content.append(inputLine);
					content.append('\r');
				}
				in.close();								//stop reading
				feedRssXml = content.toString();
			
			} else { System.out.println("Error" + status);}
		} catch (Exception e){
			e.printStackTrace();
    		return null;
		}
		finally { 
		//close connection
		if (connection != null) {
			connection.disconnect();}
		}
		return feedRssXml;
	}

	public String getFeedReedit(String urlFeed) {
		String feedReeditJson = null;
		return feedReeditJson;
	}

}
