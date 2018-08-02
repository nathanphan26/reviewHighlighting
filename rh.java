import java.util.*;
import java.io.*;

public class rh{

	public static void main(String[] args){
		String reviews[] = new String[128]; //Assumption
		int reviewIndex = 0;
		int maxNum = -1;
		FileReader in = null;
		BufferedReader br = null;
		if(args.length == 2){
			try {
				in = new FileReader(args[0]);
				br = new BufferedReader(in);
				maxNum = Integer.parseInt(args[1]);

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
		List<Sentence> sent = new ArrayList<>();
		// Global index
		int index = 0;

		// Scanner in = new Scanner(System.in);
		Map<String,Double> wordValue = new HashMap<String,Double>();

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
				double value = wordValue.containsKey(word) ? wordValue.get(word) : 1.0;
				// System.out.println(word+ " "+value);
				wordValue.put(word, value * 1.5);
			}
		}

		//loop through each review and creates sentences with average points
		for(String review : reviews){
			if(review == null) break;
			boolean end = false;
			String sentence = "";
			double points = 0.0;
			int words = 0;
			for(String word : review.split("[\\s@,&?$+-]+")){
				if(word == null) break;
				sentence = sentence + " " + word; 
				if(word.endsWith(".")){
					word = word.substring(0, word.length() - 1);
					end = true;
				}
				word = word.toLowerCase();
				if(d.get(word) != null){
					points += d.get(word);
				}
				if(excludes.contains(word)){
					words--;
				}
				else if(wordValue.get(word)!=null){
						points += wordValue.get(word);
				} 
				words++;
				if(end == true){
					Sentence newSentence = new Sentence(sentence, points/words);
					//sentences[index++] = newSentence;
					sent.add(newSentence);
					sentence = "";
					points = 0.0;
					words = 0;
					end = false;
				}
			}
		}

		Collections.sort(sent);
		// for(Sentence s : sent){
		// 	System.out.println("s.value: " + s.value);
		// }

		for(int i = 0; i<(maxNum>sent.size() ? sent.size() : maxNum); i++){
			System.out.println(sent.get(i).sentenceCls);
		}

		/* DEBUGGING */
		// System.out.println("HASHMAP KEY AND VALUES");
		// System.out.printf("%-15s|%-5s\n\n", "Key", "Value");
		// for(String word : wordValue.keySet()){
		// 	System.out.printf("%-15s|%5f\n", word, wordValue.get(word));
		// }
		// System.out.println();
		// for(Sentence sentence : sent){
		// 	if(sentence == null) continue;
		// 	System.out.printf("Sentence: %s | Average Value: %.2f\n", sentence.sentenceCls, sentence.value);
		// }
		/* /DEBUGGING */
	}
}

class Sentence implements Comparable{
	String sentenceCls;
	double value;

	public Sentence(String sentence, double value){
		this.sentenceCls = sentence;
		this.value = value;
	}

	public double getValue(){
		return value;
	}

	@Override
	public int compareTo(Object s1){
		return Double.valueOf(((Sentence) s1).getValue()).compareTo(Double.valueOf(this.getValue()));
	}
}