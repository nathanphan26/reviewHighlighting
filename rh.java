import java.util.*;
import java.io.*;

public class rh{

	public static void main(String[] args){

		//Variable initialization
		//Array for reviews
		String reviews[] = new String[128]; //Assumption
		//Indexing for review insertion
		int reviewIndex = 0;
		//Max number of highlights
		int maxNum = -1;
		//File variables
		FileReader in = null; 
		BufferedReader br = null; 

		//List of Sentence class
		List<Sentence> sent = new ArrayList<>();

		//HashMap to keep track of each words occurence
		Map<String,Double> wordValue = new HashMap<String,Double>();

		//Keyword to add weight on sentences that include them
		//Value can be adjusted based off certain trends
		Map<String, Integer> d = new HashMap<String, Integer>();
		d.put("price", 5);
		d.put("service", 5);
		d.put("quality", 5);
		d.put("clean", 5);
		d.put("cleanliness", 5);

		//Excluded words initialization
		Set<String> excludes = new HashSet<String>();
		//exclude articles
		excludes.add("the");
		excludes.add("a");
		excludes.add("an");
		//exclude pronouns
		excludes.add("i");
		excludes.add("me");
		excludes.add("he");
		excludes.add("she");
		excludes.add("his");
		excludes.add("her");
		excludes.add("they");
		excludes.add("them");
		//exclude being verbs
		excludes.add("was");
		excludes.add("is");
		//exclude conjunctions
		excludes.add("and");
		excludes.add("but");
		excludes.add("for");
		excludes.add("yet");
		excludes.add("for");
		excludes.add("or");
		excludes.add("as");


		//Checks if correct number of arguments
		if(args.length == 2){
			try {
				//Sets arguments variables
				in = new FileReader(args[0]); // Initialize File Reader
				br = new BufferedReader(in); // Initialize Read Buffer
				maxNum = Integer.parseInt(args[1]);

				String line;
				try{
					//Adds reviews to reviews array
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
			//Exits on wrong number of arguments
			System.out.println("Wrong number of arguments.");
			System.exit(1);
		}

		//Loops through each word in each review, ignoring special characters and white spaces
		//Increment each words value for each occurence
		for(String review : reviews){
			//If review is null, skip
			if(review == null) break;

			//For loop for each word in the review splitting on white space and special characters
			for(String word : review.split("[\\s@&.,?$+-]+")){

				//If word is in exclusion list, skip
				if(excludes.contains(word)){
					continue;
				}
				//Puts word in lowercase to count occurences of capitalization
				word = word.toLowerCase();
				//Stores word in HashMap wordValue
				double value = wordValue.containsKey(word) ? wordValue.get(word) : 1.0;
				wordValue.put(word, value * 1.5);

			}
		}

		//Loops through each review and breaks them up into sentences
		//Each sentence is awarded an average point which is determined by (total points)/number of words
		for(String review : reviews){

			//If review is null, skip
			if(review == null) break;
			//Logic for end of sentence
			boolean end = false;
			//Variable initialization
			String sentence = "";
			double points = 0.0;
			int words = 0;

			//For loop for words splitting on white space and special characters EXCLUDING "."
			for(String word : review.split("[\\s@,&?$+-]+")){

				//If word is null, skip
				if(word == null) break;

				//Construct sentence
				sentence = sentence + " " + word; 

				//If word ends with ".", remove "." from word and set end of sentence to true
				if(word.endsWith(".")){
					word = word.substring(0, word.length() - 1);
					end = true;
				}

				//Puts word in lowercase to count occurences of capitalization
				word = word.toLowerCase();

				//If word is a keyword, increment points
				if(d.get(word) != null){
					points += d.get(word);
				}

				//If word exists in HashMap, increment points
				if(wordValue.get(word)!=null){
						points += wordValue.get(word);
				} 

				//Increment number of words
				words++;
				
				//If end of sentence, construct new Sentence class and add it to ArrayList
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

		//If max number of reviews is greater than 2, include most frequent word as highlight
		if(maxNum>2){
			double maxWord = Collections.max(wordValue.values());
			String maxWordString = "";
			Iterator it = wordValue.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry)it.next();
				if(pair.getValue().equals(maxWord)) System.out.println(pair.getKey());
			}
			for(int i = 0; i<(maxNum>sent.size() ? sent.size()-1 : maxNum-1); i++){
				System.out.println(sent.get(i).sentenceCls.trim());
			}
		} else{
			for(int i = 0; i<(maxNum>sent.size() ? sent.size() : maxNum); i++){
				System.out.println(sent.get(i).sentenceCls.trim());
			}
		}
	}
}

//Sentence Class
class Sentence implements Comparable{
	String sentenceCls;
	double value;

	//Constructor
	public Sentence(String sentence, double value){
		this.sentenceCls = sentence;
		this.value = value;
	}

	//Methods
	public double getValue(){
		return value;
	}

	//Overrides compareTo method to suit our sorting needs for this class
	@Override
	public int compareTo(Object s1){
		return Double.valueOf(((Sentence) s1).getValue()).compareTo(Double.valueOf(this.getValue()));
	}
}