import java.util.*;
import java.io.*;

public class rh{

	public static void main(String[] args){

		//Variable initialization
		String reviews[] = new String[128]; //Assumption
		int reviewIndex = 0;
		int maxNum = -1;
		FileReader in = null;
		BufferedReader br = null;

		//List of Sentence class
		List<Sentence> sent = new ArrayList<>();

		//HashMap to keep track of each words occurence
		Map<String,Double> wordValue = new HashMap<String,Double>();

		//Keyword initialization
		Map<String, Integer> d = new HashMap<String, Integer>();
		d.put("price", 5);
		d.put("service", 5);
		d.put("quality", 5);
		d.put("clean", 5);
		d.put("cleanliness", 5);

		//Excluded words initialization
		Set<String> excludes = new HashSet<String>();
		excludes.add("and");
		excludes.add("i");
		excludes.add("is");
		excludes.add("the");
		excludes.add("was");

		//Checks if correct number of arguments
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
		    }
		} else{
			System.out.println("Wrong number of arguments.");
			System.exit(1);
		}



		//Loops through each word in each review, ignoring special characters and white spaces
		//Increment each words value for each occurence
		for(String review : reviews){
			if(review == null) break;
			for(String word : review.split("[\\s@&.,?$+-]+")){
				word = word.toLowerCase();
				double value = wordValue.containsKey(word) ? wordValue.get(word) : 1.0;
				wordValue.put(word, value * 1.5);
			}
		}

		//Loops through each review and breaks them up into sentences
		//Each sentence is awarded an average point which is determined by (total points)/number of words
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
					sent.add(newSentence);
					sentence = "";
					points = 0.0;
					words = 0;
					end = false;
				}
			}
		}

		//Sorts ArrayList with Overrided method in Sentence class
		Collections.sort(sent);

		for(int i = 0; i<(maxNum>sent.size() ? sent.size() : maxNum); i++){
			System.out.println(sent.get(i).sentenceCls);
		}
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

	//Overrides compareTo method to suit our sorting needs for this class
	@Override
	public int compareTo(Object s1){
		return Double.valueOf(((Sentence) s1).getValue()).compareTo(Double.valueOf(this.getValue()));
	}
}