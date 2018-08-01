import java.util.*;

public class rh{

	public static void main(String[] args){
		/* 
	    	Test reviews
			Should read in a file
		*/
		String reviews[] = {"The price here was reasonable. They had clean bathrooms.",
							"Their bathrooms were really nice. I loved the sandwiches",
							"sandwiches"};
		//All sentences 
		Sentence sentences[] = new Sentence[100];
		// Global index
		int index = 0;

		Scanner in = new Scanner(System.in);
		Map<String,Integer> wordValue = new HashMap<String,Integer>();

		//Dictionary initialization
		Dictionary<String, Integer> d = new Hashtable<String, Integer>();
		d.put("price", 5);
		d.put("service", 5);
		d.put("quality", 5);
		d.put("clean", 5);
		d.put("cleanliness", 5);

		//loop through each word in each review and counts each occurence in HashMap
		for(String review : reviews){
			for(String word : review.split("[\\s@&.?$+-]+")){
				int value = wordValue.containsKey(word) ? wordValue.get(word) : 0;
				wordValue.put(word, value + 1);
			}
		}

		//loop through each review and creates sentences with average points
		for(String review : reviews){
			String sentence = "";
			double points = 0.0;
			int words = 0;
			for(String word : review.split(" ")){
				if(d.get(word) != null){
					points += d.get(word);
				}

				if(word.endsWith(".")){
					word = word.substring(0, word.length() - 1);
					points += wordValue.get(word);
					words++;
					sentence = sentence + " " + word; 
					Sentence newSentence = new Sentence(sentence, points/words);
					sentences[index++] = newSentence;
					sentence = "";
					points = 0.0;
					words = 0;
				} else {
					sentence = sentence + " " + word;
					points += wordValue.get(word); 
					words++;
				}
			}
		}

		/* DEBUGGING */
		System.out.println("HASHMAP KEY AND VALUES");
		System.out.printf("%-15s|%-5s\n\n", "Key", "Value");
		for(String word : wordValue.keySet()){
			System.out.printf("%-15s|%5d\n", word, wordValue.get(word));
		}
		System.out.println();
		for(Sentence sentence : sentences){
			if(sentence == null) continue;
			System.out.printf("Sentence: %s | Average Value: %.2f\n", sentence.sentenceCls, sentence.value);
		}
		/* /DEBUGGING */
	}
}

class Sentence{
	String sentenceCls;
	double value;

	public Sentence(String sentence, double value){
		this.sentenceCls = sentence;
		this.value = value;
	}
}