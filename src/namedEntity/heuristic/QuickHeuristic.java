package namedEntity.heuristic;

import java.util.List;

public class QuickHeuristic extends Heuristic{
	
	private static List<String> keyWords = List.of(
		    "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you",
		    "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she",
		    "her", "hers", "herself", "it", "its", "itself", "they", "them", "your",
		    "their", "theirs", "themselves", "what", "which", "who", "whom",
		    "this", "that", "these", "those", "am", "is", "are", "was", "were",
		    "be", "been", "being", "have", "has", "had", "having", "do", "does",
		    "did", "doing", "a", "an", "the", "and", "but", "if", "or",
		    "because", "as", "until", "while", "of", "at", "by", "for", "with",
		    "about", "against", "between", "into", "through", "during", "before",
		    "after", "above", "below", "to", "from", "up", "down", "in", "out",
		    "off", "over", "under", "again", "further", "then", "once", "here",
		    "there", "when", "where", "why", "how", "all", "any", "both", "each",
		    "few", "more", "most", "other", "some", "such", "no", "nor", "not",
		    "only", "own", "same", "so", "than", "too", "very", "s", "t", "can",
		    "will", "just", "don", "should", "now", "on",
		    // Contractions without '
		    "im", "ive", "id", "Youre", "youd", "youve",
		    "hes", "hed", "shes", "shed", "itd", "were", "wed", "weve",
		    "theyre", "theyd", "theyve",
		    "shouldnt", "couldnt", "musnt", "cant", "wont",
		    // Common uppercase words
		    "hi", "hello"
			);
	
	
	public boolean isEntity(String word) {
		if (word == null || word.length() < 2) return false;

		if (this.getCategory(word) != null) return true;

		return Character.isUpperCase(word.charAt(0)) && !keyWords.contains(word.toLowerCase());
	}
	
	public static void main(String[] args) {
		QuickHeuristic qh = new QuickHeuristic();
	
		String[] testWords = {
			"Milei", "Motorola", "the", "Hello", "IBM", "AIX", "Cloud", "Itil", "a"
		};
	
		for (String word : testWords) {
			System.out.println("Es entidad \"" + word + "\"? --> " + qh.isEntity(word));
		}
	}


}

