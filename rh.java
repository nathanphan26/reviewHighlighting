import java.util.*;
import java.io.*;

public class rh{

	public static void main(String[] args){
		String reviews[] = new String[128]; //Assumption
		int reviewIndex = 0;
		FileReader in = null;
		BufferedReader br = null;
		if(args.length == 2){
			try {
				in = new FileReader(args[0]);
				br = new BufferedReader(in);

				String line;
				try{
					while ((line = br.readLine()) != null) {
					    reviews[reviewIndex++] = line;
					}
				}
				catch(IOException e) {
				  e.printStackTrace();
				}

			}
			catch (FileNotFoundException ex)  
		    {
		        // insert code to run when exception occurs
		    }
		} else{
			System.out.println("Wrong number of arguments.");
			System.exit(1);
		}


		/* 
	    	Test reviews
			Should read in a file
		*/
	
		//All sentences 
		Sentence sentences[] = new Sentence[100];
		// Global index
		int index = 0;

		// Scanner in = new Scanner(System.in);
		Map<String,Integer> wordValue = new HashMap<String,Integer>();

		//Dictionary initialization
		Map<String, Integer> d = new HashMap<String, Integer>();
		d.put("price", 5);
		d.put("service", 5);
		d.put("quality", 5);
		d.put("clean", 5);
		d.put("cleanliness", 5);

		Set<String> excludes = new HashSet<String>();
		excludes.add("and");
		excludes.add("i");
		excludes.add("is");
		excludes.add("the");
		excludes.add("was");

		//loop through each word in each review and counts each occurence in HashMap
		for(String review : reviews){
			if(review == null) break;
			for(String word : review.split("[\\s@&.,?$+-]+")){
				word = word.toLowerCase();
				int value = wordValue.containsKey(word) ? wordValue.get(word) : 0;
				// System.out.println(word+ " "+value);
				wordValue.put(word, value + 1);
			}
		}

		//loop through each review and creates sentences with average points
		for(String review : reviews){
			if(review == null) break;
			String sentence = "";
			double points = 0.0;
			int words = 0;
			for(String word : review.split("[\\s@,&?$+-]+")){
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
					// System.out.println(word+ " " + wordValue.get(word));
					if(wordValue.get(word)!=null){
						points += wordValue.get(word);
					} 
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