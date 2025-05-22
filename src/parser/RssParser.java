package parser;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import feed.Article;
import feed.Feed;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser {

	/*
	 * Implementación del método parser para feeds RSS
	 * 
	 * @param source
	 * @return un objeto de tipo Feed que contiene la información parseada
	 * @throws Exception
	 * */
	@Override
	public Object parser(String source) throws Exception {
		// Inicializo la variable que alamcenara el resultado del parseo
		Feed feed = null;
		try {
			//Configuro el factory para crear el parser XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//Parseo el documento
			Document document = db.parse(source);
			document.getDocumentElement().normalize();
			
			// Obtener el nombre del sitio
			String siteName = getSiteName(document);
			
			feed = new Feed(siteName);
		
			// Obtener todos los items (artículos) del feed
			NodeList itemList = ((Document) db).getElementsByTagName("item");
			
			// Iterar sobre cada item y extraer la información
			for (int i = 0; i < itemList.getLength(); i++) {
				Element item = (Element) itemList.item(i);
				
				// Extraer los atributos del artículo
				String title = getElementValue(item, "title");
				String link = getElementValue(item, "link");
				String description = getElementValue(item, "description");
				Date pubDate = parseDate(getElementValue(item, "pubDate"));
				
				// Crear un nuevo artículo y añadirlo al feed
				Article article = new Article(title, description, pubDate, link);
				feed.addArticle(article);
			}
		} catch (DOMException e) {
			logError("Error al parsear el documento XML: " +e.getMessage(), e);
		} catch (SAXException e) {
			logError("Error al parsear el docuemento XML: " + e.getMessage(), e);
		} catch (IOException e) {
			logError("Error de entrada/salida: " + e.getMessage(), e);
		} catch (Exception e) {
			logError("Error inesperado: " + e.getMessage(), e);
		}
		return feed;
	}

	/**
	 * Obtiene el nombre del sitio del feed RSS
	 * 
	 * @param document El documento XML parseado
	 * @return El nombre del sitio del feed
	 */
	private String getSiteName(Document document) {
		// Obtener la lista de canales
		NodeList channelsList = document.getElementsByTagName("channel");
		if (channelsList.getLength() > 0) {
			// Obtener el primer canal
			Element channel = (Element) channelsList.item(0);
			String title = getElementValue(channel, "title");
			if (title != null && !title.isEmpty()) {
				// Si se encuentra el nombre del sitio, devolverlo
				return title;
			}
		}
		// Si no se encuentra el nombre del sitio, devolver un mensaje por defecto
		return "Sitio Desconocido";
	}


	/**
	 * Extrae el valor de texto de un elemento XML específico
	 * 
	 * @param element El elemento padre que contiene el elemento deseado
	 * @param tagName El nombre del tag del cual queremos extraer el valor
	 * @return El valor de texto del elemento, o una cadena vacía si no existe
	 */
	private String getElementValue(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Element tagElement = (Element) nodeList.item(0);
			if (tagElement.getFirstChild() != null) {
				return tagElement.getFirstChild().getNodeValue();
			}
		}
		return "";
	}
			
	/**
     * Parsea una fecha en formato de string a un objeto Date
     * 
     * @param dateString La fecha en formato string
     * @return Un objeto Date correspondiente a la fecha, o la fecha actual si hay un error
     */
    private Date parseDate(String dateString) {
    	if (dateString == null || dateString.isEmpty()) {
    		return new Date(); // Retorno la fecha actual si no hay fecha
    	}
    	
    	// Formatos de fecha comunes en RSS
    	String[] dateFormats = {
    			 "EEE, dd MMM yyyy HH:mm:ss zzz",  // RFC 822/RFC 1123
    	         "EEE, dd MMM yyyy HH:mm:ss Z",    // Variante de RFC 822
    	         "yyyy-MM-dd'T'HH:mm:ssXXX",       // ISO 8601
    	         "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",   // ISO 8601 con milisegundos
    	         "EEE MMM dd HH:mm:ss zzz yyyy"    // Formato de fecha de Twitter
    	};
    	for (String format : dateFormats) {
    		try {
    			SimpleDateFormat sdf = new SimpleDateFormat(format);
    			return sdf.parse(dateString);
    		} catch (ParseException e) {
				// Ignorar la excepción y probar con el siguiente formato
			}
    	}
        // Si ningún formato funciona, devolver la fecha actual
        logError("No se pudo parsear la fecha: " + dateString, null);
        return new Date(); // Retorno la fecha actual si no hay fecha
    }
}
